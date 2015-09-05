package com.peaceful.task.container.exception;

import com.peaceful.task.container.msg.Task2;

/**
 * @author WangJun <wangjuntytl@163.com>
 * @version 1.0 15/8/29
 * @since 1.6
 */

public class TaskUnitException extends RuntimeException {

    public TaskUnitException(Task2 task2) {
        super("illegal task2 unit , " + task2);
    }
}
