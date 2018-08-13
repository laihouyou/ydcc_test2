package com.movementinsome.database.vo;


public class BurstPipesPointVO {

	// 漏水点ID
	private Long brpLdId;

	// 爆管点 格式=x y
	private String brpPoint;

	public Long getBrpLdId() {
		return brpLdId;
	}

	public void setBrpLdId(Long brpLdId) {
		this.brpLdId = brpLdId;
	}

	public String getBrpPoint() {
		return brpPoint;
	}

	public void setBrpPoint(String brpPoint) {
		this.brpPoint = brpPoint;
	}

}
