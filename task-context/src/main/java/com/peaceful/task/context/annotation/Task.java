package com.peaceful.task.context.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 任务单元描述
 * <p>
 * 每次方法的调用都会作为一个task单元并被push到queue服务，然后等待调度和执行。该注解可以用来描述task将会被送入到什么队列，最终会被哪一个执行器执行
 *
 * @author WangJun <wangjuntytl@163.com>
 * @version 1.0 15/8/16
 * @since 1.6
 */

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface Task {

    /**
     * task 所存放队列
     *
     * @return
     */
    String value() default "default";

    /**
     * task 实际执行的executor
     *
     * @return
     */
    String executor() default "default";

}
