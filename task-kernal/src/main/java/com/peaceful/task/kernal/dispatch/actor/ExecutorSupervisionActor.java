package com.peaceful.task.kernal.dispatch.actor;

import akka.actor.UntypedActor;
import akka.event.DiagnosticLoggingAdapter;
import akka.event.Logging;
import com.peaceful.task.kernal.TaskContext;
import com.peaceful.task.kernal.TaskExecutor;
import com.peaceful.task.kernal.coding.TUR;
import com.peaceful.task.kernal.dispatch.actor.msg.DecorateTask;

/**
 * executor监控者
 * <p/>
 * Created by wangjun on 16/1/12.
 */
public class ExecutorSupervisionActor extends UntypedActor {

    // // TODO: 16/1/12 测试executor失败的情况 
    private TaskExecutor taskExecutor;
    private TaskContext context;
    DiagnosticLoggingAdapter log = Logging.getLogger(this);

    public ExecutorSupervisionActor(TaskExecutor taskExecutor, TaskContext context) {
        this.taskExecutor = taskExecutor;
        this.context = context;
    }

    @Override
    public void onReceive(Object o) throws Exception {
        if (o instanceof TUR) {
            TUR taskUnit = (TUR) o;
            DecorateTask decorate = new DecorateTask(context, taskUnit, self());
            taskExecutor.execute(decorate);
        } else {
            unhandled(o);
        }
    }


}
