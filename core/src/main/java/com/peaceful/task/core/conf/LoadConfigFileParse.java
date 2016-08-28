package com.peaceful.task.core.conf;

import com.peaceful.common.util.chain.Command;
import com.peaceful.common.util.chain.Context;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * load conf file
 * <p>
 * Created by wangjun on 16/1/12.
 */
public class LoadConfigFileParse implements Command {

    private Logger logger = LoggerFactory.getLogger(getClass());


    @Override
    public boolean execute(Context context) throws Exception {
        // search conf file
        String defaultPath = "task-reference.conf";
        Config config = ConfigFactory.load(defaultPath);
        String customerPath = "task.conf";
        String conf = System.getProperty("task.conf.file");
        if (StringUtils.isNotEmpty(conf)) customerPath = conf;
        if (LoadConfigFileParse.class.getClassLoader().getResource(customerPath) != null) {
            Config _config = ConfigFactory.load(customerPath);
            config = _config.withFallback(config);
        } else {
            logger.info("not fount customer config file, reference the task-reference.conf");
        }
        context.put("conf", config);
        return CONTINUE_PROCESSING;
    }
}
