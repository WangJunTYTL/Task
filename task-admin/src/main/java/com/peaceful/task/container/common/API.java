package com.peaceful.task.container.common;

/**
 * @author WangJun <wangjuntytl@163.com>
 * @version 1.0 15/9/16
 * @since 1.6
 */

public class API {


    public final static String host = "http://127.0.0.1";
    public final static int port = 8889;
    public final static String url = "/task/console";


    public static String cat(String method) {
        return HttpClient.post(host + ":" + port + url + "?method=" + method);
    }

    public static class Method {
        public final static String focusedTask = "focusedTasks";
        public final static String focusedTaskMap = "focusedTaskMap";
        public final static String focusedTaskBeanSet = "focusedTaskBeanSet";
        public final static String firstFlexibleTaskBeanSet = "firstFlexibleTaskBeanSet";
    }


}
