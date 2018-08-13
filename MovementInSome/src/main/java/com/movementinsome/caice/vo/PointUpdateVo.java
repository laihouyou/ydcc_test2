package com.movementinsome.caice.vo;

import java.util.List;

/**
 * Created by zzc on 2018/4/8.
 */

public class PointUpdateVo {

    /**
     * code : 0
     * msg : 更新工程数据成功
     * jsonData : {"failNum":0,"successIdList":["e2505d18-af88-4c6c-82c8-4ae118332a5b"]}
     */

    private int code;
    private String msg;
    private JsonDataBean jsonData;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public JsonDataBean getJsonData() {
        return jsonData;
    }

    public void setJsonData(JsonDataBean jsonData) {
        this.jsonData = jsonData;
    }

    public static class JsonDataBean {
        /**
         * failNum : 0
         * successIdList : ["e2505d18-af88-4c6c-82c8-4ae118332a5b"]
         */

        private int failNum;
        private List<String> successIdList;

        public int getFailNum() {
            return failNum;
        }

        public void setFailNum(int failNum) {
            this.failNum = failNum;
        }

        public List<String> getSuccessIdList() {
            return successIdList;
        }

        public void setSuccessIdList(List<String> successIdList) {
            this.successIdList = successIdList;
        }
    }
}
