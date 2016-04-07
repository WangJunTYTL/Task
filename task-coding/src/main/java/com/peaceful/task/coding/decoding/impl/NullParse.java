package com.peaceful.task.coding.decoding.impl;

import com.peaceful.task.coding.decoding.InvokeContext;
import com.peaceful.task.coding.decoding.Parse;
import com.peaceful.common.util.chain.Context;

/**
 * Null类型参数解析，该解析器的位置应该放置第一位
 *
 * @author WangJun <wangjuntytl@163.com>
 * @version 1.0 15/8/27
 * @since 1.6
 */

public class NullParse implements Parse {

    @Override
    public boolean execute(Context context) throws Exception {
        InvokeContext invokeContext = (InvokeContext) context;
        if (invokeContext.args[invokeContext.index] == null) {
            invokeContext.newArgs[invokeContext.index] = null;
            invokeContext.index++;
            return PROCESSING_COMPLETE;
        }
        return CONTINUE_PROCESSING;
    }
}
