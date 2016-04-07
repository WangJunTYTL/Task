package com.peaceful.task.context;

import com.peaceful.task.context.coding.TU;
import com.peaceful.task.context.coding.TUR;

import java.lang.reflect.Method;

/**
 * 对异步类内方法调用的编码和解码
 * <p>
 * 我们在调用Java方法时,每次调用其实就是产生一个Task,该接口是描述对调用信息的编码和解码操作
 *
 * @author <a href="mailto:wangjuntytl@163.com">WangJun</a>
 * @version 1.0 16/1/12
 */
public interface TaskCoding {

    /**
     * encoding将方法的调用转为可序列化的Task对象,最终目的是序列化成一串指令,可以存储到queue服务中,并支持可以反序列化,还原方法的调用信息
     *
     * @param zClass
     * @param method
     * @param args
     * @return SimpleTaskContext
     */
    TU encoding(Class zClass, Method method, Object[] args);

    /**
     * encoding方法将方法的调用动作编码为一个Task对象,Task对象序列化可以存储到queue服务中
     * decoding在根据queue中序列化的Task对象进行反序列化,主要操作包括类型转换,并包装为Runnable对象可供executor执行
     *
     * @param taskJson
     * @return Runnable的子对象TaskUnit, 可以被executor模块执行
     */
    TUR decoding(String taskJson);

}
