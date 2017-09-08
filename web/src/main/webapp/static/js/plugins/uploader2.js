;(function($){
	var defaultOptions = {
		id:'',
		value:'',
		isImage:false,
		fileNum:xgs.utils.uploader.fileNum,
		fileSingleSizeLimit:xgs.utils.uploader.fileSingleSizeLimit
	}
	$.fn.uploader2 = function(options){
		var setting = $.extend({},defaultOptions,options);
		builderInput(this,setting);
	}
	function builderInput(container,opt){
		var input = $('<input type="hidden" class="col-xs-12 col-sm-12">');
		input.attr({
			 id:opt.id,
			 name:opt.id
		});
		if($.trim(opt.value)!=''){
			input.val(opt.value);
		}
		var div = $('<div class="wu-example input-group xgs-uploader" style="border: 1px solid #d5d5d5; height:34px;"></div>');
		var btns = $('<div class="btns"></div>');
		var i = $('<i class="icon fa fa-upload"></i>');
		var pick = $('<div class="xgs-false-select">选择文件</div>');
		var tooltips = $('<span class="input-group-addon xgs-view"  data-toggle="tooltip" title="查看"><i class="fa fa-eye bigger-110"></i></span>');
		btns.on('click',function(){
			var dom = $('#'+opt.id+"_xgs_uploader",container);
			var rt___ = $('[id^="rt_"]',container);
			var isadd = (opt.value == ''?true:false);
			if(rt___.height()<44){
				rt___.css({'top':'0px','width':'168px','height':'44px'});
				if(isadd){
					rt___.css({'left':'448px'});
				}
			}
			layer.open({
				type:1,
				offset: '300px',
				area: '1024px',
	 		    title: '搜索',
	 		    shade: 0.6, //遮罩透明度
	 		    moveType: 1, //拖拽风格，0是默认，1是传统拖动
	 		    shift: 5, //0-6的动画形式，-1不开启,
	 		    zIndex:1000,
	 		    btn: ['确定'],
	 		    yes: function(index, layero){
	 		    	var _input_value = [];
	 		    	$(dom).find('li').each(function(){
	 		    		var _val = $(this).attr('xgs-value');
	 		    		if(!xgs.utils.isUndefined(_val) && $.trim(_val) != ''){
		 		    		_input_value.push(_val);
	 		    		}
	 		    	});
	 		    	var input_value = _input_value.join(',');
	 		    	if($.trim(input_value) != ''){
	 		    		$('.xgs-false-select',container).html('文件已上传'+_input_value.length+'个');
	 		    	}else{
	 		    		$('.xgs-false-select',container).html('选择文件');
	 		    	}
	 		    	input.val(input_value);
	 		    	input.closest('form').formValidation('updateStatus', input, 'NOT_VALIDATED')
	 		    		.formValidation('validateField',input);
	 		    	
	 		 	  layer.close(index);
	 		    },
				content: $('#'+opt.id+"_xgs_uploader").parent()
			});
		});
		
		btns.append(i);
		btns.append(pick);
		div.append(btns);
		container.append(input);
		container.append(div);
		div.append(tooltips);
		var _tempc = $('<div style="display:none;"></div>');
		container.append(_tempc);
		builderContainer(_tempc,opt);
	}
	function builderContainer(container,opt){
		var uploaderContainerId = opt.id+"_xgs_uploader";
		var uploaderContainer = $('<div class="multipleuploader"></div>').attr("id",uploaderContainerId);
		var queueList = $('<div class="queueList"><div id="dndArea'+uploaderContainerId+'" class="placeholder"><div id="filePicker'+uploaderContainerId+'"></div><p>或将文件拖到这里</p></div></div>');
		var statusBarArr = [];
		statusBarArr.push('<div class="statusBar" style="display:none;">');
		statusBarArr.push('        <div class="progress">');
		statusBarArr.push('        <span class="text">0%</span>');
		statusBarArr.push('        <span class="percentage"></span>');
		statusBarArr.push('    </div><div class="info"></div>');
		statusBarArr.push('    <div class="btns">');
		statusBarArr.push('        <div id="filePicker2'+uploaderContainerId+'" class="filePicker2"></div><div class="uploadBtn">开始上传</div>');
		statusBarArr.push('    </div>');
		statusBarArr.push('</div>');
		uploaderContainer.append(queueList);
		uploaderContainer.append(statusBarArr.join(""));
		container.append(uploaderContainer);
		init(uploaderContainer,opt);
	}
	function init($wrap,opt){
		var id = $wrap.attr("id"),
        // 图片容器
        $queue = $('<ul class="filelist"></ul>')
            .appendTo( $wrap.find('.queueList') ),
        // 状态栏，包括进度和控制按钮
        $statusBar = $wrap.find('.statusBar'),
        // 文件总体选择信息。
        $info = $statusBar.find('.info'),
        // 上传按钮
        $upload = $wrap.find('.uploadBtn'),
        // 没选择文件之前的内容。
        $placeHolder = $wrap.find('.placeholder'),
        // 总体进度条
        $progress = $statusBar.find('.progress').hide(),
        // 添加的文件数量
        fileCount = 0,
        // 添加的文件总大小
        fileSize = 0,
        // 优化retina, 在retina下这个值是2
        ratio = window.devicePixelRatio || 1,
        // 缩略图大小
        thumbnailWidth = 110 * ratio,
        thumbnailHeight = 110 * ratio,
        // 可能有pedding, ready, uploading, confirm, done.
        state = 'pedding',
        // 所有文件的进度信息，key为file id
        percentages = {},
        supportTransition = (function(){
            var s = document.createElement('p').style,
                r = 'transition' in s ||
                      'WebkitTransition' in s ||
                      'MozTransition' in s ||
                      'msTransition' in s ||
                      'OTransition' in s;
            s = null;
            return r;
        })(),
        // WebUploader实例
        uploader;
		opt.uploader = uploader;
	    if ( !WebUploader.Uploader.support() ) {
	        alert( 'Web Uploader 不支持您的浏览器！如果你使用的是IE浏览器，请尝试升级 flash 播放器');
	        throw new Error( 'WebUploader does not support the browser you are using.' );
	    }
	    var xgs_accept = xgs.utils.uploader.accept.all;
	    if(opt.isImage){
	    	xgs_accept = xgs.utils.uploader.accept.image;
	    }
	    // 实例化
	    uploader = WebUploader.create({
	        pick: {
	            id: '#filePicker'+id,
	            innerHTML: '点击选择文件'
	        },
	        //dnd: '#'+id+' .queueList',
	        //paste: document.body,

	        accept: xgs_accept,

	        // swf文件路径
	        swf: 'js/Uploader.swf',
	        resize: false,
	        //disableGlobalDnd: true,

	        //chunked: true,
	        // server: 'http://webuploader.duapp.com/server/fileupload.php',
	        server: xgs.utils.uploader.url+'index',
	        fileNumLimit: opt.fileNum,
	        fileSizeLimit: opt.fileNum*opt.fileSingleSizeLimit,    // 200 M
	        fileSingleSizeLimit: opt.fileSingleSizeLimit    // 50 M
	    });

	    // 添加“添加文件”的按钮，
	    uploader.addButton({
	        id: '#filePicker2'+id,
	        innerHTML: '继续添加'
	    });

	    var xgs_edit = false;

	    if(!xgs.utils.isUndefined(opt.value) && $.trim(opt.value)!=''){
	    	addExistFile(opt.value);
	    	xgs_edit = true;
	    }

	    function createLi(file){
	    	var $li = $( '<li id="' + file.id + '">' +
                '<p class="title">' + file.name + '</p>' +
                '<p class="imgWrap"></p>'+
                '<p class="progress"><span></span></p>' +
                '</li>' ),
            $btns = $('<div class="file-panel"><span class="cancel">删除</span></div>').appendTo( $li ),
            $xgs_wrap = $li.find( 'p.imgWrap' );
            
			$li.on( 'mouseenter', function() {
	            $btns.stop().animate({height: 30});
	        });

	        $li.on( 'mouseleave', function() {
	            $btns.stop().animate({height: 0});
	        });

	        $btns.on( 'click', 'span', function() {
	            var index = $(this).index(),
	                deg;

	            switch ( index ) {
	                case 0:
	                	var _ul = $(this).closest('.filelist');
	                    try{
	                    	uploader.removeFile( file );
	                    }catch(e){
	                    	$(this).closest('li').remove();
	                    };
	                	var lis = _ul.find('li');
	                	if(lis.length == 0){
	                    	setState( 'pedding' );
	                	}
	                    return;

	                case 1:
	                    file.rotation += 90;
	                    break;

	                case 2:
	                    file.rotation -= 90;
	                    break;
	            }

	            if ( supportTransition ) {
	                deg = 'rotate(' + file.rotation + 'deg)';
	                $xgs_wrap.css({
	                    '-webkit-transform': deg,
	                    '-mos-transform': deg,
	                    '-o-transform': deg,
	                    'transform': deg
	                });
	            } else {
	                $xgs_wrap.css( 'filter', 'progid:DXImageTransform.Microsoft.BasicImage(rotation='+ (~~((file.rotation/90)%4 + 4)%4) +')');
	                // use jquery animate to rotation
	                // $({
	                //     rotation: rotation
	                // }).animate({
	                //     rotation: file.rotation
	                // }, {
	                //     easing: 'linear',
	                //     step: function( now ) {
	                //         now = now * Math.PI / 180;

	                //         var cos = Math.cos( now ),
	                //             sin = Math.sin( now );

	                //         $wrap.css( 'filter', "progid:DXImageTransform.Microsoft.Matrix(M11=" + cos + ",M12=" + (-sin) + ",M21=" + sin + ",M22=" + cos + ",SizingMethod='auto expand')");
	                //     }
	                // });
	            }


	        });
	        return $li;
	    }

	    function addExistFile(fileIds){
	    	if(!xgs.utils.isArray(fileIds)){
	    		var _ids = fileIds.split(",");
	    		for(var i=0,len=_ids.length;i<len;i++){
	    			$.post(xgs.utils.uploader.url+'detail/'+_ids[i],{},function(d){
	    				if(!d.code){
		    				var file = {
		    					id:d.data.id,
		    					name:d.data.file_name,
		    					rotation:0
		    				}
		    				var $li = createLi(file);
		    				$li.attr('size',d.data.file_size);
		    				$xgs_wrap = $li.find( 'p.imgWrap' );
		    				$li.append( '<span class="success"></span>' );
		    				viewFile(d.data,$xgs_wrap);
		        			$li.appendTo( $queue );
		        			$placeHolder.addClass( 'element-invisible' );
	            			$statusBar.show();
					        setState( 'ready' );
	    					$li.attr('xgs-value',d.data.id);
	    				}
		    		})
	    		}
	    	}
	    }

	    function viewFile(file,$wrap){
	    	if(file.file_type.indexOf('image')>-1){
	    		var src = xgs.utils.uploader.url+"image/"+file.id+"-"+thumbnailWidth+"-"+thumbnailHeight;
	    	}else{
	    		if(!xgs.utils.filePrev.hasOwnProperty(file.ext)){
                    $wrap.text( '不能预览' );
                    return;
            	}
            	src = xgs.utils.filePrev[file.file_ext];
	    	}
	    	var img = $('<img src="'+src+'">');
	    	$wrap.empty().append( img );
	    }
	    // 当有文件添加进来时执行，负责view的创建
	    function addFile( file ) {
	        var $li = createLi(file),
	            $prgress = $li.find('p.progress span'),
	            $wrap = $li.find( 'p.imgWrap' ),
	            $error = $('<p class="error"></p>'),

	            showError = function( code ) {
	                switch( code ) {
	                    case 'exceed_size':
	                        text = '文件大小超出';
	                        break;

	                    case 'interrupt':
	                        text = '上传暂停';
	                        break;

	                    default:
	                        text = '上传失败，请重试';
	                        break;
	                }

	                $error.text( text ).appendTo( $li );
	            };

	        if ( file.getStatus() === 'invalid' ) {
	            showError( file.statusText );
	        } else {
	            // @todo lazyload
	            $wrap.text( '预览中' );
	            uploader.makeThumb( file, function( error, src ) {
	                if ( error ) {
	                	if(!xgs.utils.filePrev.hasOwnProperty(file.ext)){
		                    $wrap.text( '不能预览' );
		                    return;
	                	}
	                	src = xgs.utils.filePrev[file.ext];
	                }

	                var img = $('<img src="'+src+'">');
	                $wrap.empty().append( img );
	            }, thumbnailWidth, thumbnailHeight );

	            percentages[ file.id ] = [ file.size, 0 ];
	            file.rotation = 0;
	        }

	        file.on('statuschange', function( cur, prev ) {
	            if ( prev === 'progress' ) {
	                $prgress.hide().width(0);
	            } else if ( prev === 'queued' ) {
	                //$li.off( 'mouseenter mouseleave' );
	                //$btns.remove();
	            }

	            // 成功
	            if ( cur === 'error' || cur === 'invalid' ) {
	                console.log( file.statusText );
	                showError( file.statusText );
	                percentages[ file.id ][ 1 ] = 1;
	            } else if ( cur === 'interrupt' ) {
	                showError( 'interrupt' );
	            } else if ( cur === 'queued' ) {
	                percentages[ file.id ][ 1 ] = 0;
	            } else if ( cur === 'progress' ) {
	                $error.remove();
	                $prgress.css('display', 'block');
	            } else if ( cur === 'complete' ) {
	                $li.append( '<span class="success"></span>' );
	            }

	            $li.removeClass( 'state-' + prev ).addClass( 'state-' + cur );
	        });

	        

	        $li.appendTo( $queue );
	    }

	    // 负责view的销毁
	    function removeFile( file ) {
	        var $li = $('#'+file.id);
	        delete percentages[ file.id ];
	        updateTotalProgress();
	        $li.off().find('.file-panel').off().end().remove();
	    }

	    function updateTotalProgress() {
	        var loaded = 0,
	            total = 0,
	            spans = $progress.children(),
	            percent;

	        $.each( percentages, function( k, v ) {
	            total += v[ 0 ];
	            loaded += v[ 0 ] * v[ 1 ];
	        } );

	        percent = total ? loaded / total : 0;

	        spans.eq( 0 ).text( Math.round( percent * 100 ) + '%' );
	        spans.eq( 1 ).css( 'width', Math.round( percent * 100 ) + '%' );
	        updateStatus();
	    }

	    function updateStatus() {
	        var text = '', stats;
	        if ( state === 'ready' ) {
	            text = '选中' + fileCount + '个文件，共' +
	                    WebUploader.formatSize( fileSize ) + '。';
	        } else if ( state === 'confirm' ) {
	            stats = uploader.getStats();
	            if ( stats.uploadFailNum ) {
	                text = '已成功上传' + stats.successNum+ '个文件，'+
	                    stats.uploadFailNum + '个文件上传失败'
	            }

	        } else {
	            stats = uploader.getStats();
	            text = '共' + fileCount + '个文件（' +
	                    WebUploader.formatSize( fileSize )  +
	                    '），已上传' + stats.successNum + '个文件';

	            if ( stats.uploadFailNum ) {
	                text += '，失败' + stats.uploadFailNum + '个文件';
	            }
	        }

	        $info.html( text );
	    }

	    function setState( val ) {
	        var file, stats;

	        if ( val === state ) {
	            return;
	        }

	        $upload.removeClass( 'state-' + state );
	        $upload.addClass( 'state-' + val );
	        state = val;

	        switch ( state ) {
	            case 'pedding':
	                $placeHolder.removeClass( 'element-invisible' );
	                $queue.parent().removeClass('filled');
	                $queue.hide();
	                $statusBar.addClass( 'element-invisible' );
	                uploader.refresh();
	                break;

	            case 'ready':
	                $placeHolder.addClass( 'element-invisible' );
	                $( '#filePicker2'+id ).removeClass( 'element-invisible');
	                $queue.parent().addClass('filled');
	                $queue.show();
	                $statusBar.removeClass('element-invisible');
	                uploader.refresh();
	                break;

	            case 'uploading':
	                $( '#filePicker2'+id ).addClass( 'element-invisible' );
	                $progress.show();
	                $upload.text( '暂停上传' );
	                break;

	            case 'paused':
	                $progress.show();
	                $upload.text( '继续上传' );
	                break;

	            case 'confirm':
	                $progress.hide();
	                $upload.text( '开始上传' ).addClass( 'disabled' );

	                stats = uploader.getStats();
	                if ( stats.successNum && !stats.uploadFailNum ) {
	                    setState( 'finish' );
	                    return;
	                }
	                break;
	            case 'finish':
	                stats = uploader.getStats();
	                $( '#filePicker2'+id ).removeClass( 'element-invisible');
	                $upload.removeClass( 'disabled' );
	                if ( stats.successNum ) {
	                    //alert( '上传成功' );
	                } else {
	                    // 没有成功的图片，重设
	                    state = 'done';
	                    //location.reload();
	                }
	                break;
	        }

	        updateStatus();
	    }

	    uploader.onUploadProgress = function( file, percentage ) {
	        var $li = $('#'+file.id),
	            $percent = $li.find('.progress span');

	        $percent.css( 'width', percentage * 100 + '%' );
	        percentages[ file.id ][ 1 ] = percentage;
	        updateTotalProgress();
	    };

	    uploader.onFileQueued = function( file ) {
	        fileCount++;
	        fileSize += file.size;

	        if ( fileCount === 1 ) {
	            $placeHolder.addClass( 'element-invisible' );
	            $statusBar.show();
	        }

	        addFile( file );
	        setState( 'ready' );
	        updateTotalProgress();
	    };
	    uploader.onUploadSuccess = function(file,data){
	    	$('#'+id+" #"+file.id).attr('xgs-value',data.id).attr('size',data.file_size);
	    }
	    uploader.onFileDequeued = function( file ) {
	        fileCount--;
	        fileSize -= file.size;

	        if ( !fileCount && !xgs_edit) {
	            setState( 'pedding' );
	        }

	        removeFile( file );
	        updateTotalProgress();

	    };

	    uploader.on( 'all', function( type ) {
	        var stats;
	        switch( type ) {
	            case 'uploadFinished':
	                setState( 'confirm' );
	                break;

	            case 'startUpload':
	                setState( 'uploading' );
	                break;

	            case 'stopUpload':
	                setState( 'paused' );
	                break;

	        }
	    });

	    uploader.onError = function( type ,size,file) {
	    	if (type=="Q_TYPE_DENIED"){
	        	layer.msg('请上传JPG、PNG格式文件', {
        				icon: 5,
        				time: 1500
        			}); 
	        	return;
	        }else if(type=="F_EXCEED_SIZE"){
	        	layer.msg('文件大小不能超过'+xgs.utils.uploader.sizeFormat(size, file.size), 
        			{
        				icon: 5,
        				time: 1500
        			}
	        	);
	        	return;
	        }else if(type=="Q_EXCEED_FILE_TYPE"){
                layer.msg('请上传'+xgs_accept.accept.extensions+'格式文件', {//_accept.join("、")
                    icon: 5,
                    time: 1500
                });
                return;
            }
	    	layer.msg('添加文件时出错');
	    };

	    $upload.on('click', function() {
	        if ( $(this).hasClass( 'disabled' ) ) {
	            return false;
	        }

	        if ( state === 'ready' ) {
	            uploader.upload();
	        } else if ( state === 'paused' ) {
	            uploader.upload();
	        } else if ( state === 'uploading' ) {
	            uploader.stop();
	        }
	    });

	    $info.on( 'click', '.retry', function() {
	        uploader.retry();
	    } );

	    $info.on( 'click', '.ignore', function() {
	        alert( 'todo' );
	    } );

	    $upload.addClass( 'state-' + state );
	    updateTotalProgress();
	}
})(jQuery);