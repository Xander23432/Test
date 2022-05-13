package com.realnet.wfb.entity;

import java.util.Date;



public class Rn_Fb_Line_DTO {
	
	
	
	private Integer id;
	private Date createdAt;
	
	private Date updatedAt;
	private Long createdBy;
	private Long updatedBy;
	private Long accountId;
	
	private String fieldName;

	
	private String mapping;

	
	private String dataType;

	
	private String formCode;


	private String key1;


	private String type1;


	private boolean mandatory;


	private boolean hidden;

	private boolean readonly;


	private boolean dependent;


	private String dependent_on;


	private String dependent_sp;

	
	private String dependent_sp_param;

	
	private boolean validation_1;


	private String val_type;


	private String val_sp;


	private String val_sp_param;

	
	private boolean sequence;

	
	private String seq_name;

	private String seq_sp;

	private String seq_sp_param;

	private boolean default_1;

	private String default_type;

	private String default_value;

	private String default_sp;

	private String default_sp_param;

	private boolean calculated_field;

	private String cal_sp;

	private String cal_sp_param;

	private boolean add_to_grid;

	private boolean sp_for_autocomplete;

	private boolean sp_for_dropdown;

	private String sp_name_for_autocomplete;
	
	private String sp_name_for_dropdown;

	private String type_field;

	private String methodName;

	private int seq;

	private String form_type;

	private int section_num;

	private int button_num;

	private String type2;

	private String line_table_name;

	private int line_table_no;

	private String table_name;
		
	private String action;
	
	private String request_param;
	
	private String action_uiname;
	
	 public String getTable_name() {
		return table_name;
	}
	public void setTable_name(String table_name) {
		this.table_name = table_name;
	}
	public Integer getId() {
			return id;
		}
		public void setId(Integer id) {
			this.id = id;
		}
	
	public String getFieldName() {
		return fieldName;
	}
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	public String getMapping() {
		return mapping;
	}
	public void setMapping(String mapping) {
		this.mapping = mapping;
	}
	public String getDataType() {
		return dataType;
	}
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	public String getFormCode() {
		return formCode;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public String getRequest_param() {
		return request_param;
	}
	public void setRequest_param(String request_param) {
		this.request_param = request_param;
	}
	public String getAction_uiname() {
		return action_uiname;
	}
	public void setAction_uiname(String action_uiname) {
		this.action_uiname = action_uiname;
	}
	public void setFormCode(String formCode) {
		this.formCode = formCode;
	}
	public String getKey1() {
		return key1;
	}
	public void setKey1(String key1) {
		this.key1 = key1;
	}
	public String getType1() {
		return type1;
	}
	public void setType1(String type1) {
		this.type1 = type1;
	}
	public boolean isMandatory() {
		return mandatory;
	}
	public void setMandatory(boolean mandatory) {
		this.mandatory = mandatory;
	}
	public boolean isHidden() {
		return hidden;
	}
	public void setHidden(boolean hidden) {
		this.hidden = hidden;
	}
	public boolean isReadonly() {
		return readonly;
	}
	public void setReadonly(boolean readonly) {
		this.readonly = readonly;
	}
	public boolean isDependent() {
		return dependent;
	}
	public void setDependent(boolean dependent) {
		this.dependent = dependent;
	}
	public String getDependent_on() {
		return dependent_on;
	}
	public void setDependent_on(String dependent_on) {
		this.dependent_on = dependent_on;
	}
	public String getDependent_sp() {
		return dependent_sp;
	}
	public void setDependent_sp(String dependent_sp) {
		this.dependent_sp = dependent_sp;
	}
	public String getDependent_sp_param() {
		return dependent_sp_param;
	}
	public void setDependent_sp_param(String dependent_sp_param) {
		this.dependent_sp_param = dependent_sp_param;
	}
	public boolean isValidation_1() {
		return validation_1;
	}
	public void setValidation_1(boolean validation_1) {
		this.validation_1 = validation_1;
	}
	public String getVal_type() {
		return val_type;
	}
	public void setVal_type(String val_type) {
		this.val_type = val_type;
	}
	public String getVal_sp() {
		return val_sp;
	}
	public void setVal_sp(String val_sp) {
		this.val_sp = val_sp;
	}
	public String getVal_sp_param() {
		return val_sp_param;
	}
	public void setVal_sp_param(String val_sp_param) {
		this.val_sp_param = val_sp_param;
	}
	public boolean isSequence() {
		return sequence;
	}
	public void setSequence(boolean sequence) {
		this.sequence = sequence;
	}
	public String getSeq_name() {
		return seq_name;
	}
	public void setSeq_name(String seq_name) {
		this.seq_name = seq_name;
	}
	public String getSeq_sp() {
		return seq_sp;
	}
	public void setSeq_sp(String seq_sp) {
		this.seq_sp = seq_sp;
	}
	public String getSeq_sp_param() {
		return seq_sp_param;
	}
	public void setSeq_sp_param(String seq_sp_param) {
		this.seq_sp_param = seq_sp_param;
	}
	public boolean isDefault_1() {
		return default_1;
	}
	public void setDefault_1(boolean default_1) {
		this.default_1 = default_1;
	}
	public String getDefault_type() {
		return default_type;
	}
	public void setDefault_type(String default_type) {
		this.default_type = default_type;
	}
	public String getDefault_value() {
		return default_value;
	}
	public void setDefault_value(String default_value) {
		this.default_value = default_value;
	}
	public String getDefault_sp() {
		return default_sp;
	}
	public void setDefault_sp(String default_sp) {
		this.default_sp = default_sp;
	}
	public String getDefault_sp_param() {
		return default_sp_param;
	}
	public void setDefault_sp_param(String default_sp_param) {
		this.default_sp_param = default_sp_param;
	}
	public boolean isCalculated_field() {
		return calculated_field;
	}
	public void setCalculated_field(boolean calculated_field) {
		this.calculated_field = calculated_field;
	}
	public String getCal_sp() {
		return cal_sp;
	}
	public void setCal_sp(String cal_sp) {
		this.cal_sp = cal_sp;
	}
	public String getCal_sp_param() {
		return cal_sp_param;
	}
	public void setCal_sp_param(String cal_sp_param) {
		this.cal_sp_param = cal_sp_param;
	}
	public boolean isAdd_to_grid() {
		return add_to_grid;
	}
	public void setAdd_to_grid(boolean add_to_grid) {
		this.add_to_grid = add_to_grid;
	}
	public boolean isSp_for_autocomplete() {
		return sp_for_autocomplete;
	}
	public void setSp_for_autocomplete(boolean sp_for_autocomplete) {
		this.sp_for_autocomplete = sp_for_autocomplete;
	}
	public boolean isSp_for_dropdown() {
		return sp_for_dropdown;
	}
	public void setSp_for_dropdown(boolean sp_for_dropdown) {
		this.sp_for_dropdown = sp_for_dropdown;
	}
	public String getSp_name_for_autocomplete() {
		return sp_name_for_autocomplete;
	}
	public void setSp_name_for_autocomplete(String sp_name_for_autocomplete) {
		this.sp_name_for_autocomplete = sp_name_for_autocomplete;
	}
	public String getSp_name_for_dropdown() {
		return sp_name_for_dropdown;
	}
	public void setSp_name_for_dropdown(String sp_name_for_dropdown) {
		this.sp_name_for_dropdown = sp_name_for_dropdown;
	}
	public String getType_field() {
		return type_field;
	}
	public void setType_field(String type_field) {
		this.type_field = type_field;
	}
	public String getMethodName() {
		return methodName;
	}
	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}
	public int getSeq() {
		return seq;
	}
	public void setSeq(int seq) {
		this.seq = seq;
	}
	public String getForm_type() {
		return form_type;
	}
	public void setForm_type(String form_type) {
		this.form_type = form_type;
	}
	public int getSection_num() {
		return section_num;
	}
	public void setSection_num(int section_num) {
		this.section_num = section_num;
	}
	public int getButton_num() {
		return button_num;
	}
	public void setButton_num(int button_num) {
		this.button_num = button_num;
	}
	public String getType2() {
		return type2;
	}
	public void setType2(String type2) {
		this.type2 = type2;
	}
	
	public String getLine_table_name() {
		return line_table_name;
	}
	public void setLine_table_name(String line_table_name) {
		this.line_table_name = line_table_name;
	}
	public int getLine_table_no() {
		return line_table_no;
	}
	public void setLine_table_no(int line_table_no) {
		this.line_table_no = line_table_no;
	}
	public Date getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}
	public Date getUpdatedAt() {
		return updatedAt;
	}
	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}
	public Long getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(Long createdBy) {
		this.createdBy = createdBy;
	}
	public Long getUpdatedBy() {
		return updatedBy;
	}
	public void setUpdatedBy(Long updatedBy) {
		this.updatedBy = updatedBy;
	}
	public Long getAccountId() {
		return accountId;
	}
	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}

}
