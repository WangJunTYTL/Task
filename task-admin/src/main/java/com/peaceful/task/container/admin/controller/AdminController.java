package com.peaceful.task.container.admin.controller;

import com.alibaba.fastjson.JSON;
import com.peaceful.task.container.common.API;
import com.peaceful.task.container.common.HttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author WangJun <wangjuntytl@163.com>
 * @version 1.0 15/9/15
 * @since 1.6
 */
@Controller
public class AdminController {


    Logger logger = LoggerFactory.getLogger(getClass());

    @RequestMapping({"index", "welcome", "/"})
    public String welcome(HttpServletRequest request) {
        String focusedTaskData = API.cat(API.Method.focusedTaskBeanSet);
        String firstFlexibleTaskData = API.cat(API.Method.firstFlexibleTaskBeanSet);
        logger.info("focusedTaskData {}", focusedTaskData);
        logger.info("firstFlexibleTaskData {}", firstFlexibleTaskData);
        request.setAttribute("focusedTaskData", JSON.parseObject(focusedTaskData, List.class));
        request.setAttribute("firstFlexibleTaskData", JSON.parseObject(firstFlexibleTaskData, List.class));
        return "welcome/index";
    }
}
