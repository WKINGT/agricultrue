package net.xgs.controller.core;

import com.jfinal.core.Injector;
import com.jfinal.kit.StrKit;
import com.jfinal.log.Log;
import com.jfinal.plugin.activerecord.Model;
import net.xgs.entity.Constants;
import net.xgs.entity.Msg;
import net.xgs.model.BaseMember;
import net.xgs.model.SysFunctions;
import net.xgs.model.SysMenu;
import net.xgs.query.Filter;
import net.xgs.query.FilterBuilder;
import net.xgs.query.liger.LigerFilter;
import net.xgs.services.BaseService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * 自定义的控制 器，不同于上层的控制 器。这个控制器和上层的控制器一样，是线程安全的
 *
 * @pdOid 4c87f67d-398b-4628-8582-63b420cc5cb3
 */
public class BaseController {


    protected SysFunctions func;

    protected BaseService baseService;
    protected Class<?> baseServiceClass;
    protected String tableName ;
    protected Class<? extends Model<?>> modelClass;


    protected List<SysMenu> userMenus;
    protected List<SysFunctions> userFuncs;
    protected Map<String, SysFunctions> mapFunctions;



    public void before() {};
    protected Log log = Log.getLog(getClass());

    /** @pdOid 0f57a59d-19d9-4399-b1ef-d26a91172f86 */
    protected HttpServletRequest request;
    protected HttpServletResponse response;
    protected Msg msg;
    protected int pageNo=1;
    protected int pageSize = 15;
    protected boolean sp = true;
    protected boolean isGet = false;
    protected boolean isPost = true;
    protected String order = "";
    protected IndexController controller;

    protected String webExt = ".jsp";
    protected BaseMember baseMember;

    protected String ctlName;
    protected String actName;
    protected String param;
    protected static String where = "where";
    protected FilterBuilder filterBuilder;
    protected final void getFilter(){
        Filter filter = LigerFilter.getInstance();
        filterBuilder = filter.parse(request.getParameter(where));
    }

    /** @pdOid 8c9bd880-257a-427a-bb60-ebb6ce2864bb */
    public final void setController(IndexController controller,String ctl,String act,String param,SysFunctions func) {
        this.func = func;
        this.controller = controller;
        this.request = this.controller.getRequest();
        this.response = this.controller.getResponse();
        this.ctlName = ctl;
        this.actName = act;
        this.param = param;
        msg = new Msg();
        this.jadgeMethod();
        this.initPage();
        this.getFilter();
        baseMember = (BaseMember)this.controller.getSession().getAttribute(Constants.sessionUser);
        userMenus = baseMember.get(Constants.userListMenus);
        userFuncs = baseMember.get(Constants.userListFunctions);
        mapFunctions = baseMember.get(Constants.userFunctions);
        this.setSetting();
        this.before();
    }
    /** @pdOid 8c9bd880-257a-427a-bb60-ebb6ce2864bb */
    public final void setController(IndexController controller,String ctl,String act) {
        this.controller = controller;
        this.request = this.controller.getRequest();
        this.response = this.controller.getResponse();
        this.ctlName = ctl;
        this.actName = act;
        msg = new Msg();
        this.jadgeMethod();
        this.initPage();
        this.getFilter();
        this.before();
    }



        protected String getListToken(){
        return func.getParentId();
    }

    private void setSetting(){
        if(StrKit.isBlank(func.getSetting())){
            func.setSetting("{}");
        }
    }


    protected void initPage(){
        String _pageNo = request.getParameter("pageNo");
        String _pageSize = request.getParameter("pageSize");
        try {
            pageNo = Integer.parseInt(_pageNo);
        } catch (Exception e) {
            pageNo = 1;
        }
        try {
            pageSize = Integer.parseInt(_pageSize);
        } catch (Exception e) {
            pageSize = 15;
        }
        if(pageNo < 1){
            pageNo = 1;
        }
    }

    private void jadgeMethod() {
        if (this.request.getMethod().toLowerCase().equals("get")) {
            isGet = true;
        }
    }

    public String getCtlName() {
        return ctlName;
    }

    public void setCtlName(String ctlName) {
        this.ctlName = ctlName;
    }

    public String getActName() {
        return actName;
    }

    public void setActName(String actName) {
        this.actName = actName;
    }

    public BaseMember getSessionSysUser(){
        return controller.getSessionSysUser();
    }

    public String getSessionUserId(){
        return getSessionSysUser().getId();
    }


    public String filterRender(String str){
        str = str.replaceAll("(\"render\":)\"(.*?)\"", "$1$2");
        return str;
    }
    /*public String getContextMenu(){
        SysFunctions functions = new SysFunctions();
        if(Constants.debugger){
            //functions = functionService.findById(SysFunctions.dao, this.controller.token);
        }else{
            //functions = this.mapFunctions.get(this.controller.token);
        }

        List<Map<String,String>> contextMenu = new ArrayList<Map<String,String>>();
        if(StrKit.notBlank(functions.)){
            List<String> funcIds = JSON.parseArray(functions.getOperation(), String.class);
            for(String id : funcIds){
                SysFunctions func = null;
                if(Constants.debugger){
                    //func = functionService.findById(SysFunctions.dao,id);
                }else{
                    func = this.mapFunctions.get(id);
                }
                if(func != null){
                    Map<String,String> map = new HashMap<String,String>();
                    map.put("text", func.getName());
                    map.put("click", "itemclick");
                    map.put("act", func.getAct());
                    map.put("token", func.getId());
                    map.put("icon", func.getIcon());
                    contextMenu.add(map);
                }
            }
        }
        String json = FastJson.getJson().toJson(contextMenu);
        json = json.replace("\"itemclick\"", "itemclick");
        return json;
    }*/

    public void renderHtml(String string){
        this.controller.renderHtml(string);
    }

    public void renderJsp(String string){
        this.controller.renderJsp(string);
    }

    public String[] getParaValues(String name){
        return controller.getParaValues(name);
    }

    public void renderJson(Object o){
        this.controller.renderJson(o);
    }

    /**
     * Get model from http request.
     */
    public <T> T getModel(Class<T> modelClass) {
        return (T)Injector.injectModel(modelClass, null, request, true);
    }

    public <T> T getModel(Class<T> modelClass,String modelName) {
        return (T)Injector.injectModel(modelClass, modelName, request, true);
    }

    public void renderJavascript(String javascriptText) {
        this.controller.renderJavascript(javascriptText);
    }

    public void render(String name){
        String[] extPath = this.ctlName.split("\\.");
        String path = "";
        for(String p : extPath){
            path += p+"/";
        }
        // this.controller.render(this.webRoot+path+name+this.webExt);
    }
    protected <T> T getAttr(String attrName){
        return controller.getAttr(attrName);
    }
    protected String getParam(String paramName){
        return controller.getPara(paramName);
    }
    protected Integer getParamToInt(String paramName){
        return controller.getParaToInt(paramName);
    }

    protected void setAttr(String name, Object value){
        controller.setAttr(name,value);
    }

}