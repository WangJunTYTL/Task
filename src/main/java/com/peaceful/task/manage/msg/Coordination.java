package com.peaceful.task.manage.msg;

/**
 * @author WangJun <wangjuntytl@163.com>
 * @version 1.0 15/5/15
 * @since 1.6
 */

public class Coordination {
    public long messageId;
    public String queueName;

    public Coordination(long messageId, String queueName) {
        this.messageId = messageId;
        this.queueName = queueName;
    }
}
