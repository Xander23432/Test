//package com.realnet.formdrag.entity;

//import javax.persistence.Entity;
//import javax.persistence.FetchType;
//import javax.persistence.GeneratedValue;
//import javax.persistence.GenerationType;
//import javax.persistence.Id;
//import javax.persistence.ManyToOne;
//import javax.persistence.Table;
//
//@Entity
//@Table(name = "Rn_wf_value")
//public class Value {
//	
//	@Id
//	@GeneratedValue(strategy = GenerationType.AUTO)
//	private Long vId;
//	private String label;
//	private String value;
//	private boolean selected = false;
//	
//	@ManyToOne(fetch = FetchType.EAGER)
//	private Attributes attribute;
//	
//	public Value() {}
//	
//	public Value(Long vId, String label, String value, boolean selected) {
//		super();
//		this.vId = vId;
//		this.label = label;
//		this.value = value;
//		this.selected = selected;
////		this.attribute = attribute;
//	}
//
//	public Long getvId() {
//		return vId;
//	}
//
//	public void setvId(Long vId) {
//		this.vId = vId;
//	}
//
//	public String getLabel() {
//		return label;
//	}
//
//	public void setLabel(String label) {
//		this.label = label;
//	}
//
//	public String getValue() {
//		return value;
//	}
//
//	public void setValue(String value) {
//		this.value = value;
//	}
//
//	public Attributes getAttribute() {
//		return attribute;
//	}
//
//	public void setAttribute(Attributes attribute) {
//		this.attribute = attribute;
//	}
//
//	public boolean isSelected() {
//		return selected;
//	}
//
//	public void setSelected(boolean selected) {
//		this.selected = selected;
//	}
//	
//}
