package com.peaceful.task.manage.common;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author wangjun
 * @version 1.0
 * @since 15/5/4.
 */

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER})
public @interface Param {
    String name();

    String value() default "";
}
