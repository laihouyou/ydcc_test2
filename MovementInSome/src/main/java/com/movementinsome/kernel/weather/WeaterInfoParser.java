package com.movementinsome.kernel.weather;

import java.util.StringTokenizer;

public class WeaterInfoParser {
	 /** 
     * 通过解析content，得到一个一维为城市编号，二维为城市名的二维数组 
     * 解析的字符串的形式为: <code>编号|城市名,编号|城市名,.....</code> 
     * @param content 需要解析的字符串 
     * @return 封装有城市编码与名称的二维数组 
     */  
    public static String[][] parseCity(String content) {  
        //判断content不为空  
        if(content!=null&&content.trim().length()!=0) {  
            StringTokenizer st=new StringTokenizer(content, ",");  
            int count = st.countTokens();  
            String[][] citys = new String[count][2];  
            int i=0, index=0;  
            while(st.hasMoreTokens()) {  
                String city = st.nextToken();  
                index = city.indexOf('|');  
                citys[i][0] = city.substring(0, index);  
                citys[i][1] = city.substring(index+1);  
                i = i+1;  
            }  
            return citys;  
        }  
        return null;  
    }  
}
