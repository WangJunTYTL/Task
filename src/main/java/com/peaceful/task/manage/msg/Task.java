package com.peaceful.task.manage.msg;

import com.peaceful.task.manage.common.QueueTaskConf;
import com.peaceful.task.manage.exception.AddTaskQueueException;
import com.peaceful.task.manage.exception.NotFindQueueException;
import com.peaceful.task.manage.repo.Queue;
import com.peaceful.task.manage.service.impl.MonitorQueueImpl;
import com.alibaba.fastjson.JSON;

import java.io.Serializable;
import java.util.Map;

/**
 * @author wangjun
 * @version 1.0
 * @since 15/5/4.
 */

public class Task implements Serializable {


    public long id;
    private String queueName;
    private String method;
    private Map<String, Object> params;

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Map<String, Object> getParams() {
        return params;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }

    public String getQueueName() {
        return queueName;
    }

    public void setQueueName(String queueName) {
        this.queueName = queueName;
    }

    public Task() {
        this.id = System.currentTimeMillis();
    }

    public Task(String queueName, String method, Map<String, Object> params) {
        if (QueueTaskConf.getConf().queues.contains(queueName)) {
            this.id = System.currentTimeMillis();
        } else {
            throw new NotFindQueueException(queueName);
        }
        this.queueName = queueName;
        this.method = method;
        this.params = params;
        try {
            Queue.lpush(queueName, JSON.toJSONString(this));
            MonitorQueueImpl.commitCount.incrementAndGet();
        } catch (Exception e) {
            throw new AddTaskQueueException(toString(), e);
        }
    }

    @Override
    public String toString() {
        return "task id is " + id + " queueName is " + queueName + " method is " + method + " params is " + params;
    }

    public long getId() {
        return id;
    }
}
