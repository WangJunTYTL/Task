package com.peaceful.task.context.coding;

import com.peaceful.task.context.SimpleTaskContext;
import com.peaceful.common.util.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 每个Task对象对应的一次方法的调用,这将会转为Runnable对象供Executor执行
 *
 * @author <a href="mailto:wangjuntytl@163.com">WangJun</a>
 * @version 1.0 16/1/12
 */
public class TUR implements Runnable{

    private TU task;

    public TUR(TU task02) {
        this.task = task02;
    }

    public long createTime = System.currentTimeMillis();

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void run() {
        try {
            if (task == null) return;
            Method method;
            try {
                method = task.aclass.getMethod(task.method, task.parameterTypes);
            } catch (NoSuchMethodException e) {
                logger.error("task id {} no matching method {}  {}", task.id, task.aclass.getSimpleName() + "." + task.method, ExceptionUtils.getStackTrace(e));
                return;
            }
            try {
                method.invoke(SimpleTaskContext.BEAN_FACTORY.getBean(task.aclass), task.args);
            } catch (IllegalAccessException e) {
                logger.error("task {} can't be invoked {}", task.id, ExceptionUtils.getStackTrace(e));
            } catch (InvocationTargetException e) {
                logger.error("task {} can't be invoked {}", task.id, ExceptionUtils.getStackTrace(e));
            }
        } catch (Exception e) {
            logger.error("task {} invoke error {}", task.getId(), ExceptionUtils.getStackTrace(e));
        }
    }

    public TU getTask() {
        return task;
    }
}
