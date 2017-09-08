package net.xgs.services;

import java.util.List;

import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.tx.Tx;

import com.jfinal.plugin.redis.Cache;
import com.jfinal.plugin.redis.Redis;
import net.xgs.commons.annotation.Inject;
import net.xgs.commons.annotation.Service;
import net.xgs.entity.Constants;
import net.xgs.entity.edomain.StatusEnum;
import net.xgs.exception.TurburException;
import net.xgs.init.XgsConfig;
import net.xgs.model.*;
import net.xgs.query.FilterBuilder;
import net.xgs.utils.DBUtils;
import net.xgs.utils.ObjectUtils;
import org.apache.commons.lang.StringUtils;

@Service
public class MachineService extends BaseService {
	@Inject
	MachineParamService machineParamService;
	public Page<ViewMachine> page(int pageNumber, int pageSize, FilterBuilder builder) {
		if(builder == null ){
			builder = new FilterBuilder();
		}
		String select = "select * ";
		String sqlExceptSelect = "from view_machine_type_manufactor where 1 = 1 ";
		sqlExceptSelect += builder.getSnippets();
		List<Object> paras = builder.getParams();
		sqlExceptSelect += " AND status = ? ";
		paras.add(StatusEnum.NORMAL_USE.getValue());
		sqlExceptSelect += " order by create_time desc ";
		return ViewMachine.dao.paginate(pageNumber, pageSize, select, sqlExceptSelect, paras.toArray());
	}

	public List<ViewMachine> findViewMachine() {
		String sqlExceptSelect = "select * from view_machine_type_manufactor where 1 = 1 ";
		sqlExceptSelect += " AND status = ? ";
		sqlExceptSelect += " order by create_time desc ";
		return ViewMachine.dao.find( sqlExceptSelect,StatusEnum.NORMAL_USE.getValue());
	}
	public List<BaseMachine> findAll(){
		StringBuilder sb = new StringBuilder();
		sb.append(" SELECT ");
		sb.append(" 	machine.id, ");
		sb.append(" 	terminal_id, ");
		sb.append(" 	parent_id, ");
		sb.append(" 	machine. NAME, ");
		sb.append(" 	type.is_main ");
		sb.append(" FROM ");
		sb.append(" 	base_machine AS machine ");
		sb.append(" LEFT JOIN base_machine_machine_type bmmt ON bmmt.machine_id = machine.id ");
		sb.append(" LEFT JOIN base_machine_type type ON type.id = bmmt.machine_type_id ");
		return BaseMachine.dao.find(sb.toString());
	}
	public Integer findAllCount(){
		String sqlExceptSelect = "select count(1) as count from base_machine ";
		return BaseMachine.dao.findFirst(sqlExceptSelect).getLong("count").intValue();
	}
	
	public BaseMachine getById(String id) {
		String sql = "select * from base_machine where id = ? and status = ? ";
		return BaseMachine.dao.findFirst(sql, id, StatusEnum.NORMAL_USE.getValue());
	}
	
	/**
	 * 新增设备
	 * @param baseMachine 设备
	 * @param params
	 * @return
	 * @throws TurburException
	 */
	@Before(Tx.class)
	public boolean save(BaseMachine baseMachine, String optId, List<BaseMachineParams> params) throws TurburException {
		if (StringUtils.isBlank(baseMachine.getId())){
			baseMachine.setId(getUUID());
			baseMachine.setCreateTime(getDateTime());
			baseMachine.setStatus(StatusEnum.NORMAL_USE.getValue());
			baseMachine.setCreateBy(optId);
			baseMachine.save();
		}else {
			Db.update("delete from base_machine_machine_type where machine_id = ?",baseMachine.getId());
			baseMachine.update();
		}
		List<String> ids =ObjectUtils.getMethodValue(params,"getId");
		if (ids.size()>0){
			machineParamService.deleteByMachine(baseMachine.getId(),ids);
		}
		for (BaseMachineParams param:params){
			if (StringUtils.isNotBlank(param.getId())){
				param.update();
				continue;
			}
			param.setMachineId(baseMachine.getId());
			param.setStatus(StatusEnum.NORMAL_USE.getValue());
			param.setId(getUUID());
			param.setCreateBy(optId);
			param.setCreateTime(getDateTime());
			param.save();
		}
		BaseMachineMachineType baseMachineMachineType = new BaseMachineMachineType();
		baseMachineMachineType.setId(getUUID());
		baseMachineMachineType.setMachineId(baseMachine.getId());
		baseMachineMachineType.setMachineTypeId(baseMachine.get("machine_type_id"));
		baseMachineMachineType.setManufactorId(baseMachine.get("manufactor_id"));
		// 保存
		return baseMachineMachineType.save();
	}

	
	/**
	 * 修改设备
	 * @param baseMachine
	 * @return
	 */
	public boolean update(BaseMachine baseMachine) {
		//TODO: log who update
		return baseMachine.update();
	}
	

	
	/**
	 * 删除设备-逻辑
	 * 删除设备与区块的关系-物理
	 * 删除设备与厂商/设备类型的关系-物理
	 * @param ids
	 * @return 0:成功 1：父级关联 2:田园关联
	 */
	@Before(Tx.class)
	public Integer delete(String[] ids) {
		for (String id : ids){
			Record base_machine_relation = Db.findFirst("select id from base_machine where parent_id = ? and status = ?",id,StatusEnum.NORMAL_USE.getValue());
			if (base_machine_relation!=null) return 1;
			Record base_block_machine = Db.findFirst("select id from base_block_machine where machine_id = ?",id);
			if (base_block_machine!=null) return 2;
			// 删除设备-逻辑
			Db.update("update base_machine set status = ? where id = ? ", StatusEnum.PROHIBITED_USE.getValue(), id);
			Db.update("delete from base_machine_machine_type where machine_id = ?",id);
		}
		return 0;
	}

	public List<BaseMachine> findByParents(String parentId,Boolean isparent) {
		String sql = "select * from base_machine where (parent_id = ? and status = ?) ";
		if (isparent){
			sql += " or id = '"+parentId+"'";
		}
		return BaseMachine.dao.find(sql,parentId, StatusEnum.NORMAL_USE.getValue());
	}
	public Page<BaseMachine> findByParents(Integer pageNumber,Integer pageSize,FilterBuilder builder) {
		String select = "select * ";
		String sqlExceptSelect = "from base_machine where 1 = 1 ";
		sqlExceptSelect += builder.getSnippets();
		sqlExceptSelect += " AND status = ? AND parent_id = ?";
		builder.getParams().add(StatusEnum.NORMAL_USE.getValue());
		builder.getParams().add(Constants.defaultParentId);
		sqlExceptSelect += " order by create_time desc ";
		return BaseMachine.dao.paginate(pageNumber, pageSize, select, sqlExceptSelect, builder.getParams().toArray());
	}

	public BaseMachine findByDevice(String systemId,String deviceId){
		String key = DBUtils.getRedisKey(systemId,deviceId);
		BaseMachine machine = null;//cache.get(key);
		if (machine==null){
			machine = BaseMachine.dao.findFirst("select * from base_machine where system_id = ? and device_id = ?",systemId,deviceId);
			//cache.set(key,machine);
		}
		return machine;
	}
	/**
	 * 根据systemId查询主控箱
	 * @param systemId
	 * @return
	 */
	public BaseMachine findByDeviceMain(String systemId){
		return BaseMachine.dao.findFirst("select id from view_machine_type_manufactor  where system_id = ? and is_main = 1 ",systemId);
	}
}
