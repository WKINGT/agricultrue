package net.xgs.controller.block;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.plugin.activerecord.Page;
import net.xgs.commons.annotation.Inject;
import net.xgs.controller.core.BaseController;
import net.xgs.entity.Constants;
import net.xgs.model.*;
import net.xgs.services.BlockMemberService;
import net.xgs.services.BlockService;
import net.xgs.services.MemberService;
import net.xgs.services.OrgService;
import org.apache.commons.lang.StringUtils;

import java.util.List;

/**
 * Created by duai on 2017-07-24.
 */
public class BlockUserController extends BaseController {
    @Inject
    BlockMemberService blockMemberService;
    @Inject
    OrgService orgService;
    @Inject
    BlockService blockService;
    @Inject
    MemberService memberService;
    public void index(){
        if (isGet){
            renderJsp("admin/block/member/list.jsp");
        }else {
            filterBuilder.setSnippets(filterBuilder.getSnippets()+" AND pass like ?");
            filterBuilder.getParams().add(controller.getOrg().getPass()+"%");
            Page<ViewBlockMember> page = blockMemberService.page(pageNo,pageSize,filterBuilder);
            renderHtml(JSONObject.toJSONString(page));
        }
    }

    public void save(){
        if (isGet){
            String id = getParam("id");
            if (StringUtils.isNotBlank(id)){
                setAttr("viewBlockMember",blockMemberService.findById(id));
                setAttr("name","修改");
            }else {
                setAttr("name","添加");
            }
            List<BaseMember> list = memberService.findByPass(controller.getOrg().getPass());
            controller.setAttr("userdata",list);
            List<BaseBlock> blocks = blockService.findBlockByMemberId(getSessionUserId());
            controller.setAttr("blocks",blocks);
            renderJsp("admin/block/member/update.jsp");
        }else {
            BaseMemberBlock baseMemberBlock = getModel(BaseMemberBlock.class,"viewBlockMember");
            if (blockMemberService.existByMemberAndBlock(baseMemberBlock.getBlockId(),baseMemberBlock.getMemberId())){
                msg.setCode(1);
                msg.setMsg("选择用户以管理所选区块，请勿重复添加！");
                renderHtml(JSONObject.toJSONString(msg));
                return;
            }
            Boolean result = blockMemberService.save(baseMemberBlock,getSessionUserId());
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
        Boolean result =  blockMemberService.delete(ids);
        if (result){
            renderHtml(JSONObject.toJSONString(Constants.deleteSuccess));
        }else {
            renderHtml(JSONObject.toJSONString(Constants.deleteFail));
        }
    }
}
