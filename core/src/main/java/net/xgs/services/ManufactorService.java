package net.xgs.services;

import java.util.List;

import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;

import com.jfinal.plugin.activerecord.tx.Tx;
import net.xgs.commons.annotation.Service;
import net.xgs.entity.edomain.StatusEnum;
import net.xgs.model.BaseManufactor;
import net.xgs.query.FilterBuilder;
import org.apache.commons.lang.StringUtils;

@Service
public class ManufactorService extends BaseService {

	public Page<BaseManufactor> page(int pageNumber, int pageSize, FilterBuilder builder) {
		if(builder == null ){
			builder = new FilterBuilder();
		}
		String select = "select * ";
		String sqlExceptSelect = "from base_manufactor where 1 = 1 ";
		sqlExceptSelect += builder.getSnippets();
		List<Object> paras = builder.getParams();
		sqlExceptSelect += " AND status = ? ";
		paras.add(StatusEnum.NORMAL_USE.getValue());
		sqlExceptSelect += " order by create_time desc ";
		return BaseManufactor.dao.paginate(pageNumber, pageSize, select, sqlExceptSelect, paras.toArray());
	}
	@Before(Tx.class)
	public boolean save(BaseManufactor baseManufactor,String optId) {
		if (StringUtils.isNotBlank(baseManufactor.getId())){
			return baseManufactor.update();
		}else {
			baseManufactor.setId(getUUID());
			baseManufactor.setCreateBy(optId);
			baseManufactor.setStatus(StatusEnum.NORMAL_USE.getValue());
			baseManufactor.setCreateTime(getDateTime());
			return baseManufactor.save();
		}
	}
	/**
	 * 删除厂家-逻辑
	 * 删除厂家/设备/设备类型-物理
	 * @param ids
	 * @return
	 */
	@Before(Tx.class)
	public boolean delete(String[] ids) {
		for (String id:ids){
			Db.update("update base_manufactor set status = ? where id = ? ", StatusEnum.PROHIBITED_USE.getValue(), id);
		}
		return true;
	}

    public BaseManufactor findById(String id) {
		return BaseManufactor.dao.findById(id);
    }

    public List<BaseManufactor> findAll() {
   		return BaseManufactor.dao.find("select id,name from base_manufactor where 1 = 1 ");
    }
}