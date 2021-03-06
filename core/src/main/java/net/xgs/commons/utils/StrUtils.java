package net.xgs.commons.utils;

import com.jfinal.kit.PathKit;
import com.jfinal.kit.StrKit;
import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.*;

/**
 * 字符串工具类
 * @author L.cm
 */
public class StrUtils extends StrKit {
    /**
     * 清除左右空格
     * @param str 字符串
     * @return String
     */
    public static String trim(String str) {
        return str == null ? null : str.trim();
    }
    
    /**
     * 清除左右空格，null to ""
     * @param str
     * @return
     */
    public static String trimToEmpty(String str) {
       if (str == null) {
           return "";
       }
       return str.trim();
    }
    
    /**
     * 生成sql占位符 ?,?,?
     * @param size
     * @return ?,?,?
     */
    public static String sqlHolder(int size) {
        String[] paras = new String[size];
        Arrays.fill(paras, "?");
        return StrUtils.join(paras, ",");
    }

    /**
     * 实现简易的模板
     * @param view
     * @param map
     * @return
     */
    public static String render(String view, Map<String, String> map) {
        String viewPath = PathKit.getWebRootPath() + view;
        try {
            String html = FileUtils.readToString(new File(viewPath));
            return format(html, map);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 字符串格式化
     * 
     * use: format("my name is {0}, and i like {1}!", "L.cm", "java")
     * 
     * int long use {0,number,#}
     * 
     * @param s 
     * @param args
     * @return 转换后的字符串
     */
    public static String format(String s, Object... args) {
        return MessageFormat.format(s, args);
    }

    /**
     * 转义HTML用于安全过滤
     * @param html
     * @return
     */
    public static String escapeHtml(String html) {
        if (html == null) return "";
        html = html.replace("<", "&lt;");
        html = html.replace(">", "&gt;");
        html = html.replace("\"", "&quot;");
        html = html.replace("\n", "</br>");
        return html;
    }

    /**
     * 清理字符串，清理出某些不可见字符
     * @param txt
     * @return {String}
     */
    public static String cleanChars(String txt) {
        return txt.replaceAll("[ 　`·•�\\f\\t\\v\\s]", "");
    }
    
    // 随机字符串
    private static final String _INT = "0123456789";
    private static final String _STR = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static final String _ALL = _INT + _STR;

    private static final Random RANDOM = new Random();

    /**
     * 生成的随机数类型
     * @author L.cm
     * @email: 596392912@qq.com
     * @site: http://www.dreamlu.net
     * @date 2015年4月20日下午9:15:23
     */
    public static enum RandomType {
        INT, STRING, ALL;
    }

    /**
     * 随机数生成
     * @param count 字符长度
     * @return 随机数
     */
    public static String random(int count) {
        return StrUtils.random(count, RandomType.ALL);
    }
    
    /**
     * 随机数生成
     * @param count 字符长度
     * @param randomType 随机数类别
     * @return 随机数
     */
    public static String random(int count, RandomType randomType) {
        if (count == 0) return "";
        if (count < 0) {
            throw new IllegalArgumentException("Requested random string length " + count + " is less than 0.");
        }
        char[] buffer = new char[count];
        for (int i = 0; i < count; i++) {
            if (randomType.equals(RandomType.INT)) {
                buffer[i] = _INT.charAt(RANDOM.nextInt(_INT.length()));
            } else if (randomType.equals(RandomType.STRING)) {
                buffer[i] = _STR.charAt(RANDOM.nextInt(_STR.length()));
            }else {
                buffer[i] = _ALL.charAt(RANDOM.nextInt(_ALL.length()));
            }
        }
        return new String(buffer);
    }
    
    public static String[] split(String str) {
        return StrUtils.split(str, ',');
    }
    
    public static String[] split(String str, char delimiter) {
        List<String> results = new ArrayList<String>();

        int ipos = 0, lastpos = 0;
        while ((ipos = str.indexOf(delimiter, lastpos)) != -1) {
            results.add(str.substring(lastpos, ipos));
            lastpos = ipos + 1;
        }
        if (lastpos < str.length()) {
            results.add(str.substring(lastpos));
        }
        return results.toArray(new String[results.size()]);
    }

    /**
     * 拼接格式化数据
     * @param size 拼接大小
     * @param str 拼接内容
     * @param mosaicStr 连接字符
     * @return
     */
    public static  String joinSymbolBySize(Integer size,String str,String mosaicStr){
        String [] result = new String[size];
        for (int i = 0;i<result.length;i++){
            result[i] = str;
        }
        return StringUtils.join(result,mosaicStr);
    }

    /**
     * 拼接in语句sql
     * @param param
     * @return
     */
    public static String joinInSql(List<?> param,char prefix){
        StringBuilder sb = new StringBuilder();
        sb.append(prefix);
        sb.append(StringUtils.join(param,prefix+","+prefix));
        sb.append(prefix);
        return sb.toString();
    }
    public static String joinInSql(List<?> param){
        return joinInSql(param,'\'');
    }

}
