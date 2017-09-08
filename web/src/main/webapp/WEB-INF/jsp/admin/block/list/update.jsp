<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="../../../include/lib.jsp" %>
<link rel="stylesheet" href="${ctxStatic}/assets/css/select2.css" />
<!--引入CSS-->
<link rel="stylesheet" type="text/css" href="${ctxStatic}/js/webuploader/webuploader.css">
<link rel="stylesheet" href="${ctxStatic}/css/widget-header.css">
<!--引入JS-->
<script type="text/javascript" src="${ctxStatic}/js/webuploader/webuploader.js"></script>

<div class="row">
	<div class="col-xs-12 update-col-xs-12">
		<!-- PAGE CONTENT BEGINS -->
		<div class="widget-box">
			<div class="widget-header widget-header-blue widget-header-flat">
				<h6 class="widget-title lighter">${name}区块</h6>
			</div>
			<div class="widget-body">
				<div class="widget-main">
					<form class="form-horizontal" id="validation-form" method="post" role="form">
					<input type="hidden" id="id" name="block.id" value="${block.get('id') }">
						<div class="form-group" id="validation-form-group">
							<div class="col-lg-12">
								<div class="form-group" style="margin:0px;">
									<div class="col-lg-12">
										<div class="form-group has-feedback">
											<label class="control-label col-xs-12 col-sm-1 no-padding-right" for="name">区块名<span style="color:#f00">*</span></label>
											<div class="col-xs-12 col-sm-11">
												<div class="clearfix">
													<input type="text" class="col-xs-12 col-sm-12" id="name" name="block.name" value="${block.get('name')}" required/>
												</div>
											</div>
										</div>
									</div>
								</div>
								<div class="form-group" style="margin:0px;">
									<div class="col-lg-12">
										<div class="form-group has-feedback">
											<label class="control-label col-xs-12 col-sm-1 no-padding-right" for="block_acreage">面积<span style="color:#f00">*</span></label>
											<div class="col-xs-12 col-sm-11">
												<div class="clearfix">
													<div class="input-group">
														<input type="number" class="col-xs-12 col-sm-12" id="block_acreage" aria-describedby="basic-addon2" name="block.block_acreage" value="${block.get('block_acreage')}" required/>
														<span class="input-group-addon" id="basic-addon2">亩</span>
													</div>
												</div>
											</div>
										</div>
									</div>
								</div>
								<div class="form-group" style="margin:0px;">
									<div class="col-lg-12">
										<div class="form-group has-feedback">
											<label class="control-label col-xs-12 col-sm-1 no-padding-right" for="lng_lat">坐标点<span style="color:#f00">*</span></label>
											<div class="col-xs-12 col-sm-11">
												<div class="clearfix">
													<input type="text" class="col-xs-12 col-sm-12" readonly="readonly" onclick="openMap()" onchange="onChangerText(this)" id="lng_lat" name="block.lng_lat" value="${block.get('lng_lat')}"required/>
													<input type="hidden" id="city" name="block.city" value="${block.get('city') }">
													<input type="hidden" id="county" name="block.county" value="${block.get('county') }">
													<input type="hidden" id="province" name="block.province" value="${block.get('province') }">
												</div>
											</div>
										</div>
									</div>
								</div>

								<div class="form-group" style="margin:0px;">
									<div class="col-lg-12">
										<div class="form-group has-feedback">
											<label class="control-label col-xs-12 col-sm-1 no-padding-right" for="address">详细地址<span style="color:#f00">*</span></label>
											<div class="col-xs-12 col-sm-11">
												<div class="clearfix">
													<input type="text" class="col-xs-12 col-sm-12" id="address"  onchange="onChangerText(this)" name="block.address" value="${block.get('address') }"required/>
												</div>
											</div>
										</div>
									</div>
								</div>
								<div class="form-group" style="margin:0px;">
									<div class="col-lg-12">
										<div class="form-group has-feedback">
											<label class="control-label col-xs-12 col-sm-1 no-padding-right" for="install_type">安装方式<span style="color:#f00">*</span></label>
											<div class="col-xs-12 col-sm-11">
												<div class="clearfix">
													<select class="col-xs-12 col-sm-12 form-control" id="install_type" name="machine.install_type">
														<option value="1">有线安装</option>
														<option value="2">无线安装</option>
													</select>
												</div>
											</div>
										</div>
									</div>
								</div>
								<div class="form-group" style="margin:0px;">
									<div class="col-lg-12">
										<div class="form-group has-feedback">
											<label class="control-label col-xs-12 col-sm-1 no-padding-right" for="machineIds">设备分配<span style="color:#f00">*</span></label>
											<div class="col-xs-12 col-sm-11">
												<div class="clearfix">
													<sys:treeselect id="machineIds" name="machineIds"  path="${ctx}/findMachineByBlockId" checkedEvent="" params="{blockId:'${block.get('id')}'}" />
												</div>
											</div>
										</div>
									</div>
								</div>
								<div class="form-group" style="margin:0px;">
									<div class="col-lg-12">
										<div class="form-group has-feedback">
											<label class="control-label col-xs-12 col-sm-1 no-padding-right" for="description">区块描述<span style="color:#f00">*</span></label>
											<div class="col-xs-12 col-sm-11">
												<div class="clearfix">
													<textarea class="col-xs-12 col-sm-12" id="description" name="block.description"  >${block.get('description')}</textarea>
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
<script type="text/javascript" src="${ctxStatic}/js/tree/jquery.ztree.exhide.min.js"></script>

<script type="text/javascript">

    function onChangerText(obj){
        $(obj).closest('#validation-form').formValidation('updateStatus', $(obj), 'NOT_VALIDATED')
            .formValidation('validateField',$(obj));
    }


    function openMap(){
        layer.open({
            type: 2,
            area: ['1000px', '650px'],
            fix: true, //不固定
            maxmin: false,
            content: '${ctxStatic}/baidumap/index.html',
            btn: ['确定'],
            yes: function(index,selector){
                try {
                    var iframeWindow = $('iframe',selector)[0].contentWindow;
                    var iframedocument = iframeWindow.document.body;
                    onChangerText("#address");
                    onChangerText("#lng_lat");
                    //TODO　ｚｚｈｄａｏｌ
                } catch (e) {
                }
                layer.close(index);
            }
        });
    }
$(function(){
    if("${block.get('install_type')}"!==""){
        $("#install_type").val("${block.get('install_type')}");
    }
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
