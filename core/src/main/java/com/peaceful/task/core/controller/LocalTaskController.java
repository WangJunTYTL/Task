package com.peaceful.task.core.controller;

import com.peaceful.task.core.*;
import com.peaceful.task.core.dispatch.TaskMeta;
import com.peaceful.task.core.conf.Executor;
import com.peaceful.task.core.conf.TaskConfigOps;
import com.peaceful.task.core.helper.NetHelper;
import com.peaceful.common.util.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 本地任务调度控制模块
 *
 * @author WangJun
 * @version 1.0 16/3/30
 */
public class LocalTaskController implements TaskController {


    private Logger logger = LoggerFactory.getLogger(getClass());

    private TaskConfigOps ops;
    private TaskQueue queue;

    public LocalTaskController(TaskConfigOps ops, TaskQueue queue) {
        this.ops = ops;
        this.queue = queue;
        // 启动时获取所有的executor
        for (Executor e : ops.executorConfigOps.executorNodeList) {
            EXECUTOR_LIST.add(e);
        }
        // // TODO: 16-8-27 线程池统一
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(8);
        // 观察任务的执行情况，在两个任务容器中进行切换
        executorService.scheduleAtFixedRate(new ObserveTask(), 0, 5, TimeUnit.MINUTES);
        // 清理过期的任务
        executorService.scheduleAtFixedRate(new ClearExpireTask(), 0, 8, TimeUnit.HOURS);
    }


    @Override
    public Collection<TaskMeta> findAllTasks() {
        Collection<TaskMeta> tasks = new ArrayList<TaskMeta>();
        tasks.addAll(TASK_HISTORY_LIST.values());
        //后加入未过期任务,可以覆盖过期任务
        tasks.addAll(TASK_LIST.values());
        return tasks;
    }

    @Override
    public void insertTask(String name) {
        if (TASK_LIST.containsKey(name)) {
            TaskMeta task = TASK_LIST.get(name);
            task.updateTime = System.currentTimeMillis();
        } else {
            TaskMeta task = new TaskMeta(name);
            task.createTime = System.currentTimeMillis();
            task.updateTime = System.currentTimeMillis();
            TASK_LIST.put(task.name, task);
        }
    }

    @Override
    public void removeTask(TaskMeta task) {
        TASK_LIST.remove(task.name);
    }

    @Override
    public Collection<TaskMeta> findNeedDispatchTasks() {
        Set<TaskMeta> tasks = new HashSet<TaskMeta>();
        for (TaskMeta task : TASK_LIST.values()) {
            if (task.state.equals("isLock")) {
                // pass
            } else if (task.state.equals("OK")) {
                tasks.add(task);
            } else if (task.state.equals(NetHelper.getHostname())) {
                tasks.add(task);
            } else {
                // ignore
            }
        }
        return tasks;
    }

    @Override
    public Collection<Executor> findAllExecutors() {
        return EXECUTOR_LIST;
    }

    /**
     * 观察任务的调度情况，如果任务的队列为空，则认为任务已经完成，将放入到任务可能完成的容器，如果任务队列不为空，则放入到正在调度的容器
     */
    private  class ObserveTask implements Runnable {

        private Logger logger = LoggerFactory.getLogger(getClass());

        @Override
        public void run() {
            try {
                // 清理过期的任务,
                // 过期逻辑是:如果一个任务的updateTime长时间未被更新且任务所属队列为空,则说明该任务已经过期
                // 如果过期任务被再次提交,任务会进入到可能被调度列表,但此时过期列表也存在,为了避免疑惑,则从过期列表中清除
                for (TaskMeta meta : TASK_LIST.values()) {
                    if ((meta.updateTime + TimeUnit.MINUTES.toMillis(5)) < System.currentTimeMillis()) {
                        if (queue.size("TASK-" + ops.name + "-" + meta.name) == 0) {
                            TASK_LIST.remove(meta.name);
                            meta.expire = true;
                            TASK_HISTORY_LIST.put(meta.name, meta);
                        }
                    }
                }
                for (TaskMeta meta : TASK_HISTORY_LIST.values()) {
                    if (queue.size("TASK-" +  ops.name+ "-" + meta.name) != 0) {
                        TASK_HISTORY_LIST.remove(meta.name);
                        meta.expire = false;
                        meta.updateTime = System.currentTimeMillis();
                        TASK_LIST.put(meta.name, meta);
                    }
                }
            } catch (Exception e) {
                logger.error("Error: {}", ExceptionUtils.getStackTrace(e));
            }
        }
    }

    /**
     * 清理7天都没有更新的任务
     */
    private  class ClearExpireTask implements Runnable {
        private Logger logger = LoggerFactory.getLogger(getClass());

        @Override
        public void run() {
            try {
                for (TaskMeta meta : TASK_HISTORY_LIST.values()) {
                    if ((System.currentTimeMillis() - meta.updateTime) / 1000 / 60 / 60 > 24 * 7) {
                        TASK_HISTORY_LIST.remove(meta.name);
                        logger.info("task->{} will be removed,for more than 30 days no update！", meta.name);
                    }
                }
            } catch (Exception e) {
                logger.error("Error: {}", ExceptionUtils.getStackTrace(e));
            }
        }
    }


}
