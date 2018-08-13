package com.movementinsome.database.vo;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "HistoryTrajectoryVO")
public class HistoryTrajectoryVO {
	
	// id
	@DatabaseField(id = true)
	private Long id;

	// 坐标X
	@DatabaseField
	private Double coordinatesX;

	// 坐标Y
	@DatabaseField
	private Double coordinatesY;
	
	// 时间
	@DatabaseField
	private String time;
	
	
	public Double getCoordinatesX() {
		return coordinatesX;
	}

	public void setCoordinatesX(Double coordinatesX) {
		this.coordinatesX = coordinatesX;
	}

	public Double getCoordinatesY() {
		return coordinatesY;
	}

	public void setCoordinatesY(Double coordinatesY) {
		this.coordinatesY = coordinatesY;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	


}
