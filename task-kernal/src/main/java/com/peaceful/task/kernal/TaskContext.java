package com.peaceful.task.kernal;

import com.peaceful.task.kernal.conf.TaskConfigOps;

/**
 * Created by wangjun on 16-8-27.
 */
public interface TaskContext {

    /**
     * @return 任务系统配置
     */
    TaskConfigOps getConfigOps();
    /**
     * @return 任务存储器
     */
    TaskQueue getTaskQueue();

    /**
     * @return 任务控制器
     */
    TaskController getTaskController();

    /**
     * @return 任务代理器
     */
    TaskProxy getTaskProxy();

    /**
     * @return 任务调度器
     */
    TaskDispatcher getTaskDispatcher();

    TaskBeanFactory getTaskBeanFactory();

    /**
     * @return 任务编码器
     */
    TaskCoding getTaskCoding();

    /**
     * @return 任务监视器
     */
    TaskMonitor getTaskMonitor();



}
