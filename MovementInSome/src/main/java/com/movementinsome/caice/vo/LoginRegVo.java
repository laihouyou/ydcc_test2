package com.movementinsome.caice.vo;

/**
 * Created by zzc on 2017/12/27.
 */

public class LoginRegVo {
    private String usUsercode;
    private String usNameZh;
    private String usPassword;
    private String usPhone;
    private String usEmail;
    private String usSex;
    private String mdn; //设备码
    private String captcha;
    private String appType;     //所属项目  0  移动采测, 1  移动探漏

    public String getUsUsercode() {
        return usUsercode;
    }

    public void setUsUsercode(String usUsercode) {
        this.usUsercode = usUsercode;
    }

    public String getUsNameZh() {
        return usNameZh;
    }

    public void setUsNameZh(String usNameZh) {
        this.usNameZh = usNameZh;
    }

    public String getUsPassword() {
        return usPassword;
    }

    public void setUsPassword(String usPassword) {
        this.usPassword = usPassword;
    }

    public String getUsPhone() {
        return usPhone;
    }

    public void setUsPhone(String usPhone) {
        this.usPhone = usPhone;
    }

    public String getUsEmail() {
        return usEmail;
    }

    public void setUsEmail(String usEmail) {
        this.usEmail = usEmail;
    }

    public String getUsSex() {
        return usSex;
    }

    public void setUsSex(String usSex) {
        this.usSex = usSex;
    }

    public String getMdn() {
        return mdn;
    }

    public void setMdn(String mdn) {
        this.mdn = mdn;
    }

    public String getCaptcha() {
        return captcha;
    }

    public void setCaptcha(String captcha) {
        this.captcha = captcha;
    }

    public String getAppType() {
        return appType;
    }

    public void setAppType(String appType) {
        this.appType = appType;
    }
}
