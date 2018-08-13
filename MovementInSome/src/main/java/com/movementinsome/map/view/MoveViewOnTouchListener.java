package com.movementinsome.map.view;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;

public class MoveViewOnTouchListener {

	private Handler myHandler;
	private int startY;
	private boolean isFristMove;
	private int viewY;
	private boolean isOnTouch;
	private boolean isTopMoveUp;
	
	// 初始显示高度
	private int showH;
	// 底部显示高度
	private int bottomH;
	
	// 显示状态
	private int displayStatus;
	public static final int DISPLAY_TOP = 1;
	public static final int DISPLAY_MIDDLE = 2;
	public static final int DISPLAY_BOTTOM = 3;
	
	private boolean isMove;

	public MoveViewOnTouchListener(Handler myHandler) {
		this.myHandler = myHandler;
	}

	public boolean onTouchEvent(MotionEvent event) {//触摸事件
		final int actionMasked =event.getActionMasked() & MotionEvent.ACTION_MASK;
		//event.getAction()
		switch (actionMasked) {
		case MotionEvent.ACTION_DOWN://点下显示页面的时候
			if(displayStatus==DISPLAY_MIDDLE&&event.getY()<showH
			||displayStatus==DISPLAY_BOTTOM&&event.getY()<bottomH){
				return false;
			}
			myHandler.sendEmptyMessage(1);
			break;
		case MotionEvent.ACTION_MOVE://移动的时候
			int tempY = (int) event.getY();
			if (!isFristMove) {
				startY = (int) event.getY();
				isFristMove = true;
			}
			float moveY = viewY + tempY - startY;
			Message msg = new Message();
			msg.what = 2;
			Bundle data = new Bundle();
			data.putFloat("moveY", moveY);
			msg.setData(data);
			myHandler.sendMessage(msg);
			return true;
		case MotionEvent.ACTION_UP://手指离开屏幕的时候
			isFristMove = false;
			myHandler.sendEmptyMessage(3);
			break;

		default:
			break;
		}
		return true;
	}
	public boolean onTouchEvent(MotionEvent event,int firstVisibleItem) {
		final int actionMasked =event.getActionMasked() & MotionEvent.ACTION_MASK;
		//event.getAction()
		switch (actionMasked) {
		case MotionEvent.ACTION_DOWN://点下显示页面的时候
			if((displayStatus==DISPLAY_MIDDLE&&event.getY()<showH)
					||(displayStatus==DISPLAY_BOTTOM&&event.getY()<bottomH)){
				isOnTouch = false;
				return false;
			}else{
				isOnTouch = true;
			}
			myHandler.sendEmptyMessage(1);
			break;
		case MotionEvent.ACTION_MOVE://移动的时候
			isMove = true;
			int tempY = (int) event.getY();
			if (!isFristMove) {
				startY = (int) event.getY();
				isFristMove = true;
			}
			if(displayStatus==DISPLAY_TOP&&firstVisibleItem==0){
				if(tempY - startY>0){
					float moveY = viewY + tempY - startY;
					Message msg = new Message();
					msg.what = 2;
					Bundle data = new Bundle();
					data.putFloat("moveY", moveY);
					msg.setData(data);
					myHandler.sendMessage(msg);
					isTopMoveUp = false;
					setDisplayStatus(MoveViewOnTouchListener.DISPLAY_MIDDLE);
					return false;
				}else{
					isTopMoveUp = true;
					return false;
				}
			}else{
				float moveY = viewY + tempY - startY;
				Message msg = new Message();
				msg.what = 2;
				Bundle data = new Bundle();
				data.putFloat("moveY", moveY);
				msg.setData(data);
				myHandler.sendMessage(msg);
				return false;
			}
			
		case MotionEvent.ACTION_UP://手指离开屏幕的时候
			isFristMove = false;
			if(isMove()){
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						try {
							Thread.sleep(100);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						setMove(false);
					}
				}).start();
			}
			if(isTopMoveUp){
				return false;
			}
			myHandler.sendEmptyMessage(3);
			break;

		default:
			break;
		}
		return true;
	}

	public void setViewY(int viewY) {
		this.viewY = viewY;
	}

	public int getDisplayStatus() {
		return displayStatus;
	}

	public void setDisplayStatus(int displayStatus) {
		this.displayStatus = displayStatus;
	}

	public void setShowH(int showH) {
		this.showH = showH;
	}

	public int getBottomH() {
		return bottomH;
	}

	public void setBottomH(int bottomH) {
		this.bottomH = bottomH;
	}

	public boolean isOnTouch() {
		return isOnTouch;
	}

	public void setOnTouch(boolean isOnTouch) {
		this.isOnTouch = isOnTouch;
	}

	public boolean isMove() {
		return isMove;
	}

	public void setMove(boolean isMove) {
		this.isMove = isMove;
	} 
}
