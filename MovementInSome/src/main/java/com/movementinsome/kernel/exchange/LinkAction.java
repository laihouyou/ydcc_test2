package com.movementinsome.kernel.exchange;

public class LinkAction {

	private String actionName;
	private String label;
	private String keyId;

	public LinkAction(String actionName, String label, String keyId) {
		this.actionName = actionName;
		this.label = label;
		this.keyId = keyId;
	}

	public String getActionName() {
		return actionName;
	}

	public void setActionName(String actionName) {
		this.actionName = actionName;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getKeyId() {
		return keyId;
	}

	public void setKeyId(String keyId) {
		this.keyId = keyId;
	}

}
