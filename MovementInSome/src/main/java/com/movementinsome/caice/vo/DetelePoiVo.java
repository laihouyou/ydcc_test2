package com.movementinsome.caice.vo;

import java.io.Serializable;

/**
 * Created by zzc on 2017/8/30.
 */

public class DetelePoiVo implements Serializable{

    /**
     * {"code":-1,"msg":"设施ID为空,创建工程数据失败","jsonData":null}
     */
    private String msg;
    private String jsonData;
    private int code;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getJsonData() {
        return jsonData;
    }

    public void setJsonData(String jsonData) {
        this.jsonData = jsonData;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
