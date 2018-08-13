package com.movementinsome.kernel.initial.model;

import java.util.List;

/**
 * Created by zzc on 2017/6/8.
 */

public class FacilityType {
    private List<Facility> facilities;
    private String name;

    public List<Facility> getFacilities() {
        return facilities;
    }

    public void setFacilities(List<Facility> facilities) {
        this.facilities = facilities;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
