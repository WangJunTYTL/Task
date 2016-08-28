package com.peaceful.task.core.dispatch.actor.msg;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.peaceful.common.util.ExceptionUtils;
import com.peaceful.task.core.TaskBeanFactory;
import com.peaceful.task.core.TaskContext;
import com.peaceful.task.core.coding.TUR;
import com.peaceful.task.core.dispatch.NoBlockAutoDispatch;
import org.perf4j.StopWatch;
import org.perf4j.slf4j.Slf4JStopWatch;

/**
 * 装饰TaskUnit,使其在完成任务后通知executor dispatch
 * <p/>
 * Created by wangjun on 16/1/15.
 */
public class DecorateTask implements Runnable {

    TUR taskUnit;
    private TaskContext context;
    ActorRef executorActor;
    private final LoggingAdapter log;
    private ActorSystem system;

    public DecorateTask(TaskContext context, TUR taskUnit, ActorRef execuotActor) {
        this.taskUnit = taskUnit;
        this.taskUnit.context = context;
        this.executorActor = execuotActor;
        this.system = NoBlockAutoDispatch.getSystem(context.getConfigOps().name);
        log = Logging.getLogger(system, DecorateTask.class);
    }

    @Override
    public void run() {
        TaskCompleted completed = new TaskCompleted(taskUnit);
        completed.startTime = System.currentTimeMillis();
        try {
            StopWatch watch = new Slf4JStopWatch();
            taskUnit.run();
            watch.stop("TASK.CONSUME");
            watch.stop("task." + taskUnit.getTask().queueName + ".consume");
        } catch (Exception e) {
            log.error("{} execute error {}", taskUnit.getTask().id, ExceptionUtils.getStackTrace(e));
        } finally {
            completed.completeTime = System.currentTimeMillis();
            // 通知所属ExecutorActor任务完成
            system.actorSelection("/user/dispatcher/executorDispatch").tell(completed, executorActor);
        }

    }
}
