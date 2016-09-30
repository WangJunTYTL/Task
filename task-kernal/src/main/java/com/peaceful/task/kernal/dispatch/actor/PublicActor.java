package com.peaceful.task.kernal.dispatch.actor;

import akka.actor.OneForOneStrategy;
import akka.actor.Props;
import akka.actor.SupervisorStrategy;
import akka.actor.UntypedActor;
import akka.event.DiagnosticLoggingAdapter;
import akka.event.Logging;
import akka.japi.Function;
import scala.concurrent.duration.Duration;

/**
 * 如果需要扩展,请以该public actor为root 向下创建actor
 * supervisor strategy: restart
 * <p/>
 * Created by wangjun on 16/1/13.
 */
public class PublicActor extends UntypedActor {

    DiagnosticLoggingAdapter log = Logging.getLogger(this);

    @Override
    public void preStart() throws Exception {
        getContext().actorOf(Props.create(TaskRouterActor.class), "taskDispatch");
        super.preStart();
    }

    @Override
    public void onReceive(Object message) throws Exception {
        log.info("public unhandle message {}", message);
        unhandled(message);
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
                log.error("{} supervisor will resume child actor exception {}", self().path().name(), t);
                return SupervisorStrategy.restart();
            }
        });
        return strategy;
    }
}
