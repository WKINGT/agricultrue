<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>

 <script type="text/javascript" src="assets/js/treegrid/jquery.treegrid.js"></script>
 <script type="text/javascript" src="js/plugins/treegrid.js"></script>
 <script type="text/javascript">
		
		clomuns1 = [
		           {display:'菜单名称',field:'menu_name',render:function(d){
		        	   return '<span style="color:green;cursor:pointer;">'+d+'</span>';
		           }},
				{display:'图标', field:'icon'}
			];
		data1 = ${tree};
        $(document).ready(function() {
            $(".page-content").xgsTreeGrid({
            	rootId:-1,
        		id:'id',
        		pid:'parent_id',
        		clomuns : clomuns1,
        		data : data1,
        		showLine : true,
        		showCheckBox : true,
        		showReload:true,
        		url : null,
        		rightKey:null
            });
        });
		</script>