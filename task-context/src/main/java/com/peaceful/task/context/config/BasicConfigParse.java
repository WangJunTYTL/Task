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
        taskConfigOps.name = config.getString("task.name");
        taskConfigOps.bootMode = config.getString("task.boot-mode");
        taskConfigOps.developMode = config.getString("task.develop-mode");
        if (config.hasPath("task.queue")){
            taskConfigOps.queue = config.getString("task.queue");
        }else{
            taskConfigOps.queue = "com.peaceful.task.queue.redis.RedisQueue";
        }
        if (config.hasPath("task.bean-factory")){
            taskConfigOps.beanFactory = config.getString("task.bean-factory");
        }else{
            taskConfigOps.beanFactory = "com.peaceful.task.context.dispatch.TaskBeanFactoryImpl";
        }


        taskConfigOps.beanFactory = config.getString("task.bean-factory");
        return CONTINUE_PROCESSING;
    }
}
