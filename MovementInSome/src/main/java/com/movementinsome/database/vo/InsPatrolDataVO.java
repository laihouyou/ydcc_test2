package com.movementinsome.database.vo;


import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.Polyline;
import com.movementinsome.AppContext;
import com.movementinsome.map.utils.MapUtil;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "InsPatrolDataVO")
public class InsPatrolDataVO implements java.io.Serializable{// formList

	// ID
	@DatabaseField(id = true)
	private String guid;
	@DatabaseField
	private Long wpdId;
	//编号
	@DatabaseField
	private String num;
	//名称
	@DatabaseField
	private String name;
	//类型
	@DatabaseField
	private String facType;
	//频率类型
	@DatabaseField
	private String frequencyType;
	//频率
	@DatabaseField
	private String frequency;
	//次数
	@DatabaseField
	private Long frequencyNumber;
	//频率描述
	@DatabaseField
	private String frequencyDesc;
	// 图形
	@DatabaseField
	private String geometryStr;
	//数据编号
	@DatabaseField
	private String serialNumber;
	//任务编号
	@DatabaseField
	private String workTaskNum;
	//最后巡查日期:yyyy-MM-dd
	@DatabaseField
	private String lastInsDateStr;
	//巡查次数
	@DatabaseField
	private Long insCount;
	//巡查状态（start,finish,pause）
	@DatabaseField
	private String state;
	@DatabaseField
	private String title;
	// 本机任务分类
	@DatabaseField
	private String taskCategory;
	// 模板
	@DatabaseField
	private String androidForm;
	@DatabaseField
	private Long areaId;
	

	public Long getAreaId() {
		return areaId;
	}

	public void setAreaId(Long areaId) {
		this.areaId = areaId;
	}

	public String getAndroidForm() {
		return androidForm;
	}

	public void setAndroidForm(String androidForm) {
		this.androidForm = androidForm;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTaskCategory() {
		return taskCategory;
	}

	public void setTaskCategory(String taskCategory) {
		this.taskCategory = taskCategory;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getGuid() {
		return guid;
	}

	public void setGuid(String guid) {
		this.guid = guid;
	}

	public Long getWpdId() {
		return wpdId;
	}

	public void setWpdId(Long wpdId) {
		this.wpdId = wpdId;
	}

	public String getFacType() {
		return facType;
	}

	public void setFacType(String facType) {
		this.facType = facType;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFrequencyDesc() {
		return frequencyDesc;
	}

	public void setFrequencyDesc(String frequencyDesc) {
		this.frequencyDesc = frequencyDesc;
	}

	public String getFrequency() {
		return frequency;
	}

	public void setFrequency(String frequency) {
		this.frequency = frequency;
	}

	public String getFrequencyType() {
		return frequencyType;
	}

	public void setFrequencyType(String frequencyType) {
		this.frequencyType = frequencyType;
	}

	public Long getFrequencyNumber() {
		return frequencyNumber;
	}

	public void setFrequencyNumber(Long frequencyNumber) {
		this.frequencyNumber = frequencyNumber;
	}

	public String getGeometryStr() {
		return geometryStr;
	}

	public void setGeometryStr(String geometryStr) {
		this.geometryStr = geometryStr;
	}

	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	public String getWorkTaskNum() {
		return workTaskNum;
	}

	public void setWorkTaskNum(String workTaskNum) {
		this.workTaskNum = workTaskNum;
	}

	public String getLastInsDateStr() {
		return lastInsDateStr;
	}

	public void setLastInsDateStr(String lastInsDateStr) {
		this.lastInsDateStr = lastInsDateStr;
	}

	public Long getInsCount() {
		return insCount;
	}

	public void setInsCount(Long insCount) {
		this.insCount = insCount;
	}

	public String getNum() {
		return num;
	}

	public void setNum(String num) {
		this.num = num;
	}
	
	public Geometry getShape(){
		return MapUtil.wkt2Geometry(geometryStr);
	}
	
	/**
	 * 获取路段已经轨迹
	 * @param id
	 * @return
	 */
	public Polyline getRoadPreTrace(){
		try {
			Dao<DynamicFormVO, Long> dynamicFormDao = AppContext.getInstance().getAppDbHelper().getDao(DynamicFormVO.class);
			if (dynamicFormDao.queryForEq("id", this.guid).isEmpty()){
				return new Polyline();
			}else{
				DynamicFormVO	dynFormVo = dynamicFormDao.queryForEq("id", this.guid).get(0);
				Geometry geo = MapUtil.wkt2Geometry(dynFormVo.getContent());
				if (null == geo || geo.isEmpty()){
					return new Polyline();
				}else if (geo instanceof Polyline) {
					Polyline pline = (Polyline)geo;
					return pline;
				}else{
					return new Polyline();
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block			
			e.printStackTrace();
			return new Polyline();
		}
	}
	
	public String getRoadPreTraceStr(){
		try {
			Dao<DynamicFormVO, Long> dynamicFormDao = AppContext.getInstance().getAppDbHelper().getDao(DynamicFormVO.class);
			if (dynamicFormDao.queryForEq("id", this.guid).isEmpty()){
				return null;
			}else{
				DynamicFormVO	dynFormVo = dynamicFormDao.queryForEq("id", this.guid).get(0);
				return dynFormVo.getContent();
				
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block			
			e.printStackTrace();
			return null;
		}
	}
	
	public void delRoadPreTrace(){
		try {
			Dao<DynamicFormVO, Long> dynamicFormDao = AppContext.getInstance().getAppDbHelper().getDao(DynamicFormVO.class);
			List<DynamicFormVO> dynamicFormVOList = dynamicFormDao.queryForEq("id", this.guid);
			if(dynamicFormVOList!=null&&dynamicFormVOList.size()>0){
				dynamicFormDao.delete(dynamicFormVOList.get(0));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block			
			e.printStackTrace();
		}
	}
	
	/**
	 * 保存路段巡查轨迹
	 * @param id
	 * @param content
	 */
	public void saveRoadPreTrace(String content){
		try {
			Dao<DynamicFormVO, Long> dynamicFormDao = AppContext.getInstance().getAppDbHelper().getDao(DynamicFormVO.class);
			
			DynamicFormVO	dynFormVo = new DynamicFormVO();
			dynFormVo.setId(this.guid);  //用GUID作为主要键，防止重装系统后上传到后台查询时候有记录重复
			//dynFormVo.setPid("".equals(pid)?null:pid);
			dynFormVo.setImei(AppContext.getInstance().getPhoneIMEI());
			//dynFormVo.setForm(template);
			//dynFormVo.setVersion("1");
			dynFormVo.setCreateDate(new Date());
			dynFormVo.setUsId(AppContext.getInstance().getCurUser().getUserId());
			dynFormVo.setUsUsercode(AppContext.getInstance().getCurUser().getUserName());
			dynFormVo.setUsNameZh(AppContext.getInstance().getCurUser().getUserAlias());
			if (AppContext.getInstance().getCurLocation()!=null){
				dynFormVo.setGpsCoord(AppContext.getInstance().getCurLocation().getCurGpsPosition());
				dynFormVo.setMapCoord(AppContext.getInstance().getCurLocation().getCurMapPosition());
			}
			/*if(guid!=null){
				dynFormVo.setGuid(guid);
			}else{
				dynFormVo.setGuid(UUID.randomUUID().toString());
			}*/
			dynFormVo.setContent(content);
			
			dynamicFormDao.createOrUpdate(dynFormVo);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
