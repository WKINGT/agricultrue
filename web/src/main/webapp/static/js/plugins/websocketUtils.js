(function(win){
	var obj = new Object();
	var isLogin = false;
	var sessions = new Array();
	var message = {userId:"",uuid:"",msg:{},cmd:"",sysId:""};
	var session = {sessionId:"",cmd:"",element:null};
	var heartbeatInterval = null;
	var varUserId = "";
	var body = null;
	var socket;
    obj.isLogin = function getIsLogin() {
		return  isLogin;
    }
    obj.socket = function getSocket() {
		return socket;
    };
	obj.init = function(url,userId,_body){
		body = _body;
		varUserId = userId;
		if (!window.WebSocket) {
			window.WebSocket = window.MozWebSocket;
		}
		if (window.WebSocket) {
			socket = new WebSocket(url);
			socket.onmessage = function(event) {
                callBack(event.data);
			};
			socket.onopen = function(event) {
				console.log("open")
			};
			socket.onclose = function(event) {
				console.log("close")
			};
		} else {
			alert("你的浏览器不支持 WebSocket！");
		}
	}
	obj.clearHeartbeat=function clearHeartbeat() {
        window.clearInterval(heartbeatInterval);
    }
	function heartbeat(){
        if (!isLogin){
            return;
        }
        heartbeatInterval = setInterval(function () {
            var uuid = getUUID(32,9);
            send(uuid,"",50,"");
        },45000)

	}

	obj.login = function login(data){
		if (isLogin) { return;}
		var uuid = getUUID(32,9);
		sessions.push(uuid);
        if (socket.readyState == WebSocket.OPEN) {
           send(uuid,data,40,"");
        } else {
            $("#loginStatus").text("状态：登录失败，请点击右侧登录按钮手动登录");
        }

	}
	obj.send = function outSend(objId,operation,cmd,sysId,element) {
        var uuid = getUUID(32,9);
        var msg = {"objId":objId,"operation":operation}
        send(uuid,msg,cmd,sysId,element)
    }

	function send(uuid,msg,cmd,sysId,element) {
		saveSession(uuid,cmd,element);
		message.uuid = uuid;
		message.msg = msg;
		message.userId = varUserId;
		message.cmd = cmd;
		message.sysId = sysId;
		if (!window.WebSocket) {
			return;
		}
		if (socket.readyState == WebSocket.OPEN) {
			socket.send(JSON.stringify(message));
		} else {
			obj.isLogin = false;
            $("#loginStatus").text("状态：登录失败，请点击右侧登录按钮手动登录");
            layer.msg(data.msg || '链接失败，请刷新重连！', {
                icon : 5,
                skin : 'layer-ext-moon',
                time : 1500
            }, function(index) {
                layer.close(index);
            })
		}
	}
	function saveSession(uuid,cmd,element){
		session.sessionId = uuid;
		session.cmd = cmd;
		session.element = element;
		sessions.push(session);
	}
	function getUUID(len, radix) {
		 var chars = '0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz'.split('');
		 var uuid = [], i;
		 radix = radix || chars.length;
		 if (len) {
		   // Compact form
		   for (i = 0; i < len; i++) uuid[i] = chars[0 | Math.random()*radix];
		 } else {
		   // rfc4122, version 4 form
		   var r;
		
		   // rfc4122 requires these characters
		   uuid[8] = uuid[13] = uuid[18] = uuid[23] = '-';
		   uuid[14] = '4';
		
		   // Fill in random data.  At i==19 set the high bits of clock sequence as
		   // per rfc4122, sec. 4.1.5
		   for (i = 0; i < 36; i++) {
		     if (!uuid[i]) {
		       r = 0 | Math.random()*16;
		       uuid[i] = chars[(i == 19) ? (r & 0x3) | 0x8 : r];
		     }
		   }
		 }
  		return uuid.join('');
	}
	function callBack(data){
		data = JSON.parse(data);
		var msg = JSON.parse(data.msg);
		if (data.cmd==="40"){
			if (msg.code===0){
				isLogin = true;
				$("#loginStatus").text("状态：登录成功");
                heartbeat();
			}else {
                $("#loginStatus").text("状态：登录失败，请点击右侧登录按钮手动登录");
			}
		}
		if (data.cmd==="24"){
			var session = findSessionByUUID(data.uuid);
			if (xgs.utils.isUndefined(session)){//主动传输信息
                body.find("input[device_id]").each(function (e) {
					var that = $(this);
					if (that.attr("device_id")===msg.objId){
                        setStatus(that.attr("id"),msg.operationResult);
					}
                })
                return;
			}
            if (msg.code===0){
                layer.msg( '操作'+session.element.attr("machine-name")+"成功", {
                    icon : 1,
                    skin : 'layer-ext-moon',
                    time : 1500
                }, function(index) {
                    layer.close(index);
                })
				var id = session.element.attr("id");
                setStatus(id,msg.operationResult);
            }else {
                layer.msg('操作'+session.element.attr("machine-name")+"失败", {
                    icon : 5,
                    skin : 'layer-ext-moon',
                    time : 1500
                }, function(index) {
                    layer.close(index);
                })
            }
		}
	}
	function setStatus(id,cmd) {
		debugger;
        var msg_ = cmd===100?"开":"关";
        var checked_ = cmd===100?"checked":"";
        $("#"+id+"_msg").text("当前状态:"+msg_);
        if (checked_===""){
            $("#"+id).prop('checked',false);
        }else {
            $("#"+id).prop('checked',true);
		}
    }
	function findSessionByUUID(uuid) {
		for (var i= 0;i<sessions.length;i++){
			if (sessions[i].sessionId===uuid){
				var sess = sessions[i];
                removeSession(i);
				return sess;
			}
		}
    }
    function removeSession(j) {
		var sessions_new = new Array();
        for (var i= 0;i<sessions.length;i++){
        	if (i!==j){
                sessions_new.push(sessions[i]);
			}
        }
        sessions = sessions_new;
    }
	win.xgs = win['xgs'] || {};
    win.xgs.WebSocket = obj;
})(window)
