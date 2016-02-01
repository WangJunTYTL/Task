package com.peaceful.task.container.store;

import com.peaceful.common.redis.service.JedisPoolService;
import com.peaceful.task.container.store.help.Helper;
import org.apache.commons.lang3.StringUtils;
import org.perf4j.StopWatch;
import org.perf4j.log4j.Log4JStopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * 基于redis list的lpush和rpop实现的FIFO模式的队列
 *
 * @author WangJun <wangjuntytl@163.com>
 * @version 1.0 15/9/15
 * @since 1.6
 */

public class FIFOQueueImpl implements Queue {

    public static JedisPool jedisPool = JedisPoolService.getJedisPoolService().getJedisPoolByHostName("haproxy");
    protected static StopWatch stopWatch = new Log4JStopWatch();
    protected static Logger logger = LoggerFactory.getLogger(FIFOQueueImpl.class);


    private static class Single {
        public static Queue queue = new FIFOQueueImpl();
    }

    public static Queue getInstance() {
        return Single.queue;
    }

    @Override
    public void push(String key, String value) {
        stopWatch.stop();
        Jedis jedis = jedisPool.getResource();
        try {
            jedis.lpush(KeyCreate.get(key), value);
            stopWatch.lap("queue_push");
            jedisPool.returnResource(jedis);
        } catch (Exception e) {
            jedisPool.returnBrokenResource(jedis);
        }
        Helper.remotePushCount.increment(key, 1);
    }

    @Override
    public String pop(String key) {
        stopWatch.stop();
        if (isLock(key)) {
            logger.warn("please note , key is {}, is locked", key);
            return null;
        }
        Jedis jedis = jedisPool.getResource();
        String res = null;
        try {
            res = jedis.rpop(KeyCreate.get(key));
            stopWatch.lap("queue_pop");
            jedisPool.returnResource(jedis);
        } catch (Exception e) {
            jedisPool.returnBrokenResource(jedis);
        }
        Helper.remotePopCount.increment(key, 1);
        return res;
    }

    @Override
    public long size(String key) {
        Jedis jedis = jedisPool.getResource();
        Long res = null;
        try {
            res = jedis.llen(KeyCreate.get(key));
            jedisPool.returnResource(jedis);
        } catch (Exception e) {
            jedisPool.returnBrokenResource(jedis);
        }
        return res;
    }

    @Override
    public boolean lock(String key) {
        key = (KeyCreate.get(key) + "_lock");
        Jedis jedis = jedisPool.getResource();
        try {
            jedis.getSet(key, "1");
            jedisPool.returnResource(jedis);
        } catch (Exception e) {
            jedisPool.returnBrokenResource(jedis);
            return false;
        }
        return true;
    }

    @Override
    public boolean lock(String key, int seconds) {
        key = (KeyCreate.get(key) + "_lock");
        Jedis jedis = jedisPool.getResource();
        try {
            jedis.getSet(key, "1");
            jedis.expire(key, seconds);
            jedisPool.returnResource(jedis);
        } catch (Exception e) {
            jedisPool.returnBrokenResource(jedis);
            return false;
        }
        return true;
    }

    @Override
    public boolean isLock(String key) {
        key = (KeyCreate.get(key) + "_lock");
        Jedis jedis = jedisPool.getResource();
        String res = null;
        try {
            res = jedis.get(key);
            jedisPool.returnResource(jedis);
        } catch (Exception e) {
            jedisPool.returnBrokenResource(jedis);
        }
        if (StringUtils.isNotEmpty(res))
            return true;
        return false;
    }

    @Override
    public boolean openLock(String key) {
        key = (KeyCreate.get(key) + "_lock");
        Jedis jedis = jedisPool.getResource();
        try {
            jedis.del(key);
            jedisPool.returnResource(jedis);
        } catch (Exception e) {
            jedisPool.returnBrokenResource(jedis);
            return false;
        }
        return true;
    }


}
