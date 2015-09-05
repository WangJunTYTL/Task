package com.peaceful.task.container.dispatch;

import com.peaceful.task.container.invoke.excute.ArgsHandler;
import com.peaceful.task.container.invoke.excute.HandlerChain;
import com.peaceful.task.container.invoke.excute.InvokeHandler;
import net.sf.cglib.reflect.FastClass;

import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Map;

/**
 * @author WangJun <wangjuntytl@163.com>
 * @version 1.0 15/8/16
 * @since 1.6
 */

public class DispatchContainer extends HashMap {

    static Map<Class, Object> DISPATCH_INSTANCE_CONTAINER = new IdentityHashMap();
    static Map<Class, FastClass> DISPATCH_FASTCLASS_CONTAINER = new IdentityHashMap();
    final static HandlerChain HANDLER_CHAIN = new HandlerChain();

    static {
        HANDLER_CHAIN.addCommand(new ArgsHandler());
        HANDLER_CHAIN.addCommand(new InvokeHandler());
    }


    public static Map<Class, Object> getDispatchInstanceContainer() {
        return DISPATCH_INSTANCE_CONTAINER;
    }

    public static Map<Class, FastClass> getDispatchFastclassContainer() {
        return DISPATCH_FASTCLASS_CONTAINER;
    }


    public static HandlerChain getHandlerChain() {
        return HANDLER_CHAIN;
    }

}

