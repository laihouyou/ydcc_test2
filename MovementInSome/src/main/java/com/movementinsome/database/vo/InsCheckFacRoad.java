package com.movementinsome.database.vo;

import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.Polyline;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.movementinsome.AppContext;
import com.movementinsome.map.utils.MapUtil;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

@DatabaseTable(tableName = "InsCheckFacRoad")
public class InsCheckFacRoad implements Serializable {
	
	// ID
	@DatabaseField(id = true)
	private Long id;
	
	@DatabaseField
	private Long areaId;
	
	@DatabaseField
	private String roadName;

	@DatabaseField
	private String gid;

	@DatabaseField
	private String facType;

	@DatabaseField
	private String facNum;

	@DatabaseField
	private String shapeStr;

	@DatabaseField
	private Long shapeLength;
	
	@DatabaseField
	private Long state;

	@DatabaseField
	private String checkPeople;
	
	@DatabaseField
	private String startDate;
	
	@DatabaseField
	private String endDate;
	
	@DatabaseField
	private String flag;
	
	@DatabaseField
	private String remarks;
	
	@DatabaseField
	private String workTaskNum;
	
	@DatabaseField
	private String androidForm;
	
	@DatabaseField
	private String guid;
	
	@DatabaseField
	private String pitchOn;
	
	@DatabaseField
	private boolean Selected;
	
	//数据编号
	@DatabaseField
	private String serialNumber;
	
	@DatabaseField
	private Long wpdId;

	
	
	public String getAndroidForm() {
		return androidForm;
	}

	public void setAndroidForm(String androidForm) {
		this.androidForm = androidForm;
	}

	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	public Long getWpdId() {
		return wpdId;
	}

	public void setWpdId(Long wpdId) {
		this.wpdId = wpdId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getAreaId() {
		return areaId;
	}

	public void setAreaId(Long areaId) {
		this.areaId = areaId;
	}

	public String getRoadName() {
		return roadName;
	}

	public void setRoadName(String roadName) {
		this.roadName = roadName;
	}

	public String getGid() {
		return gid;
	}

	public void setGid(String gid) {
		this.gid = gid;
	}

	public String getFacType() {
		return facType;
	}

	public void setFacType(String facType) {
		this.facType = facType;
	}

	public String getFacNum() {
		return facNum;
	}

	public void setFacNum(String facNum) {
		this.facNum = facNum;
	}

	public String getShapeStr() {
		return shapeStr;
	}

	public void setShapeStr(String shapeStr) {
		this.shapeStr = shapeStr;
	}

	public Long getShapeLength() {
		return shapeLength;
	}

	public void setShapeLength(Long shapeLength) {
		this.shapeLength = shapeLength;
	}
	
	public Long getState() {
		return state;
	}

	public void setState(Long state) {
		this.state = state;
	}

	public String getCheckPeople() {
		return checkPeople;
	}

	public void setCheckPeople(String checkPeople) {
		this.checkPeople = checkPeople;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getWorkTaskNum() {
		return workTaskNum;
	}

	public void setWorkTaskNum(String workTaskNum) {
		this.workTaskNum = workTaskNum;
	}

	public String getGuid() {
		return guid;
	}

	public void setGuid(String guid) {
		this.guid = guid;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public String getPitchOn() {
		return pitchOn;
	}

	public void setPitchOn(String pitchOn) {
		this.pitchOn = pitchOn;
	}

	public boolean isSelected() {
		return Selected;
	}

	public void setSelected(boolean selected) {
		Selected = selected;
	}
	
	public Geometry getShape(){
		return MapUtil.wkt2Geometry(shapeStr);
	}
	
	/**
	 * 获取路段已经轨迹
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
