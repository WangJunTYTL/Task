package com.peaceful.task.core.queue.local;


import com.peaceful.task.core.TaskQueue;

import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * 仅用于本地测试
 * <p/>
 * Created by wangjun on 16/1/13.
 * @since 1.7
 */
public class LocalQueue implements TaskQueue {

    Map<String, java.util.Queue<String>> map = new HashMap<String, java.util.Queue<String>>();

    @Override
    public long size(String name) {
        if (map.containsKey(name)) {
            return map.get(name).size();
        }
        return 0;
    }

    @Override
    public synchronized boolean push(String name, String object) {
        if (map.containsKey(name)) {
            return map.get(name).offer(object);
        } else {
            Queue<String> queue = new ArrayBlockingQueue<String>(1024);
            map.put(name, queue);
            return map.get(name).offer(object);
        }
    }

    @Override
    public String pop(String name) {
        if (map.containsKey(name)) {
            return map.get(name).poll();
        }
        return null;
    }
}
