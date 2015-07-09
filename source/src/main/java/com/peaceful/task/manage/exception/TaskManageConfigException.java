package com.peaceful.task.manage.exception;

/**
 * @author WangJun <wangjuntytl@163.com>
 * @version 1.0 15/5/20
 * @since 1.6
 */

public class TaskManageConfigException extends RuntimeException {

    public TaskManageConfigException(String msg){
        super(msg);
    }
    public TaskManageConfigException(Throwable e){
        super(e);
    }
}
