package com.peaceful.task.container.admin.controller;

import com.peaceful.common.util.Http;
import com.peaceful.task.container.common.Conf;
import org.springframework.context.annotation.Description;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author WangJun
 * @version 1.0 16/4/7
 */
@RequestMapping("/conf")
@Controller
@ResponseBody
public class ConfController {

    @RequestMapping("/cluster")
    @Description("获取集群列表")
    public void getClusterList() {
        Http.responseJSON(Conf.getConf().clusterList);
    }
}
