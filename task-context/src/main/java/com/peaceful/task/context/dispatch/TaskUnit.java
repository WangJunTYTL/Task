package com.peaceful.task.context.dispatch;

/**
 * @author WangJun
 * @version 1.0 16/3/30
 */
public class TaskUnit {

    public String name;
    public String desc;
    public String state = "OK";
    public long reserve = 0;
    public long createTime;
    public long updateTime;
    public boolean expire;

    public TaskUnit() {

    }

    public TaskUnit(String name) {
        this.name = name;
        state = "OK";
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TaskUnit)) return false;
        TaskUnit task = (TaskUnit) o;
        return name != null ? name.equals(task.name) : task.name == null;
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }
}
