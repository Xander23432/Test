package com.realnet.actionbuilder.service;

public interface Rn_Cff_ActionBuilderRules_Service {
	void updateUsingJDBC(String table_name, String column_name, String value, int id);

	void updateUsingJdbcTemplate(String table_name, String column_name, String value, int id);

	void updateUsingHibernate(String table_name, String column_name, String value, int id);

	void fileStringReplace(String path, String oldString, String newString);

	String variableStringReplace(String str, String oldString, String newString);

	String stringAppendInVariable(String str, String newString);

	void stringAppendInFile(String path, String str);

}
