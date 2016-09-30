package com.peaceful.task.ops.common;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

/**
 * @author WangJun <wangjuntytl@163.com>
 * @version 1.0 15/9/25
 * @since 1.6
 */

public class ClusterConsoleFilter implements Filter {


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String currentCluster = request.getParameter("currentCluster");
        if (currentCluster == null || currentCluster.equals("")) {
            List<String> keys = Conf.getConf().clusterList;
            for (String key : keys) {
                currentCluster = key;
                break;
            }
        }
        request.setAttribute("currentCluster", currentCluster);
        filterChain.doFilter(request, servletResponse);
    }

    @Override
    public void destroy() {

    }
}
