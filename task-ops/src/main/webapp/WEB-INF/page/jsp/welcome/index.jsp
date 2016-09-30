<%--
  Created by IntelliJ IDEA.
  User: wangjun
  Date: 15/1/27
  Time: 上午11:03
  To change this weixin.template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>


<jsp:include page="../../../../template/04/pageHeader.jsp"></jsp:include>
<jsp:include page="../../../../template/04/nav.jsp"></jsp:include>
<%--<script src="//cdn.bootcss.com/holder/2.8.2/holder.js"></script>--%>
<script src="/js/holder.js"></script>
<%--<script src="/js/Chart-1.0.2.js"></script>--%>
<%--<h1 class="page-header">Dashboard</h1>--%>
<script src="/js/echart/echarts.js"></script>

<!-- Content Wrapper. Contains page content -->
<div class="content-wrapper">
    <!-- Content Header (Page header) -->
    <section class="content-header">
        <h1>
            Dashboard
            <small>${currentCluster}</small>
        </h1>
        <ol class="breadcrumb">
            <li><a href="#"><i class="fa fa-dashboard"></i> Home</a></li>
            <li class="active">Dashboard</li>
        </ol>
    </section>
    <!-- Main content -->
    <section class="content">
        <div class="row">
            <div class="col-lg-12">
                <div class="panel panel-info">
                    <div class="panel-heading">
                        TPS
                    </div>
                    <div class="panel-body">
                        <div id="graph_canvas" style="height: 300px;"></div>
                    </div>
                </div>
            </div>
        </div>

        <div class="row">
            <div class="col-lg-12">
                <div class="panel panel-info">
                    <div class="panel-heading">Dispatch</div>
                    <div class="panel-body" id="taskListPanelBody">
                        <table class="table table-striped">
                            <thead>
                            <tr>
                                <th>队列名</th>
                                <th>生产/消费/剩余</th>
                                <th>调度策略</th>
                                <th>备注</th>
                                <th>最后一次提交</th>
                            </tr>
                            </thead>
                            <tbody id="taskListPanel">
                            </tbody>
                            <tfoot>
                            <th>
                                <a href="javascript:void(0)" id="expireTaskMenu">
                                    查看已调度完成任务
                                    <i class="fa fa-arrow-circle-right"></i>
                                </a>
                            </th>
                            </tfoot>
                        </table>

                        <span id="expireTasks"></span>
                    </div>
                </div>
                <div class="row">
                    <div class="col-lg-6">
                        <div class="panel panel-info">
                            <div class="panel-heading">Cluster</div>
                            <div class="panel-body">
                                <table class="table table-striped">
                                    <thead>
                                    <tr>
                                        <th>hostname</th>
                                        <th>model</th>
                                        <th>tick</th>
                                    </tr>
                                    </thead>
                                    <tbody id="nodeMapPanel">
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                    <div class="col-lg-6">
                        <div class="panel panel-info">
                            <div class="panel-heading">Executor</div>
                            <div class="panel-body">
                                <table class="table table-striped">
                                    <thead>
                                    <tr>
                                        <%--<th>tick</th>--%>
                                    </tr>
                                    </thead>
                                    <tbody id="executorMapPanel">
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </section>
</div>
<script src="/js/page/index.js"></script>
<jsp:include page="../../../../template/myModal.jsp"></jsp:include>
<jsp:include page="../../../../template/04/pageFooter.jsp"></jsp:include>

