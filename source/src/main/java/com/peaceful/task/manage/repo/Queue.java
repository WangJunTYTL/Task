package com.peaceful.task.manage.repo;

import com.peaceful.task.manage.common.TaskManageLogger;
import com.peaceful.task.manage.common.TaskManageConf;
import com.peaceful.common.redis.proxy.JedisPoolServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.perf4j.StopWatch;
import org.perf4j.log4j.Log4JStopWatch;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * @author WangJun <wangjuntytl@163.com>
 * @version 1.0 15/5/15
 * @since 1.6
 */

public class Queue {

    private static JedisPool jedisPool = JedisPoolServiceImpl.getJedisPoolService().getJedisPoolByHostName(TaskManageConf.getConf().redisNode);
    private static StopWatch stopWatch = new Log4JStopWatch();

    public static String rpop(String key) {
        stopWatch.stop();
        if (isLock(key)) {
            TaskManageLogger.LOGGER.warn("please note , key is {}, is locked", key);
            return null;
        }
        key += ("_" + TaskManageConf.getConf().projectName);
        Jedis jedis = jedisPool.getResource();
        String res = null;
        try {
            res = jedis.rpop(key);
            stopWatch.lap("queue_pop");
            jedisPool.returnResource(jedis);
        } catch (Exception e) {
            jedisPool.returnBrokenResource(jedis);
        }
        return res;

    }

    public static Long lpush(String key, String value) {
        stopWatch.stop();
        key += ("_" + TaskManageConf.getConf().projectName);
        Jedis jedis = jedisPool.getResource();
        Long res = null;
        try {
            res = jedis.lpush(key, value);
            stopWatch.lap("queue_push");
            jedisPool.returnResource(jedis);
        } catch (Exception e) {
            jedisPool.returnBrokenResource(jedis);
        }
        return res;

    }

    public static Long llen(String key) {
        key += ("_" + TaskManageConf.getConf().projectName);
        Jedis jedis = jedisPool.getResource();
        Long res = null;
        try {
            res = jedis.llen(key);
            jedisPool.returnResource(jedis);
        } catch (Exception e) {
            jedisPool.returnBrokenResource(jedis);
        }
        return res;

    }

    public static String lock(String key) {
        key += ("_" + TaskManageConf.getConf().projectName + "_lock");
        Jedis jedis = jedisPool.getResource();
        String res = null;
        try {
            res = jedis.getSet(key, "1");
            jedisPool.returnResource(jedis);
        } catch (Exception e) {
            jedisPool.returnBrokenResource(jedis);
        }
        return res;
    }

    public static boolean isLock(String key) {
        key += ("_" + TaskManageConf.getConf().projectName + "_lock");
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

    public static Long unLock(String key) {
        key += ("_" + TaskManageConf.getConf().projectName + "_lock");
        Jedis jedis = jedisPool.getResource();
        Long res = null;
        try {
            res = jedis.del(key);
            jedisPool.returnResource(jedis);
        } catch (Exception e) {
            jedisPool.returnBrokenResource(jedis);
        }
        return res;
    }
}
