package com.peaceful.task.container.invoke.excute;

import com.peaceful.task.container.common.TaskContainerLogger;
import com.peaceful.task.container.dispatch.DispatchContainer;
import com.peaceful.task.container.invoke.InvokeContext;
import com.peaceful.task.container.invoke.chain.Context;
import net.sf.cglib.reflect.FastClass;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * @author WangJun <wangjuntytl@163.com>
 * @version 1.0 15/8/28
 * @since 1.6
 */

public class InvokeHandler implements Handler {


    @Override
    public boolean execute(Context context) throws Exception {
        InvokeContext invokeContext = (InvokeContext) context;
        Object instance = getProcessInstance(invokeContext.aClass);
        Method method = invokeContext.aClass.getMethod(invokeContext.methodName, invokeContext.parameterTypes);
        if (method == null) {
            throw new NotFoundMethod(invokeContext.methodName, invokeContext.parameterTypes);
        }
        Object result = method.invoke(instance, invokeContext.newArgs);
        //下面的调用方式可能在方法多的对象中调用更快
//        FastMethod fastMethod = getFastClass(invokeContext.aClass).getMethod(invokeContext.methodName,invokeContext.parameterTypes);
//        fastMethod.invoke(instance,invokeContext.newArgs);
        if (result != null) {
            TaskContainerLogger.ROOT_LOGGER.warn("{}.{} Suggested that do not have a return value", invokeContext.aClass.getName(), invokeContext.methodName);
        }
        return false;
    }

    private static Object getProcessInstance(Class zClass) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        Map<Class, Object> scheduleContainer = DispatchContainer.getDispatchInstanceContainer();
        if (scheduleContainer.containsKey(zClass)) {
            //logger.warn("{} has already exists", task2.getaClass().getName());
        } else {
            scheduleContainer.put(zClass, zClass.newInstance());
        }
        return scheduleContainer.get(zClass);
    }

    private static FastClass getFastClass(Class zClass) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        Map<Class, FastClass> fastClassContainer = DispatchContainer.getDispatchFastClassContainer();
        if (fastClassContainer.containsKey(zClass)) {
            //logger.warn("{} has already exists", task2.getaClass().getName());
        } else {
            fastClassContainer.put(zClass, FastClass.create(zClass));
        }
        return fastClassContainer.get(zClass);
    }
}
