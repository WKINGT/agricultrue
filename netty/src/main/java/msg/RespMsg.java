package msg;

public class RespMsg {
    private String uuid;
    private String msg;
    private String cmd;
    private String sysId;

    public RespMsg(String uuid, String msg, String cmd, String sysId) {
        this.uuid = uuid;
        this.msg = msg;
        this.cmd = cmd;
        this.sysId = sysId;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getCmd() {
        return cmd;
    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }

    public String getSysId() {
        return sysId;
    }

    public void setSysId(String sysId) {
        this.sysId = sysId;
    }
}
