package com.peaceful.task.container.console;

import java.io.Serializable;

/**
 * 定时分析任务单元的提交情况和调度情况，并保存最近时间断的分析结果
 * <p/>
 * 分析后的结果会被保存在一个循环的队列中{@link CycleQueue},你可以在初始化的时候设置cycle queue的大小，确定要保存的分析结果的数量
 *
 * @see CycleQueue
 * @see TaskBeanAnalyzing
 *
 * @author WangJun <wangjuntytl@163.com>
 * @version 1.0 15/9/16
 * @since 1.6
 */

public class ChartData implements Serializable {

    private final static int length = 12;

    /**
     * 任务单元编号
     */
    public String id;
    /**
     * 某任务单元在某段时期的push量
     */
    public CycleQueue<Long> push = new CycleQueue<Long>(length);
    /**
     * 某任务单元在某段时期的pop量
     */
    public CycleQueue<Long> pop = new CycleQueue<Long>(length);
    /**
     * 记录每次分析的时间点
     */
    public CycleQueue<String> label = new CycleQueue<String>(length);


}
