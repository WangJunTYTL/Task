package com.peaceful.task.coding.decoding;

import com.peaceful.common.util.chain.Command;
import com.peaceful.common.util.chain.Context;
import com.peaceful.task.context.coding.TU;
import org.perf4j.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author <a href="mailto:wangjuntytl@163.com">WangJun</a>
 * @version 1.0 16/1/10
 */
public class TaskPopMonitor implements Command {

    Logger logger = LoggerFactory.getLogger(getClass());
//    TaskAction taskAction = (TaskAction) ConsoleModule.getSystemContext().get("taskAction");

    @Override
    public boolean execute(Context context) throws Exception {
        TU task = (TU) context.get("taskUnit");

        // 单点监控:perf4j 记录单点pop频率
        StopWatch stopwatch = (StopWatch) context.get("stopWatch");
        stopwatch.stop();
        logger.debug("start {} {} {}", task.getId(), task.getAclass().getSimpleName() + "." + task.getMethod(), task.getArgs());

//        TaskMeta taskMeta = new TaskMeta();
//        taskMeta.queueName = task.getQueueName();
//        taskMeta.action = TaskMeta.POP_ACTION;
//        taskAction.offer(taskMeta);
        return false;
    }
}
