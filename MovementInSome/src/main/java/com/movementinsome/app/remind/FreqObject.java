package com.movementinsome.app.remind;

public class FreqObject {

	private String facType;
	// 频率类型
	private String frequencyType;
	// 频率
	private String frequency;
	// 次数
	private Long frequencyNumber;
	// 最后巡查日期:yyyy-MM-dd
	private String lastInsDateStr;
	// 巡查次数
	private Long insCount;

	public String getFacType() {
		return facType;
	}

	public void setFacType(String facType) {
		this.facType = facType;
	}

	public String getFrequencyType() {
		return frequencyType;
	}

	public void setFrequencyType(String frequencyType) {
		this.frequencyType = frequencyType;
	}

	public String getFrequency() {
		return frequency;
	}

	public void setFrequency(String frequency) {
		this.frequency = frequency;
	}

	public Long getFrequencyNumber() {
		return frequencyNumber;
	}

	public void setFrequencyNumber(Long frequencyNumber) {
		this.frequencyNumber = frequencyNumber;
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

}
