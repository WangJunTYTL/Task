package com.peaceful.task.container.task.api;

/**
 * @author WangJun
 * @version 1.0 16/4/5
 */
public class GraphData {

    public String tag;
    public CycleQueue<Long> timeAxis;
    public CycleQueue<Long> produceAxis;
    public CycleQueue<Long> consumeAxis;

    public GraphData(String tag,int size){
        this.tag = tag;
        this.timeAxis = new CycleQueue<Long>(size);
        produceAxis = new CycleQueue<Long>(size);
        consumeAxis = new CycleQueue<Long>(size);
    }
}
