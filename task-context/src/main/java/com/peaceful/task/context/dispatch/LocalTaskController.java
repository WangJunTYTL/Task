package com.peaceful.task.context.dispatch;

import com.peaceful.task.context.common.ContextConstant;
import com.peaceful.task.context.config.Executor;
import com.peaceful.task.context.config.TaskConfigOps;
import com.peaceful.task.context.error.IllegalTaskStateException;
import com.peaceful.task.context.helper.NetHelper;
import com.peaceful.common.util.ExceptionUtils;
import com.peaceful.task.context.SimpleTaskContext;
import com.peaceful.task.context.TaskController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author WangJun
 * @version 1.0 16/3/30
 */
public class LocalTaskController implements TaskController {


    Map<String, TaskUnit> taskLockList = new LinkedHashMap<String, TaskUnit>();
    private final static TaskConfigOps ops = (TaskConfigOps) SimpleTaskContext.CONTEXT.get(ContextConstant.CONFIG);
    private final static String name = ops.name;

    private Logger logger = LoggerFactory.getLogger(getClass());

    static {
        // 获取所有的executor
        TaskConfigOps ops = (TaskConfigOps) SimpleTaskContext.CONTEXT.get(ContextConstant.CONFIG);
        for (Executor e : ops.executorConfigOps.executorNodeList) {
            EXECUTOR_LIST.add(e);
        }

        ScheduledExecutorService executorService = (ScheduledExecutorService) SimpleTaskContext.CONTEXT.get(ContextConstant.PUBLICE_SCHEDULE);
        executorService.scheduleAtFixedRate(new ScanTask(), 0, 5, TimeUnit.MINUTES);

    }


    @Override
    public Collection<TaskUnit> findAllTasks() {
        Collection<TaskUnit> tasks = new ArrayList<TaskUnit>();
        tasks.addAll(TASK_HISTORY_LIST.values());
        //后加入未过期任务,可以覆盖过期任务
        tasks.addAll(TASK_LIST.values());
        return tasks;
    }

    @Override
    public void insertTask(String name) {
        if (TASK_LIST.containsKey(name)) {
            TaskUnit task = TASK_LIST.get(name);
            task.updateTime = System.currentTimeMillis();
        } else {
            TaskUnit task = new TaskUnit(name);
            task.createTime = System.currentTimeMillis();
            task.updateTime = System.currentTimeMillis();
            TASK_LIST.put(task.name, task);
        }
    }

    @Override
    public void removeTask(TaskUnit task) {
        TASK_LIST.remove(task.name);
    }

    @Override
    public Collection<TaskUnit> findNeedDispatchTasks() {
        Set<TaskUnit> tasks = new HashSet<TaskUnit>();
        for (TaskUnit task : TASK_LIST.values()) {
            if (task.state.equals("isLock")) {
                // pass
            } else if (task.state.equals("OK")) {
                tasks.add(task);
            } else if (task.state.equals(NetHelper.getHostname())) {
                tasks.add(task);
            } else {
                throw new IllegalTaskStateException("state " + task.state + " is illegal");
            }
        }
        return tasks;
    }

    @Override
    public Collection<Executor> findAllExecutors() {
        return EXECUTOR_LIST;
    }

    static class ScanTask implements Runnable {

        private Logger logger = LoggerFactory.getLogger(getClass());

        @Override
        public void run() {
            try {
                // 清理过期的任务,
                // 过期逻辑是:如果一个任务的updateTime长时间未被更新且任务所属队列为空,则说明该任务已经过期
                // 如果过期任务被再次提交,任务会进入到可能被调度列表,但此时过期列表也存在,为了避免疑惑,则从过期列表中清除
                for (TaskUnit task : TASK_LIST.values()) {
                    if ((task.updateTime + TimeUnit.MINUTES.toMillis(5)) < System.currentTimeMillis()) {
                        if (SimpleTaskContext.QUEUE.size("TASK-" + name + "-" + task.name) == 0) {
                            TASK_LIST.remove(task.name);
                            task.expire = true;
                            TASK_HISTORY_LIST.put(task.name, task);
                        }
                    }
                }
                for (TaskUnit task : TASK_HISTORY_LIST.values()) {
                    if (SimpleTaskContext.QUEUE.size("TASK-" + name + "-" + task.name) != 0) {
                        TASK_HISTORY_LIST.remove(task.name);
                        task.expire = false;
                        task.updateTime = System.currentTimeMillis();
                        TASK_LIST.put(task.name, task);
                    }
                }
            } catch (Exception e) {
                logger.error("Error: {}", ExceptionUtils.getStackTrace(e));
            }

        }
    }


}
