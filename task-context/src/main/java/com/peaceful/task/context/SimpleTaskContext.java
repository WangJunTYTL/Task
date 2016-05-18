package com.peaceful.task.context;

import com.peaceful.common.util.ClassUtils;
import com.peaceful.common.util.SPI;
import com.peaceful.common.util.chain.BaseContext;
import com.peaceful.common.util.chain.Context;
import com.peaceful.task.context.coding.TaskClientProxyImpl;
import com.peaceful.task.context.common.ContextConstant;
import com.peaceful.task.context.config.ConfigParseChain;
import com.peaceful.task.context.config.TaskConfigOps;
import com.peaceful.task.context.dispatch.RedisCloudTaskController;
import com.peaceful.task.context.monitor.CloudTaskCountMonitor;
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
    //公共线程池
    private static final ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(8);
    // 被代理类实例容器
    private final static Map<Class, Object> CLIENT_PROXY_INSTANCE = new ConcurrentHashMap<Class, Object>();
    // 任务提交客户端代理工具类
    public static TaskClientProxy CLIENT_PROXY = null;
    // 任务编码器
    public static TaskCoding CODING = null;
    // 任务队列存储器
    public static TaskQueue<String> QUEUE = null;
    // 任务bean实例管理工厂
    public static TaskBeanFactory BEAN_FACTORY = null;

    private final static Logger LOGGER = LoggerFactory.getLogger(SimpleTaskContext.class);


    static {

        Context configContext = new BaseContext();
        try {
            ConfigParseChain.getSingleInstance().execute(configContext);
        } catch (Exception e) {
            LOGGER.error("init task config fail -> {}", com.peaceful.common.util.ExceptionUtils.getStackTrace(e));
            System.exit(1);
        }
        TaskConfigOps taskConfigOps = (TaskConfigOps) SimpleTaskContext.CONTEXT.get(ContextConstant.CONFIG);
        String bootMode = System.getProperty("bootMode");
        if (StringUtils.isNotEmpty(bootMode)) {
            if (bootMode.equals("server")) {
                taskConfigOps.bootMode = "server";
            } else if (bootMode.equals("client")) {
                taskConfigOps.bootMode = "client";
            } else {
                LOGGER.error("bootMode option can only be client or server,system will shutdown ");
                System.exit(1);
            }
        }

        CONTEXT.put(ContextConstant.PUBLICE_SCHEDULE, scheduledExecutorService);
        CONTEXT.put(ContextConstant.CLIENT_PROXY_INSTANCE, CLIENT_PROXY_INSTANCE);

        try {
            QUEUE = ClassUtils.newInstance(taskConfigOps.queue);
            CONTEXT.put(ContextConstant.QUEUE, QUEUE);
        } catch (Exception e) {
            LOGGER.error("init instance {} fail,please check it,system will shutdown!", taskConfigOps.queue);
            System.exit(1);
        }

        try {
            CLIENT_PROXY = new TaskClientProxyImpl();
            CONTEXT.put(ContextConstant.CLIENT_PROXY, CLIENT_PROXY);
        } catch (Exception e) {
            LOGGER.error("init task client proxy fail", com.peaceful.common.util.ExceptionUtils.getStackTrace(e));
            System.exit(1);
        }

        try {
            CONTEXT.put(ContextConstant.CONTROLLER, new RedisCloudTaskController());
        } catch (Exception e) {
            LOGGER.error("init task controller module fail ->{}", com.peaceful.common.util.ExceptionUtils.getStackTrace(e));
            System.exit(1);
        }

        try {
            CONTEXT.put(ContextConstant.MONITOR, new CloudTaskCountMonitor());
        } catch (Exception e) {
            LOGGER.error("init task monitor module fail ->{}", com.peaceful.common.util.ExceptionUtils.getStackTrace(e));
            System.exit(1);
        }

        try {
            CODING = SPI.selectOne(TaskCoding.class);
            CONTEXT.put(ContextConstant.CODING, CODING);
        } catch (Exception e) {
            LOGGER.error("init task CODING module fail ->{}", com.peaceful.common.util.ExceptionUtils.getStackTrace(e));
            System.exit(1);
        }

        try {
            BEAN_FACTORY = ClassUtils.newInstance(taskConfigOps.beanFactory);
            CONTEXT.put(ContextConstant.BEAN_FACTORY, BEAN_FACTORY);
        } catch (Exception e) {
            LOGGER.error("init task CODING module fail ->{}", com.peaceful.common.util.ExceptionUtils.getStackTrace(e));
            System.exit(1);
        }
    }

}
