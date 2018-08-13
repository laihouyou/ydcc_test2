package com.movementinsome.map.view;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.AbsListView.OnScrollListener;

public class IdentifyFacListView  extends ListView implements OnScrollListener {
	private int mPosition;
    private int firstVisibleItem;
    private Handler myHandler;
    private MoveViewOnTouchListener moveViewOnTouchListener;
    private Context context;
    // 显示状态
 	private int displayStatus;
 
    public IdentifyFacListView(Context context) {
        super(context);
        this.context = context;
    }
 
    public IdentifyFacListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }
 
    public IdentifyFacListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
    }
    
    private int startY;
	private boolean isFristMove;
	private boolean isMoveDown;
	private boolean isMoveUp;
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		moveViewOnTouchListener.onTouchEvent(ev,firstVisibleItem);
		if(!moveViewOnTouchListener.isOnTouch()){
			return false;
		}
		if (moveViewOnTouchListener.getDisplayStatus() == MoveViewOnTouchListener.DISPLAY_MIDDLE
				|| moveViewOnTouchListener.getDisplayStatus() == MoveViewOnTouchListener.DISPLAY_BOTTOM) {
			final int actionMasked = ev.getActionMasked()
					& MotionEvent.ACTION_MASK;

			if (actionMasked == MotionEvent.ACTION_DOWN) {
				// 记录手指按下时的位置
				return super.dispatchTouchEvent(ev);
			}
			if (actionMasked == MotionEvent.ACTION_MOVE) {
				return true;
			}
			return super.dispatchTouchEvent(ev);

		}else {
			return super.dispatchTouchEvent(ev);
		}
	}
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		return super.onTouchEvent(ev);
		
	}
	@Override
	public void onScroll(AbsListView arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		this.firstVisibleItem = arg1;
	}

	@Override
	public void onScrollStateChanged(AbsListView arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}
	public void setMyHandler(Handler myHandler) {
		this.myHandler = myHandler;
	}

	public void setMoveViewOnTouchListener(
			MoveViewOnTouchListener moveViewOnTouchListener) {
		this.moveViewOnTouchListener = moveViewOnTouchListener;
	}

	public boolean isFristMove() {
		return isFristMove;
	}

	public void setFristMove(boolean isFristMove) {
		this.isFristMove = isFristMove;
	}

	public boolean isMoveDown() {
		return isMoveDown;
	}

	public void setMoveDown(boolean isMoveDown) {
		this.isMoveDown = isMoveDown;
	}

	public int getDisplayStatus() {
		return displayStatus;
	}

	public void setDisplayStatus(int displayStatus) {
		this.displayStatus = displayStatus;
	}
	
}
