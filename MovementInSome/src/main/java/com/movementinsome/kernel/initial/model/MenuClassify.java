package com.movementinsome.kernel.initial.model;

/**
 * 模块分类栏目属性
 * */
public class MenuClassify implements java.io.Serializable {
	/** id */
	public Integer id;
	/** 类型 */
	public String type;
	/** 标题 */
	public String title;
	/** 是否感兴趣 */
	//public Boolean is_myinterest;
	/**加载模块名**/
	public String clazz;
	private String icon;
	// 是否显示任务数量
	public String isDisplayTaskCount;
	
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public String getIsDisplayTaskCount() {
		return isDisplayTaskCount;
	}
	public void setIsDisplayTaskCount(String isDisplayTaskCount) {
		this.isDisplayTaskCount = isDisplayTaskCount;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

/*	public Boolean getIs_myinterest() {
		return is_myinterest;
	}

	public void setIs_myinterest(Boolean is_myinterest) {
		this.is_myinterest = is_myinterest;
	}*/

	public String getClazz() {
		return clazz;
	}

	public void setClazz(String clazz) {
		this.clazz = clazz;
	}

}
