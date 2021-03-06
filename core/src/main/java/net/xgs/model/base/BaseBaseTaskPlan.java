package net.xgs.model.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
public abstract class BaseBaseTaskPlan<M extends BaseBaseTaskPlan<M>> extends Model<M> implements IBean {

	public void setId(java.lang.String id) {
		set("id", id);
	}

	public java.lang.String getId() {
		return get("id");
	}

	public void setTaskName(java.lang.String taskName) {
		set("task_name", taskName);
	}

	public java.lang.String getTaskName() {
		return get("task_name");
	}

	public void setTaskNo(java.lang.Integer taskNo) {
		set("task_no", taskNo);
	}

	public java.lang.Integer getTaskNo() {
		return get("task_no");
	}

	public void setTaskType(java.lang.Integer taskType) {
		set("task_type", taskType);
	}

	public java.lang.Integer getTaskType() {
		return get("task_type");
	}

	public void setTaskStatus(java.lang.Integer taskStatus) {
		set("task_status", taskStatus);
	}

	public java.lang.Integer getTaskStatus() {
		return get("task_status");
	}

	public void setSystemId(java.lang.String systemId) {
		set("system_id", systemId);
	}

	public java.lang.String getSystemId() {
		return get("system_id");
	}

	public void setBlockId(java.lang.String blockId) {
		set("block_id", blockId);
	}

	public java.lang.String getBlockId() {
		return get("block_id");
	}

	public void setMachineId(java.lang.String machineId) {
		set("machine_id", machineId);
	}

	public java.lang.String getMachineId() {
		return get("machine_id");
	}

	public void setTaskNum(java.lang.Integer taskNum) {
		set("task_num", taskNum);
	}

	public java.lang.Integer getTaskNum() {
		return get("task_num");
	}

	public void setCreateTime(java.lang.String createTime) {
		set("create_time", createTime);
	}

	public java.lang.String getCreateTime() {
		return get("create_time");
	}

	public void setCreateBy(java.lang.String createBy) {
		set("create_by", createBy);
	}

	public java.lang.String getCreateBy() {
		return get("create_by");
	}

	public void setStatus(java.lang.String status) {
		set("status", status);
	}

	public java.lang.String getStatus() {
		return get("status");
	}

}
