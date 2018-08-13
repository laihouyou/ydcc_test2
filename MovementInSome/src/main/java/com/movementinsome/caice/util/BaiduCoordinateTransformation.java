package com.movementinsome.caice.util;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.CoordinateConverter;

/**
 * Created by zzc on 2017/10/23.
 */

public class BaiduCoordinateTransformation {
    /**
     * 百度官方gcj02转Bd09
     * @param latLng_gcj02
     * @return
     */
    public static LatLng gcj02ToBd09(LatLng latLng_gcj02){
        // 将google地图、soso地图、aliyun地图、mapabc地图和amap地图// 所用坐标转换成百度坐标
        CoordinateConverter converter  = new CoordinateConverter();
        converter.from(CoordinateConverter.CoordType.COMMON);
        // sourceLatLng待转换坐标
        converter.coord(latLng_gcj02);
        LatLng desLatLng_bd09 = converter.convert();
        return desLatLng_bd09;
    }
    /**
     * 百度官方wgs84转Bd09
     * @param latLng_wgs84
     * @return
     */
    public static LatLng wgs84ToBd09(LatLng latLng_wgs84){
        // 将google地图、soso地图、aliyun地图、mapabc地图和amap地图// 所用坐标转换成百度坐标
        CoordinateConverter converter  = new CoordinateConverter();
        converter.from(CoordinateConverter.CoordType.GPS);
        // sourceLatLng待转换坐标
        converter.coord(latLng_wgs84);
        LatLng desLatLng_bd09 = converter.convert();
        return desLatLng_bd09;
    }
}
