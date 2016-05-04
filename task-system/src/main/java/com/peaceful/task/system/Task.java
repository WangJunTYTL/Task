package com.peaceful.task.system;

import akka.actor.ActorSystem;
import com.peaceful.task.system.error.AfterStartException;
import com.peaceful.task.system.error.BeforeStartException;
import com.peaceful.common.util.ExceptionUtils;
import com.peaceful.task.context.SimpleTaskContext;
import com.peaceful.task.context.TaskContext;
import com.peaceful.task.context.common.ContextConstant;
import com.peaceful.task.context.config.TaskConfigOps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scala.concurrent.duration.FiniteDuration;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 系统启动入口,你可以以client或server模式启动该系统
 * <p>
 * client: 只提交任务到queue服务中
 * server: 既可以提交任务到queue服务中,同时也会启动executor模块,调度queue中的服务
 *
 * @author WangJun
 * @version 1.0 16/3/29
 */
public class Task {


    private final static ActorSystem AKKA = ActorSystem.create("TASK");

    private final static Logger LOGGER = LoggerFactory.getLogger(Task.class);

    private final static TaskContext CONTEXT = SimpleTaskContext.CONTEXT;

    private final static Map<Class, Object> CLIENT_PROXY_INSTANCE = (Map<Class, Object>) SimpleTaskContext.CONTEXT.get(ContextConstant.CLIENT_PROXY_INSTANCE);

    static {

        // task system pre start
        try {
            // 系统启动前加载的模块
            SystemPreStart.getSingleInstance().execute(CONTEXT);
        } catch (Exception e) {
            LOGGER.error("before starting the system load module failure,{}", ExceptionUtils.getStackTrace(e));
            throw new BeforeStartException(e.getMessage());
        }

        // task system post start
        TaskConfigOps configOps = (TaskConfigOps) CONTEXT.get(ContextConstant.CONFIG);
        if (configOps.bootMode.equals("server")) {
            CONTEXT.put("actorSystem", AKKA);
            try {
                SystemPostStart.getSingleInstance().execute(CONTEXT);
            } catch (Exception e) {
                AKKA.shutdown();
                LOGGER.error("after starting the system load module failure,will shutdown {}", e);
                throw new AfterStartException(e.getMessage());
            }
        }
        LOGGER.info("Your TaskUnit System Successfully Started,Play it as a lightweight Distributed Computing platform!");
    }


    /**
     * 注册Java原生类到容器中,通过容器获取到的Java实例,可以通过Task调度器异步执行
     *
     * @param tClass
     * @param <T>
     * @return
     */
    public static <T> T registerASyncClass(final Class<T> tClass) {
        if (CLIENT_PROXY_INSTANCE.containsKey(tClass)) {
            return (T) CLIENT_PROXY_INSTANCE.get(tClass);
        } else {
            T o = SimpleTaskContext.CLIENT_PROXY.enhancer(tClass);
            CLIENT_PROXY_INSTANCE.put(tClass, o);
            return o;
        }
    }

    public static void scheduleOnce(long delay, TimeUnit timeUnit, Runnable runnable) {
        AKKA.scheduler().scheduleOnce(FiniteDuration.apply(delay, timeUnit), runnable, AKKA.dispatcher());
    }


    public static void scheduleOnce(long delay, long interval, TimeUnit timeUnit, Runnable runnable) {
        AKKA.scheduler().schedule(FiniteDuration.apply(delay, timeUnit), FiniteDuration.apply(interval, timeUnit), runnable, AKKA.dispatcher());
    }

    public static void loadToJvm(){
        // pass
    }


}
