package com.movementinsome.caice.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.esri.android.map.GraphicsLayer;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.Polyline;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.PictureMarkerSymbol;
import com.esri.core.symbol.SimpleLineSymbol;
import com.j256.ormlite.dao.Dao;
import com.movementinsome.AppContext;
import com.movementinsome.R;
import com.movementinsome.caice.vo.MiningSurveyChildVO;
import com.movementinsome.kernel.location.LocationInfo;
import com.movementinsome.map.coordinate.TranslateCoordinateFactory;
import com.movementinsome.map.view.MyMapView;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

/**
 * arcgis地图操作类
 * Created by zzc on 2017/3/21.
 */

public class ArcgisMapOperations {

    public static final String GCJ02 = "gcj02";
    public static final String BD09 = "bd09";
    public static final String ARCGIS = "地方平面坐标";
    private String pointTypees=GCJ02;
    
    private Context mContext;
    private MyMapView moveMapView;
    private MapMeterMoveScope mapMeterMoveScope;
    private GraphicsLayer grapLayerW;
    private GraphicsLayer grapPayerW;
    private GraphicsLayer grapPayer;
    private AlertDialog alertDiaChildlog;
    private AlertDialog progDialog;
    private AlertDialog pointDialog;
    private List<Double> pointList;
    private List<List<Double>> listPoint;
    private String wpId;

    
    public ArcgisMapOperations(Context context, MyMapView moveMapView, MapMeterMoveScope mapMeterMoveScope,
                               GraphicsLayer grapPayer, GraphicsLayer grapLayerW,
                               GraphicsLayer grapPayerW, AlertDialog alertDiaChildlog,
                               AlertDialog progDialog, AlertDialog pointDialog,
                               List<Double> pointList, List<List<Double>> listPoint,
                               String wpId
                               ){
        this.mContext=context;
        this.moveMapView=moveMapView;
        this.mapMeterMoveScope=mapMeterMoveScope;
        this.grapLayerW=grapLayerW;
        this.grapPayerW=grapPayerW;
        this.grapPayer=grapPayer;
        this.alertDiaChildlog=alertDiaChildlog;
        this.progDialog=progDialog;
        this.pointDialog=pointDialog;
        this.pointList=pointList;
        this.listPoint=listPoint;
        this.wpId=wpId;
        
    }
    
    /**
     * 查看工程全部点线数据
     * @param projectId 工程ID
     */
    public void setPointLineDate(String projectId, String projectName) {
        if (alertDiaChildlog!=null&&alertDiaChildlog.isShowing()){
            alertDiaChildlog.dismiss();
        }
        if (progDialog!=null&&progDialog.isShowing()){
            progDialog.dismiss();
        }
        if (mapMeterMoveScope!=null){
            mapMeterMoveScope.grapLayerW.removeAll();
            mapMeterMoveScope.grapPayerW.removeAll();
            mapMeterMoveScope.grapLayer.removeAll();
            mapMeterMoveScope.grapPayer.removeAll();
        }
        grapLayerW.removeAll();
        grapPayerW.removeAll();
//        moveMapView.getMoveTvxs().setVisibility(View.VISIBLE);
//        moveMapView.getMoveTvxs().setText(projectName);
        try {
            Dao<MiningSurveyChildVO, Long> miningSurveyChildVODao= AppContext.getInstance().getAppDbHelper().getDao(MiningSurveyChildVO.class);
            List<MiningSurveyChildVO> miningSurveyChildVOList = miningSurveyChildVODao.queryForEq("wpId",projectId);
            Log.i("tag",miningSurveyChildVOList.toString());
            grapPayerW.removeAll();
            grapLayerW.removeAll();
            if (miningSurveyChildVOList!=null&&miningSurveyChildVOList.size()>0){
                for(int i=0;i<miningSurveyChildVOList.size();i++){
                    String type=miningSurveyChildVOList.get(i).getChildProjectType();
                    switch (type){
                        case MapMeterMoveScope.POINT://点
                            showCompletePoint(miningSurveyChildVOList.get(i).getPoint());
                            break;
                        case MapMeterMoveScope.LINE://单线
                            showCompleteLine(miningSurveyChildVOList.get(i).getPoint());
                            break;
                        case MapMeterMoveScope.DUOXIAN://多线
                            showCompleteLine(miningSurveyChildVOList.get(i).getPoint());
                            break;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
//
//    public void SaveMove(final String wpiId, final String name) {
//        if (wpiId != null && !wpiId.equals("")) {
//            try {
//                final Dao<MiningSurveyChildVO, Long> miningSurveyChildVOdao = AppContext
//                        .getInstance().getAppDbHelper()
//                        .getDao(MiningSurveyChildVO.class);
//
////                Map<String, Object> n = new HashMap<String, Object>();
////                n.put("wpId", wpiId);
//                List<MiningSurveyChildVO> miningSurveyChildlist = miningSurveyChildVOdao.queryForEq("wpId",wpiId);
//                if (miningSurveyChildlist != null && miningSurveyChildlist.size() > 0) {
//                    View viewList = View.inflate(mContext, R.layout.move_mining_list, null);
//                    ListView movelist = (ListView) viewList.findViewById(R.id.moveList);
////                    MoveListChildAdapter MoveListAdapter = new MoveListChildAdapter(mContext, mContext, miningSurveyChildlist,miningSurveyChildVOdao);
//                    BaseAdapter MoveListAdapter=new CommonRecycleViewAdapter(mContext, R.layout.move_mining_child_list_item,miningSurveyChildlist) {
//
//                        @Override
//                        protected void convert(ViewHolder viewHolder, final MiningSurveyChildVO item, int position) {
//                            viewHolder.setText(R.id.moveType3,item.getChildProjectType());
//                            viewHolder.setText(R.id.moveTv3,name);
//                            viewHolder.setText(R.id.moveUpdatatime3,item.getChildProjectSDateStr());
//                            //查看点线信息
//                            viewHolder.setOnClickListener(R.id.moveBtnSave3, new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//                                    if (mapMeterMoveScope!=null){
//                                        mapMeterMoveScope.grapLayerW.removeAll();
//                                        mapMeterMoveScope.grapPayerW.removeAll();
//                                        mapMeterMoveScope.grapLayer.removeAll();
//                                        mapMeterMoveScope.grapPayer.removeAll();
//                                    }
//                                    grapLayerW.removeAll();
//                                    grapPayerW.removeAll();
//                                    switch (item.getChildProjectType()){
//                                        case MapMeterMoveScope.POINT:
//                                            showCompletePoint(item.getPoint());
//                                            break;
//                                        case MapMeterMoveScope.LINE:
//                                            showCompleteLine(item.getPoint());
//                                            break;
//                                    }
//                                    if (alertDiaChildlog.isShowing()){
//                                        alertDiaChildlog.dismiss();
//                                    }
//                                    if (progDialog.isShowing()){
//                                        progDialog.dismiss();
//                                    }
////                                    moveMapView.getMoveTvxs().setVisibility(View.VISIBLE);
////                                    moveMapView.getMoveTvxs().setText(name);
//                                }
//                            });
//                            //删除点线信息
//                            viewHolder.setOnClickListener(R.id.moveBtnDelete3, new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//                                    try {
//                                        if (alertDiaChildlog.isShowing()){
//                                            alertDiaChildlog.dismiss();
//                                        }
//                                        if (progDialog.isShowing()){
//                                            progDialog.dismiss();
//                                        }
//                                        int s=miningSurveyChildVOdao.delete(item);
//                                        if (s==1){
//                                            Toast.makeText(mContext,"删除成功", Toast.LENGTH_LONG).show();
//                                            setPointLineDate(wpiId,name);
//                                        }else {
//                                            Toast.makeText(mContext,"删除失败", Toast.LENGTH_LONG).show();
//                                        }
//                                    } catch (SQLException e) {
//                                        e.printStackTrace();
//                                    }
//                                }
//                            });
//                        }
//                    };
//                    movelist.setAdapter(MoveListAdapter);
//
//                    //查看工程全部信息
//                    Button moveGone_all= (Button) viewList.findViewById(R.id.moveGone_all);
//                    moveGone_all.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//
//                            setPointLineDate(wpiId,name);
//                        }
//                    });
//
//                    alertDiaChildlog = new AlertDialog.Builder(mContext)
//                            .setTitle(name)
//                            .setView(viewList)
//                            .show();
//                }
//            } catch (Exception e) {
//                // TODO: handle exception
//                e.printStackTrace();
//            }
//        }
//    }

    /**
     * 显示采点采线按钮
     * @param wpiId
     * @param Name
     */
    public void GatherMove(final String wpiId, String Name, final String isCompleteSubmit,
                           TextView moveTvxs, ImageButton moveDrawing) {

//        moveMapView.setOnTouchListener(myTouchListener);

        if (progDialog!=null&&progDialog.isShowing()) {
            progDialog.dismiss();
        }
        if (wpiId != null && !wpiId.equals("")) {
            wpId = wpiId;
            moveTvxs.setVisibility(View.VISIBLE);
            moveTvxs.setText(Name);
            moveDrawing.setVisibility(View.VISIBLE);
            moveDrawing.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
//					ClickOn(wpiId);
//                    Caidian(wpiId,isCompleteSubmit);
                }
            });
//            moveMapView.getMoveDrawingLine().setVisibility(View.VISIBLE);
//            moveMapView.getMoveDrawingLine().setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Caixian(wpiId,isCompleteSubmit);
//                }
//            });
        }
    }
//
//    private void Caidian(final String wpiId, final String isCompleteSubmit) {
//        final String[] items_1 = {"精准点", "坐标点", "手绘点"};
//        final String[] pointType = {GCJ02, BD09, ARCGIS};
//        new AlertDialog.Builder(
//                mContext)
//                .setTitle("你选择采集方式是:" + "采点")
//                .setItems(items_1, new DialogInterface.OnClickListener() {
//
//                    @Override
//                    public void onClick(
//                            DialogInterface dialog,
//                            int which2) {
//                        // TODO Auto-generated method stub
//                        if (items_1[which2].equals("精准点")) {
////                            moveMapView.setLocate(true);
////                            moveMapView.startLoaction();
//                            //定位启动后延迟1S
//                            new Thread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    try {
//                                        Thread.sleep(1000);
//                                        final LocationInfo info=moveMapView.getLocationManage().getLocation();
//                                        TextView tv=new TextView(mContext);
//                                        tv.setText("经纬度:"+"("+info.getLongitudeWg84()+","+info.getLatitudeWg84()+")"+"\n"+"地方坐标:"+
//                                                "("+info.getMapx()+","+info.getMapy()+")"+"\n"+"当前位置已确定，点击采集坐标");
//                                        tv.setOnClickListener(new View.OnClickListener() {
//                                            @Override
//                                            public void onClick(View v) {
//                                                Toast.makeText(mContext,"测试定位采集", Toast.LENGTH_SHORT).show();
//                                                //每次只能提一个点
////                                                String Point =info.getMapx()+","+info.getMapy();
////                                                HashMap<String, String> params = new HashMap<String, String>();
////                                                Intent newFormInfo = new Intent(
////                                                        mContext, RunForm.class);
////                                                newFormInfo.putExtra("template",
////                                                        "ins_hidden_trouble_form_zs.xml");
////                                                newFormInfo.putExtra("moveTable",
////                                                        true);
////                                                params.put("wpId", wpId);
//////                    if(drawType.equals(POINT)){
////                                                params.put("graph", "point");
//////                    }
////                                                params.put("PointList", Point);
////                                                params.put("moveType", "point");
////
////                                                newFormInfo.putExtra("moveparams", params);
////                                                startActivity(newFormInfo);
//                                                grapPayer.removeAll();
//                                                moveMapView.getCallout().hide();
//                                            }
//                                        });
//                                        Point point = TranslateCoordinateFactory.gpsTranslate(AppContext.getInstance().getCoordParam(),
//                                                moveMapView.getSpatialReference(), info.getLatitudeWg84(),
//                                                info.getLongitudeWg84(),0);
//                                        //点
//                                        PictureMarkerSymbol pointSymbol = new PictureMarkerSymbol(mContext.getResources().getDrawable(R.drawable.dot3));
//                                        Graphic grahic = new Graphic(point, pointSymbol);
//                                        grapPayer.addGraphic(grahic);
//                                        moveMapView.getCallout().hide();
//                                        moveMapView.getCallout().show(point, tv);
//                                    } catch (InterruptedException e) {
//                                        e.printStackTrace();
//                                    }
//                                }
//                            }).start();
//
//                        } else if (items_1[which2].equals("坐标点")) {
//                            final View pointView = LayoutInflater.from(mContext).inflate(R.layout.move_mining_point, null);
//                            final EditText editTextLat = (EditText) pointView.findViewById(R.id.moveLat);//纬度
//                            final EditText editTextLng = (EditText) pointView.findViewById(R.id.moveLng);//经度
//                            final TextView tvlng=(TextView)pointView.findViewById(R.id.tvlng);
//                            final TextView tvlat=(TextView)pointView.findViewById(R.id.tvlat);
//                            Spinner pointSpinner= (Spinner) pointView.findViewById(R.id.pointType);
//                            final ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, pointType);
//                            //设置下拉列表的风格
//                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                            //将adapter 添加到spinner中
//                            pointSpinner.setAdapter(adapter);
//                            pointSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//
//                                @Override
//                                public void onItemSelected(AdapterView<?> parent,
//                                                           View view, int position, long id) {
//                                    // TODO Auto-generated method stub
//                                    switch (parent.getItemAtPosition(position).toString()){
//                                        case GCJ02:
//                                            tvlng.setText("经度");
//                                            tvlat.setText("纬度");
//                                            editTextLng.setHint("请输入经度");
//                                            editTextLat.setHint("请输入纬度");
//                                            editTextLng.setText("");
//                                            editTextLat.setText("");
//                                            pointTypees=GCJ02;
//                                            break;
//                                        case BD09:
//                                            tvlng.setText("经度");
//                                            tvlat.setText("纬度");
//                                            editTextLng.setHint("请输入经度");
//                                            editTextLat.setHint("请输入纬度");
//                                            editTextLng.setText("");
//                                            editTextLat.setText("");
//                                            pointTypees=BD09;
//                                            break;
//                                        case ARCGIS:
//                                            tvlng.setText("X(北)");
//                                            tvlat.setText("Y(东)");
//                                            editTextLng.setHint("请输入地方坐标系");
//                                            editTextLat.setHint("请输入地方坐标系");
//                                            editTextLng.setText("");
//                                            editTextLat.setText("");
//                                            pointTypees=ARCGIS;
//                                            break;
//                                    }
//                                }
//
//                                @Override
//                                public void onNothingSelected(AdapterView<?> parent) {
//                                    // TODO Auto-generated method stub
//
//                                }
//                            });
//                            pointDialog = new AlertDialog.Builder(mContext)
//                                    .setView(pointView)
//                                    .setTitle("坐标采点")
//                                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
//                                        @Override
//                                        public void onClick(DialogInterface dialog, int which) {
//                                            if (pointDialog.isShowing()) {
//                                                pointDialog.dismiss();
//                                            }
//                                        }
//                                    })
//                                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                                        @Override
//                                        public void onClick(DialogInterface dialog, int which) {
//                                            if (!editTextLat.getText().toString().equals("")&&!editTextLng.getText().toString().equals("")){
//                                                switch (pointTypees){
//                                                    case GCJ02:
//                                                        ShowPoint(editTextLng.getText().toString(),editTextLat.getText().toString());
//                                                        break;
//                                                    case BD09:
//                                                        //将BD09转成GCJ02
//                                                        double lonlat[]= Gcj022Bd09.bd09Decrypt(Double.parseDouble(editTextLat.getText().toString()),
//                                                                Double.parseDouble(editTextLng.getText().toString()));
//                                                        ShowPoint(lonlat[0]+"",lonlat[1]+"");
//                                                        break;
//                                                    case ARCGIS:
//                                                        ShowPointArcgis(editTextLng.getText().toString(),editTextLat.getText().toString());
//                                                        break;
//                                                }
//                                            }else {
//                                                Toast.makeText(mContext,"经纬度不能为空", Toast.LENGTH_LONG).show();
//                                            }
//
//                                            Toast.makeText(mContext, "坐标+" + editTextLat.getText().toString() + "\n" + editTextLng.getText().toString(), Toast.LENGTH_SHORT).show();
//                                        }
//                                    }).show();
//
//                        } else if (items_1[which2].equals("手绘点")) {
//                            if (mapMeterMoveScope == null) {
//                                mapMeterMoveScope = new MapMeterMoveScope(mContext, moveMapView, wpiId,isCompleteSubmit);
//                            } else {
//                                mapMeterMoveScope.clearAllLayer();
//                            }
//                            mapMeterMoveScope.setDrawType(MapMeterMoveScope.POINT);
//                        }
//                    }
//                }).show();
//    }

    /**
     * 输入坐标并转成地方坐标系
     * @param editTextLat
     * @param editTextLng
     */
    private void ShowPoint(String editTextLng, String editTextLat){
        grapPayer.removeAll();
        LocationInfo locationInfo = new LocationInfo();
        if (editTextLat != null && editTextLng != null) {

            locationInfo.setLatitude(Double.parseDouble(editTextLat));
            locationInfo.setLongitude(Double.parseDouble(editTextLng));
            Point point = TranslateCoordinateFactory.gpsTranslate(AppContext.getInstance().getCoordTransform(),
                    moveMapView.getSpatialReference(), locationInfo.getLatitude(),
                    locationInfo.getLongitude(),0);
            //点
            PictureMarkerSymbol pointSymbol = new PictureMarkerSymbol(mContext.getResources().getDrawable(R.drawable.dot3));
            Graphic grahic = new Graphic(point, pointSymbol);
            grapPayer.addGraphic(grahic);
            TextView tv = new TextView(mContext);
            final String x2 = String.valueOf(point.getX());
            final String y2 = String.valueOf(point.getY());
            tv.setText("x=" + x2 + "\n" + "y+" + y2);
            pointList.add(point.getX());
            pointList.add(point.getY());
            listPoint.remove(listPoint);
            listPoint.add(pointList);
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(mContext, "测试", Toast.LENGTH_SHORT).show();

                    //每次只能提一个点
//                    String Point =x2+","+y2;
//                    HashMap<String, String> params = new HashMap<String, String>();
//                    Intent newFormInfo = new Intent(
//                            mContext, RunForm.class);
//                    newFormInfo.putExtra("template",
//                            "ins_hidden_trouble_form_zs.xml");
//                    newFormInfo.putExtra("moveTable",
//                            true);
//                    params.put("wpId", wpId);
////                    if(drawType.equals(POINT)){
//                    params.put("graph", "point");
////                    }
//                    params.put("PointList", Point);
//                    params.put("moveType", "point");
//
//                    newFormInfo.putExtra("moveparams", params);
//                    startActivity(newFormInfo);
                    grapPayer.removeAll();
                    moveMapView.getCallout().hide();
                }
            });
            moveMapView.getCallout().hide();
            moveMapView.getCallout().show(point, tv);
        }
    }

    private void ShowPointArcgis(String x, String y){
        grapPayer.removeAll();
        Point point=new Point(Double.parseDouble(x), Double.parseDouble(y));
        //点
        PictureMarkerSymbol pointSymbol = new PictureMarkerSymbol(mContext.getResources().getDrawable(R.drawable.dot3));
        Graphic grahic = new Graphic(point, pointSymbol);
        grapPayer.addGraphic(grahic);
        TextView tv = new TextView(mContext);
        final String x2 = String.valueOf(point.getX());
        final String y2 = String.valueOf(point.getY());
        tv.setText("x=" + x2 + "\n" + "y+" + y2);
        pointList.add(point.getX());
        pointList.add(point.getY());
        listPoint.remove(listPoint);
        listPoint.add(pointList);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "测试", Toast.LENGTH_SHORT).show();
                //每次只能提一个点
//                String Point =x2+","+y2;
//                HashMap<String, String> params = new HashMap<String, String>();
//                Intent newFormInfo = new Intent(
//                        mContext, RunForm.class);
//                newFormInfo.putExtra("template",
//                        "ins_hidden_trouble_form_zs.xml");
//                newFormInfo.putExtra("moveTable",
//                        true);
//                params.put("wpId", wpId);
////                    if(drawType.equals(POINT)){
//                params.put("graph", "point");
////                    }
//                params.put("PointList", Point);
//                params.put("moveType", "point");
//
//                newFormInfo.putExtra("moveparams", params);
//                startActivity(newFormInfo);
                grapPayer.removeAll();
                moveMapView.getCallout().hide();
            }
        });
        moveMapView.getCallout().hide();
        moveMapView.getCallout().show(point, tv);
    }

    private void Caixian(final String wpiId, final String isCompleteSubmit) {
        final String[] items_2 = {"轨迹采线", "手绘线", "反向绘线"};
        new AlertDialog.Builder(
                mContext)
                .setTitle("你选择采集方式是:" + "采线")
                .setItems(items_2, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(
                            DialogInterface dialog,
                            int which2) {
                        // TODO Auto-generated method stub
                        if (items_2[which2].equals("轨迹采线")) {

                        } else if (items_2[which2].equals("拐点采线")) {

                        } else if (items_2[which2].equals("手绘线")) {
                            if (mapMeterMoveScope == null) {
                                mapMeterMoveScope = new MapMeterMoveScope(mContext, moveMapView, wpiId,isCompleteSubmit);
                            } else {
                                mapMeterMoveScope.clearAllLayer();
                            }
                            mapMeterMoveScope.setDrawType(MapMeterMoveScope.LINE);
                        } else if (items_2[which2].equals("反向绘线")) {

                        } else if (items_2[which2].equals("属性赋值")) {

                        } else if (items_2[which2].equals("线生成")) {

                        }
                    }
                }).show();
    }

    /**
     * 绘制已完成的点
     * @param pointStr 点坐标
     */
    public void showCompletePoint(String pointStr){
        String[] points= pointStr.split(",");
        Point point=new Point(Double.parseDouble(points[0]), Double.parseDouble(points[1]));
        PictureMarkerSymbol pointSymbol = new PictureMarkerSymbol(mContext.getResources().getDrawable(R.drawable.dot1));
        Graphic grahic = new Graphic(point, pointSymbol);
        grapPayerW.addGraphic(grahic);

    }

    public void showCompleteLine(String pointsStr){
        Graphic graphic = null;
        String[] points= pointsStr.split(":");
        List<String> pointArray= Arrays.asList(points);
        Point point = new Point();
        Polyline polylines = new Polyline();
        for (int i=0;i<pointArray.size();i++){
            if (i==0){
                String pointSta=pointArray.get(0);
                String[] pointS=pointSta.split(",");
                point.setXY(Double.parseDouble(pointS[0]), Double.parseDouble(pointS[1]));
                polylines.startPath(point);
                PictureMarkerSymbol pointSymbol = new PictureMarkerSymbol(mContext.getResources().getDrawable(R.drawable.dot0));
                Graphic grahicd = new Graphic(point,pointSymbol);
                graphic = new Graphic(point, new SimpleLineSymbol(
                        Color.RED, 5));
            }else{
                String pointSta=pointArray.get(i);
                String[] pointS=pointSta.split(",");
                point.setXY(Double.parseDouble(pointS[0]), Double.parseDouble(pointS[1]));
                polylines.lineTo(point);
            }
            SimpleLineSymbol lineSymbol = new SimpleLineSymbol(Color.YELLOW, 5);
            Graphic g = new Graphic(polylines, lineSymbol);
            grapLayerW.addGraphic(g);
        }
        moveMapView.getCallout().hide();

    }

}
