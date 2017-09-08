package net.protocol.entity;

public class OperateDeviceCmd {
	private String objId;           
	private byte operation;
	public String getObjId() {
		return objId;
	}
	public void setObjId(String objId) {
		this.objId = objId;
	}
	public byte getOperation() {
		return operation;
	}
	public void setOperation(byte operation) {
		this.operation = operation;
	} 
}
