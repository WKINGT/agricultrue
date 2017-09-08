package net.xgs.controller.block;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.plugin.activerecord.Page;
import net.xgs.commons.annotation.Inject;
import net.xgs.controller.core.BaseController;
import net.xgs.entity.Constants;
import net.xgs.model.BaseBlock;
import net.xgs.model.ViewBlockMember;
import net.xgs.services.BlockService;
import org.apache.commons.lang.StringUtils;

/**
 * Created by duai on 2017-07-22.
 * 区域
 */
public class BlockController extends BaseController {
    @Inject
    BlockService blockService;
    public void index(){
        if (isGet){
            renderJsp("admin/block/list/list.jsp");
        }else {
          filterBuilder.setSnippets(filterBuilder.getSnippets()+" AND member_id = ?");
          filterBuilder.getParams().add(getSessionUserId());
          Page<ViewBlockMember> page = blockService.page(pageNo,pageSize,filterBuilder);
          renderHtml(JSONObject.toJSONString(page));
        }
    }

    public void save(){
        if (isGet){
            String id = getParam("id");
            if (StringUtils.isNotBlank(id)){
                BaseBlock baseBlock = blockService.getById(id);
                setAttr("block",baseBlock);
                setAttr("name","修改");
            }else {
                setAttr("name","添加");
            }
            renderJsp("admin/block/list/update.jsp");
        }else {
            BaseBlock baseBlock = getModel(BaseBlock.class,"block");
            boolean result =  blockService.saveBlock(baseBlock,getSessionUserId(),getParam("machineIds"));
            if (result){
                msg = Constants.editSuccess;
            }else {
                msg = Constants.editFail;
            }
            renderHtml(JSONObject.toJSONString(msg));
        }
    }
    public void update(){
       save();
    }
    public void delete(){
        String [] ids =  getParam("ids").split(",");
        Integer result = blockService.deleteBlock(ids,getSessionUserId());
        if (result==0){
            msg = Constants.deleteSuccess;
        }else{
            msg.setCode(1);
            msg.setMsg("当前区块正在使用！不可删除！");
        }
        renderHtml(JSONObject.toJSONString(msg));
    }
}
