package net.xgs.services;

import java.util.ArrayList;
import java.util.List;

import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.tx.Tx;

import net.xgs.commons.annotation.Service;
import net.xgs.entity.edomain.StatusEnum;
import net.xgs.exception.TurburException;
import net.xgs.model.BaseMember;
import net.xgs.model.BaseOrg;
import net.xgs.query.FilterBuilder;
import net.xgs.utils.ObjectUtils;
import org.apache.commons.lang.StringUtils;

@Service
public class OrgService extends BaseService {
	
	/**
	 * 根级别组织机构
	 */
	public final static String BASE_ORG_ID = "1";
	
	public Page<BaseOrg> page(int pageNumber, int pageSize, FilterBuilder builder) {
		if(builder == null ){
			builder = new FilterBuilder();
		}
		String select = "select * ";
		String sqlExceptSelect = "from base_org where 1 = 1 ";
		sqlExceptSelect += builder.getSnippets();
		List<Object> paras = builder.getParams();
		sqlExceptSelect += " status = ? ";
		paras.add(StatusEnum.NORMAL_USE.getValue());
		sqlExceptSelect += " order by create_time desc ";
		return BaseOrg.dao.paginate(pageNumber, pageSize, select, sqlExceptSelect, paras.toArray());
	}
	
	/**
	 * 获取全部组织机构
	 * 
	 * @return
	 */
	public List<BaseOrg> findOrgAll() {
		String sql = "select * from base_org where 1 = 1 ";
		List<Object> params = new ArrayList<Object>();
		params.add(StatusEnum.NORMAL_USE.getValue());
		sql += "and status = ? ";
		sql += "order by create_time desc ";
		return BaseOrg.dao.find(sql, params.toArray());
	}

	public List<BaseOrg> findOrgAllByUser(BaseOrg baseOrg,boolean isPass){
		StringBuilder sql = new StringBuilder();
		sql.append("select id,parent_id,name,address,create_time,pass ");
		sql.append(" from base_org where status = ? and pass like  ");
		sql.append(" '"+baseOrg.getPass()+"%' ");
		if (isPass){
			sql.append("and pass != '"+baseOrg.getPass()+"' ");
		}
		return BaseOrg.dao.find(sql.toString(),StatusEnum.NORMAL_USE.getValue());
	}
	//获取最浅层次做为根ID
	public String getRootId(List<BaseOrg> list){
		String string = "";
		Integer integer = Integer.MAX_VALUE;
		for (BaseOrg baseOrg:list){
			Integer size = baseOrg.getPass().split(",").length;
			if (size<integer){
				string = baseOrg.getParentId();
				integer = size;
			}
		}
		return string;
	}
	/**
	 * 根据父级组织机构id获取组织机构
	 * @param parentId
	 * @return
	 */
	public List<BaseOrg> findOrgByParentId(int parentId) {
		String sql = "select * from base_org where 1 = 1 ";
		List<Object> params = new ArrayList<Object>();
		sql += "and parent_id = ? ";
		params.add(parentId);
		sql += "and status = ? ";
		params.add(StatusEnum.NORMAL_USE.getValue());
		sql += "order by create_time desc ";
		return BaseOrg.dao.find(sql, params.toArray());
	}
	
	/**
	 * 根据父级组织机构id获取所有下级组织机构
	 * @param parentId
	 * @return
	 */
	public List<BaseOrg> findOrgByParentIdAll(String parentId) {
		BaseOrg baseOrg = this.getById(parentId);
		if (baseOrg == null) {
			return null;
		}
		String sql = "select * from base_org where 1 = 1 ";
		List<Object> params = new ArrayList<Object>();
		sql += "and pass like ? ";
		params.add(baseOrg.getPass() + "%");
		sql += "and status = ? ";
		params.add(StatusEnum.NORMAL_USE.getValue());
		sql += "order by create_time desc ";
		return BaseOrg.dao.find(sql, params.toArray());
	}
	
	/**
	 * 查询组织机构下的人员列表
	 * @param orgId
	 * @return
	 */
	public List<BaseMember> findMemberByOrgId(int orgId) {
		return BaseMember.dao.find("select * from base_member where org_id = ? and status = ? ", orgId, StatusEnum.NORMAL_USE.getValue());
	}

	/**
	 * 根据父级组织机构id获取所有下级组织机构(被删除的)
	 * @param parentId
	 * @return
	 */
	public List<BaseOrg> findOrgAllBeenDeleted(int parentId) {
		BaseOrg baseOrg = BaseOrg.dao.findById(parentId);
		if (baseOrg == null) {
			return null;
		}
		String sql = "select * from base_org where 1 = 1 ";
		List<Object> params = new ArrayList<Object>();
		sql += "and pass like ? ";
		params.add(baseOrg.getPass() + "%");
		sql += "and status = ? ";
		params.add(StatusEnum.PROHIBITED_USE.getValue());
		sql += "order by create_time desc ";
		return BaseOrg.dao.find(sql, params.toArray());
	}
	
	/**
	 * 根据id获取单个组织机构
	 * @param id
	 * @return
	 */
	public BaseOrg getById(String id) {
		return BaseOrg.dao.findFirst("select * from base_org where id = ? and status = ? ", id, StatusEnum.NORMAL_USE.getValue());
	}
	
	/**
	 * 获取最跟级组织机构
	 * @return
	 */
	public BaseOrg getBaseOrg() {
		return BaseOrg.dao.findFirst("select * from base_org where id = ? and status = ? ", BASE_ORG_ID, StatusEnum.NORMAL_USE.getValue());
	}
	
	/**
	 * 新增组织机构
	 * @param baseOrg
	 * @return
	 * @throws TurburException
	 */
	@Before(Tx.class)
	public boolean saveOrg(BaseOrg baseOrg,String optId) throws TurburException {
		BaseOrg parentOrg = null;
		parentOrg = this.getById(baseOrg.getParentId());
		if (parentOrg == null) {
			throw new TurburException(TurburException.getCode("org.parent.null"));
		}
		String id = baseOrg.getId();
		if (StringUtils.isBlank(id)){
			id = getUUID();
		}
		String pass = "";
		if (parentOrg != null) {
			pass = parentOrg.getPass() + ","  + id;
		}
		baseOrg.setPass(pass);
		if (StringUtils.isBlank(baseOrg.getId())){
			baseOrg.setId(id);
			baseOrg.setCreateBy(optId);
			baseOrg.setCreateTime(getDateTime());
			baseOrg.setStatus(StatusEnum.NORMAL_USE.getValue());
			return baseOrg.save();
		}
		return baseOrg.update();
	}
	
	/**
	 * 删除组织机构-逻辑
	 * 删除所有下级组织机构-逻辑
	 * 删除member-逻辑
	 * @param ids 组织机构id
	 * @return
	 */
	@Before(Tx.class)
	public boolean deleteOrg(String[] ids){
		for (String id :ids){
			BaseOrg baseOrg = this.getById(id);
			if (baseOrg == null) {
				return true;
			}
			//TODO:log who delete
			List<BaseOrg> all = this.findOrgByParentIdAll(id);
			//删除自己和下级-逻辑
			Db.update("update base_org set status = ? where pass like ? ", StatusEnum.PROHIBITED_USE.getValue(), baseOrg.getPass() + "%");
			List<Object[]> paras = new ArrayList<>();
			for (BaseOrg bo : all) {
				Object[] para = new Object[]{StatusEnum.PROHIBITED_USE.getValue(), bo.getId()};
				paras.add(para);
			}
			//删除member-逻辑
			Db.batch("update base_member set status = ? where org_id = ? ", paras.toArray(new Object[][]{}), paras.size());
		}
		return true;
	}
	
	/**
	 * 恢复被删除的组织机构
	 * 恢复被删除的所有下级组织机构
	 * 恢复被删除的member
	 * @param id 组织机构id
	 * @return
	 */
	public boolean recoveryDeleteOrg(String id) throws Exception {
		List<BaseOrg> all = this.findOrgByParentIdAll(id);
		//恢复被删除自己和下级
		Db.update("update base_org set status = ? where pass like ? ", StatusEnum.NORMAL_USE.getValue(), String.valueOf(id) + "%");
		List<Object[]> paras = new ArrayList<>();
		for (BaseOrg baseOrg : all) {
			Object[] para = new Object[]{StatusEnum.NORMAL_USE.getValue(), baseOrg.getId()};
			paras.add(para);
		}
		//恢复被删除member-逻辑
		Db.batch("update base_member set status = ? where org_id = ? ", paras.toArray(new Object[][]{}), paras.size());
		return true;
	}
}
