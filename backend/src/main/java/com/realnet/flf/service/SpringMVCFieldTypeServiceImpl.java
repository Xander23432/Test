package com.realnet.flf.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.realnet.fnd.entity.Rn_Lookup_Values;
import com.realnet.utils.RealNetUtils;
import com.realnet.utils.WireFrameConstant;
import com.realnet.wfb.entity.Rn_Fb_Line_DTO;

@Service
public class SpringMVCFieldTypeServiceImpl implements FieldTypeService {
	private static final Logger logger = LoggerFactory.getLogger(FieldTypeService.class);
	

	@Override
	public String dataTypeFieldsForModelClass(List<Rn_Fb_Line_DTO> lineDto) {
		StringBuilder model_class_fields = new StringBuilder();
		for (Rn_Fb_Line_DTO line : lineDto) {
			// String field_name = line.getField_name();
			String mapping = line.getMapping();
			String lowerCase = mapping.toLowerCase();
			String upperCase = mapping.toUpperCase();
			// String firstUpper = toFirstUpperCase(mapping);

			String data_type = line.getDataType();
			String key1 = line.getKey1();

			if (WireFrameConstant.DT_PK.equalsIgnoreCase(key1)) {
				logger.info("PK FIELD ADDED...");
				model_class_fields.append("@Id\r\n" 
								+ "@GeneratedValue(strategy = GenerationType.IDENTITY)\r\n" + "@Column(name = \""
								+ upperCase + "\")\r\n" + "private " + data_type + " " + lowerCase + ";\r\n");
			} else if (WireFrameConstant.DT_INTEGER.equalsIgnoreCase(data_type)) {
				logger.info("INTEGER FIELD ADDED...");
				model_class_fields.append(
						"\r\n@Column(name = \"" + upperCase + "\")\r\n" + "private int \t" + lowerCase + ";\r\n");
			} else if (WireFrameConstant.DT_DOUBLE.equalsIgnoreCase(data_type)) {
				logger.info("DOUBLE FIELD ADDED...");
				model_class_fields.append(
						"\r\n@Column(name = \"" + upperCase + "\")\r\n" + "private double\t" + lowerCase + ";\r\n");
			} else if (WireFrameConstant.DT_DATE_TIME.equalsIgnoreCase(data_type)) {
				logger.info("DATE FIELD ADDED...");
				model_class_fields.append(
						"\r\n@Column(name = \"" + upperCase + "\")\r\n" + "private Date\t" + lowerCase + ";\r\n");
			} else if (WireFrameConstant.DT_VARCHAR.equalsIgnoreCase(data_type)) {
				logger.info("VARCHAR FIELD ADDED...");
				model_class_fields.append(
						"\r\n@Column(name = \"" + upperCase + "\")\r\n" + "private String \t" + lowerCase + ";\r\n");
			} else if (WireFrameConstant.DT_LONGTEXT.equalsIgnoreCase(data_type)) {
				logger.info("LONGTEXT FIELD ADDED...");
				model_class_fields.append(
						"\r\n@Column(name = \"" + upperCase + "\")\r\n" + "private String\t" + lowerCase + ";\r\n");
			}
		}
		return model_class_fields.toString();
	}

	@Override
	public String attribute_flex_fieldsForModelClass(List<Rn_Lookup_Values> attribute_flex_values) {
		StringBuilder model_class_attribute_flex_fields = new StringBuilder();
		for (Rn_Lookup_Values attribute_flex : attribute_flex_values) {
			String lookupCode = attribute_flex.getLookupCode();
			String upperCase = lookupCode.toUpperCase();
			String lowerCase = lookupCode.toLowerCase();
			logger.info("Attribute Flex: {} ", upperCase);
			model_class_attribute_flex_fields.append("@Column(name = \"" + upperCase + "\")" + "\r\nprivate String \t" + lowerCase + ";\r\n");

		}
		return model_class_attribute_flex_fields.toString();
	}

	@Override
	public String fieldsGetterSetterForModelClass(List<Rn_Fb_Line_DTO> lineDto) {
		StringBuilder model_class_fields_getter_setter = new StringBuilder();
		for (Rn_Fb_Line_DTO line : lineDto) {
			String mapping = line.getMapping();
			String lowerCase = mapping.toLowerCase();
			String firstUpper = RealNetUtils.toFirstUpperCase(mapping);

			String data_type = line.getDataType();
			String key1 = line.getKey1();

			if (WireFrameConstant.DT_PK.equalsIgnoreCase(key1)) {
				logger.info("PK FIELD GETTER SETTER ADDED...");
				model_class_fields_getter_setter.append("\r\n" + "	public " + data_type + " get" + firstUpper
						+ "() {\r\n" + "		return\t" + lowerCase + ";\r\n" + "	}\r\n" + "	public void set"
						+ firstUpper + "(" + data_type + " " + lowerCase + ") {\r\n" + "		this." + lowerCase
						+ " = " + lowerCase + ";\r\n" + "	}\r\n");
			} else if (WireFrameConstant.DT_INTEGER.equalsIgnoreCase(data_type)) {
				logger.info("INTEGER FIELD GETTER SETTER ADDED...");
				model_class_fields_getter_setter.append("\r\n" + "	public int get" + firstUpper + "() {\r\n"
						+ "		return " + lowerCase + ";\r\n" + "	}\r\n" + "	public void set" + firstUpper + "(int "
						+ lowerCase + ") {\r\n" + "		this." + lowerCase + " = " + lowerCase + ";\r\n" + "	}\r\n");
			} else if (WireFrameConstant.DT_DOUBLE.equalsIgnoreCase(data_type)) {
				logger.info("DOUBLE FIELD GETTER SETTER ADDED...");
				model_class_fields_getter_setter
						.append("\r\n" + "	public double get" + firstUpper + "() {\r\n" + "		return " + lowerCase
								+ ";\r\n" + "	}\r\n" + "	public void set" + firstUpper + "(double " + lowerCase
								+ ") {\r\n" + "		this." + lowerCase + " = " + lowerCase + ";\r\n" + "	}\r\n");
			} else if (WireFrameConstant.DT_DATE_TIME.equalsIgnoreCase(data_type)) {
				logger.info("DATE FIELD GETTER SETTER ADDED...");
				model_class_fields_getter_setter.append("\r\n" + "	public Date get" + firstUpper + "() {\r\n"
						+ "		return " + lowerCase + ";\r\n" + "	}\r\n" + "	public void set" + firstUpper + "(Date "
						+ lowerCase + ") {\r\n" + "		this." + lowerCase + " = " + lowerCase + ";\r\n" + "	}\r\n");
			} else if (WireFrameConstant.DT_VARCHAR.equalsIgnoreCase(data_type)) {
				logger.info("VARCHAR FIELD GETTER SETTER ADDED...");
				model_class_fields_getter_setter
						.append("\r\n" + 
								"	public String get" + firstUpper + "() {\r\n" 
								+ "		return " + lowerCase + ";\r\n" 
								+ "	}\r\n" 
								+ "	public void set" + firstUpper + "(String " + lowerCase + ") {\r\n" 
								+ "		this." + lowerCase + " = " + lowerCase + ";\r\n" 
								+ "	}\r\n");
			} else if (WireFrameConstant.DT_LONGTEXT.equalsIgnoreCase(data_type)) {
				logger.info("LONGTEXT FIELD GETTER SETTER ADDED...");
				model_class_fields_getter_setter
						.append("\r\n" 
								+ "	public String get" + firstUpper + "() {\r\n" 
								+ "		return " + lowerCase + ";\r\n" + "	}\r\n" 
								+ "	public void set" + firstUpper + "(String " + lowerCase + ") {\r\n" 
								+ "		this." + lowerCase + " = " + lowerCase + ";\r\n" 
								+ "	}\r\n");
			}
		}
		return model_class_fields_getter_setter.toString();
	}

	@Override
	public String attribute_flex_fieldsGetterSetterForModelClass(List<Rn_Lookup_Values> attribute_flex_values) {
		StringBuilder model_class_attribute_flex_getter_setter = new StringBuilder();
		for (Rn_Lookup_Values attribute_flex : attribute_flex_values) {
			String lookupCode = attribute_flex.getLookupCode();
			String lowerCase = lookupCode.toLowerCase();
			String firstUpper = RealNetUtils.toFirstUpperCase(lookupCode);
			//logger.info("Attribute Flex: {} ", upperCase);

			model_class_attribute_flex_getter_setter.append("public String get" + firstUpper + "() {\r\n" 
					+ "return " + lowerCase + ";\r\n}\r\n" 
					+ "public void set" + firstUpper + "(String\t" + lowerCase + ") {\r\n"
					+ "\nthis." + lowerCase + "=" + lowerCase + ";\r\n"
					+ "}\r\n");
		}
		return model_class_attribute_flex_getter_setter.toString();
	}
	
	@Override
	public String whoColumnsForModelClass() {
		StringBuilder who_column_fields = new StringBuilder();
		who_column_fields.append("@Temporal(TemporalType.TIMESTAMP)\r\n" + 
				"	@Column(name = \"CREATED_AT\", nullable = false, updatable = false)\r\n" + 
				"	@CreatedDate\r\n" + 
				"	private Date createdAt;\r\n" + 
				"\r\n" + 
				"\r\n" + 
				"	@Temporal(TemporalType.TIMESTAMP)\r\n" + 
				"	@Column(name = \"UPDATED_AT\", nullable = false)\r\n" + 
				"	@LastModifiedDate\r\n" + 
				"	private Date updatedAt;\r\n" + 
				"\r\n" + 
				"	@Column(name = \"CREATED_BY\", updatable = false)\r\n" + 
				"	private Long createdBy;\r\n" + 
				"\r\n" + 
				"	@Column(name = \"UPDATED_BY\")\r\n" + 
				"	private Long updatedBy;\r\n" + 
				"\r\n" + 
				"	@Column(name = \"ACCOUNT_ID\")\r\n" + 
				"	private Long accountId;\r\n" +
				"\r\n" + 
				"	public Date getCreatedAt() {\r\n" + 
				"		return createdAt;\r\n" + 
				"	}\r\n" + 
				"\r\n" + 
				"	public void setCreatedAt(Date createdAt) {\r\n" + 
				"		this.createdAt = createdAt;\r\n" + 
				"	}\r\n" + 
				"\r\n" + 
				"	public Date getUpdatedAt() {\r\n" + 
				"		return updatedAt;\r\n" + 
				"	}\r\n" + 
				"\r\n" + 
				"	public void setUpdatedAt(Date updatedAt) {\r\n" + 
				"		this.updatedAt = updatedAt;\r\n" + 
				"	}\r\n" + 
				"\r\n" + 
				"	public Long getCreatedBy() {\r\n" + 
				"		return createdBy;\r\n" + 
				"	}\r\n" + 
				"\r\n" + 
				"	public void setCreatedBy(Long createdBy) {\r\n" + 
				"		this.createdBy = createdBy;\r\n" + 
				"	}\r\n" + 
				"\r\n" + 
				"	public Long getUpdatedBy() {\r\n" +
				"		return updatedBy;\r\n" +
				"	}\r\n" + 
				"\r\n" + 
				"	public void setUpdatedBy(Long updatedBy) {\r\n" +
				"		this.updatedBy = updatedBy;\r\n" +
				"	}\r\n" +
				"\r\n" + 
				"	public Long getAccountId() {\r\n" + 
				"		return accountId;\r\n" + 
				"	}\r\n" + 
				"\r\n" + 
				"	public void setAccountId(Long accountId) {\r\n" +
				"		this.accountId = accountId;\r\n" +
				"	}\r\n"
			);
		return who_column_fields.toString();
	}
	
	
	/*
	 * dao_impl code
	 * */ 
	@Override
	public String comma_separated_datatypes(List<Rn_Fb_Line_DTO> lineDto) {
		StringBuilder dao_impl_comma_separated_fields = new StringBuilder();
		for (Rn_Fb_Line_DTO line : lineDto) {
			String data_type = line.getDataType();
			String mapping = line.getMapping();
			String upperCase = mapping.toUpperCase();
			
			if(!WireFrameConstant.DT_NULL.equalsIgnoreCase(data_type)) {
				dao_impl_comma_separated_fields.append(upperCase + ", ");
			}
		}
		return dao_impl_comma_separated_fields.toString();
	}
	
	@Override
	public String comma_separated_attribute_flex(List<Rn_Lookup_Values> attribute_flex_values) {
		StringBuilder dao_Impl_comma_separated_attribute_flex = new StringBuilder();
		for (Rn_Lookup_Values attribute_flex : attribute_flex_values) {
			String lookupCode = attribute_flex.getLookupCode();
			String upperCase = lookupCode.toUpperCase();
			dao_Impl_comma_separated_attribute_flex.append(upperCase + ", ");
		}
		return dao_Impl_comma_separated_attribute_flex.toString();
	}
	
	@Override
	public String comma_separated_who_columns() {
		StringBuilder dao_impl_comma_separated_fields = new StringBuilder();
		dao_impl_comma_separated_fields.append("created_by, updated_by, created_at, updated_at, account_id");
		return dao_impl_comma_separated_fields.toString();
	}
	
	@Override
	public String fieldsSetterInDaoImpl(List<Rn_Fb_Line_DTO> lineDto) {
		StringBuilder dao_impl_class_fields_setter = new StringBuilder();
		for (Rn_Fb_Line_DTO line : lineDto) {
			String mapping = line.getMapping();
			String upperCase = mapping.toUpperCase();
			String firstUpper = RealNetUtils.toFirstUpperCase(mapping);
			String table_name = line.getTable_name();
			String data_type = line.getDataType();
			String key1 = line.getKey1();

			if (WireFrameConstant.DT_PK.equalsIgnoreCase(key1)) {
				logger.info("PK FIELD SETTER ADDED...");
				dao_impl_class_fields_setter.append(table_name + ".set" + firstUpper + "(rs.get"
						+ RealNetUtils.toFirstUpperCase(data_type) + "(\"" + upperCase + "\"));\r\n");
			} else if (WireFrameConstant.DT_INTEGER.equalsIgnoreCase(data_type)) {
				logger.info("INTEGER FIELD SETTER ADDED...");
				dao_impl_class_fields_setter
						.append(table_name + ".set" + firstUpper + "(rs.getInt(\"" + upperCase + "\"));\r\n");
			} else if (WireFrameConstant.DT_DOUBLE.equalsIgnoreCase(data_type)) {
				logger.info("DOUBLE FIELD SETTER ADDED...");
				dao_impl_class_fields_setter
						.append(table_name + ".set" + firstUpper + "(rs.getDouble(\"" + upperCase + "\"));\r\n");
			} else if (WireFrameConstant.DT_DATE_TIME.equalsIgnoreCase(data_type)) {
				logger.info("DATE FIELD SETTER ADDED...");
				dao_impl_class_fields_setter
						.append(table_name + ".set" + firstUpper + "(rs.getDate(\"" + upperCase + "\"));\r\n");
			} else if (WireFrameConstant.DT_VARCHAR.equalsIgnoreCase(data_type)) {
				logger.info("VARCHAR FIELD SETTER ADDED...");
				dao_impl_class_fields_setter
						.append(table_name + ".set" + firstUpper + "(rs.getString(\"" + upperCase + "\"));\r\n");
			} else if (WireFrameConstant.DT_LONGTEXT.equalsIgnoreCase(data_type)) {
				logger.info("LONGTEXT FIELD SETTER ADDED...");
				dao_impl_class_fields_setter
						.append(table_name + ".set" + firstUpper + "(rs.getString(\"" + upperCase + "\"));\r\n");
			}
		}
		return dao_impl_class_fields_setter.toString();
	}

	@Override
	public String attribute_flex_fieldsSetterInDaoImpl(List<Rn_Lookup_Values> attribute_flex_values,
			String table_name) {
		String table_name_lower = table_name.toLowerCase();
		StringBuilder dao_Impl_attribute_flex_setter = new StringBuilder();
		for (Rn_Lookup_Values attribute_flex : attribute_flex_values) {
			String lookupCode = attribute_flex.getLookupCode();
			String upperCase = lookupCode.toUpperCase();
			String firstUpper = RealNetUtils.toFirstUpperCase(lookupCode);
			dao_Impl_attribute_flex_setter
					.append(table_name_lower + ".set" + firstUpper + "(rs.getString(\"" + upperCase + "\"));\r\n");
		}
		return dao_Impl_attribute_flex_setter.toString();
	}
	
	@Override
	public String who_columns_setterInDaoImpl(String table_name) {
		table_name = table_name.toLowerCase();
		StringBuilder dao_impl_who_columns_setter = new StringBuilder();
		dao_impl_who_columns_setter.append(table_name + ".setAccount_id(rs.getInt(\"ACCOUNT_ID\"));"
				+ "\r\n" 
				+ table_name + ".setCreatedBy(rs.getLong(\"CREATED_BY\"));" 
				+ "\r\n" 
				+ table_name + ".setCreatedAt(rs.getDate(\"CREATED_AT\"));" 
				+ "\r\n" 
				+ table_name + ".setUpdatedBy(rs.getLong(\"UPDATED_BY\"));" 
				+ "\r\n" 
				+ table_name + ".setUpdatedAt(rs.getDate(\"UPDATED_AT\"));" 
				+ "\r\n");
		return dao_impl_who_columns_setter.toString();
	}
	


	// JSP CODE
	@Override
	public String fieldLabelForGridViewJsp(List<Rn_Fb_Line_DTO> lineDto) {
		StringBuilder jsp_lable_code = new StringBuilder();
		for (Rn_Fb_Line_DTO line : lineDto) {
			String data_type = line.getDataType();
			String field_name = line.getFieldName();
			if(!WireFrameConstant.DT_NULL.equalsIgnoreCase(data_type)) {
				jsp_lable_code.append("\r\n<th class=\"center\">" + field_name + "</th>\r\n");
			}
		}
		return jsp_lable_code.toString();
	}

	@Override
	public String fieldValueForGridViewJsp(List<Rn_Fb_Line_DTO> lineDto) {
		StringBuilder jsp_value_code = new StringBuilder();
		for (Rn_Fb_Line_DTO line : lineDto) {
			String mapping = line.getMapping();
			String lowerCase = mapping.toLowerCase();
			String data_type = line.getDataType();
			String key1 = line.getKey1();
			String table_name = line.getTable_name();
			if(WireFrameConstant.DT_PK.equalsIgnoreCase(key1)) {
				jsp_value_code.append("\r\n<td class=\"center\">\r\n" + 
						"	<a href=\"" + table_name + "_readonly?id=${" + table_name + "." + lowerCase + "}\">\r\n" + 
						"		<i class=\"fa fa-eye green\" aria-hidden=\"true\"></i>\r\n" + 
						"	</a>/\r\n" + 
						"	<a href=\"" + table_name + "_update?id=${" + table_name + "." + lowerCase + "}\">\r\n" + 
						"		<i class=\"fa fa-edit red\" aria-hidden=\"true\"></i>\r\n" + 
						"	</a>/\r\n" + 
						"	<a href=\"rolenewviewreports?user_id=${rn_userlist." + lowerCase + "}\">\r\n" + 
						"		<i class=\"fa fa-graduation-cap\" aria-hidden=\"true\"></i>\r\n" + 
						"	</a>\r\n" + 
						"</td>\r\n");
			} else {
				if(!WireFrameConstant.DT_NULL.equalsIgnoreCase(data_type)) {
					jsp_value_code.append("\r\n<td class=\"center\">${" +table_name + "." + lowerCase + "}</td>\r\n");
				}
			}
		}
		return jsp_value_code.toString();
	}

	@Override
	public String fieldsForReadOnlyJsp(List<Rn_Fb_Line_DTO> lineDto) {
		StringBuilder jsp_read_only_code = new StringBuilder();
		int loopCount = 0;
		for (Rn_Fb_Line_DTO line : lineDto) {
			String mapping = line.getMapping();
			String lowerCase = mapping.toLowerCase();
			
			String data_type = line.getDataType();
			String field_name = line.getFieldName();
			String key1 = line.getKey1();
			String table_name = line.getTable_name();
			
			if(WireFrameConstant.DT_PK.equalsIgnoreCase(key1)) {
				jsp_read_only_code.append("\r\n<input type=\"hidden\" name=\"" + lowerCase + "\" id=\"" + lowerCase + "\" value=\"${" + table_name + "_update." + lowerCase + "}\" />\r\n");
				jsp_read_only_code.append("<tr>\n");
			} else {
				if(!WireFrameConstant.DT_NULL.equalsIgnoreCase(data_type)) {
					int i = ++loopCount;
					logger.info("i value in READ-ONLY JSP = " + i);
					jsp_read_only_code.append("\r\n<td>\r\n" + 
							"	<div class=\"form-group\" style=\"margin-left: 10%; margin-right: 10%;\">\r\n" + 
							"		<label class=\"control-label col-xs-12 col-sm-3 no-padding-right\">\r\n" + 
							"			" + field_name + "\r\n" + 
							"		</label>\r\n" + 
							"		<div class=\"col-xs-12 col-sm-9\">\r\n" + 
							"			<div class=\"clearfix\">\r\n" + 
							"				<input value=\"${" + table_name + "_update." + lowerCase + "}\"\r\n" + 
							"					type=\"text\" name=\"" + lowerCase + "\" id=\"" + lowerCase + "\" class=\"form-control\" readonly />\r\n" + 
							"			</div>\r\n" + 
							"		</div>\r\n" + 
							"	</div>\r\n" + 
							"</td>\r\n");
					if(i % 3 == 0) {
						jsp_read_only_code.append("</tr>\n<tr>\n");
					}	
				}
			}
		}
		String code = jsp_read_only_code.toString();
		if(code.endsWith("<tr>\n")) {
			code = code.substring(0, code.lastIndexOf("<tr>"));
		}
		if(!code.endsWith("</tr>\n")) {
			code = code.concat("</tr>");
		}
		return code;
	}

	@Override
	public String fieldsForUpdateJsp(List<Rn_Fb_Line_DTO> lineDto) {
		StringBuilder jsp_update_code = new StringBuilder();
		int loopCount = 0;
		for (Rn_Fb_Line_DTO line : lineDto) {
			String mapping = line.getMapping();
			String lowerCase = mapping.toLowerCase();
			String data_type = line.getDataType();
			String field_name = line.getFieldName();
			String key1 = line.getKey1();
			String table_name = line.getTable_name();
			
			if(WireFrameConstant.DT_PK.equalsIgnoreCase(key1)) {
				jsp_update_code.append("\n<input type=\"hidden\" name=\"" + lowerCase + "\" id=\"" + lowerCase + "\" value=\"${" + table_name + "_update." + lowerCase + "}\" />\r\n");
				jsp_update_code.append("<tr>\n");
			} else {
				if(!WireFrameConstant.DT_NULL.equalsIgnoreCase(data_type)) {
					int i = ++loopCount;
					logger.info("i value in UPDATE JSP = " + i);
					if(!WireFrameConstant.DT_DATE_TIME.equalsIgnoreCase(data_type) && !WireFrameConstant.DT_DOUBLE.equalsIgnoreCase(data_type) && !WireFrameConstant.DT_INTEGER.equalsIgnoreCase(data_type)) {
						jsp_update_code.append("\n<td>\r\n" 
								+ "	<div class=\"form-group\" style=\"margin-left: 10%; margin-right: 10%;\">\r\n"
								+ "		<label class=\"control-label col-xs-12 col-sm-3 no-padding-right\">\r\n"
								+ "			" + field_name + "</label>\r\n"
								+ "		<div class=\"col-xs-12 col-sm-9\">\r\n"
								+ "			<div class=\"clearfix\">\r\n"
								+ "				<input value=\"${" + table_name + "_update." + lowerCase + "}\"\r\n"
								+ "					type=\"text\" name=\"" + lowerCase + "\" id=\"" + lowerCase + "\" class=\"form-control\" />\r\n"
								+ "			</div>\r\n"
								+ "		</div>\r\n" 
								+ "	</div>\r\n" 
								+ "</td>\r\n");
					}
					if(WireFrameConstant.DT_DATE_TIME.equalsIgnoreCase(data_type)) {
						jsp_update_code.append("<td>\r\n" 
								+ "	<div class=\"form-group\" style=\"margin-left: 10%; margin-right: 10%;\">\r\n"
								+ "		<label class=\"control-label col-xs-12 col-sm-3 no-padding-right\">\r\n"
								+ "			" + field_name + "</label>\r\n"
								+ "		<div class=\"col-xs-12 col-sm-9\">\r\n"
								+ "			<div class=\"clearfix\">\r\n"
								+ "				<input value=\"${" + table_name + "_update." + lowerCase + "}\"\r\n"
								+ "					type=\"date\" name=\"" + lowerCase + "\" id=\"" + lowerCase + "\" class=\"form-control\" />\r\n"
								+ "			</div>\r\n"
								+ "		</div>\r\n" 
								+ "	</div>\r\n" 
								+ "</td>\r\n");
					}
					if(WireFrameConstant.DT_INTEGER.equalsIgnoreCase(data_type) && WireFrameConstant.DT_DOUBLE.equalsIgnoreCase(data_type)) {
						jsp_update_code.append("<td>\r\n" 
								+ "	<div class=\"form-group\" style=\"margin-left: 10%; margin-right: 10%;\">\r\n"
								+ "		<label class=\"control-label col-xs-12 col-sm-3 no-padding-right\">\r\n"
								+ "			" + field_name + "</label>\r\n"
								+ "		<div class=\"col-xs-12 col-sm-9\">\r\n"
								+ "			<div class=\"clearfix\">\r\n"
								+ "				<input value=\"${" + table_name + "_update." + lowerCase + "}\"\r\n"
								+ "					type=\"number\" name=\"" + lowerCase + "\" id=\"" + lowerCase + "\" class=\"form-control\" />\r\n"
								+ "			</div>\r\n"
								+ "		</div>\r\n" 
								+ "	</div>\r\n" 
								+ "</td>\r\n");
					}
					if(i % 3 == 0) {
						jsp_update_code.append("</tr>\n<tr>\n");
					}
				}
			}			
		}
		String code = jsp_update_code.toString();
		if(code.endsWith("<tr>\n")) {
			code = code.substring(0, code.lastIndexOf("<tr>"));
		}
		if(!code.endsWith("</tr>\n")) {
			code = code.concat("</tr>");
		}
		return code;
	}

	@Override
	public String fieldsForEntryFormJsp(List<Rn_Fb_Line_DTO> lineDto) {
		StringBuilder jsp_entry_form_code = new StringBuilder();
		int loopCount = 0;
		jsp_entry_form_code.append("\n<tr>\n");
		for (Rn_Fb_Line_DTO line : lineDto) {
			String key1 = line.getKey1();
			String mapping = line.getMapping();
			String lowerCase = mapping.toLowerCase();
			String data_type = line.getDataType();
			String field_name = line.getFieldName();

//			if(!WireFrameConstant.DT_NULL.equalsIgnoreCase(data_type)) {
			if(!WireFrameConstant.DT_NULL.equalsIgnoreCase(data_type) && !WireFrameConstant.DT_PK.equalsIgnoreCase(key1)) {
				int i = ++loopCount;
				logger.info("i value in ENTRY-FORM JSP = " + i);
				if(!WireFrameConstant.DT_DATE_TIME.equalsIgnoreCase(data_type) && !WireFrameConstant.DT_DOUBLE.equalsIgnoreCase(data_type) && !WireFrameConstant.DT_INTEGER.equalsIgnoreCase(data_type)) {
					jsp_entry_form_code.append("\n<td>\r\n" 
							+ "	<div class=\"form-group\" style=\"margin-left: 10%; margin-right: 10%;\">\r\n"
							+ "		<label class=\"control-label col-xs-12 col-sm-3 no-padding-right\">\r\n"
							+ "			" + field_name + "</label>\r\n"
							+ "		<div class=\"col-xs-12 col-sm-9\">\r\n"
							+ "			<div class=\"clearfix\">\r\n"
							+ "				<input type=\"text\" name=\"" + lowerCase + "\" id=\"" + lowerCase + "\" class=\"form-control\" />\r\n"
							+ "			</div>\r\n"
							+ "		</div>\r\n" 
							+ "	</div>\r\n" 
							+ "</td>\r\n");
				}
				if(WireFrameConstant.DT_DATE_TIME.equalsIgnoreCase(data_type)) {
					jsp_entry_form_code.append("<td>\r\n" 
							+ "	<div class=\"form-group\" style=\"margin-left: 10%; margin-right: 10%;\">\r\n"
							+ "		<label class=\"control-label col-xs-12 col-sm-3 no-padding-right\">\r\n"
							+ "			" + field_name + "</label>\r\n"
							+ "		<div class=\"col-xs-12 col-sm-9\">\r\n"
							+ "			<div class=\"clearfix\">\r\n"
							+ "				<input type=\"date\" name=\"" + lowerCase + "\" id=\"" + lowerCase + "\" class=\"form-control\" />\r\n"
							+ "			</div>\r\n"
							+ "		</div>\r\n" 
							+ "	</div>\r\n" 
							+ "</td>\r\n");
				}
				if(WireFrameConstant.DT_INTEGER.equalsIgnoreCase(data_type) && WireFrameConstant.DT_DOUBLE.equalsIgnoreCase(data_type)) {
					jsp_entry_form_code.append("<td>\r\n" 
							+ "	<div class=\"form-group\" style=\"margin-left: 10%; margin-right: 10%;\">\r\n"
							+ "		<label class=\"control-label col-xs-12 col-sm-3 no-padding-right\">\r\n"
							+ "			" + field_name + "</label>\r\n"
							+ "		<div class=\"col-xs-12 col-sm-9\">\r\n"
							+ "			<div class=\"clearfix\">\r\n"
							+ "				<input type=\"number\" name=\"" + lowerCase + "\" id=\"" + lowerCase + "\" class=\"form-control\" />\r\n"
							+ "			</div>\r\n"
							+ "		</div>\r\n" 
							+ "	</div>\r\n" 
							+ "</td>\r\n");
				}
				
				if(i % 3 == 0) {
					jsp_entry_form_code.append("</tr>\n<tr>\n");
				}
			}
		}
		
		String code = jsp_entry_form_code.toString();
		if(code.endsWith("<tr>\n")) {
			code = code.substring(0, code.lastIndexOf("<tr>"));
		}
		if(!code.endsWith("</tr>\n")) {
			code = code.concat("</tr>");
		}
		return code;
	}

	//=======@@@@@@@@@ NEED MODIFICATION ===========
	// TILES CODE
	@Override
	public String tilesCode() {
		StringBuilder tilesCode = new StringBuilder();
		tilesCode.append(
					"			//String tilespath = request.getServletContext().getRealPath(\"/WEB-INF\");\r\n" + 
					"			//File tilespath1 = new File(tilespath + \"/tiles.xml\");\r\n" +
					"			//String tilespath = projectPath + \"/Projects/\" +  project_name + \"/src/main/webapp/WEB-INF/tiles.xml\";\r\n" +
					"			//File tilespath1 = new File(tilespath);\r\n" +		
					"			//System.out.println(\"TILES PATH = \" + tilespath1);\r\n" + 
					"\r\n" + 
					"			// NEED TO MODIFY.... (NILADRI'S CODE)\r\n" + 
					"			File temp = new File(projectPath + \"/Projects/\" + project_name + \"/src/main/webapp/WEB-INF/tiles.xml\");\r\n" + 
					"			File newtemp = new File(projectPath + \"/Projects/\" + project_name + \"/src/main/webapp/WEB-INF/xyz.xml\");\r\n" + 
					"			//File temp = new File(projectPath + \"/src/main/webapp/WEB-INF/tiles.xml\");\r\n" + 
					"			//File newtemp = new File(projectPath  + \"/src/main/webapp/WEB-INF/xyz.xml\");\r\n" + 
					"\r\n" + 
					"			//File newTilespath = new File(tilespath + \"/xyz.xml\");\r\n" + 
					"\r\n" + 
					"\nif (!temp.exists()) {"
						
						+"\ntemp.createNewFile();"
					+"\n}"+
					"			// (NILADRI'S CODE)\r\n" +
					"			BufferedReader br = new BufferedReader(new FileReader(temp));\r\n" + 
					"			BufferedWriter bw1 = new BufferedWriter(new FileWriter(newtemp));\r\n" + 
					"\r\n" + 
					"			// (GANESH'S CODE)\r\n" + 
					"			//BufferedReader br2 = new BufferedReader(new FileReader(tilespath1));\r\n" + 
					"			//BufferedWriter bw2 = new BufferedWriter(new FileWriter(newTilespath));\r\n" + 
					"			String removeStr = \"</tiles-definitions>\";\r\n" + 
					"			String currentLine;\r\n" + 
					"			//String currentLine2;\r\n" + 
					"\r\n" + 
					"			// (NILADRI'S CODE)\r\n" + 
					"			System.out.println(temp.getName());\r\n" + 
					"			while ((currentLine = br.readLine()) != null) {\r\n" + 
					"				String trimmedLine = currentLine.trim();\r\n" + 
					"				if (trimmedLine.equals(removeStr)) {\r\n" + 
					"					currentLine = \"\";\r\n" + 
					"				}\r\n" + 
					"				bw1.write(currentLine + System.getProperty(\"line.separator\"));\r\n" + 
					"			}\r\n" + 
					"			bw1.close();\r\n" + 
					"			br.close();\r\n" + 
					"			boolean delete = temp.delete();\r\n" + 
					"			boolean b = newtemp.renameTo(temp);\r\n" + 
					"\r\n" + 
					"			// (GANESH'S CODE)\r\n" + 
					"			// for production\r\n" + 
					"			//while ((currentLine2 = br2.readLine()) != null) {\r\n" + 
					"				//String trimmedLine = currentLine2.trim();\r\n" + 
					"				//if (trimmedLine.equals(removeStr)) {\r\n" + 
					"					//currentLine2 = \"\";\r\n" + 
					"				//}\r\n" + 
					"				//bw2.write(currentLine2 + System.getProperty(\"line.separator\"));\r\n" + 
					"\r\n" + 
					"			//}\r\n" + 
					"			//bw2.close();\r\n" + 
					"			//br2.close();\r\n" + 
					"			//boolean delete2 = tilespath1.delete();\r\n" + 
					"			//boolean b2 = newTilespath.renameTo(tilespath1);\r\n" + 
					"\r\n" + 
					"			StringBuilder tiles = new StringBuilder();\r\n" + 
					"\r\n" + 
					
					"				tiles.append(\r\n" + 
					"						\"\\n<definition name=\\\"\" + table_name_first_upper + \"_grid\\\" extends=\\\"acemaster.definition\\\">\"\r\n" + 
					"								+ \"\\n<put-attribute name=\\\"title\\\" value=\\\"WASIB\\\"/>\"\r\n" + 
					"								+ \"\\n<put-attribute name=\\\"body\\\" value=\\\"/WEB-INF/tiles/acemaster/\" + module_name + \"/\"\r\n" + 
					"								+ table_name_first_upper + \"_grid.jsp\\\"/>\" + \"\\n</definition>\");\r\n" + 
					"\r\n" + 
					"				tiles.append(\r\n" + 
					"						\"\\n<definition name=\\\"\" + table_name_first_upper + \"_view\\\" extends=\\\"acemaster.definition\\\">\"\r\n" + 
					"								+ \"\\n<put-attribute name=\\\"title\\\" value=\\\"WASIB\\\"/>\"\r\n" + 
					"								+ \"\\n<put-attribute name=\\\"body\\\" value=\\\"/WEB-INF/tiles/acemaster/\" + module_name + \"/\"\r\n" + 
					"								+ table_name_first_upper + \"_view.jsp\\\"/>\" + \"\\n</definition>\");\r\n" + 
					"\r\n" + 
					"				tiles.append(\r\n" + 
					"						\"\\n<definition name=\\\"\" + table_name_first_upper + \"_update\\\" extends=\\\"acemaster.definition\\\">\"\r\n" + 
					"								+ \"\\n<put-attribute name=\\\"title\\\" value=\\\"WASIB\\\"/>\"\r\n" + 
					"								+ \"\\n<put-attribute name=\\\"body\\\" value=\\\"/WEB-INF/tiles/acemaster/\" + module_name + \"/\"\r\n" + 
					"								+ table_name_first_upper + \"_update.jsp\\\"/>\" + \"\\n</definition>\");\r\n" + 
					"\r\n" + 
					"				tiles.append(\"\\n<definition name=\\\"\" + table_name_first_upper\r\n" + 
					"						+ \"_readonly\\\" extends=\\\"acemaster.definition\\\">\"\r\n" + 
					"						+ \"\\n<put-attribute name=\\\"title\\\" value=\\\"WASIB\\\"/>\"\r\n" + 
					"						+ \"\\n<put-attribute name=\\\"body\\\" value=\\\"/WEB-INF/tiles/acemaster/\" + module_name + \"/\"\r\n" + 
					"						+ table_name_first_upper + \"_readonly.jsp\\\"/>\" + \"\\n</definition>\"\r\n" + 
					"\r\n" + 
					"						+ \"\\n</tiles-definitions>\");\r\n" + 
					"\r\n" + 
					"				// (NILADRI'S CODE)\r\n" + 
					"				String filename = projectPath + \"/Projects/\" + project_name + \"/src/main/webapp/WEB-INF/tiles.xml\";\r\n" + 
					"\r\n" + 
					"				// (GANESH'S CODE)\r\n" + 
					"				//String filename2 = tilespath + \"/tiles.xml\";\r\n" + 
					"\r\n" + 
					"				// (NILADRI'S CODE)\r\n" + 
					"				fw = new FileWriter(filename, true);\r\n" + 
					"				fw.write(tiles.toString());\r\n" + 
					"				fw.close();\r\n" + 
					"\r\n" + 
					"				// (GANESH'S CODE)\r\n" + 
					"				//FileWriter fw2 = new FileWriter(filename2, true);\r\n" + 
					"				//fw2.write(tiles.toString());\r\n" + 
					"				//fw2.close();\r\n");
		return tilesCode.toString();
	}

	// PROPERTIES FILE CODE
	@Override
	public String propertiesFileCode() {
		StringBuilder propertiesFileCode = new StringBuilder();
		propertiesFileCode.append("	StringBuilder properties_file_code = new StringBuilder();\r\n" +
				// FIELDS CODE SHOULD COME FROM LOOP
				"	StringBuilder properties_file_fields_code = new StringBuilder();\r\n" +
				"	properties_file_fields_code.append(\"created_by, updated_by, created_at, updated_at, account_id\");\r\n" + 
				"	String fields_code = properties_file_fields_code.toString();\r\n" + 
				"\r\n" + 
				"\r\n" + 
				"		properties_file_code.append(\"table_name = \" + table_name_first_upper + \"\\n\" \r\n" + 
				"				//+ \"line_table_name = \" + line_table_name + \"\\n\"\r\n" + 
				"				+ \"controller_name = \" + controller_name_first_upper + \"\\n\" \r\n" + 
				"				+ \"dao_name = \" + dao_name_first_upper + \"\\n\"\r\n" + 
				"				+ \"dao_impl_name = \" + dao_impl_name_first_upper + \"\\n\" \r\n" + 
				"				+ \"service_name = \" + service_name_first_upper + \"\\n\" \r\n" + 
				"				+ \"service_impl_name = \" + service_impl_name_first_upper + \"\\n\" \r\n" + 
				"				+ \"jsp_name = \" + ui_name + \"\\n\" \r\n" + 
				"				+ \"form_code = \" + form_code + \"\\n\"\r\n" + 
				"				+ fields_code + \"\\n\");\r\n" + 
				"		\r\n" +
				"				File propertiesFile = new File(\r\n" + 
				"					projectPath + \"/Projects/\" + project_name + \"/src/main/resources/\" + form_code + \".properties\");\r\n" + 
				"				System.out.println(\"in /Project Properties file path = \" + propertiesFile);\r\n" + 
				"				if (!propertiesFile.exists()) {\r\n" + 
				"					propertiesFile.getParentFile().mkdirs();\r\n" + 
				"					propertiesFile.createNewFile();\r\n" + 
				"				}\r\n" + 
				"				fw = new FileWriter(propertiesFile.getAbsoluteFile());\r\n" + 
				"				bw = new BufferedWriter(fw);\r\n" + 
				"				bw.write(properties_file_code.toString());\r\n" + 
				"				bw.close();\r\n" + 
				"				fw.close();\r\n" + 
				"				\r\n"
//				"				// in main project folder (FOR TESTING) \r\n" + 
//				"				File propertiesFile2 = new File(\r\n" + 
//				"					projectPath + \"/src/main/resources/\" + form_code + \".properties\");\r\n" + 
//				"				System.out.println(\"in /main Project Properties file path = \" + propertiesFile);\r\n" + 
//				"				if (!propertiesFile2.exists()) {\r\n" + 
//				"					propertiesFile2.createNewFile();\r\n" + 
//				"				}\r\n" + 
//				"				fw = new FileWriter(propertiesFile.getAbsoluteFile());\r\n" + 
//				"				bw = new BufferedWriter(fw);\r\n" + 
//				"				bw.write(properties_file_code.toString());\r\n" + 
//				"				bw.close();\r\n" + 
//				"				fw.close();\r\n"
				);
		return propertiesFileCode.toString();
	}

	// @@@@@@@@=========== need mod
	@Override
	public String action_builder_code() {
		StringBuilder cff_action_builder_code = new StringBuilder();
		cff_action_builder_code.append(
				"		// EXTRA BUTTON LIST (IF USER ADD EXTRA BUTTON THEN THIS WILL CALL)\r\n" 
				+ "		List<Rn_Fb_Line> extraButtonList = wireFrameService.getExtraButton(id);\r\n"
				+ " 	// CFF ACTION BUILDER\r\n" 
				+ "		StringBuilder cff_add_button_update_controller = new StringBuilder();\r\n"
				+ " 	StringBuilder cff_add_button_code_in_update_jsp = new StringBuilder();\r\n" +
				"		int buttonLoopCount = 0;\r\n" + 
				"		for (Rn_Fb_Line btn : extraButtonList) {\r\n" + 
				"			System.out.println(\"BUTON LOOP COUNT = \" + ++buttonLoopCount);\r\n" + 
				"			String field_name = btn.getFieldName();\r\n" + 
				"			String mapping = btn.getMapping();\r\n" + 
				"			String method_name = btn.getMethodName();\r\n" + 
				"\r\n" + 
				"			System.out.println(\"BUTTON-LOOP field name = \" + field_name + \" || mapping = \" + mapping\r\n" + 
				"					+ \" || method_name = \" + method_name);\r\n" + 
				"			\r\n" + 
				// update controller code
				"		// CFF ACTION BUILDER CODE IN CONTROLLER..\r\n" + 
				"		cff_add_button_update_controller.append(\"\\nmap.addAttribute(\\\"\" + table_name_lower + \"_update\\\",\" + table_name_lower + \");\"\r\n" + 
				"				+ \"\\nList<\" + table_name_first_upper + \"> report =\" + service_name_lower + \".prefield(u_id);\\n\"\r\n" + 
				"				+ \"int updt_id = report.get(0).getId();\\n\" \r\n" + 
				"				// \r\n" + 
				"				+ \"map.addAttribute(\\\"\" + \"cff_id\\\", update_id);\\n\" \r\n" + 
				"				+ \"\\nmap.addAttribute(\\\"\" + table_name_lower + \"_update\\\", report);\");\r\n" +
				
				// jsp code
				"			// JSP CODE FOR EXTRA BUTTON\r\n" + 
				"			cff_add_button_code_in_update_jsp.append(\"\\r\\n<div>\\r\\n\"\r\n" + 
				"					+ \"			<a href=\\\"\"\r\n" + 
				"					+ method_name + \"?id=${cff_id}\\\">\\r\\n\"\r\n" + 
				"					+ \"					<button type=\\\"button\\\" id=\\\"\" + mapping + \"\\\" name = \\\"\" + mapping\r\n" + 
				"					+ \"\\\" class=\\\"btn btn-success btn-sm btn-next\\\">\" + field_name + \"</button>\\r\\n\"\r\n" + 
				"					+ \"			</a>\\r\\n\" \r\n" + 
				"					+ \"		</div>\\r\\n\");\r\n" + 
				
				// controller file code for extra button
				"			StringBuilder cff_add_button_controller_code = new StringBuilder();\r\n" + 
				"			StringBuilder cff_add_button_controller_imports = new StringBuilder();\r\n" + 
				"			cff_add_button_controller_imports.append(\"package com.realnet.\" + module_name + \".controller;\\r\\n\" \r\n" + 
				"					+ \"\\r\\n\"\r\n" + 
				"					+ \"import java.io.IOException;\\r\\n\" \r\n" + 
				"					+ \"import java.text.ParseException;\\r\\n\"\r\n" + 
				"					+ \"import java.time.LocalDateTime;\\r\\n\"\r\n" + 
				"					+ \"import java.time.format.DateTimeFormatter;\\r\\n\"\r\n" + 
				"					+ \"import java.util.List;\\r\\n\" \r\n" + 
				"					+ \"\\r\\n\" \r\n" + 
				"					+ \"import javax.servlet.http.HttpServletRequest;\\r\\n\"\r\n" + 
				"					+ \"import javax.validation.Valid;\\r\\n\" + \"\\r\\n\"\r\n" + 
				"					+ \"import org.springframework.beans.factory.annotation.Autowired;\\r\\n\"\r\n" + 
				"					+ \"import org.springframework.stereotype.Controller;\\r\\n\"\r\n" + 
				"					+ \"import org.springframework.ui.ModelMap;\\r\\n\"\r\n" + 
				"					+ \"import org.springframework.validation.BindingResult;\\r\\n\"\r\n" + 
				"					+ \"import org.springframework.web.bind.annotation.GetMapping;\\r\\n\"\r\n" + 
				"					+ \"import org.springframework.web.bind.annotation.ModelAttribute;\\r\\n\"\r\n" + 
				"					+ \"import org.springframework.web.bind.annotation.PostMapping;\\r\\n\"\r\n" + 
				"					+ \"import org.springframework.web.bind.annotation.RequestMapping;\\r\\n\"\r\n" + 
				"					+ \"import org.springframework.web.bind.annotation.RequestMethod;\\r\\n\"\r\n" + 
				"					+ \"import org.springframework.web.bind.annotation.RequestParam;\\r\\n\"\r\n" + 
				"					+ \"import org.springframework.web.servlet.ModelAndView;\\r\\n\" + \"\\r\\n\"\r\n" + 
				"					+ \"import com.realnet.\" + module_name + \".model.\" + table_name_first_upper + \";\\n\"\r\n" + 
				"					+ \"import com.realnet.\" + module_name + \".dao.\" + dao_name_first_upper + \";\\n\"\r\n" + 
				"					+ \"import com.realnet.\" + module_name + \".service.\" + service_name_first_upper + \";\\n\" + \"\");\r\n" + 
				"\r\n" + 
				"			// CONTROLLER NAME FOR EACH BUTTON TYPE\r\n" + 
				"			String controllerName = method_name.substring(0, 1).toUpperCase()\r\n" + 
				"					+ method_name.substring(1).concat(\"Controller\");\r\n" + 
				"			System.out.println(\"Niladri controllerName = \" + controllerName);\r\n" + 
				"			// CONTROLLER PREFIX\r\n" + 
				"			cff_add_button_controller_code.append(\r\n" + 
				"					cff_add_button_controller_imports + \"@Controller\\r\\n\" + \"public class \" + controllerName + \" {\\r\\n\" \r\n" + 
				"							+ \"	@Autowired\" + \"	private \" + dao_name_first_upper + \"\\t\" + dao_name_lower + \";\\n\");\r\n" + 
				"\r\n" + 
				"				cff_add_button_controller_code.append(\"	// INSERT FIELDS USING ACTION BUILDER\\r\\n\"\r\n" + 
				"						+ \"@GetMapping(value = \\\"/\" + method_name + \"\\\")\\r\\n\" + \"	public ModelAndView \" + method_name\r\n" + 
				"						+ \"(@RequestParam(value = \\\"id\\\") String h_id) throws IOException {\\r\\n\"\r\n" + 
				"						+ \"		int hId = Integer.parseInt(h_id);\\r\\n\"\r\n" + 
				"						+ \"		//System.out.println(\\\"JSP ID = \\\" + hId);\\r\\n\" \r\n" + 
				"						+ \"	// CFF_LAYOUT_CONTROLLER_START\\r\\n\"\r\n" + 
				"						+ \"		System.out.println(\\\"PLEASE INSERT CODE... GO TO ACTION BUILDER... \\\");\\r\\n\" \r\n" + 
				"						+ \"	// CFF_LAYOUT_CONTROLLER_END\\r\\n\" \r\n" + 
				"						+ \"		return new ModelAndView(\\\"redirect:\" + table_name_lower + \"_update?id=\\\" + hId);\\r\\n\" \r\n" + 
				"						+ \"	}\\n\" \r\n" + 
				"						+ \"}\");\r\n" + 
				"\r\n" + 
				"			// FILE PATH CHANGE NEEDED...\r\n" + 
				"			File file = new File(projectPath + \"/Projects/\" + project_name + \"/src/main/java/com/realnet/\" + module_name + \"/controller/\" + controllerName + \".java\");\r\n" + 
				"			System.out.println(\"Niladri file path = \" + file.getAbsolutePath());\r\n" + 
				"			if (!file.exists()) {\r\n" + 
				"				file.getParentFile().mkdirs();\r\n" + 
				"				file.createNewFile();\r\n" + 
				"			}\r\n" + 
				"\r\n" + 
				"			fw = new FileWriter(file.getAbsoluteFile());\r\n" + 
				"			bw = new BufferedWriter(fw);\r\n" + 
				"			bw.write(cff_add_button_controller_code.toString());\r\n" + 
				"			bw.close();\r\n" + 
				"\r\n" + 
				"			/*\r\n" + 
				"			 * CONTROLLER NAME AND FILE-LOCATION SHOULD SAVE IN CFF ACTION BUILDER HEADER\r\n" + 
				"			 * TABLE\r\n" + 
				"			 */\r\n" + 
				"			String file_location = file.getAbsolutePath().replace(\"\\\\\", \"/\");\r\n" + 
				"			// SHOULD BE LIKE = /Projects/only_header_testing/Demo.java\r\n" + 
				"			file_location = file_location.substring(file_location.lastIndexOf(\"/Projects/\"));\r\n" + 
				"			/*\r\n" + 
				"			 * ====== MODIFICATION ==== path =\r\n" + 
				"			 * path.substring(path.lastIndexOf(\"/Projects/\")+1); path =\r\n" + 
				"			 * path.substring(path.indexOf(\"/\")); \r\n" + 
				"			 * OP = /only_header_testing/Demo.java\r\n" + 
				"			 */\r\n" + 
				"			System.out.println(\"FILE LOCATION SUBSTRING = \" + file_location);\r\n" + 
				"			controllerName = controllerName.concat(\".java\");\r\n" + 
				"			Rn_cff_ActionBuilder_Header header = new Rn_cff_ActionBuilder_Header();\r\n" + 
				"				header.setRn_fb_header(rn_fb_header);// SAVE rn_fb_header_id\r\n" + 
				"				header.setTechnologyStack(technology_stack);\r\n" + 
				"				header.setControllerName(controllerName); // NO NEED\r\n" + 
				"				header.setMethodName(method_name);\r\n" + 
				"				header.setFileLocation(file_location);\r\n" + 
				"				header.setActionName(method_name); // action name and method name is same\r\n" + 
				"				actionBuilderService.save(header);\r\n" + 
				"		}\r\n" + 
				"		// EXTRA BUTTON LOOP END\r\n");
		
		
		
		return cff_action_builder_code.toString();
	}
	



}
