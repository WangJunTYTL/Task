package com.peaceful.task.system;

import com.peaceful.task.context.annotation.Task;

/**
 * @author <a href="mailto:wangjuntytl@163.com">WangJun</a>
 * @version 1.0 16/1/11
 */
public class Hello {

    @Task("Test07")
    public void say(String msg) throws InterruptedException {
        Thread.sleep(1000);
        System.out.println(msg);
    }
}
