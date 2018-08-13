package com.movementinsome.app.pub.view;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.movementinsome.kernel.util.ActivityUtil;

/**
 * Created by zzc on 2017/6/8.
 */

public class MaxHeightListView extends ListView {
    public MaxHeightListView(Context context) {
        super(context);
    }

    public MaxHeightListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MaxHeightListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public MaxHeightListView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        setViewHeightBasedOnChildren();
    }

    public void setViewHeightBasedOnChildren() {
        ListAdapter listAdapter = this.getAdapter();
        if (listAdapter == null) {
            return;
        }
        // int h = 10;
        // int itemHeight = BdUtilHelper.getDimens(getContext(), R.dimen.ds30);
        int sumHeight = 0;
        int size = listAdapter.getCount();


        for (int i = 0; i < size; i++) {
            View v = listAdapter.getView(i, null, this);
            v.measure(0, 0);
            Log.i("melon", "item.heiht = " + v.getMeasuredHeight());
            sumHeight += v.getMeasuredHeight();


        }


        if (sumHeight > ActivityUtil.getWindowsHetght((Activity) getContext())/3) {
            sumHeight = ActivityUtil.getWindowsHetght((Activity) getContext())/3;
        }
        android.view.ViewGroup.LayoutParams params = this.getLayoutParams();
        // this.getLayoutParams();
        params.height = sumHeight;


        this.setLayoutParams(params);
    }
}
