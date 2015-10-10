package com.peaceful.task.container.store.help;

import com.peaceful.task.container.store.FIFOQueueImpl;
import com.peaceful.task.container.store.KeyCreate;
import com.peaceful.task.container.store.NamespaceManage;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * @author WangJun <wangjuntytl@163.com>
 * @version 1.0 15/9/15
 * @since 1.6
 */

public class RemoteFlexibleRegister {

    private static JedisPool jedisPool = FIFOQueueImpl.jedisPool;

    public static Long registerFlexibleTaskToTaskCenter( String value) {
        Jedis jedis = jedisPool.getResource();
        Long res = null;
        try {
            res = jedis.sadd(KeyCreate.get(NamespaceManage.FORCE_TASK_PERSISTENCE_QUEUE), value);
            jedisPool.returnResource(jedis);
        } catch (Exception e) {
            jedisPool.returnBrokenResource(jedis);
        }
        return res;
    }

    public static String getFlexibleTaskFromTaskCenter() {
        Jedis jedis = jedisPool.getResource();
        String res = null;
        try {
            res = jedis.spop(KeyCreate.get(NamespaceManage.FORCE_TASK_PERSISTENCE_QUEUE));
            jedisPool.returnResource(jedis);
        } catch (Exception e) {
            jedisPool.returnBrokenResource(jedis);
        }
        return res;
    }


}
