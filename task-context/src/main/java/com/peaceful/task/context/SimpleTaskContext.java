package com.peaceful.task.context;

import com.peaceful.task.context.common.ContextConstant;
import com.peaceful.task.context.config.ConfigParseChain;
import com.peaceful.task.context.config.TaskConfigOps;
import com.peaceful.task.context.dispatch.RedisCloudTaskController;
import com.peaceful.task.context.dispatch.TaskBeanFactoryImpl;
import com.peaceful.task.context.coding.TaskClientProxyImpl;
import com.peaceful.task.context.monitor.CloudTaskCountMonitor;
import com.peaceful.common.util.SPI;
import com.peaceful.common.util.chain.BaseContext;
import com.peaceful.common.util.chain.Context;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * 系统启动入口,你可以以client或server模式启动该系统
 * <p>
 * client: 只提交任务到queue服务中
 * server: 既可以提交任务到queue服务中,同时也会启动executor模块,调度queue中的服务
 *
 * @author WangJun
 * @version 1.0 16/3/29
 */
public class SimpleTaskContext extends TaskContext {


    private static String configFile = "task.conf";

    // Task系统上下文
    public final static TaskContext CONTEXT = new SimpleTaskContext();
    // 任务提交代理工具类实例
    private final static Map<Class, Object> CLIENT_PROXY_INSTANCE = new ConcurrentHashMap<Class, Object>();
    // 任务提交代理工具类
    public final static TaskClientProxy CLIENT_PROXY = new TaskClientProxyImpl();
    // 任务编码器
    public final static TaskCoding CODING = SPI.search(TaskCoding.class);
    // 任务队列存储器
    public static final TaskQueue<String> QUEUE = SPI.search(TaskQueue.class);
    // 任务bean获取工厂
    public static final TaskBeanFactory BEAN_FACTORY = new TaskBeanFactoryImpl();
    //公共线程池
    private static final ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(8);

    private final static Logger LOGGER = LoggerFactory.getLogger(SimpleTaskContext.class);


    static {

        Context configContext = new BaseContext();
        try {
            ConfigParseChain.getSingleInstance().execute(configContext);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        TaskConfigOps taskConfigOps = (TaskConfigOps) SimpleTaskContext.CONTEXT.get(ContextConstant.CONFIG);
        String bootMode = System.getProperty("bootMode");
        if (StringUtils.isNotEmpty(bootMode)) {
            if (bootMode.equals("server")) {
                taskConfigOps.bootMode = "server";
            } else if (bootMode.equals("client")) {
                taskConfigOps.bootMode = "client";
            } else {
                throw new RuntimeException("bootMode parameter error. client or server");
            }
        }

        CONTEXT.put(ContextConstant.PUBLICE_SCHEDULE, scheduledExecutorService);
        CONTEXT.put(ContextConstant.CONTROLLER, new RedisCloudTaskController());
        CONTEXT.put(ContextConstant.MONITOR, new CloudTaskCountMonitor());
        CONTEXT.put(ContextConstant.CODING, CODING);
        CONTEXT.put(ContextConstant.QUEUE, QUEUE);
        CONTEXT.put(ContextConstant.CLIENT_PROXY, CLIENT_PROXY);
        CONTEXT.put(ContextConstant.BEAN_FACTORY, BEAN_FACTORY);
        CONTEXT.put(ContextConstant.CLIENT_PROXY_INSTANCE, CLIENT_PROXY_INSTANCE);


    }


}
