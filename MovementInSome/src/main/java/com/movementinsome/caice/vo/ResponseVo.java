package com.movementinsome.caice.vo;

/**
 * Created by zzc on 2018/1/4.
 */

public class ResponseVo {

    /**
     * state : 1
     * message : 验证码已发送至gddstydce@163.com，请登录邮箱查收
     */

    private int state;
    private String message;

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
