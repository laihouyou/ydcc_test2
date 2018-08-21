package com.movementinsome.caice.activity;

import com.movementinsome.kernel.activity.FullActivity;

/**
 * Created by zzc on 2017/6/10.
 */

public class NewMap  extends FullActivity{

//    private BaiduMap map;
//    private MapView mMapView;
//
//    private MarkerOptions markerOption;
//    private BitmapDescriptor bitmap;
//    private Marker marker;
//    private TextOptions textOption ;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_new_map);
//        initView();
//    }
//
//    private void initView() {
//        mMapView= (MapView) findViewById(R.id.mapview);
//        map=mMapView.getMap();
//
//        Intent intent=getIntent();
//        String dateType=intent.getStringExtra("dateType");
//        String facName=intent.getStringExtra("facName");
//        String longitude=intent.getStringExtra("longitude");
//        String latitude=intent.getStringExtra("latitude");
//        String pointList=intent.getStringExtra("pointList");
//
//        if (dateType!=null&&dateType.equals(MapMeterMoveScope.POINT)){
//            if (longitude!=null&&!longitude.equals("")&&latitude!=null&&!latitude.equals("")){
//                LatLng cenpt=new LatLng(Double.parseDouble(latitude),Double.parseDouble(longitude));
//
//                //构建Marker图标
//                bitmap = BitmapDescriptorFactory
//                        .fromResource(R.drawable.icon_marka);
//
//                //构建MarkerOption，用于在地图上添加Marker
//                markerOption = new MarkerOptions()
//                        .position(cenpt)
//                        .icon(bitmap);
//                //在地图上添加Marker，并显示
//                marker = (Marker) map.addOverlay(markerOption);
//
//                //构建文字Option对象，用于在地图上添加文字
//                textOption = new TextOptions()
//                        .fontSize(24)
//                        .align(TextOptions.ALIGN_CENTER_HORIZONTAL, TextOptions.ALIGN_TOP)
//                        .fontColor(0xFFFF00FF)
//                        .text((facName!=null&&!facName.equals(""))?facName:" ")
//                        .position(cenpt);
//                //在地图上添加该文字对象并显示
//                 map.addOverlay(textOption);
//
//                MapStatus.Builder builder = new MapStatus.Builder();
//                builder.target(cenpt);
//                builder.zoom(18.0f);
//                map.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
//
//                TextView infoTv = new TextView(this);
//                infoTv.setTextColor(getResources().getColor(R.color.red));
//                infoTv.setGravity(Gravity.CENTER);
//                infoTv.setPadding(10, 10, 10, 10);
//                infoTv.setTextColor(getResources().getColor(R.color.white));
//                infoTv.setBackgroundResource(R.color.darkblue1);
//                DecimalFormat df = new DecimalFormat("0.000000");    //保留6为有效数字
//                infoTv.setText("经度:" + df.format(cenpt.longitude) + "\n" + "纬度:" + df.format(cenpt.latitude));
//                InfoWindow infoWindow = new InfoWindow(infoTv, cenpt, -80);
//                map.showInfoWindow(infoWindow);
//
//
//            }
//        }else if (dateType!=null&&dateType.equals(MapMeterMoveScope.LINE)){
//            if (pointList!=null&&!pointList.equals("")){
//                String [] pointDate=pointList.split(",");
//                if (pointDate.length>=2){
//                    List<LatLng> latlngs=new ArrayList<>();
//                    for (int i=0;i<pointDate.length;i++){
//                        LatLng latlng=new LatLng(Double.parseDouble(pointDate[i].split(" ")[1]),
//                                Double.parseDouble(pointDate[i].split(" ")[0]));
//                        latlngs.add(latlng);
//                    }
//
//                    // 画线 markerOptions画图
//                    PolylineOptions polylineOptions = new PolylineOptions();
//                    // 画线用到的点
//                    polylineOptions.points(latlngs);
//                    polylineOptions.color(0xAABBAA00);
//                    polylineOptions.width(10);
//                    map.addOverlay(polylineOptions);
//
//                    for (int i = 1; i < latlngs.size(); i++) {
//                        double lon = (latlngs.get(i - 1).longitude + latlngs.get(i).longitude) / 2;
//                        double lat = (latlngs.get(i - 1).latitude + latlngs.get(i).latitude) / 2;
//                        LatLng latLng = new LatLng(lat, lon);
//                        //构建文字Option对象，用于在地图上添加文字
//                        textOption = new TextOptions()
//                                .fontSize(25)
//                                .fontColor(this.getResources().getColor(R.color.crimson))
//                                .text(facName)
//                                .position(latLng);
//                        //在地图上添加该文字对象并显示
//                        map.addOverlay(textOption);
//                    }
//
//                    MapStatus.Builder builder = new MapStatus.Builder();
//                    builder.target(latlngs.get(latlngs.size()/2));
//                    builder.zoom(17.0f);
//                    map.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
//                }
//            }
//        }
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
//        mMapView.onDestroy();
//    }
//    @Override
//    protected void onResume() {
//        super.onResume();
//        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
//        mMapView.onResume();
//    }
//    @Override
//    protected void onPause() {
//        super.onPause();
//        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
//        mMapView.onPause();
//    }
}

