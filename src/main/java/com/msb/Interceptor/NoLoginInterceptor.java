package com.msb.Interceptor;

import com.msb.Service.UserService;
import com.msb.exceptions.NoLoginException;
import com.msb.util.LoginUserUtil;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class NoLoginInterceptor implements HandlerInterceptor {
    @Resource
    private UserService userService;

    /**
     * 判断用户是否是登录状态
     *      获取Cookie对象，解析用户ID的值，
     *              如果用户ID不为空，且在数据库中存在对应的用户记录，表示请求合法
     *              否则，请求不合法，进行拦截重定向到登录页面
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 获取cookie中的userId
        Integer userId = LoginUserUtil.releaseUserIdFromCookie(request);
        // 判断用户Id不为空，且数据库中存在对应的用户记录
        if (null == userId || userService.selectByPrimaryKey(userId)==null){
            // 抛出异常
            throw new NoLoginException();
        }
        return true;  // 放行
    }
}
