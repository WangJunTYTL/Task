package com.peaceful.task.executor.dispatch.msg;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.peaceful.common.util.ExceptionUtils;
import com.peaceful.task.context.SimpleTaskContext;
import com.peaceful.task.context.coding.TUR;

/**
 * 装饰TaskUnit,使其在完成任务后通知executor dispatch
 * <p/>
 * Created by wangjun on 16/1/15.
 */
public class DecorateTask implements Runnable {

    final static ActorSystem actorSystem = (ActorSystem) SimpleTaskContext.CONTEXT.get("actorSystem");
    TUR taskUnit;
    ActorRef executorActor;
    final LoggingAdapter log = Logging.getLogger(actorSystem, DecorateTask.class);

    public DecorateTask(TUR taskUnit, ActorRef execuotActor) {
        this.taskUnit = taskUnit;
        this.executorActor = execuotActor;
    }

    @Override
    public void run() {
        TaskCompleted completed = new TaskCompleted(taskUnit);
        completed.startTime = System.currentTimeMillis();
        try {
            taskUnit.run();
        } catch (Exception e) {
            log.error("{} execute error {}", taskUnit.getTask().id, ExceptionUtils.getStackTrace(e));
        } finally {
            completed.completeTime = System.currentTimeMillis();
            // 通知所属ExecutorActor任务完成
            actorSystem.actorSelection("/user/myroot/executorDispatch").tell(completed, executorActor);
        }

    }
}
