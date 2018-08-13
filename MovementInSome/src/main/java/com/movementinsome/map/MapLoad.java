package com.movementinsome.map;

import com.esri.android.map.Layer;
import com.esri.android.map.MapView;
import com.esri.android.map.ags.ArcGISDynamicMapServiceLayer;
import com.esri.android.map.ags.ArcGISImageServiceLayer;
import com.esri.android.map.ags.ArcGISLayerInfo;
import com.esri.android.map.ags.ArcGISLocalTiledLayer;
import com.esri.android.map.ags.ArcGISTiledMapServiceLayer;
import com.esri.android.runtime.ArcGISRuntime;
import com.esri.core.map.ImageServiceParameters;
import com.movementinsome.AppContext;
import com.movementinsome.kernel.initial.model.LAYERTYPE;
import com.movementinsome.kernel.initial.model.MapParam;
import com.movementinsome.kernel.initial.model.Mapservice;
import com.movementinsome.kernel.util.WebAccessTools.NET_STATE;

public class MapLoad {

	private static String localMapDir;

	public static MapParam mapParam;

	public static void LoadMap(MapView mMapView) {

		ArcGISRuntime.setClientId("fq7HOxALflHRYo7m");
		mapParam = AppContext.getInstance().getMapParam();
		localMapDir = AppContext.getInstance().getAppStoreMapPath();
		mMapView.removeAll();
		mMapView.setExtent(mapParam.getExtent());

		int index = -1;
		// 底图服务
		for (Mapservice mapObj : mapParam.getBaseMap().getMapservices()) {
			index++;
			Layer layer = findLayer(mapObj);
			if (mapObj.getId() == mapParam.getBaseMap().getDefault()) {
				layer.setVisible(true);
			} else {
				layer.setVisible(false);
			}
			try{
				mMapView.addLayer(layer, index);
			}catch(NullPointerException ex){
				ex.printStackTrace();
			}
		}

		// 业务应用地图服务
		for (Mapservice mapObj : mapParam.getBizMap()) {
			index++;
			mMapView.addLayer(findLayer(mapObj), index);
		}
	}
	
	public static boolean restLayerId(MapView mMapView){
		//设置图层的LayerID值	
		for(int i=0;i<mMapView.getLayers().length;i++){
			try {
				//System.out.println("重设LayerID退出！");
				Layer layer = mMapView.getLayer(i);
				if (layer == null){
					continue;
				}else{
				//if (layer!=null) {
					Mapservice mapService = mapParam
							.findBizMapservice(layer.getUrl());
					if (mapService == null)
						continue;

					if (layer instanceof ArcGISDynamicMapServiceLayer){
						for (ArcGISLayerInfo layerInfo : ((ArcGISDynamicMapServiceLayer)layer).getAllLayers()) {
							mapParam.setLayerId(mapService, layerInfo.getName(),
									layerInfo.getId());
						}
					}else if (layer instanceof ArcGISTiledMapServiceLayer){
						for (ArcGISLayerInfo layerInfo : ((ArcGISTiledMapServiceLayer)layer).getAllLayers()) {
							mapParam.setLayerId(mapService, layerInfo.getName(),
									layerInfo.getId());
						}
					}
				}
				return true;
			} catch (NullPointerException e) {
				//out = true;
				e.printStackTrace();

				return false;
			}
		}
		return true;
	}

	private static Layer findLayer(Mapservice mapObj) {
		
		String url = "";
		try{
			if (AppContext.getInstance().ACCESS_NET_STATE == NET_STATE.NULL){
				url = mapObj.getForeign();
			}else if (AppContext.getInstance().ACCESS_NET_STATE == NET_STATE.FOEIGN ){
				url = mapObj.getForeign();
			}else{
				url = mapObj.getLocal();
			}
		 
			if (mapObj.getType() != null && mapObj.getType() == LAYERTYPE.local) {// 离线模式，本地sdcard
				/*url = "file://" + localMapDir + mapObj.getLocal();
				mapObj.setLocal(url);
				mapObj.setForeign(url);*/
				
				ArcGISLocalTiledLayer layer = new ArcGISLocalTiledLayer(mapObj.getLocal());
				layer.setVisible(mapObj.isVisible());
				layer.setOpacity(mapObj.getAlpha());
				return layer;
	
			} else if (mapObj.getType() != null
					&& mapObj.getType() == LAYERTYPE.image) {// 图片格式png,jpg,gif,bmp……
				ImageServiceParameters options = new ImageServiceParameters();
				options.setFormat(mapObj.getFormat());
				ArcGISImageServiceLayer imglayer = new ArcGISImageServiceLayer(url,
						options);
				imglayer.setVisible(mapObj.isVisible());
				imglayer.setOpacity(mapObj.getAlpha());
				return imglayer;
			}
			if (mapObj.getType() != null && mapObj.getType() == LAYERTYPE.tiled) {// 切片
				ArcGISTiledMapServiceLayer layer = new ArcGISTiledMapServiceLayer(
						url);
				layer.setVisible(mapObj.isVisible());
				layer.setOpacity(mapObj.getAlpha());
				return layer;
			} else if (mapObj.getType() != null
					&& mapObj.getType() == LAYERTYPE.dynamic) {// 动态
				ArcGISDynamicMapServiceLayer layer = new ArcGISDynamicMapServiceLayer(
						url);
				layer.setVisible(mapObj.isVisible());
				layer.setOpacity(mapObj.getAlpha());
				return layer;
			}
		}catch(NullPointerException ex){
			return null;
		}
		return null;
	}
	

}
