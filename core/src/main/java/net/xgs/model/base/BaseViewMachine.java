package net.xgs.model.base;

import com.jfinal.plugin.activerecord.IBean;
import com.jfinal.plugin.activerecord.Model;

/**
 * Created by duai on 2017-07-22.
 */
public class BaseViewMachine<M extends BaseViewMachine<M>> extends Model<M> implements IBean {
    public void setId(java.lang.String id) {
        set("id", id);
    }

    public java.lang.String getId() {
        return get("id");
    }

    public void setTerminalId(java.lang.String terminalId) {
        set("terminal_id", terminalId);
    }

    public java.lang.String getTerminalId() {
        return get("terminal_id");
    }

    public void setName(java.lang.String name) {
        set("name", name);
    }

    public java.lang.String getName() {
        return get("name");
    }

    public void setDescription(java.lang.String description) {
        set("description", description);
    }

    public java.lang.String getDescription() {
        return get("description");
    }

    public void setCreateTime(java.lang.String createTime) {
        set("create_time", createTime);
    }

    public java.lang.String getCreateTime() {
        return get("create_time");
    }

    public void setCreateBy(java.lang.String createBy) {
        set("create_by", createBy);
    }

    public java.lang.String getCreateBy() {
        return get("create_by");
    }

    public void setStatus(java.lang.String status) {
        set("status", status);
    }

    public java.lang.String getStatus() {
        return get("status");
    }

    public void setTypeName(java.lang.String typeName) {
        set("type_name", typeName);
    }

    public java.lang.String getTypeName() {
        return get("type_name");
    }
    public void setManufactorName(java.lang.String manufactorName) {
        set("manufactor_name", manufactorName);
    }

    public java.lang.String getManufactorName() {
        return get("manufactor_name");
    }

    public void setMachineTypeId(java.lang.String machineTypeId) {
        set("machine_type_id", machineTypeId);
    }

    public java.lang.String getMachineTypeId() {
        return get("machine_type_id");
    }

    public void setManufactorId(java.lang.String manufactorId) {
        set("manufactor_id", manufactorId);
    }

    public java.lang.String getManufactorId() {
        return get("manufactor_id");
    }


    public void setSystemCommand(java.lang.String systemCommand) {
        set("system_command", systemCommand);
    }

    public java.lang.String getSystemCommand() {
        return get("system_command");
    }

    public void setSystemId(java.lang.String systemId) {
        set("system_id", systemId);
    }

    public java.lang.String getSystemId() {
        return get("system_id");
    }

    public void setIsMain(java.lang.String isMain) {
        set("is_main", isMain);
    }

    public java.lang.String getIsMain() {
        return get("is_main");
    }
    public void setDeviceId(java.lang.String deviceId) {
        set("device_id", deviceId);
    }

    public java.lang.String getDeviceId() {
        return get("device_id");
    }
}
