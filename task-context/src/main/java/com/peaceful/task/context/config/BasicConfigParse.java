package com.peaceful.task.context.config;

import com.peaceful.task.context.common.ContextConstant;
import com.peaceful.task.context.SimpleTaskContext;
import com.peaceful.common.util.chain.Command;
import com.peaceful.common.util.chain.Context;
import com.typesafe.config.Config;

/**
 * 基本配置信息解析
 * <p/>
 * Created by wangjun on 16/1/12.
 */
public class BasicConfigParse implements Command {

    @Override
    public boolean execute(Context context) throws Exception {
        TaskConfigOps taskConfigOps = (TaskConfigOps) SimpleTaskContext.CONTEXT.get(ContextConstant.CONFIG);
        Config config = (Config) context.get("config");
        taskConfigOps.name = config.getString("task-system.name");
        taskConfigOps.bootMode = config.getString("task-system.boot-mode");
        taskConfigOps.developMode = config.getString("task-system.develop-mode");
        return CONTINUE_PROCESSING;
    }
}
