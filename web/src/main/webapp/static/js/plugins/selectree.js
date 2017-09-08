;(function($,win){
	function tree(element,opts){
		this.ele = element;
		this.deflautOpts = {
			pid:'pid',
			id:'id',
			text:'text',
			sid:'',
			pre:'-|',
			children:'children',
			value:'',
			data:[],
			select2:false,
			tab:4,
			callback:null
		}
		this.opts = $.extend({},this.deflautOpts,opts);
		this.init();
	}
	tree.prototype = {
		init:function(){
			this.container = $('<select class="form-control col-xs-12 col-sm-12"></select>').attr({
				id:this.opts['sid'],
				name:this.opts['sid']
			});
			if(typeof this.opts['callback'] == 'function'){
				var that = this;
				this.container.on('change',function(){
					that.opts['callback'](this);
				})
			}
			this.ele.append(this.container);
			var data = this.transformTo(this.opts.data);
			this.recursion(data,0);
			if(this.opts.select2){
				this.container.select2({
					minimumResultsForSearch: -1,
					width: "100%",
				}); 
			}
		},
		transformTo:function(sNodes){
			var i,l,
			key = this.opts.id,
			parentKey = this.opts.pid,
			childKey = this.opts.children;
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
		},
		deeps:function(deep){
			if(deep == 0) return "";
			var sp = "&nbsp;";
			var temp = "";
			for(var i=0;i<deep*this.opts.tab;i++){
				temp +=sp;
			}
			return temp+this.opts.pre;
		},
		recursion:function(data,deep){
			for(var i=0,len=data.length;i<len;i++){
				var text = this.deeps(deep)+data[i][this.opts.text];
				var option = $('<option></option>').attr("id",data[i][this.opts.id]).attr('value',data[i][this.opts.id]).html(text);
				if(this.opts.value == data[i][this.opts.id]){
					option.prop('selected',true);
				}
				this.container.append(option);
				var isSub = (typeof data[i][this.opts.children] !== 'undefined')?true:false;
				if(isSub){
					this.recursion(data[i][this.opts.children],deep+1);
				}
			} 
		}
	}
	$.fn.selectree = function(options){
		var totree = new tree(this,options);
	}
})(jQuery,window);