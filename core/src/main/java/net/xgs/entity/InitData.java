package net.xgs.entity;

import java.util.*;

import com.jfinal.plugin.activerecord.Record;

import net.xgs.entity.webvo.FunctionVO;
import net.xgs.entity.webvo.MenuVO;
import net.xgs.model.SysFunctions;
import net.xgs.model.SysMenu;
import net.xgs.model.base.BaseSysMenu;

public class InitData {

	private List<Record> areas = new ArrayList<Record>();
	private List<SysMenu> menus = new ArrayList<>();
	private List<MenuVO> menuVOS = new ArrayList<>();
	
	private List<SysFunctions> sysFunctions = new ArrayList<>();

	private Map<String,SysMenu> mids = new HashMap<String,SysMenu>();
	private Map<String,MenuVO> mvoIds = new HashMap<String,MenuVO>();
	private Map<String,SysFunctions> fids = new HashMap<String,SysFunctions>();
	
	private static InitData instance;
	
	public static InitData instance(){
		if(instance == null){
			instance = new InitData();
		}
		return instance;
	}
	
	public void init(List<SysMenu> menus,List<SysFunctions> sysFunctions){
		this.menus = menus;
		this.sysFunctions = sysFunctions;
		

		for(SysMenu mid:menus){
			this.mids.put(mid.getId(), mid);
			this.mvoIds.put(mid.getId(), new MenuVO(mid));
		}
		for(SysFunctions fid:sysFunctions){
			this.fids.put(fid.getId(), fid);
		}
		menuVOS = treeMenus(sysFunctions,fids,menus,mvoIds);
	}

	/**
	 * 菜单与功能之间的关系
	 */
	public List<MenuVO> treeMenus(List<SysFunctions> sysFunctions,Map<String,SysFunctions> fids, List<SysMenu> menus,Map<String,MenuVO> mvoIds){
		List<FunctionVO> functionVOS = assembleFunction(Constants.defaultParentId,fids,sysFunctions);
		mvo:for (String id :mvoIds.keySet()){
			MenuVO menuVO = mvoIds.get(id);
			for (FunctionVO functionVO : functionVOS){
				if (functionVO.getId().equals(menuVO.getFunctionId())){
					menuVO.setFunctionVO(functionVO);
					continue mvo;
				}
			}
		}
		return assembleMenu(Constants.defaultParentId,mvoIds,menus);
	}

	/**
	 * 封装功能与功能的关系
	 * @param parentId
	 * @return
	 */
	public List<FunctionVO> assembleFunction(String parentId,Map<String,SysFunctions> fids,List<SysFunctions> sysFunctions){
		List<FunctionVO> list = new ArrayList<>();
		for (String id :fids.keySet()){
			FunctionVO functionVO = new FunctionVO(fids.get(id));
			if (functionVO.getParentId().equals(parentId)){
				List<FunctionVO> son = new ArrayList<>();
				for(SysFunctions sysFunction:sysFunctions){
					if (sysFunction.getParentId().equals(functionVO.getId())){
						son.add(new FunctionVO(sysFunction));
					}
				}
				functionVO.setSonData(son);
				list.add(functionVO);
			}
		}
		return list;
	}

	/**
	 * 封装菜单与菜单的关系
	 * @param parentId
	 * @return
	 */
	public List<MenuVO> assembleMenu(String parentId, Map<String,MenuVO> mvoIds,List<SysMenu> menus){
		List<MenuVO> menuVOS = new ArrayList<>();
		for (String id : mvoIds.keySet()){
			MenuVO menuVO = mvoIds.get(id);
			if (parentId.equals(menuVO.getParentId())){
				List<MenuVO> son = new ArrayList<>();
				for (SysMenu sysMenu : menus){
					if (sysMenu.getParentId().equals(menuVO.getId())){
						List<MenuVO> sons =assembleMenu(sysMenu.getId(),mvoIds,menus);
						MenuVO sonMenuVo  = mvoIds.get(sysMenu.getId());
						sonMenuVo.setSonData(sons);
						son.add(sonMenuVo);
					}
				}
				menuVO.setSonData(son);
				menuVOS.add(menuVO);
			}
		}
		return menuVOS;
	}

	public List<Record> getAreas(){
		return this.areas;
	}
	
	public void setAreas(List<Record> rs){
		this.areas = rs;
	}
	
	public List<SysMenu> menus(){
		return this.menus;
	}
	
	public List<SysFunctions> funcs(){
		return this.sysFunctions;
	}
	
	public List<MenuVO> menus(List<String> mids,List<String> fids){
		List<MenuVO> tempmenu  = new ArrayList<>(Arrays.asList(new MenuVO[menuVOS.size()]));
		Collections.copy(tempmenu,menuVOS);
		menus(tempmenu,mids,fids);
		return tempmenu;
	}
	public void menus(List<MenuVO> tempmenu,List<String> mids,List<String> fids){
		for (int i = 0;i<tempmenu.size();i++){
			MenuVO menuVO = tempmenu.get(i);
			if (mids.contains(menuVO.getId())){
				if (menuVO.getFunctionVO()!=null){
					for (int j = 0;j<menuVO.getFunctionVO().getSonData().size();j++){
						FunctionVO functionVO = menuVO.getFunctionVO().getSonData().get(j);
						if (!fids.contains(functionVO.getId())){
							menuVO.getFunctionVO().getSonData().remove(j);
						}
					}
				}
			}else {
				tempmenu.remove(i);
			}
			if (menuVO.getSonData()!=null&&menuVO.getSonData().size()>0){
				menus(menuVO.getSonData(),mids,fids);
			}
		}
	}
	
	public List<SysFunctions> funcs(List<String> _fids){
		List<SysFunctions> tempfuncs = new ArrayList<SysFunctions>();
		for(String _fid:_fids){
			if(this.fids.containsKey(_fid+"")){
				tempfuncs.add(this.fids.get(_fid+""));
			}
		}
		return tempfuncs;
	}

	public List<SysMenu> menus(List<String> _mids){
		List<SysMenu> tempmenu  = new ArrayList<>();
		for(String _mid:_mids){
			if(this.mids.containsKey(_mid+"")&&!tempmenu.contains(this.mids.get(_mid))){
				tempmenu.add(this.mids.get(_mid));
			}
		}
		Collections.sort(tempmenu, Comparator.comparingInt(BaseSysMenu::getNo));
		return tempmenu;
	}
	public Map<String,SysFunctions> getFuncs(List<SysFunctions> data){
		Map<String,SysFunctions> fids = new HashMap<>();
			for (SysFunctions sysFunctions :data){
				fids.put(sysFunctions.getId(),sysFunctions);
			}
		 return fids;
	}

	public Map<String,SysMenu> getMids(List<SysMenu> data){
		Map<String,SysMenu> mids = new HashMap<>();
		for (SysMenu sysMenu:data){
			mids.put(sysMenu.getId(),sysMenu);
		}
		return mids;
	}
	public Map<String,MenuVO> getMvoids(List<MenuVO> data){
		Map<String,MenuVO> mvoids = new HashMap<>();
		for (MenuVO menuVO:data){
			mvoids.put(menuVO.getId(),menuVO);
		}
		return mvoids;
	}
}
