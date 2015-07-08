package com.peaceful.task.manage.service.impl;

import com.peaceful.task.manage.common.TaskManageConf;
import com.peaceful.task.manage.repo.Queue;
import com.peaceful.task.manage.service.MontiorQueue;
import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author WangJun <wangjuntytl@163.com>
 * @version 1.0 15/5/15
 * @since 1.6
 */
public class MonitorQueueImpl implements MontiorQueue {

    public static int pressure = 0;
    public final static AtomicLong commitCount = new AtomicLong(0);

    TaskManageConf queueTaskConf = TaskManageConf.getConf();

    public List<String> getAllQueue() {
        return queueTaskConf.queues;
    }

    @Override
    public Map<String, Long> getAllQueueCurrentTaskSize() {
        List<String> queues = getAllQueue();
        Map<String, Long> result = Maps.newHashMap();
        for (String q : queues) {
            if (Queue.isLock(q))
                result.put("<span style='color:red'>" + q + "</span>", Queue.llen(q));
            else
                result.put(q, Queue.llen(q));
        }
        return result;
    }

    @Override
    public long getSumOfQueueSize() {
        List<String> queues = getAllQueue();
        Long sum = new Long(0);
        for (String q : queues) {
            sum += Queue.llen(q);
        }
        return sum;
    }


    @Override
    public Map<String, String> getBasicInfo() {
        Map<String, String> result = Maps.newHashMap();
        result.put("commitTaskCount", String.valueOf(commitCount.get()));
        result.put("router", String.valueOf(queueTaskConf.dispatchParallel));
        result.put("worker", String.valueOf(queueTaskConf.execParallel));
        result.put("maxParallel", String.valueOf(queueTaskConf.dispatchParallel * queueTaskConf.execParallel));
        result.put("currentPressure", String.valueOf(pressure + "/" + queueTaskConf.execParallel));
        return result;
    }

    @Override
    public long getCommitTaskCount() {
        return commitCount.get();
    }

}
