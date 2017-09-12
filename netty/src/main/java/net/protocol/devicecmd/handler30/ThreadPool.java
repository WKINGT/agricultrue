package net.protocol.devicecmd.handler30;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.jfinal.aop.Enhancer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.xgs.model.BaseMachine;
import net.xgs.services.AlarmMsgService;
import net.xgs.services.MachineDataService;
import net.xgs.services.MachineService;

public class ThreadPool{
	Logger logger = LoggerFactory.getLogger(getClass());
	
	MachineDataService machineDataService = Enhancer.enhance(MachineDataService.class);
	MachineService machineService =  Enhancer.enhance(MachineService.class);
	AlarmMsgService service =  Enhancer.enhance(AlarmMsgService.class);
	private ExecutorService singlExecutorService;
	private static ThreadPool pool;
	
	private ThreadPool(){
		singlExecutorService = Executors.newSingleThreadExecutor(new AgriThreadFactory());
	}
	
	public static ThreadPool instance(){
		if(pool == null){
			pool = new ThreadPool();
		}
		return pool;
	}
	
	public ExecutorService get(){
		return this.singlExecutorService;
	}
	
	public void execSaveReportMsg(String systemId, String deviceIdString,String data, Integer dataType,String jointData){
		this.singlExecutorService.execute(new Runnable() {
			public void run() {
				BaseMachine baseMachine = machineService.findByDevice(systemId,deviceIdString);
				if(baseMachine==null) {
					return;
				}
				machineDataService.save(baseMachine.getId(), data, dataType, jointData);
			}
		});
	}
	public void execSaveReportExceptionMsg(String systemId,String deviceId,int deviceType,String dataValue) {
		this.singlExecutorService.execute(new Runnable() {
			public void run() {
				String[] dataValues = dataValue.split(",");
				int value = Integer.parseInt(dataValues[0], 16);
				String code = String.valueOf(value);
				String errcode = "253";
				switch (deviceType) {
				case 0x00://电动阀 
				case 0x01://电磁阀
				case 0x02://水泵 
				case 0x03://风机
				case 0x05://生长灯
				case 0x06://加热器
				case 0x07://除湿器
					if(!dataValue.equals("0")&&!dataValue.equals("64")) {
						service.save(systemId, deviceId, code);
					}
					break;
					
				case 0x04://卷帘机
					break;
				
				case 0x40://土壤湿度传感器
				case 0x48://空气湿度传感器
					if(value<0||value>100) {
						service.save(systemId, deviceId, errcode);
					}
					break;
				case 0x41://土壤温度传感器 
				case 0x49://空气温度传感器
					if(value<-100||value>100) {
						service.save(systemId, deviceId, errcode);
					}
					break;
				case 0x42://土壤温湿度传感器
				case 0x4A://空气温湿度传感器
					int value1 = Integer.parseInt(dataValues[1], 16);
//					String code1 = String.valueOf(value1);
					if(value<-100||value>100) {
						service.save(systemId, deviceId, errcode);
					}
					if(value1<0||value1>100) {
						service.save(systemId, deviceId, errcode);
					}
					break;
				case 0x50://光照传感器
					if(value>1000000) {
						service.save(systemId, deviceId, errcode);
					}
					break;
				case 0x81://田间控制器
					if(value!=252) {
						service.save(systemId, deviceId, code);
					}
					break;
				}
			}
		});
	}
	
	
}