package com.peaceful.task.container.console;


import com.peaceful.task.container.store.TaskStore;

/**
 * @author WangJun <wangjuntytl@163.com>
 * @version 1.0 15/9/30
 * @since 1.6
 */

public class ConsoleImpl implements Console {


    @Override
    public boolean lockTask(String key) {
        return TaskStore.get().lock(key);
    }

    @Override
    public boolean lockTask(String key, int seconds) {
        return TaskStore.get().lock(key, seconds);
    }

    @Override
    public boolean openTask(String key) {
        return TaskStore.get().openLock(key);
    }

    @Override
    public boolean isLock(String key) {
        return TaskStore.get().isLock(key);
    }
}
