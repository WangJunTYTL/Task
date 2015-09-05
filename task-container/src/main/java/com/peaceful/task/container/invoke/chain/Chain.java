package com.peaceful.task.container.invoke.chain;

/**
 * @author WangJun <wangjuntytl@163.com>
 * @version 1.0 15/8/28
 * @since 1.6
 */

public interface Chain {

    void addCommand(Command command);

    boolean execute(Context context) throws Exception;

}
