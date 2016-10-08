package com.peaceful.task.kernal.client;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.google.inject.Inject;
import com.peaceful.task.kernal.TaskClient;
import com.peaceful.task.kernal.TaskController;
import com.peaceful.task.kernal.TaskMonitor;
import com.peaceful.task.kernal.TaskQueue;
import com.peaceful.task.kernal.coding.TU;
import com.peaceful.task.kernal.conf.TaskConfigOps;
import com.peaceful.task.kernal.helper.TaskLog;

/**
 * Created by wangjun on 2016/9/30.
 */
public class TaskClientService implements TaskClient {

    @Inject
    TaskConfigOps ops;
    @Inject
    TaskQueue queue;
    @Inject
    private TaskController controller;
    @Inject
    private TaskMonitor monitor;

    @Override
    public void submit(TU tu) {
        //解决FastJson循环引用的问题
        SerializerFeature feature = SerializerFeature.DisableCircularReferenceDetect;
        tu.setSubmitTime(System.currentTimeMillis());
        String cmd = JSON.toJSONString(tu, feature);
        // 提交到队列服务
        queue.push(ops.name + "-" + tu.queueName, cmd);
        controller.insertTask(tu.queueName);
        monitor.produce(tu);
        TaskLog.SUBMIT_TASK.info("submit task: {}", tu);
    }
}
