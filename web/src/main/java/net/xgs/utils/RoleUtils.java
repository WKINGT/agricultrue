package net.xgs.utils;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.kit.JsonKit;
import net.xgs.entity.webvo.FunctionVO;
import net.xgs.entity.webvo.FunctionWebVO;
import net.xgs.model.SysMenu;

import java.util.List;

/**
 * Created by duai on 2017-07-18.
 */
public class RoleUtils {
    public static String roleMenuData(List<SysMenu> sysMenus, List<FunctionVO> functionVOS, List<String> functionIds, List<String> menuIds){
        for (SysMenu sysMenu:sysMenus){
            if (menuIds.contains(sysMenu.getId())){
                sysMenu.put("checked",true);
            }else {
                sysMenu.put("checked",false);
            }
            for (FunctionVO functionVO : functionVOS){
                if (functionVO.getId().equals(sysMenu.getFunctionId())){
                    setChecked(functionVO,functionIds);
                    sysMenu.put("smfuncs", JSONObject.toJSON(functionVO));
                }
            }
        }
        return JsonKit.toJson(sysMenus);
    }
    private static void setChecked(FunctionVO functionVO, List<String> functionIds){
        if (functionIds.contains(functionVO.getId())){
            functionVO.setChecked(true);
        }else {
            functionVO.setChecked(false);
        }
        if (functionVO.getSonData()!=null&&functionVO.getSonData().size()>0){
            for (FunctionVO functionVO1:functionVO.getSonData()){
                setChecked(functionVO1,functionIds);
            }
        }
    }
}
