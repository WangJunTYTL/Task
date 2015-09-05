package com.peaceful.task.container.invoke.trace;

/**
 * @author WangJun <wangjuntytl@163.com>
 * @version 1.0 15/8/28
 * @since 1.6
 */

public class InvokeTrace {

    public static TraceChain start = new TraceChain();
    public static TraceChain end = new TraceChain();


    static {
        end.addCommand(new InvokeInfoTrace());
    }


    /**
     * 前跟踪，在任务执行完毕后调用
     *
     * @param trace
     */
    public static void addStartTrace(Trace trace) {
        start.addCommand(trace);
    }

    /**
     * 后跟踪，在任务执行完毕后调用
     *
     * @param trace
     */
    public static void addEndTrace(Trace trace) {
        end.addCommand(trace);
    }


}
