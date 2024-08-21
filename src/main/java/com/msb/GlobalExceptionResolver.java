package com.msb;

import com.alibaba.fastjson.JSON;
import com.msb.base.ResultInfo;
import com.msb.exceptions.AuthException;
import com.msb.exceptions.NoLoginException;
import com.msb.exceptions.ParamsException;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 * 方法返回值类型
 *     视图
 *     JSON
 *  如果判断方法的返回值类型
 *         如果方法级别配置了，@RequstBody 注解，表示方法返回的是JSON
 *         反之就是视图页面
 *
 */
@Component
public class GlobalExceptionResolver implements HandlerExceptionResolver {

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        // 设置未登录异常处理
        if (ex instanceof NoLoginException){
            ModelAndView modelAndView = new ModelAndView("redirect/index");
            return modelAndView;
        }


        // 设置默认异常处理
        ModelAndView mv = new ModelAndView();
        mv.setViewName("");
        mv.addObject("code", 500);
        mv.addObject("msg", "系统异常，请稍后再试...");

        // 判断HandlerMethod   视图异常
        if (handler instanceof HandlerMethod) {
            // 类型转换
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            // 获取方法上的 ResponseBody 注解
            ResponseBody responseBody = handlerMethod.getMethod().getDeclaredAnnotation(ResponseBody.class);

            // 判断 ResponseBody 注解是否存在（如果不存在，表示返回的是视图；如果存在，表示返回的是JSON）
            if (null == responseBody) {
                // 方法返回视图
                if (ex instanceof ParamsException) {
                    ParamsException pe = (ParamsException) ex;
                    mv.addObject("code", pe.getCode());
                    mv.addObject("msg", pe.getMsg());
                }else if (ex instanceof AuthException){  // 认证异常
                    AuthException e = (AuthException) ex;
                    mv.addObject("code", e.getCode());
                    mv.addObject("msg", e.getMsg());
                }
                return mv;
            } else {      // json异常
                // 方法上返回JSON
                ResultInfo resultInfo = new ResultInfo();
                resultInfo.setCode(300);
                resultInfo.setMsg("系统异常，请重试！");

                // 如果捕获的是自定义异常
                if (ex instanceof ParamsException) {
                    ParamsException pe = (ParamsException) ex;
                    resultInfo.setCode(pe.getCode());
                    resultInfo.setMsg(pe.getMsg());
                }else if (ex instanceof AuthException){   // 认证异常
                    AuthException a = (AuthException) ex;
                    resultInfo.setCode(a.getCode());
                    resultInfo.setMsg(a.getMsg());
                }

                // 设置响应类型和编码格式
                response.setContentType("application/json;charset=utf-8");
                // 得出输出流
                PrintWriter out = null;
                try {
                    out = response.getWriter();
                    // 将对象转换为JSON，输出
                    out.write(JSON.toJSONString(resultInfo));
                    out.flush();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (out != null) {
                        out.close();
                    }
                }
                return null;
            }
        }
        return mv;
    }
}
