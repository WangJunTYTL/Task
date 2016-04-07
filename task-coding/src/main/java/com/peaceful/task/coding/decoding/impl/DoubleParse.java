package com.peaceful.task.coding.decoding.impl;


import com.peaceful.task.coding.decoding.InvokeContext;
import com.peaceful.task.coding.decoding.Parse;
import com.peaceful.common.util.chain.Context;

/**
 * Double类型参数解析
 *
 * @author WangJun <wangjuntytl@163.com>
 * @version 1.0 15/8/27
 * @since 1.6
 */

public class DoubleParse implements Parse {

    @Override
    public boolean execute(Context context) throws Exception {
        InvokeContext invokeContext = (InvokeContext) context;
        if (invokeContext.parameterTypes[invokeContext.index].equals(Double.class)) {
            invokeContext.newArgs[invokeContext.index] = Double.valueOf(invokeContext.args[invokeContext.index].toString());
            invokeContext.index++;
            return PROCESSING_COMPLETE;
        }
        return CONTINUE_PROCESSING;
    }
}
