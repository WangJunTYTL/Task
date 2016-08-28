package com.peaceful.task.core.coding.decoding.impl;


import com.peaceful.common.util.chain.Context;
import com.peaceful.task.core.coding.decoding.InvokeContext;
import com.peaceful.task.core.coding.decoding.Parse;

/**
 * Integer类型参数解析
 *
 * @author WangJun <wangjuntytl@163.com>
 * @version 1.0 15/8/27
 * @since 1.6
 */

public class IntegerParse implements Parse {

    @Override
    public boolean execute(Context context) throws Exception {
        InvokeContext invokeContext = (InvokeContext) context;
        if (invokeContext.parameterTypes[invokeContext.index].equals(Integer.class)) {
            invokeContext.newArgs[invokeContext.index] = Integer.valueOf(invokeContext.args[invokeContext.index].toString());
            invokeContext.index++;
            return PROCESSING_COMPLETE;
        }
        return CONTINUE_PROCESSING;
    }
}
