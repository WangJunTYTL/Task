package com.peaceful.task.core.conf;

import com.google.common.base.Throwables;
import com.peaceful.common.util.chain.BaseContext;
import com.peaceful.common.util.chain.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 启动时加载配置，系统默认配置文件task-reference.conf ,如果需要覆盖其中的某些配置，可以在task.conf配置，加载配置时会自动进行合并
 *
 * Created by wangjun on 16-8-27.
 */
public class TaskConfService {

    private Logger logger = LoggerFactory.getLogger(getClass());
    private TaskConfigOps taskConfigOps;


    protected void startUp() {
        try {
            Context configContext = new BaseContext();
            taskConfigOps = new TaskConfigOps();
            configContext.put("task.conf", taskConfigOps);
            ConfigParseChain.getSingleInstance().execute(configContext);
        } catch (Exception e) {
            Throwables.propagate(e);
        }
    }


    public synchronized TaskConfigOps get() {
        if (taskConfigOps == null) {
            startUp();
        }
        logger.info(taskConfigOps.toString());
        return taskConfigOps;
    }
}
