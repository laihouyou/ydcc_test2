package com.movementinsome.caice.text;

/**
 * Created by zzc on 2017/10/9.
 */

public class ExGroupVo extends AbstractExpandableItem<ExChindVo> implements MultiItemEntity{
    //省id
    private int  provinceId;
    //省名字
    private String  provinceName;
    //省id
    private int  provinceType;
    //省id
    private long  dataSize;

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public int getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(int provinceId) {
        this.provinceId = provinceId;
    }

    public int getProvinceType() {
        return provinceType;
    }

    public void setProvinceType(int provinceType) {
        this.provinceType = provinceType;
    }

    public long getDataSize() {
        return dataSize;
    }

    public void setDataSize(long dataSize) {
        this.dataSize = dataSize;
    }

    @Override
    public int getItemType() {
        return 1;
    }

    @Override
    public int getLevel() {
        return 1;
    }
}
