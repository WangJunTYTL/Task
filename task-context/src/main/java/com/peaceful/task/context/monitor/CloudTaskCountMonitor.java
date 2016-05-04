package com.peaceful.task.context.monitor;

import com.peaceful.task.context.SimpleTaskContext;
import com.peaceful.task.context.common.ContextConstant;
import com.peaceful.task.context.config.TaskConfigOps;
import com.peaceful.common.redis.service.Redis;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author WangJun
 * @version 1.0 16/4/4
 */
public class CloudTaskCountMonitor extends SimpleTaskCountMonitor {

    private static String CLOUD_TASK_PRODUCE_COUNT;
    private static String CLOUD_TASK_CONSUME_COUNT;

    public CloudTaskCountMonitor() {
        TaskConfigOps ops = (TaskConfigOps) SimpleTaskContext.CONTEXT.get(ContextConstant.CONFIG);
        CLOUD_TASK_PRODUCE_COUNT = "TASK-" + ops.name + "-PRODUCE-COUNT-MONITOR";
        CLOUD_TASK_CONSUME_COUNT = "TASK-" + ops.name + "-CONSUME-COUNT-MONITOR";
        ScheduledExecutorService executorService = (ScheduledExecutorService) SimpleTaskContext.CONTEXT.get(ContextConstant.PUBLICE_SCHEDULE);
        executorService.scheduleAtFixedRate(new CloudMonitorSync(), 6, 6, TimeUnit.SECONDS);
    }


    class CloudMonitorSync implements Runnable {

        @Override
        public void run() {

            for (String queue : TASK_PRODUCT_COUNT.keySet()) {
                Redis.cmd().hincrBy(CLOUD_TASK_PRODUCE_COUNT, queue, TASK_PRODUCT_COUNT.get(queue).getAndSet(0));
            }

            for (String queue : TASK_CONSUME_COUNT.keySet()) {
                Redis.cmd().hincrBy(CLOUD_TASK_CONSUME_COUNT, queue, TASK_CONSUME_COUNT.get(queue).getAndSet(0));
            }
        }
    }

}
