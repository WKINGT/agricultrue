<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="../../../include/lib.jsp" %>
		<script src="${ctxStatic}/assets/js/date-time/bootstrap-datepicker.js"></script>
		<script src="${ctxStatic}/assets/js/date-time/moment.js"></script>
		<script src="${ctxStatic}/assets/js/date-time/daterangepicker.js"></script>
		<script src="${ctxStatic}/assets/js/date-time/bootstrap-datetimepicker.js"></script>
		<script type="text/javascript" src="${ctxStatic}/js/layer/layer.js"></script>
		<script type="text/javascript" src="${ctxStatic}/js/plugins/grid.js"></script>
		<script type="text/javascript" src="${ctxStatic}/js/plugins/filter.js"></script>
		<script type="text/javascript" src="${ctxStatic}/js/other.js"></script>
		<script type="text/javascript">
		var grid
        var url = xgs.utils.setToken(location.href,'${token}');
		jQuery(function($) {
			grid = $(".page-content").xgsGrid({
				//[{"model":"sys_menu","param":null,"ctl":"sys.menu","type":"","id":"c4ca4238a0b923820dcc509a6f75849b","setting":"","operation":null,"is_enabled":false,"updated":"","created":"","description":null,"name":"菜单管理","is_del":false,"act":"list"},{"model":"sys_functions","param":null,"ctl":"sys.functions","type":"","id":"c81e728d9d4c2f636f067f89cc14862c","setting":"","operation":null,"is_enabled":false,"updated":"","created":"","description":null,"name":"功能管理","is_del":false,"act":"list"}]
				filter:[{'display':'区块名','name':'name','type':'text'},
                    	{'display':'时间','name':'createTime','type':'date'}],
				url:url,
				title:'<span style="font-size: 13px;">数据列表</span>',
				clomuns:[
				   {
		                data: 'blockName',
		                title:'区块名'
		            },
                    {
                        data: 'blockCrops',
                        title:'农作物'
                    },
                    {
                        data: 'blockThroughput',
                        title:'预产量'
                    },
                    {
                        data: 'blockGrowthStage',
                        title:'生长阶段'
                    },
                    {
                        data: 'technician',
                        title:'农技员'
                    },
                    {
                        data: 'technicianMobile',
                        title:'联系方式'
                    },
		            {
		                data: 'createTime',
		                title:'创建时间'
		            }
	            ],
				toolBarMenu:${functions},
				showLine:true
			});
		});


		</script>