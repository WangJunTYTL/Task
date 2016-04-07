package com.peaceful.task.context.coding;

import com.peaceful.task.context.SimpleTaskContext;
import com.peaceful.task.context.TaskClientProxy;
import com.peaceful.task.context.TaskMonitor;
import com.peaceful.task.context.common.ContextConstant;
import com.peaceful.task.context.config.TaskConfigOps;
import com.peaceful.task.context.TaskController;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.peaceful.common.util.ExceptionUtils;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.InvocationHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

/**
 * 通过代理模式将方法调用动作编码成任务对象并序列化成json对象提交任务到任务队列中
 *
 * @author <a href="mailto:wangjuntytl@163.com">WangJun</a>
 * @version 1.0 16/1/9
 */
public class TaskClientProxyImpl implements TaskClientProxy {

    private static Logger logger = LoggerFactory.getLogger(TaskClientProxyImpl.class);

    @Override
    public <T> T enhancer(final Class<T> tClass) {
        final TaskConfigOps taskConfigOps = (TaskConfigOps) SimpleTaskContext.CONTEXT.get(ContextConstant.CONFIG);
        final TaskController controller = (TaskController) SimpleTaskContext.CONTEXT.get("controller");
        final TaskMonitor monitor = (TaskMonitor) SimpleTaskContext.CONTEXT.get(ContextConstant.MONITOR);
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(tClass);
        enhancer.setCallback(new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args)
                    throws Throwable {
                // 调用信息编码
                TU task02 = SimpleTaskContext.CODING.encoding(tClass, method, args);
                if (task02 != null) {
                    //解决FastJson循环引用的问题
                    SerializerFeature feature = SerializerFeature.DisableCircularReferenceDetect;
                    task02.setSubmitTime(System.currentTimeMillis());
                    String cmd = JSON.toJSONString(task02, feature);
                    try {
                        // 提交到队列服务
                        SimpleTaskContext.QUEUE.push("TASK-" + taskConfigOps.name + "-" + task02.queueName, cmd);
                        controller.insertTask(task02.queueName);
                        monitor.produce(task02);
                        logger.info("submit task {}", task02);
                    } catch (Exception e) {
                        logger.info("task queue service error,{}", ExceptionUtils.getStackTrace(e));
                        throw new RuntimeException("queue service error", e);
                    }
                }
                return null;
            }
        });
        T t = (T) enhancer.create();
        return t;
    }
}
