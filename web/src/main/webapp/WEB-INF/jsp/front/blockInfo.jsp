<%--
  Created by IntelliJ IDEA.
  User: duai
  Date: 2017-08-11
  Time: 15:54
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="../include/lib.jsp"%>
<html>
<head>
    <title>大田信息</title>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0" />
    <script src="${ctxStatic}/login/js/jquery.min.js"></script>
    <meta charset="UTF-8">
    <title>扫一扫详情</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no">
    <link rel="stylesheet" href="${ctxStatic}/blockInfo/css/index.css">
    <link rel="stylesheet" href="${ctxStatic}/blockInfo/css/reset.css">
    <script>
        function set_html_fontsize() {
            var h = Math.max(document.documentElement.clientHeight, window.innerHeight || 0);
            var w = Math.max(document.documentElement.clientWidth, window.innerWidth || 0);
            var width = w > h ? h : w;
            width = w > 720 ? 720 : w
            document.getElementsByTagName("html")[0].style.cssText = 'font-size: ' + ~~(width*100000/36)/10000+"px;";
        }
        window.orientationchange = function(){set_html_fontsize()};
        window.onresize = function(){set_html_fontsize()};
        set_html_fontsize();
    </script>
</head>
<body>
<div class="qp">
    <header class="m_header" id="m_header">当前无信息</header>
    <section class="m_section">
        <h2>信息</h2>
        <ul class="clearfix" id="content">

        </ul>
        <h2>区域描述</h2>
        <p id="description"></p>
    </section>
    <footer class="m_footer"><a href="javascript:;">下载APP</a></footer>
</div>
</body>
<script type="text/javascript">
    $.ajax({
        url:location.href,
        type:"POST",
        async:false,
        success:function (data) {
            var res = JSON.parse(data);
            if (!res.code){
                var result = res.data;
                $("#m_header").text(result.block_name);
                var html = "";
                html +='<li class="info"><em>区域面积</em><span>'+isUndefined(result.block_acreage)+'亩</span></li>';
                html +='<li class="info"><em>区域农作物</em><span>'+isUndefined(result.block_crops)+'</span></li>';
                html +='<li class="info"><em>预产量</em><span>'+isUndefined(result.block_throughput)+'</span></li>';
                html +='<li class="info"><em>生产阶段</em><span>'+isUndefined(result.block_growth_stage)+'</span></li>';
                html +='<li class="info"><em>农技员姓名</em><span>'+isUndefined(result.technician)+'</span></li>';
                html +='<li class="info"><em>农技员电话</em><span>'+isUndefined(result.technician_mobile)+'</span></li>';
                html +='<li>';
                html +='    <ul class="nest clearfix">';
                for (var p in result.machinedata){
                    html +='<li ><img src="${ctx}'+result.machinedata[p].machineImage+'" style="  vertical-align:middle;"/><u>'+result.machinedata[p].machineData+'</u></li>';
                }
                html +='    </ul>';
                html +='</li>';
                for (var p in result.tasklist){
                    var status = result.tasklist[p].task_status===1?"运行":"停止";
                    html +='<li class="info"><em>'+result.tasklist[p].task_name+'</em><span>'+status+'</span></li>';
                }
                $("#description").text(isUndefined(result.description));
                $("#content").html(html);
            }else {
                layer.msg(res.msg || '操作失败', {
                    icon : 5,
                    skin : 'layer-ext-moon',
                    time : 1500
                }, function(index) {
                    layer.close(index);
                })
            }
        }
    })
    function isUndefined(obj) {
        if (Object.prototype.toString.call(obj) === '[object Undefined]'){
            return "当前无信息";
        }
        return obj;
    }
</script>
</html>
