package com.peaceful.task.kernal.queue.redis;

import com.peaceful.common.redis.service.Redis;
import com.peaceful.task.kernal.TaskQueue;

/**
 * @author <a href="mailto:wangjuntytl@163.com">WangJun</a>
 * @version 1.0 16/1/10
 */
public class RedisQueue implements TaskQueue {

    @Override
    public boolean push(String key, String object) {
        Redis.cmd().lpush(key, object);
        return true;
    }

    @Override
    public String pop(String key) {
        return Redis.cmd().rpop(key);
    }

    @Override
    public long size(String key) {
        return Redis.cmd().llen(key);
    }
}
