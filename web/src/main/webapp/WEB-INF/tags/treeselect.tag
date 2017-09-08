<%--
  Created by IntelliJ IDEA.
  User: duai
  Date: 2017-09-01
  Time: 12:07
  To change this template use File | Settings | File Templates.
--%>
<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/include/lib.jsp"%>
<%@ attribute name="id" type="java.lang.String" required="true" description="控件ID"%>
<%@ attribute name="name" type="java.lang.String" required="true" description="提交参数的name"%>
<%@ attribute name="path" type="java.lang.String" required="true" description="请求路劲"%>
<%@ attribute name="params" type="java.lang.String" required="false" description="请求参数"%>
<%@ attribute name="checkedEvent" type="java.lang.String" required="false" description="选中事件"%>
<input type="hidden" class="col-xs-12 col-sm-12" id="${id}"  name="${name}" />
<input type="text"  class="col-xs-12 col-sm-12"  onclick="init();" id="${id}_name" placeholder="点击选择设备" />
<script type="text/javascript">
    var hiddenNodes=[];	//用于存储被隐藏的结点
    var zNodes=[];
    var zTreeObj;

    function init(){
        $.ajax({
            url:'${path}',
            async:false,
            data:${params},
            success:function (result) {
                if(!result.code&&result.data.length>0){
                    var html = "";
                    html +='<div class="container" style="width: 250px">';
                    html += '<div class="row">';
                    html += '  <div class="col-lg-12">';
                    html += '       <div class="input-group">';
                    html += '           <input type="text" class="form-control" id="keyword"  type="text" placeholder="请输入...">';
                    html += '           <span class="input-group-btn">';
                    html += '             <button class="btn btn-default" style="color: #fff;height: 34px;padding: 0;" type="button"onclick="filter()">搜索</button>';
                    html += '           </span>';
                    html += '       </div>';
                    html += '  </div><!-- /.col-lg-6 -->';
                    html += '</div><!-- /.row -->';
                    html +='	<div class="content">';
                    html +='		<ul id="tree-obj" class="ztree"></ul>';
                    html +='	</div>';
                    html +='</div>';
                    zNodes = result.data;
                    layer.open({
                        title: '设备列表',
                        shade: 0.6, //遮罩透明度
                        moveType: 1, //拖拽风格，0是默认，1是传统拖动
                        shift: 0, //0-6的动画形式，-1不开启
                        area: ['300px', '500px'],
                        content: html,
                        yes:function (index) {
                            var select = zTreeObj.getCheckedNodes();
                            var ids = "";
                            var name = "";
                            for (var i = 0;i<select.length;i++){
                                ids += select[i].id+",";
                                name += select[i].name+",";
                            }
                            $("#${id}").val(ids.substr(0,ids.length-1));
                            $("#${id}_name").val(name.substr(0,name.length-1))
                            layer.close(index);
                        }
                    });
                }else{
                    layer.alert(result.msg, {
                        icon: 5,
                        skin: 'layer-ext-moon'
                    },function(index){
                        layer.close(index);
                    })
                }
            }
        });

        var setting = {		//ztree配置选项
            check: {
                enable: true,
                chkStyle:"checkbox",
                chkboxType:{ "Y" : "ps", "N" : "ps" }
            },
            view: {
                dblClickExpand: false
            },
            data: {
                simpleData: {
                    enable: true,
                    idKey: "id",
                    pIdKey: "parentId",
                    rootPId: -1
                }
            },
            callback: {
               <c:if test="${checkedEvent != null and not empty checkedEvent }">
                onCheck: ${checkedEvent}
                </c:if>
            }
        };
        zTreeObj = $.fn.zTree.init($("#tree-obj"), setting, zNodes);
        var ids = $("#${id}").val().split(",");
        for (var i = 0;i<ids.length;i++){
            var node = zTreeObj.getNodeByParam("id", ids[i]);
            if (!xgs.utils.isNull(node)){
                zTreeObj.checkNode(node);
            }
        }

    };
    //过滤ztree显示数据
    function filter(){
        //显示上次搜索后背隐藏的结点
        zTreeObj.showNodes(hiddenNodes);
        //查找不符合条件的叶子节点
        function filterFunc(node){
            var _keywords=$("#keyword").val();
            if(node.isParent||node.name.indexOf(_keywords)!=-1) return false;
            return true;
        };
        //获取不符合条件的叶子结点
        hiddenNodes=zTreeObj.getNodesByFilter(filterFunc);
        //隐藏不符合条件的叶子结点
        zTreeObj.hideNodes(hiddenNodes);
    };
</script>