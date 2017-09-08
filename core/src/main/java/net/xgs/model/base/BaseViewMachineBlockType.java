package net.xgs.model.base;

import com.jfinal.plugin.activerecord.IBean;
import com.jfinal.plugin.activerecord.Model;

/**
 * 由于视图经常修改此处忽略字段定义
 * 其他以前定义的字段暂时保留
 * Created by duai on 2017-08-07.
 */
public class BaseViewMachineBlockType <M extends BaseViewMachineBlockType<M>> extends Model<M> implements IBean {
}
