package com.msb.controller;

import com.msb.Service.UserService;
import com.msb.base.BaseController;
import com.msb.base.BaseQuery;
import com.msb.base.ResultInfo;
import com.msb.exceptions.ParamsException;
import com.msb.model.UserModel;
import com.msb.query.UserQuery;
import com.msb.util.LoginUserUtil;
import com.msb.vo.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("user")
public class UserController extends BaseController {
    @Resource
    private UserService userService;


    /**
     * 1登录模块-登录
     * @param userName
     * @param userPwd
     * @return
     */
    @RequestMapping("login")
    @ResponseBody
    public ResultInfo userLogin(String userName,String userPwd) {
        ResultInfo resultInfo = new ResultInfo();

        // 调用service层的登录方法
        //  (因为要将id、username、trueName传给前端做cookie，所以要在service中将登录成功的账户密码传过来)
        UserModel userModel = userService.userLogin(userName, userPwd);

        // 将userModel传入到resultInfo对象中
        resultInfo.setResult(userModel);

        return resultInfo;
    }

    /**
     * 2登录模块-修改密码
     * @param request
     * @param oldPassword
     * @param newPassword
     * @param confirmPassword
     * @return
     */
    @PostMapping("updatePassword")
    @ResponseBody
    public ResultInfo updateUserPassword (HttpServletRequest request, String oldPassword, String newPassword, String confirmPassword) {
        ResultInfo resultInfo = new ResultInfo();  // 默认code=200  msg=success
        // 获取userId
        Integer userId = LoginUserUtil.releaseUserIdFromCookie(request);
        userService.updataPassword(userId,oldPassword,newPassword,confirmPassword);   // 如果更新失败，直接抛出异常
        return resultInfo;
    }

    // 用于修改密码跳转，前台跳在后台 ； 后台再跳回去
    @RequestMapping("toPasswordPage")
    public String toPasswordPage(){
        return "user/password";
    }


    /**
     * 查询所有的销售人员
     * @return
     */
    @RequestMapping("queryAllSales")
    @ResponseBody
    public List<Map<String,Object>> queryAllSales(){
        return userService.queryAllSales();
    }




/*----------------------------------------------------------------------------------------------------------*/




    /**
     * 用户管理-多条件查询【service层查询已经写好了，所以不用写了】
     * @param
     * @return
     */
    @RequestMapping("list")
    @ResponseBody
    public Map<String,Object> selectByParams(UserQuery userQuery){
        // 多条件查询所有满足条件的数据（条件：用户名、邮箱、手机号）  queryByParamsForTable:分页查询后台写好了的
        return userService.queryByParamsForTable(userQuery);
    }

    /**
     * 用户管理-增加
     * @param user
     * @return
     */
    @RequestMapping("save")
    @ResponseBody
    public ResultInfo add_UserManage(User user){
        userService.add_UserManage(user);
        return success("系统设置-用户管理-添加成功");
    }

    /**
     * 用户管理-修改
     * @param user
     * @return
     */
    @RequestMapping("update")
    @ResponseBody
    public ResultInfo update_UserMange(User user){
        userService.update_UserMange(user);
        return success("系统设置-用户管理-更新成功");
    }

    /**
     * 用户管理-删除
     * @param ids
     * @return
     */
    @RequestMapping("delete")
    @ResponseBody
    public ResultInfo delete_UserMange(Integer[] ids){
        userService.delete_UserMange(ids);
        return success("系统设置-用户管理-删除成功");
    }



    /**
     * 进入系统设置---用户管理界面
     * @return
     */
    @RequestMapping("index")
    public String userMange(){
        return "user/user";
    }

//    /**
//     * 进入系统设置---角色管理界面
//     * @return
//     */
//    @RequestMapping("index")
//    public String roleMange(){
//        return "user/user";
//    }

    /**
     * 跳小窗口
     * @return
     */
    @RequestMapping("addOrUpdateUserPage")
    public String addOrUpdateUserPage(Integer id,Model model){
        if(null != id){
            model.addAttribute("users",userService.selectByPrimaryKey(id));  // 根据ID返回对象
        }
        return "user/add_update";
    }




}
