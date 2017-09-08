<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" session="false"%>
<%@ include file="../include/lib.jsp"%>
<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
		<meta charset="utf-8" />
		<title>智慧农业</title>
		<meta name="description" content="Mailbox with some customizations as described in docs" />
		<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0" />
		<!-- bootstrap & fontawesome -->
		<link rel="stylesheet" href="${ctxStatic}//assets/css/bootstrap.css" />
		<link rel="stylesheet" href="${ctxStatic}//assets/css/bootstrap-datepicker3.css" />
		<link rel="stylesheet" href="${ctxStatic}//assets/css/bootstrap-timepicker.css" />
		<link rel="stylesheet" href="${ctxStatic}//assets/css/daterangepicker.css" />
		<link rel="stylesheet" href="${ctxStatic}//assets/css/bootstrap-datetimepicker.css" />
 		<link rel="stylesheet" href="${ctxStatic}//assets/css/treegrid/jquery.treegrid.css" />
		<link rel="stylesheet" href="${ctxStatic}//assets/css/font-awesome.css" />
		<!-- page specific plugin styles -->
		<!-- text fonts -->
		<link rel="stylesheet" href="${ctxStatic}//assets/css/ace-fonts.css" />
		<!-- ace styles -->
		<link rel="stylesheet" href="${ctxStatic}//assets/css/ace.css" class="ace-main-stylesheet" id="main-ace-style" />
		<!--[if lte IE 9]>
			<link rel="stylesheet" href="${ctxStatic}//assets/css/ace-part2.css" class="ace-main-stylesheet" />
		<![endif]-->
		<!--[if lte IE 9]>
		  <link rel="stylesheet" href="${ctxStatic}//assets/css/ace-ie.css" />
		<![endif]-->
		<!-- inline styles related to this page -->
		
		<link rel="stylesheet" href="${ctxStatic}//assets/css/select2.css" />
		<!-- ace settings handler -->
		<script src="${ctxStatic}//assets/js/ace-extra.js"></script>
		<!-- HTML5shiv and Respond.js for IE8 to support HTML5 elements and media queries -->
		<!--[if lte IE 8]>
		<script src="${ctxStatic}//assets/js/html5shiv.js"></script>
		<script src="${ctxStatic}//assets/js/respond.js"></script>
		<![endif]-->
		<link rel="stylesheet" href="${ctxStatic}//css/index.css" />
		
		
		
		
		<link rel="stylesheet" type="text/css" href="${ctxStatic}//js/tree/metro.css">
		<style>
			.update-col-xs-12{
				padding:0 1px;
			}

		</style>
	</head>

	<body class="no-skin">
		<!-- #section:basics/navbar.layout -->
		<div id="navbar" class="navbar navbar-default">
			<script type="text/javascript">
				try{ace.settings.check('navbar' , 'fixed')}catch(e){}
			</script>

			<div class="navbar-container" id="navbar-container">
				<!-- #section:basics/sidebar.mobile.toggle -->
				<button type="button" class="navbar-toggle menu-toggler pull-left" id="menu-toggler" data-target="#sidebar">
					<span class="sr-only">Toggle sidebar</span>

					<span class="icon-bar"></span>

					<span class="icon-bar"></span>

					<span class="icon-bar"></span>
				</button>

				<!-- /section:basics/sidebar.mobile.toggle -->
				<div class="navbar-header pull-left">
					<!-- #section:basics/navbar.layout.brand -->
					<a href="#" class="navbar-brand">
						<small>
							<i class="fa fa-eye" aria-hidden="true"></i>
							智慧农业 -- 后台管理
						</small>
					</a>

					<!-- /section:basics/navbar.layout.brand -->

					<!-- #section:basics/navbar.toggle -->

					<!-- /section:basics/navbar.toggle -->
				</div>

				<!-- #section:basics/navbar.dropdown -->
				<div class="navbar-buttons navbar-header pull-right" role="navigation">
					<ul class="nav ace-nav">
						<!-- <li class="purple">
							<a data-toggle="dropdown" class="dropdown-toggle" href="#">
								<i class="ace-icon fa fa-bell icon-animated-bell"></i>
								<span class="badge badge-important">8</span>
							</a>

							<ul class="dropdown-menu-right dropdown-navbar navbar-pink dropdown-menu dropdown-caret dropdown-close">
								<li class="dropdown-header">
									<i class="ace-icon fa fa-exclamation-triangle"></i>
									8 条通知
								</li>

								<li class="dropdown-content">
									<ul class="dropdown-menu dropdown-navbar navbar-pink">
										<li>
											<a href="#">
												<div class="clearfix">
													<span class="pull-left">
														<i class="btn btn-xs no-hover btn-pink fa fa-comment"></i>
														评论
													</span>
													<span class="pull-right badge badge-info">+12</span>
												</div>
											</a>
										</li>

										<li>
											<a href="#">
												<div class="clearfix">
													<span class="pull-left">
														<i class="btn btn-xs no-hover btn-success fa fa-shopping-cart"></i>
														订单
													</span>
													<span class="pull-right badge badge-success">+8</span>
												</div>
											</a>
										</li>

										<li>
											<a href="#">
												<div class="clearfix">
													<span class="pull-left">
														<i class="btn btn-xs no-hover btn-info fa fa-twitter"></i>
														收藏
													</span>
													<span class="pull-right badge badge-info">+11</span>
												</div>
											</a>
										</li>
									</ul>
								</li>
							</ul>
						</li> -->

						<!-- <li class="green">
							<a data-toggle="dropdown" class="dropdown-toggle" href="#">
								<i class="ace-icon fa fa-envelope icon-animated-vertical"></i>
								<span class="badge badge-success">5</span>
							</a>

							<ul class="dropdown-menu-right dropdown-navbar dropdown-menu dropdown-caret dropdown-close">
								<li class="dropdown-header">
									<i class="ace-icon fa fa-envelope-o"></i>
									5 条评论
								</li>

								<li class="dropdown-content">
									<ul class="dropdown-menu dropdown-navbar">
										<li>
											<a href="#" class="clearfix">
												<img src="assets/avatars/avatar.png" class="msg-photo" alt="Alex's Avatar" />
												<span class="msg-body">
													<span class="msg-title">
														<span class="blue">Alex:</span>
														Ciao sociis natoque penatibus et auctor ...
													</span>

													<span class="msg-time">
														<i class="ace-icon fa fa-clock-o"></i>
														<span>a moment ago</span>
													</span>
												</span>
											</a>
										</li>

										<li>
											<a href="#" class="clearfix">
												<img src="assets/avatars/avatar3.png" class="msg-photo" alt="Susan's Avatar" />
												<span class="msg-body">
													<span class="msg-title">
														<span class="blue">Susan:</span>
														Vestibulum id ligula porta felis euismod ...
													</span>

													<span class="msg-time">
														<i class="ace-icon fa fa-clock-o"></i>
														<span>20 minutes ago</span>
													</span>
												</span>
											</a>
										</li>

										<li>
											<a href="#" class="clearfix">
												<img src="assets/avatars/avatar4.png" class="msg-photo" alt="Bob's Avatar" />
												<span class="msg-body">
													<span class="msg-title">
														<span class="blue">Bob:</span>
														Nullam quis risus eget urna mollis ornare ...
													</span>

													<span class="msg-time">
														<i class="ace-icon fa fa-clock-o"></i>
														<span>3:15 pm</span>
													</span>
												</span>
											</a>
										</li>

										<li>
											<a href="#" class="clearfix">
												<img src="assets/avatars/avatar2.png" class="msg-photo" alt="Kate's Avatar" />
												<span class="msg-body">
													<span class="msg-title">
														<span class="blue">Kate:</span>
														Ciao sociis natoque eget urna mollis ornare ...
													</span>

													<span class="msg-time">
														<i class="ace-icon fa fa-clock-o"></i>
														<span>1:33 pm</span>
													</span>
												</span>
											</a>
										</li>

										<li>
											<a href="#" class="clearfix">
												<img src="assets/avatars/avatar5.png" class="msg-photo" alt="Fred's Avatar" />
												<span class="msg-body">
													<span class="msg-title">
														<span class="blue">Fred:</span>
														Vestibulum id penatibus et auctor  ...
													</span>

													<span class="msg-time">
														<i class="ace-icon fa fa-clock-o"></i>
														<span>10:09 am</span>
													</span>
												</span>
											</a>
										</li>
									</ul>
								</li>

								<li class="dropdown-footer">
									<a href="inbox.html">
										查看所有评论
										<i class="ace-icon fa fa-arrow-right"></i>
									</a>
								</li>
							</ul>
						</li> -->
						<li class="purple dropdown-modal">
							<a data-toggle="dropdown" class="dropdown-toggle" href="#">
								<i class="ace-icon fa fa-bell icon-animated-bell"></i>
								<span class="badge badge-important">8</span>
							</a>

							<ul class="dropdown-menu-right dropdown-navbar navbar-pink dropdown-menu dropdown-caret dropdown-close">
								<li class="dropdown-header">
									<i class="ace-icon fa fa-exclamation-triangle"></i>
									8 Notifications
								</li>

								<li class="dropdown-content">
									<ul class="dropdown-menu dropdown-navbar navbar-pink">
										<li>
											<a href="#">
												<div class="clearfix">
													<span class="pull-left">
														<i class="btn btn-xs no-hover btn-pink fa fa-comment"></i>
														New Comments
													</span>
													<span class="pull-right badge badge-info">+12</span>
												</div>
											</a>
										</li>
									</ul>
								</li>

								<li class="dropdown-footer">
									<a href="#">
										See all notifications
										<i class="ace-icon fa fa-arrow-right"></i>
									</a>
								</li>
							</ul>
						</li>
						<!-- #section:basics/navbar.user_menu -->
						<li class="light-blue">
							<a data-toggle="dropdown" href="#" class="dropdown-toggle">
								<img class="nav-user-photo" src="${ctxStatic}/ace/assets/images/user.jpg" alt="${requestScope.su.name}" />
								<span class="user-info">
									<small>欢迎您,</small>${requestScope.su.name}
								</span>

								<i class="ace-icon fa fa-caret-down"></i>
							</a>

							<ul class="user-menu dropdown-menu-right dropdown-menu dropdown-yellow dropdown-caret dropdown-close">
								<li>
									<a href="javascript:;" id="changePwd">
										<i class="ace-icon fa fa-cog"></i>
										修改密码
									</a>
								</li>

								<li class="divider"></li>

								<li>
									<a href="#" id="logout">
										<i class="ace-icon fa fa-power-off"></i>
										登出
									</a>
								</li>
							</ul>
						</li>

						<!-- /section:basics/navbar.user_menu -->
					</ul>
				</div>

				<!-- /section:basics/navbar.dropdown -->
			</div><!-- /.navbar-container -->
		</div>

		<!-- /section:basics/navbar.layout -->
		<div class="main-container" id="main-container">
			<script type="text/javascript">
				try{ace.settings.check('main-container' , 'fixed')}catch(e){}
			</script>

			<!-- #section:basics/sidebar -->
			<div id="sidebar" class="sidebar                  responsive">
				<script type="text/javascript">
					try{ace.settings.check('sidebar' , 'fixed')}catch(e){}
				</script>

				<div class="sidebar-shortcuts" id="sidebar-shortcuts">
					<div class="sidebar-shortcuts-large" id="sidebar-shortcuts-large">
						<button class="btn btn-success">
							<i class="ace-icon fa fa-signal"></i>
						</button>

						<button class="btn btn-info">
							<i class="ace-icon fa fa-pencil"></i>
						</button>

						<!-- #section:basics/sidebar.layout.shortcuts -->
						<button class="btn btn-warning">
							<i class="ace-icon fa fa-users"></i>
						</button>

						<button class="btn btn-danger">
							<i class="ace-icon fa fa-cogs"></i>
						</button>

						<!-- /section:basics/sidebar.layout.shortcuts -->
					</div>

					<div class="sidebar-shortcuts-mini" id="sidebar-shortcuts-mini">
						<span class="btn btn-success"></span>

						<span class="btn btn-info"></span>

						<span class="btn btn-warning"></span>

						<span class="btn btn-danger"></span>
					</div>
				</div><!-- /.sidebar-shortcuts -->

				<ul class="nav nav-list" id="xgs_nav">
					
				</ul><!-- /.nav-list -->

				<!-- #section:basics/sidebar.layout.minimize -->
				<div class="sidebar-toggle sidebar-collapse" id="sidebar-collapse">
					<i class="ace-icon fa fa-angle-double-left" data-icon1="ace-icon fa fa-angle-double-left" data-icon2="ace-icon fa fa-angle-double-right"></i>
				</div>

				<!-- /section:basics/sidebar.layout.minimize -->
				<script type="text/javascript">
					try{ace.settings.check('sidebar' , 'collapsed')}catch(e){}
				</script>
			</div>

			<!-- /section:basics/sidebar -->
			<div class="main-content">
				<div class="main-content-inner">
					<!-- #section:basics/content.breadcrumbs -->
					<div class="breadcrumbs" id="breadcrumbs">
						<script type="text/javascript">
							try{ace.settings.check('breadcrumbs' , 'fixed')}catch(e){}
						</script>

						<ul class="breadcrumb">
							<li>
								<i class="ace-icon fa fa-home home-icon"></i>
								<a href="#">首页</a>
							</li>

						</ul><!-- /.breadcrumb -->

						<!-- /section:basics/content.searchbox -->
					</div>

					<!-- /section:basics/content.breadcrumbs -->
					<div class="page-content">
						<div class="container" >
							<c:forEach var="block" items="${blocks}" varStatus="i">
								<c:set var="machines" value="${funs:getControllerDataName(block.block_id)}"/>
								<c:if test="${i.index%2==0}">
									</div>
									<div class="row" style="display: flex;flex-flow: row wrap;justify-content: space-between;">
								</c:if>
								<c:set var="index" value="${index+1}"/>
								<div class="col-xs-6 psLine" id="${block.block_id}" legend="${machines.get("legend")}"  param="${machines.get("ids")}" titleText="${block.blockName}" echartsType="line" style="height:300px; "></div>

							</c:forEach>
						</div>
							<!-- #section:settings.box -->
						<!-- /.ace-settings-container -->

						<!-- /section:settings.box -->
						
					</div><!-- /.page-content -->
				</div>
			</div><!-- /.main-content -->

			<div class="footer">
				<div class="footer-inner">
					<!-- #section:basics/footer -->
					<div class="footer-content">
						<span class="bigger-120">
							<span class="blue bolder">智慧农业</span>
							 &copy; 2017-2018
						</span>

					<!-- 	&nbsp; &nbsp;
						<span class="action-buttons">
							<a href="#">
								<i class="ace-icon fa fa-twitter-square light-blue bigger-150"></i>
							</a>

							<a href="#">
								<i class="ace-icon fa fa-facebook-square text-primary bigger-150"></i>
							</a>

							<a href="#">
								<i class="ace-icon fa fa-rss-square orange bigger-150"></i>
							</a>
						</span> -->
					</div>

					<!-- /section:basics/footer -->
				</div>
			</div>

			<a href="#" id="btn-scroll-up" class="btn-scroll-up btn btn-sm btn-inverse">
				<i class="ace-icon fa fa-angle-double-up icon-only bigger-110"></i>
			</a>
		</div><!-- /.main-container -->

		<!-- basic scripts -->

		<!--[if !IE]> -->
		<script type="text/javascript">
			window.jQuery || document.write("<script src='${ctxStatic}/assets/js/jquery.js'>"+"<"+"/script>");
		</script>

		<!-- <![endif]-->

		<!--[if IE]>
<script type="text/javascript">
 window.jQuery || document.write("<script src='${ctxStatic}/assets/js/jquery1x.js'>"+"<"+"/script>");
</script>
<![endif]-->
		<script type="text/javascript">
			if('ontouchstart' in document.documentElement) document.write("<script src='${ctxStatic}/assets/js/jquery.mobile.custom.js'>"+"<"+"/script>");
		</script>
		<script src="${ctxStatic}/assets/js/bootstrap.js"></script>

		<!-- page specific plugin scripts -->
		<script src="${ctxStatic}/assets/js/bootstrap-tag.js"></script>
		<script src="${ctxStatic}/assets/js/jquery.hotkeys.js"></script>
		<script src="${ctxStatic}/assets/js/bootstrap-wysiwyg.js"></script>

		<!-- ace scripts -->
		<script src="${ctxStatic}/assets/js/ace/elements.scroller.js"></script>
		<script src="${ctxStatic}/assets/js/ace/elements.colorpicker.js"></script>
		<script src="${ctxStatic}/assets/js/ace/elements.fileinput.js"></script>
		<script src="${ctxStatic}/assets/js/ace/elements.typeahead.js"></script>
		<script src="${ctxStatic}/assets/js/ace/elements.wysiwyg.js"></script>
		<script src="${ctxStatic}/assets/js/ace/elements.spinner.js"></script>
		<script src="${ctxStatic}/assets/js/ace/elements.treeview.js"></script>
		<script src="${ctxStatic}/assets/js/ace/elements.wizard.js"></script>
		<script src="${ctxStatic}/assets/js/ace/elements.aside.js"></script>
		<script src="${ctxStatic}/assets/js/ace/ace.js"></script>
		<script src="${ctxStatic}/assets/js/ace/ace.ajax-content.js"></script>
		<script src="${ctxStatic}/assets/js/ace/ace.touch-drag.js"></script>
		<script src="${ctxStatic}/assets/js/ace/ace.sidebar.js"></script>
		<script src="${ctxStatic}/assets/js/ace/ace.sidebar-scroll-1.js"></script>
		<script src="${ctxStatic}/assets/js/ace/ace.submenu-hover.js"></script>
		<script src="${ctxStatic}/assets/js/ace/ace.widget-box.js"></script>
		<script src="${ctxStatic}/assets/js/ace/ace.settings.js"></script>
		<script src="${ctxStatic}/assets/js/ace/ace.settings-rtl.js"></script>
		<script src="${ctxStatic}/assets/js/ace/ace.settings-skin.js"></script>
		<script src="${ctxStatic}/assets/js/ace/ace.widget-on-reload.js"></script>
		<script src="${ctxStatic}/assets/js/ace/ace.searchbox-autocomplete.js"></script>
		<script src="${ctxStatic}/js/plugins/utils.js"></script>
		<script src="${ctxStatic}/js/plugins/nav.js"></script>


<!-- page specific plugin scripts -->
<script src="${ctxStatic}/assets/js/dataTables/jquery.dataTables.js"></script>
<script src="${ctxStatic}/assets/js/dataTables/jquery.dataTables.bootstrap.js"></script>
<script src="${ctxStatic}/assets/js/dataTables/extensions/select/dataTables.select.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/layer/layer.js"></script>



		<!-- inline scripts related to this page -->
		<script type="text/javascript">

			jQuery(function($){
				$("#xgs_nav").xgsNav({
					simple:true,
					click:function(obj){
						if(!xgs.utils.isUndefined(obj['function_id']) && $.trim(obj['function_id']) != ''){
							$.ajax({
								url : '${ctx}/index?token='+obj['function_id'],
								type : "GET",
								//cache : false, // 禁用缓存
								dataType : "html",
								success : function(result) {
                                    var inters = xgs.AssembleEcharts.intervals;
									for(var i = 0;i<inters.length;i++){
                                        clearInterval(inters[i]);
                                    }
									$(".page-content").html(result);
								},
								error : function(XMLHttpRequest, textStatus, errorThrown) {
								}
							});
						}
					},
					id:'id',
					pid:'parent_id',
					text:'menu_name',
					token:'function_id',
					data:${menu}
				});
				$("#changePwd").on('click',function(){
					layer.open({
				 		   type: 1, //page层
				 		   area: '600px',
				 		   title: '修改密码',
				 		   shade: 0.6, //遮罩透明度
				 		   moveType: 1, //拖拽风格，0是默认，1是传统拖动
				 		   shift: 5, //0-6的动画形式，-1不开启,
				 		   zIndex:100,
				 		   btn: ['确定'],
				 		   yes: function(index, layero){
				 			  var oldPwd = $('#oldPwd',layero).val();
				 			  var newPwd = $('#newPwd',layero).val();
				 			  var newPwd1= $('#newPwd2',layero).val();
				 			  if($.trim(oldPwd) =='' || $.trim(newPwd) == '' || $.trim(newPwd1) ==''){
				 				  layer.msg('不能为空',{
				 					  	icon : 5,
										skin : 'layer-ext-moon',
										time : 1500
				 				  });
				 				  return;
				 			  }
				 			  if($.trim(newPwd) != $.trim(newPwd1)){
				 				 layer.msg('两次输入的密码不一致',{
				 					  	icon : 5,
										skin : 'layer-ext-moon',
										time : 1500
				 				  });
				 				  return;
				 			  }
				 			  layer.close(index);
				 			 $.ajax({
									url : '${ctx}/changePwd',
									type : "POST",
									data:{oldPwd:oldPwd,newPwd:newPwd},
									//cache : false, // 禁用缓存
									dataType : "json",
									success : function(result) {
										if (!result.code) {
											layer.msg(result.msg || '操作成功', {
												icon : 1,
												skin : 'layer-ext-moon',
												time : 1500
											}, function(index) {
												layer.close(index);
											})
										} else {
											layer.msg(result.msg || '操作失败', {
												icon : 5,
												skin : 'layer-ext-moon',
												time : 1500
											}, function(index) {
												layer.close(index);
											})
										}
									},
									error : function(XMLHttpRequest, textStatus, errorThrown) {
									}
								});
				 		   },
				 		   content: $('#changePwdDiv')
				 		 });
					return;
				});
				$("#logout").on('click',function(){
					$.ajax({
						url : '${ctx}/logout',
						type : "GET",
						//cache : false, // 禁用缓存
						dataType : "json",
						success : function(result) {
							if (!result.code) {
								layer.msg(result.msg || '操作成功', {
									icon : 1,
									skin : 'layer-ext-moon',
									time : 1500
								}, function(index) {
									location.href = '${ctx}/login';
									layer.close(index);
								})
							} else {
								layer.msg(result.msg || '操作失败', {
									icon : 5,
									skin : 'layer-ext-moon',
									time : 1500
								}, function(index) {
									layer.close(index);
								})
							}
						},
						error : function(XMLHttpRequest, textStatus, errorThrown) {
						}
					});
				});
			});
            function renderHtml(token) {
                $.ajax({
                    url : '${ctx}/index?token='+token,
                    type : "GET",
                    //cache : false, // 禁用缓存
                    dataType : "html",
                    success : function(result) {
                        $(".page-content").html(result);
                    },
                    error : function(XMLHttpRequest, textStatus, errorThrown) {
                    }
                });
            }
		</script>

		<!-- the following scripts are used in demo only for onpage help and you don't need them -->
		<link rel="stylesheet" href="${ctxStatic}/assets/css/ace.onpage-help.css" />

		<script src="${ctxStatic}/assets/js/ace/elements.onpage-help.js"></script>
		<script src="${ctxStatic}/assets/js/ace/ace.onpage-help.js"></script>
		<script src="${ctxStatic}/canvas/echarts.min.js"></script>
		<script src="${ctxStatic}/js/plugins/echartsUtils.js"></script>

		<div style="display:none;width:90%" id="changePwdDiv">
			<div style="width:98%;padding:10px">
				<form class="form-horizontal" id="validation-form-index" method="post" role="form">
					<div class="form-group" id="validation-form-group">
						<div class="form-group" style="margin:0px;">
							<div class="col-lg-12">
								<div class="form-group has-feedback">
									<label class="control-label col-xs-12 col-sm-2 no-padding-right" for="oldPwd">原密码：<span style="color:#f00">*</span></label>
									<div class="col-xs-12 col-sm-9">
										<div class="clearfix">
											<input type="text" class="col-xs-12 col-sm-12" id="oldPwd" name="oldPwd">
										</div>
									</div>
								</div>
							</div>
						</div>
						<div class="form-group" style="margin:0px;">
							<div class="col-lg-12">
								<div class="form-group has-feedback">
									<label class="control-label col-xs-12 col-sm-2 no-padding-right" for="newPwd">新密码：<span style="color:#f00">*</span></label>
									<div class="col-xs-12 col-sm-9">
										<div class="clearfix">
											<input type="text" class="col-xs-12 col-sm-12" id="newPwd" name="newPwd">
										</div>
									</div>
								</div>
							</div>
						</div>
						<div class="form-group" style="margin:0px;">
							<div class="col-lg-12">
								<div class="form-group has-feedback">
									<label class="control-label col-xs-12 col-sm-2 no-padding-right" for="newPwd2">再输入：<span style="color:#f00">*</span></label>
									<div class="col-xs-12 col-sm-9">
										<div class="clearfix">
											<input type="text" class="col-xs-12 col-sm-12" id="newPwd2" name="newPwd2">
										</div>
									</div>
								</div>
							</div>
						</div>
					</div>
				</form>
			</div>
		</div>
		<script type="text/javascript">
            function getRandom(min, max){
                var r = Math.random() * (max - min);
                var re = Math.round(r + min);
                re = Math.max(Math.min(re, max), min)

                return re;
            }
            //图表
            var psLine = document.getElementsByClassName('psLine');
            var psLineChar =  xgs.AssembleEcharts.load(psLine);
		</script>
	</body>
</html>