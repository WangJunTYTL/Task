package com.peaceful.task.manage.common;

import com.peaceful.task.manage.exception.QueueServiceConfigException;
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

public class QueueTaskConf {

    public String projectName = "default";
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

    Logger logger = QueueLogger.LOGGER;

    public QueueTaskConf() {
        Config config = ConfigFactory.load("queueService.conf");
        try {
            config.getString("QueueService.version");
            if (StringUtils.isNotEmpty(config.getString("QueueService.router"))) {
                dispatchParallel = config.getInt("QueueService.router");
            }
            if (StringUtils.isNotEmpty(config.getString("QueueService.worker"))) {
                execParallel = config.getInt("QueueService.worker");
            }
            if (StringUtils.isNotEmpty(config.getString("QueueService.alertPhone"))) {
                alertPhone = config.getString("QueueService.alertPhone");
            }
        } catch (Exception e) {
            logger.warn("current version is too low ,please upgrade!");
            if (StringUtils.isNotEmpty(config.getString("QueueService.DispatchParallel"))) {
                dispatchParallel = config.getInt("QueueService.DispatchParallel");
            }
            if (StringUtils.isNotEmpty(config.getString("QueueService.ExecParallel"))) {
                execParallel = config.getInt("QueueService.ExecParallel");
            }
        }


        if (StringUtils.isNotEmpty(config.getString("QueueService.ProjectName"))) {
            projectName = config.getString("QueueService.ProjectName");
        }
        if (StringUtils.isNotEmpty(config.getString("QueueService.ProcessQueueClass"))) {
            processQueueClass = config.getString("QueueService.ProcessQueueClass");
        } else {
            LOAD_STATE = FAIL_LOAD;
            throw new QueueServiceConfigException("ProcessQueueClass is empty");
        }
        try {
            aClass = Class.forName(processQueueClass);
            LOAD_STATE = FAIL_LOAD;
            processQueueInstance = aClass.newInstance();
        } catch (ClassNotFoundException e) {
            LOAD_STATE = FAIL_LOAD;
            throw new QueueServiceConfigException(e);
        } catch (InstantiationException e) {
            LOAD_STATE = FAIL_LOAD;
            throw new QueueServiceConfigException(e);
        } catch (IllegalAccessException e) {
            LOAD_STATE = FAIL_LOAD;
            throw new QueueServiceConfigException(e);
        }
        maxParallel = dispatchParallel * execParallel;
        dangerParallel = dispatchParallel + execParallel;
        queues = config.getStringList("QueueService.QueueList");
        logger.info("------------queueService load conf-------------------------");
        logger.info("projectName:{}", projectName);
        logger.info("dispatchParallel:{}", dispatchParallel);
        logger.info("execParallel:{}", dispatchParallel);
        logger.info("maxParallel:{}", maxParallel);
        logger.info("queueList:{}", queues);
        logger.info("processQueueClass:{}", processQueueClass);
        logger.info("-------------------------------------");
        LOAD_STATE = SUC_LOAD;
    }

    public static synchronized QueueTaskConf getConf() {
        QueueTaskConf conf = getInstance.queueTaskConf;
        if (QueueTaskConf.LOAD_STATE == SUC_LOAD)
            return conf;
        else
            return null;
    }

    private static class getInstance {

        public static QueueTaskConf queueTaskConf = new QueueTaskConf();
    }

}
