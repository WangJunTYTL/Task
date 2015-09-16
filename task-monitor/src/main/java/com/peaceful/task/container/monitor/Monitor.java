package com.peaceful.task.container.monitor;

import java.util.Set;

/**
 * @author WangJun <wangjuntytl@163.com>
 * @version 1.0 15/9/15
 * @since 1.6
 */

public interface Monitor {

    FirstFlexibleTaskMap firstFlexibleTaskMap = new FirstFlexibleTaskMap();
    SecondFlexibleTaskMap secondFlexibleTaskMap = new SecondFlexibleTaskMap();
    FocusedTaskMap focusedTaskMap = new FocusedTaskMap();


    /**
     * 返回所有固定的任务
     *
     * @return
     */
    Set<String> getFocusedTasks();

    FocusedTaskMap getFocusedTaskMap();

    FirstFlexibleTaskMap getFirstFlexibleTaskMap();

    Set<TaskBean> getFocusedTaskBeanSet();

    Set<TaskBean> getFirstFlexibleTaskBeanSet();

    /**
     * 返回所有的运行期任务
     *
     * @return
     */
    Set<String> getFlexibleTasks();

    void addFirstFlexibleTask(String flexibleTaskBeanId, String desc);

    void CommitTaskIncrement(String id);

    String getProjectName();


}
