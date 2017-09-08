package net.xgs.services;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import net.xgs.commons.annotation.Inject;
import net.xgs.commons.annotation.Service;
import net.xgs.entity.edomain.DataTypeEnum;
import net.xgs.entity.edomain.IsShowEnum;
import net.xgs.entity.edomain.StatusEnum;
import net.xgs.model.*;
import net.xgs.query.FilterBuilder;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by duai on 2017-08-11.
 */
@Service
public class BlockDataService extends BaseService {
    @Inject
    MachineBlockTypeService machineBlockTypeService;
    @Inject
    MachineDataService machineDataService;
    @Inject
    TaskPlanService taskPlanService;
    @Inject
    BlockService blockService;
    public Page<BaseBlockHistoryData> page(Integer pageNo, Integer pageSize, FilterBuilder filterBuilder) {
        return BaseBlockHistoryData.dao.paginate(pageNo,pageSize,"select * "," from base_block_history_data where status = 0 "+filterBuilder.getSnippets()+" order by create_time desc",filterBuilder.getParams().toArray());
    }

    public BaseBlockHistoryData findById(String id) {
        return BaseBlockHistoryData.dao.findById(id);
    }

    public BaseBlockHistoryData findInfoByBlockId(BaseBlockHistoryData baseBlockHistoryData){
        List<ViewMachineBlockType> machines = machineBlockTypeService.findMachineByBlockAndMain(baseBlockHistoryData.getBlockId(),
                IsShowEnum.FALSE.getValue(),
                IsShowEnum.FALSE.getValue(),
                IsShowEnum.TRUE.getValue());
        List<Map<String,String>> list = new ArrayList<>();
        for (ViewMachineBlockType view:machines){
            Map<String,String> map = new HashMap<>();
            DataMachine dataMachine = machineDataService.findByMachine(view.getStr("machine_id"));
            String tempStr =  view.getStr("control_data_name");
            String unit = view.getStr("control_data_unit");
            if (view.getStr("control_data_unit").indexOf(",")>-1){
                String [] units = view.getStr("control_data_unit").split(",");
                if (dataMachine.getDataType().equals(DataTypeEnum.TEMPERATURE.getValue())){
                    unit = units[0];
                }else {
                    unit = units[1];
                }
                tempStr += DataTypeEnum.findDescByValue(dataMachine.getDataType());
            }
            map.put("machineData",tempStr+": "+Integer.parseInt(dataMachine.getData(),16)+unit);
            map.put("machineImage",view.getStr("machine_icon"));
            list.add(map);
        }
        List<BaseTaskPlan> result = taskPlanService.findTaskByBlockId(baseBlockHistoryData.getBlockId());
        baseBlockHistoryData.put("taskList",result);
        baseBlockHistoryData.put("machineData",list);
        return baseBlockHistoryData;
    }

    public boolean save(BaseBlockHistoryData historyData,String optId) {
        if (StringUtils.isNotBlank(historyData.getId())){
          return   historyData.update();
        }
        historyData.setId(getUUID());
        historyData.setCreateBy(optId);
        historyData.setCreateTime(getDateTime());
        return historyData.save();
    }

    public Boolean delete(String[] ids) {
        for (String id:ids){
            Db.update("UPDATE  base_block_history_data SET status = 1 WHERE id = ?",id);
        }
        return true;
    }
    public BaseBlockHistoryData findMaxCreateTimeByBlock(String blockId){
        BaseBlock baseBlock = blockService.getById(blockId);
        if (baseBlock==null) return new BaseBlockHistoryData();
        BaseBlockHistoryData baseBlockHistoryData = BaseBlockHistoryData.dao.findFirst("select *  from base_block_history_data where status = ? and block_id = ?", StatusEnum.NORMAL_USE.getValue(),blockId);
        if (baseBlockHistoryData==null) baseBlockHistoryData = new BaseBlockHistoryData();
        baseBlockHistoryData.put("description",baseBlock.getDescription());
        baseBlockHistoryData.put("block_acreage",baseBlock.getBlockAcreage());
        baseBlockHistoryData.put("block_name",baseBlock.getName());
        return   baseBlockHistoryData;
    }

}
