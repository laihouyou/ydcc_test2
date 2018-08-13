package com.movementinsome.caice.vo;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by zzc on 2017/10/16.
 */

@DatabaseTable(tableName = "CityVo")
public class CityVo {
    //记录ID
    @DatabaseField(id = true)
    private String id;
    //城市名称
    @DatabaseField
    private String cityName;
    //城市id
    @DatabaseField
    private String cityCode;

    @DatabaseField
    private String coordinate;
    //七参数设置
    @DatabaseField
    private double sdx = 0;//X平移（米）
    @DatabaseField
    private double sdy = 0;//Y平移（米）
    @DatabaseField
    private double sdz = 0;//Z平移（米）
    @DatabaseField
    private double sqx = 0;//X轴旋转（秒）
    @DatabaseField
    private double sqy = 0;//Y轴旋转（秒）
    @DatabaseField
    private double sqz = 0;//Z轴旋转（秒）
    @DatabaseField
    private double sscale = 0;//尺度ppm
    //四参数设置
    @DatabaseField
    private double fdx = 0;//平移X。（米）
    @DatabaseField
    private double fdy = 0;//平移Y。（米）
    @DatabaseField
    private double fscale = 0; //尺度K
    @DatabaseField
    private double frotateangle = 0;//旋转角度T(弧度)
    //投影参数设置
    @DatabaseField
    private long pprojectionType = 3; //3度带、6度带
    @DatabaseField
    private double pcentralmeridian = 114;  //中央子午线
    @DatabaseField
    private double pscale = 1; //尺度
    @DatabaseField
    private double pconstantx = 0; //X常数
    @DatabaseField
    private double pconstanty = 500000; //Y常数
    @DatabaseField
    private double pbenchmarklatitude = 0; //基准纬度
    @DatabaseField
    private double semimajor = 6378245.0; //椭球长半轴54:6378245.0,80:6378140.0,84:6378137
    @DatabaseField
    private double flattening = 1.0 / 298.3; //椭球扁率54：1.0 / 298.3， 80：1.0 / 298.257;84:1.0/298.2572236

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(String coordinate) {
        this.coordinate = coordinate;
    }

    public Double getSdx() {
        return sdx;
    }

    public void setSdx(Double sdx) {
        this.sdx = sdx;
    }

    public Double getSdy() {
        return sdy;
    }

    public void setSdy(Double sdy) {
        this.sdy = sdy;
    }

    public Double getSdz() {
        return sdz;
    }

    public void setSdz(Double sdz) {
        this.sdz = sdz;
    }

    public Double getSqx() {
        return sqx;
    }

    public void setSqx(Double sqx) {
        this.sqx = sqx;
    }

    public Double getSqy() {
        return sqy;
    }

    public void setSqy(Double sqy) {
        this.sqy = sqy;
    }

    public Double getSqz() {
        return sqz;
    }

    public void setSqz(Double sqz) {
        this.sqz = sqz;
    }

    public Double getSscale() {
        return sscale;
    }

    public void setSscale(Double sscale) {
        this.sscale = sscale;
    }

    public Double getFdx() {
        return fdx;
    }

    public void setFdx(Double fdx) {
        this.fdx = fdx;
    }

    public Double getFdy() {
        return fdy;
    }

    public void setFdy(Double fdy) {
        this.fdy = fdy;
    }

    public Double getFscale() {
        return fscale;
    }

    public void setFscale(Double fscale) {
        this.fscale = fscale;
    }

    public Double getFrotateangle() {
        return frotateangle;
    }

    public void setFrotateangle(Double frotateangle) {
        this.frotateangle = frotateangle;
    }

    public Long getPprojectionType() {
        return pprojectionType;
    }

    public void setPprojectionType(Long pprojectionType) {
        this.pprojectionType = pprojectionType;
    }

    public Double getPcentralmeridian() {
        return pcentralmeridian;
    }

    public void setPcentralmeridian(Double pcentralmeridian) {
        this.pcentralmeridian = pcentralmeridian;
    }

    public Double getPscale() {
        return pscale;
    }

    public void setPscale(Double pscale) {
        this.pscale = pscale;
    }

    public Double getPconstantx() {
        return pconstantx;
    }

    public void setPconstantx(Double pconstantx) {
        this.pconstantx = pconstantx;
    }

    public Double getPconstanty() {
        return pconstanty;
    }

    public void setPconstanty(Double pconstanty) {
        this.pconstanty = pconstanty;
    }

    public Double getPbenchmarklatitude() {
        return pbenchmarklatitude;
    }

    public void setPbenchmarklatitude(Double pbenchmarklatitude) {
        this.pbenchmarklatitude = pbenchmarklatitude;
    }

    public Double getSemimajor() {
        return semimajor;
    }

    public void setSemimajor(Double semimajor) {
        this.semimajor = semimajor;
    }

    public Double getFlattening() {
        return flattening;
    }

    public void setFlattening(Double flattening) {
        this.flattening = flattening;
    }
}
