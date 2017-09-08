package algorithm;

import java.util.stream.IntStream;

public class ChangeStringToNum {
	public static void main(String[] args) {
		String  s = "ab00cd+123fght456-25  3.005fgh";
		System.out.println(s.contains("+1"));
//		method(s);
	}
	public static void method(String s) {
		StringBuilder sb = new StringBuilder();
		int[] out = new int[s.length()];
		for(int i = 0;i<s.length();i++) {
			out[i] = -1;
		}
		int cout = 0;
		for(int i = 0;i<s.length();i++) {
			char c = s.charAt(i);
			if(c>='0'&&c<='9') {
				sb.append(s.substring(i, i+1));
			}else if(sb.length() >0) {
				out[cout++] = Integer.parseInt(sb.toString());
				sb.delete(0, sb.length());
			}
		}
		int j = 0;
		while(out[j]!=-1) {
			System.out.println(out[j++]);
		}
		
	}
}
