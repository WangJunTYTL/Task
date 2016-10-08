<%--
  Created by IntelliJ IDEA.
  User: wangjun
  Date: 15/1/27
  Time: 上午11:03
  To change this weixin.template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!doctype html>
<html lang="zh-CN">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- 上述3个meta标签*必须*放在最前面，任何其他内容都*必须*跟随其后！ -->
    <meta name="description" content="">
    <meta name="author" content="">
    <title>Url Search</title>
    <!-- Bootstrap core CSS -->
    <link href="/css/bootstrap-3.5.css" rel="stylesheet">
    <script src="/js/jquery-1.9.1.js"></script>
    <script src="/js/bootstrap-3.5.js"></script>
</head>
<style>
    body {
        background: url("/image/header.jpg");
        /*background-position: center;*/
    }

    nav {
        opacity: 0.5;
    }

    a:hover {
        text-decoration: none;
    }
</style>
<body>
<nav class="navbar navbar-default">
    <div class="container-fluid">
        <div class="navbar-header">
            <a class="navbar-brand" href="#">
                <img src="/image/123.png" alt="Brand" height="28px">
            </a>
        </div>
    </div>
</nav>
<div class="container">
    <div class="row">
        <div class="col-md-3">
            <a href="/">
                <div class="panel panel-primary">
                    <div class="panel-heading">一切从这里开始</div>
                    <div class="panel-body">
                        让一切变的更简单,这里是属于你的<img src="/image/123.png" height="20px">,点击开始加入你的网址
                    </div>
                </div>
            </a>
        </div>
        <div class="col-md-3">
            <a href="/">
                <div class="panel panel-info">
                    <div class="panel-heading">Task监控</div>
                    <div class="panel-body">
                        Task是一个轻量级的分布式任务计算系统
                    </div>
                </div>
            </a>
        </div>
        <div class="col-md-3">
            <a href="#">
                <div class="panel panel-default">
                    <div class="panel-heading">
                        <h3 class="panel-title">Panel title</h3>
                    </div>
                    <div class="panel-body">
                        Panel content
                    </div>
                </div>
            </a>
        </div>
    </div>
</div>
</body>
</html>