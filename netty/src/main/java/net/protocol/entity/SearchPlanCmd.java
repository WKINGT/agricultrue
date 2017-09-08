package net.protocol.entity;

public class SearchPlanCmd {
	private byte planNO;         //byte 灌溉计划编号
	private byte entryNO;      // byte  灌溉条目编号
	public byte getPlanNO() {
		return planNO;
	}
	public void setPlanNO(byte planNO) {
		this.planNO = planNO;
	}
	public byte getEntryNO() {
		return entryNO;
	}
	public void setEntryNO(byte entryNO) {
		this.entryNO = entryNO;
	}
	
}
