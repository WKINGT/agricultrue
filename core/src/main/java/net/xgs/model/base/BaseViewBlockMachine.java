package net.xgs.model.base;

import com.jfinal.plugin.activerecord.IBean;
import com.jfinal.plugin.activerecord.Model;

/**
 * Created by duai on 2017-07-22.
 */
public class BaseViewBlockMachine<M extends BaseViewBlockMachine<M>> extends Model<M> implements IBean {
    public void setId(java.lang.String id) {
        set("id", id);
    }

    public java.lang.String getId() {
        return get("id");
    }

    public void setMachineName(java.lang.String machineName) {
        set("machine_name", machineName);
    }

    public java.lang.String getMachineName() {
        return get("machine_name");
    }

    public void setBlockName(java.lang.String blockName) {
        set("block_name", blockName);
    }

    public java.lang.String getBlockName() {
        return get("block_name");
    }

    public void setInstallType(java.lang.Integer installType){
        set("install_type", installType);
    }

    public Integer getInstallType(){
       return get("install_type");
    }

    public String getBlockId(){
        return get("block_id");
    }

    public void setBlockId(String blockId){
        set("block_id",blockId);
    }

    public String getMachineId(){
        return get("machine_id");
    }

    public void setMachineId(String blockId){
        set("machine_id",blockId);
    }

    public String getIsControlData(){
        return get("is_control_data");
    }
    public String getControlDataName(){
        return get("control_data_name");
    }
    public void setIsControlData(String isControlData){
        set("is_control_data",isControlData);
    }
    public void setControlDataName(String controlDataName){
        set("control_data_name",controlDataName);
    }
    public void setMachineStatus(String machineStatus){
        set("machine_status",machineStatus);
    }
    public String getMachineStatus(){
        return get("machine_status");
    }
    public void setDeviceId(java.lang.String deviceId) {
        set("device_id", deviceId);
    }

    public java.lang.String getDeviceId() {
        return get("device_id");
    }

}
