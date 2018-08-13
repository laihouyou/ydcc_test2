package com.movementinsome.caice.vo;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.google.gson.Gson;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.movementinsome.caice.okhttp.OkHttpParam;
import com.movementinsome.caice.util.MapMeterMoveScope;
import com.movementinsome.map.utils.MapMeterScope;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 设施点数据实体类
 *
 * Created by zzc on 2017/5/10.
 */
@DatabaseTable(tableName = "SavePointVo")
public class SavePointVo implements Serializable{

    //主键ID
    @DatabaseField(id = true)
    private String facId;

    /**
     * 设施点独有字段
     */

    //物探点号   （地图展现的物探点号）   (精简物探点号)   JPAA100    其中AA为任务编号，手动填写的    100为顺序号
    @DatabaseField
    private String facName;

    //设备名
    @DatabaseField
    private String implementorName;

    //类型
    @DatabaseField
    private String facType;

    /**
     * 管线独有字段
     */
    //管线编号
    @DatabaseField
    private String pipName;

    //管材
    @DatabaseField
    private String pipMaterial;

    //管线类型
    @DatabaseField
    private String pipType;

    //敷设类型
    @DatabaseField
    private String layingType;

    //坐标集合
    @DatabaseField
    private String pointList;

    //设施id集合
    @DatabaseField
    private String points;

    /**
     * 设施点与管线共同字段
     */

    //设备点经度     上传BS坐标      bd09
    @DatabaseField
    private String longitude;

    //设备点纬度     上传BS坐标      bd09
    @DatabaseField
    private String latitude;

    //设备点经度     wg84    原始坐标    地图展现坐标
    @DatabaseField
    private String longitudeWg84;

    //设备点纬度     wg84    原始坐标    地图展现坐标
    @DatabaseField
    private String latitudeWg84;

    //采集类型
    @DatabaseField
    private String gatherType;

    //工程ID
    @DatabaseField
    private String projectId;

    //共享码
    @DatabaseField
    private String shareCode;

    //上传人id
    @DatabaseField
    private String userId;

    //表单数据库VO 的关联附件唯一ID
    @DatabaseField
    private String guid;

    //数据类型  point 或者line
    @DatabaseField
    private String dataType;

    // 工程是否已提交
    //0为未提交
    //1为已提交
    @DatabaseField
    private String facSubmitStatus;

    //其他 字段集合
    /**
     *  地面高程    groundElevation
     *  埋深      burialDepth
     *  管线长度       pipelineLinght
     *  地方X坐标   mapx
     * 地方Y坐标    mapy
     * 发生地址     happenAddr
     * 行政区         administrativeRegion
     * 口径       caliber
     * 照片路径     camera
     * 视屏路径     videoCamera
     * 是否是连续采集      isSuccession
     * 填单所填写的地段集合   formContext
     * 定位VO 数据集合        locationJson
     * 设施管线通用上传字段 数据集合        FacPipBaseVo
     * 管线上设施表号集合 (逗号隔开)    facNameList
     * 更新来源    updateSource  （mobile  or  pc）（手机或者电脑   更新来源为手机端不做更新操作，为电脑端则要做更新操作覆盖本地数据）
     * 表单名字    formName
     */
    @DatabaseField
    private String contextStr;

    /**
     *  地面高程    groundElevation
     * @return
     */
    public String getGroundElevation(){
        try {
            JSONObject contextStr_json=new JSONObject(this.contextStr);
            String context=contextStr_json.getString(OkHttpParam.FORM_CONTEXT);
            return context;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }
    /**
     *埋深   burialDepth
     * @return
     */
    public String getBurialDepth(){
        try {
            JSONObject contextStr_json=new JSONObject(this.contextStr);
            String context=contextStr_json.getString(OkHttpParam.BURIAL_DEPTH);
            return context;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }
    /**
     *  管线长度    pipelineLinght
     * @return
     */
    public Double getPipelineLinght(){
        try {
            JSONObject contextStr_json=new JSONObject(this.contextStr);
            Double context=contextStr_json.getDouble(OkHttpParam.PIPELINE_LINGHT);
            return context;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return 0.0;
    }
    /**
     *  地方X坐标    mapx
     * @return
     */
    public String getMapx(){
        try {
            JSONObject contextStr_json=new JSONObject(this.contextStr);
            String context=contextStr_json.getString(OkHttpParam.MAP_X);
            return context;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }
    /**
     *  地方Y坐标    mapy
     * @return
     */
    public String getMapy(){
        try {
            JSONObject contextStr_json=new JSONObject(this.contextStr);
            String context=contextStr_json.getString(OkHttpParam.MAP_Y);
            return context;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }
    /**
     *  发生地址    happenAddr
     * @return
     */
    public String getHappenAddr(){
        try {
            JSONObject contextStr_json=new JSONObject(getContext());
            String context=contextStr_json.getString(OkHttpParam.HAPPEN_ADDR);
            return context;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }
    /**
     *  行政区    administrativeRegion
     * @return
     */
    public String getAdministrativeRegion(){
        try {
            JSONObject contextStr_json=new JSONObject(this.contextStr);
            String context=contextStr_json.getString(OkHttpParam.ADMINISTRATIVE_REGION);
            return context;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }
    /**
     *  口径    caliber
     * @return
     */
    public String getCaliber(){
        try {
            JSONObject contextStr_json=new JSONObject(this.contextStr);
            String context=contextStr_json.getString(OkHttpParam.CALIBER);
            return context;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }
    /**
     *  照片路径    camera
     * @return
     */
    public String getCamera(){
        try {
            String formContext=getContext();
            JSONObject contextStr_json=new JSONObject(formContext);
            String context=contextStr_json.getString(OkHttpParam.CAMERA);
            return context;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }
    /**
     *  视屏路径    camera
     * @return
     */
    public String getVideoCamera(){
        try {
            String formContext=getContext();
            JSONObject contextStr_json=new JSONObject(formContext);
            String context=contextStr_json.getString(OkHttpParam.VIDEO_CAMERA);
            return context;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }
    /**
     *  是否是连续采集    isSuccession
     * @return
     */
    public String getIsSuccession(){
        try {
            JSONObject contextStr_json=new JSONObject(this.contextStr);
            String context=contextStr_json.getString(OkHttpParam.IS_SUCCESSION);
            return context;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }
    /**
     *  定位VO 数据集合    locationJson
     * @return
     */
    public String getLocationJson(){
        try {
            JSONObject contextStr_json=new JSONObject(this.contextStr);
            String context=contextStr_json.getString(OkHttpParam.LOCATION_JSON);
            return context;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }
    /**
     *填单所填写的地段集合   formContext
     * @return
     */
    public String getContext(){
        try {
            JSONObject contextStr_json=new JSONObject(this.contextStr);
            String context=contextStr_json.getString(OkHttpParam.FORM_CONTEXT);
            return context;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     *管线上设施表号集合 (逗号隔开)    facNameList
     * @return
     */
    public String getFacNameList(){
        try {
            JSONObject contextStr_json=new JSONObject(this.contextStr);
            String context=contextStr_json.getString(OkHttpParam.FAC_NAME_LIST);
            return context;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }
    /**
     *获取更新来源    updateSource
     * @return
     */
    public String getUpdateSource(){
        try {
            JSONObject contextStr_json=new JSONObject(this.contextStr);
            String context=contextStr_json.getString(OkHttpParam.UPDATE_SOURCE);
            return context;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     *获取表单名字    formName
     * @return
     */
    public String getFormName(){
        try {
            JSONObject contextStr_json=new JSONObject(this.contextStr);
            String context=contextStr_json.getString(OkHttpParam.FORM_NAME);
            return context;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     *  设施管线通用上传字段 数据集合    FacPipBaseVo
     * @return
     */
    public FacPipBaseVo getFacPipBaseVo(){
        try {
            Gson gson=new Gson();
            JSONObject contextStr_json=new JSONObject(this.contextStr);
            String context=contextStr_json.getString(OkHttpParam.FAC_PIP_BASE_VO);
            if (!context.equals("")){
                FacPipBaseVo facPipBaseVo= gson.fromJson(context,FacPipBaseVo.class);
                return facPipBaseVo;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new FacPipBaseVo();
    }

    /**
     *设置更新来源    updateSource
     * @return
     */
    public void setUpdateSource(String updateSource){
        try {
            JSONObject contextStr_json=new JSONObject(this.contextStr);
            contextStr_json.put(OkHttpParam.UPDATE_SOURCE,updateSource);
            setContextStr(contextStr_json.toString());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     *获取管线的实时长度
     * @return
     */
    public double getLineLenght(){
        double lineLenght=0.0;
        if (getDataType().equals(MapMeterScope.LINE)){
            String [] pointListData= getPointList().split(",");
            if (pointListData.length>1){
                for (int i = 0; i < pointListData.length-1; i++) {
                    String[] pointLngLatStartData = pointListData[i].split(" ");
                    String[] pointLngLatEndData = pointListData[i+1].split(" ");

                    LatLng startLatLng = new LatLng(Double.parseDouble(pointLngLatStartData[1])
                            , Double.parseDouble(pointLngLatStartData[0]));
                    LatLng endLatLng = new LatLng(Double.parseDouble(pointLngLatEndData[1])
                            , Double.parseDouble(pointLngLatEndData[0]));

                    lineLenght+=DistanceUtil.getDistance(startLatLng,endLatLng);
                }
            }
        }
        return lineLenght;
    }

    /**
     *获取经纬度
     * @return
     */
    public LatLng getLatlng(){
        LatLng latLng = null;
        if (getDataType().equals(MapMeterMoveScope.POINT)){
            latLng=new LatLng(Double.parseDouble(getLatitude()),Double.parseDouble(getLongitude()));
        }
        return latLng;
    }

    /**
     *获取管线经纬度集合
     * @return
     */
    public List<LatLng> getLineLatlngList(){
        List<LatLng> latLngList=new ArrayList<>();
        if (getDataType().equals(MapMeterMoveScope.LINE)){
            String[] dataList=getPointList().split(",");
            if (dataList.length>0){
                for (int i = 0; i < dataList.length; i++) {
                    String latlngStr=dataList[i];
                    LatLng latLng=new LatLng(Double.parseDouble(latlngStr.split(" ")[1])
                            ,Double.parseDouble(latlngStr.split(" ")[0]));
                    latLngList.add(latLng);
                }
            }
        }
        return latLngList;
    }


    public String getFacId() {
        return facId == null ? "" : facId;
    }

    public void setFacId(String facId) {
        this.facId = facId;
    }

    public String getFacName() {
        return facName == null ? "" : facName;
    }

    public void setFacName(String facName) {
        this.facName = facName;
    }

    public String getImplementorName() {
        return implementorName == null ? "" : implementorName;
    }

    public void setImplementorName(String implementorName) {
        this.implementorName = implementorName;
    }

    public String getFacType() {
        return facType == null ? "" : facType;
    }

    public void setFacType(String facType) {
        this.facType = facType;
    }

    public String getPipName() {
        return pipName == null ? "" : pipName;
    }

    public void setPipName(String pipName) {
        this.pipName = pipName;
    }

    public String getPipMaterial() {
        return pipMaterial == null ? "" : pipMaterial;
    }

    public void setPipMaterial(String pipMaterial) {
        this.pipMaterial = pipMaterial;
    }

    public String getPipType() {
        return pipType == null ? "" : pipType;
    }

    public void setPipType(String pipType) {
        this.pipType = pipType;
    }

    public String getLayingType() {
        return layingType == null ? "" : layingType;
    }

    public void setLayingType(String layingType) {
        this.layingType = layingType;
    }

    public String getPointList() {
        return pointList == null ? "" : pointList;
    }

    public void setPointList(String pointList) {
        this.pointList = pointList;
    }

    public String getLongitude() {
        return longitude == null ? "" : longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude == null ? "" : latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitudeWg84() {
        return longitudeWg84 == null ? "" : longitudeWg84;
    }

    public void setLongitudeWg84(String longitudeWg84) {
        this.longitudeWg84 = longitudeWg84;
    }

    public String getLatitudeWg84() {
        return latitudeWg84 == null ? "" : latitudeWg84;
    }

    public void setLatitudeWg84(String latitudeWg84) {
        this.latitudeWg84 = latitudeWg84;
    }

    public String getGatherType() {
        return gatherType == null ? "" : gatherType;
    }

    public void setGatherType(String gatherType) {
        this.gatherType = gatherType;
    }

    public String getProjectId() {
        return projectId == null ? "" : projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getShareCode() {
        return shareCode == null ? "" : shareCode;
    }

    public void setShareCode(String shareCode) {
        this.shareCode = shareCode;
    }

    public String getUserId() {
        return userId == null ? "" : userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getGuid() {
        return guid == null ? "" : guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getDataType() {
        return dataType == null ? "" : dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getFacSubmitStatus() {
        return facSubmitStatus == null ? "" : facSubmitStatus;
    }

    public void setFacSubmitStatus(String facSubmitStatus) {
        this.facSubmitStatus = facSubmitStatus;
    }

    public String getContextStr() {
        return contextStr == null ? "" : contextStr;
    }

    public void setContextStr(String contextStr) {
        this.contextStr = contextStr;
    }

    public String getPoints() {
        return points == null ? "" : points;
    }

    public void setPoints(String points) {
        this.points = points;
    }
}
