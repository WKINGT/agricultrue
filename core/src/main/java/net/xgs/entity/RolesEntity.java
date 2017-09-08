package net.xgs.entity;

import java.util.HashMap;
import java.util.Map;

import net.xgs.entity.webvo.MenuVO;

public class RolesEntity {

	Map<String,MenuVO> menuVOMap = new HashMap<>();
	//	private Map<String,Object> mids = new HashMap<String,Object>();
//	private Map<String,Object> fids = new HashMap<String,Object>();
	public RolesEntity(Map<String,MenuVO> menuVOMap){
		this.menuVOMap = menuVOMap;
	}
}
