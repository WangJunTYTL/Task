package com.peaceful.task.container.admin.common;

import com.peaceful.task.container.console.BasicConsoleConf;
import com.peaceful.task.container.store.BasicTaskStoreConf;

/**
 * 目前关于任务容器面向用户可以定制的配置都放在task-container模块，其它模块需要某些相关的信息都需要在这个位置进行设置
 *
 * @author WangJun <wangjuntytl@163.com>
 * @version 1.0 15/10/8
 * @since 1.6
 */

public class LoadDependModule {

    public static void loadToJVM() {
        /** 加载其它模块并初始化所依赖模块的基本配置信息 */
        BasicTaskStoreConf.projectName = TaskContainerConf.getConf().projectName;
        BasicConsoleConf.projectName = TaskContainerConf.getConf().projectName;
        BasicConsoleConf.focusedTaskList = TaskContainerConf.getConf().focusedTasks;
    }


}
