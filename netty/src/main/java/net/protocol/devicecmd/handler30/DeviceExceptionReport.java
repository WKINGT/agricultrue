package net.protocol.devicecmd.handler30;

import net.xgs.services.AlarmMsgService;

public class DeviceExceptionReport {
	private static AlarmMsgService service = new AlarmMsgService();
	public static void report(String systemId,String deviceId,int deviceType,String dataValue) {
		String[] dataValues = dataValue.split(",");
		int value = Integer.parseInt(dataValues[0], 16);
		String code = String.valueOf(value);
		switch (deviceType) {
		case 0x00://电动阀 
		case 0x01://电磁阀
		case 0x02://水泵 
		case 0x03://风机
		case 0x05://生长灯
		case 0x06://加热器
		case 0x07://除湿器
			if(!(dataValue.equals("00")||dataValue.equals("64"))) {
				service.save(systemId, deviceId, code);
			}
			break;
			
		case 0x04://卷帘机
			break;
		
		case 0x40://土壤湿度传感器
		case 0x48://空气湿度传感器
			if(value<0||value>100) {
				service.save(systemId, deviceId, code);
			}
			break;
		case 0x41://土壤温度传感器 
		case 0x49://空气温度传感器
			if(value<-100||value>100) {
				service.save(systemId, deviceId, code);
			}
			break;
		case 0x42://土壤温湿度传感器
		case 0x4A://空气温湿度传感器
			int value1 = Integer.parseInt(dataValues[1], 16);
			String code1 = String.valueOf(value1);
			if(value<-100||value>100) {
				service.save(systemId, deviceId, code);
			}
			if(value1<0||value1>100) {
				service.save(systemId, deviceId, code1);
			}
			break;
		case 0x50://光照传感器
			if(value>1000000) {
				service.save(systemId, deviceId, code);
			}
			break;
		case 0x81://田间控制器
			if(value!=252) {
				service.save(systemId, deviceId, code);
			}
			break;
		}
	}
}
