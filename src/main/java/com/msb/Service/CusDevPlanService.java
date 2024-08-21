package com.msb.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.msb.base.BaseService;
import com.msb.mappers.CusDevPlanMapper;
import com.msb.mappers.SaleChanceMapper;
import com.msb.query.CusDevPlanQuery;
import com.msb.query.SaleChanceQuery;
import com.msb.util.AssertUtil;
import com.msb.vo.CusDevPlan;
import com.msb.vo.SaleChance;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class CusDevPlanService extends BaseService<CusDevPlan,Integer> {

    @Resource
    private CusDevPlanMapper cusDevPlanMapper;
    @Resource
    private SaleChanceMapper saleChanceMapper;

    /**
     * 客户开发计划-多条件查询（分页）
     * @param cusDevPlanQuery
     * @return
     */
    public Map<String, Object> queryCusDevPlanByParams(CusDevPlanQuery cusDevPlanQuery) {   // 继承了CusDevPlanQuery
        Map<String, Object> map = new HashMap<>();
        // 开启分页
        PageHelper.startPage(cusDevPlanQuery.getPage(), cusDevPlanQuery.getLimit());   // 分别代表当前页和页数
        // 得到对应分页对象
        PageInfo<CusDevPlan> pageInfo = new PageInfo<>(cusDevPlanMapper.selectByParams(cusDevPlanQuery));
        // 设置map对象
        map.put("code",0);
        map.put("msg", "success");
        map.put("count", pageInfo.getTotal());  // pageInfo.getTotal 代表查询的总数
        // 设置分页好的列表
        map.put("data", pageInfo.getList());   //  获取分页的数据
        return map;
    }

    /**
     * 客户开发计划-“开发里的-增加操作”
     * @param cusDevPlan
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void saveCusDevPlan(CusDevPlan cusDevPlan){
        /**
         * 1.参数校验
         *    营销机会id 非空 记录必须存在
         *    计划项内容非空
         *    计划项时间非空
         * 2. 参数默认值
         *    is_valid  1
         *    createDate 系统时间
         *    updateDate  系统时间
         * 3.执行添加 判断结果
         */
        // 1.参数校验
        checkParams(cusDevPlan);
        // 2.设置默认值
        cusDevPlan.setIsValid(1);
        cusDevPlan.setCreateDate(new Date());
        cusDevPlan.setUpdateDate(new Date());
        // 3.执行添加
        AssertUtil.isTrue(cusDevPlanMapper.insertSelective(cusDevPlan)<1,"计划项记录添加失败!");
    }


    /**
     * 客户开发计划项数据 - “开发”中的更新（编辑）
     * @param cusDevPlan
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateCusDevPlan(CusDevPlan cusDevPlan){
        // 1.参数校验（就比查询多一个Id非空校验）
        AssertUtil.isTrue(cusDevPlan.getId() == null || cusDevPlanMapper.selectByPrimaryKey(cusDevPlan.getId()) == null,"数据异常，请重试");
        checkParams(cusDevPlan);
        // 2.设置参数默认值（更新时间）
        cusDevPlan.setUpdateDate(new Date());
        // 3.执行更新程序，判断受影响行数
        AssertUtil.isTrue(cusDevPlanMapper.updateByPrimaryKeySelective(cusDevPlan) != 1,"计划项更新失败");
    }

    /**
     * 客户开发计划-删除
     * @param id
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteCusDevPlan(Integer id){
        // 1.通过ID查询计划项对象
        CusDevPlan cusDevPlan = cusDevPlanMapper.selectByPrimaryKey(id);
        AssertUtil.isTrue(null == id && cusDevPlan == null,"待更新记录不存在");
        // 2.设置记录无效（删除）
        cusDevPlan.setIsValid(0);
        cusDevPlan.setUpdateDate(new Date());

        // 执行更新操作
        AssertUtil.isTrue(cusDevPlanMapper.updateByPrimaryKeySelective(cusDevPlan) !=1,"计划项数据删除");
    }








    // 参数校验
    private void checkParams(CusDevPlan cusDevPlan) {
        // 营销机会ID  非空，数据存在
        Integer sId = cusDevPlan.getSaleChanceId();
        AssertUtil.isTrue(null==sId||null==saleChanceMapper.selectByPrimaryKey(sId),"数据异常，请设置营销机会id");
        // 计划项内容  非空
        AssertUtil.isTrue(StringUtils.isBlank(cusDevPlan.getPlanItem()),"请输入计划项内容!");
        // 计划时间  非空
        AssertUtil.isTrue(null==cusDevPlan.getPlanDate(),"请指定计划项日期!");
    }
}
