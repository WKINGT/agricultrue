;(function($){
	'use strict';
		var d_options = {
				isImage:true,
				id : '',
				value:'',
            allowType:''

		}
		$.fn.uploader = function(_options){
			builder(this,$.extend({},d_options,_options));
		}
		
		function builder(obj,options){
			var input = $('<input type="hidden" class="col-xs-12 col-sm-12">');
			input.attr({
				 id:options.id,
				 name:options.id
			});
			input.attr('data-fv-field',options.id)
			if($.trim(options.value)!=''){
				input.val(options.value);
			}
			var div = $('<div class="wu-example input-group xgs-uploader" style="border: 1px solid #d5d5d5; height:34px;"></div>');
			var btns = $('<div class="btns"></div>');
			var i = $('<i class="icon fa fa-upload"></i>');
			var pickId = options.id +"picker";
			var pick = $('<div>选择文件</div>').attr('id',pickId);
			var tooltips = $('<span class="input-group-addon xgs-view"  data-toggle="tooltip" title="查看"><i class="fa fa-eye bigger-110"></i></span>');
			tooltips.on('mouseover',function(){
				//alert('view');
			}).on('mouseout',function(){
				//alert('colse');
			}).on('click',function(){
				if($.trim(input.val()) == '') return;
				if(options.isImage){
					layer.open({
						type:1,
						area: '450px',
						offset: '300px',
						content: '<img src="'+xgs.utils.uploader.url+'image/'+input.val()+'--400"/>'
					});
				}else{
					window.open(xgs.utils.uploader.url+'downloadWholeFile/'+input.val());
				}
			});
			btns.append(i);
			btns.append(pick);
			div.append(btns);
			obj.append(input);
			obj.append(div);
			div.append(tooltips);
			signleUploader({
				pick:pickId,
				input:input,
				options:options
			});
			//$("[data-toggle='tooltip']").tooltip();
		}
		
		function signleUploader(options){
			
			var _accept = options.options.allowType;
			if(options.isImage){
				_accept = xgs.utils.uploader.accept.image;
			}
			var uploader = WebUploader.create({
			    // swf文件路径
			    swf: 'js/Uploader.swf',
			    auto: true,
			    fileSingleSizeLimit:xgs.utils.uploader.fileSingleSizeLimit,
			    // 文件接收服务端。
			    server: xgs.utils.uploader.url,
			    multiple:true,
			    // 选择文件的按钮。可选。
			    // 内部根据当前运行是创建，可能是input元素，也可能是flash.
			    pick: {
			    	id:'#'+options.pick,
			    	multiple:false
			    },
			    // 不压缩image, 默认如果是jpeg，文件上传前会压缩一把再上传！
			    resize: false,
			    accept: _accept
			});
			uploader.on("error",function (type,size,file){
		        if (type=="Q_TYPE_DENIED"){
		        	layer.msg('请上传JPG、PNG格式文件', {
	        				icon: 5,
	        				time: 1500
	        			}); 
		        }else if(type=="F_EXCEED_SIZE"){
		        	layer.msg('文件大小不能超过'+xgs.utils.uploader.sizeFormat(size, file.size), 
	        			{
	        				icon: 5,
	        				time: 1500
	        			}
		        	); 
		        }else if(type=="Q_EXCEED_FILE_TYPE"){
                    layer.msg('请上传'+_accept.extensions+'格式文件', {
                        icon: 5,
                        time: 1500
                    });
                }
		    });
			uploader.on( 'uploadProgress', function( file, percentage ) {
			    var $li = options.input.parent().find('.xgs-uploader').find(".webuploader-pick"),
			        $percent = $li.find('.progress .progress-bar');

			    // 避免重复创建
			    if ( !$percent.length ) {
			        $percent = $('<div class="progress progress-striped active">' +
			          '<div class="progress-bar" role="progressbar" style="width: 0%">' +
			          '</div>' +
			        '</div>').appendTo( $li ).find('.progress-bar');
			    }

			   // $li.find('p.state').text('上传中');

			    $percent.css( 'width', percentage * 100 + '%' );
			});
			uploader.on( 'uploadSuccess', function( file,obj ) {
				options.input.val(obj.id);
				options.input.closest('form').formValidation('updateStatus', options.input, 'NOT_VALIDATED')
			       .formValidation('validateField', options.input);
				var c = options.input.parent().find('.xgs-uploader').find(".webuploader-pick").text('文件:'+file.name+'已上传');
			});

			uploader.on( 'uploadError', function( file ) {
				
				var c = options.input.parent().find('.xgs-uploader').find(".webuploader-pick").text('文件:'+file.name+'上传时出现错误');
			});

			uploader.on( 'uploadComplete', function( file ) {
				options.input.parent().find('.xgs-uploader').find(".webuploader-pick").find('.progress').fadeOut();
			});
		}
	})(jQuery);