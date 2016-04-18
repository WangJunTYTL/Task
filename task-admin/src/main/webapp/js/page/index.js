$(function () {

    var systemName = $("#currentCluster").html();
    var task_dispatch_map = new Map();
    var task_rate_map = new Map();
    var cluster_nodes = new Array();

    var load_task_rate_data = function () {
        $.post("/rate/task", {currentCluster: systemName}, function (data) {
            var tasks = JSON.parse(data);
            $("#ratePanel").html('');
            var flag = false;
            for (var i = 0; i < tasks.length; i++) {
                task_rate_map.put(tasks[i]['task'],tasks[i]);
            }
        });
    }

    load_task_rate_data();


    //加载任务面板
    var load_task_panel = function () {
        $.post("/taskmap", {currentCluster: systemName}, function (data) {
            var taskMap = JSON.parse(data);
            $("#taskListPanel").html('');
            var expireTasks = new Array();
            var dispatchTasks = new Array();
            task_dispatch_map = new Map;
            for (key in taskMap) {
                var task = JSON.parse(taskMap[key]);
                if (task['expire']) {
                    expireTasks.push(task);
                } else {
                    dispatchTasks.push(task);
                    task_dispatch_map.put(task['name'], task);
                }
            }
            if (dispatchTasks.length == 0) {
                $("#taskListPanel").html('').append("<div class='alert'>当前任务队列为空,无可被调度任务,已调度 <a>" + expireTasks.length + "</a> 个任务</div>")
            }
            for (var i = 0; i < dispatchTasks.length; i++) {
                var task = dispatchTasks[i];
                var tr = $("<tr>");
                if (task["state"] == "isLock") {
                    tr.append($("<td>").html("<a href='javascript:void(0)' class='text-danger updatetask'  data-toggle='modal' data-target='#myModal'>" + task['name'] + "</a>"));
                } else {
                    tr.append($("<td>").html("<a href='javascript:void(0)' class='text-info updatetask'  data-toggle='modal' data-target='#myModal'>" + task['name'] + "</a>"));
                }
                // alert(task_rate_map.size());
                var rate = task_rate_map.get(task['name']);
                tr.append($("<td>").html(rate['produce']+"/s,"+rate['consume']+"/s,【"+task['reserve']+'】'));
                if (task['reserve']>687){
                    tr.attr("class","warning");
                }
                if (task["state"] == "OK") {
                    tr.append($("<td>").html('允许所有节点调度'));
                } else if (task["state"] == "isLock") {
                    tr.append($("<td>").html("不允许被任何节点调度")).attr("class", "danger");
                } else {
                    tr.append($("<td>").html("只允许被[" + task['state'] + "]调度")).attr("class", "warning");
                }
                tr.append($("<td>").html(task['desc']));
                tr.append($("<td>").html(formatDate(task['updateTime'], 'full')));
                $("#taskListPanel").append(tr);
            }
            $(document).on("click","#expireTaskMenu",function () {
                for (var i = 0; i < expireTasks.length; i++) {
                    var task = expireTasks[i];
                    var tr = $("<tr>");
                    if (task["state"] == "isLock") {
                        tr.append($("<td>").html("<a href='javascript:void(0)' class='text-danger updatetask'  data-toggle='modal' data-target='#myModal'>" + task['name'] + "</a>"));
                    } else {
                        tr.append($("<td>").html("<a href='javascript:void(0)' class='text-info updatetask'  data-toggle='modal' data-target='#myModal'>" + task['name'] + "</a>"));
                    }
                    // alert(task_rate_map.size());
                    var rate = task_rate_map.get(task['name']);
                    if (rate == null){
                        tr.append($("<td>").html("已完成调度"))
                    }else {
                        tr.append($("<td>").html(rate['produce'] + "/s," + rate['consume'] + "/s,【" + task['reserve'] + '】'));
                    }
                    if (task['reserve']>687){
                        tr.attr("class","warning");
                    }
                    if (task["state"] == "OK") {
                        tr.append($("<td>").html('允许所有节点调度'));
                    } else if (task["state"] == "isLock") {
                        tr.append($("<td>").html("不允许被任何节点调度")).attr("class", "danger");
                    } else {
                        tr.append($("<td>").html("只允许被[" + task['state'] + "]调度")).attr("class", "warning");
                    }
                    tr.append($("<td>").html(task['desc']));
                    tr.append($("<td>").html(formatDate(task['updateTime'], 'full')));
                    $("#taskListPanel").append(tr);
                }
                $(this).remove();
            });
            
        });
    }

    load_task_panel();
    



    //加载集群节点面板
    var load_node_panel = function () {
        $.post("/tasknodemap", {currentCluster: systemName}, function (data) {
            var nodeMap = JSON.parse(data);
            $("#nodeMapPanel").html('');

            for (key in nodeMap) {
                var node = JSON.parse(nodeMap[key]);
                var tr = $("<tr>");
                cluster_nodes.push(node);
                if (node["state"] == "OK") {
                    if (((Date.parse(new Date()) - node['updateTime']) < 2 * 30 * 1000)) {
                        tr.append("<td> <a class='text-info updatenode' href='javascript:void(0)' data-toggle='modal' data-target='#myModal'>" + node["hostName"] + "</a></td>");
                    } else {
                        tr.append("<td> <a class='text-warning updatenode' href='javascript:void(0)' data-toggle='modal' data-target='#myModal'>" + node["hostName"] + "</a></td>");
                    }
                    tr.append($('<td>server</td>'));
                } else {
                    tr.append("<td> <a class='text-danger updatenode' href='javascript:void(0)' data-toggle='modal' data-target='#myModal'>" + node["hostName"] + "</a></td>");
                    tr.append($('<td>client</td>'));
                    tr.attr("class","danger");
                }
                tr.append($("<td>").html(formatDate(node['updateTime'])));
                $("#nodeMapPanel").append(tr);
            }
        });
    }

    load_node_panel();

    var load_executor_panel = function () {
        $.post("/executor", {currentCluster: systemName}, function (data) {
            var executorMap = JSON.parse(data);
            for (key in executorMap) {
                var tr = $("<tr>");
                tr.append("<td>" + key + "</td>").append("<td>" + executorMap[key] + "</td>");
                $("#executorMapPanel").append(tr);
            }
        })
    }

    load_executor_panel();

    // 查看节点信息菜单
    var updatenode = function () {
        $(document).on("click", ".updatenode", function () {
            var node = $(this).html();
            $("#myModalLabel").html('').html(node);
            $.post("/tasknodemap/update", {currentCluster: systemName, node: node, method: "obtain"}, function (data) {
                var nodeObject = JSON.parse(data);
                // alert(Date.parse(new Date()))
                if ((Date.parse(new Date()) - nodeObject['updateTime']) > 2 * 30 * 1000) {
                    $("#myModalBody").html('').append("<div class='text-danger'>运行: 该节点或整个集群可能已经停止运行, 因为心跳时间存在问题,上次心跳时间是" + formatDate(nodeObject['updateTime']) + "</div>");
                } else {
                    $("#myModalBody").html('').append("<div class='text-success'>运行: 良好</div>");
                    $("#myModalBody").append("<div class='text-success'>心跳: 正常,最近一次心跳时间是" + formatDate(nodeObject['updateTime']) + "</div>");
                }
                if (nodeObject["state"] == "OK") {
                    $("#myModalBody").append("<div class='text-success'>运行模式: server[可以生产和消费任务]</div>");
                    $("#myModalBody").append("<p class='text-danger'>你是否要切换为client模式?</p>")
                    $("#modalConfirmMenu").html('').html("client")
                } else {
                    $("#myModalBody").append("<div class='text-success'>运行模式: client [只可以生产任务]</div>");
                    $("#myModalBody").append("<p class='text-danger'>你是否要切换为server模式?</p>")
                    $("#modalConfirmMenu").html('').html("server")
                }
                $(document).on('click', "#modalConfirmMenu", function () {
                    $("#myModal").modal('hide');
                    var state = nodeObject["state"];
                    if ("OK" == state) {
                        state = "client";
                    } else {
                        state = "OK";
                    }
                    $.post("/tasknodemap/update", {
                        currentCluster: systemName,
                        node: node,
                        method: "update",
                        state: state
                    }, function (data) {
                        if (data == "OK") {
                            load_node_panel();
                        } else {
                            alert("操作失败!");
                        }
                    });
                });
            });

        });
    }

    updatenode();

    // 查看任务信息
    var updatetask = function () {
        $(document).on("click", ".updatetask", function () {
            var task = $(this).html();
            $("#myModalLabel").html('').html(task);
            $.post("/taskmap/update", {currentCluster: systemName, task: task, method: "obtain"}, function (data) {
                var taskObject = JSON.parse(data);
                $("#myModalBody").html('');
                $("#myModalBody").append("<div class='text-success'>任务创建时间: " + formatDate(taskObject['createTime'], 'full') + "</div>");
                $("#myModalBody").append("<div class='text-success'>最后一次提交时间: " + formatDate(taskObject['updateTime'], 'full') + "</div>");
                if (taskObject['state'] == 'isLock') {
                    $("#myModalBody").append("<div class='text-danger'>调度策略: 不允许任何节点调度</div>");
                } else if (taskObject['state'] == 'OK') {
                    $("#myModalBody").append("<div class='text-success'>调度策略: 允许被所有节点调度</div>");
                } else {
                    $("#myModalBody").append("<div class='text-warning'>调度策略: 只允许被" + taskObject['state'] + "调度</div>");
                }
                var select = $("<select name='state'>");
                if (taskObject["state"] == "OK") {
                    select.append($("<option>").attr("value", "OK").attr("selected", "selected").html("允许所有节点调度"));
                    select.append($("<option>").attr("value", "isLock").html("不允许调度"));
                } else if (taskObject["state"] == "isLock") {
                    select.append($("<option>").attr("value", "isLock").attr("selected", "selected").html("不允许调度"));
                    select.append($("<option>").attr("value", "OK").html("允许所有节点调度"));
                } else {
                    select.append($("<option>").attr("value", "OK").html("允许所有节点调度"));
                    select.append($("<option>").attr("value", "isLock").html("不允许任何节点调度"));
                }
                $.post("/tasknodemap", {currentCluster: systemName}, function (data) {
                    var nodeMap = JSON.parse(data);
                    for (key in nodeMap) {
                        if (key == taskObject['state']) {
                            select.append($("<option>").attr("value", key).attr("selected", "selected").html("只允许被[" + key + "]调度"));
                        } else {
                            select.append($("<option>").attr("value", key).html("只允许被[" + key + "]调度"));
                        }
                    }
                });
                $("#myModalBody").append(select);

                $("#myModalBody").append("<div class='text-success'>备注信息</div>");
                var descInput = $('<input class="form-control"  placeholder="添加简要备注">');
                if (taskObject['desc'] != null || taskObject['desc'] != '') {
                    descInput.val(taskObject['desc']);
                }
                $("#myModalBody").append(descInput);
                $("#modalConfirmMenu").html('').html("请确认")
                $(document).on('click', "#modalConfirmMenu", function () {
                    $("#myModal").modal('hide');
                    var state = select.val();
                    $.post("/taskmap/update", {
                        currentCluster: systemName,
                        task: task,
                        method: "update",
                        state: state,
                        desc: descInput.val()
                    }, function (data) {
                        if (data == "OK") {
                            load_task_panel();
                        } else {
                            alert("操作失败!");
                        }
                    });
                });
            });

        });
    }

    updatetask();

    var load_total_rate_panel = function () {
        require.config({
            paths: {
                echarts: '../js/echart'
            }
        });
        require(
            [
                'echarts',
                'echarts/chart/line',   // 按需加载所需图表，如需动态类型切换功能，别忘了同时加载相应图表
                'echarts/chart/bar'
            ],
            function (ec) {
                var myChart = ec.init(document.getElementById('graph_canvas'));
                $.post("/rate/total", {currentCluster: systemName}, function (data) {
                    var parseData;
                    try {
                        parseData = JSON.parse(data);
                    } catch (error) {
                        parseData = new Object();
                        parseData['timeAxis'] = [''];
                        parseData['produceAxis'] = [''];
                        parseData['consumeAxis'] = [''];
                    }
                    var timeAxis = new Array();
                    if (parseData['timeAxis'] != null) {
                        for (var i = 0; i < parseData['timeAxis'].length; i++) {
                            timeAxis.push(formatDate(parseData['timeAxis'][i]));
                        }
                    }
                    var option = {
                        title: {
                            subtext: '单位/秒'
                        },
                        tooltip: {
                            trigger: 'axis'
                        },
                        legend: {
                            data: ['生产', '消费']
                        },
                        toolbox: {
                            show: true,
                            feature: {
                                // mark: {show: true},
                                // dataView: {show: true, readOnly: false},
                                magicType: {show: true, type: ['line', 'bar']},
                                // restore: {show: true},
                                saveAsImage: {show: true}
                            }
                        },
                        calculable: true,
                        xAxis: [
                            {
                                type: 'category',
                                boundaryGap: false,
                                data: timeAxis
                            }
                        ],
                        yAxis: [
                            {
                                type: 'value',
                                axisLabel: {
                                    formatter: '{value}'
                                }
                            }
                        ],
                        series: [
                            {
                                name: '生产',
                                symbol: 'none',
                                type: 'line',
                                data: parseData['produceAxis'],
                                smooth: true,
                                itemStyle: {normal: {areaStyle: {type: 'default'}}},
                                markPoint: {
                                    data: [
                                        {type: 'max', name: '最大值'},
                                        {type: 'min', name: '最小值'}
                                    ]
                                },
                                markLine: {
                                    data: [
                                        {type: 'average', name: '平均值'}
                                    ]
                                }
                            },
                            {
                                name: '消费',
                                type: 'line',
                                symbol: 'none',
                                smooth: true,
                                itemStyle: {normal: {areaStyle: {type: 'default'}}},
                                data: parseData['consumeAxis'],
                                markLine: {
                                    data: [
                                        {type: 'average', name: '平均值'}
                                    ]
                                }
                            }
                        ]
                    };
                    myChart.setOption(option);
                });

            }
        );
    }

    load_total_rate_panel();


    var refresh = function () {
        load_total_rate_panel();
        load_task_rate_data();
        load_node_panel();
        load_task_panel();
    }

    setInterval(refresh, 60000);


    function formatDate(timestamp, full) {
        var now = new Date(timestamp);
        var year = now.getYear();
        var month = now.getMonth() + 1;
        var date = now.getDate();
        var hour = now.getHours();
        var minute = now.getMinutes();
        var second = now.getSeconds();
        if (full == "full") {
            // return year+"/"+month+"/"+date+" "+hour + ":" + minute + ":" + second;
            return new Date(timestamp).toLocaleString().replace(/年|月/g, "-");
        } else {
            return hour + ":" + minute + ":" + second;
        }
    }

})
;

