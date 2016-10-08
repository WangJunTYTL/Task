package com.peaceful.task.kernal.monitor;

import com.peaceful.task.kernal.TaskMonitor;
import com.peaceful.task.kernal.coding.TU;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 记录本地机器的任务提交次数和消费次数
 *
 * @author WangJun
 * @version 1.0 16/4/4
 */
public class LocalTaskCountMonitor implements TaskMonitor {


    // 记录提交次数
    public static final Map<String, AtomicLong> TASK_PRODUCT_COUNT = new ConcurrentHashMap<String, AtomicLong>();
    // 记录消费次数
    public static final Map<String, AtomicLong> TASK_CONSUME_COUNT = new ConcurrentHashMap<String, AtomicLong>();
    // 记录任务执行失败次数
    public static final Map<String, AtomicLong> TASK_FAIL_COUNT = new ConcurrentHashMap<String, AtomicLong>();

    @Override
    public void produce(TU tu) {
        AtomicLong count = TASK_PRODUCT_COUNT.get(tu.getQueueName());
        if (count == null) {
            AtomicLong atomicLong = new AtomicLong(1);
            TASK_PRODUCT_COUNT.put(tu.getQueueName(), atomicLong);
        } else {
            count.incrementAndGet();
        }
    }

    @Override
    public void consume(TU tu) {
        AtomicLong count = TASK_CONSUME_COUNT.get(tu.getQueueName());
        if (count == null) {
            AtomicLong atomicLong = new AtomicLong(1);
            TASK_CONSUME_COUNT.put(tu.getQueueName(), atomicLong);
        } else {
            count.incrementAndGet();
        }
    }

    @Override
    public void exceptionIncr(String name) {
        AtomicLong count = TASK_FAIL_COUNT.get(name);
        if (count == null) {
            AtomicLong atomicLong = new AtomicLong(1);
            TASK_FAIL_COUNT.put(name, atomicLong);
        } else {
            count.incrementAndGet();
        }
    }
}
