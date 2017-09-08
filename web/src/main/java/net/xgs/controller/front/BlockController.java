package net.xgs.controller.front;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.kit.JsonKit;
import net.xgs.commons.annotation.Inject;
import net.xgs.controller.core.BaseController;
import net.xgs.entity.Msg;
import net.xgs.init.XgsConfig;
import net.xgs.model.BaseBlockHistoryData;
import net.xgs.services.BlockDataService;
import org.apache.commons.lang.StringUtils;

/**
 * Created by duai on 2017-08-11.
 */
public class BlockController extends BaseController{
    @Inject
    BlockDataService blockDataService;
    public void index(){
        if (isGet){
             renderJsp("/WEB-INF/jsp/front/blockInfo.jsp");
        }else {
            String blockId = getParam("blockId");
            if (StringUtils.isBlank(blockId)){//如果是微信端访问将从url截取数据
                String param = getParam("param");
                blockId = param.replace(XgsConfig.prop.get("http.qrCode.block.id"),"");
            }
            BaseBlockHistoryData baseBlockHistoryData = blockDataService.findMaxCreateTimeByBlock(blockId);
            if (baseBlockHistoryData==null){
                renderHtml(JSONObject.toJSONString(msg.setCode(1).setMsg("数据请求失败！")));
                return;
            }
            baseBlockHistoryData = blockDataService.findInfoByBlockId(baseBlockHistoryData);
            msg.setData(baseBlockHistoryData);
            renderHtml(JsonKit.toJson(msg));
        }
    }
}
