package net.xgs.controller.system;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.plugin.activerecord.Page;
import net.xgs.commons.annotation.Controller;
import net.xgs.commons.annotation.Inject;
import net.xgs.controller.core.BaseController;
import net.xgs.entity.Constants;
import net.xgs.model.BaseMember;
import net.xgs.model.BaseOrg;
import net.xgs.model.SysRole;
import net.xgs.services.*;
import net.xgs.utils.ObjectUtils;
import org.apache.commons.lang.StringUtils;

import java.util.List;

/**
 * Created by duai on 2017-07-12.
 */
@Controller
public class UserController extends BaseController {
    @Inject
    MemberService memberService;
    @Inject
    RoleService roleService;
    @Inject
    OrgService orgService;
    @Inject
    BlockService blockService;

    public void index(){
        if (isGet){
            renderJsp("admin/user/list.jsp");
        }else {
            Page<BaseMember> page = memberService.findByPass(pageNo,pageSize,filterBuilder,controller.getOrg().getPass());
            renderHtml(JSONObject.toJSONString(page));
        }
    }
    public void save(){
        if (isGet){
            String userId = controller.getPara("id");
            setAttr("roles",roleService.findByCreate(controller.getSessionUserId()));
            if (StringUtils.isNotBlank(userId)){
                BaseMember baseMember = memberService.findById(userId);
                List<SysRole> sysRole =  roleService.findByMemberId(baseMember.getId());
                List<String> sysRoleIds = ObjectUtils.getMethodValue(sysRole,"getid");
                List<String> blockIds = ObjectUtils.getMethodValue(blockService.findBlockNameByMemberId(baseMember.getId()),"getid");
                setAttr("checkedRoles",JSONObject.toJSONString(sysRoleIds));
                setAttr("userInfo",baseMember);
                setAttr("checkedBlocks",JSONObject.toJSONString(blockIds));
                setAttr("name","修改");
            }else {
                setAttr("name","添加");
            }
            List<BaseOrg> list = orgService.findOrgAllByUser(controller.getOrg(),false);
            setAttr("rootId",orgService.getRootId(list));
            setAttr("data",JSONObject.toJSONString(list));
            setAttr("blocks",blockService.findBlockNameByMemberId(getSessionUserId()));
            renderJsp("admin/user/update.jsp");
        }else {
            String optId = getSessionUserId();
            if (optId.equals(baseMember.getId())){
                msg = Constants.editFail;
                msg.setMsg("不能编辑自己！");
                renderHtml(JSONObject.toJSONString(msg));
            }
            BaseMember baseMember = controller.getModel(BaseMember.class,"userInfo");
            baseMember.setHeadPortrait(getParam("head_portrait"));
            String [] strings = controller.getParaValues("roleIds");
            String [] blockIds = controller.getParaValues("blockIds");
            Boolean result = memberService.save(baseMember,strings,optId,controller.getOrgId(),blockIds);
            if (result){
                renderHtml(JSONObject.toJSONString(Constants.editSuccess));
               return;
            }
            renderHtml(JSONObject.toJSONString(Constants.editFail));
        }
    }
    public void update(){
        save();
    }
    public void delete(){
        String [] ids =   getParam("ids").split(",");;
        Boolean result = memberService.deleteById(ids);
        if (result){
            msg = Constants.deleteSuccess;
        }else {
            msg = Constants.deleteFail;
        }
        renderHtml(JSONObject.toJSONString(msg));
    }
}
