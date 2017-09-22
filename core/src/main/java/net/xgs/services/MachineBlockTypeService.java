package net.xgs.services;

import net.xgs.commons.annotation.Service;
import net.xgs.commons.utils.StrUtils;
import net.xgs.model.ViewMachineBlockType;

import java.util.Arrays;
import java.util.List;

/**
 * Created by duai on 2017-08-07.
 */
@Service
public class MachineBlockTypeService extends BaseService {

    public List<ViewMachineBlockType> findMachineByBlockAndMain(Object... paras){
        return ViewMachineBlockType.dao.find("SELECT * FROM view_machine_block_type WHERE block_id = ? AND is_main = ? and is_controller = ? and is_control_data = ?",paras);
    }
    public List<ViewMachineBlockType> findMainMachineIdByBlockIds(String[] blockIds){
       return ViewMachineBlockType.dao.find("select machine_id from view_machine_block_type where is_main = 1 and block_id in ("+ StrUtils.joinInSql(Arrays.asList(blockIds))+")");
    }
    public List<ViewMachineBlockType> findMainByBlockIds(String[] blockIds,String fields){
        return ViewMachineBlockType.dao.find("select "+fields+" from view_machine_block_type where is_main = 1 and block_id in ("+ StrUtils.joinInSql(Arrays.asList(blockIds))+")");
    }

    /**
     * 查询主控箱子
     * @return
     */
    public List<ViewMachineBlockType> findMachineByMain(){
        return ViewMachineBlockType.dao.find("SELECT * FROM view_machine_block_type WHERE is_main = ? and is_controller = ? ",1,1);
    }

    /**
     * 根据is_main、is_controller、is_control_data 查询设备数据
     * @param paras 参数顺序is_main、is_controller、is_control_data
     * @return
     */
    public List<ViewMachineBlockType> findMachineByParams(String fieldStr,Object... paras){
        return ViewMachineBlockType.dao.find("SELECT "+fieldStr+" FROM view_machine_block_type WHERE is_main = ? and is_controller = ? and is_control_data = ?",paras);
    }
}
