package com.peaceful.task.container.service;

import com.peaceful.common.util.Util;
import com.peaceful.task.container.monitor.MonitorQueue;
import com.peaceful.task.container.msg.Task;
import com.peaceful.task.container.monitor.impl.MonitorQueueImpl;
import com.google.common.collect.Maps;
import org.junit.Test;

import java.util.Map;

/**
 * @author wangjun
 * @since 15/5/15.
 */
public class MontiorQueueTest {

    MonitorQueue montiorQueue = new MonitorQueueImpl();

    @Test
    public void testGetAllQueue() throws Exception {
        Util.report(montiorQueue.getAllQueue().get(0));
    }

    @Test
    public void testGetAllQueueTaskSize() throws Exception {
        Util.report(montiorQueue.getAllQueueCurrentTaskSize());
    }

    @Test
    public void run() {
        for (int i = 0; i < 1000; i++) {
            Map map = Maps.newHashMap();
            map.put("msg", "how old are you ?");
            Task task = new Task("helloWorld", "test2", map);
            Util.report(task.id);
        }
    }
}