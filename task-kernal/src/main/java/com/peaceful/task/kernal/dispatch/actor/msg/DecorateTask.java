package com.peaceful.task.kernal.dispatch.actor.msg;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.google.common.base.Throwables;
import com.peaceful.task.kernal.TaskContext;
import com.peaceful.task.kernal.coding.TUR;
import com.peaceful.task.kernal.dispatch.NoBlockAutoConsumer;
import org.perf4j.StopWatch;
import org.perf4j.slf4j.Slf4JStopWatch;

/**
 * 装饰TaskUnit,使其在完成任务后通知executor dispatch
 * <p/>
 * Created by wangjun on 16/1/15.
 */
public class DecorateTask implements Runnable {

    TUR taskUnit;
    ActorRef executorActor;
    private final LoggingAdapter log;
    private ActorSystem system;

    public DecorateTask(TaskContext context, TUR taskUnit, ActorRef execuotActor) {
        this.taskUnit = taskUnit;
        this.taskUnit.context = context;
        this.executorActor = execuotActor;
        this.system = NoBlockAutoConsumer.getSystem(context.getConfigOps().name);
        log = Logging.getLogger(system, DecorateTask.class);
    }

    @Override
    public void run() {
        TaskCompleted completed = new TaskCompleted(taskUnit);
        completed.startTime = System.currentTimeMillis();
        StopWatch watch = new Slf4JStopWatch();
        try {
            taskUnit.run();
            watch.stop("task.consume");
            watch.stop("task." + taskUnit.getTask().queueName + ".consume");
        } catch (Exception e) {
            watch.stop("task.consume.error");
            completed.isHasException = true;
            Throwables.propagate(e);
        } finally {
            completed.completeTime = System.currentTimeMillis();
            // 通知所属ExecutorActor任务完成
            system.actorSelection("/user/dispatcher/executors").tell(completed, executorActor);
        }
    }
}
