package net.xgs.model.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
public abstract class BaseDataMachine<M extends BaseDataMachine<M>> extends Model<M> implements IBean {

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

	public void setDataType(java.lang.Integer dataType) {
		set("data_type", dataType);
	}

	public java.lang.Integer getDataType() {
		return get("data_type");
	}

	public void setData(java.lang.String data) {
		set("data", data);
	}

	public java.lang.String getData() {
		return get("data");
	}

	public void setJointData(java.lang.String jointData) {
		set("joint_data", jointData);
	}

	public java.lang.String getJointData() {
		return get("joint_data");
	}

	public void setCreateTime(java.lang.Long createTime) {
		set("create_time", createTime);
	}

	public java.lang.Long getCreateTime() {
		return get("create_time");
	}

}
