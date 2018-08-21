package com.movementinsome.caice.util;

public class BaiduMapDraw {
//    private static BaiduMap mBaiduMap;
//    private String drawType;
//    private static List<Overlay> lineOverlayList = new ArrayList<Overlay>();
//    private Overlay markerOverlay;
//    private InfoWindow mInfoWindow;
//    private LatLng dragStart;
//    private static List<LatLng> pointList = new ArrayList();// 画图的坐标
//    private static List<LatLng> lineList = new ArrayList();// 画图的坐标
//    private Context context;
//    private Activity activity;
//
//    public BaiduMapDraw(final BaiduMap mBaiduMap, final String drawType, final Context context, final Activity activity) {
//        this.mBaiduMap = mBaiduMap;
//        this.drawType = drawType;
//        this.context = context;
//        this.activity = activity;
//        /**
//         * 覆盖物点击事件
//         */
//        mBaiduMap.setOnMarkerClickListener(new OnMarkerClickListener() {
//
//            @Override
//            public boolean onMarkerClick(Marker marker) {
//                // TODO Auto-generated method stub
////                View view = getInfoWindoView(marker, marker.getPosition());
////                if (view != null) {
////                    mInfoWindow = new InfoWindow(view,
////                            marker.getPosition(), -ActivityUtil.getWindowsHetght(activity) * 1 / 21);
////                    mBaiduMap.showInfoWindow(mInfoWindow);
////                }
//                return false;
//            }
//
//        });
//
//
//        /**
//         * 覆盖物移动事件
//         */
//        mBaiduMap.setOnMarkerDragListener(new OnMarkerDragListener() {
//            @Override
//            public void onMarkerDragStart(Marker marker) {
//
//            }
//
//            @Override
//            public void onMarkerDragEnd(Marker marker) {
//                List<LatLng> lineListOld = new ArrayList<LatLng>();
//                if (marker.getExtraInfo() != null) {
//                    if (marker.getExtraInfo().getString("drawType").equals(ConstantDate.LINE)) {
//                        if (lineList.size() >= 2) {
//                            for (int i = 0; i < lineList.size(); i++) {
//                                if (lineList.get(i).latitude == marker.getExtraInfo().getDouble("positionY") || lineList.get(i).longitude == marker.getExtraInfo().getDouble("positionX")) {
//                                    lineListOld.add(marker.getPosition());
//                                } else {
//                                    lineListOld.add(lineList.get(i));
//                                }
//                            }
//                            Bundle bundle = marker.getExtraInfo();
//                            bundle.putDouble("positionX", marker.getPosition().longitude);
//                            bundle.putDouble("positionY", marker.getPosition().latitude);
//                            marker.setExtraInfo(bundle);
//                            lineList.removeAll(lineList);
//                            lineList.addAll(lineListOld);
//                            for (int j = 0; j < lineOverlayList.size(); j++) {
//                                lineOverlayList.get(j).remove();
//                            }
//                            lineOverlayList.removeAll(lineOverlayList);
//                            // 画线 markerOptions画图
//                            PolylineOptions polylineOptions = new PolylineOptions();
//                            // 画线用到的点
//                            polylineOptions.points(lineList);
//                            polylineOptions.color(0xAABBAA00);
//                            polylineOptions.width(10);
//                            lineOverlayList.add(mBaiduMap.addOverlay(polylineOptions));
//                        }
//                    }
//                }
//                Toast.makeText(
//                        activity,
//                        "新位置：" + marker.getPosition().latitude + ", "
//                                + marker.getPosition().longitude,
//                        Toast.LENGTH_LONG).show();
//            }
//
//            @Override
//            public void onMarkerDrag(Marker arg0) {
//
//            }
//        });
//
//        /**
//         * 地图点击件事
//         */
//        mBaiduMap.setOnMapClickListener(new OnMapClickListener() {
//
//            @Override
//            public boolean onMapPoiClick(MapPoi arg0) {
//                // TODO Auto-generated method stub
//                return false;
//            }
//
//            @Override
//            public void onMapClick(LatLng position) {
//                // TODO Auto-generated method stub
//                mBaiduMap.hideInfoWindow();
//                if (drawType.equals(ConstantDate.DRAWPOINT)) {
//                    if (markerOverlay != null) {
//                        markerOverlay.remove();
//                    }
//                    pointList.removeAll(pointList);
//                    pointList.add(position);
//                    Bundle bundle = new Bundle();
//                    bundle.putString("drawType", ConstantDate.POINT);
//                    bundle.putDouble("positionX", position.longitude);
//                    bundle.putDouble("positionY", position.latitude);
//                    BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.drawable.icon_marka);
//                    OverlayOptions option = new MarkerOptions().draggable(true).position(position).icon(bitmap).extraInfo(bundle);
//                    markerOverlay = mBaiduMap.addOverlay(option);
//                } else if (drawType.equals(ConstantDate.DRAWLINE)) {
//                    lineList.add(position);
//                    Bundle bundle = new Bundle();
//                    bundle.putString("drawType", ConstantDate.LINE);
//                    bundle.putDouble("positionX", position.longitude);
//                    bundle.putDouble("positionY", position.latitude);
//                    BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.drawable.icon_marka);
//                    OverlayOptions option = new MarkerOptions().draggable(true).position(position).icon(bitmap).extraInfo(bundle);
//                    markerOverlay = mBaiduMap.addOverlay(option);
//                    if (lineList.size() >= 2) {
//                        List<LatLng> lineList2 = new ArrayList();
//                        for (int i = 0; i < lineList.size(); i++) {
//                            if (lineList.size() - 2 <= i) {
//                                lineList2.add(lineList.get(i));
//                            }
//                        }
//                        // 画线 markerOptions画图
//                        PolylineOptions polylineOptions = new PolylineOptions();
//                        // 画线用到的点
//                        polylineOptions.points(lineList2);
//                        polylineOptions.color(0xAABBAA00);
//                        polylineOptions.width(10);
//                        lineOverlayList.add(mBaiduMap.addOverlay(polylineOptions));
//                    }
//                }
//            }
//        });
//    }
////
////    private View getInfoWindoView(final Marker marker, final LatLng latLng) {
////        View view = View.inflate(context, R.layout.infowindor_view, null);
////        TextView infoWindo_particulars_td_tv = (TextView) view.findViewById(R.id.infoWindo_particulars_td_tv);
////        infoWindo_particulars_td_tv.setOnClickListener(new OnClickListener() {
////
////            @Override
////            public void onClick(View v) {
////                // TODO Auto-generated method stub
////                Toast.makeText(context, marker.getExtraInfo().getString("drawType"), Toast.LENGTH_SHORT).show();
////
////            }
////        });
////
////        TextView infoWindo_particulars_ig_tv = (TextView) view.findViewById(R.id.infoWindo_particulars_ig_tv);
////        infoWindo_particulars_ig_tv.setOnClickListener(new OnClickListener() {
////
////            @Override
////            public void onClick(View v) {
////                // TODO Auto-generated method stub
////                mBaiduMap.hideInfoWindow();
////            }
////        });
////        return view;
////    }
//
//    public static void queryDrawDate() {
//
//    }
//
//    public static void remoLayout() {
//        mBaiduMap.clear();
//        pointList.removeAll(pointList);
//        lineList.removeAll(lineList);
//    }
//
//    /**
//     * 绘制百度线
//     * @param mBaiduMap
//     * @param points 坐标集合
//     * @param projectId     所属工程ID
//     * @return
//     */
//    public static Polyline drawLine(BaiduMap mBaiduMap, List<LatLng> points, String projectId,
//                                    ProjectVo projectVo, Context mContext) {
//        try {
//            BitmapDescriptor mBlueTexture = BitmapDescriptorFactory.fromAsset("icon_road_blue_arrow.png");
//
//            // 画线 markerOptions画图
//            PolylineOptions polylineOptions = new PolylineOptions();
//            // 画线用到的点
//            polylineOptions.points(points);
//            polylineOptions.color(projectVo.getProjectId().equals(projectId)
//                    ?mContext.getResources().getColor(R.color.color_bbaa00)
//                    :mContext.getResources().getColor(R.color.olivedrab));
//            polylineOptions.width(10);
//            polylineOptions.dottedLine(true).customTexture(mBlueTexture);
//            Polyline mPolyline = (Polyline) mBaiduMap.addOverlay(polylineOptions);
//            Bundle bundle=new Bundle();
////            bundle.putString("facNum",facNum);
////            bundle.putString("drawType",drawType);
//            bundle.putString(OkHttpParam.PROJECT_ID,projectId);
////            bundle.putStringArrayList("ids",(ArrayList<String>) ids);
////            bundle.putStringArrayList("facNumLines",(ArrayList<String>) facNumLines);
////            bundle.putStringArrayList("facDrawNum",(ArrayList<String>) facDrawNum);
////            bundle.putStringArrayList("facDrawNums",(ArrayList<String>) facDrawNums);
//            mPolyline.setExtraInfo(bundle);
//            return mPolyline;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
}
