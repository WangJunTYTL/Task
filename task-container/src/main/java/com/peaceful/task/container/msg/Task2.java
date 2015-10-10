package com.peaceful.task.container.msg;

import com.peaceful.task.container.dispatch.DispatchContainer;
import com.peaceful.task.container.schedule.TaskSchedule;
import com.alibaba.fastjson.JSON;

import java.io.Serializable;

/**
 * 每一个任务单元对象描述
 * worker 可以接收该消息单元，并识别需要执行的业务{@link DispatchContainer}
 *
 * @author WangJun <wangjuntytl@163.com>
 * @version 1.0 15/8/16
 * @see TaskSchedule
 * @since 1.6
 */

public class Task2 implements Serializable {

    // 任务创建时应生成一个唯一id，用于log跟踪
    public String id;
    // 任务单元存放队列位置
    public String queueName;
    // 该任务单元的Class入口
    public Class aClass;
    // 该任务单元的方法入口
    public String method;
    // 该任务单元的方法参数类星列表
    public Class[] parameterTypes;
    // 该任务单元的方法参数列表
    public Object[] args;
    // 该任务单元的所属版本号
    public String version = "2.0";


    public Class getaClass() {
        return aClass;
    }

    public void setaClass(Class aClass) {
        this.aClass = aClass;
    }

    public Object[] getArgs() {
        return args;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Class[] getParameterTypes() {
        return parameterTypes;
    }

    public void setParameterTypes(Class[] parameterTypes) {
        this.parameterTypes = parameterTypes;
    }

    public String getQueueName() {
        return queueName;
    }

    public void setQueueName(String queueName) {
        this.queueName = queueName;
    }

    @Override
    public String toString() {
        return id+" "+aClass.getName()+"."+method+ JSON.toJSONString(args);
    }
}
