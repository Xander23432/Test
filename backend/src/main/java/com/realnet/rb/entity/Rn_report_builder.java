package com.realnet.rb.entity;

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
import com.realnet.fnd.entity.Rn_Forms_Component_Setup;
import com.realnet.fnd.entity.Rn_Module_Setup;
import com.realnet.fnd.entity.Rn_Who_Columns;

import lombok.ToString;


@ToString(exclude = {"module"})
@Entity
@Table(name = "RN_RB_REPORTS_T")
public class Rn_report_builder extends Rn_Who_Columns
{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private int id;
	
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getReport_name() {
		return report_name;
	}

	public void setReport_name(String report_name) {
		this.report_name = report_name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getReport_tags() {
		return report_tags;
	}

	public void setReport_tags(String report_tags) {
		this.report_tags = report_tags;
	}

	public String getDate_string() {
		return date_string;
	}

	public void setDate_string(String date_string) {
		this.date_string = date_string;
	}

	public String getAdd_param_string() {
		return add_param_string;
	}

	public void setAdd_param_string(String add_param_string) {
		this.add_param_string = add_param_string;
	}

	public String getMaster_select() {
		return master_select;
	}

	public void setMaster_select(String master_select) {
		this.master_select = master_select;
	}

	public String getGrid_headers() {
		return grid_headers;
	}

	public void setGrid_headers(String grid_headers) {
		this.grid_headers = grid_headers;
	}

	
	
	@ManyToOne(fetch = FetchType.LAZY,cascade=CascadeType.ALL)
	@JoinColumn(name = "MODULE_ID",insertable = false, updatable = false)
	@JsonBackReference
	private Rn_Module_Setup module;
	
	
	
/*	@Column(name = "REPORT_ID")
	private int report_id;*/
	
/*	public int getReport_id() {
		return report_id;
	}
	public void setReport_id(int report_id) {
		this.report_id = report_id;
	}*/

	

	

	public Rn_Module_Setup getModule() {
		return module;
	}

	public void setModule(Rn_Module_Setup module) {
		this.module = module;
	}

	@Column(name = "REPORT_NAME")
	private String report_name;
	
	@Column(name = "DESCRIPTION")
	private String description;
	
	
	@Column(name = "REPORT_TAGS")
	private String report_tags;
	
	
	
	@Column(name = "DATE_STRING")
	private String date_string;
	
	@Column(name = "ADD_PARAM_STRING")
	private String add_param_string;
	
	@Column(name = "MASTER_SELECT")
	private String master_select;
	
	@Column(name = "GRID_HEADERS")
	private String grid_headers;
	
	@Column(name = "STD_PARAM_VIEW")
	private String std_param_view;
	
	@Column(name = "GRID_VALUES")
	private String grid_values;
	
	
	@Column(name = "MODEL_STRING")
	private String model_string;
	
	@Column(name = "MODULE_ID")
	private int module_id;
	
	@Column(name = "uiname")
	private String uiname;
	
	@Column(name="ServiceName")
	private String servicename;
	
	@Column(name="ReportType")
	private String reporttype; 
	
	

	public int getModule_id() {
		return module_id;
	}

	public void setModule_id(int module_id) {
		this.module_id = module_id;
	}

	public String getModel_string() {
		return model_string;
	}

	public void setModel_string(String model_string) {
		this.model_string = model_string;
	}

	public String getGrid_values() {
		return grid_values;
	}

	public void setGrid_values(String grid_values) {
		this.grid_values = grid_values;
	}

	public String getStd_param_view() {
		return std_param_view;
	}

	public void setStd_param_view(String std_param_view) {
		this.std_param_view = std_param_view;
	}
	
	
	@Column(name = "ACCOUNT_ID")
	private int account_id;
	
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

	public String getIs_build() {
		return is_build;
	}

	public void setIs_build(String is_build) {
		this.is_build = is_build;
	}

	public Rn_report_builder() {
		super();
	}

	public String getIs_updated() {
		return is_updated;
	}

	public Rn_report_builder(int id, Rn_Module_Setup module, String report_name, String description,
			String report_tags, String date_string, String add_param_string, String master_select, String grid_headers,
			String std_param_view, String grid_values, String model_string, int module_id, int account_id,
			int project_id, String is_build, String is_updated) {
		super();
		this.id = id;
		this.module = module;
		this.report_name = report_name;
		this.description = description;
		this.report_tags = report_tags;
		this.date_string = date_string;
		this.add_param_string = add_param_string;
		this.master_select = master_select;
		this.grid_headers = grid_headers;
		this.std_param_view = std_param_view;
		this.grid_values = grid_values;
		this.model_string = model_string;
		this.module_id = module_id;
		this.account_id = account_id;
		this.project_id = project_id;
		this.is_build = is_build;
		this.is_updated = is_updated;
		
	}

	public void setIs_updated(String is_updated) {
		this.is_updated = is_updated;
	}

	public String getUiname() {
		return uiname;
	}

	public void setUiname(String uiname) {
		this.uiname = uiname;
	}

	public String getServicename() {
		return servicename;
	}

	public void setServicename(String servicename) {
		this.servicename = servicename;
	}

	public String getReporttype() {
		return reporttype;
	}

	public void setReporttype(String reporttype) {
		this.reporttype = reporttype;
	}



	@Column(name = "PROJECT_ID")
	private int project_id;
	
	@Column(name = "IS_BUILD")
	private String is_build;
	
	@Column(name = "IS_UPDATED")
	private String is_updated;
	
	/*
	 * @OneToMany(mappedBy = "rn_report_builder", cascade = CascadeType.ALL)
	 * 
	 * @JsonManagedReference private List<Rn_rb_Tables> components;
	 * 
	 * 
	 * public List<Rn_rb_Tables> getComponents() { return components; }
	 * 
	 * public void setComponents(List<Rn_rb_Tables> components) { this.components =
	 * components; }
	 */
	
}
