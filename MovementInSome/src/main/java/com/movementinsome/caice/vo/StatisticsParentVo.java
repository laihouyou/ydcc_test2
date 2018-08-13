package com.movementinsome.caice.vo;

import java.io.Serializable;
import java.util.List;

/**每一个工程的父数据集合
 * Created by zzc on 2017/6/5.
 */

public class StatisticsParentVo implements Serializable{
    //日期
    public List<String> parentDates;
    //管线长度
    public List<Double> lintLenghts;
    //设施点数量
    public List<Integer> pointNums;

    public List<String> getParentDates() {
        return parentDates;
    }

    public void setParentDates(List<String> parentDates) {
        this.parentDates = parentDates;
    }

    public List<Double> getLintLenghts() {
        return lintLenghts;
    }

    public void setLintLenghts(List<Double> lintLenghts) {
        this.lintLenghts = lintLenghts;
    }

    public List<Integer> getPointNums() {
        return pointNums;
    }

    public void setPointNums(List<Integer> pointNums) {
        this.pointNums = pointNums;
    }
}
