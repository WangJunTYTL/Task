package com.peaceful.task.kernal.helper;

/**
 * SimpleTaskContext 集群节点信息
 *
 * @author <a href="mailto:wangjuntytl@163.com">WangJun</a>
 * @version 1.0 16/1/11
 */
public class Node {

    // 节点hostname
    public String hostName;
    // 节点ip
    public String ip;
    // 节点启动模式
    public String mode;
    // 节点状态
    public String state;
    // 节点启动时间
    public long startTime;
    // 节点信息更新时间
    public long updateTime;

    public Node() {
    }

    public Node(String hostName, String ip) {
        this.hostName = hostName;
        this.ip = ip;
        this.state = "OK";
    }
}
