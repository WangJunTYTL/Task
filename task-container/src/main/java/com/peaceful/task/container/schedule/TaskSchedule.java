package com.peaceful.task.container.schedule;

import akka.actor.Cancellable;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.peaceful.task.container.TaskContainer;
import com.peaceful.task.container.common.Constant;
import com.peaceful.task.container.common.IdGenerate;
import com.peaceful.task.container.common.Task;
import com.peaceful.task.container.common.TaskContainerConf;
import com.peaceful.task.container.dispatch.DispatchContainer;
import com.peaceful.task.container.exception.NotFindQueueException;
import com.peaceful.task.container.invoke.InvokeContext;
import com.peaceful.task.container.invoke.trace.InvokeTrace;
import com.peaceful.task.container.msg.Task2;
import com.peaceful.task.container.repo.TaskQueue;
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
 * 任务获取与调度中心
 *
 * @author WangJun <wangjuntytl@163.com>
 * @version 1.0 15/8/16
 * @since 1.6
 */

public abstract class TaskSchedule {

    private static Map<Class, Object> scheduleProcessMap = new IdentityHashMap<Class, Object>();
    private static Logger logger = LoggerFactory.getLogger(TaskSchedule.class);

    /**
     * 利用动态代理打断Java的原生方法的执行，获取运行时的调用信息，并序列化为json数据提交到任务存储中心
     * 此处动态代理技术有CGLIB提供，你不必编写代理对象的接口
     *
     * @param tClass
     * @param <T>
     * @return
     */
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
                    String forceName = Schedule.forceChangeTaskName.get();
                    if (forceName != null) {
                        if (task != null) {
                            logger.debug("force change task name {} -> {}", task.value(), forceName);
                        }
                        TaskContainerConf.getConf().flexibleTasks.add(forceName);
                        TaskQueue.persistenceForceTask(Constant.FORCE_TASK_PERSISTENCE_QUEUE, forceName);
                        task2.setQueueName(Constant.FORCE_TASK_NAME_PREFIX + forceName);
                    } else if (task == null)
                        task2.setQueueName("commonQueue");
                    else {
                        if (TaskContainerConf.getConf().focusedTasks.contains(task.value())) {
                        } else {
                            throw new NotFindQueueException(task.value());
                        }
                        task2.setQueueName(task.value());
                    }
                    task2.id = IdGenerate.getNext();
                    //解决FastJson循环引用的问题
                    SerializerFeature feature = SerializerFeature.DisableCircularReferenceDetect;
                    TaskQueue.push(task2.queueName, JSON.toJSONString(task2, feature));
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

    /**
     * 根据一个安全正确的Task2任务单元，去执行正确的方法
     *
     * @param task2
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws ClassNotFoundException
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     */
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

    /**
     * 任务调度：在指定时刻调度一次
     *
     * @param delay
     * @param timeUnit
     * @param runnable
     */
    public static void scheduleOnce(long delay, TimeUnit timeUnit, Runnable runnable) {
        TaskContainer.getSystem().scheduler().scheduleOnce(Duration.create(delay, timeUnit), runnable, TaskContainer.getSystem().dispatcher());
    }

    /**
     * 可以取消的任务调度：在指定时刻按固定周期调度
     *
     * @param startTime
     * @param delay
     * @param timeUnit
     * @param runnable
     * @return
     */
    public static Cancellable schedule(FiniteDuration startTime, long delay, TimeUnit timeUnit, Runnable runnable) {
        return TaskContainer.getSystem().scheduler().schedule(startTime, Duration.create(delay, timeUnit), runnable, TaskContainer.getSystem().dispatcher());
    }


    public static class Schedule {
        // 强制更改任务名称，使其可以进入到别的队列
        public static ThreadLocal<String> forceChangeTaskName = new ThreadLocal<String>();


    }


}
