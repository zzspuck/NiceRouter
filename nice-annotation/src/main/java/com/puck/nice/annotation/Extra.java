package com.puck.nice.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author: zzs
 * @date: 2022/6/9
 */

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.CLASS)
@interface Extra {
}
