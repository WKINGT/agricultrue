package net.xgs.services;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import net.xgs.commons.annotation.Service;
import net.xgs.commons.utils.StrUtils;
import net.xgs.utils.ObjectUtils;
import net.xgs.utils.ReflectionUtils;

import java.util.List;

/**
 * Created by duai on 2017-07-16.
 */
@Service
public class MenuFunctionService {
    /**
     * 查询角色下所有的菜单与功能权限 sys_menu_function_role表
     * @param sysRoleIds
     * @return 三个字段 functionId、roleId、menuId
     */
    public List<Record> findByUser(List<String> sysRoleIds){
        List<Record> records = Db.find("SELECT * FROM sys_menu_function_role where role_id in ("+ StrUtils.joinInSql(sysRoleIds)+")");
        return  records;
    }
    public List<String> findFunctionIdByRecords(List<Record> list){
        List<String> functionIds = ObjectUtils.getMethodValue(list,"getStr","function_id");
        return functionIds;
    }
    public List<String> findMenuIdByRecords(List<Record> list){
        List<String> menuIds = ObjectUtils.getMethodValue(list,"getStr","menu_id");
        return menuIds;
    }
}
