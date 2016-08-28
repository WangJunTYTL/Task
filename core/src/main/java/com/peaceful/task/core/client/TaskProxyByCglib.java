package com.peaceful.task.core.client;

import com.google.inject.Inject;
import com.peaceful.task.core.*;
import com.peaceful.task.core.coding.TU;
import com.peaceful.task.core.conf.TaskConfigOps;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.peaceful.common.util.ExceptionUtils;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.InvocationHandler;
import org.perf4j.StopWatch;
import org.perf4j.slf4j.Slf4JStopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

/**
 * 通过代理将方法调用编码成任务对象并序列化成json对象提交任务到任务队列中
 *
 * @author <a href="mailto:wangjuntytl@163.com">WangJun</a>
 * @version 1.0 16/1/9
 */
public class TaskProxyByCglib implements TaskProxy {

    private static Logger logger = LoggerFactory.getLogger(TaskProxyByCglib.class);
    @Inject
    private TaskConfigOps ops;
    @Inject
    private TaskController controller;
    @Inject
    private TaskCoding coding;
    @Inject
    private TaskQueue queue;
    @Inject
    private TaskMonitor monitor;

    @Override
    public <T> T getProxyInstance(final Class<T> tClass) {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(tClass);
        enhancer.setCallback(new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args)
                    throws Throwable {
                StopWatch watch = new Slf4JStopWatch();
                // 调用信息编码
                TU task02 = coding.encoding(tClass, method, args);
                if (task02 != null) {
                    try {
                        //解决FastJson循环引用的问题
                        SerializerFeature feature = SerializerFeature.DisableCircularReferenceDetect;
                        task02.setSubmitTime(System.currentTimeMillis());
                        String cmd = JSON.toJSONString(task02, feature);
                        // 提交到队列服务
                        queue.push(ops.name + "-" + task02.queueName, cmd);
                        controller.insertTask(task02.queueName);
                        monitor.produce(task02);
                        logger.debug("submit task {}", task02);
                        watch.stop("TASK.PRODUCE");
                        watch.stop("task." + task02.queueName + ".produce");
                    } catch (Exception e) {
                        logger.error("task submit fail -> {}", ExceptionUtils.getStackTrace(e));
                        throw new RuntimeException("task submit fail", e);
                    }
                }
                return null;
            }
        });
        T t = (T) enhancer.create();
        return t;
    }
}
