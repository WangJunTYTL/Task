package com.peaceful.task.context.config;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangjun on 16/1/10.
 */
public class TaskConfigOps {

    // 版本号
    public static final String VERSION = "2.0";

    // 系统名字
    public String name = null;

    //系统启动模式,client or server
    public String bootMode = "client";

    // test or product
    public String developMode = "product";

    public long dispatchTick = 2000;

    //executor module config
    public ExecutorConfigOps executorConfigOps = new ExecutorConfigOps();

    public class ExecutorConfigOps {
        public List<Executor> executorNodeList = new ArrayList<Executor>();

    }

}
