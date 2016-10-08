package com.peaceful.task.kernal.coding;

import com.alibaba.fastjson.JSON;
import com.peaceful.task.kernal.TaskCoding;
import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;
import java.lang.reflect.Method;

/**
 * 任务描述协议
 *
 * 每次方法的调用都是一个Task的生成,通过{@link TaskCoding#encoding(Class, Method, Object[])}可以把
 * 方法的调用指令信息编码为一个TU对象,支持状态序列化
 *
 * @author <a href="mailto:wangjuntytl@163.com">WangJun</a>
 * @version 1.0 16/1/9
 */
public class TU implements Serializable {

    // 任务创建时应生成一个唯一id，用于log跟踪
    public String id;
    // 任务单元存放队列位置
    public String queueName = "default";
    // 该任务单元的Class入口
    @JSONField(deserialize = true)
    public Class aclass;
    // 该任务单元的方法入口
    public String method;
    // 该任务单元的方法参数类星列表
    public Class[] parameterTypes;
    // 该任务单元的方法参数列表
    public Object[] args;
    // 任务最终的执行器
    public String executor = "default";
    // 任务协议版本
    public String version = "2.0";
    // 任务提交时间
    public long submitTime;

    public long getSubmitTime() {
        return submitTime;
    }

    public void setSubmitTime(long submitTime) {
        this.submitTime = submitTime;
    }

    public Class getAclass() {
        return aclass;
    }

    public void setAclass(Class aclass) {
        this.aclass = aclass;
    }

    public String getExecutor() {
        return executor;
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

    public void setExecutor(String executor) {
        this.executor = executor;
    }

    @Override
    public String toString() {
        return id + " " + aclass.getSimpleName() + "." + method + " " + arrayToString(args);
    }

    private String arrayToString(Object[] args) {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[");
        for (Object o : args) {
            buffer.append(JSON.toJSONString(o) + ",");
        }
        return buffer.substring(0, buffer.length() - 1) + "]";
    }
}
