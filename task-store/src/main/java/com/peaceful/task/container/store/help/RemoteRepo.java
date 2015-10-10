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

public class RemoteRepo {

    private static JedisPool jedisPool = FIFOQueueImpl.jedisPool;


    private String name;

    public RemoteRepo(String name) {
        this.name = NamespaceManage.REMOTE_REPO_KEY + name;
    }

    public void set(String value) {
        Jedis jedis = jedisPool.getResource();
        try {
            jedis.set(name, value);
            jedisPool.returnResource(jedis);
        } catch (Exception e) {
            jedisPool.returnBrokenResource(jedis);
        }
    }

    public String get() {
        Jedis jedis = jedisPool.getResource();
        String res = null;
        try {
            res = jedis.get(name);
            jedisPool.returnResource(jedis);
        } catch (Exception e) {
            jedisPool.returnBrokenResource(jedis);
        }
        return res;
    }

    public void clear(){
        Jedis jedis = jedisPool.getResource();
        try {
            jedis.del(name);
            jedisPool.returnResource(jedis);
        } catch (Exception e) {
            jedisPool.returnBrokenResource(jedis);
        }
    }


}
