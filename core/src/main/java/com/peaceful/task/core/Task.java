package com.peaceful.task.core;

import com.google.inject.ConfigurationException;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Stage;
import com.peaceful.task.core.conf.TaskConfigOps;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wangjun on 16-8-28.
 */
public class Task {

    private TaskContext context;
    private Map<Class, Object> proxyInstance = new HashMap<Class, Object>();
    private static Map<String, TaskContext> contextMap = new HashMap<String, TaskContext>();

    public synchronized static Task create() {
        Task task = new Task();
        Injector injector = Guice.createInjector(Stage.PRODUCTION, new TaskModule());
        task.context = new NewTaskContext(injector);
        task.contextMap.put(injector.getInstance(TaskConfigOps.class).name, task.context);
        return task;
    }

    /**
     * 注册Java原生类到容器中,通过容器获取到的Java实例,可以通过Task调度器异步执行
     *
     * @param clszz
     * @param <T>
     * @return
     */
    public <T> T registASyncClass(Class<T> clszz) {
        if (proxyInstance.containsKey(clszz)) {
            return (T) proxyInstance.get(clszz);
        } else {
            proxyInstance.put(clszz, context.getTaskProxy().getProxyInstance(clszz));
        }
        return (T) proxyInstance.get(clszz);
    }

    public static TaskContext getTaskContext(String name) {
        return contextMap.get(name);
    }


   /* public void scheduleOnce(long delay, TimeUnit timeUnit, Runnable runnable) {
        AKKA.scheduler().scheduleOnce(FiniteDuration.apply(delay, timeUnit), runnable, AKKA.dispatcher());
    }


    public void schedule(long delay, long interval, TimeUnit timeUnit, Runnable runnable) {
        AKKA.scheduler().schedule(FiniteDuration.apply(delay, timeUnit), FiniteDuration.apply(interval, timeUnit), runnable, AKKA.dispatcher());
    }*/
}
