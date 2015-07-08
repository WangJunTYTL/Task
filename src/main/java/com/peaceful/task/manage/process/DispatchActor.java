package com.peaceful.task.manage.process;

import akka.actor.*;
import akka.event.DiagnosticLoggingAdapter;
import akka.event.Logging;
import akka.japi.Function;
import akka.routing.ActorRefRoutee;
import akka.routing.RoundRobinRoutingLogic;
import akka.routing.Routee;
import akka.routing.Router;
import com.peaceful.task.manage.msg.Coordination;
import com.peaceful.task.manage.common.QueueTaskConf;
import com.peaceful.task.manage.msg.OpenValve;
import com.peaceful.task.manage.msg.Task;
import com.peaceful.task.manage.repo.Queue;
import com.peaceful.task.manage.service.impl.MonitorQueueImpl;
import com.alibaba.fastjson.JSON;
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
        for (int i = 0; i < QueueTaskConf.getConf().execParallel; i++) {
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
        if (msg instanceof Task) {
            log.debug("open dispatch task ");
            pressure.incrementAndGet();
            router.route(msg, getSelf());
        } else if (msg instanceof Coordination) {
            log.debug(getSender().path().toSerializationFormat() + " receive worker  msg");
            pressure.decrementAndGet();
            MonitorQueueImpl.pressure = pressure.get();
            if (pressure.get() >= QueueTaskConf.getConf().execParallel) {
                //pass
            } else {
                Coordination coordination = (Coordination) msg;
                try {
                    String taskJson = Queue.rpop(coordination.queueName);
                    Task t = null;
                    if (StringUtils.isNotEmpty(taskJson)) {
                        log.info("task is {}", taskJson);
                        t = JSON.parseObject(taskJson, Task.class);
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
        if (pressure.get() > (QueueTaskConf.getConf().dangerParallel + 2)) {
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
