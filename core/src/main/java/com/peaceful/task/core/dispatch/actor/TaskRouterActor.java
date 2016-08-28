package com.peaceful.task.core.dispatch.actor;

import akka.actor.*;
import akka.event.DiagnosticLoggingAdapter;
import akka.event.Logging;
import akka.japi.Function;
import akka.routing.ActorRefRoutee;
import akka.routing.RoundRobinRoutingLogic;
import akka.routing.Routee;
import akka.routing.Router;
import com.peaceful.task.core.dispatch.actor.msg.DecorateTask;
import scala.concurrent.duration.Duration;

import java.util.ArrayList;
import java.util.List;

/**
 * 任务调度路由中心
 * <p/>
 * 路由中心分发任务方式有两种：
 * 第一，由系统的任务存储和调度监管委派，路由在把该任务分发给某个适合的actor，这种分发称之为强制分发，目的是为了路由中心和worker不会在一个任务队列上循环
 * 使每个任务队列都有机会被调度
 * 第二，由路由中心和worker之间的内部协调分发，这种分发称之为协调分发，目的是为了路由中心和某个actor之间维持一种近长连接的状态，快速循环调度
 * <p/>
 * <p/>
 * 调度中心模块的router采用轮询的方式调用worker
 * Created by wangjun on 15/3/11.
 */
public class TaskRouterActor extends UntypedActor {

    final DiagnosticLoggingAdapter log = Logging.getLogger(this);
    Router router;


    {
        List<Routee> routees = new ArrayList<Routee>();
        for (int i = 0; i < 8; i++) {
            ActorRef r = getContext().actorOf(Props.create(WorkerActor.class), "worker-" + i);
            getContext().watch(r);
            routees.add(new ActorRefRoutee(r));
        }
        router = new Router(new RoundRobinRoutingLogic(), routees);
    }


    @Override
    public void onReceive(Object msg) throws Exception {
        if (msg instanceof DecorateTask) {
            router.route(msg, getSelf());
        }
        else if (msg instanceof Terminated) {
            router = router.removeRoutee(((Terminated) msg).actor());
            ActorRef r = getContext().actorOf(Props.create(WorkerActor.class), ((Terminated) msg).actor().path().name());
            getContext().watch(r);
            router = router.addRoutee(new ActorRefRoutee(r));
        }
    }


    /**
     * worker 监管策略 restart
     * 如果worker在执行过程中发生异常，目前只是简单的尝试重启worker，如果1分钟内尝试10次还是不可以执行,则停止尝试
     *
     * @return
     */
    @Override
    public SupervisorStrategy supervisorStrategy() {
        SupervisorStrategy strategy = new OneForOneStrategy(
                10, Duration.create("1 minute"), new Function<Throwable, SupervisorStrategy.Directive>() {
            @Override
            public SupervisorStrategy.Directive apply(Throwable t) {
                log.error("supervise worker found error {},cause {}", t, t.getCause());
                return SupervisorStrategy.restart();
            }
        });
        return strategy;
    }
}