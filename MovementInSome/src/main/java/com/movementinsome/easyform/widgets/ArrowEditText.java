package com.movementinsome.easyform.widgets;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import com.movementinsome.R;

import java.util.Calendar;


public class ArrowEditText extends EditText {
	private Context context;

	private Drawable mRightDrawable;
	private boolean isHasFocus;
	private Drawable img_right;
	private boolean readOnly;
	
	private String type;// date or time
	
	private int mYear;
	private int mMonth;
	private int mDay;
	private int mHour;
	private int mMinute;
	private int mSecond;
	
	public ArrowEditText(Context context,String type,boolean readOnly) {
		super(context);
		this.type=type;
		this.readOnly = readOnly;
		init(context);
	}

	public ArrowEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public ArrowEditText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	private void init(final Context context) {
		// getCompoundDrawables:
		// Returns drawables for the left, top, right, and bottom borders.
		this.context = context;
				
		Resources res = getResources();
		if("date".equals(type)){
			img_right = res.getDrawable(R.drawable.date_icon);
		}else if("time".equals(type)){
			img_right = res.getDrawable(R.drawable.time_icon);
		}
		// ����setCompoundDrawablesʱ���������Drawable.setBounds()����,����ͼƬ����ʾ
		img_right.setBounds(0, 0, img_right.getMinimumWidth(),
				img_right.getMinimumHeight());
		this.setBackgroundResource(R.drawable.input_icon);
		this.setCompoundDrawables(null, null, img_right, null); // ������ͼ��

		// Drawable [] drawables=this.getCompoundDrawables();

		// ȡ��rightλ�õ�Drawable
		// �������ڲ����ļ������õ�android:drawableRight
		mRightDrawable = this.getResources().getDrawable(
				R.drawable.arrow_drawer);

		// ���ý���仯�ļ���
		this.setOnFocusChangeListener(new FocusChangeListenerImpl());

		// ����EditText���ֱ仯�ļ���
		// this.addTextChangedListener(new TextWatcherImpl());
		// ��ʼ��ʱ���ұ�cleanͼ�겻�ɼ�
		// setClearDrawableVisible(false);
		
	}

	/**
	 * ����ָ̧���λ����clean��ͼ������� ���ǽ�����Ϊ���������� getWidth():�õ��ؼ��Ŀ��
	 * event.getX():̧��ʱ�����(�����������ڿؼ�������Ե�)
	 * getTotalPaddingRight():clean��ͼ�����Ե���ؼ��ұ�Ե�ľ���
	 * getPaddingRight():clean��ͼ���ұ�Ե���ؼ��ұ�Ե�ľ��� ����: getWidth() -
	 * getTotalPaddingRight()��ʾ: �ؼ���ߵ�clean��ͼ�����Ե������ getWidth() -
	 * getPaddingRight()��ʾ: �ؼ���ߵ�clean��ͼ���ұ�Ե������ ����������֮�������պ���clean��ͼ�������
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_UP:

			/*boolean isClick = (event.getX() > (getWidth() - getTotalPaddingRight()))
					&& (event.getX() < (getWidth() - getPaddingRight()));
			if (isClick) {*/
			if(!readOnly){
				if("date".equals(type)){
					showDateDialog();
				}else if("time".equals(type)){
					showTimeDialog();
				}
			}		
			//}
			break;

		default:
			break;
		}
		return super.onTouchEvent(event);
	}

	private class FocusChangeListenerImpl implements OnFocusChangeListener {
		@Override
		public void onFocusChange(View v, boolean hasFocus) {
			// if (hasFocus)
			// isHasFocus=hasFocus;
			// setClickDrawableVisible(true);
			/*
			 * if (isHasFocus) { boolean
			 * isVisible=getText().toString().length()>=1;
			 * setClearDrawableVisible(isVisible); } else {
			 * setClearDrawableVisible(false); }
			 */
		}

	}

	// �����������ж��Ƿ���ʾ�ұ�clean��ͼ��
	private class TextWatcherImpl implements TextWatcher {
		@Override
		public void afterTextChanged(Editable s) {
			boolean isVisible = getText().toString().length() >= 1;
			setClickDrawableVisible(isVisible);
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {

		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {

		}

	}

	// ���ػ�����ʾ�ұ�clean��ͼ��
	protected void setClickDrawableVisible(boolean isVisible) {
		Drawable rightDrawable;
		if (isVisible) {
			rightDrawable = mRightDrawable;
		} else {
			rightDrawable = null;
		}
		// ʹ�ô������øÿؼ�left, top, right, and bottom����ͼ��
		/*
		 * setCompoundDrawables(getCompoundDrawables()[0],getCompoundDrawables()[
		 * 1], rightDrawable,getCompoundDrawables()[3]);
		 */
		setCompoundDrawables(null, null, img_right, null); // ������ͼ��
	}

	// ��ʾһ������,����ʾ�û�����
	public void setShakeAnimation() {
		this.setAnimation(shakeAnimation(5));
	}

	// CycleTimes�����ظ��Ĵ���
	public Animation shakeAnimation(int CycleTimes) {
		Animation translateAnimation = new TranslateAnimation(0, 10, 0, 10);
		translateAnimation.setInterpolator(new CycleInterpolator(CycleTimes));
		translateAnimation.setDuration(1000);
		return translateAnimation;
	}

	public void showDateDialog(){
		final Calendar c = Calendar.getInstance();
		mYear = c.get(Calendar.YEAR);
		mMonth = c.get(Calendar.MONTH);
		mDay = c.get(Calendar.DAY_OF_MONTH);

		mHour = c.get(Calendar.HOUR_OF_DAY);
		mMinute = c.get(Calendar.MINUTE);
		
		new DatePickerDialog(context, mDateSetListener, mYear, mMonth,
				mDay).show();
	}
	
	private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			mYear = year;
			String mm;
			String dd;
			if (monthOfYear <= 8) {
				mMonth = monthOfYear + 1;
				mm = "0" + mMonth;
			} else {
				mMonth = monthOfYear + 1;
				mm = String.valueOf(mMonth);
			}
			if (dayOfMonth <= 8) {
				mDay = dayOfMonth;
				dd = "0" + mDay;
			} else {
				mDay = dayOfMonth;
				dd = String.valueOf(mDay);
			}
			mDay = dayOfMonth;
			
			setText(String.valueOf(mYear) + "-" + mm + "-"
					+ dd);
		}
	};
	public void showTimeDialog(){
		final Calendar c = Calendar.getInstance();
		mHour = c.get(Calendar.HOUR_OF_DAY);
		mMinute = c.get(Calendar.MINUTE);
		
		new TimePickerDialog(context, mTimeSelistener, mHour, mMinute, true).show();
	}

	TimePickerDialog.OnTimeSetListener mTimeSelistener = new TimePickerDialog.OnTimeSetListener() {

		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			// TODO Auto-generated method stub
			final Calendar c = Calendar.getInstance();
			mSecond=c.get(Calendar.SECOND);
			String strTime = new StringBuilder().append(hourOfDay>9?hourOfDay:"0"+hourOfDay).append(":")
								.append(minute>9?minute:"0"+minute).append(":").
								append(mSecond>9?mSecond:"0"+mSecond).toString();
			setText(strTime);
		}
		
	};

}
