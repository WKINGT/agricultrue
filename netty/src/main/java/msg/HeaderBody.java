package msg;

/**
 * 请求or命令报文实体
 * 
 * @author TianW
 *
 */
public class HeaderBody {
	private int len;
	private String version;
	private String userId;
	private String sysId;
	private String uuid;
	private short cmd;
	private String msg;

	public HeaderBody(int len, String version, String userId, String sysId, String uuid, short cmd, String msg) {
		this.len = len;
		this.version = version;
		this.userId = userId;
		this.sysId = sysId;
		this.uuid = uuid;
		this.cmd = cmd;
		this.msg = msg;
	}

	public int getLen() {
		return len;
	}

	public void setLen(int len) {
		this.len = len;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getSysId() {
		return sysId;
	}

	public void setSysId(String sysId) {
		this.sysId = sysId;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public short getCmd() {
		return cmd;
	}

	public void setCmd(short cmd) {
		this.cmd = cmd;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

}