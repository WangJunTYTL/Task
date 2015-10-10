package com.peaceful.task.container.invoke.excute;

import com.peaceful.task.container.invoke.InvokeContext;
import com.peaceful.task.container.invoke.args.NotSupportParamType;
import com.peaceful.task.container.invoke.args.ParseChain;
import com.peaceful.task.container.invoke.args.impl.*;
import com.peaceful.task.container.invoke.chain.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 调用参数解析
 *
 * @author WangJun <wangjuntytl@163.com>
 * @version 1.0 15/8/28
 * @since 1.6
 */

public class ArgsHandler implements Handler {

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


    @Override
    public boolean execute(Context context) throws Exception {
        InvokeContext invokeContext = (InvokeContext) context;
        for (int i = 0; i < invokeContext.length; i++) {
            if (parseChain.execute(invokeContext)) {
                logger.debug("paramType {} value {}", invokeContext.getCurrentParamType().getName(), invokeContext.getCurrentArg());
            } else {
                throw new NotSupportParamType(invokeContext.getCurrentParamType());
            }
        }
        return false;
    }
}
