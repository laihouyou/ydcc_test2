package com.movementinsome.database.vo;


import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.movementinsome.kernel.util.MyDateTools;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.Date;

@DatabaseTable(tableName = "DynamicFormValue")
public class DynamicFormVO implements Serializable {
	
	@DatabaseField(id=true)
	private String id;  //记录ID
	@DatabaseField
	private String pid; // 上级表单ID
	//表单标志信息
	@DatabaseField
	private String form; // 表单类型
	@DatabaseField
	private String version; // 表单版本号
	// 用户信息
	@DatabaseField
	private String usId;  //用户ID,系统自动处理
	@DatabaseField
	private String usUsercode; //用户编号,系统自动处理
	@DatabaseField
	private String usNameZh;  //用户名称,系统自动处理
	//更新信息
	@DatabaseField
	private Date createDate;  //创建日期,系统自动处理
	@DatabaseField
	private Date updateDate;  //修改日期,系统自动处理
	//位置信息
	@DatabaseField
	private String gpsCoord;  //gps坐标值,"经度，纬度"
	@DatabaseField
	private String mapCoord; //地图相应坐标值,"x，y"
	//写入内容
	@DatabaseField(width = 4000)
	private String content;
	//处理信息
	@DatabaseField
	private int status;  //处理状态,初始值为0:未提交;1:已提交;2:异常
	@DatabaseField(width = 400)
	private String memo; //记录导演处理返回信息
	@DatabaseField
	private String imei; //终端唯一编码
	@DatabaseField
	private String guid;//关联附件内容的唯一值
	@DatabaseField
	private String parentTable;  //上级任务单表名
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPid() {
		return pid;
	}
	public void setPid(String pid) {
		this.pid = pid;
	}
	public String getForm() {
		return form;
	}
	public void setForm(String form) {
		this.form = form;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getUsId() {
		return usId;
	}
	public void setUsId(String usId) {
		this.usId = usId;
	}
	public String getUsUsercode() {
		return usUsercode;
	}
	public void setUsUsercode(String usUsercode) {
		this.usUsercode = usUsercode;
	}
	public String getUsNameZh() {
		return usNameZh;
	}
	public void setUsNameZh(String usNameZh) {
		this.usNameZh = usNameZh;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public Date getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
	public String getGpsCoord() {
		return gpsCoord;
	}
	public void setGpsCoord(String gpsCoord) {
		this.gpsCoord = gpsCoord;
	}
	public String getMapCoord() {
		
		return transformE(mapCoord);
		
		// mapCoord;
	}
	public void setMapCoord(String mapCoord) {
		this.mapCoord = mapCoord;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public String getImei() {
		return imei;
	}
	public void setImei(String imei) {
		this.imei = imei;
	}
	public String getGuid() {
		return guid;
	}
	public void setGuid(String guid) {
		this.guid = guid;
	}
	
	public String getParentTable() {
		return parentTable;
	}
	public void setParentTable(String parentTable) {
		this.parentTable = parentTable;
	}
	/**
	 * 科学计数法转化
	 * */
	public static String transformE(String mapCoord){
		if(mapCoord!=null && mapCoord.contains("E")){
			String[] arr = null;
			String sp = null;
			if(mapCoord.contains(",")){
				arr = mapCoord.split(",");
				sp = ",";
			}else if(mapCoord.contains(" ")){
				arr = mapCoord.split(" ");
				sp = " ";
			}
			if(arr==null){
				mapCoord = _transformE(mapCoord);
			}else{
				StringBuffer buf = new StringBuffer();
				for(String x: arr){
					if(buf.length()!=0){
						buf.append(sp);
					}
					if(x.contains("E")){
						buf.append(_transformE(x));
					}else{
						buf.append(x);
					}
				}
				mapCoord = buf.toString();
			}
		}
		return mapCoord;
	}
	
	private static String _transformE(String x){
		StringBuffer coord = new StringBuffer();
		int idx = x.indexOf("E");
		String startStr = x.substring(0, idx);
		String endStr = x.substring(idx+1);
		idx = startStr.indexOf(".");
		coord.append(startStr.substring(0, idx));
		startStr = startStr.substring(idx+1);
		idx = Integer.valueOf(endStr);
		coord.append(startStr.substring(0, idx));
		if(idx<startStr.length()){
			coord.append(".");
			coord.append(startStr.substring(idx));
		}
		return coord.toString();
	}
	
	public String toJson(String tableName,String noAttachContext,String taskCategory,String workTaskNum){
		JSONObject ob=new JSONObject();
		try {
			ob.put("moiNum", this.getId());
			ob.put("imei", this.getImei());
			ob.put("guid", this.getGuid());
			
			ob.put("workTaskNum", workTaskNum);
			ob.put("taskCategory", taskCategory);
			ob.put("tableName", tableName);
			ob.put("parentTable", this.getParentTable());
			
			ob.put("usId", this.getUsId());
			ob.put("usUsercode", this.getUsUsercode());
			ob.put("usNameZh", this.getUsNameZh());
			if (this.getCreateDate() != null){
				ob.put("createDate", MyDateTools.date2String(this.getCreateDate()));
			}else{
				ob.put("createDate", null);
			}
			if (this.getUpdateDate() != null){
				ob.put("updateDate", MyDateTools.date2String(this.getUpdateDate()));
			}else{
				ob.put("updateDate", null);
			}
			if (this.getGpsCoord()!=null){
				ob.put("gpsCoord","Point("+this.getGpsCoord()+")");
			}else{
				ob.put("gpsCoord",null);
			}
			if(this.getMapCoord()!=null){
				ob.put("mapCoord","Point("+this.getMapCoord()+")");
			}else{
				ob.put("mapCoord",null);
			}
			ob.put("content",noAttachContext);
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        return ob.toString();
	}
	
/*	private String date2String(Date date){
		SimpleDateFormat f=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String sdate = f.format(date);
		return sdate;
	}*/
}
