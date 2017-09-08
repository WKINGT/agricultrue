<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="../../include/lib.jsp" %>
 <script type="text/javascript" src="${ctxStatic}/assets/js/treegrid/jquery.treegrid.js"></script>
 <script type="text/javascript" src="${ctxStatic}/js/plugins/utils.js"></script>
 <script type="text/javascript" src="${ctxStatic}/js/other.js"></script>
 <script type="text/javascript" src="${ctxStatic}/js/plugins/role.js"></script>
 <div class="widget-body">
		<div class="widget-main">
			<div id="roles"></div>
		</div>
	</div>
 <script type="text/javascript">

 		function getPmid(arr,pid){
 			if($('tr[class=treegrid-parent-'+pid+']').length != 0){
 				
 			}
 		}
		var clomuns1 = [
		           {display:'菜单名称',field:'menu_name',render:function(d){
		        	   return '<span style="color:green;cursor:pointer;">'+d+'</span>';
		           }},
		           {
		        	   display:'菜单选择',field:null,render:function(data,c,obj){
		        		   var ctl = $(' <div class="controls" style="padding-left: 20px;"></div>');
		        		   var label = $('<label class="checkbox inline menu" style="padding:0 20px;margin-top:0px;margin-bottom:0px;"><input type="checkbox" class="show" fid="'+c.function_id+'" parentid="'+c.parent_id+'" menuid="'+c.id+'"/>选择菜单</label>');
		        		   $('input',label).on('click',function(){
		        		       var parentId = $(this).attr('parentid');
		        			   if(!$(this).prop('checked')){
                                   if (parentId==="-1"){
                                       var menu = $('.menu input[parentid='+$(this).attr("menuid")+']');
                                       menu.prop('checked',false);
                                       menu.each(function () {
                                           $('.function input[mid='+$(this).attr("menuid")+']').prop('checked',false);
                                       })

                                   }
		        				   $(this).closest('tr').find('input').prop('checked',false);
		        			   }else {
                                   if (parentId!=="-1"){
                                       $('.menu input[menuid='+parentId+']').prop('checked',true);
                                       $('.function input[mid='+$(this).attr("menuid")+'][parentid=-1]').prop('checked',true);
                                   }
                               }

		        		   }).prop('checked',c.checked);
		        		   ctl.append(label);
		        		   if (c.parent_id !== "-1"){
                               var selectAll = $('<label class="checkbox inline" style="padding:0 20px;margin-top:0px;margin-bottom:0px;"><input type="checkbox"  parentid="'+c.parent_id+'" menuid="'+c.id+'"/>全选</label>');
                               $('input',selectAll).on('click',function(){
                                   if($(this).prop('checked')){
                                       var parentid = $(this).attr("parentid");
                                       $('.menu input[menuid='+parentid+']').prop('checked',true);
                                       $(this).closest('tr').find('input').prop('checked',true);
                                   }else{
                                       $(this).closest('tr').find('input').prop('checked',false);
                                   }
                               });
                               ctl.append(selectAll);
                           }
		        		   return ctl;
		        	   }
		           },
		           {
		        	   display:'功能',field:null,render:function(data,c,obj){
	        			   var div = $('<div></div>');
		        		   if(!xgs.utils.isUndefined(c.smfuncs)){
		        			   var ctl = $('<div class="controls" style="padding-left: 20px;"></div>');
		        			   var  label =getFunction(c.smfuncs,c.id);
                               ctl.append(label);
                               if(!xgs.utils.isUndefined(c.smfuncs.sonData)&&c.smfuncs.sonData.length>0){
                                   for (var i = 0;i<c.smfuncs.sonData.length;i++ ){
                                       var  label1 =getFunction(c.smfuncs.sonData[i],c.id);
                                       ctl.append(label1);
                                   }
                               }

		        			   div.append(ctl);
		        		   }
		        		   return div;
		        	   }
		           }
			];
		var data1 = ${menu};
		function getFunction(data,menuId) {
                var label = $('<label class="checkbox inline function" style="padding:0 20px;margin-top:0px;margin-bottom:0px;"><input type="checkbox" funid="'+data.id+'" mid="'+menuId+'" parentid="'+data.parentId+'"/>'+data.name+'</label>');
                $('input',label).on('click',function(){
                    var parentId = $(this).attr('parentid');
                    var menu  = $('.menu input[menuid='+$(this).attr('mid')+']');
                    var menuParentId =  menu.attr("parentid");
                    if($(this).prop('checked')){
                        menu.prop('checked',true);
                        if (parentId!=="-1"){
                            $('.function input[funid='+parentId+']').prop('checked',true);
                        }
                        if(!xgs.utils.isUndefined(menuParentId)&&!menuParentId !== ""){
                            $('.menu input[menuid='+menuParentId+']').prop('checked',true);
                        }
                    }else {
                        if (parentId ==="-1"){
                            $('.function input[parentid='+$(this).attr('id')+']').prop('checked',false);
                        }
                    }
                }).prop('checked',data.checked);
                return label;

        }
        $(document).ready(function() {
            $("#roles").xgsTreeGrid({
            	rootId:-1,
        		id:'id',
        		pid:'parent_id',
        		clomuns : clomuns1,
        		data :data1,
        		showLine : true,
        		showCheckBox : false,
        		showReload:true,
        		url : null,
        		rightKey:null,
        		toolBarMenu:[{"icon":"fa-plus","text":"保存","token":"${token}","click":saveData,"act":"${act}"}]
            });
        });
		function saveData() {
		    debugger
            var mids = [];
            $('input.show').each(function(){
                var menu={mid:"",fids:[]};
                var fids = [];
                if(!$(this).prop('checked')) return;
                var mid = $(this).attr('menuid');
                var fid = $(this).attr('fid');
                if(fid) fids.push(fid);
                $('input[mid='+mid+']').each(function(){
                    if($(this).prop('checked')){
                        fids.push($(this).attr('funid'));
                    }
                });
                menu.mid = mid;
                menu.fids = xgs.utils.arrayUnique(fids);
                mids.push(menu);
            });
            var menu_code = JSON.stringify(mids);
            var url = xgs.utils.setToken(location.href,'${token}');
            $.ajax({
                url : url,
                type : "POST",
                data:{
                    roleId:'${roleId}',
                    menu:menu_code
                },
                //cache : false, // 禁用缓存
                dataType : "json",
                success : function(result) {
                    xgs.citem.tips(result);
                },
                error : function(XMLHttpRequest, textStatus, errorThrown) {
                }
            });
        }
		</script>