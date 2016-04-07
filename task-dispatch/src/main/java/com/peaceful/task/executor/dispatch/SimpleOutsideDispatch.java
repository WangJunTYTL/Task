package com.peaceful.task.executor.dispatch;

import akka.actor.ActorSelection;
import com.peaceful.common.util.ExceptionUtils;
import com.peaceful.task.context.SimpleTaskContext;
import com.peaceful.task.context.TaskController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * focused task 外部固定频率调度器
 * <p/>
 * 固定周期的调度其实是一种强制性的打断worker和router之间的自动协调调度。这样可以防止worker和router在一个task中不间断的调度，导致其它task的调度
 * 出现堵塞
 * <p/>
 * 另外固定周期的调度，也可以为新生的task提交的router中，使其参与到调度中
 *
 * @author WangJun <wangjuntytl@163.com>
 * @version 1.0 15/9/15
 * @since 1.6
 */

public class SimpleOutsideDispatch extends OutsideDispatch implements Runnable {

    private static Logger LOGGER = LoggerFactory.getLogger(SimpleOutsideDispatch.class);
    private static TaskController controller = (TaskController) SimpleTaskContext.CONTEXT.get("controller");
//    private static TaskConfigOps taskConfigOps = (TaskConfigOps) ExecutorModule.getSystemContext().get("config");
//    private static OutsideDispatch outsideDispatch = new OutsideDispatch(taskConfigOps.name);


    private ActorSelection actorSelection;

    public SimpleOutsideDispatch(ActorSelection actorSelection) {
        this.actorSelection = actorSelection;
    }

    @Override
    public void run() {
        try {
            dispatch(actorSelection,controller.findNeedDispatchTasks());
        } catch (Exception e) {
            LOGGER.error("outside dispatch exception {}", ExceptionUtils.getStackTrace(e));
        }
    }
}
