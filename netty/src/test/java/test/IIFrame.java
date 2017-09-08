package test;

public class IIFrame {
	private byte len;
	private byte[] terminalID;
	private byte[] commandID;
	private byte command;
	private byte[] data;
	public byte getLen() {
		return len;
	}
	public void setLen(byte len) {
		this.len = len;
	}
	public byte[] getTerminalID() {
		return terminalID;
	}
	public void setTerminalID(byte[] terminalID) {
		this.terminalID = terminalID;
	}
	public byte[] getCommandID() {
		return commandID;
	}
	public void setCommandID(byte[] commandID) {
		this.commandID = commandID;
	}
	public byte getCommand() {
		return command;
	}
	public void setCommand(byte command) {
		this.command = command;
	}
	public byte[] getData() {
		return data;
	}
	public void setData(byte[] data) {
		this.data = data;
	}

}
