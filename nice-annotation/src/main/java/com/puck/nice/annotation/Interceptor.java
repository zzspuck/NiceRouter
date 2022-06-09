package com.puck.nice.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author: zzs
 * @date: 2022/6/9
 */

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.CLASS)
@interface Interceptor {
    /**
     * 拦截器优先级
     */
    int priority();

    /**
     * 拦截器的名称
     */
    String name() default "";
}
