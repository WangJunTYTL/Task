package com.peaceful.task.kernal;

import com.google.inject.AbstractModule;
import com.peaceful.task.kernal.client.TaskClientService;
import com.peaceful.task.kernal.client.TaskProxyByCglib;
import com.peaceful.task.kernal.coding.TaskCodingService;
import com.peaceful.task.kernal.conf.TaskConfService;
import com.peaceful.task.kernal.conf.TaskConfigOps;
import com.peaceful.task.kernal.controller.RedisCloudTaskController;
import com.peaceful.task.kernal.dispatch.TaskBeanFactoryImpl;
import com.peaceful.task.kernal.dispatch.TaskDispatcherService;
import com.peaceful.task.kernal.monitor.CloudTaskCountMonitor;
import com.peaceful.task.kernal.queue.redis.RedisQueue;

/**
 * Task Mail Module load
 * <p>
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
        // 绑定beanFactory
        bind(TaskBeanFactory.class).to(TaskBeanFactoryImpl.class).asEagerSingleton();
        // 装绑定任务监控器
        bind(TaskMonitor.class).to(CloudTaskCountMonitor.class).asEagerSingleton();
        // 绑定Task Client
        bind(TaskClient.class).to(TaskClientService.class).asEagerSingleton();
        // 绑定任务代理器
        bind(TaskProxy.class).to(TaskProxyByCglib.class).asEagerSingleton();
        // 绑定任务调度器
        bind(TaskDispatcher.class).to(TaskDispatcherService.class).asEagerSingleton();
    }
}
