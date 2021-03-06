package net.xgs.model.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
public abstract class BaseDataCmdLog<M extends BaseDataCmdLog<M>> extends Model<M> implements IBean {

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

	public void setCustomerId(java.lang.String customerId) {
		set("customer_id", customerId);
	}

	public java.lang.String getCustomerId() {
		return get("customer_id");
	}

	public void setUserId(java.lang.String userId) {
		set("user_id", userId);
	}

	public java.lang.String getUserId() {
		return get("user_id");
	}

	public void setData(java.lang.String data) {
		set("data", data);
	}

	public java.lang.String getData() {
		return get("data");
	}

	public void setResultStatus(java.lang.String resultStatus) {
		set("result_status", resultStatus);
	}

	public java.lang.String getResultStatus() {
		return get("result_status");
	}

	public void setResultData(java.lang.String resultData) {
		set("result_data", resultData);
	}

	public java.lang.String getResultData() {
		return get("result_data");
	}

	public void setCreateTime(java.lang.String createTime) {
		set("create_time", createTime);
	}

	public java.lang.String getCreateTime() {
		return get("create_time");
	}

}
