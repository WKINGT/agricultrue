package net.xgs.controller.block;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.kit.JsonKit;
import com.jfinal.plugin.activerecord.Page;
import net.xgs.commons.annotation.Inject;
import net.xgs.config.WebConfig;
import net.xgs.controller.core.BaseController;
import net.xgs.entity.Constants;
import net.xgs.entity.edomain.IsShowEnum;
import net.xgs.init.XgsConfig;
import net.xgs.model.*;
import net.xgs.query.FilterBuilder;
import net.xgs.services.*;
import org.apache.commons.lang.StringUtils;

import java.util.Collections;
import java.util.List;

/**
 * Created by duai on 2017-08-11.
 */
public class BlockDataController extends BaseController {
    @Inject
    BlockDataService blockDataService;
    @Inject
    MachineBlockService machineBlockService;
    @Inject
    MachineParamService machineParamService;
    @Inject
    BlockService blockService;
    @Inject
    MachineDataService machineDataService;
    public void index(){
        if (isGet){
            renderJsp("admin/block/data/list.jsp");
        }else {
            Page<BaseBlockHistoryData> page = blockDataService.page(pageNo,pageSize,filterBuilder);
            renderHtml(JSONObject.toJSONString(page));
        }
    }

    public void save(){
        if (isGet){
            String id = getParam("id");
            if (StringUtils.isNotBlank(id)){
                setAttr("block_data",blockDataService.findById(id));
                setAttr("name","修改");
            }else {
                setAttr("name","添加");
            }
            List<BaseBlock> blocks = blockService.findBlockByMemberId(getSessionUserId());
            controller.setAttr("blocks",blocks);
            renderJsp("admin/block/data/update.jsp");
        }else {
            BaseBlockHistoryData historyData = getModel(BaseBlockHistoryData.class,"block_data");
            boolean result = blockDataService.save(historyData,getSessionUserId());
            if (result){
                renderHtml(JSONObject.toJSONString(Constants.editSuccess));
            }else {
                renderHtml(JSONObject.toJSONString(Constants.editFail));
            }
        }
    }
    public void update(){
        save();
    }
    public void delete(){
        String[] ids =  getParam("ids").split(",");
        Boolean result =  blockDataService.delete(ids);
        if (result){
            renderHtml(JSONObject.toJSONString(Constants.deleteSuccess));
        }else {
            renderHtml(JSONObject.toJSONString(Constants.deleteFail));
        }
    }

    public void blockQr(){
        String content = XgsConfig.prop.get("http.qrCode.block.post.info")+getParam("blockId");
        this.controller.renderQrCode(content,200,200);
    }
    public void control(){
        if (isGet){
            setAttr("blockId",this.getParam("blockId"));
            setAttr("url", WebConfig.webSocket.get("url"));
            setAttr("userId",getSessionUserId());
            renderJsp("admin/block/control/list.jsp");
        }else {
            String blockId = this.getParam("blockId");
            FilterBuilder filterBuilder =  new FilterBuilder();
            filterBuilder.getParams().add(blockId);
            filterBuilder.setSnippets("AND block.block_id = ? ");
            filterBuilder.getParams().add(IsShowEnum.TRUE.getValue());
            filterBuilder.setSnippets("AND type.is_controller = ? ");
            Page<ViewBlockMachine> page = machineBlockService.pageRestful(getParamToInt("pageNo"),getParamToInt("pageSize"),filterBuilder);
            for (ViewBlockMachine viewBlockMachine:page.getList()){
                List<BaseMachineParams>  machineParams =machineParamService.findParamByMachine(viewBlockMachine.getMachineId());
                viewBlockMachine.put("machineParams",machineParams);
                viewBlockMachine.put("control_data_value","");
                if (viewBlockMachine.getIsControlData().equals(IsShowEnum.TRUE.getValueStr())){
                    viewBlockMachine.put("control_data_value",machineDataService.findByMachine(viewBlockMachine.getMachineId()).getJointData());
                }
                if(viewBlockMachine.getStr("is_controller").equals(IsShowEnum.TRUE.getValueStr()))
                {
                    DataMachine dataMachine = machineDataService.findByMachine(viewBlockMachine.getMachineId());
                    if (dataMachine==null) continue;
                    viewBlockMachine.put("controller_data",dataMachine.getData());
                }
            }
            Collections.sort(page.getList(), (o1, o2) -> {
                if (o1.getIsMain().equals(IsShowEnum.TRUE.getValueStr())) {
                    return -1;
                }
                return 1;
            });
            renderHtml(JsonKit.toJson(page));}
    }

}
