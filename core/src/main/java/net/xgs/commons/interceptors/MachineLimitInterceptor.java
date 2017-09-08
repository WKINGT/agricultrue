package net.xgs.commons.interceptors;

import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang.StringUtils;

import com.jfinal.aop.Before;
import com.jfinal.aop.Enhancer;
import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.kit.Prop;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.tx.Tx;

import net.xgs.commons.utils.DateUtils;
import net.xgs.entity.edomain.DataTypeEnum;
import net.xgs.model.BaseAlarmMsg;
import net.xgs.model.BaseMachineLimit;
import net.xgs.services.AlarmMsgService;
import net.xgs.services.MachineLimitService;

/**
 * Created by duai on 2017-08-15.
 * 智能报警
 */
public class MachineLimitInterceptor implements Interceptor {
    MachineLimitService machineLimitService = Enhancer.enhance(MachineLimitService.class);
    AlarmMsgService alarmMsgService = Enhancer.enhance(AlarmMsgService.class);
    Prop prop = PropKit.use("deviceError.txt");
    Prop systemProp = PropKit.use("cfg.properties");
    @Override
    @Before(Tx.class)
    public void intercept(Invocation invocation) {
        invocation.invoke();
        Object[] param = invocation.getArgs();
        Integer dataType = Integer.parseInt(String.valueOf(param[2]));
        BaseMachineLimit machineLimit = machineLimitService.findLimitByMachineId(String.valueOf(param[0]),dataType);
        if (machineLimit!=null){
            saveAlarm(Integer.parseInt(String.valueOf(param[1]),16),machineLimit);
        }
    }
    private void saveAlarm(Integer data, BaseMachineLimit machineLimit){
        String code = "";
        String propName = "";
        Integer tData = 0;//设置阈值
        if (DataTypeEnum.LUX.getValue().equals(machineLimit.getDataType())){
            propName = "lightsensor";
        }else if (DataTypeEnum.TEMPERATURE.getValue().equals(machineLimit.getDataType())){
            propName = "temperature";
        }else if (DataTypeEnum.HUMIDITY.getValue().equals(machineLimit.getDataType())){
            propName = "humidity";
        }
        if (machineLimit.getMaxValue()<=data){
            code = prop.get(propName+".exceedsMaxValue");
            tData = machineLimit.getMaxValue();
        }
        if (machineLimit.getMinValue()>=data){
            code = prop.get(propName+".exceedsMinValue");
            tData = machineLimit.getMinValue();
        }
        if (StringUtils.isBlank(code)) return;
        BaseAlarmMsg baseAlarmMsg = alarmMsgService.findMaxByCode(machineLimit.getMachineId(),code);
        if (baseAlarmMsg!=null && DateUtils.isNotBlank(baseAlarmMsg.getCreateTime())){
            Date now = new Date();
            Date createTime = DateUtils.parse(baseAlarmMsg.getCreateTime(),DateUtils.PATTERN_DATETIME);if (DateUtils.add(now, Calendar.HOUR_OF_DAY, -systemProp.getInt("system.alarmMsgTime")).before(createTime)){
                return;
            }
        }
        alarmMsgService.save(machineLimit.getMachineId(),code,data, tData);
    }
}
