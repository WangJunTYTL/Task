package com.peaceful.task.core;

import com.google.inject.AbstractModule;
import com.peaceful.task.core.client.TaskProxyByCglib;
import com.peaceful.task.core.coding.TaskCodingImpl;
import com.peaceful.task.core.conf.TaskConfService;
import com.peaceful.task.core.conf.TaskConfigOps;
import com.peaceful.task.core.controller.RedisCloudTaskController;
import com.peaceful.task.core.dispatch.TaskBeanFactoryImpl;
import com.peaceful.task.core.dispatch.TaskDispatcherService;
import com.peaceful.task.core.monitor.CloudTaskCountMonitor;
import com.peaceful.task.core.queue.redis.RedisQueue;

/**
 * Created by wangjun on 16-8-27.
 */
public class TaskModule extends AbstractModule {
    @Override
    protected void configure() {
        // 初始化conf
        TaskConfService confService = new TaskConfService();
        bind(TaskConfigOps.class).toInstance(confService.get());
//        bind(TaskContext.class).to(NewTaskContext.class).asEagerSingleton();
        // 装配任务控制器
        bind(TaskController.class).to(RedisCloudTaskController.class).asEagerSingleton();
        // 装配任务编码器
        bind(TaskCoding.class).to(TaskCodingImpl.class).asEagerSingleton();
        // 装配任务存储器
        bind(TaskQueue.class).to(RedisQueue.class).asEagerSingleton();
        // 装配任务bean factory
        bind(TaskBeanFactory.class).to(TaskBeanFactoryImpl.class).asEagerSingleton();
        // 装配任务监控器
        bind(TaskMonitor.class).to(CloudTaskCountMonitor.class).asEagerSingleton();
        // 装配任务代理器
        bind(TaskProxy.class).to(TaskProxyByCglib.class).asEagerSingleton();
        // 开始装配任务调度器
        bind(TaskDispatcher.class).to(TaskDispatcherService.class).asEagerSingleton();
    }
}
