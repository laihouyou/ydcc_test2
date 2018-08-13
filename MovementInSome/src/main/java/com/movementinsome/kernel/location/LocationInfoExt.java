package com.movementinsome.kernel.location;

public class LocationInfoExt extends LocationInfo {

	private String zoning;
	/**
	 * 
	 */
	private static final long serialVersionUID = 3603407440727658993L;

	public LocationInfoExt(){
		;
	}
	
	public LocationInfoExt(double latitude, double longitude, double altitude,
			float accuracy, float speed, float bearing, String time, double mapx,
			double mapy,String locationModel,String addr,int satellites,String zoning)  {
		super(latitude, longitude, altitude,
				 accuracy,  speed,  bearing,  time,  mapx,
				 mapy, locationModel, addr, satellites) ;
		this.zoning = zoning;
	}

	public String getZoning() {
		return zoning;
	}

	public void setZoning(String zoning) {
		this.zoning = zoning;
	}
	
	
}
