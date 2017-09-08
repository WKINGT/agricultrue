package net.xgs.controller;

import java.util.List;

import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.Page;

import net.xgs.commons.annotation.Controller;
import net.xgs.commons.annotation.Inject;
import net.xgs.entity.edomain.IsShowEnum;
import net.xgs.interceptor.Post;
import net.xgs.model.BaseMachineParams;
import net.xgs.model.ViewBlockMachine;
import net.xgs.model.ViewMachineBlockType;
import net.xgs.query.FilterBuilder;
import net.xgs.services.MachineBlockService;
import net.xgs.services.MachineBlockTypeService;
import net.xgs.services.MachineDataService;
import net.xgs.services.MachineParamService;

/**
 * Created by duai on 2017-07-27.
 */
@Controller(value = "/machine")
public class MachineController  extends BaseController {
    @Inject
    MachineBlockService machineBlockService;
    @Inject
    MachineParamService machineParamService;
    @Inject
    MachineDataService machineDataService;
    @Inject
    MachineBlockTypeService machineBlockTypeService;
    @Before(Post.class)
    public Object findMachineByBlockData(){
        String blockId = getPara("blockId");
        FilterBuilder filterBuilder =  new FilterBuilder();
        filterBuilder.getParams().add(blockId);
        filterBuilder.setSnippets("AND block.block_id = ? ");
        Page<ViewBlockMachine> page = machineBlockService.pageRestful(getParaToInt("pageNo"),getParaToInt("pageSize"),filterBuilder);
        for (ViewBlockMachine viewBlockMachine:page.getList()){
            List<BaseMachineParams>  machineParams =machineParamService.findParamByMachine(viewBlockMachine.getMachineId());
            viewBlockMachine.put("machineParams",machineParams);
            viewBlockMachine.put("control_data_value","");
            if (viewBlockMachine.getIsControlData().equals(IsShowEnum.TRUE.getValueStr())){
                viewBlockMachine.put("control_data_value",machineDataService.findByMachine(viewBlockMachine.getMachineId()).getJointData());
            }
        }
        return page;
    }
    @Before(Post.class)
    public Object findMachineByIds(){
        String [] ids = getPara("ids").split(",");
        return machineDataService.findByMachine(ids);
    }

    @Before(Post.class)
    public Object findMachineByBlock(){
        String blockId = getPara("blockId");
        Integer isMain = getParaToInt("isMain");
        Integer isController = getParaToInt("isController");
        Integer isControllerData = getParaToInt("isControllerData");
        List<ViewMachineBlockType> result = machineBlockTypeService.findMachineByBlockAndMain(blockId,isMain,isController,isControllerData);
        for (ViewMachineBlockType viewMachineBlockType :result){
            List<BaseMachineParams>  machineParams =machineParamService.findParamByMachine(viewMachineBlockType.get("machine_id"));
            viewMachineBlockType.put("machineParams",machineParams);
        }
        return result;
    }
}
