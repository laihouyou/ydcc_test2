package com.movementinsome.caice.vo;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

/**
 * Created by zzc on 2017/5/10.
 */
@DatabaseTable(tableName = "SaveLineVo")
public class SaveLineVo implements Serializable{

    /**
     * pipName : jjj
     * caliber : 366
     * happenAddr : 海珠区敦和路189-191
     * tubularProduct : njjj
     * pipType : nnj
     * layingType : nnn
     * administrativeRegion : dedd
     * pointList : (113.32625259412 23.098110352757),(113.32829174763 23.095642091396),(113.32845344262 23.092500601512)
     * camera :
     */


    //主键ID
    @DatabaseField(id = true)
    private String id;
    //管线编号
    @DatabaseField
    private String pipName;
    //口径
    @DatabaseField
    private String caliber;
    //所在道路
    @DatabaseField
    private String happenAddr;
    //管材
    @DatabaseField
    private String tubularProduct;
    //管线类型
    @DatabaseField
    private String pipType;
    //敷设类型
    @DatabaseField
    private String layingType;
    //行政区
    @DatabaseField
    private String administrativeRegion;
    //坐标集合
    @DatabaseField
    private String pointList;
    //照片路径
    @DatabaseField
    private String camera;

    //采集类型
    @DatabaseField
    private String gatherType;

    //是否是连续采集
    @DatabaseField
    private String isSuccession;

    //管线长度
    @DatabaseField
    private double pipelineLinght;

    //最后一个提交点ID
    @DatabaseField
    private String endMarkerId;

    //工程ID
    @DatabaseField
    private String projectId;

    //工程数据是否共享  0为共享，1为不共享
    @DatabaseField
    private String isProjectShare;

    //共享码
    @DatabaseField
    private String shareCode;

    //工程名字
    @DatabaseField
    private String projectName;

    //工程类型
    @DatabaseField
    private String projectType;


    //终端经度
    @DatabaseField
    private String myLongitude;

    //终端纬度
    @DatabaseField
    private String myLatitude;

    //坐标类型
    @DatabaseField
    private String coordinateType;

    //上传时间
    @DatabaseField
    private String uploadTime;

    //上传人
    @DatabaseField
    private String uploadName;

    //原来数据库VO 的ID
    @DatabaseField
    private String dynamicFormVOId;

    //返回的百度数据ID集合
    @DatabaseField
    private String ids;

    //设施点数据ID集合
    @DatabaseField
    private String sIds;

    //设施点编号集合
    @DatabaseField
    private String facNums;

    //第一个设施编号
    @DatabaseField
    private String firstFacNum;

    //最后一个设施编号
    @DatabaseField
    private String endFacNum;

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

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getIsProjectShare() {
        return isProjectShare;
    }

    public void setIsProjectShare(String isProjectShare) {
        this.isProjectShare = isProjectShare;
    }

    public String getShareCode() {
        return shareCode;
    }

    public void setShareCode(String shareCode) {
        this.shareCode = shareCode;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getMyLongitude() {
        return myLongitude;
    }

    public void setMyLongitude(String myLongitude) {
        this.myLongitude = myLongitude;
    }

    public String getMyLatitude() {
        return myLatitude;
    }

    public void setMyLatitude(String myLatitude) {
        this.myLatitude = myLatitude;
    }

    public String getCoordinateType() {
        return coordinateType;
    }

    public void setCoordinateType(String coordinateType) {
        this.coordinateType = coordinateType;
    }

    public String getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(String uploadTime) {
        this.uploadTime = uploadTime;
    }

    public String getUploadName() {
        return uploadName;
    }

    public void setUploadName(String uploadName) {
        this.uploadName = uploadName;
    }

    public String getEndMarkerId() {
        return endMarkerId;
    }

    public void setEndMarkerId(String endMarkerId) {
        this.endMarkerId = endMarkerId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProjectType() {
        return projectType;
    }

    public void setProjectType(String projectType) {
        this.projectType = projectType;
    }

    public String getDynamicFormVOId() {
        return dynamicFormVOId;
    }

    public void setDynamicFormVOId(String dynamicFormVOId) {
        this.dynamicFormVOId = dynamicFormVOId;
    }

    public String getGatherType() {
        return gatherType;
    }

    public void setGatherType(String gatherType) {
        this.gatherType = gatherType;
    }

    public double getPipelineLinght() {
        return pipelineLinght;
    }

    public void setPipelineLinght(double pipelineLinght) {
        this.pipelineLinght = pipelineLinght;
    }

    public String getIds() {
        return ids;
    }

    public void setIds(String ids) {
        this.ids = ids;
    }

    public String getsIds() {
        return sIds;
    }

    public void setsIds(String sIds) {
        this.sIds = sIds;
    }

    public String getFacNums() {
        return facNums;
    }

    public void setFacNums(String facNums) {
        this.facNums = facNums;
    }

    public String getFirstFacNum() {
        return firstFacNum;
    }

    public void setFirstFacNum(String firstFacNum) {
        this.firstFacNum = firstFacNum;
    }

    public String getEndFacNum() {
        return endFacNum;
    }

    public void setEndFacNum(String endFacNum) {
        this.endFacNum = endFacNum;
    }

    public String getIsSuccession() {
        return isSuccession;
    }

    public void setIsSuccession(String isSuccession) {
        this.isSuccession = isSuccession;
    }
}
