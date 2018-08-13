package com.movementinsome.database.vo;



import java.util.List;
import java.util.Map;

public class SubServiceVO {
	
	/**
	 * 七参
	 */
	private CoordTransformModel coordTransform;
	
	/**
	 * 地图范围
	 */
	private String extent;

	/**
	 * 中间层服务内网/外网
	 * local
	 * foreign
	 */
	private Map<String, String> dataInteractionRest;

	/**
	 * 推送服务内网/外网
	 * local
	 * foreign
	 */
	private Map<String, String> pushNClitentRest;

	/**
	 * 地图加载地址
	 */
	private List<MapServerVO> mapServers;

	/**
	 * REST服务/爆漏分析服务
	 */
	private List<RestVO> restList;

	/**
	 * identify设施查询
	 */
	private IdentifyConfig identifyConfig;

	/**
	 * query查询
	 */
	private QueryConfig queryConfig;

	/**
	 * 图层信息
	 */
	private List<LayerInfo> layers;

	/**
	 * 路名定位
	 */
	private PlaceNameLocateVO placeNameLocateVO;

	/**
	 * 水表组下水表
	 */
	private GroupWaterMeterVO groupWaterMeterVO;

	/**
	 * 设施编辑
	 */
	private List<FacEditElementVO> facEditElementList;

	/**
	 * 底图配置
	 */
	private List<MapServerVO> switchbasemaps;
	
	public CoordTransformModel getCoordTransform() {
		return coordTransform;
	}

	public void setCoordTransform(CoordTransformModel coordTransform) {
		this.coordTransform = coordTransform;
	}

	public String getExtent() {
		return extent;
	}

	public void setExtent(String extent) {
		this.extent = extent;
	}

	public Map<String, String> getDataInteractionRest() {
		return dataInteractionRest;
	}

	public void setDataInteractionRest(Map<String, String> dataInteractionRest) {
		this.dataInteractionRest = dataInteractionRest;
	}

	public Map<String, String> getPushNClitentRest() {
		return pushNClitentRest;
	}

	public void setPushNClitentRest(Map<String, String> pushNClitentRest) {
		this.pushNClitentRest = pushNClitentRest;
	}

	public List<MapServerVO> getMapServers() {
		return mapServers;
	}

	public void setMapServers(List<MapServerVO> mapServers) {
		this.mapServers = mapServers;
	}

	public List<RestVO> getRestList() {
		return restList;
	}

	public void setRestList(List<RestVO> restList) {
		this.restList = restList;
	}

	public IdentifyConfig getIdentifyConfig() {
		return identifyConfig;
	}

	public void setIdentifyConfig(IdentifyConfig identifyConfig) {
		this.identifyConfig = identifyConfig;
	}

	public QueryConfig getQueryConfig() {
		return queryConfig;
	}

	public void setQueryConfig(QueryConfig queryConfig) {
		this.queryConfig = queryConfig;
	}

	public List<LayerInfo> getLayers() {
		return layers;
	}

	public void setLayers(List<LayerInfo> layers) {
		this.layers = layers;
	}

	public PlaceNameLocateVO getPlaceNameLocateVO() {
		return placeNameLocateVO;
	}

	public void setPlaceNameLocateVO(PlaceNameLocateVO placeNameLocateVO) {
		this.placeNameLocateVO = placeNameLocateVO;
	}

	public GroupWaterMeterVO getGroupWaterMeterVO() {
		return groupWaterMeterVO;
	}

	public void setGroupWaterMeterVO(GroupWaterMeterVO groupWaterMeterVO) {
		this.groupWaterMeterVO = groupWaterMeterVO;
	}

	public List<FacEditElementVO> getFacEditElementList() {
		return facEditElementList;
	}

	public void setFacEditElementList(List<FacEditElementVO> facEditElementList) {
		this.facEditElementList = facEditElementList;
	}

	public List<MapServerVO> getSwitchbasemaps() {
		return switchbasemaps;
	}

	public void setSwitchbasemaps(List<MapServerVO> switchbasemaps) {
		this.switchbasemaps = switchbasemaps;
	}
	
	
}
