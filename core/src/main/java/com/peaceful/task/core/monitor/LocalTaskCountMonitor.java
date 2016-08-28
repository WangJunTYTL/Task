package com.peaceful.task.core.monitor;

import com.peaceful.task.core.TaskMonitor;
import com.peaceful.task.core.coding.TU;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author WangJun
 * @version 1.0 16/4/4
 */
public class LocalTaskCountMonitor implements TaskMonitor {


    public static final Map<String, AtomicLong> TASK_PRODUCT_COUNT = new ConcurrentHashMap<String, AtomicLong>();
    public static final Map<String, AtomicLong> TASK_CONSUME_COUNT = new ConcurrentHashMap<String, AtomicLong>();

    @Override
    public void produce(TU tu) {
        AtomicLong count = TASK_PRODUCT_COUNT.get(tu.getQueueName());
        if (count == null) {
            AtomicLong atomicLong = new AtomicLong(1);
            TASK_PRODUCT_COUNT.put(tu.getQueueName(),atomicLong);
        } else {
            count.incrementAndGet();
        }
    }

    @Override
    public void consume(TU tu) {
        AtomicLong count = TASK_CONSUME_COUNT.get(tu.getQueueName());
        if (count == null) {
            AtomicLong atomicLong = new AtomicLong(1);
            TASK_CONSUME_COUNT.put(tu.getQueueName(),atomicLong);
        } else {
            count.incrementAndGet();
        }
    }
}
