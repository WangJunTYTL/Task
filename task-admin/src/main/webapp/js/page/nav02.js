$(function () {
    $.post("/conf/cluster", function (data) {
        var clusterList = JSON.parse(data);
        var currentCluster = $("#currentCluster").html();
        for (var i = 0; i < clusterList.length; i++) {
            if(currentCluster == clusterList[i]){
                $("#cluster_list").append($("<li>").append($("<a class='cluster_nav' href='javascript:void(0)'>").attr("class","active").html(clusterList[i])));
            }else{
                $("#cluster_list").append($("<li>").append($("<a class='cluster_nav' href='javascript:void(0)'>").html(clusterList[i])));
            }

        }
        /*var url = window.location;
        var element = $('ul.nav a').filter(function() {
            return this.href == url;
        }).addClass('active').parent().parent().addClass('in').parent();
        if (element.is('li')) {
            element.addClass('active');
        }*/
    });

    $(document).on('click','.cluster_nav',function () {
        var name = $(this).html();
        var url = window.location.pathname
        window.location.href = url+"?currentCluster="+name;
    });
});