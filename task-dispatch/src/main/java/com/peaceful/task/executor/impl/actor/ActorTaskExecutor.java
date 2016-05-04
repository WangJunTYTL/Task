package com.peaceful.task.executor.impl.actor;

import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import akka.actor.ActorSystem;
import com.peaceful.task.context.SimpleTaskContext;
import com.peaceful.task.context.TaskExecutor;

/**
 * @author <a href="mailto:wangjuntytl@163.com">WangJun</a>
 * @version 1.0 16/1/11
 */
public class ActorTaskExecutor implements TaskExecutor {

    private ActorSystem actorSystem = (ActorSystem) SimpleTaskContext.CONTEXT.get("actorSystem");
    private ActorSelection router = actorSystem.actorSelection("/user/myroot/public/taskDispatch");

    @Override
    public void execute(Runnable task) {
        router.tell(task, ActorRef.noSender());
    }

}
