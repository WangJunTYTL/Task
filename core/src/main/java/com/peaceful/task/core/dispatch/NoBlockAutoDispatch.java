package com.peaceful.task.core.dispatch;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import com.google.common.util.concurrent.AbstractIdleService;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.peaceful.task.core.Task;
import com.peaceful.task.core.TaskBeanFactory;
import com.peaceful.task.core.coding.TUR;
import com.peaceful.task.core.conf.TaskConfigOps;
import com.peaceful.task.core.dispatch.actor.DispatchManagerActor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * 无阻塞自动任务调度模块
 * Created by wangjun on 16-8-27.
 */
@Singleton
public class NoBlockAutoDispatch extends AbstractIdleService {

    private ActorSystem system;
    private Logger logger = LoggerFactory.getLogger(getClass());
    private static Map<String,ActorSystem> systemMap = new HashMap<String, ActorSystem>();

    @Inject
    private TaskConfigOps ops;

    public void tell(TUR tur) {
        system.actorSelection("/user/dispatcher/executorDispatch").tell(tur, ActorRef.noSender());
    }

    protected void startUp() throws Exception {
        system = ActorSystem.create(ops.name);
        // 创建actor系统的top actor,所有actor都要求从这里继承下去
        system.actorOf(Props.create(DispatchManagerActor.class, Task.getTaskContext(ops.name)), "dispatcher");
        systemMap.put(ops.name,system);
        logger.info("no block auto dispatch service start...");
    }

    protected void shutDown() throws Exception {
        if (!system.isTerminated()) {
            system.shutdown();
        }
    }

    public static ActorSystem getSystem(String name){
        return systemMap.get(name);
    }
}
