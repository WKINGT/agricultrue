;
(function($) {
	var returnObj = {}
	$.xgsDefaults = $.xgsDefaults || {};
	var _container;
	var _table;
	var setting = {};
	$.fn.xgsTreeGrid = function(options) {
		setting = $.extend({}, $.xgsDefaults.TreeGrid, options);
		_container = this;
		init(this);
		return returnObj;
	}
	$.xgsDefaults.TreeGrid = {
		rootId : '-1',
		rightKey:null,
		id:'id',
		pid:'pid',
		children:'children',
		title : '&nbsp;&nbsp;&nbsp;&nbsp;',
		clomuns : [],
		data : [],
		filter:[],
		render : null,
		showLine : true,
		showCheckBox : true,
		showReload:true,
		expandAll:false,
		collapseAll:false,
		toolBarMenu : [],
		url : null,
		refunc:null
	}
	function init(container) {
		builder(container);
		var _len = 0;
		if(setting.showLine){
			_len++;
		}
		if(setting.showCheckBox){
			_len++;
		}
		
		 $(document).ready(function() {
				$(_table).treegrid({
		        	initialState:'expanded',
		        	treeColumn:_len,
		            expanderExpandedClass: 'fa fa-minus-circle',
		            expanderCollapsedClass: 'fa fa-plus-circle'
		        });

	        });
		
		returnObj.reload=function(re){
			if(xgs.utils.isFunction(setting.refunc)){
				setting.refunc();
			}
		};
		returnObj.selectedRows=getSelected;
	}
	;
	function getSelected(){
		var arr = [];
		_table.find('>tbody > tr > td input[type=checkbox]').each(function(){
			if($(this).prop('checked')){
				arr.push({pkid:$(this).val()});
			}
		});
		return arr;
	}
	function addMenus(menu) {
		if(setting.expandAll){
			setting.toolBarMenu.push({
				text:'展开所有',icon:'fa fa-expand',click:function(){
					$(_table).treegrid('expandAll');
	        	 }
			});
		}
		if(setting.collapseAll){
			setting.toolBarMenu.push({
				text:'折叠所有',icon:'fa fa-compress',click:function(){
					$(_table).treegrid('collapseAll');
	        	 }
			});
		}
		if(setting.showReload){
			if (!xgs.utils.isArray(setting.toolBarMenu)){
				setting.toolBarMenu = [];
			}
			setting.toolBarMenu.push({
				text:'刷新',icon:'fa-spinner',click:function(){
					returnObj.reload();
	        	 }
			});
		}
		
		if (xgs.utils.isArray(setting.toolBarMenu)) {
			var _ = $('<div style="margin-right:20px;"></div>');
			menu.append(_);
			for (var i = 0; i < setting.toolBarMenu.length; i++) {
				var a = $('<a class="btn btn-primary btn-xs" style="margin-left:10px;border-width:1px;"><i class="fa '
						+ setting.toolBarMenu[i].icon
						+ '"></i>&nbsp;'
						+ setting.toolBarMenu[i].text + '</a>');
				_.append(a);
				if (xgs.utils.isFunction(setting.toolBarMenu[i].click)) {
					(function(d) {
						a.on("click", function() {
							return d.click(d);
						}).on('mousemove', function() {
							$(this).find("i").addClass("fa-spin")
						}).on('mouseout', function() {
							$(this).find("i").removeClass("fa-spin")
						});
					})(setting.toolBarMenu[i]);
				}
			}
		}
	}

	function builderBody(table){
		
		var tbody = $('<tbody></tbody>');
		setting.data = transformTo(setting.data);
		recursion(setting.data,tbody);
		table.append(tbody);
	}
	
	function builder(container) {
		var div = $('<div class="row"><div class="col-xs-12"><div class="row"><div class="col-xs-12"></div></div></div></div>');
		var menuBar = $('<div class="clearfix"></div><div class="table-header">'
				+ (xgs.utils.isNull(setting.title) ? '' : setting.title)
				+ '</div>');
		var _menu = $('<div class="pull-right tableTools-container"></div>');
		menuBar.append(_menu);
		addMenus(_menu);
		var _div = $('<div></div>');
		_table = $('<table class="table xgs-tree table-bordered table-striped table-condensed"></table>');
		_div.append(_table);
		div.append(menuBar);
		div.append(_div);
		builderHead(_table);
		builderBody(_table);
		container.html(div);
		
	}
	function builderHead(table) {

		var thead = $('<thead></thead>');
		var tr = $('<tr></tr>');
		thead.append(tr);
		table.append(thead);

		if (setting.showCheckBox) {
			var checkbox = $('<td class="center" style="width:10px;"><label class="pos-rel"><input type="checkbox" class="ace" /><span class="lbl"></span></label></td>');
			tr.append(checkbox);
		}

		if (setting.showLine) {
			var no = $('<td class="center">序号</td>');
			tr.append(no);
		}

		for (var i = 0, len = setting.clomuns.length; i < len; i++) {
			var th = $('<td></td>').text(setting.clomuns[i].display);
			if (!xgs.utils.isUndefined(setting.clomuns[i].width)
					&& setting.clomuns[i].width) {
				th.css({
					width : setting.clomuns[i].width
				})
			}
			tr.append(th);
		}
	}
	
	var _data_length=0;
	
	function recursion(data,container){
		for(var i=0,len=data.length;i<len;i++){
			_data_length++;
			var tr = $('<tr></tr>').attr('id',data[i][setting.id]).addClass('treegrid-'+data[i][setting.id]);
			if(xgs.utils.isFunction(setting.rightKey)){
				(function(d){
					$(tr).on('mousedown',function(e){
						if(3 == e.which){// 鼠标右键
							//document.oncontextmenu = new Function("return false;")// 屏蔽浏览器自带右键功能
				            return setting.rightKey(d,e);
				        }
						if(1 == e.which){ // 鼠标左键
							$(".rightButDiv").css("display","none");
						}
					});
				})(data[i]);
			}
			
			if(data[i][setting.pid] != setting.rootId){
				tr.addClass("treegrid-parent-"+data[i][setting.pid]);
			}
			if(setting.showCheckBox){
				var td = $('<td class="center"><label class="pos-rel"><input type="checkbox" class="ace" id="_checkIds" value="'+data[i][setting.id]+'"><span class="lbl"></span></label></td>');
				tr.append(td);
			}

			if (setting.showLine) {
				var no = $('<td class="center">'+_data_length+'</td>');
				tr.append(no);
			}
			for(var k=0,ken=setting.clomuns.length;k<ken;k++){
				var td = $('<td></td>').addClass("hidden-xs");
				var text = data[i][setting.clomuns[k]['field']];
				if(xgs.utils.isFunction(setting.clomuns[k]['render'])){
					text = setting.clomuns[k]['render'](text,data[i]);
				}
				td.html(text);
				tr.append(td);
			}
			container.append(tr);
			var isSub = (typeof data[i][setting.children] !== 'undefined')?true:false;
			if(isSub){
				recursion(data[i][setting.children],container);
			}
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
	
})(jQuery);