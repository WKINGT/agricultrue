package net.xgs.entity.webvo;

import net.xgs.model.SysMenu;

import java.io.Serializable;
import java.util.List;

/**
 * Created by duai on 2017-07-14.
 * 封装菜单与菜单，菜单与功能的关系
 */
public class MenuVO  implements Serializable {
    private String id;
    private String menuName;
    private String parentId;
    private String icon;
    private String pass;
    private Integer floor;
    private String functionId;
    private Integer no;
    private List<MenuVO> sonData;
    private FunctionVO functionVO;

    public MenuVO(SysMenu sysMenu) {
        setId(sysMenu.getId());
        setMenuName(sysMenu.getMenuName());
        setParentId(sysMenu.getParentId());
        setIcon(sysMenu.getIcon());
        setPass(sysMenu.getPass());
        setFloor(sysMenu.getFloor());
        setNo(sysMenu.getNo());
        setFunctionId(sysMenu.getFunctionId());
    }
    public MenuVO(){

    }

    public String getFunctionId() {
        return functionId;
    }

    public void setFunctionId(String functionId) {
        this.functionId = functionId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public Integer getFloor() {
        return floor;
    }

    public void setFloor(Integer floor) {
        this.floor = floor;
    }

    public Integer getNo() {
        return no;
    }

    public void setNo(Integer no) {
        this.no = no;
    }

    public List<MenuVO> getSonData() {
        return sonData;
    }

    public void setSonData(List<MenuVO> sonData) {
        this.sonData = sonData;
    }

    public FunctionVO getFunctionVO() {
        return functionVO;
    }

    public void setFunctionVO(FunctionVO functionVO) {
        this.functionVO = functionVO;
    }
}
