package com.peaceful.task.container.monitor;

import org.apache.commons.lang3.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

/**
 * @author WangJun <wangjuntytl@163.com>
 * @version 1.0 15/9/15
 * @since 1.6
 */

public class TaskViewController extends HttpServlet {

    private static Monitor monitor = MonitorImpl.getInstance();


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        router(req, resp);
    }


    public void router(HttpServletRequest request, HttpServletResponse response) {
        String method = request.getParameter("method");
        if (StringUtils.isBlank(method)) method = "default";
        if (method.equals("flexibleTasks")) {
            getFlexibleTask(response);
        } else if (method.equals("focusedTasks")) {
            getFocusedTask(response);
        } else if (method.equals("focusedTaskMap")) {
            getFocusedTaskMap(response);
        } else if (method.equals("focusedTaskBeanSet")) {
            getFocusedTaskBeanSet(response);
        } else if (method.equals("firstFlexibleTaskBeanSet")) {
            getFirstFlexibleTaskBeanSet(response);
        } else {
            Http.responseString(response, "params error");
        }
    }


    public void getFlexibleTask(HttpServletResponse resp) {
        Set<String> flexibleTasks = monitor.getFlexibleTasks();
        Http.responseJson(resp, flexibleTasks);

    }

    public void getFocusedTask(HttpServletResponse resp) {
        Set<String> flexibleTasks = monitor.getFocusedTasks();
        Http.responseJson(resp, flexibleTasks);
    }

    public void getFocusedTaskMap(HttpServletResponse resp) {
        FocusedTaskMap focusedTaskMap = monitor.getFocusedTaskMap();
        Http.responseJson(resp, focusedTaskMap);
    }

    public void getFocusedTaskBeanSet(HttpServletResponse resp) {
        Http.responseJson(resp, monitor.getFocusedTaskBeanSet());
    }

    public void getFirstFlexibleTaskBeanSet(HttpServletResponse resp) {
        Http.responseJson(resp, monitor.getFirstFlexibleTaskBeanSet());
    }


}
