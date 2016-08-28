package com.peaceful.task.core;

/**
 * 任务执行器
 *
 * @author <a href="mailto:wangjuntytl@163.com">WangJun</a>
 * @version 1.0 16/1/11
 */
public interface TaskExecutor {

    /**
     * 任务在某个节点被消费时，会被包装成一个Runnable对象，然后可以被Executor执行，或者直接调用起run方法执行
     *
     * @param task 一个实现Runnable接口的任务对象
     */
    void execute(Runnable task);

}
