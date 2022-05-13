package com.realnet.actionbuilder.entity;

import java.util.List;

public class PropertiesDTO {

	private String table_name;
	private String controller_name;
	private String dao_name;
	private String dao_impl_name;
	private String service_name;
	private String service_impl_name;
	private String jsp_name;
	private String form_code;
	private List<String> fieldList;
	
	private String line_table_name;
	private List<String> line_fields;
	
	

	public PropertiesDTO() {
			super();
			// TODO Auto-generated constructor stub
		}

	// header only
	public PropertiesDTO(String table_name, String controller_name, String dao_name, String dao_impl_name,
				String service_name, String service_impl_name, String jsp_name, String form_code, List<String> fieldList) {
			super();
			this.table_name = table_name;
			this.controller_name = controller_name;
			this.dao_name = dao_name;
			this.dao_impl_name = dao_impl_name;
			this.service_name = service_name;
			this.service_impl_name = service_impl_name;
			this.jsp_name = jsp_name;
			this.form_code = form_code;
			this.fieldList = fieldList;
		}
	
	// header-line
	public PropertiesDTO(String table_name, String controller_name, String dao_name, String dao_impl_name,
			String service_name, String service_impl_name, String jsp_name, String form_code, List<String> fieldList,
			String line_table_name, List<String> line_fields) {
		super();
		this.table_name = table_name;
		this.controller_name = controller_name;
		this.dao_name = dao_name;
		this.dao_impl_name = dao_impl_name;
		this.service_name = service_name;
		this.service_impl_name = service_impl_name;
		this.jsp_name = jsp_name;
		this.form_code = form_code;
		this.fieldList = fieldList;
		this.line_table_name = line_table_name;
		this.line_fields = line_fields;
	}

	public String getTable_name() {
		return table_name;
	}

	public void setTable_name(String table_name) {
		this.table_name = table_name;
	}

	public String getController_name() {
		return controller_name;
	}

	public void setController_name(String controller_name) {
		this.controller_name = controller_name;
	}

	public String getDao_name() {
		return dao_name;
	}

	public void setDao_name(String dao_name) {
		this.dao_name = dao_name;
	}

	public String getDao_impl_name() {
		return dao_impl_name;
	}

	public void setDao_impl_name(String dao_impl_name) {
		this.dao_impl_name = dao_impl_name;
	}

	public String getService_name() {
		return service_name;
	}

	public void setService_name(String service_name) {
		this.service_name = service_name;
	}

	public String getService_impl_name() {
		return service_impl_name;
	}

	public void setService_impl_name(String service_impl_name) {
		this.service_impl_name = service_impl_name;
	}

	public String getJsp_name() {
		return jsp_name;
	}

	public void setJsp_name(String jsp_name) {
		this.jsp_name = jsp_name;
	}

	public String getForm_code() {
		return form_code;
	}

	public void setForm_code(String form_code) {
		this.form_code = form_code;
	}

	public List<String> getFieldList() {
		return fieldList;
	}

	public void setFieldList(List<String> fieldList) {
		this.fieldList = fieldList;
	}

	public String getLine_table_name() {
		return line_table_name;
	}

	public void setLine_table_name(String line_table_name) {
		this.line_table_name = line_table_name;
	}

	public List<String> getLine_fields() {
		return line_fields;
	}

	public void setLine_fields(List<String> line_fields) {
		this.line_fields = line_fields;
	}

}
