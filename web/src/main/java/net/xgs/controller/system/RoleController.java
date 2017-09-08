package net.xgs.controller.system;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.kit.JsonKit;
import com.jfinal.plugin.activerecord.Record;
import net.xgs.commons.annotation.Controller;
import net.xgs.commons.annotation.Inject;
import net.xgs.controller.core.BaseController;
import net.xgs.entity.Constants;
import net.xgs.entity.InitData;
import net.xgs.entity.RolesEntity;
import net.xgs.entity.webvo.FunctionVO;
import net.xgs.entity.webvo.MenuVO;
import net.xgs.model.BaseMember;
import net.xgs.model.SysFunctions;
import net.xgs.model.SysMenu;
import net.xgs.model.SysRole;
import net.xgs.services.FunctionService;
import net.xgs.services.MenuFunctionService;
import net.xgs.services.RoleService;
import net.xgs.utils.ObjectUtils;
import net.xgs.utils.RoleUtils;
import org.apache.commons.lang.StringUtils;

import java.util.*;

/**
 * Created by duai on 2017-07-14.
 */
@Controller
public class RoleController extends BaseController {
    @Inject
    RoleService roleService;
    @Inject
    public FunctionService functionService;
    @Inject
    public MenuFunctionService menuFunctionService;
    public void index(){
        if (isGet){
            renderJsp("admin/role/list.jsp");
        }else {
            renderHtml(JSONObject.toJSONString(roleService.findByCreate(pageNo,pageSize,filterBuilder,controller.getSessionUserId())));
        }
    }
    public void update(){
        save();
    }
    public void save(){
        if (isGet){
            String roleId = controller.getPara("id");
            if (StringUtils.isNotBlank(roleId)){
                controller.setAttr(Constants.updateParam,"修改");
                controller.setAttr("role", roleService.findById(roleId));
            }else {
                controller.setAttr(Constants.updateParam,"添加");
            }
            renderJsp("admin/role/update.jsp");
        }else {
            SysRole sysRole = controller.getModel(SysRole.class,"role");
            Boolean result = roleService.save(sysRole,getSessionUserId());
            if (result){
                renderHtml(JSONObject.toJSONString(Constants.editSuccess));
            }else {
                renderHtml(JSONObject.toJSONString(Constants.editFail));
            }
        }
    }


    public void delete(){
        String [] ids =  getParam("ids").split(",");
        Boolean result = roleService.deleteById(ids);
        if (result){
            msg = Constants.deleteSuccess;
        }else {
            msg = Constants.deleteFail;
        }
        renderHtml(JSONObject.toJSONString(msg));
    }

    public void auth(){
        if (isGet){
            String roleId = controller.getPara("id");
            controller.setAttr("roleId",roleId);
            BaseMember baseMember = controller.getSessionSysUser();
            List<SysMenu> sysMenus = new ArrayList<>();
            List<FunctionVO> functionVOS = new ArrayList<>();
            if (Constants.functionIsCache){
                sysMenus = baseMember.get(Constants.userListMenus);
                functionVOS = baseMember.get(Constants.functionListVos);
            }else {
                List<Record> records =functionService.findMenuFunctionByUser(baseMember.getId());
                List<String> mids = menuFunctionService.findMenuIdByRecords(records);
                List<String> fids =  menuFunctionService.findFunctionIdByRecords(records);
                List<SysFunctions> sysFunctions = InitData.instance().funcs(fids);
                Map<String,SysFunctions> sysFunctionsMap = InitData.instance().getFuncs(sysFunctions);
                functionVOS = InitData.instance().assembleFunction(Constants.defaultParentId,sysFunctionsMap,sysFunctions);
                sysMenus = InitData.instance().menus(mids);
            }
            List<Record> records = menuFunctionService.findByUser(Arrays.asList(roleId));
            controller.setAttr("menu", RoleUtils.roleMenuData(sysMenus,functionVOS,
                   menuFunctionService.findFunctionIdByRecords(records),
                   menuFunctionService.findMenuIdByRecords(records)));
            renderJsp("admin/role/treegrid.jsp");
        }else {
            String roleId = controller.getPara("roleId");
            JSONArray jsonArray = JSONArray.parseArray(controller.getPara("menu"));
            Boolean result = roleService.auth(jsonArray,roleId);
            if (result){
                msg.setMsg("授权成功！");
            }else {
                msg.setMsg("授权失败！");
                msg.setCode(1);
            }
            renderHtml(JSONObject.toJSONString(msg));
        }
    }
}
