package com.realnet.wfb.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.realnet.actionbuilder.entity.Rn_cff_ActionBuilder_Header;
import com.realnet.fnd.entity.Rn_Module_Setup;
import com.realnet.fnd.entity.Rn_Who_AccId_Column;

public class Rn_Fb_HeaderDTO {

	private String techStack;
	private String objectType;
	private String subObjectType;
	private String uiName;
	private String formType;
	private String tableName;
	private String lineTableName;
	private String multilineTableName;
	private String formCode;
	private String jspName;
	private String controllerName;
	private String serviceName;
	private String serviceImplName;
	private String daoName;
	private String daoImplName;
// ==============================
	private boolean build;
	private boolean updated;
	private String menuName;
	private String headerName;
	private String convertedTableName;

	private List<Rn_Fb_Line> rn_fb_lines;
	private Rn_Module_Setup module;
	
	// --- action builder relation --
	private List<Rn_cff_ActionBuilder_Header> rn_cff_actionBuilder;

	// ----------- get set

	public String getTechStack() {
		return techStack;
	}

	public void setTechStack(String techStack) {
		this.techStack = techStack;
	}

	public String getObjectType() {
		return objectType;
	}

	public void setObjectType(String objectType) {
		this.objectType = objectType;
	}

	public String getSubObjectType() {
		return subObjectType;
	}

	public void setSubObjectType(String subObjectType) {
		this.subObjectType = subObjectType;
	}

	public String getUiName() {
		return uiName;
	}

	public void setUiName(String uiName) {
		this.uiName = uiName;
	}

	public String getFormType() {
		return formType;
	}

	public void setFormType(String formType) {
		this.formType = formType;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getLineTableName() {
		return lineTableName;
	}

	public void setLineTableName(String lineTableName) {
		this.lineTableName = lineTableName;
	}

	public String getMultilineTableName() {
		return multilineTableName;
	}

	public void setMultilineTableName(String multilineTableName) {
		this.multilineTableName = multilineTableName;
	}

	public String getFormCode() {
		return formCode;
	}

	public void setFormCode(String formCode) {
		this.formCode = formCode;
	}

	public String getJspName() {
		return jspName;
	}

	public void setJspName(String jspName) {
		this.jspName = jspName;
	}

	public String getControllerName() {
		return controllerName;
	}

	public void setControllerName(String controllerName) {
		this.controllerName = controllerName;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getServiceImplName() {
		return serviceImplName;
	}

	public void setServiceImplName(String serviceImplName) {
		this.serviceImplName = serviceImplName;
	}

	public String getDaoName() {
		return daoName;
	}

	public void setDaoName(String daoName) {
		this.daoName = daoName;
	}

	public String getDaoImplName() {
		return daoImplName;
	}

	public void setDaoImplName(String daoImplName) {
		this.daoImplName = daoImplName;
	}

	public boolean isBuild() {
		return build;
	}

	public void setBuild(boolean build) {
		this.build = build;
	}

	public boolean isUpdated() {
		return updated;
	}

	public void setUpdated(boolean updated) {
		this.updated = updated;
	}

	public String getMenuName() {
		return menuName;
	}

	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}

	public String getHeaderName() {
		return headerName;
	}

	public void setHeaderName(String headerName) {
		this.headerName = headerName;
	}

	public String getConvertedTableName() {
		return convertedTableName;
	}

	public void setConvertedTableName(String convertedTableName) {
		this.convertedTableName = convertedTableName;
	}

	public List<Rn_Fb_Line> getRn_fb_lines() {
		return rn_fb_lines;
	}

	public void setRn_fb_lines(List<Rn_Fb_Line> rn_fb_lines) {
		this.rn_fb_lines = rn_fb_lines;
	}

	public Rn_Module_Setup getModule() {
		return module;
	}

	public void setModule(Rn_Module_Setup module) {
		this.module = module;
	}

	

	public List<Rn_cff_ActionBuilder_Header> getRn_cff_actionBuilder() {
		return rn_cff_actionBuilder;
	}

	public void setRn_cff_actionBuilder(List<Rn_cff_ActionBuilder_Header> rn_cff_actionBuilder) {
		this.rn_cff_actionBuilder = rn_cff_actionBuilder;
	}

	public Rn_Fb_HeaderDTO() {
		super();
	}
	
	public Rn_Fb_HeaderDTO(String techStack, String objectType, String subObjectType, String uiName,
			String formType, String tableName, String lineTableName, String multilineTableName, String formCode,
			String jspName, String controllerName, String serviceName, String serviceImplName, String daoName,
			String daoImplName, boolean build, boolean updated, String menuName, String headerName,
			String convertedTableName, List<Rn_Fb_Line> rn_fb_lines, Rn_Module_Setup module,
			List<Rn_cff_ActionBuilder_Header> rn_cff_actionBuilder) {
		super();
		this.techStack = techStack;
		this.objectType = objectType;
		this.subObjectType = subObjectType;
		this.uiName = uiName;
		this.formType = formType;
		this.tableName = tableName;
		this.lineTableName = lineTableName;
		this.multilineTableName = multilineTableName;
		this.formCode = formCode;
		this.jspName = jspName;
		this.controllerName = controllerName;
		this.serviceName = serviceName;
		this.serviceImplName = serviceImplName;
		this.daoName = daoName;
		this.daoImplName = daoImplName;
		this.build = build;
		this.updated = updated;
		this.menuName = menuName;
		this.headerName = headerName;
		this.convertedTableName = convertedTableName;
		this.rn_fb_lines = rn_fb_lines;
		this.module = module;
		this.rn_cff_actionBuilder = rn_cff_actionBuilder;
	}

	@Override
	public String toString() {
		return "Rn_Fb_Header [techStack=" + techStack + ", objectType=" + objectType + ", subObjectType="
				+ subObjectType + ", uiName=" + uiName + ", formType=" + formType + ", tableName=" + tableName
				+ ", lineTableName=" + lineTableName + ", multilineTableName=" + multilineTableName + ", formCode="
				+ formCode + ", jspName=" + jspName + ", controllerName=" + controllerName + ", serviceName="
				+ serviceName + ", serviceImplName=" + serviceImplName + ", daoName=" + daoName + ", daoImplName="
				+ daoImplName + ", build=" + build + ", updated=" + updated + ", menuName=" + menuName + ", headerName="
				+ headerName + ", convertedTableName=" + convertedTableName + ", rn_fb_lines=" + rn_fb_lines
				+ ", module=" + module + ", rn_cff_actionBuilder=" + rn_cff_actionBuilder + "]";
	}

	

}