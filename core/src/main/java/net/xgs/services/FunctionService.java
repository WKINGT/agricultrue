package net.xgs.services;

import java.util.List;

import com.jfinal.plugin.activerecord.Record;

import net.xgs.commons.annotation.Inject;
import net.xgs.commons.annotation.Service;
import net.xgs.model.SysFunctions;
import net.xgs.model.SysRole;
import net.xgs.utils.ObjectUtils;

/**
 * Created by duai on 2017-07-13.
 */
@Service
public class FunctionService extends BaseService{
    @Inject
    RoleService roleService;
    @Inject
    MenuFunctionService menuFunctionService;

    /**
     * 数据库校验用户是否有当前访问权限
     * @param userId
     * @param token
     * @return
     */
    public SysFunctions findByRoleId(String userId, String token) {
        List<String> functionIds = findFunctionIdsByRole(userId);
        if (functionIds.contains(token)){
            return SysFunctions.dao.findById(token);
        }
        return null;
    }

    /**
     * 获取功能的所有ID
     * 使用地方：(1、返回列表时对应的上方功能列表筛选)
     * @param userId
     * @return
     */
    public List<String> findFunctionIdsByRole(String userId){
        List<Record>  menuFunction =  findMenuFunctionByUser(userId);
        List<String> functionIds = ObjectUtils.getMethodValue(menuFunction,"getStr","function_id");
        return functionIds;
    }

    /**
     * 给用户授权时使用
     * @param userId
     * @return
     */
    public List<Record> findMenuFunctionByUser(String userId){
        List<SysRole> sysRole =  roleService.findByMemberId(userId);
        List<String> sysRoleIds = ObjectUtils.getMethodValue(sysRole,"getid");
        return   menuFunctionService.findByUser(sysRoleIds);
    }
}
