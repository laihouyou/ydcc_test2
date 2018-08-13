package com.movementinsome.kernel.initial.model;

import java.util.ArrayList;
import java.util.List;

public class BaseMap {
	private List<Mapservice> mapservices = new ArrayList();
	private int _default=-1;

	public List<Mapservice> getMapservices() {
		return mapservices;
	}

	public void setMapservices(List<Mapservice> mapservices) {
		this.mapservices = mapservices;
	}

	public int getDefault() {
		return _default;
	}

	public void setDefault(int _default) {
		this._default = _default;
	}
	
	public Mapservice getDeaultBaseMap(){
		if (mapservices.size()==0){
			return null;
		}
		
		if (_default != -1){
			for(Mapservice map:mapservices){
				if (map.getId()==_default){
					return map;
				}
			}
		}else{
			return null;
		}
		return null;
	}
}
