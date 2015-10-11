package com.peaceful.task.container.schedule;

import com.peaceful.task.container.common.ConfConstant;
import com.peaceful.task.container.console.Monitor;
import com.peaceful.task.container.console.TaskBean;
import com.peaceful.task.container.console.TaskBeanAnalyzing;
import com.peaceful.task.container.store.help.Helper;
import com.peaceful.task.container.store.help.RemoteLock;
import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * 监控任务存储中心任务单元的提交情况和调度情况
 *
 * @author WangJun <wangjuntytl@163.com>
 * @version 1.0 15/9/15
 * @since 1.6
 */

public class MonitorRunnable implements Runnable {

    private Monitor monitor;
    private static Logger logger = LoggerFactory.getLogger(MonitorRunnable.class);
    private static Logger loggerAnalyzing = LoggerFactory.getLogger(TaskBeanAnalyzing.class);
    private static RemoteLock remoteLock = new RemoteLock("console-schedule");
    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
    private static long last_exe_timestamp = System.currentTimeMillis();



    public MonitorRunnable(Monitor monitor) {
        this.monitor = monitor;
        MonitorInit.init(monitor, remoteLock);
    }


    // 分布式集群监控，应由集群中的某一个节点执行监控
    @Override
    public void run() {
        try {
            // todo
            //router.route(new OpenValve(), getSender());

            if (remoteLock.lock(100)) {
                Set<TaskBean> taskBeanSet = monitor.getFocusedTaskBeanSet();
                Set<TaskBean> taskBeanSet2 = monitor.getFirstFlexibleTaskBeanSet();
                loggerAnalyzing.debug("{}\tconsumeRate\tproduceRate", simpleDateFormat.format(new Date()));
                for (TaskBean taskBean : taskBeanSet) {
                    TaskBeanAnalyzing.analyzing(taskBean,last_exe_timestamp);
                }
                for (TaskBean taskBean : taskBeanSet2) {
                    TaskBeanAnalyzing.analyzing(taskBean,last_exe_timestamp);
                }
                last_exe_timestamp=System.currentTimeMillis();
                Helper.remoteFocusedRepo.set(JSON.toJSONString(taskBeanSet));
                Helper.remoteFirstFlexibleRepo.set(JSON.toJSONString(taskBeanSet2));
                Helper.remoteSecondFlexibleRepo.set(JSON.toJSONString(monitor.getSecondFlexibleTaskBeanSet()));
            }
        } catch (Exception e) {
            logger.error("MonitorRunnable error {},cause {}", e,e.getCause());
        } finally {
            TaskSchedule.scheduleOnce(ConfConstant.MONITOR_TICK, TimeUnit.SECONDS, this);
            remoteLock.unLock();
        }
    }
}
