package com.movementinsome.caice.vo;

/**
 * 用来临时装原照片路径的VO
 * Created by zzc on 2017/6/2.
 */

public class PointCameraVO {
    //物探点号
    private String facName;

    //设备名
    private String implementorName;

    //口径
    private String caliber;

    //类型
    private String type;

    //设备点经度
    private String longitudeWg84;

    //设备点纬度
    private String latitudeWg84;

    //地方坐标X
    private String mapx;

    //地方坐标Y
    private String mapy;

    //地面高程
    private String groundElevation;

    //埋深
    private String burialDepth;

    //发生地址
    private String happenAddr;

    //行政区
    private String administrativeRegion;

    //照片路径
    private String camera;

    public String getFacName() {
        return facName;
    }

    public void setFacName(String facName) {
        this.facName = facName;
    }

    public String getImplementorName() {
        return implementorName;
    }

    public void setImplementorName(String implementorName) {
        this.implementorName = implementorName;
    }

    public String getCaliber() {
        return caliber;
    }

    public void setCaliber(String caliber) {
        this.caliber = caliber;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLongitudeWg84() {
        return longitudeWg84;
    }

    public void setLongitudeWg84(String longitudeWg84) {
        this.longitudeWg84 = longitudeWg84;
    }

    public String getLatitudeWg84() {
        return latitudeWg84;
    }

    public void setLatitudeWg84(String latitudeWg84) {
        this.latitudeWg84 = latitudeWg84;
    }

    public String getGroundElevation() {
        return groundElevation;
    }

    public void setGroundElevation(String groundElevation) {
        this.groundElevation = groundElevation;
    }

    public String getBurialDepth() {
        return burialDepth;
    }

    public void setBurialDepth(String burialDepth) {
        this.burialDepth = burialDepth;
    }

    public String getHappenAddr() {
        return happenAddr;
    }

    public void setHappenAddr(String happenAddr) {
        this.happenAddr = happenAddr;
    }

    public String getAdministrativeRegion() {
        return administrativeRegion;
    }

    public void setAdministrativeRegion(String administrativeRegion) {
        this.administrativeRegion = administrativeRegion;
    }

    public String getCamera() {
        return camera;
    }

    public void setCamera(String camera) {
        this.camera = camera;
    }

    public String getMapx() {
        return mapx;
    }

    public void setMapx(String mapx) {
        this.mapx = mapx;
    }

    public String getMapy() {
        return mapy;
    }

    public void setMapy(String mapy) {
        this.mapy = mapy;
    }

    @Override
    public String toString() {
        return "{" +
                "facName:"+ facName +
                ", implementorName:" + implementorName +
                ", caliber:" + caliber +
                ", type:" + type +
                ", longitudeWg84:" + longitudeWg84 +
                ", latitudeWg84:" + latitudeWg84 +
                ", groundElevation:" + groundElevation +
                ", burialDepth:" + burialDepth +
                ", happenAddr:" + happenAddr +
                ", administrativeRegion:" + administrativeRegion +
                ", camera:" + camera +
                '}';
    }
}
