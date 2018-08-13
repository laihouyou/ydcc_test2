package com.movementinsome.app.pub.dialog;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import com.movementinsome.app.pub.Constant;

import java.util.Calendar;


public class DateTimeDialog {
	private static int mYear;						//年
	private static int mMonth;						//月
	private static int mDay;						//日
	private static int mHour;						//时
	private static int mMinute;						//分
	private static int mSecond;						//秒
	private static TextView et;// 显示控件
	private static boolean isShowSecond;

	private Context mContext;

	public static void showDateDialog(Context context,TextView editText){
		Calendar c = Calendar.getInstance();
		et=editText;
		new DatePickerDialog(context, FDateSelistener, c.get(Calendar.YEAR),c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();

	}
	public  void showDateDialog2(Context context,TextView editText){
		mContext=context;
		Calendar c = Calendar.getInstance();
		et=editText;
		new DatePickerDialog(context, FDateSelistener2, c.get(Calendar.YEAR),c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();

	}
	public static void showTimeDialog(Context context,TextView editText){
		Calendar c = Calendar.getInstance();
		et=editText;
		isShowSecond = true;
		mSecond=c.get(Calendar.SECOND);
		new TimePickerDialog(context, FTimeSelistener, c.get(Calendar.HOUR), c.get(Calendar.MINUTE), false).show();
	}
	public static void showTimeDialog(Context context,TextView editText,boolean showSecond){
		Calendar c = Calendar.getInstance();
		et=editText;
		mSecond=c.get(Calendar.SECOND);
		isShowSecond = showSecond;
		new TimePickerDialog(context, FTimeSelistener, c.get(Calendar.HOUR), c.get(Calendar.MINUTE), false).show();
	}
	/**
	 * 获取日期值并显示回对象
	 */
	private static void showDate(){
		String strDate = new StringBuilder().append(mYear).append("-")
				.append(mMonth+1>9?mMonth+1:"0"+(mMonth+1)).append("-").
				append(mDay>9?mDay:"0"+mDay).toString();
		et.setText(strDate);
	}
	/**
	 * 获取日期值并显示回对象
	 */
	private  void showDate2(){
		String strDate = new StringBuilder().append(mYear).append("-")
				.append(mMonth+1>9?mMonth+1:"0"+(mMonth+1)).append("-").
				append(mDay>9?mDay:"0"+mDay).toString();
		et.setText(strDate);
		Intent intent=new Intent();
		intent.setPackage(mContext.getPackageName());
		intent.setAction(Constant.DATE);
		mContext.sendBroadcast(intent);
	}

	/**
	 * 获取时间值并显示回对象
	 */
	private static void showTime() {
		// TODO Auto-generated method stub
		String strTime = new StringBuilder().append(mHour>9?mHour:"0"+mHour).append(":")
				.append(mMinute>9?mMinute:"0"+mMinute)
				.append(":").append(mSecond>9?mSecond:"0"+mSecond).toString();
		et.setText(strTime);
	}

	private static void showTime2() {
		// TODO Auto-generated method stub
		String strTime = new StringBuilder().append(mHour>9?mHour:"0"+mHour).append(":")
				.append(mMinute>9?mMinute:"0"+mMinute).toString();
		et.setText(strTime);
	}
	/**
	 * 得到：年，月，日的值，并显示
	 */
	private static DatePickerDialog.OnDateSetListener FDateSelistener = new DatePickerDialog.OnDateSetListener() {
		@Override
		public void onDateSet(DatePicker view,
				int year, int month, int day) {
			mYear=year;
			mMonth=month;
			mDay=day;
			showDate();
		}
	};
	/**
	 * 得到：年，月，日的值，并显示
	 */
	private  DatePickerDialog.OnDateSetListener FDateSelistener2 = new DatePickerDialog.OnDateSetListener() {
		@Override
		public void onDateSet(DatePicker view,
				int year, int month, int day) {
			mYear=year;
			mMonth=month;
			mDay=day;
			showDate2();
		}
	};
	/**
	 * 得到：时，分的值，并显示
	 */
	private static TimePickerDialog.OnTimeSetListener FTimeSelistener = new TimePickerDialog.OnTimeSetListener() {
		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			mHour = hourOfDay;
			mMinute = minute;
			if(isShowSecond){
				showTime();
			}else{
				showTime2();
			}
			

		}

	};
}
