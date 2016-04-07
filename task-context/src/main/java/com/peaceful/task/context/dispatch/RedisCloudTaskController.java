package com.peaceful.task.context.dispatch;

import com.peaceful.task.context.SimpleTaskContext;
import com.peaceful.task.context.cluster.Node;
import com.peaceful.task.context.common.ContextConstant;
import com.peaceful.task.context.config.Executor;
import com.peaceful.task.context.config.TaskConfigOps;
import com.peaceful.task.context.helper.NetHelper;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Sets;
import com.peaceful.common.redis.service.Redis;
import com.peaceful.common.util.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author WangJun
 * @version 1.0 16/3/30
 */
public class RedisCloudTaskController extends LocalTaskController implements CloudController {


    private static String prefix = "TASK-";
    // 任务配置列表
    private static String task_list = "";
    // 集群节点列表
    private static String task_node_list = "";
    private static String task_executor_list = "";


    private final static Logger LOGGER = LoggerFactory.getLogger(RedisCloudTaskController.class);

    static {
        TaskConfigOps ops = (TaskConfigOps) SimpleTaskContext.CONTEXT.get(ContextConstant.CONFIG);
        prefix += ops.name;
        task_list = prefix + "-CONFIG-LIST";
        task_node_list = prefix + "-NODE" + "-LIST";
        task_executor_list = prefix + "-EXECUTOR-LIST";
        ScheduledExecutorService executorService = (ScheduledExecutorService) SimpleTaskContext.CONTEXT.get(ContextConstant.PUBLICE_SCHEDULE);
        executorService.scheduleAtFixedRate(new RefreshController(), 2, 1, TimeUnit.SECONDS);

        Redis.cmd().del(task_executor_list);
        for (Executor e : EXECUTOR_LIST) {
            Redis.cmd().hset(task_executor_list, e.name, e.implementation);
        }
    }

    private void upload() {
        // 上传任务配置信息
        Collection<Task> tasks = findAllTasks();
        for (Task task : tasks) {
            Redis.cmd().hset(task_list, task.name, JSON.toJSONString(task));
        }

        // 上传服务节点信息
        addNode();
        for (Node node : NODE_MAP.values()) {
            if ((node.updateTime + TimeUnit.MINUTES.toMillis(5)) < System.currentTimeMillis()) {
                // 五分钟内没有心跳,移除该节点
                NODE_MAP.remove(node.hostName);
                Redis.cmd().hdel(task_node_list, node.hostName);
            } else {
                Redis.cmd().hset(task_node_list, node.hostName, JSON.toJSONString(node));
            }
        }
    }

    private void download() {
        // 更新本地任务配置信息
        // 更新的参照点事task的updateTime
        // 如果云端的时间比较新,则本地的时间以云端为主
        // 如果本地时间最新,则更新云端时间
        // 同步策略: desc state createTime 以远端为主  expire reserve 以本地为主,只上传,不下载 , updateTime 需要以最大为主
        Map<String, String> configs = Redis.cmd().hgetAll(task_list);
        for (String name : configs.keySet()) {
            Task task = JSON.parseObject(configs.get(name), Task.class);
            Task local = TASK_LIST.get(name);
            if (local == null) {
                local = TASK_HISTORY_LIST.get(task.name);
            }
            if (local != null) {
                if (local.expire) {
                    // 过期以本地为主:如果本地认为它是过期的,即使服务端没有标记过期,也认为它是过期
                } else {
                    local.desc = task.desc;
                    local.state = task.state;
                    local.reserve = SimpleTaskContext.QUEUE.size(prefix + "-" + task.name);
                    local.createTime = task.createTime;
                    if (local.updateTime < task.updateTime) {
                        local.updateTime = task.updateTime;
                    }
                }
            } else {
                if (task.expire) {
                    // 如果服务端认为是过期的,本地没有该任务就不要同步了
                } else {
                    TASK_LIST.put(task.name, task);
                }
            }
        }

        // 更新本地节点配置信息
        Map<String, String> nodeConfigs = Redis.cmd().hgetAll(task_node_list);
        for (
                String name
                : nodeConfigs.keySet())

        {
            Node node = JSON.parseObject(nodeConfigs.get(name), Node.class);
            if (NODE_MAP.containsKey(name)) {
                node.updateTime = NODE_MAP.get(name).updateTime;
            }
            NODE_MAP.put(name, node);
        }

    }

    @Override
    public void sync() {
        download();
        upload();
    }

    @Override
    public Collection<Node> findNodes() {
        return NODE_MAP.values();
    }


    private void addNode() {
        try {
            String hostName = InetAddress.getLocalHost().getHostName();
            String ip = InetAddress.getLocalHost().getHostAddress();
            if (NODE_MAP.containsKey(hostName)) {
                NODE_MAP.get(hostName).updateTime = System.currentTimeMillis();
            } else {
                Node node = new Node(hostName, ip);
                node.updateTime = System.currentTimeMillis();
                NODE_MAP.put(hostName, node);
            }
        } catch (UnknownHostException var8) {
            LOGGER.error("Error: {}", var8);
        }

    }

    private void removeNode(String hostname) {
        Redis.cmd().hdel(task_node_list, new String[]{hostname});
    }

    @Override
    public Collection<Task> findNeedDispatchTasks() {
        String hostname = NetHelper.getHostname();
        if (NODE_MAP.containsKey(hostname) && NODE_MAP.get(hostname).state.equals("OK")) {
            return super.findNeedDispatchTasks();
        } else {
            return Sets.newHashSet();
        }
    }
}

class RefreshController implements Runnable {
    private final static Logger LOGGER = LoggerFactory.getLogger(RedisCloudTaskController.class);

    @Override
    public void run() {
        try {
            CloudController clusterController = (CloudController) SimpleTaskContext.CONTEXT.get(ContextConstant.CONTROLLER);
            if (clusterController != null) {
                clusterController.sync();
            }
        } catch (Exception e) {
            LOGGER.error("refresh cluster controller error:{}", ExceptionUtils.getStackTrace(e));
        }

    }
}
