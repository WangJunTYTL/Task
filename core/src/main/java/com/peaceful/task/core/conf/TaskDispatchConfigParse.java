package com.peaceful.task.core.conf;

import com.peaceful.common.util.chain.Command;
import com.peaceful.common.util.chain.Context;
import com.typesafe.config.Config;

/**
 * 基本配置信息解析
 * <p/>
 * Created by wangjun on 16/1/12.
 */
public class TaskDispatchConfigParse implements Command {

    @Override
    public boolean execute(Context context) throws Exception {
        TaskConfigOps taskConfigOps = (TaskConfigOps) context.get("task.conf");
        Config config = (Config) context.get("conf");
        return CONTINUE_PROCESSING;
    }
}
