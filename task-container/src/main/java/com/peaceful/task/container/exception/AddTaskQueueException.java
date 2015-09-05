package com.peaceful.task.container.exception;

/**
 * commit task to taskContainer exception
 *
 * @author WangJun <wangjuntytl@163.com>
 * @version 1.0 15/5/15
 * @since 1.6
 */

public class AddTaskQueueException extends RuntimeException {

    public AddTaskQueueException(String msg, Throwable e) {
        super("add queue task error " + msg, e);
    }
}
