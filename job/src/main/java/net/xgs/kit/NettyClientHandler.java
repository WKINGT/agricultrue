package net.xgs.kit;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Enhancer;
import com.jfinal.kit.PropKit;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import net.xgs.config.JobConfig;
import net.xgs.core.NettySessionManager;
import net.xgs.entity.SessionEntity;
import net.xgs.entity.ResultEntity;
import net.xgs.entity.edomain.AlarmEnum;
import net.xgs.entity.edomain.BooleanEnum;
import net.xgs.services.AlarmMsgService;
import net.xgs.utils.RSAEncrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NettyClientHandler extends SimpleChannelInboundHandler<byte[]> {
    static AlarmMsgService alarmMsgService = Enhancer.enhance(AlarmMsgService.class);
    Logger logger = LoggerFactory.getLogger(NettyClientHandler.class);
    static RSAEncrypt rsaEncrypt= new RSAEncrypt(JobConfig.RSA.get("private.key"),JobConfig.RSA.get("public.key"));
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, byte[] msg) throws Exception {
        byte[] plainText = rsaEncrypt.decrypt(rsaEncrypt.getPrivateKey(), msg);
        ResultEntity resultEntity =  JSON.parseObject(new String(plainText,"utf-8"),ResultEntity.class);
        SessionEntity sessionEntity = NettySessionManager.getSession(resultEntity.getUuid());
        if (resultEntity.getCode()== BooleanEnum.FALSE.getValue()){
            logger.error("任务执行失败！对应参数--->"+JSONObject.toJSONString(sessionEntity));
            if (sessionEntity==null){
                logger.error("当前相应session查询失败--->"+JSONObject.toJSONString(sessionEntity));
                return;
            }
            alarmMsgService.save(sessionEntity.getViewMachineParams().getStr("machine_id"),
                    sessionEntity.getViewMachineParams().getStr("name"),
                    PropKit.use("deviceError.txt").get("job.execute.error")
                    , AlarmEnum.JOB.getValue(),
                    sessionEntity.getBaseTaskPlanJob().getId());
        }else {
            logger.info("任务执行成功对应参数--->"+JSONObject.toJSONString(sessionEntity));
        }
        ctx.close();
    }
    
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Client active ");
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Client close ");
        super.channelInactive(ctx);
    }
}