package net.xgs.controller.machine;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import net.xgs.commons.annotation.Inject;
import net.xgs.controller.core.BaseController;
import net.xgs.entity.Constants;
import net.xgs.model.BaseMachine;
import net.xgs.model.BaseMachineParams;
import net.xgs.model.ViewMachine;
import net.xgs.services.MachineParamService;
import net.xgs.services.MachineService;
import net.xgs.services.MachineTypeService;
import net.xgs.services.ManufactorService;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by duai on 2017-07-21.
 * 设备controller
 */
public class MachineController extends BaseController {
    @Inject
    MachineService machineService;
    @Inject
    MachineTypeService machineTypeService;
    @Inject
    ManufactorService manufactorService;
    @Inject
    MachineParamService machineParamService;
    public void index(){
        if (isGet){
            renderJsp("admin/machine/list.jsp");
        }else {
            Page<ViewMachine> page = machineService.page(pageNo,pageSize,filterBuilder);
            renderHtml(JSONObject.toJSONString(page));
        }
    }
    public void save(){
        if (isGet){
            String id = getParam("id");
            if (StringUtils.isNotBlank(id)){
                BaseMachine baseMachine = machineService.getById(id);
                Record record = Db.findFirst("select * from base_machine_machine_type where machine_id = ?",baseMachine.getId());
                baseMachine.put("machine_type_id",record.get("machine_type_id"));
                baseMachine.put("manufactor_id",record.get("manufactor_id"));
                setAttr("machine_params",machineParamService.findParamByMachine(id));
                setAttr("machine",baseMachine);
                setAttr("name","修改");
            }else {
                setAttr("name","添加");
            }
            List<BaseMachine> parents = machineService.findByParents(Constants.defaultParentId,false);
            setAttr("parents",parents);
            setAttr("machineTypes",machineTypeService.findAll());
            setAttr("manufactors",manufactorService.findAll());
            renderJsp("admin/machine/update.jsp");
        }else {
            BaseMachine baseMachine = getModel(BaseMachine.class,"machine");
            baseMachine.put("manufactor_id",getParam("manufactor_id"));
            baseMachine.put("machine_type_id",getParam("machine_type_id"));
            List<BaseMachineParams> params = new ArrayList<>();
            for (int i = 0;i<50;i++){
                BaseMachineParams param = getModel(BaseMachineParams.class,"param["+i+"]");
                if (param!=null && StringUtils.isNotBlank(param.getParamCommand())){
                    params.add(param);
                }
            }
            boolean result =  machineService.save(baseMachine,getSessionUserId(),params);
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
        String[] ids =  getParam("ids").split(",");
        Integer result = machineService.delete(ids);
        if (result==0){
            msg = Constants.deleteSuccess;
        }else{
            msg.setCode(1);
            msg.setMsg("当前设备正在使用！不可删除！");
        }
        renderHtml(JSONObject.toJSONString(msg));
    }
}
