package com.peaceful.task.container.monitor;

import com.peaceful.task.container.console.CycleQueue;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author wangjun
 * @since 15/9/16.
 */
public class CycleQueueTest {

    @Test
    public void testPush() throws Exception {
        CycleQueue<Integer> cycleQueue = new CycleQueue<Integer>(8);
        for (int i = 0; i < 20; i++) {
            cycleQueue.push(i);
            System.out.println(cycleQueue.toString());
        }
    }
}