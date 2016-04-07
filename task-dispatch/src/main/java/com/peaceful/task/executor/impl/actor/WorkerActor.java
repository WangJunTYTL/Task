package com.peaceful.task.executor.impl.actor;

import akka.actor.UntypedActor;

/**
 * @author <a href="mailto:wangjuntytl@163.com">WangJun</a>
 * @version 1.0 16/1/11
 */
public class WorkerActor extends UntypedActor {

    @Override
    public void onReceive(Object message) throws Exception {
        Runnable task = (Runnable) message;
        task.run();
    }
}
