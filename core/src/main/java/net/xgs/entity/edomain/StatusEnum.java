package net.xgs.entity.edomain;

/**
 * Created by duai on 2017-07-17.
 */
public enum StatusEnum {
    NORMAL_USE("0","正常使用"),
    PROHIBITED_USE("1","禁用");
    private String value;
    private String desc;

    StatusEnum(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
