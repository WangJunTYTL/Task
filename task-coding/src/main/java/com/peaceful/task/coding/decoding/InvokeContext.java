package com.peaceful.task.coding.decoding;


import com.peaceful.common.util.chain.BaseContext;
import com.peaceful.common.util.chain.Context;
import com.peaceful.task.context.coding.TU;

/**
 * 调用信息上下文，每个任务单元都应该一个独立的调用上下文
 * 该类不是线程安全，为了保证安全，需要为每个任务单元创建一个上下文
 *
 * @author WangJun <wangjuntytl@163.com>
 * @version 1.0 15/8/27
 * @since 1.6
 */

public class InvokeContext extends BaseContext implements Context {

    public String id;

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

    public String executor;

    public String queueName;

    // 解析进度
    public int index;

    // 解析总进度
    public int length;

    // 任务提交时间
    public long submitTime;

    // 利用Task2任务单元构造调用上下文信息
    public InvokeContext(TU task2) {
        id = task2.id;
        queueName = task2.queueName;
        aClass = task2.aclass;
        methodName = task2.method;
        parameterTypes = task2.parameterTypes;
        args = task2.args;
        executor = task2.executor;
        length = args.length;
        newArgs = new Object[length];
        index = 0;
        submitTime = task2.getSubmitTime();
    }

    public Class getCurrentParamType() {
        return parameterTypes[index - 1];
    }

    public Object getCurrentArg() {
        return newArgs[index - 1];
    }


}
