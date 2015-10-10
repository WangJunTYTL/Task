package com.peaceful.task.container.common;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 任务单元描述
 * <p/>
 * 每一个任务单元在任务存储中心都会分配存储的位置，你可以通过该注解来声明该任务单元存放位置。
 * 你也可以在实际调度中覆盖该声明通过{@link com.peaceful.task.container.schedule.TaskSchedule.Schedule#forceChangeTaskName}
 * 为了方便监控，声明的存储中心位置必须要在taskContainer.conf文件中描述的taskList里面，否则会扔出{@link com.peaceful.task.container.exception.NotFindQueueException}
 * <p/>
 * queue 任务单元存放队列 默认defaultTask
 *
 * @author WangJun <wangjuntytl@163.com>
 * @version 1.0 15/8/16
 * @since 1.6
 */

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface Task {

    // 任务名称 ，系统会更具名称创建任务队列
    String value() default "defaultTask";

    // 任务简单的描述，主要用于监控显示
    String desc() default "";
}
