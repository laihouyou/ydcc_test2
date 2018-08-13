package com.movementinsome.caice.vo;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

/**
 * 设施点数据实体类
 *
 * Created by zzc on 2017/5/10.
 */
@DatabaseTable(tableName = "SavePointVo")
public class SavePointVo implements Serializable{

    /**
     * facName : lol
     * implementorName :
     * caliber :
     * type :
     * longitude : 666
     * latitude : 3365
     * groundElevation : 啦啦啦
     * burialDepth : 5258
     * happenAddr : 海珠区敦和路189号-、191号
     * administrativeRegion : 啃你
     * camera : /storage/sdcard0/DCIM/IMG_2017-03-23682594053.jpg,/storage/sdcard0/DCIM/IMG_2017-03-23232451519.jpg
     */

    //主键ID
    @DatabaseField(id = true)
    private String id;

    /**
     * 设施点独有字段
     */

    //物探点号   （地图展现的物探点号）   (精简物探点号)   JPAA100    其中AA为任务编号，手动填写的    100为顺序号
    @DatabaseField
    private String facName;

    //设备名
    @DatabaseField
    private String implementorName;


    //地面高程
    @DatabaseField
    private String groundElevation;

    //埋深
    @DatabaseField
    private String burialDepth;


    /**
     * 管线独有字段
     */
    //管线编号
    @DatabaseField
    private String pipName;

    //管材
    @DatabaseField
    private String tubularProduct;

    //管线类型
    @DatabaseField
    private String pipType;

    //敷设类型
    @DatabaseField
    private String layingType;

    //绘制顺序
    @DatabaseField
    private String drawNum;

    //管线长度
    @DatabaseField
    private double pipelineLinght;

    //最后一个提交点ID
    @DatabaseField
    private String endMarkerId;

    //坐标集合
    @DatabaseField
    private String pointList;

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

    /**
     * 设施点与管线共同字段
     */

    //设备点经度     上传BS坐标      bd09
    @DatabaseField
    private String longitude;

    //设备点纬度     上传BS坐标      bd09
    @DatabaseField
    private String latitude;

//    //设备点经度           gcj02
//    @DatabaseField
//    private String longitudeGcj02;
//
//    //设备点纬度           gcj02
//    @DatabaseField
//    private String latitudeGcj02;

    //设备点经度     wg84    原始坐标    地图展现坐标
    @DatabaseField
    private String longitudeWg84;

    //设备点纬度     wg84    原始坐标    地图展现坐标
    @DatabaseField
    private String latitudeWg84;

    //地方X坐标
    @DatabaseField
    private String mapx;

    //地方Y坐标
    @DatabaseField
    private String mapy;

    //发生地址
    @DatabaseField
    private String happenAddr;

    //行政区
    @DatabaseField
    private String administrativeRegion;

    //口径
    @DatabaseField
    private String caliber;

    //照片路径
    @DatabaseField
    private String camera;

    //采集类型
    @DatabaseField
    private String gatherType;

    //是否是连续采集
    @DatabaseField
    private String isSuccession;

    //提交百度LBS云成功返回的ID  对应的id
    @DatabaseField
    private String markerId;

    //工程ID
    @DatabaseField
    private String projectId;

    //工程数据是否共享  0为共享，1为不共享
    @DatabaseField
    private String isProjectShare;

    //共享码
    @DatabaseField
    private String shareCode;

    // 新共享码
    @DatabaseField
    private String newShareCode;

    //工程名字
    @DatabaseField
    private String projectName;

    //工程类型
    @DatabaseField
    private String projectType;


    //终端经度
    @DatabaseField
    private String myLongitude;     //bd09

    //终端纬度
    @DatabaseField
    private String myLatitude;      //bd09

    //坐标类型
    @DatabaseField
    private String coordinateType;

    //上传时间
    @DatabaseField
    private String uploadTime;

    //上传人
    @DatabaseField
    private String uploadName;

    //上传人id
    @DatabaseField
    private String usedId;

    //原来数据库VO 的ID
    @DatabaseField
    private String dynamicFormVOId;

    //原来数据库VO 的关联附件唯一ID
    @DatabaseField
    private String guid;

    //数据类型  point 或者line
    @DatabaseField
    private String dataType;

    // 工程是否已提交
    //0为未提交
    //1为已提交
    @DatabaseField
    private String isPresent;

    //是否编辑
    //0为未编辑
    //1为已编辑
    @DatabaseField
    private String isCompile;

    //备注
    @DatabaseField
    private String remarks;

    //填单所填写的地段集合
    @DatabaseField
    private String context;

    //定位VO 数据集合
    @DatabaseField
    private String locationJson;

    /**
     * 探漏字段
     */

    //是否爆漏
    //0 为不漏
    //1 为漏点
    @DatabaseField
    private String isLeak;

    //确定口径
    @DatabaseField
    private String caliber_leak;

    //确定埋深
    @DatabaseField
    private String burialDepth_leak;

    //确定照片路径
    @DatabaseField
    private String camera_leak;

    //确定备注
    @DatabaseField
    private String remarks_leak;

    //确定人
    @DatabaseField
    private String uploadName_leak;

    //确定人id
    @DatabaseField
    private String userId_leak;

    //确定时间
    @DatabaseField
    private String uploadTime_leak;

    //确认经度
    @DatabaseField
    private String longitudeBd09_leak;

    //确认纬度
    @DatabaseField
    private String latitudeBd09_leak;

    //确认漏水量
    @DatabaseField
    private String water_leak;

    //填单所填写的字段集合
    @DatabaseField
    private String context_leak;

    //确定定位VO 数据集合
    @DatabaseField
    private String locationJson_leak;

    //探漏点id
    @DatabaseField
    private String patrolId;

    //探漏类型      探漏点或确定点     patrol  为探漏点  leak为确定点
    @DatabaseField
    private String type;

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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String isProjectShare() {
        return isProjectShare;
    }

    public void setProjectShare(String projectShare) {
        isProjectShare = projectShare;
    }

    public String getShareCode() {
        return shareCode;
    }

    public void setShareCode(String shareCode) {
        this.shareCode = shareCode;
    }

    public String getIsProjectShare() {
        return isProjectShare;
    }

    public void setIsProjectShare(String isProjectShare) {
        this.isProjectShare = isProjectShare;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getMarkerId() {
        return markerId;
    }

    public void setMarkerId(String markerId) {
        this.markerId = markerId;
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

    public String getIsSuccession() {
        return isSuccession;
    }

    public void setIsSuccession(String isSuccession) {
        this.isSuccession = isSuccession;
    }

    public String getPipName() {
        return pipName;
    }

    public void setPipName(String pipName) {
        this.pipName = pipName;
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

    public double getPipelineLinght() {
        return pipelineLinght;
    }

    public void setPipelineLinght(double pipelineLinght) {
        this.pipelineLinght = pipelineLinght;
    }

    public String getEndMarkerId() {
        return endMarkerId;
    }

    public void setEndMarkerId(String endMarkerId) {
        this.endMarkerId = endMarkerId;
    }

    public String getPointList() {
        return pointList;
    }

    public void setPointList(String pointList) {
        this.pointList = pointList;
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

    public String getIsPresent() {
        return isPresent;
    }

    public void setIsPresent(String isPresent) {
        this.isPresent = isPresent;
    }

    public String getIsCompile() {
        return isCompile;
    }

    public void setIsCompile(String isCompile) {
        this.isCompile = isCompile;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public String getUsedId() {
        return usedId;
    }

    public void setUsedId(String usedId) {
        this.usedId = usedId;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
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

    public String getLocationJson() {
        return locationJson;
    }

    public void setLocationJson(String locationJson) {
        this.locationJson = locationJson;
    }

    public String getNewShareCode() {
        return newShareCode;
    }

    public void setNewShareCode(String newShareCode) {
        this.newShareCode = newShareCode;
    }

    public String getIsLeak() {
        return isLeak;
    }

    public void setIsLeak(String isLeak) {
        this.isLeak = isLeak;
    }

    public String getCaliber_leak() {
        return caliber_leak;
    }

    public void setCaliber_leak(String caliber_leak) {
        this.caliber_leak = caliber_leak;
    }

    public String getBurialDepth_leak() {
        return burialDepth_leak;
    }

    public void setBurialDepth_leak(String burialDepth_leak) {
        this.burialDepth_leak = burialDepth_leak;
    }

    public String getCamera_leak() {
        return camera_leak;
    }

    public void setCamera_leak(String camera_leak) {
        this.camera_leak = camera_leak;
    }

    public String getRemarks_leak() {
        return remarks_leak;
    }

    public void setRemarks_leak(String remarks_leak) {
        this.remarks_leak = remarks_leak;
    }

    public String getUploadName_leak() {
        return uploadName_leak;
    }

    public void setUploadName_leak(String uploadName_leak) {
        this.uploadName_leak = uploadName_leak;
    }

    public String getUserId_leak() {
        return userId_leak;
    }

    public void setUserId_leak(String userId_leak) {
        this.userId_leak = userId_leak;
    }

    public String getUploadTime_leak() {
        return uploadTime_leak;
    }

    public void setUploadTime_leak(String uploadTime_leak) {
        this.uploadTime_leak = uploadTime_leak;
    }

    public String getLongitudeBd09_leak() {
        return longitudeBd09_leak;
    }

    public void setLongitudeBd09_leak(String longitudeBd09_leak) {
        this.longitudeBd09_leak = longitudeBd09_leak;
    }

    public String getLatitudeBd09_leak() {
        return latitudeBd09_leak;
    }

    public void setLatitudeBd09_leak(String latitudeBd09_leak) {
        this.latitudeBd09_leak = latitudeBd09_leak;
    }

    public String getContext_leak() {
        return context_leak;
    }

    public void setContext_leak(String context_leak) {
        this.context_leak = context_leak;
    }

    public String getLocationJson_leak() {
        return locationJson_leak;
    }

    public void setLocationJson_leak(String locationJson_leak) {
        this.locationJson_leak = locationJson_leak;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getPatrolId() {
        return patrolId;
    }

    public void setPatrolId(String patrolId) {
        this.patrolId = patrolId;
    }

    public String getWater_leak() {
        return water_leak;
    }

    public void setWater_leak(String water_leak) {
        this.water_leak = water_leak;
    }
}
