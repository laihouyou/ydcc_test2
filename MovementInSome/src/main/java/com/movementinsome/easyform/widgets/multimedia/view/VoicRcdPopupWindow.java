package com.movementinsome.easyform.widgets.multimedia.view;


import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.PopupWindow;

import com.movementinsome.R;
import com.movementinsome.easyform.widgets.multimedia.SoundMeter;

public class VoicRcdPopupWindow extends PopupWindow {
	private ImageView volume;
	private SoundMeter mSensor;
	private Handler mHandler = new Handler();

	public VoicRcdPopupWindow(Context context) {
		View view = View.inflate(context, R.layout.voice_rcd_hint_window, null);
		volume = (ImageView) view.findViewById(R.id.volume);
		// 设置SelectPicPopupWindow的View
		this.setContentView(view);
		// 设置SelectPicPopupWindow弹出窗体的宽
		this.setWidth(LayoutParams.WRAP_CONTENT);
		// 设置SelectPicPopupWindow弹出窗体的高
		this.setHeight(LayoutParams.WRAP_CONTENT);
		// 设置SelectPicPopupWindow弹出窗体可点击
		this.setFocusable(false);
		this.setOutsideTouchable(true);
		mSensor = new SoundMeter();
	}

	private void updateDisplay(double signalEMA) {

		switch ((int) signalEMA) {
		case 0:
		case 1:
			volume.setImageResource(R.drawable.amp1);
			break;
		case 2:
		case 3:
			volume.setImageResource(R.drawable.amp2);

			break;
		case 4:
		case 5:
			volume.setImageResource(R.drawable.amp3);
			break;
		case 6:
		case 7:
			volume.setImageResource(R.drawable.amp4);
			break;
		case 8:
		case 9:
			volume.setImageResource(R.drawable.amp5);
			break;
		case 10:
		case 11:
			volume.setImageResource(R.drawable.amp6);
			break;
		default:
			volume.setImageResource(R.drawable.amp7);
			break;
		}
	}
	private static final int POLL_INTERVAL = 300;
	private Runnable mPollTask = new Runnable() {
		public void run() {
			double amp = mSensor.getAmplitude();
			updateDisplay(amp);
			mHandler.postDelayed(mPollTask, POLL_INTERVAL);
		}
	};

	public void start(String path,String name) {
		mSensor.start(path,name);
		mHandler.postDelayed(mPollTask, POLL_INTERVAL);
	}

	private Runnable mSleepTask = new Runnable() {
		public void run() {
			stop();
		}
	};
	public void stop() {
		mHandler.removeCallbacks(mSleepTask);
		mHandler.removeCallbacks(mPollTask);
		mSensor.stop();
		volume.setImageResource(R.drawable.amp1);
	}
}
