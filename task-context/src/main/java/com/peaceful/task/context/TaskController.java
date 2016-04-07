package com.peaceful.task.context;

import com.peaceful.task.context.config.Executor;
import com.peaceful.task.context.dispatch.Task;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author WangJun
 * @version 1.0 16/3/30
 */
public interface TaskController {

    List<Executor> EXECUTOR_LIST = new ArrayList<Executor>();
    // 此处必须使用线程安全的集合,因为需要在多线程下操作
    Map<String, Task> TASK_HISTORY_LIST = new ConcurrentHashMap<String, Task>();
    Map<String, Task> TASK_LIST = new ConcurrentHashMap<String, Task>();

    /**
     * 获取所有的任务
     * @return
     */
    Collection<Task> findAllTasks();

    /**
     * 加入任务
     * @param name
     */
    void insertTask(String name);


    void removeTask(Task task);

    /**
     *  获取节点可以调度的任务
     * @return
     */
    Collection<Task> findNeedDispatchTasks();

    /**
     * 获取所有的
     * @return
     */
    Collection<Executor> findAllExecutors();


}
