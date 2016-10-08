package com.peaceful.task.kernal.coding.decoding;

import java.lang.reflect.Type;

/**
 * @author WangJun <wangjuntytl@163.com>
 * @version 1.0 15/8/28
 * @since 1.6
 */

public class NotSupportParamType extends RuntimeException {

    public NotSupportParamType(Class zClass){
        super("not support paramType "+zClass);
    }

    public NotSupportParamType(Type type){
        super("not support paramType "+type);
    }
}
