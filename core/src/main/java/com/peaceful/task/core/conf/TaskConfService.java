package com.peaceful.task.core.conf;

import com.google.common.base.Preconditions;
import com.google.common.base.Throwables;
import com.google.common.util.concurrent.AbstractIdleService;
import com.peaceful.common.util.chain.BaseContext;
import com.peaceful.common.util.chain.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * 启动时加载配置，系统默认配置文件task-reference.conf ,如果需要覆盖其中的某些配置，可以在task.conf配置，加载配置时会自动进行合并
 *
 * Created by wangjun on 16-8-27.
 */
public class TaskConfService  {

    private static Logger logger = LoggerFactory.getLogger(TaskConfService.class);
    private static Map<String,TaskConfigOps> opsMap = new HashMap<String, TaskConfigOps>();

    public static synchronized TaskConfigOps startUp() {
        try {
            Context configContext = new BaseContext();
            TaskConfigOps taskConfigOps = new TaskConfigOps();
            configContext.put("task.conf", taskConfigOps);
            ConfigParseChain.getSingleInstance().execute(configContext);
            Preconditions.checkState((!opsMap.containsKey(taskConfigOps.name)),"task["+ taskConfigOps.name+"] has been created!");
            opsMap.put(taskConfigOps.name,taskConfigOps);
            logger.info(taskConfigOps.toString());
            return taskConfigOps;
        } catch (Exception e) {
            Throwables.propagate(e);
        }
        return null;
    }

}
