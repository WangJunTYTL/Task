package com.peaceful.task.context.common;

import com.peaceful.task.context.SimpleTaskContext;

/**
 * @author WangJun
 * @version 1.0 16/3/29
 */
public class TaskClassLoader {


    public static ClassLoader getClassLoader() {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        if (loader == null) {
            loader = SimpleTaskContext.class.getClassLoader();
        }
        return loader;
    }
}
