package com.peaceful.task.container.invoke.args.impl;

import com.peaceful.task.container.invoke.InvokeContext;
import com.peaceful.task.container.invoke.args.Parse;
import com.peaceful.task.container.invoke.chain.Context;

/**
 * Primitive类型参数解析 {@link Class#isPrimitive()}
 *
 * 下面这些类型虽然也是Primitive类型，但这些类型的解析，需要单独考虑，请注意{@link EmptyParse}
 * char
 * short
 * byte
 * float
 *
 * @author WangJun <wangjuntytl@163.com>
 * @version 1.0 15/8/27
 * @since 1.6
 */

public class PrimitiveParse implements Parse {

    @Override
    public boolean execute(Context context) throws Exception {
        InvokeContext invokeContext = (InvokeContext) context;
        if (invokeContext.parameterTypes[invokeContext.index].isPrimitive()) {
            invokeContext.newArgs[invokeContext.index] = invokeContext.args[invokeContext.index];
            invokeContext.index++;
            return true;
        }
        return false;
    }
}
