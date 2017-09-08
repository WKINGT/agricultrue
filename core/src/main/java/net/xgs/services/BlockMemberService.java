package net.xgs.services;

import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.tx.Tx;
import net.xgs.commons.annotation.Service;
import net.xgs.entity.edomain.StatusEnum;
import net.xgs.model.BaseMemberBlock;
import net.xgs.model.ViewBlockMember;
import net.xgs.query.FilterBuilder;
import org.apache.commons.lang.StringUtils;

import java.util.List;

/**
 * Created by duai on 2017-07-24.
 */
@Service
public class BlockMemberService extends BaseService{
    public Page<ViewBlockMember> page(Integer pageNumber, Integer pageSize, FilterBuilder builder){
        String select = "select * ";
        String sqlExceptSelect = "from view_member_block where status = ? ";
        builder.getParams().add(0,StatusEnum.NORMAL_USE.getValue());
        sqlExceptSelect += builder.getSnippets();
        return ViewBlockMember.dao.paginate(pageNumber, pageSize, select, sqlExceptSelect, builder.getParams().toArray());
    }

    public List<ViewBlockMember> findbByMemeber(String memberId){
        String select = "select * from view_member_block where status = ? AND   member_id = ?  ORDER BY create_time desc";
        return ViewBlockMember.dao.find(select,StatusEnum.NORMAL_USE.getValue(),memberId);
    }

    public ViewBlockMember findById(String id){
        return ViewBlockMember.dao.findById(id);
    }

    /**
     * 判断数据是否已存在
     * @param blockId
     * @param memberId
     * @return
     */
    public Boolean existByMemberAndBlock(String blockId,String memberId){
        List<BaseMemberBlock> list = BaseMemberBlock.dao.find("SELECT id FROM view_member_block WHERE `status` = ? AND block_id = ? AND member_id = ?", StatusEnum.NORMAL_USE.getValue(),blockId,memberId);
        if (list.size()>0){
            return true;
        }
        return false;
    }
    @Before(Tx.class)
    public Boolean save(BaseMemberBlock baseMemberBlock, String optId) {
        if (StringUtils.isBlank(baseMemberBlock.getId())){
            baseMemberBlock.setId(getUUID());
            baseMemberBlock.setCreateBy(optId);
            baseMemberBlock.setCreateTime(getDateTime());
            return baseMemberBlock.save();
        }
        return baseMemberBlock.update();
    }
    public Boolean save(String blockId,String memberId, String optId) {
        BaseMemberBlock baseMemberBlock = new BaseMemberBlock();
        baseMemberBlock.setMemberId(memberId);
        baseMemberBlock.setBlockId(blockId);
        return save(baseMemberBlock,optId);
    }
    public Boolean save(String[] blockIds,String memberId, String optId) {
        Db.update("UPDATE  base_member_block SET status = 1 WHERE member_id = ?",memberId);
        for (String blockId:blockIds){
            save(blockId,memberId,optId);
        }
        return true;
    }

    public Boolean delete(String[] ids) {
        for (String id:ids){
            Db.update("UPDATE  base_member_block SET status = 1 WHERE id = ?",id);
        }
        return true;
    }

    /**
     * 根据箱子查询所属用户
     * @param systemId 箱子系统ID
     * @param isMain 是否为控制设备（1、是0、否）查询箱子是传入1
     * @return
     */
    public List<ViewBlockMember> findByMachine(String systemId,Integer isMain){
        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT member.* FROM view_member_block AS member ");
        sb.append(" LEFT JOIN view_machine_block AS machine ");
        sb.append(" ON machine.block_id = member.block_id ");
        sb.append(" LEFT JOIN view_machine_type_manufactor AS type ");
        sb.append(" ON type.id = machine.machine_id ");
        sb.append(" WHERE type.is_main = ? AND member.`status` = ? AND ");
        sb.append(" system_id = ?");
        List<ViewBlockMember> list = ViewBlockMember.dao.find(sb.toString(),isMain, StatusEnum.NORMAL_USE.getValue(),systemId);
        return list;
    }
}
