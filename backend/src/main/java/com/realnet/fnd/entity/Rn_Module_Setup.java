package com.realnet.fnd.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.realnet.rb.entity.Rn_report_builder;
import com.realnet.wfb.entity.Rn_Fb_Header;

import lombok.ToString;

@ToString(exclude = {"project", "rn_fb_headers","rn_report_builder"})
@Entity
@Table(name = "RN_MODULE_SETUP")
public class Rn_Module_Setup extends Rn_Who_AccId_Column {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private Integer id;

	@Column(name = "MODULE_NAME")
	private String moduleName;

	@Column(name = "DESCRIPTION")
	private String description;

	@Column(name = "MODULE_PREFIX")
	private String modulePrefix;

	@Column(name = "COPY_TO")
	private String copyTo;

	@Column(name = "TECHNOLOGY_STACK")
	private String technologyStack;

	// this should be many to one
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PROJECT_ID")
	@JsonBackReference
	private Rn_Project_Setup project;
	@Transient
	private String projectName;
	// wireframe header
//	@OneToMany(mappedBy = "module", targetEntity = Rn_Fb_Header.class, orphanRemoval = true, cascade = {
//			CascadeType.PERSIST, CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH }, fetch = FetchType.EAGER)
//	@JsonManagedReference
//	private List<Rn_Fb_Header> rn_fb_headers;
	
	@OneToMany(mappedBy = "module", targetEntity = Rn_Fb_Header.class, orphanRemoval = true, cascade = CascadeType.PERSIST)
	@JsonManagedReference
	private List<Rn_Fb_Header> rn_fb_headers;
	
	@OneToMany(mappedBy = "module", targetEntity = Rn_report_builder.class, orphanRemoval = true, cascade = CascadeType.PERSIST,fetch = FetchType.EAGER)
	@JsonManagedReference
	private List<Rn_report_builder> rn_report_builder;

	@Transient
	private int projectId;
	
	public Rn_Module_Setup() {
		super();
	}
	
	

	public Rn_Module_Setup(Integer id, String moduleName, String description, String modulePrefix, String copyTo,
			String technologyStack, Rn_Project_Setup project, List<Rn_Fb_Header> rn_fb_headers,List<Rn_report_builder> rn_report_builder
) {
		super();
		this.id = id;
		this.moduleName = moduleName;
		this.description = description;
		this.modulePrefix = modulePrefix;
		this.copyTo = copyTo;
		this.technologyStack = technologyStack;
		this.project = project;
		this.rn_fb_headers = rn_fb_headers;
		this.rn_report_builder=rn_report_builder;
		;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getModuleName() {
		return moduleName;
	}

	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getModulePrefix() {
		return modulePrefix;
	}

	public void setModulePrefix(String modulePrefix) {
		this.modulePrefix = modulePrefix;
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

	public Rn_Project_Setup getProject() {
		return project;
	}

	public void setProject(Rn_Project_Setup project) {
		this.project = project;
	}

	public List<Rn_Fb_Header> getRn_fb_headers() {
		return rn_fb_headers;
	}

	public void setRn_fb_headers(List<Rn_Fb_Header> rn_fb_headers) {
		this.rn_fb_headers = rn_fb_headers;
	}

	public List<Rn_report_builder> getRn_report_builder() {
		return rn_report_builder;
	}

	public void setRn_report_builder(List<Rn_report_builder> rn_report_builder) {
		this.rn_report_builder = rn_report_builder;
	}



	public int getProjectId() {
		return projectId;
	}



	public void setProjectId(int projectId) {
		this.projectId = projectId;
	}



	public String getProjectName() {
		return projectName;
	}



	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	
	

}