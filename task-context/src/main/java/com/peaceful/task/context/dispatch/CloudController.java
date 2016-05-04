package com.peaceful.task.context.dispatch;

import com.peaceful.task.context.TaskController;
import com.peaceful.task.context.cluster.Node;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author WangJun
 * @version 1.0 16/3/31
 */
public interface CloudController extends TaskController {


    Map<String,Node> NODE_MAP = new ConcurrentHashMap<String, Node>();

    /**
     * 本地和云端进行信息同步
     */
    void sync();


    /**
     * 获取集群中所有的服务节点
     *
     * @return
     */
    Collection<Node> findNodes();




}
