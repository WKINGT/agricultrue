package net.xgs.entity.webvo;

import net.xgs.model.SysFunctions;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by duai on 2017-07-17.
 * 界面功能列表实体
 */
public class FunctionWebVO  implements Serializable {
    private String icon;
    private String text;
    private String token;
    private String click = "itemclick";
    private String act;

    public static List<FunctionWebVO> po2WebVO(List<SysFunctions> functions){
        List<FunctionWebVO> functionWebVOS = new ArrayList<>();
        if (functions==null) return functionWebVOS;
        for (SysFunctions sysFunctions:functions){
            FunctionWebVO functionWebVO = new FunctionWebVO();
            functionWebVO.setAct(sysFunctions.getAct());
            functionWebVO.setIcon(sysFunctions.getIcon());
            functionWebVO.setText(sysFunctions.getName());
            functionWebVO.setToken(sysFunctions.getId());
            functionWebVOS.add(functionWebVO);
        }
        return functionWebVOS;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getClick() {
        return click;
    }

    public void setClick(String click) {
        this.click = click;
    }

    public String getAct() {
        return act;
    }

    public void setAct(String act) {
        this.act = act;
    }
}
