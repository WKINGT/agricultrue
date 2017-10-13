package net.xgs.entity.edomain;

/**
 * Created by duai on 2017-08-09.
 */
public enum DataTypeEnum {
    LUX(1,"光照"),
    TEMPERATURE(2,"温度"),
    HUMIDITY(3,"湿度"),
    ONLINEOFF(4,"设备掉线");
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

    DataTypeEnum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public static String findDescByValue(Integer dataType) {
        for (DataTypeEnum dataTypeEnum:DataTypeEnum.values()){
            if (dataTypeEnum.getValue().equals(dataType)){
                return dataTypeEnum.getDesc();
            }
        }
        return "";
    }
}
