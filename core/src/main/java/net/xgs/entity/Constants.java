package net.xgs.entity;

import com.jfinal.kit.Prop;
import com.jfinal.kit.PropKit;

/**
 * Created by duai on 2017-07-11.
 */
public class Constants {
    private static Prop prop = PropKit.use("cfg.properties");
    public static boolean debugger = prop.getBoolean("system.debugger");
    public static String salt = "~^_^~!!";
    public static String sessionUser = "sessionUser";
    public static String cName = "ctl";
    public static String aName = "act";
    public static String fparam = "param";
    public static String token = "token";
    public static String updateParam = "name";//更新时提示是更新还是添加
    public static String functions = "functions";//返回前端当前的菜单的功能
    public static String userMenus = "userMenus";
    public static String sessionUserRole = "sessionUserRole";
    public static String userFunctions = "userFunctions";//当前用户功能Map集合
    public static String userListFunctions = "userListFunctions";//当前用户功能List集合
    public static String userRoles = "userRoles";//当前用户角色
    public static String userRoleIds = "userRoleIds";//当前用户角色IDs
    public static String defaultParentId = "-1";
    public static String ctlPath = "net.xgs.controller.";
    public static Boolean functionIsCache =  prop.getBoolean("system.functionIsCache");
    public static String userAuthority = "userAuthority";//当前用户菜单与功能关联权限
    public static String mids = "mids";//当前用户菜单ID
    public static String fids = "fids";//当前用户功能ID
    public static String menuVos = "menuVos";//当前用户的菜单格式
    public static String userListMenus = "userListMenus";
    public static String menuListVos = "menuListVos";
    public static String functionListVos = "functionListVos";
    public static String listToken = "listToken"; //如果是更新添加等操作完成后该跳转的列表
    public static Msg editSuccess = new Msg(0,"编辑成功!",null);//编辑成功时使用的msg
    public static Msg editFail = new Msg(1,"编辑失败!",null);//编辑成功时使用的msg
    public static Msg deleteSuccess = new Msg(0,"删除成功!",null);//编辑成功时使用的msg
    public static Msg deleteFail = new Msg(1,"删除失败!",null);//编辑成功时使用的msg
    public static String baseOrg = "baseOrg";
    public static String qrCodeValue = "qrCodeValue";
    public static String ctlFrontPath = ctlPath+"front.";
    public static String MachineAllList = "MACHINEALLLIST";//设备列表
}
