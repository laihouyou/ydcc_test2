package com.aMap.overlay;

import com.amap.api.maps.AMap;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Polyline;
import com.amap.api.maps.model.PolylineOptions;
import com.amap.api.maps.model.Text;
import com.amap.api.maps.model.TextOptions;
import com.movementinsome.AppContext;
import com.movementinsome.R;
import com.movementinsome.caice.vo.ProjectVo;
import com.movementinsome.caice.vo.SavePointVo;

import java.util.ArrayList;
import java.util.List;

/**
 * Poi图层类。在高德地图API里，如果要显示Poi，可以用此类来创建Poi图层。如不满足需求，也可以自己创建自定义的Poi图层。
 * @since V2.1.0
 */
public class LineOverlay implements AMap.OnPolylineClickListener{
	private AMap mAMap;
	private ArrayList<Polyline> mPolyline = new ArrayList<Polyline>();
	private ArrayList<Text> mTexts=new ArrayList<>();

	private List<SavePointVo> savePointVoList=null;
	private ProjectVo projectVo;
	private  BitmapDescriptor myLineDataBitmap= BitmapDescriptorFactory.fromResource(R.drawable.map_alr);
	private  BitmapDescriptor shareLineDataBitmap= BitmapDescriptorFactory.fromResource(R.drawable.map_alr_night);

	/**
	 * 通过此构造函数创建Poi图层。
	 * @param amap 地图对象。
	 * @since V2.1.0
	 */
	public LineOverlay(AMap amap) {
		mAMap = amap;
	}

	/**
	 * 设置POI数据
	 *
	 * @param poiResult
	 *            设置POI数据
	 */
	public void setData(List<SavePointVo> poiResult) {
		this.savePointVoList = poiResult;
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
		try {
			if (mAMap == null) {
				return;
			}
			removeFromMap();

			for (int i = 0; i < savePointVoList.size(); i++) {
				SavePointVo savePointVo = savePointVoList.get(i);
				Polyline polyline = mAMap.addPolyline(getPolyLineOptions(savePointVo));
				if (!mPolyline.contains(polyline)) {
					mPolyline.add(polyline);
				}

				//添加文字  --管线名字
				Text text = mAMap.addText(getTextOptions(savePointVo));
				if (!mTexts.contains(text)) {
					mTexts.add(text);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 去掉PoiOverlay上所有的Marker。
	 * @since V2.1.0
	 */
	public void removeFromMap() {
		for (Polyline mark : mPolyline) {
			mark.remove();
		}
		mPolyline.clear();
		for (Text text : mTexts) {
			text.remove();
		}
		mTexts.clear();
	}

	/**
	 * 去掉PoiOverlay上所有的Marker。
	 * @since V2.1.0
	 */
	public void removeTextFromMap() {
		for (Text text : mTexts) {
			text.remove();
		}
		mTexts.clear();
	}


//	/**
//	 * 移动镜头到当前的视角。
//	 * @since V2.1.0
//	 */
//	public void zoomToSpan() {
//		try{
//			if (mLatlngs != null && mLatlngs.size() > 0) {
//				if (mAMap == null)
//					return;
//				if(mLatlngs.size()==1){
//					mAMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mLatlngs.get(0).getLatLonPoint().getLatitude(),
//							mLatlngs.get(0).getLatLonPoint().getLongitude()), 18f));
//				}else{
//					LatLngBounds bounds = getLatLngBounds();
//					mAMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 5));
//				}
//			}
//		}catch(Throwable e){
//			e.printStackTrace();
//		}
//	}
//
//	private LatLngBounds getLatLngBounds() {
//		LatLngBounds.Builder b = LatLngBounds.builder();
//		for (int i = 0; i < mLatlngs.size(); i++) {
//			b.include(new LatLng(mLatlngs.get(i).getLatLonPoint().getLatitude(),
//					mLatlngs.get(i).getLatLonPoint().getLongitude()));
//		}
//		return b.build();
//	}

	private PolylineOptions getPolyLineOptions(SavePointVo savePointVo) {
		return new PolylineOptions()
				.addAll(savePointVo.getLineLatlngList())
				.setDottedLine(true)
				.setCustomTexture(projectVo.getProjectId().equals(savePointVo.getProjectId())
						? myLineDataBitmap
						:shareLineDataBitmap);
	}

	private TextOptions getTextOptions(SavePointVo savePointVo){
		List<LatLng> points=savePointVo.getLineLatlngList();
		if (points.size() >= 2) {
			double lon = (points.get(0).longitude + points.get(1).longitude) / 2;
			double lat = (points.get(0).latitude + points.get(1).latitude) / 2;
			LatLng latLng = new LatLng(lat, lon);
			return new TextOptions()
					.text(savePointVo.getPipName() == null ? "" : savePointVo.getPipName())
					.position(latLng)
					.fontSize(25)
					.setObject(savePointVo)
					.fontColor(projectVo.getProjectId().equals(savePointVo.getProjectId())
							? AppContext.getInstance().getResources().getColor(R.color.crimson)
							: AppContext.getInstance().getResources().getColor(R.color.point_change_line));
		}
		return new TextOptions();
	}

	/**
	 * 从marker中得到poi在list的位置。
	 * @param Polyline 一个标记的对象。
	 * @return 返回该marker对应的poi在list的位置。
	 * @since V2.1.0
	 */
	public int getPoiIndex(Polyline Polyline) {
		for (int i = 0; i < mPolyline.size(); i++) {
			if (mPolyline.get(i).equals(Polyline)) {
				return i;
			}
		}
		return -1;
	}
	/**
	 * 返回第index个包含的设施信息
	 * @param index 第几个poi。
	 * @since V2.1.0
	 */
	public SavePointVo getSavePointVo(int index) {
		Text text=mTexts.get(index);
		SavePointVo savePointVo= (SavePointVo) text.getObject();
		return savePointVo==null?new SavePointVo():savePointVo;
	}

	public ArrayList<Polyline> getmPolyline() {
		if (mPolyline == null) {
			return new ArrayList<>();
		}
		return mPolyline;
	}

	/**
	 * 线通用点击事件  （点击线后变颜色）
	 * @param polyline
	 */
	@Override
	public void onPolylineClick(Polyline polyline) {
		for (int i = 0; i < mPolyline.size(); i++) {
			if (mPolyline.get(i)==polyline){
				mPolyline.get(i).setCustomTexture(shareLineDataBitmap);
			}else {
				mPolyline.get(i).setCustomTexture(myLineDataBitmap);
			}
		}
	}
}
