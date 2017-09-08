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
				filter:[{'display':'区块名','name':'name','type':'text'}],
				url:url,
				title:'<span style="font-size: 13px;">区块列表</span>',
				clomuns:[
				   {
		                data: 'blockName',
		                title:'区块名'
		            },
		            {
		                data: 'address',
		                title:'区块地址'
		            },
                    {
                        data: 'blockAcreage',
                        title:'区块面积(亩)',
                        render:function (value) {
                            return value+"亩";
                        }
                    },
                    {
                        data: 'historyData',
                        title:'农作物',
						render:function (value) {
							return value.split("|")[0];
                        }
                    },
                    {
                        data: 'historyData',
                        title:'农技员',
                        render:function (value) {
                            return value.split("|")[1];
                        }
                    },
                    {
                        data: 'historyData',
                        title:'农技员联系方式',
                        render:function (value) {
                            return value.split("|")[2];
                        }
                    },
		            {
		                data: 'createTime',
		                title:'创建时间'
		            },
                    {
                        data: 'id',
                        title:'操作',
                        render:function (id,display,data) {
                            var url  = xgs.utils.setToken(location.href,'44');
                            return "<a href='"+url+"&blockId="+id+"' onclick='Popup(this);return false;'>生成二维码</a>";
                        }
                    }
	            ],
				toolBarMenu:${functions},
				showLine:true
			});
		});
        function Popup(obj) {
            var href = $(obj).attr("href");
            //询问框
            layer.confirm('<img src="'+href+'" alt="二维码" style="margin:-20px 6px">', {
                btn: ['确认']
            });
        }
		</script>