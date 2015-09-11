package com.peaceful.task.container.monitor.impl;

import com.peaceful.task.container.common.TaskContainerConf;
import com.peaceful.task.container.repo.TaskQueue;
import com.peaceful.task.container.monitor.MonitorQueue;
import com.google.common.collect.Maps;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author WangJun <wangjuntytl@163.com>
 * @version 1.0 15/5/15
 * @since 1.6
 */
public class MonitorQueueImpl implements MonitorQueue {

    public static int pressure = 0;
    public final static AtomicLong commitCount = new AtomicLong(0);

    TaskContainerConf queueTaskConf = TaskContainerConf.getConf();

    public List<String> getAllQueue() {
        List<String> queues = new ArrayList<String>();
        queues.addAll(queueTaskConf.focusedTasks);
        queues.addAll(queueTaskConf.flexibleTasks);
        return queues;
    }


    private static class getInstancess {
        static MonitorQueue montiorQueue = new MonitorQueueImpl();
    }

    public static MonitorQueue getMonitorImpl() {
        return getInstancess.montiorQueue;
    }


    @Override
    public Map<String, Long> getAllQueueCurrentTaskSize() {
        List<String> queues = getAllQueue();
        Map<String, Long> result = Maps.newHashMap();
        for (String q : queues) {
            if (TaskQueue.isLock(q))
                result.put("<span style='color:red'>" + q + "</span>", TaskQueue.llen(q));
            else
                result.put(q, TaskQueue.llen(q));
        }

        return result;
    }

    @Override
    public long getSumOfQueueSize() {
        List<String> queues = getAllQueue();
        Long sum = new Long(0);
        for (String q : queues) {
            sum += TaskQueue.llen(q);
        }
        return sum;
    }


    @Override
    public Map<String, String> getBasicInfo() {
        Map<String, String> result = Maps.newHashMap();
        result.put("commitTaskCount", String.valueOf(commitCount.get()));
        result.put("router", String.valueOf(queueTaskConf.router));
        result.put("worker", String.valueOf(queueTaskConf.worker));
        result.put("maxParallel", String.valueOf(queueTaskConf.router * queueTaskConf.worker));
        result.put("currentPressure", String.valueOf(pressure + "/" + queueTaskConf.worker));
        return result;
    }

    @Override
    public long getCommitTaskCount() {
        return commitCount.get();
    }

}
