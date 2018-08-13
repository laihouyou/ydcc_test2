package com.movementinsome.caice.vo;

import java.io.Serializable;

/**
 * Created by zzc on 2017/6/6.
 */

public class StatisticsParentListVo implements Serializable{
    //日期
    public String parentDate;
    //管线长度
    public Double lintLenght;
    //设施点数量
    public int  pointNum;

    public String getParentDate() {
        return parentDate;
    }

    public void setParentDate(String parentDate) {
        this.parentDate = parentDate;
    }

    public Double getLintLenght() {
        return lintLenght;
    }

    public void setLintLenght(Double lintLenght) {
        this.lintLenght = lintLenght;
    }

    public int getPointNum() {
        return pointNum;
    }

    public void setPointNum(int pointNum) {
        this.pointNum = pointNum;
    }
}
