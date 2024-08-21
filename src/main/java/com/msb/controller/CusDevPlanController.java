package com.msb.controller;


import com.msb.Service.CusDevPlanService;
import com.msb.Service.SaleChanceService;
import com.msb.base.BaseController;
import com.msb.base.ResultInfo;
import com.msb.enums.StateStatus;
import com.msb.query.CusDevPlanQuery;
import com.msb.query.SaleChanceQuery;
import com.msb.util.LoginUserUtil;
import com.msb.vo.CusDevPlan;
import com.msb.vo.SaleChance;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 客户开发计划
 */
@RequestMapping("cus_dev_plan")
@Controller
public class CusDevPlanController extends BaseController {
    @Resource
    private SaleChanceService saleChanceService;

    @Resource
    private CusDevPlanService cusDevPlanService;

    /**
     * 客户开发计划-多条件查询（分页）【包含了”计划项目数据开发“和”计划项数据维护“；前台做了if判断】
     * @param id
     * @return
     */
    @RequestMapping("list")
    @ResponseBody         // 将查询到的数据显示在页面
    public Map<String,Object> queryCusDevPlanByParams(CusDevPlanQuery cusDevPlanQuery,Integer id){
//        // 创建查询类对象
//        CusDevPlanQuery cusDevPlanQuery = new CusDevPlanQuery();
        cusDevPlanQuery.setSaleChanceId(id);   // 将前台传的id设置到查询类cusDevQuery中，按照这个id取查当前的所有“客户开发计划”
        return cusDevPlanService.queryCusDevPlanByParams(cusDevPlanQuery);
    }

    /**
     * 客户开发计划-“开发里的-增加操作”
     * @param cusDevPlan
     * @return
     */
    @PostMapping("add")
    @ResponseBody
    public ResultInfo addCusDevPlan(CusDevPlan cusDevPlan){
        cusDevPlanService.saveCusDevPlan(cusDevPlan);
        return success("客户开发计划-“详情里的-增加操作成功！”");
    }

    /**
     * 客户开发计划-“开发里的-更新操作”
     * @param cusDevPlan
     * @return
     */
    @PostMapping("update")
    @ResponseBody
    public ResultInfo updateCusDevPlan(CusDevPlan cusDevPlan){
        cusDevPlanService.saveCusDevPlan(cusDevPlan);
        return success("客户开发计划-“详情里的-更新操作成功！”");
    }

    /**
     * 客户开发计划-“开发里的-删除操作”
     * @param id
     * @return
     */
    @RequestMapping("delete")
    @ResponseBody
    public ResultInfo deleteCusDevPlan(Integer id){
        cusDevPlanService.deleteCusDevPlan(id);
        return success("计划添加成功");
    }


    /**
     * 进入添加或编辑计划页项的页面【小页面】
     * @return
     */
    @RequestMapping("addOrUpdateCusDevPlanPage")
    public String addOrUpdateCusDevPlanPage(Integer sId,HttpServletRequest request,Integer id){
        // 在这里进入这个界面时，会将营销机会sID也传进来
        // 将营销机会ID设置到请求域中，给计划项页面获取
        request.setAttribute("sId",sId);

        // 通过计划项id查询记录，返回到请求域中，前台通过el表达式取值
        CusDevPlan cusDevPlan = cusDevPlanService.selectByPrimaryKey(id);
        request.setAttribute("cusDevPlan",cusDevPlan);
        return "cusDevPlan/add_update";
    }






    /**
     * 进入客户开发计划页面
     * @return
     */
    @RequestMapping("index")
    public String index(){
        return "cusDevPlan/cus_dev_plan";
    }

    /**
     * 进入到计划项数据维护界面
     * @param sid
     * @return
     */
    @RequestMapping("toCusDevPlanDataPage")
    public String toCusDevPlanDataPage(Integer sid,HttpServletRequest request){
        // 做查询（根据传过来的sid，根据sid显示当前项目信息），这里应该是要将数据传入到 ”计划向数据维护界面“ 中
        // 1.根据id查营销机会对象
        SaleChance saleChance = saleChanceService.selectByPrimaryKey(sid);
        // 2.将对象设置到请求域中
        request.setAttribute("saleChance",saleChance); // 前台会通过el表达式取
        return "cusDevPlan/cus_dev_plan_data";
    }
}
