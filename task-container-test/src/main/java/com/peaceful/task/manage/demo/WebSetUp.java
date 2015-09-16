package com.peaceful.task.manage.demo;

import com.peaceful.task.container.TaskContainer;
import org.springframework.stereotype.Controller;

/**
 * @author WangJun <wangjuntytl@163.com>
 * @version 1.0 15/9/15
 * @since 1.6
 */

@Controller
public class WebSetUp {

    static {
        TaskContainer.start();
    }
}
