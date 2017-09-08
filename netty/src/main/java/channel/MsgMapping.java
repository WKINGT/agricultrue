package channel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
/**
 * 客户端下发命令消息对应用户和消息uuid
 * @author TianW
 *
 */
public class MsgMapping {
	
	private Map<String,String>  msgIdtoUserId = new ConcurrentHashMap<String, String>();
	private Map<String,String>  msgIdtoUUID = new ConcurrentHashMap<String, String>();
	private static MsgMapping instance = new MsgMapping();

	private MsgMapping() {}
	
	public static MsgMapping instance() {
		return instance;
	}
	
	public Map<String, String> getMsgIdtoUser() {
		return this.msgIdtoUserId;
	}
	public Map<String, String> getMsgIdtoUUID() {
		return this.msgIdtoUUID;
	}
}
