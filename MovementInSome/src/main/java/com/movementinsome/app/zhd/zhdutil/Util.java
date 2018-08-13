package com.movementinsome.app.zhd.zhdutil;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.Toast;

import com.movementinsome.R;
import com.zhd.communication.object.EnumGpsQuality;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by zhdi7gg on 2016/8/26.
 */
public class Util {

    /**
     * 获取解类型字符串
     * @param quality
     * @return
     */
    public static String getGpsQualityString(Context context, int quality) {
        switch (quality) {
            case EnumGpsQuality.INVALID:
                return context.getString(R.string.gnss_position_type_invalid);
            case EnumGpsQuality.SINGLE:
                return context.getString(R.string.gnss_position_type_single);
            case EnumGpsQuality.RTD:
                return context.getString(R.string.gnss_position_type_rtd);
            case EnumGpsQuality.RTK_FIXED:
                return context.getString(R.string.gnss_position_type_rtkfixed);
            case EnumGpsQuality.RTK_FLOAT:
                return context.getString(R.string.gnss_position_type_rtkfloat);
            case EnumGpsQuality.DRM:
                return context.getString(R.string.gnss_position_type_drm);
            case EnumGpsQuality.FIXED_POS:
                return context.getString(R.string.gnss_position_type_fixedpos);
            case EnumGpsQuality.SIMULATOR:
                return context.getString(R.string.gnss_position_type_simulator);
            case EnumGpsQuality.SBAS:
                return context.getString(R.string.gnss_position_type_sbas);
            case EnumGpsQuality.PPP_FIXED:
                return context.getString(R.string.gnss_position_type_pppfixed);
            case EnumGpsQuality.PPP_FLOAT:
                return context.getString(R.string.gnss_position_type_pppfloat);
            case EnumGpsQuality.INDOOR:
                return context.getString(R.string.gnss_position_type_indoor);
            default:
                return context.getString(R.string.gnss_position_type_unknown);
        }
    }

    /**
     * 时间转换
     * @param date GMT时间
     * @return yyyy-MM-dd
     */
    public static String getDateString(Date date) {
        try {
            SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
            return sf.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 时间转换
     * @param date GMT时间
     * @return yyyy-MM-dd HH:mm:ss
     */
    public static String getTimeString(Date date) {
        try {
            SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return sf.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 显示信息
     * @param context 上下�?
     * @param msg 要显示的消息
     */
    public static void showToast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    /**
     * 显示信息
     * @param context  上下�?
     * @param msgResId 要显示的消息的id
     */
    public static void showToast(Context context, int msgResId) {
        Toast.makeText(context, msgResId, Toast.LENGTH_SHORT).show();
    }

    /**
     * 十进制度转度分秒(xxx° ==> xxx°xxx′xxx�?)
     */
    public static String getDegreeString(double radian) {
        int degree = 0;
        double minute = 0;
        double second = 0;

        try {
            double d = Math.toDegrees(radian);
            degree = (int)d;
            minute = (d - degree) * 60;
            second = (minute - (int)minute) * 60;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return String.format("%1$s ° %2$s ′ %3$.4f ″", degree, (int)minute, second);
    }

    /**
     * 字符串转换为整型
     * @param value
     * @return
     */
    public static int parseInt(String value) {
        try {
            return Integer.parseInt(value);
        } catch (Exception ex) {
            return 0;
        }
    }

    /**
     * 显示等待�?
     * @param context 上下�?
     * @param message 消息
     * @return
     */
    public static ProgressDialog showProgressDialog(Context context, String message) {
        ProgressDialog progressDialog =
                ProgressDialog.show(context, context.getString(R.string.app_toast), message, false, false);
        progressDialog.setCanceledOnTouchOutside(false);
        return progressDialog;
    }

    /**
     * 显示等待�?
     * @param context 上下�?
     * @param message 消息
     * @param listener 取消事件的监�?
     * @return
     */
    public static ProgressDialog showProgressDialog(Context context, String message,
                                                    final DialogInterface.OnCancelListener listener) {
        ProgressDialog progressDialog =
                ProgressDialog.show(context, context.getString(R.string.app_toast), message, false, true, listener);
        progressDialog.setCanceledOnTouchOutside(false);
        return progressDialog;
    }

    /**
     * 显示进度�?
     * @param context 上下�?
     * @param message 消息
     * @param listener 取消事件的监�?
     * @param maxValue 进度�?大�??
     * @return
     */
    public static ProgressDialog showHorizontalProgressDialog(Context context, String message,
                                                              final DialogInterface.OnCancelListener listener,
                                                              int maxValue) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setTitle(context.getString(R.string.app_toast));
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setButton(DialogInterface.BUTTON_NEGATIVE,
                context.getString(R.string.app_cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        progressDialog.setCancelable(true);
        progressDialog.setCanceledOnTouchOutside(false);    //禁止点击其他区域取消进度�?
        progressDialog.setOnCancelListener(listener);
        progressDialog.setMax(maxValue);
        progressDialog.setProgress(0);
        progressDialog.setMessage(message);
        progressDialog.show();
        return progressDialog;
    }
}
