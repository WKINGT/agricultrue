package net.xgs.model.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
public abstract class BaseBaseMachineRelation<M extends BaseBaseMachineRelation<M>> extends Model<M> implements IBean {

	public void setId(java.lang.String id) {
		set("id", id);
	}

	public java.lang.String getId() {
		return get("id");
	}

	public void setMachineId(java.lang.String machineId) {
		set("machine_id", machineId);
	}

	public java.lang.String getMachineId() {
		return get("machine_id");
	}

	public void setParentMachineId(java.lang.String parentMachineId) {
		set("parent_machine_id", parentMachineId);
	}

	public java.lang.String getParentMachineId() {
		return get("parent_machine_id");
	}

}
