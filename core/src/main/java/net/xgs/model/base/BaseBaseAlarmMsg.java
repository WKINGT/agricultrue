package net.xgs.model.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
public abstract class BaseBaseAlarmMsg<M extends BaseBaseAlarmMsg<M>> extends Model<M> implements IBean {

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

	public void setBlockId(java.lang.String blockId) {
		set("block_id", blockId);
	}

	public java.lang.String getBlockId() {
		return get("block_id");
	}

	public void setAlarmType(java.lang.Integer alarmType) {
		set("alarm_type", alarmType);
	}

	public java.lang.Integer getAlarmType() {
		return get("alarm_type");
	}

	public void setMsgCode(java.lang.String msgCode) {
		set("msg_code", msgCode);
	}

	public java.lang.String getMsgCode() {
		return get("msg_code");
	}

	public void setAddData(java.lang.String addData) {
		set("add_data", addData);
	}

	public java.lang.String getAddData() {
		return get("add_data");
	}

	public void setMachineData(java.lang.Integer machineData) {
		set("machine_data", machineData);
	}

	public java.lang.Integer getMachineData() {
		return get("machine_data");
	}

	public void setThresholdData(java.lang.Integer thresholdData) {
		set("threshold_data", thresholdData);
	}

	public java.lang.Integer getThresholdData() {
		return get("threshold_data");
	}

	public void setMachineName(java.lang.String machineName) {
		set("machine_name", machineName);
	}

	public java.lang.String getMachineName() {
		return get("machine_name");
	}

	public void setReadingState(java.lang.Integer readingState) {
		set("reading_state", readingState);
	}

	public java.lang.Integer getReadingState() {
		return get("reading_state");
	}

	public void setCreateTime(java.lang.Long createTime) {
		set("create_time", createTime);
	}

	public java.lang.Long getCreateTime() {
		return get("create_time");
	}

}
