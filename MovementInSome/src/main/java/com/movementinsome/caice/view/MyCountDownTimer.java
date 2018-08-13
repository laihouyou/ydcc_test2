package com.movementinsome.caice.view;

import android.os.CountDownTimer;
import android.widget.TextView;

/**
 * 倒计时
 * Created by zzc on 2017/10/31.
 */

public class MyCountDownTimer extends CountDownTimer {
    private TextView view;

    public MyCountDownTimer(long millisInFuture, long countDownInterval,TextView view) {
        super(millisInFuture, countDownInterval);
        this.view=view;
    }

    //计时过程
    @Override
    public void onTick(long l) {
        //防止计时过程中重复点击
        view.setClickable(false);
        view.setText(l / 1000 + "秒");

    }

    //计时完毕的方法
    @Override
    public void onFinish() {
        //重新给Button设置文字
        view.setText("重新获取");
        //设置可点击
        view.setClickable(true);
    }

    public void onStart(){
        view.setText("获取验证码");
        //设置可点击
        view.setClickable(true);
    }
}
