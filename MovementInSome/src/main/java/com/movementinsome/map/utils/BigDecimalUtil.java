package com.movementinsome.map.utils;

import java.math.BigDecimal;

public class BigDecimalUtil {
	public static String getBigDecimal(double value){
		int   scale   =   4;//设置位数 
		int   roundingMode   =   4;//表示四舍五入，可以选择其他舍值方式，例如去尾，等等. 
		BigDecimal   bd   =   new   BigDecimal(value); 
		bd   =   bd.setScale(scale,roundingMode); 
		return bd.toString();
	}
}
