;(function(win,$){
	var util = {};
	
	util.removeSp = function(url){
		return url.replace(/[\?|&]sp.*?(&?)/,'$1');
	}
	/***
	 * judge a string is blank or not blank
	 * @param str
	 * @returns {Boolean}
	 */
	util.isNotBlank = function(str){
		if(typeof str === 'undefined') return false;
		if(str === null) return false;
		if($.trim(str) === '') return false;
		return true;
	}
	util.isBlank = function(str){
		return !util.isNotBlank(str);
	}
	/**
	 * 
	 */
	util.request = {
			queryString:'',
			params:{},
			getParam:function(key){
				if(util.isBlank(this.queryString)){
					this.parse();
				}
				if(util.isBlank(key)){
					return this.params;
				}
				return this.params[key];
			},
			parse:function(url){
				url = url || location.href;
				this.queryString = url.substring(url.indexOf("?")+1,url.length);
		  		var params = this.queryString.split("&");
		  		for(var i=0,len=params.length;i<len;i++){
		  			var signle = params[i].split("=");
		  			if(signle.length == 1){
		  				this.params[signle[0]] = null;
		  			}else{
		  				this.params[signle[0]] = signle[1];
		  			}
		  		}
		  		return this;
			}
	};
	
	util.ajax = function(url,data,succ,dataType,method){
		dataType = dataType || 'json';
		$.ajax({
  			url:url,
  			type:method,
  			data:data,
  			dataType:dataType,
  			success:function(data){
  				succ(data);
  			},
  			error:function(){
  				//throws 
  			}
  		});
	}
	
	util.get = function(url,succ,dataType){
		this.ajax(url,{},succ,dataType,'get');
	}
	util.des=function(message, key){
        var keyHex = CryptoJS.enc.Utf8.parse(key);
        var encrypted = CryptoJS.DES.encrypt(message, keyHex, {
            mode: CryptoJS.mode.ECB,
            padding: CryptoJS.pad.Pkcs7
        });
        return encrypted.toString();
    }
	util.post = function(url,data,succ,dataType){
		this.ajax(url,data,succ,dataType,'post');
	}
	util.format = {
			date:function(date){
				return this._date(date);
			},
			datetime:function(dt){
				return this._date(dt)+" "+this._time(dt);
			},
			_time:function(dateobj){
		        var Hours = dateobj.getHours();
		        if (Hours < 10)
		            Hours = ('0' + Hours);
		        var Minutes = dateobj.getMinutes();
		        if (Minutes < 10)
		            Minutes = '0' + Minutes;
		        var Seconds = dateobj.getSeconds();
		        if (Seconds < 10)
		            Seconds = '0' + Seconds;
		        return Hours + ":" + Minutes + ":"+ Seconds;
			},
			_date:function(dateobj){
		        var year = dateobj.getFullYear();
		        var month = (dateobj.getMonth() + 1);
		        var day = dateobj.getDate();
		        if (month < 10)
		        	month = '0' + month;
		        if (day < 10)
		        	day = '0' + day;
		        return year + "-" + month + "-"+ day;
			}
	}
	
	util.save = function(id,isparent){
		var url = $("#"+id).attr('act');
		url = util.removeSp(url);
   		var form = liger.get(id);
        var manager = $.ligerDialog.waitting('正在保存中,请稍候...');
        var _data = form.getData();
        for(var i = 0,len=data.form.fields.length;i<len;i++){
        	var _fd = data.form.fields[i];
        	if(_fd.type == 'date'){
        		if(Object.prototype.toString.call(_data[_fd.name]) === "[object Date]"){//如果为日期、日期时间，则进行本地化
        			if(_fd.options && _fd.options.format === 'yyyy-MM-dd hh:mm:ss'){
            			_data[_fd.name] = util.format.datetime(_data[_fd.name]);
        			}else{
        				_data[_fd.name] = util.format.date(_data[_fd.name]);
        			}
                }
        	}
        }
        this.post(url,_data,function(data){
                if(!data.code){
                    manager.close();
                    var success = $.ligerDialog.success('保存成功');
                    setTimeout(function ()
                      {
                    	if(typeof isparent === 'undefined'){
                    		location.reload();
                    	}else{
                    		parent.location.reload();
                    	}
                      }, 1500);
                }else{
                	manager.close();
                    $.ligerDialog.error('保存失败');
                }
        });
	}
	
	util.tree = function(id,data,idName,parentIdName,checkbox,isExpand,onContextmenu){
		checkbox = checkbox || false;
		isExpand = isExpand || true;
		$("#"+id).ligerTree({
             data: data,
             idFieldName :idName,
             parentIDFieldName :parentIdName,
             checkbox:checkbox,
             isExpand:isExpand,
             onContextmenu : onContextmenu
       }); 
	}
	
	util.grid = function(id,data,pageSize,toolbar,onContextmenu,url){
		$("#"+id).ligerGrid({
			columns: data.columns,
			data: $.extend(true,{},data.data),
			pageSize:pageSize,
			toolbar:toolbar,
			width:'100%',
			height:'100%',
			checkbox:true,
			//record:data.data.Total,
			//dataAction: 'local',
			//url:url,
			rownumbers:true,
			onContextmenu:onContextmenu,
		});
	}
	
	win.util = util;
})(window,jQuery);