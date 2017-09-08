package net.protocol.entity;

/**
 * Created by duai on 2017-08-08.
 */
public class CreatePlanObject {
   private String taskObjId;        //计划操作对象Id device_id  String
    private byte operation1;          //byte 操作1
    private byte operation2;          //byte 操作2
    private String beginTime;           //String ,年月日时分秒
    private String endTime;           //String,年月日时分秒
    private short cycleTime;          //short,  循环周期
    private short intervalTime;       //short   操作1和操作2间隔时间
    private byte entryNO;      // byte  灌溉条目编号
    public String getTaskObjId() {
        return taskObjId;
    }

    public void setTaskObjId(String taskObjId) {
        this.taskObjId = taskObjId;
    }

    public byte getOperation1() {
        return operation1;
    }

    public void setOperation1(byte operation1) {
        this.operation1 = operation1;
    }

    public byte getOperation2() {
        return operation2;
    }

    public void setOperation2(byte operation2) {
        this.operation2 = operation2;
    }

    public String getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(String beginTime) {
        this.beginTime = beginTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public short getCycleTime() {
        return cycleTime;
    }

    public void setCycleTime(short cycleTime) {
        this.cycleTime = cycleTime;
    }

    public short getIntervalTime() {
        return intervalTime;
    }

    public void setIntervalTime(short intervalTime) {
        this.intervalTime = intervalTime;
    }
    public byte getEntryNO() {
        return entryNO;
    }

    public void setEntryNO(byte entryNO) {
        this.entryNO = entryNO;
    }

}
