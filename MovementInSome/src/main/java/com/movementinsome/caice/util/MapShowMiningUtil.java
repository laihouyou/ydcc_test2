package com.movementinsome.caice.util;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;

import com.amap.api.maps.model.LatLng;
import com.esri.android.map.GraphicsLayer;
import com.esri.core.geometry.Polyline;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.PictureMarkerSymbol;
import com.esri.core.symbol.SimpleLineSymbol;
import com.movementinsome.AppContext;
import com.movementinsome.R;
import com.movementinsome.caice.vo.LatlogVo;
import com.movementinsome.caice.vo.LinePointVo;
import com.movementinsome.kernel.location.coordinate.Gcj022Bd09;
import com.movementinsome.kernel.location.coordinate.Gcj022Gps;
import com.movementinsome.map.coordinate.TranslateCoordinateFactory;
import com.movementinsome.map.view.MyMapView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ZZC on 2017/3/24.
 */

public class MapShowMiningUtil {

    private static MyMapView map;
    private LatlogVo latlogVo;
    private Context context;
    private static GraphicsLayer grapPayer;//点
    private static GraphicsLayer grapLineLayer;//线
//    private PictureMarkerSymbol pointSymbol;
    private Resources res;
    private List<LatlogVo.PoisBean> allLineLatlogVo;
    private List<LatlogVo.PoisBean> allPointLatlogVo;


    public MapShowMiningUtil(MyMapView map, Context context, LatlogVo latlogVo){
        this.context=context;
        this.map = map;
        this.latlogVo = latlogVo;
        res = context.getResources();
        grapPayer = new GraphicsLayer();
        grapLineLayer = new GraphicsLayer();
        map.addLayer(grapPayer);
        map.addLayer(grapLineLayer);
        allLineLatlogVo=new ArrayList<>();
        allPointLatlogVo=new ArrayList<>();

        FilterData(this.latlogVo);
    }
    //过滤数据
    private void FilterData(LatlogVo latlogVo) {
        for (int i = 0; i < latlogVo.getSize(); i++) {
            if (latlogVo.getPois().get(i).getDraw_type()!=null&&
                    (latlogVo.getPois().get(i).getDraw_type().equals(MapMeterMoveScope.LINE)||
                            latlogVo.getPois().get(i).getDraw_type().equals(MapMeterMoveScope.LINEPOINT))){  //线 与点线
                allLineLatlogVo.add(latlogVo.getPois().get(i));

            }else if (latlogVo.getPois().get(i).getDraw_type()!=null&&
                    latlogVo.getPois().get(i).getDraw_type().equals(MapMeterMoveScope.POINT)
                    ||latlogVo.getPois().get(i).getDraw_type()==null
                    ||latlogVo.getPois().get(i).getDraw_type().equals("")
                    ){
                allPointLatlogVo.add(latlogVo.getPois().get(i));
            }
        }
    }

    public void update(LatlogVo latlogVo2){
        latlogVo = latlogVo2;
        allPointLatlogVo.clear();
        allLineLatlogVo.clear();
        FilterData(latlogVo);
    }

    public static void  clearAllLayer(){
        map.getCallout().hide();
        grapPayer.removeAll();
        grapLineLayer.removeAll();
    }

    /**
     * 绘制获取到得地图maker
     */
    public void makerShow(){
        grapPayer.removeAll();
        grapLineLayer.removeAll();
        PictureMarkerSymbol pointSymbol = new PictureMarkerSymbol(res.getDrawable(R.drawable.dot2));
        for (int i = 0; i < allPointLatlogVo.size() ; i++){
            double lonlat[]= Gcj022Bd09.bd09Decrypt(allPointLatlogVo.get(i).getLocation().get(1),allPointLatlogVo.get(i).getLocation().get(0));
            Map<String,Double> loc84= Gcj022Gps.gcj2wgs(lonlat[0],lonlat[1]);
            com.esri.core.geometry.Point point = TranslateCoordinateFactory.gpsTranslate(AppContext.getInstance().getCoordTransform(),map.getSpatialReference(),loc84.get("lat"),loc84.get("lon"),0);
            Map<String, Object> grahicMap = new HashMap<String, Object>() ;
            grahicMap.put("facId",allPointLatlogVo.get(i).getId());
            grahicMap.put("facNum",allPointLatlogVo.get(i).getFac_num());
            grahicMap.put("draw_type",allPointLatlogVo.get(i).getDraw_type());
            Graphic grahic = new Graphic(point,pointSymbol,grahicMap);
            grapPayer.addGraphic(grahic);
        }
               List<String> facName=new ArrayList<>(); //装有表号的集合，即有几条线段
//        for (int q=0;q<allLineLatlogVo.size();q++){
//            boolean isPd = true;
//            if (q==0){
//                facName.add(allLineLatlogVo.get(q).getFac_num());
//            }else {
//                for (int w=0;w<facName.size();w++){
//                    if (allLineLatlogVo.get(q).getFac_num().equals(facName.get(w))){
//                        isPd=false;
//                    }
//                }
//                if(isPd){
//                    facName.add(allLineLatlogVo.get(q).getFac_num());
//                }
//            }
//        }
//
//        //对点进行按线段分类
//        List<List<LatLng>> lineSegment=new ArrayList<>();
//        List<List<String>> pointIdSegment=new ArrayList<>();
//        List<List<String>> facNumSegment=new ArrayList<>();
//        List<LinePointVo> allLineSort;  //同一条线段下的所有点与序号       排序前
//        List<LatLng> allLineSortPX;  //同一条线段下的所有点与序号   排序后的
//        List<String> pointId = null;//点ID集合
//        List<String> facNumList = null;//点编号集合
//        LinePointVo linePointVo;
//        for (int e=0;e<facName.size();e++){
//            allLineSort=new ArrayList<>();
//            allLineSortPX=new ArrayList<>();
//            pointId=new ArrayList<>();
//            facNumList = new ArrayList<>();
//            for (int r=0;r<allLineLatlogVo.size();r++){
//                if (allLineLatlogVo.get(r).getFac_num().equals(facName.get(e))){
//                    linePointVo=new LinePointVo();
//                    linePointVo.setLatLng(new LatLng(allLineLatlogVo.get(r).getLocation().get(1),
//                            allLineLatlogVo.get(r).getLocation().get(0)));
//                    linePointVo.setPointNum(allLineLatlogVo.get(r).getDraw_num());
//                    linePointVo.setPointId(allLineLatlogVo.get(r).getId()+"");
//                    linePointVo.setFacNum(allLineLatlogVo.get(r).getFac_num());
//                    allLineSort.add(linePointVo);
//                }
//            }
//
//            //对每一条线段进行排序
//            if (allLineSort!=null&&allLineSort.size()>1){
//                for (int t=0;t<allLineSort.size();t++){
//                    for (int y=allLineSort.size()-1;y>=0;y--){
//                        if ((t+"").equals(allLineSort.get(y).getPointNum())){
//                            allLineSortPX.add(allLineSort.get(y).getLatLng());
//                            pointId.add(allLineSort.get(y).getPointId()+"");
//                            facNumList.add(allLineSort.get(y).getFacNum());
//                        }
//                    }
//                }
//
//            }
//            lineSegment.add(allLineSortPX);
//            pointIdSegment.add(pointId);
//            facNumSegment.add(facNumList);
//        }
//
//        //分批显示线段
//        for (int u=0;u<lineSegment.size();u++){
//            List <LatLng> poList=lineSegment.get(u);
//            for (int s=0;s<poList.size();s++){
//                double lonlat[]=Gcj022Bd09.bd09Decrypt(poList.get(s).latitude,poList.get(s).longitude);
//                Map<String,Double> loc84=Gcj022Gps.gcj2wgs(lonlat[0],lonlat[1]);
//                com.esri.core.geometry.Point point =TranslateCoordinateFactory.gpsTranslate(AppContext.getInstance().getCoordParam(),map.getSpatialReference(),loc84.get("lat"),loc84.get("lon"),0);
//                Map<String, Object> grahicMap = new HashMap<String, Object>() ;
//                grahicMap.put("facId",pointIdSegment.get(u).get(s));
//                grahicMap.put("facNum",facNumSegment.get(u).get(s));
//                grahicMap.put("draw_type",MapMeterMoveScope.LINE);
//                PictureMarkerSymbol PLineSymbol = new PictureMarkerSymbol(res.getDrawable(R.drawable.dot1));
//                Graphic grahic = new Graphic(point,PLineSymbol,grahicMap);
//                grapPayer.addGraphic(grahic);
//                if(s<=poList.size()-2) {
//                    double lonlat2[]=Gcj022Bd09.bd09Decrypt(poList.get(s+1).latitude,poList.get(s+1).longitude);
//                    Map<String,Double> loc842=Gcj022Gps.gcj2wgs(lonlat2[0],lonlat2[1]);
//                    com.esri.core.geometry.Point pointEnd =TranslateCoordinateFactory.gpsTranslate(AppContext.getInstance().getCoordParam(),map.getSpatialReference(),loc842.get("lat"),loc842.get("lon"),0);
////                    Line line = new Line();
////                    line.setStart(point);
////                    line.setEnd(pointEnd);
//                    SimpleLineSymbol lineSymbol = new SimpleLineSymbol(Color.YELLOW, 3);
//                    Polyline polyline = new Polyline();
//                    polyline.startPath(point);
//                    polyline.lineTo(pointEnd);
////                    polyline.addSegment(line, true);
//                    Graphic g = new Graphic(polyline, lineSymbol);
//                    grapLineLayer.addGraphic(g);
//                }
//            }
//
//        }
        for (int q = 0; q < allLineLatlogVo.size(); q++) {
            boolean isPd = true;
            boolean isPdLine = true;

            if (q == 0) {
                if (allLineLatlogVo.get(q).getDraw_type().equals(MapMeterMoveScope.LINEPOINT)) {
                    String[] facNunLiew = allLineLatlogVo.get(q).getFac_num_line().split(",");
                    if (facNunLiew.length > 0) {
                        for (String facNumline : facNunLiew) {
                            facName.add(facNumline);
                        }
                    }

                } else if (allLineLatlogVo.get(q).getDraw_type().equals(MapMeterMoveScope.LINE)) {
                    facName.add(allLineLatlogVo.get(q).getFac_num());
                }
            } else {
                for (int w = 0; w < facName.size(); w++) {
                    if (allLineLatlogVo.get(q).getDraw_type().equals(MapMeterMoveScope.LINE)) {
                        if (allLineLatlogVo.get(q).getFac_num().equals(facName.get(w))) {
                            isPd = false;
                        }
                    }
                    if (allLineLatlogVo.get(q).getDraw_type().equals(MapMeterMoveScope.LINEPOINT)) {
                        String[] facNunLiew = allLineLatlogVo.get(q).getFac_num_line().split(",");
                        if (facNunLiew.length > 0) {
                            for (String facNumline : facNunLiew) {
                                if (facNumline.equals(facName.get(w))) {
                                    isPdLine = false;
                                    break;
                                }
                                if (isPdLine) {
                                    facName.add(facNumline);
                                }
                            }
                        }
                    }
                }
                if (isPd) {
                    if (allLineLatlogVo.get(q).getDraw_type().equals(MapMeterMoveScope.LINE)) {
                        facName.add(allLineLatlogVo.get(q).getFac_num());

                    }
                }
            }
        }

        //对点进行按线段分类
        List<List<LatLng>> lineSegment = new ArrayList<>();
        List<List<String>> facNamesSeg = new ArrayList<>();
        List<List<String>> drawTypesSeg = new ArrayList<>();
        List<List<String>> facNumLinesSeg = new ArrayList<>();
        List<List<String>> facNumLinessSeg = new ArrayList<>();
        List<List<String>> facDrawNum = new ArrayList<>();
        List<List<String>> facDrawNums = new ArrayList<>();
        List<List<String>> pointIdSegment = new ArrayList<>();
        List<LinePointVo> allLineSort;  //同一条线段下的所有点与序号       排序前
        List<LatLng> allLineSortPX;  //同一条线段下的所有点与序号   排序后的
        List<String> pointId = null;//点ID集合
        List<String> facNames = null;//表号集合
        List<String> drawTypes = null;//点ID集合
        List<String> drawNums = null;//点ID集合
        List<String> drawNumss = null;//点ID集合
        List<String> facNumLines = null;//点ID集合
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
            facNumLines = new ArrayList<>();
            facNumLiness = new ArrayList<>();
            for (int r = 0; r < allLineLatlogVo.size(); r++) {
                if (allLineLatlogVo.get(r).getDraw_type().equals(MapMeterMoveScope.LINEPOINT)){
                    String[] facNunLiew=allLineLatlogVo.get(r).getFac_num_line().split(",");
                    if (facNunLiew.length>0){
                        for (String facNumline:facNunLiew){
                            if (facNumline.equals(facName.get(e))){
                                linePointVo = new LinePointVo();
                                linePointVo.setLatLng(new LatLng(allLineLatlogVo.get(r).getLocation().get(1),
                                        allLineLatlogVo.get(r).getLocation().get(0)));
                                linePointVo.setPointNum(allLineLatlogVo.get(r).getDraw_num());
                                linePointVo.setPointId(allLineLatlogVo.get(r).getId() + "");
                                linePointVo.setFacNum(allLineLatlogVo.get(r).getFac_num()+"");
                                linePointVo.setProjectId(facName.get(e)+"");
                                linePointVo.setFacNumLines(allLineLatlogVo.get(r).getFac_num_line()+"");
                                linePointVo.setDrawType(allLineLatlogVo.get(r).getDraw_type());
                                allLineSort.add(linePointVo);
                            }
                        }
                    }
                }else if (allLineLatlogVo.get(r).getDraw_type().equals(MapMeterMoveScope.LINE)){
                    if (allLineLatlogVo.get(r).getFac_num().equals(facName.get(e))) {
                        linePointVo = new LinePointVo();
                        linePointVo.setLatLng(new LatLng(allLineLatlogVo.get(r).getLocation().get(1),
                                allLineLatlogVo.get(r).getLocation().get(0)));
                        linePointVo.setPointNum(allLineLatlogVo.get(r).getDraw_num());
                        linePointVo.setPointId(allLineLatlogVo.get(r).getId() + "");
                        linePointVo.setFacNum(allLineLatlogVo.get(r).getFac_num()+"");
                        linePointVo.setProjectId(allLineLatlogVo.get(r).getFac_num_line()+"");
                        linePointVo.setDrawType(allLineLatlogVo.get(r).getDraw_type());
                        allLineSort.add(linePointVo);
                    }
                }
            }

            //对每一条线段进行排序
            if (allLineSort != null && allLineSort.size() > 1) {
                for (int y = allLineSort.size() - 1; y >= 0; y--) {
                    String[] pointNumData = allLineSort.get(y).getPointNum().split(",");
                    if (pointNumData.length == 1) {    //若是拆分数组为1，直接将该绘制顺序赋值
                        allLineSortPX.add(allLineSort.get(y).getLatLng());
                        pointId.add(allLineSort.get(y).getPointId() + "");
                        facNames.add(allLineSort.get(y).getFacNum());
                        drawTypes.add(allLineSort.get(y).getDrawType());
                        drawNums.add(allLineSort.get(y).getPointNum());
                        drawNumss.add(allLineSort.get(y).getPointNum());
                        facNumLines.add(allLineSort.get(y).getProjectId());
                        facNumLiness.add(allLineSort.get(y).getFacNumLines());
                    } else {         //若是拆分数组不为1，获取facNumLines在facNumLiness集合所在的下标值，拆分数组对应的下标值就是绘制顺序
                        int s = -1;
                        String[] facNumLineData=allLineSort.get(y).getFacNumLines().split(",");
                        if (facNumLineData.length>0){
                            for (int k=0;k<facNumLineData.length;k++){
                                if (facNumLineData[k].equals(allLineSort.get(y).getProjectId())){
                                    s=k;
                                    break;
                                }
                            }
                        }
                        String drawNum = pointNumData[s];

                        allLineSortPX.add(allLineSort.get(y).getLatLng());
                        pointId.add(allLineSort.get(y).getPointId() + "");
                        facNames.add(allLineSort.get(y).getFacNum());
                        drawTypes.add(allLineSort.get(y).getDrawType());
                        drawNums.add(drawNum);
                        drawNumss.add(allLineSort.get(y).getPointNum());
                        facNumLines.add(allLineSort.get(y).getProjectId());
                        facNumLiness.add(allLineSort.get(y).getFacNumLines());
                    }

                }
            }
            lineSegment.add(allLineSortPX);
            pointIdSegment.add(pointId);
            facNamesSeg.add(facNames);
            drawTypesSeg.add(drawTypes);
            facDrawNum.add(drawNums);
            facDrawNums.add(drawNumss);
            facNumLinesSeg.add(facNumLines);
            facNumLinessSeg.add(facNumLiness);

        }

        //分批显示线段
        for (int u=0;u<lineSegment.size();u++){
            List<LatLng> poList=lineSegment.get(u);
            for (int s=0;s<poList.size();s++){
                double lonlat[]=Gcj022Bd09.bd09Decrypt(poList.get(s).latitude,poList.get(s).longitude);
                Map<String,Double> loc84=Gcj022Gps.gcj2wgs(lonlat[0],lonlat[1]);
                com.esri.core.geometry.Point point =TranslateCoordinateFactory.gpsTranslate(AppContext.getInstance().getCoordTransform(),map.getSpatialReference(),loc84.get("lat"),loc84.get("lon"),0);
                Map<String, Object> grahicMap = new HashMap<String, Object>() ;
                grahicMap.put("facId",pointIdSegment.get(u).get(s));
                grahicMap.put("facNum",facDrawNum.get(u).get(s));
                grahicMap.put("draw_type",MapMeterMoveScope.LINE);
                PictureMarkerSymbol PLineSymbol = new PictureMarkerSymbol(res.getDrawable(R.drawable.dot1));
                Graphic grahic = new Graphic(point,PLineSymbol,grahicMap);
                grapPayer.addGraphic(grahic);
                if(s<=poList.size()-2) {
                    double lonlat2[]=Gcj022Bd09.bd09Decrypt(poList.get(s+1).latitude,poList.get(s+1).longitude);
                    Map<String,Double> loc842=Gcj022Gps.gcj2wgs(lonlat2[0],lonlat2[1]);
                    com.esri.core.geometry.Point pointEnd =TranslateCoordinateFactory.gpsTranslate(AppContext.getInstance().getCoordTransform(),map.getSpatialReference(),loc842.get("lat"),loc842.get("lon"),0);
//                    Line line = new Line();
//                    line.setStart(point);
//                    line.setEnd(pointEnd);
                    SimpleLineSymbol lineSymbol = new SimpleLineSymbol(Color.YELLOW, 3);
                    Polyline polyline = new Polyline();
                    polyline.startPath(point);
                    polyline.lineTo(pointEnd);
//                    polyline.addSegment(line, true);
                    Graphic g = new Graphic(polyline, lineSymbol);
                    grapLineLayer.addGraphic(g);
                }
            }

        }

//        AppContext.getInstance().setGrapPayerl(grapPayer);
    }
}
