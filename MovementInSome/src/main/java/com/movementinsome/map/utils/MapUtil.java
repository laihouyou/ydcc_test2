package com.movementinsome.map.utils;

import java.math.BigDecimal;

import android.graphics.Bitmap;
import android.view.View;

import com.esri.android.map.MapView;
import com.esri.core.geometry.Envelope;
import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.LinearUnit;
import com.esri.core.geometry.MultiPath;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.Polygon;
import com.esri.core.geometry.Polyline;
import com.esri.core.geometry.Unit;
import com.esri.core.map.Graphic;
import com.esri.core.map.ImageServiceParameters.IMAGE_FORMAT;
import com.esri.core.symbol.FillSymbol;
import com.esri.core.symbol.SimpleFillSymbol;
import com.movementinsome.map.view.MyMapView;

public class MapUtil {
	public static int SEARCH_RADIUS = 5;
	private static final double PI = 3.14159265;
	private static final double RAD = Math.PI / 180.0;
	private final static double EARTH_RADIUS = 6378137.0;

	public static void locateByPoint(MyMapView map, Double x, Double y) {
		Point mapPoint = new Point();
		mapPoint.setX(x);
		mapPoint.setY(y);

		Unit mapUnit = map.getMapUnit();
		double zoomWidth = Unit.convertUnits(SEARCH_RADIUS,
				Unit.create(LinearUnit.Code.MILE_US), mapUnit);
		Envelope zoomExtent = new Envelope(mapPoint, zoomWidth, zoomWidth);
		map.setRotationAngle(0);
		map.setExtent(zoomExtent);
	}

	
	public static boolean inBound(MyMapView map, Point mapPoint){		
		if (null== map || null == mapPoint){
			return false;
		}else{
			try{
				return GeometryEngine.contains(map.getExtent(), mapPoint, map.getSpatialReference());
			}catch(Exception ex){
				return false;
			}
		}
	}
	
	public static boolean inBound(MyMapView map, Double x, Double y){
		Point mapPoint = new Point();
		mapPoint.setX(x);
		mapPoint.setY(y);		
		
		return GeometryEngine.contains(map.getExtent(), mapPoint, map.getSpatialReference());
	}
	/**
	 * 从view 得到图片
	 * 
	 * @param view
	 * @return
	 */
	public static Bitmap getBitmapFromView(View view) {
		view.destroyDrawingCache();
		view.measure(View.MeasureSpec.makeMeasureSpec(0,
				View.MeasureSpec.UNSPECIFIED), View.MeasureSpec
				.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
		view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
		view.setDrawingCacheEnabled(true);
		Bitmap bitmap = view.getDrawingCache(true);
		return bitmap;
	}

	// 两点经纬度，计算距离
	public static double gps2m(double lat_a, double lng_a, double lat_b,
			double lng_b) {

		double radLat1 = (lat_a * Math.PI / 180.0);

		double radLat2 = (lat_b * Math.PI / 180.0);

		double a = radLat1 - radLat2;

		double b = (lng_a - lng_b) * Math.PI / 180.0;

		double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)

		+ Math.cos(radLat1) * Math.cos(radLat2)

		* Math.pow(Math.sin(b / 2), 2)));

		s = s * EARTH_RADIUS;

		s = Math.round(s * 10000) / 10000;

		return s;

	}

	// 计算方位角pab。
	public static double gps2d(double lat_a, double lng_a, double lat_b,
			double lng_b) {

		double d = 0;

		lat_a = lat_a * Math.PI / 180;

		lng_a = lng_a * Math.PI / 180;

		lat_b = lat_b * Math.PI / 180;

		lng_b = lng_b * Math.PI / 180;

		d = Math.sin(lat_a) * Math.sin(lat_b) + Math.cos(lat_a)
				* Math.cos(lat_b) * Math.cos(lng_b - lng_a);

		d = Math.sqrt(1 - d * d);

		d = Math.cos(lat_b) * Math.sin(lng_b - lng_a) / d;

		d = Math.asin(d) * 180 / Math.PI;

		// d = Math.round(d*10000);

		return d;

	}

	// @see
	// http://snipperize.todayclose.com/snippet/php/SQL-Query-to-Find-All-Retailers-Within-a-Given-Radius-of-a-Latitude-and-Longitude--65095/
	// The circumference of the earth is 24,901 miles.
	// 24,901/360 = 69.17 miles / degree
	/**
	 * 根据提供的经度和纬度、以及半径，取得此半径内的最大最小经纬度
	 * 
	 * @param raidus
	 *            单位米 return minLat,minLng,maxLat,maxLng
	 */
	public static double[] getAround(double lat, double lon, int raidus) {

		Double latitude = lat;
		Double longitude = lon;

		Double degree = (24901 * 1609) / 360.0;
		double raidusMile = raidus;

		Double dpmLat = 1 / degree;
		Double radiusLat = dpmLat * raidusMile;
		Double minLat = latitude - radiusLat;
		Double maxLat = latitude + radiusLat;

		Double mpdLng = degree * Math.cos(latitude * (PI / 180));
		Double dpmLng = 1 / mpdLng;
		Double radiusLng = dpmLng * raidusMile;
		Double minLng = longitude - radiusLng;
		Double maxLng = longitude + radiusLng;
		// System.out.println("["+minLat+","+minLng+","+maxLat+","+maxLng+"]");
		return new double[] { minLat, minLng, maxLat, maxLng };
	}

	/**
	 * 根据两点间经纬度坐标（double值），计算两点间距离，单位为米
	 * 
	 * @param lng1
	 * @param lat1
	 * @param lng2
	 * @param lat2
	 * @return
	 */
	public static double getDistance(double lng1, double lat1, double lng2,
			double lat2) {
		double radLat1 = lat1 * RAD;
		double radLat2 = lat2 * RAD;
		double a = radLat1 - radLat2;
		double b = (lng1 - lng2) * RAD;
		double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)
				+ Math.cos(radLat1) * Math.cos(radLat2)
				* Math.pow(Math.sin(b / 2), 2)));
		s = s * EARTH_RADIUS;
		s = Math.round(s * 10000) / 10000;
		return s;
	}

	public static IMAGE_FORMAT getIMAGE_FORMAT(String format) {
		if (format == null || format.equals("png24"))
			return IMAGE_FORMAT.PNG24;
		else if (format.equalsIgnoreCase("png8"))
			return IMAGE_FORMAT.PNG8;
		else if (format.equalsIgnoreCase("png"))
			return IMAGE_FORMAT.PNG;
		else if (format.equalsIgnoreCase("bmp"))
			return IMAGE_FORMAT.BMP;
		else if (format.equalsIgnoreCase("jpg"))
			return IMAGE_FORMAT.JPG;
		else if (format.equalsIgnoreCase("jpgpng"))
			return IMAGE_FORMAT.JPG_PNG;
		else if (format.equalsIgnoreCase("gif"))
			return IMAGE_FORMAT.GIF;
		else if (format.equalsIgnoreCase("tiff"))
			return IMAGE_FORMAT.TIFF;
		else
			return IMAGE_FORMAT.PNG24;

	}

	public static Graphic createCircle(Point center, double radius, int alpha,
			int fillColor) {
		Polygon polygon = new Polygon();
		getCircle(center, radius, polygon);
		FillSymbol symbol = new SimpleFillSymbol(fillColor);
		symbol.setAlpha(alpha);

		Graphic g = new Graphic(polygon, symbol);
		return g;
	}

	/**
	 * 获取圆的图形对象
	 * 
	 * @param center
	 * @param radius
	 * @return
	 */
	public static Polygon getCircle(Point center, double radius) {
		Polygon polygon = new Polygon();
		getCircle(center, radius, polygon);
		return polygon;
	}

	private static void getCircle(Point center, double radius, Polygon circle) {
		circle.setEmpty();
		Point[] points = getPoints(center, radius);
		circle.startPath(points[0]);
		for (int i = 1; i < points.length; i++)
			circle.lineTo(points[i]);
	}

	private static Point[] getPoints(Point center, double radius) {
		Point[] points = new Point[50];
		double sin;
		double cos;
		double x;
		double y;
		for (double i = 0; i < 50; i++) {
			sin = Math.sin(Math.PI * 2 * i / 50);
			cos = Math.cos(Math.PI * 2 * i / 50);
			x = center.getX() + radius * sin;
			y = center.getY() + radius * cos;
			points[(int) i] = new Point(x, y);
		}
		return points;
	}

	public static Bitmap getViewBitmap(MapView v) {
		v.clearFocus();
		v.setPressed(false);

		// 能画缓存就返回false
		boolean willNotCache = v.willNotCacheDrawing();
		v.setWillNotCacheDrawing(false);
		int color = v.getDrawingCacheBackgroundColor();
		v.setDrawingCacheBackgroundColor(0);
		if (color != 0) {
			v.destroyDrawingCache();
		}
		v.buildDrawingCache();
		Bitmap cacheBitmap = null;
		while (cacheBitmap == null) {
			cacheBitmap = v.getDrawingMapCache(0, 0, v.getWidth(),
					v.getHeight());
		}
		Bitmap bitmap = Bitmap.createBitmap(cacheBitmap);
		// Restore the view
		v.destroyDrawingCache();
		v.setWillNotCacheDrawing(willNotCache);
		v.setDrawingCacheBackgroundColor(color);
		return bitmap;
	}

	/* 计算椭球的卯酉圈曲率半径(单位:m) */
	public static double getN(double B) {
		double a = 6378.137 * 1000;// 椭圆长半径
		double b = 6356.7523141 * 1000;// 李四短半径
		BigDecimal a1 = new BigDecimal(Double.toString(a));
		BigDecimal b1 = new BigDecimal(Double.toString(b));
		BigDecimal B1 = new BigDecimal(Double.toString(B));
		BigDecimal cosB1 = new BigDecimal(Double.toString(Math.cos(B1
				.doubleValue())));
		BigDecimal one = new BigDecimal(Double.toString(1));
		// 椭圆第一偏心率
		BigDecimal e1 = new BigDecimal(Double.toString(a1.multiply(a1)
				.subtract(b1.multiply(b1))
				.divide(a1.multiply(a1), 100, BigDecimal.ROUND_HALF_UP)
				.doubleValue()));
		BigDecimal W1 = new BigDecimal(Double.toString(Math.sqrt((one
				.subtract(e1.multiply(one.subtract(cosB1.multiply(cosB1)))))
				.doubleValue())));
		BigDecimal N1 = new BigDecimal(Double.toString(a1.divide(W1, 100,
				BigDecimal.ROUND_HALF_UP).doubleValue()));
		return N1.doubleValue();
	}

	/*  空间大地坐标系转换空间直角坐标系(单位:m)  */
	public static void geodeticForCartesian(double B, double L, double H) {
		double a = 6378.137 * 1000;
		double b = 6356.7523141 * 1000;
		double iPI = 0.0174532925199433;//  (π/180.0)   
		BigDecimal a1 = new BigDecimal(Double.toString(a));
		BigDecimal b1 = new BigDecimal(Double.toString(b));
		BigDecimal B1 = new BigDecimal(Double.toString(B * iPI));
		BigDecimal L1 = new BigDecimal(Double.toString(L * iPI));
		BigDecimal H1 = new BigDecimal(Double.toString(H));

		BigDecimal one = new BigDecimal(Double.toString(1));

		BigDecimal e1 = new BigDecimal(Double.toString(a1.multiply(a1)
				.subtract(b1.multiply(b1))
				.divide(a1.multiply(a1), 100, BigDecimal.ROUND_HALF_UP)
				.doubleValue()));
		BigDecimal cosB1 = new BigDecimal(Double.toString(Math.cos(B1
				.doubleValue())));
		BigDecimal sinB1 = new BigDecimal(Double.toString(Math.sin(B1
				.doubleValue())));
		BigDecimal cosL1 = new BigDecimal(Double.toString(Math.cos(L1
				.doubleValue())));
		BigDecimal sinL1 = new BigDecimal(Double.toString(Math.sin(L1
				.doubleValue())));

		BigDecimal N = new BigDecimal(Double.toString(getN(B1.doubleValue())));
		double X = N.add(H1).multiply(cosB1).multiply(cosL1).doubleValue();
		double Y = N.add(H1).multiply(cosB1).multiply(sinL1).doubleValue();
		double Z = new BigDecimal(Double.toString(N.multiply(one.subtract(e1))
				.add(H1).doubleValue())).multiply(sinB1).doubleValue();

		cartesianForGeodetic(X, Y, Z);
	}

	/* 空间直角坐标系转换空间大地坐标系(单位:m) */
	public static void cartesianForGeodetic(double X, double Y, double Z) {
		double a = 6378.137 * 1000;
		double b = 6356.7523141 * 1000;
		double e = ((a * a) - (b * b)) / (a * a);
		double iPI = 0.0174532925199433;//  (π/180.0)    
		double B;
		double H;
		double N, tH, lB;
		double tB = Math.atan(Z / Math.sqrt(((X * X) + (Y * Y)))); //  迭代算法
		while (true) {
			N = getN(tB);
			tH = Math.sqrt((X * X) + (Y * Y)) / Math.cos(tB) - N;
			lB = Math.atan((Z + (N * e * Math.sin(tB)))
					/ Math.sqrt((X * X) + (Y * Y)));
			if (Math.abs(lB - tB) > 1E-99) {
				tB = lB;
			} else {
				B = lB;
				N = getN(B);
				H = Math.sqrt((X * X) + (Y * Y)) / Math.cos(B) - N;
				break;
			}
		}
		B = B / iPI;
		double L = ((3.14159265358989) + Math.atan(Y / X))
				* (180 / 3.14159265358989);
	}

	/**
	 * 将几何对象生成wkt字符串
	 */
	public static String geometry2WKT(Geometry geometry) {
		if (null == geometry || geometry.isEmpty() ) {
			return null;
		}
		String geoStr = "";
		Geometry.Type type = geometry.getType();
		if ("Point".equalsIgnoreCase(type.name())) {
			Point pt = (Point) geometry;
			geoStr = type.name() + "(" + pt.getX() + " " + pt.getY() + ")";
		} else if ("Polygon".equalsIgnoreCase(type.name())
				|| "Polyline".equalsIgnoreCase(type.name())
				|| "linestring".equalsIgnoreCase(type.name())) {
			MultiPath pg = (MultiPath) geometry;
			geoStr = type.name() + "(" + "";
			int pathSize = pg.getPathCount();
			for (int j = 0; j < pathSize; j++) {
				String temp = "(";
				int size = pg.getPathSize(j);
				for (int i = 0; i < size; i++) {
					Point pt = pg.getPoint(i);
					temp += pt.getX() + " " + pt.getY() + ",";
				}
				if ("Polygon".equalsIgnoreCase(type.name())){
					Point pt = pg.getPoint(0);
					temp += pt.getX() + " " + pt.getY() + ",";					
				}
				temp = temp.substring(0, temp.length() - 1) + ")";
				geoStr += temp + ",";
			}
			geoStr = geoStr.substring(0, geoStr.length() - 1) + ")";
		} else if ("Envelope".equalsIgnoreCase(type.name())) {
			Envelope env = (Envelope) geometry;
			geoStr = type.name() + "(" + env.getXMin() + "," + env.getYMin()
					+ "," + env.getXMax() + "," + env.getYMax() + ")";
		} else if ("MultiPoint".equalsIgnoreCase(type.name())) {
		} else {
			geoStr = null;
		}
		return geoStr;
	}

	/**
	 * 将wkt字符串拼成几何对象
	 */
	public static Geometry wkt2Geometry(String wkt) {
		try{
			Geometry geo = null;
			if (wkt == null || wkt.equals("")) {
				return null;
			}
			String headStr = wkt.substring(0, wkt.indexOf("(")).trim();//去掉空格
			String temp = wkt.substring(wkt.indexOf("(") + 1, wkt.lastIndexOf(")"));
			if (headStr.equalsIgnoreCase("Point")) {
				String temp2 = temp.trim();
				String[] values = temp2.split(" ");
				geo = new Point(Double.valueOf(values[0]),
						Double.valueOf(values[1]));
			}else if(headStr.equalsIgnoreCase("MULTILINESTRING")){
				geo = parseWKT2(temp, headStr);
			}else if (headStr.equalsIgnoreCase("Polyline")
					|| headStr.equalsIgnoreCase("Polygon")
					|| headStr.equalsIgnoreCase("linestring")
					) {
				String temp2 = temp.trim();
				geo = parseWKT(temp2, headStr);
			} else if (headStr.equalsIgnoreCase("Envelope")) {
				String[] extents = temp.split(",");
				geo = new Envelope(Double.valueOf(extents[0]),
						Double.valueOf(extents[1]), Double.valueOf(extents[2]),
						Double.valueOf(extents[3]));
			} else if (headStr.equalsIgnoreCase("MultiPoint")) {
			} else {
				return null;
			}
			return geo;
		}catch(Exception ex){
			return null;
		}
	}

	private static Geometry parseWKT(String multipath, String type) {
		String subMultipath = "";
		if (multipath.substring(0,1).equals("(")){
			subMultipath = multipath.substring(1, multipath.length() - 1);
		}else{
			subMultipath = multipath.substring(0, multipath.length());
		}
		String[] paths;
		if (subMultipath.indexOf("),(") >= 0) {
			paths = subMultipath.split("\\)\\,\\(");// 多个几何对象的字符串
		} else {
			paths = new String[] { subMultipath };
		}
		Point startPoint = null;
		MultiPath path = null;
		if (type.equalsIgnoreCase("Polyline")||type.equalsIgnoreCase("linestring")) {
			path = new Polyline();
		} else {
			path = new Polygon();
		}
		for (int i = 0; i < paths.length; i++) {
			String[] points = paths[i].split(",");
			startPoint = null;
			for (int j = 0; j < points.length; j++) {
				String poinstr=points[j].trim();
				String[] pointStr = poinstr.split(" ");
				if (startPoint == null) {
					startPoint = new Point(Double.valueOf(pointStr[0]),
							Double.valueOf(pointStr[1]));
					path.startPath(startPoint);
				} else {
					path.lineTo(new Point(Double.valueOf(pointStr[0]), Double
							.valueOf(pointStr[1])));
				}
			}
		}
		return path;
	}
	private static Geometry parseWKT2(String multipath, String type) {
		MultiPath path = null;
		String headStr = multipath.substring(0, multipath.indexOf("(")).trim();//去掉空格
		String temp = multipath.substring(multipath.indexOf("(") + 1, multipath.lastIndexOf(")"));
		String[] multiStr = temp.split("\\),\\(");
		if (type.equalsIgnoreCase("MULTILINESTRING")) {
			path = new Polyline();
		}
		Point startPoint = null;
		for (int i = 0; i < multiStr.length; i++) {
			String[] points = multiStr[i].split(",");
			startPoint = null;
			for (int j = 0; j < points.length; j++) {
				String poinstr=points[j].trim();
				String[] pointStr = poinstr.split(" ");
				if (startPoint == null) {
					startPoint = new Point(Double.valueOf(pointStr[0]),
							Double.valueOf(pointStr[1]));
					path.startPath(startPoint);
				} else {
					path.lineTo(new Point(Double.valueOf(pointStr[0]), Double
							.valueOf(pointStr[1])));
				}
			}
		}
		
		return path;
	}
}
