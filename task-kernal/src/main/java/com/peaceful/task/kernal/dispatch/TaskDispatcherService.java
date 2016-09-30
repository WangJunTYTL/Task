package com.peaceful.task.kernal.dispatch;

import com.google.common.base.Throwables;
import com.google.common.util.concurrent.AbstractScheduledService;
import com.google.inject.Inject;
import com.peaceful.task.kernal.Task;
import com.peaceful.task.kernal.TaskController;
import com.peaceful.task.kernal.TaskDispatcher;
import com.peaceful.task.kernal.coding.TUR;
import com.peaceful.task.kernal.conf.TaskConfigOps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.concurrent.TimeUnit;

/**
 * 定时从任务队列中拉取任务消息
 * Created by wangjun on 16-8-27.
 */
public class TaskDispatcherService extends AbstractScheduledService implements TaskDispatcher {

    @Inject
    private TaskController controller;
    @Inject
    private NoBlockAutoConsumer noBlockAutoDispatch;
    @Inject
    private TaskConfigOps ops;
    private PullTask pullTask;
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Inject
    public TaskDispatcherService() {
        this.startAsync();
    }

    @Override
    public void dispatch() {
        try {
            if (noBlockAutoDispatch.state() == State.NEW || noBlockAutoDispatch.state() == State.STARTING) {
                noBlockAutoDispatch.startAsync();
                noBlockAutoDispatch.awaitRunning();
                pullTask = PullTask.get(Task.getTaskContext(ops.name));
            }
            // 从controller中获取需要调度的任务
            Collection<TaskMeta> taskMetas = controller.findNeedDispatchTasks();
            if (taskMetas == null && taskMetas.isEmpty()) {
                return;
            }
            for (TaskMeta meta : taskMetas) {
                TUR unit = pullTask.next(meta.name);
                if (unit != null) {
                    if (noBlockAutoDispatch.isRunning()) {
                        noBlockAutoDispatch.tell(unit);
                    } else {
                        logger.error("dispatcher service not running,current state->{},task->{} ", noBlockAutoDispatch.state(),unit);
                    }
                }
            }
        } catch (Exception e) {
            // dispatch 失败，需要记录日志,关键性日志
            logger.error("dispatch exception:{}", Throwables.getStackTraceAsString(e));
        }
    }

    @Override
    protected void runOneIteration() throws Exception {
        dispatch();
    }

    @Override
    protected Scheduler scheduler() {
        return Scheduler.newFixedRateSchedule(1, 1, TimeUnit.SECONDS);
    }
}
