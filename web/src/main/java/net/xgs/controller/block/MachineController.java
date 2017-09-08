package net.xgs.controller.block;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.plugin.activerecord.Page;
import net.xgs.commons.annotation.Inject;
import net.xgs.controller.core.BaseController;
import net.xgs.entity.Constants;
import net.xgs.model.*;
import net.xgs.services.BlockMemberService;
import net.xgs.services.BlockService;
import net.xgs.services.MachineBlockService;
import net.xgs.services.MachineService;
import org.apache.commons.lang.StringUtils;

import java.util.List;

/**
 * Created by duai on 2017-07-22.
 */
public class MachineController extends BaseController {
    @Inject
    MachineBlockService machineBlockService;
    @Inject
    BlockService blockService;
    @Inject
    MachineService machineService;
    public void index(){
        if (isGet){
            renderJsp("admin/block/machine/list.jsp");
        }else {
           Page<ViewBlockMachine> page = machineBlockService.page(pageNo,pageSize,filterBuilder);
            renderHtml(JSONObject.toJSONString(page));
        }
    }

    public void save(){
        if (isGet){
            String id = getParam("id");
            if (StringUtils.isNotBlank(id)){
                ViewBlockMachine viewBlockMachine =  machineBlockService.findById(id);
                setAttr("machine",viewBlockMachine);
                setAttr("name","修改");
            }else {
                setAttr("name","添加");
            }
            List<BaseBlock> blocks = blockService.findBlockByMemberId(getSessionUserId());
            setAttr("blocks",blocks);
            List<ViewMachine> viewMachines = machineService.findViewMachine();
            setAttr("viewMachines",viewMachines);
            renderJsp("admin/block/machine/update.jsp");
        }else {
            BaseBlockMachine baseBlockMachine =getModel(BaseBlockMachine.class,"machine");
           /* Boolean result = machineBlockService.existByBlockAndMachine(baseBlockMachine);
            if (result){
                msg.setCode(1);
                msg.setMsg("当前设备已使用，分配失败！");
                renderHtml(JSONObject.toJSONString(msg));
                return;
            }*/
            /*Boolean  result = machineBlockService.save(baseBlockMachine);
            if (result){
                renderHtml(JSONObject.toJSONString(Constants.editSuccess));
            }else {
                renderHtml(JSONObject.toJSONString(Constants.editFail));
            }*/
        }
    }

    public void update(){
       save();
    }

    public void delete(){
        String[] ids =  getParam("ids").split(",");
        Boolean result = machineBlockService.delete(ids);
        if (result){
            renderHtml(JSONObject.toJSONString(Constants.deleteSuccess));
        }else {
            renderHtml(JSONObject.toJSONString(Constants.deleteFail));
        }
    }
}
