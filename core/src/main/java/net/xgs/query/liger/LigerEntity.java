package net.xgs.query.liger;


public class LigerEntity {

	private String field,op,value,type;

	public enum opration{equal,notequal,startwith,endwith,like,greater,greaterorequal,less,lessorequal,in,notin}
	
	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public String getOp() {
		return op;
	}

	public void setOp(String op) {
		this.op = op;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
