package com.movementinsome.kernel.initial.model;

import java.util.ArrayList;
import java.util.List;

public class Solution {
	private int defatult;
	
	private List<Sub> subs = new ArrayList();

	public int getDefatult() {
		return defatult;
	}

	public void setDefatult(int defatult) {
		this.defatult = defatult;
	}

	public List<Sub> getSubs() {
		return subs;
	}

	public void setSubs(List<Sub> subs) {
		this.subs = subs;
	}
	
	public Configuration getDefaultConfig(){
		for(Sub sub:subs){
			if (sub.getId()==defatult){
				return sub.getConfig();
			}
		}
		return null;
	}
	
	public Configuration getDefaultConfig(int id){
		for(Sub sub:subs){
			if (sub.getId()==id){
				return sub.getConfig();
			}
		}
		return null;
	}
	
	public String getDefaultFile(int id){
		for(Sub sub:subs){
			if (sub.getId()==id){
				return sub.getFile();
			}
		}
		return null;
	}
	
	public Sub getDefault(){
		for(Sub sub:subs){
			if (sub.getId()==defatult){
				return sub;
			}
		}
		return null;
	}
	
	public Sub getDefault(int id){
		for(Sub sub:subs){
			if (sub.getId()==id){
				return sub;
			}
		}
		return null;
	}
}
