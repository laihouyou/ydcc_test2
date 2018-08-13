package com.movementinsome.caice.vo;

import com.j256.ormlite.field.DatabaseField;

/**
 * Created by zzc on 2018/3/22.
 */

public class FacilityVo {
    //主键ID
    @DatabaseField(id = true)
    private String id;

    //设施点号   （地图展现的物探点号）   (精简物探点号)   JPAA100    其中AA为任务编号，手动填写的    100为顺序号
    @DatabaseField
    private String facName;

    //设施名字
    @DatabaseField
    private String implementorName;

    //设施类型
    @DatabaseField
    private String facType;

    //管线编号
    @DatabaseField
    private String pipName;

    //管线材质
    @DatabaseField
    private String pipMaterial;

    //管线类型
    @DatabaseField
    private String pipType;

    //管线敷设类型
    @DatabaseField
    private String layingType;

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

    //管线坐标集合
    @DatabaseField
    private String pointList;

    //采集类型
    @DatabaseField
    private String gatherType;

    // 提交状态
    //0为未提交
    //1为已提交
    //  对应之前 isPresent 字段
    @DatabaseField
    private String facSubmitStatus;

    //工程ID
    @DatabaseField
    private String projectId;

    //共享码
    @DatabaseField
    private String shareCode;

    //上传人id
    @DatabaseField
    private String userId;

    //原来数据库VO 的关联附件唯一ID
    @DatabaseField
    private String guid;

    //数据类型  point 或者line
    @DatabaseField
    private String dataType;

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
     * 是否是连续采集      isSuccession
     * 填单所填写的地段集合   formContext
     * 定位VO 数据集合        locationJson
     */
    @DatabaseField
    private String contextStr;
}
