package net.xgs.controller.block;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.plugin.activerecord.Page;
import net.xgs.commons.annotation.Inject;
import net.xgs.controller.core.BaseController;
import net.xgs.entity.Constants;
import net.xgs.init.XgsConfig;
import net.xgs.model.BaseBlock;
import net.xgs.model.BaseBlockHistoryData;
import net.xgs.services.BlockDataService;
import net.xgs.services.BlockService;
import org.apache.commons.lang.StringUtils;

import java.util.List;

/**
 * Created by duai on 2017-08-11.
 */
public class BlockDataController extends BaseController {
    @Inject
    BlockDataService blockDataService;
    @Inject
    BlockService blockService;
    public void index(){
        if (isGet){
            renderJsp("admin/block/data/list.jsp");
        }else {
            Page<BaseBlockHistoryData> page = blockDataService.page(pageNo,pageSize,filterBuilder);
            renderHtml(JSONObject.toJSONString(page));
        }
    }

    public void save(){
        if (isGet){
            String id = getParam("id");
            if (StringUtils.isNotBlank(id)){
                setAttr("block_data",blockDataService.findById(id));
                setAttr("name","修改");
            }else {
                setAttr("name","添加");
            }
            List<BaseBlock> blocks = blockService.findBlockByMemberId(getSessionUserId());
            controller.setAttr("blocks",blocks);
            renderJsp("admin/block/data/update.jsp");
        }else {
            BaseBlockHistoryData historyData = getModel(BaseBlockHistoryData.class,"block_data");
            boolean result = blockDataService.save(historyData,getSessionUserId());
            if (result){
                renderHtml(JSONObject.toJSONString(Constants.editSuccess));
            }else {
                renderHtml(JSONObject.toJSONString(Constants.editFail));
            }
        }
    }
    public void update(){
        save();
    }
    public void delete(){
        String[] ids =  getParam("ids").split(",");
        Boolean result =  blockDataService.delete(ids);
        if (result){
            renderHtml(JSONObject.toJSONString(Constants.deleteSuccess));
        }else {
            renderHtml(JSONObject.toJSONString(Constants.deleteFail));
        }
    }

    public void blockQr(){
        String content = XgsConfig.prop.get("http.qrCode.block.post.info")+getParam("blockId");
        this.controller.renderQrCode(content,200,200);
    }
    public static void main(String[] args) {
        String str = " http://192.168.10.95:8889/front?cltPath=block&actPath=index&param=agriculture://block/f26b396d703111e7ae6efcaa145e7e0f ";
        String protocol = str.substring(str.indexOf("agriculture://block/"));
        System.out.println(protocol);
    }

}
