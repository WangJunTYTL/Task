package com.peaceful.task.system;

import com.peaceful.common.util.chain.BaseChain;
import com.peaceful.common.util.chain.Context;
import com.peaceful.task.context.ContextModule;
import com.peaceful.task.context.TaskContext;

/**
 * // Task容器启动过程中加载模块
 *
 * @author <a href="mailto:wangjuntytl@163.com">WangJun</a>
 * @version 1.0 16/1/10
 */
public class SystemPreStart extends BaseChain {

    private SystemPreStart() {

    }

    public boolean execute(TaskContext context) throws Exception {
        return super.execute((Context) context);
    }

    public static SystemPreStart getSingleInstance() {
        return Single.chain;
    }

    public static class Single {
        public static SystemPreStart chain = new SystemPreStart();
    }

    static {
        // load context
        Single.chain.addCommand(new ContextModule());
//        Single.chain.addCommand(new ExecutorModule());
        // load console
//        Single.chain.addCommand(new LoadConsole());
        // load coding
//        Single.chain.addCommand(new CodingModule());
        // load queue
//        Single.chain.addCommand(new LoadQueue());
    }
}
