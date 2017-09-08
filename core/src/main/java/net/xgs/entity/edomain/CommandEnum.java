package net.xgs.entity.edomain;

/**
 * Created by duai on 2017-08-22.
 */
public enum CommandEnum {
    START("start-->","开"),
    STOP("end-->","关");
    private String value;
    private String desc;

    CommandEnum(String value, String desc) {
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
    public static String findDescByValue(String value){
        for (CommandEnum commandEnum : CommandEnum.values()){
            if (commandEnum.getValue().equals(value)){
                return commandEnum.getDesc();
            }
        }
        return "";
    }
}
