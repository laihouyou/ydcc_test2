package com.baidu.overlayutil;

import android.os.Bundle;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.Polyline;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.map.TextOptions;
import com.baidu.mapapi.model.LatLng;
import com.movementinsome.AppContext;
import com.movementinsome.R;
import com.movementinsome.caice.okhttp.OkHttpParam;
import com.movementinsome.caice.vo.ProjectVo;
import com.movementinsome.caice.vo.SavePointVo;

import java.util.ArrayList;
import java.util.List;

/**
 * 用于显示line的overly
 */
public class LineOverlay extends OverlayManager {

    private List<SavePointVo> savePointVoList=null;
    private ProjectVo projectVo;

    /**
     * 构造函数
     *
     * @param baiduMap
     *            该 PoiOverlay 引用的 BaiduMap 对象
     */
    public LineOverlay(BaiduMap baiduMap) {
        super(baiduMap);
    }

    /**
     * 设置POI数据
     *
     * @param poiResult
     *            设置POI数据
     */
    public void setData(List<SavePointVo> poiResult) {
        this.savePointVoList = poiResult;
    }

    /**
     * 设置POI数据
     *
     *            设置POI数据
     */
    public void setProjectVo(ProjectVo projectVo) {
        this.projectVo = projectVo;
    }

    @Override
    public final List<OverlayOptions> getOverlayOptions() {
        if (savePointVoList == null ) {
            return null;
        }
        List<OverlayOptions> markerList = new ArrayList<OverlayOptions>();
        BitmapDescriptor bitmap;
        for (int i = 0; i < savePointVoList.size(); i++) {
            try {
                SavePointVo savePointVo = savePointVoList.get(i);
                //获取坐标集合
                String pointList = "";
                if (savePointVo.getPointList() != null) {
                    pointList = savePointVo.getPointList();
                }

                List<LatLng> lineList = new ArrayList<>();
                String[] point = pointList.split(",");
                if (point != null && point.length >= 2) {
                    for (int j = 0; j < point.length; j++) {
                        String latlng = point[j];
                        String[] data = latlng.split(" ");
                        if (data != null && data.length == 2) {
                            LatLng latLng = new LatLng(Double.parseDouble(data[1]), Double.parseDouble(data[0]));
                            lineList.add(latLng);
                        }
                    }
                }

                Bundle bundle = new Bundle();
                bundle.putString(OkHttpParam.PROJECT_ID,savePointVo.getProjectId());
                bundle.putString(OkHttpParam.FAC_ID,savePointVo.getFacId());
                bundle.putSerializable(OkHttpParam.SAVEPOINTVO,savePointVo);
                BitmapDescriptor mBlueTexture = BitmapDescriptorFactory.fromAsset("icon_road_blue_arrow.png");
                BitmapDescriptor mGreenTexture = BitmapDescriptorFactory.fromAsset("icon_road_green_arrow.png");
                markerList.add(new PolylineOptions()
                        .points(lineList)
                        .customTexture(projectVo.getProjectId().equals(savePointVo.getProjectId())
                                ? mBlueTexture
                                :mGreenTexture)
                        .width(10)
                        .dottedLine(true)
                        .extraInfo(bundle)
                );

                List<LatLng> points = lineList;
                if (points.size() >= 2) {
                    for (int k = 1; k < points.size(); k++) {
                        double lon = (points.get(k - 1).longitude + points.get(k).longitude) / 2;
                        double lat = (points.get(k - 1).latitude + points.get(k).latitude) / 2;
                        LatLng latLng = new LatLng(lat, lon);
                       markerList.add(new TextOptions()
                                .fontSize(25)
                                .fontColor(projectVo.getProjectId().equals(savePointVo.getProjectId())
                                        ? AppContext.getInstance().getResources().getColor(R.color.crimson)
                                        : AppContext.getInstance().getResources().getColor(R.color.point_change_line))
                                .text(savePointVo.getPipName() == null ? "" : savePointVo.getPipName())
                                .position(latLng)
                       );
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return markerList;
    }

    /**
     * 获取该 PoiOverlay 的 poi数据
     * 
     * @return
     */
    public List<SavePointVo> getSavePointVoList() {
        return savePointVoList;
    }

    /**
     * 覆写此方法以改变默认点击行为
     * 
     * @param i
     *            被点击的poi在
     *            {@link com.baidu.mapapi.search.poi.PoiResult#getAllPoi()} 中的索引
     * @return
     */
    public boolean onPoiClick(int i) {
//        if (savePointVoList.getAllPoi() != null
//                && savePointVoList.getAllPoi().get(i) != null) {
//            Toast.makeText(BMapManager.getInstance().getContext(),
//                    savePointVoList.getAllPoi().get(i).name, Toast.LENGTH_LONG)
//                    .show();
//        }
        return false;
    }

    @Override
    public final boolean onMarkerClick(Marker marker) {
        if (!mOverlayList.contains(marker)) {
            return false;
        }
        if (marker.getExtraInfo() != null) {
            return onPoiClick(marker.getExtraInfo().getInt("index"));
        }
        return false;
    }

    @Override
    public boolean onPolylineClick(Polyline polyline) {
        // TODO Auto-generated method stub
        return false;
    }
}
