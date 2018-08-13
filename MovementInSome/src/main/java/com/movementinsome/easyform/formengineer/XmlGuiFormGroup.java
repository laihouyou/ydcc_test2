/*
 * XmlGui application.
 * Written by Frank Ableson for IBM Developerworks
 * June 2010
 * Use the code as you wish -- no warranty of fitness, etc, etc.
 */
package com.movementinsome.easyform.formengineer;

import java.util.Vector;

// class to handle each individual form
public class XmlGuiFormGroup {
	private String groupNumber;
	private String groupName;
	private boolean display;
	private String type;

	public Vector<XmlGuiFormField> fields = new Vector<XmlGuiFormField>();
	public Vector<XmlGuiFormCgroup> cgroups = new Vector<XmlGuiFormCgroup>();

	// getters & setters

	public String getGroupNumber() {
		return groupNumber;
	}

	public void setGroupNumber(String groupNumber) {
		this.groupNumber = groupNumber;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public Vector<XmlGuiFormField> getFields() {
		return fields;
	}

	public void setFields(Vector<XmlGuiFormField> fields) {
		this.fields = fields;
	}

	public boolean isDisplay() {
		return display;
	}

	public void setDisplay(boolean display) {
		this.display = display;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Vector<XmlGuiFormCgroup> getCgroups() {
		return cgroups;
	}

	public void setCgroups(Vector<XmlGuiFormCgroup> cgroups) {
		this.cgroups = cgroups;
	}

	 
}