package net.xgs.utils;

import net.xgs.entity.Constants;

import java.security.MessageDigest;
import java.util.Random;

public class MD5Utils {
	public final static String pwdRule(String pwd, String salt){
		String rule = pwd+ Constants.salt+salt;
		return md5(rule);
	}
	
	public final static String generate(int len){
		String str = "0,1,2,3,4,5,6,7,8,9,a,b,c,d,e,f,g,h,i,j,k,l,m,n,o,p,q,r,s,t,u,v,w,x,y,z";
        String str2[] = str.split(",");
        Random rand = new Random();
        int index = 0;  
        String randStr = "";
        for (int i=0; i<len; ++i)  
        {  
            index = rand.nextInt(str2.length-1);
            randStr += str2[index]; 
        }
        return randStr;
	}
	
	public final static String salt(){
        return generate(6);
	}
	
	public final static String md5(String s) {
        char hexDigits[]={'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};       
        try {
            byte[] btInput = s.getBytes();
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            mdInst.update(btInput);
            byte[] md = mdInst.digest();
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str).toLowerCase();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
	
	public static void main(String[] args) {
		String salt = salt();
		String password = pwdRule("admin",salt);
		System.out.println(salt+"---"+password);
		System.out.println(pwdValidate("admin","gck7ds","ad21aa17e2e0038909327354471a258d"));
	}
	
	public final static boolean pwdValidate(String pwd, String salt, String dpwd){
		return pwdRule(pwd,salt).equals(dpwd);
	}
}
