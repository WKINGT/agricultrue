(function() {
	window.BMap_loadScriptTime = (new Date).getTime();
	var agent="http://115.28.175.88:8080/bAgent/baidu/getscript";//代理服务器地址
	//以下为实际地址
	var sourceUrl = "http://api.map.baidu.com/api?v=2.0&ak=4f3f5948d7d58fd9db47285fd336e419";//代理
	var url = agent + "?cache=true&agent=" + sourceUrl;
	console.log(url);
	document.write('<script type="text/javascript" src="' + url + '"></script>');
})();
