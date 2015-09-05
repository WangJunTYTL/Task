package com.peaceful.task.container.common;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * queue 任务存放队列
 *
 * @author WangJun <wangjuntytl@163.com>
 * @version 1.0 15/8/16
 * @since 1.6
 */

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface Task {
    String queue() default "commonQueue";
}
