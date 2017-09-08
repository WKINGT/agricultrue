<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="../../include/lib.jsp" %>
<link rel="stylesheet" href="${ctxStatic}/assets/css/select2.css" />
<link rel="stylesheet" type="text/css" href="${ctxStatic}/js/tree/zTreeStyle/zTreeStyle.css">
<link rel="stylesheet" href="${ctxStatic}/css/widget-header.css">
<style type="text/css">
	ul.ztree {margin-top: 10px;border: 1px solid #617775;background: #f0f6e4;width:220px;height:360px;overflow-y:scroll;overflow-x:auto;}
	.select2-container .select2-choice {
		border-radius: 0;
		height: 32px;
		line-height: 30px;
		background: #fff;
		background-image:url();
		border: 0px solid #aaa;
	}
	.select2-container .select2-choice .select2-arrow{
		border-left: 0px;
		border-radius: 0;
		background: #fff;
		background-image:url();
	}
	.select2-container .select2-choice .select2-arrow b:before{
		content:''
	}
	.select2-choices{
		border: none !important;
	}
</style>
<div class="row">
	<div class="col-xs-12 update-col-xs-12" >
		<!-- PAGE CONTENT BEGINS -->
		<div class="widget-box">
			<div class="widget-header widget-header-blue widget-header-flat">
				<h6 class="widget-title lighter"><span style="font-size: 13px;">${name}用户</span></h6>
			</div>
			<div class="widget-body">
				<div class="widget-main">
					<form class="form-horizontal" id="validation-form" method="post" role="form">
					<input type="hidden" id="id" name="userInfo.id" value="${userInfo.get('id') }">
						<div class="container-fluid form-group" id="validation-form-group">
								<div class="form-group" style="margin:0px;">
									<div class="col-lg-6">
										<div class="form-group has-feedback">
											<label class="control-label col-xs-12 col-sm-2 no-padding-right" for="nick_name">姓名<span style="color:#f00">*</span></label>
											<div class="col-xs-12 col-sm-10">
												<div class="clearfix">
													<input type="text" class="col-xs-12 col-sm-12 form-control" id="nick_name" name="userInfo.name" value="${userInfo.get('name')}" required/>
												</div>
											</div>
										</div>
									</div>
									<div class="col-lg-6">
										<div class="form-group has-feedback">
											<label class="control-label col-xs-12 col-sm-2 no-padding-right" for="login_name">登录名<span style="color:#f00">*</span></label>
											<div class="col-xs-12 col-sm-10">
												<div class="clearfix">
													<input type="text" class="col-xs-12 col-sm-12 form-control" id="login_name" name="userInfo.login_account" value="${userInfo.get('login_account')}"required/>
												</div>
											</div>
										</div>
									</div>
								</div>
								<div class="form-group" style="margin:0px;">
									<div class="col-lg-6">
										<div class="form-group has-feedback">
											<label class="control-label col-xs-12 col-sm-2 no-padding-right" for="mobile">手机号<span style="color:#f00">*</span></label>
											<div class="col-xs-12 col-sm-10">
												<div class="clearfix">
													<input type="text" class="col-xs-12 col-sm-12 form-control" id="mobile" name="userInfo.mobile" value="${userInfo.get('mobile')}"required/>
												</div>
											</div>
										</div>
									</div>
									<div class="col-lg-6">
										<div class="form-group has-feedback">
											<label class="control-label col-xs-12 col-sm-2 no-padding-right" for="address">住址<span style="color:#f00">*</span></label>
											<div class="col-xs-12 col-sm-10">
												<div class="clearfix">
													<input type="text" class="col-xs-12 col-sm-12 form-control" id="address" name="userInfo.address" value="${userInfo.get('address')}"required/>
												</div>
											</div>
										</div>
									</div>
								</div>
							<div class="form-group" style="margin:0px;">
								<div class="col-lg-6">
									<div class="form-group has-feedback">
										<label class="control-label col-xs-12 col-sm-2 no-padding-right" for="login_pwd">密码<span style="color:#f00">*</span></label>
										<div class="col-xs-12 col-sm-10">
											<div class="clearfix">
												<input type="password" class="col-xs-12 col-sm-12 form-control"  id="login_pwd" name="userInfo.login_pwd" value=""
													   <c:if test="${name eq '修改'}">placeholder="如若不修改密码，请勿填写"</c:if>
														<c:if test="${name eq '添加'}"> required="required"</c:if>
												/>
											</div>
										</div>
									</div>
								</div>
								<div class="col-lg-6">
									<div class="form-group has-feedback">
										<label class="control-label col-xs-12 col-sm-2 no-padding-right" for="org_name">所属机构<span style="color:#f00">*</span></label>
										<div class="col-xs-12 col-sm-10">
											<div class="clearfix">
												<input type="text" style="display: none" class="col-xs-12 col-sm-12 form-control" id="org_id"  name="userInfo.org_id" value="${userInfo.get('org_id')}" required/>
												<input type="text" class="col-xs-12 col-sm-12" id="org_name" onclick="showMenu(); return false;"  required/>
												<div id="menuContent" class="menuContent" style="display:none; position: absolute;    z-index: 1;">
													<ul id="treeDemo" class="ztree" style="margin-top:0; width:180px; height: 300px;"></ul>
												</div>
											</div>
										</div>
									</div>
								</div>
							</div>
								<div class="form-group" style="margin:0px;">
									<div class="col-lg-6">
										<div class="form-group has-feedback">
											<label class="control-label col-xs-12 col-sm-2 no-padding-right" for="enabled">是否启用<span style="color:#f00">*</span></label>
											<div class="col-xs-12 col-sm-10">
												<div class="clearfix">
													<select class="col-xs-12 col-sm-12 form-control" id="enabled" name="userInfo.enabled" >
														<option value="0">是</option>
														<option value="1">否</option>
													</select>
												</div>
											</div>
										</div>
									</div>
									<div class="col-lg-6">
										<div class="form-group has-feedback">
											<label class="control-label col-xs-12 col-sm-2 no-padding-right" for="head_portrait">用户头像<span style="color:#f00">*</span></label>
											<div class="col-xs-12 col-sm-10">
												<div class="clearfix">
													<div class="clearfix" id="head_portrait">

													</div>
												</div>
											</div>
										</div>
									</div>
								</div>
								<div class="form-group" style="margin:0px;">
									<div class="col-lg-12">
										<div class="form-group has-feedback">
											<label class="control-label col-xs-12 col-sm-1 no-padding-right" for="role_id">角色分配<span style="color:#f00">*</span></label>
											<div class="col-xs-12 col-sm-11" >
												<div class="clearfix">
													<select multiple="multiple"  id="role_id" class="col-xs-12 col-sm-12 form-control" name="roleIds" data-class="select2"  style="padding: 0;"  data-placeholder="选择角色">
														<c:forEach items="${roles}" var="role">
															<option value="${role.id}">${role.role_name}</option>
														</c:forEach>
													</select>
												</div>
											</div>
										</div>
									</div>
								</div>
							<div class="form-group" style="margin:0px;">
								<div class="col-lg-12">
									<div class="form-group has-feedback">
										<label class="control-label col-xs-12 col-sm-1 no-padding-right" for="blockIds">区块分配<span style="color:#f00">*</span></label>
										<div class="col-xs-12 col-sm-11" >
											<div class="clearfix">
												<select multiple="multiple"  id="blockIds" class="col-xs-12 col-sm-12 form-control" name="blockIds" data-class="select2" style="padding: 0;"  data-placeholder="选择区块">
													<c:forEach items="${blocks}" var="block">
														<option value="${block.id}">${block.name}</option>
													</c:forEach>
												</select>
											</div>
										</div>
									</div>
								</div>
							</div>

							<div class="col-xs-6 ztree" id="items">
								
							</div>
						</div>
						<hr />
						<div class="form-group" style=" z-index: 2;">
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
	li {list-style-type:none;}
	.select2-container .select2-choice {
		border-radius: 0;
		height: 32px;
		line-height: 28px;
	}
	.webuploader-container{
		display: inline-block !important;
		margin-left: 20% !important;
	}
	.webuploader-element-invisible{
		display: none !important;
	}
	.xgs-uploader .btns{
		margin-top: 7px !important;
	}
</style>
<link rel="stylesheet" type="text/css" href="${ctxStatic}/js/webuploader/uploader.css">
<link rel="stylesheet" type="text/css" href="${ctxStatic}/js/tree/metro.css">
<script type="text/javascript" src="${ctxStatic}/js/webuploader/webuploader.js"></script>
<script type="text/javascript" src="${ctxStatic}/assets/js/bootstrap.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/layer/layer.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/tree/jquery.ztree.all-3.5.min.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/validation/formValidation.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/validation/formValidation.extend.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/validation/framework/bootstrap.min.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/validation/language/zh_CN.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/plugins/uploader.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/plugins/uploader2.js"></script>
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
        $("#org_name").val(nodes[0].name);
        $("#org_id").val(nodes[0].id);
    }

    function showMenu() {
        var cityObj = $("#org_name");
        var cityOffset = $("#org_name").offset();
        $("#menuContent").css({top:cityObj.outerHeight() + "px"}).slideDown("fast");

        $("body").bind("mousedown", onBodyDown);
    }
    function hideMenu() {
        $("#menuContent").fadeOut("fast");
        $("body").unbind("mousedown", onBodyDown);
    }
    function onBodyDown(event) {
        if (!(event.target.id == "org_name"  || event.target.id == "menuContent" || $(event.target).parents("#menuContent").length>0)) {
            hideMenu();
        }
    }

    $(document).ready(function(){
        $.fn.zTree.init($("#treeDemo"), setting, ${data});
    });

$(function(){
    $('#head_portrait').uploader({
        id:'head_portrait',
        isImage:true,
        allowType: xgs.utils.uploader.accept.image,
        value:'${userInfo.get("head_portrait")}'
    });
    var tree = $.fn.zTree.getZTreeObj("treeDemo");
    var node = tree.getNodeByParam("id", "${userInfo.get('org_id')}");
    if (!xgs.utils.isNull(node)){
        tree.checkNode(node);
        onCheck();
    }
    $("#enabled").val(${userInfo.get('enabled')});
    $('select[data-class=select2]').select2({formatNoMatches:"无查询内容！"});
	if('${checkedRoles}'!==''){
        $("#role_id").val(${checkedRoles}).trigger('change');
    }
    if('${checkedBlocks}'!==''){
        $("#blockIds").val(${checkedBlocks}).trigger('change');
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
