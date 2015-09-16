package com.peaceful.task.container.schedule;

import akka.actor.ActorRef;
import com.peaceful.task.container.common.Constant;
import com.peaceful.task.container.common.TaskContainerConf;
import com.peaceful.task.container.common.TaskContainerLogger;
import com.peaceful.task.container.dispatch.TaskProtocol;
import com.peaceful.task.container.monitor.Monitor;
import com.peaceful.task.container.store.FlexibleRegist;
import com.peaceful.task.container.store.TaskStore;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;

import java.util.Set;

/**
 * @author WangJun <wangjuntytl@163.com>
 * @version 1.0 15/9/15
 * @since 1.6
 */

public class FlexibleTaskSupervisorRunnable implements Runnable {

    private static Logger LOGGER = TaskContainerLogger.LOGGER;


    private ActorRef supervisorActor;
    private Monitor monitor;

    public FlexibleTaskSupervisorRunnable(ActorRef supervisorActor,Monitor monitor) {
        this.supervisorActor = supervisorActor;
        this.monitor = monitor;
    }

    @Override
    public void run() {
        try {
            Set<String> queues = TaskContainerConf.getConf().flexibleTasks;
            String qu = FlexibleRegist.getFlexbleTaskFromTaskCenter(Constant.FORCE_TASK_PERSISTENCE_QUEUE);
            if (StringUtils.isNotBlank(qu)) {
                queues.add(qu);
                // todo 添加任务描述
                monitor.addFirstFlexibleTask(qu, null);

            }
            for (String q : queues) {
                String key = Constant.RUNNING_TIME_TASK_PREFIX + q;
                if (TaskStore.get().size(key) == 0) {
                    queues.remove(q);
                } else {
                    String taskJson = TaskStore.get().pop(key);
                    Object task = null;
                    if (StringUtils.isNotEmpty(taskJson)) {
                        task = TaskProtocol.parse(taskJson);
                    }
                    if (task != null)
                        supervisorActor.tell(task, ActorRef.noSender());
                }
            }
        } catch (Exception e) {
            LOGGER.error("task container error {}", e);

        }
    }
}
