package net.xgs.entity.webvo;

import net.xgs.model.SysFunctions;

import java.io.Serializable;
import java.util.List;

/**
 * Created by duai on 2017-07-14.
 * 功能与功能的关系
 */
public class FunctionVO implements Serializable {
    private String id;
    private String name;
    private String type;
    private String ctl;
    private String act;
    private String param;
    private String model;
    private String setting;
    private String parentId;
    private String description;
    private String icon;
    private Boolean isEnabled;
    private String created;
    private String updated;
    private Boolean isDel;
    private Integer no;
    private Boolean checked = false;
    private List<FunctionVO> sonData;

    public FunctionVO(){

    }

    public FunctionVO(SysFunctions sysFunctions) {
        setId(sysFunctions.getId());
        setName(sysFunctions.getName());
        setType(sysFunctions.getType());
        setCtl(sysFunctions.getCtl());
        setAct(sysFunctions.getAct());
        setParam(sysFunctions.getParam());
        setModel(sysFunctions.getModel());
        setSetting(sysFunctions.getSetting());
        setParentId(sysFunctions.getParentId());
        setDescription(sysFunctions.getDescription());
        setIcon(sysFunctions.getIcon());
        setEnabled(sysFunctions.getIsEnabled());
        setCreated(sysFunctions.getCreated());
        setUpdated(sysFunctions.getUpdated());
        setDel(sysFunctions.getIsDel());
        setNo(sysFunctions.getNo());
    }

    public Boolean getChecked() {
        return checked;
    }

    public void setChecked(Boolean checked) {
        this.checked = checked;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCtl() {
        return ctl;
    }

    public void setCtl(String ctl) {
        this.ctl = ctl;
    }

    public String getAct() {
        return act;
    }

    public void setAct(String act) {
        this.act = act;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getSetting() {
        return setting;
    }

    public void setSetting(String setting) {
        this.setting = setting;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Boolean getEnabled() {
        return isEnabled;
    }

    public void setEnabled(Boolean enabled) {
        isEnabled = enabled;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getUpdated() {
        return updated;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }

    public Boolean getDel() {
        return isDel;
    }

    public void setDel(Boolean del) {
        isDel = del;
    }

    public Integer getNo() {
        return no;
    }

    public void setNo(Integer no) {
        this.no = no;
    }

    public List<FunctionVO> getSonData() {
        return sonData;
    }

    public void setSonData(List<FunctionVO> sonData) {
        this.sonData = sonData;
    }
}
