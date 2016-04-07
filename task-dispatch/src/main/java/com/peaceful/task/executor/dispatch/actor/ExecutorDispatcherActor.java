package com.peaceful.task.executor.dispatch.actor;

import akka.actor.OneForOneStrategy;
import akka.actor.Props;
import akka.actor.SupervisorStrategy;
import akka.actor.UntypedActor;
import akka.event.DiagnosticLoggingAdapter;
import akka.event.Logging;
import akka.japi.Function;
import com.peaceful.task.context.SimpleTaskContext;
import com.peaceful.task.context.coding.TUR;
import com.peaceful.task.context.common.ContextConstant;
import com.peaceful.task.context.config.Executor;
import com.peaceful.task.context.config.TaskConfigOps;
import com.peaceful.task.executor.dispatch.TaskUtils;
import com.peaceful.task.executor.dispatch.msg.TaskCompleted;
import scala.Option;
import scala.concurrent.duration.Duration;

import java.util.ArrayList;
import java.util.List;

/**
 * Executor 分发器,负责将Task对象分发到对应的executor上执行
 *
 * @author <a href="mailto:wangjuntytl@163.com">WangJun</a>
 * @version 1.0 16/1/12
 */
public class ExecutorDispatcherActor extends UntypedActor {

    TaskConfigOps taskConfigOps = (TaskConfigOps) SimpleTaskContext.CONTEXT.get(ContextConstant.CONFIG);
    List<String> executors;
    final DiagnosticLoggingAdapter log = Logging.getLogger(this);

    @Override
    public void preStart() throws Exception {
        // todo restart 是否会调用
        // 创建对应executor的监管者
        TaskConfigOps.ExecutorConfigOps executorConfigOps = taskConfigOps.executorConfigOps;
        executors = new ArrayList<String>();
        for (Executor executor : executorConfigOps.executorNodeList) {
            getContext().actorOf(Props.create(ExecutorActor.class, executor.Class.newInstance()), executor.name);
            log.info("Started {} xecute  OK...", executor.name);
            executors.add(executor.name);
        }
        super.preStart();
    }

    @Override
    public void preRestart(Throwable reason, Option<Object> message) throws Exception {
        // 处理失败的异常信息和最后处理的消息将发送过来
        log.error("{} will preRestart,resone:{},message: {}",getSelf().path().name(),reason,message);
        super.preRestart(reason, message);
    }

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof TUR) {
            // outside dispatch push task to execute
            TUR taskUnit = (TUR) message;
            if (executors.contains(taskUnit.getTask().executor)) {
                getContext().actorSelection(taskUnit.getTask().executor).tell(taskUnit, getSelf());
            } else {
                log.warning("execute {} is not exist,the task {} will push to {}", taskUnit.getTask().executor, taskUnit.getTask().id, executors.get(0));
                getContext().actorSelection(executors.get(0)).tell(taskUnit, getSelf());
            }
        } else if (message instanceof TaskCompleted) {
            TaskCompleted completed = (TaskCompleted) message;
            Object[] params = new Object[]{completed.id, completed.executor, completed.startTime - completed.submitTime, completed.startTime - completed.createTime, completed.completeTime - completed.startTime};
            log.info("completed {} on {} remote wait {}ms local wait {}ms cost {}ms", params);
            // 如果本地的taskUnit对象已经缓存时间超过1s,停止向executor主动推送task unit,这样可以让已经缓存的task unit 尽快执行,因为设计这个系统的初衷并不想把这些task unit缓存到本地
            if (completed.startTime - completed.createTime > 1000) {
                return;
            }
            TUR taskUnit = TaskUtils.next(completed.queue);
            if (taskUnit != null) {
                getContext().actorSelection(taskUnit.getTask().executor).tell(taskUnit, getSelf());
            }
        } else {
            unhandled(message);
        }
    }

    /**
     * top actor supervisor is restart
     *
     * @return
     */
    @Override
    public SupervisorStrategy supervisorStrategy() {
        SupervisorStrategy strategy = new OneForOneStrategy(
                10, Duration.create("1 minute"), new Function<Throwable, SupervisorStrategy.Directive>() {
            @Override
            public SupervisorStrategy.Directive apply(Throwable t) {
                log.error("dispatch actor will restart it's children,because of {}", t);
                // 如果dispatch actor存在异常,则重启
                return SupervisorStrategy.restart();
            }
        });
        return strategy;
    }

}
