package com.peaceful.task.container.console;

import java.util.HashMap;

/**
 * Second flexible task map
 * <p/>
 * 用于登记动态提交的新生任务列表
 * <p/>
 * 在flexible task的生命周期中，任务调度已经(可能)完成将会进入此map，此时在这个map中的task就爱那个不会在去被调度。进入此map的生命周期也会在
 * 一定时间后消亡，即进入消亡阶段，在消亡前会在去验证是否任务存储中心还存在未被调度的任务单元，如果存在会重新进入第一阶段
 *
 * @author WangJun <wangjuntytl@163.com>
 * @version 1.0 15/9/15
 * @since 1.6
 */

public class SecondFlexibleTaskMap extends HashMap<String, TaskBean> {

}
