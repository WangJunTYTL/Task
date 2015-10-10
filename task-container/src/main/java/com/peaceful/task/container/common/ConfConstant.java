package com.peaceful.task.container.common;

/**
 * 任务容器基本常量配置
 *
 * @author WangJun <wangjuntytl@163.com>
 * @version 1.0 15/8/16
 * @since 1.6
 */

public class ConfConstant {

    /**
     * task container log的root目录
     */
    public final static String DEFAULT_LOG_NAMESPACE = "com.peaceful.task.container";

    /**
     * focused task 调度频率 单位 s
     */
    public final static int FOCUSED_SCHEDULE_TICK = 2;

    /**
     * first flexible task 调度频率 单位 s
     */
    public final static int FIRST_FLEXIBLE_SCHEDULE_TICK = 8;

    /**
     * flexible task first  to second 检测频率 单位 min
     */
    public final static int FLEXIBLE_FIRST_SECOND_CHECK_TICK = 1;

    /**
     * console 频率 单位 s
     */
    public final static int MONITOR_TICK = 6;

    /**
     * flexible first task 存活时间 单位 ms
     */
    public final static int FIRST_TASK_LIVE_TIME = 168 * 1000;

    /**
     * flexible first task 存活时间 单位 ms
     */
    public final static int SECOND_TASK_LIVE_TIME = 10 * 60 * 1000;

    /**
     * focused task 积压数大于或等于该值时发出报警
     */
    public final static int FOCUSED_TASK_ALERT_POINT = 6666;

    /**
     * flexible task 积压数大于或等于该值时发出报警
     */
    public final static int FLEXIBLE_TASK_ALERT_POINT = 666666;


}
