package com.peaceful.task.container.store;

import org.perf4j.StopWatch;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * @author WangJun <wangjuntytl@163.com>
 * @version 1.0 15/9/15
 * @since 1.6
 */

public class FlexibleRegist {

    private static JedisPool jedisPool = FIFOQueueImpl.jedisPool;
    private static StopWatch stopWatch = FIFOQueueImpl.stopWatch;


    public static Long registFlexibleTaskToTaskCenter(String key, String value) {
        stopWatch.stop();
        Jedis jedis = jedisPool.getResource();
        Long res = null;
        try {
            res = jedis.sadd(KeyCreate.get(key), value);
            stopWatch.lap("queue_push");
            jedisPool.returnResource(jedis);
        } catch (Exception e) {
            jedisPool.returnBrokenResource(jedis);
        }
        return res;
    }

    public static String getFlexbleTaskFromTaskCenter(String key) {
        stopWatch.stop();
        Jedis jedis = jedisPool.getResource();
        String res = null;
        try {
            res = jedis.spop(KeyCreate.get(key));
            stopWatch.lap("queue_push");
            jedisPool.returnResource(jedis);
        } catch (Exception e) {
            jedisPool.returnBrokenResource(jedis);
        }
        return res;
    }


}
