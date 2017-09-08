package net.xgs.entity;

public class RenderJson {

	private int code;

	private String msg;

	private Object data;

	public int getCode() {
		return code;
	}

	public RenderJson setCode(int code) {
		this.code = code;
		return this;
	}

	public String getMsg() {
		return msg;
	}

	public RenderJson setMsg(String msg) {
		this.msg = msg;
		return this;
	}

	public Object getData() {
		return data;
	}

	public RenderJson setData(Object data) {
		this.data = data;
		return this;
	}
}