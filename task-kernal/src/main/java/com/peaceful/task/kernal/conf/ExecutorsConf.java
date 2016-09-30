package com.peaceful.task.kernal.conf;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;

/**
 * Created by wangjun on 16/8/28.
 */
public class ExecutorsConf {


    private final static ThreadFactory threadFactoryBuilder = new ThreadFactoryBuilder()
            .setNameFormat("task-commons-%d")
            .setDaemon(true)
            .build();
    public static final ScheduledExecutorService SINGLE_THREAD_SCHEDULED = Executors.newSingleThreadScheduledExecutor(threadFactoryBuilder);
}
