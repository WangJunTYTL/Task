package com.peaceful.task.container.dispatch;

import com.peaceful.task.container.common.TaskContainerConf;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author WangJun <wangjuntytl@163.com>
 * @version 1.0 15/9/24
 * @since 1.6
 */

public class WorkerPressure {

//    public static Semaphore semaphore = new Semaphore(TaskContainerConf.getConf().worker + 2);

    // worker压力辅助观察信号量
    // todo 暂时这样实现，后期考虑采用本地固定对大小的队列
    public static AtomicInteger pressure = new AtomicInteger(TaskContainerConf.getConf().worker + 2);

}
