<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="../../include/lib.jsp" %>
<link rel="stylesheet" href="${ctx}/css/multi-select.css" />
<script type="text/javascript" src="${ctx}/js/layer/layer.js"></script>
<script type="text/javascript" src="${ctx}/js/tree/jquery.ztree.all-3.5.min.js"></script>
<script type="text/javascript" src="${ctx}/js/validation/formValidation.js"></script>
<script type="text/javascript" src="${ctx}/js/validation/formValidation.extend.js"></script>
<script type="text/javascript" src="${ctx}/js/validation/framework/bootstrap.min.js"></script>
<script type="text/javascript" src="${ctx}/js/validation/language/zh_CN.js"></script>
<script type="text/javascript" src="${ctx}/js/plugins/uploader.js"></script>
<script type="text/javascript" src="${ctx}/js/plugins/uploader2.js"></script>
<script type="text/javascript" src="${ctx}/assets/js/select2.js"></script>
<script type="text/javascript" src="${ctx}/js/other.js"></script>
<script type="text/javascript" src="${ctx}/static/role/jquery.multi-select.js"></script>
<script type="text/javascript" src="${ctx}/static/role/jquery.quicksearch.js"></script>
<style>
    .ms-container {
        width: 600px;
        height: 350px;
    }
    .ms-selectable{
        height: 100%;
    }
    .ms-selection{
        height: 100%;
    }
   .ms-selectable .ms-list{
        height: 100%;
    }
    .ms-selection  .ms-list{
        height: 100%;
    }
    .col-center-block {
        float: none;
        display: block;
        margin-left: auto;
        margin-right: auto;
    }
</style>
<div class="row">
    <form class="form-horizontal" id="validation-form" method="post" role="form">
        <input type="hidden" id="id" name="id" value="${id}">
        <div class="col-xs-5 col-center-block">
            <select id='pre-selected-options' name="ids" multiple='multiple'>

            </select>
            <button type="button" onclick="savedata()" class="btn btn-primary" style="margin-top: 15%;margin-left: 47%;" id="xgs-save">Submit</button>
        </div>
    </form>
</div>


<script type="application/javascript">

    function transformTo(sNodes){
        var i,l,
            key = 'id',
            parentKey = 'parent_id',
            childKey = 'children';
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
    }

    function  selectNotAll() {
        $('#pre-selected-options').multiSelect('deselect_all');
    }
    function selectAll(){
        $('#pre-selected-options').multiSelect('select_all');
    }
    function foreachOption(obj){
        var html = "";
        for(var i = 0;i<obj.length;i++){
            html +="<optgroup label='"+obj[i].menu_name+"(有子菜单时,父级必选)'>";
            html+="<option value='"+obj[i].id+"' "+obj[i].select+">"+obj[i].menu_name+"(父级菜单)</option>";
            for(var j = 0;j<obj[i].children.length;j++){
                html+="<option value='"+obj[i].children[j].id+"' "+obj[i].children[j].select+">"+obj[i].children[j].menu_name+"</option>";
            }
            html +="</optgroup>";
        }
        $("#pre-selected-options").html(html);
    }
    $(function(){
        //加载数据
        foreachOption( transformTo(${map}));
        //初始化空间
        $('#pre-selected-options').multiSelect({
            /*selectableHeader: '<button type="button" id="right_All_1" onclick="selectAll()" class="btn btn-block"><i class="glyphicon glyphicon-forward"></i></button>',
            selectionHeader: '<button type="button" id="left_All_1" onclick="selectNotAll()" class="btn btn-block"><i class="glyphicon glyphicon-backward"></i></button>',*/
            selectableHeader: "<button type='button' id='right_All_1' onclick='selectAll()' style='width: 100%'  class='btn btn-block'><i class='glyphicon glyphicon-forward'></i></button><input type='text' class='search-input' style='width: 100%' autocomplete='off' placeholder='搜索...'>",
            selectionHeader: "<button type='button' id='left_All_1' onclick='selectNotAll()' style='width: 100%'  class='btn btn-block'><i class='glyphicon glyphicon-backward'></i></button><input type='text' class='search-input' style='width: 100%' autocomplete='off' placeholder='搜索...'>",
            selectableOptgroup: true,
            afterInit: function(ms){
                var that = this,
                    $selectableSearch = that.$selectableUl.prev(),
                    $selectionSearch = that.$selectionUl.prev(),
                    selectableSearchString = '#'+that.$container.attr('id')+' .ms-elem-selectable:not(.ms-selected)',
                    selectionSearchString = '#'+that.$container.attr('id')+' .ms-elem-selection.ms-selected';

                that.qs1 = $selectableSearch.quicksearch(selectableSearchString)
                    .on('keydown', function(e){
                        if (e.which === 40){
                            that.$selectableUl.focus();
                            return false;
                        }
                    });

                that.qs2 = $selectionSearch.quicksearch(selectionSearchString)
                    .on('keydown', function(e){
                        if (e.which == 40){
                            that.$selectionUl.focus();
                            return false;
                        }
                    });
            },
            afterSelect: function(){
                this.qs1.cache();
                this.qs2.cache();
            },
            afterDeselect: function(values){
                this.qs1.cache();
                this.qs2.cache();
            }
        });
    })

    function savedata(){
        var url = '${ctx}/role/auth';
        var form = $("#validation-form");
        xgs.utils.mask.show('数据正在提交中...');
        $.ajax({
            url : url,
            type : "POST",
            //cache : false, // 禁用缓存
            data:form.serialize(),
            dataType : "json",
            success : function(result) {
                xgs.utils.mask.hide();
                if(!result.code){
                    layer.alert(result.message, {
                        icon: 1,
                        skin: 'layer-ext-moon'
                    },function(index){
                        layer.close(index);
                    })
                    renderHtml("${ctx}/role/index");
                }else{
                    layer.alert(result.message, {
                        icon: 5,
                        skin: 'layer-ext-moon'
                    },function(index){
                        layer.close(index);
                    })
                    $("#xgs-save").removeAttr("disabled").removeClass("disabled");
                }
            },
            error : function(XMLHttpRequest, textStatus, errorThrown) {
            }
        });
    }
</script>