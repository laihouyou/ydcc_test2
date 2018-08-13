package com.movementinsome.database.vo;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "InsGroup")
public class InsGroupVO {
	
	// 班组ID
	@DatabaseField(id = true)
	private Long igrpId;
	// 上级班组ID
	@DatabaseField
	private Long igrpParentId;
	// 班组编号
	@DatabaseField
	private String igrpNum;
	// 班组名称
	@DatabaseField
	private String igrpName;
	
	public InsGroupVO(){
		;
	}
	
	public Long getIgrpId() {
		return igrpId;
	}
	public void setIgrpId(Long igrpId) {
		this.igrpId = igrpId;
	}
	public Long getIgrpParentId() {
		return igrpParentId;
	}
	public void setIgrpParentId(Long igrpParentId) {
		this.igrpParentId = igrpParentId;
	}
	public String getIgrpNum() {
		return igrpNum;
	}
	public void setIgrpNum(String igrpNum) {
		this.igrpNum = igrpNum;
	}
	public String getIgrpName() {
		return igrpName;
	}
	public void setIgrpName(String igrpName) {
		this.igrpName = igrpName;
	}
	
	@Override
	public String toString(){
		return igrpId == null ? "<Null>" : igrpId.toString();
	}
	
}
