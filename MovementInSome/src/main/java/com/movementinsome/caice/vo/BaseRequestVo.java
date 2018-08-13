package com.movementinsome.caice.vo;

import java.util.List;

/**
 * 工程请求VO
 * Created by zzc on 2017/8/22.
 */

public class BaseRequestVo {


    /**
     * code : 0
     * msg : 获取工程列表成功
     * jsonData : [{"autoNumber":1,"autoNumberLine":1,"taskNum":"","projectType":"电力","userId":"d1eb1913-b531-4566-b5c3-3cd37c35362a","projectName":"工程1","projectCreatedateStr":"2018-03-29 16:33:01","projectEnddateStr":"","projectStatus":"0","projectId":"6dc7cea5-14a7-433a-84f2-b2dfafe21f0f","projectNum":"dgv","projectUpdateddateStr":"","projectSharecode":"","projectSubmitStatus":"","contextStr":"{\"qicanStr\":\"{\\\"cityCode\\\":\\\"257\\\",\\\"cityName\\\":\\\"广州市\\\",\\\"coordinate\\\":\\\"BJ54\\\",\\\"fdx\\\":-2531752.528456,\\\"fdy\\\":-374168.451418,\\\"flattening\\\":0.003352329869,\\\"frotateangle\\\":-0.0048295512,\\\"fscale\\\":0.999946022614282,\\\"id\\\":\\\"aedde57bf1104311864c76f9f19844ed\\\",\\\"pbenchmarklatitude\\\":0.0,\\\"pcentralmeridian\\\":114.0,\\\"pconstantx\\\":0.0,\\\"pconstanty\\\":500000.0,\\\"pprojectionType\\\":3,\\\"pscale\\\":0.0,\\\"sdx\\\":181.508222,\\\"sdy\\\":132.455046,\\\"sdz\\\":49.213417,\\\"semimajor\\\":6378245.0,\\\"sqx\\\":0.442,\\\"sqy\\\":-0.336,\\\"sqz\\\":-5.886,\\\"sscale\\\":7.794586E-6}\",\"userId\":\"d1eb1913-b531-4566-b5c3-3cd37c35362a\",\"userName\":\"007\"}"},{"autoNumber":1,"autoNumberLine":1,"taskNum":"","projectType":"电力","userId":"d1eb1913-b531-4566-b5c3-3cd37c35362a","projectName":"工程2","projectCreatedateStr":"2018-03-29 16:33:16","projectEnddateStr":"","projectStatus":"0","projectId":"118010c1-1715-414e-9d73-266af1d759de","projectNum":"98","projectUpdateddateStr":"","projectSharecode":"","projectSubmitStatus":"","contextStr":"{\"qicanStr\":\"{\\\"cityCode\\\":\\\"257\\\",\\\"cityName\\\":\\\"广州市\\\",\\\"coordinate\\\":\\\"BJ54\\\",\\\"fdx\\\":-2531752.528456,\\\"fdy\\\":-374168.451418,\\\"flattening\\\":0.003352329869,\\\"frotateangle\\\":-0.0048295512,\\\"fscale\\\":0.999946022614282,\\\"id\\\":\\\"aedde57bf1104311864c76f9f19844ed\\\",\\\"pbenchmarklatitude\\\":0.0,\\\"pcentralmeridian\\\":114.0,\\\"pconstantx\\\":0.0,\\\"pconstanty\\\":500000.0,\\\"pprojectionType\\\":3,\\\"pscale\\\":0.0,\\\"sdx\\\":181.508222,\\\"sdy\\\":132.455046,\\\"sdz\\\":49.213417,\\\"semimajor\\\":6378245.0,\\\"sqx\\\":0.442,\\\"sqy\\\":-0.336,\\\"sqz\\\":-5.886,\\\"sscale\\\":7.794586E-6}\",\"userId\":\"d1eb1913-b531-4566-b5c3-3cd37c35362a\",\"userName\":\"007\"}"}]
     */

    private int code;
    private String msg;
    private List<JsonDataBean> jsonData;

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

    public List<JsonDataBean> getJsonData() {
        return jsonData;
    }

    public void setJsonData(List<JsonDataBean> jsonData) {
        this.jsonData = jsonData;
    }

    public static class JsonDataBean {
        /**
         * autoNumber : 1
         * autoNumberLine : 1
         * taskNum :
         * projectType : 电力
         * userId : d1eb1913-b531-4566-b5c3-3cd37c35362a
         * projectName : 工程1
         * projectCreatedateStr : 2018-03-29 16:33:01
         * projectEnddateStr :
         * projectStatus : 0
         * projectId : 6dc7cea5-14a7-433a-84f2-b2dfafe21f0f
         * projectNum : dgv
         * projectUpdateddateStr :
         * projectSharecode :
         * projectSubmitStatus :
         * contextStr : {"qicanStr":"{\"cityCode\":\"257\",\"cityName\":\"广州市\",\"coordinate\":\"BJ54\",\"fdx\":-2531752.528456,\"fdy\":-374168.451418,\"flattening\":0.003352329869,\"frotateangle\":-0.0048295512,\"fscale\":0.999946022614282,\"id\":\"aedde57bf1104311864c76f9f19844ed\",\"pbenchmarklatitude\":0.0,\"pcentralmeridian\":114.0,\"pconstantx\":0.0,\"pconstanty\":500000.0,\"pprojectionType\":3,\"pscale\":0.0,\"sdx\":181.508222,\"sdy\":132.455046,\"sdz\":49.213417,\"semimajor\":6378245.0,\"sqx\":0.442,\"sqy\":-0.336,\"sqz\":-5.886,\"sscale\":7.794586E-6}","userId":"d1eb1913-b531-4566-b5c3-3cd37c35362a","userName":"007"}
         */

        private int autoNumber;
        private int autoNumberLine;
        private String taskNum;
        private String projectType;
        private String userId;
        private String projectName;
        private String projectCreatedateStr;
        private String projectEnddateStr;
        private String projectStatus;
        private String projectId;
        private String projectNum;
        private String projectUpdateddateStr;
        private String projectShareCode;
        private String projectSubmitStatus;
        private String contextStr;

        public int getAutoNumber() {
            return autoNumber;
        }

        public void setAutoNumber(int autoNumber) {
            this.autoNumber = autoNumber;
        }

        public int getAutoNumberLine() {
            return autoNumberLine;
        }

        public void setAutoNumberLine(int autoNumberLine) {
            this.autoNumberLine = autoNumberLine;
        }

        public String getTaskNum() {
            return taskNum;
        }

        public void setTaskNum(String taskNum) {
            this.taskNum = taskNum;
        }

        public String getProjectType() {
            return projectType;
        }

        public void setProjectType(String projectType) {
            this.projectType = projectType;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getProjectName() {
            return projectName;
        }

        public void setProjectName(String projectName) {
            this.projectName = projectName;
        }

        public String getProjectCreatedateStr() {
            return projectCreatedateStr;
        }

        public void setProjectCreatedateStr(String projectCreatedateStr) {
            this.projectCreatedateStr = projectCreatedateStr;
        }

        public String getProjectEnddateStr() {
            return projectEnddateStr;
        }

        public void setProjectEnddateStr(String projectEnddateStr) {
            this.projectEnddateStr = projectEnddateStr;
        }

        public String getProjectStatus() {
            return projectStatus;
        }

        public void setProjectStatus(String projectStatus) {
            this.projectStatus = projectStatus;
        }

        public String getProjectId() {
            return projectId;
        }

        public void setProjectId(String projectId) {
            this.projectId = projectId;
        }

        public String getProjectNum() {
            return projectNum;
        }

        public void setProjectNum(String projectNum) {
            this.projectNum = projectNum;
        }

        public String getProjectUpdateddateStr() {
            return projectUpdateddateStr;
        }

        public void setProjectUpdateddateStr(String projectUpdateddateStr) {
            this.projectUpdateddateStr = projectUpdateddateStr;
        }

        public String getProjectShareCode() {
            return projectShareCode;
        }

        public void setProjectShareCode(String projectShareCode) {
            this.projectShareCode = projectShareCode;
        }

        public String getProjectSubmitStatus() {
            return projectSubmitStatus;
        }

        public void setProjectSubmitStatus(String projectSubmitStatus) {
            this.projectSubmitStatus = projectSubmitStatus;
        }

        public String getContextStr() {
            return contextStr;
        }

        public void setContextStr(String contextStr) {
            this.contextStr = contextStr;
        }
    }
}
