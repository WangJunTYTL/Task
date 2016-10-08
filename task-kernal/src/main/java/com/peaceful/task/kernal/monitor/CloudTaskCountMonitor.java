package com.peaceful.task.kernal.monitor;

import com.google.inject.Inject;
import com.peaceful.task.kernal.conf.TaskConfigOps;
import com.peaceful.common.redis.service.Redis;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 同步本地记录的任务提交和消费次数到一个集中的地方。在一个中心记录生产次数和消费次数的目的是
 * <p>
 * 1. 合并各个节点的生产消费次数
 * 2. 监控系统可以查询该中心的数据，做实时系统监控
 * <p>
 * 目前实现的版本是集中地方是Redis。
 *
 * @author WangJun
 * @version 1.0 16/4/4
 */
public class CloudTaskCountMonitor extends LocalTaskCountMonitor {

    private static String CLOUD_TASK_PRODUCE_COUNT;
    private static String CLOUD_TASK_CONSUME_COUNT;
    private static String CLOUD_TASK_FAIL_COUNT;
    Logger logger = LoggerFactory.getLogger(getClass());

    @Inject
    public CloudTaskCountMonitor(TaskConfigOps ops) {
        CLOUD_TASK_PRODUCE_COUNT = "TASK-" + ops.name + "-PRODUCE-COUNT-MONITOR";
        CLOUD_TASK_CONSUME_COUNT = "TASK-" + ops.name + "-CONSUME-COUNT-MONITOR";
        CLOUD_TASK_FAIL_COUNT = "TASK-" + ops.name + "-EXCEPTION-COUNT-MONITOR";
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);
        executorService.scheduleAtFixedRate(new CloudMonitorSync(), 6, 6, TimeUnit.SECONDS);
    }

    private class CloudMonitorSync implements Runnable {

        @Override
        public void run() {
            try {
                // 定时清空本地记录数据，将数据同步记录到Redis中
                for (String queue : TASK_PRODUCT_COUNT.keySet()) {
                    Redis.cmd().hincrBy(CLOUD_TASK_PRODUCE_COUNT, queue, TASK_PRODUCT_COUNT.get(queue).getAndSet(0));
                }
                for (String queue : TASK_CONSUME_COUNT.keySet()) {
                    Redis.cmd().hincrBy(CLOUD_TASK_CONSUME_COUNT, queue, TASK_CONSUME_COUNT.get(queue).getAndSet(0));
                }
                for (String queue : TASK_FAIL_COUNT.keySet()) {
                    Redis.cmd().hincrBy(CLOUD_TASK_FAIL_COUNT, queue, TASK_FAIL_COUNT.get(queue).getAndSet(0));
                }
            } catch (Exception e) {
                logger.error("upload task submit & consume count error", e);
            }
        }
    }

}
