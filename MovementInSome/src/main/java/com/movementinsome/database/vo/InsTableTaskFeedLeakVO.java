package com.movementinsome.database.vo;



import java.util.List;


public class InsTableTaskFeedLeakVO {

	/**
	 * 现场定漏
	 */
	// 移动端唯一编码
	private String moiNum;
	// 流水号
	private String serialNumber;
	// 设施编号
	private String facNum;
	// gid
	private String gid;
	// 探测类型
	private String probeType;
	// 探测环境
	private String probeCondition;
	// 探测班组
	private String probeGroup;
	// 探测地址
	private String probeAddr;
	// 到场时间
	private String persentDate;
	// 完工时间
	private String finshDate;
	// 定位状态
	private String probeState;
	// 管材
	private String facilitiesMaterial;
	// 口径
	private String facilitiesCaliber;
	// 管顶埋深
	private String facilitiesDepth;
	// 备注
	private String remarks;
	// 状态
	private String state;
	// 定位点照片
	private List<PhotoInfoVO> imageBaseCode;
	// 图形
	private GeometryVO geometry;

	public String getMoiNum() {
		return moiNum;
	}

	public void setMoiNum(String moiNum) {
		this.moiNum = moiNum;
	}

	public String getFacNum() {
		return facNum;
	}

	public void setFacNum(String facNum) {
		this.facNum = facNum;
	}

	public String getGid() {
		return gid;
	}

	public void setGid(String gid) {
		this.gid = gid;
	}

	public String getProbeType() {
		return probeType;
	}

	public void setProbeType(String probeType) {
		this.probeType = probeType;
	}

	public String getProbeCondition() {
		return probeCondition;
	}

	public void setProbeCondition(String probeCondition) {
		this.probeCondition = probeCondition;
	}

	public String getProbeGroup() {
		return probeGroup;
	}

	public void setProbeGroup(String probeGroup) {
		this.probeGroup = probeGroup;
	}

	public String getProbeAddr() {
		return probeAddr;
	}

	public void setProbeAddr(String probeAddr) {
		this.probeAddr = probeAddr;
	}

	public String getPersentDate() {
		return persentDate;
	}

	public void setPersentDate(String persentDate) {
		this.persentDate = persentDate;
	}

	public String getFinshDate() {
		return finshDate;
	}

	public void setFinshDate(String finshDate) {
		this.finshDate = finshDate;
	}

	public String getProbeState() {
		return probeState;
	}

	public void setProbeState(String probeState) {
		this.probeState = probeState;
	}

	public String getFacilitiesMaterial() {
		return facilitiesMaterial;
	}

	public void setFacilitiesMaterial(String facilitiesMaterial) {
		this.facilitiesMaterial = facilitiesMaterial;
	}

	public String getFacilitiesCaliber() {
		return facilitiesCaliber;
	}

	public void setFacilitiesCaliber(String facilitiesCaliber) {
		this.facilitiesCaliber = facilitiesCaliber;
	}

	public String getFacilitiesDepth() {
		return facilitiesDepth;
	}

	public void setFacilitiesDepth(String facilitiesDepth) {
		this.facilitiesDepth = facilitiesDepth;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public List<PhotoInfoVO> getImageBaseCode() {
		return imageBaseCode;
	}

	public void setImageBaseCode(List<PhotoInfoVO> imageBaseCode) {
		this.imageBaseCode = imageBaseCode;
	}

	public GeometryVO getGeometry() {
		return geometry;
	}

	public void setGeometry(GeometryVO geometry) {
		this.geometry = geometry;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

}
