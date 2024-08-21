package com.msb.controller;

import com.msb.Service.SaleChanceService;
import com.msb.annoation.RequiredPermission;
import com.msb.base.BaseController;
import com.msb.base.BaseService;
import com.msb.base.ResultInfo;
import com.msb.enums.StateStatus;
import com.msb.mappers.SaleChanceMapper;
import com.msb.query.SaleChanceQuery;
import com.msb.util.CookieUtil;
import com.msb.util.LoginUserUtil;
import com.msb.vo.SaleChance;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;


// 营销机会管理

@Controller
@RequestMapping("sale_chance")
public class SaleChanceController  extends BaseController {

   @Resource
   private SaleChanceService saleChanceService;

   /**
    * 1营销机会-多条件查询（分页）  101001
    * @param saleChanceQuery
    * @return
    */
   @RequiredPermission(code = "101001")
   @RequestMapping("list")
   @ResponseBody         // 将查询到的数据显示在页面               saleChanceQuery中已经存在了state的状态为1(前台默认传的)
   public Map<String,Object> querySaleChanceByParams(SaleChanceQuery saleChanceQuery,Integer flag,HttpServletRequest request){
      // 判断是营销计划还是客户开发（利用前台传的参数）
      if (flag != null && flag == 1){   // 假定前台传过来一个flag
         // 客户开发
         Integer userId = LoginUserUtil.releaseUserIdFromCookie(request);
         saleChanceQuery.setAssignMan(userId);
         // 在这个地方,前台在cus.dev.plan.js中,默认设置了state和flag均等于1,那么就将查询的有状态的和flag的数据放入在客户开发计划中
      }

      return saleChanceService.querySaleChanceByParams(saleChanceQuery);
   }

   /**
    * 2营销机会-增加          101002
    * @param request
    * @param saleChance
    * @return
    */
   @RequiredPermission(code = "101002")
   @RequestMapping("add")
   @ResponseBody
   public ResultInfo saveSaleChance(HttpServletRequest request, SaleChance saleChance){
      // 1.首先要拿到用户名为当前创建人(从cookie中获取，工具类getcookieValue)
      String userName = CookieUtil.getCookieValue(request, "userName");
      // 2.设置到营销机会对象中
      saleChance.setCreateMan(userName);
      // 3.调用service层添加方法
      saleChanceService.saveSaleChance(saleChance);
      return success("机会数据添加成功");      // 这个是调用了BaseController中的success封装方法，返回ResultINfo对象
   }

   /**
    * 3营销机会-更新          101004
    * @param saleChance
    * @return
    */
   @RequiredPermission(code = "101004")
   @RequestMapping("update")
   @ResponseBody
   public ResultInfo updateSaleChance(SaleChance saleChance){

      // 调用service层添加方法
      saleChanceService.updateSaleChance(saleChance);
      return success("营销机会更新成功");      // 这个是调用了BaseController中的success封装方法，返回ResultINfo对象
   }


   /**
    * 4营销机会-删除（批量）     101003
    * @param ids
    * @return
    */
   @RequiredPermission(code = "101003")
   @RequestMapping("delete")
   @ResponseBody
   public ResultInfo deleteSaleChance(Integer[] ids){
      saleChanceService.deleteSaleChance(ids);
      return success("营销机会数据删除成功");
   }


   /**
    * 客户计划功能-更新营销机会的开发状态
    */

   public ResultInfo updateSaleChanceDevResult(Integer id,Integer devResult){

      saleChanceService.updateSaleChanceDevResult(id,devResult);
      return success("开发状态更新成功");
   }







   /**
    * 进入营销机会的页面（分页面）
    *
    * @return
    */
   @RequestMapping("index")
   public String index(){

      return "saleChance/sale_chance";
   }

   /**
    * 进入营销机会页面下的数据页面（添加和修改）
    * @param id
    * @return
    */
   @RequestMapping("addOrUpdateSaleChancePage")
   public String addOrUpdateSaleChancePage(Integer id,HttpServletRequest req){
      if (id != null){   // 更新
         SaleChance saleChance = saleChanceService.selectByPrimaryKey(id);
         req.setAttribute("saleChance",saleChance);
      }

      return "saleChance/add_update";  // add_update 是打开添加按钮的那个小视图
   }
}
