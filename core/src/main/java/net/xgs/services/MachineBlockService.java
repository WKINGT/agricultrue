package net.xgs.services;

import java.util.ArrayList;
import java.util.List;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.redis.Cache;
import com.jfinal.plugin.redis.Redis;
import net.xgs.commons.annotation.Inject;
import net.xgs.entity.Constants;
import net.xgs.entity.webvo.TreeSelectVO;
import net.xgs.init.XgsConfig;
import net.xgs.model.BaseMachine;
import org.apache.commons.lang.StringUtils;

import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.tx.Tx;

import net.xgs.commons.annotation.Service;
import net.xgs.model.BaseBlockMachine;
import net.xgs.model.ViewBlockMachine;
import net.xgs.query.FilterBuilder;

/**
 * Created by duai on 2017-07-22.
 */
@Service
public class MachineBlockService extends BaseService{
    @Inject
    MachineService machineService;
    Cache cache = Redis.use(XgsConfig.prop.get("cache.redis.web.name"));
    public Page<ViewBlockMachine> page(Integer pageNumber, Integer pageSize, FilterBuilder builder){
        String select = "select * ";
        String sqlExceptSelect = "from view_machine_block where 1 = 1 ";
        sqlExceptSelect += builder.getSnippets();
        return ViewBlockMachine.dao.paginate(pageNumber, pageSize, select, sqlExceptSelect, builder.getParams().toArray());
    }

    public Page<ViewBlockMachine> pageRestful(Integer pageNumber, Integer pageSize, FilterBuilder builder){
        String select = "select block.*,type.is_controller AS is_controller,type.is_main AS is_main,type_image,machine_icon ";
        String sqlExceptSelect = "from view_machine_block block LEFT JOIN view_machine_type_manufactor type " +
                "ON type.id = block.machine_id where 1 = 1 ";
        sqlExceptSelect += builder.getSnippets();
        return ViewBlockMachine.dao.paginate(pageNumber, pageSize, select, sqlExceptSelect, builder.getParams().toArray());
    }

    public ViewBlockMachine findById(String id){
        return ViewBlockMachine.dao.findById(id);
    }

    public List<TreeSelectVO> findByTreeSelectByBlock(String blockId){
        List<TreeSelectVO> result = new ArrayList<>();
        List<BaseMachine> machines = cache.get(Constants.MachineAllList);
        Integer size = machineService.findAllCount();
        if (machines==null||machines.isEmpty()||size!=machines.size()){
            machines = machineService.findAll();
            cache.set(Constants.MachineAllList,machines);
        }
        List<BaseBlockMachine> baseBlockMachines = BaseBlockMachine.dao.find("select machine_id,block_id from base_block_machine");
        baseMachine:for (BaseMachine baseMachine:machines){
            TreeSelectVO treeSelectVO = new TreeSelectVO();
            treeSelectVO.setId(baseMachine.getId());
            treeSelectVO.setName(baseMachine.getName()+"("+baseMachine.getTerminalId()+")");
            treeSelectVO.setParentId(baseMachine.getParentId());
            for (BaseBlockMachine baseBlockMachine : baseBlockMachines) {
                if (baseMachine.getId().equals(baseBlockMachine.getMachineId())){
                    if (baseBlockMachine.getBlockId().equals(blockId)){
                        treeSelectVO.setChecked(true);
                    }else {
                        if (baseMachine.get("is_main").equals("0")) {
                            treeSelectVO.setChkDisabled(true);
                            treeSelectVO.setName(treeSelectVO.getName()+"(已分配)");
                        }
                    }

                }
            }
            result.add(treeSelectVO);
        }
        return result;
    }

    public Boolean save(String blockId,String machineIds) {
        Db.update("delete from base_block_machine where block_id = ?",blockId);
        if (StringUtils.isBlank(machineIds)) return true;
        String [] ids = machineIds.split(",");
        for (String id :ids){
            BaseBlockMachine baseBlockMachine = new BaseBlockMachine();
            baseBlockMachine.setId(getUUID());
            baseBlockMachine.setMachineId(id);
            baseBlockMachine.setBlockId(blockId);
            baseBlockMachine.save();
        }
       return true;
    }

    @Before(Tx.class)
    public Boolean delete(String[] ids) {
        for (String id:ids){
            BaseBlockMachine.dao.deleteById(id);
        }
           return true;
    }
}
