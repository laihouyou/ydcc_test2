package com.movementinsome.app.remind;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.movementinsome.AppContext;
import com.movementinsome.database.vo.BsEmphasisInsArea;
import com.movementinsome.database.vo.BsInsFacInfo;
import com.movementinsome.database.vo.BsLeakInsArea;
import com.movementinsome.database.vo.BsRoutineInsArea;
import com.movementinsome.database.vo.BsSupervisionPoint;
import com.movementinsome.database.vo.HolidayManage;
import com.movementinsome.database.vo.InsCheckFacRoad;
import com.movementinsome.database.vo.InsKeyPointPatrolData;
import com.movementinsome.database.vo.InsPatrolAreaData;
import com.movementinsome.database.vo.InsPatrolDataVO;
import com.movementinsome.database.vo.InsPlanTaskVO;
import com.movementinsome.database.vo.InsSiteManage;
import com.movementinsome.kernel.util.MyDateTools;
import com.j256.ormlite.dao.Dao;

public class FreqData {

	public static Dao<InsPatrolDataVO, Long> insPatrolDataDao;
	public static Dao<InsPlanTaskVO, Long> insPatrolTaskDao;
	
	public static Dao<InsSiteManage, Long> insSiteManageDao;
	public static Dao<InsKeyPointPatrolData, Long> insKeyPointPatrolDataDao;
	public static Dao<HolidayManage, Long> holidayManageDao;
	public static Dao<BsSupervisionPoint, Long> bsSupervisionPointDao;
	public static Dao<BsRoutineInsArea, Long> bsRoutineInsAreaDao;
	public static Dao<BsLeakInsArea, Long> bsLeakInsAreaDao;
	public static Dao<BsInsFacInfo, Long> bsInsFacInfoDao;
	public static Dao<BsEmphasisInsArea, Long> bsEmphasisInsAreaDao;
	// 中山区域
	public static Dao<InsPatrolAreaData, Long> insPatrolAreaDataDao;
	
	public static Dao<InsCheckFacRoad, Long> insCheckFacRoadDao;
	static {
		try {
			insPatrolDataDao = AppContext.getInstance().getAppDbHelper()
					.getDao(InsPatrolDataVO.class);
			insPatrolTaskDao = AppContext.getInstance().getAppDbHelper()
					.getDao(InsPlanTaskVO.class);
			
			insSiteManageDao = AppContext.getInstance().getAppDbHelper()
					.getDao(InsSiteManage.class);
			
			insKeyPointPatrolDataDao = AppContext.getInstance().getAppDbHelper()
			.getDao(InsKeyPointPatrolData.class);
			holidayManageDao = AppContext.getInstance().getAppDbHelper()
			.getDao(HolidayManage.class);
			bsSupervisionPointDao = AppContext.getInstance().getAppDbHelper()
			.getDao(BsSupervisionPoint.class);
			bsRoutineInsAreaDao = AppContext.getInstance().getAppDbHelper()
			.getDao(BsRoutineInsArea.class);
			bsLeakInsAreaDao = AppContext.getInstance().getAppDbHelper()
			.getDao(BsLeakInsArea.class);
			bsInsFacInfoDao = AppContext.getInstance().getAppDbHelper()
			.getDao(BsInsFacInfo.class);
			bsEmphasisInsAreaDao =  AppContext.getInstance().getAppDbHelper()
			.getDao(BsEmphasisInsArea.class);
			
			insPatrolAreaDataDao = AppContext.getInstance().getAppDbHelper()
					.getDao(InsPatrolAreaData.class);
			
			insCheckFacRoadDao = AppContext.getInstance().getAppDbHelper()
					.getDao(InsCheckFacRoad.class);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//返回当天日期
	public int getCurday(){
		SimpleDateFormat f=new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date(System.currentTimeMillis());

		//String d = f.format(date);
		String[] ymd = f.format(date).split("\\-");
		return Integer.valueOf(ymd[2]);
	}
	public String getToDate(){
		SimpleDateFormat f=new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date(System.currentTimeMillis());
		String[] ymd = f.format(date).split("\\-");
		return ymd[2];
	}
	public String getToMonth(){
		SimpleDateFormat f=new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date(System.currentTimeMillis());
		String[] ymd = f.format(date).split("\\-");
		return ymd[1];
	}
	public String getToYear(){
		SimpleDateFormat f=new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date(System.currentTimeMillis());
		String[] ymd = f.format(date).split("\\-");
		return ymd[0];
	}
	public String getToYear(String date){
		SimpleDateFormat f=new SimpleDateFormat("yyyy-MM-dd");
		//Date date = new Date(System.currentTimeMillis());
		String[] ymd;
		try {
			ymd = f.format(f.parse(date)).split("\\-");
			return ymd[0];
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}
	
	public int getCurYMonth(){
		SimpleDateFormat f=new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date(System.currentTimeMillis());
		String[] ymd = f.format(date).split("\\-");
		
		return Integer.valueOf(ymd[0]+ymd[1]);
	}
	
	public String getToYMonth(String date){
		SimpleDateFormat f=new SimpleDateFormat("yyyy-MM-dd");
		//Date date = new Date(System.currentTimeMillis());
		String[] ymd;
		try {
			ymd = f.format(f.parse(date)).split("\\-");
			return ymd[0]+ymd[1];
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return "";

	}
	
	public String getToYWeek(String date){
		SimpleDateFormat f=new SimpleDateFormat("yyyy-MM-dd");
		//Date date = new Date(System.currentTimeMillis());
		String[] ymd;
		try {
			Date d = f.parse(date);
			ymd = f.format(d).split("\\-");
			Calendar calendar = Calendar.getInstance();
			calendar.setFirstDayOfWeek(Calendar.MONDAY);
			calendar.setTime(d);
			int week = calendar.get(Calendar.WEEK_OF_YEAR);
			return ymd[0]+week;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return "";

	}
	public String getCurYWeek(){
		SimpleDateFormat f=new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date(System.currentTimeMillis());
		String[] ymd;
		ymd = f.format(date).split("\\-");
		Calendar calendar = Calendar.getInstance();
		calendar.setFirstDayOfWeek(Calendar.MONDAY);
		calendar.setTime(date);
		int week = calendar.get(Calendar.WEEK_OF_YEAR);
		return ymd[0]+week;
	}
	public String getToMonth(String date){
		SimpleDateFormat f=new SimpleDateFormat("yyyy-MM-dd");
		//Date date = new Date(System.currentTimeMillis());
		String[] ymd;
		try {
			ymd = f.format(f.parse(date)).split("\\-");
			return ymd[1];
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return "";

	}
	
	
	public Integer getCurYear(){
		SimpleDateFormat f=new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date(System.currentTimeMillis());
		String[] ymd = f.format(date).split("\\-");
		
		return Integer.valueOf(ymd[0]);

	}
	
	public Integer getCurMonth(){
		SimpleDateFormat f=new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date(System.currentTimeMillis());
		String[] ymd = f.format(date).split("\\-");
		
		return Integer.valueOf(ymd[1]);

	}
	
	public String getCurDate(){
		SimpleDateFormat f=new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date(System.currentTimeMillis());

		return f.format(date);
	}
	
	public String getToDate2(){
		SimpleDateFormat f=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date(System.currentTimeMillis());

		return f.format(date);
	}
	
	public Date getDate(String date){
		SimpleDateFormat f=new SimpleDateFormat("yyyy-MM-dd");
		//Date date = new Date(System.currentTimeMillis());
		try {
			return f.parse(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	
	public String date2String (String date){
		SimpleDateFormat f=new SimpleDateFormat("yyyy-MM-dd");
		//Date date = new Date(System.currentTimeMillis());
		try {
			return f.format(f.parse(date));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	
	//返回当天周数
	public int getWeek(){
		int week = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
		if (week==1){
			return 7;
		}else{
			return week-1;
		}
	}
	public int getWeek(String date){
		SimpleDateFormat f=new SimpleDateFormat("yyyy-MM-dd");
		Calendar c = Calendar.getInstance();
		try {
			c.setTime(f.parse(date));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		int week = c.get(Calendar.DAY_OF_WEEK);
		if (week==1){
			return 7;
		}else{
			return week-1;
		}
	}
	
	public long getDaySub(String beginDateStr,String endDateStr){
		return MyDateTools.getDaySub(beginDateStr, endDateStr);
	}
	
	public long getHours(String bigDate,String lowDate){
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		Date d1 = null,d2 = null;
		try {
			d1 = df.parse(bigDate);
			d2  = df.parse(lowDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		  
	  long diff = d1.getTime() - d2.getTime();//这样得到的差值是微秒级别
	  long days = diff / (1000 * 60 * 60 * 24);
	 
	  long hours = (diff-days*(1000 * 60 * 60 * 24))/(1000* 60 * 60);
	  long minutes = (diff-days*(1000 * 60 * 60 * 24)-hours*(1000* 60 * 60))/(1000* 60);
	  
	  return days*24+hours;
	}
	
	public double round(double d){
		int t = (int)Math.round(d*100);
		return t/100.0;
	}
	public static FreqObject changChkObj(InsPatrolAreaData obj){
		FreqObject o = new FreqObject();
		o.setFrequency(obj.getFrequency());
		o.setFrequencyNumber(obj.getFrequencyNumber());
		o.setFrequencyType(obj.getFrequencyType());
		o.setInsCount(obj.getInsCount());
		o.setLastInsDateStr(obj.getLastInsDateStr());
		return o;
	}
	public static FreqObject changChkObj(InsPatrolDataVO obj){
		FreqObject o = new FreqObject();
		o.setFacType(obj.getFacType());
		o.setFrequency(obj.getFrequency());
		o.setFrequencyNumber(obj.getFrequencyNumber());
		o.setFrequencyType(obj.getFrequencyType());
		o.setInsCount(obj.getInsCount());
		o.setLastInsDateStr(obj.getLastInsDateStr());
		return o;
	}
	public static FreqObject changChkObj(InsKeyPointPatrolData obj){
		FreqObject o = new FreqObject();
		o.setFrequency(obj.getFrequency());
		o.setFrequencyNumber(obj.getFrequencyNumber());
		o.setFrequencyType(obj.getFrequencyType());
		o.setInsCount(obj.getInsCount());
		o.setLastInsDateStr(obj.getLastInsDate());
		return o;
	}
	
	public static FreqObject changChkObj(InsSiteManage obj){
		FreqObject o = new FreqObject();
		//o.setFacType(obj.getFacType());
		o.setFrequency(obj.getFrequency());
		o.setFrequencyNumber(obj.getFrequencyNumber());
		o.setFrequencyType(obj.getFrequencyType());
		o.setInsCount(obj.getInsCount());
		o.setLastInsDateStr(obj.getLastInsDateStr());
		return o;
	}
	public static FreqObject changChkObj(BsSupervisionPoint obj){
		FreqObject o = new FreqObject();
		//o.setFacType(obj.getFacType());
		o.setFrequency(obj.getFrequency());
		o.setFrequencyNumber(obj.getFrequencyNumber());
		o.setFrequencyType(obj.getFrequencyType());
		o.setInsCount(obj.getInsCount());
		o.setLastInsDateStr(obj.getLastInsDateStr());
		return o;
	}
	public static FreqObject changChkObj(BsRoutineInsArea obj){
		FreqObject o = new FreqObject();
		//o.setFacType(obj.getFacType());
		o.setFrequency(obj.getFrequency());
		o.setFrequencyNumber(obj.getFrequencyNumber());
		o.setFrequencyType(obj.getFrequencyType());
		o.setInsCount(obj.getInsCount());
		o.setLastInsDateStr(obj.getLastInsDateStr());
		return o;
	}
	public static FreqObject changChkObj(BsLeakInsArea obj){
		FreqObject o = new FreqObject();
		//o.setFacType(obj.getFacType());
		o.setFrequency(obj.getFrequency());
		o.setFrequencyNumber(obj.getFrequencyNumber());
		o.setFrequencyType(obj.getFrequencyType());
		o.setInsCount(obj.getInsCount());
		o.setLastInsDateStr(obj.getLastInsDateStr());
		return o;
	}
	public static FreqObject changChkObj(BsInsFacInfo obj){
		FreqObject o = new FreqObject();
		//o.setFacType(obj.getFacType());
		o.setFrequency(obj.getFrequency());
		o.setFrequencyNumber(obj.getFrequencyNumber());
		o.setFrequencyType(obj.getFrequencyType());
		o.setInsCount(obj.getInsCount());
		o.setLastInsDateStr(obj.getLastInsDateStr());
		return o;
	}
	public static FreqObject changChkObj(BsEmphasisInsArea obj){
		FreqObject o = new FreqObject();
		//o.setFacType(obj.getFacType());
		o.setFrequency(obj.getFrequency());
		o.setFrequencyNumber(obj.getFrequencyNumber());
		o.setFrequencyType(obj.getFrequencyType());
		o.setInsCount(obj.getInsCount());
		o.setLastInsDateStr(obj.getLastInsDateStr());
		return o;
	}
	
}
