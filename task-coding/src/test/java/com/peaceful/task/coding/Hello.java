package com.peaceful.task.coding;


import com.peaceful.task.context.annotation.Task;

/**
 * @author <a href="mailto:wangjuntytl@163.com">WangJun</a>
 * @version 1.0 16/1/11
 */
public class Hello {

    @Task("test")
    public void say(String msg) {
        System.out.println(msg);
    }
}
