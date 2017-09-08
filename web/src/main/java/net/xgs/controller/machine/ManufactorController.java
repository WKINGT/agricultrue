package net.xgs.controller.machine;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.plugin.activerecord.Page;
import net.xgs.commons.annotation.Inject;
import net.xgs.controller.core.BaseController;
import net.xgs.entity.Constants;
import net.xgs.model.BaseManufactor;
import net.xgs.services.ManufactorService;
import org.apache.commons.lang.StringUtils;

/**
 * Created by duai on 2017-07-21.
 * 设备厂商表
 */
public class ManufactorController extends BaseController {
    @Inject
    ManufactorService manufactorService;
    public void index(){
        if (isGet){
            renderJsp("admin/manufactor/list.jsp");
        }else {
          Page<BaseManufactor> page = manufactorService.page(pageNo,pageSize,filterBuilder);
          renderHtml(JSONObject.toJSONString(page));
        }
    }
    public void save(){
        if (isGet){
            String id = getParam("id");
            if (StringUtils.isNotBlank(id)){
                BaseManufactor baseManufactor = manufactorService.findById(id);
                setAttr("manufactor",baseManufactor);
                setAttr("name","修改");
            }else {
                setAttr("name","添加");
            }
            renderJsp("admin/manufactor/update.jsp");
        }else {
            BaseManufactor baseManufactor = getModel(BaseManufactor.class,"manufactor");
            boolean result =  manufactorService.save(baseManufactor,getSessionUserId());
            if (result){
                msg = Constants.editSuccess;
            }else {
                msg = Constants.editFail;
            }
            renderHtml(JSONObject.toJSONString(msg));
        }
    }
    public void update(){
        save();
    }
    public void delete(){
        String [] ids =  getParam("ids").split(",");
        boolean result = manufactorService.delete(ids);
        if (result){
            renderHtml(JSONObject.toJSONString(Constants.deleteSuccess));
        }else {
            renderHtml(JSONObject.toJSONString(Constants.deleteFail));
        }
    }
}
