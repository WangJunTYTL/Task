package com.peaceful.task.container.schedule;

import akka.actor.ActorRef;
import com.peaceful.task.container.common.ConfConstant;
import com.peaceful.task.container.common.TaskContainerConf;
import com.peaceful.task.container.common.TaskContainerLogger;
import com.peaceful.task.container.dispatch.TaskProtocol;
import com.peaceful.task.container.store.TaskStore;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;

import java.util.List;

/**
 * focused task 固定周期调度
 * <p/>
 * 固定周期的调度其实是一种强制性的打断worker和router之间的自动协调调度。这样可以防止worker和router在一个task中不间断的调度，导致其它task的调度
 * 出现堵塞
 * <p/>
 * 另外固定周期的调度，也可以为新生的task提交的router中，使其参与到调度中
 *
 * focused task的调度应满足近实时的调度，即当有任务单元提交到任务中心时，调度模块可以在限制周期中发现并参与调度,默认是2s，可以在{@link ConfConstant#FOCUSED_SCHEDULE_TICK}设置
 *
 * @author WangJun <wangjuntytl@163.com>
 * @version 1.0 15/9/15
 * @since 1.6
 */

public class FocusedTaskSupervisorRunnable implements Runnable {

    private static Logger LOGGER = TaskContainerLogger.ROOT_LOGGER;


    private ActorRef supervisorActor;

    public FocusedTaskSupervisorRunnable(ActorRef supervisorActor) {
        this.supervisorActor = supervisorActor;
    }

    @Override
    public void run() {
        LOGGER.debug("task container start ...");
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
            LOGGER.error("task container error {},cause {}", e, e.getCause());
        }
    }
}
