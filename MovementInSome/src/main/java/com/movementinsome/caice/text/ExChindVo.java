package com.movementinsome.caice.text;

import com.baidu.mapapi.model.LatLng;

/**
 * Created by zzc on 2017/10/9.
 */

public class ExChindVo implements MultiItemEntity {
    //id
    private int ctilId;
    //城市名字
    private String ctilName;
    //城市类型
    private int cityType;
    //数据大小
    private long dataSize;

    //下载比率
    private int ratio;
    //下载状态
    private int status;
    //下载状态(字符串)
    private String statusStr;
    //城市中心点坐标
    private LatLng geoPt;
    //已下载数据大小
    private int size;
    //服务端数据大小
    private int serversize;
    //离线包地图层级
    private int level;
    //是否为更新
    private boolean update;

    public int getCtilId() {
        return ctilId;
    }

    public void setCtilId(int ctilId) {
        this.ctilId = ctilId;
    }

    public String getCtilName() {
        return ctilName;
    }

    public void setCtilName(String ctilName) {
        this.ctilName = ctilName;
    }

    public int getCityType() {
        return cityType;
    }

    public void setCityType(int cityType) {
        this.cityType = cityType;
    }

    public long getDataSize() {
        return dataSize;
    }

    public void setDataSize(long dataSize) {
        this.dataSize = dataSize;
    }

    public int getRatio() {
        return ratio;
    }

    public void setRatio(int ratio) {
        this.ratio = ratio;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public LatLng getGeoPt() {
        return geoPt;
    }

    public void setGeoPt(LatLng geoPt) {
        this.geoPt = geoPt;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getServersize() {
        return serversize;
    }

    public void setServersize(int serversize) {
        this.serversize = serversize;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public boolean isUpdate() {
        return update;
    }

    public void setUpdate(boolean update) {
        this.update = update;
    }

    public String getStatusStr() {
        return statusStr;
    }

    public void setStatusStr(String statusStr) {
        this.statusStr = statusStr;
    }

    @Override
    public int getItemType() {
        return 2;
    }
}
