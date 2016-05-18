package com.peaceful.task.context.config;

import com.peaceful.task.context.SimpleTaskContext;
import com.peaceful.task.context.error.SystemConfigException;
import com.peaceful.common.util.chain.Command;
import com.peaceful.common.util.chain.Context;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * load config file
 * <p>
 * Created by wangjun on 16/1/12.
 */
public class LoadConfigFileParse implements Command {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public boolean execute(Context context) throws Exception {
        // search config file
        String defaultPath = "task-reference.conf";
        Config config = ConfigFactory.load(defaultPath);
        String customerPath = "task.conf";
        String conf = System.getProperty("task.conf.file");
        if (StringUtils.isNotEmpty(conf)) customerPath = conf;
        if (LoadConfigFileParse.class.getClassLoader().getResource(customerPath) != null) {
            Config _config = ConfigFactory.load(customerPath);
            config = _config.withFallback(config);
        } else {
            logger.warn("you best to add  a task.conf file for your task system, reference the task-reference.conf");
        }
        context.put("config", config);
        TaskConfigOps taskConfigOps = new TaskConfigOps();
        SimpleTaskContext.CONTEXT.put("config", taskConfigOps);
        return CONTINUE_PROCESSING;
    }
}
