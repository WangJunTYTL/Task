package com.peaceful.task.context;

import com.peaceful.task.context.dispatch.TaskUnit;

import java.util.Collection;

/**
 * 任务调度入口
 *
 * @author WangJun
 * @version 1.0 16/3/31
 */
public interface TaskDispatch {

    void dispatch(Collection<TaskUnit> tasks);
}
