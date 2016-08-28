package com.peaceful.task.core.dispatch;

import com.google.common.base.Throwables;
import com.google.common.util.concurrent.AbstractScheduledService;
import com.google.inject.Inject;
import com.peaceful.task.core.Task;
import com.peaceful.task.core.TaskController;
import com.peaceful.task.core.TaskDispatcher;
import com.peaceful.task.core.coding.TUR;
import com.peaceful.task.core.conf.TaskConfigOps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.concurrent.TimeUnit;

/**
 * 定时从任务队列中拉取任务执行
 * Created by wangjun on 16-8-27.
 */
public class TaskDispatcherService extends AbstractScheduledService implements TaskDispatcher {

    @Inject
    private TaskController controller;
    @Inject
    private NoBlockAutoDispatch noBlockAutoDispatch;
    @Inject
    private TaskConfigOps ops;

    private PullTask pullTask;
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Inject
    public TaskDispatcherService() {
        this.startAsync();
        logger.info("dispatcher service start suc...");
    }

    @Override
    public void dispatch() {
        try {
            if (noBlockAutoDispatch.state() == State.NEW || noBlockAutoDispatch.state() == State.STARTING) {
                noBlockAutoDispatch.startAsync();
                noBlockAutoDispatch.awaitRunning();
                pullTask = PullTask.get(Task.getTaskContext(ops.name));
            }
            Collection<TaskMeta> taskMetas = controller.findNeedDispatchTasks();
            if (taskMetas != null && !taskMetas.isEmpty()) {
                for (TaskMeta meta : taskMetas) {
                    TUR unit = pullTask.next(meta.name);
                    if (unit != null) {
                        if (noBlockAutoDispatch.isRunning()) {
                            noBlockAutoDispatch.tell(unit);
                        } else {
                            logger.error("dispatcher service not running,current state is {}", noBlockAutoDispatch.state());
                        }
                    }
                }
            }
        } catch (Exception e) {
            // dispatch 失败，需要记录日志
            logger.error("task dispatch exception:{}", Throwables.getStackTraceAsString(e));
        }
    }

    @Override
    protected void runOneIteration() throws Exception {
        dispatch();
    }

    @Override
    protected Scheduler scheduler() {
        // TODO: 16-8-27 可配置的调度周期
        logger.info("Start dispatch service...");
        return Scheduler.newFixedRateSchedule(1, 1, TimeUnit.SECONDS);
    }
}
