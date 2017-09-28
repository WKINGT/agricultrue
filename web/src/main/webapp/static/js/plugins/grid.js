;(function($) {
	var returnObj = {}
	$.xgsDefaults = $.xgsDefaults || {};
	var _container;
	var setting = {};
	$.fn.xgsGrid = function(options) {
		setting = $.extend({}, $.xgsDefaults.grid, options);
		_container = this;
		init(this);
		return returnObj;
	}
	$.xgsDefaults.grid = {
		pkid:'id',
		title : '',
		clomuns : [],
        initComplete:null,
		data : [],
		filter:[],
		render : null,
		showLine : true,
		showCheckBox : true,
		showReload:true,
		sort : true,
		toolBarMenu : [],
		url : null
	}
	function init(container) {
		builder(container);
		var myTable = $("#dynamic-table")
		// .wrap("<div class='dataTables_borderWrap' />") //if you are applying
		// horizontal scrolling (sScrollX)
		.DataTable({
			ordering : true,// 排序操作在服务端进行，所以可以关了。
			pageLength : 10,// 首次加载的数据条数
			serverSide : true,// 分页，取数据等等的都放到服务端去
			processing : true,// 载入数据的时候是否显示“载入中”
			data : setting.data,
			"initComplete":function callback() {
				if (setting.initComplete!==null){
                    setting.initComplete();
				}
            },
			aoColumns : setting.aoColumns,
			aoColumnDefs : [ {
				sDefaultContent : '',
				aTargets : [ '_all' ],
			} ],
			language : {
				'emptyTable' : '没有数据',
				'loadingRecords' : '加载中...',
				'processing' : '查询中...',
				'search' : '检索:',
				'lengthMenu' : '每页 _MENU_ 条数据',
				'zeroRecords' : '没有数据',
				'paginate' : {
					'first' : '第一页',
					'last' : '最后一页',
					'next' : '下一页',
					'previous' : '上一页'
				},
				'info' : '第 _PAGE_ 页 / 总 _PAGES_ 页  共 _TOTAL_ 项。',
				'infoEmpty' : '没有数据',
				'infoFiltered' : '(过滤总件数 _MAX_ 条)'
			},
			autoWidth : false,
			bStateSave : false,
			bSort : setting.sort,
			order : [],
            filterMenus:[],
			select : {
				style : 'multi'
			},
			ajax : function(data, callback, settings) {// ajax配置为function,手动调用异步查询
				// 手动控制遮罩
				// $wrapper.spinModal();
				// 封装请求参数
				var param = queryFilter(data);
				xgs.utils.mask.show();
				$.ajax({
					type : "POST",
					url : setting.url,
					cache : false, // 禁用缓存
					data : param, // 传入已封装的参数
					dataType : "json",
					success : function(result) {
						// setTimeout仅为测试延迟效果
						setTimeout(function() {
							// 异常判断与处理
							if (result.errorCode) {
								$.dialog.alert("查询失败。错误码：" + result.errorCode);
								return;
							}

							// 封装返回数据，这里仅演示了修改属性名
							var returnData = {};
							returnData.draw = data.draw;// 这里直接自行返回了draw计数器,应该由后台返回
							returnData.recordsTotal = result.totalRow;
							returnData.recordsFiltered = result.totalRow;// 后台不实现过滤功能，每次查询均视作全部结果
							returnData.data = result.list;
							// 调用DataTables提供的callback方法，代表数据已封装完成并传回DataTables进行渲染
							// 此时的数据需确保正确无误，异常判断应在执行此回调前自行处理完毕
							callback(returnData);
							xgs.utils.mask.hide();
						}, 200);
					},
					error : function(XMLHttpRequest, textStatus, errorThrown) {

					}
				});
			}
		});
		if (setting.showCheckBox){
            myTable.on('select', function(e, dt, type, index) {
                if (type === 'row') {
                    $(myTable.row(index).node()).find('input:checkbox').prop(
                        'checked', true);
                }
            });
            myTable.on('deselect', function(e, dt, type, index) {
                if (type === 'row') {
                    $(myTable.row(index).node()).find('input:checkbox').prop(
                        'checked', false);
                }
            });
		}
		// ///////////////////////////////
		// table checkboxes
		$('th input[type=checkbox], td input[type=checkbox]').prop('checked',
				false);

		// select/deselect all rows according to table header checkbox
		$(
				'#dynamic-table > thead > tr > th input[type=checkbox], #dynamic-table_wrapper input[type=checkbox]')
				.eq(0).on('click', function() {
					var th_checked = this.checked;// checkbox inside "TH"
													// table header

					$('#dynamic-table').find('tbody > tr').each(function() {
						var row = this;
						if (th_checked)
							myTable.row(row).select();
						else
							myTable.row(row).deselect();
					});
				});

		// select/deselect a row when the checkbox is checked/unchecked
        if (setting.showCheckBox){
            $('#dynamic-table').on('click', 'td input[type=checkbox]', function() {
                var row = $(this).closest('tr').get(0);
                if (!this.checked)
                    myTable.row(row).deselect();
                else
                    myTable.row(row).select();
            });
        }

		$(document).on('click', '#dynamic-table .dropdown-toggle', function(e) {
			e.stopImmediatePropagation();
			e.stopPropagation();
			e.preventDefault();
		});
		returnObj.dt = myTable;
		returnObj.selectedRows = function() {
			var arrItemId = [];
			$("tbody :checkbox:checked", "#dynamic-table").each(function(i) {
				var item = myTable.row($(this).closest('tr')).data();
				item.pkid = item[setting.pkid];
				arrItemId.push(item);
			});
			return arrItemId;
		}
		returnObj.reload=function(re){
			if(xgs.utils.isBoolean(re)){
				myTable.draw(re);
			}else{
				myTable.draw(false);
			}
		}
	}
	;

	function addMenus(menu) {
		if(setting.showReload){//刷新按钮
			if (!xgs.utils.isArray(setting.toolBarMenu)){
				setting.toolBarMenu = [];
			}
			setting.toolBarMenu.push({
				text:'刷新',icon:'fa-spinner',click:function(){
					returnObj.reload();
	        	 },
			});
		}
		if(xgs.utils.isArray(setting.filter) && setting.filter.length != 0 ){
			setting.toolBarMenu.unshift({
				text:'搜索',icon:'fa-filter',click:function(){
					search();
				}
			});
		}
		if (xgs.utils.isArray(setting.toolBarMenu)) {
			var _ = $('<div style="margin-right:20px;"></div>');
			menu.append(_);
            toolBarMenu:for (var i = 0; i < setting.toolBarMenu.length; i++) {
				if (!xgs.utils.isUndefined(setting.filterMenus)){
                    for(var j = 0;j<setting.filterMenus.length;j++){
                        if (setting.filterMenus[j]===setting.toolBarMenu[i].token){
                            continue toolBarMenu;
                        }
                    }
				}
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
							$(this).find("i").addClass("fa-spin");
						}).on('mouseout', function() {
							$(this).find("i").removeClass("fa-spin");
						});
					})(setting.toolBarMenu[i]);
				}
			}
		}
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
		var table = $('<table id="dynamic-table" class="table table-striped table-bordered table-hover"></table>');
		_div.append(table);
		div.append(menuBar);
		div.append(_div);
		builderHead(table);
		table.append('<tbody></tbody>');
		container.html(div);
	}
	function builderHead(table) {

		var aoColumns = [];
		var thead = $('<thead></thead>');
		var tr = $('<tr></tr>');
		thead.append(tr);
		table.append(thead);

		if (setting.showCheckBox) {
			var checkbox = $('<th class="center" style="width:10px;"><label class="pos-rel"><input type="checkbox" class="ace" /><span class="lbl"></span></label></th>');
			tr.append(checkbox);
			aoColumns
					.push({
						bSortable : false,
						bVisible : true,
						mData : null,
						aTargets : [ 0 ],
						sWidth : '10px',
						sClass : 'center',
						mRender : function(data, type, row, meta) {
							return '<label class="pos-rel"><input type="checkbox" class="ace" id="_checkIds"/><span class="lbl"></span></label>';
						}
					});
		}

		if (setting.showLine) {
			var no = $('<th class="center">序号</th>');
			tr.append(no);
			aoColumns.push({
				bSortable : false,
				bVisible : true,
				mData : null,
				aTargets : [ 1 ],
				sWidth : '45px',
				sClass : 'center',
				mRender : function(data, type, row, meta) {
					return meta.row + 1;
				}
			});
		}

		for (var i = 0, len = setting.clomuns.length; i < len; i++) {
			var _length = aoColumns.length - 1;
			var _temp = {
				bVisible : true,
				mData : setting.clomuns[i].data,
				sWidth : setting.clomuns[i].width,
				aTargets : [ _length ]
			}
			_temp.bSortable = false;
			if (!xgs.utils.isUndefined(setting.clomuns[i].sorting)) {
				_temp.bSortable = setting.clomuns[i].sorting;
			}
			if (!xgs.utils.isUndefined(setting.clomuns[i].render)
					&& xgs.utils.isFunction(setting.clomuns[i].render)) {
				_temp.mRender = setting.clomuns[i].render;
			}
			aoColumns.push(_temp);
			var th = $('<th></th>').text(setting.clomuns[i].title);
			if (!xgs.utils.isUndefined(setting.clomuns[i].width)
					&& setting.clomuns[i].width) {
				th.css({
					width : setting.clomuns[i].width
				})
			}
			tr.append(th);
		}
		setting.aoColumns = aoColumns;
	}
	var queryFilter = function(data) {
		var i=0;
		if(setting.showCheckBox){
			i++;
		}
		if(setting.showLine){
			i++;
		}
		var param = {};
		if(!xgs.utils.isUndefined(filter) && !xgs.utils.isNull(filter) ){
			param.where = JSON.stringify(filter.getData());
		}
		var _order = {};
		if(data.order.length!=0){
			for(var k=0,len=data.order.length;k<len;k++){
				_order[setting.clomuns[data.order[k].column-i].data] = data.order[k].dir;
			}
		}
		param.order = JSON.stringify(_order);
		param.pageNo = Math.ceil(data.start / data.length) + 1;
		param.pageSize = data.length;
		return param;
	}
	var filter = null;
	function search(){
		 if(filter ==null){
    		 filter = $(".clearfix",_container).xgsFilter({
						fields:setting.filter
				});
		 }

 		 layer.open({
 		   type: 1, //page层
 		   area: '800px',
 		   title: '搜索',
 		   shade: 0.6, //遮罩透明度
 		   moveType: 1, //拖拽风格，0是默认，1是传统拖动
 		   shift: 5, //0-6的动画形式，-1不开启,
 		   zIndex:1,
 		   btn: ['确定'],
 		   yes: function(index, layero){
 			  layer.close(index);
 			  returnObj.reload(true);
 		   },
 		   content: filter.filter
 		 });
	}
})(jQuery);