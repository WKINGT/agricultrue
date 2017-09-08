;(function ($, win, document, undefined) {
    var DragLoad = function (ele, opt) {
        this.element = ele;
        this.defaults = {
        	scrollDom:win,
			fromField: "from",
			currentPageField: "currentPage",
			currentPage : 1,
			pageCountField: "pageCount",
            pageCount : 10,
            //配置加载提示dom
            loadingDom:$('.pageLoading'),
            //延迟显示，即加载提示显示时间
            delayTime:500,
            isActive:true,
			callbackFun :{
				render:null,
				callbackFun:null,
				formatData:null,
				noMoreHandle:null
			}
        };
        this.opts = $.extend({}, this.defaults, opt);
        this.init();
    }

    DragLoad.prototype = {
    	unbinded:function(){
    		$(this.opts.scrollDom).off('scroll');
    	},
        /*
         *绑定滚动事件
         */
        init: function () {
			//第一次起始数据条数
			this.opts.from = this.opts.pageCount;
            var dragThis = this;
           
            
            $(this.opts.scrollDom).scroll(function () {
            	if(!dragThis.opts.isActive) return;
                //已经滚动到上面的页面高度
                var scrollTop = $(dragThis.opts.scrollDom).scrollTop()-49;
                //页面高度
                var scrollHeight = $(dragThis.element).height();
                //浏览器窗口高度
                var windowHeight = $(document).height();
                //此处是滚动条到底部时候触发的事件，在这里写要加载的数据，或者是拉动滚动条的操作
                var bot = 50; //bot是底部距离的高度
                if (scrollTop + windowHeight >= scrollHeight) {
                	dragThis.opts.isActive=false;
                    dragThis.insertDom();
                }
            });
        },
		//判断是否是回调函数
		toolsIsFun: function(fun){
			var flag = false;
			if( typeof fun === 'function' ){
				flag = true;
			}
			return flag;
		},
        /*
         *获取下一页的数据
         */
        getData: function () {
            var dragThis = this;
			var data = {};
			data[dragThis.opts.fromField] = dragThis.opts.from;
			data[dragThis.opts.currentPageField] = dragThis.opts.currentPage;
			data[dragThis.opts.pageCountField] = dragThis.opts.pageCount;
            $.ajax({
                type: 'POST',
                url: dragThis.opts.url,
                data: data,
                dataType: 'json',
                timeout: 5000,
                beforeSend: function(){
                    //显示加载提示
                    dragThis.opts.loadingDom.css("visibility","visible");
                },
                success: function (data) {
                    //console.info(data);
					//数据回调处理，返回纯数据
					if( typeof dragThis.opts.callbackFun.formatData === 'function' ){
						data = dragThis.opts.callbackFun.formatData(data);
					}
					
                    if(data != null && data.list.length !=0){
                        var t = setTimeout(function(){
                        		dragThis.render(data);
                        	},dragThis.opts.delayTime);
                    }else{
						if(dragThis.toolsIsFun(dragThis.opts.callbackFun.noMoreHandle)){
							dragThis.opts.callbackFun.noMoreHandle();
						}else{
							dragThis.opts.loadingDom.html("没有更多");
						}
                    }
                },
                error: function (xhr, type) {
                    console.error(type);
                }
            })
        },
        render: function (data) {
            //用户自定义的化调用自用的方法
        	var dom = '';
            if( typeof this.opts.callbackFun.render === 'function' ){
                dom = this.opts.callbackFun.render(data,this.opts['xgsId'],this.opts['currentPage']);
            }
            this.opts.isActive = true;
            //隐藏加载提示
            this.opts.loadingDom.css("visibility","hidden");
            this.element.append(dom);
            this.handlePage();
        },
        insertDom: function () {
            var data = this.getData();
        },
		handlePage : function(){
			this.opts.currentPage = this.opts.currentPage + 1;
			this.opts.from = this.opts.from + this.opts.pageCount;
		},
		getCurrentPage : function(){
			return this.currentPage;
		},
        returnOjb: function () {
            return this.element;
        }
    }

    //在插件中使用DragLoad对象
    $.fn.dragLoad = function (options) {
        //创建Beautifier的实体
        var dragLoad = new DragLoad(this, options);
        //调用其方法
        return dragLoad;
    }

})(jQuery, window, document);