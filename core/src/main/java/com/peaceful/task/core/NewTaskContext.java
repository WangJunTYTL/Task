package com.peaceful.task.core;

import com.google.inject.Injector;
import com.peaceful.task.core.conf.TaskConfigOps;

/**
 * Task主要模块，系统使用成功后可以使用该context
 * Created by wangjun on 16-8-27.
 */
public class NewTaskContext implements TaskContext {

    private Injector injector;

    public NewTaskContext(Injector injector) {
        this.injector = injector;
    }

    @Override
    public TaskConfigOps getConfigOps() {
        return injector.getInstance(TaskConfigOps.class);
    }

    @Override
    public TaskQueue getTaskQueue() {
        return injector.getInstance(TaskQueue.class);
    }

    @Override
    public TaskController getTaskController() {
        return injector.getInstance(TaskController.class);
    }

    @Override
    public TaskProxy getTaskProxy() {
        return injector.getInstance(TaskProxy.class);
    }

    @Override
    public TaskDispatcher getTaskDispatcher() {
        return injector.getInstance(TaskDispatcher.class);
    }

    @Override
    public TaskBeanFactory getTaskBeanFactory() {
        return injector.getInstance(TaskBeanFactory.class);
    }

    @Override
    public TaskCoding getTaskCoding() {
        return injector.getInstance(TaskCoding.class);
    }

    @Override
    public TaskMonitor getTaskMonitor() {
        return injector.getInstance(TaskMonitor.class);
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
