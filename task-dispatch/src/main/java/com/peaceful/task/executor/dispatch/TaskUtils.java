package com.peaceful.task.executor.dispatch;

import com.peaceful.task.context.SimpleTaskContext;
import com.peaceful.task.context.TaskCoding;
import com.peaceful.task.context.TaskController;
import com.peaceful.task.context.TaskMonitor;
import com.peaceful.task.context.coding.TUR;
import com.peaceful.task.context.common.ContextConstant;
import com.peaceful.task.context.config.TaskConfigOps;
import com.peaceful.task.context.dispatch.TaskMeta;
import com.peaceful.task.context.error.GetNextTaskException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by wangjun on 16/1/12.
 */
public class TaskUtils {

    private final static TaskConfigOps taskConfigOps = (TaskConfigOps) SimpleTaskContext.CONTEXT.get("config");
    private final static TaskController TASK_CONTROLLER = (TaskController) SimpleTaskContext.CONTEXT.get(ContextConstant.CONTROLLER);
    private final static TaskMonitor TASK_MONITOR = (TaskMonitor) SimpleTaskContext.CONTEXT.get(ContextConstant.MONITOR);

    // CODING module need in system context
    final static TaskCoding taskCodingI = SimpleTaskContext.CODING;

    final static Logger LOGGER = LoggerFactory.getLogger(TaskUtils.class);

    /**
     * 从队列服务中取出task指令
     *
     * @param queueName
     * @return
     */
    public static TUR next(String queueName) {
        try {
            if (TASK_CONTROLLER.findNeedDispatchTasks().contains(new TaskMeta(queueName))) {
                String taskJson = SimpleTaskContext.QUEUE.pop("TASK-" + taskConfigOps.name + "-" + queueName);
                if (StringUtils.isNoneEmpty(taskJson)) {
                    TUR taskUnit = taskCodingI.decoding(taskJson);
                    TASK_MONITOR.consume(taskUnit.getTask());
                    return taskUnit;
                }
            }
        } catch (Exception e) {
            throw new GetNextTaskException("Error: get next task from " + queueName);
        }
        return null;
    }
}
