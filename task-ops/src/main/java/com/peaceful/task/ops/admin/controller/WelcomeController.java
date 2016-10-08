package com.peaceful.task.ops.admin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

import static com.peaceful.common.redis.service.Redis.cmd;

/**
 * @author WangJun
 * @version 1.0 16/3/31
 */
@RequestMapping("/")
@Controller
public class WelcomeController {


    @RequestMapping(value = {"index", ""})
    public String index(HttpServletRequest request) {
        return "welcome/index";
    }

    @RequestMapping(value = "01")
    public String template01(HttpServletRequest request) {
        return "welcome/template01";
    }

    @RequestMapping(value = "02")
    public String template02(HttpServletRequest request) {
        return "welcome/template02";
    }

    @RequestMapping(value = "03")
    public String template03(HttpServletRequest request) {
        return "welcome/template03";
    }

    @RequestMapping(value = "04")
    public String template04(HttpServletRequest request) {
        return "welcome/template04";
    }


    @RequestMapping(value = {"help"})
    public String help(HttpServletRequest request) {
        return "help/index";
    }


}
