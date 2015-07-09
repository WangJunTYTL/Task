package com.peaceful.task.manage.common;

import com.peaceful.task.manage.exception.TaskManageConfigException;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;

import java.util.List;

/**
 * @author WangJun <wangjuntytl@163.com>
 * @version 1.0 15/5/15
 * @since 1.6
 */

public class TaskManageConf {

    public String projectName = "default";
    public String redisNode = "haproxy";
    public int dispatchParallel = 2;
    public int execParallel = 6;
    public int maxParallel = 0;
    public int dangerParallel = 2;
    public String processQueueClass = "";
    public String alertPhone="";
    public List<String> queues;
    private static final int NOT_LOAD = 0;
    private static final int SUC_LOAD = 1;
    private static final int FAIL_LOAD = 2;
    private static int LOAD_STATE = NOT_LOAD;
    public Class aClass;
    public Object processQueueInstance;

    Logger logger = TaskManageLogger.LOGGER;

    public TaskManageConf() {
        Config config = ConfigFactory.load("taskManage.conf");
        try {
            config.getString("taskManage.version");
            if (StringUtils.isNotEmpty(config.getString("taskManage.router"))) {
                dispatchParallel = config.getInt("taskManage.router");
            }
            if (StringUtils.isNotEmpty(config.getString("taskManage.worker"))) {
                execParallel = config.getInt("taskManage.worker");
            }
            if (StringUtils.isNotEmpty(config.getString("taskManage.alertPhone"))) {
                alertPhone = config.getString("taskManage.alertPhone");
            }
        } catch (Exception e) {
            logger.warn("current version is too low ,please upgrade!");
            if (StringUtils.isNotEmpty(config.getString("taskManage.DispatchParallel"))) {
                dispatchParallel = config.getInt("taskManage.DispatchParallel");
            }
            if (StringUtils.isNotEmpty(config.getString("taskManage.ExecParallel"))) {
                execParallel = config.getInt("taskManage.ExecParallel");
            }
        }

        if (StringUtils.isNotEmpty(config.getString("taskManage.redisNode"))) {
            redisNode = config.getString("taskManage.redisNode");
        }
        if (StringUtils.isNotEmpty(config.getString("taskManage.projectName"))) {
            projectName = config.getString("taskManage.projectName");
        }
        if (StringUtils.isNotEmpty(config.getString("taskManage.processTaskClass"))) {
            processQueueClass = config.getString("taskManage.processTaskClass");
        } else {
            LOAD_STATE = FAIL_LOAD;
            throw new TaskManageConfigException("processTaskClass is empty");
        }
        try {
            aClass = Class.forName(processQueueClass);
            LOAD_STATE = FAIL_LOAD;
            processQueueInstance = aClass.newInstance();
        } catch (ClassNotFoundException e) {
            LOAD_STATE = FAIL_LOAD;
            throw new TaskManageConfigException(e);
        } catch (InstantiationException e) {
            LOAD_STATE = FAIL_LOAD;
            throw new TaskManageConfigException(e);
        } catch (IllegalAccessException e) {
            LOAD_STATE = FAIL_LOAD;
            throw new TaskManageConfigException(e);
        }
        maxParallel = dispatchParallel * execParallel;
        dangerParallel = dispatchParallel + execParallel;
        queues = config.getStringList("taskManage.queueList");
        logger.info("------------queueService load conf-------------------------");
        logger.info("projectName:{}", projectName);
        logger.info("dispatchParallel:{}", dispatchParallel);
        logger.info("execParallel:{}", dispatchParallel);
        logger.info("maxParallel:{}", maxParallel);
        logger.info("queueList:{}", queues);
        logger.info("processTaskClass:{}", processQueueClass);
        logger.info("-------------------------------------");
        LOAD_STATE = SUC_LOAD;
    }

    public static synchronized TaskManageConf getConf() {
        TaskManageConf conf = getInstance.queueTaskConf;
        if (TaskManageConf.LOAD_STATE == SUC_LOAD)
            return conf;
        else
            return null;
    }

    private static class getInstance {

        public static TaskManageConf queueTaskConf = new TaskManageConf();
    }

}
