package com.movementinsome.database.vo;


public class PipelineVO {
	// 管网管线信息ID
	private Long piId;
	// GID
	private Long gid;
	// 管线口径
	private Long pipelineCaliber;
	// 管线材质
	private String pipelineMaterial;
	// 图形
	private GeometryVO shape;

	public Long getPiId() {
		return piId;
	}

	public void setPiId(Long piId) {
		this.piId = piId;
	}

	public Long getGid() {
		return gid;
	}

	public void setGid(Long gid) {
		this.gid = gid;
	}

	public Long getPipelineCaliber() {
		return pipelineCaliber;
	}

	public void setPipelineCaliber(Long pipelineCaliber) {
		this.pipelineCaliber = pipelineCaliber;
	}

	public String getPipelineMaterial() {
		return pipelineMaterial;
	}

	public void setPipelineMaterial(String pipelineMaterial) {
		this.pipelineMaterial = pipelineMaterial;
	}

	public GeometryVO getShape() {
		return shape;
	}

	public void setShape(GeometryVO shape) {
		this.shape = shape;
	}

}
