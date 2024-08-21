package com.msb.controller;

import com.msb.Service.PermissionService;
import com.msb.Service.UserService;
import com.msb.base.BaseController;
import com.msb.util.LoginUserUtil;
import com.msb.vo.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**/
@Controller
public class IndexController extends BaseController {
    @Resource
    private UserService userService;
    @Resource
    private PermissionService permissionService;
    /**
     * 系统登录页
     * @return
     */
    @RequestMapping("index")
    public String index(){
        return "index";
    }
    // 系统界面欢迎页
    @RequestMapping("welcome")
    public String welcome(){
        return "welcome";
    }
    /**
     * 跳转到-后端管理主页面
     * @return
     */
    @RequestMapping("main")
    public String main(HttpServletRequest request){         // 首页

        // 查询用户对象[因为登录成功之后会有cookie（存有主键id），用主键id去查整个用户对象  工具类CookieUtil]


        /*主页面显示用户信息*/
        // LoginUserUtil.releaseUserIdFromCookie：获取前台的cookie对象3
        // 获取cookie中的用户id
        Integer userId = LoginUserUtil.releaseUserIdFromCookie(request);   // 获取浏览器中cookie对象并且解密
        // 查询用户对象
        User user = userService.selectByPrimaryKey(userId);
        // 为了前台获取userName显示在页面
        request.getSession().setAttribute("user",user);

        // 用户登录进来之后要查询对应的资源授权码
        List<String> permission = permissionService.queryUserHasRoleHasPermissionByUserId(userId);
        // 将资源授权码返回
        request.getSession().setAttribute("permission",permission);
        return "main";   // 这里跳转不能用request作用域（一次），因为每个界面都需要用到用户名
    }
}
