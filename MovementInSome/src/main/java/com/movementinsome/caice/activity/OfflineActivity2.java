package com.movementinsome.caice.activity;

import android.view.View;
import android.view.View.OnClickListener;

import com.movementinsome.kernel.activity.FullActivity;


public class OfflineActivity2 extends FullActivity implements OnClickListener{
	@Override
	public void onClick(View view) {

	}

//	private ImageView im_offline_back;
//	public TextView tv_offline_not_download;// 未下载
//	public TextView tv_offline_have_downloaded;// 已下载
//	public LinearLayout cl;
//	public LinearLayout lm;
//	public boolean isDownload = false;// 当前是否在下载
//	/**
//	 * 未下载列表
//	 */
//	private LinearLayout citylist_layout;
//	private LRecyclerView mRecyclerView;
//
//	private ExpandableItemAdapter mDataAdapter = null;
//
//	private LRecyclerViewAdapter mLRecyclerViewAdapter = null;
//
//	/**
//	 * 已下载列表
//	 */
//	private LinearLayout localmap_layout;
//	private static ListView localMapListView;
//
//	public static MKOfflineMap mOffline = null;
//	/**
//	 * 已下载的离线地图信息列表
//	 */
//	private static ArrayList<MKOLUpdateElement> localMapList = null;
//	private static LocalMapAdapter lAdapter = null;
////	private AllCitiesAdapter aAdapter;
//	private CommonExpandableListAdapter<MKOLSearchRecord,MKOLSearchRecord> adapter;
//
//	private List<MKOLSearchRecord> records2;
//
//	private MKOLUpdateElement e;
//
//	private int cityCode;		//当前定位城市ID
//
//	private List<Integer> offMapsCity;
//
//	private  List<MKOLSearchRecord> municipalitieRecordList;
//
//	private  List<ExChindVo> municipalitieExChilVoList;
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		// TODO 自动生成的方法存根
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_offline2);
//		mOffline = new MKOfflineMap();
//		mOffline.init(this);
//		initView();
//	}
//
//	private void initView() {
//		// TODO 自动生成的方法存根
//
//		im_offline_back = (ImageView) findViewById(R.id.im_offline_back);
//		tv_offline_not_download = (TextView) findViewById(R.id.tv_offline_not_download);
//		tv_offline_have_downloaded = (TextView) findViewById(R.id.tv_offline_have_downloaded);
//
//		im_offline_back.setOnClickListener(this);
//		tv_offline_not_download.setOnClickListener(this);
//		tv_offline_have_downloaded.setOnClickListener(this);
//
//		// 获取已下过的离线地图信息
//		localMapList = mOffline.getAllUpdateInfo();
//		if (localMapList == null) {
//			localMapList = new ArrayList<MKOLUpdateElement>();
//		}
//		offMapsCity=new ArrayList<>();
//		for (int k=0;k<localMapList.size();k++){
//			offMapsCity.add(localMapList.get(k).cityID);
//		}
//
//		mRecyclerView = (LRecyclerView) findViewById(R.id.allcitylist);
//
//		mDataAdapter = new ExpandableItemAdapter(this);
//		mDataAdapter.setDataList(generateData());
//
//		mLRecyclerViewAdapter = new LRecyclerViewAdapter(mDataAdapter);
//		mRecyclerView.setAdapter(mLRecyclerViewAdapter);
//
////		DividerDecoration divider = new DividerDecoration.Builder(this)
////				.setHeight(R.dimen.dp_1)
////				.setPadding(R.dimen.dp_4)
////				.setColorResource(R.color.split)
////				.build();
////		//mRecyclerView.setHasFixedSize(true);
////		mRecyclerView.addItemDecoration(divider);
//
//		mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
//
//		//禁用下拉刷新功能
//		mRecyclerView.setPullRefreshEnabled(false);
//
//		//添加头布局
//		if (municipalitieExChilVoList.size()>0){
//			for (int i = 0; i < municipalitieExChilVoList.size(); i++) {
//				final ExChindVo exChildVo=municipalitieExChilVoList.get(i);
//				View header = LayoutInflater.from(this).inflate(R.layout.item_all_cities_chins,(ViewGroup)findViewById(android.R.id.content), false);
//
//				TextView tv_city = (TextView) header.findViewById(R.id.tv_city);
//				tv_city.setText(exChildVo.getCtilName());
//				final TextView tv_in_the_download = (TextView) header.findViewById(R.id.tv_in_the_download);
//				TextView download_size = (TextView) header.findViewById(R.id.download_size);
//				download_size.setText(OfflineActivity2.formatDataSize(exChildVo.getDataSize())+"");
//				final TextView download_man_size = (TextView) header.findViewById(R.id.download_man_size);
//
//				ImageView download_icon = (ImageView) header.findViewById(R.id.download_icon);
//
//				if (exChildVo.getStatus()!=0){
//					tv_in_the_download.setText(exChildVo.getStatusStr());
//					tv_in_the_download.setVisibility(View.VISIBLE);
//
//					download_man_size.setText(exChildVo.getRatio()+"%");
//					download_man_size.setVisibility(View.VISIBLE);
//				}
//				if (exChildVo.getStatus()==4){
//					download_icon.setVisibility(View.GONE);
//				}else {
//					download_icon.setVisibility(View.VISIBLE);
//				}
//
//				download_icon.setOnClickListener(new OnClickListener() {
//					@Override
//					public void onClick(View v) {
//						try {
//							int cityid = exChildVo.getCtilId();
//							mOffline.start(cityid);
//
//							tv_in_the_download.setText(exChildVo.getStatusStr());
//							tv_in_the_download.setVisibility(View.VISIBLE);
//
//							download_man_size.setText(exChildVo.getRatio()+"%");
//							download_man_size.setVisibility(View.VISIBLE);
//
//							lm.setVisibility(View.VISIBLE);
//							cl.setVisibility(View.GONE);
//							tv_offline_have_downloaded.setBackgroundColor(getResources()
//									.getColor(R.color.red));
//							tv_offline_have_downloaded.setTextColor(getResources()
//									.getColor(R.color.greenyellow));
//
//							tv_offline_not_download.setBackgroundColor(getResources()
//									.getColor(R.color.gainsboro));
//							tv_offline_not_download.setTextColor(getResources().getColor(
//									R.color.black));
//							Toast.makeText(OfflineActivity2.this,
//									"开始下载" + exChildVo.getCtilName() + "离线地图: ",
//									Toast.LENGTH_SHORT).show();
//
//							isDownload = true;
//
//							mDataAdapter.notifyDataSetChanged();
//
//							updateView();
//						}catch (Exception e){
//							e.printStackTrace();
//						}
//					}
//				});
//
//				mLRecyclerViewAdapter.addHeaderView(header);
//			}
//		}
//
////
////		adapter=new CommonExpandableListAdapter<MKOLSearchRecord, MKOLSearchRecord>(
////				this,
////				R.layout.item_all_cities_chins,
////				R.layout.item_all_cities) {
////			@Override
////			protected void getChildView(ViewHolder viewHolder, final int i, final int i1, boolean b, final MKOLSearchRecord mkolSearchRecord) {
////				TextView tv_city = viewHolder.getView(R.id.tv_city);
////				tv_city.setText(mkolSearchRecord.cityName+"");
////				final TextView tv_in_the_download = viewHolder.getView(R.id.tv_in_the_download);
////				TextView download_size = viewHolder.getView(R.id.download_size);
////				download_size.setText(OfflineActivity2.formatDataSize(mkolSearchRecord.size));
////				TextView download_man_size = viewHolder.getView(R.id.download_man_size);
////				ImageView download_icon = viewHolder.getView(R.id.download_icon);
////				download_icon.setOnClickListener(new OnClickListener() {
////					@Override
////					public void onClick(View v) {
////						int cityid = mkolSearchRecord.cityID;
////						mOffline.start(cityid);
////						lm.setVisibility(View.VISIBLE);
////						cl.setVisibility(View.GONE);
////						tv_offline_have_downloaded.setBackgroundColor(getResources()
////								.getColor(R.color.red));
////						tv_offline_have_downloaded.setTextColor(getResources()
////								.getColor(R.color.greenyellow));
////
////						tv_offline_not_download.setBackgroundColor(getResources()
////								.getColor(R.color.gainsboro));
////						tv_offline_not_download.setTextColor(getResources().getColor(
////								R.color.black));
////						Toast.makeText(OfflineActivity2.this,
////								"开始下载" + mkolSearchRecord.cityName + "离线地图: ",
////								Toast.LENGTH_SHORT).show();
////
////						isDownload = true;
////
////						adapter.notifyDataSetChanged();
////
////						updateView();
////
////					}
////				});
////			}
////
////			@Override
////			protected void getGroupView(ViewHolder viewHolder, final int i, boolean b, final MKOLSearchRecord mkolSearchRecord) {
////				TextView tv_city = viewHolder.getView(R.id.tv_city);
////				tv_city.setText(mkolSearchRecord.cityName + "");
////				final TextView tv_in_the_download = viewHolder.getView(R.id.tv_in_the_download);
////				TextView download_size = viewHolder.getView(R.id.download_size);
////				download_size.setText(OfflineActivity2.formatDataSize(mkolSearchRecord.size));
////				TextView download_man_size = viewHolder.getView(R.id.download_man_size);
////				ImageView download_icon = viewHolder.getView(R.id.download_icon);
////				download_icon.setOnClickListener(new OnClickListener() {
////					@Override
////					public void onClick(View v) {
////						int cityid = mkolSearchRecord.cityID;
////						mOffline.start(cityid);
////						lm.setVisibility(View.VISIBLE);
////						cl.setVisibility(View.GONE);
////						tv_offline_have_downloaded.setBackgroundColor(getResources()
////								.getColor(R.color.red));
////						tv_offline_have_downloaded.setTextColor(getResources()
////								.getColor(R.color.greenyellow));
////
////						tv_offline_not_download.setBackgroundColor(getResources()
////								.getColor(R.color.gainsboro));
////						tv_offline_not_download.setTextColor(getResources().getColor(
////								R.color.black));
////						Toast.makeText(OfflineActivity2.this,
////								"开始下载" + mkolSearchRecord.cityName + "离线地图: ",
////								Toast.LENGTH_SHORT).show();
////
////						isDownload = true;
////
////						adapter.notifyDataSetChanged();
////
////						updateView();
////
////					}
////				});
////
////				ImageView arrows_im = viewHolder.getView(R.id.arrows_im);
////				if (b) {
////					arrows_im.setBackgroundResource(R.drawable.main_mine_down);
////				} else {
////					arrows_im.setBackgroundResource(R.drawable.main_mine_arrows);
////				}
////			}
////		};
////		mRecyclerView.setAdapter(adapter);
//
//
//		cl = (LinearLayout) findViewById(R.id.citylist_layout);
//		lm = (LinearLayout) findViewById(R.id.localmap_layout);
//		lm.setVisibility(View.GONE);
//		cl.setVisibility(View.VISIBLE);
//
//		localMapListView = (ListView) findViewById(R.id.localmaplist);
//		lAdapter = new LocalMapAdapter();
//		localMapListView.setAdapter(lAdapter);
//
////		mRecyclerView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
////			@Override
////			public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
////				return false;
////			}
////		});
//
//		/**
//		 * 点击暂停
//		 */
//		localMapListView.setOnItemClickListener(new OnItemClickListener() {
//
//			@Override
//			public void onItemClick(AdapterView<?> parent, View view,
//					int position, long id) {
//				// TODO 自动生成的方法存根
//				if (e.ratio == 100) {
//					Toast.makeText(OfflineActivity2.this, "该城市已下载完成",
//							Toast.LENGTH_LONG).show();
//				} else {
//					if (isDownload) {
//						int cityid = localMapList.get(position).cityID;
//						mOffline.pause(cityid);
//						Toast.makeText(
//								OfflineActivity2.this,
//								"暂停下载" + localMapList.get(position).cityName
//										+ "离线地图", Toast.LENGTH_SHORT).show();
//						updateView();
//						isDownload = false;
//					} else {
//						int cityid = localMapList.get(position).cityID;
//						mOffline.start(cityid);
//						lm.setVisibility(View.VISIBLE);
//						cl.setVisibility(View.GONE);
//						Toast.makeText(
//								OfflineActivity2.this,
//								"开始下载" + localMapList.get(position).cityName
//										+ "离线地图", Toast.LENGTH_SHORT).show();
//						updateView();
//						isDownload = true;
//					}
//				}
//			}
//		});
//	}
//
//	/**
//	 * 获取数据
//	 */
//	private ArrayList<MultiItemEntity> generateData() {
//
//		// 获取热门城市中的广州市
//		records2 = mOffline.getOfflineCityList();
//        List<MKOLSearchRecord> provinceRecordList=new ArrayList<>();
//        municipalitieRecordList=new ArrayList<>();
//		municipalitieExChilVoList=new ArrayList<>();
//        ArrayList<MultiItemEntity> res = new ArrayList<>();
//		if (records2!=null){
//			for (int i=0;i<records2.size();i++){
//                if (records2.get(i).cityType==1){   //省份
//                    provinceRecordList.add(records2.get(i));
//                }else if (records2.get(i).cityType==2&&records2.get(i).cityID!=9000){		//直辖市	不包括台湾省
//					municipalitieRecordList.add(records2.get(i));
//				}
////				else if (records2.get(i).cityType==0){		//全国概念图
////					municipalitieRecordList.add(records2.get(i));
////				}
//			}
//
//			//获取直辖市数据与更新数据
//			for (int i = 0; i < municipalitieRecordList.size(); i++) {
//				MKOLSearchRecord childRescord=municipalitieRecordList.get(i);
//				ExChindVo exChindVo=new ExChindVo();
//				exChindVo.setCtilId(childRescord.cityID);
//				exChindVo.setCtilName(childRescord.cityName);
//				exChindVo.setCityType(childRescord.cityType);
//				exChindVo.setDataSize(childRescord.size);
//
//				//城市的下载信息
//				//如果该城市在已下载城市中有
//				if (offMapsCity.contains(childRescord.cityID)){
//					MKOLUpdateElement updateCtil=localMapList.get(offMapsCity.lastIndexOf(childRescord.cityID));
//					if (updateCtil!=null){
//						exChindVo.setRatio(updateCtil.ratio);
//						exChindVo.setStatus(updateCtil.status);
//						switch (updateCtil.status){
//							case 0:		//未定义
//								exChindVo.setStatusStr("下载错误");
//								break;
//							case 1:		//正在下载
//								exChindVo.setStatusStr("正在下载");
//								break;
//							case 2:		//等待下载
//								exChindVo.setStatusStr("等待下载");
//								break;
//							case 3:		//已暂停
//								exChindVo.setStatusStr("已暂停");
//								break;
//							case 4:		//已完成
//								exChindVo.setStatusStr("已完成");
//								break;
//							case 5:		//校验失败
//								exChindVo.setStatusStr("校验失败");
//								break;
//							case 6:		//网络异常
//								exChindVo.setStatusStr("网络异常");
//								break;
//							case 7:		//读写异常
//								exChindVo.setStatusStr("读写异常");
//								break;
//							case 8:		//wifi网络异常
//								exChindVo.setStatusStr("wifi网络异常");
//								break;
//							case 9:		//数据错误，需重新下载
//								exChindVo.setStatusStr("数据错误，需重新下载");
//								break;
//							case 10:		//离线包正在导入中
//								exChindVo.setStatusStr("离线包正在导入中");
//								break;
//							default:	//下载错误
//								exChindVo.setStatusStr("下载错误");
//								break;
//						}
//						exChindVo.setGeoPt(updateCtil.geoPt);
//						exChindVo.setSize(updateCtil.size);
//						exChindVo.setServersize(updateCtil.serversize);
//						exChindVo.setLevel(updateCtil.level);
//						exChindVo.setUpdate(updateCtil.update);
//					}
//				}
//				municipalitieExChilVoList.add(exChindVo);
//			}
//
//			//获取省份数据与子城市数据
//            for (int i = 0; i < provinceRecordList.size(); i++) {
//                ExGroupVo exGroupVo=new ExGroupVo();
//                exGroupVo.setProvinceId(provinceRecordList.get(i).cityID);
//                exGroupVo.setProvinceName(provinceRecordList.get(i).cityName);
//                exGroupVo.setProvinceType(provinceRecordList.get(i).cityType);
//                exGroupVo.setDataSize(provinceRecordList.get(i).size);
//                for (int j=0;j<provinceRecordList.get(i).childCities.size();j++){
//                    MKOLSearchRecord childRescord=provinceRecordList.get(i).childCities.get(j);
//                    ExChindVo exChindVo=new ExChindVo();
//                    exChindVo.setCtilId(childRescord.cityID);
//                    exChindVo.setCtilName(childRescord.cityName);
//                    exChindVo.setCityType(childRescord.cityType);
//                    exChindVo.setDataSize(childRescord.size);
//
//                    //城市的下载信息
//                    //如果该城市在已下载城市中有
//                    if (offMapsCity.contains(childRescord.cityID)){
//                        MKOLUpdateElement updateCtil=localMapList.get(offMapsCity.lastIndexOf(childRescord.cityID));
//                        if (updateCtil!=null){
//                            exChindVo.setRatio(updateCtil.ratio);
//                            exChindVo.setStatus(updateCtil.status);
//							switch (updateCtil.status){
//								case 0:		//未定义
//									exChindVo.setStatusStr("下载错误");
//									break;
//								case 1:		//正在下载
//									exChindVo.setStatusStr("正在下载");
//									break;
//								case 2:		//等待下载
//									exChindVo.setStatusStr("等待下载");
//									break;
//								case 3:		//已暂停
//									exChindVo.setStatusStr("已暂停");
//									break;
//								case 4:		//已完成
//									exChindVo.setStatusStr("已完成");
//									break;
//								case 5:		//校验失败
//									exChindVo.setStatusStr("校验失败");
//									break;
//								case 6:		//网络异常
//									exChindVo.setStatusStr("网络异常");
//									break;
//								case 7:		//读写异常
//									exChindVo.setStatusStr("读写异常");
//									break;
//								case 8:		//wifi网络异常
//									exChindVo.setStatusStr("wifi网络异常");
//									break;
//								case 9:		//数据错误，需重新下载
//									exChindVo.setStatusStr("数据错误，需重新下载");
//									break;
//								case 10:		//离线包正在导入中
//									exChindVo.setStatusStr("离线包正在导入中");
//									break;
//								default:	//下载错误
//									exChindVo.setStatusStr("下载错误");
//									break;
//							}
//                            exChindVo.setGeoPt(updateCtil.geoPt);
//                            exChindVo.setSize(updateCtil.size);
//                            exChindVo.setServersize(updateCtil.serversize);
//                            exChindVo.setLevel(updateCtil.level);
//                            exChindVo.setUpdate(updateCtil.update);
//                        }
//                    }
//
//                    exGroupVo.addSubItem(exChindVo);
//                }
//                res.add(exGroupVo);
//            }
//        }
//        return res;
//	}
//
//	/**
//	 * 过滤离线地图中包含的城市
//	 * @param i
//	 */
//	private void FiltrationCity(int i) {
//        adapter.getGroupData().add(records2.get(i));
//        if (records2.get(i).childCities!=null&&records2.get(i).childCities.size()>0){
//            adapter.getChildrenData().add( records2.get(i).childCities);
//        }else {
//            adapter.getChildrenData().add( new ArrayList<MKOLSearchRecord>());
//        }
//	}
//
//	/**
//	 * 更新状态显示
//	 */
//	public void updateView() {
//		localMapList.clear();
//		localMapList = mOffline.getAllUpdateInfo();
//		if (localMapList == null) {
//			localMapList = new ArrayList<MKOLUpdateElement>();
//		}
//		lAdapter.notifyDataSetChanged();
//	}
//
//
//	@Override
//	public void onGetOfflineMapState(int type, int state) {
//		// TODO 自动生成的方法存根
//		switch (type) {
//		case MKOfflineMap.TYPE_DOWNLOAD_UPDATE: {
//			MKOLUpdateElement update = mOffline.getUpdateInfo(state);
//			// 处理下载进度更新提示
//			if (update != null) {
////				 stateView.setText(String.format("%s : %d%%", update.cityName,
////				 update.ratio));
//				updateView();
//
//                //更新城市列表信息
//                updateExListview();
//
//			}
//		}
//			break;
//		case MKOfflineMap.TYPE_NEW_OFFLINE:
//			// 有新离线地图安装
//			Log.d("OfflineDemo", String.format("add offlinemap num:%d", state));
//			break;
//		case MKOfflineMap.TYPE_VER_UPDATE:
//			// 版本更新提示
//			 MKOLUpdateElement e = mOffline.getUpdateInfo(state);
//
//			break;
//		default:
//			break;
//		}
//
//	}
//
//    /**
//     * 更新城市列表信息
//     */
//    public void updateExListview() {
//        updateView();
//
//        offMapsCity.clear();
//        for (int k=0;k<localMapList.size();k++){
//            offMapsCity.add(localMapList.get(k).cityID);
//        }
//
//        mDataAdapter.setDataList(generateData());
//    }
//
//    @Override
//	protected void onPause() {
//		super.onPause();
//	}
//
//	@Override
//	protected void onResume() {
//		super.onResume();
//	}
//
//	@Override
//	protected void onDestroy() {
//		/**
//		 * 退出时，销毁离线地图模块
//		 */
//		mOffline.destroy();
//		super.onDestroy();
//	}
//
//	public static String formatDataSize(long size) {
//		String ret = "";
//		if (size < (1024 * 1024)) {
//			ret = String.format("%dK", size / 1024);
//		} else {
//			ret = String.format("%.1fM", size / (1024 * 1024.0));
//		}
//		return ret;
//	}
//
//	@Override
//	public void onClick(View v) {
//		// TODO 自动生成的方法存根
//		switch (v.getId()) {
//		case R.id.im_offline_back:
//			this.finish();
//			break;
//		case R.id.tv_offline_not_download:
//			lm.setVisibility(View.GONE);
//			cl.setVisibility(View.VISIBLE);
//			tv_offline_not_download.setBackgroundColor(getResources().getColor(
//					R.color.red));
//			tv_offline_not_download.setTextColor(getResources().getColor(
//					R.color.greenyellow));
//
//			tv_offline_have_downloaded.setBackgroundColor(getResources()
//					.getColor(R.color.gainsboro));
//			tv_offline_have_downloaded.setTextColor(getResources().getColor(
//					R.color.black));
//			break;
//		case R.id.tv_offline_have_downloaded:
//			lm.setVisibility(View.VISIBLE);
//			cl.setVisibility(View.GONE);
//			tv_offline_have_downloaded.setBackgroundColor(getResources()
//					.getColor(R.color.red));
//			tv_offline_have_downloaded.setTextColor(getResources().getColor(
//					R.color.greenyellow));
//
//			tv_offline_not_download.setBackgroundColor(getResources().getColor(
//					R.color.gainsboro));
//			tv_offline_not_download.setTextColor(getResources().getColor(
//					R.color.black));
//			break;
//
//		default:
//			break;
//		}
//	}
//
//	public class LocalMapAdapter extends BaseAdapter {
//
//		@Override
//		public int getCount() {
//			return localMapList.size();
//		}
//
//		@Override
//		public Object getItem(int index) {
//			return localMapList.get(index);
//		}
//
//		@Override
//		public long getItemId(int index) {
//			return index;
//		}
//
//		@Override
//		public View getView(final int index, View view, ViewGroup arg2) {
//			e = localMapList.get(index);
//			ViewHolder viewHolder;
//			if (view == null) {
//				view = View.inflate(OfflineActivity2.this,
//						R.layout.offline_localmap_list, null);
//				viewHolder = new ViewHolder();
//				viewHolder.title = (TextView) view.findViewById(R.id.title);
//				viewHolder.update = (TextView) view.findViewById(R.id.update);
//				viewHolder.ratio = (TextView) view.findViewById(R.id.ratio);
//				viewHolder.remove = (Button) view.findViewById(R.id.remove);
//				view.setTag(viewHolder);
//			} else {
//				viewHolder = (ViewHolder) view.getTag();
//			}
//
//			viewHolder.ratio.setText(e.ratio + "%");
//			viewHolder.title.setText(e.cityName);
//			if (e.update) {
//				viewHolder.update.setText("可更新");
//			} else {
//				viewHolder.update.setText("最新");
//			}
//
//			viewHolder.remove.setOnClickListener(new OnClickListener() {
//				@Override
//				public void onClick(View arg0) {
//					new AlertDialog.Builder(OfflineActivity2.this)
//							.setTitle("删除后要重新下载").setMessage("确定要删除吗")
//							.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//								@Override
//								public void onClick(DialogInterface dialog, int which) {
//									mOffline.remove(localMapList.get(index).cityID);
//									localMapList.remove(index);
//									updateView();
////									upDataAllcityListView();
//								}
//							})
//							.setNegativeButton("取消", new DialogInterface.OnClickListener() {
//								@Override
//								public void onClick(DialogInterface dialog, int which) {
//									dialog.dismiss();
//								}
//							}).show();
//				}
//			});
//
//			return view;
//		}
//
//		private class ViewHolder {
//
//			private TextView title;
//			private TextView update;
//			private TextView ratio;
//			private Button remove;
//		}
//
//	}

}
