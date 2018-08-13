package com.movementinsome.caice.vo;

import com.movementinsome.AppContext;
import com.movementinsome.caice.okhttp.OkHttpParam;
import com.movementinsome.caice.util.DateUtil;
import com.movementinsome.kernel.location.LocationInfoExt;

/**
 * 设施管线通用上传字段
 * Created by zzc on 2018/3/22.
 */

public class FacPipBaseVo extends MoveBaseVo {
    public FacPipBaseVo(){
        setValue();
    }
    public FacPipBaseVo(String projectType){
        this.projectType=projectType;
        setValue();
    }

    //工程名字
    private String projectName;

    //工程类型
    private String projectType;

    //终端经度
    private String myLongitude;     //bd09

    //终端纬度
    private String myLatitude;      //bd09

    //终端坐标类型
    private String coordinateType;

    //上传时间
    private String uploadTime;

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectType() {
        return projectType;
    }

    public void setProjectType(String projectType) {
        this.projectType = projectType;
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

    public void setValue(){
        if (AppContext.getInstance().getCurLocation() != null) {
            LocationInfoExt location = AppContext.getInstance().getCurLocation();
            String locationStr = location.getCurBd09Position();        //获取bd09坐标
            String[] locationDouble = locationStr.split(" ");

            this.myLongitude=(locationDouble[0]);
            this.myLatitude=(locationDouble[1]);
            this.coordinateType=(OkHttpParam.BD09);
        }
        projectName=(AppContext.getInstance().getCurUserName());   //上传人
        uploadTime=(DateUtil.getNow());   //上传时间
    }
}
