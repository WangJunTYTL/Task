package com.peaceful.task.manage.process;

import akka.actor.*;
import akka.japi.Function;
import akka.routing.ActorRefRoutee;
import akka.routing.RoundRobinRoutingLogic;
import akka.routing.Routee;
import akka.routing.Router;
import com.peaceful.task.manage.common.QueueTaskConf;
import com.peaceful.task.manage.msg.OpenValve;
import com.peaceful.task.manage.msg.Task;
import com.peaceful.task.manage.service.MontiorQueue;
import com.peaceful.task.manage.service.impl.MonitorQueueImpl;
import scala.concurrent.duration.Duration;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangjun on 15/3/11.
 */
public class QueueSuperviseActor extends UntypedActor {

    Router router;
    MontiorQueue montiorQueue;


    {
        List<Routee> routees = new ArrayList<Routee>();
        for (int i = 0; i < QueueTaskConf.getConf().dispatchParallel; i++) {
            ActorRef r = getContext().actorOf(Props.create(DispatchActor.class));
            getContext().watch(r);
            routees.add(new ActorRefRoutee(r));
        }
        router = new Router(new RoundRobinRoutingLogic(), routees);
        montiorQueue = new MonitorQueueImpl();
    }


    @Override
    public void onReceive(Object msg) throws Exception {
        if (msg instanceof Task) {
            router.route(msg, getSender());
            long sum = montiorQueue.getSumOfQueueSize();
            if (sum > 666) {
//                UsefulUtils.alertSms(QueueTaskConf.getConf().alertPhone, "【" + QueueTaskConf.getConf().projectName + "】队列任务出现积压，目前积压任务数：" + sum);
                router.route(new OpenValve(), getSender());
            }
        } else if (msg instanceof Terminated) {
            router = router.removeRoutee(((Terminated) msg).actor());
            ActorRef r = getContext().actorOf(Props.create(DispatchActor.class));
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
