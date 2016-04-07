package com.peaceful.task.executor;

import akka.actor.ActorSystem;
import akka.actor.Props;
import com.peaceful.task.context.SimpleTaskContext;
import com.peaceful.task.context.TaskModule;
import com.peaceful.task.context.common.ContextConstant;
import com.peaceful.task.context.config.TaskConfigOps;
import com.peaceful.task.executor.dispatch.SimpleOutsideDispatch;
import com.peaceful.task.executor.dispatch.actor.ExecutorModuleRootActor;
import com.peaceful.common.util.chain.Context;
import scala.concurrent.duration.Duration;

import java.util.concurrent.TimeUnit;

/**
 * Executor模块加载入口
 * <p/>
 * Executor模块主要负责Task的调度与执行情况
 *
 * @author <a href="mailto:wangjuntytl@163.com">WangJun</a>
 * @version 1.0 16/1/11
 */
public class ExecutorModule implements TaskModule {


    @Override
    public boolean execute(Context context) throws Exception {
        TaskConfigOps taskConfigOps = (TaskConfigOps) SimpleTaskContext.CONTEXT.get(ContextConstant.CONFIG);
        ActorSystem system = (ActorSystem) context.get("actorSystem");
        // 创建actor系统的top actor,所有actor都要求从这里继承下去
        system.actorOf(Props.create(ExecutorModuleRootActor.class), "myroot");
        /**
         * 由于当前是任务容器是主动去任务存储中心拉取任务，所以需要有一个轮训者不断的区任务存储中心拉取任务
         * 当轮训中拉取到任务时，会送到任务路由中心，接下来的时刻会在worker和router之间协调的去任务存储中心拉取任务
         * 第二，为了保证router不会在某一个任务上不断的调度，该轮询方法需要固定频率的给router强加任务，使每个任务队列都有机会调度
         */
        system.scheduler().schedule(Duration.Zero(), Duration.create(taskConfigOps.dispatchTick, TimeUnit.MILLISECONDS), new SimpleOutsideDispatch(system.actorSelection("/user/myroot/executorDispatch")), system.dispatcher());

        return false;
    }

}
