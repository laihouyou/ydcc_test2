package com.movementinsome.map.coordinate;

import com.esri.core.geometry.Point;
import com.esri.core.geometry.SpatialReference;
import com.movementinsome.kernel.initial.model.CoordParam;


public interface ITranslateCoordinate {
	Point GPS2MapPoint(double lat, double lon, double alt,
			CoordParam param,SpatialReference spRefence);
}
