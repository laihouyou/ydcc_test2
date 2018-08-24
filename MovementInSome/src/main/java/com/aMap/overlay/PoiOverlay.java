package com.aMap.overlay;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.MultiPointItem;
import com.amap.api.maps.model.MultiPointOverlay;
import com.amap.api.maps.model.MultiPointOverlayOptions;
import com.movementinsome.R;
import com.movementinsome.app.pub.Constant;
import com.movementinsome.caice.util.MapMeterMoveScope;
import com.movementinsome.caice.vo.ProjectVo;
import com.movementinsome.caice.vo.SavePointVo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Poi图层类。在高德地图API里，如果要显示Poi，可以用此类来创建Poi图层。如不满足需求，也可以自己创建自定义的Poi图层。
 * @since V2.1.0
 */
public class PoiOverlay {
	private AMap mAMap;
//	private ArrayList<Marker> mPoiMarks = new ArrayList<Marker>();
//	private ArrayList<Text> mTexts=new ArrayList<>();

//	private MultiPointOverlay multiPointOverlay;
	private Map<String,MultiPointOverlay> multiPointOverlayMap;
	private Map<String,List<MultiPointItem>> multiPointItemMap;

	private List<SavePointVo> savePointVoList=null;
	private ProjectVo projectVo;
	private String acquisitionState;
	/**
	 * 通过此构造函数创建Poi图层。
	 * @param amap 地图对象。
	 * @since V2.1.0
	 */
	public PoiOverlay(AMap amap) {
		this.mAMap = amap;
	}

	/**
	 * 设置POI数据
	 *
	 * @param poiResult
	 * @param poiResult  当前采集模式
	 *            设置POI数据
	 */
	public void setData(List<SavePointVo> poiResult,String acquisitionState) {
		this.savePointVoList = poiResult;
		this.acquisitionState=acquisitionState;
		multiPointOverlayMap=new HashMap<>();
		multiPointItemMap=new HashMap<>();
		if (savePointVoList!=null){
			for (SavePointVo savePointVo:savePointVoList){
				BitmapDescriptor bitmap;
				String implementorName=savePointVo.getImplementorName();
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
					MultiPointOverlayOptions multiPointOverlayOptions=new MultiPointOverlayOptions();
					multiPointOverlayOptions.anchor(0.5f,0.5f);
					multiPointOverlayOptions.icon(bitmap);

					if (!multiPointOverlayMap.containsKey(implementorName)){
						multiPointOverlayMap.put(implementorName,mAMap.addMultiPointOverlay(multiPointOverlayOptions));
					}

					if (!multiPointItemMap.containsKey(implementorName)){
						multiPointItemMap.put(implementorName,new ArrayList<MultiPointItem>());
					}
				}

			}
		}
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

			for (int i = 0; i < savePointVoList.size(); i++) {
				SavePointVo savePointVo = savePointVoList.get(i);
				String implementorName=savePointVo.getImplementorName();

				MultiPointItem multiPointItem=new MultiPointItem(savePointVo.getLatlng());
				multiPointItem.setObject(savePointVo);
				multiPointItemMap.get(implementorName).add(multiPointItem);

//				Text text=mAMap.addText(getTextOptions(savePointVo));
//				text.setObject(savePointVo);
//				mTexts.add(text);
			}

			for (String implementorName:multiPointOverlayMap.keySet()){
				multiPointOverlayMap.get(implementorName).setItems(multiPointItemMap.get(implementorName));
				multiPointOverlayMap.get(implementorName).setEnable(true);
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
//		for (Marker mark : mPoiMarks) {
//			mark.remove();
//		}
//		mPoiMarks.clear();
//		for (Text text : mTexts) {
//			text.remove();
//		}
//		mTexts.clear();

		for (MultiPointOverlay multiPointOverlay:multiPointOverlayMap.values()){
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

	private MultiPointOverlayOptions getMultiPointOverlayOptions(SavePointVo savePointVo) {
		BitmapDescriptor bitmap;
		String implementorName=savePointVo.getImplementorName();
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

		boolean isDraggable=false;
		if (acquisitionState.equals(MapMeterMoveScope.MOVE)){      //采集模式
			if (projectVo.getProjectId().equals(savePointVo.getProjectId())){
				isDraggable=true;
			}
		}

//		DecimalFormat df = new DecimalFormat("0.000000");    //保留6为有效数字
		return new MultiPointOverlayOptions()
				.anchor(0.5f,0.5f)
				.icon(bitmap);
	}

//	private TextOptions getTextOptions(SavePointVo savePointVo){
//		return new TextOptions()
//				.position(savePointVo.getLatlng())
//				.text(savePointVo.getFacName())
////				.fontSize(25)
//				.setObject(savePointVo)
//				.fontColor(projectVo.getProjectId().equals(savePointVo.getProjectId())
//						? AppContext.getInstance().getResources().getColor(R.color.crimson)
//						: AppContext.getInstance().getResources().getColor(R.color.point_change_line))
//				;
//	}

//	/**
//	 * 从marker中得到poi在list的位置。
//	 * @param marker 一个标记的对象。
//	 * @return 返回该marker对应的poi在list的位置。
//	 * @since V2.1.0
//	 */
//	public int getPoiIndex(Marker marker) {
//		for (int i = 0; i < mPoiMarks.size(); i++) {
//			if (mPoiMarks.get(i).equals(marker)) {
//				return i;
//			}
//		}
//		return -1;
//	}

}
