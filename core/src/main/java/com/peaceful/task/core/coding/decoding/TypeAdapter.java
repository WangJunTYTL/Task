package com.peaceful.task.core.coding.decoding;

import com.google.inject.Singleton;
import com.peaceful.common.util.chain.Context;
import com.peaceful.task.core.coding.decoding.impl.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 对Task进行解码,并包装成TaskUnit对象,提供给Executor执行
 *
 * @author WangJun <wangjuntytl@163.com>
 * @version 1.0 15/8/28
 * @since 1.6
 */
@Singleton
public class TypeAdapter {

    Logger logger = LoggerFactory.getLogger(getClass());

    private static ParseChain parseChain;

    static {
        parseChain = new ParseChain();
        parseChain.addCommand(new NullParse());
        parseChain.addCommand(new EmptyParse());
        parseChain.addCommand(new PrimitiveParse());
        parseChain.addCommand(new StringParse());
        parseChain.addCommand(new IntegerParse());
        parseChain.addCommand(new BooleanParse());
        parseChain.addCommand(new LongParse());
        parseChain.addCommand(new DoubleParse());
        parseChain.addCommand(new ListParse());
        parseChain.addCommand(new MapParse());
        parseChain.addCommand(new SetParse());
        parseChain.addCommand(new UserDefinedParse());
    }

    public InvokeContext execute(Context context) throws Exception {
        InvokeContext invokeContext = (InvokeContext) context;
        for (int i = 0; i < invokeContext.length; i++) {
            if (parseChain.execute(invokeContext)) {
                logger.debug("{} param[{}] {} {}", invokeContext.aClass.getSimpleName()+"."+invokeContext.methodName,i,invokeContext.getCurrentParamType(), invokeContext.getCurrentArg());
            } else {
                throw new NotSupportParamType(invokeContext.getCurrentParamType());
            }
        }
        return invokeContext;
    }
}
