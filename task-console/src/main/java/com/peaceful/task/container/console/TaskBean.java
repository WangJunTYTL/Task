package com.peaceful.task.container.console;

import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;

/**
 * 任务commit和schedule分析结果POJO
 *
 * @author WangJun <wangjuntytl@163.com>
 * @version 1.0 15/9/15
 * @since 1.6
 */

public class TaskBean implements Serializable {

    /**
     * 任务队列编号
     */
    private String id;
    /**
     * 任务desc
     */
    public String desc;
    /**
     * 还没有被调度的个数
     */
    public long remain;
    /**
     * 该任务队列总的提交数
     */
    public long total;
    /**
     * 该任务已经完成的个数
     */
    public long completes;
    /**
     * 该任务实时commit速率
     */
    public long produceRate;
    /**
     * 该任务实时schedule速率
     */
    public long consumeRate;
    /**
     * 该任务的创建时间
     */
    public long createTime = System.currentTimeMillis();
    /**
     * 任务分析会有几个阶段，标记进入每个新阶段的时间
     */
    public long updateTime = System.currentTimeMillis();
    /**
     * 据本次分析时该任务上次提交总数
     */
    public long lastTotals;
    /**
     * 据本次分析时该任务上次完成总数
     */
    public long lastCompletes;
    /**
     * 据本次分析时该任务上次未调度总数
     */
    public long lastRemains;
    /**
     * 历史分析数据快照记录
     */
    @JSONField(deserialize = false, serialize = false)
    public ChartData chartData = new ChartData();
    public String chartDataJson;

    public void setId(String id) {
        this.id = id;
        chartData.id = id;
    }

    public String getId() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == null && obj == null) {
            return true;
        } else if (obj == null) {
            return false;
        } else if (obj instanceof TaskBean) {
            TaskBean taskBean = (TaskBean) obj;
            if (this.id.equals(taskBean.getId())) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return "task id " + id + " " +
                "remain " + remain + " " +
                "total " + total + " " +
                "produceRate " + produceRate + " " +
                "consumeRate " + consumeRate + " " +
                "createTime " + createTime + " " +
                "updateTime " + updateTime + " " +
                "lastTotals " + lastTotals + " " +
                "lastRemains " + lastRemains + " "
                ;
    }
}
