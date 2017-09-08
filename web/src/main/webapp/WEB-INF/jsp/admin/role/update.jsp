<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="../../include/lib.jsp" %>
<link rel="stylesheet" href="${ctxStatic}/assets/css/select2.css" />
<!--引入CSS-->
<link rel="stylesheet" type="text/css" href="${ctxStatic}/js/webuploader/webuploader.css">
<!--引入JS-->
<script type="text/javascript" src="${ctxStatic}/js/webuploader/webuploader.js"></script>
<link rel="stylesheet" href="${ctxStatic}/css/widget-header.css">

<div class="row">
	<div class="col-xs-12 update-col-xs-12">
		<!-- PAGE CONTENT BEGINS -->
		<div class="widget-box">
			<div class="widget-header widget-header-blue widget-header-flat">
				<h6 class="widget-title lighter">${name}角色</h6>
			</div>
			<div class="widget-body">
				<div class="widget-main">
					<form class="form-horizontal" id="validation-form" method="post" role="form">
					<input type="hidden" id="id" name="role.id" value="${role.get('id') }">
						<div class="form-group" id="validation-form-group">
							<div class="col-lg-12">
								<div class="form-group" style="margin:0px;">
									<div class="col-lg-12">
										<div class="form-group has-feedback">
											<label class="control-label col-xs-12 col-sm-1 no-padding-right" for="name">角色名<span style="color:#f00">*</span></label>
											<div class="col-xs-12 col-sm-11">
												<div class="clearfix">
													<input type="text" class="col-xs-12 col-sm-12" id="name" name="role.role_name" value="${role.get('role_name')}" required/>
												</div>
											</div>
										</div>
									</div>
								</div>
								<div class="form-group" style="margin:0px;">
									<div class="col-lg-12">
										<div class="form-group has-feedback">
											<label class="control-label col-xs-12 col-sm-1 no-padding-right" for="roleDesc">职称名</label>
											<div class="col-xs-12 col-sm-11">
												<div class="clearfix">
													<input type="text" class="col-xs-12 col-sm-12" id="roleDesc" name="role.role_desc" value="${role.get('role_desc')}"/>
												</div>
											</div>
										</div>
									</div>
								</div>
							</div>
							<div class="col-lg-6 ztree" id="items">
								
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
		enable: true
	},
    view: {
        dblClickExpand: false,
        showLine: true,
        selectedMulti: false
    },
    data: {
        simpleData: {
            enable:true,
            idKey: "id",
            pIdKey: "pid"
        }
    }
};


$(function(){
	var setting = "${func}";
	function savedata(form){
        var url = xgs.utils.setToken(location.href,'${token}');
		console.log(url);
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
