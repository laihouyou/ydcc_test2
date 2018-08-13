package com.movementinsome.caice.util;

import com.esri.core.geometry.SpatialReference;
import com.movementinsome.kernel.initial.model.CoordParam;
import com.movementinsome.kernel.location.coordinate.Gcj022Bd09;
import com.movementinsome.kernel.location.coordinate.Gcj022Gps;
import com.movementinsome.kernel.location.coordinate.Gps2Locale;
import com.movementinsome.kernel.location.coordinate.Gps2Mct;

import java.util.Map;

/**
 * Created by zzc on 2017/3/27.
 */

public class ArcgisToBd09 {
    public static String webM2Bd09Position(double x, double y){
        double wgslonlat[] = Gps2Mct.mercator2lonLat(x, y);
        double bdlonlat[] = Gcj022Bd09.bd09Encrypt(wgslonlat[1], wgslonlat[0]);
        return bdlonlat[0]+" "+bdlonlat[1];
    }

    public static String local2Bd09Position(CoordParam param, double x, double y){
        double wgslonlat[] = Gps2Locale.RevTranslate(x, y, 0, param);
        Map<String, Double> lonlat = Gcj022Gps.wgs2gcj(wgslonlat[0], wgslonlat[1]);
        double bdlonlat[] = Gcj022Bd09.bd09Encrypt(lonlat.get("lat"), lonlat.get("lon"));
        return	 bdlonlat[0]+" "+bdlonlat[1];
    }

    public static String wgs2Bd09Position(double longitude, double latitude){
        Map<String, Double> lonlat = Gcj022Gps.wgs2gcj(longitude, latitude);
        double bdlonlat[] = Gcj022Bd09.bd09Encrypt(lonlat.get("lat"), lonlat.get("lon"));
        return	 bdlonlat[0]+" "+bdlonlat[1];
    }

    public static String toBd09Position(CoordParam param, SpatialReference spRefence, double x, double y){
        if ((spRefence == null)
            &&(param == null ||param.getCoordinate().equals("") || param.getCoordinate().equals("0"))) {
            return webM2Bd09Position(x,y);
        } else {
            if (spRefence!=null&&spRefence.isAnyWebMercator()) {
                return webM2Bd09Position(x,y);
            } else if (spRefence!=null&&spRefence.isWGS84()) {
                return wgs2Bd09Position(x,y);
            } else {
                return local2Bd09Position(param,y,x);
            }
        }
    }
}
