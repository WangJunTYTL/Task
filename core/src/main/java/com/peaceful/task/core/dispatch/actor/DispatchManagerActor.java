package com.peaceful.task.core.dispatch.actor;

import akka.actor.*;
import akka.event.DiagnosticLoggingAdapter;
import akka.event.Logging;
import akka.japi.Function;
import com.peaceful.task.core.TaskContext;
import com.peaceful.task.core.error.GetNextTaskException;
import scala.concurrent.duration.Duration;

/**
 * task 系统的top actor,负责整个系统的监管策略,如果有异常抛到该actor,讲重启整个系统
 *
 * @author <a href="mailto:wangjuntytl@163.com">WangJun</a>
 * @version 1.0 16/1/12
 */
public class DispatchManagerActor extends UntypedActor {

    DiagnosticLoggingAdapter log = Logging.getLogger(this);

    private TaskContext context;

    public DispatchManagerActor(TaskContext context) {
        this.context = context;
    }

    @Override
    public void preStart() throws Exception {
//        execute dispatch actor 负责 整个Task系统的调度
        getContext().actorOf(Props.create(ExecutorManageActor.class, context), "executorDispatch");
//        public  actor 作为其它需要利用到actor地方的监管者
        getContext().actorOf(Props.create(PublicActor.class), "public");
        super.preStart();
    }

    @Override
    public void onReceive(Object message) throws Exception {
        log.info("{}", message);
        unhandled(message);

    }


    /**
     * top actor supervisor is restart
     *
     * @return
     */
    @Override
    public SupervisorStrategy supervisorStrategy() {
        final SupervisorStrategy strategy = new OneForOneStrategy(
                10, Duration.create("1 minute"), new Function<Throwable, SupervisorStrategy.Directive>() {
            @Override
            public SupervisorStrategy.Directive apply(Throwable t) {
                if (t instanceof GetNextTaskException) {
                    log.error("{} receive message [{}] from child,resume", getSelf().path(), t.getMessage());
                    return SupervisorStrategy.resume();
                }
                log.error("{} supervisor will restart child actor exception {}", self().path().name(), t);
                return SupervisorStrategy.restart();
            }
        });
        return strategy;
    }

}
