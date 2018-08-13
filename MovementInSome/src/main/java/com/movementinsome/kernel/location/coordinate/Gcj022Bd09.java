package com.movementinsome.kernel.location.coordinate;

public class Gcj022Bd09 {

	final static double x_pi = 3.14159265358979324 * 3000.0 / 180.0;

	//gcj02转bd09
	public static double[] bd09Encrypt(double gg_lat, double gg_lon) {
		double x = gg_lon;
		double y = gg_lat;
		double z = Math.sqrt(x * x + y * y) + 0.00002 * Math.sin(y * x_pi);
		double theta = Math.atan2(y, x) + 0.000003 * Math.cos(x * x_pi);

		double bd_lon = z * Math.cos(theta) + 0.0065;
		double bd_lat = z * Math.sin(theta) + 0.006;
		
		double lonlat[] = {bd_lon,bd_lat};
		
		return lonlat;
	}

	//db09转为gcj02
	public static double[] bd09Decrypt(double bd_lat, double bd_lon) {
		double x = bd_lon - 0.0065;
		double y = bd_lat - 0.006;
		double z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * x_pi);
		double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * x_pi);
		double gg_lon = z * Math.cos(theta);
		double gg_lat = z * Math.sin(theta);
		
		double lonlat[] = {gg_lon,gg_lat};
		return lonlat;
	}
}
