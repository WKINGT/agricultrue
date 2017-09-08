package net.xgs.entity.edomain;

/**
 * Created by duai on 2017-08-18.
 */
public enum AlarmEnum {
    SYSTEM(1,"系统上报"),
    JOB(2,"执行任务");
    private Integer value;
    private String desc;

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    AlarmEnum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }
}
