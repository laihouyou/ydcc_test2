package com.movementinsome.kernel.initial.model;

import java.util.ArrayList;
import java.util.List;

public class Configuration {

	private App app;
	private MapParam map;
	private ReportHistory reportHistory;
	private List<Module> modules = new ArrayList();
	private List<MenuClassify> mainmenu = new ArrayList();
	private Export export;
	private MapFacility mapFacility;
	private Setting setting;
	private Project project;
	private CityList cityList;
	private Views views;
	
	public ReportHistory getReportHistory() {
		return reportHistory;
	}

	public void setReportHistory(ReportHistory reportHistory) {
		this.reportHistory = reportHistory;
	}

	public App getApp() {
		return app;
	}

	public void setApp(App app) {
		this.app = app;
	}

	public MapParam getMap() {
		return map;
	}

	public void setMap(MapParam map) {
		this.map = map;
	}

	
	public List<Module> getModules() {
		return modules;
	}

	public void setModules(List<Module> modules) {
		this.modules = modules;
	}

	public List<MenuClassify> getMainmenu() {
		return mainmenu;
	}

	public void setMainmenu(List<MenuClassify> mainmenu) {
		this.mainmenu = mainmenu;
	}

	public Export getExport() {
		return export;
	}

	public void setExport(Export export) {
		this.export = export;
	}

	public MapFacility getMapFacility() {
		return mapFacility;
	}

	public void setMapFacility(MapFacility mapFacility) {
		this.mapFacility = mapFacility;
	}

	public Setting getSetting() {
		return setting;
	}

	public void setSetting(Setting setting) {
		this.setting = setting;
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public CityList getCityList() {
		return cityList;
	}

	public void setCityList(CityList cityList) {
		this.cityList = cityList;
	}

	public Views getViews() {
		return views;
	}

	public void setViews(Views views) {
		this.views = views;
	}
}
