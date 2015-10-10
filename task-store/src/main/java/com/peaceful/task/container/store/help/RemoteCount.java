package com.peaceful.task.container.store.help;

import com.peaceful.task.container.store.FIFOQueueImpl;
import org.apache.commons.lang3.StringUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * 基于redis的远程计数器
 *
 * @author WangJun <wangjuntytl@163.com>
 * @version 1.0 15/9/16
 * @since 1.6
 */

public class RemoteCount {

    private static JedisPool jedisPool = FIFOQueueImpl.jedisPool;

    private String namespace;

    public RemoteCount(String namespace) {
        this.namespace = namespace;
    }


    /**
     * 指定计数器增加指定value
     *
     * @param key   计数器名字
     * @param value 增加值
     * @return 返回旧值
     */
    public long increment(String key, long value) {
        Jedis jedis = jedisPool.getResource();
        Long res = null;
        try {
            res = jedis.incrBy(namespace + key, value);
            jedisPool.returnResource(jedis);
        } catch (Exception e) {
            jedisPool.returnBrokenResource(jedis);
        }
        return res;
    }

    /**
     * 获取指定技术器的值，并移除当前计数器
     *
     * @param key
     * @return
     */
    public long getAndRemove(String key) {
        Jedis jedis = jedisPool.getResource();
        Long res = null;
        try {
            String res_ = jedis.get(namespace + key);
            jedis.del(namespace + key);
            if (StringUtils.isBlank(res_)) res_ = "0";
            res = Long.valueOf(res_);
            jedisPool.returnResource(jedis);
        } catch (Exception e) {
            jedisPool.returnBrokenResource(jedis);
        }
        if (res == null) return 0;
        return res;
    }

    /**
     * 获取指定技术器的值，并移除当前计数器
     *
     * @param key
     * @return
     */
    public long get(String key) {
        Jedis jedis = jedisPool.getResource();
        Long res = null;
        try {
            String res_ = jedis.get(namespace + key);
            if (StringUtils.isBlank(res_)) res_ = "0";
            res = Long.valueOf(res_);
            jedisPool.returnResource(jedis);
        } catch (Exception e) {
            jedisPool.returnBrokenResource(jedis);
        }
        if (res == null) return 0;
        return res;
    }


}
