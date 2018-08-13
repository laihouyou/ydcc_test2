package com.movementinsome.kernel.initial.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.esri.core.geometry.Envelope;
import com.movementinsome.database.vo.MapparentmenusVO;
import com.movementinsome.kernel.util.WebAccessTools.NET_STATE;

public class MapParam implements Serializable {
	private Envelope extent;
	private String srid;//标准坐标系统ID
	private String wkt;//非标准坐标系统
	private BaseMap baseMap;
	private List<Mapservice> bizMap = new ArrayList();
	private List<Ftlayer> ftlayers = new ArrayList();
	private CoordParam coordParam;
	private List<Task> tasks = new ArrayList();
	private List<ExtAnaService> extAnaServices = new ArrayList();
	private List<Ftlayer> navFtlayers = new ArrayList(); //导航数据,本地存储
	private MapparentmenusVO mapparentmenusVO;// 菜单
	private Integer tolerance;
	
	public Envelope getExtent() {
		return extent;
	}

	public void setExtent(Envelope extent) {
		this.extent = extent;
	}

	public String getSrid() {
		return srid;
	}

	public void setSrid(String srid) {
		this.srid = srid;
	}

	
	public String getWkt() {
		return wkt;
	}

	public void setWkt(String wkt) {
		this.wkt = wkt;
	}

	public BaseMap getBaseMap() {
		return baseMap;
	}

	public void setBaseMap(BaseMap baseMap) {
		this.baseMap = baseMap;
	}

	public List<Mapservice> getBizMap() {
		return bizMap;
	}

	public void setBizMap(List<Mapservice> bizMap) {
		this.bizMap = bizMap;
	}

	public List<Ftlayer> getFtlayers() {
		return ftlayers;
	}

	public void setFtlayers(List<Ftlayer> ftlayers) {
		this.ftlayers = ftlayers;
	}

	public Ftlayer findFtlayer(int serviceId,int layerId){
		for(Ftlayer ftlayer:ftlayers){
			if (ftlayer.getMapservice() != null && ftlayer.getMapservice().getId()==serviceId){
				if (ftlayer.getLayerId()!=-1){
					if (ftlayer.getLayerId()==layerId)
						return ftlayer;
				}else{
					String[] ids = ftlayer.getLayerIds().split("\\,");
					for(int i=0;i<ids.length;i++){
						if (String.valueOf(layerId).equals(ids[i])){
							return ftlayer;
						}
					}
					
				}
			}
		}
		return  null;
	}
	
	public CoordParam getCoordParam() {
		return coordParam;
	}

	public void setCoordParam(CoordParam coordParam) {
		this.coordParam = coordParam;
	}

	public List<Task> getTasks() {
		return tasks;
	}

	public void setTasks(List<Task> tasks) {
		this.tasks = tasks;
	}

	public void addFtlayer(Ftlayer ftlayer) {
		ftlayers.add(ftlayer);
	}
	
	public int ftlayerSize() {
		return ftlayers.size();
	}
	
	public Ftlayer getFtlayer(int location) {
		return ftlayers.get(location);
	}
	
	public List<ExtAnaService> getExtAnaServices() {
		return extAnaServices;
	}

	public void setExtAnaServices(List<ExtAnaService> extAnaServices) {
		this.extAnaServices = extAnaServices;
	}

	public void setLayerId(Mapservice map,String name,int id){
		//for(Ftlayer layer:ftlayers){
		if (ftlayers == null){
			return;
		}
		for(int i=0;i<ftlayers.size();i++){
			if (ftlayers.get(i).getMapservice() != null){
				if (ftlayers.get(i).getMapservice().getName()== map.getName()&&ftlayers.get(i).getName().equalsIgnoreCase(name)){
					if (ftlayers.get(i).getLayerId() == -1 && "".equals(ftlayers.get(i).getLayerIds()))
						ftlayers.get(i).setLayerId(id);
					return;
				}
			}
		}
	}
	
	public int[] getLayerIds(Mapservice map){
		List lstIds = new ArrayList();
		for(Ftlayer layer:ftlayers){
			if (layer.getMapservice() != null &&layer.getMapservice().getName()== map.getName()){
				if (layer.getLayerId()!=-1){
					lstIds.add(layer.getLayerId());
				}else if (!"".equals(layer.getLayerIds()))  {
					String[] ids = layer.getLayerIds().split("\\,");
					for(int i=0;i<ids.length;i++){
						lstIds.add(Integer.parseInt(ids[i]));
					}
				}
			}
		}
		int[] ids = new int[lstIds.size()];
		for(int i=0;i<ids.length;i++){
			ids[i]=(Integer) lstIds.get(i);
		}
		return ids;
	}
	public List<String> getFeatureServerId(Mapservice map){
		List<String> featureServerIdList=new ArrayList<String>();
		for(Ftlayer layer:ftlayers){
			if (layer.getMapservice() != null &&layer.getMapservice().getName()== map.getName()){
				if(layer.getFeatureServerId()!=null){
					featureServerIdList.add(layer.getFeatureServerId());
				}
			}
		}
		return featureServerIdList;
	}
	public Mapservice findMapservice(String url){
		for(Mapservice basemap :baseMap.getMapservices()){
			if (url.equalsIgnoreCase(basemap.getLocal())||url.equalsIgnoreCase(basemap.getForeign())){
				return basemap;
			}
		}
		for(Mapservice bizmap :bizMap){
			if (url.equalsIgnoreCase(bizmap.getLocal())||url.equalsIgnoreCase(bizmap.getForeign())){
				return bizmap;
			}
		}
		return null;
	}
	
	public Mapservice findBizMapservice(String url){
		for(Mapservice bizmap :bizMap){
			if (url.equalsIgnoreCase(bizmap.getLocal())||url.equalsIgnoreCase(bizmap.getForeign())){
				return bizmap;
			}
		}
		return null;
	}
	
	public ExtAnaService findExtAnaService(String id){
		for(ExtAnaService anaService:extAnaServices){
			if (id.equals(anaService.getId())){
				return anaService;
			}
		}
		return null;
	}
	
	/**
	 * 根据网络状态返回扩展服务访问URL
	 * @param id
	 * @param netState
	 * @return
	 */
	public String findExtAnaServiceUrl(String id,NET_STATE netState){
		for(ExtAnaService anaService:extAnaServices){
			if (id.equals(anaService.getId())){
				switch (netState){
				case NULL:
					return null;
				case LOCAL:
					return anaService.getLocal();
				case FOEIGN:
					return anaService.getFoeign();
				}
			}
		}
		return null;
	}
	
	public Task findTask(String id){
		for(Task task:tasks){
			if (id.equals(task.getId())){
				return task;
			}
		}
		return null;
	}

	public List<Ftlayer> getNavFtlayers() {
		return navFtlayers;
	}

	public void setNavFtlayers(List<Ftlayer> navFtlayers) {
		this.navFtlayers = navFtlayers;
	}

	public MapparentmenusVO getMapparentmenusVO() {
		return mapparentmenusVO;
	}

	public void setMapparentmenusVO(MapparentmenusVO mapparentmenusVO) {
		this.mapparentmenusVO = mapparentmenusVO;
	}

	public Integer getTolerance() {
		return tolerance;
	}

	public void setTolerance(Integer tolerance) {
		this.tolerance = tolerance;
	}
	
}
