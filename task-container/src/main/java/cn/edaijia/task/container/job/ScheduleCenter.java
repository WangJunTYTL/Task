package cn.edaijia.task.container.job;

import cn.edaijia.task.container.msg.Task2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Map;

/**
 * @author WangJun <wangjuntytl@163.com>
 * @version 1.0 15/8/16
 * @since 1.6
 */

public class ScheduleCenter extends HashMap {

    static Map<Class,Object> scheduleContainer = new IdentityHashMap();
    static Logger logger = LoggerFactory.getLogger(ScheduleCenter.class);


    private static Object getProcessInstance(Task2 task2) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        if (scheduleContainer.containsKey(task2.getaClass())) {
            //logger.warn("{} has already exists", task2.getaClass().getName());
        } else {
            scheduleContainer.put(task2.getaClass(), task2.getaClass().newInstance());
        }
        return scheduleContainer.get(task2.getaClass());
    }

    public static void invoke(Task2 task2) throws IllegalAccessException, InstantiationException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException {
        Object instance = getProcessInstance(task2);
        Method method = task2.getaClass().getMethod(task2.method, task2.parameterTypes);
        Object result = method.invoke(instance, task2.args);
        if (result != null) {
            logger.warn("{}.{} Suggested that do not have a return value", task2.getaClass().getName(), task2.method);
        }
    }

}
