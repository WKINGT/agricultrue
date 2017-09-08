package net.xgs.controller;

import com.alibaba.druid.sql.ast.statement.SQLUnique;
import com.jfinal.aop.Before;
import com.jfinal.ext.interceptor.POST;
import com.mysql.jdbc.exceptions.MySQLIntegrityConstraintViolationException;
import net.xgs.commons.annotation.Controller;
import net.xgs.commons.annotation.Inject;
import net.xgs.exception.MethodException;
import net.xgs.model.BaseMachineLimit;
import net.xgs.query.FilterBuilder;
import net.xgs.services.MachineLimitService;

/**
 * Created by duai on 2017-08-15.
 */
@Controller(value = "/machine/limit")
public class MachineLimitController extends BaseController{
    @Inject()
    MachineLimitService machineLimitService;
    @Before(POST.class)
    public Object save(){
        BaseMachineLimit machineLimit = getModel(BaseMachineLimit.class,"machineLimit");
        try {
            machineLimitService.save(machineLimit,getSessionBaseMember().getId());
        } catch (Exception e) {
            logger.error(e.getMessage());
            if (e.getMessage()!=null&&e.getMessage().indexOf("MySQLIntegrityConstraintViolationException")>-1){
                throwException("sql.unique.error");
            }else {
                throw e;
            }
        }
        return "操作完成";
    }
    @Before(POST.class)
    public Object findByPage(){
        FilterBuilder filterBuilder = new FilterBuilder();
        filterBuilder.setSnippets(" and block_id = ?");
        filterBuilder.getParams().add(getPara("blockId"));
        return machineLimitService.findByPage(getParaToInt("pageNum"),getParaToInt("pageSize"),filterBuilder);
    }
    @Before(POST.class)
    public Object delete(){
        machineLimitService.delete(getPara("limitId"));
        return "操作完成";
    }
}
