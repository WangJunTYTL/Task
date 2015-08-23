<!DOCTYPE html><html>

<head>
<meta charset="utf-8">
<title></title>
<link rel="stylesheet" type="text/css" href="Clearness.css">

<style> @media print{ {.hljs{overflow: visible; word-wrap: break-word;} }</style></head><body>
<h2 id="toc_0">任务容器组件设计与使用</h2>

<h3 id="toc_1">目录</h3>

<ul>
<li>
<a href="#toc_0">任务容器组件设计与使用</a>
<ul>
<li>
<a href="#toc_1">目录</a>
</li>
<li>
<a href="#toc_2">概述</a>
</li>
<li>
<a href="#toc_3">版本</a>
</li>
<li>
<a href="#toc_4">设计模型</a>
</li>
<li>
<a href="#toc_5">使用</a>
<ul>
<li>
<ul>
<li>
<a href="#toc_6">1. 加入下面依赖</a>
</li>
<li>
<a href="#toc_7">2. 配置</a>
</li>
</ul>
</li>
</ul>
</li>
<li>
<a href="#toc_8">启动</a>
<ul>
<li>
<ul>
<li>
<a href="#toc_9">1. 嵌入模式</a>
</li>
<li>
<a href="#toc_10">2. 作为job机单独运行</a>
</li>
<li>
<a href="#toc_11">启动后输出以下log日志说明启动成功</a>
</li>
</ul>
</li>
</ul>
</li>
<li>
<a href="#toc_12">log配置</a>
<ul>
<li>
<ul>
<li>
<a href="#toc_13">1. 支持日志框架</a>
</li>
<li>
<a href="#toc_14">2. 建议配置格式(log4j为例)</a>
</li>
</ul>
</li>
</ul>
</li>
<li>
<a href="#toc_15">提交任务到任务中心</a>
<ul>
<li>
<ul>
<li>
<a href="#toc_16">比如你有一个Hello的对象</a>
</li>
<li>
<a href="#toc_17">注册该类到任务容器中</a>
</li>
<li>
<a href="#toc_18">一旦你注册Hello对象后，Hello对象所有的方法将会变成异步，只需要一次性注册。然后你就可以正常调用你的方法了，如下，测试该方法已是一个异步调用的方法</a>
</li>
</ul>
</li>
</ul>
</li>
<li>
<a href="#toc_19">任务提交和执行过程</a>
</li>
<li>
<a href="#toc_20">手动定时job</a>
</li>
<li>
<a href="#toc_21">任务容器集群</a>
<ul>
<li>
<ul>
<li>
<a href="#toc_22">集群管理</a>
</li>
<li>
<a href="#toc_23">各集群节点之间的任务调度</a>
</li>
<li>
<a href="#toc_24">异常处理</a>
</li>
</ul>
</li>
</ul>
</li>
<li>
<a href="#toc_25">监控</a>
</li>
</ul>
</li>
</ul>


<h3 id="toc_2">概述</h3>

<p>适用于java项目中需要异步处理、并发处理、任务调度等业务处理场景。 </p>

<p>先看一个简单的用例，通过注册原生的Java对象到任务容器中，快速让你的方法实现异步并发调用：</p>

<pre><code># Java原生对象
public class Hello {

    @Task(queue = &quot;testQueue&quot;)
    public void test(String str) {
        Util.report(str);
    }
}

// 注册原生Java对象到任务容器中
Hello hello = TaskSchedule.registerASyncClass(Hello.class);

// 此处调用test方法已经变成异步并发调用了
hello.test(&quot;hello world&quot;);
</code></pre>

<h3 id="toc_3">版本</h3>

<p>此文档只适合2.0-SNAPSHOT版本</p>

<h3 id="toc_4">设计模型</h3>

<p>【任务存储中心】：各个业务提交的task到任务存储中心，提交成功后将返回一个唯一的任务标识码，人后任务容器会统一的进行任务的调度、执行，目前存储任务的数据结构是基于redis list数据结构，采用LPUSH、RPOP的命令操作，暂且只支持这种FIFO消费模式 </p>

<p>【任务调度路由中心】：任务分发器,负责调度任务存储中心的task给<code>worker</code>去执行，并协调监管worker的运行情况</p>

<p>【worker】：实际任务执行者，负责执行<code>调度路由中心</code>下发的任务，并在任务执行完毕后向调度路由中心汇报执行情况。调度中心根据<code>worker</code>反馈的执行情况，进行任务调度。</p>

<p>【任务存储与调度监管】：设计该角色的目的主要是为了监管<code>任务存储中心</code>和<code>调度路由中心</code>的情况，它在两者之间起到一个纽带的作用，如果任务存储中心发生大量积压，<code>监管者</code>可以发出报警，同时他也可以监控<code>调度路由中心</code>的压力，考虑是否自动增加<code>路由中心</code>的处理能力</p>

<p>【任务容器】：我们把整个组件系统称之为任务容器组件，上面的介绍的这些组件成员对我们开发人员是完全透明，在使用时，开发者不必关心这些这些成员，只需要把你要做的事情告诉任务容器，任务容器负责整个任务中心的调度，负载与异常处理。系统各个成员的协作设计采用<code>akka</code>框架。akka是一个优秀的无锁并发设计组件，它实现了actor模型，利用这种模型可以很方便的设计出上面所说的这几种成员对象并协调这些成员对象的工作。</p>

<p><img src="design.png" width='600px' ></img></p>

<h3 id="toc_5">使用</h3>

<h5 id="toc_6">1. 加入下面依赖</h5>

<pre><code> &lt;dependency&gt;
        &lt;groupId&gt;cn.edaijia&lt;/groupId&gt;
        &lt;artifactId&gt;task-container&lt;/artifactId&gt;
        &lt;version&gt;2.0-SNAPSHOT&lt;/version&gt;
 &lt;/dependency&gt;
</code></pre>

<h5 id="toc_7">2. 配置</h5>

<p>在项目的resource目录的根目录下加入配置文件：taskContainer.conf，配置内容如下 </p>

<pre><code> taskContainer{

    version = 2.0

    # 使用服务的项目名，主要用来防止和别的项目在使用redis 队列时有同名冲突
    projectName:crmWeb

    # 需要用到的队列，实际创建的redis队列是queueName_ProjectName
    taskList[commonTask,testTaskQueue]

    # 调度路由中心调度者的个数
    router:6

    # worker 负责执行任务的个数
    worker:28

    # 处理队列类的包名全路径 2.0 版本后已经移除该配置
    processTaskClass:&quot;cn.edaijia.task.manage.demo.Process&quot;

    # redis 集群服务节点 2.0 版本新增
    redisClusterNode:&quot;haproxy&quot;

    # 监控队列积压情况，报警
    alertPhone:&quot;15652636152,13426031637,13810759781,18202794850,18612013051&quot;

}
</code></pre>

<h3 id="toc_8">启动</h3>

<h5 id="toc_9">1. 嵌入模式</h5>

<pre><code>1. 嵌入到web容器中，容器初始化过程中，加入下面code：

    TaskContainer.start();

2. 导出jar文件直接运行，启动之后会在后台监听任务储存中心

    程序入口cn.edaijia.task.container.Main
</code></pre>

<h5 id="toc_10">2. 作为job机单独运行</h5>

<pre><code>//todo  管理命令、界面待开发
</code></pre>

<h5 id="toc_11">启动后输出以下log日志说明启动成功</h5>

<pre><code>cn.edaijia.task.container(TaskContainerConf.java:95) ## ------------task container suc load conf---------------
cn.edaijia.task.container(TaskContainerConf.java:96) ## project.name:crmWeb
cn.edaijia.task.container(TaskContainerConf.java:97) ## router:6
cn.edaijia.task.container(TaskContainerConf.java:98) ## worker:28
cn.edaijia.task.container(TaskContainerConf.java:99) ## max.parallel:168
cn.edaijia.task.container(TaskContainerConf.java:100) ## task.list:[syncOrderDataToEs, defaultPushMethod, testQueue]
cn.edaijia.task.container(TaskContainerConf.java:101) ## process.task.class:cn.edaijia.task.manage.demo.Process
cn.edaijia.task.container(TaskContainerConf.java:102) ## -------------------------------------------------------
</code></pre>

<h3 id="toc_12">log配置</h3>

<h5 id="toc_13">1. 支持日志框架</h5>

<p>任务容器组件是采用slf4j的接口形式嵌入到代码中的，所以你可以采用支持实现slf4j的日志系统，比如常用的log4j、logback，组件本身不会携带任何具体实现的日志组件</p>

<h5 id="toc_14">2. 建议配置格式(log4j为例)</h5>

<pre><code>&lt;!--文件输出appender，建议日志格式按照下面方式配置--&gt;
&lt;appender name=&quot;taskContainer-file-appender&quot; class=&quot;org.apache.log4j.DailyRollingFileAppender&quot;&gt;
    &lt;param name=&quot;File&quot; value=&quot;/data/logs/project/task.log&quot;/&gt;
    &lt;param name=&quot;DatePattern&quot; value=&quot;&#39;.&#39;yyyy-MM-dd&#39;.log&#39;&quot;&gt;&lt;/param&gt;
    &lt;layout class=&quot;org.apache.log4j.PatternLayout&quot;&gt;
        &lt;param name=&quot;ConversionPattern&quot; value=&quot;akka-%d %-5p %X{akkaSource} - %m%n&quot;/&gt;
    &lt;/layout&gt;
&lt;/appender&gt;

&lt;!--开发时期建议log也设为info级别--&gt;
&lt;logger name=&quot;cn.edaijia.task.container&quot;&gt;
    &lt;level value=&quot;info&quot;&gt;&lt;/level&gt;
&lt;/logger&gt;

&lt;!--单独把这个namespace空间的log放在一个目录，里面只会输出任务调度的log信息，其它log可以根据自己的需要存放--&gt;
 &lt;logger name=&quot;cn.edaijia.task.container.process&quot; additivity=&quot;false&quot;&gt;
    &lt;appender-ref ref=&quot;taskContainer-file-appender&quot;&gt;&lt;/appender-ref&gt;
&lt;/logger&gt;
</code></pre>

<h3 id="toc_15">提交任务到任务中心</h3>

<p>2.0 改版后任务提交的方式很简单，1.0的任务提交方式已经弃用。在2.0版本中，你可以让你的代码中的任何一个方法作为异步调用，你只需要把方法所属的对象注册到<code>任务容器</code>中。</p>

<h5 id="toc_16">比如你有一个Hello的对象</h5>

<pre><code>public class Hello {

    ## 注解用于配置该方法如果作为异步将在`任务存储中心`的位置，如果不添加该注解，默认会在commonQueue中
    @Task(queue = &quot;testQueue&quot;)
    public void test(String str) {
        Util.report(str);
    }
}
</code></pre>

<h5 id="toc_17">注册该类到任务容器中</h5>

<pre><code>Hello hello = TaskSchedule.registerASyncClass(Hello.class);
</code></pre>

<h5 id="toc_18">一旦你注册Hello对象后，Hello对象所有的方法将会变成异步，只需要一次性注册。然后你就可以正常调用你的方法了，如下，测试该方法已是一个异步调用的方法</h5>

<pre><code>hello.test(&quot;hello world&quot;);
</code></pre>

<h3 id="toc_19">任务提交和执行过程</h3>

<p>【注册异步类】： <code>TaskSchedule.registerASyncClass</code>的主要作用获取指定类的代理类，目的只要是打断正常方法的调用，获取方法的调用信息，包括方法名，参数，参数类型，然后封装我Task2对象。</p>

<p>【任务提交】：例如，当在调用<code>hello.test(&quot;hello world&quot;)</code>，此时，test方法并没有执行，而是提取到调用信息，封装为Task2，然后序列化存储到<code>任务存储中心</code>。</p>

<p>【异步执行过程】：<code>调度路由中心</code>从<code>任务存储中心</code>获取到任务信息，通过反序列化为Task2对象，然后通过Task2对象描述的方法调用信息反射调用该方法。</p>

<p>整个过程如下：</p>

<p><img src="design2.png" width='300px' ></img></p>

<h3 id="toc_20">手动定时job</h3>

<p>该功能的存在只是作为该组件的一个附带功能，主要是为了弥补spring的定时job不足的地方。在spring的job中，你不可以在code需要的地方启动的一个job，也不可以在code需要的地方随时停掉job。</p>

<pre><code>   // 新建一个job,名为test 
   TimingJob.newJob(&quot;test&quot;, new Job() {

               @Override
               public void logic() {
                   Util.report(&quot;hello world&quot;);
               }
           }
   );
   // 5s后执行名为test的job一次
   TimingJob.scheduleOnce(5,TimeUnit.SECONDS,&quot;test&quot;);

   // 立马执行名为test的job，并每隔一秒执行一次
   TimingJob.schedule(scala.concurrent.duration.Duration.Zero(),1, TimeUnit.SECONDS, &quot;test&quot;);

   //取消名为test的job
   TimingJob.cancel(&quot;test&quot;);
</code></pre>

<h3 id="toc_21">任务容器集群</h3>

<h5 id="toc_22">集群管理</h5>

<p>下图是一个简单的任务容器集群模型，在这个集群模型，以redis集群来作为任务的存储中心，嵌入任务容器组件的服务机器3台，作为任务的调度者和执行者。这3台机器则都会去任务存储机器去调度任务并在自己机器上执行。本质上这3台机器是没有任何关系的。在这种集群状态中，各个节点都需要去任务存储中心拉取任务，所以可以随意的增删集群几点是不会对整个集群存在的影响。所以当你发现整个集群存在压力时，可以方便的横向扩展机器</p>

<p><img src="cluster-design.png" width="300px"></p>

<h5 id="toc_23">各集群节点之间的任务调度</h5>

<p>在上文说到的3台机器之间，调度任务的优先级不存在高低之分，完全根据自己服务的压力情况去调度任务。任务调度的优先级存在随机性和自适应性。</p>

<h5 id="toc_24">异常处理</h5>

<p>业务方在在编写自己原生的Java对象时，是鼓励把异常信息扔到任务容器中，容器在收到每一个任务时，都会产生一个唯一的任务id，worker在执行业务方的逻辑时，如果捕获到发生的异常，worker会记录异常信心到日志文件中，你可以通过该id去追踪异常信息。另外，任务容器如果存在worker崩溃或者router崩溃，任务存储和调度监管将会重启他们，使系统自动恢复正常</p>

<h3 id="toc_25">监控</h3>

<p>【currentPressure】:当前任务容器处理任务的系统的压力，最大值为1，表示系统已经火力全开，达到最大处理速度</p>

<p>【commitTaskCount】：监测节点已经往任务容器提及的任务数</p>

<p>【maxParallel】:当前节点系统批处理能力的最大值，比如下面，代表可以同时处理12个任务</p>

<p>【队列任务】：代表当前节点中用到的队列有哪些以及每个队列积压的任务数 </p>

<p><img src="monitor.jpg" width="600px"></p>

</body>

</html>
