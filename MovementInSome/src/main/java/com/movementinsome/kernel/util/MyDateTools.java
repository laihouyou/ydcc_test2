package com.movementinsome.kernel.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.content.Context;
import android.net.ParseException;
import android.widget.TextView;

public class MyDateTools {

	private static Calendar c = null;

	private static int mYear; // 年
	private static int mMonth; // 月
	private static int mDay; // 日
	private static int mHour; // 时
	private static int mMinute; // 分
	private static int mSecond; // 秒
	private static String dateTime = ""; // 系统日期存储
	
	public static final String S_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

	/**
	 * 获取系统日期
	 * 
	 * @param date_type
	 *            :time_date,date,time
	 * @return
	 */
	public static String getDate(String date_type) {
		c = Calendar.getInstance();
		mYear = c.get(Calendar.YEAR);
		mMonth = c.get(Calendar.MONTH);
		mDay = c.get(Calendar.DAY_OF_MONTH);
		mHour = c.get(Calendar.HOUR_OF_DAY);
		mMinute = c.get(Calendar.MINUTE);
		mSecond = c.get(Calendar.SECOND);

		if ("date".equals(date_type)) {
			dateTime = mYear + "-"
					+ ((mMonth + 1) > 9 ? (mMonth + 1) : "0" + (mMonth + 1))
					+ "-" + (mDay > 9 ? mDay : "0" + mDay);
		} else if ("time".equals(date_type)) {
			dateTime = (mHour > 9 ? mHour : "0" + mHour) + ":"
					+ (mMinute > 9 ? mMinute : "0" + mMinute) + ":"
					+ (mSecond > 9 ? mSecond : "0" + mSecond);
		} else if ("date_time".equals(date_type)) {
			dateTime = mYear + "-"
					+ ((mMonth + 1) > 9 ? (mMonth + 1) : "0" + (mMonth + 1))
					+ "-" + (mDay > 9 ? mDay : "0" + mDay) + " "
					+ (mHour > 9 ? mHour : "0" + mHour) + ":"
					+ (mMinute > 9 ? mMinute : "0" + mMinute) + ":"
					+ (mSecond > 9 ? mSecond : "0" + mSecond);
		} else if ("number".equals(date_type)) {
			dateTime = mYear + "" + mMonth + "" + mDay + "" + mHour + ""
					+ mMinute + "" + mSecond;
		}
		return dateTime;
	}

	/**
	 * 获取系统日期（带周期类型）
	 * 
	 * @param type
	 *            (login,map,phone)
	 * @return
	 */
	/*
	 * public static String getWeek(String type) { String timeStr =
	 * "yyyy-MM-dd HH:mm:ss"; if ("login".equals(type)) timeStr =
	 * "yyyy-MM-dd, E, HH:mm"; else if ("map".equals(type)) timeStr =
	 * "HH时mm分    E"; else if ("phone".equals(type)) timeStr = "yyMMdd"; return
	 * new SimpleDateFormat(timeStr).format(new Date());
	 * 
	 * }
	 */

	/**
	 * 判断当前日期是星期几
	 * 
	 * @param pTime
	 *            设置的需要判断的时间 //格式如2012-09-08
	 * 
	 * 
	 * @return dayForWeek 判断结果
	 * @Exception 发生异常
	 */

	// String pTime = "2012-03-12";
	private String getWeek(String pTime) {

		String week = "";

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Calendar c = Calendar.getInstance();
		try {
			c.setTime(format.parse(pTime));
		} catch (java.text.ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (c.get(Calendar.DAY_OF_WEEK) == 1) {
			week += "天";
		}
		if (c.get(Calendar.DAY_OF_WEEK) == 2) {
			week += "一";
		}
		if (c.get(Calendar.DAY_OF_WEEK) == 3) {
			week += "二";
		}
		if (c.get(Calendar.DAY_OF_WEEK) == 4) {
			week += "三";
		}
		if (c.get(Calendar.DAY_OF_WEEK) == 5) {
			week += "四";
		}
		if (c.get(Calendar.DAY_OF_WEEK) == 6) {
			week += "五";
		}
		if (c.get(Calendar.DAY_OF_WEEK) == 7) {
			week += "六";
		}
		
		return week;
	}

	/**
	 * 传递属性，获取时间对象
	 * 
	 * @param context
	 *            ：所属对象
	 * @param tvTime
	 *            ：显示空间对象
	 * @param type
	 *            :获取的类型：日期，时间
	 */
	public void tvShowTime(Context context, TextView tvTime, String type) {
		/*
		 * this.tvTime = tvTime; this.strTvType = type; if("date".equals(type))
		 * ((Activity)context).showDialog(SHOW_DATE); else
		 * ((Activity)context).showDialog(SHOW_TIME);
		 */
	}

	/**
	 * 获取时间值并显示回对象
	 */
	public void showTime() {
		// TODO Auto-generated method stub
		/*
		 * String strTime = new
		 * StringBuilder().append(MyPublicData.mHour>9?MyPublicData
		 * .mHour:"0"+MyPublicData.mHour).append(":")
		 * .append(MyPublicData.mMinute
		 * >9?MyPublicData.mMinute:"0"+MyPublicData.mMinute).append(":").
		 * append(
		 * MyPublicData.mSecond>9?MyPublicData.mSecond:"0"+MyPublicData.mSecond
		 * ).toString(); if("date_time".equals(strTvType))
		 * tvTime.setText(MyPublicData.MyDate("date")+ " "+strTime); else
		 * tvTime.setText(strTime);
		 */
	}

	/**
	 * 获取日期值并显示回对象
	 */
	public void showDate() {
		/*
		 * String strDate = new
		 * StringBuilder().append(MyPublicData.mYear).append("-")
		 * .append(MyPublicData
		 * .mMonth+1>9?MyPublicData.mMonth+1:"0"+(MyPublicData
		 * .mMonth+1)).append("-").
		 * append(MyPublicData.mDay>9?MyPublicData.mDay:
		 * "0"+MyPublicData.mDay).toString(); tvTime.setText(strDate);
		 */
	}

	 /** 
     * @param String sourceTime 待转化的时间 
     * @param String dataFormat 日期的组织形式 
     * @return long 当前时间的长整型格式,如 1247771400000 
     */  
    public static long string2long(String sourceTime,String dataFormat){  
        long longTime = 0L;  
        DateFormat f = new SimpleDateFormat(dataFormat);  
        Date d = null;  
        try {  
            d = f.parse(sourceTime);  
        } catch (ParseException e) {  
            e.printStackTrace();  
        } catch (java.text.ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
        longTime = d.getTime();  
        return longTime;  
    }  
      
    /** 
     * 长整型转换为日期类型    
     * @param long longTime 长整型时间 
     * @param String dataFormat 时间格式 
     * @return String 长整型对应的格式的时间 
     */  
    public static String long2String(long longTime,String dataFormat)  
    {  
        Date d = new Date(longTime);  
        SimpleDateFormat s = new SimpleDateFormat(dataFormat);  
        String str = s.format(d);  
        return str;  
  
    }
    
    public static Date string2Date(String date, String format) {
		SimpleDateFormat bartDateFormat = new SimpleDateFormat(format);
		Date d = null;
		try {
			d = (Date) bartDateFormat.parse(date);
		} catch (java.text.ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return d;
	}
    
    public static String date2String(Date date){
		SimpleDateFormat f=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String sdate = f.format(date);
		return sdate;
	}
    
    public static long getDaySub(String beginDateStr,String endDateStr)
    {
        long day=0;
        java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd");    
        java.util.Date beginDate;
        java.util.Date endDate;
        try
        {
			beginDate = format.parse(beginDateStr);
            endDate= format.parse(endDateStr);    
            day=(endDate.getTime()-beginDate.getTime())/(24*60*60*1000);    
            //System.out.println("相隔的天数="+day);   
        }catch (java.text.ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return day;
    }
    
	public static String getCurDate(){
		SimpleDateFormat f=new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date(System.currentTimeMillis());

		return f.format(date);
	}
	public static String getAfterDaysDate(Date d,int count){
		SimpleDateFormat f=new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date(d.getTime()+count*24*60*60*1000);
		return f.format(date);
	}
	public static String getBeforeDaysDate(Date d,int count){
		SimpleDateFormat f=new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date(d.getTime()-count*24*60*60*1000);
		return f.format(date);
	}
	
	public static String getCurDateTime(){
		SimpleDateFormat f=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date(System.currentTimeMillis());

		return f.format(date);
	}
}
