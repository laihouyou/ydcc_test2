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
import com.movementinsome.caice.util.CreateFiles;
import com.movementinsome.caice.util.MapMeterMoveScope;
import com.movementinsome.caice.vo.LinePointVo;
import com.movementinsome.caice.vo.MiningSurveyVO;
import com.movementinsome.caice.vo.SavePointVo;
import com.movementinsome.map.MapViewer;

import java.util.ArrayList;
import java.util.List;

/**
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

    private List<List<String>> pointIdSegment;
    private List<List<String>> drawTypesSeg;
    private List<String> facName;
    private List<LinePointVo> allLineSort;  //同一条线段下的所有点与序号       排序前

    private ProgressDialog progress;
    private MapViewer mapViewer;

    private MiningSurveyVO miningSurveyVO;

    public ShowMakreTask(Context context,BaiduMap baiduMap, MarkerOptions markerOption, Overlay pointOver,
                         List<Overlay> mOverlayList, TextOptions textOption,List<com.baidu.mapapi.map.Polyline> mBaidPolyline
                ,MiningSurveyVO miningSurveyVO
    ){
        this.mContext=context;
        this.mapViewer=(MapViewer)context;
        this.mBaiduMap=baiduMap;
        this.markerOption=markerOption;
        this.pointOver=pointOver;
        this.mOverlayList=mOverlayList;
        this.textOption=textOption;
        this.mBaidPolyline=mBaidPolyline;
        this.miningSurveyVO=miningSurveyVO;
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
                            .title(savePointVoList.get(i).getId()+"")
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
                    if (savePointVoList.get(i).getType().equals(OkHttpParam.PATROL)){       //只加载探漏点,不加载确定点
                        //定义Maker坐标点
                        point = new LatLng(Double.parseDouble(savePointVoList.get(i).getLatitude()),
                                Double.parseDouble(savePointVoList.get(i).getLongitude()));

                        String leak=savePointVoList.get(i).getIsLeak();

                        if (leak!=null&&leak.equals("是")){
                            //构建Marker图标
                            bitmap = BitmapDescriptorFactory
                                    .fromResource(R.drawable.icon_marka_b);
                        }else if (leak==null||leak.equals("")){
                            //构建Marker图标
                            bitmap = BitmapDescriptorFactory
                                    .fromResource(R.drawable.icon_marka_b_yellow);
                        }

//                        if (savePointVoList.get(i).getImplementorName() != null) {
//                            switch (savePointVoList.get(i).getImplementorName()) {
//                                case Constant.VALVE:    //阀门
//                                    //构建Marker图标
//                                    bitmap = BitmapDescriptorFactory
//                                            .fromResource(R.drawable.valve_inspection);
//                                    break;
//                                case Constant.MUD_VALVE:    //排泥阀
//                                    //构建Marker图标
//                                    bitmap = BitmapDescriptorFactory
//                                            .fromResource(R.drawable.mud_valve);
//                                    break;
//                                case Constant.VENT_VALVE:    //排气阀
//                                    //构建Marker图标
//                                    bitmap = BitmapDescriptorFactory
//                                            .fromResource(R.drawable.drain_tap);
//                                    break;
//                                case Constant.WATER_METER:    //水表
//                                    //构建Marker图标
//                                    bitmap = BitmapDescriptorFactory
//                                            .fromResource(R.drawable.meter_reading);
//                                    break;
//                                case Constant.FIRE_HYDRANT:    //消防栓
//                                    //构建Marker图标
//                                    bitmap = BitmapDescriptorFactory
//                                            .fromResource(R.drawable.hydrant);
//                                    break;
//                                case Constant.DISCHARGE_OUTLET:    //出水口
//                                    //构建Marker图标
//                                    bitmap = BitmapDescriptorFactory
//                                            .fromResource(R.drawable.water_outlet);
//                                    break;
//                                case Constant.PLUG_SEAL:    //封头堵坂
//                                    //构建Marker图标
//                                    bitmap = BitmapDescriptorFactory
//                                            .fromResource(R.drawable.plug_seal_plate);
//                                    break;
//                                case Constant.NODE_BLACK:    //节点
//                                    //构建Marker图标
//                                    bitmap = BitmapDescriptorFactory
//                                            .fromResource(R.drawable.node);
//                                    break;
//                                default:
//                                    //构建Marker图标
//                                    bitmap = BitmapDescriptorFactory
//                                            .fromResource(R.drawable.icon_marka_b_yellow);
//
//                                    break;
//                            }
//                        } else {
//                            //构建Marker图标
//                            bitmap = BitmapDescriptorFactory
//                                    .fromResource(R.drawable.icon_marka_b_yellow);
//                        }
//                    //构建Marker图标
//                    bitmap = BitmapDescriptorFactory
//                            .fromResource(R.drawable.icon_marka_b_blue);
                        //构建MarkerOption，用于在地图上添加Marker
                        markerOption = new MarkerOptions()
                                .position(point)
                                .title(savePointVoList.get(i).getId() + "")
                                .draggable(miningSurveyVO.getProjectId().equals(savePointVoList.get(i).getProjectId()))//设置手势拖拽
                                .icon(bitmap);
                        //在地图上添加Marker，并显示
                        pointOver = mBaiduMap.addOverlay(markerOption);
                        mOverlayList.add(pointOver);

                        //构建文字Option对象，用于在地图上添加文字
                        textOption = new TextOptions()
                                .fontSize(24)
                                .align(TextOptions.ALIGN_CENTER_HORIZONTAL, TextOptions.ALIGN_TOP)
                                .fontColor(miningSurveyVO.getProjectId().equals(savePointVoList.get(i).getProjectId())
                                        ?mContext.getResources().getColor(R.color.color_ff00ff)
                                        :mContext.getResources().getColor(R.color.point_change_line)
                                )
                                .text(savePointVoList.get(i).getFacName()+"")
                                .position(point);
                        //在地图上添加该文字对象并显示
                        mOverlayList.add(mBaiduMap.addOverlay(textOption));
                    }
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

    public  void showLine(List<SavePointVo> allLineLatlogVo) {
        facName = new ArrayList<>(); //装有表号的集合，即有几条线段

        for (int q = 0; q < allLineLatlogVo.size(); q++) {
            boolean isPd = true;
            boolean isPdLine = true;
            if (q == 0) {
                if (allLineLatlogVo.get(q).getDataType().equals(MapMeterMoveScope.LINEPOINT)) {
//                    String [] facNunLiew=allLineLatlogVo.get(q).getFac_num_line().split(",");
//                    if (facNunLiew.length>0){
//                        for (String facNumline:facNunLiew){
//                            facName.add(facNumline);
//                        }
//                    }

                } else if (allLineLatlogVo.get(q).getDataType().equals(MapMeterMoveScope.LINE)) {
                    facName.add(allLineLatlogVo.get(q).getPipName());
                }
            } else {
                for (int w = 0; w < facName.size(); w++) {
                    if (allLineLatlogVo.get(q).getDataType().equals(MapMeterMoveScope.LINE)) {
                        if (allLineLatlogVo.get(q).getPipName().equals(facName.get(w))) {
                            isPd = false;
                        }
                    }
                    if (allLineLatlogVo.get(q).getDataType().equals(MapMeterMoveScope.LINEPOINT)) {
//                        String [] facNunLiew=allLineLatlogVo.get(q).getFac_num_line().split(",");
//                        if (facNunLiew.length>0){
//                            for (String facNumline:facNunLiew){
//                                if (facNumline.equals(facName.get(w))){
//                                    isPdLine=false;
//                                    break;
//                                }
//                                if (isPdLine){
//                                    facName.add(facNumline);
//                                }
//                            }
//                        }
                    }
                }
                if (isPd) {
                    if (allLineLatlogVo.get(q).getDataType().equals(MapMeterMoveScope.LINE)) {
                        facName.add(allLineLatlogVo.get(q).getPipName());

                    }
                }
            }
        }


        //集合去重
        facName= CreateFiles.removeDuplicateWithList(facName);

        showPointLinePoint(allLineLatlogVo);


    }

    private void showPointLinePoint(List<SavePointVo> allLineLatlogVo) {
        //对点进行按线段分类
        List<List<LatLng>> lineSegment = new ArrayList<>();
        List<List<String>> facNamesSeg = new ArrayList<>();
        drawTypesSeg = new ArrayList<>();
        List<List<String>> facNumLinesSeg = new ArrayList<>();
        List<List<String>> facNumLinessSeg = new ArrayList<>();
        List<List<String>> facDrawNum = new ArrayList<>();
        List<List<String>> facDrawNums = new ArrayList<>();
        pointIdSegment = new ArrayList<>();
        List<LatLng> allLineSortPX;  //同一条线段下的所有点与序号   排序后的
        List<String> pointId = null;//点ID集合
        List<String> facNames = null;//表号集合
        List<String> drawTypes = null;//点ID集合
        List<String> drawNums = null;//点ID集合
        List<String> drawNumss = null;//点ID集合
        List<String> projectIds = null;//点ID集合
        List<String> facNumLiness = null;//点ID集合
        LinePointVo linePointVo;
        for (int e = 0; e < facName.size(); e++) {
            allLineSort = new ArrayList<>();
            allLineSortPX = new ArrayList<>();
            pointId = new ArrayList<>();
            facNames = new ArrayList<>();
            drawTypes = new ArrayList<>();
            drawNums = new ArrayList<>();
            drawNumss = new ArrayList<>();
            projectIds = new ArrayList<>();
            facNumLiness = new ArrayList<>();
            for (int r = 0; r < allLineLatlogVo.size(); r++) {
                if (allLineLatlogVo.get(r).getDataType().equals(MapMeterMoveScope.LINEPOINT)){
//                    String [] facNunLiew=allLineLatlogVo.get(r).getFac_num_line().split(",");
//                    if (facNunLiew.length>0){
//                        for (String facNumline:facNunLiew){
//                            if (facNumline.equals(facName.get(e))){
//                                linePointVo = new LinePointVo();
//                                linePointVo.setLatLng(new LatLng(allLineLatlogVo.get(r).getLocation().get(1),
//                                        allLineLatlogVo.get(r).getLocation().get(0)));
//                                linePointVo.setPointNum(allLineLatlogVo.get(r).getDraw_num());
//                                linePointVo.setPointId(allLineLatlogVo.get(r).getId() + "");
//                                linePointVo.setFacNum(allLineLatlogVo.get(r).getFac_num()+"");
//                                linePointVo.setProjectId(facName.get(e)+"");
//                                linePointVo.setFacNumLines(allLineLatlogVo.get(r).getFac_num_line()+"");
//                                linePointVo.setDrawType(allLineLatlogVo.get(r).getDataType());
//                                allLineSort.add(linePointVo);
//                            }
//                        }
//                    }
                }else if (allLineLatlogVo.get(r).getDataType().equals(MapMeterMoveScope.LINE)){
                    if (allLineLatlogVo.get(r).getPipName().equals(facName.get(e))) {
                        linePointVo = new LinePointVo();
                        linePointVo.setLatLng(new LatLng(Double.parseDouble(allLineLatlogVo.get(r).getLatitude()),
                                Double.parseDouble(allLineLatlogVo.get(r).getLongitude())));
                        linePointVo.setPointNum(allLineLatlogVo.get(r).getDrawNum());
                        linePointVo.setPointId(allLineLatlogVo.get(r).getId() + "");
                        linePointVo.setFacNum(allLineLatlogVo.get(r).getPipName()+"");
                        linePointVo.setProjectId(allLineLatlogVo.get(r).getProjectId());
                        linePointVo.setDrawType(allLineLatlogVo.get(r).getDataType());
                        allLineSort.add(linePointVo);
                    }
                }
            }


            //对每一条线段进行排序
            if (allLineSort != null && allLineSort.size() > 1) {
                boolean is=true;
                for (int y = 0; y <allLineSort.size(); y++) {
                    String[] pointNumData = allLineSort.get(y).getPointNum().split(",");
                    if (pointNumData.length == 1&&!pointNumData[0].equals("")) {    //若是拆分数组为1，直接将该绘制顺序赋值
                        if (is){
                            for (int i=0;i<allLineSort.size();i++){
                                for (int j=0;j<allLineSort.size()-1-i;j++){
                                    if (Integer.parseInt(allLineSort.get(j).getPointNum())
                                            >Integer.parseInt(allLineSort.get(j+1).getPointNum())){
                                        int temp=Integer.parseInt(allLineSort.get(j).getPointNum());
                                        allLineSort.get(j).setPointNum(allLineSort.get(j+1).getPointNum());
                                        allLineSort.get(j+1).setPointNum(temp+"");
                                    }
                                }
                            }
                        }

                        is=false;

                        allLineSortPX.add(allLineSort.get(y).getLatLng());
                        pointId.add(allLineSort.get(y).getPointId() + "");
                        facNames.add(allLineSort.get(y).getFacNum());
                        drawTypes.add(allLineSort.get(y).getDrawType());
                        drawNums.add(allLineSort.get(y).getPointNum());
                        drawNumss.add(allLineSort.get(y).getPointNum());
                        projectIds.add(allLineSort.get(y).getProjectId());
                        facNumLiness.add(allLineSort.get(y).getFacNumLines());
                    } else {         //若是拆分数组不为1，获取facNumLines在facNumLiness集合所在的下标值，拆分数组对应的下标值就是绘制顺序
//                        int s = -1;
//                        String [] facNumLineData=allLineSort.get(y).getFacNumLines().split(",");
//                        if (facNumLineData.length>0){
//                            for (int k=0;k<facNumLineData.length;k++){
//                                if (facNumLineData[k].equals(allLineSort.get(y).getProjectId())){
//                                    s=k;
//                                    break;
//                                }
//                            }
//                        }
//                        String drawNum = pointNumData[s];
//
//                        allLineSortPX.add(allLineSort.get(y).getLatLng());
//                        pointId.add(allLineSort.get(y).getPointId() + "");
//                        facNames.add(allLineSort.get(y).getFacNum());
//                        drawTypes.add(allLineSort.get(y).getDataType());
//                        drawNums.add(drawNum);
//                        drawNumss.add(allLineSort.get(y).getPointNum());
//                        projectIds.add(allLineSort.get(y).getProjectId());
//                        facNumLiness.add(allLineSort.get(y).getFacNumLines());
                    }

                }
            }
            lineSegment.add(allLineSortPX);
            pointIdSegment.add(pointId);
            facNamesSeg.add(facNames);
            drawTypesSeg.add(drawTypes);
            facDrawNum.add(drawNums);
            facDrawNums.add(drawNumss);
            facNumLinesSeg.add(projectIds);
            facNumLinessSeg.add(facNumLiness);

        }

        //这里要把pointIdSegment设置到arcgis地图中
//        mapMeterMoveScope.setPointIdSegment(pointIdSegment);
//        mapMeterMoveScope.setOkHttpRequest(okHttpRequest);
        mapViewer.setPointIdSegment(pointIdSegment);
        mapViewer.setDrawTypesSeg(drawTypesSeg);

        //分批显示线段
        for (int u = 0; u < lineSegment.size(); u++) {
//            List<String > pointIds=pointIdSegment.get(u);
            List<String > pointDrawTypes=drawTypesSeg.get(u);
            List<LatLng> poList = lineSegment.get(u);
            for (int s = 0; s < poList.size(); s++) {
                Bundle bundle = new Bundle();
                bundle.putDouble("positionX", poList.get(s).longitude);
                bundle.putDouble("positionY", poList.get(s).latitude);
//                BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.drawable.icon_marka_b_yellow);
//                OverlayOptions option;
                if (pointDrawTypes.get(s).equals(MapMeterMoveScope.LINEPOINT)){

                }else  if (pointDrawTypes.get(s).equals(MapMeterMoveScope.LINE)){
//                    option = new MarkerOptions()
//                            .draggable(true)
//                            .position(poList.get(s))
//                            .title(pointIdSegment.get(u).get(s))
//                            .icon(bitmap)
//                            .extraInfo(bundle);
//                    pointOver = (Marker) mBaiduMap.addOverlay(option);
//                    mOverlayList.add(pointOver);
                }
            }

            String facName="";
            String drawType="";
            String facNameLine="";
            List<String> facNameLines=new ArrayList<>();
            if (facNamesSeg.get(u).size()>0){
                facName=facNamesSeg.get(u).get(0);
            }
            if (drawTypesSeg.get(u).size()>0){
                drawType=drawTypesSeg.get(u).get(0);
            }
            if (facNumLinesSeg.get(u).size()>0){
                facNameLine=facNumLinesSeg.get(u).get(0);
            }
            if (facNumLinessSeg.get(u).size()>0){
                facNameLines=facNumLinessSeg.get(u);
            }

            mOverlayList.add(BaiduMapDraw.drawLine(mBaiduMap, lineSegment.get(u),
                    facName,
                    drawType,
                    facNumLinesSeg.get(u),
                    pointIdSegment.get(u),
                    facNameLines,
                    facDrawNum.get(u),
                    facDrawNums.get(u),
                    miningSurveyVO,
                    mContext
            ));

            mapViewer.setmOverlayList(mOverlayList);

            List<LatLng> points= lineSegment.get(u);
            if (points.size()>=2){
                for (int i=1;i<points.size();i++){
                    double lon=(points.get(i-1).longitude+points.get(i).longitude)/2;
                    double lat=(points.get(i-1).latitude+points.get(i).latitude)/2;
                    LatLng latLng=new LatLng(lat,lon);
                    //构建文字Option对象，用于在地图上添加文字
                    if (drawType.equals(MapMeterMoveScope.LINEPOINT)){
                        textOption = new TextOptions()
                                .fontSize(25)
                                .fontColor(miningSurveyVO.getProjectId().equals(facNumLinesSeg.get(u).get(0))
                                        ?mContext.getResources().getColor(R.color.crimson)
                                        :mContext.getResources().getColor(R.color.point_change_line))
                                .text(facNameLine)
                                .position(latLng);
                        //在地图上添加该文字对象并显示
                        mOverlayList.add(mBaiduMap.addOverlay(textOption));
                    }else if (drawType.equals(MapMeterMoveScope.LINE)){
                        textOption = new TextOptions()
                                .fontSize(25)
                                .fontColor(miningSurveyVO.getProjectId().equals(facNumLinesSeg.get(u).get(0))
                                        ?mContext.getResources().getColor(R.color.crimson)
                                        :mContext.getResources().getColor(R.color.point_change_line))
                                .text(facName)
                                .position(latLng);
                        //在地图上添加该文字对象并显示
                        mOverlayList.add(mBaiduMap.addOverlay(textOption));
                    }
                    mapViewer.setmOverlayList(mOverlayList);

                }
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
