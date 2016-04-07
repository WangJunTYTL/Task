<%--
  Created by IntelliJ IDEA.
  User: wangjun
  Date: 15/1/27
  Time: 上午11:03
  To change this weixin.template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>


<jsp:include page="../../../../template/pageHeader02.jsp"></jsp:include>
<jsp:include page="../../../../template/nav02.jsp"></jsp:include>
<%--<script src="//cdn.bootcss.com/holder/2.8.2/holder.js"></script>--%>
<script src="/js/holder.js"></script>
<%--<script src="/js/Chart-1.0.2.js"></script>--%>
<%--<h1 class="page-header">Dashboard</h1>--%>
<script src="/js/echart/echarts.js"></script>

<div id="page-wrapper">
    <div class="row">
        <div class="col-lg-12">
            <h3 class="sub-header">TPS</h3>
            <div class="col-lg-6">
                <div id="graph_canvas" style="height: 300px;"></div>
            </div>
            <%--<h4 class="sub-header">系统消息</h4>--%>
            <div class="col-lg-6" id="ratePanel">
                <%--<img src="holder.js/66x66?theme=social&outline=yes&text=123 \n line breaks \n anywhere">--%>
            </div>
        </div>
    </div>
    <div class="row">
    <div class="col-lg-12">
        <h4 class="sub-header">调度中心</h4>

        <div class="col-lg-12">
            <div class="panel panel-info">
                <div class="panel-heading">调度中任务</div>
                <div class="panel-body" id="taskListPanelBody">
                    <table class="table table-striped">
                        <thead>
                        <tr>
                            <th>剩余任务</th>
                            <th>队列名</th>
                            <th>调度策略</th>
                            <th>备注</th>
                            <th>最后一次提交</th>
                        </tr>
                        </thead>
                        <tbody id="taskListPanel">
                        </tbody>
                    </table>
                    <span id="expireTasks"></span>
                </div>
            </div>
        </div>

        <div class="col-lg-6">
            <div class="panel panel-success">
                <div class="panel-heading">集群</div>
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
            <div class="panel panel-success">
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
        <div class="col-lg-6">
            <div class="panel panel-warning">
                <div class="panel-heading">已完成调度</div>
                <div class="panel-body">
                    <table class="table table-striped">
                        <thead>
                        <tr>
                            <%--<th>tick</th>--%>
                        </tr>
                        </thead>
                        <tbody id="expireTaskPanel">
                        </tbody>
                    </table>
                </div>
            </div>
        </div>

    </div>
    </div>
</div>
<script src="/js/page/index.js"></script>
<jsp:include page="../../../../template/myModal.jsp"></jsp:include>
<jsp:include page="../../../../template/pageFooter02.jsp"></jsp:include>

