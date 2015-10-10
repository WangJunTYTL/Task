package com.peaceful.task.container.schedule;

import com.peaceful.task.container.console.Monitor;
import com.peaceful.task.container.console.TaskBean;
import com.peaceful.task.container.store.help.Helper;
import com.peaceful.task.container.store.help.RemoteLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

/**
 * 任务容器会监控任务存储中心各个任务的提交情况和带哦度情况，这些统计会在每次任务容器重新启动后清除以前的统计，重新开始统计
 *
 * @author WangJun <wangjuntytl@163.com>
 * @version 1.0 15/9/25
 * @since 1.6
 */

public class MonitorInit {


    private static Logger logger = LoggerFactory.getLogger(MonitorInit.class);


    public static void init(Monitor monitor, RemoteLock remoteLock) {
        try {
            if (remoteLock.lock(100)) {
                Set<TaskBean> taskBeanSet = monitor.getFocusedTaskBeanSet();
                for (TaskBean taskBean : taskBeanSet) {
                    // 擦除task的commit统计
                    Helper.remotePushCount.getAndRemove(taskBean.getId());
                    // 擦除task的schedule统计
                    Helper.remotePopCount.getAndRemove(taskBean.getId());
                }
                // 擦除 focused task graph data
                Helper.remoteFocusedRepo.clear();
                // 擦除 first flexible task graph data
                Helper.remoteFirstFlexibleRepo.clear();
                // 擦除 second flexible task graph data
                Helper.remoteSecondFlexibleRepo.clear();
            }
        } catch (Exception e) {
            logger.error("monitor init found error {} ,cause {}", e, e.getCause());
        }
    }

}
