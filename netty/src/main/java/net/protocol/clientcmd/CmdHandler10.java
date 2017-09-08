package net.protocol.clientcmd;

import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Enhancer;

import exception.AgriException;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import msg.PackageMsg;
import net.protocol.ClientCmdHandler;
import net.protocol.Protocol;
import net.protocol.entity.CreatePlanCmd;
import net.protocol.entity.CreatePlanObject;
import net.util.BytesHelper;
import net.util.DateUtil;
import net.xgs.model.BaseTaskObject;
import net.xgs.model.BaseTaskPlan;
import net.xgs.services.MachineService;

/**
 * 创建灌溉周期计划
 * @author TianW
 *
 */
public class CmdHandler10 extends ClientCmdHandler {
//	private TaskPlanService taskPlanService = Enhancer.enhance(TaskPlanService.class);
	private MachineService machineService = Enhancer.enhance(MachineService.class);
	@Override
	public Object exec(String userId, String uuid, String msg, String sysId, Channel channel) throws AgriException {
		CreatePlanCmd entity = null;
		CreatePlanObject object = null;
		byte[] data = new byte[23];
		try {
			entity = JSON.parseObject(msg, CreatePlanCmd.class);
			object = entity.getParameterList();
			byte planNO = entity.getPlanNO();         //byte 灌溉计划编号
			byte entryNOsum = entity.getEntryNOsum();   // byte 灌溉计划条目总数
			byte entryNO = entity.getEntryNO();      // byte  灌溉条目编号
			String taskObjId = object.getTaskObjId();            //String
			byte[] taskObjIdb = BytesHelper.getObjIdByte(taskObjId);
			byte operation1 = object.getOperation1();          //byte 操作1
			byte operation2 = object.getOperation2();          //byte 操作2
			String beginTime = object.getBeginTime();           //String ,年月日时分秒
			byte[] beginTimeb = DateUtil.getbyteFromString(beginTime);
			String endTime = object.getEndTime();           //String,年月日时分秒
			byte[] endTimeb = DateUtil.getbyteFromString(endTime);
			short cycleTime = object.getCycleTime();          //short,  循环周期
			byte[] cycleTimeb = BytesHelper.shortToByteArray(cycleTime);
			short intervalTime = object.getIntervalTime();       //short   操作1和操作2间隔时间
			byte[] intervalTimeb = BytesHelper.shortToByteArray(intervalTime);
		
			data[0] = planNO;
			data[1] = entryNOsum;
			data[2] = entryNO;
			data[3] = 0; //任务类型，暂时固定为周期性定时任务
			System.arraycopy(taskObjIdb, 0, data, 4, 3);
			data[7] = operation1;
			data[8] = operation2;
			System.arraycopy(beginTimeb, 0, data, 9, 5);
			System.arraycopy(endTimeb, 0, data, 14, 5);
			System.arraycopy(cycleTimeb, 0, data, 19, 2);
			System.arraycopy(intervalTimeb, 0, data, 21, 2);
		} catch (Exception e) {
			e.printStackTrace();
			int code = prop.getInt("error.format");
			throw new AgriException(code,prop.get(code+"",e.getMessage()));
		}
		
		
		
		Map<String, Channel> mapDeviceChannel = mc.getDeviceToChannel();
		//箱子的管道
		Channel ch = mapDeviceChannel.get(sysId);
		if(ch!=null) {
			String sysIdStr = new String(sysId);
			BaseTaskPlan baseTaskPlan = new BaseTaskPlan();
			baseTaskPlan.setBlockId(entity.getBlockId());
			baseTaskPlan.setSystemId(sysIdStr);
			baseTaskPlan.setTaskName(entity.getTaskName());
			baseTaskPlan.setMachineId(machineService.findByDeviceMain(sysIdStr).getId());
			baseTaskPlan.setTaskType(entity.getTaskType());
			baseTaskPlan.setTaskNo(Integer.valueOf(entity.getPlanNO()));
			baseTaskPlan.setTaskNum(Integer.valueOf(entity.getEntryNOsum()));
			BaseTaskObject baseTaskObject = new BaseTaskObject();
			baseTaskObject.setMachineId(machineService.findByDevice(sysIdStr,object.getTaskObjId()).getId());
			baseTaskObject.setDeviceId(object.getTaskObjId());
			baseTaskObject.setParameterList(JSONObject.toJSONString(object));
			
			ByteBuf cmdMsg = PackageMsg.packingDevice(BytesHelper.getSystemIdByte(sysId), BytesHelper.toBytes(++cmdNO), Protocol.CREATE_PLAN_BYTE, data);
			ch.writeAndFlush(cmdMsg);
			logger.debug("创建灌溉计划命令发送成功");
			String msgId = sysId + cmdNO;
			Map<String, String> msgIDtoUser = msgMapping.getMsgIdtoUser();
			Map<String, String> msgIdtoUUID = msgMapping.getMsgIdtoUUID();
			msgIDtoUser.put(msgId, userId);
			msgIdtoUUID.put(msgId, uuid);
		}else {
			//TODO 给手机端返回错误信息
			int code = prop.getInt("error.wrongSysId");
			throw new AgriException(code,prop.get(code+""));
		}
		return null;
	}

}
