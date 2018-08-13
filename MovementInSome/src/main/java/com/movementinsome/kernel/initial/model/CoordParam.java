package com.movementinsome.kernel.initial.model;

public class CoordParam {
	private String id ;
	private String coordinate = "54";
	//七参数设置
	private double sdx = 0;//X平移（米）
	private double sdy = 0;//Y平移（米）
	private double sdz = 0;//Z平移（米）
	private double sqx = 0;//X轴旋转（秒）
	private double sqy = 0;//Y轴旋转（秒）
	private double sqz = 0;//Z轴旋转（秒）
	private double sscale = 0;//尺度ppm
	//四参数设置
	private double fdx = 0;//平移X。（米）
	private double fdy = 0;//平移Y。（米）
	private double fscale = 0; //尺度K
	private double frotateangle = 0;//旋转角度T(弧度)
	//投影参数设置
	private int pprojectionType = 3; //3度带、6度带
	private double pcentralmeridian = 114;  //中央子午线
	private double pscale = 1; //尺度
	private double pconstantx = 0; //X常数
	private double pconstanty = 500000; //Y常数
	private double pbenchmarklatitude = 0; //基准纬度
	private double semimajor = 6378245.0; //椭球长半轴54:6378245.0,80:6378140.0,84:6378137
	private double flattening = 1.0 / 298.3; //椭球扁率54：1.0 / 298.3， 80：1.0 / 298.257;84:1.0/298.2572236

	//城市名称
	private String cityName;
	//城市id
	private String cityCode;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCoordinate() {
		return coordinate;
	}

	public void setCoordinate(String coordinate) {
		this.coordinate = coordinate;
	}

	public double getSdx() {
		return sdx;
	}

	public void setSdx(double sdx) {
		this.sdx = sdx;
	}

	public double getSdy() {
		return sdy;
	}

	public void setSdy(double sdy) {
		this.sdy = sdy;
	}

	public double getSdz() {
		return sdz;
	}

	public void setSdz(double sdz) {
		this.sdz = sdz;
	}

	public double getSqx() {
		return sqx;
	}

	public void setSqx(double sqx) {
		this.sqx = sqx;
	}

	public double getSqy() {
		return sqy;
	}

	public void setSqy(double sqy) {
		this.sqy = sqy;
	}

	public double getSqz() {
		return sqz;
	}

	public void setSqz(double sqz) {
		this.sqz = sqz;
	}

	public double getSscale() {
		return sscale;
	}

	public void setSscale(double sscale) {
		this.sscale = sscale;
	}

	public double getFdx() {
		return fdx;
	}

	public void setFdx(double fdx) {
		this.fdx = fdx;
	}

	public double getFdy() {
		return fdy;
	}

	public void setFdy(double fdy) {
		this.fdy = fdy;
	}

	public double getFscale() {
		return fscale;
	}

	public void setFscale(double fscale) {
		this.fscale = fscale;
	}

	public double getFrotateangle() {
		return frotateangle;
	}

	public void setFrotateangle(double frotateangle) {
		this.frotateangle = frotateangle;
	}

	public int getPprojectionType() {
		return pprojectionType;
	}

	public void setPprojectionType(int pprojectionType) {
		this.pprojectionType = pprojectionType;
	}

	public double getPcentralmeridian() {
		return pcentralmeridian;
	}

	public void setPcentralmeridian(double pcentralmeridian) {
		this.pcentralmeridian = pcentralmeridian;
	}

	public double getPscale() {
		return pscale;
	}

	public void setPscale(double pscale) {
		this.pscale = pscale;
	}

	public double getPconstantx() {
		return pconstantx;
	}

	public void setPconstantx(double pconstantx) {
		this.pconstantx = pconstantx;
	}

	public double getPconstanty() {
		return pconstanty;
	}

	public void setPconstanty(double pconstanty) {
		this.pconstanty = pconstanty;
	}

	public double getPbenchmarklatitude() {
		return pbenchmarklatitude;
	}

	public void setPbenchmarklatitude(double pbenchmarklatitude) {
		this.pbenchmarklatitude = pbenchmarklatitude;
	}

	public double getSemimajor() {
		return semimajor;
	}

	public void setSemimajor(double semimajor) {
		this.semimajor = semimajor;
	}

	public double getFlattening() {
		return flattening;
	}

	public void setFlattening(double flattening) {
		this.flattening = flattening;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getCityCode() {
		return cityCode;
	}

	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}
}
