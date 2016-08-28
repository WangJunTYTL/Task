package com.peaceful.task.core.dispatch.actor;

import akka.actor.OneForOneStrategy;
import akka.actor.Props;
import akka.actor.SupervisorStrategy;
import akka.actor.UntypedActor;
import akka.event.DiagnosticLoggingAdapter;
import akka.event.Logging;
import akka.japi.Function;
import com.peaceful.task.core.TaskBeanFactory;
import com.peaceful.task.core.TaskContext;
import com.peaceful.task.core.coding.TUR;
import com.peaceful.task.core.conf.Executor;
import com.peaceful.task.core.conf.TaskConfigOps;
import com.peaceful.task.core.dispatch.NoBlockAutoDispatch;
import com.peaceful.task.core.dispatch.PullTask;
import com.peaceful.task.core.dispatch.actor.msg.TaskCompleted;
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
public class ExecutorManageActor extends UntypedActor {

    List<String> executors;
    final DiagnosticLoggingAdapter log = Logging.getLogger(this);

    private TaskContext context;
    private PullTask pullTask;
    public ExecutorManageActor(TaskContext context){
        this.context = context;
        this.pullTask = PullTask.get(context);
    }

    @Override
    public void preStart() throws Exception {
        // todo restart 是否会调用
        // 创建对应executor的监管者
        executors = new ArrayList<String>();
        for (Executor executor : context.getConfigOps().executorConfigOps.executorNodeList) {
            getContext().actorOf(Props.create(ExecutorSupervisionActor.class, executor.Class.newInstance(),context), executor.name);
            log.info("Started {} executor  OK...", executor.name);
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
            // 如果本地的taskUnit对象已经缓存时间超过1s,停止向executor主动推送task unit,这样可以让已经缓存的task unit 尽快执行,因为设计这个系统的初衷并不想把这些task unit缓存到本地
            if (completed.startTime - completed.createTime > 1000) {
                log.warning("SLOW TASK: completed {} on {} remote wait {}ms local wait {}ms cost {}ms", params);
                return;
            }else {
                log.info("completed {} on {} remote wait {}ms local wait {}ms cost {}ms", params);
            }
            TUR taskUnit = pullTask.next(completed.queue);
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
