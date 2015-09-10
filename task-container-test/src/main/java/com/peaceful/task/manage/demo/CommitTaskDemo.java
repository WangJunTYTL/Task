package com.peaceful.task.manage.demo;

import com.peaceful.common.util.Util;
import com.peaceful.task.container.schedule.TaskSchedule;

/**
 * @author WangJun <wangjuntytl@163.com>
 * @version 1.0 15/8/16
 * @since 1.6
 */

public class CommitTaskDemo {

    public static Hello hello = TaskSchedule.registerASyncClass(Hello.class);


    public static void main(String[] args) {
        for (int i = 0; i < 1000; i++)
            forceChangeTaskName();
    }


    public static void simple() {
        long start = System.currentTimeMillis();
        for (int i = 0; i < 10000; i++)
            hello.test("hello world");
        // Object 方法直接执行
        Util.report(hello.hashCode());
        Util.report(hello.toString());
        Util.report(System.currentTimeMillis() - start);
    }

    public static void forceChangeTaskName() {

        // 强制更改任务名称，使其可以进入到aa123队列
        TaskSchedule.Schedule.forceChangeTaskName.set("aa123");
        hello.test("hello world");
        TaskSchedule.Schedule.forceChangeTaskName.remove();
        hello.test2("123");

    }

}
