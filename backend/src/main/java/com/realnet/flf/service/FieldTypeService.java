package com.realnet.flf.service;

import java.util.List;

import com.realnet.fnd.entity.Rn_Lookup_Values;
import com.realnet.wfb.entity.Rn_Fb_Line_DTO;

public interface FieldTypeService {
	// MODEL CLASS ENTITY FIELDS (COLUMNS) 
	String dataTypeFieldsForModelClass(List<Rn_Fb_Line_DTO> lineDto);
	String attribute_flex_fieldsForModelClass(List<Rn_Lookup_Values> attribute_flex_values);
	
	// MODEL CLASS GETTER SETTERS
	String fieldsGetterSetterForModelClass(List<Rn_Fb_Line_DTO> lineDto);
	String attribute_flex_fieldsGetterSetterForModelClass(List<Rn_Lookup_Values> attribute_flex_values);

	// MODEL CLASS WHO COLUMNS WITH GETTER SETTER
	String whoColumnsForModelClass();
	
	// DAO_IMPL SELECT QUERY (COMMA separated FIELDS)
	String comma_separated_datatypes(List<Rn_Fb_Line_DTO> lineDto);
	String comma_separated_attribute_flex(List<Rn_Lookup_Values> attribute_flex_values);
	String comma_separated_who_columns();
	
	// DAO_IMPL METHOD SETTERS
	String fieldsSetterInDaoImpl(List<Rn_Fb_Line_DTO> lineDto);
	String attribute_flex_fieldsSetterInDaoImpl(List<Rn_Lookup_Values> attribute_flex_values, String table_name);
	// who columns setter code
	String who_columns_setterInDaoImpl(String table_name);
	
	// GRID-VIEW JSP DATA-TYPE
	String fieldLabelForGridViewJsp(List<Rn_Fb_Line_DTO> lineDto);
	String fieldValueForGridViewJsp(List<Rn_Fb_Line_DTO> lineDto);
	
	// READ-ONLY JSP DATA-TYPE (IN BETWEEN </c:forEach>)
	String fieldsForReadOnlyJsp(List<Rn_Fb_Line_DTO> lineDto);
	
	// UPDATE JSP DATA-TYPE (IN BETWEEN </c:forEach>)
	String fieldsForUpdateJsp(List<Rn_Fb_Line_DTO> lineDto);
	
	// ENTRY-FOEM DATA-TYPE CODE
	String fieldsForEntryFormJsp(List<Rn_Fb_Line_DTO> lineDto);
	
	// TILES CODE
	String tilesCode();
	// PROPERTIES FILE CODE
	String propertiesFileCode();
	
	// ACTION BUILDER CODE
	String action_builder_code();

}
