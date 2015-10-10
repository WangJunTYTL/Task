package com.peaceful.task.container.dispatch;

import akka.actor.*;
import akka.japi.Function;
import akka.routing.ActorRefRoutee;
import akka.routing.RoundRobinRoutingLogic;
import akka.routing.Routee;
import akka.routing.Router;
import com.peaceful.task.container.common.TaskContainerConf;
import com.peaceful.task.container.msg.Task;
import com.peaceful.task.container.msg.Task2;
import scala.concurrent.duration.Duration;

import java.util.ArrayList;
import java.util.List;

/**
 * 任务存储与调度监管
 * 设计该角色的目的主要是为了监管 任务存储中⼼心 和 调度路由中⼼心 的情况,它 在两者之间起到一个纽带的作用,
 * 如果任务存储中心发生大量积压, 监管者 可以发出报警,
 * 同时他也 可以监控 调度路由中⼼心 的压力,考虑是否自动增加 路由中⼼心 的处理能力
 * Created by wangjun on 15/3/11.
 */
public class TaskSuperviseActor extends UntypedActor {

    Router router;


    {
        List<Routee> routees = new ArrayList<Routee>();
        for (int i = 0; i < TaskContainerConf.getConf().router; i++) {
            ActorRef r = getContext().actorOf(Props.create(RouterActor.class));
            getContext().watch(r);
            routees.add(new ActorRefRoutee(r));
        }
        router = new Router(new RoundRobinRoutingLogic(), routees);
    }


    @Override
    public void onReceive(Object msg) throws Exception {
        if (msg instanceof Task || msg instanceof Task2) {
            router.route(msg, getSender());
        } else if (msg instanceof Terminated) {
            router = router.removeRoutee(((Terminated) msg).actor());
            ActorRef r = getContext().actorOf(Props.create(RouterActor.class));
            getContext().watch(r);
            router = router.addRoutee(new ActorRefRoutee(r));
        } else {
            unhandled(msg);
        }
    }

    // manage child strategy
    @Override
    public SupervisorStrategy supervisorStrategy() {
        SupervisorStrategy strategy = new OneForOneStrategy(
                10, Duration.create("1 minute"), new Function<Throwable, SupervisorStrategy.Directive>() {
            @Override
            public SupervisorStrategy.Directive apply(Throwable t) {
                if (t instanceof ArithmeticException) return SupervisorStrategy.resume();
                else if (t instanceof NullPointerException) return SupervisorStrategy.restart();
                else if (t instanceof Exception) return SupervisorStrategy.restart();
                else return SupervisorStrategy.resume();
            }
        });
        return strategy;
    }
}
