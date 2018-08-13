package com.movementinsome.easyform.formengineer;

import java.util.HashMap;
import java.util.Map;

public class XmlGuiRule {

	private String id;
	private Map<String,String> dos = new HashMap();
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Map<String, String> getDos() {
		return dos;
	}

/*	public void setDos(Map dos) {
		this.dos = dos;
	}*/
	
}
