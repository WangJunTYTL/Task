package com.peaceful.task.coding.decoding;


import com.peaceful.common.util.chain.BaseChain;

/**
 * @author <a href="mailto:wangjuntytl@163.com">WangJun</a>
 * @version 1.0 16/1/10
 */
public class TaskDecodingChain extends BaseChain {

    private TaskDecodingChain() {

    }

    public static TaskDecodingChain getSingleInstance() {
        return Single.chain;
    }

    public static class Single {
        public static TaskDecodingChain chain = new TaskDecodingChain();
    }

    static {

        Single.chain.addCommand(new TaskPopMonitor());
    }
}
