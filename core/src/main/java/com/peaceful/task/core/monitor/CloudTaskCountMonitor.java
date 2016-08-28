package com.peaceful.task.core.monitor;

import com.google.inject.Inject;
import com.peaceful.task.core.conf.TaskConfigOps;
import com.peaceful.common.redis.service.Redis;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author WangJun
 * @version 1.0 16/4/4
 */
public class CloudTaskCountMonitor extends LocalTaskCountMonitor {

    private static String CLOUD_TASK_PRODUCE_COUNT;
    private static String CLOUD_TASK_CONSUME_COUNT;
    Logger logger = LoggerFactory.getLogger(getClass());

    @Inject
    public CloudTaskCountMonitor(TaskConfigOps ops) {
        CLOUD_TASK_PRODUCE_COUNT = "TASK-" + ops.name + "-PRODUCE-COUNT-MONITOR";
        CLOUD_TASK_CONSUME_COUNT = "TASK-" + ops.name + "-CONSUME-COUNT-MONITOR";
        ScheduledExecutorService executorService =  Executors.newScheduledThreadPool(1);
        executorService.scheduleAtFixedRate(new CloudMonitorSync(), 6, 6, TimeUnit.SECONDS);
    }


    class CloudMonitorSync implements Runnable {

        @Override
        public void run() {
            try {
                for (String queue : TASK_PRODUCT_COUNT.keySet()) {
                    Redis.cmd().hincrBy(CLOUD_TASK_PRODUCE_COUNT, queue, TASK_PRODUCT_COUNT.get(queue).getAndSet(0));
                }

                for (String queue : TASK_CONSUME_COUNT.keySet()) {
                    Redis.cmd().hincrBy(CLOUD_TASK_CONSUME_COUNT, queue, TASK_CONSUME_COUNT.get(queue).getAndSet(0));
                }
            } catch (Exception e) {
                logger.error("upload task submit & consume count error", e);
            }

        }
    }

}
