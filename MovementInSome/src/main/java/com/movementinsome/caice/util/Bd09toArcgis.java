package com.movementinsome.caice.util;

import com.amap.api.maps.model.LatLng;
import com.esri.core.geometry.Point;
import com.movementinsome.AppContext;
import com.movementinsome.kernel.initial.model.CoordParam;
import com.movementinsome.kernel.location.coordinate.Gcj022Bd09;
import com.movementinsome.kernel.location.coordinate.Gcj022Gps;
import com.movementinsome.map.coordinate.Gps2LocaleCoordinate;
import com.movementinsome.map.coordinate.Gps2Mercator;
import com.movementinsome.map.coordinate.ITranslateCoordinate;

import java.util.Map;

/**
 * Created by zzc on 2017/9/14.
 */

public class Bd09toArcgis {
    /**
     * bd09转地方坐标
     * @param latLng_bd09
     * @return
     */
    public static Point bd09ToArcgis(LatLng latLng_bd09, CoordParam coordParam){
        Map<String,Double> mapLatlng_wg84= bd09ToWg84(latLng_bd09);
        if (coordParam==null){
            coordParam= AppContext.getInstance().getCoordTransform();
        }
        Point point= wgs84ToArcgis(coordParam,mapLatlng_wg84.get("lon"),mapLatlng_wg84.get("lat"),0);
        return point;
    }

    /**
     * bd09转wg84
     * @param latLng_bd09
     * @return
     */
    public static Map<String,Double>  bd09ToWg84(LatLng latLng_bd09){
        double[] latlng_gcj02= Gcj022Bd09.bd09Decrypt(latLng_bd09.latitude,latLng_bd09.longitude);
        Map<String,Double> mapLatlng_wg84= Gcj022Gps.gcj2wgs(latlng_gcj02[0],latlng_gcj02[1]);
        return mapLatlng_wg84;
    }

    /**
     * gcj02 转地方坐标
     * @param longitude
     * @param latitude
     * @return
     */
    public static Point gcj02ToArcgis(double longitude,double latitude,CoordParam coordParam){
        Map<String, Double> map_wgs84=Gcj022Gps.gcj2wgs(longitude,latitude);
        return wgs84ToArcgis(coordParam,map_wgs84.get("lon"),map_wgs84.get("lat"),0);
    }

    /**
     *  wgs84 转 地方坐标
     * @param param
     * @param lat   纬度
     * @param lon   经度
     * @param alt   高度
     * @return
     */
    public static Point wgs84ToArcgis(CoordParam param, double lon, double lat, double alt){
        // 参数转换实例
        ITranslateCoordinate translate = null;

        if (param.getCoordinate().equals("") || param.getCoordinate().equals("0")) {
            translate = new Gps2Mercator();
        } else {
            translate = new Gps2LocaleCoordinate();
        }
        return translate.GPS2MapPoint(lat, lon, alt, param, null);
    }
}
