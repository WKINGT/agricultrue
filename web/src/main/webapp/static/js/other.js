window.xgs = window['xgs']||{};
xgs.citem = {
	func:function(obj){
		var ids = grid.selectedRows();
		if(ids.length == 0){
			layer.alert('请选择数据', {
				  icon: 5,
				  skin: 'layer-ext-moon'
			});
			return;
		}
		var _ids = [];
		for(var i=0,len=ids.length;i<len;i++){
			_ids.push(ids[i].id)
		}
        var url = xgs.utils.setToken(location.href,obj.token);
		$.ajax({
			url : url,
			type : "POST",
			data:{ids:_ids.join(",")},
			//cache : false, // 禁用缓存
			dataType : "json",
			success : function(result) {
				xgs.citem.tips(result);
			},
			error : function(XMLHttpRequest, textStatus, errorThrown) {
			}
		});
	}
}
xgs.citem.tips = function(result){
	if(!result.code){
		layer.alert(result.msg||'操作成功', {
			  icon: 1,
			  skin: 'layer-ext-moon'
		},function(index){
			if(grid){
				grid.reload();
			}
			layer.close(index);
		})
	}else{
		layer.alert(result.msg||'操作失败', {
			  icon: 5,
			  skin: 'layer-ext-moon'
		},function(index){
			layer.close(index);
		})
	}
}
function itemclick(obj){
    var url = xgs.utils.setToken(location.href,obj.token);
    if(obj.act == 'save'){
		$.ajax({
			url : url,
			type : "GET",
			//cache : false, // 禁用缓存
			dataType : "html",
			success : function(result) {
				$(".page-content").html(result);
			},
			error : function(XMLHttpRequest, textStatus, errorThrown) {
			}
		});
		return;
	}
	if(obj.act == 'update'){
		var ids = grid.selectedRows();
		if(ids.length != 1){
			layer.alert('请选择一条数据', {
				  icon: 5,
				  skin: 'layer-ext-moon'
			});
			return;
		}
		var param ="";
        if (undefined===obj.start){
            param = {id:ids[0].id}
        }else{
            param = {id:ids[0].id,start:obj.start()}
		}
		$.ajax({
			url : url,
			type : "GET",
			data:param,
			//cache : false, // 禁用缓存
			dataType : "html",
			success : function(result) {
				$(".page-content").html(result);
			},
			error : function(XMLHttpRequest, textStatus, errorThrown) {
			}
		});
		return;
	}
	if(obj.act == 'delete'){
		xgs.citem.func(obj);
		return;
	}
	if(obj.act == 'updown'){
		xgs.citem.func(obj);
		return;
	}
	if(obj.act == 'close'){
		xgs.citem.close(obj);
		return;
	}
	if(obj.act == 'open'){
		xgs.citem.open(obj);
		return;
	}
	if(obj.act == 'recommend'){
		xgs.citem.open(obj);
		return;
	}
	if(obj.act == 'enabled'){
		xgs.citem.open(obj);
		return;
	}
	if(obj.act == 'pass'){
		xgs.citem.open(obj);
		return;
	}
	if(obj.act == 'nopass'){
		xgs.citem.close(obj);
		return;
	}
	if(obj.act == 'changeOrder'){
		xgs.citem.open(obj);
		return;
	}
	if(obj.act == 'statistics'){
		xgs.citem.open(obj);
		return;
	}
    if(obj.act == 'export'){
        xgs.citem.export(obj);
        return;
    }
	if(obj.act == 'auth'){
        var ids = grid.selectedRows();
        if(ids.length != 1){
            layer.alert('请选择一条数据', {
                icon: 5,
                skin: 'layer-ext-moon'
            });
            return;
        }
        var param ="";
        if (undefined===obj.start){
            param = {id:ids[0].id}
        }else{
            param = {id:ids[0].id,start:obj.start()}
        }
        $.ajax({
            url : url,
            type : "GET",
            data:param,
            //cache : false, // 禁用缓存
            dataType : "html",
            success : function(result) {
                $(".page-content").html(result);
            },
            error : function(XMLHttpRequest, textStatus, errorThrown) {
            }
        });
        return;
	}
}
xgs.citem.export = function (data) {
/*	debugger;
    var f=document.createElement("form");
    f.setAttribute("action" , data.token );
    f.setAttribute("method" , 'post' );
    f.setAttribute("target" , '_new' );
    data.document.appendChild(f);
    f.submit();*/
debugger;
	data.function.getGrid().searchExport();
}

xgs.citem.open = function(data){
	var ids = grid.selectedRows();
	if(ids.length == 0){
		layer.alert('请选择数据', {
			  icon: 5,
			  skin: 'layer-ext-moon'
		});
		return;
	}
	var _ids = [];
	for(var i=0,len=ids.length;i<len;i++){
		_ids.push(ids[i].pkid)
	}
	xgs_ajax_json(data.token,{ids:_ids.join(",")});
}
xgs.citem.close = function(data){
	var ids = grid.selectedRows();
	if(ids.length != 1){
		layer.alert('请选择一条数据', {
			  icon: 5,
			  skin: 'layer-ext-moon'
		});
		return;
	}
	layer.open({
		   type: 1, //page层
		   area: '400px',
		   title: '关闭',
		   shade: 0.6, //遮罩透明度
		   moveType: 1, //拖拽风格，0是默认，1是传统拖动
		   shift: 5, //0-6的动画形式，-1不开启,
		   zIndex:1,
		   btn: ['确定'],
		   yes: function(index, layero){
			   var d = {id:ids[0].pkid};
			   d.close_reason = $(layero).find('textarea').val();
			  xgs_ajax_json(data.token,d);
			  layer.close(index);
		   },
		   content: '<div style="text-align:center"><div>理由：</div><div><textarea style="width:90%"></textarea></div></div>'
		 });
}

function xgs_ajax_json(token,data){
	xgs.utils.mask.show();
	$.ajax({
		url : '/admin?token='+token,
		type : "POST",
		//cache : false, // 禁用缓存
		data:data,
		dataType : "json",
		success : function(result) {
			xgs.utils.mask.hide();
			xgs.citem.tips(result);
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
		}
	});
}

function xgs_ajax(token){
	xgs.utils.mask.show();
	$.ajax({
		url : '/admin?token='+token,
		type : "GET",
		//cache : false, // 禁用缓存
		dataType : "html",
		success : function(result) {
			xgs.utils.mask.hide();
			$(".page-content").html(result);
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
		}
	});
}