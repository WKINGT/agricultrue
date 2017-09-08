package net.xgs.exception;

public class MethodException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int code;

	public MethodException(int code, String msg) {
		super(msg);
		this.code = code;
	}

	public int getCode() {
		return this.code;
	}
}
