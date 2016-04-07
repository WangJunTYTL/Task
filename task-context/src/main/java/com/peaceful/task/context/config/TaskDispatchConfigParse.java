package com.peaceful.task.context.config;

import com.peaceful.task.context.common.ContextConstant;
import com.peaceful.task.context.SimpleTaskContext;
import com.peaceful.common.util.chain.Command;
import com.peaceful.common.util.chain.Context;
import com.typesafe.config.Config;

import java.util.concurrent.TimeUnit;

/**
 * 基本配置信息解析
 * <p/>
 * Created by wangjun on 16/1/12.
 */
public class TaskDispatchConfigParse implements Command {

    @Override
    public boolean execute(Context context) throws Exception {
        TaskConfigOps taskConfigOps = (TaskConfigOps) SimpleTaskContext.CONTEXT.get(ContextConstant.CONFIG);
        Config config = (Config) context.get("config");
        taskConfigOps.dispatchTick = config.getDuration("task-system.dispatch-tick", TimeUnit.MILLISECONDS);
        return CONTINUE_PROCESSING;
    }
}
