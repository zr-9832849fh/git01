package com.msb.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.msb.base.BaseQuery;
import com.msb.base.BaseService;
import com.msb.enums.DevResult;
import com.msb.enums.StateStatus;
import com.msb.mappers.SaleChanceMapper;
import com.msb.query.SaleChanceQuery;
import com.msb.util.AssertUtil;
import com.msb.util.PhoneUtil;
import com.msb.vo.SaleChance;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class SaleChanceService extends BaseService<SaleChance,Integer> {

    @Resource
    private SaleChanceMapper saleChanceMapper;


    /**
     *  营销机会管理-多条件查询
     * @param query
     * @return
     */
    public Map<String, Object> querySaleChanceByParams(SaleChanceQuery query) {   // 继承了basequery
        PageHelper.startPage(query.getPage(),query.getLimit());
        // 利用需要查询条件获取所有的对象
        PageInfo<SaleChance> pageInfo = new PageInfo<>(saleChanceMapper.selectByParams(query));
        Map<String,Object> map = new HashMap<>();
        map.put("code",200);
        map.put("msg","success");
        map.put("count",pageInfo.getTotal());
        map.put("data",pageInfo.getList());
        return map;
    }

    /**
     * 营销机会管理-增加
     * @param saleChance
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void saveSaleChance(SaleChance saleChance){
        /**
         * 1.参数校验
         *      customerName  客户名非空
         *      linkMan  非空
         *      linkPhone  非空 11位手机号（工具类）
         * 2. 设置相关参数默认值
         *       createMan创建人   为当前用户名（在控制层完成）
         *       isValid  默认有效(1-有效  0-无效)
         *       createDate  updateDate:默认系统当前时间
         *
         *      // 判断是否指派人
         *       state 默认未分配   如果选择分配人  state 为已分配状态
         *       assignTime 默认空   如果选择分配人  分配时间为系统当前时间
         *       devResult  默认未开发  如果选择分配人 devResult 为开发中 0-未开发  1-开发中 2-开发成功 3-开发失败
         * 3.执行添加 判断添加结果
         */
        // 1.参数校验
        checkParams(saleChance.getCustomerName(),saleChance.getLinkMan(),saleChance.getLinkPhone());

        // 2.设置相关字段的默认值（isValid、createDate  updateDate）
        saleChance.setIsValid(1); // 0=无效 1=有效
        saleChance.setCreateDate(new Date());
        saleChance.setUpdateDate(new Date());

        // 判断是否指派人
        if(StringUtils.isNotBlank(saleChance.getAssignMan())){ // saleChance.getAssignMan()如果为空
            // 如果为空，代表未指派人
            // StateStatus 未开发状态
            saleChance.setState(StateStatus.UNSTATE.getType());
            // devResult为开发状态
            saleChance.setDevResult(DevResult.UNDEV.getStatus());
            // 默认分配时间为null
            saleChance.setAssignTime(null);
        }else {  // 设置了指派人
            // 如果不为空，代表设置了指派人
            // StateStatus为分配状态
            saleChance.setState(StateStatus.STATED.getType());
            // devResult为开发状态
            saleChance.setDevResult(DevResult.DEVING.getStatus());
            // 默认指派时间为当前系统时间
            saleChance.setAssignTime(new Date());
        }

        // 3.执行添加 判断添加结果()
        AssertUtil.isTrue(saleChanceMapper.insertSelective(saleChance) != 1,"机会数据更新失败!");

    }

    /**
     * 营销机会管理-更新
     * @param saleChance
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateSaleChance(SaleChance saleChance){
        /**
         * 1.参数校验
         *     id 记录必须存在
         *     customerName  客户名非空
         *     linkMan  非空
         *     linkPhone  非空 11位手机号
         * 2.设置相关参数值
         *     updateDate  系统当前时间
         *       原始记录 未分配 修改后 已分配(分配人是否存在)
         *          state   0--->1
         *          assignTime   设置分配时间 系统时间
         *          devResult  0--->1
         *       原始记录  已分配  修改后  未分配
         *         state 1-->0
         *         assignTime  null
         *         devResult 1-->0
         *  3.执行更新 判断结果
         */
        // 1.参数校验 - 营销机会ID 非空，数据库中对应的记录存在
        checkParams(saleChance.getCustomerName(),saleChance.getLinkMan(),saleChance.getLinkPhone());
        AssertUtil.isTrue(saleChance.getId() == null,"待更新记录不存在");
        // 通过主键查询对象
        SaleChance temp = saleChanceMapper.selectByPrimaryKey(saleChance.getId());
        // 判断数据库与对应记录是否存在
        AssertUtil.isTrue(null==temp,"待更新记录不存在!");

        // 2.设置默认参数
        if(StringUtils.isBlank(temp.getAssignMan())){  // 不存在
            // 判断修改后的值是否存在
            if (!StringUtils.isBlank(saleChance.getAssignMan())){ // 修改前为空，修改后有值
                // assignTime指派时间  设置为系统当前时间
                saleChance.setAssignTime(new Date());
                // 分配状态  1=已分配
                saleChance.setState(StateStatus.STATED.getType());
                // 开发状态  1=开发中
                saleChance.setDevResult(DevResult.DEVING.getStatus());
            }                                                     // 修改前为空，修改后还是为空
        }else {   // 存在
            // 判断修改后的值是否存在
            if (StringUtils.isBlank(temp.getAssignMan())){   // 修改前有值，修改后无值
                // assignTime指派时间 设置为null
                saleChance.setAssignTime(null);
                // 分配状态  0=未分配
                saleChance.setState(StateStatus.UNSTATE.getType());
                // 开发状态  0=未开发
                saleChance.setDevResult(DevResult.UNDEV.getStatus());
            }else {                                             // 修改前有值，修改后有值
                // 判断修改前后是否是同一个指派人
                if (!saleChance.getAssignMan().equals(temp.getAssignMan())){
                    // 更新分配时间
                    saleChance.setAssignTime(new Date());   // 如果不是同一指派人，那么更改分配时间
                }else {
                    // 设置分配时间为修改前分配的时间
                    saleChance.setAssignTime(temp.getAssignTime());  //   temp是从数据库中拿到的对象是有指派时间的  saleChangce是原来没有指派时间的
                }
            }
        }
        AssertUtil.isTrue(saleChanceMapper.updateByPrimaryKeySelective(saleChance) < 1,"更新营销机会失败!");
    }

    /**
     * 营销机会管理-删除（批量）
     * @param ids
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteSaleChance(Integer[] ids){
        // 判断ids非空
        AssertUtil.isTrue(null==ids || ids.length==0,"待删除记录不存在");
        // 调用mapper层方法，返回受影响行数 与  需要删除行数作比较
        AssertUtil.isTrue(saleChanceMapper.deleteBatch(ids) != ids.length,"营销机会数据删除失败");
    }


    /**
     * 客户开发计划-{开发成功，开发失败}
     * @param id
     * @param devResult
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateSaleChanceDevResult(Integer id,Integer devResult){
        // 1.判断ID和数据库中的数据是否存在
        AssertUtil.isTrue(null == id,"待更新记录不存在");
        SaleChance saleChance = saleChanceMapper.selectByPrimaryKey(id);
        AssertUtil.isTrue(saleChance==null,"更新对象为空");
        // 2.设置开发状态
        saleChance.setDevResult(devResult);
        // 3.执行更新操作， 判断受影响行数
        AssertUtil.isTrue(saleChanceMapper.updateByPrimaryKeySelective(saleChance) != 1,"开发状态更新失败");
    }



    // 增加-参数校验
    private void checkParams(String customerName, String linkMan, String linkPhone) {
        AssertUtil.isTrue(StringUtils.isBlank(customerName),"请输入客户名!");
        AssertUtil.isTrue(StringUtils.isBlank(linkMan),"请输入联系人!");
        AssertUtil.isTrue(StringUtils.isBlank(linkPhone),"请输入手机号!");
        AssertUtil.isTrue(!(PhoneUtil.isMobile(linkPhone)),"手机号格式不合法!");
    }

}
