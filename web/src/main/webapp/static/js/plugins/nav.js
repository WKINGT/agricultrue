;(function($){
	$.xgsDefaults = $.xgsDefaults || {};
	var _container;
	var setting = {};
	$.fn.xgsNav = function(options){
		setting = $.extend({},$.xgsDefaults.nav,options);
		_container= this;
		init(this);
	}
	$.xgsDefaults.nav = {
		simple:true,
		click:null,
		id:'id',
		pid:'pid',
		children:'children',
		icon:'icon',
		text:'name',
		token:'token',
		url:null,
		data:[]
	}

	function init(container){
		if(setting.url != null){
			$.ajax({
				type: "post",
            	url: setting.url,
            	dataType: "json",
            	success: function(data){
                	setting.data = data;
                	//TODO 调用
					bulider(container);
             	}
			});
		}else{
			bulider(container);
		}
	}
	function transformTo(sNodes){
		var i,l,
			key = setting.id,
			parentKey = setting.pid,
			childKey = setting.children;
			if (!key || key=="" || !sNodes) return [];

			if (xgs.utils.isArray(sNodes)) {
				var r = [];
				var tmpMap = [];
				for (i=0, l=sNodes.length; i<l; i++) {
					tmpMap[sNodes[i][key]] = sNodes[i];
				}
				for (i=0, l=sNodes.length; i<l; i++) {
					if (tmpMap[sNodes[i][parentKey]] && sNodes[i][key] != sNodes[i][parentKey]) {
						if (!tmpMap[sNodes[i][parentKey]][childKey])
							tmpMap[sNodes[i][parentKey]][childKey] = [];
						tmpMap[sNodes[i][parentKey]][childKey].push(sNodes[i]);
					} else {
						r.push(sNodes[i]);
					}
				}
				return r;
			}else {
				return [sNodes];
			}
	}
	function bulider(container){
		if(setting.simple){
			setting.data = transformTo(setting.data);
		}
		container.append(recursion(setting.data,container));
	}
	function recursion(data,container){
		for(var i=0,len=data.length;i<len;i++){
			var li = $('<li></li>').attr("id",data[i].id).css({cursor:'pointer'});
			container.append(li);
			var a = $('<a></a>');
			if(typeof data[i][setting.token] !== 'undefined' && $.trim(data[i][setting.token]) != ''){
				if(!xgs.utils.isNull(setting.click) && xgs.utils.isFunction(setting.click)){
					//a.trigger('click',[setting.click,data[i]]);
					(function(d){
						a.on("click",function(){
							_container.find("li").each(function(){
								$(this).removeClass("active");
							})
							$(this).closest("li").addClass("active");
							return setting.click(d); 
						})
					})(data[i]);
				}
				a.attr('data-url',data[i][setting.token]);
			}
			a.attr('herf','javascript:;').addClass('dropdown-toggle');
			if(xgs.utils.isUndefined(data[i][setting.icon]) ){
				data[i][setting.icon] = 'fa-caret-right';
			}
			a.append($('<i class="menu-icon fa"></i>').addClass(data[i][setting.icon]));
			a.append($('<span class="menu-text"></span>').text(data[i][setting.text]));

			var isSub = (typeof data[i][setting.children] !== 'undefined')?true:false;
			if(isSub){
				a.append($('<b class="arrow fa fa-angle-down"></b>'));
			}
			li.append(a);
			li.append($('<b class="arrow"></b>'));
			if(isSub){
				var _sub = $('<ul class="submenu"></ul>');
				li.append(_sub);
				recursion(data[i][setting.children],_sub);
			}
		} 
	}
})(jQuery)