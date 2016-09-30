package com.peaceful.task.kernal;

import com.peaceful.task.kernal.coding.TU;

/**
 * @author WangJun
 * @version 1.0 16/3/31
 */
public interface TaskMonitor {

    void produce(TU tu);

    void consume(TU tu);
}
