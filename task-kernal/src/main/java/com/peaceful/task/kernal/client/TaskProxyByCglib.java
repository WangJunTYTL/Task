package com.peaceful.task.kernal.client;

import com.google.inject.Inject;
import com.peaceful.task.kernal.*;
import com.peaceful.task.kernal.coding.TU;
import com.peaceful.task.kernal.conf.TaskConfigOps;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.peaceful.common.util.ExceptionUtils;
import com.peaceful.task.kernal.helper.TaskLog;
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
    private TaskClient client;
    @Inject
    private TaskCoding coding;

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
                TU tu = coding.encoding(tClass, method, args);
                if (tu != null) {
                    try {
                        client.submit(tu);
                        watch.stop("task.produce");
                        watch.stop("task." + tu.queueName + ".produce");
                    } catch (Exception e) {
                        TaskLog.SUBMIT_TASK.error("task submit fail:{},cause:{}", tu.toString(), ExceptionUtils.getStackTrace(e));
                        throw new RuntimeException("task submit fail:" + tu, e);
                    }
                }
                return null;
            }
        });
        T t = (T) enhancer.create();
        return t;
    }
}
