package com.peaceful.task.core.dispatch;

/**
 * 任务队列描述
 *
 * @author WangJun
 * @version 1.0 16/3/30
 */
public class TaskMeta {

    public String name;// 队列名
    public String desc;// 描述
    public String state = "OK"; // 状态 OK 可以被所有机器调用 hostname 只能被某台机器调用 isLock 不允许被调用
    public long reserve = 0;
    public long createTime;// 任务创建时间
    public long updateTime;// 任务最近更新时间
    public boolean expire;// 是否已经过期

    public TaskMeta() {

    }

    public TaskMeta(String name) {
        this.name = name;
        state = "OK";
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TaskMeta)) return false;
        TaskMeta task = (TaskMeta) o;
        return name != null ? name.equals(task.name) : task.name == null;
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }
}
