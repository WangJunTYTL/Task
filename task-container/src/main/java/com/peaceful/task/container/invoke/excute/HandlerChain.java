package com.peaceful.task.container.invoke.excute;

import com.peaceful.task.container.invoke.chain.BaseChain;

/**
 * @author WangJun <wangjuntytl@163.com>
 * @version 1.0 15/8/28
 * @since 1.6
 */

public class HandlerChain extends BaseChain {

    public HandlerChain(Handler... handler){
        super(handler);
    }

}
