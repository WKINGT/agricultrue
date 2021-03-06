package net.xgs.model.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
public abstract class BaseSysFunctions<M extends BaseSysFunctions<M>> extends Model<M> implements IBean {

	public void setId(java.lang.String id) {
		set("id", id);
	}

	public java.lang.String getId() {
		return get("id");
	}

	public void setName(java.lang.String name) {
		set("name", name);
	}

	public java.lang.String getName() {
		return get("name");
	}

	public void setType(java.lang.String type) {
		set("type", type);
	}

	public java.lang.String getType() {
		return get("type");
	}

	public void setCtl(java.lang.String ctl) {
		set("ctl", ctl);
	}

	public java.lang.String getCtl() {
		return get("ctl");
	}

	public void setAct(java.lang.String act) {
		set("act", act);
	}

	public java.lang.String getAct() {
		return get("act");
	}

	public void setParam(java.lang.String param) {
		set("param", param);
	}

	public java.lang.String getParam() {
		return get("param");
	}

	public void setModel(java.lang.String model) {
		set("model", model);
	}

	public java.lang.String getModel() {
		return get("model");
	}

	public void setSetting(java.lang.String setting) {
		set("setting", setting);
	}

	public java.lang.String getSetting() {
		return get("setting");
	}

	public void setParentId(java.lang.String parentId) {
		set("parentId", parentId);
	}

	public java.lang.String getParentId() {
		return get("parentId");
	}

	public void setDescription(java.lang.String description) {
		set("description", description);
	}

	public java.lang.String getDescription() {
		return get("description");
	}

	public void setIcon(java.lang.String icon) {
		set("icon", icon);
	}

	public java.lang.String getIcon() {
		return get("icon");
	}

	public void setIsEnabled(java.lang.Boolean isEnabled) {
		set("is_enabled", isEnabled);
	}

	public java.lang.Boolean getIsEnabled() {
		return get("is_enabled");
	}

	public void setCreated(java.lang.String created) {
		set("created", created);
	}

	public java.lang.String getCreated() {
		return get("created");
	}

	public void setUpdated(java.lang.String updated) {
		set("updated", updated);
	}

	public java.lang.String getUpdated() {
		return get("updated");
	}

	public void setIsDel(java.lang.Boolean isDel) {
		set("is_del", isDel);
	}

	public java.lang.Boolean getIsDel() {
		return get("is_del");
	}

	public void setNo(java.lang.Integer no) {
		set("no", no);
	}

	public java.lang.Integer getNo() {
		return get("no");
	}

}
