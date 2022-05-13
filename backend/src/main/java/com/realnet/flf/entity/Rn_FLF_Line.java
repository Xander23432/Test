package com.realnet.flf.entity;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.realnet.fnd.entity.Rn_Who_AccId_Column;
import com.realnet.wfb.entity.Rn_Fb_Header;

@Entity
@Table(name = "RN_FLF_LINE_T")
public class Rn_FLF_Line extends Rn_Who_AccId_Column {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private int id;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "HEADER_ID")
	@JsonBackReference
	private Rn_FLF_Header rn_flf_header;

	@Column(name = "TECH_STACK")
	private String techStack;

	@Column(name = "OBJECT_TYPE")
	private String objectType;

	@Column(name = "SUB_OBJECT_TYPE")
	private String subObjectType;

	@Column(name = "JAVA")
	private String java;

	@Column(name = "JSP")
	private String jsp;

	@Column(name = "JAVASCRIPT")
	private String javascript;

	@Column(name = "XML")
	private String xml;

	@Column(name = "FIELDTYPE")
	private String fieldtype;

	public Rn_FLF_Line() {
		super();
	}

	public Rn_FLF_Line(int id, Rn_FLF_Header rn_flf_header, String techStack, String objectType, String subObjectType,
			String java, String jsp, String javascript, String xml, String fieldtype) {
		super();
		this.id = id;
		this.rn_flf_header = rn_flf_header;
		this.techStack = techStack;
		this.objectType = objectType;
		this.subObjectType = subObjectType;
		this.java = java;
		this.jsp = jsp;
		this.javascript = javascript;
		this.xml = xml;
		this.fieldtype = fieldtype;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Rn_FLF_Header getRn_flf_header() {
		return rn_flf_header;
	}

	public void setRn_flf_header(Rn_FLF_Header rn_flf_header) {
		this.rn_flf_header = rn_flf_header;
	}

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

	public String getJava() {
		return java;
	}

	public void setJava(String java) {
		this.java = java;
	}

	public String getJsp() {
		return jsp;
	}

	public void setJsp(String jsp) {
		this.jsp = jsp;
	}

	public String getJavascript() {
		return javascript;
	}

	public void setJavascript(String javascript) {
		this.javascript = javascript;
	}

	public String getXml() {
		return xml;
	}

	public void setXml(String xml) {
		this.xml = xml;
	}

	public String getFieldtype() {
		return fieldtype;
	}

	public void setFieldtype(String fieldtype) {
		this.fieldtype = fieldtype;
	}

}