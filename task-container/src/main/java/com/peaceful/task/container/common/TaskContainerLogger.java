package com.peaceful.task.container.common;

import org.slf4j.LoggerFactory;

/**
 * 任务容器指定某些log的namespace
 * <p/>
 * 为了使该组件的log可以和业务上的log分离，该容器的log的namespace要求必须严格从{@link TaskContainerLogger#ROOT_LOGGER}继承
 *
 * @author WangJun <wangjuntytl@163.com>
 * @version 1.0 15/5/15
 * @since 1.6
 */

public class TaskContainerLogger {

    // 任务容器默认log namespace
    public static final org.slf4j.Logger ROOT_LOGGER = LoggerFactory.getLogger(ConfConstant.DEFAULT_LOG_NAMESPACE);

}
