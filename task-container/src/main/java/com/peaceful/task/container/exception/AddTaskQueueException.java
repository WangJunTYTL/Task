package com.peaceful.task.container.exception;

/**
 * commit task to taskContainer exception
 * <p/>
 * 发生该异常的原因，很有可能是任务存储中心模块存在问题，比如任务存储中心网络不通，服务有问题等等
 *
 * @author WangJun <wangjuntytl@163.com>
 * @version 1.0 15/5/15
 * @since 1.6
 */

public class AddTaskQueueException extends RuntimeException {

    public AddTaskQueueException(String msg, Throwable e) {
        super("commit task unit to task store error " + msg, e);
    }
}
