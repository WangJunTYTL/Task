package com.peaceful.task.container.console;

import java.util.Set;

/**
 * 任务容器运行期查看、监控
 * <p/>
 * 提供查看所有 focused task、flexible task 信息以及通过{@link TaskBean}分析的监控结果
 *
 * @author WangJun <wangjuntytl@163.com>
 * @version 1.0 15/9/15
 * @since 1.6
 */

public interface Monitor {

    /**
     * focused task map
     */
    FocusedTaskMap focusedTaskMap = new FocusedTaskMap();
    /**
     * first flexible task map
     **/
    FirstFlexibleTaskMap firstFlexibleTaskMap = new FirstFlexibleTaskMap();
    /**
     * second flexible task map
     */
    SecondFlexibleTaskMap secondFlexibleTaskMap = new SecondFlexibleTaskMap();


    /**
     * 返回所有固定的任务名称列表
     *
     * @return task name set
     */
    Set<String> getFocusedTasks();

    /**
     * 获取所有focused task
     *
     * @return focused taskbean map
     */
    FocusedTaskMap getFocusedTaskMap();

    /**
     * 获取所有first flexible task task
     *
     * @return fisrt flexible taskbean map
     */
    FirstFlexibleTaskMap getFirstFlexibleTaskMap();

    /**
     * 获取所有focused task task
     *
     * @return focused taskbean set
     */
    Set<TaskBean> getFocusedTaskBeanSet();


    /**
     * 获取所有 first flexible task task
     *
     * @return first flexible task set
     */
    Set<TaskBean> getFirstFlexibleTaskBeanSet();

    /**
     * 获取所有 second flexible task task
     *
     * @return second flexible task set
     */
    Set<TaskBean> getSecondFlexibleTaskBeanSet();

    /**
     * 从 first flexible task map 中移除指定 tasbean
     *
     * @param key taskBean id
     */
    void removeFirstFlexibleTask(String key);

    /**
     * 从 second flexible task map 中移除指定 tasbean
     *
     * @param key taskBean id
     */
    void removeSecondFlexibleTask(String key);


    /**
     * 获取所有 flexible task name
     *
     * @return all flexible task id set
     */
    Set<String> getFlexibleTasks();

    /**
     * 增加 flexible task to firstFlexibleTaskMap
     *
     * @param flexibleTaskBeanId taskbean#id
     * @param desc               taskbean#desc
     */
    void addFirstFlexibleTask(String flexibleTaskBeanId, String desc);


    /**
     * 增加 flexible task to firstFlexibleTaskMap
     *
     * @param taskBean
     */
    void addFirstFlexibleTask(TaskBean taskBean);


    /**
     * 增加 flexible task to secondFlexibleTaskMap
     *
     * @param taskBean
     */
    void addSecondFlexibleTask(TaskBean taskBean);

    /**
     * 获取运行期信息
     *
     * @return running info
     */
    String getRunningInfo();


}
