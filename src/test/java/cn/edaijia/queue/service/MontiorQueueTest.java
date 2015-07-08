package cn.edaijia.queue.service;

import cn.edaijia.queue.msg.Task;
import cn.edaijia.queue.service.impl.MonitorQueueImpl;
import com.google.common.collect.Maps;
import org.junit.Test;

import java.util.Map;

/**
 * @author wangjun
 * @since 15/5/15.
 */
public class MontiorQueueTest {

    MontiorQueue montiorQueue = new MonitorQueueImpl();

    @Test
    public void testGetAllQueue() throws Exception {
       System.out.println(montiorQueue.getAllQueue().get(0));
    }

    @Test
    public void testGetAllQueueTaskSize() throws Exception {
       System.out.println(montiorQueue.getAllQueueCurrentTaskSize());
    }

    @Test
    public void run() {
        for (int i = 0; i < 1000; i++) {
            Map map = Maps.newHashMap();
            map.put("msg", "how old are you ?");
            Task task = new Task("helloWorld", "test2", map);
           System.out.println(task.id);
        }
    }
}