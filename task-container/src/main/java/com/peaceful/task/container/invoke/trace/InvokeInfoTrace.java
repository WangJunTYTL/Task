package com.peaceful.task.container.invoke.trace;

import com.peaceful.task.container.invoke.InvokeContext;
import com.peaceful.task.container.invoke.chain.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 跟踪调用信息，这是是一个后跟踪{@link InvokeTrace#addEndTrace(Trace)}
 *
 * @author WangJun <wangjuntytl@163.com>
 * @version 1.0 15/8/28
 * @since 1.6
 */

public class InvokeInfoTrace implements Trace {

    Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public boolean execute(Context context) throws Exception {
        InvokeContext invokeContext = (InvokeContext) context;
        if (invokeContext.get(InvokeContext.START_TIME) != null) {
            long start = Long.valueOf((String) invokeContext.get(InvokeContext.START_TIME));
            logger.info("complete task {} {} cost time {}",
                    invokeContext.get(InvokeContext.TASK_ID),
                    invokeContext.aClass + "." + invokeContext.methodName ,
                    System.currentTimeMillis() - start);
        }
        return false;
    }
}
