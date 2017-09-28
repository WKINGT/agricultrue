package net.xgs.controller.core;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Enhancer;
import com.jfinal.kit.JsonKit;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import net.xgs.commons.annotation.Controller;
import net.xgs.commons.annotation.Inject;
import net.xgs.commons.annotation.Log;
import net.xgs.commons.plugin.ioc.InjectUtils;
import net.xgs.commons.utils.DateUtils;
import net.xgs.commons.utils.StrUtils;
import net.xgs.entity.Constants;
import net.xgs.entity.InitData;
import net.xgs.entity.Msg;
import net.xgs.entity.edomain.BooleanEnum;
import net.xgs.entity.edomain.IsShowEnum;
import net.xgs.entity.edomain.StatusEnum;
import net.xgs.entity.webvo.FunctionVO;
import net.xgs.entity.webvo.FunctionWebVO;
import net.xgs.entity.webvo.MenuVO;
import net.xgs.model.*;
import net.xgs.services.*;
import net.xgs.utils.*;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller(value = "/",viewPath = "/WEB-INF/jsp/")
public class IndexController extends JfinalController {
	@Inject
	public MenuFunctionService menuFunctionService;
	@Inject
	public MemberService memberService;
	@Inject
	public FunctionService functionService;
	@Inject
	public RoleService roleService;
	@Inject
	public MenuService menuService;
	@Inject
	public BlockMemberService blockMemberService;
	@Inject
	public MachineDataService machineDataService;
	@Inject
	public MachineBlockService blockService;
	protected BaseMember su;

	/** @pdOid a09859ed-b913-4111-a8b8-bda94fc23301 */
	private void init() throws Exception {
		String ctl, act,param;
		SysFunctions function;//
		if (Constants.functionIsCache) {
			function = this.getFunction();
		} else {
			function = functionService.findByRoleId(su.getId(),token);
		}
		if(function == null){
			this.render("500.html");
			return;
		}
		ctl = function.getStr(Constants.cName);
		act = function.getStr(Constants.aName);
		param = function.getStr(Constants.fparam);
		Object object = ObjectUtils.getCtl(ctl);
		BaseController bc = (BaseController) object;
		bc.setController(this, ctl, act,param,function);
		bc.controller.setAttr(Constants.token,token);
		try {
			if (Constants.defaultParentId.equals(function.getParentId())){
				List<SysFunctions> result = new ArrayList<>();
				if (Constants.functionIsCache){
					List<SysFunctions> functions = su.get(Constants.userListFunctions);
					for (SysFunctions sysFunctions:functions){
						if (function.getId().equals(sysFunctions.getParentId())){
							result.add(sysFunctions);
						}
					}
				}else {
					List<String> functionIds =functionService.findFunctionIdsByRole(su.getId());
					List<SysFunctions> sysFunctions =  InitData.instance().funcs();
					for (SysFunctions functions :sysFunctions){
						if (token.equals(functions.getParentId())&&functionIds.contains(functions.getId())){
							result.add(functions);
						}
					}
				}
				setAttr(Constants.functions, JSONObject.toJSONString(FunctionWebVO.po2WebVO(result)).replace("\"itemclick\"","itemclick"));
			}
			setAttr(Constants.listToken,function.getParentId());
			InjectUtils.inject(object);
			ReflectionUtils.invokeMethod(object, act, null, null);
		} catch (Exception e) {
			this.render("404.html");
			e.printStackTrace();
		}
	}



	/** @pdOid 9debacd9-7aef-4c52-9ba8-12d40308c098 */
	public String token;

	/**
	 * @throws Exception
	 * @pdOid 6b913f40-53b5-4d97-b15a-f78638016ea8
	 */
	public void index() throws Exception {
		token = getPara(Constants.token);
		su = (BaseMember) getSession().getAttribute(Constants.sessionUser);
		if (su != null) {
			if (token == null) {
				List<SysMenu> menu;
				if (false) {//
					menu = menuService.tree();
				}else{
					menu = su.get(Constants.userListMenus);
				}
				List<ViewBlockMember> blocks = blockMemberService.findbByMemeber(getSessionUserId());
				setAttr("blocks",blocks);
				this.setAttr("menu", JsonKit.toJson(menu));
				this.setAttr("su", su);
				this.renderJsp("admin/index.jsp");
				return;
			}
			this.init();
		} else {
			login();
		}
	}

	public void captcha(){
		this.renderCaptcha();
	}
	@Log("web登陆--controller")
	public void login() {
		if (isGet()) {
			this.renderJsp("admin/login.jsp");
		} else {
			Msg msg = new Msg();
			try {
				String account = getPara("userCode");
				String pwd = getPara("userPwd");

				boolean validate = this.validateCaptcha("verifycode");
				if (!validate) {
					msg.setCode(1).setMsg("验证码错误");
				} else {
					 su = memberService.findByAccountPwd(account, pwd);
					if (su == null) {
						msg.setCode(1).setMsg("用户名或密码错误");
					}else if (StatusEnum.PROHIBITED_USE.getValue().equals(su.getEnabled())){
						msg.setCode(1).setMsg("账户已禁用错误，请联系管理员开通！");
					} else {
						this.filters();
						getSession().setAttribute(Constants.sessionUser, su);
					}
				}
			} catch (Exception e) {
				msg.setCode(1).setMsg(e.getMessage());
				e.printStackTrace();
			}

			this.renderJson(msg);
		}
	}

	private SysFunctions getFunction() {
		if (su != null) {
			Map<String, SysFunctions> funcs = su.get(Constants.userFunctions);
			if (funcs != null && funcs.containsKey(token)) {
				return funcs.get(token);
			}
		}
		return null;
	}

	/**
	 * 初始化用户权限输
	 */
	private void filters() {
		List<MenuVO> result = new ArrayList<>();
		if (su.get(Constants.userRoleIds) != null) {
			//获取当前登录用的权限关联列表
			List<Record> record =	su.get(Constants.userAuthority);
			List<String> mids = ObjectUtils.getMethodValue(record,"getStr","menu_id");
			List<String> fids = ObjectUtils.getMethodValue(record,"getStr","function_id");
			List<MenuVO> menuVOS = InitData.instance().menus(mids,fids);
			List<SysMenu> sysMenus = InitData.instance().menus(mids);
			List<SysFunctions> sysFunctions = InitData.instance().funcs(fids);
			Map<String,SysFunctions> sysFunctionsMap = InitData.instance().getFuncs(sysFunctions);
			List<FunctionVO> functionVOS = InitData.instance().assembleFunction(Constants.defaultParentId,sysFunctionsMap,sysFunctions);
			Map<String,SysMenu> sysMenuMap = InitData.instance().getMids(sysMenus);
			Map<String,MenuVO> menuVOMap = InitData.instance().getMvoids(menuVOS);
			su.put(Constants.mids,mids);
			su.put(Constants.fids,fids);
			su.put(Constants.menuListVos,menuVOS);
			su.put(Constants.functionListVos,functionVOS);
			su.put(Constants.userListMenus,sysMenus);
			su.put(Constants.userListFunctions,sysFunctions);
			su.put(Constants.userFunctions,sysFunctionsMap);
			su.put(Constants.userMenus,sysMenuMap);
			su.put(Constants.menuVos,menuVOMap);
		}
	}

	public void changePwd(){
		Msg msg = new Msg();
		try {
			String oldPwd = this.getPara("oldPwd");
			String newPwd = this.getPara("newPwd");
			BaseMember baseMember = getSessionSysUser();
			if (!MD5Utils.pwdValidate(oldPwd,baseMember.getSalt(),baseMember.getLoginPwd())){
				msg.setCode(1).setMsg("原密码错误！");
				this.renderHtml(JsonKit.toJson(msg));
				return;
			}
			boolean flag = memberService.changePwd(baseMember,newPwd);
			if(flag){
				msg.setMsg("密码修改成功");
			}else{
				msg.setCode(1).setMsg("修改密码失败");
			}
		} catch (Exception e) {
			msg.setCode(1);
			if(Constants.debugger){
				msg.setMsg(e.getMessage());
			}else{
				msg.setMsg("修改密码失败");
			}
		}
		this.renderHtml(JsonKit.toJson(msg));
	}

	public void logout() {
		getSession().removeAttribute(Constants.sessionUser);
		Msg msg = new Msg();
		msg.setMsg("登出成功");
		this.renderJson(msg);
	}

	public void echartData(){
		String [] ids = getPara("ids").split(",");
		renderHtml(JSONObject.toJSONString(machineDataService.findByMachine(ids)));
	}

	public void qrCode(){
		String ipAddress = IPUtils.getRemoteLoginUserIp(getRequest());
		String content = ipAddress+System.currentTimeMillis()+ SystemUUIDUtils.getUUID();
		String sessionValue = MD5Utils.pwdRule(content,MD5Utils.salt());
		content = PropKit.use("cfg.properties").get("http.qrCode.login")+sessionValue;
		setSessionAttr(Constants.qrCodeValue,sessionValue);
		renderQrCode(content,180,180);
	}

	public void checkQrCode(){
		String sessionValue = getSessionAttr(Constants.qrCodeValue);
		Msg msg = new Msg();
		if (StringUtils.isNotBlank(sessionValue)){
			su = memberService.qrCode(sessionValue);
			if (su !=null){
				this.filters();
				getSession().setAttribute(Constants.sessionUser, su);
				renderJson(msg.setMsg("登录成功！"));
				return;
			}
		}
		renderJson(msg.setMsg("登录失败").setCode(1));
	}
	public void findMachineByBlockId(){
		String block = getPara("blockId");
		Msg msg = Constants.editSuccess;
		msg.setData(blockService.findByTreeSelectByBlock(block));
		renderJson(msg);
	}

	/**
	 * 前端开放方法与类
	 */
	public void front(){
		String cltPath = getPara("cltPath");
		try {
			String actPath = getPara("actPath");
			Object object = ObjectUtils.getFrontCtl(cltPath);
			BaseController bc = (BaseController) object;
			bc.setController(this, cltPath, actPath);
			bc.controller.setAttr(Constants.token,token);
			InjectUtils.inject(object);
			ReflectionUtils.invokeMethod(object, actPath, null, null);
		} catch (Exception e) {
			this.render("404.html");
			e.printStackTrace();
		}

	}
}