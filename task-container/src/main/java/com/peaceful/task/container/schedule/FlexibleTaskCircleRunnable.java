package com.peaceful.task.container.schedule;

import com.peaceful.task.container.common.ConfConstant;
import com.peaceful.task.container.console.Monitor;
import com.peaceful.task.container.console.SecondFlexibleTaskMap;
import com.peaceful.task.container.console.TaskBean;
import com.peaceful.task.container.store.TaskStore;
import com.peaceful.task.container.store.help.Helper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * flexible task 生命周期自动过渡
 *
 * 动态任务目前会有3个生命周期阶段，第一阶段是任务单元创建和调度阶段，在这个阶段，任务一直会被调度，当已经提交的动态任务已经完全被调度完毕后，在一定时间内再也
 * 没有收到该任务的提交，则该动态任务会被迁移到{@link SecondFlexibleTaskMap}中,此时是任务的第二阶段，此时该任务不再被调度，但可以在监控中还可以继续查到该动态任务的相关信息，比如
 * commit、schedule信息。在过一定时间段。该动态任务会从{@link SecondFlexibleTaskMap} 中移除，此时称为第三阶段，也叫消亡阶段。在消亡前会出于考虑，再次检查该任务是否还有未被调度的任务单元，
 * 如果存在会重新再次进入第一阶段
 *
 * @author WangJun <wangjuntytl@163.com>
 * @version 1.0 15/9/15
 * @since 1.6
 *
 * @see com.peaceful.task.container.console.FirstFlexibleTaskMap
 * @see com.peaceful.task.container.console.SecondFlexibleTaskMap
 */

public class FlexibleTaskCircleRunnable implements Runnable {

    private static Logger logger = LoggerFactory.getLogger(FlexibleTaskCircleRunnable.class);

    private Monitor monitor;

    public FlexibleTaskCircleRunnable(Monitor monitor) {
        this.monitor = monitor;
    }

    @Override
    public void run() {

        // first flexible task to second flexible task
        for (TaskBean taskBean : monitor.getFirstFlexibleTaskBeanSet()) {
            long updateTime = taskBean.updateTime;
            if ((System.currentTimeMillis() - updateTime) > ConfConstant.FIRST_TASK_LIVE_TIME) {
                if (TaskStore.get().size(taskBean.getId()) > 0) {
                    // 记录最近一次的检测时间
                    taskBean.updateTime = System.currentTimeMillis();
                } else {
                    // first 阶段到 second 阶段过渡
                    logger.debug("first task {} to second ", taskBean.getId());
                    monitor.removeFirstFlexibleTask(taskBean.getId());
                    monitor.addSecondFlexibleTask(taskBean);
                }
            }
        }

        // second flexible die out
        for (TaskBean taskBean : monitor.getSecondFlexibleTaskBeanSet()) {
            long updateTime = taskBean.updateTime;
            if ((System.currentTimeMillis() - updateTime) > ConfConstant.SECOND_TASK_LIVE_TIME) {
                if (TaskStore.get().size(taskBean.getId()) > 0) {
                    // 记录最近一次的检测时间,重新进入first阶段
                    logger.warn("second task {} to first ", taskBean.getId());
                    taskBean.updateTime = System.currentTimeMillis();
                    monitor.addFirstFlexibleTask(taskBean);
                } else {
                    // 清除相关的统计监控信息
                    // 擦除task的commit统计
                    Helper.remotePushCount.getAndRemove(taskBean.getId());
                    // 擦除task的schedule统计
                    Helper.remotePopCount.getAndRemove(taskBean.getId());
                }
                monitor.removeSecondFlexibleTask(taskBean.getId());
                logger.info("remove secondFlexible {}", taskBean);
            }
        }

    }
}
