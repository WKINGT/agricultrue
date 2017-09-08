package net.xgs.controller;

import com.jfinal.aop.Before;
import com.jfinal.ext.interceptor.POST;
import com.jfinal.kit.JsonKit;
import net.xgs.commons.annotation.Controller;
import net.xgs.commons.annotation.Inject;
import net.xgs.entity.Constants;
import net.xgs.entity.edomain.StatusEnum;
import net.xgs.exception.MethodException;
import net.xgs.interceptor.Get;
import net.xgs.interceptor.Post;
import net.xgs.model.BaseMember;
import net.xgs.services.BlockMemberService;
import net.xgs.services.MachineDataService;
import net.xgs.services.MemberBlockDataService;
import net.xgs.services.MemberService;
import net.xgs.session.RestfulSession;
import net.xgs.session.RestfulSessionWrapper;
import net.xgs.utils.MD5Utils;

import java.util.ArrayList;
import java.util.List;

@Controller(value = "/a")
public class IndexController extends BaseController {
	@Inject
	MemberService memberService;
	@Inject
	BlockMemberService blockMemberService;
	@Inject
	MemberBlockDataService memberBlockDataService;
	@Before(Post.class)
	public Object login() {
		String username = getPara(
				"username");
		String password = getPara("password");
		BaseMember member = memberService.loginAndroid(username,password);
		if (member==null){
			throwException("login.error");
		}
		if (member.getEnabled().equals(StatusEnum.PROHIBITED_USE.getValue())){
			throwException("login.disabled");
		}
		member.put("block",blockMemberService.findbByMemeber(member.getId()));
		RestfulSession session =  new RestfulSession(member.getId());
		session.setAttribute(Constants.sessionUser,member);
		RestfulSessionWrapper.getRestfulSessionManager().save(session);
		member.setLoginPwd(null);
		member.setSalt(null);
		member.setCreateBy(null);
		return member;
	}
	@Before(Post.class)
	public Object logout(){
		RestfulSession session = getRestSession();
		if (session==null){
			return "登出成功！";
		}
		RestfulSessionWrapper.getRestfulSessionManager().remove(session.getId());
		return "登出成功！";
	}
	@Before(Post.class)
	public Object changepwd(){
		String oldPwd = this.getPara("oldPwd");
		String newPwd = this.getPara("newPwd");
		BaseMember baseMember = (BaseMember) getRestSession().getAttribute(Constants.sessionUser);
		if (!MD5Utils.pwdValidate(oldPwd,baseMember.getSalt(),baseMember.getLoginPwd())){
			throwException("change.pwd.oldpwd.error");
		}
		boolean flag = memberService.changePwd(baseMember,newPwd);
		if(flag){
			setSessiontAttribute(Constants.sessionUser,baseMember);
			return "密码修改成功";
		}else{
			throwException("change.pwd.error");
		}
		return null;
	}
	@Before(Post.class)
	public Object changeAttribute(){
		String attrName = this.getPara("attrName");
		String attrValue = this.getPara("attrValue");
		List<String> list = new ArrayList<>();
		list.add("name");
		list.add("user_port");
		list.add("qr_code");
		list.add("head_portrait");
		if (!list.contains(attrName)){
			throwException("change.attr.notAllow");
		}
		BaseMember baseMember = (BaseMember) getRestSession().getAttribute(Constants.sessionUser);
	 	baseMember.set(attrName,attrValue);
		if (baseMember.update()){
			return "修改成功";
		}
		throwException("change.name.error");
		return null;
	}
	@Before(POST.class)
	public Object findBlockByMember(){
		return memberBlockDataService.findIndexDataByMember(getSessionBaseMember().getId());
	}
}