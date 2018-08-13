package com.movementinsome.caice.util;

import android.view.View;

import com.movementinsome.R;

/**
 * Created by zzc on 2018/2/5.
 */

public class SetValueUtil {
    /**
     * 设置工程类型图片
     * @param view
     * @param projectType
     */
    public static void setProjectBackgroup(View view,String projectType){
        if (view!=null){
            if (projectType!=null){
                switch (projectType){
                    case "电力":
                        view.setBackgroundResource(R.drawable.power);
                        break;

                    case "电信":
                        view.setBackgroundResource(R.drawable.telegraphy);
                        break;

                    case "给水":
                        view.setBackgroundResource(R.drawable.water_delivery);
                        break;

                    case "排水":
                        view.setBackgroundResource(R.drawable.drain_away_water);
                        break;

                    case "燃气":
                        view.setBackgroundResource(R.drawable.combustion_gas);
                        break;

                    case "综合":
                        view.setBackgroundResource(R.drawable.synthesis);
                        break;

                    default:
                        view.setBackgroundResource(R.drawable.water_delivery);
                        break;
                }
                view.setBackgroundResource(R.drawable.water_delivery);
            }
        }
    }

}
