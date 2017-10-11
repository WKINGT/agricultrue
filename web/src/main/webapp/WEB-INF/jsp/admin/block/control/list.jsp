<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="../../../include/lib.jsp" %>
		<script src="${ctxStatic}/assets/js/date-time/bootstrap-datepicker.js"></script>
		<script src="${ctxStatic}/assets/js/date-time/moment.js"></script>
		<script src="${ctxStatic}/assets/js/date-time/daterangepicker.js"></script>
		<script src="${ctxStatic}/assets/js/date-time/bootstrap-datetimepicker.js"></script>
		<script type="text/javascript" src="${ctxStatic}/js/layer/layer.js"></script>
		<script type="text/javascript" src="${ctxStatic}/js/plugins/grid.js"></script>
		<script type="text/javascript" src="${ctxStatic}/js/plugins/filter.js"></script>
		<script type="text/javascript" src="${ctxStatic}/js/plugins/websocketUtils.js"></script>
		<script type="text/javascript" src="${ctxStatic}/js/other.js"></script>

<script type="text/javascript">
		var grid
        var url = xgs.utils.setToken(location.href,'${token}&blockId=${blockId}');
		jQuery(function($) {
			grid = $(".page-content").xgsGrid({
				//[{"model":"sys_menu","param":null,"ctl":"sys.menu","type":"","id":"c4ca4238a0b923820dcc509a6f75849b","setting":"","operation":null,"is_enabled":false,"updated":"","created":"","description":null,"name":"菜单管理","is_del":false,"act":"list"},{"model":"sys_functions","param":null,"ctl":"sys.functions","type":"","id":"c81e728d9d4c2f636f067f89cc14862c","setting":"","operation":null,"is_enabled":false,"updated":"","created":"","description":null,"name":"功能管理","is_del":false,"act":"list"}]
				filter:[],
				url:url,
                showCheckBox:false,
                initComplete:function () {
                    if(xgs.utils.isUndefined(xgs.WebSocket.socket())){
                        xgs.WebSocket.init("${url}","${userId}",$("body"));
                    }
                },
				title:'<span style="font-size: 13px;" >设备列表  <span style="margin-left: 35%" id="loginStatus"></span></span>',
				clomuns:[
				   {
		                data: 'machine_name',
		                title:'设备名'
		            },
                    {
                        data: 'system_id',
                        title:'系统id'
                    },
                    {
                        data: 'device_id',
                        title:'设备id'
                    },
                    {
                        data: 'id',
                        title:'操作',
						render:function (data,type,rowData) {
                            if (rowData.machineparams.length<=0||rowData.machineparams.length>2){
                                return "当前设备无操作";
                            }
                            var check_on = "";
                            var check_off ="";
                            var status = "";
                            for (var i = 0 ;i<rowData.machineparams.length;i++){
                                if (rowData.machineparams[i].param_name.indexOf("开")>-1){
                                    check_on = rowData.machineparams[i].param_command;
                                }else {
                                    check_off = rowData.machineparams[i].param_command;
                                }
                                if (+rowData.machineparams[i].param_command===+rowData.controller_data){
                                    status = rowData.machineparams[i].param_name;
                                }
                            }
                            var  html = "";
							var checked =status==='开'?'checked':'';
                            html += '<div  class="container_webscket" style="width: 42px;margin-bottom: 8px;height:24px;float:left;">';
                            html += '<div class="bg_con">';
                            html += '<input id = "'+rowData.machine_id+'"  device_id="'+rowData.device_id+'"  machine-name="'+rowData.machine_name+'" type="checkbox" onclick="sendMsg($(this),'+JSON.stringify(rowData).replace(/"/g, '&quot;') + ')" class="switch" check-on="'+check_on+'" check-off="'+check_off+'" '+checked+' />';
                            html += '<label for="'+rowData.machine_id+'"></label>';
                            html += '</div>';
                            html += '</div>';
                            html += "&nbsp;<span id='"+rowData.machine_id+"_msg' class='status-span'>当前状态:"+status+"</span>";
                            return html;
                        }
                    }
	            ],
				toolBarMenu:[{"act":"save","click":loginWS,"icon":"fa-plus","text":"登录","token":"27"}],
				showLine:true
			});
		});
        function sendMsg(obj,rowData) {
            if (!xgs.WebSocket.isLogin()){
                layer.msg('请先进行登录!', {
                    icon : 5,
                    skin : 'layer-ext-moon',
                    time : 1500
                }, function(index) {
                    layer.close(index);
                })
				if (obj.is(":checked"))
                	obj.removeAttr("checked");
                else
                    obj.attr("checked","checked");
                return false;
            }
            obj =  $(obj);
            var cmd = parseInt(obj.is(':checked')?obj.attr("check-on"):obj.attr("check-off"),16);
            var msg = cmd===100?"开":"关";
            var id = obj.attr("id");
            $("#"+id+"_msg").text("当前状态:"+msg);
            xgs.WebSocket.send(rowData.device_id,cmd,24,rowData.system_id,obj);
        }

        loginWS();
        setTimeout(loginWS(),500);
		function loginWS() {
            if(!xgs.WebSocket.isLogin()&&!xgs.utils.isUndefined(xgs.WebSocket.socket())){
                xgs.WebSocket.login('${websocketLoginInfo}');
            }else if (xgs.WebSocket.isLogin()){
                $("#loginStatus").text("状态：登录成功");
            }else {
                $("#loginStatus").text("状态：登录失败，请点击右侧登录按钮手动登录");
            }
        }
		</script>
<style>

</style>