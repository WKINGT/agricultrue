<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="../../include/lib.jsp" %>
 <script type="text/javascript" src="${ctxStatic}/assets/js/treegrid/jquery.treegrid.js"></script>
 <script type="text/javascript" src="${ctxStatic}/js/plugins/utils.js"></script>
 <script type="text/javascript" src="${ctxStatic}/js/other.js"></script>
 <script type="text/javascript" src="${ctxStatic}/js/plugins/treegrid.js"></script>
 <div class="widget-body">
			<div id="roles"></div>
	</div>
 <script type="text/javascript">
     var url = xgs.utils.setToken(location.href,'${token}');
     var grid;
		var clomuns1 = [
		       {display:'机构名',field:'name'},
               {display:'创建时间',field:'createTime'}
			];
        $(document).ready(function() {
            grid = $("#roles").xgsTreeGrid({
                title:'<span style="font-size: 13px;">机构列表</span>',
            	rootId:"${rootId}",
        		id:'id',
        		pid:'parentId',
        		clomuns : clomuns1,
        		showLine : true,
        		data:${data},
        		showCheckBox : true,
        		showReload:true,
        		url : null,
        		rightKey:null,
        		toolBarMenu:${functions}
            });
        });
		</script>