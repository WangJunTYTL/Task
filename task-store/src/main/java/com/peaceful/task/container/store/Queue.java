package com.peaceful.task.container.store;

/**
 * @author WangJun <wangjuntytl@163.com>
 * @version 1.0 15/9/15
 * @since 1.6
 */

public interface Queue {


    void push(String key,String value);

    String pop(String key);

    long size(String key);

    boolean lock(String key);

    boolean isLock(String key);

    boolean openLock(String key);

    boolean getTmpLock(String lockName);
}
