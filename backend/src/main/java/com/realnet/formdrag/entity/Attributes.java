//package com.realnet.formdrag.entity;

//import java.util.ArrayList;
//import java.util.List;
//
//import javax.persistence.CascadeType;
//import javax.persistence.Entity;
//import javax.persistence.FetchType;
//import javax.persistence.GeneratedValue;
//import javax.persistence.GenerationType;
//import javax.persistence.Id;
//import javax.persistence.ManyToOne;
//import javax.persistence.OneToMany;
//import javax.persistence.Table;
//
//import com.fasterxml.jackson.annotation.JsonIgnore;
//
//@Entity
//@Table(name = "Rn_wf_attribute")
//public class Attributes {
//	
//	@Id
//	@GeneratedValue(strategy = GenerationType.AUTO)
//	private Long aId;
//	private String name;
//	private String type;
//	private String icon;
//	private boolean toggle = false;
//	private boolean required = false;
//	private String regex;
//	private String errorText;
//	private String label;
//	private String description;
//	private String placeholder;
//	private String className;
//	private String subtype;
//	private boolean handle = false;
//	private int min;
//	private int max;
//	private boolean inline = false;
//	private String value;
//	
//	@OneToMany(mappedBy = "attribute", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
//	@JsonIgnore
//	private List<Value> values = new ArrayList<>();
//	
////	@OneToMany(cascade = CascadeType.ALL, mappedBy = "values")
////	@JsonIgnore
////	private List<Value> values = new ArrayList<>();
//	
//	@ManyToOne(fetch = FetchType.EAGER)
//	private ModelEntity model;
//	
//	public Attributes() {}
//	
//	public Attributes(Long aId, String name, String type, String icon, boolean toggle, boolean required, String regex,
//			String errorText, String label, String description, String placeholder, String className, String subtypes,
//			boolean handle, int min, int max, boolean inline, String value, List<Value> values) {
//		super();
//		this.aId = aId;
//		this.name = name;
//		this.type = type;
//		this.icon = icon;
//		this.toggle = toggle;
//		this.required = required;
//		this.regex = regex;
//		this.errorText = errorText;
//		this.label = label;
//		this.description = description;
//		this.placeholder = placeholder;
//		this.className = className;
//		this.subtype = subtypes;
//		this.handle = handle;
//		this.min = min;
//		this.max = max;
//		this.inline = inline;
//		this.value = value;
//		this.values = values;
//		
//	}
//
//	public Long getaId() {
//		return aId;
//	}
//
//	public void setaId(Long aId) {
//		this.aId = aId;
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
//	public String getType() {
//		return type;
//	}
//
//	public void setType(String type) {
//		this.type = type;
//	}
//
//	public String getIcon() {
//		return icon;
//	}
//
//	public void setIcon(String icon) {
//		this.icon = icon;
//	}
//
//	public boolean isToggle() {
//		return toggle;
//	}
//
//	public void setToggle(boolean toggle) {
//		this.toggle = toggle;
//	}
//
//	public boolean isRequired() {
//		return required;
//	}
//
//	public void setRequired(boolean required) {
//		this.required = required;
//	}
//
//	public String getRegex() {
//		return regex;
//	}
//
//	public void setRegex(String regex) {
//		this.regex = regex;
//	}
//
//	public String getErrorText() {
//		return errorText;
//	}
//
//	public void setErrorText(String errorText) {
//		this.errorText = errorText;
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
//	public String getDescription() {
//		return description;
//	}
//
//	public void setDescription(String description) {
//		this.description = description;
//	}
//
//	public String getPlaceholder() {
//		return placeholder;
//	}
//
//	public void setPlaceholder(String placeholder) {
//		this.placeholder = placeholder;
//	}
//
//	public String getClassName() {
//		return className;
//	}
//
//	public void setClassName(String className) {
//		this.className = className;
//	}
//
//	public String getSubtypes() {
//		return subtype;
//	}
//
//	public void setSubtypes(String subtypes) {
//		this.subtype = subtypes;
//	}
//
//	public boolean isHandle() {
//		return handle;
//	}
//
//	public void setHandle(boolean handle) {
//		this.handle = handle;
//	}
//
//	public int getMin() {
//		return min;
//	}
//
//	public void setMin(int min) {
//		this.min = min;
//	}
//
//	public int getMax() {
//		return max;
//	}
//
//	public void setMax(int max) {
//		this.max = max;
//	}
//
//	public boolean isInline() {
//		return inline;
//	}
//
//	public void setInline(boolean inline) {
//		this.inline = inline;
//	}
//
//	public List<Value> getValues() {
//		return values;
//	}
//
//	public void setValues(List<Value> values) {
//		this.values = values;
//	}
//
//	public ModelEntity getModel() {
//		return model;
//	}
//
//	public void setModel(ModelEntity model) {
//		this.model = model;
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
//	
//}
