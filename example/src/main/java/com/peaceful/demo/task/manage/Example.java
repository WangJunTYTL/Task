package com.peaceful.demo.task.manage;

import akka.actor.ActorSystem;
import com.peaceful.task.manage.TaskManageStart;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author WangJun <wangjuntytl@163.com>
 * @version 1.0 15/7/9
 * @since 1.6
 */
@Controller
@ResponseBody
public class Example {


    {
        // 启动taskManage
        ActorSystem system = ActorSystem.create("MySystem");
        TaskManageStart.setSystem(system);
        TaskManageStart.run();

    }


    @RequestMapping("index")
    public String test() {
        return "hello world";

    }
}
