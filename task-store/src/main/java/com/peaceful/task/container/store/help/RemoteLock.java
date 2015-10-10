package com.peaceful.task.container.store.help;

import com.peaceful.task.container.store.FIFOQueueImpl;
import com.peaceful.task.container.store.NamespaceManage;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * @author WangJun <wangjuntytl@163.com>
 * @version 1.0 15/9/22
 * @since 1.6
 */

public class RemoteLock {

    private static JedisPool jedisPool = FIFOQueueImpl.jedisPool;


    private String name;

    public RemoteLock(String name) {
        this.name = NamespaceManage.REMOTE_LOCK_KEY + name;
    }

    // todo 这地方实现还不严谨
    public boolean lock() {
        Jedis jedis = jedisPool.getResource();
        String res = null;
        try {
            res = jedis.getSet(name, "lock");
            jedisPool.returnResource(jedis);
        } catch (Exception e) {
            jedisPool.returnBrokenResource(jedis);
        }
        return res == null ? true : false;
    }

    // todo 这地方实现还不严谨
    public boolean lock(int seconds) {
        Jedis jedis = jedisPool.getResource();
        String res = null;
        try {
            res = jedis.getSet(name, "lock");
            jedis.expire(name,seconds);
            jedisPool.returnResource(jedis);
        } catch (Exception e) {
            jedisPool.returnBrokenResource(jedis);
        }
        return res == null ? true : false;
    }

    // todo 这地方实现还不严谨
    public void unLock() {
        Jedis jedis = jedisPool.getResource();
        try {
            jedis.del(name);
            jedisPool.returnResource(jedis);
        } catch (Exception e) {
            jedis.del(name);
            jedisPool.returnBrokenResource(jedis);
        }
    }
}
