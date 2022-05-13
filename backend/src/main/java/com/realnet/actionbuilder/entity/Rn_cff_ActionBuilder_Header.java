package com.realnet.actionbuilder.entity;

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

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.realnet.fnd.entity.Rn_Who_AccId_Column;
import com.realnet.wfb.entity.Rn_Fb_Header;

import lombok.ToString;

@ToString(exclude = "actionBuilderLines")
@Entity
@Table(name = "RN_CFF_ACTION_BUILDER_HEADER_T")
public class Rn_cff_ActionBuilder_Header extends Rn_Who_AccId_Column {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private Integer id;

//	@Column(name = "RN_FB_HEADER_ID")
//	private int rn_fb_header_id;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "RN_FB_HEADER_ID")
	@JsonBackReference
	private Rn_Fb_Header rn_fb_header; // need mod

	@Column(name = "TECHNOLOGY_STACK")
	private String technologyStack;

	@Column(name = "CONTROLLER_NAME")
	private String controllerName;

	@Column(name = "METHOD_NAME")
	private String methodName;

	@Column(name = "ACTION_NAME")
	private String actionName;

	@Column(name = "FILE_LOCATION")
	private String fileLocation;
	
	@OneToMany(mappedBy = "rn_cff_actionBuilderHeader", targetEntity = Rn_cff_ActionBuilder_Lines.class, cascade = CascadeType.ALL)
	@JsonManagedReference
	private List<Rn_cff_ActionBuilder_Lines> actionBuilderLines;

	public Rn_cff_ActionBuilder_Header() {
		super();
	}

	public Rn_cff_ActionBuilder_Header(Integer id, Rn_Fb_Header rn_fb_header, String technologyStack,
			String controllerName, String methodName, String actionName, String fileLocation,
			List<Rn_cff_ActionBuilder_Lines> actionBuilderLines) {
		super();
		this.id = id;
		this.rn_fb_header = rn_fb_header;
		this.technologyStack = technologyStack;
		this.controllerName = controllerName;
		this.methodName = methodName;
		this.actionName = actionName;
		this.fileLocation = fileLocation;
		this.actionBuilderLines = actionBuilderLines;
	}



	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Rn_Fb_Header getRn_fb_header() {
		return rn_fb_header;
	}

	public void setRn_fb_header(Rn_Fb_Header rn_fb_header) {
		this.rn_fb_header = rn_fb_header;
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

	public List<Rn_cff_ActionBuilder_Lines> getActionBuilderLines() {
		return actionBuilderLines;
	}

	public void setActionBuilderLines(List<Rn_cff_ActionBuilder_Lines> actionBuilderLines) {
		this.actionBuilderLines = actionBuilderLines;
	}
}
