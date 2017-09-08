package net.xgs.controller.system;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.plugin.activerecord.Page;
import net.xgs.commons.annotation.Controller;
import net.xgs.commons.annotation.Inject;
import net.xgs.controller.core.BaseController;
import net.xgs.entity.Constants;
import net.xgs.model.BaseOrg;
import net.xgs.services.OrgService;
import org.apache.commons.lang.StringUtils;

import java.util.List;

/**
 * Created by duai on 2017-07-20.
 */
@Controller
public class OrgController extends BaseController {
    @Inject
    OrgService orgService;
    public void index(){
        List<BaseOrg> list = orgService.findOrgAllByUser(controller.getOrg(),false);
        controller.setAttr("data",JSONObject.toJSONString(list));
        controller.setAttr("rootId",orgService.getRootId(list));
        renderJsp("admin/org/list.jsp");
    }

    public void save(){
        if (isGet){
            String orgId = getParam("id");
            if (StringUtils.isNotBlank(orgId)){
                setAttr("org",orgService.getById(orgId));
                setAttr("name","修改");
            }else {
                setAttr("name","添加");
            }
            List<BaseOrg> list = orgService.findOrgAllByUser(controller.getOrg(),false);
            controller.setAttr("rootId",orgService.getRootId(list));
            controller.setAttr("data",JSONObject.toJSONString(list));
            renderJsp("admin/org/update.jsp");
        }else {
            BaseOrg org  =  getModel(BaseOrg.class,"org");
            boolean result = orgService.saveOrg(org,getSessionUserId());
            if (result){
                msg = Constants.editSuccess;
            }else {
               msg = Constants.editFail;
            }
            renderHtml(JSONObject.toJSONString(msg));
        }
    }
    public void update(){
        save();
    }
    public void delete(){
        String [] ids =   getParam("ids").split(",");
        boolean result = orgService.deleteOrg(ids);
        if (result){
            renderHtml(JSONObject.toJSONString(Constants.deleteSuccess));
        }else {
            renderHtml(JSONObject.toJSONString(Constants.deleteFail));
        }
    }
}
