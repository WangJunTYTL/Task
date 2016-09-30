package com.peaceful.task.kernal.conf;

import com.google.common.base.Preconditions;
import com.google.common.base.Throwables;
import com.peaceful.common.util.chain.BaseContext;
import com.peaceful.common.util.chain.Context;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 启动时加载配置，系统默认配置文件task-reference.conf ,如果需要覆盖其中的某些配置，可以在task.conf配置，加载配置时会自动进行合并
 * <p>
 * Created by wangjun on 16-8-27.
 */
public class TaskConfService {

    private static Logger logger = LoggerFactory.getLogger(TaskConfService.class);
    private static Map<String, TaskConfigOps> opsMap = new ConcurrentHashMap<String, TaskConfigOps>();

    public static synchronized TaskConfigOps startUp() {
        TaskConfigOps taskConfigOps = new TaskConfigOps();
        Config config = loadConfig();
        basicConf(taskConfigOps, config);
        Preconditions.checkState((!opsMap.containsKey(taskConfigOps.name)), "task[" + taskConfigOps.name + "] has been created!");
        executorConf(taskConfigOps, config);
        opsMap.put(taskConfigOps.name, taskConfigOps);
        logger.info("Load conf for task\n{}",taskConfigOps.detail());
        return taskConfigOps;
    }

    private static Config loadConfig() {
        // 系统默认配置文件，可覆盖
        String defaultPath = "task-reference.conf";
        Config config = ConfigFactory.load(defaultPath);
        // 用户自定义配置文件
        String customerPath = "task.conf";
        String conf = System.getProperty("task.conf.file");
        if (StringUtils.isNotEmpty(conf)) customerPath = conf;
        if (LoadConfigFileParse.class.getClassLoader().getResource(customerPath) != null) {
            Config _config = ConfigFactory.load(customerPath);
            // 合并用户配置和默认配置
            config = _config.withFallback(config);
        } else {
            logger.info("Not found customer config file for task, reference the task-reference.conf");
        }
        return config;
    }

    private static void basicConf(TaskConfigOps taskConfigOps, Config config) {
        taskConfigOps.name = config.getString("task.name");
        taskConfigOps.bootMode = config.getString("task.boot-mode");
        taskConfigOps.developMode = config.getString("task.develop-mode");
        if (config.hasPath("task.queue")) {
            taskConfigOps.queue = config.getString("task.queue");
        } else {
            taskConfigOps.queue = "com.peaceful.task.queue.redis.RedisQueue";
        }
        if (config.hasPath("task.bean-factory")) {
            taskConfigOps.beanFactory = config.getString("task.bean-factory");
        } else {
            taskConfigOps.beanFactory = "com.peaceful.task.core.dispatch.TaskBeanFactoryImpl";
        }
        taskConfigOps.beanFactory = config.getString("task.bean-factory");
    }

    private static void executorConf(TaskConfigOps taskConfigOps, Config config) {
        List<? extends ConfigObject> executors = config.getObjectList("task.executor");
        for (ConfigObject object : executors) {
            Executor executorNode = new Executor();
            executorNode.name = object.get("name").unwrapped().toString();
            executorNode.implementation = object.get("implementation").unwrapped().toString();
            try {
                executorNode.Class = Thread.currentThread().getContextClassLoader().loadClass(executorNode.implementation);
            } catch (ClassNotFoundException e) {
                Throwables.propagate(e);
            }
            taskConfigOps.executorConfigOps.executorNodeList.add(executorNode);
        }
    }

}
