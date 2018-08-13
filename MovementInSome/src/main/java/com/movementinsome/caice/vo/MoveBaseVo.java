package com.movementinsome.caice.vo;

import com.movementinsome.AppContext;

/**
 * Created by zzc on 2018/3/20.
 */

public class MoveBaseVo {

    public MoveBaseVo(){

    }

    public MoveBaseVo(String qicanStr){
        this.setUserId(AppContext.getInstance().getCurUser().getUserId());
        this.setUserName(AppContext.getInstance().getCurUserName());
        this.setQicanStr(qicanStr);
    }

    public MoveBaseVo(String userId,String userName,String qicanStr){
        this.setUserId(userId);
        this.setUserName(userName);
        this.setQicanStr(qicanStr);
    }

    // 用户名
    private String userName;

    // 用户名Id
    private String userId;

    // 七参数据
    private String qicanStr;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getQicanStr() {
        return qicanStr;
    }

    public void setQicanStr(String qicanStr) {
        this.qicanStr = qicanStr;
    }
}
