package com.peaceful.task.context.error;

/**
 * @author WangJun
 * @version 1.0 16/4/1
 */
public class IllegalTaskStateException extends RuntimeException{

    public IllegalTaskStateException(String msg){
        super(msg);
    }

}
