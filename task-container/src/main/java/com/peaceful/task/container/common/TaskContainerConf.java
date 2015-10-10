package com.peaceful.task.container.common;

import com.peaceful.task.container.exception.TaskContainerConfigException;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;

import java.util.List;

/**
 * 任务容器全局配置信息
 * <p/>
 * 任务容器的全局信息配置在taskContainer.conf文件，该配置类在容器启动时会加载该文件的描述
 * 在使用配置时可以通过{@link TaskContainerConf#getConf()}获取类的实例对象
 *
 * @author WangJun <wangjuntytl@163.com>
 * @version 2.0 15/5/15
 * @since 1.6
 */

public class TaskContainerConf {

    /**
     * 定义容器的的namespace
     * 如果你有多个项目嵌入了该容器或在一个项目中嵌入了多个容器，而且多个项目共用了一套任务存储模块，为了隔离系统之间的存储的对象，会在必要的地方加上该namespace
     * 比如 projectName = 'crmWeb'
     *
     * @Task("testQueue") public void test(String str) {
     * Util.report(str);
     * }
     * <p/>
     * 实际调用该方法会进入到testQueue_crmWeb 队列
     */
    public String projectName = "default";
    /**
     * router count
     */
    public int router = 2;
    /**
     * worker count
     */
    public int worker = 6;
    /**
     * 最大同时并行处理数
     */
    public int maxParallel = 0;
    /**
     * 处理任务业务入口，2.0 后已移除
     */
    @Deprecated
    public String processTaskClass = "";
    /**
     * 任务积压超过指定数量后，发出报警，报警手机号列表
     */
    public String alertPhone = "";
    /**
     * 固定的任务对列列表
     */
    public List<String> focusedTasks;
    // 为载入配置文件
    private static final int NOT_LOAD = 0;
    // 成功载入配置文件
    private static final int SUC_LOAD = 1;
    // 失败载入配置文件
    private static final int FAIL_LOAD = 2;
    private static int LOAD_STATE = NOT_LOAD;
    /**
     * 任务处理业务入口对象Class 2.0版本后移除
     */
    @Deprecated
    public Class aClass;
    /**
     * 任务处理业务入口实例  2.0 版本后移除
     */
    @Deprecated
    public Object processQueueInstance;

    Logger logger = TaskContainerLogger.ROOT_LOGGER;

    /**
     * 初始化配置
     *
     * @throws TaskContainerConfigException
     */
    public TaskContainerConf() {
        Config config = ConfigFactory.load("taskContainer.conf");
        config.getString("taskContainer.version");
        if (StringUtils.isNotEmpty(config.getString("taskContainer.router"))) {
            router = config.getInt("taskContainer.router");
        }
        if (StringUtils.isNotEmpty(config.getString("taskContainer.worker"))) {
            worker = config.getInt("taskContainer.worker");
        }
        if (StringUtils.isNotEmpty(config.getString("taskContainer.alertPhone"))) {
            alertPhone = config.getString("taskContainer.alertPhone");
        }

        if (StringUtils.isNotEmpty(config.getString("taskContainer.projectName"))) {
            projectName = config.getString("taskContainer.projectName");
        }
        if (StringUtils.isNotEmpty(config.getString("taskContainer.processTaskClass"))) {
            processTaskClass = config.getString("taskContainer.processTaskClass");
        } else {
            LOAD_STATE = FAIL_LOAD;
            throw new TaskContainerConfigException("processTaskClass is empty");
        }
        try {
            aClass = Class.forName(processTaskClass);
            LOAD_STATE = FAIL_LOAD;
            processQueueInstance = aClass.newInstance();
        } catch (ClassNotFoundException e) {
            LOAD_STATE = FAIL_LOAD;
            throw new TaskContainerConfigException(e);
        } catch (InstantiationException e) {
            LOAD_STATE = FAIL_LOAD;
            throw new TaskContainerConfigException(e);
        } catch (IllegalAccessException e) {
            LOAD_STATE = FAIL_LOAD;
            throw new TaskContainerConfigException(e);
        }
        maxParallel = router * worker;
        focusedTasks = config.getStringList("taskContainer.focusedTaskList");
        logger.info("------------task container suc load conf---------------");
        logger.info("project.name:{}", projectName);
        logger.info("router:{}", router);
        logger.info("worker:{}", worker);
        logger.info("max.parallel:{}", maxParallel);
        logger.info("task.list:{}", focusedTasks);
        logger.info("process.task.class:{}", processTaskClass);
        logger.info("-------------------------------------------------------");
        LOAD_STATE = SUC_LOAD;
    }

    /**
     * 获取配置实例
     *
     * @return TaskContainerConf
     * @throws TaskContainerConfigException
     */
    public static TaskContainerConf getConf() {
        TaskContainerConf conf = getInstance.queueTaskConf;
        if (TaskContainerConf.LOAD_STATE == SUC_LOAD)
            return conf;
        else
            return null;
    }

    private static class getInstance {
        public static TaskContainerConf queueTaskConf = new TaskContainerConf();
    }

}
