package com.movementinsome.caice.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.map.offline.MKOLSearchRecord;
import com.baidu.mapapi.map.offline.MKOLUpdateElement;
import com.baidu.mapapi.map.offline.MKOfflineMap;
import com.baidu.mapapi.map.offline.MKOfflineMapListener;
import com.movementinsome.R;
import com.movementinsome.kernel.activity.FullActivity;

import java.util.ArrayList;
import java.util.List;

import sam.android.utils.viewhelper.CommonExpandableListAdapter;

/**
 * 离线地图界面
 */
public class OfflineActivity extends FullActivity implements OnClickListener,
		MKOfflineMapListener{

	private ImageView im_offline_back;
	private TextView tv_offline_not_download;// 未下载
	private TextView tv_offline_have_downloaded;// 已下载
	private LinearLayout cl;
	private LinearLayout lm;
	private boolean isDownload = false;// 当前是否在下载
	/**
	 * 未下载列表
	 */
	private LinearLayout citylist_layout;
	private ExpandableListView allcitylist;
	/**
	 * 已下载列表
	 */
	private LinearLayout localmap_layout;
	private static ListView localMapListView;

	private static MKOfflineMap mOffline = null;
	/**
	 * 已下载的离线地图信息列表
	 */
	private static ArrayList<MKOLUpdateElement> localMapList = null;
	private static LocalMapAdapter lAdapter = null;
//	private AllCitiesAdapter aAdapter;
	private CommonExpandableListAdapter<MKOLSearchRecord,MKOLSearchRecord> adapter;

	private List<MKOLSearchRecord> records2;

	private MKOLUpdateElement e;

	private int cityCode;		//当前定位城市ID

	private List<Integer> offMapsCity;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_offline);
		mOffline = new MKOfflineMap();
		mOffline.init(this);
		initView();
	}

	private void initView() {
		// TODO 自动生成的方法存根

		im_offline_back = (ImageView) findViewById(R.id.im_offline_back);
		tv_offline_not_download = (TextView) findViewById(R.id.tv_offline_not_download);
		tv_offline_have_downloaded = (TextView) findViewById(R.id.tv_offline_have_downloaded);

		im_offline_back.setOnClickListener(this);
		tv_offline_not_download.setOnClickListener(this);
		tv_offline_have_downloaded.setOnClickListener(this);

		// 获取已下过的离线地图信息
		localMapList = mOffline.getAllUpdateInfo();
		if (localMapList == null) {
			localMapList = new ArrayList<MKOLUpdateElement>();
		}
		offMapsCity=new ArrayList<>();
		for (int k=0;k<localMapList.size();k++){
			offMapsCity.add(localMapList.get(k).cityID);
		}

		allcitylist = (ExpandableListView) findViewById(R.id.allcitylist);

		adapter=new CommonExpandableListAdapter<MKOLSearchRecord, MKOLSearchRecord>(
				this,
				R.layout.item_all_cities_chins,
				R.layout.item_all_cities) {
			@Override
			protected void getChildView(ViewHolder viewHolder, final int i, final int i1, boolean b, final MKOLSearchRecord mkolSearchRecord) {
				TextView tv_city = viewHolder.getView(R.id.tv_city);
				tv_city.setText(mkolSearchRecord.cityName+"");
				final TextView tv_in_the_download = viewHolder.getView(R.id.tv_in_the_download);
				TextView download_size = viewHolder.getView(R.id.download_size);
				download_size.setText(OfflineActivity.formatDataSize(mkolSearchRecord.dataSize));
				TextView download_man_size = viewHolder.getView(R.id.download_man_size);
				ImageView download_icon = viewHolder.getView(R.id.download_icon);
				download_icon.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						int cityid = mkolSearchRecord.cityID;
						mOffline.start(cityid);
						lm.setVisibility(View.VISIBLE);
						cl.setVisibility(View.GONE);
						tv_offline_have_downloaded.setBackgroundColor(getResources()
								.getColor(R.color.red));
						tv_offline_have_downloaded.setTextColor(getResources()
								.getColor(R.color.greenyellow));

						tv_offline_not_download.setBackgroundColor(getResources()
								.getColor(R.color.gainsboro));
						tv_offline_not_download.setTextColor(getResources().getColor(
								R.color.black));
						Toast.makeText(OfflineActivity.this,
								"开始下载" + mkolSearchRecord.cityName + "离线地图: ",
								Toast.LENGTH_SHORT).show();

						isDownload = true;

						adapter.notifyDataSetChanged();

						updateView();

					}
				});
			}

			@Override
			protected void getGroupView(ViewHolder viewHolder, final int i, boolean b, final MKOLSearchRecord mkolSearchRecord) {
				TextView tv_city = viewHolder.getView(R.id.tv_city);
				tv_city.setText(mkolSearchRecord.cityName + "");
				final TextView tv_in_the_download = viewHolder.getView(R.id.tv_in_the_download);
				TextView download_size = viewHolder.getView(R.id.download_size);
				download_size.setText(OfflineActivity.formatDataSize(mkolSearchRecord.dataSize));
				TextView download_man_size = viewHolder.getView(R.id.download_man_size);
				ImageView download_icon = viewHolder.getView(R.id.download_icon);
				download_icon.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						int cityid = mkolSearchRecord.cityID;
						mOffline.start(cityid);
						lm.setVisibility(View.VISIBLE);
						cl.setVisibility(View.GONE);
						tv_offline_have_downloaded.setBackgroundColor(getResources()
								.getColor(R.color.red));
						tv_offline_have_downloaded.setTextColor(getResources()
								.getColor(R.color.greenyellow));

						tv_offline_not_download.setBackgroundColor(getResources()
								.getColor(R.color.gainsboro));
						tv_offline_not_download.setTextColor(getResources().getColor(
								R.color.black));
						Toast.makeText(OfflineActivity.this,
								"开始下载" + mkolSearchRecord.cityName + "离线地图: ",
								Toast.LENGTH_SHORT).show();

						isDownload = true;

						adapter.notifyDataSetChanged();

						updateView();

					}
				});

				ImageView arrows_im = viewHolder.getView(R.id.arrows_im);
				if (b) {
					arrows_im.setBackgroundResource(R.drawable.main_mine_down);
				} else {
					arrows_im.setBackgroundResource(R.drawable.main_mine_arrows);
				}
			}
		};
		allcitylist.setAdapter(adapter);

		upDataAllcityListView();


		cl = (LinearLayout) findViewById(R.id.citylist_layout);
		lm = (LinearLayout) findViewById(R.id.localmap_layout);
		lm.setVisibility(View.GONE);
		cl.setVisibility(View.VISIBLE);

		localMapListView = (ListView) findViewById(R.id.localmaplist);
		lAdapter = new LocalMapAdapter();
		localMapListView.setAdapter(lAdapter);

		allcitylist.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
			@Override
			public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
				return false;
			}
		});

		/**
		 * 点击暂停
		 */
		localMapListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO 自动生成的方法存根
				if (e.ratio == 100) {
					Toast.makeText(OfflineActivity.this, "该城市已下载完成",
							Toast.LENGTH_LONG).show();
				} else {
					if (isDownload) {
						int cityid = localMapList.get(position).cityID;
						mOffline.pause(cityid);
						Toast.makeText(
								OfflineActivity.this,
								"暂停下载" + localMapList.get(position).cityName
										+ "离线地图", Toast.LENGTH_SHORT).show();
						updateView();
						isDownload = false;
					} else {
						int cityid = localMapList.get(position).cityID;
						mOffline.start(cityid);
						lm.setVisibility(View.VISIBLE);
						cl.setVisibility(View.GONE);
						Toast.makeText(
								OfflineActivity.this,
								"开始下载" + localMapList.get(position).cityName
										+ "离线地图", Toast.LENGTH_SHORT).show();
						updateView();
						isDownload = true;
					}
				}
			}
		});
	}

	/**
	 * 获取数据，更新状态
	 */
	private void upDataAllcityListView() {

		adapter.getGroupData().clear();
		adapter.getChildrenData().clear();

		// 获取热门城市中的广州市
		records2 = mOffline.getOfflineCityList();
		if (records2!=null){
			for (int i=0;i<records2.size();i++){
				if (records2.get(i).cityID<=9000&&records2.get(i).cityID!=1){		//小于9000的为全国城市，1为全球基础包
					switch (records2.get(i).cityID){
						case 131:		//北京
							FiltrationCity(i);
							break;
						case 289:		//上海
							FiltrationCity(i);
							break;
						case 332:		//天津
							FiltrationCity(i);
							break;
						case 132:		//重庆
							FiltrationCity(i);
							break;
						case 2912:		//香港
							FiltrationCity(i);
							break;
						case 2911:		//澳门
							FiltrationCity(i);
							break;

						case 9000:		//台湾		百度返回的台湾省没有子城市
							FiltrationCity(i);
							break;

						default:        //普通省份
							adapter.getGroupData().add(records2.get(i));
							if (records2.get(i).childCities != null && records2.get(i).childCities.size() > 0) {
								adapter.getChildrenData().add(records2.get(i).childCities);
							}
							break;
					}
				}
			}
		}
	}

	/**
	 * 过滤离线地图中包含的城市
	 * @param i
	 */
	private void FiltrationCity(int i) {
        adapter.getGroupData().add(records2.get(i));
        if (records2.get(i).childCities!=null&&records2.get(i).childCities.size()>0){
            adapter.getChildrenData().add( records2.get(i).childCities);
        }else {
            adapter.getChildrenData().add( new ArrayList<MKOLSearchRecord>());
        }
	}

	/**
	 * 更新状态显示
	 */
	public void updateView() {
		localMapList.clear();
		localMapList = mOffline.getAllUpdateInfo();
		if (localMapList == null) {
			localMapList = new ArrayList<MKOLUpdateElement>();
		}
		lAdapter.notifyDataSetChanged();
	}


	@Override
	public void onGetOfflineMapState(int type, int state) {
		// TODO 自动生成的方法存根
		switch (type) {
		case MKOfflineMap.TYPE_DOWNLOAD_UPDATE: {
			MKOLUpdateElement update = mOffline.getUpdateInfo(state);
			// 处理下载进度更新提示
			if (update != null) {
//				 stateView.setText(String.format("%s : %d%%", update.cityName,
//				 update.ratio));
				updateView();
			}
		}
			break;
		case MKOfflineMap.TYPE_NEW_OFFLINE:
			// 有新离线地图安装
			Log.d("OfflineDemo", String.format("add offlinemap num:%d", state));
			break;
		case MKOfflineMap.TYPE_VER_UPDATE:
			// 版本更新提示
			 MKOLUpdateElement e = mOffline.getUpdateInfo(state);

			break;
		default:
			break;
		}

	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		/**
		 * 退出时，销毁离线地图模块
		 */
		mOffline.destroy();
		super.onDestroy();
	}

	public static String formatDataSize(long size) {
		String ret = "";
		if (size < (1024 * 1024)) {
			ret = String.format("%dK", size / 1024);
		} else {
			ret = String.format("%.1fM", size / (1024 * 1024.0));
		}
		return ret;
	}

	@Override
	public void onClick(View v) {
		// TODO 自动生成的方法存根
		switch (v.getId()) {
		case R.id.im_offline_back:
			this.finish();
			break;
		case R.id.tv_offline_not_download:
			lm.setVisibility(View.GONE);
			cl.setVisibility(View.VISIBLE);
			tv_offline_not_download.setBackgroundColor(getResources().getColor(
					R.color.red));
			tv_offline_not_download.setTextColor(getResources().getColor(
					R.color.greenyellow));

			tv_offline_have_downloaded.setBackgroundColor(getResources()
					.getColor(R.color.gainsboro));
			tv_offline_have_downloaded.setTextColor(getResources().getColor(
					R.color.black));
			break;
		case R.id.tv_offline_have_downloaded:
			lm.setVisibility(View.VISIBLE);
			cl.setVisibility(View.GONE);
			tv_offline_have_downloaded.setBackgroundColor(getResources()
					.getColor(R.color.red));
			tv_offline_have_downloaded.setTextColor(getResources().getColor(
					R.color.greenyellow));

			tv_offline_not_download.setBackgroundColor(getResources().getColor(
					R.color.gainsboro));
			tv_offline_not_download.setTextColor(getResources().getColor(
					R.color.black));
			break;

		default:
			break;
		}
	}

	public class LocalMapAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return localMapList.size();
		}

		@Override
		public Object getItem(int index) {
			return localMapList.get(index);
		}

		@Override
		public long getItemId(int index) {
			return index;
		}

		@Override
		public View getView(final int index, View view, ViewGroup arg2) {
			e = localMapList.get(index);
			ViewHolder viewHolder;
			if (view == null) {
				view = View.inflate(OfflineActivity.this,
						R.layout.offline_localmap_list, null);
				viewHolder = new ViewHolder();
				viewHolder.title = (TextView) view.findViewById(R.id.title);
				viewHolder.update = (TextView) view.findViewById(R.id.update);
				viewHolder.ratio = (TextView) view.findViewById(R.id.ratio);
				viewHolder.remove = (Button) view.findViewById(R.id.remove);
				view.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) view.getTag();
			}

			viewHolder.ratio.setText(e.ratio + "%");
			viewHolder.title.setText(e.cityName);
			if (e.update) {
				viewHolder.update.setText("可更新");
			} else {
				viewHolder.update.setText("最新");
			}

			viewHolder.remove.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					new AlertDialog.Builder(OfflineActivity.this)
							.setTitle("删除后要重新下载").setMessage("确定要删除吗")
							.setPositiveButton("确定", new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
									mOffline.remove(localMapList.get(index).cityID);
									localMapList.remove(index);
									updateView();
//									upDataAllcityListView();
								}
							})
							.setNegativeButton("取消", new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
									dialog.dismiss();
								}
							}).show();
				}
			});

			return view;
		}

		private class ViewHolder {

			private TextView title;
			private TextView update;
			private TextView ratio;
			private Button remove;
		}

	}

}
