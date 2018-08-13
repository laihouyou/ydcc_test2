package com.movementinsome.caice.util;

import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zzc on 2017/5/15.
 */

public class Lanlngutil {
    /**
     * 功能：  度-->度分秒（满足图片格式）
     * @param d   传入待转化格式的经度或者纬度
     * @return
     */
    public static String DDtoDMS_photo(Double d){

        String[] array=d.toString().split("[.]");
        String D=array[0];//得到度

        Double m= Double.parseDouble("0."+array[1])*60;
        String[] array1=m.toString().split("[.]");
        String M=array1[0];//得到分

        Double s= Double.parseDouble("0."+array1[1])*60;
        String[] array2=s.toString().split("[.]");
        Double S= Double.parseDouble(array2[0]);//得到秒
        String point=D+"°"+M+"'"+s+"''";
        return  point;
    }

    /**
     * 判断邮箱是否合法
     * @param email
     * @return
     */
    public static boolean isEmail(String email){
        String strPattern = "^[a-zA-Z0-9][\\w\\.-]*[a-zA-Z0-9]@[a-zA-Z0-9][\\w\\.-]*[a-zA-Z0-9]\\.[a-zA-Z][a-zA-Z\\.]*[a-zA-Z]$";
        if (TextUtils.isEmpty(strPattern)) {
            return false;
        } else {
            return email.matches(strPattern);
        }
    }

    /**
     * 验证手机号
     * @author lipw
     * @date   2017年4月5日上午11:34:07
     * @param mobiles
     * 手机号码
     * @return
     * 有效返回true,否则返回false
     */
    public static boolean isMobileNO(String mobiles) {

        // Pattern p =
        // Pattern.compile("^((147)|(17[0-9])|(13[0-9])|(15[^4,\\D])|(18[0-9]))\\d{8}$");
        Pattern p = Pattern.compile("^((13[0-9])|(15[^4])|(18[0-9])|(17[0-9])|(147))\\d{8}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }
}
