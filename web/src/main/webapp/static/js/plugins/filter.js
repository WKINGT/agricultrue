;(function($,win){
	var setting = {};
	var formDiv;
	$.fn.xgsFilter = function(options){
		if(xgs.utils.isUndefined(options)) optioins = {};
		setting = $.extend({},$.xgsFilter.Filter,options)
		/**
		 * data = {
				fields:[{'display':'用户 名','name':'useraccount','type':'text'},
				        {'display':'创建时间','name':'createTime','type':'date'},
				        {'display':'更新时间','name':'updateTime','type':'datetime'},
				        {'display':'类型','name':'type','type':'select',options:[{name:'启用',value:1},{name:'禁用',value:2}]}
				       ]
			}
		 */
		formDiv = $('<div class="form-group" id="validation-form-group-filter"></div>');
		formDiv.append(buildBase());
		this.html(formDiv);
		var that = this;
		return {
			getData:getData,
			filter:that
		}
	}
	
	
	function getData(){
		//field: fieldName, op: op, value: value, type: type
		var _data = [];
		formDiv.find("> .form-group").each(function(){
			var fieldName = $(this).find(".self-field select").val();
			var op = $(this).find(".self-op select").val();
			for(var i=0,len=setting.fields.length;i<len;i++){
				if(fieldName == setting.fields[i].name){
					if(setting.fields[i].type == 'select'){
						value = $(this).find(".self-val select").val();
					}else{
						value = $(this).find(".self-val input").val();
					}
					break;
				}
			}
			if($.trim(value)!=''){
				_data.push({
					field:fieldName,op:op,value:value
				})
			}
		});
		return {rules:_data};
	}
	
	function addRule(){
		formDiv.append(buildBase());
	}
	
	function init(op,val){
		if(!xgs.utils.isUndefined(setting.fields[0])){
			$(".self-op",op).html(buildOperation(setting.fields[0]));
			$(".self-val",val).html('');
			$.xgsFilter.Filter.editors[setting.fields[0].type].create(setting.fields[0],$(".self-val",val))
		}
	}
	
	function buildFields(){
		var editor = $('<select class="form-control col-xs-12 col-sm-12"></select>');
		for(var i=0,len=setting.fields.length;i<len;i++){
			editor.append('<option value="'+setting.fields[i].name+'">'+setting.fields[i].display+'</option>');
		}
		editor.on('change',function(){
			changeField(this);
		});
		return editor;
	}
	
	function createEditor(){
		
	}
	
	function changeField(obj){
		var _id = $(obj).val();
		for(var i=0,len=setting.fields.length;i<len;i++){
			if(_id == setting.fields[i].name){
				$(obj).closest(".form-group").find(".self-op").html(buildOperation(setting.fields[i]));
				var _c = $(obj).closest(".form-group").find(".self-val")
				_c.html('');
				$.xgsFilter.Filter.editors[setting.fields[i].type].create(setting.fields[i],_c)
				//TODO 改变状态
				break;
			}
		}
	}
	
	function buildOperation(field){//.type
		var types = $.xgsFilter.Filter.operators[field.type];
		var editor = $('<select class="form-control col-xs-12 col-sm-12"></select>');
		editor.attr({
			id:field.name,
			name:field.name
		})
		for(var i=0,len=types.length;i<len;i++){
			editor.append('<option value="'+types[i]+'">'+$.xgsFilter.FilterString.strings[types[i]]+'</option>');
		}
		return editor;
	}
	
	function buildBase(){
		var formGrop = $('<div class="form-group" style="margin:0px;"><div class="col-lg-12"><div class="form-group has-feedback"></div></div></div>');
		var field = $('<div class="col-xs-12 col-sm-3"><div class="clearfix self-field"></div></div>');
		var selector = $('<div class="col-xs-12 col-sm-3"><div class="clearfix self-op"></div></div>');
		var value = $('<div class="col-xs-12 col-sm-4"><div class="clearfix  self-val"></div></div>');
		var op = $('<div class="col-xs-12 col-sm-2"><div class="clearfix"></div></div>');
		var plus = $('<div></div>').css({height:34,'margin-top':8,'font-size':16,'float':'left','padding-right':20,color:'#87b87f'});
		var minus =  $('<div></div>').css({height:34,'margin-top':8,'font-size':16,'cursor':'pointer',color:'#d15b47'});
		var _plus = $('<i class="fa fa-plus"></i>').css('cursor','pointer');
		var _minus = $('<i class="fa fa-minus"></i>').css('cursor','pointer');
		plus.append(_plus);
		minus.append(_minus);
		_plus.on('click',function(){
			addRule();
		});
		_minus.on('click',function(){
			if(formDiv.find("> .form-group").length ==1){
				return;
			}
			$(this).closest(".form-group").remove();
		})
		op.append(plus);
		op.append(minus);
		$('.clearfix',field).html(buildFields());
		init(selector,value);
		formGrop.append(field);
		formGrop.append(selector);
		formGrop.append(value);
		formGrop.append(op);
		return formGrop;
	}
	
	$.xgsFilter = {};
	$.xgsFilter.Filter = {
	        //字段列表
	        fields: [],
	        //字段类型 - 运算符 的对应关系
	        operators: {},
	        //自定义输入框(如下拉框、日期)
	        editors: {}
	    };
	$.xgsFilter.FilterString={
		strings: {
	        "and": "并且",
	        "or": "或者",
	        "equal": "相等",
	        "notequal": "不相等",
	        "startwith": "以..开始",
	        "endwith": "以..结束",
	        "like": "相似",
	        "greater": "大于",
	        "greaterorequal": "大于或等于",
	        "less": "小于",
	        "lessorequal": "小于或等于",
	        "addrule": "增加条件",
	    }
	}
	    $.xgsFilter.Filter.operators['select'] =
	    ["equal", "notequal"];

	$.xgsFilter.Filter.operators['string'] =
    $.xgsFilter.Filter.operators['text'] =
    ["equal", "notequal", "startwith", "endwith", "like", "greater", "greaterorequal", "less", "lessorequal"];

    $.xgsFilter.Filter.operators['number'] =
    $.xgsFilter.Filter.operators['int'] =
    $.xgsFilter.Filter.operators['float'] =
        ["equal", "notequal", "greater", "greaterorequal", "less", "lessorequal"];
    $.xgsFilter.Filter.operators['date'] =
    $.xgsFilter.Filter.operators['time'] =
    $.xgsFilter.Filter.operators['datetime'] =
        ["greater", "greaterorequal", "less", "lessorequal","startwith"];
    $.xgsFilter.Filter.editors = {
    	time:{
			create:function(entity,container){
				var cls = 'time-picker';
				var _time = $('<div class="input-group bootstrap-timepicker"></div>');
				var editor = $('<input type="text" class="form-control '+cls+'" />');
				editor.attr({
					id: entity.name,
					name: entity.name
				});
				var _icon = $('<span class="input-group-addon"><i class="fa fa-clock-o bigger-110"></i></span>');
				_time.append(editor).append(_icon);
				container.append(_time);

				editor.timepicker({
					minuteStep: 1,
					showSeconds: true,
					showMeridian: false,
					disableFocus: true
				}).on('focus', function() {
					$(this).timepicker('showWidget');
				}).next().on(ace.click_event, function(){
					$(this).prev().focus();
				});

				var e = {"editor":editor,"control":this,"name":entity.name};
				return e;
			},
			setValue:function(editor,value){
				editor.val(value);
			},
			getValue:function(editor){
				return editor.val();
			}

		},
		date:{
			create:function(entity,container){
				var cls = 'date-picker';
				var format = 'yyyy-mm-dd';
				if(typeof entity.format !== 'undefined'){
					format = entity.format;
				}
				var _date = $('<div class="input-group"></div>');
				var editor = $('<input class="form-control date-picker" type="text" data-date-format="'+format+'" />');
				editor.attr({
					id: entity.name,
					name: entity.name
				});
				var _icon = $('<span class="input-group-addon"><i class="fa fa-calendar bigger-110"></i></span>');
				_date.append(editor).append(_icon);
				container.append(_date);
				editor.datepicker({
					autoclose: true,
					todayHighlight: true
				}).next().on(ace.click_event, function(){
					$(this).prev().focus();
				});
				var e = {"editor":editor,"control":this,"name":entity.name};
				return e;
			},
			setValue:function(editor,value){
				editor.val(value);
			},
			getValue:function(editor){
				return editor.val();
			}

		},
		datetime:{
			create:function(entity,container){
				var cls = 'date-timepicker';
				var format = 'YYYY-MM-DD HH:mm:ss';
				if(typeof entity.format !== 'undefined'){
					format = entity.format;
				}
				var _datetime = $('<div class="input-group"></div>');
				var editor = $('<input type="text" class="form-control '+cls+'" data-date-format="'+format+'"/>');
				editor.attr({
					id: entity.name,
					name: entity.name
				});
				var _icon = $('<span class="input-group-addon"><i class="fa fa-clock-o bigger-110"></i></span>');
				_datetime.append(editor).append(_icon);
				container.append(_datetime);

				editor.datetimepicker({
				 icons: {
					time: 'fa fa-clock-o',
					date: 'fa fa-calendar',
					up: 'fa fa-chevron-up',
					down: 'fa fa-chevron-down',
					previous: 'fa fa-chevron-left',
					next: 'fa fa-chevron-right',
					today: 'fa fa-arrows ',
					clear: 'fa fa-trash',
					close: 'fa fa-times'
				 }
				}).next().on(ace.click_event, function(){
					$(this).prev().focus();
				});


				var e = {"editor":editor,"control":this,"name":entity.name};
				return e;
			},
			setValue:function(editor,value){
				editor.val(value);
			},
			getValue:function(editor){
				return editor.val();
			}

		},
		text:{
			create:function(entity,container){
				var editor = $('<input type="'+entity.type+'" class="col-xs-12 col-sm-12" />');
				editor.attr({
					id: entity.name,
					name: entity.name
				});
				if(typeof entity.readonly != 'undefined' && entity.readonly){
					editor.attr("readonly", true);
				}
				container.append(editor);
				var e = {"editor":editor,"control":this,"name":entity.name};
				return e;
			},
			setValue:function(editor,value){
				editor.val(value);
			},
			getValue:function(editor){
				return editor.val();
			}
		},
		select:{
			create:function(entity,container){
				var editor = $('<select class="form-control col-xs-12 col-sm-12"></select>');
				editor.attr({
					id: entity.name,
					name: entity.name
				});
				editor.append('<option value=""></option>');
				for(var i=0,len=entity.options.length;i<len;i++){
					editor.append('<option value="'+entity.options[i].value+'">'+entity.options[i].name+'</option>');
				}
				container.append(editor);
				var e = {"editor":editor,"control":this,"name":entity.name};
				return e;
			},
			setValue:function(editor,value){
				editor.val(value);
			},
			getValue:function(editor){
				return editor.val();
			}
		}
    }

    $.xgsFilter.Filter.editors.number= 
    $.xgsFilter.Filter.editors.int=
    $.xgsFilter.Filter.editors.float=
    $.xgsFilter.Filter.editors.string=$.xgsFilter.Filter.editors.text;
})(jQuery,window)