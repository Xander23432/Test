package com.realnet.actionbuilder.entity;

public class Rn_cff_ActionBuilder_HeaderDTO {
	private int rn_fb_header_id; // need mod
	private String technologyStack;
	private String controllerName;
	private String methodName;
	private String actionName;
	private String fileLocation;
	
	public Rn_cff_ActionBuilder_HeaderDTO() {
		super();
	}

	public Rn_cff_ActionBuilder_HeaderDTO(int rn_fb_header_id, String technologyStack, String controllerName,
			String methodName, String actionName, String fileLocation) {
		super();
		this.rn_fb_header_id = rn_fb_header_id;
		this.technologyStack = technologyStack;
		this.controllerName = controllerName;
		this.methodName = methodName;
		this.actionName = actionName;
		this.fileLocation = fileLocation;
	}



	public String getTechnologyStack() {
		return technologyStack;
	}

	public void setTechnologyStack(String technologyStack) {
		this.technologyStack = technologyStack;
	}

	public String getControllerName() {
		return controllerName;
	}

	public void setControllerName(String controllerName) {
		this.controllerName = controllerName;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public String getActionName() {
		return actionName;
	}

	public void setActionName(String actionName) {
		this.actionName = actionName;
	}

	public String getFileLocation() {
		return fileLocation;
	}

	public void setFileLocation(String fileLocation) {
		this.fileLocation = fileLocation;
	}

	public int getRn_fb_header_id() {
		return rn_fb_header_id;
	}

	public void setRn_fb_header_id(int rn_fb_header_id) {
		this.rn_fb_header_id = rn_fb_header_id;
	}
}
