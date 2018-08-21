package com.baidu.pano;

/**
 * 全景Demo主Activity
 */
//public class PanoDemoMain extends Activity {
//
//    private static final String LTAG = "BaiduPanoSDKDemo";
//
//    private PanoramaView mPanoView;
//    private TextView textTitle;
//    private Button btnImageMarker, btnTextMarker;// 添加移除marker测试
//    private Button btnIsShowArrow, btnArrowStyle01, btnArrowStyle02;// 全景其他功能测试
//    private Button btnIsShowInoorAblum;
//
//    private View seekPitchLayout, seekHeadingLayout, seekLevelLayout;
//    private SeekBar seekPitch, seekHeading, seekLevel;// 俯仰角,偏航角,全景图缩放测试
//
//    private boolean isAddImageMarker = false;
//    private boolean isAddTextMarker = false;
//    private boolean isShowArrow = false;
//    private boolean isShowAblum = true;
//
//    private double lat;
//    private double lon;
//    private SavePointVo savePointVo;
////    private List<SavePointVo> savePointVoList;
//    private Drawable bitmap;
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        // 先初始化BMapManager
//        initBMapManager();
//        setContentView(R.layout.panodemo_main);
//
//        Intent intent = getIntent();
//        if (intent != null) {
//            lon=intent.getDoubleExtra("lon",0);
//            lat=intent.getDoubleExtra("lat",0);
//            savePointVo= (SavePointVo) intent.getSerializableExtra(OkHttpParam.SAVEPOINTVO);
////            try {
////                Dao<SavePointVo,Long> savePointVoLongDao=AppContext.getInstance().getAppDbHelper().getDao(SavePointVo.class);
////                savePointVoList=savePointVoLongDao.queryForEq(OkHttpParam.PROJECT_ID,savePointVo.getProjectId());
////            } catch (SQLException e) {
////                e.printStackTrace();
////            }
//        }
//
//        initView();
//        testPanoByType();
//    }
//
//    private void initBMapManager() {
//        if (AppContext.getInstance().mBMapManager == null) {
//            AppContext.getInstance().mBMapManager = new BMapManager(AppContext.getInstance());
////            AppContext.getInstance().mBMapManager.init(new PanoDemoApplication.MyGeneralListener());
//        }
//    }
//
//    private void initView() {
//        textTitle = (TextView) findViewById(R.id.panodemo_main_title);
//        mPanoView = (PanoramaView) findViewById(R.id.panorama);
//        btnImageMarker = (Button) findViewById(R.id.panodemo_main_btn_imagemarker);
//        btnTextMarker = (Button) findViewById(R.id.panodemo_main_btn_textmarker);
//
//        btnIsShowArrow = (Button) findViewById(R.id.panodemo_main_btn_showarrow);
//        btnArrowStyle01 = (Button) findViewById(R.id.panodemo_main_btn_arrowstyle_01);
//        btnArrowStyle02 = (Button) findViewById(R.id.panodemo_main_btn_arrowstyle_02);
//        btnIsShowInoorAblum = (Button) findViewById(R.id.panodemo_main_btn_indoor_album);
//
//        btnImageMarker.setOnClickListener(new OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                if (!isAddImageMarker) {
////                    addImageMarker();
//                    btnImageMarker.setText("删除图片标注");
//                } else {
//                    removeImageMarker();
//                    btnImageMarker.setText("添加图片标注");
//                }
//                isAddImageMarker = !isAddImageMarker;
//            }
//        });
//
//        btnTextMarker.setOnClickListener(new OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                if (!isAddTextMarker) {
////                    addTextMarker();
//                    btnTextMarker.setText("删除文字标注");
//                } else {
//                    removeTextMarker();
//                    btnTextMarker.setText("添加文字标注");
//                }
//                isAddTextMarker = !isAddTextMarker;
//            }
//        });
//        addTextMarker();
//        addImageMarker();
//
//        seekPitchLayout = findViewById(R.id.seekpitch_ly);
//        seekHeadingLayout = findViewById(R.id.seekheading_ly);
//        seekLevelLayout = findViewById(R.id.seeklevel_ly);
//        seekPitch = (SeekBar) findViewById(R.id.seekpitch);
//        seekLevel = (SeekBar) findViewById(R.id.seeklevel);
//        seekHeading = (SeekBar) findViewById(R.id.seekheading);
//
//        seekPitch.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//
//            @Override
//            public void onStopTrackingTouch(SeekBar seekBar) {
//            }
//
//            @Override
//            public void onStartTrackingTouch(SeekBar seekBar) {
//            }
//
//            @Override
//            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                mPanoView.setPanoramaPitch(progress - 90);
//            }
//        });
//        seekHeading.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//
//            @Override
//            public void onStopTrackingTouch(SeekBar seekBar) {
//            }
//
//            @Override
//            public void onStartTrackingTouch(SeekBar seekBar) {
//            }
//
//            @Override
//            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                mPanoView.setPanoramaHeading(progress);
//            }
//        });
//        seekLevel.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//            @Override
//            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                mPanoView.setPanoramaZoomLevel(progress + 1);
//            }
//
//            @Override
//            public void onStartTrackingTouch(SeekBar seekBar) {
//
//            }
//
//            @Override
//            public void onStopTrackingTouch(SeekBar seekBar) {
//
//            }
//        });
//
//    }
//
//    private void testPanoByType() {
//        mPanoView.setShowTopoLink(true);
//        hideMarkerButton();
//        hideSeekLayout();
//        hideOtherLayout();
//        hideIndoorAblumLayout();
//
//        // 测试回调函数,需要注意的是回调函数要在setPanorama()之前调用，否则回调函数可能执行异常
//        mPanoView.setPanoramaViewListener(new PanoramaViewListener() {
//
//            @Override
//            public void onLoadPanoramaBegin() {
//                Log.i(LTAG, "onLoadPanoramaStart...");
//            }
//
//            @Override
//            public void onLoadPanoramaEnd(String json) {
//                Log.i(LTAG, "onLoadPanoramaEnd : " + json);
//            }
//
//            @Override
//            public void onLoadPanoramaError(String error) {
//                Log.i(LTAG, "onLoadPanoramaError : " + error);
//            }
//
//            @Override
//            public void onDescriptionLoadEnd(String json) {
//
//            }
//
//            @Override
//            public void onMessage(String msgName, int msgType) {
//
//            }
//
//            @Override
//            public void onCustomMarkerClick(String key) {
//
//            }
//        });
//        if (lon!=0&&lat!=0){
//            mPanoView.setPanorama(lon, lat);
//        }
//
////        if (type == PanoDemoActivity.PID) {
////            textTitle.setText(R.string.demo_desc_pid);
////
////            mPanoView.setPanoramaImageLevel(PanoramaView.ImageDefinition.ImageDefinitionHigh);
////            String pid = "0900220000141205144547300IN";
////            mPanoView.setPanorama(pid);
////        } else if (type == PanoDemoActivity.GEO) {
////            textTitle.setText(R.string.demo_desc_geo);
////
////            double lat = 39.945;
////            double lon = 116.404;
////            mPanoView.setPanorama(lon, lat);
////        } else if (type == PanoDemoActivity.MERCATOR) {
////            textTitle.setText(R.string.demo_desc_mercator);
////
////            mPanoView.setPanoramaImageLevel(PanoramaView.ImageDefinition.ImageDefinitionHigh);
////            int mcX = 12971348;
////            int mcY = 4826239;
////            mPanoView.setPanorama(mcX, mcY);
////        } else if (type == PanoDemoActivity.UID_STREET) {
////            // 默认相册
////            IndoorAlbumPlugin.getInstance().init();
////
////            textTitle.setText(R.string.demo_desc_uid_street);
////
////            mPanoView.setPanoramaZoomLevel(5);
////            mPanoView.setArrowTextureByUrl("http://d.lanrentuku.com/down/png/0907/system-cd-disk/arrow-up.png");
////            mPanoView.setPanoramaImageLevel(PanoramaView.ImageDefinition.ImageDefinitionMiddle);
////            String uid = "7aea43b75f0ee3e17c29bd71";
////            mPanoView.setPanoramaByUid(uid, PanoramaView.PANOTYPE_STREET);
////        } else if (type == PanoDemoActivity.UID_INTERIOR) {
////            // 默认相册
////            IndoorAlbumPlugin.getInstance().init();
////
////            textTitle.setText(R.string.demo_desc_uid_interior);
////            showIndoorAblumLayout();
////
////            mPanoView.setPanoramaByUid("7c5e480b109e67adacb22aae", PanoramaView.PANOTYPE_INTERIOR);
////
////            btnIsShowInoorAblum.setOnClickListener(new OnClickListener() {
////
////                @Override
////                public void onClick(View v) {
////                    if (!isShowAblum) {
////                        btnIsShowInoorAblum.setText("隐藏内景相册");
////                        mPanoView.setIndoorAlbumVisible();
////                    } else {
////                        btnIsShowInoorAblum.setText("显示内景相册");
////                        mPanoView.setIndoorAlbumGone();
////                    }
////                    isShowAblum = !isShowAblum;
////                }
////            });
////        } else if (type == PanoDemoActivity.UID_STREET_CUSTOMALBUM) {
////            // 自定义相册
////            IndoorAlbumPlugin.getInstance().init(new IndoorAlbumCallback() {
////
////                @Override
////                public View loadAlbumView(PanoramaView panoramaView, EntryInfo info) {
////                    if (panoramaView != null && info != null) {
////                        View albumView = LayoutInflater.from(panoramaView.getContext())
////                                .inflate(R.layout.baidupano_photoalbum_container, null);
////                        if (albumView != null) {
////                            AlbumContainer mAlbumContainer =
////                                    (AlbumContainer) albumView.findViewById(R.id.page_pano_album_view);
////                            TextView mTvAddress = (TextView) albumView.findViewById(R.id.page_pano_album_address);
////                            mAlbumContainer.setControlView(panoramaView, mTvAddress);
////                        }
////                        LayoutParams lp = (LayoutParams) albumView.getLayoutParams();
////                        if (lp == null) {
////                            lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
////                        }
////                        lp.gravity = Gravity.BOTTOM;
////                        albumView.setLayoutParams(lp);
////                        AlbumContainer albumContainer =
////                                (AlbumContainer) albumView.findViewById(R.id.page_pano_album_view);
////                        albumContainer.startLoad(panoramaView.getContext(), info);
////                        return albumView;
////                    } else {
////                        return null;
////                    }
////                }
////            });
////
////            textTitle.setText(R.string.demo_desc_uid_street_customalbum);
////
////            mPanoView.setPanoramaZoomLevel(5);
////            mPanoView.setPanoramaImageLevel(PanoramaView.ImageDefinition.ImageDefinitionMiddle);
////            String uid = "7aea43b75f0ee3e17c29bd71";
////            mPanoView.setPanoramaByUid(uid, PanoramaView.PANOTYPE_STREET);
////        } else if (type == PanoDemoActivity.MARKER) {
////            textTitle.setText(R.string.demo_desc_marker);
////
////            showMarkerButton();
////            mPanoView.setPanorama("0900220001150514054806738T5");
////            mPanoView.setShowTopoLink(false);
////        } else if (type == PanoDemoActivity.OTHER) {
////            textTitle.setText(R.string.demo_desc_other);
////
////            showSeekLayout();
////            showOtherLayout();
////
////            mPanoView.setPanoramaImageLevel(PanoramaView.ImageDefinition.ImageDefinitionHigh);
////            String pid = "0900220001150514054806738T5";
////            mPanoView.setPanorama(pid);
////
////            // 测试获取内景的相册描述信息和服务推荐描述信息
////            testPanoramaRequest();
////
////            btnIsShowArrow.setOnClickListener(new OnClickListener() {
////
////                @Override
////                public void onClick(View v) {
////                    if (!isShowArrow) {
////                        mPanoView.setShowTopoLink(false);
////                        btnIsShowArrow.setText("显示全景箭头");
////                    } else {
////                        mPanoView.setShowTopoLink(true);
////                        btnIsShowArrow.setText("隐藏全景箭头");
////                    }
////                    isShowArrow = !isShowArrow;
////                }
////            });
////
////            btnArrowStyle01.setOnClickListener(new OnClickListener() {
////
////                @Override
////                public void onClick(View v) {
////                    mPanoView.setArrowTextureByUrl("http://d.lanrentuku.com/down/png/0907/system-cd-disk/arrow-up.png");
////                }
////            });
////
////            btnArrowStyle02.setOnClickListener(new OnClickListener() {
////
////                @Override
////                public void onClick(View v) {
////                    Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.street_arrow);
////                    mPanoView.setArrowTextureByBitmap(bitmap);
////                }
////            });
////        }
//
//    }
//
//    private void testPanoramaRequest() {
//
//        new Thread(new Runnable() {
//
//            @Override
//            public void run() {
//                PanoramaRequest panoramaRequest = PanoramaRequest.getInstance(PanoDemoMain.this);
//
//                String pid = "01002200001307201550572285B";
//                Log.e(LTAG, "PanoramaRecommendInfo");
//                Log.i(LTAG, panoramaRequest.getPanoramaRecommendInfo(pid).toString());
//
//                String iid = "978602fdf6c5856bddee8b62";
//                Log.e(LTAG, "PanoramaByIIdWithJson");
//                Log.i(LTAG, panoramaRequest.getPanoramaByIIdWithJson(iid).toString());
//
//                // 通过百度经纬度坐标获取当前位置相关全景信息，包括是否有外景，外景PID，外景名称等
//                double lat = 40.029233;
//                double lon = 116.32085;
//                BaiduPanoData mPanoDataWithLatLon = panoramaRequest.getPanoramaInfoByLatLon(lon, lat);
//                Log.e(LTAG, "PanoDataWithLatLon");
//                Log.i(LTAG, mPanoDataWithLatLon.getDescription());
//
//                // 通过百度墨卡托坐标获取当前位置相关全景信息，包括是否有外景，外景PID，外景名称等
//                int x = 12948920;
//                int y = 4842480;
//                BaiduPanoData mPanoDataWithXy = panoramaRequest.getPanoramaInfoByMercator(x, y);
//
//                Log.e(LTAG, "PanoDataWithXy");
//                Log.i(LTAG, mPanoDataWithXy.getDescription());
//
//                // 通过百度地图uid获取该poi下的全景描述信息，以此来判断此UID下是否有内景及外景
//                String uid = "bff8fa7deabc06b9c9213da4";
//                BaiduPoiPanoData poiPanoData = panoramaRequest.getPanoramaInfoByUid(uid);
//                Log.e(LTAG, "poiPanoData");
//                Log.i(LTAG, poiPanoData.getDescription());
//            }
//        }).start();
//
//    }
//
//    // 隐藏添加删除标注按钮
//    private void hideMarkerButton() {
//        btnImageMarker.setVisibility(View.GONE);
//        btnTextMarker.setVisibility(View.GONE);
//    }
//
//    // 显示添加删除标注按钮
//    private void showMarkerButton() {
//        btnImageMarker.setVisibility(View.VISIBLE);
//        btnTextMarker.setVisibility(View.VISIBLE);
//    }
//
//    // 隐藏设置俯仰角偏航角SeekBar
//    private void hideSeekLayout() {
//        seekPitchLayout.setVisibility(View.GONE);
//        seekHeadingLayout.setVisibility(View.GONE);
//        seekLevelLayout.setVisibility(View.GONE);
//    }
//
//    // 显示设置俯仰角偏航角SeekBar
//    private void showSeekLayout() {
//        seekPitchLayout.setVisibility(View.VISIBLE);
//        seekHeadingLayout.setVisibility(View.VISIBLE);
//        seekLevelLayout.setVisibility(View.VISIBLE);
//    }
//
//    // 隐藏其他功能测试
//    private void hideOtherLayout() {
//        btnIsShowArrow.setVisibility(View.GONE);
//        btnArrowStyle01.setVisibility(View.GONE);
//        btnArrowStyle02.setVisibility(View.GONE);
//    }
//
//    // 显示其他功能测试
//    private void showOtherLayout() {
//        btnIsShowArrow.setVisibility(View.VISIBLE);
//        btnArrowStyle01.setVisibility(View.VISIBLE);
//        btnArrowStyle02.setVisibility(View.VISIBLE);
//    }
//
//    // 隐藏内景相册测试
//    private void hideIndoorAblumLayout() {
//        btnIsShowInoorAblum.setVisibility(View.GONE);
//    }
//
//    // 显示内景相册测试
//    private void showIndoorAblumLayout() {
//        btnIsShowInoorAblum.setVisibility(View.VISIBLE);
//    }
//
//    private ImageMarker marker1;
////    private ImageMarker marker2;
//
//    /**
//     * 添加图片标注
//     */
//    private void addImageMarker() {
//        // 天安门西南方向
//        marker1 = new ImageMarker();
//        marker1.setMarkerPosition(new Point(Double.parseDouble(savePointVo.getLongitude())
//                , Double.parseDouble(savePointVo.getLatitude())));
//        marker1.setMarkerHeight(-1f);
//        if (savePointVo!=null){
//            String implementorName=savePointVo.getImplementorName();
//            if (implementorName!=null){
//                switch (implementorName) {
//                    case Constant.VALVE:    //阀门
//                        //构建Marker图标
//                        bitmap = getResources()
//                                .getDrawable(R.drawable.valve_inspection);
//                        break;
//                    case Constant.MUD_VALVE:    //排泥阀
//                        //构建Marker图标
//                        bitmap = getResources()
//                                .getDrawable(R.drawable.mud_valve);
//                        break;
//                    case Constant.VENT_VALVE:    //排气阀
//                        //构建Marker图标
//                        bitmap = getResources()
//                                .getDrawable(R.drawable.drain_tap);
//                        break;
//                    case Constant.WATER_METER:    //水表
//                        //构建Marker图标
//                        bitmap = getResources()
//                                .getDrawable(R.drawable.meter_reading);
//                        break;
//                    case Constant.FIRE_HYDRANT:    //消防栓
//                        //构建Marker图标
//                        bitmap = getResources()
//                                .getDrawable(R.drawable.hydrant);
//                        break;
//                    case Constant.DISCHARGE_OUTLET:    //出水口
//                        //构建Marker图标
//                        bitmap = getResources()
//                                .getDrawable(R.drawable.water_outlet);
//                        break;
//                    case Constant.PLUG_SEAL:    //封头堵坂
//                        //构建Marker图标
//                        bitmap =getResources()
//                                .getDrawable(R.drawable.plug_seal_plate);
//                        break;
//                    case Constant.NODE_BLACK:    //节点
//                        //构建Marker图标
//                        bitmap = getResources().getDrawable(R.drawable.node);
//                        break;
//                    case Constant.POOL:    //水池
//                        //构建Marker图标
//                        bitmap = getResources().getDrawable(R.drawable.pool);
//                        break;
//
//                    case Constant.METER_READING_UNDONE:    //水表(未完成)
//                        //构建Marker图标
//                        bitmap = getResources().getDrawable(R.drawable.meter_reading_undone);
//                        break;
//
//                    case Constant.METER_READING_COMPLETED:    //水表(已完成)
//                        //构建Marker图标
//                        bitmap = getResources().getDrawable(R.drawable.meter_reading_completed);
//                        break;
//                    default:
//                        //构建Marker图标
//                        bitmap = getResources().getDrawable(R.drawable.icon_marka);
//
//                        break;
//                }
//            }
//        }else {
//            //构建Marker图标
//            bitmap = getResources().getDrawable(R.drawable.icon_marka);
//        }
//        marker1.setMarker(bitmap);
//        marker1.setOnTabMarkListener(new OnTabMarkListener() {
//
//            @Override
//            public void onTab() {
//                Toast.makeText(PanoDemoMain.this, "图片MarkerA标注已被点击", Toast.LENGTH_SHORT).show();
//            }
//        });
//        // 天安门东北方向
////        marker2 = new ImageMarker();
////        marker2.setMarkerPosition(new Point(116.427116, 39.929718));
//////        marker2.setMarker(getResources().getDrawable(R.drawable.icon_markb));
////        marker2.setMarkerHeight(7);
////        marker2.setOnTabMarkListener(new OnTabMarkListener() {
////
////            @Override
////            public void onTab() {
////                Toast.makeText(PanoDemoMain.this, "图片MarkerB标注已被点击", Toast.LENGTH_SHORT).show();
////            }
////        });
//        mPanoView.addMarker(marker1);
////        mPanoView.addMarker(marker2);
//    }
//
//    /**
//     * 删除图片标注
//     */
//    private void removeImageMarker() {
//        mPanoView.removeMarker(marker1);
////        mPanoView.removeMarker(marker2);
//    }
//
//    private TextMarker textMark1;
////    private TextMarker textMark2;
//
//    /**
//     * 添加文本标注
//     */
//    private void addTextMarker() {
//        // 天安门西北方向
//        textMark1 = new TextMarker();
//        textMark1.setMarkerPosition(new Point(Double.parseDouble(savePointVo.getLongitude())
//                , Double.parseDouble(savePointVo.getLatitude())));
//        textMark1.setFontColor(0xFFFF0000);
//        textMark1.setText(savePointVo.getFacName());
//        textMark1.setFontSize(12);
////        textMark1.setBgColor(0xFFFFFFFF);
//        textMark1.setPadding(0, 40, 0, 0);
//        textMark1.setMarkerHeight(-1f);
//        textMark1.setOnTabMarkListener(new OnTabMarkListener() {
//
//            @Override
//            public void onTab() {
//                Toast.makeText(PanoDemoMain.this, "textMark1标注已被点击", Toast.LENGTH_SHORT).show();
//            }
//        });
//        // 天安门东南方向
////        textMark2 = new TextMarker();
////        textMark2.setMarkerPosition(new Point(116.409766, 39.911808));
////        textMark2.setFontColor(Color.RED);
////        textMark2.setText("你好marker");
////        textMark2.setFontSize(12);
////        textMark2.setBgColor(Color.BLUE);
////        textMark2.setPadding(10, 20, 15, 25);
////        textMark2.setMarkerHeight(10);
////        textMark2.setOnTabMarkListener(new OnTabMarkListener() {
////
////            @Override
////            public void onTab() {
////                Toast.makeText(PanoDemoMain.this, "textMark2标注已被点击", Toast.LENGTH_SHORT).show();
////            }
////        });
//        mPanoView.addMarker(textMark1);
////        mPanoView.addMarker(textMark2);
//    }
//
//    /**
//     * 删除文本标注
//     */
//    private void removeTextMarker() {
//        mPanoView.removeMarker(textMark1);
////        mPanoView.removeMarker(textMark2);
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//    }
//
//    @Override
//    protected void onDestroy() {
//        mPanoView.destroy();
//        super.onDestroy();
//    }
//
//}
