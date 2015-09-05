package com.peaceful.task.container.invoke;

import com.peaceful.task.container.invoke.chain.BaseContext;
import com.peaceful.task.container.invoke.chain.Context;
import com.peaceful.task.container.msg.Task2;

/**
 * 调用信息上下文，每个任务单元都应该一个独立的调用上下文
 * 该类不是线程安全，为了保证安全，需要为每个任务单元创建一个上下文
 *
 * @author WangJun <wangjuntytl@163.com>
 * @version 1.0 15/8/27
 * @since 1.6
 */

public class InvokeContext extends BaseContext implements Context {

    // 调用类
    public Class aClass;

    // 调用方法
    public String methodName;

    // 参数类型列表
    public Class[] parameterTypes;

    // 调用实际传参
    public Object[] args;

    // 解析参数到对应的类型
    public Object[] newArgs;

    // 解析进度
    public int index;

    // 解析总进度
    public int length;

    //上下文初始信息
    public final static String START_TIME = "start_time";
    public final static String QUEUE_NAME = "queue_name";
    public final static String TASK_ID = "task_id";

    // 利用Task2任务单元构造调用上下文信息
    public InvokeContext(Task2 task2) {
        aClass = task2.aClass;
        methodName = task2.method;
        parameterTypes = task2.parameterTypes;
        args = task2.args;
        length = args.length;
        newArgs = new Object[length];
        index = 0;
        this.put(START_TIME, String.valueOf(System.currentTimeMillis()));
        this.put(QUEUE_NAME, String.valueOf(task2.queueName));
        this.put(TASK_ID, String.valueOf(task2.id));
    }

    public Class getCurrentParamType() {
        return parameterTypes[index - 1];
    }

    public Object getCurrentArg() {
        return newArgs[index - 1];
    }


}
