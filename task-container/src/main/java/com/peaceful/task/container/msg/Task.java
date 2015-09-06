package com.peaceful.task.container.msg;

import com.peaceful.task.container.common.IdGenerate;
import com.peaceful.task.container.common.TaskContainerConf;
import com.peaceful.task.container.exception.AddTaskQueueException;
import com.peaceful.task.container.exception.NotFindQueueException;
import com.peaceful.task.container.repo.TaskQueue;
import com.peaceful.task.container.monitor.impl.MonitorQueueImpl;
import com.alibaba.fastjson.JSON;

import java.io.Serializable;
import java.util.Map;

/**
 * 每一个任务单元的对象描述 2.0 后已移除
 *
 * @author wangjun
 * @version 1.0
 * @since 15/5/4.
 */
@Deprecated
public class Task implements Serializable {


    public String id;
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
        this.id = IdGenerate.getNext();
    }

    public Task(String queueName, String method, Map<String, Object> params) {
        if (TaskContainerConf.getConf().queues.contains(queueName)) {
            this.id = IdGenerate.getNext();
        } else {
            throw new NotFindQueueException(queueName);
        }
        this.queueName = queueName;
        this.method = method;
        this.params = params;
        try {
            TaskQueue.push(queueName, JSON.toJSONString(this));
            MonitorQueueImpl.commitCount.incrementAndGet();
        } catch (Exception e) {
            throw new AddTaskQueueException(toString(), e);
        }
    }

    @Override
    public String toString() {
        return "task id is " + id + " queueName is " + queueName + " method is " + method + " params is " + params;
    }

    public String getId() {
        return id;
    }
}
