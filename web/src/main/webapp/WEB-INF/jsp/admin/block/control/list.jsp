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
				title:'<span style="font-size: 13px;" >设备列表  <span style="margin-left: 35%" id="loginStatus"></span></span>',
				clomuns:[
				   {
		                data: 'machineName',
		                title:'设备名'
		            },
                    {
                        data: 'systemId',
                        title:'系统id'
                    },
                    {
                        data: 'deviceId',
                        title:'设备id'
                    },
                    {
                        data: 'id',
                        title:'操作',
						render:function (data,type,rowData) {
							console.log(data);
                            console.log(type);
                            console.log(rowData);

                        }
                    }
	            ],
				toolBarMenu:[{"act":"save","click":"","icon":"fa-plus","text":"登录","token":"27"}],
				showLine:true
			});
		});
		if(xgs.utils.isUndefined(xgs.WebSocket.socket)){
            xgs.WebSocket.init("${url}","${userId}");
		}
		if(!xgs.utils.isLogin){
            xgs.WebSocket.login('${websocketLoginInfo}');
		}

		</script>