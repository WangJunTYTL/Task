package com.peaceful.task.context;

/**
 * 任务的最终执行者
 *
 * @author <a href="mailto:wangjuntytl@163.com">WangJun</a>
 * @version 1.0 16/1/11
 */
public interface TaskExecutor {

    void execute(Runnable task);

}
