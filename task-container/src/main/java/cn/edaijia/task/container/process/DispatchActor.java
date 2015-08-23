package cn.edaijia.task.container.process;

import akka.actor.*;
import akka.event.DiagnosticLoggingAdapter;
import akka.event.Logging;
import akka.japi.Function;
import akka.routing.ActorRefRoutee;
import akka.routing.RoundRobinRoutingLogic;
import akka.routing.Routee;
import akka.routing.Router;
import cn.edaijia.task.container.job.TaskProtocol;
import cn.edaijia.task.container.msg.Coordination;
import cn.edaijia.task.container.common.TaskContainerConf;
import cn.edaijia.task.container.msg.OpenValve;
import cn.edaijia.task.container.msg.Task;
import cn.edaijia.task.container.msg.Task2;
import cn.edaijia.task.container.repo.TaskQueue;
import cn.edaijia.task.container.monitor.impl.MonitorQueueImpl;
import org.apache.commons.lang3.StringUtils;
import scala.concurrent.duration.Duration;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by wangjun on 15/3/11.
 */
public class DispatchActor extends UntypedActor {

    final DiagnosticLoggingAdapter log = Logging.getLogger(this);
    Router router;
    private AtomicInteger pressure = new AtomicInteger(0);

    {
        List<Routee> routees = new ArrayList<Routee>();
        for (int i = 0; i < TaskContainerConf.getConf().worker; i++) {
            ActorRef r = getContext().actorOf(Props.create(WorkActor.class));
            getContext().watch(r);
            routees.add(new ActorRefRoutee(r));
        }
        router = new Router(new RoundRobinRoutingLogic(), routees);
    }

    @Override
    public void onReceive(Object msg) throws Exception {
        if (pressure.get() < 0) pressure.set(0);
        MonitorQueueImpl.pressure = pressure.get();
        if (msg instanceof Task || msg instanceof Task2) {
            log.debug("open dispatch task ");
            pressure.incrementAndGet();
            router.route(msg, getSelf());
        } else if (msg instanceof Coordination) {
            log.debug(getSender().path().toSerializationFormat() + " receive worker  msg");
            pressure.decrementAndGet();
            MonitorQueueImpl.pressure = pressure.get();
            if (pressure.get() >= TaskContainerConf.getConf().worker) {
                //pass
            } else {
                Coordination coordination = (Coordination) msg;
                try {
                    String taskJson = TaskQueue.rpop(coordination.queueName);
                    Object t = null;
                    if (StringUtils.isNotEmpty(taskJson)) {
                        log.debug("task is {}", taskJson);
                        t = TaskProtocol.parse(taskJson);
                    }
                    if (t != null) {
                        log.debug("inner coordination task ");
                        pressure.incrementAndGet();
                        router.route(t, getSelf());
                    }
                } catch (Exception e) {
                    log.error("queue task error {}", e);
                }
            }
        } else if (msg instanceof Terminated) {
            router = router.removeRoutee(((Terminated) msg).actor());
            ActorRef r = getContext().actorOf(Props.create(WorkActor.class));
            getContext().watch(r);
            router = router.addRoutee(new ActorRefRoutee(r));
        } else if (msg instanceof OpenValve) {
            pressure.set(0);
        } else {
            unhandled(msg);
        }
        if (pressure.get() > (TaskContainerConf.getConf().worker + 2)) {
            pressure.set(0);
            Thread.sleep(500);
        }
        log.debug("current pressure value is " + pressure.get());
    }


    // manage child strategy
    @Override
    public SupervisorStrategy supervisorStrategy() {
        SupervisorStrategy strategy = new OneForOneStrategy(
                10, Duration.create("1 minute"), new Function<Throwable, SupervisorStrategy.Directive>() {
            @Override
            public SupervisorStrategy.Directive apply(Throwable t) {
                pressure.decrementAndGet();
                return SupervisorStrategy.restart();
            }
        });
        return strategy;
    }
}
