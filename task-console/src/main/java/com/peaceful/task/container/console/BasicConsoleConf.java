package com.peaceful.task.container.console;

import com.peaceful.common.util.Application;

import java.util.List;

/**
 * 任务容器基本配置
 * 任务存储中心需要拿到任务容器的某些基本配置信息，由于目前没有单独的配置加载模块，所以当加载任务存储模块前，需要先初始化该类
 *
 * @author WangJun <wangjuntytl@163.com>
 * @version 1.0 15/9/15
 * @since 1.6
 */

public class BasicConsoleConf {

    static {
        Application.loadToJVM();
    }

    /**
     * 项目名
     **/
    public static String projectName;

    /**
     * 固定任务列表
     */
    public static List<String> focusedTaskList;
}
