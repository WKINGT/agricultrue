package net.xgs.model.base;

import com.jfinal.plugin.activerecord.IBean;
import com.jfinal.plugin.activerecord.Model;

/**
 * Created by duai on 2017-07-24.
 */
public class BaseViewBlockMember<M extends BaseViewBlockMember<M>> extends Model<M> implements IBean {
    public String getId(){
        return get("id");
    }
    public String getBlockId(){
        return get("block_id");
    }
    public String getMemberId(){
        return get("member_id");
    }
    public String getCreateTime(){
        return get("create_time");
    }
    public String getStatus(){
        return get("status");
    }
    public String getMemberName(){
        return get("member_name");
    }
    public String getMemberMobile(){
        return get("member_mobile");
    }
    public String getBlockName(){
        return get("block_name");
    }
    public String getPass(){
        return get("pass");
    }
    public String getDescription(){
        return get("description");
    }
    public void  setPass(String pass){
        set("pass",pass);
    }
    public void setId(String id){
        set("id",id);
    }
    public void setBlockId(String blockId){
        set("block_id",blockId);
    }
    public void setMemberId(String memberId){
        set("member_id",memberId);
    }
    public void setCreateTime(String createTime){
        set("create_time",createTime);
    }
    public void setStatus(String status){
        set("status",status);
    }
    public void setMemberName(String memberName){
        set("member_name",memberName);
    }
    public void setMemberMobile(String memberMobile){
        set("member_mobile",memberMobile);
    }
    public void setBlockName(String blockName){
        set("block_name",blockName);
    }
    public void  setDescription(String description){
        set("description",description);
    }

    public String getAddress(){
        return get("address");
    }
    public Integer getBlockAcreage(){
        return getInt("block_acreage");
    }
    public String getHistoryData(){
        return get("history_data");
    }
}
