package com.peaceful.task.container.exception;

/**
 * load taskContainer.conf error
 *
 * @author WangJun <wangjuntytl@163.com>
 * @version 1.0 15/5/20
 * @since 1.6
 */

public class TaskContainerConfigException extends RuntimeException {

    public TaskContainerConfigException(String msg){
        super(msg);
    }
    public TaskContainerConfigException(Throwable e){
        super(e);
    }
}
