package com.peaceful.task.container.dispatch;

import akka.actor.*;
import akka.event.DiagnosticLoggingAdapter;
import akka.event.Logging;
import akka.japi.Function;
import akka.routing.ActorRefRoutee;
import akka.routing.RoundRobinRoutingLogic;
import akka.routing.Routee;
import akka.routing.Router;
import com.peaceful.task.container.common.TaskContainerConf;
import com.peaceful.task.container.msg.Coordination;
import com.peaceful.task.container.msg.OpenValve;
import com.peaceful.task.container.msg.Task;
import com.peaceful.task.container.msg.Task2;
import com.peaceful.task.container.store.TaskStore;
import org.apache.commons.lang3.StringUtils;
import scala.concurrent.duration.Duration;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 任务调度路由中心
 * <p/>
 * 路由中心分发任务方式有两种：
 * 第一，由系统的任务存储和调度监管委派，路由在把该任务分发给某个适合的actor，这种分发称之为强制分发
 * 第二，由路由中心和worker之间的内部协调分发，这种分发称之为Coordination分发
 * <p/>
 * Created by wangjun on 15/3/11.
 */
public class DispatchActor extends UntypedActor {

    final DiagnosticLoggingAdapter log = Logging.getLogger(this);
    Router router;
    // 此处暂时这样处理，主要为了限制actor的执行和观察actor的执行情况
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
        RouterCoordinate.pressure = pressure.get();
        if (msg instanceof Task || msg instanceof Task2) {
            log.debug("open dispatch task ");
            pressure.incrementAndGet();
            router.route(msg, getSelf());
        } else if (msg instanceof Coordination) {
            log.debug(getSender().path().toSerializationFormat() + " receive worker  msg");
            pressure.decrementAndGet();
            RouterCoordinate.pressure = pressure.get();
            if (pressure.get() >= TaskContainerConf.getConf().worker) {
                //pass
            } else {
                Coordination coordination = (Coordination) msg;
                try {
                    String taskJson = TaskStore.get().pop(coordination.queueName);
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
