package net.xgs.utils;

import com.jfinal.aop.Enhancer;
import com.jfinal.kit.Prop;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.Page;
import net.xgs.model.ViewBlockMachine;
import net.xgs.query.FilterBuilder;
import net.xgs.services.MachineBlockService;
import net.xgs.services.MachineService;
import org.apache.commons.lang.StringUtils;

import java.util.*;

/**
 * Created by duai on 2017-08-04.
 */
public class BlockDataUtils {
    private static Prop porp = PropKit.use("cfg.properties");
    private static MachineBlockService machineBlockService = Enhancer.enhance(MachineBlockService.class);
    public static Map<String,String> getControllerDataName(String blockId){
        Map<String,String> result = new HashMap<>();
        List<String> legends = new ArrayList<>();
        List<String> ids = new ArrayList<>();
        FilterBuilder filterBuilder =  new FilterBuilder();
        filterBuilder.getParams().add(blockId);
        filterBuilder.setSnippets("AND block.block_id = ? ");
        filterBuilder.getParams().add(0);
        filterBuilder.setSnippets("and is_controller = ?");
        filterBuilder.getParams().add(0);
        filterBuilder.setSnippets("and is_main = ?");
        Page<ViewBlockMachine> page = machineBlockService.pageRestful(1,Integer.MAX_VALUE,filterBuilder);
        if (page.getList().size()<=0){
            return result;
        }
        for (ViewBlockMachine blockMachine:page.getList()){
            String unit = blockMachine.get("control_data_unit");
            if (StringUtils.isNotBlank(unit)){
                String [] units = unit.split(",");
                for(int i = 0;i<units.length;i++){
                    if (units.length>1){
                       String un = porp.get("units.order").split(",")[i];
                        legends.add(blockMachine.getControlDataName()+un+"("+units[i]+")");
                    }else {
                        legends.add(blockMachine.getControlDataName()+"("+units[i]+")");
                    }
                }
            }
            ids.add(blockMachine.getMachineId());
        }

        result.put("legend",StringUtils.join(legends.toArray(),","));
        result.put("ids",StringUtils.join(ids.toArray(),","));
        return result;
    }
}
