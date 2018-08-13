package com.movementinsome.caice.async;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.Overlay;
import com.baidu.mapapi.map.TextOptions;
import com.baidu.mapapi.model.LatLng;
import com.movementinsome.R;
import com.movementinsome.app.pub.Constant;
import com.movementinsome.caice.okhttp.OkHttpParam;
import com.movementinsome.caice.okhttp.OkHttpRequest;
import com.movementinsome.caice.util.BaiduMapDraw;
import com.movementinsome.caice.util.MapMeterMoveScope;
import com.movementinsome.caice.vo.LinePointVo;
import com.movementinsome.caice.vo.ProjectVo;
import com.movementinsome.caice.vo.SavePointVo;
import com.movementinsome.map.MapViewer;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**在地图上显示所有设施点异步任务
 * Created by zzc on 2017/5/11.
 */

public class ShowMakreTask extends AsyncTask<List<SavePointVo>, String, String>{
    private Context mContext;
    private BitmapDescriptor bitmap;
    private BaiduMap mBaiduMap;
    private MarkerOptions markerOption;
    private Overlay pointOver;
    private List<Overlay> mOverlayList = null;//已完成的点图层
    private TextOptions textOption ;
    private OkHttpRequest okHttpRequest;
    private List<com.baidu.mapapi.map.Polyline> mBaidPolyline;

    private List<String> facName;
    private List<LinePointVo> allLineSort;  //同一条线段下的所有点与序号       排序前

    private ProgressDialog progress;
    private MapViewer mapViewer;

    private ProjectVo projectVo;

    public ShowMakreTask(Context context,BaiduMap baiduMap, MarkerOptions markerOption, Overlay pointOver,
                         List<Overlay> mOverlayList, TextOptions textOption,List<com.baidu.mapapi.map.Polyline> mBaidPolyline
                ,ProjectVo projectVo
    ){
        this.mContext=context;
        this.mapViewer=(MapViewer)context;
        this.mBaiduMap=baiduMap;
        this.markerOption=markerOption;
        this.pointOver=pointOver;
        this.mOverlayList=mOverlayList;
        this.textOption=textOption;
        this.mBaidPolyline=mBaidPolyline;
        this.projectVo = projectVo;
        if (okHttpRequest==null){
            okHttpRequest=new OkHttpRequest((Activity)mContext,baiduMap,mOverlayList);
        }
    }

    @Override
    protected void onPreExecute() {
        if (progress==null){
            progress = new ProgressDialog(mContext);
            progress.setMessage("正在加载数据,请等待……");
            progress.setCancelable(false);
            progress.setCanceledOnTouchOutside(false);
            progress.show();
        }else {
            progress.show();
        }
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(List<SavePointVo>... params) {
        try {
            List<SavePointVo> savePointVoList=params[0];

            LatLng point;
            List<LatLng> pointList = new ArrayList<>();
            List<SavePointVo> allLineLatlogVo = new ArrayList<>();
            List<SavePointVo> lineLatlogVo = new ArrayList<>();//未排序的线集合
            List<SavePointVo> lineLatlogVoSort = new ArrayList<>();//已排序的线集合

            for (int i = 0; i < savePointVoList.size(); i++) {
                   /* getMaker(latlogVo.getPois().get(i).getLocation().get(1),
                            latlogVo.getPois().get(i).getLocation().get(0),i);*/
                if ((savePointVoList.get(i).getDataType() != null &&
                        savePointVoList.get(i).getDataType().equals(MapMeterMoveScope.LINE))) {  //线
                    allLineLatlogVo.add(savePointVoList.get(i));

                } else if (savePointVoList.get(i).getDataType() != null &&
                        savePointVoList.get(i).getDataType().equals(MapMeterMoveScope.LINEPOINT)
                        ) {  //线点   带线的设施点
                    allLineLatlogVo.add(savePointVoList.get(i));
                    //定义Maker坐标点
                    point = new LatLng(Double.parseDouble(savePointVoList.get(i).getLatitude()),
                           Double.parseDouble( savePointVoList.get(i).getLongitude()));
                    if (savePointVoList.get(i).getImplementorName() != null) {
                        switch (savePointVoList.get(i).getImplementorName()) {
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

                    //构建MarkerOption，用于在地图上添加Marker
                    markerOption = new MarkerOptions()
                            .position(point)
                            .title(savePointVoList.get(i).getFacId()+"")
                            .draggable(true)//设置手势拖拽
                            .icon(bitmap);
                    Bundle bundle=new Bundle();
//                    bundle.putString("projectId",savePointVoList.get(i).getFac_num_line());
//                    bundle.putString("drawNum",savePointVoList.get(i).getDraw_num());
                    markerOption.extraInfo(bundle);
                    //在地图上添加Marker，并显示
                    pointOver = mBaiduMap.addOverlay(markerOption);
                    mOverlayList.add(pointOver);

                    //构建文字Option对象，用于在地图上添加文字
                    textOption = new TextOptions()
                            .fontSize(24)
                            .align(TextOptions.ALIGN_CENTER_HORIZONTAL, TextOptions.ALIGN_TOP)
                            .fontColor(0xFFFF00FF)
                            .text(savePointVoList.get(i).getFacName())
                            .position(point);
                    //在地图上添加该文字对象并显示
                    mOverlayList.add(mBaiduMap.addOverlay(textOption));

                } else if (savePointVoList.get(i).getDataType() == null ||
                        savePointVoList.get(i).getDataType().equals(MapMeterMoveScope.POINT)) {     //点   普通设施点
                    //定义Maker坐标点
                    point = new LatLng(Double.parseDouble(savePointVoList.get(i).getLatitude()),
                            Double.parseDouble(savePointVoList.get(i).getLongitude()));
                    if (savePointVoList.get(i).getImplementorName() != null) {
                        switch (savePointVoList.get(i).getImplementorName()) {
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
//                    //构建Marker图标
//                    bitmap = BitmapDescriptorFactory
//                            .fromResource(R.drawable.icon_marka_b_blue);
                    //构建MarkerOption，用于在地图上添加Marker
                    markerOption = new MarkerOptions()
                            .position(point)
                            .title(savePointVoList.get(i).getFacId() + "")
                            .draggable(projectVo.getProjectId().equals(savePointVoList.get(i).getProjectId()))//设置手势拖拽
                            .icon(bitmap);
                    //在地图上添加Marker，并显示
                    pointOver = mBaiduMap.addOverlay(markerOption);
                    mOverlayList.add(pointOver);

                    //构建文字Option对象，用于在地图上添加文字
                    textOption = new TextOptions()
                            .fontSize(24)
                            .align(TextOptions.ALIGN_CENTER_HORIZONTAL, TextOptions.ALIGN_TOP)
                            .fontColor(projectVo.getProjectId().equals(savePointVoList.get(i).getProjectId())
                                    ?mContext.getResources().getColor(R.color.color_ff00ff)
                                    :mContext.getResources().getColor(R.color.point_change_line)
                            )
                            .text(savePointVoList.get(i).getFacName()+"")
                            .position(point);
                    //在地图上添加该文字对象并显示
                    mOverlayList.add(mBaiduMap.addOverlay(textOption));
                }
            }
            okHttpRequest.setmOverlayList(mOverlayList);

            showLine(allLineLatlogVo);      //加载普通线  与点线

        } catch (Exception e) {
            e.printStackTrace();
            progress.dismiss();
        }
        return "1";
    }

    public  void showLine(List<SavePointVo> allLineLatlogVo)  {
        facName = new ArrayList<>(); //装有表号的集合，即有几条线段

        //显示管线新方法
        for (SavePointVo saveLineVo : allLineLatlogVo) {
            try {
                //获取坐标集合
                String pointList = "";
                if (saveLineVo.getPointList() != null) {
                    pointList = saveLineVo.getPointList();
                } else {
                    String context = saveLineVo.getContext();
                    JSONObject jsonObject = new JSONObject(context);
                    pointList = jsonObject.getString(OkHttpParam.POINT_LIST);
                }
                List<LatLng> lineList = new ArrayList<>();
                String[] point = pointList.split(",");
                if (point != null && point.length >= 2) {
                    for (int i = 0; i < point.length; i++) {
                        String latlng = point[i];
                        String[] data = latlng.split(" ");
                        if (data != null && data.length == 2) {
                            LatLng latLng = new LatLng(Double.parseDouble(data[1]), Double.parseDouble(data[0]));
                            lineList.add(latLng);
                        }
                    }
                }


                mOverlayList.add(BaiduMapDraw.drawLine(
                        mBaiduMap,
                        lineList,
                        saveLineVo.getProjectId(),
                        projectVo,
                        mContext
                ));

                mapViewer.setmOverlayList(mOverlayList);

                String drawType = saveLineVo.getDataType();

                List<LatLng> points = lineList;
                if (points.size() >= 2) {
                    for (int i = 1; i < points.size(); i++) {
                        double lon = (points.get(i - 1).longitude + points.get(i).longitude) / 2;
                        double lat = (points.get(i - 1).latitude + points.get(i).latitude) / 2;
                        LatLng latLng = new LatLng(lat, lon);
                        //构建文字Option对象，用于在地图上添加文字
                        if (drawType.equals(MapMeterMoveScope.LINEPOINT)) {
                            textOption = new TextOptions()
                                    .fontSize(25)
                                    .fontColor(projectVo.getProjectId().equals(saveLineVo.getProjectId())
                                            ? mContext.getResources().getColor(R.color.crimson)
                                            : mContext.getResources().getColor(R.color.point_change_line))
                                    .text(saveLineVo.getPipName() == null ? "" : saveLineVo.getPipName())
                                    .position(latLng);
                            //在地图上添加该文字对象并显示
                            mOverlayList.add(mBaiduMap.addOverlay(textOption));
                        } else if (drawType.equals(MapMeterMoveScope.LINE)) {
                            textOption = new TextOptions()
                                    .fontSize(25)
                                    .fontColor(projectVo.getProjectId().equals(saveLineVo.getProjectId())
                                            ? mContext.getResources().getColor(R.color.crimson)
                                            : mContext.getResources().getColor(R.color.point_change_line))
                                    .text(saveLineVo.getPipName() == null ? "" : saveLineVo.getPipName())
                                    .position(latLng);
                            //在地图上添加该文字对象并显示
                            mOverlayList.add(mBaiduMap.addOverlay(textOption));
                        }
                        mapViewer.setmOverlayList(mOverlayList);

                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    protected void onPostExecute(String s) {
        if (s.equals("1")){
            if (progress.isShowing()){
                progress.dismiss();
            }
        }
        super.onPostExecute(s);
    }
}
