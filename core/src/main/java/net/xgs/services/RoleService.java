package net.xgs.services;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.tx.Tx;
import net.xgs.commons.annotation.Service;
import net.xgs.commons.utils.DateUtils;
import net.xgs.entity.edomain.BooleanEnum;
import net.xgs.entity.edomain.StatusEnum;
import net.xgs.model.SysRole;
import net.xgs.query.Filter;
import net.xgs.query.FilterBuilder;
import net.xgs.utils.DBUtils;
import org.apache.commons.lang.StringUtils;

import java.util.*;

/**
 * Created by duai on 2017-07-13.
 */
@Service
public class RoleService extends BaseService{

    public List<SysRole> findByMemberId(String id) {
        List<SysRole> sysRoles = SysRole.dao.find("SELECT * FROM sys_role WHERE status = ? AND id in (SELECT role_id FROM sys_user_role WHERE user_id = ?) ",StatusEnum.NORMAL_USE.getValue(),id);
        return sysRoles;
    }
    //只能查询用户创建的角色
    public Page<SysRole> findByCreate(Integer pageNum,Integer pageSize, FilterBuilder filterBuilder,String userId){
        filterBuilder.getParams().add(StatusEnum.NORMAL_USE.getValue());
        filterBuilder.getParams().add(userId);
        Page<SysRole> sysRoles = SysRole.dao.paginate(pageNum,pageSize,"SELECT *  "," FROM sys_role WHERE status = ? AND  create_by = ?"+filterBuilder.getSnippets()+" ORDER BY create_time DESC",filterBuilder.getParams().toArray());
        return sysRoles;
    }

    //只能查询用户创建的角色（用户授权使用）
    public List<SysRole> findByCreate(String userId){
        List<SysRole> sysRoles = SysRole.dao.find("SELECT  id,role_name  FROM sys_role WHERE status = ? AND  create_by = ? ORDER BY create_time DESC",StatusEnum.NORMAL_USE.getValue(),userId);
        return sysRoles;
    }

    public SysRole findById(String roleId) {
        SysRole sysRole = SysRole.dao.findById(roleId);
        return sysRole;
    }
    @Before(Tx.class)
    public Boolean save(SysRole sysRole,String optId){
        if (StringUtils.isNotBlank(sysRole.getId())){
           return sysRole.update();
        }else {
            sysRole.setId(DBUtils.getUUID());
            sysRole.setCreateBy(optId);
            sysRole.setCreateTime(DateUtils.format(new Date(),DateUtils.PATTERN_DATETIME));
            return sysRole.save();
        }
    }

    /**
     * 此为逻辑删除
     * @param ids
     * @return
     */
    @Before(Tx.class)
    public Boolean deleteById(String [] ids) {
        for (String id:ids){
            SysRole sysRole = SysRole.dao.findById(id);
            sysRole.setStatus(StatusEnum.PROHIBITED_USE.getValue());
            sysRole.update();
            Db.update("DELETE FROM sys_menu_function_role WHERE role_id  = ?",id);
        }
        return true;
    }

    /**
     * 角色授权 数据结构{mid:菜单ID,fids:对应的功能ids}
     * @param jsonArray
     * @return
     */
    @Before(Tx.class)
    public Boolean auth(JSONArray jsonArray,String roleId){
        Db.update("DELETE FROM sys_menu_function_role WHERE role_id  = ?",roleId);
        if (jsonArray.size()<0||jsonArray==null) return false;
        Iterator<Object> menuIter = jsonArray.iterator();
        while (menuIter.hasNext()){
            JSONObject menu = (JSONObject) menuIter.next();
            String menuId = menu.getString("mid");
            JSONArray fids = menu.getJSONArray("fids");
            Iterator<Object> fidIter = fids.iterator();
            if (!fidIter.hasNext()){
                saveAuth(menuId,"",roleId);
                continue;
            }
            while (fidIter.hasNext()){
                String fid = (String) fidIter.next();
                saveAuth(menuId,fid,roleId);
            }
        }
        return true;
    }
    @Before(Tx.class)
    private void saveAuth(String menuId,String fid,String roleId){
        Record record = new Record();
        record.set("menu_id",menuId);
        record.set("function_id",fid);
        record.set("role_id",roleId);
        Db.save("sys_menu_function_role",record);
    }
}
