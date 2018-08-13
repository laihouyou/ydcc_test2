package com.baidu.overlayutil;

import android.os.Bundle;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.Polyline;
import com.baidu.mapapi.map.TextOptions;
import com.baidu.mapapi.model.LatLng;
import com.movementinsome.AppContext;
import com.movementinsome.R;
import com.movementinsome.app.pub.Constant;
import com.movementinsome.caice.util.MapMeterMoveScope;
import com.movementinsome.caice.vo.ProjectVo;
import com.movementinsome.caice.vo.SavePointVo;

import java.util.ArrayList;
import java.util.List;

/**
 * 用于显示poi的overly
 */
public class PoiOverlay extends OverlayManager {

    private List<SavePointVo> savePointVoList=null;
    private ProjectVo projectVo;
    private String acquisitionState;

    /**
     * 构造函数
     * 
     * @param baiduMap
     *            该 PoiOverlay 引用的 BaiduMap 对象
     */
    public PoiOverlay(BaiduMap baiduMap) {
        super(baiduMap);
    }

    /**
     * 设置POI数据
     * 
     * @param poiResult
     * @param poiResult  当前采集模式
     *            设置POI数据
     */
    public void setData(List<SavePointVo> poiResult,String acquisitionState) {
        this.savePointVoList = poiResult;
        this.acquisitionState=acquisitionState;
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
        int markerSize = 0;
        BitmapDescriptor bitmap;
        for (int i = 0; i < savePointVoList.size(); i++) {
            SavePointVo savePointVo=savePointVoList.get(i);
            String longitudeStr=savePointVo.getLongitude();
            String latitudeStr=savePointVo.getLatitude();
            LatLng pointLatlng=null;
            if (longitudeStr!=null&&!longitudeStr.equals("")
                    &&latitudeStr!=null&&!latitudeStr.equals("")){
                pointLatlng=new LatLng(Double.parseDouble(latitudeStr),
                        Double.parseDouble( longitudeStr));
            }
            if (pointLatlng == null) {
                continue;
            }
            String implementorName=savePointVo.getImplementorName();
            if (implementorName != null&&!implementorName.equals("")) {
                switch (implementorName) {
                    case Constant.VALVE:    //阀门
                        //构建Marker图标
                        bitmap = BitmapDescriptorFactory
                                .fromResource(R.drawable.valve_inspection);
                        break;
                    case Constant.MUD_VALVE:    //排泥阀
                        //构建Marker图标
                        bitmap = BitmapDescriptorFactory
                                .fromResource(R.drawable.mud_valve);
                        break;
                    case Constant.VENT_VALVE:    //排气阀
                        //构建Marker图标
                        bitmap = BitmapDescriptorFactory
                                .fromResource(R.drawable.drain_tap);
                        break;
                    case Constant.WATER_METER:    //水表
                        //构建Marker图标
                        bitmap = BitmapDescriptorFactory
                                .fromResource(R.drawable.meter_reading);
                        break;
                    case Constant.FIRE_HYDRANT:    //消防栓
                        //构建Marker图标
                        bitmap = BitmapDescriptorFactory
                                .fromResource(R.drawable.hydrant);
                        break;
                    case Constant.DISCHARGE_OUTLET:    //出水口
                        //构建Marker图标
                        bitmap = BitmapDescriptorFactory
                                .fromResource(R.drawable.water_outlet);
                        break;
                    case Constant.PLUG_SEAL:    //封头堵坂
                        //构建Marker图标
                        bitmap = BitmapDescriptorFactory
                                .fromResource(R.drawable.plug_seal_plate);
                        break;
                    case Constant.NODE_BLACK:    //节点
                        //构建Marker图标
                        bitmap = BitmapDescriptorFactory
                                .fromResource(R.drawable.node);
                    case Constant.POOL:    //水池
                        //构建Marker图标
                        bitmap = BitmapDescriptorFactory
                                .fromResource(R.drawable.pool);
                        break;

                    case Constant.METER_READING_UNDONE:    //水表(未完成)
                        //构建Marker图标
                        bitmap = BitmapDescriptorFactory
                                .fromResource(R.drawable.meter_reading_undone);
                        break;

                    case Constant.METER_READING_COMPLETED:    //水表(已完成)
                        //构建Marker图标
                        bitmap = BitmapDescriptorFactory
                                .fromResource(R.drawable.meter_reading_completed);
                        break;

                    default:
                        //构建Marker图标
                        bitmap = BitmapDescriptorFactory
                                .fromResource(R.drawable.icon_marka_b_yellow);

                        break;
                }
            } else {
                //构建Marker图标
                bitmap = BitmapDescriptorFactory
                        .fromResource(R.drawable.icon_marka_b_yellow);

            }

            markerSize++;
            Bundle bundle = new Bundle();
            bundle.putInt("index", i);
            boolean isDraggable=false;
            if (acquisitionState.equals(MapMeterMoveScope.MOVE)){      //采集模式
                if (projectVo.getProjectId().equals(savePointVoList.get(i).getProjectId())){
                    isDraggable=true;
                }
            }
            markerList.add(new MarkerOptions()
                    .icon(bitmap)
                    .extraInfo(bundle)
                    .title(savePointVoList.get(i).getFacId() + "")
                    //采集模式下，本工程数据可拖拽，共享不可拖拽，查看模式下都不可以拖拽
                    .draggable(isDraggable)
                    .position(pointLatlng));

            markerList.add(new TextOptions()
                    .fontSize(24)
                    .align(TextOptions.ALIGN_CENTER_HORIZONTAL, TextOptions.ALIGN_TOP)
                    .fontColor(projectVo.getProjectId().equals(savePointVo.getProjectId())
                            ? AppContext.getInstance().getResources().getColor(R.color.crimson)
                            : AppContext.getInstance().getResources().getColor(R.color.point_change_line))
                    .text(savePointVoList.get(i).getFacName())
                    .position(pointLatlng));
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
