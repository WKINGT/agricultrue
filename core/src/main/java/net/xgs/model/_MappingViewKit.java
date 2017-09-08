package net.xgs.model;

import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import net.xgs.model.base.BaseViewMachineBlockType;

/**
 * Created by duai on 2017-07-22.
 */
public class _MappingViewKit {
    public static void mapping(ActiveRecordPlugin arp) {
        arp.addMapping("view_machine_type_manufactor", "id", ViewMachine.class);
        arp.addMapping("view_machine_block","id",ViewBlockMachine.class);
        arp.addMapping("view_member_block","id",ViewBlockMember.class);
        arp.addMapping("view_machine_block_type","id",ViewMachineBlockType.class);
        arp.addMapping("view_machine_params","id",ViewMachineParams.class);

    }
}
