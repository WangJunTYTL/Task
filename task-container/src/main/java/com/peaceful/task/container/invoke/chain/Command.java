package com.peaceful.task.container.invoke.chain;

/**
 * @author WangJun <wangjuntytl@163.com>
 * @version 1.0 15/8/28
 * @since 1.6
 */

public interface Command {

    public static final boolean CONTINUE_PROCESSING = false;

    public static final boolean PROCESSING_COMPLETE = true;


    boolean execute(Context context) throws Exception;

}
