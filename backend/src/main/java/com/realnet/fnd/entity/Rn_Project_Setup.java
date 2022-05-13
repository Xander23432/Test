package com.realnet.fnd.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.ToString;

@ToString(exclude = "modules")
@Entity
@Table(name = "RN_PROJECT_SETUP")
public class Rn_Project_Setup extends Rn_Who_AccId_Column {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private Integer id;

	@Column(name = "PROJECT_NAME")
	private String projectName;

	@Column(name = "DESCRIPTION")
	private String description;

//	@OneToMany(mappedBy = "project", targetEntity = Rn_Module_Setup.class, orphanRemoval = true, cascade = {
//			CascadeType.PERSIST, CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH }, fetch = FetchType.EAGER)
//	@JsonManagedReference
//	private List<Rn_Module_Setup> modules;

	@OneToMany(mappedBy = "project", targetEntity = Rn_Module_Setup.class, orphanRemoval = true, cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
	@JsonManagedReference
	private List<Rn_Module_Setup> modules;

	@Column(name = "COPY_TO")
	private String copyTo;

	@Column(name = "Technology_stack")
	private String technologyStack;

	@Column(name = "TECH_STACK_ID")
	private int techStackId;

	@Column(name = "PROJECT_PREFIX")
	private String projectPrefix;

	@Column(name = "DB_NAME")
	private String dbName;

	@Column(name = "DB_USERNAME")
	private String dbUserName;

	@Column(name = "DB_PASSWORD")
	private String dbPassword;

	@Column(name = "PORT_NO")
	private String portNumber;

	public Rn_Project_Setup() {
		super();
	}

	public Rn_Project_Setup(Integer id, String projectName, String description, List<Rn_Module_Setup> modules,
			String copyTo, String technologyStack, int techStackId, String projectPrefix, String dbName,
			String dbUserName, String dbPassword, String portNumber) {
		super();
		this.id = id;
		this.projectName = projectName;
		this.description = description;
		this.modules = modules;
		this.copyTo = copyTo;
		this.technologyStack = technologyStack;
		this.techStackId = techStackId;
		this.projectPrefix = projectPrefix;
		this.dbName = dbName;
		this.dbUserName = dbUserName;
		this.dbPassword = dbPassword;
		this.portNumber = portNumber;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<Rn_Module_Setup> getModules() {
		return modules;
	}

	public void setModules(List<Rn_Module_Setup> modules) {
		this.modules = modules;
	}

	public String getCopyTo() {
		return copyTo;
	}

	public void setCopyTo(String copyTo) {
		this.copyTo = copyTo;
	}

	public String getTechnologyStack() {
		return technologyStack;
	}

	public void setTechnologyStack(String technologyStack) {
		this.technologyStack = technologyStack;
	}

	public int getTechStackId() {
		return techStackId;
	}

	public void setTechStackId(int techStackId) {
		this.techStackId = techStackId;
	}

	public String getProjectPrefix() {
		return projectPrefix;
	}

	public void setProjectPrefix(String projectPrefix) {
		this.projectPrefix = projectPrefix;
	}

	public String getDbName() {
		return dbName;
	}

	public void setDbName(String dbName) {
		this.dbName = dbName;
	}

	public String getDbUserName() {
		return dbUserName;
	}

	public void setDbUserName(String dbUserName) {
		this.dbUserName = dbUserName;
	}

	public String getDbPassword() {
		return dbPassword;
	}

	public void setDbPassword(String dbPassword) {
		this.dbPassword = dbPassword;
	}

	public String getPortNumber() {
		return portNumber;
	}

	public void setPortNumber(String portNumber) {
		this.portNumber = portNumber;
	}

	@Override
	public String toString() {
		return "Rn_Project_Setup [id=" + id + ", projectName=" + projectName + ", description=" + description
				+ ", copyTo=" + copyTo + ", technologyStack=" + technologyStack
			+ ", techStackId=" + techStackId + ", projectPrefix=" + projectPrefix + ", dbName=" + dbName
				+ ", dbUserName=" + dbUserName + ", dbPassword=" + dbPassword + ", portNumber=" + portNumber + "]";
	}

}