package net.xgs.services;

import net.xgs.commons.annotation.Service;
import net.xgs.model.SysMenu;

import java.util.List;

/**
 * Created by duai on 2017-07-13.
 */
@Service
public class MenuService extends BaseService {
    public List<SysMenu> tree() {
        return SysMenu.dao.find("SELECT * FROM sys_menu where is_enabled = 1");
    }

    public List<SysMenu> findByRoleIds(List<String> sysRoleIds) {
        return null;
    }
}
