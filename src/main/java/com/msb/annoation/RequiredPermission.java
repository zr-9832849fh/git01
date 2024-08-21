package com.msb.annoation;


import java.lang.annotation.*;

/**
 * 定义方法需要的对应资源的权限码
 */
@Target({ElementType.METHOD})   // 目标（{元素类型.方法}）
@Retention(RetentionPolicy.RUNTIME)     //  保留（保留政策.运行时间）
@Documented                      // 记录在案
public @interface RequiredPermission {
    // 权限码
    String code() default "";
}
