package com.peaceful.task.ops.task.api;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.peaceful.common.redis.service.Redis;
import com.peaceful.task.ops.common.Conf;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Task运行期的信息是注册到redis中，这里主要是从redis中获取信息
 *
 * @author WangJun
 * @version 1.0 16/3/31
 */
public class RedisControllerApi {

    private static final ScheduledExecutorService SCHEDULE = Executors.newScheduledThreadPool(2);
    // 任务生产总数
    private static final Map<String, Long> latestTaskProduceCount = new HashMap<String, Long>();
    // 任务消费总数
    private static final Map<String, Long> latestTaskConsumeCount = new HashMap<String, Long>();
    // 从redis中获取信息的频率
    private static final int rate = 26;
    private static final int COUNT_SIZE = 168;
    private static final Map<String, GraphData> COUNT_GRAPH = new HashMap<String, GraphData>();
    private static Map<String, String> taskProduceRate = new HashMap<String, String>();
    private static Map<String, String> taskConsumeRate = new HashMap<String, String>();


    static {
        SCHEDULE.scheduleAtFixedRate(new CycleAnalysis(), 0, rate, TimeUnit.SECONDS);
    }


    // 获取所有任务配置列表
    public static Map<String, String> getTaskDetail(String name) {
        Map<String, String> tasks = Redis.cmd().hgetAll("TASK-" + name + "-CONFIG-LIST");
        return tasks;
    }

    // 获取所有节点配置列表
    public static Map<String, String> getNodes(String name) {
        Map<String, String> nodes = Redis.cmd().hgetAll("TASK-" + name + "-NODE-LIST");

        return nodes;
    }

    // 更新节点配置
    public static void updateNode(String name, String node, String state) {
        Map<String, String> nodes = Redis.cmd().hgetAll("TASK-" + name + "-NODE-LIST");
        String n = nodes.get(node);
        if (n != null) {
            JSONObject object = JSON.parseObject(n);
            object.put("state", state);
            Redis.cmd().hset("TASK-" + name + "-NODE-LIST", node, object.toJSONString());
        }
    }

    // 更新任务配置
    public static void updateTask(String name, String task, String state, String desc) {
        Map<String, String> tasks = Redis.cmd().hgetAll("TASK-" + name + "-CONFIG-LIST");
        String n = tasks.get(task);
        if (n != null) {
            JSONObject object = JSON.parseObject(n);
            object.put("state", state);
            object.put("desc", desc);
            Redis.cmd().hset("TASK-" + name + "-CONFIG-LIST", task, object.toJSONString());
        }
    }

    public static Map<String, String> getExecutors(String name) {
        return Redis.cmd().hgetAll("TASK-" + name + "-EXECUTOR-LIST");
    }

    public static GraphData getCycleCount(String name) {
        return COUNT_GRAPH.get(name);
    }

    public static Map<String, String> getTaskConsumeRate(String name) {
        return taskConsumeRate;
    }

    public static Map<String, String> getTaskProduceRate(String name) {
        return taskProduceRate;
    }


    static class CycleAnalysis implements Runnable {

        @Override
        public void run() {
            try {
                for (String name : Conf.getConf().clusterList) {
                    Map<String, String> produce = Redis.cmd().hgetAll("TASK-" + name + "-PRODUCE-COUNT-MONITOR");
                    Map<String, String> consume = Redis.cmd().hgetAll("TASK-" + name + "-CONSUME-COUNT-MONITOR");
                    long produceTotal = 0;
                    long consumeTotal = 0;
                    GraphData graphData = null;
                    if (COUNT_GRAPH.containsKey(name)) {
                        graphData = COUNT_GRAPH.get(name);
                    } else {
                        graphData = new GraphData("TOTAL", COUNT_SIZE);
                        COUNT_GRAPH.put(name, graphData);
                    }
                    for (String key : produce.keySet()) {
                        Long newCount = Long.valueOf(produce.get(key));
                        if (latestTaskProduceCount.containsKey(key)) {
                            long nowCount = (newCount - latestTaskProduceCount.get(key));
                            produceTotal += nowCount;
                            taskProduceRate.put(key, String.valueOf(nowCount / rate));
                        }
                        latestTaskProduceCount.put(key, newCount);
                    }

                    for (String key : consume.keySet()) {
                        Long newCount = Long.valueOf(consume.get(key));
                        if (latestTaskConsumeCount.containsKey(key)) {
                            long nowCount = (newCount - latestTaskConsumeCount.get(key));
                            consumeTotal += nowCount;
                            taskConsumeRate.put(key, String.valueOf(nowCount / rate));
                        }
                        latestTaskConsumeCount.put(key, newCount);
                    }
                    graphData.timeAxis.push(System.currentTimeMillis());
                    graphData.produceAxis.push(produceTotal / rate);
                    graphData.consumeAxis.push(consumeTotal / rate);
                }
            } catch (Exception e) {
                // ignore
            }
        }


    }
}