package net.protocol.entity;

import java.util.HashMap;
import java.util.Map;

public class ReportMsgResp {
	private String time;// String eg. 2017-7-20 16:10:20
	private byte type; // byte 信息采集类型 1定时上报，2实时上报
	private Map<String,String> values = new HashMap<String,String>(); // String 设备对象Id,值

	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public byte getType() {
		return type;
	}
	public void setType(byte type) {
		this.type = type;
	}
	public Map<String, String> getValues() {
		return values;
	}
	public void setValues(Map<String, String> values) {
		this.values = values;
	}}
