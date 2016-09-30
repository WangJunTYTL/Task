package com.peaceful.task.kernal.coding;

import com.alibaba.fastjson.JSON;
import com.google.inject.Inject;
import com.peaceful.common.util.ExceptionUtils;
import com.peaceful.task.kernal.TaskCoding;
import com.peaceful.task.kernal.annotation.Task;
import com.peaceful.task.kernal.coding.decoding.InvokeContext;
import com.peaceful.task.kernal.coding.decoding.Magic;
import com.peaceful.task.kernal.coding.decoding.TypeAdapter;
import com.peaceful.task.kernal.helper.IdGenerate;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

/**
 * @author <a href="mailto:wangjuntytl@163.com">WangJun</a>
 * @version 1.0 16/1/9
 */
public class TaskCodingService implements TaskCoding {

    private Logger logger = LoggerFactory.getLogger(getClass());
    @Inject
    private TypeAdapter typeAdapter;
    @Inject
    private Magic magic;

    /**
     * 把调用信息按TU协议进行编码
     * @param zClass
     * @param method
     * @param args
     * @return
     */
    public TU encoding(Class zClass, Method method, Object[] args) {
        if (method.getDeclaringClass() != Object.class) {
            TU tu = new TU();
            tu.setAclass(zClass);
            tu.setMethod(method.getName());
            tu.setParameterTypes(method.getParameterTypes());
            tu.setArgs(args);
            Task taskAno = method.getAnnotation(Task.class);
            if (taskAno == null) {
                tu.setQueueName(zClass.getSimpleName()+"."+method.getName());
            }else{
                tu.setQueueName(taskAno.value());
                tu.setExecutor(taskAno.executor());
            }
            tu.setId(IdGenerate.getNext(tu.queueName));
            return tu;
        }
        if (method.getReturnType().toString().equals("void")) {
            // ignore
        } else {
            logger.warn("TaskSystem suggested that do not have a return value");
        }
        return null;
    }

    /**
     * 解析TU协议为一个Runnable对象
     * @param taskJson
     * @return
     */

    public TUR decoding(String taskJson) {
        if (StringUtils.isEmpty(taskJson)) return null;
        TU tu = JSON.parseObject(taskJson, TU.class);
        if (tu.aclass == null) {
            logger.error("can't decoding the {} ,may be the task matched class object not in the jvm", taskJson);
            return null;
        }
        try {
            InvokeContext invokeContext = new InvokeContext(tu);
            TUR taskUnit = magic.go(typeAdapter.execute(invokeContext));
            return taskUnit;
        } catch (Exception e) {
            logger.error("decoding task id {}, method {} error {}", tu.id, tu.aclass.getSimpleName() + "." + tu.method, ExceptionUtils.getStackTrace(e));
            return null;
        }
    }


}
