package com.movementinsome.caice.vo;

import java.io.Serializable;

/**
 * Created by zzc on 2017/6/6.
 */

public class StatisticsChileListVo implements Serializable{

    private String facName;     //编号
    public String dateType;     //数据类型
    public String type;     //设备类型
    public String gatherType;   //采集类型
    public String uploadTime;   //上传时间
    public String happenAddr;  //采集地址
    public String isPresent;  //设施的提交状态
    public String isLeak;  //是否爆漏

    //设备点经度
    public String longitude;
    //设备点纬度
    public String latitude;

    //坐标集合
    private String pointList;

    //设施点点ID(百度云)
    private String pointId;

    //管线中点ID集合(百度云)
    private String lineIds;

    //设施点ID(本地数据库)
    private String pointDatabaseId;

    //管线id(本地数据库)
    private String lineDatabaseId;

    //管线长度
    private double pipelineLinght;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getGatherType() {
        return gatherType;
    }

    public void setGatherType(String gatherType) {
        this.gatherType = gatherType;
    }

    public String getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(String uploadTime) {
        this.uploadTime = uploadTime;
    }

    public String getHappenAddr() {
        return happenAddr;
    }

    public void setHappenAddr(String happenAddr) {
        this.happenAddr = happenAddr;
    }

    public String getDateType() {
        return dateType;
    }

    public void setDateType(String dateType) {
        this.dateType = dateType;
    }

    public String getFacName() {
        return facName;
    }

    public void setFacName(String facName) {
        this.facName = facName;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getPointList() {
        return pointList;
    }

    public void setPointList(String pointList) {
        this.pointList = pointList;
    }

    public String getPointId() {
        return pointId;
    }

    public void setPointId(String pointId) {
        this.pointId = pointId;
    }

    public String getLineIds() {
        return lineIds;
    }

    public void setLineIds(String lineIds) {
        this.lineIds = lineIds;
    }

    public String getPointDatabaseId() {
        return pointDatabaseId;
    }

    public void setPointDatabaseId(String pointDatabaseId) {
        this.pointDatabaseId = pointDatabaseId;
    }

    public String getLineDatabaseId() {
        return lineDatabaseId;
    }

    public void setLineDatabaseId(String lineDatabaseId) {
        this.lineDatabaseId = lineDatabaseId;
    }

    public double getPipelineLinght() {
        return pipelineLinght;
    }

    public void setPipelineLinght(double pipelineLinght) {
        this.pipelineLinght = pipelineLinght;
    }

    public String getIsPresent() {
        return isPresent;
    }

    public void setIsPresent(String isPresent) {
        this.isPresent = isPresent;
    }

    public String getIsLeak() {
        return isLeak;
    }

    public void setIsLeak(String isLeak) {
        this.isLeak = isLeak;
    }
}
