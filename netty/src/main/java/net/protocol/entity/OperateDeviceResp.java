package net.protocol.entity;

public class OperateDeviceResp {
	private int code;
	private String objId;           
	private byte operation; 
	private byte operationResult;
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
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
	public byte getOperationResult() {
		return operationResult;
	}
	public void setOperationResult(byte operationResult) {
		this.operationResult = operationResult;
	}
	
}
