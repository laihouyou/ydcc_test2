/*
 * XmlGui application.
 * Written by Frank Ableson for IBM Developerworks
 * June 2010
 * Use the code as you wish -- no warranty of fitness, etc, etc.
 */
package com.movementinsome.easyform.formengineer;

import java.util.Vector;

// class to handle each individual form
public class XmlGuiFormCgroup {
	private String groupNumber;
	private String groupName;
	private boolean display;
	private String type;
	private String field;
	private String value;

	public Vector<XmlGuiFormField> fields = new Vector<XmlGuiFormField>();

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

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	
	 
}