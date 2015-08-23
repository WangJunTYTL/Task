package cn.edaijia.task.container.job;

import cn.edaijia.task.container.common.IdGenerate;
import cn.edaijia.task.container.common.Task;
import cn.edaijia.task.container.common.TaskContainerConf;
import cn.edaijia.task.container.exception.NotFindQueueException;
import cn.edaijia.task.container.msg.Task2;
import cn.edaijia.task.container.repo.TaskQueue;
import com.alibaba.fastjson.JSON;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.InvocationHandler;

import java.lang.reflect.Method;
import java.util.IdentityHashMap;
import java.util.Map;

/**
 * @author WangJun <wangjuntytl@163.com>
 * @version 1.0 15/8/16
 * @since 1.6
 */

public abstract class TaskSchedule {

    static Map<Class, Object> scheduleProcessMap = new IdentityHashMap<Class, Object>();

    public static <T> T registerASyncClass(final Class<T> tClass) {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(tClass);
        enhancer.setCallback(new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args)
                    throws Throwable {
                if (method.getDeclaringClass() != Object.class) {
                    Task2 task2 = new Task2();
                    task2.setaClass(tClass);
                    task2.setMethod(method.getName());
                    task2.setParameterTypes(method.getParameterTypes());
                    task2.setArgs(args);
                    Task task = method.getAnnotation(Task.class);
                    if (task == null)
                        task2.setQueueName("commonQueue");
                    else {
                        if (TaskContainerConf.getConf().queues.contains(task.queue())) {
                            task2.id = IdGenerate.getNext();
                        } else {
                            throw new NotFindQueueException(task.queue());
                        }
                        task2.setQueueName(task.queue());
                    }
                    TaskQueue.lpush(task2.queueName, JSON.toJSONString(task2));
                } else {
                    return method.invoke(this, args);
                }
                return "task container Suggested that do not have a return value";
            }
        });
        T t = (T) enhancer.create();
        scheduleProcessMap.put(tClass, t);
        return t;
    }

}
