package com.peaceful.task.core.cluster;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 服务中心用于管理TaskSystem运行期的状态信息
 *
 * @author <a href="mailto:wangjuntytl@163.com">WangJun</a>
 * @version 1.0 16/1/11
 */
public interface ServiceCenter {


    /**
     * 获取系统的名字
     *
     * @return
     */
    String getSystemName();

    /**
     * 获取集群中所有的服务节点
     *
     * @return
     */
    List<Node> getAllServiceNode();

    /**
     * 获取master节点
     *
     * @return
     */
    String getMaster();


    /**
     * 加入当前节点到服务中心
     */
    void addServiceNode();

    /**
     * 移除当前节点
     */
    void removeServiceNode();

    /**
     * 移除指定服务节点
     *
     * @param hostName
     */
    void removeServiceNode(String hostName);

    /**
     * 选举master节点
     *
     * @return
     */
    Node electMasterNode();

    /**
     * 当前节点是否是master节点
     *
     * @return
     */
    boolean isMasterNode();

    boolean addRouter(String queueName, String hostname);

    boolean removeRouter(String queueName);

    Map<String, String> getRouterTable();


    /**
     * 添加 focused task
     *
     * @param queueName
     * @return
     */
    boolean addFocusedTask(String queueName);

//    Map<String, QueueMBean> updateQueue();

//    void updateQueueState();


    /**
     * 清除focused task 列表
     *
     * @return
     */
    boolean clearFocusedTask();

    /**
     * 添加 flexible task
     *
     * @param queueName
     * @return
     */
    boolean addFlexibleTask(String queueName);

    /**
     * 获取focused task 列表
     *
     * @return
     */
    Set<String> getAllFocusedTask();

    /**
     * 获取flexible task 列表
     *
     * @return
     */
    Set<String> getAllFlexibleTask();

    /**
     * 停止指定队列的调度
     *
     * @param queueName
     * @return
     */
    boolean lockQueue(String queueName);

    boolean queueIsLock(String queueName);

    /**
     * 打开指定队列的调度
     *
     * @param queueName
     * @return
     */
    boolean openQueueLock(String queueName);

    /**
     * 停止指定节点的调度
     *
     * @param hostname
     * @return
     */
    boolean lockNode(String hostname);

    boolean nodeIsLock(String hostname);

    /**
     * 打开指定节点的调度
     *
     * @param hostname
     * @return
     */
    boolean openNodeLock(String hostname);



}
