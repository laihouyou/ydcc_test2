package com.movementinsome.caice.vo;

/**
 * 用来临时装原照片路径的VO
 * Created by zzc on 2017/6/2.
 */

public class LineCameraVO {
    //管线编号
    private String pipName;
    //口径
    private String caliber;
    //所在道路
    private String happenAddr;
    //管材
    private String tubularProduct;
    //管线类型
    private String pipType;
    //敷设类型
    private String layingType;
    //行政区
    private String administrativeRegion;
    //坐标集合
    private String pointList;
    //照片路径
    private String camera;

    public String getPipName() {
        return pipName;
    }

    public void setPipName(String pipName) {
        this.pipName = pipName;
    }

    public String getCaliber() {
        return caliber;
    }

    public void setCaliber(String caliber) {
        this.caliber = caliber;
    }

    public String getHappenAddr() {
        return happenAddr;
    }

    public void setHappenAddr(String happenAddr) {
        this.happenAddr = happenAddr;
    }

    public String getTubularProduct() {
        return tubularProduct;
    }

    public void setTubularProduct(String tubularProduct) {
        this.tubularProduct = tubularProduct;
    }

    public String getPipType() {
        return pipType;
    }

    public void setPipType(String pipType) {
        this.pipType = pipType;
    }

    public String getLayingType() {
        return layingType;
    }

    public void setLayingType(String layingType) {
        this.layingType = layingType;
    }

    public String getAdministrativeRegion() {
        return administrativeRegion;
    }

    public void setAdministrativeRegion(String administrativeRegion) {
        this.administrativeRegion = administrativeRegion;
    }

    public String getPointList() {
        return pointList;
    }

    public void setPointList(String pointList) {
        this.pointList = pointList;
    }

    public String getCamera() {
        return camera;
    }

    public void setCamera(String camera) {
        this.camera = camera;
    }

    @Override
    public String toString() {
        return "{" +
                "pipName:" + pipName +
                ", caliber:" + caliber +
                ", happenAddr:" + happenAddr +
                ", tubularProduct:" + tubularProduct +
                ", pipType:" + pipType +
                ", layingType:" + layingType +
                ", administrativeRegion:" + administrativeRegion +
                ", pointList:" + pointList +
                ", camera:" + camera +
                '}';
    }
}
