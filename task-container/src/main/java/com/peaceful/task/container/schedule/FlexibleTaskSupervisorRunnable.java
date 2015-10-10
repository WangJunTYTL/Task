package com.peaceful.task.container.schedule;

import akka.actor.ActorRef;
import com.peaceful.task.container.common.ConfConstant;
import com.peaceful.task.container.common.TaskContainerLogger;
import com.peaceful.task.container.console.Monitor;
import com.peaceful.task.container.dispatch.TaskProtocol;
import com.peaceful.task.container.store.TaskStore;
import com.peaceful.task.container.store.help.RemoteFlexibleRegister;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;

/**
 * flexible task 固定周期调度
 * <p/>
 * 固定周期的调度其实是一种强制性的打断worker和router之间的自动协调调度。这样可以防止worker和router在一个task中不间断的调度，导致其它task的调度
 * 出现堵塞
 * <p/>
 * 另外固定周期的调度，也可以为新生的task提交的router中，使其参与到调度中
 * <p/>
 * flexible task的调度及时性相对于focused task来说，可以不要求调度及时性，即当有任务单元提交到任务中心时，调度模块只要可以发现并参与调度就好,默认是8s，可以在{@link ConfConstant#FIRST_FLEXIBLE_SCHEDULE_TICK}设置
 *
 * @author WangJun <wangjuntytl@163.com>
 * @version 1.0 15/9/15
 * @since 1.6
 */

public class FlexibleTaskSupervisorRunnable implements Runnable {

    private static Logger LOGGER = TaskContainerLogger.ROOT_LOGGER;


    private ActorRef supervisorActor;
    private Monitor monitor;

    public FlexibleTaskSupervisorRunnable(ActorRef supervisorActor, Monitor monitor) {
        this.supervisorActor = supervisorActor;
        this.monitor = monitor;
    }

    @Override
    public void run() {
        try {
            String qu = RemoteFlexibleRegister.getFlexibleTaskFromTaskCenter();
            if (StringUtils.isNotBlank(qu)) {
                monitor.addFirstFlexibleTask(qu, null);
            }
            for (String q : monitor.getFirstFlexibleTaskMap().keySet()) {
                String key = q;
                if (TaskStore.get().size(key) == 0) {
                    // pass
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
            LOGGER.error("task container found error {},cause {}", e, e.getCause());

        }
    }
}
