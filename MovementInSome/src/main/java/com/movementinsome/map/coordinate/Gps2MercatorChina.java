package com.movementinsome.map.coordinate;

import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.SpatialReference;
import com.movementinsome.kernel.initial.model.CoordParam;
import com.movementinsome.kernel.location.coordinate.Gps2MctC;

/**
 * 
 * 经纬度纠偏工具类
 * 
 * [一句话功能简述]
 * <p>
 * 
 * [功能详细描述]
 * <p>
 * 
 * @author PeiYu
 * 
 * @version 1.0, 2012-8-24
 * 
 * @see
 * 
 * @since gframe-v100
 */

public class Gps2MercatorChina extends Gps2MctC  implements ITranslateCoordinate

{

	private double casm_f = 0.0;

	private double casm_rr = 0.0;

	private double casm_t1 = 0.0;

	private double casm_t2 = 0.0;

	private double casm_x1 = 0.0;

	private double casm_x2 = 0.0;

	private double casm_y1 = 0.0;

	private double casm_y2 = 0.0;

	/**
	 * 纠偏
	 * 
	 * @param x
	 *            经度
	 * @param y
	 *            纬度
	 * @return [0]纠偏后经度 [1]纠偏后纬度
	 */
	public Point GPS2MapPoint(double lat, double lon, double alt,
			CoordParam param, SpatialReference spRefence) {
		double[] rxy = this.lonLat2Mercator(lat, lon, alt);
		Point ptMap_ = (Point) GeometryEngine
				.project(rxy[0], rxy[1], spRefence);

		return ptMap_;
	}
}
