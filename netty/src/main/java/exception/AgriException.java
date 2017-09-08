package exception;

public class AgriException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9141364582416152614L;
	
	private int code;
	private String message;
	
	public AgriException(int code){
		this.code = code;
	}
	
	public AgriException(int  code ,String message){
		super(message);
		this.code = code;
		this.message = message;
	}

	public int getCode(){
		return this.code;
	}
	
	public String getMsg(){
		return this.message;
	}
}
