package com.realnet.fnd.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "RN_EXT_FIELD_T")
public class Rn_Ext_Fields extends Rn_Who_Columns {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private int id;

	@Column(name = "TYPE")
	private String type;

	@Column(name = "FIELD_NAME")
	private String field_name;

	@Column(name = "MAPPING")
	private String mapping;

	@Column(name = "DATA_TYPE")
	private String data_type;

	@Column(name = "FORM_CODE")
	private String form_code;

	@Column(name = "IS_ACTIVE")
	private boolean isActive;
	
	@Column(name = "ACCOUNT_ID")
	private long account_id;
	
	public long getAccount_id() {
		return account_id;
	}

	public void setAccount_id(long account_id) {
		this.account_id = account_id;
	}

	

	
	

//	@Column(name = "SP_NAME_FOR_AUTOCOMPLETE")
//	private String sp_name_for_autocomplete;
//
//	@Column(name = "SP_FOR_AUTOCOMPLETE")
//	private String sp_for_autocomplete;
//
//	@Column(name = "EXT_AUTO_ID")
//	private String ext_auto_id;
//
//	@Column(name = "EXT_DEPENDENT_ID")
//	private String ext_dependent_id;
//
//	@Column(name = "EXT_DD_ID")
//	private String ext_dd_id;
//
//	@Column(name = "SP_NAME")
//	private String sp_name;
//
//	@Column(name = "DROP_VALUE")
//	private String drop_value;
//
//	@Column(name = "DROPDOWN")
//	private String dropdown;
//
//	@Column(name = "MANDATORY")
//	private String mandatory;
//
//	@Column(name = "HIDDEN")
//	private String hidden;
//
//	@Column(name = "READONLY")
//	private String readonly;
//
//	@Column(name = "DEPENDENT")
//	private String dependent;
//	@Column(name = "SEQ_NAME")
//	private String seq_name;
//
//	@Column(name = "DEPENDENT_SP")
//	private String dependent_sp;
//
//	@Column(name = "DEPENDENT_SP_PARAM")
//	private String dependent_sp_param;
//
//	@Column(name = "VALIDATION_1")
//	private String validation_1;
//
//	@Column(name = "VAL_TYPE")
//	private String val_type;
//
//	@Column(name = "VAL_SP")
//	private String val_sp;
//
//	@Column(name = "VAL_SP_PARAM")
//	private String val_sp_param;
//
//	@Column(name = "SEQUENCE")
//	private String sequence;
//
//	@Column(name = "SEQ_SP")
//	private String seq_sp;
//
//	@Column(name = "SEQ_SP_PARAM")
//	private String seq_sp_param;
//
//	@Column(name = "DEFAULT_1")
//	private String default_1;
//
//	@Column(name = "DEFAULT_TYPE")
//	private String default_type;
//
//	@Column(name = "DEFAULT_VALUE")
//	private String default_value;
//
//	@Column(name = "DEFAULT_SP")
//	private String default_sp;
//
//	@Column(name = "DEFAULT_SP_PARAM")
//	private String default_sp_param;
//
//	@Column(name = "CALCULATED_FIELD")
//	private String calculated_field;
//
//	@Column(name = "CAL_SP")
//	private String cal_sp;
//
//	@Column(name = "CAL_SP_PARAM")
//	private String cal_sp_param;
//
//	@Column(name = "ADD_TO_GRID")
//	private String add_to_grid;
//
//	@Column(name = "DEPENDENT_ON")
//	private String dependent_on;
//	@Column(name = "RADIO")
//	private String radio;
//	@Column(name = "RADIO_OPTION")
//	private String radio_option;


	public boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(boolean isActive) {
		this.isActive = isActive;
	}

	// public String getRadio() {
//		return radio;
//	}
//
//	public void setRadio(String radio) {
//		this.radio = radio;
//	}
//
//	public String getRadio_option() {
//		return radio_option;
//	}
//
//	public void setRadio_option(String radio_option) {
//		this.radio_option = radio_option;
//	}
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

//	public String getValidation_1() {
//		return validation_1;
//	}
//
//	public void setValidation_1(String validation_1) {
//		this.validation_1 = validation_1;
//	}

	public String getForm_code() {
		return form_code;
	}

	public void setForm_code(String form_code) {
		this.form_code = form_code;
	}

	public String getField_name() {
		return field_name;
	}

	public void setField_name(String field_name) {
		this.field_name = field_name;
	}

	public String getMapping() {
		return mapping;
	}

	public void setMapping(String mapping) {
		this.mapping = mapping;
	}

	public String getData_type() {
		return data_type;
	}

	public void setData_type(String data_type) {
		this.data_type = data_type;
	}

//	public String getSeq_name() {
//		return seq_name;
//	}
//
//	public void setSeq_name(String seq_name) {
//		this.seq_name = seq_name;
//	}
//
//	public String getDependent_on() {
//		return dependent_on;
//	}
//
//	public void setDependent_on(String dependent_on) {
//		this.dependent_on = dependent_on;
//	}
//
//	public String getDependent_sp() {
//		return dependent_sp;
//	}
//
//	public void setDependent_sp(String dependent_sp) {
//		this.dependent_sp = dependent_sp;
//	}
//
//	public String getDependent_sp_param() {
//		return dependent_sp_param;
//	}
//
//	public void setDependent_sp_param(String dependent_sp_param) {
//		this.dependent_sp_param = dependent_sp_param;
//	}
//
//	public String getVal_type() {
//		return val_type;
//	}
//
//	public void setVal_type(String val_type) {
//		this.val_type = val_type;
//	}
//
//	public String getVal_sp() {
//		return val_sp;
//	}
//
//	public void setVal_sp(String val_sp) {
//		this.val_sp = val_sp;
//	}
//
//	public String getVal_sp_param() {
//		return val_sp_param;
//	}
//
//	public void setVal_sp_param(String val_sp_param) {
//		this.val_sp_param = val_sp_param;
//	}
//
//	public String getSequence() {
//		return sequence;
//	}
//
//	public void setSequence(String sequence) {
//		this.sequence = sequence;
//	}
//
//	public String getSeq_sp() {
//		return seq_sp;
//	}
//
//	public void setSeq_sp(String seq_sp) {
//		this.seq_sp = seq_sp;
//	}
//
//	public String getSeq_sp_param() {
//		return seq_sp_param;
//	}
//
//	public void setSeq_sp_param(String seq_sp_param) {
//		this.seq_sp_param = seq_sp_param;
//	}
//
//	public String getDefault_1() {
//		return default_1;
//	}
//
//	public void setDefault_1(String default_1) {
//		this.default_1 = default_1;
//	}
//
//	public String getDefault_type() {
//		return default_type;
//	}
//
//	public void setDefault_type(String default_type) {
//		this.default_type = default_type;
//	}
//
//	public String getDefault_value() {
//		return default_value;
//	}
//
//	public void setDefault_value(String default_value) {
//		this.default_value = default_value;
//	}
//
//	public String getDefault_sp() {
//		return default_sp;
//	}
//
//	public void setDefault_sp(String default_sp) {
//		this.default_sp = default_sp;
//	}
//
//	public String getDefault_sp_param() {
//		return default_sp_param;
//	}
//
//	public void setDefault_sp_param(String default_sp_param) {
//		this.default_sp_param = default_sp_param;
//	}
//
//	public String getCalculated_field() {
//		return calculated_field;
//	}
//
//	public void setCalculated_field(String calculated_field) {
//		this.calculated_field = calculated_field;
//	}
//
//	public String getCal_sp() {
//		return cal_sp;
//	}
//
//	public void setCal_sp(String cal_sp) {
//		this.cal_sp = cal_sp;
//	}
//
//	public String getCal_sp_param() {
//		return cal_sp_param;
//	}
//
//	public void setCal_sp_param(String cal_sp_param) {
//		this.cal_sp_param = cal_sp_param;
//	}
//
//	public String getAdd_to_grid() {
//		return add_to_grid;
//	}
//
//	public void setAdd_to_grid(String add_to_grid) {
//		this.add_to_grid = add_to_grid;
//	}

//	public String getMandatory() {
//		return mandatory;
//	}
//
//	public void setMandatory(String mandatory) {
//		this.mandatory = mandatory;
//	}
//
//	public String getHidden() {
//		return hidden;
//	}
//
//	public void setHidden(String hidden) {
//		this.hidden = hidden;
//	}
//
//	public String getReadonly() {
//		return readonly;
//	}
//
//	public void setReadonly(String readonly) {
//		this.readonly = readonly;
//	}
//
//	public String getDependent() {
//		return dependent;
//	}
//
//	public void setDependent(String dependent) {
//		this.dependent = dependent;
//	}
//
//	public String getExt_dependent_id() {
//		return ext_dependent_id;
//	}
//
//	public void setExt_dependent_id(String ext_dependent_id) {
//		this.ext_dependent_id = ext_dependent_id;
//	}
//
//	public String getExt_auto_id() {
//		return ext_auto_id;
//	}
//
//	public void setExt_auto_id(String ext_auto_id) {
//		this.ext_auto_id = ext_auto_id;
//	}
//
//	public String getSp_name_for_autocomplete() {
//		return sp_name_for_autocomplete;
//	}
//
//	public void setSp_name_for_autocomplete(String sp_name_for_autocomplete) {
//		this.sp_name_for_autocomplete = sp_name_for_autocomplete;
//	}
//
//	public String getSp_for_autocomplete() {
//		return sp_for_autocomplete;
//	}
//
//	public void setSp_for_autocomplete(String sp_for_autocomplete) {
//		this.sp_for_autocomplete = sp_for_autocomplete;
//	}
//
//	public String getDropdown() {
//		return dropdown;
//	}
//
//	public void setDropdown(String dropdown) {
//		this.dropdown = dropdown;
//	}
//
//	public String getDrop_value() {
//		return drop_value;
//	}
//
//	public void setDrop_value(String drop_value) {
//		this.drop_value = drop_value;
//	}
//
//	public String getSp_name() {
//		return sp_name;
//	}
//
//	public void setSp_name(String sp_name) {
//		this.sp_name = sp_name;
//	}
//
//	public String getExt_dd_id() {
//		return ext_dd_id;
//	}
//
//	public void setExt_dd_id(String ext_dd_id) {
//		this.ext_dd_id = ext_dd_id;
//	}

}
