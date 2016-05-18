package com.peaceful.task.context;

import com.peaceful.task.context.common.ContextConstant;
import com.peaceful.task.context.config.Executor;
import com.peaceful.task.context.config.TaskConfigOps;
import com.peaceful.task.context.error.SystemConfigException;
import com.peaceful.common.util.chain.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Config 模块加载入口
 * <p>
 * Config模块用于整个系统需要做到可配置选项的读取模块。支持.conf文件格式
 *
 * @author <a href="mailto:wangjuntytl@163.com">WangJun</a>
 * @version 1.0 16/1/11
 */
public class ContextModule implements TaskModule {

    static final Logger LOGGER = LoggerFactory.getLogger(ContextModule.class);

    @Override
    public boolean execute(Context context) throws Exception {
        try {
            TaskConfigOps taskConfigOps = (TaskConfigOps) SimpleTaskContext.CONTEXT.get(ContextConstant.CONFIG);
            LOGGER.info("---------------SimpleTaskContext-----------------");
            LOGGER.info("name: {}", taskConfigOps.name);
            LOGGER.info("boot-mode: {}", taskConfigOps.bootMode);
            if (taskConfigOps.developMode.equals("test")) {
                LOGGER.info("develop-mode: only for test!");
            } else {
                LOGGER.info("develop-mode:product");
            }
            LOGGER.info("queue: {}", taskConfigOps.queue);
            LOGGER.info("bean-factory: {}", taskConfigOps.beanFactory);
            LOGGER.info("dispatch-tick: {}ms", taskConfigOps.dispatchTick);
            for (Executor executor : taskConfigOps.executorConfigOps.executorNodeList) {
                LOGGER.info("executor: {} implementation: {}", executor.name, executor.implementation);
            }
            LOGGER.info("-------------------------------------------");
        } catch (Exception e) {
            LOGGER.error("parse config file error,{}", e);
            throw new SystemConfigException("parse config file error");
        }
        return CONTINUE_PROCESSING;
    }
}
