package com.peaceful.task.container.monitor;

import java.io.Serializable;

/**
 * 运行期任务统计分期，按30s统计一次
 *
 * @author WangJun <wangjuntytl@163.com>
 * @version 1.0 15/9/15
 * @since 1.6
 */

public class TaskBean implements Serializable {

    public String id;
    public String desc;
    public long remain;
    public long total;
    public double proudceRate;
    public double consumeRate;
    public long createTime = System.currentTimeMillis();
    public long invalidTime;
    public long lastTotals;
    public long lastRemains;

}
