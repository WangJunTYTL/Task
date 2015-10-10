package com.peaceful.task.container.dispatch;

import com.peaceful.task.container.invoke.excute.ArgsHandler;
import com.peaceful.task.container.invoke.excute.HandlerChain;
import com.peaceful.task.container.invoke.excute.InvokeHandler;
import net.sf.cglib.reflect.FastClass;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 调度任务时用到对象容器管理
 * 调度某个任务单元是，需要用到任务单元对应的实例，该实例要求只被实例化一次，为了管理这些实例，需要提供单独的容器进行管理
 * 当调度某个任务单元时，发现该容器没有这样的任务单元，该容器会尝试去主动实例化
 * 另外对于一些比较复杂的对象实例，也许容器并不知道怎么去实例化它，比如spring容器中的实例可能还会间接依赖其它类的实例，这时需要主动
 * 提供一种外部实例注入的方式 todo
 *
 * @author WangJun <wangjuntytl@163.com>
 * @version 1.0 15/8/16
 * @since 1.6
 */

public class DispatchContainer extends HashMap {

    /**
     * Java原生对象实例
     */
    static Map<Class, Object> DISPATCH_INSTANCE_CONTAINER = new ConcurrentHashMap<Class, Object>();
    /**
     * Java对应的代理对象FastClass
     * 在大对象中，fastclass比jdk自带的也许更快
     */
    static Map<Class, FastClass> DISPATCH_FASTCLASS_CONTAINER = new ConcurrentHashMap<Class, FastClass>();
    /**
     * 执行任务单元调用的指令
     */
    final static HandlerChain HANDLER_CHAIN = new HandlerChain();

    static {
        HANDLER_CHAIN.addCommand(new ArgsHandler());
        HANDLER_CHAIN.addCommand(new InvokeHandler());
    }


    /**
     * 获取任务单元所属的对象实例管理容器
     *
     * @return Map(Class, Instance)
     */
    public static Map<Class, Object> getDispatchInstanceContainer() {
        return DISPATCH_INSTANCE_CONTAINER;
    }

    /**
     * 获取任务单元所属的对象实例管理容器
     *
     * @return Map(Class, FastClass)
     */
    public static Map<Class, FastClass> getDispatchFastClassContainer() {
        return DISPATCH_FASTCLASS_CONTAINER;
    }

    /**
     * 任务执行单元处理链
     *
     * @return task unit handle chain
     */
    public static HandlerChain getHandlerChain() {
        return HANDLER_CHAIN;
    }

}

