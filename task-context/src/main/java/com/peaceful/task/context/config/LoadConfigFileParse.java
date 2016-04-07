package com.peaceful.task.context.config;

import com.peaceful.task.context.SimpleTaskContext;
import com.peaceful.task.context.error.SystemConfigException;
import com.peaceful.common.util.chain.Command;
import com.peaceful.common.util.chain.Context;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.apache.commons.lang3.StringUtils;

/**
 * load config file
 * <p>
 * Created by wangjun on 16/1/12.
 */
public class LoadConfigFileParse implements Command {

    @Override
    public boolean execute(Context context) throws Exception {
        // search config file
        String path = "task.conf";
        String conf = System.getProperty("task.conf.file");
        if (StringUtils.isNotEmpty(conf)) path = conf;
        if (StringUtils.isEmpty(path)) {
            throw new SystemConfigException("configPath is empty");
        }
        Config config = ConfigFactory.load(path);
        context.put("config", config);
        TaskConfigOps taskConfigOps = new TaskConfigOps();
        SimpleTaskContext.CONTEXT.put("config", taskConfigOps);
        return CONTINUE_PROCESSING;
    }
}
