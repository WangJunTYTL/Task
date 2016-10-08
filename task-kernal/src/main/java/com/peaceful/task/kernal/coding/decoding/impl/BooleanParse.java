package com.peaceful.task.kernal.coding.decoding.impl;


import com.peaceful.common.util.chain.Context;
import com.peaceful.task.kernal.coding.decoding.InvokeContext;
import com.peaceful.task.kernal.coding.decoding.Parse;

/**
 * Boolean类型参数解析
 *
 * @author WangJun <wangjuntytl@163.com>
 * @version 1.0 15/8/27
 * @since 1.6
 */

public class BooleanParse implements Parse {

    @Override
    public boolean execute(Context context) throws Exception {
        InvokeContext invokeContext = (InvokeContext) context;
        if (invokeContext.parameterTypes[invokeContext.index].equals(Boolean.class)) {
            invokeContext.newArgs[invokeContext.index] = Boolean.valueOf(invokeContext.args[invokeContext.index].toString());
            invokeContext.index++;
            return PROCESSING_COMPLETE;
        }
        return CONTINUE_PROCESSING;
    }
}
