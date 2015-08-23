package cn.edaijia.task.container.exception;

/**
 * 如果提交任务到指定的队列，如果队列未找到则抛出
 *
 * @author WangJun <wangjuntytl@163.com>
 * @version 1.0 15/5/15
 * @since 1.6
 */

public class NotFindQueueException extends RuntimeException {

    public NotFindQueueException(String queueName) {
        super("not find queue name is " + queueName);
    }
}
