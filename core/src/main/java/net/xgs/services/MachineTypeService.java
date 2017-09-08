package net.xgs.services;

import java.util.List;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;

import com.jfinal.plugin.activerecord.Record;
import net.xgs.commons.annotation.Service;
import net.xgs.entity.edomain.StatusEnum;
import net.xgs.model.BaseMachineType;
import net.xgs.query.FilterBuilder;
import org.apache.commons.lang.StringUtils;

@Service
public class MachineTypeService extends BaseService {

	public Page<BaseMachineType> page(int pageNumber, int pageSize, FilterBuilder builder) {
		if(builder == null ){
			builder = new FilterBuilder();
		}
		String select = "select * ";
		String sqlExceptSelect = "from base_machine_type where 1 = 1 ";
		sqlExceptSelect += builder.getSnippets();
		List<Object> paras = builder.getParams();
		sqlExceptSelect += " and status = ? ";
		paras.add(StatusEnum.NORMAL_USE.getValue());
		sqlExceptSelect += " order by create_time desc ";
		return BaseMachineType.dao.paginate(pageNumber, pageSize, select, sqlExceptSelect, paras.toArray());
	}
	
	public BaseMachineType getById(String id) {
		return BaseMachineType.dao.findFirst("select * from base_machine_type where id = ? and status = ? ", id, StatusEnum.NORMAL_USE.getValue());
	}


	/**
	 * 查询所有设备类型
	 * @return
	 */
	public List<BaseMachineType> findAll() {
		return BaseMachineType.dao.find("select id,name from base_machine_type where status = ? order by create_time desc ", StatusEnum.NORMAL_USE.getValue());
	}
	
	public boolean save(BaseMachineType baseMachineType, String optId) {
		if (StringUtils.isBlank(baseMachineType.getId())){
			baseMachineType.setCreateTime(getDateTime());
			baseMachineType.setId(getUUID());
			baseMachineType.setStatus(StatusEnum.NORMAL_USE.getValue());
			baseMachineType.setCreateBy(optId);
			return baseMachineType.save();
		}
		return baseMachineType.update();
	}
	

	/**
	 * 删除-逻辑
	 * 删除设备/厂商/类型关联-物理
	 * @param ids
	 * @return
	 */
	public Integer delete(String[] ids) {
		for (String id:ids){
			Record record = Db.findFirst("SELECT * from base_machine_machine_type WHERE machine_type_id = ? AND ",id);
			if (record!=null) return 2;
			Db.update("update base_machine_type set status = ? where id = ? ", StatusEnum.PROHIBITED_USE.getValue(), id);
		}
		return 0;
	}
}
