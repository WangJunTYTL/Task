package com.peaceful.task.container.invoke.args.impl;

import com.peaceful.task.container.invoke.InvokeContext;
import com.peaceful.task.container.invoke.args.Parse;
import com.peaceful.task.container.invoke.chain.Context;

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
            return true;
        }
        return false;
    }
}
