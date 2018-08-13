package com.movementinsome.map.coordinate;

import com.esri.core.geometry.Point;
import com.esri.core.geometry.SpatialReference;
import com.movementinsome.kernel.initial.model.CoordParam;
import com.movementinsome.kernel.location.coordinate.Gps2Locale;

public class Locale2GpsCoordinate extends Gps2Locale implements ITranslateCoordinate  {

//	地方的坐标系
	@SuppressWarnings("unused")
	//private double _dLocalX, _dLocalY, _dLocalZ;
	
	public Point GPS2MapPoint(double lat, double lon, double alt, CoordParam param,SpatialReference spRefence){

			this.CalculateLocalCoordinate(lat, lon, alt, param);
			return new Point(this._dLocalX, this._dLocalY);
	}	
 
	/**
	 * 计算椭球的第一偏心率
	 * @return
	 */
	private double cal_e(){
		double f =0.00335281065983501543;
		double a = 6378245.000;
		
		
		//e为椭球的第一偏心率
		double e = Math.sqrt(2*f-1)/f;
				
		
		return e;
	}
	
	/**
	 * 计算椭球面卯酉圈的曲率半径
	 * @param B
	 * @return
	 */
	private double cal_N(double B){
		double f =0.00335281065983501543;
		double a = 6378245.000;
		
		
		//e为椭球的第一偏心率
		double e = cal_e();
				
		
		//W为第一辅助系数
		double W = Math.sqrt(1 - e * e * Math.sin(B) * Math.sin(B));
		
		//N为椭球面卯酉圈的曲率半径
		double N = a/W;
		
		return N;
	}
	
	/**
	 * 大地坐标转换为空间直角坐标
	 * @param B
	 * @param L
	 * @param H
	 */
	private void blh2Xyz(double B,double L,double H){
		//e为椭球的第一偏心率
		double e = cal_e();
				
		//W为第一辅助系数
		double W = Math.sqrt(1 - e * e * Math.sin(B) * Math.sin(B));
		
		//N为椭球面卯酉圈的曲率半径
		double N = cal_N(B);
				
		double X = (N+H)*Math.cos(B)*Math.cos(L);
		double Y = (N+H)*Math.cos(B)*Math.sin(L);
		double Z = (N*(1-e*e)+H)*Math.sin(B);
	}
	
	
	private void xyz2Blh(double X,double Y,double Z){
		//e为椭球的第一偏心率
		double e = cal_e();
		
		double L = Math.atan(Y/X);
		
		//N为椭球面卯酉圈的曲率半径
		double N = 0;
				
		//double B = Math.atan(Z*(N+H)/(Math.sqrt(X*X-Y*Y)*(N*(1-e*e)+H)))
	}
	
}
