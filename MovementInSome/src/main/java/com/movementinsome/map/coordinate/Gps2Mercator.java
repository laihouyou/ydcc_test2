package com.movementinsome.map.coordinate;

import com.esri.core.geometry.Point;
import com.esri.core.geometry.SpatialReference;
import com.movementinsome.kernel.initial.model.CoordParam;
import com.movementinsome.kernel.location.coordinate.Gps2Mct;


public class Gps2Mercator extends Gps2Mct implements ITranslateCoordinate {
	static double M_PI = Math.PI;

	@Override
	// 经度(lon)，纬度(lat)，海拨(alt)
	public Point GPS2MapPoint(double lat, double lon, double alt,
			CoordParam param,SpatialReference spRefence) {
		// TODO Auto-generated method stub
		double[] xy = this.lonLat2Mercator(lat, lon, alt);
		
		Point ptMap_ = new Point(xy[0],xy[1]);//(Point) GeometryEngine.project(xy[0],xy[1],spRefence);
		return ptMap_;
	}


}
