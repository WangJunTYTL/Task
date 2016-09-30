package com.peaceful.task.kernal.helper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 系统关键性日志
 * Created by wangjun on 16/8/29.
 */
public class TaskLog {

    public static final Logger SUBMIT_TASK = LoggerFactory.getLogger("task.kernal.log.client.submit");
    public static final Logger DISPATCH_TASK = LoggerFactory.getLogger("task.kernal.log.server.dispatch");
    public static final Logger COMPLETE_TASK = LoggerFactory.getLogger("task.kernal.log.server.dispatch.complete");

}
