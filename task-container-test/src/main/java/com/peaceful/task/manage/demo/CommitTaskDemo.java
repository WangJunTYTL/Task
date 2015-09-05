package com.peaceful.task.manage.demo;

import com.peaceful.common.util.Util;
import com.peaceful.task.container.schedule.TaskSchedule;

/**
 * @author WangJun <wangjuntytl@163.com>
 * @version 1.0 15/8/16
 * @since 1.6
 */

public class CommitTaskDemo {



    public static void main(String[] args) {
       Hello hello = TaskSchedule.registerASyncClass(Hello.class);
        long start = System.currentTimeMillis();
        for (int i = 0; i < 10000; i++)
            hello.test("hello world");

        // Object 方法直接执行
        Util.report(hello.hashCode());
        Util.report(hello.toString());
        Util.report(System.currentTimeMillis() - start);
    }

}
