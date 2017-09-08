<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="../../include/lib.jsp" %>
		<link rel="stylesheet" type="text/css" href="${ctxStatic}/js/tree/zTreeStyle/zTreeStyle.css">
		<script src="${ctxStatic}/assets/js/date-time/bootstrap-datepicker.js"></script>
		<script src="${ctxStatic}/assets/js/date-time/moment.js"></script>
		<script src="${ctxStatic}/assets/js/date-time/daterangepicker.js"></script>
		<script src="${ctxStatic}/assets/js/date-time/bootstrap-datetimepicker.js"></script>
		<script type="text/javascript" src="${ctxStatic}/js/tree/jquery.ztree.all-3.5.min.js"></script>
		<script type="text/javascript" src="${ctxStatic}/js/layer/layer.js"></script>
		<script type="text/javascript" src="${ctxStatic}/js/plugins/grid.js"></script>
		<script type="text/javascript" src="${ctxStatic}/js/plugins/filter.js"></script>
		<script type="text/javascript" src="${ctxStatic}/js/other.js"></script>


		<script type="text/javascript">
		var grid;
        var url = xgs.utils.setToken(location.href,'${token}');
		jQuery(function($) {
			grid = $(".page-content").xgsGrid({
				//[{"model":"sys_menu","param":null,"ctl":"sys.menu","type":"","id":"c4ca4238a0b923820dcc509a6f75849b","setting":"","operation":null,"is_enabled":false,"updated":"","created":"","description":null,"name":"菜单管理","is_del":false,"act":"list"},{"model":"sys_functions","param":null,"ctl":"sys.functions","type":"","id":"c81e728d9d4c2f636f067f89cc14862c","setting":"","operation":null,"is_enabled":false,"updated":"","created":"","description":null,"name":"功能管理","is_del":false,"act":"list"}]
				filter:[{'display':'主控系统标识','name':'system_id','type':'text'},
                    	{'display':'设备编号','name':'terminal_id','type':'text'},
                    	{'display':'设备名','name':'name','type':'text'}],
				url:url,
				title:'<span style="font-size: 13px;">关联列表</span>',
				clomuns:[
                    {
                        data: 'systemId',
                        title:'主控系统标识'
                    },
					{
                        data: 'terminalId',
                        title:'设备编号'
                    },
                    {
                        data: 'name',
                        title:'设备名'
                    },
				   	{
		                data: 'createTime',
		                title:'创建时间'
		            },
                    {
                        data: 'id',
                        title:'操作',
						render:function (data) {
							return "<a href='javascript:void(0)' onclick='openTree(\""+data+"\")'>子级</a>";
                        }
                    }
	            ],
				toolBarMenu:[],
				showLine:true
			});
		});

		function openTree(id) {
		    var html = "<div class='zTreeObj'> <ul id='treeDemo' class='ztree'></ul> </div>";
            var setting = {
                view: {
                    dblClickExpand: false
                },
                open:true,
                data: {
                    simpleData: {
                        enable: true,
                        idKey: "id",
                        pIdKey: "parentId",
                        rootPId: -1
                    }
                }
            };
            var data=[];
            $.ajax({
				url:xgs.utils.setToken(location.href,'39'),
				async:false,
				data:{id:id},
				success:function (result) {
                    result = JSON.parse(result);
                    if(!result.code&&result.data.length>0){
                        data = result.data;
                        layer.open({
                            title: '子级设备',
                            shade: 0.6, //遮罩透明度
                            moveType: 1, //拖拽风格，0是默认，1是传统拖动
                            shift: 0, //0-6的动画形式，-1不开启
                            area: ['300px', '500px'],
                            content: html
                        });
                    }else{
                        layer.alert(result.msg, {
                            icon: 5,
                            skin: 'layer-ext-moon'
                        },function(index){
                            layer.close(index);
                        })
                    }
                }
			})
            var ztree =$.fn.zTree.init($("#treeDemo"), setting, data);
            ztree.expandAll(true);

        }
		</script>