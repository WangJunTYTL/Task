package com.peaceful.task.kernal.coding;

import com.google.common.base.Preconditions;
import com.google.common.base.Throwables;
import com.peaceful.task.kernal.TaskContext;
import com.peaceful.task.kernal.helper.TaskLog;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.UndeclaredThrowableException;

/**
 * TU协议解析
 * <p>
 * 将TU协议解析,这将会被解析成Runnable对象,然后可以被Executor执行
 * <p>
 * 任务执行异常处理
 * <p>
 * 任务在执行时如果抛出异常,则会将异常转为运行期异常并向上进行传播
 *
 * @author <a href="mailto:wangjuntytl@163.com">WangJun</a>
 * @version 1.0 16/1/12
 */
public class TUR implements Runnable {

    // TU协议信息
    private TU tu;
    // Task Context
    public TaskContext context;
    // 生成TUR的时间
    public long createTime = System.currentTimeMillis();
    private Logger logger = LoggerFactory.getLogger(getClass());

    public TUR(TU tu) {
        this.tu = tu;
    }

    @Override
    public void run() {
        if (tu == null) return;
        // 获取调用方法
        Method method = null;
        try {
            method = tu.aclass.getMethod(tu.method, tu.parameterTypes);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("task execute error:task->" + tu.id + ", not found method " + tu.aclass.getSimpleName() + "." + tu.method + " cause:" + ExceptionUtils.getStackTrace(e));
        }
        // 获取调用bean
        Object bean = null;
        try {
            bean = context.getTaskBeanFactory().getBean(tu.aclass);
        } catch (Exception e) {
            throw new RuntimeException("task execute error:task->" + tu.id + " not found bean instance " + tu.aclass.getSimpleName() + "." + tu.method + " cause:" + ExceptionUtils.getStackTrace(e));
        }
        Preconditions.checkState(method != null, "execute task fail,Not Fount " + tu.aclass.getName() + "." + tu.method);
        Preconditions.checkState(bean != null, "execute task fail,Not Fount bean instance for " + tu.aclass.getName() + "." + tu.method);
        // 开始执行方法
        try {
            method.invoke(bean, tu.args);
        } catch (Exception e) {
            TaskLog.DISPATCH_TASK.error("task execute error:task->{},TaskSystem only log the exception but catch it,cause:{}",this,Throwables.getStackTraceAsString(unwrapThrowable(e)));
            Throwables.propagate(unwrapThrowable(e));
        }
    }

    public TU getTask() {
        return tu;
    }

    public static Throwable unwrapThrowable(Throwable wrapped) {
        Throwable unwrapped = wrapped;

        while(true) {
            while(!(unwrapped instanceof InvocationTargetException)) {
                if(!(unwrapped instanceof UndeclaredThrowableException)) {
                    return unwrapped;
                }

                unwrapped = ((UndeclaredThrowableException)unwrapped).getUndeclaredThrowable();
            }

            unwrapped = ((InvocationTargetException)unwrapped).getTargetException();
        }
    }

    @Override
    public String toString() {
        return tu.id+"["+ tu.aclass.getSimpleName()+"."+tu.method+"]";
    }
}
