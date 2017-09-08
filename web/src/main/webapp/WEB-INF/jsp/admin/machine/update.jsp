<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="../../include/lib.jsp" %>
<link rel="stylesheet" href="${ctxStatic}/assets/css/select2.css" />
<link rel="stylesheet" href="${ctxStatic}/css/widget-header.css">
<link rel="stylesheet" href="${ctxStatic}/css/select2.css">
<!--引入CSS-->
<div class="row">
	<div class="col-xs-12 update-col-xs-12">
		<!-- PAGE CONTENT BEGINS -->
		<div class="widget-box">
			<div class="widget-header widget-header-blue widget-header-flat">
				<h6 class="widget-title lighter">${name}设备</h6>
			</div>
			<div class="widget-body">
				<div class="widget-main">
					<form class="form-horizontal" id="validation-form" method="post" role="form">
						<input type="hidden" id="id" name="machine.id" value="${machine.get('id') }">
						<div class="form-group" id="validation-form-group">
							<div class="form-group" style="margin:0px;">
								<div class="col-lg-6">
									<div class="form-group has-feedback">
										<label class="control-label col-xs-12 col-sm-2 no-padding-right" for="terminal_id">设备编号<span style="color:#f00">*</span></label>
										<div class="col-xs-12 col-sm-10">
											<div class="clearfix">
												<input type="text" class="col-xs-12 col-sm-12 form-control" id="terminal_id" name="machine.terminal_id" value="${machine.get('terminal_id')}" required/>
											</div>
										</div>
									</div>
								</div>
								<div class="col-lg-6">
									<div class="form-group has-feedback">
										<label class="control-label col-xs-12 col-sm-2 no-padding-right" for="name">设备名称<span style="color:#f00">*</span></label>
										<div class="col-xs-12 col-sm-10">
											<div class="clearfix">
												<input type="text" class="col-xs-12 col-sm-12 form-control" id="name" name="machine.name" value="${machine.get('name')}" required/>
											</div>
										</div>
									</div>
								</div>
							</div>
							<div class="form-group" style="margin:0px;">
								<div class="col-lg-6">
									<div class="form-group has-feedback">
										<label class="control-label col-xs-12 col-sm-2 no-padding-right" for="system_id">主控系统标识<span style="color:#f00">*</span></label>
										<div class="col-xs-12 col-sm-10">
											<div class="clearfix">
												<input type="text" class="col-xs-12 col-sm-12  form-control" id="system_id" name="machine.system_id" value="${machine.get('system_id')}" required/>
											</div>
										</div>
									</div>
								</div>
								<div class="col-lg-6">
									<div class="form-group has-feedback">
										<label class="control-label col-xs-12 col-sm-2 no-padding-right" for="machine_type_id">设备类型<span style="color:#f00">*</span></label>
										<div class="col-xs-12 col-sm-10">
											<div class="clearfix">
												<select id="machine_type_id"  name="machine_type_id" class="col-xs-12 col-sm-12  form-control"   data-placeholder="选择类型">
													<c:forEach items="${machineTypes}" var="machineType">
														<option value="${machineType.id}">${machineType.name}</option>
													</c:forEach>
												</select>
											</div>
										</div>
									</div>
								</div>
							</div>
							<div class="form-group" style="margin:0px;">
								<div class="col-lg-6">
									<div class="form-group has-feedback">
										<label class="control-label col-xs-12 col-sm-2 no-padding-right" for="description">设备描述<span style="color:#f00">*</span></label>
										<div class="col-xs-12 col-sm-10">
											<div class="clearfix">
												<input type="text" class="col-xs-12 col-sm-12 form-control" id="description" name="machine.description" value="${machine.get('description')}" required/>
											</div>
										</div>
									</div>
								</div>
								<div class="col-lg-6">
									<div class="form-group has-feedback">
										<label class="control-label col-xs-12 col-sm-2 no-padding-right" for="manufactor_id">设备厂商<span style="color:#f00">*</span></label>
										<div class="col-xs-12 col-sm-10">
											<div class="clearfix">
												<select  id="manufactor_id"  name="manufactor_id" class="col-xs-12 col-sm-12 form-control"   data-placeholder="选择厂商">
													<c:forEach items="${manufactors}" var="manufactor">
														<option value="${manufactor.id}">${manufactor.name}</option>
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
										<label class="control-label col-xs-12 col-sm-1 no-padding-right" for="parent_id">父级设备<span style="color:#f00">*</span></label>
										<div class="col-xs-12 col-sm-11" >
											<div class="clearfix">
												<select id="parent_id" class="select2 col-xs-12 col-sm-12  form-control"style="padding: 0px"   name="machine.parent_id">
													<option value="-1" selected>父级设备</option>
													<c:forEach var="parent" items="${parents}">
														<option value="${parent.id}">${parent.name}(${parent.terminal_id})</option>
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
										<label class="control-label col-xs-12 col-sm-1 no-padding-right" for="is_control_data">是否有监控数据<span style="color:#f00">*</span></label>
										<div class="col-xs-12 col-sm-11" >
											<div class="clearfix">
												<select id="is_control_data" class="col-xs-12 col-sm-12  form-control"style="padding: 0px"   name="machine.is_control_data" onchange="isControlData(this)">
													<option value="0" selected>否</option>
													<option value="1" >是</option>
												</select>
											</div>
										</div>
									</div>
								</div>
							</div>
							<div id="control_data_name_div">

							</div>
							<div class="form-group" style="margin:0px;">
								<div class="col-lg-12">
									<div class="form-group has-feedback">
										<label class="control-label col-xs-12 col-sm-1 no-padding-right" for="device_id">设备安装标识<span style="color:#f00">*</span></label>
										<div class="col-xs-12 col-sm-11" >
											<div class="clearfix">
												<input id="device_id" class="col-xs-12 col-sm-12  form-control"style="padding: 0px" value="${machine.get('device_id')}"   name="machine.device_id" required />
											</div>
										</div>
									</div>
								</div>
							</div>

							<div class="form-group" style="margin:0px;">
								<div class="param_parent">
									<c:forEach items="${machine_params}" var="params" varStatus="i">
										<div class="param" remove-id="${i.index}">
											<div class="col-lg-5">
												<div class="form-group has-feedback">
													<label class="control-label col-xs-12 col-sm-3 no-padding-right" for="param_name_${i.index}">参数名<span style="color:#f00">*</span></label>
													<div class="col-xs-12 col-sm-9" >
														<div class="clearfix">
															<input type="hidden" value="${params.get("id")}" id="param_id_${i.index}" name="param[${i.index}].id" />
															<input type="text" class="col-xs-12 col-sm-12 form-control" value="${params.get("param_name")}" id="param_name_${i.index}" name="param[${i.index}].param_name"  placeholder="如：开启、关闭等" required/>
														</div>
													</div>
												</div>
											</div>
											<div class="col-lg-5">
												<div class="form-group has-feedback">
													<label class="control-label col-xs-12 col-sm-3 no-padding-right" for="param_command_${i.index}">参数指令<span style="color:#f00">*</span></label>
													<div class="col-xs-12 col-sm-9" >
														<div class="clearfix">
															<input type="text" class="col-xs-12 col-sm-12 form-control" value="${params.get("param_command")}" id="param_command_${i.index}" name="param[${i.index}].param_command" placeholder="如：000001" required/>
														</div>
													</div>
												</div>
											</div>
											<div class="col-lg-2">
												<div class="form-group has-feedback" style="text-align: center;">
													<button  class="btn btn-sm btn-white btn-info btn-bold" type="button" onclick="addParam(this)" ><i class="ace-icon fa fa-plus-square blue"></i>添加</button>
													<button  class="btn btn-sm btn-white btn-warning btn-bold" type="button" onclick="delParam(this,${i.index})"><i class="ace-icon fa fa-trash-o bigger-120 orange"></i>删除</button>
												</div>
											</div>
										</div>
									</c:forEach>

								</div>
							</div>
							<div class="col-xs-6 ztree" id="items">

							</div>
						</div>
						<hr />
						<div class="form-group">
							<div class="col-sm-9 col-sm-offset-3">
								<button  class="btn btn-sm btn-primary" id="xgs-save" name="signup" value="Sign up">提交</button>
								<button  class="btn btn-sm " type="button" onclick="addParam(this)" >添加参数</button>
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
<script type="text/javascript" src="${ctxStatic}/ckfinder/ckfinder.js"></script>
<script type="text/javascript">
    function BrowseServer(inputId)
    {
        var finder = new CKFinder() ;
        finder.basePath = '../ckfinder/';  //导入CKFinder的路径
        finder.selectActionFunction = SetFileField; //设置文件被选中时的函数
        finder.selectActionData = inputId;  //接收地址的input ID
        finder.popup() ;
    }

    //文件选中时执行
    function SetFileField(fileUrl,data)
    {
        document.getElementById(data["selectActionData"]).value = fileUrl ;
    }

    function isControlData(obj) {
        if ($(obj).val()==="1"){
            var html = "";
            html += '<div class="form-group" style="margin:0px;" >';
            html += '	<div class="col-lg-6">';
            html += '		<div class="form-group has-feedback">';
            html += '			<label class="control-label col-xs-12 col-sm-2 no-padding-right" for="control_data_name">监控数据名<span style="color:#f00">*</span></label>';
            html += '			<div class="col-xs-12 col-sm-10" >';
            html += '				<div class="clearfix">';
            html += '					<input id="control_data_name" class="col-xs-12 col-sm-12  form-control"style="padding: 0px" value="${machine.get('control_data_name')}"  placeholder="如：空气、土壤温度（当为温湿度传感器时，只用填写名称，如：空气温湿度填写为空气）"  name="machine.control_data_name" required/>';
            html += '				</div>';
            html += '			</div>';
            html += '		</div>';
            html += '	</div>';
            html += '	<div class="col-lg-6">';
            html += '		<div class="form-group has-feedback">';
            html += '			<label class="control-label col-xs-12 col-sm-2 no-padding-right" for="control_data_unit">数据单位<span style="color:#f00">*</span></label>';
            html += '			<div class="col-xs-12 col-sm-10" >';
            html += '				<div class="clearfix">';
            html += '					<input id="control_data_unit" class="col-xs-12 col-sm-12  form-control"style="padding: 0px" value="${machine.get('control_data_unit')}"  placeholder="如：C°、当多单位时按顺序英文“,”隔开"  name="machine.control_data_unit"  required/>';
            html += '				</div>';
            html += '			</div>';
            html += '		</div>';
            html += '	</div>';
            html += '</div>';
            html +='<div class="form-group" style="margin:0px;">';
            html +='	<div class="col-lg-12">';
            html +='		<div class="form-group has-feedback">';
            html +='			<label class="control-label col-xs-12 col-sm-1 no-padding-right" for="machine_icon">数据图标</label>';
            html +='			<div class="col-xs-12 col-sm-11">';
            html +='				<div class="clearfix">';
            html +='					<input type="text" class="col-xs-12 col-sm-12"id="machine_icon" name="machine.machine_icon" onclick="BrowseServer(\'machine_icon\');" value="${machine.get('machine_icon')}" />';
            html +='				</div>';
            html +='			</div>';
            html +='		</div>';
            html +='	</div>';
            html +='</div>';
            $("#control_data_name_div").html(html);
            $('#validation-form').bootstrapValidator('addField', 'machine.control_data_name', {
                validators: {
                    notEmpty: {}
                }
            });
            $('#validation-form').bootstrapValidator('addField', 'machine.machine_icon', {
                validators: {
                    notEmpty: {}
                }
            });
            $('#validation-form').bootstrapValidator('addField', 'machine.control_data_unit', {
                validators: {
                    notEmpty: {}
                }
            });
        }else {
            $("#control_data_name_div").html("");
            $("#validation-form").formValidation('removeField','machine.control_data_name');
            $("#validation-form").formValidation('removeField','machine.control_data_unit');
            $("#validation-form").formValidation('removeField','machine.machine_icon');
        }
    }
    function addParam(obj) {
        var size = $(".param").length;
        var html = "";
        html+='<div class="param" remove-id="'+size+'">';
        html+='    <div class="col-lg-5">';
        html+='        <div class="form-group has-feedback">';
        html+='            <label class="control-label col-xs-12 col-sm-3 no-padding-right" for="param_name_'+size+'">参数名<span style="color:#f00">*</span></label>';
        html+='            <div class="col-xs-12 col-sm-9" >';
        html+='                <div class="clearfix">';
        html+='                    <input type="text" class="col-xs-12 col-sm-12 form-control" id="param_name_'+size+'" name="param['+size+'].param_name"  placeholder="如：开启、关闭等" required/>';
        html+='                </div>';
        html+='            </div>';
        html+='        </div>';
        html+='    </div>';
        html+='    <div class="col-lg-5">';
        html+='        <div class="form-group has-feedback">';
        html+='            <label class="control-label col-xs-12 col-sm-3 no-padding-right" for="param_command_'+size+'">参数指令<span style="color:#f00">*</span></label>';
        html+='            <div class="col-xs-12 col-sm-9" >';
        html+='                <div class="clearfix">';
        html+='                    <input type="text" class="col-xs-12 col-sm-12 form-control" id="param_command_'+size+'" name="param['+size+'].param_command" placeholder="如：000001" required/>';
        html+='                </div>';
        html+='            </div>';
        html+='        </div>';
        html+='    </div>';
        html+='    <div class="col-lg-2">';
        html+='        <div class="form-group has-feedback" style="text-align: center;">';
        html+='            <button  class="btn btn-sm btn-white btn-info btn-bold" type="button" onclick="addParam(this)" ><i class="ace-icon fa fa-plus-square blue"></i>添加</button>';
        html+='            <button  class="btn btn-sm btn-white btn-warning btn-bold" type="button" onclick="delParam(this,'+size+')"><i class="ace-icon fa fa-trash-o bigger-120 orange"></i>删除</button>';
        html+='        </div>';
        html+='    </div>';
        html+='</div>';

        $(".param_parent").append(html);

        $('#validation-form').bootstrapValidator('addField', 'param['+size+'].param_name', {
            validators: {
                notEmpty: {}
            }
        });
        $('#validation-form').bootstrapValidator('addField', 'param['+size+'].param_command', {
            validators: {
                notEmpty: {}
            }
        });
    }
    function delParam(obj,id) {
        layer.confirm('是否删除?删除后将去除此参数设置！', {icon: 3, title:'提示'}, function(index){
            debugger;
            $("#validation-form").formValidation('removeField','param['+id+'].param_name');
            $("#validation-form").formValidation('removeField','param['+id+'].param_command');
            var parent = $(obj).parents("div[remove-id="+id+"]");
            if (xgs.utils.isUndefined(parent.html())){
                return;
            }
            parent.remove();
            $(".param").each(function (i) {
                var id = $(this).attr("remove-id");
                $("#param_name_"+id).attr("name","param["+i+"].param_name");
                $("#param_command_"+id).attr("name","param["+i+"].param_command");
                var idObj =  $("#param_id_"+id);
                if (!xgs.utils.isUndefined(idObj)){
                    idObj.attr("name","param["+i+"].id")
                }
            })
            layer.close(index);
            return;
        },function(index){
            return;
        });
    }

    $(function(){
        $("#is_control_data").val("${machine.get("is_control_data")}");
        var select = $('.select2').select2({formatNoMatches:"无查询内容！"});
        if("${machine.get('machine_type_id')}"!==""){$("#machine_type_id").val("${machine.get('machine_type_id')}");}
        if("${machine.get('manufactor_id')}"!==""){$("#manufactor_id").val("${machine.get('manufactor_id')}");}
        if("${machine.get('parent_id')}"!==""){
            $("#parent_id").val("${machine.get('parent_id')}").trigger('change');
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

    isControlData("#is_control_data");
</script>
