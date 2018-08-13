package com.movementinsome.caice.util;

import android.location.Location;

public class GPSUtils {
    //gps转照片的度分秒
    public static String gpsInfoConvert(double gpsInfo) {
        gpsInfo = Math.abs(gpsInfo);
        String dms = Location.convert(gpsInfo, Location.FORMAT_SECONDS);
        String[] splits = dms.split(":");
        String[] secnds = (splits[2]).split("\\.");
        String seconds;
        if (secnds.length == 0) {
            seconds = splits[2];
        } else {
            seconds = secnds[0];
        }
        return splits[0] + "/1," + splits[1] + "/1," + seconds + "/1";
    }

    //照片的度分秒转gps
    public static Double convertToDegree(String stringDMS){
        Float result = null;
        String[] DMS = stringDMS.split(",", 3);

        String[] stringD = DMS[0].split("/", 2);
        Double D0 = new Double(stringD[0]);
        Double D1 = new Double(stringD[1]);
        Double FloatD = D0/D1;

        String[] stringM = DMS[1].split("/", 2);
        Double M0 = new Double(stringM[0]);
        Double M1 = new Double(stringM[1]);
        Double FloatM = M0/M1;

        String[] stringS = DMS[2].split("/", 2);
        Double S0 = new Double(stringS[0]);
        Double S1 = new Double(stringS[1]);
        Double FloatS = S0/S1;

        result = new Float(FloatD + (FloatM/60) + (FloatS/3600));

        return (double) result;
    }

    public static String getDegrees(Double gpsInfo){
        String[] array=gpsInfo.toString().split("[.]");
        String degrees=array[0];//得到度

//        Double m=Double.parseDouble("0."+array[1])*60;
//        String[] array1=m.toString().split("[.]");
//        String minutes=array1[0];//得到分
//
//        Double s=Double.parseDouble("0."+array1[1])*60;
//        String[] array2=s.toString().split("[.]");
//        String seconds=array2[0];//得到秒

        return degrees;
    }

    public static String getMinutes(Double gpsInfo){
        String[] array=gpsInfo.toString().split("[.]");
//        String degrees=array[0];//得到度

        Double m=Double.parseDouble("0."+array[1])*60;
        String[] array1=m.toString().split("[.]");
        String minutes=array1[0];//得到分

        Double s=Double.parseDouble("0."+array1[1])*60;
        String[] array2=s.toString().split("[.]");
//        String seconds=array2[0];//得到秒

        return minutes;
    }
    public static String getSeconds(Double gpsInfo){
        String[] array=gpsInfo.toString().split("[.]");
//        String degrees=array[0];//得到度

        Double m=Double.parseDouble("0."+array[1])*60;
        String[] array1=m.toString().split("[.]");
//        String minutes=array1[0];//得到分

        Double s=Double.parseDouble("0."+array1[1])*60;
        String[] array2=s.toString().split("[.]");
        String seconds=array2[0];//得到秒

        return seconds;
    }
}
