package com.peaceful.task.container.console;

import java.util.HashMap;

/**
 * 一级动态任务容器
 * <p/>
 * 用于登记动态提交的新生任务列表
 * <p/>
 * 动态任务目前会有3个生命周期阶段，第一阶段是任务单元创建和调度阶段，在这个阶段，任务一直会被调度，当已经提交的动态任务已经完全被调度完毕后，在一定时间内再也
 * 没有收到该任务的提交，则该动态任务会被迁移到{@link SecondFlexibleTaskMap}中,此时是任务的第二阶段，此时该任务不再被调度，但可以在监控中还可以继续查到该动态任务的相关信息，比如
 * commit、schedule信息。在过一定时间段。该动态任务会从{@link SecondFlexibleTaskMap} 中移除，此时称为第三阶段，也叫消亡阶段。在消亡前会出于考虑，再次检查该任务是否还有未被调度的任务单元，
 * 如果存在会重新再次进入第一阶段
 *
 * @author WangJun <wangjuntytl@163.com>
 * @version 1.0 15/9/15
 * @see FocusedTaskMap
 * @see SecondFlexibleTaskMap
 * @since 1.6
 */

public class FirstFlexibleTaskMap extends HashMap<String, TaskBean> {

}
