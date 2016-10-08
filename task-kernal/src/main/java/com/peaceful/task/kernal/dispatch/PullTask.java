package com.peaceful.task.kernal.dispatch;

import com.google.common.base.Throwables;
import com.peaceful.task.kernal.*;
import com.peaceful.task.kernal.coding.TUR;
import com.peaceful.task.kernal.conf.TaskConfigOps;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by wangjun on 16/1/12.
 */
public class PullTask {

    private TaskConfigOps ops;
    private TaskMonitor monitor;
    private TaskController controller;
    private TaskCoding coding;
    private TaskQueue queue;
    private Logger logger = LoggerFactory.getLogger(getClass());

    public static PullTask get(TaskContext context) {
        PullTask pullTask = new PullTask();
        pullTask.ops = context.getConfigOps();
        pullTask.monitor = context.getTaskMonitor();
        pullTask.controller = context.getTaskController();
        pullTask.coding = context.getTaskCoding();
        pullTask.queue = context.getTaskQueue();
        return pullTask;
    }

    /**
     * 从队列服务中取出task指令
     *
     * @param queueName
     * @return
     */
    public TUR next(String queueName) {
        try {
            if (controller.findNeedDispatchTasks().contains(new TaskMeta(queueName))) {
                String taskJson = queue.pop(ops.name + "-" + queueName);
                if (StringUtils.isNoneEmpty(taskJson)) {
                    TUR taskUnit = coding.decoding(taskJson);
                    monitor.consume(taskUnit.getTask());
                    return taskUnit;
                }
            }
        } catch (Exception e) {
            // 关键性日志
            logger.error("can't get next task from {},cause:{}", queueName, Throwables.getStackTraceAsString(e));
        }
        return null;
    }
}
