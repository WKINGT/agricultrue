package net.protocol.entity;

public class CreatePlanCmd {
	private byte planNO;         //byte 灌溉计划编号
	private byte entryNOsum;   // byte 灌溉计划条目总数
	private byte entryNO;      // byte  灌溉条目编号
	private String blockId;            //String
	private String taskName;  //任务名 String
	private Integer taskType; //任务类型
	private CreatePlanObject parameterList;

	public byte getPlanNO() {
		return planNO;
	}

	public void setPlanNO(byte planNO) {
		this.planNO = planNO;
	}

	public byte getEntryNOsum() {
		return entryNOsum;
	}

	public void setEntryNOsum(byte entryNOsum) {
		this.entryNOsum = entryNOsum;
	}

	public byte getEntryNO() {
		return entryNO;
	}

	public void setEntryNO(byte entryNO) {
		this.entryNO = entryNO;
	}

	public String getBlockId() {
		return blockId;
	}

	public void setBlockId(String blockId) {
		this.blockId = blockId;
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public Integer getTaskType() {
		return taskType;
	}

	public void setTaskType(Integer taskType) {
		this.taskType = taskType;
	}

	public CreatePlanObject getParameterList() {
		return parameterList;
	}

	public void setParameterList(CreatePlanObject parameterList) {
		this.parameterList = parameterList;
	}
}
