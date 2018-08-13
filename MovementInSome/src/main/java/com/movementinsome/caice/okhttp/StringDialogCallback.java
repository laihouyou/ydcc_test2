package com.movementinsome.caice.okhttp;

import android.app.Activity;
import android.app.ProgressDialog;

import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Request;

/**
 * Created by zzc on 2018/1/2.
 */

public abstract class StringDialogCallback extends StringCallback {
    final static ProgressDialog[] progress = {null};
    private Activity mContext;

    public StringDialogCallback(Activity activity){
        this.mContext=activity;
        initDialog();
    }

    private void initDialog(){
        if (progress[0]==null){
            progress[0] = new ProgressDialog(mContext);
            progress[0].setCancelable(false);
            progress[0].setCanceledOnTouchOutside(false);
        }
    }

    @Override
    public void onBefore(Request request, int id) {
        super.onBefore(request, id);
        progress[0].setMessage("正在加载……");
        progress[0].show();
    }

    @Override
    public void onAfter(int id) {
        super.onAfter(id);
        if (progress[0].isShowing()){
            progress[0].dismiss();
        }
    }

}
