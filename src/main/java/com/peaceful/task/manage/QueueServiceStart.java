package com.peaceful.task.manage;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import com.peaceful.task.manage.common.QueueLogger;
import com.peaceful.task.manage.common.QueueTaskConf;
import com.peaceful.task.manage.msg.Task;
import com.peaceful.task.manage.process.QueueSuperviseActor;
import com.peaceful.task.manage.repo.Queue;
import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import scala.concurrent.duration.Duration;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by wangjun on 15/1/22.
 */
public class QueueServiceStart {

    private static Logger LOGGER = QueueLogger.LOGGER;

    private static ActorSystem system;

    public static void setSystem(ActorSystem system) {
        QueueServiceStart.system = system;
    }

    public static ActorSystem getSystem() {
        return QueueServiceStart.system;
    }


    public static void run() {
        final ActorRef supervise = system.actorOf(Props.create(QueueSuperviseActor.class), "queueSupervise");
        system.scheduler().schedule(Duration.Zero(), Duration.create(2, TimeUnit.SECONDS),
                new Runnable() {
                    @Override
                    public void run() {
                        LOGGER.debug("queue service monitor ...");
                        try {
                            List<String> queues = QueueTaskConf.getConf().queues;
                            for (String q : queues) {
                                String taskJson = Queue.rpop(q);
                                Task task = null;
                                if (StringUtils.isNotEmpty(taskJson)) {
                                    LOGGER.debug("monitor dispatch task  {}", taskJson);
                                    task = JSON.parseObject(taskJson, Task.class);
                                }
                                if (task != null)
                                    supervise.tell(task, ActorRef.noSender());
                            }
                        } catch (Exception e) {
                            LOGGER.error("queue task error {}", e);
                        }
                    }
                }, system.dispatcher());

    }

}
