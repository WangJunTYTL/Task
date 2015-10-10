package com.peaceful.task.container.store;

/**
 * 任务存储中心基本配置
 * 任务存储中心需要拿到任务容器的某些基本配置信息，由于目前没有单独的配置加载模块，所以当加载任务存储模块前，需要先初始化该类
 *
 * @author WangJun <wangjuntytl@163.com>
 * @version 1.0 15/9/15
 * @since 1.6
 */
public class BasicTaskStoreConf {

    /**
     * 项目名 用于存储对象时，包装key的命名空间,防止不同任务容器的key冲突 see {@link KeyCreate}
     **/
    public static String projectName;
}
