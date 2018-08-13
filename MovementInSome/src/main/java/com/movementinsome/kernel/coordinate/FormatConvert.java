package com.movementinsome.kernel.coordinate;

public class FormatConvert {

	/// <summary>
    /// 将角度的小数形式转换为弧度
    /// </summary>
    /// <param name="x"></param>
    /// <returns></returns>
	public static double d2dd(double degree)
    {
        return (degree * Math.PI / 180.0);
    }
    
	// / <summary>
	// / 弧度制度数转换成度分秒角度格式
	// / </summary>
	// / <param name="doubleDegree">弧度制</param>
	// / <returns>格式如：115:23:25.24</returns>
	public static String dd2dms(double doubleDegree) {
		double Degree = doubleDegree * 180.0 / Math.PI;
		double Minute = (Degree - (int) Degree) * 60.0;
		double Second = (Minute - (int) Minute) * 60;
		String DegreeMinuteSecond = (int) Degree + ":" + (int) Minute + ":"
				+ Second;
		return DegreeMinuteSecond;
	}

	// / <summary>
	// / 度分秒角度格式转换成弧度制度数
	// / </summary>
	// / <param name="degreeMinuteSecond">格式如：115 23 25.24</param>
	// / <returns>返回弧度制</returns>
	public static double dms2dd(String degreeMinuteSecond) {
		String[] str = degreeMinuteSecond.split(":");
		int Degree = Integer.parseInt(str[0]);
		int Minute = Integer.parseInt(str[1]);
		double Second = Double.parseDouble(str[2]);
		double DoubleDegree = Degree / 180.0 * Math.PI + Minute / 60.0 / 180.0
				* Math.PI + Second / 60.0 / 60.0 / 180.0 * Math.PI;
		return DoubleDegree;
	}
}
