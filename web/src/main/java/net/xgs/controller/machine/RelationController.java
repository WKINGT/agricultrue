package net.xgs.controller.machine;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.plugin.activerecord.Page;
import net.xgs.commons.annotation.Inject;
import net.xgs.controller.core.BaseController;
import net.xgs.model.BaseMachine;
import net.xgs.services.MachineService;
import org.apache.commons.lang.StringUtils;

import java.util.List;

/**
 * Created by duai on 2017-07-24.
 */
public class RelationController extends BaseController {

    @Inject
    MachineService machineService;
    public void index(){
        if (isGet){
            renderJsp("admin/relation/list.jsp");
        }else {
            Page<BaseMachine>  page = machineService.findByParents(pageNo,pageSize,filterBuilder);
            renderHtml(JSONObject.toJSONString(page));
        }
    }
    public void ajax(){
        String id = getParam("id");
        if (StringUtils.isBlank(id)){
            msg.setCode(1);
            msg.setMsg("查询失败，父级获取失败！");
            renderHtml(JSONObject.toJSONString(msg));
            return;
        }
        List<BaseMachine> list = machineService.findByParents(id,true);
        msg.setCode(0);
        msg.setMsg("查询成功！");
        msg.setData(list);
        renderHtml(JSONObject.toJSONString(msg));

    }
}
