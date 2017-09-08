$(document).ready(function() {
	// 点击切换
	$('.m_login_title h2').click(function() {
		if($(this).attr('class') == 'account' ){
			$('#m_main').css('display', 'block');
			$('.m_main2').css('display', 'none');
		}else{
			$('#m_main').css('display', 'none');
			$('.m_main2').css('display', 'block');
		}
	});
	// 打开关闭修改密码窗口
	$('.forget_pwd').click(function() {
		$('.m_masking').css('display', 'block');
		
	});
	$('.m_change_close').click(function() {
		$('.m_masking').css('display', 'none');
	});

	// 左侧
	$('.m_state ul li a').mouseover(function() {
		$(this).addClass('on');
		$(this).find('em').css({display: 'block'});
		$(this).find('span').css({display: 'block'});
	});
	$('.m_state ul li a').mouseout(function() {
		$(this).removeClass('on');
		$(this).find('em').css({display: 'none'});
		$(this).find('span').css({display: 'none'});
	});


	//获取光标变色
	// $('.username input').focus(function() {
	// 	$(this).siblings('i').addClass('on')
	// });
	// $('.password input').focus(function() {
	// 	$(this).siblings('i').addClass('on')
	// });
	// $('.code_input input').focus(function() {
	// 	$(this).siblings('i').addClass('on')
	// });
	$('.m_login_info .pic').each(function() {
		$(this).focus(function() {
			$(this).siblings('i').addClass('on')
		});
		$(this).blur(function() {
			$(this).siblings('i').removeClass('on')
		});
	});
});