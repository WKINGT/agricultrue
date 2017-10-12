package net.xgs.services;

import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.tx.Tx;
import net.xgs.commons.annotation.Inject;
import net.xgs.commons.annotation.Service;
import net.xgs.commons.utils.DateUtils;
import net.xgs.entity.Constants;
import net.xgs.entity.edomain.StatusEnum;
import net.xgs.model.BaseMember;
import net.xgs.model.SysRole;
import net.xgs.query.FilterBuilder;
import net.xgs.utils.MD5Utils;
import net.xgs.utils.ObjectUtils;
import org.apache.commons.lang.StringUtils;

import java.util.Date;
import java.util.List;

/**
 * Created by duai on 2017-07-13.
 */
@Service
public class MemberService extends BaseService{
    @Inject
    RoleService roleService;
    @Inject
    MachineBlockService machineBlockService;
    @Inject
    OrgService orgService;
    @Inject
    MenuFunctionService menuFunctionService;
    @Inject
    BlockMemberService blockMemberService;
    public BaseMember findByAccountPwd(String account, String pwd) {
        BaseMember baseMember =  BaseMember.dao.findFirst("SELECT * FROM base_member where status = ? and login_account = ?", StatusEnum.NORMAL_USE.getValue(),account);
        if (baseMember!=null){
            if(MD5Utils.pwdValidate(pwd,baseMember.getSalt(),baseMember.getLoginPwd())){
                if (baseMember.getEnabled().equals(StatusEnum.NORMAL_USE.getValue())) {
                    assembleMember(baseMember);
                }
                return baseMember;
            }
        }
        return null;
    }

    public void assembleMember(BaseMember baseMember){
        List<SysRole> sysRole =  roleService.findByMemberId(baseMember.getId());
        List<String> sysRoleIds = ObjectUtils.getMethodValue(sysRole,"getid");
        List<Record> records =menuFunctionService.findByUser(sysRoleIds);
        baseMember.put(Constants.userRoleIds,sysRoleIds);
        baseMember.put(Constants.userRoles,sysRole);
        baseMember.put(Constants.sessionUserRole,sysRole);
        baseMember.put(Constants.userAuthority,records);
        baseMember.put(Constants.baseOrg,orgService.getById(baseMember.getOrgId()));
    }

    public BaseMember loginAndroid(String account, String pwd) {
        BaseMember baseMember =  BaseMember.dao.findFirst("SELECT * FROM base_member where status = ? and login_account = ?", StatusEnum.NORMAL_USE.getValue(),account);
        if (baseMember!=null){
            if(MD5Utils.pwdValidate(pwd,baseMember.getSalt(),baseMember.getLoginPwd())){
                List<String> mains = machineBlockService.findMachineIdByMember(baseMember.getId());
                baseMember.put("mains",mains);
                return baseMember;
            }
        }
        return null;
    }

    @Before(Tx.class)
    public boolean changePwd(BaseMember baseMember,  String newPwd) {
        String salt = MD5Utils.salt();
        String password = MD5Utils.pwdRule(newPwd,salt);
        baseMember.setLoginPwd(password);
        baseMember.setSalt(salt);
        return baseMember.update();
    }

    /**
     * 查询当前用户组织结构下的用户数据
     * @param pass
     * @return
     */
    public List<BaseMember> findByPass( String pass){
        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT ");
        sb.append(" 	member.* ");
        sb.append(" FROM ");
        sb.append(" 	base_member member ");
        sb.append(" LEFT JOIN base_org org ");
        sb.append(" ON  org.id = member.org_id ");
        sb.append(" WHERE member.status = ? AND org.pass LIKE ?");
        return BaseMember.dao.find(sb.toString(),StatusEnum.NORMAL_USE.getValue(),pass+"%");
    }

    /**
     * 查询当前用户创建的用户数据
     * @param pass
     * @return
     */
    public Page<BaseMember> findByPass(Integer pageNum, Integer pageSize, FilterBuilder filterBuilder, String pass){
        filterBuilder.getParams().add(StatusEnum.NORMAL_USE.getValue());
        filterBuilder.getParams().add(pass+"%");
        StringBuilder sb = new StringBuilder();
        sb.append(" FROM ");
        sb.append(" 	base_member member ");
        sb.append(" LEFT JOIN base_org org ");
        sb.append(" ON  org.id = member.org_id ");
        sb.append(" WHERE member.status = ? AND org.pass LIKE ?");
        Page<BaseMember> baseMemberPage = BaseMember.dao.paginate(pageNum,pageSize,"SELECT member.*  ",sb.toString()+filterBuilder.getSnippets()+" ORDER BY create_time DESC",filterBuilder.getParams().toArray());
        return baseMemberPage;
    }

    public BaseMember findById(String userId) {
        return BaseMember.dao.findById(userId);
    }
    @Before(Tx.class)
    public Boolean save(BaseMember baseMember, String[] roleIds, String optId, String orgId,String []  blockIds) {
        if (StringUtils.isNotBlank(baseMember.getLoginPwd())){
            String salt = MD5Utils.salt();
            String password = MD5Utils.pwdRule(baseMember.getLoginPwd(),salt);
            baseMember.setLoginPwd(password);
            baseMember.setSalt(salt);
        }
        if (StringUtils.isBlank(baseMember.getId())){
            baseMember.setOrgId(orgId);
            baseMember.setCreateBy(optId);
            baseMember.setId(getUUID());
            baseMember.setCreateTime(DateUtils.format(new Date(),DateUtils.PATTERN_DATETIME));
            baseMember.save();
        }else {
            Db.update("DELETE FROM sys_user_role WHERE user_id = ?",baseMember.getId());
            if (StringUtils.isBlank(baseMember.getLoginPwd())){
                baseMember.remove("login_pwd");
            }
            baseMember.update();
        }
        for (String roleId:roleIds){
            Db.save("sys_user_role",new Record().set("user_id",baseMember.getId()).set("role_id",roleId));
        }
        blockMemberService.save(blockIds,baseMember.getId(),optId);
        return true;
    }
    @Before(Tx.class)
    public Boolean deleteById(String[] ids) {
        for (String id:ids){
            BaseMember baseMember = BaseMember.dao.findById(id);
            baseMember.setStatus(StatusEnum.PROHIBITED_USE.getValue());
            baseMember.update();
        }
        return true;
    }

    /**
     * 校验二维码内容是否存在数据库字段中如果存在进行登录
     * @param sessionValue
     * @return
     */
    public BaseMember qrCode(String sessionValue) {
        BaseMember baseMember =  BaseMember.dao.findFirst("SELECT * FROM base_member where status = ? and qr_code = ?", StatusEnum.NORMAL_USE.getValue(),sessionValue);
        if (baseMember!=null){
            if (baseMember.getEnabled().equals(StatusEnum.NORMAL_USE.getValue())) {
                assembleMember(baseMember);
            }
        }
        return baseMember;
    }
}
