package com.peaceful.task.container.store;

/**
 * @author WangJun <wangjuntytl@163.com>
 * @version 1.0 15/9/23
 * @since 1.6
 */

public class NamespaceManage {

    /**
     * 任务单元pop计数器key的namespace,如果清除任务队列，需要检查该namespace下的任务队列计数器是否清除
     */
    public static String COUNT_POP_KEY = "task_container_" + BasicTaskStoreConf.projectName + "_system_count_pop_key";
    /**
     * 任务单元push计数器key的namespace，如果清除任务队列，需要检查该namespace下的任务队列计数器是否清除
     */
    public static String COUNT_PUSH_KEY = "task_container_" + BasicTaskStoreConf.projectName + "_system_count_push_key";

    /**
     * 远程lock的namespace
     */
    public static String REMOTE_LOCK_KEY = "task_container_" + BasicTaskStoreConf.projectName + "_system_lock_key_";

    /**
     * 远程对象存储的namespace，如果清除任务队列，需要检查该namespace下的任务队列存储的对象是否清除
     */
    public static String REMOTE_REPO_KEY = "task_container_" + BasicTaskStoreConf.projectName + "_system_repo_key_";

    /**
     * 动态提交任务的namespace
     */
    public final static String FORCE_TASK_PERSISTENCE_QUEUE = "force_task_persistence_queue";



}
