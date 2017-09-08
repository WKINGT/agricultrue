package net.protocol.entity;

public class DeletePlanCmd {
	private byte planNO;         //byte 灌溉计划编号

	public byte getPlanNO() {
		return planNO;
	}

	public void setPlanNO(byte planNO) {
		this.planNO = planNO;
	}
}
