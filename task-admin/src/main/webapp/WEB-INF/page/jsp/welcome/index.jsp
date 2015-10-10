<%--
  Created by IntelliJ IDEA.
  User: wangjun
  Date: 15/1/27
  Time: 上午11:03
  To change this weixin.template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>


<jsp:include page="../../../../template/pageHeader.jsp"></jsp:include>
<jsp:include page="../../../../template/nav.jsp"></jsp:include>

<%--<script src="//cdn.bootcss.com/holder/2.8.2/holder.js"></script>--%>
<script src="/js/holder.js"></script>
<script src="/js/Chart.js"></script>
<h1 class="page-header">Dashboard</h1>

<div class="row placeholders">
    <c:forEach items="${focusedTaskData}" var="task">
        <div class="col-xs-6 col-sm-2 placeholder">
            <c:choose>
            <c:when test="${task.remain < 666}">
                <img data-src="holder.js/66x66?auto=yes&theme=vine&text=${task.remain}" class="img-responsive"
            </c:when>
            <c:otherwise>
            <img data-src="holder.js/66x66?auto=yes&theme=lava&text=${task.remain}" class="img-responsive"
            </c:otherwise>
            </c:choose>
                 alt="Generic placeholder thumbnail">
            <h5>${task.id}</h5>
            <span class="text-muted"></span>

            <div id="chartGraph_${task.id}" style="display: none">${task.chartDataJson}</div>
        </div>
    </c:forEach>

    <c:forEach items="${firstFlexibleTaskData}" var="task">
        <div class="col-xs-6 col-sm-2 placeholder">
            <c:choose>
            <c:when test="${task.remain < 666}">
                <img data-src="holder.js/66x66?auto=yes&theme=vine&text=${task.remain}" class="img-responsive"
            </c:when>
            <c:otherwise>
            <img data-src="holder.js/66x66?auto=yes&theme=lava&text=${task.remain}" class="img-responsive"
            </c:otherwise>
            </c:choose>
                 alt="Generic placeholder thumbnail">
            <h5>${task.id}</h5>
            <span class="text-muted"></span>

            <div id="chartGraph_${task.id}" style="display: none">${task.chartDataJson}</div>
        </div>
    </c:forEach>
</div>

<h2 class="sub-header">Push Pop</h2>

<div class="container-fluid" id="graph_canvas">

    <%--<div class="col-xs-6 col-sm-4 placeholder">
        <h5> aa </h5>

        <div>
            <canvas id="myChart" height="400"></canvas>
        </div>
    </div>
    <div class="col-xs-6 col-sm-4 placeholder">
        <h5> aa </h5>

        <div>
            <canvas id="myChart2" height="400"></canvas>
        </div>
    </div>
    <div class="col-xs-6 col-sm-4 placeholder">
        <h5> aa </h5>

        <div>
            <canvas id="myChart3" height="400"></canvas>
        </div>
    </div>
    <div class="col-xs-6 col-sm-4 placeholder">
        <h5> aa </h5>

        <div>
            <canvas id="myChart4" height="400"></canvas>
        </div>
    </div>--%>
</div>

<h2 class="sub-header">Flexible Tasks</h2>

<div class="table-responsive">
    <table class="table table-striped" id="flexible-task">
        <thead>
        <tr>
            <th>批次</th>
            <th>描述</th>
            <th>积压数</th>
            <th>完成数</th>
            <th>生产速率</th>
            <th>消费速率</th>
            <th>开始时间</th>
        </tr>
        </thead>
        <tbody>
        <tr>
            <td>1,001</td>
            <td>Lorem</td>
            <td>ipsum</td>
            <td>dolor</td>
            <td>sit</td>
        </tr>
        <tr>
            <td>1,002</td>
            <td>amet</td>
            <td>consectetur</td>
            <td>adipiscing</td>
            <td>elit</td>
        </tr>
        <tr>
            <td>1,003</td>
            <td>Integer</td>
            <td>nec</td>
            <td>odio</td>
            <td>Praesent</td>
        </tr>
        <tr>
            <td>1,003</td>
            <td>libero</td>
            <td>Sed</td>
            <td>cursus</td>
            <td>ante</td>
        </tr>
        <tr>
            <td>1,004</td>
            <td>dapibus</td>
            <td>diam</td>
            <td>Sed</td>
            <td>nisi</td>
        </tr>
        <tr>
            <td>1,005</td>
            <td>Nulla</td>
            <td>quis</td>
            <td>sem</td>
            <td>at</td>
        </tr>
        <tr>
            <td>1,006</td>
            <td>nibh</td>
            <td>elementum</td>
            <td>imperdiet</td>
            <td>Duis</td>
        </tr>
        <tr>
            <td>1,007</td>
            <td>sagittis</td>
            <td>ipsum</td>
            <td>Praesent</td>
            <td>mauris</td>
        </tr>
        <tr>
            <td>1,008</td>
            <td>Fusce</td>
            <td>nec</td>
            <td>tellus</td>
            <td>sed</td>
        </tr>
        <tr>
            <td>1,009</td>
            <td>augue</td>
            <td>semper</td>
            <td>porta</td>
            <td>Mauris</td>
        </tr>
        <tr>
            <td>1,010</td>
            <td>massa</td>
            <td>Vestibulum</td>
            <td>lacinia</td>
            <td>arcu</td>
        </tr>

        <tr>
            <td>1,011</td>
            <td>eget</td>
            <td>nulla</td>
            <td>Class</td>
            <td>aptent</td>
        </tr>
        <tr>
            <td>1,012</td>
            <td>taciti</td>
            <td>sociosqu</td>
            <td>ad</td>
            <td>litora</td>
        </tr>
        <tr>
            <td>1,013</td>
            <td>torquent</td>
            <td>per</td>
            <td>conubia</td>
            <td>nostra</td>
        </tr>
        <tr>
            <td>1,014</td>
            <td>per</td>
            <td>inceptos</td>
            <td>himenaeos</td>
            <td>Curabitur</td>
        </tr>
        <tr>
            <td>1,015</td>
            <td>sodales</td>
            <td>ligula</td>
            <td>in</td>
            <td>libero</td>
        </tr>
        </tbody>
    </table>
</div>

<script src="/js/index-graph.js"></script>

<jsp:include page="../../../../template/pageFooter.jsp"></jsp:include>

