package com.movementinsome.database.vo;

import java.util.List;

public class InitDataVO {

	/**
	 * 下拉项
	 */
	private List<TpconfigVO> tpconfigs;

	/**
	 * 部门列表
	 */
	private List<SecDepartmentVO> deptList;

	/**
	 * 班组
	 */
	private List<InsGroupVO> groupList;

	/**
	 * 用户列表
	 */
	private List<SecUserBasicInfoVO> userList;
	
	/**
	 * 机械情况表
	 */
	private List<BasTBaseMechanicalVO> mechanicals;

	/**
	 * 材料列表
	 */
	private List<BasTBaseMaterialVO> materialList;
	
	/**
	 * 巡检项目类型和内容
	 */
	private List<BsInsTypeSettingVO> typeSettingList;

	/**
	 * 节假日
	 */
	private List<HolidayManage> holidayList;
	

	public List<TpconfigVO> getTpconfigs() {
		return tpconfigs;
	}

	public void setTpconfigs(List<TpconfigVO> tpconfigs) {
		this.tpconfigs = tpconfigs;
	}

	public List<SecDepartmentVO> getDeptList() {
		return deptList;
	}

	public void setDeptList(List<SecDepartmentVO> deptList) {
		this.deptList = deptList;
	}

	public List<InsGroupVO> getGroupList() {
		return groupList;
	}

	public void setGroupList(List<InsGroupVO> groupList) {
		this.groupList = groupList;
	}

	public List<SecUserBasicInfoVO> getUserList() {
		return userList;
	}

	public void setUserList(List<SecUserBasicInfoVO> userList) {
		this.userList = userList;
	}

	public List<BasTBaseMechanicalVO> getMechanicals() {
		return mechanicals;
	}

	public void setMechanicals(List<BasTBaseMechanicalVO> mechanicals) {
		this.mechanicals = mechanicals;
	}

	public List<BasTBaseMaterialVO> getMaterialList() {
		return materialList;
	}

	public void setMaterialList(List<BasTBaseMaterialVO> materialList) {
		this.materialList = materialList;
	}

	public List<BsInsTypeSettingVO> getTypeSettingList() {
		return typeSettingList;
	}

	public void setTypeSettingList(List<BsInsTypeSettingVO> typeSettingList) {
		this.typeSettingList = typeSettingList;
	}

	public List<HolidayManage> getHolidayList() {
		return holidayList;
	}

	public void setHolidayList(List<HolidayManage> holidayList) {
		this.holidayList = holidayList;
	}
	
	
}
