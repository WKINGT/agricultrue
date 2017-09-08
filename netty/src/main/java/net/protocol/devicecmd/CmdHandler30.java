package net.protocol.devicecmd;


import io.netty.channel.Channel;
import net.protocol.DeviceCmdHandler;
import net.protocol.devicecmd.handler30.ThreadPool;
import net.util.BytesHelper;
import net.xgs.entity.edomain.DataTypeEnum;
/**
 * 信息上报命令
 * @author TianW
 *
 */
import net.xgs.services.MachineDataService;
import net.xgs.services.MachineService;
public class CmdHandler30 extends DeviceCmdHandler {
	MachineDataService machineDataService = new MachineDataService();
	MachineService machineService = new MachineService();
	@Override
	public Object exec(byte[] sysIdbyte, byte[] cmdNo, byte cmd, byte[] data, Channel channel) {
		logger.debug("%n=={}，信息上报命令,箱子主动上报信息的data内容16进制{}" ,channel.remoteAddress(), BytesHelper.byte2hex(data));
		
		int i = 0;
		byte year = data[i++];
		byte month = data[i++];
		byte day = data[i++];
		byte hour = data[i++];
		byte minute = data[i++];
		byte second = data[i++];
////		String time = DateUtil.getStringTime(BytesHelper.getUnsignedInt(year), BytesHelper.getUnsignedInt(month), BytesHelper.getUnsignedInt(day), BytesHelper.getUnsignedInt(hour), BytesHelper.getUnsignedInt(minute), BytesHelper.getUnsignedInt(second));
		byte type = data[i++];
		String systemId = BytesHelper.getSystemId(sysIdbyte);
		int j = 1; //上报设备个数
		while(i!=data.length) {
			byte[] deviceId = new byte[3];
			System.arraycopy(data, i, deviceId, 0, 3);
			i += 3;
			String deviceIdString = BytesHelper.getTwoNumByte(BytesHelper.getUnsignedInt(deviceId[0]))+"-"+
					BytesHelper.getTwoNumByte(BytesHelper.getUnsignedInt(deviceId[1]))+"-"+
					BytesHelper.getTwoNumByte(BytesHelper.getUnsignedInt(deviceId[2]));
			int deviceType = BytesHelper.getUnsignedInt(deviceId[1]);
////			BaseMachine baseMachine = machineService.findByDevice(systemId,deviceIdString);
			String dataValue = "";
			if(deviceType==0x50) {//光照传感器
				byte[] valueB = new byte[4];
				System.arraycopy(data, i, valueB, 0, 4);
				i += 4;
				int value = BytesHelper.bytesToInt2((valueB));
				dataValue =Integer.toHexString(value);
////				machineDataService.save(baseMachine.getId(), dataValue, DataTypeEnum.LUX.getValue(),dataValue);
				ThreadPool.instance().execSaveReportMsg(systemId, deviceIdString, dataValue, DataTypeEnum.LUX.getValue(),dataValue);
				ThreadPool.instance().execSaveReportExceptionMsg(systemId,deviceIdString,deviceType,dataValue);
			} else if(deviceType==0x4a||deviceType==0x42) { //空气温湿度传感器
				byte[] valueB = new byte[2];
				System.arraycopy(data, i, valueB, 0, 2);
				i += 2;
				int tempValue = BytesHelper.getUnsignedInt(valueB[0]);//空气温度
				int humidityValue = BytesHelper.getUnsignedInt(valueB[1]);//空气湿度
				String jionData = Integer.toHexString(tempValue)+","+Integer.toHexString(humidityValue);
////				machineDataService.save(baseMachine.getId(), Integer.toHexString(tempValue), DataTypeEnum.TEMPERATURE.getValue(),jionData);
////				machineDataService.save(baseMachine.getId(), Integer.toHexString(humidityValue), DataTypeEnum.HUMIDITY.getValue(),jionData);
				ThreadPool.instance().execSaveReportMsg(systemId, deviceIdString, Integer.toHexString(tempValue), DataTypeEnum.TEMPERATURE.getValue(),jionData);
				ThreadPool.instance().execSaveReportMsg(systemId, deviceIdString, Integer.toHexString(humidityValue), DataTypeEnum.HUMIDITY.getValue(),jionData);
				
				ThreadPool.instance().execSaveReportExceptionMsg(systemId,deviceIdString,deviceType,jionData);
			} else {
				int value = BytesHelper.getUnsignedInt(data[i++]);
				dataValue = Integer.toHexString(value);
				Integer dateType = 1;
				switch (deviceType) {
					case 0x40:
					case 0x48:
						dateType = DataTypeEnum.HUMIDITY.getValue();
						break;
					case 0x41:
					case 0x49:
						dateType = DataTypeEnum.TEMPERATURE.getValue();
						break;
					default:
						dateType = 0;//其他设备（电磁阀、电动阀、水泵、风机等可操作设备）
						break;
				}
////				machineDataService.save(baseMachine.getId(), dataValue, dateType, dataValue);
				ThreadPool.instance().execSaveReportMsg(systemId, deviceIdString, dataValue, dateType,dataValue);
				
				ThreadPool.instance().execSaveReportExceptionMsg(systemId,deviceIdString,deviceType,dataValue);
			}

			j++;
		}
		
		return null;
	}

}
