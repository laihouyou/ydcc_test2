package com.aMap.overlay;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.MultiPointItem;
import com.amap.api.maps.model.MultiPointOverlay;
import com.amap.api.maps.model.MultiPointOverlayOptions;
import com.movementinsome.caice.vo.ProjectVo;
import com.movementinsome.caice.vo.SavePointVo;

import java.util.ArrayList;
import java.util.List;

/**
 * Poi图层类。在高德地图API里，如果要显示Poi，可以用此类来创建Poi图层。如不满足需求，也可以自己创建自定义的Poi图层。
 * @since V2.1.0
 */
public class PoiOverlay2 {
	private AMap mAMap;

	private MultiPointOverlay multiPointOverlay;
	private BitmapDescriptor mBitmapDescriptor;

	private List<SavePointVo> savePointVoList=null;
	private ProjectVo projectVo;
	/**
	 * 通过此构造函数创建Poi图层。
	 * @param amap 地图对象。
	 * @since V2.1.0
	 */
	public PoiOverlay2(AMap amap) {
		this.mAMap = amap;
	}

	/**
	 * 设置POI数据
	 *
	 * @param poiResult
	 * @param poiResult  当前采集模式
	 *            设置POI数据
	 */
	public void setData(List<SavePointVo> poiResult, BitmapDescriptor bitmapDescriptor) {
		this.savePointVoList = poiResult;
		this.mBitmapDescriptor=bitmapDescriptor;
//		if (savePointVoList!=null){
//			for (SavePointVo savePointVo:savePointVoList){
//				BitmapDescriptor bitmap;
//				String implementorName=savePointVo.getImplementorName();
//				if (implementorName != null&&!implementorName.equals("")) {
//					switch (implementorName) {
//						case Constant.VALVE:    //阀门
//							//构建Marker图标
//							bitmap = BitmapDescriptorFactory
//									.fromResource(R.drawable.valve_inspection);
//							break;
//						case Constant.MUD_VALVE:    //排泥阀
//							//构建Marker图标
//							bitmap = BitmapDescriptorFactory
//									.fromResource(R.drawable.mud_valve);
//							break;
//						case Constant.VENT_VALVE:    //排气阀
//							//构建Marker图标
//							bitmap = BitmapDescriptorFactory
//									.fromResource(R.drawable.drain_tap);
//							break;
//						case Constant.WATER_METER:    //水表
//							//构建Marker图标
//							bitmap = BitmapDescriptorFactory
//									.fromResource(R.drawable.meter_reading);
//							break;
//						case Constant.FIRE_HYDRANT:    //消防栓
//							//构建Marker图标
//							bitmap = BitmapDescriptorFactory
//									.fromResource(R.drawable.hydrant);
//							break;
//						case Constant.DISCHARGE_OUTLET:    //出水口
//							//构建Marker图标
//							bitmap = BitmapDescriptorFactory
//									.fromResource(R.drawable.water_outlet);
//							break;
//						case Constant.PLUG_SEAL:    //封头堵坂
//							//构建Marker图标
//							bitmap = BitmapDescriptorFactory
//									.fromResource(R.drawable.plug_seal_plate);
//							break;
//						case Constant.NODE_BLACK:    //节点
//							//构建Marker图标
//							bitmap = BitmapDescriptorFactory
//									.fromResource(R.drawable.node);
//						case Constant.POOL:    //水池
//							//构建Marker图标
//							bitmap = BitmapDescriptorFactory
//									.fromResource(R.drawable.pool);
//							break;
//
//						case Constant.METER_READING_UNDONE:    //水表(未完成)
//							//构建Marker图标
//							bitmap = BitmapDescriptorFactory
//									.fromResource(R.drawable.meter_reading_undone);
//							break;
//
//						case Constant.METER_READING_COMPLETED:    //水表(已完成)
//							//构建Marker图标
//							bitmap = BitmapDescriptorFactory
//									.fromResource(R.drawable.meter_reading_completed);
//							break;
//
//						default:
//							//构建Marker图标
//							bitmap = BitmapDescriptorFactory
//									.fromResource(R.drawable.icon_marka_b_yellow);
//
//							break;
//					}
//					MultiPointOverlayOptions multiPointOverlayOptions=new MultiPointOverlayOptions();
//					multiPointOverlayOptions.anchor(0.5f,0.5f);
//					multiPointOverlayOptions.icon(bitmap);
//
//
//				}
//
//			}
//		}
	}
	/**
	 * 设置POI数据
	 *
	 *            设置POI数据
	 */
	public void setProjectVo(ProjectVo projectVo) {
		this.projectVo = projectVo;
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

			MultiPointOverlayOptions overlayOptions = new MultiPointOverlayOptions();
			overlayOptions.icon(mBitmapDescriptor);
			overlayOptions.anchor(0.5f,0.5f);

			multiPointOverlay = mAMap.addMultiPointOverlay(overlayOptions);
			List<MultiPointItem> list = new ArrayList<MultiPointItem>();

			for (int i = 0; i < savePointVoList.size(); i++) {
				SavePointVo savePointVo = savePointVoList.get(i);
				String implementorName=savePointVo.getImplementorName();

				MultiPointItem multiPointItem=new MultiPointItem(savePointVo.getLatlng());
				multiPointItem.setObject(savePointVo);
				list.add(multiPointItem);
			}

			if(multiPointOverlay != null) {
				multiPointOverlay.setItems(list);
				multiPointOverlay.setEnable(true);
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
		if (multiPointOverlay!=null){
			multiPointOverlay.remove();
		}
	}
	/**
	 * 移动镜头到当前的视角。
	 * @since V2.1.0
	 */
	public void zoomToSpan() {
		try{
			if (savePointVoList != null && savePointVoList.size() > 0) {
				if (mAMap == null)
					return;
				if(savePointVoList.size()==1){
					mAMap.moveCamera(CameraUpdateFactory.newLatLngZoom(savePointVoList.get(0).getLatlng(), 18f));
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
		for (int i = 0; i < savePointVoList.size(); i++) {
			b.include(savePointVoList.get(i).getLatlng());
		}
		return b.build();
	}

}
