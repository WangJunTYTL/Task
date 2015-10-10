package com.peaceful.task.container.exception;

/**
 * 如果提交固定任务时，如果任务队列未找到则抛出该异常
 *
 * @author WangJun <wangjuntytl@163.com>
 * @version 1.0 15/5/15
 * @since 1.6
 */

public class NotFindQueueException extends RuntimeException {

    public NotFindQueueException(String queueName) {
        super("not find task queue name is " + queueName);
    }
}
