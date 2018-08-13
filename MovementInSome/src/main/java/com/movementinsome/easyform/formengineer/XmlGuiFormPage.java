/*
 * XmlGui application.
 * Written by Frank Ableson for IBM Developerworks
 * June 2010
 * Use the code as you wish -- no warranty of fitness, etc, etc.
 */
package com.movementinsome.easyform.formengineer;

import java.util.Vector;

// class to handle each individual form
public class XmlGuiFormPage {
	private String pageNumber;
	private String pageName;

	public Vector<XmlGuiFormGroup> groups = new Vector<XmlGuiFormGroup>();

	// getters & setters

	public String getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(String pageNumber) {
		this.pageNumber = pageNumber;
	}

	public String getPageName() {
		return pageName;
	}

	public void setPageName(String pageName) {
		this.pageName = pageName;
	}

	public Vector<XmlGuiFormGroup> getGroups() {
		return groups;
	}

	public void setGroups(Vector<XmlGuiFormGroup> groups) {
		this.groups = groups;
	}

}