package com.movementinsome.caice.vo;

import java.util.List;

/**
 * Created by zzc on 2017/2/23.
 */

public class LatlogVo {


    /**
     * status : 0
     * size : 3
     * total : 3
     * pois : [{"address":"海珠区敦和路189号-、191号","city":"广州市","create_time":"2017-05-11 17:07:32","district":"海珠区","draw_num":"0","draw_type":"point","fac_num":"E","fac_type":"阀门","geotable_id":166492,"isProjectShare":"1","location":[113.32719581491,23.093955004212],"modify_time":"2017-05-11 17:19:41","province":"广东省","shareCode":"AAAA","tags":"工程1","title":"testhy","gcj_location":[113.32064746454,23.088133241873],"city_id":257,"id":2098602940},{"address":"海珠区敦和路189号-、191号","city":"广州市","create_time":"2017-05-11 17:07:25","district":"海珠区","fac_num":"W","fac_num_line":"AD","geotable_id":166492,"location":[113.32690835714,23.09507696113],"modify_time":"2017-05-11 17:20:03","province":"广东省","title":"testhy","gcj_location":[113.32036184163,23.089251036141],"city_id":257,"id":2098602844},{"title":"testhy","location":[113.32598310247,23.095949587737],"city":"广州市","create_time":"2017-05-11 17:07:19","geotable_id":166492,"address":"海珠区敦和路189号-、191号","province":"广东省","district":"海珠区","fac_num":"Q","gcj_location":[113.3194402601,23.09010953171],"city_id":257,"id":2098602765}]
     * message : 成功
     */

    private int status;
    private int size;
    private int total;
    private String message;
    private List<PoisBean> pois;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<PoisBean> getPois() {
        return pois;
    }

    public void setPois(List<PoisBean> pois) {
        this.pois = pois;
    }

    public static class PoisBean {
        /**
         * address : 海珠区敦和路189号-、191号
         * city : 广州市
         * create_time : 2017-05-11 17:07:32
         * district : 海珠区
         * draw_num : 0
         * draw_type : point
         * fac_num : E
         * fac_type : 阀门
         * geotable_id : 166492
         * isProjectShare : 1
         * location : [113.32719581491,23.093955004212]
         * modify_time : 2017-05-11 17:19:41
         * province : 广东省
         * shareCode : AAAA
         * tags : 工程1
         * title : testhy
         * gcj_location : [113.32064746454,23.088133241873]
         * city_id : 257
         * id : 2098602940
         * fac_num_line : AD
         */

        private String address;
        private String city;
        private String create_time;
        private String district;
        private String draw_num;
        private String draw_type;
        private String fac_num;
        private String fac_type;
        private int geotable_id;
        private String isProjectShare;
        private String modify_time;
        private String province;
        private String projectId;
        private String shareCode;
        private String projectName;
        private String tags;
        private String title;
        private String savePointVoId;
        private int city_id;
        private Long id;
        private String fac_num_line;
        private List<Double> location;
        private List<Double> gcj_location;

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

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
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

        public String getDistrict() {
            return district;
        }

        public void setDistrict(String district) {
            this.district = district;
        }

        public String getDraw_num() {
            return draw_num;
        }

        public void setDraw_num(String draw_num) {
            this.draw_num = draw_num;
        }

        public String getDraw_type() {
            return draw_type;
        }

        public void setDraw_type(String draw_type) {
            this.draw_type = draw_type;
        }

        public String getFac_num() {
            return fac_num;
        }

        public void setFac_num(String fac_num) {
            this.fac_num = fac_num;
        }

        public String getFac_type() {
            return fac_type;
        }

        public void setFac_type(String fac_type) {
            this.fac_type = fac_type;
        }

        public int getGeotable_id() {
            return geotable_id;
        }

        public void setGeotable_id(int geotable_id) {
            this.geotable_id = geotable_id;
        }

        public String getIsProjectShare() {
            return isProjectShare;
        }

        public void setIsProjectShare(String isProjectShare) {
            this.isProjectShare = isProjectShare;
        }

        public String getModify_time() {
            return modify_time;
        }

        public void setModify_time(String modify_time) {
            this.modify_time = modify_time;
        }

        public String getProvince() {
            return province;
        }

        public void setProvince(String province) {
            this.province = province;
        }

        public String getShareCode() {
            return shareCode;
        }

        public void setShareCode(String shareCode) {
            this.shareCode = shareCode;
        }

        public String getTags() {
            return tags;
        }

        public void setTags(String tags) {
            this.tags = tags;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
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

        public String getFac_num_line() {
            return fac_num_line;
        }

        public void setFac_num_line(String fac_num_line) {
            this.fac_num_line = fac_num_line;
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

        public String getSavePointVoId() {
            return savePointVoId;
        }

        public void setSavePointVoId(String savePointVoId) {
            this.savePointVoId = savePointVoId;
        }
    }
}
