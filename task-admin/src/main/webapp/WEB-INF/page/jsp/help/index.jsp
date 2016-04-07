<%--
  Created by IntelliJ IDEA.
  User: wangjun
  Date: 15/1/27
  Time: 上午11:03
  To change this weixin.template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:include page="../../../../template/pageHeader02.jsp"></jsp:include>
<jsp:include page="../../../../template/nav02.jsp"></jsp:include>
<!--语法高亮-->
<link href="http://cdn.bootcss.com/highlight.js/8.0/styles/monokai_sublime.min.css" rel="stylesheet">
<script src="http://cdn.bootcss.com/highlight.js/8.0/highlight.min.js"></script>
<script>hljs.initHighlightingOnLoad();</script>
<div id="page-wrapper">

    <div class="container-fluid">
        <div class="row">
            <div class="col-lg-12">
                <h1 class="page-header">Task是什么</h1>
                <div>
                    <p>
                        Task是一个轻量级分布式任务计算组件,让你通过一行代码就可以把你本有的方法变成可分布式执行的方法.正如下面所示
                    </p>

                        <pre>
                        <code>
public class Hello {

@Task("Test16")
public void say(String msg) throws InterruptedException {
Thread.sleep(1000);
System.out.println(msg);
}
}
                        </code>
                        </pre>
                </div>

            </div>
            <!-- /.col-lg-12 -->
        </div>
        <!-- /.row -->
    </div>
    <!-- /.container-fluid -->

</div>
<jsp:include page="../../../../template/pageFooter02.jsp"></jsp:include>

