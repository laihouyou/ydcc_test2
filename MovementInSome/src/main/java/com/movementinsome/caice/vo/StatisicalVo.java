package com.movementinsome.caice.vo;

/**
 * Created by lhy on 2018/5/3.
 */

public class StatisicalVo extends ProjectVo {
    //管线长度
    public Double lintLenght;
    //设施点数量
    public int  pointNum;



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
