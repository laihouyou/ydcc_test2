package com.movementinsome.caice.vo;

import com.amap.api.maps.model.LatLng;

/**
 * Created by zzc on 2017/3/27.
 */


/**
 *
 * <p>
 * ClassName CollectionsSort
 * </p>
 * <p>
 * Description 主要介绍两种集合的排序算法<br/>
 * 第一：java.util.Collections.sort(java.util.List)，要求所排序元素必须实现java.lang.Comparable接口 <br/>
 * 第二：java.util.Collections.sort(java.util.List, java.util.Comparator)，这个方法要求实现java.util.Comparator接口 <br/>
 * 第三：下面的例子使用的是对int型属性排序，对String属性排序可以使用以下方法<br/>
 * public int compareTo(Cat o){return this.getName().compareTo(o.getName(0);}<br/>
 * 第四：compareTo()函数的说明 <br/>
 * 如果 结果;<br/>
 * <0 a<b ;<br/>=
 * ==0 a==b;<br/>
 * >=0 a>b;
 * </p>
 *
 * @author wangxu wangx89@126.com
 *     <p>
 *     Date 2014-9-16 下午04:52:57
 *     </p>
 * @version V1.0
 *
 */
public class LinePointVo {
    public LatLng latLng;//坐标集合
    public String pointNum;//绘制序号
    public String pointId;//每个点ID
    public String facNum;//编号
    public String projectId;//  坐在的工程ID
    public String facNumLines;//点线编号(未拆分)
    public String drawType;//绘制类型

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

    public String getPointNum() {
        return pointNum;
    }

    public void setPointNum(String pointNum) {
        this.pointNum = pointNum;
    }

    public String getPointId() {
        return pointId;
    }

    public void setPointId(String pointId) {
        this.pointId = pointId;
    }

    public String getFacNum() {
        return facNum;
    }

    public void setFacNum(String facNum) {
        this.facNum = facNum;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getDrawType() {
        return drawType;
    }

    public void setDrawType(String drawType) {
        this.drawType = drawType;
    }

    public String getFacNumLines() {
        return facNumLines;
    }

    public void setFacNumLines(String facNumLines) {
        this.facNumLines = facNumLines;
    }
}
