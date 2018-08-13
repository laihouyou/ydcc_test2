package com.movementinsome.kernel.location;

import com.movementinsome.kernel.initial.model.CoordParam;
import com.movementinsome.kernel.location.coordinate.Gcj022Bd09;
import com.movementinsome.kernel.location.coordinate.Gcj022Gps;
import com.movementinsome.kernel.location.coordinate.Gps2Locale;
import com.movementinsome.kernel.location.coordinate.Gps2Mct;

import java.io.Serializable;
import java.util.Map;

public class LocationInfo implements Serializable {
	
	private double latitude; // 经度		wgs84
	
	private double longitude; // 纬度		wgs84

	private double latitude_gcj02; // 经度		gcj02

	private double longitude_gcj02; // 纬度		gcj02

	private double altitude; // 海拨
	
	private float accuracy; // 精度
	
	private float speed; // 速度
	
	private float bearing;// 方位角
	
	private String time; // 卫星时间
	
	private double mapx;
	
	private double mapy;
	
	private int satellites; //卫星数量
	
	private String locationModel; //定位模式:gps、网络、百度
	
	private String addr; //所在地址
	
	private String imei;
	
	private String usnum;
	
	private String usid;
	
	private Long speedType; // 前进方式 （1、步行 2、车载）

	private boolean isConnected;	//当前是否连接中海达

	private int Quality;  		//中海达解状态

	private String qualityStr;  		//中海达解状态 (字符串)

	private double aziMagneticNorth; 	//磁北方位角（单位：度）（来自GPVTG）

	private double aziTrueNorth; 	//真北方位角（单位：度）（来自GPVTG）

	private float diffAge; 	//差分龄期（单位：秒）（来自GPGGA）

	private String diffStationID; 	//基站ID（四位）（来自GPGGA）

	private double 	E; 	//测量坐标系下的东向坐标值

	private double 	N; 	//测量坐标系下的北向坐标值

	private double 	Z; 	//测量坐标系下的高程

	private double 	H; 	//WGS84坐标系下的高程（单位：米）（来自GPGGA）  = 海拔高程 + 椭球距离

	private double 	HDop; 	//水平精度因子（来自GPGGA，GPGSA）

	private double 	PDop; 	//位置精度因子（来自GPGSA）

	private double 	Undulation; 	//椭球距离（单位：米）（来自GPGGA）

	private double 	VDop; 	//垂直精度因子（来自GPGSA）

//	private double 	Velocity; 	//速度（米/秒）（来自GPVTG）

	private double 	XRms; 	//水平x中误差（单位：米）（来自GPGST）

	private double 	YRms; 	//水平y中误差（单位：米）（来自GPGST）

	private double 	HRms; 	//垂直中误差（单位：米）（来自GPGST）


	private int 	SolutionUsedSats; 	//解算使用的卫星数（来自BESTPOS）

	private boolean 	IsVerified; 	//是否已经过验证（来自BESTPOS）



/*	public LocationInfo(double latitude, double longitude, double altitude,
			float accuracy, float speed, float bearing, String time, double mapx,
			double mapy) {
		this.latitude = latitude;
		this.longitude = longitude;
		this.altitude = altitude;
		this.accuracy = accuracy;
		this.speed = speed;
		this.bearing = bearing;
		this.time = time;
		this.mapx = mapx;
		this.mapy = mapy;
	}*/

	public LocationInfo(){
		;
	}
	
	public LocationInfo(double latitude, double longitude, double altitude,
			float accuracy, float speed, float bearing, String time, double mapx,
			double mapy,String locationModel,String addr,int satellites) {
		this.latitude = latitude;
		this.longitude = longitude;
		this.altitude = altitude;
		this.accuracy = accuracy;
		this.speed = speed;
		this.bearing = bearing;
		this.time = time;
		this.mapx = mapx;
		this.mapy = mapy;
		this.locationModel = locationModel;
		this.addr = addr;
		this.satellites = satellites;
	}
	
	public Long getSpeedType() {
		return speedType;
	}

	public void setSpeedType(Long speedType) {
		this.speedType = speedType;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public double getAltitude() {
		return altitude;
	}

	public void setAltitude(double altitude) {
		this.altitude = altitude;
	}

	public float getAccuracy() {
		return accuracy;
	}

	public void setAccuracy(float accuracy) {
		this.accuracy = accuracy;
	}

	public float getSpeed() {
		return speed;
	}

	public void setSpeed(float speed) {
		this.speed = speed;
	}

	public float getBearing() {
		return bearing;
	}

	public void setBearing(float bearing) {
		this.bearing = bearing;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public double getMapx() {
		return mapx;
	}

	public void setMapx(double mapx) {
		this.mapx = mapx;
	}

	public double getMapy() {
		return mapy;
	}

	public void setMapy(double mapy) {
		this.mapy = mapy;
	}

	public int getSatellites() {
		return satellites;
	}

	public void setSatellites(int satellites) {
		this.satellites = satellites;
	}

	public String getLocationModel() {
		return locationModel;
	}

	public void setLocationModel(String locationModel) {
		this.locationModel = locationModel;
	}

	public String getAddr() {
		return addr;
	}

	public void setAddr(String addr) {
		this.addr = addr;
	}

	public String getImei() {
		return imei;
	}

	public void setImei(String imei) {
		this.imei = imei;
	}

	public String getUsnum() {
		return usnum;
	}

	public void setUsnum(String usnum) {
		this.usnum = usnum;
	}

	public String getUsid() {
		return usid;
	}

	public void setUsid(String usid) {
		this.usid = usid;
	}

	public boolean isConnected() {
		return isConnected;
	}

	public void setConnected(boolean connected) {
		isConnected = connected;
	}

	public int getQuality() {
		return Quality;
	}

	public void setQuality(int quality) {
		Quality = quality;
	}

	public double getAziMagneticNorth() {
		return aziMagneticNorth;
	}

	public void setAziMagneticNorth(double aziMagneticNorth) {
		this.aziMagneticNorth = aziMagneticNorth;
	}

	public double getAziTrueNorth() {
		return aziTrueNorth;
	}

	public void setAziTrueNorth(double aziTrueNorth) {
		this.aziTrueNorth = aziTrueNorth;
	}

	public float getDiffAge() {
		return diffAge;
	}

	public void setDiffAge(float diffAge) {
		this.diffAge = diffAge;
	}

	public String getDiffStationID() {
		return diffStationID;
	}

	public void setDiffStationID(String diffStationID) {
		this.diffStationID = diffStationID;
	}

	public double getE() {
		return E;
	}

	public void setE(double e) {
		E = e;
	}

	public double getN() {
		return N;
	}

	public void setN(double n) {
		N = n;
	}

	public double getZ() {
		return Z;
	}

	public void setZ(double z) {
		Z = z;
	}

	public double getH() {
		return H;
	}

	public void setH(double h) {
		H = h;
	}

	public double getHDop() {
		return HDop;
	}

	public void setHDop(double HDop) {
		this.HDop = HDop;
	}

	public double getHRms() {
		return HRms;
	}

	public void setHRms(double HRms) {
		this.HRms = HRms;
	}

	public double getPDop() {
		return PDop;
	}

	public void setPDop(double PDop) {
		this.PDop = PDop;
	}

	public double getUndulation() {
		return Undulation;
	}

	public void setUndulation(double undulation) {
		Undulation = undulation;
	}

	public double getVDop() {
		return VDop;
	}

	public void setVDop(double VDop) {
		this.VDop = VDop;
	}

//	public double getVelocity() {
//		return Velocity;
//	}
//
//	public void setVelocity(double velocity) {
//		Velocity = velocity;
//	}

	public double getXRms() {
		return XRms;
	}

	public void setXRms(double XRms) {
		this.XRms = XRms;
	}

	public double getYRms() {
		return YRms;
	}

	public void setYRms(double YRms) {
		this.YRms = YRms;
	}

	public int getSolutionUsedSats() {
		return SolutionUsedSats;
	}

	public void setSolutionUsedSats(int solutionUsedSats) {
		SolutionUsedSats = solutionUsedSats;
	}

	public boolean isVerified() {
		return IsVerified;
	}

	public void setVerified(boolean verified) {
		IsVerified = verified;
	}

	public String getQualityStr() {
		return qualityStr;
	}

	public void setQualityStr(String qualityStr) {
		this.qualityStr = qualityStr;
	}

	public double getLatitude_gcj02() {
		return latitude_gcj02;
	}

	public void setLatitude_gcj02(double latitude_gcj02) {
		this.latitude_gcj02 = latitude_gcj02;
	}

	public double getLongitude_gcj02() {
		return longitude_gcj02;
	}

	public void setLongitude_gcj02(double longitude_gcj02) {
		this.longitude_gcj02 = longitude_gcj02;
	}

	//获取当前GPS坐标位置
	public String getCurGpsPosition(){
		return this.longitude +" "+this.latitude;
		//AppContext.getInstance().getCurLocation().getLongitude()+" "+AppContext.getInstance().getCurLocation().getLatitude()
	}
	
	//获取当前地图坐标系统的坐标位置
	public String getCurMapPosition(){
		String x = String.valueOf(this.mapx);
		String y = String.valueOf(this.mapy);
		
		if(x.contains("E")){
			x = _transformE(String.valueOf(this.mapx));
		}
		if (y.contains("E")){
			y = _transformE(String.valueOf(this.mapx));
		}
		return x+" "+y;
	}
	
	public String getCurGcjPosition(){
		Map<String, Double> lonlat = Gcj022Gps.wgs2gcj(this.longitude, this.latitude);
		return	 lonlat.get("lon")+" "+lonlat.get("lat");
		//AppContext.getInstance().getCurLocation().getLongitude()+" "+AppContext.getInstance().getCurLocation().getLatitude()
	}
	
	public String getCurBd09Position(){
		Map<String, Double> lonlat = Gcj022Gps.wgs2gcj(this.longitude, this.latitude);
		double bdlonlat[] = Gcj022Bd09.bd09Encrypt(lonlat.get("lat"), lonlat.get("lon"));
		return	 bdlonlat[0]+" "+bdlonlat[1];
		//AppContext.getInstance().getCurLocation().getLongitude()+" "+AppContext.getInstance().getCurLocation().getLatitude()
	}
	
	//当地图坐标值为web莫卡托时,转换为经纬度时国内的坐标值为gcj02，所以不需要转为84.可直接通过gcj02转为百度的bd09
	public String getCurWebM2Bd09Position(){
		double wgslonlat[] = Gps2Mct.mercator2lonLat(this.mapx, this.mapy);
		//Map<String, Double> lonlat = Gcj022Gps.wgs2gcj(wgslonlat[0], wgslonlat[1]);
		double bdlonlat[] = Gcj022Bd09.bd09Encrypt(wgslonlat[1], wgslonlat[0]);
		return bdlonlat[0]+" "+bdlonlat[1];
		//AppContext.getInstance().getCurLocation().getLongitude()+" "+AppContext.getInstance().getCurLocation().getLatitude()
	}
	
	public String getCurWebM2Bd09Position(double x,double y){
		double wgslonlat[] = Gps2Mct.mercator2lonLat(x, y);
		//Map<String, Double> lonlat = Gcj022Gps.wgs2gcj(wgslonlat[0], wgslonlat[1]);
		double bdlonlat[] = Gcj022Bd09.bd09Encrypt(wgslonlat[1], wgslonlat[0]);
		return bdlonlat[0]+" "+bdlonlat[1];
		//AppContext.getInstance().getCurLocation().getLongitude()+" "+AppContext.getInstance().getCurLocation().getLatitude()
	}
	
	public String getCurLocal2Bd09Position(CoordParam param){
		double wgslonlat[] = Gps2Locale.RevTranslate(this.mapx, this.mapy, 0, param);
		Map<String, Double> lonlat = Gcj022Gps.wgs2gcj(wgslonlat[0], wgslonlat[1]);
		double bdlonlat[] = Gcj022Bd09.bd09Encrypt(lonlat.get("lat"), lonlat.get("lon"));
		return	 bdlonlat[0]+" "+bdlonlat[1];
		//AppContext.getInstance().getCurLocation().getLongitude()+" "+AppContext.getInstance().getCurLocation().getLatitude()
	}
	/**
	 * 科学计数法转化
	 * */
/*	public static String transformE(String mapCoord){
		if(mapCoord!=null && mapCoord.contains("E")){
			String[] arr = null;
			String sp = null;
			if(mapCoord.contains(",")){
				arr = mapCoord.split(",");
				sp = ",";
			}else if(mapCoord.contains(" ")){
				arr = mapCoord.split(" ");
				sp = " ";
			}
			if(arr==null){
				mapCoord = _transformE(mapCoord);
			}else{
				StringBuffer buf = new StringBuffer();
				for(String x: arr){
					if(buf.length()!=0){
						buf.append(sp);
					}
					if(x.contains("E")){
						buf.append(_transformE(x));
					}else{
						buf.append(x);
					}
				}
				mapCoord = buf.toString();
			}
		}
		return mapCoord;
	}*/
	
	private static String _transformE(String x){
		StringBuffer coord = new StringBuffer();
		int idx = x.indexOf("E");
		if (idx<0){
			return x;
		}
		String startStr = x.substring(0, idx);
		String endStr = x.substring(idx+1);
		idx = startStr.indexOf(".");
		coord.append(startStr.substring(0, idx));
		startStr = startStr.substring(idx+1);
		idx = Integer.valueOf(endStr);
		coord.append(startStr.substring(0, idx));
		if(idx<startStr.length()){
			coord.append(".");
			coord.append(startStr.substring(idx));
		}
		return coord.toString();
	}
	
	public String toString(){
		return this.time+String.valueOf(this.mapx)+String.valueOf(this.mapy);
	}
}
