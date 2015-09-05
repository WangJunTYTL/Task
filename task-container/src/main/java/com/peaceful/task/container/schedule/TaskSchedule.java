package com.peaceful.task.container.schedule;

import akka.actor.Cancellable;
import com.peaceful.task.container.TaskContainer;
import com.peaceful.task.container.common.IdGenerate;
import com.peaceful.task.container.common.Task;
import com.peaceful.task.container.common.TaskContainerConf;
import com.peaceful.task.container.dispatch.DispatchContainer;
import com.peaceful.task.container.exception.NotFindQueueException;
import com.peaceful.task.container.invoke.InvokeContext;
import com.peaceful.task.container.invoke.trace.InvokeTrace;
import com.peaceful.task.container.msg.Task2;
import com.peaceful.task.container.repo.TaskQueue;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.peaceful.common.util.Util;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.InvocationHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scala.concurrent.duration.Duration;
import scala.concurrent.duration.FiniteDuration;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author WangJun <wangjuntytl@163.com>
 * @version 1.0 15/8/16
 * @since 1.6
 */

public abstract class TaskSchedule {

    private static Map<Class, Object> scheduleProcessMap = new IdentityHashMap<Class, Object>();
    private static Logger logger = LoggerFactory.getLogger(TaskSchedule.class);

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
                    //解决FastJson循环引用的问题
                    SerializerFeature feature = SerializerFeature.DisableCircularReferenceDetect;
                    Util.report(JSON.toJSONString(task2, feature));
                    TaskQueue.lpush(task2.queueName, JSON.toJSONString(task2, feature));
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

    public static void invoke(Task2 task2) throws IllegalAccessException, InstantiationException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException {
        InvokeContext invokeContext = new InvokeContext(task2);
        try {
            InvokeTrace.start.execute(invokeContext);
            DispatchContainer.getHandlerChain().execute(invokeContext);
            InvokeTrace.end.execute(invokeContext);
        } catch (Exception e) {
            logger.error("task2 invoke error {}", e);
        }
    }

    public static void scheduleOnce(long delay, TimeUnit timeUnit, Runnable runnable) {
        TaskContainer.getSystem().scheduler().scheduleOnce(Duration.create(delay, timeUnit), runnable, TaskContainer.getSystem().dispatcher());
    }

    public static Cancellable schedule(FiniteDuration startTime, long delay, TimeUnit timeUnit, Runnable runnable) {
        return TaskContainer.getSystem().scheduler().schedule(startTime, Duration.create(delay, timeUnit), runnable, TaskContainer.getSystem().dispatcher());
    }


}
