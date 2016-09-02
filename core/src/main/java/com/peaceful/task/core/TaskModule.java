package com.peaceful.task.core;

import com.google.inject.AbstractModule;
import com.peaceful.task.core.client.TaskProxyByCglib;
import com.peaceful.task.core.coding.TaskCodingService;
import com.peaceful.task.core.conf.TaskConfService;
import com.peaceful.task.core.conf.TaskConfigOps;
import com.peaceful.task.core.controller.RedisCloudTaskController;
import com.peaceful.task.core.dispatch.TaskBeanFactoryImpl;
import com.peaceful.task.core.dispatch.TaskDispatcherService;
import com.peaceful.task.core.monitor.CloudTaskCountMonitor;
import com.peaceful.task.core.queue.redis.RedisQueue;

/**
 * 按照加载顺序装配各个模块的实现
 * Created by wangjun on 16-8-27.
 */
public class TaskModule extends AbstractModule {

    @Override
    protected void configure() {
        // 初始化conf
        bind(TaskConfigOps.class).toInstance(TaskConfService.startUp());
        // 绑定任务控制器
        bind(TaskController.class).to(RedisCloudTaskController.class).asEagerSingleton();
        // 绑定任务编码器
        bind(TaskCoding.class).to(TaskCodingService.class).asEagerSingleton();
        // 绑定任务存储器
        bind(TaskQueue.class).to(RedisQueue.class).asEagerSingleton();
        // 绑定beanactory
        bind(TaskBeanFactory.class).to(TaskBeanFactoryImpl.class).asEagerSingleton();
        // 装绑定任务监控器
        bind(TaskMonitor.class).to(CloudTaskCountMonitor.class).asEagerSingleton();
        // 绑定任务代理器
        bind(TaskProxy.class).to(TaskProxyByCglib.class).asEagerSingleton();
        // 绑定任务调度器
        bind(TaskDispatcher.class).to(TaskDispatcherService.class).asEagerSingleton();
    }
}
