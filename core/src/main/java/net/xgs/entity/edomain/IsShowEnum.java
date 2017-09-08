package net.xgs.entity.edomain;

/**
 * Created by duai on 2017-08-08.
 */
public enum IsShowEnum {
    TRUE(1,"1","true"),
    FALSE(0,"0","false");
    private Integer value;
    private String valueStr;
    private String desc;

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public String getValueStr() {
        return valueStr;
    }

    public void setValueStr(String valueStr) {
        this.valueStr = valueStr;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    IsShowEnum(Integer value, String valueStr, String desc) {
        this.value = value;
        this.valueStr = valueStr;
        this.desc = desc;
    }
}
