package com.movementinsome.caice.vo;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zzc on 2017/2/23.
 */

public class MarkerDetailsVO implements Serializable {


    /**
     * status : 0
     * poi : {"title":"zgp","location":[113.32839056123,23.093996558341],"city":"广州市","create_time":"2017-05-20 14:45:59","geotable_id":166492,"address":"海珠区敦和路189号-、191号","province":"广东省","district":"海珠区","savePointVoId":"1495262759244","projectName":"工程","projectId":"1495261774918","shareCode":"Y","isProjectShare":"0","fac_num":"GYKJ1","gcj_location":[113.32183864857,23.088192458293],"city_id":257,"id":2113734536}
     * message : 成功
     */

    private int status;
    private PoiBean poi;
    private String message;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public PoiBean getPoi() {
        return poi;
    }

    public void setPoi(PoiBean poi) {
        this.poi = poi;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static class PoiBean {
        /**
         * title : zgp
         * location : [113.32839056123,23.093996558341]
         * city : 广州市
         * create_time : 2017-05-20 14:45:59
         * geotable_id : 166492
         * address : 海珠区敦和路189号-、191号
         * province : 广东省
         * district : 海珠区
         * savePointVoId : 1495262759244
         * projectName : 工程
         * projectId : 1495261774918
         * shareCode : Y
         * isProjectShare : 0
         * fac_num : GYKJ1
         * gcj_location : [113.32183864857,23.088192458293]
         * city_id : 257
         * id : 2113734536
         */

        private String title;
        private String city;
        private String create_time;
        private int geotable_id;
        private String address;
        private String province;
        private String district;
        private String savePointVoId;
        private String projectName;
        private String projectId;
        private String shareCode;
        private String isProjectShare;
        private String fac_num;
        private String draw_type;
        private int city_id;
        private Long id;
        private List<Double> location;
        private List<Double> gcj_location;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getCreate_time() {
            return create_time;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
        }

        public int getGeotable_id() {
            return geotable_id;
        }

        public void setGeotable_id(int geotable_id) {
            this.geotable_id = geotable_id;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getProvince() {
            return province;
        }

        public void setProvince(String province) {
            this.province = province;
        }

        public String getDistrict() {
            return district;
        }

        public void setDistrict(String district) {
            this.district = district;
        }

        public String getSavePointVoId() {
            return savePointVoId;
        }

        public void setSavePointVoId(String savePointVoId) {
            this.savePointVoId = savePointVoId;
        }

        public String getProjectName() {
            return projectName;
        }

        public void setProjectName(String projectName) {
            this.projectName = projectName;
        }

        public String getProjectId() {
            return projectId;
        }

        public void setProjectId(String projectId) {
            this.projectId = projectId;
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

        public String getFac_num() {
            return fac_num;
        }

        public void setFac_num(String fac_num) {
            this.fac_num = fac_num;
        }

        public int getCity_id() {
            return city_id;
        }

        public void setCity_id(int city_id) {
            this.city_id = city_id;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public List<Double> getLocation() {
            return location;
        }

        public void setLocation(List<Double> location) {
            this.location = location;
        }

        public List<Double> getGcj_location() {
            return gcj_location;
        }

        public void setGcj_location(List<Double> gcj_location) {
            this.gcj_location = gcj_location;
        }

        public String getDraw_type() {
            return draw_type;
        }

        public void setDraw_type(String draw_type) {
            this.draw_type = draw_type;
        }
    }
}
