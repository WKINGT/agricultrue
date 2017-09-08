package net.xgs.utils;

import com.jfinal.plugin.activerecord.IBean;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Record;
import net.xgs.entity.Constants;
import net.xgs.model.SysRole;
import org.apache.commons.lang.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by duai on 2017-07-13.
 */
public class ObjectUtils {
    public static Object getCtl(String ctlPath,String key) throws Exception{
        String[] names = key.split("\\.");
        String className = names[names.length-1]+"Controller";
        String pac = "";
        for(int i=0;i<names.length-1;i++){
            pac += names[i].toLowerCase()+".";
        }
        String name = ctlPath+pac+ StringUtils.capitalize(className);
        return Class.forName(name).newInstance();
    }

    public static Object getFrontCtl(String key) throws Exception{
        return getCtl(Constants.ctlFrontPath,key);
    }
    public static Object getCtl(String key) throws Exception{
        return getCtl(Constants.ctlPath,key);
    }

    /**
     * 获取集合中对象的指定get方法的值
     * @param list 集合
     * @param methodName 方法名
     * @param params 参数 可为空
     * @return
     */
    public static List<String> getMethodValue(List<?> list, String methodName,String... params){
        List<String> result = new ArrayList<>();
        try {
            for (Object o : list){
                Method method = ReflectionUtils.getMethodLikeName(o,methodName);
                Object re;
                if (params != null){
                    re =  method.invoke(o,params);
                }else {
                    re = method.invoke(o);
                }
                if (re!=null){
                    result.add(String.valueOf(re));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;

    }

    /**
     * 获取集合中对象的指定get方法的值
     * @param list 集合
     * @param methodName 方法名
     * @return
     */
    public static List<String> getMethodValue(List<?> list, String methodName){
        List<String> result = new ArrayList<>();
        try {
            for (Object o : list){
                Method method = ReflectionUtils.getMethodLikeName(o,methodName);
                Object re = method.invoke(o);
                if (re!=null){
                    result.add(String.valueOf(re));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;

    }
    /**
     * 循环向上转型, 获     * @param object : 子类对象
     * @param fieldName : 父类中     * @return 父类中     */

    public static Field getDeclaredField(Object object, String fieldName){
        Field field = null ;

        Class<?> clazz = object.getClass() ;

        for(; clazz != Object.class ; clazz = clazz.getSuperclass()) {
            try {
                Field[] fields = clazz.getDeclaredFields();
                field = clazz.getDeclaredField(fieldName) ;
                return field ;
            } catch (Exception e) {
                //这里甚么都不能抛出去。
                //如果这里的异常打印或者往外抛，则就不会进入
            }
        }

        return null;
    }

    public static void main(String[] args) throws Exception {
        /*List<SysRole> list = new ArrayList<>();
        SysRole sysRole = new SysRole();
        sysRole.setId("111");
        list.add(sysRole);

        SysRole sysRole1 = new SysRole();
        sysRole1.setId("222");
        list.add(sysRole1);
        List<String>  strings = getMethodValue(list,"getId");
        for (String s:strings){
            System.out.println(s);
        }*/
       /* Record record = new Record();
        record.set("xxx","sss");
        record.set("yyy","ttt");
        record.set("ttt","xxx");
        List<Record> list = new ArrayList<>();
        list.add(record);
        List<String>  strings =   getMethodValue(list,"getStr","xxx");
        for (String s:strings){
            System.out.println(s);
        }*/
        getCtl("role");

    }
}
