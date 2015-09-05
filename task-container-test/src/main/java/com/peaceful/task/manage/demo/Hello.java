package com.peaceful.task.manage.demo;

import com.peaceful.task.container.common.Task;
import com.peaceful.common.util.Util;

/**
 * @author WangJun <wangjuntytl@163.com>
 * @version 1.0 15/8/16
 * @since 1.6
 */

public class Hello {

    @Task(queue = "testQueue")
    public void test(String str) {
        Util.report(str);
    }
}
