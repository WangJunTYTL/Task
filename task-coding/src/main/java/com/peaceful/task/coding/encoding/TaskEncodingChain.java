package com.peaceful.task.coding.encoding;


import com.peaceful.common.util.chain.BaseChain;

/**
 * @author <a href="mailto:wangjuntytl@163.com">WangJun</a>
 * @version 1.0 16/1/10
 */
public class TaskEncodingChain extends BaseChain {

    private TaskEncodingChain() {

    }

    public static TaskEncodingChain getSingleInstance() {
        return Single.chain;
    }

    public static class Single {
        public static TaskEncodingChain chain = new TaskEncodingChain();
    }

    static {
        Single.chain.addCommand(new TaskPushMonitor());
    }

}
