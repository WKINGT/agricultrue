package net.xgs.entity;

public class Msg {

	private int code=0;
	private String msg;
	private Object data;
	
	public int getCode() {
		return code;
	}
	public Msg setCode(int code) {
		this.code = code;
		return this;
	}
	public String getMsg() {
		return msg;
	}
	public Msg setMsg(String msg) {
		this.msg = msg;
		return this;
	}
	public Object getData() {
		return data;
	}
	public Msg setData(Object data) {
		this.data = data;
		return this;
	}

	public Msg(int code, String msg, Object data) {
		this.code = code;
		this.msg = msg;
		this.data = data;
	}

	public Msg() {
	}
}
