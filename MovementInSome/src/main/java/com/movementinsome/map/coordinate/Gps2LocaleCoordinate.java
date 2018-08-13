package com.movementinsome.map.coordinate;

import com.esri.core.geometry.Point;
import com.esri.core.geometry.SpatialReference;
import com.movementinsome.kernel.initial.model.CoordParam;
import com.movementinsome.kernel.location.coordinate.Gps2Locale;

public class Gps2LocaleCoordinate extends Gps2Locale implements ITranslateCoordinate  {

//	地方的坐标系
	@SuppressWarnings("unused")
	//private double _dLocalX, _dLocalY, _dLocalZ;
	
	public Point GPS2MapPoint(double lat, double lon, double alt, CoordParam param,SpatialReference spRefence){

			this.CalculateLocalCoordinate(lat, lon, alt, param);
			return new Point(this._dLocalX, this._dLocalY);
	}	
 
}
