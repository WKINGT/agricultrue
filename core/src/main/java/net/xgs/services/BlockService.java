package net.xgs.services;

import java.util.ArrayList;
import java.util.List;

import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.tx.Tx;

import net.xgs.commons.annotation.Inject;
import net.xgs.commons.annotation.Service;
import net.xgs.entity.edomain.BooleanEnum;
import net.xgs.entity.edomain.StatusEnum;
import net.xgs.model.BaseBlock;
import net.xgs.model.BaseMachine;
import net.xgs.model.BaseMemberBlock;
import net.xgs.model.ViewBlockMember;
import net.xgs.query.FilterBuilder;
import net.xgs.utils.ObjectUtils;
import org.apache.commons.lang.StringUtils;

@Service
public class BlockService extends BaseService {
	@Inject
	BlockMemberService blockMemberService;
	@Inject
	MachineBlockService machineBlockService;
	public Page<ViewBlockMember> page(int pageNumber, int pageSize, FilterBuilder builder) {
		String select = "select id,block_name,address,block_acreage,history_data,member_id,create_time";
		String sqlExceptSelect = "from view_block_history_data where 1 = 1 ";
		sqlExceptSelect += builder.getSnippets() + "  ORDER BY create_time desc";
		return ViewBlockMember.dao.paginate(pageNumber, pageSize, select, sqlExceptSelect, builder.getParams().toArray());
	}

	public BaseBlock getById(String id) {
		String sql = "select * from base_block where id = ? and status = ? ";
		return BaseBlock.dao.findFirst(sql, id, StatusEnum.NORMAL_USE.getValue());
	}
	
	/**
	 * 查询用户下属的区域
	 * @param MemberId
	 * @return
	 */
	public List<BaseBlock> findBlockByMemberId(String MemberId) {
		return findBlockByMemberId(MemberId,"bb.*");
	}
	/**
	 * 查询用户下属的区域
	 * @param MemberId
	 * @return
	 */
	public List<BaseBlock> findBlockNameByMemberId(String MemberId) {
		return findBlockByMemberId(MemberId,"bb.id,bb.name");
	}
	/**
	 * 查询用户下属的区域
	 * @param MemberId
	 * @return
	 */
	public List<BaseBlock> findBlockByMemberId(String MemberId,String column) {
		String sql = "select "+column+" from base_block bb ";
		sql += "left join base_member_block bmb on bmb.block_id = bb.id where 1 = 1 ";
		List<Object> param = new ArrayList<>();
		sql += "and bmb.member_id = ? ";
		param.add(MemberId);
		sql += "and bb.status = ? ";
		param.add(StatusEnum.NORMAL_USE.getValue());
		sql += "and bmb.status = ? ";
		param.add(StatusEnum.NORMAL_USE.getValue());
		sql += "order by bb.create_time desc ";
		return BaseBlock.dao.find(sql, param.toArray());
	}

	/**
	 * 查询用户下属的区域ID
	 * @param memberId
	 * @return
	 */
	public List<String> findBlockIdByMemberId(String memberId) {
		String sql = "select block_id from view_member_block where member_id = ? ";
		List<ViewBlockMember> result = ViewBlockMember.dao.find(sql, memberId);
		return ObjectUtils.getMethodValue(result,"getBlockId");
	}
	
	/**
	 * 新增区块
	 * @param baseBlock
	 * @param optId
	 * @return
	 */
	public boolean saveBlock(BaseBlock baseBlock, String optId,String machineIds) {

		if (StringUtils.isBlank(baseBlock.getId())){
			baseBlock.setId(getUUID());
			baseBlock.setStatus(StatusEnum.NORMAL_USE.getValue());
			baseBlock.setCreateTime(getDateTime());
			baseBlock.setCreateBy(optId);
			BaseMemberBlock baseMemberBlock = new BaseMemberBlock();
			baseMemberBlock.setBlockId(baseBlock.getId());
			baseMemberBlock.setMemberId(optId);
			blockMemberService.save(baseMemberBlock,optId);
			if (StringUtils.isNotBlank(machineIds))
				machineBlockService.save(baseBlock.getId(),machineIds);
			return baseBlock.save();
		}else {
			if (StringUtils.isNotBlank(machineIds))
				machineBlockService.save(baseBlock.getId(),machineIds);
			return baseBlock.update();
		}
	}
	

	/**
	 * 删除区块-逻辑
	 * 会删除用户与区块的关系-逻辑
	 * 会删除区块与设备的关系-物理
	 * @param ids
	 * @return 0:成功 1:正在使用 2：异常
	 */
	@Before(Tx.class)
	public Integer deleteBlock(String[] ids) {
		for (String id : ids){
			Record base_block_machine = Db.findFirst("select id from base_block_machine where block_id = ?",id);
			if (base_block_machine!=null) return 1;
			Record base_member_block = Db.findFirst("select id from base_member_block where block_id = ?",id);
			if (base_member_block!=null) return 1;
			Db.update("update base_block set status = ? where id = ? ", BooleanEnum.FALSE.getValueStr(), id);
		}
		//TODO: log who delete
		return 0;
	}
	
	/**
	 * 查询区块里的设备
	 * @param blockId
	 * @return
	 */
	public List<BaseMachine> findMachineByBlockId(String blockId) {
		String sql = "select bm.* from base_machine bm ";
		sql += "left join base_block_machine bbm on bbm.machine_id = bm.id where 1 = 1 ";
		List<Object> param = new ArrayList<>();
		sql += "and bbm.block = ? ";
		param.add(blockId);
		sql += "and bm.status = ? ";
		param.add(StatusEnum.NORMAL_USE.getValue());
		sql += "order by bm.create_time desc ";
		return BaseMachine.dao.find(sql, param.toArray());
	}
	
	/**
	 * 为区块添加设备
	 * @param blockId
	 * @param machineIds
	 * @return
	 */
	public boolean addMachineToBlock(String blockId, String... machineIds) throws Exception {
		String insertSql = "insert into base_block_machine values(?, ?, ?)";
		List<Object[]> paras = new ArrayList<>();
		for (int i = 0; i < machineIds.length; i++) {
			Object[] para = new Object[]{getUUID(), blockId, machineIds[i]};
			paras.add(para);
		}
		Db.batch(insertSql, paras.toArray(new Object[][]{}), paras.size());
		return true;
	}
	
	/**
	 * 移除区块中的设备-物理
	 * @param blockId
	 * @param machineIds
	 * @return
	 * @throws Exception
	 */
	public boolean removeMachineFromBlock(String blockId, String... machineIds) throws Exception {
		//TODO:log who delete
		String deleteSql = "delete from base_block_machine where block_id = ? and machine_id = ? ";
		List<Object[]> paras = new ArrayList<>();
		for (int i = 0; i < machineIds.length; i++) {
			Object[] para = new Object[]{blockId, machineIds[i]};
			paras.add(para);
		}
		Db.batch(deleteSql, paras.toArray(new Object[][]{}), paras.size());
		return true;
	}
}
