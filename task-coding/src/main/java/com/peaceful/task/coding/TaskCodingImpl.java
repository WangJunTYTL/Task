package com.peaceful.task.coding;

import com.peaceful.task.coding.decoding.InvokeContext;
import com.peaceful.task.coding.decoding.Magic;
import com.peaceful.task.coding.decoding.TypeAdapter;
import com.alibaba.fastjson.JSON;
import com.peaceful.common.util.ExceptionUtils;
import com.peaceful.common.util.chain.BaseContext;
import com.peaceful.common.util.chain.Context;
import com.peaceful.task.context.TaskCoding;
import com.peaceful.task.context.annotation.Task;
import com.peaceful.task.context.coding.TU;
import com.peaceful.task.context.coding.TUR;
import com.peaceful.task.context.helper.IdGenerate;
import org.apache.commons.lang3.StringUtils;
import org.perf4j.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

/**
 * @author <a href="mailto:wangjuntytl@163.com">WangJun</a>
 * @version 1.0 16/1/9
 */
public class TaskCodingImpl implements TaskCoding {

    Logger logger = LoggerFactory.getLogger(TaskCodingImpl.class);
    TypeAdapter typeAdapter = new TypeAdapter();
    Magic magic = new Magic();

    public TU encoding(Class zClass, Method method, Object[] args) {
        if (method.getDeclaringClass() != Object.class) {
            TU task = new TU();
            task.setAclass(zClass);
            task.setMethod(method.getName());
            task.setParameterTypes(method.getParameterTypes());
            task.setArgs(args);
            Task taskAno = method.getAnnotation(Task.class);
            if (taskAno != null) {
                task.setQueueName(taskAno.value());
                task.setExecutor(taskAno.executor());
            }else{
                task.setQueueName(zClass.getSimpleName()+"."+method.getName());
            }
            task.setId(IdGenerate.getNext(task.queueName));
            return task;
        }
        if (method.getReturnType().toString().equals("void")) {

        } else {
            logger.warn("task system suggested that do not have a return value");
        }

        return null;
        // todo forename

    }

    public TUR decoding(String taskJson) {
        if (StringUtils.isEmpty(taskJson)) return null;
        TU task = JSON.parseObject(taskJson, TU.class);
        Context context = new BaseContext();
        context.put("taskUnit", task);
        context.put("stopWatch", new StopWatch("QUEUE.POP"));
        if (task.aclass == null) {
            logger.error("can't decoding the {} ,may be the task matched class object not in the jvm", taskJson);
            return null;
        }

        try {
            InvokeContext invokeContext = new InvokeContext(task);
            TUR taskUnit = magic.go(typeAdapter.execute(invokeContext));
            // 后续处理链
//            taskDecodingChain.execute(context);
            return taskUnit;
        } catch (Exception e) {
            logger.error("decoding task id {}, method {} error {}", task.id, task.aclass.getSimpleName() + "." + task.method, ExceptionUtils.getStackTrace(e));
            return null;
        }
    }


}
