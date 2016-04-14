package com.peaceful.task.context.config;

import com.peaceful.task.context.common.ContextConstant;
import com.peaceful.common.util.chain.Command;
import com.peaceful.common.util.chain.Context;
import com.peaceful.task.context.SimpleTaskContext;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigObject;

import java.util.List;

/**
 * executor 模块配置解析
 * <p/>
 * Created by wangjun on 16/1/12.
 */
public class ExecutorConfigParse implements Command {

    @Override
    public boolean execute(Context context) throws Exception {
        TaskConfigOps taskConfigOps = (TaskConfigOps) SimpleTaskContext.CONTEXT.get(ContextConstant.CONFIG);
        Config config = (Config) context.get("config");
        List<? extends ConfigObject> executors = config.getObjectList("task.executor");
        for (ConfigObject object : executors) {
            Executor executorNode = new Executor();
            executorNode.name = object.get("name").unwrapped().toString();
            executorNode.implementation = object.get("implementation").unwrapped().toString();
            executorNode.Class = Thread.currentThread().getContextClassLoader().loadClass(executorNode.implementation);
            taskConfigOps.executorConfigOps.executorNodeList.add(executorNode);
        }
        return CONTINUE_PROCESSING;
    }
}
