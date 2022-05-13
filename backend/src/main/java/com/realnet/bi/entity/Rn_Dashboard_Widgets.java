package com.realnet.bi.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.realnet.fnd.entity.Rn_Module_Setup;

@Entity
@Table(name = "RN_DASHBOARD_WIDGETS_T")
public class Rn_Dashboard_Widgets {
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
	@GenericGenerator(name = "native", strategy = "native")
	@Column(name = "ID")
	private int id;
	
	@Column(name = "ACCOUNT_ID")
	private int account_id;
	
	@Column(name = "PROJECT_ID")
	private int project_id;
	
	@Column(name = "MODULE_ID")
	private int module_id;
	
	@Column(name = "WIDGET_NAME")
	private String widget_name;
	
	@Column(name = "WIDGET_DESCRIPTION")
	private String widget_description;
	
	@Column(name = "CHART_TYPE")
	private String chart_type;
	
	@Column(name = "SQL_QUERY")
	private String sql_query;
	
	@Column(name = "LABEL")
	private String label;
	
	@Column(name = "COLOR_SCHEME")
	private String color_scheme;
	
	@ManyToOne(fetch = FetchType.LAZY,cascade=CascadeType.ALL)
	@JoinColumn(name = "MODULE_ID",insertable = false, updatable = false)
	@JsonBackReference
	private Rn_Module_Setup moduledata;

	

	public Rn_Module_Setup getModuledata() {
		return moduledata;
	}

	public void setModuledata(Rn_Module_Setup moduledata) {
		this.moduledata = moduledata;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	

	public int getAccount_id() {
		return account_id;
	}

	public void setAccount_id(int account_id) {
		this.account_id = account_id;
	}

	public int getProject_id() {
		return project_id;
	}

	public void setProject_id(int project_id) {
		this.project_id = project_id;
	}

	
	public int getModule_id() {
		return module_id;
	}

	public void setModule_id(int module_id) {
		this.module_id = module_id;
	}

	public String getWidget_name() {
		return widget_name;
	}

	public void setWidget_name(String widget_name) {
		this.widget_name = widget_name;
	}

	public String getWidget_description() {
		return widget_description;
	}

	public void setWidget_description(String widget_description) {
		this.widget_description = widget_description;
	}

	public String getChart_type() {
		return chart_type;
	}

	public void setChart_type(String chart_type) {
		this.chart_type = chart_type;
	}

	public String getSql_query() {
		return sql_query;
	}

	public void setSql_query(String sql_query) {
		this.sql_query = sql_query;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getColor_scheme() {
		return color_scheme;
	}

	public void setColor_scheme(String color_scheme) {
		this.color_scheme = color_scheme;
	}

}