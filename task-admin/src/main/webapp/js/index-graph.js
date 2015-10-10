$(function () {

    var chartData = $("div[id^='chartGraph_']");
    for (var n = 0; n < chartData.length; n++) {
        var parseData = JSON.parse($(chartData[n]).html());
        var data = {
            labels: parseData.label,
            datasets: [
                {
                    label: "pop",
                    fillColor: "rgba(220,220,220,0.5)",
                    strokeColor: "rgba(220,220,220,1)",
                    pointColor: "rgba(220,220,220,1)",
                    pointStrokeColor: "#fff",
                    data: parseData.pop
                },
                {
                    label: "push",
                    fillColor: "rgba(151,187,205,0.5)",
                    strokeColor: "rgba(151,187,205,1)",
                    pointColor: "rgba(151,187,205,1)",
                    pointStrokeColor: "#fff",
                    data: parseData.push
                }
            ]
        }
        var div = $("<div>").attr("class", "col-xs-12 col-sm-4 placeholder");
        var title = $("<h5>").html(parseData.id);
        var canvas = $("<canvas>").attr("id", "chart" + n).attr("height", 300);
        $(div).append(title);
        $(div).append(canvas);
        $("#graph_canvas").append(div);
        var ctx = document.getElementById("chart" + n).getContext("2d");
        new Chart(ctx).Line(data, {
            multiTooltipTemplate: "<%= datasetLabel %> : <%= value %>",
            scaleOverlay: true,
            datasetFill: true
        });
    }
});