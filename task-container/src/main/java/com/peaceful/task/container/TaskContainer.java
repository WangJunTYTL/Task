package com.peaceful.task.container;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import com.peaceful.task.container.common.TaskContainerLogger;
import com.peaceful.task.container.common.TaskContainerConf;
import com.peaceful.task.container.dispatch.TaskProtocol;
import com.peaceful.task.container.dispatch.TaskSuperviseActor;
import com.peaceful.task.container.repo.TaskQueue;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import scala.concurrent.duration.Duration;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by wangjun on 15/1/22.
 */
public class TaskContainer {

    private static Logger LOGGER = TaskContainerLogger.LOGGER;

    private static ActorSystem system;

    public static void setSystem(ActorSystem system) {
        TaskContainer.system = system;
    }

    public static ActorSystem getSystem() {
        return TaskContainer.system;
    }


    public static void start() {
        if (system == null) {
            system = ActorSystem.create("TaskContainer");
            LOGGER.info("akka system is default!");
        }
        final ActorRef supervise = system.actorOf(Props.create(TaskSuperviseActor.class), "taskSupervise");
        system.scheduler().schedule(Duration.Zero(), Duration.create(2, TimeUnit.SECONDS),
                new Runnable() {
                    @Override
                    public void run() {
                        LOGGER.debug("task container monitor start ...");
                        try {
                            List<String> queues = TaskContainerConf.getConf().queues;
                            for (String q : queues) {
                                String taskJson = TaskQueue.pop(q);
                                Object task = null;
                                if (StringUtils.isNotEmpty(taskJson)) {
                                    task = TaskProtocol.parse(taskJson);
                                }
                                if (task != null)
                                    supervise.tell(task, ActorRef.noSender());
                            }
                        } catch (Exception e) {
                            LOGGER.error("task container error {}", e);
                        }
                    }
                }, system.dispatcher());

    }

}
