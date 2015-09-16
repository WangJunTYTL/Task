package com.peaceful.task.container.schedule;

import akka.actor.ActorRef;
import com.peaceful.task.container.common.TaskContainerConf;
import com.peaceful.task.container.common.TaskContainerLogger;
import com.peaceful.task.container.dispatch.TaskProtocol;
import com.peaceful.task.container.store.TaskStore;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;

import java.util.List;

/**
 * @author WangJun <wangjuntytl@163.com>
 * @version 1.0 15/9/15
 * @since 1.6
 */

public class FocusedTaskSupervisorRunnable implements Runnable {

    private static Logger LOGGER = TaskContainerLogger.LOGGER;


    private ActorRef supervisorActor;

    public FocusedTaskSupervisorRunnable(ActorRef supervisorActor) {
        this.supervisorActor = supervisorActor;
    }

    @Override
    public void run() {
        LOGGER.debug("task container monitor start ...");
        try {
            List<String> queues = TaskContainerConf.getConf().focusedTasks;
            for (String q : queues) {
                String taskJson = TaskStore.get().pop(q);
                Object task = null;
                if (StringUtils.isNotEmpty(taskJson)) {
                    task = TaskProtocol.parse(taskJson);
                }
                if (task != null)
                    supervisorActor.tell(task, ActorRef.noSender());
            }
        } catch (Exception e) {
            LOGGER.error("task container error {}", e);
        }
    }
}
