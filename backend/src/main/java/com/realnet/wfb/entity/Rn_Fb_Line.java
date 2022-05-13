package com.realnet.wfb.entity;

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

import lombok.ToString;

@ToString(exclude="rn_fb_header")
@Entity
@Table(name = "RN_FB_LINES")
public class Rn_Fb_Line extends Rn_Who_AccId_Column {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private Integer id;

	@Column(name = "FIELD_NAME")
	private String fieldName;

	@Column(name = "MAPPING")
	private String mapping;

	@Column(name = "DATA_TYPE")
	private String dataType;

	@Column(name = "FORM_CODE")
	private String formCode;

	@Column(name = "KEY1")
	private String key1;

	@Column(name = "TYPE1")
	private String type1;

	@Column(name = "IS_MANDATORY")
	private boolean mandatory;

	@Column(name = "IS_HIDDEN")
	private boolean hidden;

	@Column(name = "IS_READONLY")
	private boolean readonly;

	@Column(name = "IS_DEPENDENT")
	private boolean dependent;

	@Column(name = "DEPENDENT_ON")
	private String dependent_on;

	@Column(name = "DEPENDENT_SP")
	private String dependent_sp;

	@Column(name = "DEPENDENT_SP_PARAM")
	private String dependent_sp_param;

	@Column(name = "VALIDATION_1")
	private boolean validation_1;

	@Column(name = "VAL_TYPE")
	private String val_type;

	@Column(name = "VAL_SP")
	private String val_sp;

	@Column(name = "VAL_SP_PARAM")
	private String val_sp_param;

	@Column(name = "SEQUENCE")
	private boolean sequence;

	@Column(name = "SEQ_NAME")
	private String seq_name;

	@Column(name = "SEQ_SP")
	private String seq_sp;

	@Column(name = "SEQ_SP_PARAM")
	private String seq_sp_param;

	@Column(name = "DEFAULT_1")
	private boolean default_1;

	@Column(name = "DEFAULT_TYPE")
	private String default_type;

	@Column(name = "DEFAULT_VALUE")
	private String default_value;

	@Column(name = "DEFAULT_SP")
	private String default_sp;

	@Column(name = "DEFAULT_SP_PARAM")
	private String default_sp_param;

	@Column(name = "CALCULATED_FIELD")
	private boolean calculated_field;

	@Column(name = "CAL_SP")
	private String cal_sp;

	@Column(name = "CAL_SP_PARAM")
	private String cal_sp_param;

	@Column(name = "ADD_TO_GRID")
	private boolean add_to_grid;

	@Column(name = "SP_FOR_AUTOCOMPLETE")
	private boolean sp_for_autocomplete;

	@Column(name = "SP_FOR_DROPDOWN")
	private boolean sp_for_dropdown;

	@Column(name = "SP_NAME_FOR_AUTOCOMPLETE")
	private String sp_name_for_autocomplete;
	
	@Column(name = "SP_NAME_FOR_DROPDOWN")
	private String sp_name_for_dropdown;

	@Column(name = "TYPE_FIELD")
	private String type_field;

	@Column(name = "METHOD_NAME")
	private String methodName;

	@Column(name = "SEQ")
	private int seq;

	@Column(name = "FORM_TYPE")
	private String form_type;

	@Column(name = "SECTION_NUM")
	private int section_num;

	@Column(name = "BUTTON_NUM")
	private int button_num;

	@Column(name = "TYPE2")
	private String type2;

	@Column(name = "LINE_TABLE_NAME")
	private String line_table_name;

	@Column(name = "LINE_TABLE_NO")
	private int line_table_no;
	
	@Column(name = "Action")
	private String action;
	
	@Column(name = "Request_param")
	private String request_param;
	
	@Column(name="Action_uiname")
	private String action_uiname;

	// =======================
//		@Column(name = "MODULE_ID")
//		private int module_id;

//		@Column(name = "PROJECT_ID")
//		private int project_id;
//		@Column(name = "HEADER_ID")
//		private int header_id;

	// ------ RELATIONSHIP ---------
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "HEADER_ID")
	@JsonBackReference
	private Rn_Fb_Header rn_fb_header;

	public Rn_Fb_Line() {
		super();
	}

	// get set
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

	public boolean getAdd_to_grid() {
		return add_to_grid;
	}

	public void setAdd_to_grid(boolean add_to_grid) {
		this.add_to_grid = add_to_grid;
	}

	public boolean getSp_for_autocomplete() {
		return sp_for_autocomplete;
	}

	public void setSp_for_autocomplete(boolean sp_for_autocomplete) {
		this.sp_for_autocomplete = sp_for_autocomplete;
	}

	public boolean getSp_for_dropdown() {
		return sp_for_dropdown;
	}

	public void setSp_for_dropdown(boolean sp_for_dropdown) {
		this.sp_for_dropdown = sp_for_dropdown;
	}

	public String getSp_name_for_dropdown() {
		return sp_name_for_dropdown;
	}

	public void setSp_name_for_dropdown(String sp_name_for_dropdown) {
		this.sp_name_for_dropdown = sp_name_for_dropdown;
	}

	public String getSp_name_for_autocomplete() {
		return sp_name_for_autocomplete;
	}

	public void setSp_name_for_autocomplete(String sp_name_for_autocomplete) {
		this.sp_name_for_autocomplete = sp_name_for_autocomplete;
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

	public Rn_Fb_Header getRn_fb_header() {
		return rn_fb_header;
	}

	public void setRn_fb_header(Rn_Fb_Header rn_fb_header) {
		this.rn_fb_header = rn_fb_header;
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

	public Rn_Fb_Line(Integer id, String fieldName, String mapping, String dataType, String formCode, String key1,
			String type1, boolean mandatory, boolean hidden, boolean readonly, boolean dependent, String dependent_on,
			String dependent_sp, String dependent_sp_param, boolean validation_1, String val_type, String val_sp,
			String val_sp_param, boolean sequence, String seq_name, String seq_sp, String seq_sp_param,
			boolean default_1, String default_type, String default_value, String default_sp, String default_sp_param,
			boolean calculated_field, String cal_sp, String cal_sp_param, boolean add_to_grid,
			boolean sp_for_autocomplete, boolean sp_for_dropdown, String sp_name_for_autocomplete,
			String sp_name_for_dropdown, String type_field, String methodName, int seq, String form_type,
			int section_num, int button_num, String type2, String line_table_name, int line_table_no, String action,
			String request_param, String action_uiname, Rn_Fb_Header rn_fb_header) {
		super();
		this.id = id;
		this.fieldName = fieldName;
		this.mapping = mapping;
		this.dataType = dataType;
		this.formCode = formCode;
		this.key1 = key1;
		this.type1 = type1;
		this.mandatory = mandatory;
		this.hidden = hidden;
		this.readonly = readonly;
		this.dependent = dependent;
		this.dependent_on = dependent_on;
		this.dependent_sp = dependent_sp;
		this.dependent_sp_param = dependent_sp_param;
		this.validation_1 = validation_1;
		this.val_type = val_type;
		this.val_sp = val_sp;
		this.val_sp_param = val_sp_param;
		this.sequence = sequence;
		this.seq_name = seq_name;
		this.seq_sp = seq_sp;
		this.seq_sp_param = seq_sp_param;
		this.default_1 = default_1;
		this.default_type = default_type;
		this.default_value = default_value;
		this.default_sp = default_sp;
		this.default_sp_param = default_sp_param;
		this.calculated_field = calculated_field;
		this.cal_sp = cal_sp;
		this.cal_sp_param = cal_sp_param;
		this.add_to_grid = add_to_grid;
		this.sp_for_autocomplete = sp_for_autocomplete;
		this.sp_for_dropdown = sp_for_dropdown;
		this.sp_name_for_autocomplete = sp_name_for_autocomplete;
		this.sp_name_for_dropdown = sp_name_for_dropdown;
		this.type_field = type_field;
		this.methodName = methodName;
		this.seq = seq;
		this.form_type = form_type;
		this.section_num = section_num;
		this.button_num = button_num;
		this.type2 = type2;
		this.line_table_name = line_table_name;
		this.line_table_no = line_table_no;
		this.action = action;
		this.request_param = request_param;
		this.action_uiname = action_uiname;
		this.rn_fb_header = rn_fb_header;
	}

}
