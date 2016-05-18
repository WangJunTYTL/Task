<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  ~ Copyright (c) 2014.
  ~ Author WangJun
  ~ Email  wangjuntytl@163.com
  --%>


<!-- Navigation -->
<!-- Left side column. contains the logo and sidebar -->
<aside class="main-sidebar">
    <!-- sidebar: style can be found in sidebar.less -->
    <section class="sidebar">
        <!-- search form -->
        <form action="#" method="get" class="sidebar-form">
            <div class="input-group">
                <input type="text" name="q" class="form-control" placeholder="Search...">
              <span class="input-group-btn">
                <button type="submit" name="search" id="search-btn" class="btn btn-flat"><i class="fa fa-search"></i>
                </button>
              </span>
            </div>
        </form>
        <!-- /.search form -->
        <!-- sidebar menu: : style can be found in sidebar.less -->
        <ul class="sidebar-menu">
            <li class="header">MAIN NAVIGATION</li>
            <li class="active treeview">
                <a href="#">
                    <i class="fa fa-dashboard"></i> <span>集群列表</span> <i class="fa fa-angle-left pull-right"></i>
                </a>
                <ul class="treeview-menu" id="cluster_list">
                </ul>
            </li>
            <li class="treeview">
                <a href="#">
                    <i class="fa fa-files-o"></i>
                    <span>帮助文档</span>
                </a>
                <ul class="treeview-menu">
                    <li>
                        <a href="/static/page/overview.html" target="_blank">概述</a>
                    </li>
                    <li>
                        <a href="/static/page/quickstart.html" target="_blank">快速上手</a>
                    </li>
                    <li>
                        <a href="/static/page/taskconf.html" target="_blank">Task配置</a>
                    </li>
                    <li>
                        <a href="/static/page/taskmonitor.html" target="_blank">部署监控</a>
                    </li>
                    <li>
                        <a href="/static/page/taskproduce.html" target="_blank">任务生产</a>
                    </li>
                    <li>
                        <a href="/static/page/dispatch.html" target="_blank">调度策略</a>
                    </li>
                    <li>
                        <a href="/static/page/extension.html" target="_blank">扩展性</a>
                    </li>
                    <li>
                        <a href="https://github.com/WangJunTYTL/task" target="_blank">项目地址</a>
                    </li>
                </ul>
            </li>
            <li class="header">LABELS</li>
            <li class="treeview">
                <a href="#">
                    <i class="fa fa-circle-o text-primary"></i>
                    <span>刷新频率</span>
                </a>
                <ul class="treeview-menu">
                    <li>
                        <a class="page-refresh" href="javascript:void(0)">5s</a>
                    </li>
                    <li>
                        <a class="page-refresh" href="javascript:void(0)">10s</a>
                    </li>
                    <li>
                        <a class="page-refresh" href="javascript:void(0)">30s</a>
                    </li>
                    <li class="active">
                        <a class="page-refresh" href="javascript:void(0)">60s</a>
                    </li>
                </ul>
            </li>
            <%--<li><a href="#"><i class="fa fa-circle-o text-red"></i> <span>Important</span></a></li>
            <li><a href="#"><i class="fa fa-circle-o text-yellow"></i> <span>Warning</span></a></li>
            <li><a href="#"><i class="fa fa-circle-o text-aqua"></i> <span>Information</span></a></li>
            <li><a href="#"><i class="fa fa-circle-o text-danger"></i> <span>Information</span></a></li>--%>
        </ul>
    </section>
    <!-- /.sidebar -->
</aside>

<span style="display: none" id="currentCluster">${currentCluster}</span>
<script src="/js/page/nav.js"></script>
