package com.peaceful.task.coding.encoding;

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
public class TaskPushMonitor implements Command {

    Logger logger = LoggerFactory.getLogger(getClass());


    @Override
    public boolean execute(Context context) throws Exception {
        TU tu = (TU) context.get("taskUnit");
        logger.debug("submit task {}",tu);

        // 单点监控:perf4j 记录单点pop频率
        StopWatch stopwatch = (StopWatch) context.get("stopWatch");
        stopwatch.stop();
        return false;
    }
}
