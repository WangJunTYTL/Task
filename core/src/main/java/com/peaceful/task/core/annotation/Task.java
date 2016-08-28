package com.peaceful.task.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 任务单元描述:用于描述任务存放的队列和任务的执行器
 *
 * @author WangJun <wangjuntytl@163.com>
 * @version 1.0 15/8/16
 * @since 1.6
 */

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface Task {

    /**
     * 所存放队列
     *
     * @return
     */
    String value() default "default";

    /**
     * 执行者
     *
     * @return
     */
    String executor() default "default";

}
