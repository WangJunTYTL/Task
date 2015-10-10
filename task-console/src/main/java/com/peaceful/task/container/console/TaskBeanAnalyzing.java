package com.peaceful.task.container.console;

import com.alibaba.fastjson.JSONObject;
import com.peaceful.task.container.store.TaskStore;
import com.peaceful.task.container.store.help.Helper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 分析每个任务单元队列中，任务单元的commit和schedule情况，并把分析后的结果保存到一个循环队列中
 * <p/>
 * 为了分析每个任务单元队列的情况，需要在任务存储中心收到commit和schedule时记录这两种情况{@link Helper#remotePushCount} {@link Helper#remotePopCount}
 * 这样在定时分析时就可以依据这两个指标分析出任务单元每秒commit的TPS和schedule的TPS
 * <p/>
 * 分析后结果也会被保存到{@link Helper#remoteFocusedRepo}{@link Helper#remoteFirstFlexibleRepo}{@link Helper#remoteSecondFlexibleRepo}
 * 这些结果的格式很容易被一些，第三方监控平台支持，任务容器也提供了自己的一套监控在task-admin module中
 *
 * @author WangJun <wangjuntytl@163.com>
 * @version 1.0 15/9/27
 * @see CycleQueue
 * @see TaskBean
 * @since 1.6
 */

public class TaskBeanAnalyzing {

    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
    private static Logger logger = LoggerFactory.getLogger(TaskBeanAnalyzing.class);


    /**
     * 依据{@link TaskBean}对象分析
     *
     * @param taskBean
     */
    public static void analyzing(TaskBean taskBean,long last_exe_timestamp) {
        // task unit remain count
        taskBean.remain = TaskStore.get().size(taskBean.getId());
        // task unit total schedule count
        long pop = Helper.remotePopCount.get(taskBean.getId());
        // task unit total commit count
        long push = Helper.remotePushCount.get(taskBean.getId());
        taskBean.lastTotals = taskBean.total;
        taskBean.lastCompletes = taskBean.completes;
        taskBean.total = push;
        taskBean.completes = pop;
        push = (taskBean.total - taskBean.lastTotals);
        pop = (taskBean.completes - taskBean.lastCompletes);

        // Calculate rate of task commit & schedule
        long currentTimeMillis = System.currentTimeMillis();
        long tick = (currentTimeMillis - last_exe_timestamp) / 1000;
        if (tick == 0) return;
        taskBean.consumeRate = Long.valueOf(pop / tick);
        taskBean.produceRate = Long.valueOf(push / tick);
        logger.debug("{}\t{}\t{}", taskBean.getId(), taskBean.consumeRate, taskBean.produceRate);

        // To generate the chart data
        taskBean.chartData.pop.push(taskBean.consumeRate);
        taskBean.chartData.push.push(taskBean.produceRate);
        taskBean.chartData.label.push(simpleDateFormat.format(new Date()));
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", taskBean.getId());
        jsonObject.put("push", taskBean.chartData.push.get());
        jsonObject.put("pop", taskBean.chartData.pop.get());
        jsonObject.put("label", taskBean.chartData.label.get());
        taskBean.chartDataJson = jsonObject.toJSONString();
    }

}
