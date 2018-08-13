package com.movementinsome.map.coordinate;

import com.esri.core.geometry.Point;
import com.esri.core.geometry.SpatialReference;
import com.movementinsome.kernel.initial.model.CoordParam;
import com.movementinsome.kernel.location.LocationInfo;

public class TranslateCoordinateFactory {

	/**
	 *  gcj02 转arcgis坐标
	 * @param param
	 * @param spRefence
	 * @param lat
	 * @param lon
	 * @param alt
	 * @return
	 */

	public static Point gpsTranslate(CoordParam param,
			SpatialReference spRefence, double lat, double lon, double alt) {
		// 参数转换实例
		ITranslateCoordinate translate = null;
		
		//if (translate == null) {
		if (spRefence == null){
			if (param.getCoordinate().equals("") || param.getCoordinate().equals("0")) {
				translate = new Gps2Mercator();
			}else{
				translate = new Gps2LocaleCoordinate();
			}
		} else {
			if (spRefence.isAnyWebMercator()) {
				translate = new Gps2MercatorChina();
			} else if (spRefence.isWGS84()) {
				return new Point(lat, lon);
			} else {
				translate = new Gps2LocaleCoordinate();
			}
		}
		//}
		//lat = 27.774065500;
		//lon = 120.505378867;
		return translate.GPS2MapPoint(lat, lon, alt, param, spRefence);
	}

	public static LocationInfo gpsTranslate(CoordParam param,
			String srid, LocationInfo location) {
		// 参数转换实例
		ITranslateCoordinate translate = null;
				
		SpatialReference spRefence = null;
		if (!"".equals(srid))
			spRefence = SpatialReference.create(Integer.valueOf(srid));
		Point mapPoint = null;
		//if (translate == null) {
		if ((spRefence == null)
				&& (param.getCoordinate().equals("") || param.getCoordinate().equals("0"))||"3587".equals(srid )) {
			translate = new Gps2Mercator();
		} else {
			if (spRefence.isAnyWebMercator()||"102100".equals(srid )) {
				translate = new Gps2MercatorChina();
			} else if (spRefence.isWGS84()) {
				mapPoint = new Point(location.getLatitude(),
						location.getLongitude());
			} else {
				translate = new Gps2LocaleCoordinate();
			}
		}
		//}
		mapPoint = translate.GPS2MapPoint(location.getLatitude(),
				location.getLongitude(), location.getAltitude(), param,
				spRefence);
		location.setMapx(mapPoint.getX());
		location.setMapy(mapPoint.getY());
		return location;
	}
}
