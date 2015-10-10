package com.peaceful.task.container.store.help;


import com.peaceful.task.container.store.NamespaceManage;

/**
 * @author WangJun <wangjuntytl@163.com>
 * @version 1.0 15/9/23
 * @since 1.6
 */

public class Helper {

    /**
     * 任务单元pop计数器
     **/
    public static RemoteCount remotePopCount = new RemoteCount(NamespaceManage.COUNT_POP_KEY);
    /**
     * 任务单元push计数器
     **/
    public static RemoteCount remotePushCount = new RemoteCount(NamespaceManage.COUNT_PUSH_KEY);


    public static RemoteRepo remoteFocusedRepo = new RemoteRepo("remote-console-focused-repo");
    public static RemoteRepo remoteFirstFlexibleRepo = new RemoteRepo("remote-console-first-flexible-repo");
    public static RemoteRepo remoteSecondFlexibleRepo = new RemoteRepo("remote-console-second-flexible-repo");




}
