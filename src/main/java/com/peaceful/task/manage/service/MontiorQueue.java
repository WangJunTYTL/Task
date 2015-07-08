package com.peaceful.task.manage.service;

import java.util.List;
import java.util.Map;

/**
 * @author WangJun <wangjuntytl@163.com>
 * @version 1.0 15/5/15
 * @since 1.6
 */

public interface MontiorQueue {

    /**
     * 获得所有队列集合
     *
     * @return
     */
    public List<String> getAllQueue();


    /**
     * 获取各个队列当前大小
     *
     * @return
     */
    public Map<String, Long> getAllQueueCurrentTaskSize();

    /**
     * 获取所有队列目前积压任务的总和
     *
     * @return
     */
    public long getSumOfQueueSize();

    /**
     * 获取队列基本信息，包括配置，已提交任务数，目前队列任务压力情况
     *
     * @return
     */
    public Map<String, String> getBasicInfo();

    /**
     * 获取队列服务总共收到的任务数
     *
     * @return
     */
    public long getCommitTaskCount();

}
