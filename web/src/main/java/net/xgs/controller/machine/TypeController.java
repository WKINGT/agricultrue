package net.xgs.controller.machine;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.plugin.activerecord.Page;
import net.xgs.commons.annotation.Inject;
import net.xgs.controller.core.BaseController;
import net.xgs.entity.Constants;
import net.xgs.model.BaseMachineType;
import net.xgs.services.MachineTypeService;
import org.apache.commons.lang.StringUtils;

/**
 * Created by duai on 2017-07-21.
 * 设备类型controller
 */
public class TypeController extends BaseController {
    @Inject
    MachineTypeService machineTypeService;
    public void index(){
        if (isGet){
            renderJsp("admin/type/list.jsp");
        }else {
            Page<BaseMachineType> page = machineTypeService.page(pageNo,pageSize,filterBuilder);
            renderHtml(JSONObject.toJSONString(page));
        }
    }
    public void save() {
        if (isGet) {
            String typeId = getParam("id");
            if (StringUtils.isNotBlank(typeId)){
                BaseMachineType baseMachineType = machineTypeService.getById(typeId);
                setAttr("machineType",baseMachineType);
                setAttr("name","修改");
            }else {
                setAttr("name","添加");
            }
            renderJsp("admin/type/update.jsp");
        } else {
            BaseMachineType baseMachineType = getModel(BaseMachineType.class,"machineType");
            boolean result = machineTypeService.save(baseMachineType,getSessionUserId());
            if (result){
                msg = Constants.editSuccess;
            }else {
                msg = Constants.editFail;
            }
            renderHtml(JSONObject.toJSONString(msg));
        }
    }
    public void update() {
        save();
    }
    public void delete(){
        String [] ids =  getParam("ids").split(",");
        Integer result = machineTypeService.delete(ids);
        if (result==0){
            msg = Constants.deleteSuccess;
        }else if (result==1){
            msg = Constants.deleteFail;
        }else if (result==2){
            msg.setCode(1);
            msg.setMsg("当前类型正在使用！不可删除！");
        }
        renderHtml(JSONObject.toJSONString(msg));
    }
}
