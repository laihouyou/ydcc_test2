package com.movementinsome.kernel.initial.model;

/**
 * Created by LJQ on 2017/6/1.
 */

public class Attribute {
    private String name;
    private String value;

    /**
     *sourceValue   取值来源  json  从json中获取  entity  从实体类中获取
     */
    private String sourceValue;
    private String type;



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getSourceValue() {
        return sourceValue;
    }

    public void setSourceValue(String sourceValue) {
        this.sourceValue = sourceValue;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
