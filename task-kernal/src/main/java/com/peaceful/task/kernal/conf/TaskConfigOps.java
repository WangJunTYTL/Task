package com.peaceful.task.kernal.conf;

import com.google.inject.Singleton;
import com.peaceful.task.kernal.helper.NetHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangjun on 16/1/10.
 */
@Singleton
public class TaskConfigOps {

    // 当前系统版本号
    public static final String VERSION = "2.6";
    // 系统名称，用于区分多个系统
    public String name = null;
    //系统启动模式,client or server
    public String bootMode = "client";
    // TaskQueue 任务队列实现
    public String queue;
    // BeanFactory实现
    public String beanFactory;
    // test or product
    public String developMode = "product";
    //executor module conf
    public ExecutorConfigOps executorConfigOps = new ExecutorConfigOps();
    public class ExecutorConfigOps {
        public List<Executor> executorNodeList = new ArrayList<Executor>();

        @Override
        public String toString() {
            StringBuffer buffer = new StringBuffer();
            buffer.append("[").append("\n");
            for (Executor executor:executorNodeList){
                buffer.append("\t").append(executor.name).append("->").append(executor.implementation).append("\n");
            }
            buffer.append("]");
            return buffer.toString();
        }
    }

    @Override
    public String toString() {
        return "Task Kernal["+VERSION+"]"+ " Name["+name+"]";
    }

    public String detail(){
        StringBuffer buffer = new StringBuffer();
        buffer.append("----------------------Task Kernal--------------------------------------").append("\n");
        buffer.append(toString()).append("\n");
        buffer.append("hostname:").append(NetHelper.getHostname()).append("\n");
        buffer.append("task.boot.mode:").append(bootMode).append("\n");
        buffer.append("task.queue.impl:").append(queue).append("\n");
        buffer.append("task.bean.factory.impl:").append(beanFactory).append("\n");
        buffer.append("task.executor.list:").append(executorConfigOps).append("\n");
        buffer.append("-----------------------------------------------------------------------");
        return buffer.toString();
    }
}
