;(function(win,$){
	var _search_marker;
	var constants = {
		icon:"http://118.180.8.80/ioop-bcs-web/res/skin/default/css/img/map_mark.png"
	};
	var icon = {w:21,h:21,l:0,t:0,x:6,lb:5}
	var xmap = {
		marker:{},//当前标记点
		cicle :{},//当前标记圈
		markers:[],//所有标记点
		cicles :[],//所有标记圈
		label :{},//当前所标记的显示label
		labels:[], //所有标记显示的label
		ac:{}//自动 完成
	};
	
	

	win.xgs = win['xgs'] || {};
	win.xgs.bmap = xmap;
	
	xmap.init = function(_point){
		this.map=new BMap.Map("xmap",{enableMapClick: false});//enableMapClick: false
		//new BMap.Map('map', {enableMapClick: false})关闭点击事件
		var p = _point.split(",");
		var lat = parseFloat(p[0]);
		var lng = parseFloat(p[1]);
		var point = new BMap.Point(lng,lat);  
		this.map.centerAndZoom(point,15);// 初始化地图,设置中心点坐标和地图级别
		this.map.enableScrollWheelZoom();
		this.map.enableDragging();//启用地图拖拽事件，默认启用(可不写)
		this.map.enableScrollWheelZoom();//启用地图滚轮放大缩小
		this.map.enableDoubleClickZoom();//启用鼠标双击放大，默认启用(可不写) 
		this.map.enableKeyboard();//启用键盘上下左右键移动地图
		this.map.addControl(new BMap.NavigationControl({anchor:BMAP_ANCHOR_TOP_LEFT,type:BMAP_NAVIGATION_CONTROL_LARGE}));    
		this.map.addControl(new BMap.ScaleControl({anchor:BMAP_ANCHOR_BOTTOM_LEFT}));  
		this.map.addControl(new BMap.OverviewMapControl({anchor:BMAP_ANCHOR_BOTTOM_RIGHT,isOpen:1}));    
		this.map.addControl(new BMap.MapTypeControl());
		var that = this;
		this.map.addEventListener("rightclick", function(e){
			that.closeAdd();
        }); 
	};
	xmap.openAdd = function(){
		if(this.markers.length != 0)return;
		this.closeAdd();//防止重复点击
		this.map.addEventListener("mousemove", this.move); 
		this.map.addEventListener("click", this.adapter); 
	}
	xmap.closeAdd = function(){
		$("#markImg").hide();
		this.map.removeEventListener("mousemove", this.move); 
		this.map.removeEventListener("click", this.adapter); 
	}
	xmap.move = function(e){
		$("#markImg").css({"top":(e.pixel.y-22),"left":(e.pixel.x-10)}).show();
	}
	xmap.createIcon = function(){
		return new BMap.Icon(constants.icon,new BMap.Size(icon.w,icon.h));
	};
	xmap.adapter = function(e){
		this.addMarker(e.point,false);
	}
	
	xmap.remove_overlay = function(){
		this.map.clearOverlays(); 
		this.markers = [];
		$(".shops").hide();
	}
	xmap.setMarkerLabel = function(obj,index){
		var _marker = this.markers[index];
		_marker.getLabel().setContent($(obj).find("#configName").text());
	};
	xmap.setVal = function(p){
		var myGeo = new BMap.Geocoder();
		var address = "仪器地址";
		var addressComponents = {city:"",county:"",province:""};
		// 根据坐标得到地址描述
		myGeo.getLocation(p, function(result){
             if (result){
             	address=result.address;
                addressComponents.city = result.addressComponents.city;
                addressComponents.county = result.addressComponents.district;
                addressComponents.province = result.addressComponents.province;
	        	//var shopAddress = $("#shop_map_address input",window.parent.document);
//	        	if($.trim($(shopAddress).val())==''){
	        		//$(shopAddress).val(address);
//	        	}
              }
              debugger;
        	$(".shops-shop p").html(address+"<br/>经纬度："+p.lng+","+p.lat);
           	$("#lng_lat",window.parent.document).val(p.lng+","+p.lat);
           	$("#city",window.parent.document).val(addressComponents.city);
           	$("#county",window.parent.document).val(addressComponents.county);
           	$("#province",window.parent.document).val(addressComponents.province);
           	$("#address",window.parent.document).val(address);
		});
		//$("#shop_map_id input",window.parent.document).val(p.lat+","+p.lng);
	}
	xmap.addMarker = function(p,isNew,obj){
		this.setVal(p);
		var point = new BMap.Point(p.lng,p.lat); 
		var iconImg = this.createIcon();  
		var marker = new BMap.Marker(point,{icon:iconImg});
		var length = this.markers.length;
		var str;
		
		var attr = p.lng+"|"+p.lat;
		var content = $('<input type="button" value="删除" class="delete"/>').on('click',function(){
			xgs.bmap.remove_overlay();
		});
		
		this.markers.push(marker);
		this.map.addOverlay(marker);
	    marker.setAnimation(BMAP_ANIMATION_BOUNCE); //跳动的动画
    	marker.enableDragging();//可拖拽
        $(".shops").show();
		if(_search_marker){
			this.map.removeOverlay(_search_marker);
			_search_marker = undefined;
		}
		//$("#attend_marks").append($("<form id=\"form_"+length+"\"><dl id=\"marker_"+length+"\" point=\""+attr+"\" index=\""+length+"\">"+$("#attend_edit").html()+"</dl></form>"));

		if(isNew){//如果为初始化来的，则赋值
			
		}
		//dragend
		var that = this;
		marker.addEventListener("dragend",function(e){
			that.setVal(e.point);
		});
		if(!isNew){
			var markerObj = $("#marker_"+length);
			markerObj.find("#indexId").text("#marker_"+length);
			markerObj.find("#orgLat").text(p.lat);
			markerObj.find("#orgLng").text(p.lng);
			editMarkDetail($("#marker_"+length),isNew);//定位到编辑页面
			this.closeAdd();//关闭事件
		}
	};
	xmap.initMarkers = function(json){
			var point = new BMap.Point(json.orgLng,json.orgLat); 
			this.addMarker(point,true,null);
	};
	xmap.addEvent = function(marker){
		marker.addEventListener("click",function(){
                this.openInfoWindow($("#infoWin").html());
            });
	}
	xmap.setInfo = function(obj,index,isNew){
		if(!isNew) return;
		//TODO 对infoWin进行文本编辑
		$("#infoWin").html($("<div class=\"form-main attend-item-d\">"+$(obj).find(".attend-item-d").html()+"</div>"));
		var _marker = this.markers[index];
		this.map.setCenter(_marker.getPosition());
		_marker.openInfoWindow(new BMap.InfoWindow($("#infoWin").html()));
	}
	xmap.posotion = function(obj,index,isNew){
		this.setInfo(obj,index,isNew);
	};
	xmap.save = function(obj,index){
		//对圈进行和标签进行重写
		//TODO ajax操作本地数据库
	};
	xmap.getPosition = function(index){
		return this.map.setCenter(this.markers[index].getPosition());
	};
	xmap.remove = function(obj,index){
		this.map.removeOverlay(this.markers[index]);
		this.map.removeOverlay(this.cicles[index]);
		$("#marker_"+index).remove();
		//TODO ajax操作本地数据库
	};
	xmap.Autocomplete = function(suggestId){
		var that = this;
		this.ac =  new BMap.Autocomplete({//建立一个自动完成的对象
			"input" : suggestId,
			"location" : that.map
		});
		var myValue;
		this.ac.addEventListener("onconfirm", function(e) {//鼠标点击下拉列表后的事件
			var _value = e.item.value;
			myValue = _value.province +  _value.city +  _value.district +  _value.street +  _value.business;
			var local = new BMap.LocalSearch(that.map, { //智能搜索
			  onSearchComplete: function(){
				  if(_search_marker){
					  that.map.removeOverlay(_search_marker);
					  _search_marker = undefined;
				  } 
				  var point = local.getResults().getPoi(0).point;    //获取第一个智能搜索的结果
				  that.markers[0].setPosition(point);
				  that.setVal(point);
				  that.map.centerAndZoom(point, 15);
				  $("#"+suggestId).val("");
			  }
			});
			local.search(myValue);
		});
	};
})(window,jQuery);
