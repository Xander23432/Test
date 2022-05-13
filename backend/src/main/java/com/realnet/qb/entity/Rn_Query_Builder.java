package com.realnet.qb.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "RN_QUERY_BUILDER_SETUP_T")

public class Rn_Query_Builder {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private Integer id;

	@Column(name = "PROJECT_PROFILE")
	private String project_profile;

	@Column(name = "TABLE_NAME_PREFIX")
	private String table_name_prefix;

	@Column(name = "TABLE_NAME_SUFIX")
	private String table_name_sufix;

	@Column(name = "STRING1")
	private String string1;

	@Column(name = "STRING2")
	private String string2;

	@Column(name = "STRING3")
	private String string3;

	@Column(name = "STRING4")
	private String string4;

	@Column(name = "STRING5")
	private String string5;

	@Column(name = "STRING6")
	private String string6;

	@Column(name = "PROJECT_ID")
	private int project_id;

	@Column(name = "MODULE_ID")
	private int module_id;

	@Column(name = "STRING7")
	private String string7;

	public Rn_Query_Builder() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Rn_Query_Builder(Integer id, String project_profile, String table_name_prefix, String table_name_sufix,
			String string1, String string2, String string3, String string4, String string5, String string6,
			int project_id, int module_id, String string7) {
		super();
		this.id = id;
		this.project_profile = project_profile;
		this.table_name_prefix = table_name_prefix;
		this.table_name_sufix = table_name_sufix;
		this.string1 = string1;
		this.string2 = string2;
		this.string3 = string3;
		this.string4 = string4;
		this.string5 = string5;
		this.string6 = string6;
		this.project_id = project_id;
		this.module_id = module_id;
		this.string7 = string7;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getProject_profile() {
		return project_profile;
	}

	public void setProject_profile(String project_profile) {
		this.project_profile = project_profile;
	}

	public String getTable_name_prefix() {
		return table_name_prefix;
	}

	public void setTable_name_prefix(String table_name_prefix) {
		this.table_name_prefix = table_name_prefix;
	}

	public String getTable_name_sufix() {
		return table_name_sufix;
	}

	public void setTable_name_sufix(String table_name_sufix) {
		this.table_name_sufix = table_name_sufix;
	}

	public String getString1() {
		return string1;
	}

	public void setString1(String string1) {
		this.string1 = string1;
	}

	public String getString2() {
		return string2;
	}

	public void setString2(String string2) {
		this.string2 = string2;
	}

	public String getString3() {
		return string3;
	}

	public void setString3(String string3) {
		this.string3 = string3;
	}

	public String getString4() {
		return string4;
	}

	public void setString4(String string4) {
		this.string4 = string4;
	}

	public String getString5() {
		return string5;
	}

	public void setString5(String string5) {
		this.string5 = string5;
	}

	public String getString6() {
		return string6;
	}

	public void setString6(String string6) {
		this.string6 = string6;
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

	public String getString7() {
		return string7;
	}

	public void setString7(String string7) {
		this.string7 = string7;
	}
}