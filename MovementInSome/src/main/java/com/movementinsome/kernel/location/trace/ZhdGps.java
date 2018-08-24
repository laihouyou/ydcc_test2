package com.movementinsome.kernel.location.trace;

import android.content.Context;

import com.amap.api.maps.CoordinateConverter;
import com.amap.api.maps.model.LatLng;
import com.movementinsome.AppContext;
import com.movementinsome.app.zhd.zhdutil.Util;
import com.movementinsome.caice.util.BaiduCoordinateTransformation;
import com.movementinsome.kernel.location.LocationInfoExt;
import com.movementinsome.kernel.location.coordinate.ConvertLatlng;
import com.zhd.communication.DeviceManager;
import com.zhd.communication.object.EnumGpsQuality;
import com.zhd.communication.object.GpsPoint;

import java.text.DecimalFormat;

/**
 * Created by zzc on 2017/5/27.
 */

public class ZhdGps {
    private LocationInfoExt locationInfo = null;
    private GpsPoint gpsPoint;
    private Context context;

    public ZhdGps(Context context){
        gpsPoint = DeviceManager.getInstance().getPosition();
        locationInfo=new LocationInfoExt();
        this.context=context;
    }

    public LocationInfoExt getLocation(){
        gpsPoint = DeviceManager.getInstance().getPosition();
        if (gpsPoint==null){
//            Toast.makeText(context,"gpsPoint===null",Toast.LENGTH_SHORT).show();
            return null;
        }else {
//            Toast.makeText(context,"经度："+Util.getDegreeString(gpsPoint.L)
//                    +"    "+"纬度："+Util.getDegreeString(gpsPoint.B),Toast.LENGTH_SHORT).show();

            double wg84log= ConvertLatlng.convertToDecimalByString(Util.getDegreeString(gpsPoint.L));
            double wg84lat= ConvertLatlng.convertToDecimalByString(Util.getDegreeString(gpsPoint.B));

            locationInfo.setLongitude(wg84log);  //经度
            locationInfo.setLatitude(wg84lat);   //纬度

            LatLng latLng_wgs84=new LatLng(wg84lat,wg84log);
            LatLng latLng_bd09= BaiduCoordinateTransformation.toGcj02(latLng_wgs84.longitude,latLng_wgs84.latitude, CoordinateConverter.CoordType.GPS);
            locationInfo.setLongitude_gcj02(latLng_bd09.longitude);
            locationInfo.setLatitude_gcj02(latLng_bd09.latitude);

            locationInfo.setAltitude(Double.parseDouble(new DecimalFormat("0.00").format(gpsPoint.H)));   //海拔高程
            locationInfo.setSatellites(gpsPoint.UsedSats);      //使用的卫星数（来自GPGGA）
            locationInfo.setConnected(DeviceManager.getInstance().isConnected());   //当前是否连接中海达
            locationInfo.setQualityStr(Util.getGpsQualityString(AppContext.getInstance(), gpsPoint.Quality));  //GPS质量（解类型）（来自GPGGA）
            locationInfo.setAziMagneticNorth(gpsPoint.AziMagneticNorth);  //磁北方位角（单位：度）（来自GPVTG）
            locationInfo.setAziTrueNorth(gpsPoint.AziTrueNorth);  //真北方位角（单位：度）（来自GPVTG）
            locationInfo.setDiffAge(gpsPoint.DiffAge);      //差分龄期（单位：秒）（来自GPGGA）
            locationInfo.setDiffStationID(gpsPoint.DiffStationID);      //基站ID（四位）（来自GPGGA）
            locationInfo.setE(gpsPoint.E);      //测量坐标系下的东向坐标值
            locationInfo.setN(gpsPoint.N);      //测量坐标系下的被向坐标值
            locationInfo.setZ(gpsPoint.Z);      //测量坐标系下的高程
            locationInfo.setH(gpsPoint.H);      //WGS84坐标系下的高程（单位：米）（来自GPGGA）
            locationInfo.setHDop(gpsPoint.HDop);    //水平精度因子（来自GPGGA，GPGSA）
            locationInfo.setSolutionUsedSats(gpsPoint.SolutionUsedSats);    //解算使用的卫星数（来自BESTPOS）
            locationInfo.setUndulation(gpsPoint.Undulation);    //椭球距离（来自BESTPOS）
            locationInfo.setVDop(gpsPoint.VDop);    //垂直精度因子（来自GPGSA）
            locationInfo.setSpeed((float) gpsPoint.Velocity);   //速度（米/秒）（来自GPVTG）
            locationInfo.setXRms(Double.parseDouble(new DecimalFormat("0.0000").format(gpsPoint.XRms)));    //水平x中误差（单位：米）（来自GPGST）
            locationInfo.setYRms(Double.parseDouble(new DecimalFormat("0.0000").format(gpsPoint.YRms)));    //水平y中误差（单位：米）（来自GPGST）
            locationInfo.setHRms(Double.parseDouble(new DecimalFormat("0.0000").format(gpsPoint.HRms)));    //垂直中误差（单位：米）（来自GPGST）

            // 单点定位时，无基站ID和差分龄期
            if (gpsPoint.Quality <= EnumGpsQuality.SINGLE) {
                locationInfo.setDiffStationID("");
                locationInfo.setDiffAge(0);
            }

            locationInfo.setLocationModel("中海达");   //定位类型

            return locationInfo;
        }
    }

}
