package com.msb.config;

import com.msb.Interceptor.NoLoginInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

public class MyConfig implements WebMvcConfigurer {
    @Resource
    private NoLoginInterceptor noLoginInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 需要拦截的路径
        registry.addInterceptor(noLoginInterceptor).addPathPatterns("/**").excludePathPatterns("/index","/user/login","/css/**","/images/**","/js/**","/lib/**");
    }
}
