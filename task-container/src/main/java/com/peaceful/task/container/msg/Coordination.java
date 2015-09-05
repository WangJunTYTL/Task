package com.peaceful.task.container.msg;

import java.io.Serializable;

/**
 * worker 向 dispatch 发送消息，表示worker已经完成工作，dispatch可以再次调度任务给该worker
 *
 * @author WangJun <wangjuntytl@163.com>
 * @version 1.0 15/5/15
 * @since 1.6
 */

public class Coordination implements Serializable{
    public String messageId;
    public String queueName;

    @Deprecated
    public Coordination(String messageId, String queueName) {
        this.messageId = messageId;
        this.queueName = queueName;
    }

    public Coordination(long messageId, String queueName) {
        this.messageId = String.valueOf(messageId);
        this.queueName = queueName;
    }
}
