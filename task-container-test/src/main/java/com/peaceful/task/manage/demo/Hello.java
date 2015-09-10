package com.peaceful.task.manage.demo;

import com.peaceful.task.container.common.Task;
import com.peaceful.common.util.Util;
import com.peaceful.task.container.schedule.TaskSchedule;

/**
 * @author WangJun <wangjuntytl@163.com>
 * @version 1.0 15/8/16
 * @since 1.6
 */

public class Hello {

    @Task("testQueue")
    public void test(String str) {
        Util.report(str);
    }

    @Task("testQueue2")
    public void test2(String str){
        Util.report(str);

    }
}
