package net.xgs.query.liger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.kit.StrKit;
import com.jfinal.log.Log;
import net.xgs.query.Filter;
import net.xgs.query.FilterBuilder;

public class LigerFilter implements Filter {

	private Log log = Log.getLog(getClass());
	
	private static String blank = " ";
	private static enum fields{field,op,value,type};
	private static enum rule{and,or}
	private static Map<String,String> opration;
	private static Map<String,String> params;
	static{
		opration = new HashMap<String, String>();
		opration.put("equal", " = ? ");
		opration.put("notequal", " <> ? ");
		opration.put("startwith", " like ? ");
		opration.put("endwith", " like ? ");
		opration.put("like", " like ? ");
		opration.put("greater", " > ? ");
		opration.put("greaterorequal", " >= ? ");
		opration.put("less", " < ? ");
		opration.put("lessorequal", " <= ? ");
		opration.put("in", "like ? ");
		opration.put("notin", " not like ? ");
		
		params = new HashMap<String, String>();
		params.put("equal", "?");
		params.put("notequal", "?");
		params.put("startwith", "?%");
		params.put("endwith", "%?");
		params.put("like", "%?%");
		params.put("greater", "?");
		params.put("greaterorequal", "?");
		params.put("less", "?");
		params.put("lessorequal", "?");
		params.put("in", "%?%");
		params.put("notin", "%?%");
	}

	private LigerFilter(){}
	private static LigerFilter instance;
	public static LigerFilter getInstance(){
		if(instance == null){
			instance = new LigerFilter();
		}
		return instance;
	}
	
	private Entity factory(JSONObject obj){
		StringBuilder sb = new StringBuilder();
		String op = obj.getString(fields.op.name());
		String value = obj.getString(fields.value.name());
		String param = obj.getString(value); 
		sb.append(obj.getString(fields.field.name()));
		sb.append(blank);
		if(opration.containsKey(op)){
			sb.append(blank);
			sb.append(opration.get(op));
			param = params.get(op).replace("?", value);
			Entity entity = new Entity();
			entity.setParam(param);
			entity.setSnippets(sb.toString());
			return entity;
		}
		return null;
	}
	
	/**
	 * 对单个字段进行构建片段
	 * @param obj
	 * @return
	 */
	private Entity builder(JSONObject obj){
		StringBuilder sb = new StringBuilder();
		sb.append(blank);
		sb.append(rule.and.name());
		sb.append(blank);
		Entity entity = this.factory(obj);
		sb.append(entity.getSnippets());
		entity.setSnippets(sb.toString());
		return entity;
	}
	
	public HashMap<String,Object> parsetoMap(String text){
		if(StrKit.isBlank(text)) return null;
		JSONObject object = JSON.parseObject(text);
		JSONArray list = (JSONArray) object.get("rules");
		HashMap<String,Object> map = new HashMap<String,Object>();
		for(int i=0,len=list.size();i<len;i++){
			JSONObject obj = list.getJSONObject(i);
			map.put(obj.getString(fields.field.name()), obj.getString(fields.value.name()));
		}
		return map;
	}
	
	public FilterBuilder parse(String text){
		FilterBuilder fe = new FilterBuilder();
		if(StrKit.notBlank(text)){
			JSONObject object = JSON.parseObject(text);
			JSONArray list = (JSONArray) object.get("rules");
			StringBuilder sb = new StringBuilder();
			List<Object> params = new ArrayList<Object>();
			for(int i=0,len=list.size();i<len;i++){
				JSONObject obj = list.getJSONObject(i);
				Entity entity = this.builder(obj);
				sb.append(entity.getSnippets());
				params.add(entity.getParam());
			}
			fe.setParams(params);
			fe.setSnippets(sb.toString());
		}
		log.debug(JSON.toJSONString(fe));
		return fe;
	}
	
	class Entity{
		private String param;
		private String snippets;
		public String getParam() {
			return param;
		}
		public void setParam(String param) {
			this.param = param;
		}
		public String getSnippets() {
			return snippets;
		}
		public void setSnippets(String snippets) {
			this.snippets = snippets;
		}
		
	}
	
	public static void main(String[] args) {
		String where = "{\"rules\":[{\"field\":\"user_name\",\"op\":\"equal\",\"value\":\"aa\",\"type\":\"text\"},{\"field\":\"state\",\"op\":\"equal\",\"value\":\"1\",\"type\":\"select\"}]}  ";
		LigerFilter filter = LigerFilter.getInstance();
		filter.parse(where);
	}

}
