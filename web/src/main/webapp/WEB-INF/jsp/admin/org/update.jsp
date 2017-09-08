<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="../../include/lib.jsp" %>
<link rel="stylesheet" href="${ctxStatic}/assets/css/select2.css" />
<!--引入CSS-->
<link rel="stylesheet" type="text/css" href="${ctxStatic}/js/webuploader/webuploader.css">
<link rel="stylesheet" type="text/css" href="${ctxStatic}/js/tree/zTreeStyle/zTreeStyle.css">
<link rel="stylesheet" href="${ctxStatic}/css/widget-header.css">
<style type="text/css">
	ul.ztree {margin-top: 10px;border: 1px solid #617775;background: #f0f6e4;width:220px;height:360px;overflow-y:scroll;overflow-x:auto;}
</style>
<!--引入JS-->
<script type="text/javascript" src="${ctxStatic}/js/webuploader/webuploader.js"></script>

<div class="row">
	<div class="col-xs-12 update-col-xs-12">
		<!-- PAGE CONTENT BEGINS -->
		<div class="widget-box">
			<div class="widget-header widget-header-blue widget-header-flat">
				<h6 class="widget-title lighter">${name}机构</h6>
			</div>
			<div class="widget-body">
				<div class="widget-main">
					<form class="form-horizontal" id="validation-form" method="post" role="form">
					<input type="hidden" id="id" name="org.id" value="${org.get('id') }">
						<div class="form-group" id="validation-form-group">
							<div class="col-lg-12">
								<div class="form-group" style="margin:0px;">
									<div class="col-lg-12">
										<div class="form-group has-feedback">
											<label class="control-label col-xs-12 col-sm-1 no-padding-right" for="name">机构名<span style="color:#f00">*</span></label>
											<div class="col-xs-12 col-sm-11">
												<div class="clearfix">
													<input type="text" class="col-xs-12 col-sm-12" id="name" name="org.name" value="${org.get('name')}" required/>
												</div>
											</div>
										</div>
									</div>
								</div>

								<div class="form-group" style="margin:0px;">
									<div class="col-lg-12">
										<div class="form-group has-feedback">
											<label class="control-label col-xs-12 col-sm-1 no-padding-right" for="parent_id">所属机构<span style="color:#f00">*</span></label>
											<div class="col-xs-12 col-sm-11">
												<div class="clearfix">
													<input type="text" style="display: none" class="col-xs-12 col-sm-12" id="parent_id"  name="org.parent_id" value="${org.get('parent_id')}" />
													<input type="text" class="col-xs-12 col-sm-12" id="parent_name" onclick="showMenu(); return false;" value="${org.get('parent_id')}" />
													<div id="menuContent" class="menuContent" style="display:none; position: absolute;">
														<ul id="treeDemo" class="ztree" style="margin-top:0; width:180px; height: 300px;"></ul>
													</div>
												</div>
											</div>
										</div>
									</div>
								</div>
							</div>
						</div>
						<hr />
						<div class="form-group">
							<div class="col-sm-9 col-sm-offset-3">
								<button  class="btn btn-sm btn-primary" id="xgs-save" name="signup" value="Sign up">提交</button>
							</div>
						</div>
					</form>
					<!-- /section:plugins/fuelux.wizard -->
				</div>
				<!-- /.widget-main -->
			</div>
			<!-- /.widget-body -->
		</div>
		<!-- PAGE CONTENT ENDS -->
	</div>
	<!-- /.col -->
</div>
<!-- /.row -->

<!-- page specific plugin scripts -->
<style>
.select2-container .select2-choice {
    border-radius: 0;
    height: 32px;
    line-height: 28px;
}
</style>
<link rel="stylesheet" type="text/css" href="${ctxStatic}/js/webuploader/uploader.css">
<link rel="stylesheet" type="text/css" href="${ctxStatic}/js/tree/metro.css">
<script type="text/javascript" src="${ctxStatic}/assets/js/bootstrap.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/layer/layer.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/tree/jquery.ztree.all-3.5.min.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/validation/formValidation.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/validation/formValidation.extend.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/validation/framework/bootstrap.min.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/validation/language/zh_CN.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/plugins/uploader.js"></script>
<script type="text/javascript" src="${ctxStatic}/assets/js/select2.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/other.js"></script>
<script type="text/javascript">
    var setting = {
        check: {
            enable: true,
            chkStyle: "radio",
            radioType: "all"
        },
        view: {
            dblClickExpand: false
        },
        data: {
            simpleData: {
                enable: true,
                idKey: "id",
                pIdKey: "parentId",
                rootPId: ${rootId}
            }
        },
        callback: {
            onClick: onClick,
            onCheck: onCheck
        }
    };



    function onClick(e, treeId, treeNode) {
        var zTree = $.fn.zTree.getZTreeObj("treeDemo");
        zTree.checkNode(treeNode, !treeNode.checked, null, true);
        return false;
    }

    function onCheck() {
        var zTree = $.fn.zTree.getZTreeObj("treeDemo"),
            nodes = zTree.getCheckedNodes(true);
        if (nodes.length===0){
            return;
		}
       $("#parent_name").val(nodes[0].name);
       $("#parent_id").val(nodes[0].id);
    }

    function showMenu() {
        var cityObj = $("#parent_name");
        var cityOffset = $("#parent_name").offset();
        $("#menuContent").css({top:cityObj.outerHeight() + "px"}).slideDown("fast");

        $("body").bind("mousedown", onBodyDown);
    }
    function hideMenu() {
        $("#menuContent").fadeOut("fast");
        $("body").unbind("mousedown", onBodyDown);
    }
    function onBodyDown(event) {
        if (!(event.target.id == "parent_name"  || event.target.id == "menuContent" || $(event.target).parents("#menuContent").length>0)) {
            hideMenu();
        }
    }

    $(document).ready(function(){
        $.fn.zTree.init($("#treeDemo"), setting, ${data});
    });

$(function(){
    var tree = $.fn.zTree.getZTreeObj("treeDemo");
    var node = tree.getNodeByParam("id", "${org.get('parent_id')}");
    if (!xgs.utils.isNull(node)){
        tree.checkNode(node);
        onCheck();
    }
	function savedata(form){
        var url = xgs.utils.setToken(location.href,'${token}');
		var form = $(form);
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
					layer.alert(result.msg, {
						  icon: 1,
						  skin: 'layer-ext-moon'
					},function(index){
						layer.close(index);
					})
                    renderHtml("${listToken}");
				}else{
					layer.alert(result.msg, {
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
	
	


	var valid = $("#validation-form").formValidation({
		locale:'zh_CN',
		excluded: [':disabled'],
		//err:{container:'tooltip'},
		button:{selector:'#xgs-save'},
        icon: {
            valid: 'glyphicon glyphicon-ok',
            invalid: 'glyphicon glyphicon-remove',
            validating: 'glyphicon glyphicon-refresh'
        },
        fields:{
        	t_id:{
        		validators:{
        			notEmpty: {}
        		}
        	}
        }
	}).on('success.form.fv', function(e) {
        // Prevent form submission
        e.preventDefault();

        // Some instances you can use are
        var $form = $(e.target),        // The form instance
            fv    = $(e.target).data('formValidation'); // FormValidation instance
            savedata(this);
        // Do whatever you want here ...
    });
})

</script>
