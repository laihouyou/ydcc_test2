package com.movementinsome.map.utils;


/**
 * @author YuLeyuan 比例尺工具类,用于在百度地图上使用
 */
public class ScaleUtils {
	// 最大缩放级别
	private static final int MAX_LEVEL = 19;
	// 各级比例尺分母值数组
	private static final int[] SCALES = { 20, 50, 100, 200, 500, 1000, 2000,
			5000, 10000, 20000, 25000, 50000, 100000, 200000, 500000, 1000000,
			2000000 };
	// 各级比例尺说明数组
	private static final String[] SCALE_DESCS = { "20米", "50米", "100米", "200米",
			"500米", "1公里", "2公里", "5公里", "10公里", "20公里", "25公里", "50公里",
			"100公里", "200公里", "500公里", "1000公里", "2000公里" };

	/**
	 * 根据缩放级别，得到对应比例尺在SCALES数组中的位置（索引）
	 * 
	 * @param zoomLevel
	 *            缩放级别
	 * @return 比例尺的分母
	 */
	private static int getScaleIndex(int zoomLevel) {
		return MAX_LEVEL - zoomLevel;
	}

	/**
	 * 根据缩放级别，得到对应比例尺
	 * 
	 * @param zoomLevel
	 *            缩放级别
	 * @return 比例尺的分母
	 */
	public static int getScale(int zoomLevel) {
		return SCALES[getScaleIndex(zoomLevel)];
	}

	/**
	 * 根据缩放级别，得到对应比例尺文字
	 * 
	 * @param zoomLevel
	 *            缩放级别
	 * @return 比例尺文字说明
	 */
	public static String getScaleDesc(int zoomLevel) {
		return SCALE_DESCS[getScaleIndex(zoomLevel)];
	}

	/**
	 * 根据地图当前中心位置的纬度，当前比例尺，得出比例尺图标应该显示多长（多少像素）
	 * 
	 * @param map
	 *            对应的地图控件
	 * @param scale
	 *            当前比例尺
	 * @return
	 */
/*	public static int meterToPixels(MapView map, int scale) {
		// 得到当前中心位置对象
		Point geoPoint = map.getMapCenter();
		// 得到当前中心位置纬度
		double latitude = geoPoint.getLatitudeE6() / 1E6;
		// 得到象素数，比如当前比例尺是1/10000，比如scale=10000，对应在该纬度应在地图中绘多少象素
		// 参考http://rainbow702.iteye.com/blog/1124244
		return (int) (map.getProjection().metersToEquatorPixels(scale) / (Math
				.cos(Math.toRadians(latitude))));
	}*/

}