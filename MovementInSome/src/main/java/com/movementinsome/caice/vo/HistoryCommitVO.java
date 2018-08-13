package com.movementinsome.caice.vo;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by LJQ on 2017/3/15.
 */

@DatabaseTable(tableName = "HistoryCommitVO")
public class HistoryCommitVO  {

    @DatabaseField(id = true)
    private String id;

    @DatabaseField
    private String tableNum;
    @DatabaseField
    private String addr;
    @DatabaseField
    private String longitude;
    @DatabaseField
    private String latitude;
    @DatabaseField
    private String photo;

    @DatabaseField
    private String caliber;

    //点还是线   数据类型
    @DatabaseField
    private String dataType;

    //线的绘制顺序
    @DatabaseField
    private String drawNum;

    //手机本地数据库设施点ID
    @DatabaseField
    private String savePointVoId;

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

    public HistoryCommitVO(){

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCaliber() {
        return caliber;
    }

    public void setCaliber(String caliber) {
        this.caliber = caliber;
    }
    public String getTableNum() {
        return tableNum;
    }

    public void setTableNum(String tableNum) {
        this.tableNum = tableNum;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
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

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getDrawNum() {
        return drawNum;
    }

    public void setDrawNum(String drawNum) {
        this.drawNum = drawNum;
    }

    public String getSavePointVoId() {
        return savePointVoId;
    }

    public void setSavePointVoId(String savePointVoId) {
        this.savePointVoId = savePointVoId;
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

    public String getProjectType() {
        return projectType;
    }

    public void setProjectType(String projectType) {
        this.projectType = projectType;
    }

    @Override
    public String toString() {
        return "HistoryCommitVO{" +
                "id='" + id + '\'' +
                ", tableNum='" + tableNum + '\'' +
                ", addr='" + addr + '\'' +
                ", longitude='" + longitude + '\'' +
                ", latitude='" + latitude + '\'' +
                ", photo='" + photo + '\'' +
                ", caliber='" + caliber + '\'' +
                ", dataType='" + dataType + '\'' +
                ", drawNum='" + drawNum + '\'' +
                ", savePointVoId='" + savePointVoId + '\'' +
                ", projectId='" + projectId + '\'' +
                ", isProjectShare='" + isProjectShare + '\'' +
                ", shareCode='" + shareCode + '\'' +
                ", projectName='" + projectName + '\'' +
                ", myLongitude='" + myLongitude + '\'' +
                ", myLatitude='" + myLatitude + '\'' +
                ", coordinateType='" + coordinateType + '\'' +
                ", uploadTime='" + uploadTime + '\'' +
                ", uploadName='" + uploadName + '\'' +
                '}';
    }
}
