package com.movementinsome.kernel.initial.model;

public class App {
	private Locate locate;
	private Push push;
	private CommuService commuService;
	private FileService fileService;
	private AutoStart autoStart;
	private Navigation navigation = new Navigation();
	private WorkorderManagement workorderManagement;
	private SendNewTask sendNewTask;
	private UserRight userRight;
	
	private Login Login;
	
	public UserRight getUserRight() {
		return userRight;
	}

	public void setUserRight(UserRight userRight) {
		this.userRight = userRight;
	}

	public SendNewTask getSendNewTask() {
		return sendNewTask;
	}

	public void setSendNewTask(SendNewTask sendNewTask) {
		this.sendNewTask = sendNewTask;
	}

	public WorkorderManagement getWorkorderManagement() {
		return workorderManagement;
	}

	public void setWorkorderManagement(WorkorderManagement workorderManagement) {
		this.workorderManagement = workorderManagement;
	}
	public Login getLogin() {
		return Login;
	}

	public void setLogin(Login login) {
		Login = login;
	}

	public Locate getLocate() {
		return locate;
	}

	public void setLocate(Locate locate) {
		this.locate = locate;
	}

	public Push getPush() {
		return push;
	}

	public void setPush(Push push) {
		this.push = push;
	}

	public CommuService getCommuService() {
		return commuService;
	}

	public void setCommuService(CommuService commuService) {
		this.commuService = commuService;
	}

	public FileService getFileService() {
		return fileService;
	}

	public void setFileService(FileService fileService) {
		this.fileService = fileService;
	}

	public AutoStart getAutoStart() {
		return autoStart;
	}

	public void setAutoStart(AutoStart autoStart) {
		this.autoStart = autoStart;
	}

	public Navigation getNavigation() {
		return navigation;
	}

	public void setNavigation(Navigation navigation) {
		this.navigation = navigation;
	}
	
}
