package com.peaceful.task.system;

import com.peaceful.common.util.chain.BaseChain;
import com.peaceful.common.util.chain.Context;
import com.peaceful.task.context.TaskContext;
import com.peaceful.task.executor.ExecutorModule;

/**
 * Task启动之后加载模块
 *
 * @author <a href="mailto:wangjuntytl@163.com">WangJun</a>
 * @version 1.0 16/1/10
 */
public class SystemPostStart extends BaseChain {

    private SystemPostStart() {

    }

    public static SystemPostStart getSingleInstance() {
        return Single.chain;
    }

    public static class Single {
        public static SystemPostStart chain = new SystemPostStart();
    }

    public boolean execute(TaskContext context) throws Exception {
        return super.execute(context);
    }

    static {
        // load executor module
        Single.chain.addCommand(new ExecutorModule());

    }
}
