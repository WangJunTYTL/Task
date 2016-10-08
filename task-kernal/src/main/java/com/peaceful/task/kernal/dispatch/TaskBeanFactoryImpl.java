package com.peaceful.task.kernal.dispatch;

import com.peaceful.task.kernal.TaskBeanFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author WangJun
 * @version 1.0 16/3/31
 */
public class TaskBeanFactoryImpl implements TaskBeanFactory {

    // 任务对象实例容器
    private final static Map<Class, Object> ASYNC_TASK_INSTANCE = new ConcurrentHashMap<Class, Object>();

    private final static Logger LOGGER = LoggerFactory.getLogger(TaskBeanFactoryImpl.class);

    @Override
    public <T> T getBean(Class<T> zclass) {
        if (ASYNC_TASK_INSTANCE.containsKey(zclass)) {
            return (T) ASYNC_TASK_INSTANCE.get(zclass);
        } else {
            try {
                Object instance = zclass.newInstance();
                LOGGER.debug("auto create {} instance OK...", zclass.getName());
                ASYNC_TASK_INSTANCE.put(zclass, instance);
                return (T) instance;
            } catch (InstantiationException e) {
                LOGGER.error("Error:{}", e);
            } catch (IllegalAccessException e) {
                LOGGER.error("Error:{}", e);
            }
        }
        return null;
    }

    @Override
    public void newInstance(Class zClass,Object instance) {
        if (!ASYNC_TASK_INSTANCE.containsKey(zClass)) {
            ASYNC_TASK_INSTANCE.put(zClass, instance);
            LOGGER.info("new instance info ,insert instance suc {}", zClass);
        } else {
            LOGGER.warn("new instance warn , instance {} is exist", zClass);
        }
    }
}
