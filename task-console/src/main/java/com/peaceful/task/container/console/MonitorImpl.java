package com.peaceful.task.container.console;


import com.peaceful.common.util.Application;
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

    private MonitorImpl(){}


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
                taskBean.setId(key);
            }
            firstFlexibleTaskMap.put(key, taskBean);
            taskBeans.add(taskBean);
        }
        return taskBeans;
    }

    @Override
    public Set<TaskBean> getSecondFlexibleTaskBeanSet() {
        Set<TaskBean> taskBeans = new HashSet<TaskBean>();
        for (String key : secondFlexibleTaskMap.keySet()) {
            TaskBean taskBean = secondFlexibleTaskMap.get(key);
            if (taskBean == null) {
                taskBean = new TaskBean();
                taskBean.setId(key);
            }
            secondFlexibleTaskMap.put(key, taskBean);
            taskBeans.add(taskBean);
        }
        return taskBeans;
    }

    @Override
    public void removeFirstFlexibleTask(String key) {
        firstFlexibleTaskMap.remove(key);
    }

    @Override
    public void removeSecondFlexibleTask(String key) {
        secondFlexibleTaskMap.remove(key);
    }

    @Override
    public FocusedTaskMap getFocusedTaskMap() {
        if (focusedTaskMap.size() == 0) {
            for (String t : BasicConsoleConf.focusedTaskList) {
                TaskBean focusedTaskBean = new TaskBean();
                focusedTaskBean.setId(t);
                focusedTaskBean.lastRemains = focusedTaskBean.remain;
                focusedTaskBean.remain = TaskStore.get().size(t);
                focusedTaskMap.put(t, focusedTaskBean);
            }
        } else {
            for (String t : BasicConsoleConf.focusedTaskList) {
                TaskBean focusedTaskBean = focusedTaskMap.get(t);
                focusedTaskBean.setId(t);
                focusedTaskBean.lastRemains = focusedTaskBean.remain;
                focusedTaskBean.remain = TaskStore.get().size(t);
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
        TaskBean taskBean = firstFlexibleTaskMap.get(flexibleTaskBeanId);
        if (taskBean == null) {
            taskBean = new TaskBean();
            taskBean.setId(flexibleTaskBeanId);
            taskBean.desc = desc;
        }
        taskBean.updateTime=System.currentTimeMillis();
        firstFlexibleTaskMap.put(taskBean.getId(), taskBean);
    }

    @Override
    public void addFirstFlexibleTask(TaskBean taskBean) {
        firstFlexibleTaskMap.put(taskBean.getId(),taskBean);
    }

    @Override
    public void addSecondFlexibleTask(TaskBean taskBean) {
        secondFlexibleTaskMap.put(taskBean.getId(), taskBean);
    }

    @Override
    public String getRunningInfo() {
        return Application.out();
    }


}
