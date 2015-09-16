package com.peaceful.task.container.monitor;

import com.peaceful.task.container.store.TaskStore;

import java.util.HashSet;
import java.util.Set;

/**
 * @author WangJun <wangjuntytl@163.com>
 * @version 1.0 15/9/15
 * @since 1.6
 */

public class MonitorImpl implements Monitor {

    private static class Single {
        public static Monitor monitor = new MonitorImpl();
    }

    public static Monitor getInstance() {
        return Single.monitor;
    }


    @Override
    public Set<String> getFocusedTasks() {
        return getFocusedTaskMap().keySet();
    }

    @Override
    public Set<TaskBean> getFocusedTaskBeanSet() {
        FocusedTaskMap focusedTaskMap = getFocusedTaskMap();
        Set<TaskBean> focusedTaskMapSet = new HashSet<TaskBean>();
        for (String key : getFocusedTasks()) {
            if (focusedTaskMap.get(key) != null) {
                focusedTaskMapSet.add(focusedTaskMap.get(key));
            }
        }
        return focusedTaskMapSet;
    }

    @Override
    public Set<TaskBean> getFirstFlexibleTaskBeanSet() {
        FirstFlexibleTaskMap firstFlexibleTaskMap = getFirstFlexibleTaskMap();
        Set<TaskBean> taskBeans = new HashSet<TaskBean>();
        for (String key : firstFlexibleTaskMap.keySet()) {
            TaskBean taskBean = firstFlexibleTaskMap.get(key);
            if (taskBean == null) {
                taskBean = new TaskBean();
                taskBean.id = key;
            }
            taskBean.lastRemains = taskBean.remain;
            taskBean.remain = TaskStore.get().size(key);
            firstFlexibleTaskMap.put(key, taskBean);
            taskBeans.add(taskBean);
        }
        return taskBeans;
    }

    @Override
    public FocusedTaskMap getFocusedTaskMap() {
        if (focusedTaskMap.size() == 0) {
            for (String t : BasicMonitorConf.taskList) {
                TaskBean focusedTaskBean = new TaskBean();
                focusedTaskBean.id = t;
                focusedTaskBean.lastRemains = focusedTaskBean.remain;
                focusedTaskBean.remain = TaskStore.get().size(t);
                focusedTaskMap.put(t, focusedTaskBean);
            }
        }
        return focusedTaskMap;
    }

    @Override
    public FirstFlexibleTaskMap getFirstFlexibleTaskMap() {
        return firstFlexibleTaskMap;
    }


    @Override
    public Set<String> getFlexibleTasks() {
        Set<String> flexibleTasks = new HashSet<String>();
        flexibleTasks.addAll(firstFlexibleTaskMap.keySet());
        flexibleTasks.addAll(secondFlexibleTaskMap.keySet());
        return flexibleTasks;
    }

    @Override
    public void addFirstFlexibleTask(String flexibleTaskBeanId, String desc) {
        if (firstFlexibleTaskMap.containsKey(flexibleTaskBeanId)) {
            // pass
        } else {
            TaskBean taskBean = new TaskBean();
            taskBean.id = flexibleTaskBeanId;
            taskBean.desc = desc;
            firstFlexibleTaskMap.put(taskBean.id, taskBean);
        }
    }

    @Override
    public void CommitTaskIncrement(String id) {

    }

    @Override
    public String getProjectName() {
        return BasicMonitorConf.projectName;
    }


}
