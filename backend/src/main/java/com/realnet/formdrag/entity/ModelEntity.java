//package com.realnet.formdrag.entity;
//
//import java.util.ArrayList;
//import java.util.List;

//import javax.persistence.CascadeType;
//import javax.persistence.Entity;
//import javax.persistence.FetchType;
//import javax.persistence.GeneratedValue;
//import javax.persistence.GenerationType;
//import javax.persistence.Id;
//import javax.persistence.OneToMany;
//import javax.persistence.OneToOne;
//import javax.persistence.Table;
//
//import com.fasterxml.jackson.annotation.JsonIgnore;
//
//@Entity
//@Table(name = "Rn_wf_model")
//public class ModelEntity {
//	
//	@Id
//	@GeneratedValue(strategy = GenerationType.AUTO)
//	private Long mId;
//	private String name;
//	private String description;
//	
//	@OneToOne(cascade = CascadeType.ALL)
//	private Theme theme;
//	
//	@OneToMany(mappedBy = "model", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
//	@JsonIgnore
//	private List<Attributes> attributes = new ArrayList<>();
//	
////	@OneToMany(cascade = CascadeType.ALL, mappedBy = "attributes")
////	@JsonIgnore
////	private List<Attributes> attributes = new ArrayList<>();
//	
//	public ModelEntity() {}
//	
//	public ModelEntity(Long mId, String name, String description, Theme theme, List<Attributes> attributes) {
//		super();
//		this.mId = mId;
//		this.name = name;
//		this.description = description;
//		this.theme = theme;
//		this.attributes = attributes;
//	}
//
//	public Long getmId() {
//		return mId;
//	}
//
//	public void setmId(Long mId) {
//		this.mId = mId;
//	}
//
//	public String getName() {
//		return name;
//	}
//
//	public void setName(String name) {
//		this.name = name;
//	}
//
//	public String getDescription() {
//		return description;
//	}
//
//	public void setDescription(String description) {
//		this.description = description;
//	}
//
//	public Theme getTheme() {
//		return theme;
//	}
//
//	public void setTheme(Theme theme) {
//		this.theme = theme;
//	}
//
//	public List<Attributes> getAttributes() {
//		return attributes;
//	}
//
//	public void setAttributes(List<Attributes> attributes) {
//		this.attributes = attributes;
//	}
//	
//	
//
//}
