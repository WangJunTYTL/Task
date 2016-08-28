package com.peaceful.task.core;

import com.peaceful.task.core.conf.Executor;
import com.peaceful.task.core.dispatch.TaskMeta;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 任务控制器：可以在这里查看所有的任务生产消费情况,通过特定的API干预任务的执行
 *
 * @author WangJun
 * @version 1.0 16/3/30
 */
public interface TaskController {

    // 执行器列表
    List<Executor> EXECUTOR_LIST = new ArrayList<Executor>();
    // 可能已经完成的任务调度列表，此处必须使用线程安全的集合
    Map<String, TaskMeta> TASK_HISTORY_LIST = new ConcurrentHashMap<String, TaskMeta>();
    // 正在调度任务列表
    Map<String, TaskMeta> TASK_LIST = new ConcurrentHashMap<String, TaskMeta>();

    /**
     * 获取所有的任务，包括可能完成的任务与正在调度的任务
     *
     * @return
     */
    Collection<TaskMeta> findAllTasks();

    /**
     * 加入任务，如果任务已经存在，则更新任务的更新时间
     *
     * @param name
     */
    void insertTask(String name);


    void removeTask(TaskMeta task);

    /**
     * 获取本地可以调度的任务
     *
     * @return
     */
    Collection<TaskMeta> findNeedDispatchTasks();

    /**
     * 获取所有的任务执行器
     *
     * @return
     */
    Collection<Executor> findAllExecutors();


}
