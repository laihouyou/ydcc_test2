package com.movementinsome.kernel.initial.model;

import com.movementinsome.database.vo.CoordTransformModel;

/**
 * Created by zzc on 2017/10/16.
 */

public class City {
    //记录ID
    private String id;
    //城市名称
    private String cityName;
    //城市id
    private String cityCode;
    //城市七参
    private CoordTransformModel coordTransformModel;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public CoordTransformModel getCoordTransformModel() {
        return coordTransformModel;
    }

    public void setCoordTransformModel(CoordTransformModel coordTransformModel) {
        this.coordTransformModel = coordTransformModel;
    }
}
