package com.aMap.overlay;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.Text;
import com.movementinsome.R;
import com.movementinsome.app.pub.Constant;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Poi图层类。在高德地图API里，如果要显示Poi，可以用此类来创建Poi图层。如不满足需求，也可以自己创建自定义的Poi图层。
 * @since V2.1.0
 */
public class InfoWindowPoiOverlay {
	private AMap mAMap;
	private ArrayList<Marker> mPoiMarks = new ArrayList<Marker>();
	private ArrayList<Text> mTexts=new ArrayList<>();

	private List<LatLng> latLngList=null;
	private List<LatLng> wgs84LatLngList=null;
	private String implementorName="";
	/**
	 * 通过此构造函数创建Poi图层。
	 * @param amap 地图对象。
	 * @since V2.1.0
	 */
	public InfoWindowPoiOverlay(AMap amap) {
		this.mAMap = amap;
	}

	/**
	 * 设置POI数据
	 *
	 *            设置POI数据
	 */
	public void setData(List<LatLng> latLngs,List<LatLng> wgs84Latlngs) {
		this.latLngList = latLngs;
		this.wgs84LatLngList = wgs84Latlngs;
	}

	public void setImplementorName(String implementorName) {
		this.implementorName = implementorName;
	}

	/**
	 * 添加Marker到地图中。
	 * @since V2.1.0
	 */
	public void addToMap() {
		try{
			if (mAMap == null) {
				return;
			}

			removeFromMap();

			for (int i = 0; i < latLngList.size(); i++) {
				Marker marker = mAMap.addMarker(getMarkerOptions(latLngList.get(i),wgs84LatLngList.get(i)));
				marker.setObject(i);
				marker.showInfoWindow();
				mPoiMarks.add(marker);

			}
		}catch(Throwable e){
			e.printStackTrace();
		}
	}
	/**
	 * 去掉PoiOverlay上所有的Marker。
	 * @since V2.1.0
	 */
	public void removeFromMap() {
		for (Marker mark : mPoiMarks) {
			mark.hideInfoWindow();
			mark.remove();
		}
		mPoiMarks.clear();

	}
	/**
	 * 移动镜头到当前的视角。
	 * @since V2.1.0
	 */
	public void zoomToSpan() {
		try{
			if (latLngList != null && latLngList.size() > 0) {
				if (mAMap == null)
					return;
				if(latLngList.size()==1){
					mAMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLngList.get(0), 18f));
				}else{
					LatLngBounds bounds = getLatLngBounds();
					mAMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 5));
				}
			}
		}catch(Throwable e){
			e.printStackTrace();
		}
	}

	private LatLngBounds getLatLngBounds() {
		LatLngBounds.Builder b = LatLngBounds.builder();
		for (int i = 0; i < latLngList.size(); i++) {
			b.include(latLngList.get(i));
		}
		return b.build();
	}

	private MarkerOptions getMarkerOptions(LatLng latLng,LatLng wgs84Latlng) {
		BitmapDescriptor bitmap;
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

		DecimalFormat df = new DecimalFormat("0.000000");    //保留6为有效数字
		return new MarkerOptions()
				.position(latLng)
				.draggable(false)
				.title("经度:"+df.format(wgs84Latlng.longitude))
				.snippet("纬度:"+df.format(wgs84Latlng.latitude))
				.icon(bitmap);
	}


	/**
	 * 从marker中得到poi在list的位置。
	 * @param marker 一个标记的对象。
	 * @return 返回该marker对应的poi在list的位置。
	 * @since V2.1.0
	 */
	public int getPoiIndex(Marker marker) {
		for (int i = 0; i < mPoiMarks.size(); i++) {
			if (mPoiMarks.get(i).equals(marker)) {
				return i;
			}
		}
		return -1;
	}

}
