//折线图echarts封装。
(function(win){
    var obj = new Object();//输出对象
    obj.intervals = [];
    var builtIn = new Object();//内置对象
    obj.load = function load(elements,paramName="param") {
        var echartss = [];
        for (var i = 0; i < elements.length; i++) {
            var obj = elements[i];
            //组装数据结构param：请求参数，obj：百度画图对象，element：html对应元素，echartsType：类型（目前只支持折线图）
            var data = {param:obj.getAttribute(paramName),obj:echarts.init(obj),element:obj,echartsType:obj.getAttribute("echartsType")};
            echartss.push(data);
        }
        drugs(echartss);
    }
    function drugs(echartss) {
        for (var i = 0; i < echartss.length; i++) {
            var ech = echartss[i];
            var type = ech.echartsType;
            var option = builtIn.line(ech);
            obj.intervals[i] = setIntervals(option,ech);
        }
    }
    function setIntervals(option,ech) {
        return setInterval(function (){
            var axisData = (new Date()).toLocaleTimeString().replace(/^\D*/,'');
            if (option.series[0].name === "") return;
            var resultData = [];
            $.ajax({
                url:location.origin+"/echartData",
                data:{"ids":ech.param},
                async:false,
                success:function (result) {
                    resultData = JSON.parse(result);
                }
            });
            var ids = ech.param.split(",");
           /* for (var i = 0;i<option.series.length;){
                var data0 = option.series[i].data;
                if(data0.length>10){
                    data0.shift();
                }
                var data = resultData[i].data.split(",");
                if (resultData[i].machineId===ids[i]){
                    if (data.length>1){
                        data0.push(parseInt("0x"+data[0]));
                        for (var k = 1;k<data.length;k++){
                            i++;
                            data0 = option.series[i].data;
                            data0.push(parseInt("0x"+data[k]));
                        }
                        i++;
                    }else {
                        data0.push(parseInt("0x"+data[0]));
                        i++;
                    }
                }
            }*/
           var j = 0;
           for (var i = 0;i<ids.length;i++,j++){
               var data0 = option.series[j].data;
               if(data0.length>10){
                   data0.shift();
               }
               var data = getResultData(ids[i],resultData);
               if (data.length>1){
                   data0.push(scalingNumber(parseInt("0x"+data[0])));
                   for (var k = 1;k<data.length;k++){
                       data0 = option.series[++j].data;
                       data0.push(scalingNumber(parseInt("0x"+data[k])));
                   }
               }else {
                   data0.push(scalingNumber(parseInt("0x"+data[0])));
               }
            }
            option.xAxis[0].data.shift();
            option.xAxis[0].data.push(axisData);
            ech.obj.setOption(option);
        }, 2100);
    }

    function getResultData(id,resultData) {
        for (var j = 0;j<resultData.length;j++){
          if (resultData[j].machineId===id) return resultData[j].jointData.split(",");
        }
        return "";
    }
    function scalingNumber(num){
        var numStr = num.toString();
        var numInter = parseInt(numStr.substring(0,numStr.indexOf(".")>-1?numStr.indexOf("."):2));
        if(numInter>100){
            numInter = parseInt(numInter.toString().substring(0,1));
        }
        return numInter;
    }
    builtIn.line=function setLineOption(data) {
        var titleText = data.element.getAttribute("titleText");
        var legends = data.element.getAttribute("legend").split(",");
        var series = [];
        for (var i = 0; i < legends.length; i++) {
            var name = legends[i];
            series.push({name:name,type:'line',data:[]});
        }
        var option = {
            title: {
                text: titleText,
                top: '7%',
                left: "45%"
            },
            tooltip: {
                trigger: 'axis',
                axisPointer: {
                    type: 'cross',
                    label: {
                        backgroundColor: '#283b56'
                    }
                }
            },
            legend: {
                top:"-5px",
                data:legends
            },
            toolbox: {
                show: true,
                feature: {
                    saveAsImage: {}
                },
                top:"7%"
            },
            dataZoom: {
                show: false,
                start: 0,
                end: 100
            },
            xAxis: [
                {
                    type: 'category',
                    boundaryGap: true,
                    data: (function (){
                        var now = new Date();
                        var res = [];
                        var len = 10;
                        while (len--) {
                            res.unshift(now.toLocaleTimeString().replace(/^\D*/,''));
                            now = new Date(now - 2000);
                        }
                        return res;
                    })()
                }
            ],
            yAxis: [
                {
                    type: 'value',
                    scale: true,
                    name: '溫度℃',
                    max:100,
                    min:0,
                    boundaryGap: [0.2, 0.2]
                }
            ],
            series: series
        };
        data.obj.setOption(option,true);
        return option;
    }
    win.xgs = win['xgs'] || {};
    win.xgs.AssembleEcharts = obj;
})(window)