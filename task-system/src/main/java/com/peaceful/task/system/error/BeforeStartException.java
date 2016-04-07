package com.peaceful.task.system.error;

/**
 * Created by wangjun on 16/1/11.
 */
public class BeforeStartException extends RuntimeException {

    public BeforeStartException(String msg) {
        super(msg);
    }
}
