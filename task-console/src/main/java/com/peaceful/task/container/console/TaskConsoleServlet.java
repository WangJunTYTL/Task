package com.peaceful.task.container.console;

import com.peaceful.task.container.store.help.Helper;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

/**
 * Task console public api
 * <p/>
 * 任务容器对外提供的标准 http api，主要用于查看、监控、操作任务容器
 * <p/>
 * 当启动任务容器后，该api并不会已http的形式暴漏出去，需要开发者手动把这个servlet配置到web.xml文件中
 *
 * @author WangJun <wangjuntytl@163.com>
 * @version 1.0 15/9/15
 * @since 1.6
 */

public class TaskConsoleServlet extends HttpServlet {

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
        } else if (method.equals("getRunningInfo")) {
            getRunningInfo(response);
        } else if (method.equals("secondFlexibleTaskBeanSet")) {
            getSecondFlexibleTaskBeanSet(response);
        } else {
            Http.responseString(response, "params error");
        }
    }


    /**
     * 获取所有的flexible task name list
     *
     * @param resp HttpServletResponse
     */
    public void getFlexibleTask(HttpServletResponse resp) {
        Set<String> flexibleTasks = monitor.getFlexibleTasks();
        Http.responseJson(resp, flexibleTasks);

    }

    /**
     * 获取所有的focused task name list
     *
     * @param resp HttpServletResponse
     */
    public void getFocusedTask(HttpServletResponse resp) {
        Set<String> flexibleTasks = monitor.getFocusedTasks();
        Http.responseJson(resp, flexibleTasks);
    }

    /**
     * 获取所有的 focused task Map
     *
     * @param resp HttpServletResponse
     */
    public void getFocusedTaskMap(HttpServletResponse resp) {
        FocusedTaskMap focusedTaskMap = monitor.getFocusedTaskMap();
        Http.responseJson(resp, focusedTaskMap);
    }

    /**
     * 获取所有的 focused task data
     *
     * @param resp HttpServletResponse
     */
    public void getFocusedTaskBeanSet(HttpServletResponse resp) {
        Http.responseJson(resp, Helper.remoteFocusedRepo.get());
    }

    /**
     * 获取所有的 first flexible task data
     *
     * @param resp HttpServletResponse
     */
    public void getFirstFlexibleTaskBeanSet(HttpServletResponse resp) {
        Http.responseJson(resp, Helper.remoteFirstFlexibleRepo.get());
    }

    /**
     * 获取所有的 second flexible task data
     *
     * @param resp HttpServletResponse
     */
    public void getSecondFlexibleTaskBeanSet(HttpServletResponse resp) {
        Http.responseJson(resp, Helper.remoteSecondFlexibleRepo.get());
    }

    /**
     * 获取运行信息
     *
     * @param resp HttpServletResponse
     */
    public void getRunningInfo(HttpServletResponse resp) {
        Http.responseJson(resp, monitor.getRunningInfo());
    }


}
