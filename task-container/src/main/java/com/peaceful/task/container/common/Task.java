package com.peaceful.task.container.common;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 配置任务单元
 * queue 任务存放队列 默认commonQueue
 *
 * @author WangJun <wangjuntytl@163.com>
 * @version 1.0 15/8/16
 * @since 1.6
 */

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface Task {

    // 任务名称 ，系统会更具名称创建任务队列
    String value() default "commonQueue";

    // 任务简单的描述，主要用于监控显示
    String desc() default  "";
}
