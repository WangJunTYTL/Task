<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  ~ Copyright (c) 2014.
  ~ Author WangJun
  ~ Email  wangjuntytl@163.com
  --%>
<%--图标--%>
<link rel="stylesheet" href="/css/font-awesome.min.css">

<%--左侧导航栏--%>
<link rel="stylesheet" href="/css/metisMenu.min.css">
<script src="/js/metisMenu.min.js"></script>

<link rel="stylesheet" href="/static/template02/sb-admin-2.css">
<script src="/static/template02/sb-admin-2.js"></script>

<div id="wrapper">
    <!-- Sidebar -->
    <div id="sidebar-wrapper">
        <div class="mysidebar-brand">
            <li>
                <a href="#">
                    Task Dashboard V2.6
                </a>
            </li>
        </div>
        <div class="navbar-default sidebar" role="navigation">

            <div class="sidebar-nav navbar-collapse">
                <ul class="nav" id="side-menu">
                    <li class="active">
                        <a href="#"><i class="fa fa-sitemap fa-fw"></i> 集群列表<span class="fa arrow"></span></a>
                        <ul class="nav nav-second-level" id="cluster_list">
                        </ul>
                        <!-- /.nav-second-level -->
                    </li>
                    <li>
                        <a href="#"><i class="fa fa-files-o fa-fw"></i> 帮助文档<span class="fa arrow"></span></a>
                        <ul class="nav nav-second-level">
                            <li>
                                <a href="/static/page/overview.html" target="_blank">概述</a>
                            </li>
                            <li>
                                <a href="/static/page/quickstart.html" target="_blank">快速上手</a>
                            </li>
                            <li>
                                <a href="/static/page/taskmonitor.html" target="_blank">部署监控</a>
                            </li>
                            <li>
                                <a href="/static/page/dispatch.html" target="_blank">调度策略</a>
                            </li>
                            <li>
                                <a href="/static/page/taskmonitor.html" target="_blank">扩展性</a>
                            </li>
                            <li>
                                <a href="/static/page/taskmonitor.html" target="_blank">疑问解答</a>
                            </li>
                            <li>
                                <a href="https://github.com/WangJunTYTL/task" target="_blank">项目地址</a>
                            </li>
                        </ul>
                        <!-- /.nav-second-level -->
                    </li>
                </ul>
            </div>
            <!-- /.sidebar-collapse -->
        </div>
    </div>
    <!-- /#sidebar-wrapper -->

    <nav class="navbar navbar-default navbar-static-top">
        <div class="container-fluid">
            <div class="navbar-header">
                <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar"
                        aria-expanded="false" aria-controls="navbar">
                    <span class="sr-only">Toggle navigation</span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                </button>
                <a class="navbar-brand" href="/" id="menu-toggle"><span class="glyphicon glyphicon-align-justify"
                                                                        aria-hidden="true"></span></a>
            </div>

            <div id="navbar" class="navbar-collapse collapse">
                <ul class="nav navbar-nav navbar-right">
                    <li><a href="/">Dashboard</a></li>
                    <li><a href="#">Settings</a></li>
                    <li><a href="#">Profile</a></li>
                    <li><a href="/help">Help</a></li>
                </ul>
                <form class="navbar-form navbar-right">
                    <input type="text" class="form-control" placeholder="Search...">
                </form>
            </div>
        </div>
    </nav>
    <!-- Menu Toggle Script -->
    <script>
        $(document).on('click', '#menu-toggle', function (e) {
            e.preventDefault();
            $("#wrapper").toggleClass("toggled");
        });
    </script>

    <!-- Page Content -->
    <div id="page-content-wrapper">