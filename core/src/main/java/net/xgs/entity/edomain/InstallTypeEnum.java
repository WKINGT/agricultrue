package net.xgs.entity.edomain;

/**
 * Created by duai on 2017-07-22.
 */
public enum InstallTypeEnum {
    WIRED(1,"有线安装"),
    WIRELESS(2,"无线安装");
    private Integer value;
    private String desc;

    InstallTypeEnum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }

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
}