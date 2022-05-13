package com.realnet.wfb.controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.realnet.fnd.entity.Error;
import com.realnet.fnd.entity.ErrorPojo;
import com.realnet.fnd.entity.Rn_Lookup_Values;
import com.realnet.fnd.entity.Success;
import com.realnet.fnd.entity.SuccessPojo;
import com.realnet.fnd.service.Rn_LookUp_Service;
import com.realnet.qb.entity.ExportDataDTO;
import com.realnet.qb.entity.Rn_CreateQuery;
import com.realnet.qb.service.Rn_CreateQuery_Service;
import com.realnet.users.entity.User;
import com.realnet.users.service.UserService;
import com.realnet.utils.Constant;
import com.realnet.utils.RealNetUtils;
import com.realnet.wfb.entity.Rn_Fb_Header;
import com.realnet.wfb.entity.Rn_Fb_Line;
import com.realnet.wfb.service.Rn_WireFrame_Service;

import io.swagger.annotations.Api;

@RestController
@RequestMapping(value = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(tags = { "Spring Boot WireFrame" })
public class Rn_Angular_Wireframe_Builder_Controller {

	@Value("${angularProjectPath}")
	private String angularProjectPath;
	
	@Value("${projectPath}")
	private String projectPath;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private Rn_CreateQuery_Service createQueryService;
	
	@Autowired(required=false)
	private UserService userService;
	
	@Autowired
	private Rn_WireFrame_Service wireFrameService;

	@Autowired
	private Rn_LookUp_Service lookUpService;

	@Transactional
	@PostMapping(value = "/createAngularTable")
	public ResponseEntity<?> createHeaderTable(@RequestParam("header_id") Integer id, @Valid @RequestBody ExportDataDTO exportDataDTO) throws IOException { // this is rn_fb_header ID

	
		StringBuilder sqlQuery = new StringBuilder();

		// get data from Angular Form (form data)
		boolean data = exportDataDTO.getData();
		if (data) {
			System.out.println("Data Present");
		} else {
			System.out.println("Data Not Present");
		}

		// Rn_Fb_Header table value by Id
		Rn_Fb_Header fbh = wireFrameService.getById(id);
		String table_name = fbh.getTableName();
		String line_table_name = fbh.getLineTableName();
		String form_type = fbh.getFormType();

		// Rn_Fb_Line table value by Id
		List<Rn_Fb_Line> fbl = fbh.getRn_fb_lines();
		String mapping = fbl.get(0).getMapping();

		if (form_type.equals("header_only") || form_type.equals("header_line") || form_type.equals("multiline")) {
			String dropQuery = "DROP TABLE IF EXISTS " + table_name + "";
			jdbcTemplate.execute(dropQuery);
		} else if (form_type.equals("line_only") || form_type.equals("header_line") || form_type.equals("multiline")) {
			String dropQuery = "DROP TABLE IF EXISTS " + line_table_name + "";
			jdbcTemplate.execute(dropQuery);
		}

		if (form_type.equals("line_only")) {
			sqlQuery.append("create table if not exists " + line_table_name + " (");
		} else if (form_type.equals("header_only")) {
			sqlQuery.append("create table if not exists " + table_name + " (");
		}

		for (int i = 0; i < fbl.size(); i++) {
			mapping = fbl.get(i).getMapping();
			String data_type = fbl.get(i).getDataType();
			String key1 = fbl.get(i).getKey1();

			if (key1.equals("PRI")) {
				sqlQuery.append(mapping + " " + data_type + "(45) NOT NULL AUTO_INCREMENT, ");
			} else if (data_type.equals("longtext") || data_type.equals("datetime") || data_type.equals("double")) {
				sqlQuery.append(mapping + " " + data_type + ", ");
			} else {
				sqlQuery.append(mapping + " " + data_type + "(45), ");
			}
		}

		sqlQuery.append(
				" account_id int(11), CREATED_BY varchar(25),LAST_UPDATED_BY varchar(25), CREATION_DATE datetime, LAST_UPDATE_DATE datetime,"
						+ " extn1 varchar(25),extn2 varchar(25),extn3 varchar(25),extn4 varchar(25),extn5 varchar(25),extn6 varchar(25),"
						+ " extn7 varchar(25),extn8 varchar(25),extn9 varchar(25),extn10 varchar(25),extn11 varchar(25),extn12 varchar(25),"
						+ "extn13 varchar(25),extn14 varchar(25),extn15 varchar(25), flex1 varchar(25), flex2 varchar(25), flex3 varchar(25), flex4 varchar(25),"
						+ " flex5 varchar(25),  PRIMARY KEY(id));");

		System.out.println("Spring Boot sqlQuery = " + sqlQuery);

		String final_query = sqlQuery.toString().toLowerCase();
		jdbcTemplate.execute(final_query);
		User loggedInUser = userService.getLoggedInUser();
		long user_id = loggedInUser.getUserId();
		long account_id = loggedInUser.getSys_account().getId();
		int project_id = fbh.getModule().getProject().getId();
		System.out.println("export as data::" + data);

		// save data into rn_create_query_t Table
		Rn_CreateQuery rn_create_query_t = new Rn_CreateQuery();
		rn_create_query_t.setProjectId(project_id);
		rn_create_query_t.setAccountId(account_id);
		rn_create_query_t.setTableName(table_name);
		rn_create_query_t.setCreateQuery(final_query);
		rn_create_query_t.setCreatedBy(user_id);
		rn_create_query_t.setUpdatedBy(user_id);
		rn_create_query_t.setBuild(false);
		rn_create_query_t.setData(data);

		Rn_CreateQuery savedQuery = createQueryService.save(rn_create_query_t);

		if (savedQuery == null) {
			ErrorPojo errorPojo = new ErrorPojo();
			Error error = new Error();
			error.setTitle(Constant.QUERY_API_TITLE);
			error.setMessage(Constant.QUERY_CREATE_FAILURE);
			errorPojo.setError(error);
			return new ResponseEntity<ErrorPojo>(errorPojo, HttpStatus.EXPECTATION_FAILED);
		}
		SuccessPojo successPojo = new SuccessPojo();
		Success success = new Success();
		success.setTitle(Constant.QUERY_API_TITLE);
		success.setMessage(Constant.QUERY_CREATE_SUCCESS);
		successPojo.setSuccess(success);
		return new ResponseEntity<SuccessPojo>(successPojo, HttpStatus.CREATED);
	}

	@GetMapping(value = "/build_angular_form")
	public ResponseEntity<?> build_wireframe(@RequestParam("header_id") Integer id) throws IOException { // this is rn_fb_header ID
		/*
		 * Spring Boot Back-End Builder
		 * =======================================================
		 */

		// HEADER PART (FINAL STRING WILL APPEND HERE)
		StringBuilder controller = new StringBuilder();
		StringBuilder repository = new StringBuilder();
		StringBuilder service = new StringBuilder();
		StringBuilder service_impl = new StringBuilder();
		StringBuilder model = new StringBuilder();

		// LINE PART (FINAL STRING WILL APPEND HERE)
		StringBuilder line_controller = new StringBuilder();
		StringBuilder line_repository = new StringBuilder();
		StringBuilder line_model = new StringBuilder();

		// model class dynamic variables
		StringBuilder model_class_imports = new StringBuilder();
		StringBuilder model_class_pk_id_attribute = new StringBuilder();
		StringBuilder model_class_integer_attribute = new StringBuilder();
		StringBuilder model_class_double_attribute = new StringBuilder();
		StringBuilder model_class_datetime_attribute = new StringBuilder();
		StringBuilder model_class_longtext_attribute = new StringBuilder();
		StringBuilder model_class_varchar_attribute = new StringBuilder();
		StringBuilder model_class_flex_attribute = new StringBuilder();
		StringBuilder model_class_getter_setter_attribute = new StringBuilder();
		StringBuilder model_class_who_columns = new StringBuilder();

		// repository class dynamic variables
		StringBuilder repository_class_imports = new StringBuilder();

		// service class dynamic variables
		StringBuilder service_class_imports = new StringBuilder();

		// serviceImpl class dynamic variables
		StringBuilder service_impl_class_imports = new StringBuilder();
		StringBuilder service_impl_class_get_set_attribute = new StringBuilder();

		// controller class dynamic variables
		StringBuilder controller_class_imports = new StringBuilder();
		
		// H-L dependency (LINE TABLE)
		StringBuilder line_model_class_imports = new StringBuilder();
		
		StringBuilder header_model_class_line_dependency = new StringBuilder();
		StringBuilder line_model_class_datatype_variables = new StringBuilder();
		StringBuilder line_model_class_flex_attribute = new StringBuilder();
		//StringBuilder line_model_class_header_dependency = new StringBuilder();
		StringBuilder line_model_class_getter_setter_attribute = new StringBuilder();
		StringBuilder line_controller_class_update_method_variable = new StringBuilder();

		
		// ================ANGULAR CODE WILL BE APPEND HERE============
		StringBuilder ng_header_model_ts = new StringBuilder();
		StringBuilder ng_component_html = new StringBuilder();
		StringBuilder ng_component_css = new StringBuilder();
		StringBuilder ng_component_ts = new StringBuilder();
		StringBuilder ng_module_ts = new StringBuilder();
		StringBuilder ng_service_ts = new StringBuilder();
		StringBuilder ng_routing_module_ts = new StringBuilder();
		
		// ANGULAR HEADER-LINE CODE WILL APPEND HERE
		StringBuilder ng_line_model_ts = new StringBuilder();
		StringBuilder ng_header_model_ts_line_imports = new StringBuilder();
		StringBuilder ng_header_model_ts_array_variable = new StringBuilder();
		StringBuilder ng_service_ts_line_imports = new StringBuilder();
		StringBuilder ng_service_ts_line_methods = new StringBuilder();
		
		StringBuilder ng_add_form_component_ts_formArray_variable = new StringBuilder();
		StringBuilder ng_add_form_component_ts_formArray_methods = new StringBuilder();
		StringBuilder ng_add_form_component_ts_formArray_validation_variables = new StringBuilder();
		
		StringBuilder ng_add_form_component_html_line_table_th = new StringBuilder();
		StringBuilder ng_add_form_component_html_line_table_td = new StringBuilder();
		
		StringBuilder ng_add_form_component_html_line_table_attribute_flex_th = new StringBuilder();
		StringBuilder ng_add_form_component_html_line_table_attribute_flex_td = new StringBuilder();
		
		
		StringBuilder ng_add_form_component_html_line_table_code = new StringBuilder();


		// ANGULAR MODEL class dynamic variables
		StringBuilder ng_header_model_ts_attribute = new StringBuilder();
		StringBuilder ng_line_model_ts_attribute = new StringBuilder();

		// ANGULAR MODULE class dynamic variables
		StringBuilder ng_module_ts_imports = new StringBuilder();	
		StringBuilder ng_module_ts_line_imports = new StringBuilder();
		// StringBuilder ng_module_ts_line_declarations = new StringBuilder(); // USE IF NOT WORK IN MODULE

		// ANGULAR ROUTING class dynamic variables
		StringBuilder ng_routing_ts_imports = new StringBuilder();
		StringBuilder ng_routing_ts_line_imports = new StringBuilder();
		
		StringBuilder ng_routing_ts_routing_paths = new StringBuilder(); // only for header
		StringBuilder ng_routing_ts_header_line_routing_paths = new StringBuilder(); // for header-line  
		

		// ===============HEADER CRUD CODE WILL APPEND HERE====================
		// ALL GRID VIEW
		StringBuilder ng_all_grid_view_component_html = new StringBuilder();
		StringBuilder ng_all_grid_view_component_css = new StringBuilder();
		StringBuilder ng_all_grid_view_component_ts = new StringBuilder();
		// ADD FORM
		StringBuilder ng_add_form_component_html = new StringBuilder();
		StringBuilder ng_add_form_component_css = new StringBuilder();
		StringBuilder ng_add_form_component_ts = new StringBuilder();
		// EDIT
		StringBuilder ng_edit_form_component_html = new StringBuilder();
		StringBuilder ng_edit_form_component_css = new StringBuilder();
		StringBuilder ng_edit_form_component_ts = new StringBuilder();
		// READ ONLY
		StringBuilder ng_read_only_form_component_html = new StringBuilder();
		StringBuilder ng_read_only_form_component_css = new StringBuilder();
		StringBuilder ng_read_only_form_component_ts = new StringBuilder();
		
		// ===============LINE CRUD CODE WILL APPEND HERE====================
		// LINE ALL GRID VIEW
		StringBuilder ng_line_all_grid_view_component_html = new StringBuilder();
		StringBuilder ng_line_all_grid_view_component_css = new StringBuilder();
		StringBuilder ng_line_all_grid_view_component_ts = new StringBuilder();
		// LINE ADD FORM
		StringBuilder ng_line_add_form_component_html = new StringBuilder();
		StringBuilder ng_line_add_form_component_css = new StringBuilder();
		StringBuilder ng_line_add_form_component_ts = new StringBuilder();
		// LINE EDIT
		StringBuilder ng_line_edit_form_component_html = new StringBuilder();
		StringBuilder ng_line_edit_form_component_css = new StringBuilder();
		StringBuilder ng_line_edit_form_component_ts = new StringBuilder();
		// LINE READ ONLY
		StringBuilder ng_line_read_only_form_component_html = new StringBuilder();
		StringBuilder ng_line_read_only_form_component_css = new StringBuilder();
		StringBuilder ng_line_read_only_form_component_ts = new StringBuilder();
		

		// ANGULAR ADD Form dynamic variables
		StringBuilder ng_add_form_component_html_integer_attribute = new StringBuilder();
		StringBuilder ng_add_form_component_html_double_attribute = new StringBuilder();
		StringBuilder ng_add_form_component_html_datetime_attribute = new StringBuilder();
		StringBuilder ng_add_form_component_html_varchar_attribute = new StringBuilder();
		StringBuilder ng_add_form_component_html_longtext_attribute = new StringBuilder();
		StringBuilder ng_add_form_component_html_button_attribute = new StringBuilder();
		StringBuilder ng_add_form_component_ts_imports = new StringBuilder();
		StringBuilder ng_add_form_component_ts_validation_var = new StringBuilder();
		
		// ANGULAR LINE-ADD Form dynamic variables
		StringBuilder ng_line_add_form_component_html_datatype_variables = new StringBuilder();
		StringBuilder ng_line_add_form_component_ts_validation_var = new StringBuilder();

		// ANGULAR EDIT Form dynamic variables
		StringBuilder ng_edit_form_component_html_datatype_attribute = new StringBuilder();// I,Dou,DATE,VAR,LNG,FLX
		StringBuilder ng_edit_form_component_html_flex_attribute = new StringBuilder();// FLX
		StringBuilder ng_edit_form_component_html_line_code = new StringBuilder();
		StringBuilder ng_edit_form_component_ts_imports = new StringBuilder();
		
		// ANGULAR LINE-EDIT Form dynamic variables
		StringBuilder ng_line_edit_form_component_html_datatype_attribute = new StringBuilder();// I,Dou,DATE,VAR,LNG,FLX
		StringBuilder ng_line_edit_form_component_html_flex_attribute = new StringBuilder();// FLX
		

		// ANGULAR (DETAILS FORM) dynamic HEADER variables
		//StringBuilder ng_read_only_form_component_html_datatype_attribute = new StringBuilder();
		StringBuilder ng_read_only_form_component_html_datatype_th = new StringBuilder();
		StringBuilder ng_read_only_form_component_html_datatype_td = new StringBuilder();
		
		// ANGULAR (DETAILS FORM) dynamic LINE variables
		StringBuilder ng_read_only_form_component_html_line_code = new StringBuilder();
		StringBuilder ng_read_only_form_component_html_line_datatype_th = new StringBuilder();
		StringBuilder ng_read_only_form_component_html_line_datatype_td = new StringBuilder();
		
		// ANGULAR (LINE-DETAILS FORM) dynamic variables
		StringBuilder ng_line_read_only_form_component_html_table_th = new StringBuilder();
		StringBuilder ng_line_read_only_form_component_html_table_td = new StringBuilder();
		
		
		StringBuilder ng_read_only_form_component_ts_imports = new StringBuilder();

		// ANGULAR ALL (GRID VIEW) dynamic variables
		StringBuilder ng_all_grid_view_component_html_datatype_attribute = new StringBuilder();
		StringBuilder ng_all_grid_view_component_ts_imports = new StringBuilder();
		StringBuilder ng_all_grid_view_component_ts_display_columns = new StringBuilder();
		StringBuilder ng_all_grid_view_component_ts_search_string = new StringBuilder();
		StringBuilder ng_all_grid_view_component_ts_sort_string = new StringBuilder();
		
		// ANGULAR LINE-ALL (GRID VIEW) Form dynamic variables
		StringBuilder ng_line_all_grid_view_component_html_datatype_attribute = new StringBuilder();
		StringBuilder ng_line_all_grid_view_component_ts_display_columns = new StringBuilder();
		StringBuilder ng_line_all_grid_view_component_ts_search_string = new StringBuilder();
		StringBuilder ng_line_all_grid_view_component_ts_sort_string = new StringBuilder();
		

		// ===================HEADER EXTENSION FORM FIELDS============================
		StringBuilder ng_extension_add_form_component_html = new StringBuilder();
		StringBuilder ng_extension_add_form_component_html_flex_attribute = new StringBuilder();
		StringBuilder ng_extension_add_form_component_ts = new StringBuilder();
		
		// ===================LINE EXTENSION FORM FIELDS============================
		StringBuilder ng_line_extension_add_form_component_html = new StringBuilder();
		StringBuilder ng_line_extension_add_form_component_html_flex_attribute = new StringBuilder();
		StringBuilder ng_line_extension_add_form_component_ts = new StringBuilder();

		/* H E A D E R - TABLE - V A L U E S */
		/*
		 * GETTING TYPES OF DATA (INT, DOUBLE,VARCHAR, LONGTEXT, ATTRIBUTES, FLEX,
		 * SECTION)
		 */
		// FLEX (Extension) values (ATTRIBUTE 1 - 15, FLEX 1-5)
		List<Rn_Lookup_Values> attribute_flex_values = lookUpService.getExtensions();

		// SECTION values
		List<Rn_Fb_Line> section_values = wireFrameService.getSection(id);

		// if PK, GET = [FIELD_NAME(LABEL1-4, SUBMIT) , MAPPING(TEXTFIELD 1-4, BUTTON,
		// SECTION)
		List<Rn_Fb_Line> pk_value = wireFrameService.getPrimaryKeyField(id);

		// -----testing---------
		String field_name_for_id = pk_value.get(0).getMapping();
		String field_name_first_upper = RealNetUtils.toFirstUpperCase(field_name_for_id);
		System.out.println("Niladri fitst upper method value testing :::" + field_name_first_upper);

		// INTEGER values (Not PK)
		List<Rn_Fb_Line> integerList = wireFrameService.getIntegerFields(id);

		// VARCHAR values
		List<Rn_Fb_Line> varcharList = wireFrameService.getVarcharFields(id);

		// Date Time
		List<Rn_Fb_Line> datetimeList = wireFrameService.getDateTimeFields(id);

		// Long Text
		List<Rn_Fb_Line> longtextList = wireFrameService.getLongtextFields(id);

		// Double values
		List<Rn_Fb_Line> doubleList = wireFrameService.getDoubleFields(id);

		// BUTTON
		List<Rn_Fb_Line> buttonList = wireFrameService.getButtonList(id);

		/* L I N E - TABLE - V A L U E S */
		/*
		 * GETTING TYPES OF DATA (PK , VARCHAR, ATTRIBUTES, FLEX, SECTION)
		 */
		List<Rn_Fb_Line> line_pk_value = wireFrameService.getLinePrimarkKeyField(id);
		List<Rn_Fb_Line> line_section = wireFrameService.getLineSection(id);
		List<Rn_Fb_Line> line_varcharList = wireFrameService.getLineVarcharFields(id);

		System.out.println("-----------build from started-------------------------");

		/*
		 * Header Table Values GET UI_NAME SET controller, model, repository, service
		 * name.
		 */
		Rn_Fb_Header headerTableValue = wireFrameService.getById(id);
		String ui_name = headerTableValue.getUiName();
		String controller_name = ui_name.concat("_Controller");
		String dao_name = ui_name.concat("_Dao");
		String dao_impl_name = ui_name.concat("_DaoImpl");
		String repository_name = ui_name.concat("_Repository");
		String service_name = ui_name.concat("_Service");
		String service_impl_name = ui_name.concat("_ServiceImpl");

		// @Column(table="table_name") && Model class name
		// String table_name = ui_name.concat("_T");
		String table_name = null;

		// String header_table_name = null;
		String line_table_name = null;
		String line_repository_name = null;
		String line_controller_name = null;

		// FORM_TYPE
		String form_type = headerTableValue.getFormType();

		// HEADER & LINE TABLE NAME DEPENDS ON FORM_TYPE
		if (form_type.equalsIgnoreCase("HEADER_LINE")) {
			table_name = ui_name.concat("_header_T");
			line_table_name = ui_name.concat("_line_T");
			line_repository_name = ui_name.concat("_Line_Repository");
			line_controller_name = ui_name.concat("_Line_Controller");
		} else {
			table_name = ui_name.concat("_T");
		}

		System.out.println("Before = " + ui_name + "\n" + controller_name + "\n" + dao_name + "\n" + dao_impl_name
				+ "\n" + repository_name + "\n" + service_name + "\n" + service_impl_name + "\n"
				+ "HEADER TABLE NAME = " + table_name + "\n" + "LINE TABLE NAME = " + line_table_name);

		/*---- First Upper name (HEADER-CLASSES) (back-end)----------*/
		String controller_name_first_upper = RealNetUtils.toFirstUpperCase(controller_name);
		String repository_name_first_upper = RealNetUtils.toFirstUpperCase(repository_name);
		
		String dao_name_first_upper = RealNetUtils.toFirstUpperCase(dao_name); // dao not used in project
		String dao_impl_name_first_upper = RealNetUtils.toFirstUpperCase(dao_impl_name); //dao impl is not used in project
		String service_name_first_upper = RealNetUtils.toFirstUpperCase(service_name);
		String service_impl_name_first_upper = RealNetUtils.toFirstUpperCase(service_impl_name);
		String table_name_first_upper = RealNetUtils.toFirstUpperCase(table_name);
		String table_name_upper = table_name.toUpperCase(); // table name == Model class name
		

		/*---- First Upper name (LINE-CLASSES) (back-end)----------*/
		
		// header-line || line only table
//		String header_table_name_upper = header_table_name.toUpperCase(); // INSTRUCTOR_HEADER_T
//		String header_table_name_lower = header_table_name.toLowerCase(); // instructor_header_t
//		String header_table_name_first_upper = RealNetUtils.toFirstUpperCase(header_table_name); // Instructor_header_t
		
		String line_table_name_upper = line_table_name.toUpperCase(); // COURSE_LINE_T
		String line_table_name_lower = line_table_name.toLowerCase(); // course_line_t
		String line_table_name_first_upper = RealNetUtils.toFirstUpperCase(line_table_name); // Course_line_t
		String line_controller_name_first_upper = RealNetUtils.toFirstUpperCase(line_controller_name);
		String line_repository_name_first_upper = RealNetUtils.toFirstUpperCase(line_repository_name);
		String line_repository_name_lower = line_repository_name.toLowerCase();

		/*-------------lower name for instance----------*/
		String table_name_lower = table_name.toLowerCase();
		String repository_name_lower = repository_name.toLowerCase();
		String service_name_lower = service_name.toLowerCase();
		
		System.out.println("After = " + controller_name_first_upper + "\n" + dao_name_first_upper + "\n"
				+ dao_impl_name_first_upper + "\n" + repository_name_first_upper + "\n" + service_name_first_upper
				+ "\n" + service_impl_name_first_upper + "\n"
				+ "HEADER TABLE NAME = " + table_name_first_upper + "\n"
				+ "LINE TABLE NAME = " + line_table_name_first_upper);

		// ===========FRONT END FILE NAMES DEPENDS ON UI NAME===============
		
		String ng_ui_name = RealNetUtils.toFirstUpperCase(ui_name);
		String ng_component_ts_name = ng_ui_name.concat("Component");
		String ng_module_ts_name = ng_ui_name.concat("Module");
		String ng_service_ts_name = ng_ui_name.concat("Service");
		String ng_routing_module_ts_name = ng_ui_name.concat("RoutingModule");

		String ng_service_ts_name_lower = ui_name.toLowerCase().concat("Service"); // Ex: trainingService
		// Routing Path names
		String ng_path_name = ui_name.toLowerCase().substring(3, ui_name.length() - 1);
		// CRUD Components name
		String ng_all_grid_view_component_name = "All" + ng_ui_name.concat("Component");
		String ng_add_form_component_name = "Add" + ng_ui_name.concat("Component");
		String ng_edit_component_name = "Edit" + ng_ui_name.concat("Component");
		String ng_read_only_component_name = ng_ui_name + "DetailsComponent";
		// Extension Components name
		String ng_extension_add_component_name = "AddExt" + ng_ui_name.concat("Component");
		// File Folder name
		String ng_folder_name = ui_name.toLowerCase();
		String ng_file_name = ui_name.toLowerCase();
		
		// MODEL Name depends on form_type
		String ng_model_ts_name = null;
		
		// HEADER-LINE CRUD COMPONENTS NAME
		String ng_line_model_ts_name = null;
		String ng_line_all_grid_view_component_name = null;
		String ng_line_add_form_component_name = null;
		String ng_line_edit_component_name = null;
		String ng_line_read_only_component_name = null;
		// Extension Components name
		String ng_line_extension_add_component_name = null;

		// file names depend on this variable
		String ng_line_file_name = null;
		String ng_line_folder_name = null;
		
		if(form_type.equalsIgnoreCase("HEADER_LINE")) {
			ng_model_ts_name = ng_ui_name.concat("_header_T");
			ng_line_model_ts_name = ng_ui_name.concat("_line_T");

			// LINE CRUD
			ng_line_all_grid_view_component_name = "All" + ng_ui_name.concat("_lineComponent");
			ng_line_add_form_component_name = "Add" + ng_ui_name.concat("_lineComponent");
			ng_line_edit_component_name = "Edit" + ng_ui_name.concat("_lineComponent");
			ng_line_read_only_component_name = ng_ui_name + "_lineDetailsComponent";
			// Extension Components name
			ng_line_extension_add_component_name = "AddExt" + ng_ui_name.concat("_lineComponent");
			
			ng_line_file_name = ui_name.toLowerCase().concat("_line"); // add-rn_hl_line.component.css,HTML,TS file name
			ng_line_folder_name = ui_name.toLowerCase().concat("_line");
		}
		if(form_type.equalsIgnoreCase("HEADER_ONLY")) {
			ng_model_ts_name = ng_ui_name.concat("_T");
		}
		String ng_model_ts_name_lower = ng_model_ts_name.toLowerCase();
		String ng_line_model_ts_name_lower = ng_line_model_ts_name.toLowerCase();
		
		
		// PROPERTIES FILE CODE
		StringBuilder properties_file_code = new StringBuilder();
		StringBuilder properties_file_fields_code = new StringBuilder("fields = ");
		
		
		/*-----------------------BUILDER CODE START HERE----------------------------*/
		
		
		// ===========IF form_type::: HEADER_ONLY===========
		if (form_type.equalsIgnoreCase("HEADER_ONLY")) {
		// routing.module.ts routing paths: ng_path_name
		ng_routing_ts_routing_paths.append(
				  "		{ path: '', component:\t" + ng_component_ts_name + ",\r\n" 
				+ "      children: [\r\n" 
				+ "          { path: '', redirectTo: '" + ng_path_name + "' },\r\n" 
				+ "          { path: '" + ng_path_name + "', component:\t" + ng_all_grid_view_component_name + "},\r\n" 
				+ "          { path: 'add', component:\t" + ng_add_form_component_name + " },\r\n" 
				+ "          { path:'details/:id', component:\t" + ng_read_only_component_name + "},\r\n"
				+ "          { path: 'update/:id', component:\t" + ng_edit_component_name + " },\r\n"
				+ "		 ]\r\n"
				+ "		}\r\n");

		}
		// ===========IF form_type::: HEADER_ONLY OR HEADER_LINE===========
		if (form_type.equalsIgnoreCase("HEADER_ONLY") || form_type.equalsIgnoreCase("HEADER_LINE")) {
			// GET PK for HEADER TABLE
			for (int i = 0; i < pk_value.size(); i++) {
				//String field_name = pk_value.get(i).getField_name();
				String mapping = pk_value.get(i).getMapping();
				String lowerCase = mapping.toLowerCase();
				String upperCase = mapping.toUpperCase();
				String firstUpper = RealNetUtils.toFirstUpperCase(mapping);
				System.out.println("model_class PK name = " + firstUpper);

				// write BACK-END model class code
				model_class_pk_id_attribute.append("@Id\r\n" + "@GeneratedValue(strategy = GenerationType.IDENTITY)\r\n"
						+ "@Column(name = \"" + upperCase + "\")\r\n" + "private int " + lowerCase + ";\r\n");
				// getter setter
				model_class_getter_setter_attribute.append("\r\n" + "public int get" + firstUpper + "() {\r\n"
						+ "	return\t" + lowerCase + ";\r\n" + "}\r\n" + "public void set" + firstUpper + "(int\t"
						+ lowerCase + ") {\r\n" + "	this." + lowerCase + "=" + lowerCase + ";\r\n" + "}\r\n");

				// write ANGULAR model.ts code
				ng_header_model_ts_attribute.append("\r\n" + " public\t" + lowerCase + ": number;");

				// write ANGULAR ALL (GRID-VIEW) component.ts code
				ng_all_grid_view_component_ts_display_columns.append("'" + lowerCase + "', ");
				// filter
				ng_all_grid_view_component_ts_search_string.append(ng_model_ts_name_lower + "." + lowerCase + "+");
				// sort
				ng_all_grid_view_component_ts_sort_string.append("case\t'" + lowerCase
						+ "': [propertyA, propertyB] = [a." + lowerCase + ", b." + lowerCase + "]; break;\r\n");
				// write ANGULAR DETAILS component.ts code
				ng_read_only_form_component_html_datatype_th.append("<th>" + upperCase + "</th>\r\n");
				ng_read_only_form_component_html_datatype_td.append("<td>{{ " + ng_model_ts_name_lower + "." + lowerCase + " }}</td>\r\n");
				
				// ui_name.properties file CODE
				properties_file_fields_code.append(lowerCase + ", ");
			}

			// GET INTEGER TYPE VALUE
			for (int i = 0; i < integerList.size(); i++) {
				String field_name = integerList.get(i).getFieldName();
				String mapping = integerList.get(i).getMapping();
				String dataType = integerList.get(i).getDataType();
				String lowerCase = mapping.toLowerCase();
				String upperCase = mapping.toUpperCase();
				String firstUpper = RealNetUtils.toFirstUpperCase(mapping);
				System.out.println("model_class INTEGER fields name" + firstUpper);

				if (dataType.equalsIgnoreCase("INT")) {
					// write BACK-END model class code
					model_class_integer_attribute.append(
							"\r\n@Column(name = \"" + upperCase + "\")\r\n" + "private int \t" + lowerCase + ";\r\n");
					model_class_getter_setter_attribute.append("\r\n" + "public int get" + firstUpper + "() {\r\n"
							+ "	return\t" + lowerCase + ";\r\n" + "}\r\n" + "public void set" + firstUpper + "(int\t"
							+ lowerCase + ") {\r\n" + "	this." + lowerCase + "=" + lowerCase + ";\r\n" + "}\r\n");

					// write BACK-END service class code
					service_impl_class_get_set_attribute.append(
							"\r\n" + table_name_lower + ".set" + firstUpper + "(value.get" + firstUpper + "());");

					// write FRONT-END model.ts code
					ng_header_model_ts_attribute.append("\r\n" + " public\t" + lowerCase + ": number;");

					// write FRONT-END ADD FORM component.html code
					ng_add_form_component_html_integer_attribute
							.append("        <mat-form-field class=\"example-full-width\">\r\n"
									+ "          <mat-label>" + field_name + "</mat-label>\r\n"
									+ "          <input matInput type=\"number\" formControlName=\"" + lowerCase
									+ "\" placeholder=\"" + firstUpper + "\" required>\r\n"
									+ "        </mat-form-field>");

					// write FRONT-END ADD form component.ts code
					ng_add_form_component_ts_validation_var.append(lowerCase + ": [null, Validators.required],\r\n");

					// write front-end EDIT FORM component.html code
					ng_edit_form_component_html_datatype_attribute.append(
							"						<mat-form-field class=\"example-full-width\">\r\n" + 
							"							<mat-label>" + field_name + "</mat-label>\r\n" + 
							"							<input matInput [(ngModel)]=\"" + ng_model_ts_name_lower + "." + lowerCase + "\" name=\"" + lowerCase + "\" type=\"number\">\r\n" + 
							"						</mat-form-field>\r\n");

					// write front-end DETAILS FORM component.html code
					ng_read_only_form_component_html_datatype_th.append("<th>" + upperCase + "</th>\r\n");
					
					ng_read_only_form_component_html_datatype_td.append("<td>{{ " + ng_model_ts_name_lower + "." + lowerCase + " }}</td>\r\n");

					// write front-end ALL (GRID-VIEW) component.html code
					ng_all_grid_view_component_html_datatype_attribute.append("<ng-container matColumnDef=\""
							+ lowerCase + "\">\r\n" + "      <th mat-header-cell *matHeaderCellDef mat-sort-header>"
							+ field_name + "</th>\r\n" + "      <td mat-cell *matCellDef=\"let element\"> {{element."
							+ lowerCase + "}} </td>\r\n" + "    </ng-container>");
					// write front-end ALL (GRID-VIEW) component.ts code
					ng_all_grid_view_component_ts_display_columns.append("'" + lowerCase + "', ");
					// filter
					ng_all_grid_view_component_ts_search_string.append(ng_model_ts_name_lower + "." + lowerCase + "+");
					// sort
					ng_all_grid_view_component_ts_sort_string.append("case\t'" + lowerCase
							+ "': [propertyA, propertyB] = [a." + lowerCase + ", b." + lowerCase + "]; break;\r\n");
					
					// ui_name.properties file CODE
					properties_file_fields_code.append(lowerCase + ", ");
				}
			}

			// GET DOUBLE TYPE VALUE
			for (int i = 0; i < doubleList.size(); i++) {
				String field_name = doubleList.get(i).getFieldName();
				String mapping = doubleList.get(i).getMapping();
				String dataType = doubleList.get(i).getDataType();
				String lowerCase = mapping.toLowerCase();
				String upperCase = mapping.toUpperCase();
				String firstUpper = RealNetUtils.toFirstUpperCase(mapping);
				System.out.println("model_class DOUBLE fields name" + firstUpper);

				if (dataType.equalsIgnoreCase("DOUBLE")) {

					// write BACK-END model class code
					model_class_double_attribute.append(
							"\r\n@Column(name = \"" + upperCase + "\")" + "\r\nprivate String\t" + lowerCase + ";");

					model_class_getter_setter_attribute
							.append("\r\npublic String get" + firstUpper + "(){" + "\nreturn\t" + lowerCase + ";"
									+ "\r\n }" + "\n\npublic void set" + firstUpper + "(String\t" + lowerCase + ")"
									+ "\r\n{" + "\r\nthis." + lowerCase + " = " + lowerCase + ";" + "\r\n}");

					// write BACK-END service code
					service_impl_class_get_set_attribute.append(
							"\r\n" + table_name_lower + ".set" + firstUpper + "(value.get" + firstUpper + "());");

					// write ANGULAR model.ts code
					ng_header_model_ts_attribute.append("\r\n" + " public\t" + lowerCase + ": number;");

					// write ANGULAR ADD form component.html code
					ng_add_form_component_html_double_attribute
							.append("        <mat-form-field class=\"example-full-width\">\r\n"
									+ "          <mat-label>" + field_name + "</mat-label>\r\n"
									+ "          <input matInput type=\"number\" formControlName=\"" + lowerCase
									+ "\" placeholder=\"" + firstUpper + "\" required>\r\n"
									+ "        </mat-form-field>");
					// write front-end ADD form component.ts code
					ng_add_form_component_ts_validation_var.append(lowerCase + ": [null, Validators.required],\r\n");

					// write front-end EDIT FORM component.html code
					ng_edit_form_component_html_datatype_attribute
							.append("						<mat-form-field class=\"example-full-width\">\r\n" + 
									"							<mat-label>" + field_name + "</mat-label>\r\n" + 
									"							<input matInput [(ngModel)]=\"" + ng_model_ts_name_lower + "." + lowerCase + "\" name=\"" + lowerCase + "\" type=\"number\">\r\n" + 
									"						</mat-form-field>\r\n");

					// write front-end DETAILS FORM component.html code
					ng_read_only_form_component_html_datatype_th
							.append("<th>" + field_name + "</th>\r\n");
					
					ng_read_only_form_component_html_datatype_td.append("<td>{{ " + ng_model_ts_name_lower + "." + lowerCase + " }}</td>\r\n");

					// write front-end ALL (GRID-VIEW) component.html code
					ng_all_grid_view_component_html_datatype_attribute.append("<ng-container matColumnDef=\""
							+ lowerCase + "\">\r\n" + "      <th mat-header-cell *matHeaderCellDef mat-sort-header>"
							+ field_name + "</th>\r\n" + "      <td mat-cell *matCellDef=\"let element\"> {{element."
							+ lowerCase + "}} </td>\r\n" + "    </ng-container>");
					// write front-end ALL (GRID-VIEW) component.ts code
					ng_all_grid_view_component_ts_display_columns.append("'" + lowerCase + "', ");
					ng_all_grid_view_component_ts_search_string.append(ng_model_ts_name_lower + "." + lowerCase + "+");
					// sort string
					ng_all_grid_view_component_ts_sort_string.append("case\t'" + lowerCase
							+ "': [propertyA, propertyB] = [a." + lowerCase + ", b." + lowerCase + "]; break;\r\n");
					
					// ui_name.properties file CODE
					properties_file_fields_code.append(lowerCase + ", ");
				}
			}

			// GET DATETIME TYPE VALUE
			for (int i = 0; i < datetimeList.size(); i++) {
				String field_name = datetimeList.get(i).getFieldName();
				String mapping = datetimeList.get(i).getMapping();
				String dataType = datetimeList.get(i).getDataType();
				String lowerCase = mapping.toLowerCase();
				String upperCase = mapping.toUpperCase();
				String firstUpper = RealNetUtils.toFirstUpperCase(mapping);
				System.out.println("model_class DATETIME fields name" + firstUpper);

				if (dataType.equalsIgnoreCase("DATETIME")) {

					// write model class code
					model_class_datetime_attribute.append(
							"\r\n" + "@Column(name = \"" + upperCase + "\")" + "\r\nprivate Date\t" + lowerCase + ";");

					model_class_getter_setter_attribute.append("\r\n" + "public Date get" + firstUpper + "(){\r\n"
							+ "\nreturn\t" + lowerCase + ";" + "\n }" + "public void set" + firstUpper + "(Date\t"
							+ lowerCase + "){\r\n" + "this." + lowerCase + " = " + lowerCase + ";" + "\n}");

					// write service code
					service_impl_class_get_set_attribute.append(
							"\r\n" + table_name_lower + ".set" + firstUpper + "(value.get" + firstUpper + "());");

					// write angular model class code
					ng_header_model_ts_attribute.append("\r\n" + " public\t" + lowerCase + ": Date;");

					// write ANGULAR ADD form component.html code
					ng_add_form_component_html_datetime_attribute
							.append("        <mat-form-field class=\"example-full-width\">\r\n"
									+ "          <mat-label>" + field_name + "</mat-label>\r\n"
									+ "          <input matInput type=\"date\" formControlName=\"" + lowerCase
									+ "\" placeholder=\"" + firstUpper + "\">\r\n"
									+ "        </mat-form-field>");
					// write front-end ADD form component.ts code
					ng_add_form_component_ts_validation_var.append(lowerCase + ": [null, Validators.required],\r\n");

					// write front-end EDIT FORM component.html code
					ng_edit_form_component_html_datatype_attribute
							.append("						<mat-form-field class=\"example-full-width\">\r\n" + 
									"							<mat-label>" + field_name + "</mat-label>\r\n" + 
									"							<input matInput [(ngModel)]=\"" + ng_model_ts_name_lower + "." + lowerCase + "\" name=\"" + lowerCase + "\" type=\"date\">\r\n" + 
									"						</mat-form-field>\r\n");

					// write front-end DETAILS FORM component.html code
					ng_read_only_form_component_html_datatype_th.append("<th>" + field_name.toUpperCase() + "</th>\r\n");
			
					ng_read_only_form_component_html_datatype_td.append("<td>{{ " + ng_model_ts_name_lower + "." + lowerCase + " }}</td>\r\n");

		

					// write front-end ALL (GRID-VIEW) component.html code
					ng_all_grid_view_component_html_datatype_attribute.append("<ng-container matColumnDef=\""
							+ lowerCase + "\">\r\n" + "      <th mat-header-cell *matHeaderCellDef mat-sort-header>"
							+ field_name + "</th>\r\n" + "      <td mat-cell *matCellDef=\"let element\"> {{element."
							+ lowerCase + "}} </td>\r\n" + "    </ng-container>");
					// write front-end ALL (GRID-VIEW) component.ts code
					ng_all_grid_view_component_ts_display_columns.append("'" + lowerCase + "', ");
					ng_all_grid_view_component_ts_search_string.append(ng_model_ts_name_lower + "." + lowerCase + "+");
					// sort string
					ng_all_grid_view_component_ts_sort_string.append("case\t'" + lowerCase
							+ "': [propertyA, propertyB] = [a." + lowerCase + ", b." + lowerCase + "]; break;\r\n");
					
					// ui_name.properties file CODE
					properties_file_fields_code.append(lowerCase + ", ");
				}
			}

			// GET VARCHAR TYPE VALUE
			for (int i = 0; i < varcharList.size(); i++) {
				String field_name = varcharList.get(i).getFieldName();
				String mapping = varcharList.get(i).getMapping();
				String dataType = varcharList.get(i).getDataType();
				String lowerCase = mapping.toLowerCase();
				String upperCase = mapping.toUpperCase();
				String firstUpper = RealNetUtils.toFirstUpperCase(mapping);

				if (dataType.equalsIgnoreCase("VARCHAR")) {
					// write model class code
					model_class_varchar_attribute.append(
							"\r\n@Column(name = \"" + upperCase + "\")" + "\r\nprivate String \t" + lowerCase + ";");
					model_class_getter_setter_attribute.append("\r\npublic String get" + firstUpper + "() {\r\n"
							+ "\nreturn\t" + lowerCase + ";\r\n}" + "\r\npublic void set" + firstUpper + "(String\t"
							+ lowerCase + ")\n{" + "\nthis." + lowerCase + "=" + lowerCase + ";" + "\r\n}");

					// write BACK-END service class code
					service_impl_class_get_set_attribute.append(
							"\r\n" + table_name_lower + ".set" + firstUpper + "(value.get" + firstUpper + "());");

					// write FRONT-END model class code
					ng_header_model_ts_attribute.append("\r\n" + " public\t" + lowerCase + ": string;");

					// write FRONT-END ADD form component.html code
					ng_add_form_component_html_varchar_attribute
							.append("        <mat-form-field class=\"example-full-width\">\r\n"
									+ "          <mat-label>" + field_name + "</mat-label>\r\n"
									+ "          <input matInput type=\"text\" formControlName=\"" + lowerCase
									+ "\" placeholder=\"" + firstUpper + "\">\r\n"
									+ "        </mat-form-field>");
					// write FRONT-END ADD form component.ts code
					ng_add_form_component_ts_validation_var.append(lowerCase + ": [null, Validators.required],\r\n");

					// write FRONT-END EDIT FORM component.html code
					ng_edit_form_component_html_datatype_attribute
							.append("						<mat-form-field class=\"example-full-width\">\r\n" + 
									"							<mat-label>" + field_name + "</mat-label>\r\n" + 
									"							<input matInput [(ngModel)]=\"" + ng_model_ts_name_lower + "." + lowerCase + "\" name=\"" + lowerCase + "\" type=\"text\">\r\n" + 
									"						</mat-form-field>\r\n");

					// write FRONT-END DETAILS FORM component.html code
					ng_read_only_form_component_html_datatype_th.append("<th>" + field_name.toUpperCase() + "</th>\r\n");
			
					ng_read_only_form_component_html_datatype_td.append("<td>{{ " + ng_model_ts_name_lower + "." + lowerCase + " }}</td>\r\n");

					
					// write FRONT-END ALL (GRID-VIEW) component.html code
					ng_all_grid_view_component_html_datatype_attribute.append("<ng-container matColumnDef=\""
							+ lowerCase + "\">\r\n" + "      <th mat-header-cell *matHeaderCellDef mat-sort-header>"
							+ field_name + "</th>\r\n" + "      <td mat-cell *matCellDef=\"let element\"> {{element."
							+ lowerCase + "}} </td>\r\n" + "    </ng-container>");
					// write front-end ALL (GRID-VIEW) component.ts code
					ng_all_grid_view_component_ts_display_columns.append("'" + lowerCase + "', ");
					ng_all_grid_view_component_ts_search_string.append(ng_model_ts_name_lower + "." + lowerCase + "+");
					// sort string
					ng_all_grid_view_component_ts_sort_string.append("case\t'" + lowerCase
							+ "': [propertyA, propertyB] = [a." + lowerCase + ", b." + lowerCase + "]; break;\r\n");
				
					// ui_name.properties file CODE
					properties_file_fields_code.append(lowerCase + ", ");
				}
			}

			// GET LONGTEXT TYPE VALUE
			for (int i = 0; i < longtextList.size(); i++) {
				String field_name = longtextList.get(i).getFieldName(); // label for html
				String mapping = longtextList.get(i).getMapping();
				String dataType = longtextList.get(i).getDataType();
				String lowerCase = mapping.toLowerCase();
				String upperCase = mapping.toUpperCase();
				String firstUpper = RealNetUtils.toFirstUpperCase(mapping);

				if (dataType.equalsIgnoreCase("LONGTEXT")) {

					// write BACK-END model class code
					model_class_longtext_attribute.append(
							"\r\n@Column(name = \"" + upperCase + "\")" + "\r\nprivate String\t" + lowerCase + ";");
					model_class_getter_setter_attribute
							.append("\r\npublic String get" + firstUpper + "(){" + "\r\nreturn\t" + lowerCase + ";"
									+ "\r\n }" + "\r\npublic void set" + firstUpper + "(String\t" + lowerCase + ")"
									+ "\n{" + "\nthis." + lowerCase + " = " + lowerCase + ";" + "\n}");

					// write BACK-END service code
					service_impl_class_get_set_attribute.append(
							"\r\n" + table_name_lower + ".set" + firstUpper + "(value.get" + firstUpper + "());");

					// write FRONT-END model class code
					ng_header_model_ts_attribute.append("\r\n" + " public\t" + lowerCase + ": string;");

					// write FRONT-END ADD form component.html code
					ng_add_form_component_html_longtext_attribute
							.append("        <mat-form-field class=\"example-full-width\">\r\n"
									+ "          <mat-label>" + field_name + "</mat-label>\r\n"
									+ "          <input matInput type=\"text\" formControlName=\"" + lowerCase
									+ "\" placeholder=\"" + firstUpper + "\" required>\r\n"
									+ "        </mat-form-field>");
					// write FRONT-END ADD form component.ts code
					ng_add_form_component_ts_validation_var.append(lowerCase + ": [null, Validators.required],\r\n");

					// write FRONT-END EDIT FORM component.html code
					ng_edit_form_component_html_datatype_attribute
							.append("						<mat-form-field class=\"example-full-width\">\r\n" + 
									"							<mat-label>" + field_name + "</mat-label>\r\n" + 
									"							<input matInput [(ngModel)]=\"" + ng_model_ts_name_lower + "." + lowerCase + "\" name=\"" + lowerCase + "\" type=\"text\">\r\n" + 
									"						</mat-form-field>\r\n");

					// write FRONT-END DETAILS FORM component.html code
					ng_read_only_form_component_html_datatype_th.append("<th>" + field_name.toUpperCase() + "</th>\r\n");
			
					ng_read_only_form_component_html_datatype_td.append("<td>{{ " + ng_model_ts_name_lower + "." + lowerCase + " }}</td>\r\n");


					// write FRONT-END ALL (GRID-VIEW) component.html code
					ng_all_grid_view_component_html_datatype_attribute.append("<ng-container matColumnDef=\""
							+ lowerCase + "\">\r\n" + "      <th mat-header-cell *matHeaderCellDef mat-sort-header>"
							+ field_name + "</th>\r\n" + "      <td mat-cell *matCellDef=\"let element\"> {{element."
							+ lowerCase + "}} </td>\r\n" + "    </ng-container>");
					// write front-end ALL (GRID-VIEW) component.ts code
					ng_all_grid_view_component_ts_display_columns.append("'" + lowerCase + "', ");
					ng_all_grid_view_component_ts_search_string.append(ng_model_ts_name_lower + "." + lowerCase + "+");
					// sort string
					ng_all_grid_view_component_ts_sort_string.append("case\t'" + lowerCase
							+ "': [propertyA, propertyB] = [a." + lowerCase + ", b." + lowerCase + "]; break;\r\n");
				
					// ui_name.properties file CODE
					properties_file_fields_code.append(lowerCase + ", ");
				}
			}

			// BUTTON
			for (int i = 0; i < buttonList.size(); i++) {
				String field_name = buttonList.get(i).getFieldName(); // label for html
				String type_field = buttonList.get(i).getType_field();

				if (type_field.equalsIgnoreCase("BUTTON")) {
					ng_add_form_component_html_button_attribute
							.append("			<button mat-raised-button color=\"primary\" type=\"submit\" [disabled]=\"!"
									+ ng_model_ts_name_lower + "Form.valid\">" + field_name + "</button>\r\n");
				}
			}

			// FLEX ATTRIBUTE VALUES: [ATTRIBUTE 1-15, FLEX 1-5]
			for (int i = 0; i < attribute_flex_values.size(); i++) {
				int count = 1;
				String lookupCode = attribute_flex_values.get(i).getLookupCode();
				String upperCase = lookupCode.toUpperCase();
				String lowerCase = lookupCode.toLowerCase();
				String firstUpper = RealNetUtils.toFirstUpperCase(lookupCode);

				// write BACK-END model class code here
				model_class_flex_attribute.append(
						"\r\n@Column(name = \"" + upperCase + "\")" + "\r\nprivate String \t" + lowerCase + ";");
				model_class_getter_setter_attribute.append("\r\npublic String get" + firstUpper + "() {\r\n"
						+ "\nreturn\t" + lowerCase + ";\r\n}" + "\r\npublic void set" + firstUpper + "(String\t"
						+ lowerCase + ")\n{" + "\nthis." + lowerCase + "=" + lowerCase + ";" + "\r\n}");

				// write BACK-END service class code here
				service_impl_class_get_set_attribute
						.append("\r\n" + table_name_lower + ".set" + firstUpper + "(value.get" + firstUpper + "());");

				// write ANGULAR model class code
				ng_header_model_ts_attribute.append("\r\n" + " public\t" + lowerCase + ": string;");

				// write front-end ADD form component.ts code
				ng_add_form_component_ts_validation_var.append(lowerCase + ": [null],\r\n");

				// write EXTENSION ADD form component.html code
				ng_extension_add_form_component_html_flex_attribute
						.append("    <mat-form-field class=\"example-full-width\">\r\n"
								+ "        <mat-label>Extension label " + (count + i) + "</mat-label>\r\n"
								+ "        <input matInput type=\"text\" formControlName=\"" + lowerCase
								+ "\" placeholder=\"" + firstUpper + "\">\r\n" + "    </mat-form-field>\r\n");

				// write FRONT-END ALL (GRID-VIEW) component.html code
				ng_all_grid_view_component_html_datatype_attribute.append("<ng-container matColumnDef=\"" + lowerCase
						+ "\">\r\n" + "      <th mat-header-cell *matHeaderCellDef mat-sort-header>" + firstUpper
						+ "</th>\r\n" + "      <td mat-cell *matCellDef=\"let element\"> {{element." + lowerCase
						+ "}} </td>\r\n" + "    </ng-container>");

				// write front-end ALL (GRID-VIEW) component.ts code
				ng_all_grid_view_component_ts_display_columns.append("'" + lowerCase + "', ");
				ng_all_grid_view_component_ts_search_string.append(ng_model_ts_name_lower + "." + lowerCase + "+");

				// sort string
				ng_all_grid_view_component_ts_sort_string.append("case\t'" + lowerCase
						+ "': [propertyA, propertyB] = [a." + lowerCase + ", b." + lowerCase + "]; break;\r\n");

				// write front-end EDIT FORM component.html code
				ng_edit_form_component_html_flex_attribute
						.append("						<mat-form-field class=\"example-full-width\">\r\n" + 
								"							<mat-label>" + firstUpper + "</mat-label>\r\n" + 
								"							<input matInput [(ngModel)]=\"" + ng_model_ts_name_lower + "." + lowerCase + "\" name=\"" + lowerCase + "\" type=\"text\">\r\n" + 
								"						</mat-form-field>\r\n");

				// write FRONT-END DETAILS FORM component.html code
				ng_read_only_form_component_html_datatype_th.append("<th *ngIf=\"" + ng_model_ts_name_lower + "." + lowerCase + " !==null && " + ng_model_ts_name_lower + "." + lowerCase + " !== ''\">" + upperCase + "</th>\r\n");
		
				ng_read_only_form_component_html_datatype_td.append("<td *ngIf=\"" + ng_model_ts_name_lower + "." + lowerCase + " !==null && " + ng_model_ts_name_lower + "." + lowerCase + " !== ''\">{{ " + ng_model_ts_name_lower + "." + lowerCase + " }}</td>\r\n");

				// ui_name.properties file CODE
				properties_file_fields_code.append(lowerCase + ", ");
			}
			
			// BACK-END HEADER-MODEL CLASS STATIC DEPENDENCY IMPORTS
			model_class_imports.append("package com.realnet.model;\r\n" + "import java.util.Date;\r\n" + "\r\n"
					+ "import java.util.List;\r\n" + "\r\n" + "import javax.persistence.Column;\r\n"
					+ "import javax.persistence.Entity;\r\n" + "import javax.persistence.GeneratedValue;\r\n"
					+ "import javax.persistence.GenerationType;\r\n" + "import javax.persistence.Id;\r\n"
					+ "import javax.persistence.Table;\r\n");
		}
		// ================ HEADER-LINE CODE =====================//
		if (form_type.equalsIgnoreCase("HEADER_LINE")) {
			// LINE-TABLE PK
			for (int i = 0; i < line_pk_value.size(); i++) {
				//String field_name = line_pk_value.get(i).getField_name();
				String mapping = line_pk_value.get(i).getMapping();
				String lowerCase = mapping.toLowerCase();
				String upperCase = mapping.toUpperCase();
				String firstUpper = RealNetUtils.toFirstUpperCase(mapping);
				System.out.println("line_model_class PK name = " + firstUpper);

				// BACK-END HL LINE-MODEL CLASS CODE
				line_model_class_datatype_variables
						.append("@Id\r\n" + "@GeneratedValue(strategy = GenerationType.IDENTITY)\r\n"
								+ "@Column(name = \"" + upperCase + "\")\r\n" + "private int " + lowerCase + ";\r\n");
				
				line_model_class_getter_setter_attribute.append("\r\n" 
						+ "public int get" + firstUpper + "() {\r\n"
						+ "	return\t" + lowerCase + ";\r\n" + "}\r\n"
						+ "public void set" + firstUpper + "(int\t" + lowerCase + ") {\r\n" 
						+ "	this." + lowerCase + "=" + lowerCase + ";\r\n" + "}\r\n");
				
				// FRONT-END HL LINE-MODEL.TS CODE
				ng_line_model_ts_attribute.append("\r\n" + " public\t" + lowerCase + ": number;\r\n");
				
				// FRONT-END HL DETAILS FORM component.html code
				ng_read_only_form_component_html_line_datatype_th.append("<th>" + upperCase + "</th>\r\n");
		
				ng_read_only_form_component_html_line_datatype_td.append("<td>{{ lines_value." + lowerCase + " }}</td>\r\n");
				
				/*--------------------HL LINE PART-----------------------*/
				
				// FRONT-END HL LINE-ALL (GRID-VIEW) component.HTML CODE
				ng_line_all_grid_view_component_html_datatype_attribute.append(
						"    <!------------- Select Column ---------->\r\n" +
						"    <ng-container matColumnDef=\"" + lowerCase + "\">\r\n" + 
						"      <th mat-header-cell *matHeaderCellDef mat-sort-header> select </th>\r\n" + 
						"      <tr mat-cell *matCellDef=\"let row\">\r\n" + 
						"        <button mat-icon-button color=\"accent\" (click)=\"gotoUpdate(row." + lowerCase + ")\">\r\n" + 
						"          <mat-icon aria-label=\"edit\" [ngStyle]=\"{ color: 'green' }\">edit</mat-icon>\r\n" + 
						"        </button>\r\n" + 
						"        <button mat-icon-button color=\"accent\" (click)=\"delete(row." + lowerCase + ")\">\r\n" + 
						"          <mat-icon aria-label=\"Delete\" [ngStyle]=\"{ color: 'red' }\">delete</mat-icon>\r\n" + 
						"        </button>\r\n" + 
						"        <button mat-icon-button (click)=\"goToView(row." + lowerCase + ")\">\r\n" + 
						"          <mat-icon [ngStyle]=\"{ color: 'darkslategray' }\">visibility</mat-icon>\r\n" + 
						"        </button>\r\n" + 
						"      </tr>\r\n" + 
						"    </ng-container>\r\n");
				
				// FRONT-END HL LINE-ALL (GRID-VIEW) component.TS CODE
				ng_line_all_grid_view_component_ts_display_columns.append("'" + lowerCase + "', ");
				// filter
				ng_line_all_grid_view_component_ts_search_string.append(ng_line_model_ts_name_lower + "." + lowerCase + " +\r\n");
				// sort
				ng_line_all_grid_view_component_ts_sort_string.append("case\t'" + lowerCase
						+ "': [propertyA, propertyB] = [a." + lowerCase + ", b." + lowerCase + "]; break;\r\n");
				
				// FRONT-END HL LINE-DETAILS component.HTML CODE
				ng_line_read_only_form_component_html_table_th.append("<th>" + upperCase + "</th>\r\n");
				ng_line_read_only_form_component_html_table_td.append("<td>{{ " + ng_line_model_ts_name_lower + "." + lowerCase + " }}</td>\r\n");
				
				// ui_name.properties file CODE
				properties_file_fields_code.append(lowerCase + ", ");
				
				
			}
			// LINE-TABLE VARCHAR || DATE
			for (int i = 0; i < line_varcharList.size(); i++) {
				String field_name = line_varcharList.get(i).getFieldName();
				String mapping = line_varcharList.get(i).getMapping();
				String lowerCase = mapping.toLowerCase();
				String upperCase = mapping.toUpperCase();
				String firstUpper = RealNetUtils.toFirstUpperCase(mapping);

				String dataType = line_varcharList.get(i).getDataType();

				if (dataType.equals("datetime")) {
					line_model_class_datatype_variables.append(
							"\n@Column(name = \"" + upperCase + "\")" + "\r\nprivate Date \t" + lowerCase + ";\r\n");

					line_model_class_getter_setter_attribute.append("\r\n" 
							+ "public Date get" + firstUpper + "() {"
							+ "\r\nreturn\t" + lowerCase + ";\n}" 
							+ "\r\npublic void set" + firstUpper + "(Date\t" + lowerCase + ") {" 
							+ "\r\nthis." + lowerCase + " = " + lowerCase + ";" + "\r\n}");
					
					// line controller class PUT method variable
					line_controller_class_update_method_variable.append(line_table_name_lower + ".set" + firstUpper + "(" + line_table_name_lower + "Request.get" + firstUpper + "());\r\n");
					
					// FRONT-END HL LINE-MODEL.TS CODE
					ng_line_model_ts_attribute.append("\r\n" + " public\t" + lowerCase + ": Date;");

					// FRONT-END HL add-form.HTML code
					ng_add_form_component_html_line_table_th.append("<th style=\"white-space: nowrap;padding-right: 4em;float:center\">" + field_name + "</th>\r\n");
					ng_add_form_component_html_line_table_td.append("<td> <input formControlName=\"" + lowerCase + "\" class=\"form-control reset\"></td>\r\n");
					
					// FRONT-END HL add-form.TS code
					ng_add_form_component_ts_formArray_validation_variables.append(lowerCase + ": [null],\r\n");
					
					// FRONT-END HL DETAILS FORM component.html code
					ng_read_only_form_component_html_line_datatype_th.append("<th>" + upperCase + "</th>\r\n");
					ng_read_only_form_component_html_line_datatype_td.append("<td>{{ lines_value." + lowerCase + " }}</td>\r\n");
					
					/*--------------------------HL LINE PART---------------------------*/
					
					// FRONT-END HL LINE add-form.HTML code
					ng_line_add_form_component_html_datatype_variables.append("    <mat-form-field class=\"example-full-width\">\r\n" + 
							"      <mat-label>" + field_name + "</mat-label>\r\n" + 
							"      <input matInput type=\"date\" formControlName=\"" + lowerCase + "\" placeholder=\"" + lowerCase + "\">\r\n" + 
							"    </mat-form-field>\r\n");
					
					// FRONT-END HL LINE ADD form component.ts code
					ng_line_add_form_component_ts_validation_var.append(lowerCase + ": [null],\r\n");
					
					// FRONT-END HL LINE-ALL (GRID-VIEW) component.HTML CODE
					ng_line_all_grid_view_component_html_datatype_attribute.append("    <ng-container matColumnDef=\"" + lowerCase + "\">\r\n" + 
							"      <th mat-header-cell *matHeaderCellDef mat-sort-header>" + field_name + "</th>\r\n" + 
							"      <td mat-cell *matCellDef=\"let row\"> {{ row." + lowerCase + " }} </td>\r\n" + 
							"    </ng-container>");
					
					// FRONT-END HL LINE-ALL (GRID-VIEW) component.TS CODE
					ng_line_all_grid_view_component_ts_display_columns.append("'" + lowerCase + "', ");
					// filter
					ng_line_all_grid_view_component_ts_search_string.append(ng_line_model_ts_name_lower + "." + lowerCase + " +\r\n");
					// sort
					ng_line_all_grid_view_component_ts_sort_string.append("case\t'" + lowerCase
							+ "': [propertyA, propertyB] = [a." + lowerCase + ", b." + lowerCase + "]; break;\r\n");
					
					// FRONT-END HL LINE-EDIT FORM component.HTML CODE
					ng_line_edit_form_component_html_datatype_attribute.append(
							"                        <mat-form-field class=\"example-full-width\">\r\n" + 
							"                            <mat-label>" + field_name + "</mat-label>\r\n" + 
							"                            <input matInput [(ngModel)]=\"" + ng_line_model_ts_name_lower + "." + lowerCase + "\" name=\"" + lowerCase + "\" type=\"date\">\r\n" + 
							"                        </mat-form-field>\r\n");
					
					// FRONT-END HL LINE-DETAILS component.HTML CODE
					ng_line_read_only_form_component_html_table_th.append("<th>" + upperCase + "</th>\r\n");
					ng_line_read_only_form_component_html_table_td.append("<td>{{ " + ng_line_model_ts_name_lower + "." + lowerCase + " }}</td>\r\n");
					
					
					
				}
				if (!dataType.equals("datetime")) {
					line_model_class_datatype_variables.append(
							"\n@Column(name = \"" + upperCase + "\")" + "\r\nprivate Date \t" + lowerCase + ";\r\n");

					line_model_class_getter_setter_attribute.append("\r\npublic Date get" + firstUpper + "() {\r\n"
							+ "return\t" + lowerCase + ";" 
							+ "\r\n}" 
							+ "\r\n" 
							+ "public void set" + firstUpper + "(Date " + lowerCase + ") {\r\n" 
							+ "this." + lowerCase + "=" + lowerCase + ";" 
							+ "\r\n}\r\n");
					// line controller class PUT method variable
					line_controller_class_update_method_variable.append(line_table_name_lower + ".set" + firstUpper + "(" + line_table_name_lower + "Request.get" + firstUpper + "());\r\n");
					
					// FRONT-END LINE-MODEL.TS CODE
					ng_line_model_ts_attribute.append("\r\n" + " public\t" + lowerCase + ": Date;");
					
					// FRONT-END HL add-form.HTML code
					ng_add_form_component_html_line_table_th.append("<th style=\"white-space: nowrap;padding-right: 4em;float:center\">" + field_name + "</th>\r\n");
					ng_add_form_component_html_line_table_td.append("<td> <input formControlName=\"" + lowerCase + "\" class=\"form-control reset\"></td>\r\n");
					
					// FRONT-END HL add-form.TS code
					ng_add_form_component_ts_formArray_validation_variables.append(lowerCase + ": [null],\r\n");
					
					// FRONT-END HL DETAILS FORM component.html code
					ng_read_only_form_component_html_line_datatype_th.append("<th>" + upperCase + "</th>\r\n");
			
					ng_read_only_form_component_html_line_datatype_td.append("<td>{{ lines_value." + lowerCase + " }}</td>\r\n");
					
					/*-----------------------------HL LINE PART-------------------------------*/
					
					// FRONT-END HL LINE-ADD FORM component.HTML CODE
					ng_line_add_form_component_html_datatype_variables.append("    <mat-form-field class=\"example-full-width\">\r\n" + 
							"      <mat-label>" + field_name + "</mat-label>\r\n" + 
							"      <input matInput type=\"date\" formControlName=\"" + lowerCase + "\" placeholder=\"" + lowerCase + "\">\r\n" + 
							"    </mat-form-field>\r\n");
					// FRONT-END HL LINE ADD form component.ts code
					ng_line_add_form_component_ts_validation_var.append(lowerCase + ": [null],\r\n");
					
					// FRONT-END HL LINE-ALL (GRID-VIEW) component.HTML CODE
					ng_line_all_grid_view_component_html_datatype_attribute.append("    <ng-container matColumnDef=\"" + lowerCase + "\">\r\n" + 
							"      <th mat-header-cell *matHeaderCellDef mat-sort-header>" + field_name + "</th>\r\n" + 
							"      <td mat-cell *matCellDef=\"let row\"> {{ row." + lowerCase + " }} </td>\r\n" + 
							"    </ng-container>");
					
					// FRONT-END HL LINE-ALL (GRID-VIEW) component.TS CODE
					ng_line_all_grid_view_component_ts_display_columns.append("'" + lowerCase + "', ");
					// filter
					ng_line_all_grid_view_component_ts_search_string.append(ng_line_model_ts_name_lower + "." + lowerCase + " +\r\n");
					// sort
					ng_line_all_grid_view_component_ts_sort_string.append("case\t'" + lowerCase
							+ "': [propertyA, propertyB] = [a." + lowerCase + ", b." + lowerCase + "]; break;\r\n");
					
					// FRONT-END HL LINE-EDIT FORM component.HTML CODE
					ng_line_edit_form_component_html_datatype_attribute.append(
							"                        <mat-form-field class=\"example-full-width\">\r\n" + 
							"                            <mat-label>" + field_name + "</mat-label>\r\n" + 
							"                            <input matInput [(ngModel)]=\"" + ng_line_model_ts_name_lower + "." + lowerCase + "\" name=\"" + lowerCase + "\" type=\"text\">\r\n" + 
							"                        </mat-form-field>\r\n");
					
					// FRONT-END HL LINE-DETAILS component.HTML CODE
					ng_line_read_only_form_component_html_table_th.append("<th>" + upperCase + "</th>\r\n");
					ng_line_read_only_form_component_html_table_td.append("<td>{{ " + ng_line_model_ts_name_lower + "." + lowerCase + " }}</td>\r\n");
					
					// ui_name.properties file CODE
					properties_file_fields_code.append(lowerCase + ", ");
					
					// ui_name.properties file CODE
					properties_file_fields_code.append(lowerCase + ", ");

				} // END IF
			} // END FOR LOOP

			// H-L FLEX ATTRIBUTE VALUES: [ATTRIBUTE 1-15, FLEX 1-5]
			for (int i = 0; i < attribute_flex_values.size(); i++) {
				//int count = 1;
				String lookupCode = attribute_flex_values.get(i).getLookupCode();
				String upperCase = lookupCode.toUpperCase();
				String lowerCase = lookupCode.toLowerCase();
				String firstUpper =RealNetUtils.toFirstUpperCase(lookupCode);

				line_model_class_flex_attribute.append(
						"\r\n@Column(name = \"" + upperCase + "\")" + "\r\nprivate String \t" + lowerCase + ";");

				line_model_class_getter_setter_attribute.append("\r\npublic String get" + firstUpper + "() {\r\n"
						+ "return\t" + lowerCase + ";\r\n}"
						+ "\r\npublic void set" + firstUpper + "(String\t" + lowerCase + ") {\r\n"
						+ "this." + lowerCase + " = " + lowerCase + ";" 
						+ "\r\n}\r\n");
				
				// line controller class PUT method variable
				line_controller_class_update_method_variable.append(line_table_name_lower + ".set" + firstUpper + "(" + line_table_name_lower + "Request.get" + firstUpper + "());\r\n");
				
				// FRONT-END LINE-MODEL.TS CODE
				ng_line_model_ts_attribute.append("\r\n" + " public\t" + lowerCase + ": string;");
				
				// FRONT-END HL ADD-FORM component.HTML code
				// HEADER-LINE EXENSION
				ng_add_form_component_html_line_table_attribute_flex_th.append("<th style=\"white-space: nowrap;padding-right: 4em;float:center\">" + firstUpper + "</th>\r\n");
				ng_add_form_component_html_line_table_attribute_flex_td.append("<td> <input formControlName=\"" + lowerCase + "\" class=\"form-control reset\"></td>\r\n");
				
				// FRONT-END HL ADD-FORM component.TS code
				ng_add_form_component_ts_formArray_validation_variables.append(lowerCase + ": [null],\r\n");
				
				// FRONT-END HL DETAILS-FORM component.html code
				ng_read_only_form_component_html_line_datatype_th.append("<th *ngIf=\"" + ng_model_ts_name_lower + "." + line_table_name_lower + "_values[0]." + lowerCase + " !==null && " + ng_model_ts_name_lower + "." + line_table_name_lower + "_values[0]." + lowerCase + " !== ''\">" + upperCase + "</th>\r\n");
		
				ng_read_only_form_component_html_line_datatype_td.append("<td *ngIf=\"" + ng_model_ts_name_lower + "." + line_table_name_lower + "_values[0]." + lowerCase + " !==null && " + ng_model_ts_name_lower + "." + line_table_name_lower + "_values[0]." + lowerCase + " !==''\">{{ lines_value." + lowerCase + " }}</td>\r\n");
				
				/*----------------------------------HL LINE PART-------------------------------*/
				// FRONT-END HL LINE ADD form component.ts code
				ng_line_add_form_component_ts_validation_var.append(lowerCase + ": [null],\r\n");
				
				// FRONT-END HL LINE-ALL (GRID-VIEW) component.HTML CODE
				ng_line_all_grid_view_component_html_datatype_attribute.append("    <ng-container matColumnDef=\"" + lowerCase + "\">\r\n" + 
						"      <th mat-header-cell *matHeaderCellDef mat-sort-header>" + firstUpper + "</th>\r\n" + 
						"      <td mat-cell *matCellDef=\"let row\"> {{ row." + lowerCase + " }} </td>\r\n" + 
						"    </ng-container>");
				
				// FRONT-END HL LINE-ALL (GRID-VIEW) component.TS CODE
				ng_line_all_grid_view_component_ts_display_columns.append("'" + lowerCase + "', ");
				// search
				ng_line_all_grid_view_component_ts_search_string.append(ng_line_model_ts_name_lower + "." + lowerCase + " +");
				// sort
				ng_line_all_grid_view_component_ts_sort_string.append("case\t'" + lowerCase
						+ "': [propertyA, propertyB] = [a." + lowerCase + ", b." + lowerCase + "]; break;\r\n");
				
				// FRONT-END HL LINE-EDIT FORM component.HTML CODE
				ng_line_edit_form_component_html_flex_attribute.append(
						"                        <mat-form-field class=\"example-full-width\">\r\n" + 
						"                            <mat-label>" + firstUpper + "</mat-label>\r\n" + 
						"                            <input matInput [(ngModel)]=\"" + ng_line_model_ts_name_lower + "." + lowerCase + "\" name=\"" + lowerCase + "\" type=\"text\">\r\n" + 
						"                        </mat-form-field>\r\n");
				
				// FRONT-END HL LINE-DETAILS component.HTML CODE
				ng_line_read_only_form_component_html_table_th.append("        <th *ngIf=\"" + ng_line_model_ts_name_lower + "." + lowerCase + " !==null && " + ng_line_model_ts_name_lower + "." + lowerCase + " !== ''\">" + lowerCase + "</th>\r\n");
				ng_line_read_only_form_component_html_table_td.append("        <td *ngIf=\"" + ng_line_model_ts_name_lower + "." + lowerCase + " !==null && " + ng_line_model_ts_name_lower + "." + lowerCase + " !== ''\">{{ " + ng_line_model_ts_name_lower + "." + lowerCase + "}}</td>\r\n");
				
				// FRONT-END HL LINE-ADD EXTENSION component.HTML CODE
				ng_line_extension_add_form_component_html_flex_attribute.append(
						"    <mat-form-field class=\"example-full-width\">\r\n" + 
						"        <mat-label>" + firstUpper + "</mat-label>\r\n" + 
						"        <input matInput type=\"text\" formControlName=\"" + lowerCase + "\" placeholder=\"" + firstUpper + "\">\r\n" + 
						"    </mat-form-field>\r\n");
				
				// ui_name.properties file CODE
				properties_file_fields_code.append(lowerCase + ", ");
				
			} // END LINE ATTRIBUTE-FLEX FOR LOOP

			model_class_imports.append("import javax.persistence.CascadeType;\r\n" 
					+ "import javax.persistence.OneToMany;\r\n" 
					+ "\r\n"
					+ "import com.fasterxml.jackson.annotation.JsonManagedReference;\r\n");

			header_model_class_line_dependency.append("@OneToMany(mappedBy = \"" + table_name_lower
					+ "\", orphanRemoval = true, cascade = CascadeType.PERSIST)\r\n" 
					+ "	@JsonManagedReference\r\n" 
					+ "	private List<" + line_table_name_first_upper + ">\t" + line_table_name_lower + "_values;");

			model_class_getter_setter_attribute.append(
					"public List<" + line_table_name_first_upper + "> get" + line_table_name_first_upper + "_values() {\r\n" 
					+ "		return\t" + line_table_name_lower + "_values;\r\n"
					+ "	}\r\n" + "\r\n" + "	public void set" + line_table_name_first_upper + "_values(List<"
					+ line_table_name_first_upper + ">\t" + line_table_name_lower + "_values) {\r\n" + "		this."
					+ line_table_name_lower + "_values = " + line_table_name_lower + "_values;\r\n" + "	}\r\n");

			line_model_class_getter_setter_attribute.append("public\t" + table_name_first_upper + "\tget"
					+ table_name_first_upper + "() {\r\n" 
					+ "		return\t" + table_name_lower + ";\r\n"
					+ "	}\r\n" 
					+ "\r\n" 
					+ "	public void set" + table_name_first_upper + "(" + table_name_first_upper
					+ " " + table_name_lower + ") {\r\n" 
					+ "		this." + table_name_lower + " = " + table_name_lower
					+ ";\r\n" 
					+ "	}\r\n");

			// ============ HEADER-LINE FRONT-END CODE =================//

			
			//FRONT-END H-L MODULE.TS LINE DEPENDENCY code
			ng_module_ts_line_imports.append(
					"import {" + ng_line_all_grid_view_component_name + "} from './" + ng_line_folder_name + "/all/all-" + ng_line_file_name + ".component';\r\n" 
					+ "import {" + ng_line_add_form_component_name + "} from './" + ng_line_folder_name + "/add/add-" + ng_line_file_name + ".component';\r\n" 
					+ "import {" + ng_line_edit_component_name + "} from './" + ng_line_folder_name + "/edit/edit-" + ng_line_file_name + ".component';\r\n" 
					+ "import {" + ng_line_read_only_component_name + "} from './" + ng_line_folder_name + "/details/" + ng_line_file_name + "-details.component';\r\n" 
					+ "import {" + ng_line_extension_add_component_name + "} from './" + ng_line_folder_name + "/extLinePages/ext-add-" + ng_line_file_name + ".component';\r\n");
			
			//FRONT-END H-L ROUTING.MODULE.TS LINE DEPENDENCY code
			ng_routing_ts_line_imports.append(
					  "import {" + ng_line_all_grid_view_component_name + "} from './" + ng_line_folder_name + "/all/all-" + ng_line_file_name + ".component';\r\n" 
					+ "import {" + ng_line_add_form_component_name + "} from './" + ng_line_folder_name + "/add/add-" + ng_line_file_name + ".component';\r\n" 
					+ "import {" + ng_line_edit_component_name + "} from './" + ng_line_folder_name + "/edit/edit-" + ng_line_file_name + ".component';\r\n" 
					+ "import {" + ng_line_read_only_component_name + "} from './" + ng_line_folder_name + "/details/" + ng_line_file_name + "-details.component';\r\n" 
					+ "\r\n");
			
			ng_routing_ts_header_line_routing_paths.append(
							  "		{ path: '', component:\t" + ng_component_ts_name + ",\r\n" 
							+ "			children: [\r\n" 
							+ "         	{ path: '', redirectTo: '" + ng_path_name + "' },\r\n" 
							+ "          	{ path: '" + ng_path_name + "', component:\t" + ng_all_grid_view_component_name + "},\r\n" 
							+ "          	{ path: 'add', component:\t" + ng_add_form_component_name + " },\r\n" 
							+ "          	{ path:'details/:id', component:\t" + ng_read_only_component_name + "},\r\n"
							+ "          	{ path: 'update/:id', component:\t" + ng_edit_component_name + ",\r\n"
							+ "					children: [\r\n"
							+ "						{ path: \"\", redirectTo: \"all-line\" },\r\n"
							+ "						{ path: \"all-line\", component: " + ng_line_all_grid_view_component_name + " },\r\n"
							+ "						{ path: \"add-line\", component: " + ng_line_add_form_component_name + " },\r\n"
							+ "						{ path: \"edit-line/:l_id\", component: " + ng_line_edit_component_name + " },\r\n"
							+ "						{ path: \"line-details/:l_id\", component: " + ng_line_read_only_component_name + " },\r\n"
							+ "					]\r\n"
							+ "			 	},\r\n"
							+ "			]\r\n"
							+ "		}\r\n");


			// FRONT-END HEADER-LINE  HEADER CLASS LINE DEPENDENCY code
			ng_header_model_ts_line_imports.append("import { " + ng_line_model_ts_name + " } from \"./" + ng_line_model_ts_name + "\";\r\n");
			ng_header_model_ts_array_variable.append("\r\npublic\t" + ng_line_model_ts_name_lower + "_values: " + ng_line_model_ts_name + "[];\r\n");
			
			// FRONT-END HEADER-LINE (LINE PART) service.ts code
			ng_service_ts_line_imports.append("import { " + ng_line_model_ts_name + " } from './"+ ng_line_model_ts_name + "';\r\n");

			ng_service_ts_line_methods.append("\r\n"
					+ "/*==========================LINE PART SERVICE METHODS=================================*/\r\n"
					+ "lineDataChange: BehaviorSubject<" + ng_line_model_ts_name + "[]> = new BehaviorSubject<\r\n" + 
					"    " + ng_line_model_ts_name + "[]\r\n" + 
					"  >([]);\r\n" + 
					"\r\n" + 
					"  get line_data(): " + ng_line_model_ts_name + "[] {\r\n" + 
					"    return this.lineDataChange.value;\r\n" + 
					"  }\r\n"
					+ "\r\n"
					+ "  getLinesByHeaderId(headerId: number): any{\r\n" + 
					"    return this.http.get<" + ng_line_model_ts_name + "[]>(this.API_URL + \"/\" + headerId + \"/" + line_table_name_lower + "\")\r\n" + 
					"      .subscribe(data => {\r\n" + 
					"        this.lineDataChange.next(data);\r\n" + 
					"      },(error: HttpErrorResponse) => {\r\n" + 
					"        console.log(error.name + \" \" + error.message);\r\n" + 
					"      });\r\n" + 
					"  }\r\n" + 
					"\r\n" + 
					" 	getLineByHeaderIdAndLineId(headerId: number, lineId: number): Observable<any> {\r\n" + 
					"    	return this.http\r\n" + 
					"      		.get<" + ng_line_model_ts_name + "[]>(this.API_URL + \"/\" + headerId + \"/" + line_table_name_lower + "/\" + lineId);\r\n" + 
					"  	}\r\n" +
					"\r\n" +
					"  createLineByHeaderId(headerId: number, " + line_table_name_lower + ": Object): Observable<any> {\r\n" + 
					"    return this.http.post(this.API_URL + '/' + headerId + '/" + line_table_name_lower + "', " + line_table_name_lower + ");\r\n" + 
					"  }\r\n" + 
					"\r\n" + 
					"  updateLineByHeaderIdAndLineId(headerId: number,lineId: number, " + line_table_name_lower + ": any): Observable<any> {\r\n" + 
					"    return this.http.put(this.API_URL + '/' + headerId + '/" + line_table_name_lower + "/' + lineId, " + line_table_name_lower + ");\r\n" + 
					"  }\r\n" + 
					"\r\n" + 
					"  deleteLineByHeaderIdAndLineId(headerId: number,lineId: number): Observable<any> {\r\n" + 
					"    return this.http.delete(this.API_URL+'/'+headerId + '/" + line_table_name_lower + "/' + lineId, {responseType: \"text\"});\r\n" + 
					"  }\r\n"
					);
			
			// FRONT-END HEADER-LINE (LINE PART) ADD FORM component.HTML
			ng_add_form_component_html_line_table_code.append( 
					"		<div class=\"row\">\r\n" + 
					"          <div class=\"col-sm-12\" formArrayName=\"" + ng_line_model_ts_name_lower + "_values\">\r\n" + 
					"            <table class=\"table-responsive\">\r\n" + 
					"              <thead>\r\n" + 
					"                <tr>\r\n" + 
					"				" + ng_add_form_component_html_line_table_th + "\r\n" + 
					"					<div *ngIf=\"lineShow\">\r\n" +
					"						" + ng_add_form_component_html_line_table_attribute_flex_th + "\r\n" +
					"					</div>\r\n" +
					"                </tr>\r\n" + 
					"              </thead>\r\n" + 
					"              <tbody>\r\n" +
					"                <tr *ngFor=\"let item of controls; let i=index\" [formGroupName]=\"i\">\r\n" +
					" 				 " + ng_add_form_component_html_line_table_td + 
					"					<div *ngIf=\"lineShow\">\r\n" +
					"						" + ng_add_form_component_html_line_table_attribute_flex_td + "\r\n" +
					"					</div>\r\n" +
					"                  <td>\r\n" +
					"                    <button mat-mini-fab *ngIf=\"controls.length > 1\" (click)=\"delete" + ng_line_model_ts_name + "_values(i)\" color=\"warn\">\r\n" + 
					"                      <mat-icon>delete</mat-icon>\r\n" + 
					"                    </button>\r\n" + 
					"                  </td>\r\n" + 
					"                </tr>\r\n" + 
					"              </tbody>\r\n" + 
					"              <tfoot>\r\n" + 
					"                <tr>\r\n" + 
					"                  <td>\r\n" + 
					"                    <button mat-icon-button type=\"button\" (click)=\"add" + ng_line_model_ts_name + "_values()\">\r\n" + 
					"                      <mat-icon>add</mat-icon>\r\n" + 
					"                    </button>\r\n" + 
					"                    </td>\r\n" + 
					"                  </tr>\r\n" + 
					"              </tfoot>\r\n" + 
					"            </table>\r\n" + 
					"          </div>  \r\n" + 
					"        </div>");
			
			// FRONT-END HEADER-LINE (LINE PART) ADD FORM component.TS
			ng_add_form_component_ts_formArray_variable.append(ng_line_model_ts_name_lower + "_values: this._fb.array([this.init" + ng_line_model_ts_name + "Form()])");
			ng_add_form_component_ts_formArray_methods.append(
					"\r\n init" + ng_line_model_ts_name + "Form() {\r\n" + 
					"    return this._fb.group({\r\n" +
					"		" + ng_add_form_component_ts_formArray_validation_variables + "\r\n" +
					"	 });\r\n" + 
					"  }\r\n" + 
					"  get controls() {\r\n" + 
					"    return (this." + ng_model_ts_name_lower + "Form.get('" + ng_line_model_ts_name_lower + "_values') as FormArray).controls;\r\n" + 
					"  }\r\n" + 
					"\r\n" + 
					"  add" + ng_line_model_ts_name + "_values() {\r\n" + 
					"    (<FormArray>this." + ng_model_ts_name_lower + "Form.get('" + ng_line_model_ts_name_lower + "_values')).push(this.init" + ng_line_model_ts_name + "Form());\r\n" + 
					"  }\r\n" + 
					"  \r\n" + 
					"  delete" + ng_line_model_ts_name + "_values(index: number) {\r\n" + 
					"    (<FormArray>this." + ng_model_ts_name_lower + "Form.get('" + ng_line_model_ts_name_lower + "_values')).removeAt(index);\r\n" + 
					"  }\r\n");
			
			// FRONT-END HEADER-LINE (LINE-PART) Edit FORM component.html
			ng_edit_form_component_html_line_code.append("<div>\r\n" + 
			"	<router-outlet></router-outlet>\r\n" + 
			"</div>");
			
			
			// FRONT-END HEADER-LINE (LINE-PART) DETAILS FORM component.html
			ng_read_only_form_component_html_line_code.append("<h4 style=\"text-align: center;\">" + ng_line_model_ts_name.toUpperCase() + "</h4>\r\n" + 
					"    <table class=\"table\" *ngIf=\"" + ng_model_ts_name_lower + "." + ng_line_model_ts_name_lower + "_values.length >=1\">\r\n" + 
					"		<tr>\r\n" +
					"			" + ng_read_only_form_component_html_line_datatype_th +
					"		</tr>\r\n" + 
					"		<tr *ngFor=\"let lines_value of " + ng_model_ts_name_lower + "." + ng_line_model_ts_name_lower + "_values\">\r\n" +
					"			" + ng_read_only_form_component_html_line_datatype_td + 
					"		</tr>\r\n" +
					"	</table>\r\n");		
			
			

		} // END IF BLOCK (HEADER-LINE)

		// REMOVING LAST + CHARACTER FROM THE STRING
		String ng_all_grid_view_component_search = ng_all_grid_view_component_ts_search_string.substring(0,
				ng_all_grid_view_component_ts_search_string.length() - 1);
		String ng_line_all_grid_view_component_search = ng_line_all_grid_view_component_ts_search_string.substring(0,ng_line_all_grid_view_component_ts_search_string.length()-1);

		// BACK-END HEADER-MODEL CLASS STATIC WHO COLUMNS
		model_class_who_columns.append("	@Column(name = \"CREATED_BY\")\r\n" + "	private int created_by;\r\n"
				+ "	@Column(name = \"LAST_UPDATED_BY\")\r\n" + "	private int last_updated_by;\r\n"
				+ "	@Column(name = \"CREATION_DATE\")\r\n" + "	private Date creation_date;\r\n"
				+ "	@Column(name = \"LAST_UPDATE_DATE\")\r\n" + "	private Date last_update_date;\r\n"
				+ "	@Column(name = \"ACCOUNT_ID\")\r\n" + "	private int account_id;\r\n" + "	\r\n"
				+ "	public int getCreated_by() {\r\n" + "		return created_by;\r\n" + "	}\r\n"
				+ "	public void setCreated_by(int created_by) {\r\n" + "		this.created_by = created_by;\r\n"
				+ "	}\r\n" + "	public int getLast_updated_by() {\r\n" + "		return last_updated_by;\r\n"
				+ "	}\r\n" + "	public void setLast_updated_by(int last_updated_by) {\r\n"
				+ "		this.last_updated_by = last_updated_by;\r\n" + "	}\r\n"
				+ "	public Date getCreation_date() {\r\n" + "		return creation_date;\r\n" + "	}\r\n"
				+ "	public void setCreation_date(Date creation_date) {\r\n"
				+ "		this.creation_date = creation_date;\r\n" + "	}\r\n"
				+ "	public Date getLast_update_date() {\r\n" + "		return last_update_date;\r\n" + "	}\r\n"
				+ "	public void setLast_update_date(Date last_update_date) {\r\n"
				+ "		this.last_update_date = last_update_date;\r\n" + "	}\r\n"
				+ "	public int getAccount_id() {\r\n" + "		return account_id;\r\n" + "	}\r\n"
				+ "	public void setAccount_id(int account_id) {\r\n" + "		this.account_id = account_id;\r\n"
				+ "	}\r\n");

		/*------BACK-END HEADER-MODEL CLASS CODE FINAL--------*/
		model.append(model_class_imports + "@Entity\r\n" + "@Table(name = \"" + table_name_upper + "\")\r\n"
				+ "public class \t" + table_name_first_upper + " {\r\n" + model_class_pk_id_attribute + "\r\n"
				+ model_class_varchar_attribute + "\r\n" + model_class_integer_attribute + "\r\n"
				+ model_class_double_attribute + "\r\n" + model_class_datetime_attribute + "\r\n"
				+ model_class_longtext_attribute + "\r\n" + header_model_class_line_dependency + "\r\n"
				+ model_class_flex_attribute + "\r\n" + model_class_getter_setter_attribute + "\r\n"
				+ model_class_who_columns
				+ "\r\n}");

		// LINE-MOEDL CLASS IMPORTS
		line_model_class_imports.append("package com.realnet.model;\r\n" + 
				"\r\n" + 
				"import javax.persistence.Column;\r\n" + 
				"import javax.persistence.Entity;\r\n" + 
				"import javax.persistence.FetchType;\r\n" + 
				"import javax.persistence.GeneratedValue;\r\n" + 
				"import javax.persistence.GenerationType;\r\n" + 
				"import javax.persistence.Id;\r\n" + 
				"import javax.persistence.JoinColumn;\r\n" + 
				"import javax.persistence.ManyToOne;\r\n" + 
				"import javax.persistence.Table;\r\n" + 
				"import java.util.Date;\r\n" + 
				"\r\n" + 
				"import com.fasterxml.jackson.annotation.JsonBackReference;\r\n" + 
				"\r\n");

		/*---------BACK-END HEADER-LINE (LINE-MODEL CLASS) CODE FINAL--------*/
		line_model.append(line_model_class_imports + "\r\n" + "@Entity\r\n" + "@Table(name = \"" + line_table_name_upper
				+ "\")\r\n" + "public class\t" + line_table_name_first_upper + " {\r\n"
				+ line_model_class_datatype_variables + "\r\n" 
				+ " @ManyToOne(fetch = FetchType.LAZY, optional = false)\r\n"
				+ " @JoinColumn(name = \"" + table_name_lower + "_id\")\r\n" 
				+ "	@JsonBackReference\r\n"
				+ " private\t" + table_name_first_upper + " " + table_name_lower + ";\r\n"
				+ "	" + line_model_class_flex_attribute + "\r\n" 
				+ "	" + line_model_class_getter_setter_attribute + "\r\n" 
				+ " " + model_class_who_columns 
				+ "\r\n}");

		// BACK-END REPOSITORY (HEADER CLASS) DEPENDENCY IMPORTS
		repository_class_imports.append("package com.realnet.repository;\r\n" + "\r\n"
				+ "import org.springframework.data.jpa.repository.JpaRepository;\r\n"
				+ "import org.springframework.stereotype.Repository;\r\n" + "\r\n" + "import com.realnet.model."
				+ table_name_first_upper + ";\r\n");

		/*------BACK-END REPOSITORY CLASS CODE FINAL--------*/
		repository.append(
				repository_class_imports + "\r\n@Repository\r\n" + "public interface\t" + repository_name_first_upper
						+ "\textends JpaRepository<" + table_name_first_upper + ", Integer>{\r\n" + "\r\n" + "}");

		// BACK-END LINE-REPOSITORY CLASS DEPENDENCY IMPORTS
		StringBuilder line_repository_class_imports = new StringBuilder();
		line_repository_class_imports.append("package com.realnet.repository;\r\n" + 
				"\r\n" + 
				"import java.util.List;\r\n" + 
				"import java.util.Optional;\r\n" + 
				"\r\n" + 
				"import org.springframework.data.jpa.repository.JpaRepository;\r\n" +
				"import org.springframework.data.jpa.repository.Query;\r\n" + 
				"import org.springframework.data.repository.query.Param;\r\n" +
				"import org.springframework.stereotype.Repository;\r\n" + 
				"\r\n" + 
				"import com.realnet.model." + line_table_name_first_upper + ";\r\n" + 
				"\r\n");

		/*------BACK-END REPOSITORY (LINE-CLASS) CODE FINAL--------*/
		line_repository.append(line_repository_class_imports +
				"\r\n@Repository\r\n" + "public interface\t" + line_repository_name_first_upper +
				"\textends JpaRepository<" + line_table_name_first_upper + ", Integer>{\r\n" +
				"\r\n" +
				"	@Query(value = \"SELECT * FROM " + line_table_name_upper + " WHERE " + table_name_lower + "_id = ?1\" , nativeQuery = true)\r\n" +
				"	List<" + line_table_name_first_upper + "> findBy" + table_name_first_upper + "Id(Integer\t" + table_name_lower + "_id);\r\n" +
				
				" 	@Query(value = \"SELECT * FROM " + line_table_name_upper + " WHERE " + table_name_lower + "_id = :h_id AND l_id = :l_id\" , nativeQuery = true)\r\n" +
				"	Optional<" + line_table_name_first_upper + "> findByHeaderIdAndLineId(@Param(\"h_id\")Integer headerId, @Param(\"l_id\") Integer lineId);\r\n" + 
				"}");
		
		
		// BACK-END SERVICE CLASS DEPENDECY IMPORTS
		service_class_imports.append("package com.realnet.service;\r\n" + "\r\n" + "import java.util.List;\r\n" + "\r\n"
				+ "import com.realnet.exception.ResourceNotFoundException;\r\n" + "import com.realnet.model."
				+ table_name_first_upper + ";\r\n");

		/*------BACK-END SERVICE CLASS CODE FINAL--------*/
		service.append(service_class_imports + "\r\n" + "public interface\t" + service_name_first_upper + "{\r\n"
				+ "	List<" + table_name_first_upper + "> getAll();\r\n" + "	\r\n" + table_name_first_upper
				+ "\tgetById(int id);\r\n" + "	\r\n" + table_name_first_upper + " save(" + table_name_first_upper + " "
				+ table_name_lower + ");\r\n" + "	\r\n" + table_name_first_upper + " updateById(int id,"
				+ table_name_first_upper + " " + table_name_lower + ") throws ResourceNotFoundException;\r\n"
				+ "	\r\n" + "	void deleteById(int id) throws ResourceNotFoundException;\r\n" + "}");

		// BACK-END SERVICE IMPL CLASS DEPENDECY IMPORTS
		service_impl_class_imports.append("package com.realnet.service;\r\n" + "\r\n" + "import java.util.List;\r\n"
				+ "import org.springframework.beans.factory.annotation.Autowired;\r\n"
				+ "import org.springframework.stereotype.Service;\r\n" + "\r\n"
				+ "import com.realnet.exception.ResourceNotFoundException;\r\n" + "import com.realnet.model."
				+ table_name_first_upper + ";\r\n" + "import com.realnet.repository." + repository_name_first_upper
				+ ";\r\n" + "");

		/*------BACK-END SERVICE IMPL CLASS CODE FINAL--------*/
		service_impl.append(service_impl_class_imports + "\r\n@Service\r\n" + "public class\t"
				+ service_impl_name_first_upper + "\timplements\t" + service_name_first_upper + " {\r\n" + "\r\n"
				+ "	@Autowired\r\n" + "	private\t" + repository_name_first_upper + " " + repository_name_lower + ";\r\n"
				+ "\r\n" + "	@Override\r\n" + "	public List<" + table_name_first_upper + "> getAll() {\r\n"
				+ "		return\t" + repository_name_lower + ".findAll();\r\n" + "}\r\n" + "	@Override\r\n"
				+ "	public\t" + table_name_first_upper + " " + "getById(int id) throws ResourceNotFoundException {\r\n"
				+ "		return\t" + repository_name_lower + ".findById(id)\r\n"
				+ "				.orElseThrow(() -> new ResourceNotFoundException(\" " + table_name_first_upper
				+ " not found :: \" + id));\r\n" + "	}\r\n" + "\r\n" +

				"//==================UPDATE DATA VARIABLE WILL BE PASS HERE==========\r\n" + "	@Override\r\n"
				+ "	public\t" + table_name_first_upper + " " + "updateById(int id, " + table_name_first_upper
				+ "\tvalue) throws ResourceNotFoundException {\r\n" + "		" + table_name_first_upper + " "
				+ table_name_lower + " = " + repository_name_lower + ".findById(id)\r\n"
				+ "				.orElseThrow(() -> new ResourceNotFoundException(\" " + table_name_lower
				+ " not found :: \" + id));\r\n" + "		" + service_impl_class_get_set_attribute + "\r\n"
				+ "		" + table_name_first_upper + " " + "updated" + table_name_first_upper + " = "
				+ repository_name_lower + ".save(" + table_name_lower + ");\r\n" + "		return\t" + "updated"
				+ table_name_first_upper + ";\r\n" + "}\r\n" + "	@Override\r\n" + "	public\t"
				+ table_name_first_upper + " " + "save(" + table_name_first_upper + " " + table_name_lower + ") {\r\n"
				+ "		return\t" + repository_name_lower + ".save(" + table_name_lower + ");\r\n" + "	}\r\n" + "\r\n"
				+ "	@Override\r\n" + "	public void deleteById(int id) throws ResourceNotFoundException {\r\n"
				+ table_name_first_upper + " " + table_name_lower + " = " + repository_name_lower + ".findById(id)\r\n"
				+ "				.orElseThrow(() -> new ResourceNotFoundException(\" " + table_name_lower
				+ " not found :: \" + id));\r\n" + "		" + repository_name_lower + ".delete(" + table_name_lower
				+ ");\r\n" + "	}\r\n" + "}");

		// BACK-END CONTROLLER CLASS DEPENDECY IMPORTS
		controller_class_imports.append("package com.realnet.controller;\r\n" + "\r\n" + "import java.util.HashMap;\r\n"
				+ "import java.util.List;\r\n" + "import java.util.Map;\r\n" + "\r\n"
				+ "import javax.validation.Valid;\r\n" + "\r\n"
				+ "import org.springframework.beans.factory.annotation.Autowired;\r\n"
				+ "import org.springframework.http.ResponseEntity;\r\n"
				+ "import org.springframework.web.bind.annotation.CrossOrigin;\r\n"
				+ "import org.springframework.web.bind.annotation.DeleteMapping;\r\n"
				+ "import org.springframework.web.bind.annotation.GetMapping;\r\n"
				+ "import org.springframework.web.bind.annotation.PathVariable;\r\n"
				+ "import org.springframework.web.bind.annotation.PostMapping;\r\n"
				+ "import org.springframework.web.bind.annotation.PutMapping;\r\n"
				+ "import org.springframework.web.bind.annotation.RequestBody;\r\n"
				+ "import org.springframework.web.bind.annotation.RequestMapping;\r\n"
				+ "import org.springframework.web.bind.annotation.RestController;\r\n" + "\r\n"
				+ "import com.realnet.model." + table_name_first_upper + ";\r\n" + "import com.realnet.service."
				+ service_name_first_upper + ";\r\n" + "");

		/*------BACK-END CONTROLLER CLASS CODE FINAL--------*/
		controller.append(controller_class_imports + "\r\n" + "\r\n" + "@RestController\r\n"
				+ "@CrossOrigin(origins = \"http://localhost:4200\")\r\n" + "@RequestMapping(\"/api\")\r\n"
				+ "public class\t" + controller_name_first_upper + "{\r\n" 
				+ "	@Autowired\r\n" 
				+ "	private\t"
				+ service_name_first_upper + " " + service_name_lower + ";\r\n" + "\r\n"
				+ "	// ==========GET ALL=========\r\n" + "	@GetMapping(\"/" + table_name_lower + "\")\r\n"
				+ "	public List<" + table_name_first_upper + "> get" + table_name_first_upper + "Values() {\r\n"
				+ "		return\t" + service_name_lower + ".getAll();\r\n" + "	}\r\n" + "\r\n"
				+ "	// ==============GET BY ID=============\r\n" 
				+ "	@GetMapping(\"/" + table_name_lower
				+ "/{id}\")\r\n" + "	public ResponseEntity<" + table_name_first_upper + "> get"
				+ table_name_first_upper + "ValuesById(@PathVariable(value = \"id\") Integer id) {\r\n" + "		"
				+ table_name_first_upper + " " + table_name_lower + " = " + service_name_lower + ".getById(id);\r\n"
				+ "		return ResponseEntity.ok().body(" + table_name_lower + ");\r\n" + "	}\r\n" + "\r\n"
				+ "	// ================SAVE==================\r\n" + "	@PostMapping(\"/" + table_name_lower + "\")\r\n"
				+ "	public\t" + table_name_first_upper + " save" + table_name_first_upper + "(@Valid @RequestBody\t"
				+ table_name_first_upper + " " + table_name_lower + ") {\r\n" + "		return\t" + service_name_lower
				+ ".save(" + table_name_lower + ");\r\n" + "\r\n" + "	}\r\n" + "\r\n"
				+ "	// ================UPDATE==================\r\n" + "	@PutMapping(\"/" + table_name_lower
				+ "/{id}\")\r\n" + "	public ResponseEntity<" + table_name_first_upper + "> update"
				+ table_name_first_upper + "(@PathVariable(value = \"id\") Integer id,\r\n"
				+ "			@Valid @RequestBody\t" + table_name_first_upper + " " + table_name_lower + ") {\r\n"
				+ "		" + table_name_first_upper + " " + "updated" + table_name_first_upper + " = "
				+ service_name_lower + ".updateById(id, " + table_name_lower + ");\r\n"
				+ "		return ResponseEntity.ok(updated" + table_name_first_upper + ");\r\n" + "	}\r\n" + "\r\n"
				+ "	// =================DELETE====================\r\n" + "	@DeleteMapping(\"/" + table_name_lower
				+ "/{id}\")\r\n" + "	public Map<String, Boolean> delete" + table_name_first_upper
				+ "Value(@PathVariable(value = \"id\") Integer id) {\r\n" + "		" + service_name_lower
				+ ".deleteById(id);\r\n" + "		Map<String, Boolean> response = new HashMap<>();\r\n"
				+ "		response.put(\"deleted\", Boolean.TRUE);\r\n" + "		return response;\r\n" + "	}\r\n"
				+ "}\r\n" + "");
		
		
		// BACK-END HL LINE-CONTROLLER CLASS DEPENDECY IMPORTS
		StringBuilder line_controller_class_imports = new StringBuilder();
		line_controller_class_imports.append("package com.realnet.controller;\r\n" + 
				"\r\n" + 
				"import java.util.List;\r\n" + 
				"\r\n" + 
				"import javax.validation.Valid;\r\n" + 
				"\r\n" + 
				"import org.springframework.beans.factory.annotation.Autowired;\r\n" + 
				"import org.springframework.http.ResponseEntity;\r\n" + 
				"import org.springframework.web.bind.annotation.CrossOrigin;\r\n" + 
				"import org.springframework.web.bind.annotation.DeleteMapping;\r\n" + 
				"import org.springframework.web.bind.annotation.GetMapping;\r\n" + 
				"import org.springframework.web.bind.annotation.PathVariable;\r\n" + 
				"import org.springframework.web.bind.annotation.PostMapping;\r\n" + 
				"import org.springframework.web.bind.annotation.PutMapping;\r\n" + 
				"import org.springframework.web.bind.annotation.RequestBody;\r\n" + 
				"import org.springframework.web.bind.annotation.RequestMapping;\r\n" + 
				"import org.springframework.web.bind.annotation.RestController;\r\n" + 
				"\r\n" + 
				"import com.realnet.exception.ResourceNotFoundException;\r\n" + 
				"import com.realnet.model." + line_table_name_first_upper + ";\r\n" + 
				"import com.realnet.repository." + line_repository_name_first_upper + ";\r\n" + 
				"import com.realnet.repository." + repository_name_first_upper + ";\r\n" + 
				"");
		
		/*------BACK-END LINE-CONTROLLER CLASS CODE FINAL--------*/
		line_controller.append(line_controller_class_imports + 
				"\r\n" + 
				"@RestController\r\n" + 
				"@CrossOrigin(origins = \"http://localhost:4200\")\r\n" + 
				"@RequestMapping(\"/api\")\r\n" + 
				"public class\t" + line_controller_name_first_upper + " {\r\n" + 
				"\r\n" + 
				"    @Autowired\r\n" + 
				"    private\t" + line_repository_name_first_upper + " " + line_repository_name_lower + ";\r\n" + 
				"\r\n" + 
				"    @Autowired\r\n" + 
				"    private\t" + repository_name_first_upper + " " + repository_name_lower + ";\r\n" + 
				"\r\n" + 
				"	 // ==========GET ALL LINES BY HEADER ID=========\r\n"  +
				"    @GetMapping(\"/" + table_name_lower + "/{" + table_name_lower + "_id}/" + line_table_name_lower + "\")\r\n" + 
				"    public List <" + line_table_name_first_upper + "> getAll" + line_table_name_first_upper + "By" + table_name_first_upper + "(@PathVariable(value = \"" + table_name_lower + "_id\") Integer\t" + table_name_lower + "_id) {\r\n" + 
				"        return\t" + line_repository_name_lower + ".findBy" + table_name_first_upper + "Id(" + table_name_lower + "_id);\r\n" + 
				"    }\r\n" +
				"\r\n" + 
				"	 // ==========GET LINE BY HEADER ID & LINE ID=========\r\n"  +
				"	@GetMapping(\"/" + table_name_lower + "/{" + table_name_lower + "_id}/" + line_table_name_lower + "/{" + line_table_name_lower + "_id}\")\r\n" + 
				"	public\t" + line_table_name_first_upper + " get" + line_table_name_first_upper + "By"+ table_name_first_upper + "_idAnd" + line_table_name_first_upper + "_id(\r\n" + 
				"			@PathVariable(value = \"" + table_name_lower + "_id\") Integer " + table_name_lower + "_id,\r\n" + 
				"			@PathVariable(value = \"" + line_table_name_lower + "_id\") Integer " + line_table_name_lower + "_id) {\r\n" + 
				"		if (!" + repository_name_lower+ ".existsById(" + table_name_lower + "_id)) {\r\n" + 
				"			throw new ResourceNotFoundException(\"" + table_name_lower + "_id not found\");\r\n" + 
				"		}\r\n" + 
				"		return " + line_repository_name_lower + ".findByHeaderIdAndLineId(" + table_name_lower + "_id, " + line_table_name_lower + "_id)\r\n" + 
				"				.orElseThrow(() -> new ResourceNotFoundException(\"" + line_table_name_lower + "_id not found\"));\r\n" + 
				"	}\r\n" +				
				"\r\n" +
				"	 // ==========CREATE LINES=========\r\n"  +
				"    @PostMapping(\"/" + table_name_lower + "/{" + table_name_lower + "_id}/" + line_table_name_lower + "\")\r\n" + 
				"    public\t" + line_table_name_first_upper + " " + "create" + line_table_name_first_upper + "(@PathVariable(value = \"" + table_name_lower + "_id\") Integer\t" + table_name_lower + "_id,\r\n" + 
				"        @Valid @RequestBody\t" + line_table_name_first_upper + " " + line_table_name_lower + ") throws ResourceNotFoundException {\r\n" + 
				"        return\t" + repository_name_lower + ".findById(" + table_name_lower + "_id).map(" + table_name_lower + " -> {\r\n" + 
				"            " + line_table_name_lower + ".set" + table_name_first_upper + "(" + table_name_lower + ");\r\n" + 
				"            return\t" + line_repository_name_lower + ".save(" + line_table_name_lower + ");\r\n" + 
				"        }).orElseThrow(() -> new ResourceNotFoundException(\"" + table_name_lower + " not found\"));\r\n" + 
				"    }\r\n" + 
				"\r\n" + 
				"	 // ==========UPDATE LINES=========\r\n"  +
				"    @PutMapping(\"/" + table_name_lower + "/{" + table_name_lower + "_id}/" + line_table_name_lower + "/{" + line_table_name_lower + "_id}\")\r\n" + 
				"    public\t" + line_table_name_first_upper + "\tupdate" + line_table_name_first_upper + "(@PathVariable(value = \"" + table_name_lower + "_id\") Integer\t" + table_name_lower + "_id,\r\n" + 
				"        @PathVariable(value = \"" + line_table_name_lower + "_id\") Integer\t" + line_table_name_lower + "_id, @Valid @RequestBody\t" + line_table_name_first_upper + " " + line_table_name_lower + "Request)\r\n" + 
				"    throws ResourceNotFoundException {\r\n" + 
				"        if (!" + repository_name_lower + ".existsById(" + table_name_lower + "_id)) {\r\n" + 
				"            throw new ResourceNotFoundException(\"" + table_name_lower + "_id not found\");\r\n" + 
				"        }\r\n" + 
				"\r\n" + 
				"        return\t" + line_repository_name_lower + ".findById(" + line_table_name_lower + "_id).map(" + line_table_name_lower + " -> {\r\n" + 
				"		//===============LOOP TO UPDATE GET SET DEVELOPEMENT================\r\n" +
				"		" +  line_controller_class_update_method_variable + "\r\n" +
				"            return\t" + line_repository_name_lower + ".save(" + line_table_name_lower + ");\r\n" + 
				"        }).orElseThrow(() -> new ResourceNotFoundException(\"" + line_table_name_lower + "_id not found\"));\r\n" + 
				"    }\r\n" + 
				"\r\n" + 
				"	 // ==========DELETE LINES=========\r\n"  +
				"    @DeleteMapping(\"/" + table_name_lower + "/{" + table_name_lower + "_id}/" + line_table_name_lower + "/{" + line_table_name_lower + "_id}\")\r\n" + 
				"    public ResponseEntity<?> delete" + line_table_name_first_upper + "(@PathVariable(value = \"" + table_name_lower + "_id\") Integer\t" + table_name_lower + "_id,\r\n" 
				+ 
				"        @PathVariable(value = \"" + line_table_name_lower + "_id\") Integer\t" + line_table_name_lower + "_id) throws ResourceNotFoundException {\r\n" + 
				"        return\t" + line_repository_name_lower + ".findByHeaderIdAndLineId(" + line_table_name_lower + "_id, " + table_name_lower + "_id).map(" + line_table_name_lower + " -> {\r\n" + 
				"            " + line_repository_name_lower + ".delete(" + line_table_name_lower + ");\r\n" + 
				"            return ResponseEntity.ok().body(\"Record Deleted Successfully with Line Id :: \" + " + line_table_name_lower + "_id);\r\n" + 
				"        }).orElseThrow(() -> new ResourceNotFoundException(\r\n" + 
				"            \"" + line_table_name_first_upper + " not found with id = \" + " + line_table_name_lower + "_id" + " + \" and " + table_name_lower + "_id = \" + " + table_name_lower + "_id));\r\n" + 
				"    }\r\n" + 
				"}");
		
//----------------------------------------------BACK-END DONE---------------------------------------------------------------

		/*
		 * ===================FRONT-END FINAL PART WILL START HERE===============================
		 */
		
		// FRONT-END Model.ts FINAL code
		ng_header_model_ts
				.append(ng_header_model_ts_line_imports + "export class\t" + ng_model_ts_name + " {\r\n"
						+ ng_header_model_ts_attribute + "\r\n" + ng_header_model_ts_array_variable + "\r\n}");

		// FRONT-END HEADER-LINE Model_L.ts FINAL code
		ng_line_model_ts.append("export class\t" + ng_line_model_ts_name +" {\r\n" 
				+ "	" + ng_line_model_ts_attribute + "\r\n" 
				+ "}");
		
		
		// FRONT-END component.html  FINAL code (router-outlet static code)
		ng_component_html.append("<section class=\"bg-white mat-elevation-z10\" style=\"min-height: 80vh;\">\r\n"
				+ "    <router-outlet style=\"padding: 12px;\"></router-outlet>\r\n" + "  </section>");
		// FRONT-END component.css FINAL code
		ng_component_css.append("/*=========add CSS Style here===========*/\r\n");

		// FRONT-END component.ts FINAL code
		ng_component_ts.append("import { Component, OnInit } from '@angular/core';\r\n" + "\r\n" + "@Component({\r\n"
				+ "  selector: 'app-" + ng_file_name + "',\r\n" + "  templateUrl: './" + ng_file_name
				+ ".component.html',\r\n" + "  styleUrls: ['./" + ng_file_name + ".component.css']\r\n" + "})\r\n"
				+ "export class\t" + ng_component_ts_name + "\timplements OnInit {\r\n" + "  constructor() { }\r\n"
				+ "\r\n" + "  ngOnInit(): void {\r\n" + "  }\r\n" + "}");

		// FRONT-END module.ts imports
		ng_module_ts_imports.append("import { NgModule } from \"@angular/core\";\r\n"
				+ "import { SharedModule } from \"src/app/shared/shared.module\";\r\n"
				+ "import { CommonModule } from \"@angular/common\";\r\n"
				+ "import { FormsModule, ReactiveFormsModule } from \"@angular/forms\";\r\n"
				+ "import { MatIconModule } from \"@angular/material/icon\";\r\n"
				+ "import { MatExpansionModule } from '@angular/material/expansion';\r\n"
				+ "import { MatFormFieldModule } from \"@angular/material/form-field\";\r\n"
				+ "import { MatInputModule } from \"@angular/material/input\";\r\n"
				+ "import { MatCheckboxModule } from \"@angular/material/checkbox\";\r\n"
				+ "import { MatCardModule } from \"@angular/material/card\";\r\n"
				+ "import { MatSlideToggleModule } from \"@angular/material/slide-toggle\";\r\n"
				+ "import { MatTableModule } from \"@angular/material/table\";\r\n"
				+ "import { MatPaginatorModule } from '@angular/material/paginator';\r\n"
				+ "import { MatSortModule } from '@angular/material/sort';\r\n"
				+ "import { MatToolbarModule } from '@angular/material/toolbar';"
				+ "import { MatAutocompleteModule } from \"@angular/material/autocomplete\";\r\n"
				+ "import { FlexLayoutModule } from \"@angular/flex-layout\";\r\n"
				+ "import { MatTabsModule } from \"@angular/material/tabs\";\r\n"
				+ "import { MatRadioModule } from \"@angular/material/radio\";\r\n"
				+ "import { MatDatepickerModule } from \"@angular/material/datepicker\";\r\n"
				+ "import { MatNativeDateModule } from \"@angular/material/core\";\r\n"
				+ "import { MatSelectModule } from \"@angular/material/select\";\r\n"
				+ "import { MatSliderModule } from \"@angular/material/slider\";\r\n"
				+ "import { MatButtonModule } from '@angular/material/button';\r\n"
				+ "//========================HEADER COMPONENT IMPORTS====================\r\n" 
				+ "import {" + ng_component_ts_name + " } from './" + ng_file_name + ".component';\r\n"
				+ "import {" + ng_routing_module_ts_name + "} from './" + ng_file_name + "-routing.module';\r\n" 
				+ "import {" + ng_all_grid_view_component_name + "} from './all/all-" + ng_file_name + ".component';\r\n" 
				+ "import {" + ng_add_form_component_name + "} from './add/add-" + ng_file_name + ".component';\r\n" 
				+ "import {" + ng_edit_component_name + "} from './edit/edit-" + ng_file_name + ".component';\r\n" 
				+ "import {" + ng_read_only_component_name + "} from './details/" + ng_file_name + "-details.component';\r\n" 
				+ "import {" + ng_extension_add_component_name + "} from './extPages/ext-add-" + ng_file_name + ".component';\r\n"
				+ "//=====H-L LINE COMPONENT IMPORTS=======\r\n"
				+ ng_module_ts_line_imports + "\r\n"
				+ "\r\n"
				+ "");

		/* ====================module.ts FINAL code=========================== */
		ng_module_ts.append(ng_module_ts_imports + "\r\n" + "@NgModule({\r\n" + "  imports: [\r\n"
				+ "    CommonModule,\r\n" + "    SharedModule,\r\n" + "    " + ng_routing_module_ts_name + ",\r\n"
				+ "    FormsModule,\r\n" + "    ReactiveFormsModule,\r\n" + "    FlexLayoutModule,\r\n"
				+ "    MatTabsModule,\r\n" + "    MatAutocompleteModule,\r\n" + "    MatSlideToggleModule,\r\n"
				+ "    MatTableModule,\r\n" + "    MatPaginatorModule,\r\n" + "    MatSortModule,\r\n"
				+ "    MatToolbarModule,\r\n" + "    MatFormFieldModule,\r\n" + "    MatInputModule,\r\n"
				+ "    MatButtonModule,\r\n" + "    MatCardModule,\r\n" + "    MatCheckboxModule,\r\n"
				+ "    MatRadioModule,\r\n" + "    MatDatepickerModule,\r\n" + "    MatNativeDateModule,\r\n"
				+ "    MatFormFieldModule,\r\n" + "    MatSelectModule,\r\n" + "    MatSliderModule,\r\n"
				+ "    MatIconModule,\r\n" 
				+ "    MatExpansionModule\r\n  ],\r\n" 
				+ "  declarations: [\r\n" 
				+ "    " + ng_component_ts_name + ",\r\n" 
				+ "    " + ng_all_grid_view_component_name + ",\r\n" 
				+ "    " + ng_add_form_component_name + ",\r\n" 
				+ "    " + ng_read_only_component_name + ",\r\n" 
				+ "    " + ng_edit_component_name + ",\r\n"
				+ "		"+ng_extension_add_component_name + ",\r\n" 
				+ "		//==========H-L LINE COMPONENTS==============\r\n"
				+ "    " + ng_line_all_grid_view_component_name + ",\r\n" 
				+ "    " + ng_line_add_form_component_name + ",\r\n" 
				+ "    " + ng_line_read_only_component_name + ",\r\n" 
				+ "    " + ng_line_edit_component_name + ",\r\n"
				+ "		"+ng_line_extension_add_component_name + ",\r\n"
				+ "  ],\r\n"
				+ "  providers: []\r\n" 
				+ "})\r\n" 
				+ "export class\t" + ng_module_ts_name + " {}\r\n" + "");

		/*
		 * ==================service.ts FINAL code (HTTP METHODS)=====================
		 */
		ng_service_ts.append("import { Injectable } from \"@angular/core\";\r\n"
				+ "import { Observable, BehaviorSubject } from \"rxjs\";\r\n"
				+ "import { HttpClient, HttpErrorResponse, HttpHeaders} from \"@angular/common/http\";\r\n"
				+ "import { map } from 'rxjs/operators';\r\n" 
				+ "import { " + ng_model_ts_name + " } from './"+ ng_model_ts_name + "';\r\n"
				+ ng_service_ts_line_imports 
				+ "\r\n" 
				+ "@Injectable({\r\n" + "  providedIn: 'root',\r\n" + "})\r\n"
				+ "export class\t" + ng_service_ts_name + " {\r\n" 
				+ "  private API_URL = \"http://localhost:8081/api/" + table_name_lower + "\";\r\n"
				+ "	 dataChange: BehaviorSubject<" + ng_model_ts_name
				+ "[]> = new BehaviorSubject<" + ng_model_ts_name + "[]>([]);\r\n" 
				+ "  readonly httpOptions = {\r\n"
				+ "    headers: new HttpHeaders({\r\n" + "      'Content-Type':  'application/json'\r\n" + "    })\r\n"
				+ "  };\r\n" + "  private extractData(res: Response) {\r\n" + "    let body = res;\r\n"
				+ "    return body || { };\r\n" + "  }\r\n" + "\r\n" + "  constructor(private http: HttpClient) {}\r\n"
				+ "  \r\n" + "	 get data(): " + ng_model_ts_name + "[] {\r\n"
				+ "    	return this.dataChange.value;\r\n}" + "	\r\n" + "  /*====GET ALL DATA=======*/\r\n"
				+ " getAll(): any {\r\n" + "    return this.http.get<" + ng_model_ts_name
				+ "[]>(this.API_URL).subscribe(data => {\r\n" + "      this.dataChange.next(data);\r\n" + "    },\r\n"
				+ "    (error: HttpErrorResponse) => {\r\n" + "    console.log (error.name + ' ' + error.message);\r\n"
				+ "    });\r\n}" + "\r\n" + "  /*=======GET DATA with ID*/\r\n"
				+ "  getById(id: number): Observable<any> {\r\n"
				+ "    return this.http.get(`${this.API_URL}/${id}`);\r\n" + "  }\r\n" + "\r\n"
				+ "  /*=========CREATE DATA=========*/\r\n" + "  create(" + table_name_lower
				+ ": Object): Observable<Object> {\r\n" + "    return this.http.post(`${this.API_URL}`, "
				+ table_name_lower + ");\r\n" + "  }\r\n" + "\r\n" + "  /*==========UPDATE DATA=============*/\r\n"
				+ "  update(id: number, value: any): Observable<Object> {\r\n"
				+ "    return this.http.put(`${this.API_URL}/${id}`, value);\r\n" + "  }\r\n" + "\r\n"
				+ "  /*===========DELETE DATA=============*/\r\n" + "  delete(id: number): Observable<any> {\r\n"
				+ "    return this.http.delete(`${this.API_URL}/${id}`, {\r\n" + "      responseType: \"text\"\r\n"
				+ "    });\r\n"
				+ "	}"
				+ "//=====LINE CODE WILL INJECT HERE========\r\n"
				+ " " + ng_service_ts_line_methods
				+ "\r\n}");

		// routing.module.ts dependency imports
		ng_routing_ts_imports.append("import { RouterModule, Routes } from '@angular/router';\r\n"
				+ "import { NgModule } from '@angular/core';\r\n" + "import { " + ng_all_grid_view_component_name
				+ "} from './all/all-" + ng_file_name + ".component';\r\n" + "import { " + ng_component_ts_name
				+ " } from './" + ng_file_name + ".component';\r\n" + "import { " + ng_add_form_component_name
				+ " } from './add/add-" + ng_file_name + ".component';\r\n" + "import { " + ng_read_only_component_name
				+ " } from './details/" + ng_file_name + "-details.component';\r\n" + "import { "
				+ ng_edit_component_name + " } from './edit/edit-" + ng_file_name + ".component';\r\n");

		
		// routing.module.ts code
		ng_routing_module_ts
				.append(ng_routing_ts_imports + "\r\n" 
						+ "//------------H-L LINE IMPORTS-----------\r\n"
						+ ng_routing_ts_line_imports
						+ "\t\nconst routes: Routes = [\r\n"
						+ ng_routing_ts_routing_paths
						+ ng_routing_ts_header_line_routing_paths
						+ "  ];\r\n"
						+ "\r\n" + "  @NgModule({\r\n"
						+ "    imports: [RouterModule.forChild(routes)],\r\n" 
						+ "    exports: [RouterModule]\r\n"
						+ "  })\r\n" 
						+ "  export class\t " + ng_routing_module_ts_name + " {}");

		// ====================CRUD CODE START HERE==========================

		/* ==============ANGULAR ADD FORM component.HTML FINAL========== */
		ng_add_form_component_html.append(
				"<div class=\"container\">\r\n" + 
				"  <h4 style=\"text-align: center;\">" + ng_model_ts_name + "</h4>\r\n" + 
				"  <button mat-button class=\"ext_btn\" mat-button (click)=\"addExtension()\">\r\n" + 
				"    <mat-icon>near_me</mat-icon>\r\n" + 
				"  </button>\r\n" 
						+ "		<form [formGroup]=\"" + ng_model_ts_name_lower + "Form\" (ngSubmit)=\"onSubmit()\">\r\n"
						+ ng_add_form_component_html_integer_attribute + "			"
						+ ng_add_form_component_html_double_attribute + "			"
						+ ng_add_form_component_html_datetime_attribute + "			"
						+ ng_add_form_component_html_varchar_attribute + "			"
						+ ng_add_form_component_html_longtext_attribute + "			"
						+ "        \r\n<!-----for EXTENSION part(Attribute, Flex)---->\r\n"
						+ "        <ng-container *ngIf=\"isShow\">\r\n" 
						+ "        <app-ext-add-" + ng_file_name 
						+ " [" + ng_model_ts_name_lower + "Form]=\"" + ng_model_ts_name_lower + "Form\"></app-ext-add-" + ng_file_name + ">\r\n"
						+ "        </ng-container>" + "\r\n"
						+ "        \r\n<!-----H-L LINE part(FormArray For Multiple Lines)---->\r\n"
						+ ng_add_form_component_html_line_table_code
						+ "\r\n"
						+ ng_add_form_component_html_button_attribute + "\r\n"
						//+ "			<button mat-raised-button color="primary" type="submit" [disabled]="!rn_nil_home1Form.valid">SUBMIT</button>
						+ "      	</form>\r\n"
						+ " </div>\r\n");

		
		ng_add_form_component_ts_imports.append("import { Component, OnInit } from \"@angular/core\";\r\n"
				+ "import { FormGroup, FormBuilder, Validators, FormArray } from \"@angular/forms\";\r\n"
				//+ "import { " + ng_model_ts_name + " } from \"../" + ng_model_ts_name + "\";\r\n" 
				+ "import { " + ng_service_ts_name + " } from \"../" + ng_file_name + ".service\";\r\n"
				+ "import { Router, ActivatedRoute } from \"@angular/router\";\r\n");

		/* ==============ANGULAR ADD FORM component.TS FINAL========== */
		ng_add_form_component_ts.append(ng_add_form_component_ts_imports + "\r\n" + "@Component({\r\n"
				+ "  selector: \"app-add-" + ng_file_name + "\",\r\n" + "  templateUrl: \"./add-" + ng_file_name
				+ ".component.html\",\r\n" + "  styleUrls: [\"./add-" + ng_file_name + ".component.css\"]\r\n" + "})"
				+ "\r\n" + "export class\t" + ng_add_form_component_name + "\timplements OnInit {\r\n" 
				+ "  public\t" + ng_model_ts_name_lower + "Form: FormGroup;\r\n" 
				+ "  submitted = false;\r\n"
				+ "	 isShow: boolean = false;" + "\r\n"
				+ "	 lineShow: boolean = false;" + "\r\n"
				+ "  constructor(\r\n" + "    private _fb: FormBuilder,\r\n"
				+ "    private\t" + ng_service_ts_name_lower + ": " + ng_service_ts_name + ",\r\n"
				+ "    private router: Router,\r\n" 
				+ "    private route: ActivatedRoute\r\n" + "  ) {}\r\n" 
				+ "\r\n"
				+ "  ngOnInit() {\r\n" 
				+ "    this." + ng_model_ts_name_lower + "Form = this._fb.group({\r\n" + "    "
				+ ng_add_form_component_ts_validation_var 
				+ "      //====LINE TABLE VALUES WILL GO THERE USING: new FormArray([])\r\n" 
				+ ng_add_form_component_ts_formArray_variable + "\r\n"
				+ "    });\r\n"
				+ "  }"
				+ "\r\n" 
				+ "//====ADD LINE FORM METHODS========\r\n"
				+ ng_add_form_component_ts_formArray_methods + "\r\n"
				+ "	onCreate() {\r\n"
				+ " 	this." + ng_service_ts_name_lower + "\r\n"
				+ "      	.create(this." + ng_model_ts_name_lower + "Form.value)\r\n" 
				+ "      	.subscribe(res => {\r\n"
				+ "					console.log('add.component HEADER VALUE GOES INTO DATABASE = ', res);\r\n"
				+ "      		});\r\n" 
				+ "}" 
				+ "\r\n" 
				+ "	onSubmit() {\r\n"
				+ "    //======call API to save==========\r\n"
				+ "    console.log(\"ADD FORM SUBMITTED VALUE = \",this." + ng_model_ts_name_lower + "Form.value);\r\n" 
				+ "    this.submitted = true;\r\n" 
				+ "    // stop here if form is invalid\r\n"
				+ "    if (this." + ng_model_ts_name_lower + "Form.invalid) {\r\n" 
				+ "      return;\r\n" 
				+ "    }\r\n"
				+ "    this.onCreate();\r\n" 
				+ "    this.router.navigate([\"../" + ng_path_name + "\"], { relativeTo: this.route });\r\n"
				+ "  }\r\n" 
				+ "addExtension() {\r\n" 
				+ "    this.isShow = !this.isShow;"
				+ "\r\n}" 
				+ "\r\n" 
				+ "addLineExtension() {\r\n" + 
				"    this.lineShow = !this.lineShow;\r\n" + 
				"  }\r\n"
				+ "}");
		
		/* ==============ANGULAR ADD FORM component.CSS FINAL========== */
		ng_add_form_component_css.append("/*---------WRITE CSS CODE HERE--------------*/\r\n" + "mat-icon {\r\n"
				+ "    font-size: 20px;\r\n" + "}\r\n" + "\r\n" + ".ext_btn {\r\n" + "    float: right;\r\n"
				+ "    margin-right: 150px;\r\n" + "}");

		// =====================EXTENSION PART=============================

		/* =========== ANGULAR ADD FORM EXTENSION component.HTML FINAL */
		ng_extension_add_form_component_html.append("<div [formGroup]=\"" + ng_model_ts_name_lower + "Form\">" + "\r\n"
				+ ng_extension_add_form_component_html_flex_attribute + "</div>");
		
		/* =========== ANGULAR ADD FORM EXTENSION component.TS FINAL */
		ng_extension_add_form_component_ts.append("import { Component, OnInit, Input } from \"@angular/core\";\r\n"
				+ "import { FormGroup } from '@angular/forms';\r\n" + "\r\n" + "@Component({\r\n"
				+ "  selector: \"app-ext-add-" + ng_file_name + "\",\r\n" + "  templateUrl: \"./ext-add-" + ng_file_name
				+ ".component.html\",\r\n" + "})\r\n" + "export class\t" + ng_extension_add_component_name
				+ "\timplements OnInit {\r\n" + "    @Input()\t" + ng_model_ts_name_lower + "Form: FormGroup;\r\n"
				+ "    constructor() { }\r\n" + "    ngOnInit() {\r\n" + "    }\r\n" + "}\r\n" + "");

		/* ==============ANGULAR EDIT FORM component.HTML FINAL========== */
		ng_edit_form_component_html.append(
				  "<div class=\"container\">\r\n" +
				"	<div class=\"header-only\">\r\n" +
				"		<h4 style=\"text-align: center\">UPDATE " + ng_model_ts_name.toUpperCase() + " FORM" + "</h4>\r\n" +
				"		<mat-accordion class=\"example-headers-align\" multi>\r\n" + 
				"			<form class=\"form\" (ngSubmit)=\"onSubmit()\">\r\n" + 
				"				<mat-expansion-panel [expanded]=\"step === 0\" (opened)=\"setStep(0)\" hideToggle>\r\n" + 
				"					<mat-expansion-panel-header>\r\n" + 
				"						<mat-panel-title>" + ng_model_ts_name.toUpperCase() + " ITEMS</mat-panel-title>\r\n" + 
				"						<mat-panel-description>update " + ng_model_ts_name.toLowerCase() + " table fields</mat-panel-description>\r\n" + 
				"					</mat-expansion-panel-header>\r\n" + 
				"					<!-- DEFAULT HEADER FORM FIELDS -->\r\n" + 
				"					<table class=\"header-table\" cellspacing=\"0\">\r\n" + 
				"						" + ng_edit_form_component_html_datatype_attribute + "\r\n" +
				"					</table>\r\n" + 
				"				</mat-expansion-panel>\r\n" + 
				"\r\n" + 
				"				<mat-expansion-panel>\r\n" + 
				"					<mat-expansion-panel-header>\r\n" + 
				"						<mat-panel-title>" + ng_model_ts_name.toUpperCase() + " EXTENDION ITEMS</mat-panel-title>\r\n" + 
				"						<mat-panel-description>update " + ng_model_ts_name.toLowerCase() + " table extension fields</mat-panel-description>\r\n" + 
				"					</mat-expansion-panel-header>\r\n" + 
				"					<!-- HEADER EXTENSION FORM FIELDS -->\r\n" + 
				"					<table class=\"header-table\" cellspacing=\"0\">\r\n" + 
				"						" + ng_edit_form_component_html_flex_attribute + "\r\n" + 
				"					</table>\r\n" + 
				"				</mat-expansion-panel>\r\n" + 
				"\r\n" + 
				"				<button mat-raised-button color=\"primary\" style=\"left: 89%;top: 8px\">UPDATE</button>\r\n" + 
				"			</form>\r\n" + 
				"		</mat-accordion>\r\n" + 
				"		<br>\r\n" + 
				"	</div>\r\n" + 
				"	<hr />\r\n" + 
				"</div>" +
				"\r\n" +
				"<!------------LINE CODE WILL INJECT HERE------------->\r\n" +
				" 	" + ng_edit_form_component_html_line_code);

		// edit.component.ts dependency imports
		ng_edit_form_component_ts_imports.append("import { Component, OnInit, ViewChild } from '@angular/core';\r\n" 
				+ "import { " + ng_model_ts_name + " } from '../" + ng_model_ts_name + "';\r\n"
				+ "import { Router, ActivatedRoute } from '@angular/router';\r\n"
				+ "import { " + ng_service_ts_name + " } from '../" + ng_file_name + ".service';\r\n" 
				+ "import { MatAccordion } from '@angular/material/expansion';");

		/* ==============ANGULAR EDIT FORM component.TS FINAL========== */
		ng_edit_form_component_ts.append(ng_edit_form_component_ts_imports 
				+ "\r\n"
				+ "@Component({\r\n"
				+ "  selector: 'app-edit-" + ng_file_name + "',\r\n" + "  templateUrl: './edit-" + ng_file_name
				+ ".component.html',\r\n" + "  styleUrls: ['./edit-" + ng_file_name + ".component.css']\r\n" + "})\r\n"
				+ "export class\t" + ng_edit_component_name + "\timplements OnInit {\r\n" 
				+ "@ViewChild(MatAccordion) accordion: MatAccordion;\r\n" + 
				"  step = 0;\r\n" + 
				"\r\n" + 
				"  setStep(index: number) {\r\n" + 
				"    this.step = index;\r\n" + 
				"  }\r\n" 
				+ "\r\n"
				+ ng_model_ts_name_lower + "Id: number;\r\n" 
				+ ng_model_ts_name_lower + ": " + ng_model_ts_name + ";\r\n"
				+ "  updated = false;\r\n" + "\r\n"
				+ "  constructor(private router: Router, private route: ActivatedRoute, private\t"
				+ ng_service_ts_name_lower + ": " + ng_service_ts_name + ") { }\r\n" 
				+ "\r\n"
				+ "  ngOnInit() {\r\n" + "    this." + ng_model_ts_name_lower + " = new\t" + ng_model_ts_name+ "();\r\n" 
				+ "    this." + ng_model_ts_name_lower + "Id = this.route.snapshot.params['id'];\r\n"
				+ "    console.log(\"[header edit component] header_id = \",this." + ng_model_ts_name_lower + "Id);\r\n"
				+ "    //Get DATA By Id\r\n"
				+ "    this." + ng_service_ts_name_lower + ".getById(this." + ng_model_ts_name_lower
				+ "Id).subscribe(data => {\r\n" 
				+ "        this." + ng_model_ts_name_lower + "= data;\r\n" 
				+ "      }, error => console.log(error));\r\n" 
				+ "  }\r\n"
				+ "\r\n" 
				+ "  update" + ng_model_ts_name + "() {\r\n" 
				+ "    this." + ng_service_ts_name_lower 
				+ ".update(this." + ng_model_ts_name_lower + "Id, this." + ng_model_ts_name_lower
				+ ").subscribe(data => {\r\n"
				+ " console.log(data);\r\n"
				+ "        }, error => {console.log(error)});\r\n" 
				+ "    this." + ng_model_ts_name_lower + "= new\t" + ng_model_ts_name + "();\r\n" 
				+ "  }\r\n" 
				+ "\r\n" 
				+ "  onSubmit() {\r\n"
				+ "    this.updated = true;\r\n" 
				+ "    this.update" + ng_model_ts_name + "();\r\n"
				+ "    this.router.navigate(['../../" + ng_path_name + "'], { relativeTo: this.route });\r\n"
				+ "  }\r\n"
				+ "\r\n" 
				+ "}");

		/* ==============ANGULAR EDIT FORM component.CSS FINAL========== */
		ng_edit_form_component_css.append("/*---------WRITE CSS CODE HERE--------------*/\r\n");

		/*
		 * ==============ANGULAR READ-ONLY (DETAILS) FORM component.HTML FINAL==========
		 */
		ng_read_only_form_component_html.append(
				"<div class=\"container\">\r\n" + 
				"  <h4 style=\"text-align: center;\">" + ng_model_ts_name.toUpperCase() + " TABLE VALUE DETAILS</h4>\r\n" + 
				"  <button mat-raised-button color=\"primary\" style=\"left: 90%; top: -37px;\" (click)=\"back()\">Back</button>\r\n" + 
				"  <div *ngIf=\"" + ng_model_ts_name_lower + "\">\r\n" + 
				"    <table class=\"table\">\r\n" + 
				"      <tr>\r\n" +
				"		" + ng_read_only_form_component_html_datatype_th + 
				"		</tr>\r\n" + 
				"		<tr>\r\n" + 
				"		" + ng_read_only_form_component_html_datatype_td + 
				"		</tr>\r\n" +
				"    </table>\r\n" + 
				"   <hr />\r\n" +
				"	<br />\r\n" + 
				"<!-- --------------LINE TABLE--------------- -->\r\n" +
				"	"+ ng_read_only_form_component_html_line_code +
				"  </div>\r\n" + 
				"  <hr />\r\n" + 
				"  <button mat-raised-button color=\"primary\" style=\"left: 90%;top: -10px;\" (click)=\"back()\">Back</button>\r\n" + 
				"</div>");
		
		/*
		 * ==============ANGULAR READ-ONLY (DETAILS) FORM component.CSS FINAL==========
		 */
		ng_read_only_form_component_css.append("/*---------WRITE CSS CODE HERE--------------*/");

		ng_read_only_form_component_ts_imports.append("import { Component, OnInit } from \"@angular/core\";\r\n"
				+ "import { " + ng_model_ts_name + " } from \"../" + ng_model_ts_name + "\";\r\n"
				+ "import { Router, ActivatedRoute } from \"@angular/router\";\r\n" + "import { " + ng_service_ts_name
				+ " } from \"../" + ng_file_name + ".service\";\r\n"
				+ "import { FormBuilder } from '@angular/forms';\r\n");
		/*
		 * ==============ANGULAR READ-ONLY (DETAILS) FORM component.TS FINAL==========
		 */
		ng_read_only_form_component_ts.append(ng_read_only_form_component_ts_imports + "\r\n" + "@Component({\r\n"
				+ "  selector: \"app-" + ng_file_name + "-details\",\r\n" + "  templateUrl: \"./" + ng_file_name
				+ "-details.component.html\",\r\n" + "  styleUrls: [\"./" + ng_file_name
				+ "-details.component.css\"]\r\n" + "})\r\n" 
				+ "export class\t" + ng_read_only_component_name + "\timplements OnInit {\r\n" 
				+ "  constructor(\r\n" + "    private fb: FormBuilder,\r\n"
				+ "    private\t" + ng_service_ts_name_lower + ": " + ng_service_ts_name + ",\r\n"
				+ "    private router: Router,\r\n" 
				+ "    private route: ActivatedRoute\r\n" + "  ) {}\r\n" 
				+ "\r\n"
				+ "  id: number;\r\n" 
				+ "  " + ng_model_ts_name_lower + ": " + ng_model_ts_name + ";\r\n"  
				+ "\r\n"
				+ "  ngOnInit() {\r\n"
				+ "    this.get" + ng_model_ts_name + "ById();\r\n"
				+ "  }\r\n" 
				+ "\r\n"
				+ "  get" + ng_model_ts_name + "ById() {\r\n"
				+ "     this.id = this.route.snapshot.params['id'];\r\n"
				+ "     console.log(\"[header details component] header id = \",this.id);\r\n"
				+ "     //Get DATA\r\n" 
				+ "      this." + ng_service_ts_name_lower + ".getById(this.id)\r\n" 
				+ "        .subscribe(data => {\r\n"
				+ "          this." + ng_model_ts_name_lower + " = data;\r\n"
				+ "			 console.log(\"HEADER TABLE value [in header details component] = \", this." + ng_model_ts_name_lower + ");"
				+ "        }, err => console.log(err));\r\n" 
				+ "    }\r\n"
				+ "\r\n"
				+ "  back() {\r\n"
				+ "    this.router.navigate(['../../" + ng_path_name + "'], { relativeTo: this.route });\r\n"
				+ "  }\r\n" 
				+ " }");

		/*
		 * ==============ANGULAR ALL (GRID-VIEW) FORM component.HTML FINAL==============
		 */
		ng_all_grid_view_component_html.append("<div class=\"container\">\r\n<h4 style=\"text-align: center;\">"
				+ ng_model_ts_name_lower + "</h4>\r\n" 
				+ "\r\n" +
				"<button mat-button (click)=\"goToAdd()\">\r\n"
				+ "  <mat-icon aria-label=\"Example icon-button with a heart icon\">add</mat-icon>\r\n" 
				+ "</button>"
				+ "\r\n" 
				+ "<!-- FILTER -->\r\n" 
				+ " <div class=\"search\">\r\n"
				+ "  <mat-form-field class=\"search-form-field\">\r\n"
				+ "    <input matInput #filter placeholder=\"search..\" autocomplete=\"off\" />\r\n"
				+ "  </mat-form-field>\r\n"
				+ "</div>"
				+ "\r\n" 
				+ "<!-- TOGGLE -->\r\n"
				+ "<mat-slide-toggle (change)=\"toggle($event)\">\r\n" 
				+ "  Hide Empty Columns\r\n"
				+ "</mat-slide-toggle>"
				+ "\r\n" 
				+ " <table mat-table id=\"table\" [dataSource]=\""
				+ ng_model_ts_name_lower + "DataSource\" matSort class=\"mat-cell\">" + "\r\n"
				+ "<!-- Select Column -->\r\n"
				+ "  <ng-container matColumnDef=\"id\">\r\n"
				+ "    <th mat-header-cell *matHeaderCellDef mat-sort-header> select </th>\r\n"
				+ "    <tr mat-cell *matCellDef=\"let row\">\r\n"
				+ "      <button mat-icon-button color=\"accent\" (click)=\"gotoUpdate(row.id)\">\r\n"
				+ "        <mat-icon aria-label=\"edit\" [ngStyle]=\"{ color: 'green' }\">edit</mat-icon>\r\n"
				+ "      </button>\r\n"
				+ "      <button mat-icon-button color=\"accent\" (click)=\"delete(row.id)\">\r\n"
				+ "        <mat-icon aria-label=\"Delete\" [ngStyle]=\"{ color: 'red' }\">delete</mat-icon>\r\n"
				+ "      </button>\r\n" + "      <button mat-icon-button (click)=\"goToView(row.id)\">\r\n"
				+ "        <mat-icon [ngStyle]=\"{ color: 'darkslategray' }\">visibility</mat-icon>\r\n"
				+ "      </button>\r\n" 
				+ "    </tr>\r\n"
				+ "  </ng-container>\r\n" + "\r\n"
				+ ng_all_grid_view_component_html_datatype_attribute + "\r\n"
				+ "    <tr mat-header-row *matHeaderRowDef=\"displayedColumns\"></tr>\r\n"
				+ "    <tr mat-row *matRowDef=\"let row; columns: displayedColumns\"></tr>" 
				+ " </table>"
				+ "\r\n"
				+ "<div class=\"no-results\" [style.display]=\"" + ng_model_ts_name_lower
				+ "DataSource.renderedData.length == 0 ? '' : 'none'\">\r\n" + "  No results\r\n" + "</div>\r\n"
				+ "\r\n"
				+ "<mat-paginator\r\n"
				+ "    #paginator\r\n" + "    [length]=\"" + ng_model_ts_name_lower
				+ "DataSource.filteredData.length\"\r\n"
				+ "    [pageIndex]=\"0\"\r\n" 
				+ "    [pageSize]=\"5\"\r\n"
				+ "    [pageSizeOptions]=\"[5, 10, 25, 100]\">\r\n" 
				+ "  </mat-paginator>" 
				+ "\r\n" 
				+ "</div>");

		/*
		 * ==============ANGULAR ALL (GRID-VIEW) FORM component.CSS FINAL==============
		 */
		ng_all_grid_view_component_css.append("/*----------WRITE CSS CODE HERE-------*/" + "\r\n" 
				+ "/* Filter */\r\n" + 
				".search {\r\n" + 
				"  min-height: 56px;\r\n" + 
				"  max-height: 20px;\r\n" + 
				"  width: 100px;\r\n" + 
				"  display: flex;\r\n" + 
				"  align-items: center;\r\n" + 
				"  padding: 8px 24px 0;\r\n" + 
				"  font-size: 20px;\r\n" + 
				"  border-radius: 5px;\r\n" + 
				"  justify-content: space-between;\r\n" + 
				"  border-bottom: 1px solid transparent;\r\n" + 
				"}\r\n" + 
				"\r\n" + 
				"/* Mat table */\r\n" + 
				".no-results {\r\n" + 
				"  display: flex;\r\n" + 
				"  justify-content: center;\r\n" + 
				"  padding: 14px;\r\n" + 
				"  font-size: 14px;\r\n" + 
				"  font-style: italic;\r\n" + 
				"}\r\n" + 
				"\r\n" + 
				"#table {\r\n" + 
				"    font-family: \"Trebuchet MS\", Arial, Helvetica, sans-serif;\r\n" + 
				"    font-size: 150%;\r\n" + 
				"    border-collapse: collapse;\r\n" + 
				"    width: 100%;\r\n" + 
				"}\r\n" + 
				"\r\n" + 
				"#table th, #table td {\r\n" + 
				"    border: 1px solid #ddd;\r\n" + 
				"    padding: 18px;\r\n" + 
				"  }\r\n" + 
				"#table th {\r\n" + 
				"    padding-top: 12px;\r\n" + 
				"    padding-bottom: 12px;\r\n" + 
				"    text-align: left;\r\n" + 
				"  }\r\n" + 
				"\r\n" + 
				"mat-icon {\r\n" + 
				"  font-size: 20px;\r\n" + 
				"}");

		ng_all_grid_view_component_ts_imports
				.append("import { Component, OnInit, ViewChild, ElementRef } from '@angular/core';\r\n" + "import { "
						+ ng_service_ts_name + " } from '../" + ng_file_name + ".service';\r\n"
						+ "import { MatPaginator } from '@angular/material/paginator';\r\n"
						+ "import { MatSlideToggleChange } from '@angular/material/slide-toggle';\r\n"
						+ "import { MatSort } from '@angular/material/sort';\r\n"
						+ "import { HttpClient } from '@angular/common/http';\r\n"
						+ "import { fromEvent, BehaviorSubject, Observable, merge } from 'rxjs';\r\n"
						+ "import { DataSource } from '@angular/cdk/table';\r\n"
						+ "import { " + ng_model_ts_name + " } from '../" + ng_model_ts_name + "';\r\n"
						+ "import { map } from 'rxjs/operators';\r\n"
						+ "import { Router, ActivatedRoute } from '@angular/router';\r\n");

		/*
		 * ==============ANGULAR ALL (GRID-VIEW) FORM component.TS FINAL==============
		 */
		ng_all_grid_view_component_ts.append(ng_all_grid_view_component_ts_imports + "\r\n" 
				+ "@Component({\r\n"
				+ "  selector: 'app-all-" + ng_file_name + "',\r\n" + "  templateUrl: './all-" + ng_file_name
				+ ".component.html',\r\n" 
				+ "  styleUrls: ['./all-" + ng_file_name + ".component.css']\r\n" 
				+ "})\r\n"
				+ "export class\t" + ng_all_grid_view_component_name + "\timplements OnInit {\r\n"
				+ "	defaultColumns: string[] = [" + ng_all_grid_view_component_ts_display_columns + "];\r\n"
				+ "	displayedColumns: string[] = this.defaultColumns;\r\n" 
				+ " exampleDatabase: " + ng_service_ts_name + " | null;\r\n" 
				+ "  " + ng_model_ts_name_lower + "DataSource: " + ng_model_ts_name
				+ "DataSource | null;\r\n" 
				+ "  id: number;\r\n"
				+ "  @ViewChild(MatPaginator, {static: true}) paginator: MatPaginator;\r\n"
				+ "  @ViewChild(MatSort, {static: true}) sort: MatSort;\r\n"
				+ "  @ViewChild('filter',  {static: true}) filter: ElementRef;\r\n" + " constructor(private\t"
				+ ng_service_ts_name_lower + ": " + ng_service_ts_name
				+ ", private httpClient: HttpClient, private router: Router, private route: ActivatedRoute) {}\r\n"
				+ "\r\n" 
				+ "    ngOnInit() {\r\n"
				+ "      this.loadData();\r\n"
				+ "      console.log(\"BEFORE : \",this." + ng_model_ts_name_lower + "DataSource);\r\n" 
				+ "      \r\n"
				+ "    }\r\n"
				+ "\r\n" 
				+ "    public loadData() {\r\n" 
				+ "      this.exampleDatabase = new\t" + ng_service_ts_name + "(this.httpClient);\r\n" 
				+ "      this." + ng_model_ts_name_lower + "DataSource = new\t" + ng_model_ts_name + "DataSource(\r\n"
				+ "		 this.exampleDatabase,\r\n"
				+ "		 this.paginator,\r\n"
				+ "		 this.sort);\r\n"
				+ "      fromEvent(this.filter.nativeElement, 'keyup')\r\n" 
				+ "        .subscribe(() => {\r\n"
				+ "          if (!this." + ng_model_ts_name_lower + "DataSource) {\r\n" 
				+ "            return;\r\n"
				+ "          }\r\n" 
				+ "          this." + ng_model_ts_name_lower
				+ "DataSource.filter = this.filter.nativeElement.value;\r\n" + "        });\r\n" 
				+ "    }\r\n" 
				+ "\r\n"
				+ "//======toggle to show hide empty columns========\r\n"
				+ "  public toggle(event: MatSlideToggleChange) {\r\n" 
				+ "    if (event.checked) {\r\n"
				+ "      const columns = this.getEmptyColumns();\r\n"
				+ "      this.displayedColumns = this.defaultColumns.filter(col => !columns[col]);\r\n"
				+ "    } else {\r\n" 
				+ "      this.displayedColumns = this.defaultColumns;\r\n" 
				+ "    }\r\n"
				+ "  }\r\n" 
				+ "\r\n"
				+ "  public getEmptyColumns(): {[key: string]: boolean} {\r\n"
				+ "    const columns = {};\r\n" + "\r\n"
				+ "    this.displayedColumns.forEach(col => {\r\n"
				+ "      columns[col] = this." + ng_model_ts_name_lower
				+ "DataSource.renderedData.every(element => {\r\n"
				+ "        return !element[col];\r\n"
				+ "      });\r\n"
				+ "    });\r\n" 
				+ "\r\n"
				+ "    return columns;\r\n"
				+ "  }" + "\r\n"
				+ "    goToAdd(){\r\n"
				+ "      this.router.navigate(['../add'], { relativeTo: this.route });\r\n"
				+ "    }\r\n" 
				+ "\r\n"
				+ "    gotoUpdate(id) {\r\n" 
				+ "      this.id = id;\r\n"
				+ "		 let key = 'header_id';\r\n" 
				+ "      let value: any = this.id;\r\n" 
				+ "    	 localStorage.setItem(key, value);\r\n"
				+ "      this.router.navigate(['../update/' + this.id], { relativeTo: this.route });\r\n" 
				+ "    }\r\n"
				+ "\r\n" 
				+ "    goToView(id: number) {\r\n"
				+ "      this.id = id;\r\n"
				+ "      this.router.navigate(['../details/' + this.id], { relativeTo: this.route });\r\n" 
				+ "    }\r\n"
				+ "\r\n" 
				+ "    delete(id: number) {\r\n"
				+ "    	 this.id = id;\r\n"
				+ "      this." + ng_service_ts_name_lower + ".delete(this.id)\r\n"
				+ "        .subscribe(\r\n" 
				+ "          data => {\r\n"
				+ "            this.loadData();\r\n"
				+ "          }, (error) => console.log(error));\r\n"
				+ "    }\r\n" 
				+ "}\r\n"
				+ "//====PAGINATOR|| SEARCH || SORT CODE HERE===\r\n"
				+ "export class\t" + ng_model_ts_name
				+ "DataSource extends DataSource<" + ng_model_ts_name + "> {\r\n"
				+ " _filterChange = new BehaviorSubject(\"\");\r\n" + "\r\n" + "  get filter(): string {\r\n"
				+ "    return this._filterChange.value;\r\n" + "  }\r\n" + "\r\n" + "  set filter(filter: string) {\r\n"
				+ "    this._filterChange.next(filter);\r\n" + "  }\r\n" + "filteredData: " + ng_model_ts_name
				+ "[] = [];\r\n" + "  renderedData: " + ng_model_ts_name + "[] = [];\r\n" + "\r\n"
				+ "  constructor(\r\n" + "    private\t" + ng_service_ts_name_lower + ": " + ng_service_ts_name
				+ ",\r\n" + "    private _paginator: MatPaginator,\r\n" + "    private _sort: MatSort\r\n" + "  ) {\r\n"
				+ "    super();\r\n" + "    this._filterChange.subscribe(() => (this._paginator.pageIndex = 0));\r\n"
				+ "  }\r\n" + "\r\n"
				+ "  connect(): Observable<" + ng_model_ts_name + "[]> {\r\n"
				+ "    const displayDataChanges = [\r\n" 
				+ "      this." + ng_service_ts_name_lower + ".dataChange,\r\n"
				+ "      this._sort.sortChange,\r\n"
				+ "      this._filterChange,\r\n"
				+ "      this._paginator.page,\r\n"
				+ "    ];\r\n"
				+ "    this." + ng_service_ts_name_lower + ".getAll();\r\n" 
				+ " return merge(...displayDataChanges).pipe(\r\n" 
				+ "      map(() => {\r\n"
				+ "        // Filter data\r\n"
				+ "        this.filteredData = this." + ng_service_ts_name_lower + ".data\r\n" 
				+ "          .slice()\r\n"
				+ "          .filter((" + ng_model_ts_name_lower + ": " + ng_model_ts_name + ") => {\r\n" 
				+ "            const searchStr = (\r\n"
				+ ng_all_grid_view_component_search
				+ ").toLowerCase();\r\n"
				+ "            return searchStr.indexOf(this.filter.toLowerCase()) !== -1;\r\n"
				+ "          });\r\n" + "\r\n" + "        // Sort filtered data\r\n"
				+ "        const sortedData = this.sortData(this.filteredData.slice());\r\n" + "\r\n"
				+ "        // Grab the page's slice of the filtered sorted data.\r\n"
				+ "        const startIndex = this._paginator.pageIndex * this._paginator.pageSize;\r\n"
				+ "        this.renderedData = sortedData.splice(\r\n" + "          startIndex,\r\n"
				+ "          this._paginator.pageSize\r\n" + "        );\r\n" + "        return this.renderedData;\r\n"
				+ "      })\r\n" + "    );\r\n" + "  }\r\n" + "  disconnect() {}\r\n" + "\r\n" + "  sortData(data: "
				+ ng_model_ts_name + "[]): " + ng_model_ts_name + "[] {\r\n"
				+ "    if (!this._sort.active || this._sort.direction === \"\") {\r\n" + "      return data;\r\n"
				+ "	 }\r\n" + " return data.sort((a, b) => {\r\n"
				+ "      let propertyA: Date | number | string = \"\";\r\n"
				+ "      let propertyB: Date | number | string = \"\";\r\n" + "\r\n"
				+ "      switch (this._sort.active) {\r\n" 
				+ "		" + ng_all_grid_view_component_ts_sort_string
				+ "\r\n"
				+ "}\r\n" 
				+ "\r\n" 
				+ "      const valueA = isNaN(+propertyA) ? propertyA : +propertyA;\r\n"
				+ "      const valueB = isNaN(+propertyB) ? propertyB : +propertyB;\r\n" + "\r\n"
				+ "      return ((valueA < valueB ? -1 : 1) * (this._sort.direction === \"asc\" ? 1 : -1));\r\n"
				+ "    });\r\n" 
				+ "  }\r\n" 
				+ "}\r\n");
		
		
		// ====================HL LINE CRUD CODE START HERE==========================
		
		
		/*
		 * ==============ANGULAR HL LINE-ADD FORM component.HTML FINAL==============
		 */
		ng_line_add_form_component_html.append("<div class=\"container\">\r\n" + 
				"  <h4 style=\"text-align: center;\">ADD " + ng_line_model_ts_name.toUpperCase() + " FORM</h4>\r\n" + 
				"  <button mat-raised-button style=\"right: auto;top: -40px;\" (click)=\"back()\">BACK TO ALL " + ng_line_model_ts_name.toUpperCase() + " TABLE</button>\r\n" + 
				"  <button mat-button style=\"left: 55%;top: -37px;\" mat-button (click)=\"addLineExtension()\">\r\n" + 
				"    <mat-icon>near_me</mat-icon>\r\n" + 
				"  </button>\r\n" + 
				"  <form [formGroup]=\"" + ng_line_model_ts_name_lower + "Form\" (ngSubmit)=\"onSubmit()\">\r\n" +
				ng_line_add_form_component_html_datatype_variables + "\r\n" +
				"    <!-----H-L LINE part(FormArray For Multiple Lines)---->\r\n" + 
				"    <ng-container *ngIf=\"lineShow\">\r\n" + 
				"      <app-ext-add-" + ng_line_file_name + " [" + ng_line_model_ts_name_lower + "Form]=\"" + ng_line_model_ts_name_lower + "Form\"></app-ext-add-" + ng_line_file_name + ">\r\n" + 
				"    </ng-container>\r\n" +  
				"    <br/>\r\n" + 
				"    <button \r\n" + 
				"      mat-raised-button \r\n" + 
				"      type=\"submit\" \r\n" + 
				"      color=\"primary\" \r\n" + 
				"      [disabled]=\"!" + ng_line_model_ts_name_lower + "Form.valid\" \r\n" + 
				"      style=\"top: -13px;left: 33%\">SUBMIT</button>\r\n" + 
				"  </form>\r\n" + 
				"</div>");
		
		/*
		 * ==============ANGULAR HL LINE-ADD FORM component.CSS FINAL==============
		 */
		ng_line_add_form_component_css.append("/*----------ADD CSS STYLE CODE HERE-----------*/\r\n");
		
		/*
		 * ==============ANGULAR HL LINE-ADD FORM component.TS FINAL==============
		 */
		ng_line_add_form_component_ts.append("import { Component, OnInit } from \"@angular/core\";\r\n" + 
				"import { Router, ActivatedRoute } from \"@angular/router\";\r\n" + 
				"import { " + ng_service_ts_name + " } from '../../" + ng_file_name + ".service';\r\n" + 
				"import { FormGroup, FormBuilder } from '@angular/forms';\r\n" + 
				"\r\n" + 
				"\r\n" + 
				"@Component({\r\n" + 
				"  selector: \"app-add-" + ng_line_file_name + "\",\r\n" + 
				"  templateUrl: \"./add-" + ng_line_file_name + ".component.html\",\r\n" + 
				"  styleUrls: [\"./add-" + ng_line_file_name + ".component.css\"],\r\n" + 
				"})\r\n" + 
				"export class " + ng_line_add_form_component_name + " implements OnInit {  \r\n" + 
				"  header_id: number;\r\n" + 
				"  public " + ng_line_model_ts_name_lower + "Form: FormGroup;\r\n" + 
				"  submitted = false;\r\n" + 
				"  lineShow: boolean = false;\r\n" + 
				"  constructor(\r\n" + 
				"    private _fb: FormBuilder,\r\n" + 
				"    private " + ng_service_ts_name_lower + ": " + ng_service_ts_name + ",\r\n" + 
				"    private router: Router,\r\n" + 
				"    private route: ActivatedRoute\r\n" + 
				"  ) {}\r\n" + 
				"\r\n" + 
				"  ngOnInit() {\r\n" + 
				"    this." + ng_line_model_ts_name_lower + "Form = this._fb.group({\r\n"
				+ ng_line_add_form_component_ts_validation_var + 
				"});\r\n" + 
				"  }\r\n" + 
				"\r\n" + 
				"\r\n" + 
				"  onSubmit() {\r\n" + 
				"    //======call API to save==========\r\n" + 
				"    console.log(\"LINE FORM SUBMIT VALUE = \", this." + ng_line_model_ts_name_lower + "Form.value);\r\n" + 
				"    this.submitted = true;\r\n" + 
				"    if (this." + ng_line_model_ts_name_lower + "Form.invalid) {\r\n" + 
				"      return;\r\n" + 
				"    }\r\n" + 
				"    this.onCreate();\r\n" + 
				"    this.router.navigate([\"../../all-line\"], { relativeTo: this.route });\r\n" + 
				"  }\r\n" + 
				"\r\n" + 
				"  onCreate() {\r\n" + 
				"	 // GET HEADER ID FROM LOCAL STORAGE\r\n" +
				"    this.header_id = +localStorage.getItem('header_id');\r\n" +  
				"    this." + ng_service_ts_name_lower + "\r\n" + 
				"      .createLineByHeaderId(this.header_id,this." + ng_line_model_ts_name_lower + "Form.value)\r\n" + 
				"      .subscribe((res) => {\r\n" + 
				"          console.log(\"VALUE GOES INTO DATABASE = \", res);\r\n" + 
				"      });\r\n" + 
				"  }\r\n" + 
				"\r\n" + 
				"  addLineExtension() {\r\n" + 
				"    this.lineShow = !this.lineShow;\r\n" + 
				"  }\r\n" + 
				"  back() {\r\n" + 
				"    this.router.navigate(['../../all-line'], { relativeTo: this.route });\r\n" + 
				"  }\r\n" + 
				"}"); 
		
		
		/*
		 * ==============ANGULAR HL LINE-ALL FORM component.HTML FINAL==============
		 */
		ng_line_all_grid_view_component_html.append("<div class=\"container\">\r\n" + 
				"  <h3 style=\"text-align: center;\">" + ng_line_model_ts_name.toUpperCase() + " VALUES</h3>\r\n" + 
				"  <button mat-button (click)=\"addLines()\">\r\n" + 
				"    <mat-icon aria-label=\"Example icon-button with a heart icon\">add</mat-icon>\r\n" + 
				"  </button>\r\n" + 
				"\r\n" + 
				"  <!-- FILTER -->\r\n" + 
				"  <div class=\"search\">\r\n" + 
				"    <mat-form-field class=\"search-form-field\">\r\n" + 
				"      <input matInput #filter placeholder=\"search..\" autocomplete=\"off\" />\r\n" + 
				"    </mat-form-field>\r\n" + 
				"  </div>\r\n" + 
				"  <!-- TOGGLE -->\r\n" + 
				"  <mat-slide-toggle (change)=\"toggle($event)\">\r\n" + 
				"    Hide Empty Columns\r\n" + 
				"  </mat-slide-toggle>\r\n" + 
				"  <!-- LINE TABLE -->\r\n" + 
				"  <table mat-table id=\"table\" [dataSource]=\"" + ng_line_model_ts_name_lower + "DataSource\" matSort class=\"mat-cell\">\r\n" + 
				ng_line_all_grid_view_component_html_datatype_attribute +
				"\r\n" +
				"    <!-- HEADER ROW -->\r\n" + 
				"    <tr mat-header-row *matHeaderRowDef=\"displayedColumns\"></tr>\r\n" + 
				"    <tr mat-row *matRowDef=\"let row; columns: displayedColumns\"></tr>\r\n" + 
				"  </table>\r\n" + 
				"  <mat-paginator #paginator [length]=\"" + ng_line_model_ts_name_lower + "DataSource.filteredData.length\" [pageIndex]=\"0\" [pageSize]=\"5\"\r\n" + 
				"    [pageSizeOptions]=\"[5, 10, 25, 100]\">\r\n" + 
				"  </mat-paginator>\r\n" + 
				"</div>");
	
		/*
		 * ==============ANGULAR HL LINE-ALL FORM component.CSS FINAL==============
		 */
		ng_line_all_grid_view_component_css.append("/*----------WRITE CSS CODE HERE-------*/\r\n" + 
				"/* Filter */\r\n" + 
				".search {\r\n" + 
				"  min-height: 56px;\r\n" + 
				"  max-height: 20px;\r\n" + 
				"  width: 100px;\r\n" + 
				"  display: flex;\r\n" + 
				"  align-items: center;\r\n" + 
				"  padding: 8px 24px 0;\r\n" + 
				"  font-size: 20px;\r\n" + 
				"  border-radius: 5px;\r\n" + 
				"  justify-content: space-between;\r\n" + 
				"  border-bottom: 1px solid transparent;\r\n" + 
				"}\r\n" + 
				"\r\n" + 
				"/* Mat table */\r\n" + 
				".no-results {\r\n" + 
				"  display: flex;\r\n" + 
				"  justify-content: center;\r\n" + 
				"  padding: 14px;\r\n" + 
				"  font-size: 14px;\r\n" + 
				"  font-style: italic;\r\n" + 
				"}\r\n" + 
				"\r\n" + 
				"#table {\r\n" + 
				"  font-family: \"Trebuchet MS\", Arial, Helvetica, sans-serif;\r\n" + 
				"  font-size: 150%;\r\n" + 
				"  border-collapse: collapse;\r\n" + 
				"  width: 100%;\r\n" + 
				"}\r\n" + 
				"\r\n" + 
				"#table th, #table td {\r\n" + 
				"  border: 1px solid #ddd;\r\n" + 
				"  padding: 18px;\r\n" + 
				"}\r\n" + 
				"#table th {\r\n" + 
				"  padding-top: 12px;\r\n" + 
				"  padding-bottom: 12px;\r\n" + 
				"  text-align: left;\r\n" + 
				"}\r\n" + 
				"\r\n" + 
				"mat-icon {\r\n" + 
				"font-size: 20px;\r\n" + 
				"}");

		/*
		 * ==============ANGULAR HL LINE-ALL FORM component.TS FINAL==============
		 */
		ng_line_all_grid_view_component_ts.append("import { Component, OnInit, ViewChild, ElementRef } from \"@angular/core\";\r\n" + 
				"import { MatPaginator } from \"@angular/material/paginator\";\r\n" + 
				"import { MatSlideToggleChange } from \"@angular/material/slide-toggle\";\r\n" + 
				"import { MatSort } from \"@angular/material/sort\";\r\n" + 
				"import { Router, ActivatedRoute } from \"@angular/router\";\r\n" + 
				"import { " + ng_service_ts_name + " } from '../../" + ng_file_name + ".service';\r\n" + 
				"import { " + ng_line_model_ts_name + " } from '../../" + ng_line_model_ts_name + "';\r\n" + 
				"import { merge, Observable, BehaviorSubject, fromEvent } from 'rxjs';\r\n" + 
				"import { map } from 'rxjs/operators';\r\n" + 
				"import { DataSource } from '@angular/cdk/table';\r\n" + 
				"import { HttpClient } from '@angular/common/http';\r\n" + 
				"\r\n" + 
				"\r\n" + 
				"@Component({\r\n" + 
				"  selector: \"app-all-" + ng_line_file_name + "\",\r\n" + 
				"  templateUrl: \"./all-" + ng_line_file_name + ".component.html\",\r\n" + 
				"  styleUrls: [\"./all-" + ng_line_file_name + ".component.css\"],\r\n" + 
				"})\r\n" + 
				"export class " + ng_line_all_grid_view_component_name + " implements OnInit {\r\n" + 
				"  defaultColumns: string[] = [" + ng_line_all_grid_view_component_ts_display_columns + "];\r\n" +
				"  displayedColumns: string[] = this.defaultColumns;\r\n" + 
				"\r\n" + 
				"  exampleDatabase: " + ng_service_ts_name + " | null;\r\n" + 
				"  " + ng_line_model_ts_name_lower + "DataSource: " + ng_line_model_ts_name + "DataSource | null;\r\n" + 
				"\r\n" + 
				"  @ViewChild(MatPaginator, { static: true }) paginator: MatPaginator;\r\n" + 
				"  @ViewChild(MatSort, { static: true }) sort: MatSort;\r\n" + 
				"  @ViewChild(\"filter\", { static: true }) filter: ElementRef;\r\n" + 
				"\r\n" + 
				"  " + ng_line_model_ts_name_lower + ": " + ng_line_model_ts_name + "[] = [];\r\n" + 
				"  header_id: number;\r\n" + 
				"  line_id: number;\r\n" + 
				"\r\n" + 
				"\r\n" + 
				"  constructor(\r\n" + 
				"    private " + ng_service_ts_name_lower + ": " + ng_service_ts_name + ",\r\n" + 
				"    private httpClient: HttpClient,\r\n" + 
				"    private router: Router,\r\n" + 
				"    private route: ActivatedRoute\r\n" + 
				"  ) {}\r\n" + 
				"\r\n" + 
				"  ngOnInit() {\r\n" + 
				"    this.loadData();\r\n" + 
				"  }\r\n" + 
				"\r\n" + 
				"  loadData() {\r\n" + 
				"    this.exampleDatabase = new " + ng_service_ts_name + "(this.httpClient);\r\n" + 
				"    this." + ng_line_model_ts_name_lower + "DataSource = new " + ng_line_model_ts_name + "DataSource(\r\n" + 
				"      this.exampleDatabase,\r\n" + 
				"      this.paginator,\r\n" + 
				"      this.sort\r\n" + 
				"    );\r\n" + 
				"    fromEvent(this.filter.nativeElement, \"keyup\").subscribe(() => {\r\n" + 
				"      if (!this." + ng_line_model_ts_name_lower + "DataSource) {\r\n" + 
				"        return;\r\n" + 
				"      }\r\n" + 
				"      this." + ng_line_model_ts_name_lower + "DataSource.filter = this.filter.nativeElement.value;\r\n" + 
				"    });\r\n" + 
				"  }\r\n" + 
				"\r\n" + 
				"  //======toggle to show hide empty columns========\r\n" + 
				"  public toggle(event: MatSlideToggleChange) {\r\n" + 
				"    if (event.checked) {\r\n" + 
				"      const columns = this.getEmptyColumns();\r\n" + 
				"      this.displayedColumns = this.defaultColumns.filter(\r\n" + 
				"        (col) => !columns[col]\r\n" + 
				"      );\r\n" + 
				"    } else {\r\n" + 
				"      this.displayedColumns = this.defaultColumns;\r\n" + 
				"    }\r\n" + 
				"  }\r\n" + 
				"\r\n" + 
				"  public getEmptyColumns(): { [key: string]: boolean } {\r\n" + 
				"    const columns = {};\r\n" + 
				"\r\n" + 
				"    this.displayedColumns.forEach((col) => {\r\n" + 
				"      columns[col] = this." + ng_line_model_ts_name_lower + "DataSource.renderedData.every((element) => {\r\n" + 
				"        return !element[col];\r\n" + 
				"      });\r\n" + 
				"    });\r\n" + 
				"\r\n" + 
				"    return columns;\r\n" + 
				"  }\r\n" +
				"\r\n" +
				"  addLines() {\r\n" + 
				"    this.router.navigate([\"../add-line\"], { relativeTo: this.route });\r\n" + 
				"  }\r\n" + 
				"\r\n" + 
				"  gotoUpdate(l_id: number) {\r\n" + 
				"    this.line_id = l_id;\r\n" + 
				"    this.router.navigate([\"../edit-line/\" + this.line_id], { relativeTo: this.route });\r\n" + 
				"  }\r\n" + 
				"\r\n" + 
				"  goToView(l_id: number) {\r\n" + 
				"    this.line_id = l_id;\r\n" + 
				"    this.router.navigate([\"../line-details/\" + this.line_id], { relativeTo: this.route });\r\n" + 
				"  }\r\n" + 
				"\r\n" + 
				"  delete(l_id:number) {\r\n" + 
				"    this.line_id = l_id;\r\n" + 
				"    this.header_id = +localStorage.getItem('header_id');\r\n" + 
				"    //this.header_id = this.route.snapshot.params['id'];\r\n" + 
				"    this." + ng_service_ts_name_lower + ".deleteLineByHeaderIdAndLineId(this.header_id, this.line_id).subscribe(\r\n" + 
				"      (data) => {\r\n" + 
				"        this.loadData();\r\n" + 
				"      },\r\n" + 
				"      (error) => console.log(error)\r\n" + 
				"    );\r\n" + 
				"  }\r\n" + 
				"}\r\n" + 
				"\r\n" + 
				"\r\n" + 
				"//====PAGINATOR|| SEARCH || SORT CODE HERE===\r\n" + 
				"export class " + ng_line_model_ts_name + "DataSource extends DataSource<" + ng_line_model_ts_name + "> {\r\n" + 
				"  _filterChange = new BehaviorSubject(\"\");\r\n" + 
				"\r\n" + 
				"  get filter(): string {\r\n" + 
				"    return this._filterChange.value;\r\n" + 
				"  }\r\n" + 
				"\r\n" + 
				"  set filter(filter: string) {\r\n" + 
				"    this._filterChange.next(filter);\r\n" + 
				"  }\r\n" + 
				"  filteredData: " + ng_line_model_ts_name + "[] = [];\r\n" + 
				"  renderedData: " + ng_line_model_ts_name + "[] = [];\r\n" + 
				"\r\n" + 
				"  constructor(\r\n" + 
				"    private " + ng_service_ts_name_lower + ": " + ng_service_ts_name + ",\r\n" + 
				"    private _paginator: MatPaginator,\r\n" + 
				"    private _sort: MatSort\r\n" + 
				"  ) {\r\n" + 
				"    super();\r\n" + 
				"    this._filterChange.subscribe(() => (this._paginator.pageIndex = 0));\r\n" + 
				"  }\r\n" + 
				"\r\n" + 
				"  connect(): Observable<" + ng_line_model_ts_name + "[]> {\r\n" + 
				"    const displayDataChanges = [\r\n" + 
				"      this." + ng_service_ts_name_lower + ".lineDataChange,\r\n" + 
				"      this._sort.sortChange,\r\n" + 
				"      this._filterChange,\r\n" + 
				"      this._paginator.page,\r\n" + 
				"    ];\r\n" + 
				"    let header_id = localStorage.getItem('header_id');\r\n" +  
				"    this." + ng_service_ts_name_lower + ".getLinesByHeaderId(+header_id);\r\n" + 
				"    return merge(...displayDataChanges).pipe(\r\n" + 
				"      map(() => {\r\n" + 
				"        // Filter data\r\n" + 
				"        this.filteredData = this." +ng_service_ts_name_lower + ".line_data\r\n" + 
				"          .slice()\r\n" + 
				"          .filter((" + ng_line_model_ts_name_lower + " : " + ng_line_model_ts_name + ") => {\r\n" + 
				"            const searchStr = (\r\n" + ng_line_all_grid_view_component_search +
				"			 ).toLowerCase();\r\n" + 
				"            return searchStr.indexOf(this.filter.toLowerCase()) !== -1;\r\n" + 
				"          });\r\n" + 
				"\r\n" + 
				"        // Sort filtered data\r\n" + 
				"        const sortedData = this.sortData(this.filteredData.slice());\r\n" + 
				"\r\n" +  
				"        const startIndex = this._paginator.pageIndex * this._paginator.pageSize;\r\n" + 
				"        this.renderedData = sortedData.splice(\r\n" + 
				"          startIndex,\r\n" + 
				"          this._paginator.pageSize\r\n" + 
				"        );\r\n" + 
				"        return this.renderedData;\r\n" + 
				"      })\r\n" + 
				"    );\r\n" + 
				"  }\r\n" + 
				"  disconnect() {}\r\n" + 
				"\r\n" + 
				"  sortData(data: " + ng_line_model_ts_name + "[]): " + ng_line_model_ts_name + "[] {\r\n" + 
				"    if (!this._sort.active || this._sort.direction === \"\") {\r\n" + 
				"      return data;\r\n" + 
				"    }\r\n" + 
				"    return data.sort((a, b) => {\r\n" + 
				"      let propertyA: Date | number | string = \"\";\r\n" + 
				"      let propertyB: Date | number | string = \"\";\r\n" + 
				"\r\n" + 
				"      switch (this._sort.active) {\r\n" + ng_line_all_grid_view_component_ts_sort_string +
				"\r\n" +
				"}\r\n" +
				"\r\n" +
				"      const valueA = isNaN(+propertyA) ? propertyA : +propertyA;\r\n" + 
				"      const valueB = isNaN(+propertyB) ? propertyB : +propertyB;\r\n" + 
				"\r\n" + 
				"      return (\r\n" + 
				"        (valueA < valueB ? -1 : 1) * (this._sort.direction === \"asc\" ? 1 : -1)\r\n" + 
				"      );\r\n" + 
				"    });\r\n" + 
				"  }\r\n" + 
				"}");
		
		
		/*
		 * ==============ANGULAR HL LINE-EDIT FORM component.HTML FINAL==============
		 */
		ng_line_edit_form_component_html.append("<div class=\"container\">\r\n" + 
				"    <div class=\"line-only\">\r\n" + 
				"        <h3 style=\"text-align: center\">UPDATE " + ng_line_model_ts_name.toUpperCase() + " FORM</h3>\r\n" + 
				"        <mat-accordion class=\"example-headers-align\" multi>\r\n" + 
				"            <form class=\"form\" (ngSubmit)=\"onSubmit()\">\r\n" + 
				"                <mat-expansion-panel [expanded]=\"step === 0\" (opened)=\"setStep(0)\" hideToggle>\r\n" + 
				"                    <mat-expansion-panel-header>\r\n" + 
				"                        <mat-panel-title>" + ng_line_model_ts_name.toUpperCase() + " ITEMS</mat-panel-title>\r\n" + 
				"                        <mat-panel-description>\r\n" + 
				"                            update " + ng_line_model_ts_name + " fields\r\n" + 
				"                        </mat-panel-description>\r\n" + 
				"                    </mat-expansion-panel-header>\r\n" + 
				"                    <!-- DEFAULT LINE FORM FIELDS -->\r\n" + 
				"                    <table class=\"header-table\" cellspacing=\"0\">\r\n" +
										ng_line_edit_form_component_html_datatype_attribute +
				"                    </table>\r\n" + 
				"                </mat-expansion-panel>\r\n" + 
				"\r\n" + 
				"                <!-- LINE EXTENSION FORM FIELDS -->\r\n" + 
				"                <mat-expansion-panel>\r\n" + 
				"                    <mat-expansion-panel-header>\r\n" + 
				"                        <mat-panel-title>" + ng_line_model_ts_name.toUpperCase() + " EXTENSION ITEMS</mat-panel-title>\r\n" + 
				"                        <mat-panel-description>\r\n" + 
				"                            update " + ng_line_model_ts_name + " extension fields\r\n" + 
				"                        </mat-panel-description>\r\n" + 
				"                    </mat-expansion-panel-header>\r\n" + 
				"\r\n" + 
				"                    <table class=\"line-extension-table\" cellspacing=\"0\">\r\n" + 
										ng_line_edit_form_component_html_flex_attribute + "\r\n" + 
				"                    </table>\r\n" + 
				"                </mat-expansion-panel>\r\n" + 
				"                <button mat-raised-button color=\"primary\" style=\"left: 86%;top: 8px\">UPDATE</button>\r\n" + 
				"            </form>\r\n" + 
				"        </mat-accordion>\r\n" + 
				"        <br>\r\n" + 
				"    </div>\r\n" + 
				"</div>");
		
		/*
		 * ==============ANGULAR HL LINE-EDIT FORM component.CSS FINAL==============
		 */
		ng_line_edit_form_component_css.append("/*----------ADD CSS STYLE CODE HERE-----------*/\r\n");

		/*
		 * ==============ANGULAR HL LINE-EDIT FORM component.TS FINAL==============
		 */
		ng_line_edit_form_component_ts.append("import { OnInit, Component, ViewChild } from \"@angular/core\";\r\n" + 
				"import { MatAccordion } from \"@angular/material/expansion\";\r\n" + 
				"import { " + ng_line_model_ts_name + " } from \"../../" + ng_line_model_ts_name + "\";\r\n" + 
				"import { Router, ActivatedRoute } from \"@angular/router\";\r\n" + 
				"import { " + ng_service_ts_name + " } from \"../../" + ng_file_name + ".service\";\r\n" + 
				"\r\n" + 
				"@Component({\r\n" + 
				"  selector: \"app-edit-" + ng_line_file_name + "\",\r\n" + 
				"  templateUrl: \"./edit-" + ng_line_file_name + ".component.html\",\r\n" + 
				"  styleUrls: [\"./edit-" + ng_line_file_name + ".component.css\"],\r\n" + 
				"})\r\n" + 
				"export class " + ng_line_edit_component_name + " implements OnInit {\r\n" + 
				"  @ViewChild(MatAccordion) accordion2: MatAccordion;\r\n" + 
				"  step = 0;\r\n" + 
				"\r\n" + 
				"  setStep(index: number) {\r\n" + 
				"    this.step = index;\r\n" + 
				"  }\r\n" + 
				"\r\n" + 
				"  " + ng_model_ts_name_lower + "Id: number;\r\n" + 
				"  " + ng_line_model_ts_name_lower + "Id: number;\r\n" +  
				"  " + ng_line_model_ts_name_lower + ": " + ng_line_model_ts_name + ";\r\n" + 
				"  updated = false;\r\n" + 
				"\r\n" + 
				"  constructor(\r\n" + 
				"    private router: Router,\r\n" + 
				"    private route: ActivatedRoute,\r\n" + 
				"    private " + ng_service_ts_name_lower + ": " + ng_service_ts_name + "\r\n" + 
				"  ) {}\r\n" + 
				"\r\n" + 
				"  ngOnInit() {\r\n" + 
				"    this." + ng_line_model_ts_name_lower + " = new " + ng_line_model_ts_name + "();\r\n" + 
				"    // GET PARAM VALUE AND MAP IT TO LOCAL VARIABLE\r\n" + 
				"    this.route.paramMap.subscribe( params => {\r\n" +  
				"      this." + ng_line_model_ts_name_lower + "Id = +params.get('l_id');\r\n" + 
				"    });\r\n" + 
				"    this." + ng_model_ts_name_lower + "Id = +localStorage.getItem(\"header_id\");\r\n" + 
				"\r\n" + 
				"    console.log(\"LINE-EDIT COMPONENT HEADER ID = \" + this." + ng_model_ts_name_lower + "Id + \"\\nLINE ID = \" + this." + ng_line_model_ts_name_lower + "Id);\r\n" + 
				"    this." + ng_service_ts_name_lower + ".getLineByHeaderIdAndLineId(this." + ng_model_ts_name_lower + "Id, this." + ng_line_model_ts_name_lower + "Id)\r\n" + 
				"      .subscribe(res => {\r\n" + 
				"        console.log('LINE VALUE BY ID ::: ', res);\r\n" + 
				"        this." + ng_line_model_ts_name_lower + " = res;\r\n" + 
				"      }, err => {\r\n" + 
				"        console.log(err);\r\n" + 
				"      });\r\n" + 
				"  }\r\n" + 
				"\r\n" + 
				"  update" + ng_line_model_ts_name + "() {\r\n" + 
				"    console.log('LINE-EDIT COMPONENT LINE ID', this." + ng_line_model_ts_name_lower + "Id);\r\n" + 
				"    this." + ng_service_ts_name_lower + "\r\n" + 
				"      .updateLineByHeaderIdAndLineId(this." + ng_model_ts_name_lower + "Id, this." + ng_line_model_ts_name_lower + "Id, this." + ng_line_model_ts_name_lower + ")\r\n" + 
				"      .subscribe(\r\n" + 
				"        (data) => {\r\n" + 
				"          console.log('all line component data : ',data);\r\n" + 
				"        },\r\n" + 
				"        (err) => {\r\n" + 
				"          console.log(err);\r\n" + 
				"        }\r\n" + 
				"      );\r\n" + 
				"    this." + ng_line_model_ts_name_lower + " = new " + ng_line_model_ts_name + "();\r\n" + 
				"  }\r\n" + 
				"\r\n" + 
				"  onSubmit() {\r\n" + 
				"    this.updated = true;\r\n" + 
				"    this.update" + ng_line_model_ts_name + "();\r\n" + 
				"    this.router.navigate([\"../../all-line\"], { relativeTo: this.route });\r\n" + 
				"  }\r\n" + 
				"}\r\n");
		
		/*
		 * ==============ANGULAR HL LINE-DETAILS FORM component.HTML FINAL==============
		 */
		ng_line_read_only_form_component_html.append("<div class=\"container\">\r\n" + 
				"  <h4 style=\"text-align: center;\">" + ng_line_model_ts_name.toUpperCase() + " VALUES</h4>\r\n" + 
				"  <button mat-raised-button style=\"right: auto;\" (click)=\"back()\">BACK TO ALL TABLE</button>\r\n" +
				"  <div *ngIf=\"" + ng_line_model_ts_name_lower + "\">\r\n" + 
				"    <table class=\"table\">\r\n" + 
				"      <tr>\r\n" +
						ng_line_read_only_form_component_html_table_th + "\r\n" +
				"      </tr>\r\n" + 
				"      <tr>\r\n" +
						ng_line_read_only_form_component_html_table_td +
				"      </tr>\r\n" + 
				"    </table>\r\n" + 
				"  </div>\r\n" + 
				"</div>");
		
		/*
		 * ==============ANGULAR HL LINE-DETAILS FORM component.CSS FINAL==============
		 */
		ng_line_read_only_form_component_css.append("/*---------WRITE CSS CODE HERE--------------*/\r\n");

		/*
		 * ==============ANGULAR HL LINE-DETAILS FORM component.TS FINAL==============
		 */
		ng_line_read_only_form_component_ts.append("import { Component, OnInit } from \"@angular/core\";\r\n" + 
				"import { Router, ActivatedRoute } from \"@angular/router\";\r\n" + 
				"\r\n" + 
				"//====== LINE CLASS IMPORT HERE=====;\r\n" + 
				"import { " + ng_service_ts_name + " } from \"../../" + ng_file_name + ".service\";\r\n" + 
				"import { " + ng_line_model_ts_name + " } from \"../../" + ng_line_model_ts_name + "\";\r\n" + 
				"\r\n" + 
				"@Component({\r\n" + 
				"  selector: \"app-" + ng_line_file_name + "-details\",\r\n" + 
				"  templateUrl: \"./" + ng_line_file_name + "-details.component.html\",\r\n" + 
				"  styleUrls: [\"./" + ng_line_file_name + "-details.component.css\"],\r\n" + 
				"})\r\n" + 
				"export class " + ng_line_read_only_component_name + " implements OnInit {\r\n" + 
				"  constructor(\r\n" + 
				"    private " + ng_service_ts_name_lower + ": " + ng_service_ts_name + ",\r\n" + 
				"    private router: Router,\r\n" + 
				"    private route: ActivatedRoute\r\n" + 
				"  ) {}\r\n" + 
				"\r\n" + 
				"  " + ng_line_model_ts_name_lower + ": " + ng_line_model_ts_name + ";\r\n" + 
				"\r\n" + 
				"  ngOnInit() {\r\n" + 
				"    this.get" + ng_line_model_ts_name + "ById();\r\n" + 
				"  }\r\n" + 
				"\r\n" + 
				"  get" + ng_line_model_ts_name + "ById() {\r\n" + 
				"    const headerId = +localStorage.getItem(\"header_id\");\r\n" + 
				"    const lineId = this.route.snapshot.params[\"l_id\"];\r\n" + 
				"    //Get HEADER DATA\r\n" + 
				"    this." + ng_service_ts_name_lower + ".getLineByHeaderIdAndLineId(headerId, lineId).subscribe(\r\n" + 
				"      (data) => {\r\n" + 
				"        this." + ng_line_model_ts_name_lower + " = data;\r\n" + 
				"        console.log(\"LINE-DETAILS COMPONENT\", data);\r\n" + 
				"      },\r\n" + 
				"      (err) => {\r\n" + 
				"        console.log(err);\r\n" + 
				"      }\r\n" + 
				"    );\r\n" + 
				"  }\r\n" + 
				"\r\n" + 
				"  back() {\r\n" + 
				"    this.router.navigate([\"../../all-line\"], { relativeTo: this.route });\r\n" + 
				"  }\r\n" + 
				"}\r\n" + 
				"");
		


		/*
		 * ==============ANGULAR HL LINE-EXTENSION component.HTML FINAL==============
		 */
		ng_line_extension_add_form_component_html.append("<div [formGroup]=\"" + ng_line_model_ts_name_lower + "Form\">\r\n" + 
			ng_line_extension_add_form_component_html_flex_attribute +
				"</div>");
		
		/*
		 * ==============ANGULAR HL LINE-EXTENSION component.TS FINAL==============
		 */
		ng_line_extension_add_form_component_ts.append("import { Component, OnInit, Input } from \"@angular/core\";\r\n" + 
				"import { FormGroup } from \"@angular/forms\";\r\n" + 
				"\r\n" + 
				"@Component({\r\n" + 
				"  selector: \"app-ext-add-" + ng_line_file_name + "\",\r\n" + 
				"  templateUrl: \"./ext-add-" + ng_line_file_name + ".component.html\",\r\n" + 
				"})\r\n" + 
				"export class " + ng_line_extension_add_component_name + " implements OnInit {\r\n" + 
				"  @Input() " + ng_line_model_ts_name_lower + "Form: FormGroup;\r\n" + 
				"  constructor() {}\r\n" + 
				"  ngOnInit() {}\r\n" + 
				"}\r\n" + 
				"");
		/*--------------------H-L CODE END HERE----------------------------------*/
		
		
		

		// ================= FILE WRITER CODE START HERE============================
		
		try {
			// <<<<<<<<<<<<<FRONT-END DIRECTORY>>>>>>>>>>>>>
			String frontEndDir = angularProjectPath.concat("/frontend/");

			// create folder for specific project
			String ngProjectDir = frontEndDir.concat("src/app/admin/" + ng_folder_name);
			File directory = new File(ngProjectDir);
			if (!directory.exists()) {
				directory.mkdir();
			}

			// FRONT-END Model.ts FILE
			File ngModelFile = new File(ngProjectDir + "/" + ng_model_ts_name + ".ts");
			System.out.println("FRONT-END model.ts directory name = " + ngModelFile);

			// FRONT-END component.html FILE
			File ngHtmlFile = new File(ngProjectDir + "/" + ng_file_name + ".component.html");
			System.out.println("FRONT-END component.html directory name = " + ngHtmlFile);

			// FRONT-END component.css FILE
			File ngCssFile = new File(ngProjectDir + "/" + ng_file_name + ".component.css");
			System.out.println("FRONT-END component.css directory name = " + ngCssFile);

			// FRONT-END component.ts FILE
			File ngTsFile = new File(ngProjectDir + "/" + ng_file_name + ".component.ts");
			System.out.println("FRONT-END component.ts directory name = " + ngTsFile);

			// FRONT-END module.ts FILE
			File ngModuleFile = new File(ngProjectDir + "/" + ng_file_name + ".module.ts");
			System.out.println("FRONT-END model.ts directory name = " + ngModuleFile);

			// FRONT-END service.ts FILE
			File ngServiceFile = new File(ngProjectDir + "/" + ng_file_name + ".service.ts");
			System.out.println("FRONT-END service.ts directory name = " + ngServiceFile);

			// FRONT-END routing.module.ts FILE
			File ngRoutingFile = new File(ngProjectDir + "/" + ng_file_name + "-routing.module.ts");
			System.out.println("FRONT-END routing.module.ts directory name = " + ngRoutingFile);
			

			// =======<<<<<<<<<<<<<<CRUD DIRECTORY & FILES>>>>>>>>>>>>>>=======
			
			String ngAllDir = ngProjectDir.concat("/all");
			String ngAddDir = ngProjectDir.concat("/add");
			String ngEditDir = ngProjectDir.concat("/edit");
			String ngDetailsDir = ngProjectDir.concat("/details");

			// CREATE ALL FOLDER
			File sub_directory_all = new File(ngAllDir);
			if (!sub_directory_all.exists()) {
				sub_directory_all.mkdir();
			}
			// all.component.html FILE
			File ngAllHtmlFile = new File(sub_directory_all + "/all-" + ng_file_name + ".component.html");
			System.out.println("SUB DIRECTORY -- ALL component.html directory name = " + ngAllHtmlFile);
			// all.component.css File
			File ngAllCssFile = new File(sub_directory_all + "/all-" + ng_file_name + ".component.css");
			System.out.println("SUB DIRECTORY -- ALL component.css directory name = " + ngAllCssFile);
			// all.component.ts FILE
			File ngAllTsFile = new File(sub_directory_all + "/all-" + ng_file_name + ".component.ts");
			System.out.println("SUB DIRECTORY -- ALL component.ts directory name = " + ngAllTsFile);

			// CREATE ADD FOLDER
			File sub_directory_add = new File(ngAddDir);
			if (!sub_directory_add.exists()) {
				sub_directory_add.mkdir();
			}
			// add.component.html File
			File ngAddHtmlFile = new File(sub_directory_add + "/add-" + ng_file_name + ".component.html");
			System.out.println("SUB DIRECTORY -- ADD -- component.html directory name = " + ngAddHtmlFile);
			// add.component.css File
			File ngAddCssFile = new File(sub_directory_add + "/add-" + ng_file_name + ".component.css");
			System.out.println("SUB DIRECTORY -- ADD -- component.css directory name = " + ngAddCssFile);
			// add.component.html File
			File ngAddTsFile = new File(sub_directory_add + "/add-" + ng_file_name + ".component.ts");
			System.out.println("SUB DIRECTORY -- ADD -- component.ts directory name = " + ngAddTsFile);

			// CREATE EDIT FOLDER
			File sub_directory_edit = new File(ngEditDir);
			if (!sub_directory_edit.exists()) {
				sub_directory_edit.mkdir();
			}
			// edit.component.html File
			File ngEditHtmlFile = new File(sub_directory_edit + "/edit-" + ng_file_name + ".component.html");
			System.out.println("SUB DIRECTORY -- EDIT -- component.html directory name = " + ngEditHtmlFile);
			// edit.component.css File
			File ngEditCssFile = new File(sub_directory_edit + "/edit-" + ng_file_name + ".component.css");
			System.out.println("SUB DIRECTORY -- EDIT -- component.css directory name = " + ngEditCssFile);
			// edit.component.html File
			File ngEditTsFile = new File(sub_directory_edit + "/edit-" + ng_file_name + ".component.ts");
			System.out.println("SUB DIRECTORY -- EDIT -- component.ts directory name = " + ngEditTsFile);

			// CREATE DETAILS FOLDER
			File sub_directory_details = new File(ngDetailsDir);
			if (!sub_directory_details.exists()) {
				sub_directory_details.mkdir();
			}
			// details.component.html File
			File ngDetailsHtmlFile = new File(sub_directory_details + "/" + ng_file_name + "-details.component.html");
			System.out.println("SUB DIRECTORY -- DETAILS -- component.html directory name = " + ngDetailsHtmlFile);
			// details.component.css File
			File ngDetailsCssFile = new File(sub_directory_details + "/" + ng_file_name + "-details.component.css");
			System.out.println("SUB DIRECTORY -- DETAILS -- component.css directory name = " + ngDetailsCssFile);
			// details.component.html File
			File ngDetailsTsFile = new File(sub_directory_details + "/" + ng_file_name + "-details.component.ts");
			System.out.println("SUB DIRECTORY -- DETAILS -- component.ts directory name = " + ngDetailsTsFile);

			FileWriter fw = null;
			BufferedWriter bw = null;

			// model.ts
			if (!ngModelFile.exists()) {
				ngModelFile.createNewFile();
			}
			fw = new FileWriter(ngModelFile.getAbsoluteFile());
			bw = new BufferedWriter(fw);
			bw.write(ng_header_model_ts.toString());
			bw.close();

			// app.component.html
			if (!ngHtmlFile.exists()) {
				ngHtmlFile.createNewFile();
			}
			fw = new FileWriter(ngHtmlFile.getAbsoluteFile());
			bw = new BufferedWriter(fw);
			bw.write(ng_component_html.toString());
			bw.close();

			// app.component.css
			if (!ngCssFile.exists()) {
				ngCssFile.createNewFile();
			}
			fw = new FileWriter(ngCssFile.getAbsoluteFile());
			bw = new BufferedWriter(fw);
			bw.write(ng_component_css.toString());
			bw.close();

			// app.component.ts
			if (!ngTsFile.exists()) {
				ngTsFile.createNewFile();
			}
			fw = new FileWriter(ngTsFile.getAbsoluteFile());
			bw = new BufferedWriter(fw);
			bw.write(ng_component_ts.toString());
			bw.close();

			// app.module.ts
			if (!ngModuleFile.exists()) {
				ngModelFile.mkdirs();
			}
			fw = new FileWriter(ngModuleFile.getAbsoluteFile());
			bw = new BufferedWriter(fw);
			bw.write(ng_module_ts.toString());
			bw.close();

			// app.service.ts
			if (!ngServiceFile.exists()) {
				ngServiceFile.createNewFile();
			}
			fw = new FileWriter(ngServiceFile.getAbsoluteFile());
			bw = new BufferedWriter(fw);
			bw.write(ng_service_ts.toString());
			bw.close();

			// app-routing.module.ts
			if (!ngRoutingFile.exists()) {
				ngRoutingFile.createNewFile();
			}
			fw = new FileWriter(ngRoutingFile.getAbsoluteFile());
			bw = new BufferedWriter(fw);
			bw.write(ng_routing_module_ts.toString());
			bw.close();
			
			

			// ===================CRUD FILES===============
			// WRITE ADD CODE HERE...
			// add-form.component.HTML
			if (!ngAddHtmlFile.exists()) {
				ngAddHtmlFile.createNewFile();
			}
			fw = new FileWriter(ngAddHtmlFile.getAbsoluteFile());
			bw = new BufferedWriter(fw);
			bw.write(ng_add_form_component_html.toString());
			bw.close();

			// add-form.component.CSS
			if (!ngAddCssFile.exists()) {
				ngAddCssFile.createNewFile();
			}
			fw = new FileWriter(ngAddCssFile.getAbsoluteFile());
			bw = new BufferedWriter(fw);
			bw.write(ng_add_form_component_css.toString());
			bw.close();

			// add-form.component.TS
			if (!ngAddTsFile.exists()) {
				ngAddTsFile.createNewFile();
			}
			fw = new FileWriter(ngAddTsFile.getAbsoluteFile());
			bw = new BufferedWriter(fw);
			bw.write(ng_add_form_component_ts.toString());
			bw.close();

			// WRITE EDIT CODE HERE...
			// edit-form.component.HTML
			if (!ngEditHtmlFile.exists()) {
				ngEditHtmlFile.createNewFile();
			}
			fw = new FileWriter(ngEditHtmlFile.getAbsoluteFile());
			bw = new BufferedWriter(fw);
			bw.write(ng_edit_form_component_html.toString());
			bw.close();

			// edit-form.component.CSS
			if (!ngEditCssFile.exists()) {
				ngEditCssFile.createNewFile();
			}
			fw = new FileWriter(ngEditCssFile.getAbsoluteFile());
			bw = new BufferedWriter(fw);
			bw.write(ng_edit_form_component_css.toString());
			bw.close();

			// edit-form.component.TS
			if (!ngEditTsFile.exists()) {
				ngEditTsFile.createNewFile();
			}
			fw = new FileWriter(ngEditTsFile.getAbsoluteFile());
			bw = new BufferedWriter(fw);
			bw.write(ng_edit_form_component_ts.toString());
			bw.close();

			// WRITE DETAILS CODE HERE...
			// read-only-form.component.HTML
			if (!ngDetailsHtmlFile.exists()) {
				ngDetailsHtmlFile.createNewFile();
			}
			fw = new FileWriter(ngDetailsHtmlFile.getAbsoluteFile());
			bw = new BufferedWriter(fw);
			bw.write(ng_read_only_form_component_html.toString());
			bw.close();

			// read-only-form.component.CSS
			if (!ngDetailsCssFile.exists()) {
				ngDetailsCssFile.createNewFile();
			}
			fw = new FileWriter(ngDetailsCssFile.getAbsoluteFile());
			bw = new BufferedWriter(fw);
			bw.write(ng_edit_form_component_css.toString());
			bw.close();

			// read-only-form.component.TS
			if (!ngDetailsTsFile.exists()) {
				ngDetailsTsFile.createNewFile();
			}
			fw = new FileWriter(ngDetailsTsFile.getAbsoluteFile());
			bw = new BufferedWriter(fw);
			bw.write(ng_read_only_form_component_ts.toString());
			bw.close();

			// WRITE ALL (GRID-VIEW) CODE HERE
			// grid-view-form.component.HTML
			if (!ngAllHtmlFile.exists()) {
				ngAllHtmlFile.createNewFile();
			}
			fw = new FileWriter(ngAllHtmlFile.getAbsoluteFile());
			bw = new BufferedWriter(fw);
			bw.write(ng_all_grid_view_component_html.toString());
			bw.close();

			// gtid-view-form.component.CSS
			if (!ngAllCssFile.exists()) {
				ngAllCssFile.createNewFile();
			}
			fw = new FileWriter(ngAllCssFile.getAbsoluteFile());
			bw = new BufferedWriter(fw);
			bw.write(ng_all_grid_view_component_css.toString());
			bw.close();

			// add-form.component.TS
			if (!ngAllTsFile.exists()) {
				ngAllTsFile.createNewFile();
			}
			fw = new FileWriter(ngAllTsFile.getAbsoluteFile());
			bw = new BufferedWriter(fw);
			bw.write(ng_all_grid_view_component_ts.toString());
			bw.close();
			
			// =============<<<<<<<<HEADER EXTENSION DIRECTORY & FILES>>>>>>>>================

			// EXTENDION dir
			String ngExtensionDir = ngProjectDir.concat("/extPages"); // ng_extension_add_form_component_ts

			// create EXTENSION dir
			File sub_directory_extension = new File(ngExtensionDir);
			if (!sub_directory_extension.exists()) {
				sub_directory_extension.mkdir();
			}
			// ext-add.component.html FILE
			File ngExtAllHtmlFile = new File(sub_directory_extension + "/ext-add-" + ng_file_name + ".component.html");
			System.out.println("SUB DIRECTORY -- EXTENSION ADD component.html directory name = " + ngExtAllHtmlFile);

			// ext-add.component.ts FILE
			File ngExtAllTsFile = new File(sub_directory_extension + "/ext-add-" + ng_file_name + ".component.ts");
			System.out.println("SUB DIRECTORY -- EXTENSION ADD component.ts directory name = " + ngExtAllTsFile);

			if (!ngExtAllHtmlFile.exists()) {
				ngExtAllHtmlFile.createNewFile();
			}
			fw = new FileWriter(ngExtAllHtmlFile.getAbsoluteFile());
			bw = new BufferedWriter(fw);
			bw.write(ng_extension_add_form_component_html.toString());
			bw.close();
			if (!ngExtAllTsFile.exists()) {
				ngExtAllTsFile.createNewFile();
			}
			fw = new FileWriter(ngExtAllTsFile.getAbsoluteFile());
			bw = new BufferedWriter(fw);
			bw.write(ng_extension_add_form_component_ts.toString());
			bw.close();


			/*===========<<<<<<FRONT-END HEADER-LINE FILES>>>>>>================*/
			if (form_type.equalsIgnoreCase("HEADER_LINE")) {
				
				// FRONT-END HEADER-LINE (LINE-MODEL TS FILE)
				File ngLineModelFile = new File(ngProjectDir + "/" + ng_line_model_ts_name + ".ts");
				System.out.println("FRONT-END LINE Model_L.ts directory name = " + ngLineModelFile);
				
				if (!ngLineModelFile.exists()) {
					ngLineModelFile.createNewFile();
				}
				fw = new FileWriter(ngLineModelFile.getAbsoluteFile());
				bw = new BufferedWriter(fw);
				bw.write(ng_line_model_ts.toString());
				bw.close();
				
				
				// CREATE LINE COMPONENTS FOLDER
				String ngLineDir = ngProjectDir.concat("/" + ng_line_folder_name);
				System.out.println("LINE DIRECTORY PATH " + ngLineDir);
				File line_directory = new File(ngLineDir);
				if (!line_directory.exists()) {
					line_directory.mkdir();
				}
				
				// =======<<<<<<<<<<<<<<H-L LINE CRUD DIRECTORY & FILES>>>>>>>>>>>>>>=======
				String ngAllLineDir = ngLineDir.concat("/all");
				String ngAddLineDir = ngLineDir.concat("/add");
				String ngEditLineDir = ngLineDir.concat("/edit");
				String ngDetailsLineDir = ngLineDir.concat("/details");
				String ngExtensionLineDir = ngLineDir.concat("/extLinePages");	// EXTENSION FILE
				
				// CREATE LINE ALL FOLDER
				File line_sub_directory_all = new File(ngAllLineDir);
				if (!line_sub_directory_all.exists()) {
					line_sub_directory_all.mkdir();
				}
				// all-line.component.html FILE
				File ngLineAllHtmlFile = new File(line_sub_directory_all + "/all-" + ng_line_file_name + ".component.html");
				System.out.println("LINE SUB DIRECTORY -- ALL-LINE component.html directory name = " + ngLineAllHtmlFile);
				// all-line.component.css File
				File ngLineAllCssFile = new File(line_sub_directory_all + "/all-" + ng_line_file_name + ".component.css");
				System.out.println("LINE SUB DIRECTORY -- ALL-LINE component.css directory name = " + ngLineAllCssFile);
				// all-line.component.ts FILE
				File ngLineAllTsFile = new File(line_sub_directory_all + "/all-" + ng_line_file_name + ".component.ts");
				System.out.println("LINE SUB DIRECTORY -- ALL-LINE component.ts directory name = " + ngLineAllTsFile);

				// CREATE LINE ADD FOLDER
				File line_sub_directory_add = new File(ngAddLineDir);
				if (!line_sub_directory_add.exists()) {
					line_sub_directory_add.mkdir();
				}
				// add.component.html File
				File ngLineAddHtmlFile = new File(line_sub_directory_add + "/add-" + ng_line_file_name + ".component.html");
				System.out.println("LINE SUB DIRECTORY -- LINE-ADD -- component.html directory name = " + ngLineAddHtmlFile);
				// add.component.css File
				File ngLineAddCssFile = new File(line_sub_directory_add + "/add-" + ng_line_file_name + ".component.css");
				System.out.println("LINE SUB DIRECTORY -- LINE-ADD -- component.css directory name = " + ngLineAddCssFile);
				// add.component.html File
				File ngLineAddTsFile = new File(line_sub_directory_add + "/add-" + ng_line_file_name + ".component.ts");
				System.out.println("LINE SUB DIRECTORY -- LINE-ADD -- component.ts directory name = " + ngLineAddTsFile);

				// CREATE LINE EDIT FOLDER
				File line_sub_directory_edit = new File(ngEditLineDir);
				if (!line_sub_directory_edit.exists()) {
					line_sub_directory_edit.mkdir();
				}
				// edit.component.html File
				File ngLineEditHtmlFile = new File(line_sub_directory_edit + "/edit-" + ng_line_file_name + ".component.html");
				System.out.println("LINE SUB DIRECTORY -- LINE-EDIT -- component.html directory name = " + ngLineEditHtmlFile);
				// edit.component.css File
				File ngLineEditCssFile = new File(line_sub_directory_edit + "/edit-" + ng_line_file_name + ".component.css");
				System.out.println("LINE SUB DIRECTORY -- LINE-EDIT -- component.css directory name = " + ngLineEditCssFile);
				// edit.component.html File
				File ngLineEditTsFile = new File(line_sub_directory_edit + "/edit-" + ng_line_file_name + ".component.ts");
				System.out.println("LINE SUB DIRECTORY -- LINE-EDIT -- component.ts directory name = " + ngLineEditTsFile);

				// CREATE LINE DETAILS FOLDER
				File line_sub_directory_details = new File(ngDetailsLineDir);
				if (!line_sub_directory_details.exists()) {
					line_sub_directory_details.mkdir();
				}
				// details.component.html File
				File ngLineDetailsHtmlFile = new File(line_sub_directory_details + "/" + ng_line_file_name + "-details.component.html");
				System.out.println("LINE SUB DIRECTORY -- LINE-DETAILS -- component.html directory name = " + ngLineDetailsHtmlFile);
				// details.component.css File
				File ngLineDetailsCssFile = new File(line_sub_directory_details + "/" + ng_line_file_name + "-details.component.css");
				System.out.println("LINE SUB DIRECTORY -- LINE-DETAILS -- component.css directory name = " + ngLineDetailsCssFile);
				// details.component.html File
				File ngLineDetailsTsFile = new File(line_sub_directory_details + "/" + ng_line_file_name + "-details.component.ts");
				System.out.println("LINE SUB DIRECTORY -- LINE-DETAILS -- component.ts directory name = " + ngLineDetailsTsFile);

				// CREATE LINE EXTENSION FOLDER
				File line_sub_directory_extension = new File(ngExtensionLineDir);
				if (!line_sub_directory_extension.exists()) {
					line_sub_directory_extension.mkdir();
				}
				// details.component.html File
				File ngLineExtensionHtmlFile = new File(line_sub_directory_extension + "/ext-add-" + ng_line_file_name + ".component.html");
				System.out.println("LINE SUB DIRECTORY -- LINE-EXTENSION -- component.html directory name = " + ngLineExtensionHtmlFile);
				// details.component.html File
				File ngLineExtensionTsFile = new File(line_sub_directory_extension + "/ext-add-" + ng_line_file_name + ".component.ts");
				System.out.println("LINE SUB DIRECTORY -- LINE-EXTENSION -- component.ts directory name = " + ngLineExtensionTsFile);
				
				// =========================LINE CRUD FILES===================
				// WRITE LINE-ADD CODE HERE...
				// add-line-form.component.HTML
				if (!ngLineAddHtmlFile.exists()) {
					ngLineAddHtmlFile.createNewFile();
				}
				fw = new FileWriter(ngLineAddHtmlFile.getAbsoluteFile());
				bw = new BufferedWriter(fw);
				bw.write(ng_line_add_form_component_html.toString());
				bw.close();
				// add-line-form.component.CSS
				if (!ngLineAddCssFile.exists()) {
					ngLineAddCssFile.createNewFile();
				}
				fw = new FileWriter(ngLineAddCssFile.getAbsoluteFile());
				bw = new BufferedWriter(fw);
				bw.write(ng_line_add_form_component_css.toString());
				bw.close();

				// add-line-form.component.TS
				if (!ngLineAddTsFile.exists()) {
					ngLineAddTsFile.createNewFile();
				}
				fw = new FileWriter(ngLineAddTsFile.getAbsoluteFile());
				bw = new BufferedWriter(fw);
				bw.write(ng_line_add_form_component_ts.toString());
				bw.close();

				// WRITE LINE-EDIT CODE HERE...
				// edit-line-form.component.HTML
				if (!ngLineEditHtmlFile.exists()) {
					ngLineEditHtmlFile.createNewFile();
				}
				fw = new FileWriter(ngLineEditHtmlFile.getAbsoluteFile());
				bw = new BufferedWriter(fw);
				bw.write(ng_line_edit_form_component_html.toString());
				bw.close();

				// edit-line-form.component.CSS
				if (!ngLineEditCssFile.exists()) {
					ngLineEditCssFile.createNewFile();
				}
				fw = new FileWriter(ngLineEditCssFile.getAbsoluteFile());
				bw = new BufferedWriter(fw);
				bw.write(ng_line_edit_form_component_css.toString());
				bw.close();

				// edit-line-form.component.TS
				if (!ngLineEditTsFile.exists()) {
					ngLineEditTsFile.createNewFile();
				}
				fw = new FileWriter(ngLineEditTsFile.getAbsoluteFile());
				bw = new BufferedWriter(fw);
				bw.write(ng_line_edit_form_component_ts.toString());
				bw.close();

				// WRITE LINE-DETAILS CODE HERE...
				// read-only-line-form.component.HTML
				if (!ngLineDetailsHtmlFile.exists()) {
					ngLineDetailsHtmlFile.createNewFile();
				}
				fw = new FileWriter(ngLineDetailsHtmlFile.getAbsoluteFile());
				bw = new BufferedWriter(fw);
				bw.write(ng_line_read_only_form_component_html.toString());
				bw.close();

				// read-only-line-form.component.CSS
				if (!ngLineDetailsCssFile.exists()) {
					ngLineDetailsCssFile.createNewFile();
				}
				fw = new FileWriter(ngLineDetailsCssFile.getAbsoluteFile());
				bw = new BufferedWriter(fw);
				bw.write(ng_line_read_only_form_component_css.toString());
				bw.close();

				// read-only-line-form.component.TS
				if (!ngLineDetailsTsFile.exists()) {
					ngLineDetailsTsFile.createNewFile();
				}
				fw = new FileWriter(ngLineDetailsTsFile.getAbsoluteFile());
				bw = new BufferedWriter(fw);
				bw.write(ng_line_read_only_form_component_ts.toString());
				bw.close();

				// WRITE LINE-ALL (GRID-VIEW) CODE HERE
				// grid-view-line-form.component.HTML
				if (!ngLineAllHtmlFile.exists()) {
					ngLineAllHtmlFile.createNewFile();
				}
				fw = new FileWriter(ngLineAllHtmlFile.getAbsoluteFile());
				bw = new BufferedWriter(fw);
				bw.write(ng_line_all_grid_view_component_html.toString());
				bw.close();

				// grid-view-line-form.component.CSS
				if (!ngLineAllCssFile.exists()) {
					ngLineAllCssFile.createNewFile();
				}
				fw = new FileWriter(ngLineAllCssFile.getAbsoluteFile());
				bw = new BufferedWriter(fw);
				bw.write(ng_line_all_grid_view_component_css.toString());
				bw.close();

				// grid-view-line-form.component.TS
				if (!ngLineAllTsFile.exists()) {
					ngLineAllTsFile.createNewFile();
				}
				fw = new FileWriter(ngLineAllTsFile.getAbsoluteFile());
				bw = new BufferedWriter(fw);
				bw.write(ng_line_all_grid_view_component_ts.toString());
				bw.close();
				
				// =============H-L LINE EXTENSION FILES ================
				
				// WRITE LINE-ADD-EXTENSION CODE HERE
				if (!ngLineExtensionHtmlFile.exists()) {
					ngLineExtensionHtmlFile.createNewFile();
				}
				fw = new FileWriter(ngLineExtensionHtmlFile.getAbsoluteFile());
				bw = new BufferedWriter(fw);
				bw.write(ng_line_extension_add_form_component_html.toString());
				bw.close();
				if (!ngLineExtensionTsFile.exists()) {
					ngLineExtensionTsFile.createNewFile();
				}
				fw = new FileWriter(ngLineExtensionTsFile.getAbsoluteFile());
				bw = new BufferedWriter(fw);
				bw.write(ng_line_extension_add_form_component_ts.toString());
				bw.close();
				
			}
		
			
			/*-----------------------UPDATE ADMIN ROUTING TS FILE --------------------*/
			File adminRoutingModule = new File(frontEndDir + "src/app/admin/admin-routing.module.ts");
			File tempRoutingModule = new File(frontEndDir + "src/app/admin/temp-routing.module.ts");

			BufferedReader reader = new BufferedReader(new FileReader(adminRoutingModule));
			BufferedWriter writer = new BufferedWriter(new FileWriter(tempRoutingModule));
			String removeStr = "]}];@NgModule({imports: [RouterModule.forChild(routes)],exports: [RouterModule]})export class AdminRoutingModule{}";
			String currentLine;
			System.out.println(adminRoutingModule.getName());
			while ((currentLine = reader.readLine()) != null) {
				String trimmedLine = currentLine.trim();
				if (trimmedLine.equals(removeStr)) {
					currentLine = "";
				}
				writer.write(currentLine + System.getProperty("line.separator"));

			}
			writer.close();
			reader.close();
			boolean delete = adminRoutingModule.delete();
			boolean b22 = tempRoutingModule.renameTo(adminRoutingModule);

			StringBuilder admin_routing_module_string = new StringBuilder();
			admin_routing_module_string.append("      {\r\n" + "			path: '" + ng_path_name + "-route',\r\n"
					+ "        	loadChildren: () => import('./" + ng_folder_name + "/" + ng_file_name
					+ ".module').then(m => m." + ng_module_ts_name + ")\r\n" + "      }," + "\r\n"
					+ "]}];@NgModule({imports: [RouterModule.forChild(routes)],exports: [RouterModule]})export class AdminRoutingModule{}");
			String adminRoutingModuleName = frontEndDir + "src/app/admin/admin-routing.module.ts";

			fw = new FileWriter(adminRoutingModuleName, true);
			fw.write(admin_routing_module_string.toString());
			fw.close();

			/*---------------ANGULAR UPDATE DYNAMIC MENU -------------------------*/
			File sideNavComponent = new File(frontEndDir + "src/app/admin/layout/side-nav/side-nav.component.ts");
			File tempSideNavComponent = new File(
					frontEndDir + "src/app/admin/layout/side-nav/temp-side-nav.component.ts");

			BufferedReader br2 = new BufferedReader(new FileReader(sideNavComponent));
			BufferedWriter bw2 = new BufferedWriter(new FileWriter(tempSideNavComponent));
			String SideNavRemoveStr = "]}";
			String cLine;
			System.out.println(sideNavComponent.getName());
			while ((cLine = br2.readLine()) != null) {
				String trimmedLine = cLine.trim();
				if (trimmedLine.equals(SideNavRemoveStr)) {
					cLine = "";
				}
				bw2.write(cLine + System.getProperty("line.separator"));

			}
			bw2.close();
			br2.close();
			boolean del = sideNavComponent.delete();
			boolean br33 = tempSideNavComponent.renameTo(sideNavComponent);

			StringBuilder admin_side_nav_string = new StringBuilder();
			admin_side_nav_string
					.append("    {\r\n" + "      label: '" + ng_model_ts_name + "',\r\n" + "      path:'/admin/"
							+ ng_path_name + "-route',\r\n" + "      icon: 'face'\r\n" + "    },\r\n" + "\r\n" + "]}");
			String sideNavComponentName = frontEndDir + "src/app/admin/layout/side-nav/side-nav.component.ts";

			fw = new FileWriter(sideNavComponentName, true);
			fw.write(admin_side_nav_string.toString());
			fw.close();

			/* =================B A C K ------- E N D================= */

			// <<<<<<<<<BACK-END DIRECTORY>>>>>>>>>
			String backEndDir = angularProjectPath.concat("/backend/");

			// BACK-END MODEL CLASS FILE
			File modelFile = new File(
					backEndDir + "src/main/java/com/realnet/model/" + table_name_first_upper + ".java");
			System.out.println("BACK-END model class directory name = " + modelFile);

			// BACK-END REPOSITORY CLASS FILE File repository
			File repositoryFile = new File(
					backEndDir + "src/main/java/com/realnet/repository/" + repository_name_first_upper + ".java");
			System.out.println("BACK-END repository class directory name = " + repositoryFile);

			// BACK-END SERVICE CLASS FILE
			File serviceFile = new File(
					backEndDir + "src/main/java/com/realnet/service/" + service_name_first_upper + ".java");
			System.out.println("BACK-END service class directory name = " + serviceFile);

			// BACK-END SERVICE IMPL CLASS FILE
			File serviceImplFile = new File(
					backEndDir + "src/main/java/com/realnet/service/" + service_impl_name_first_upper + ".java");
			System.out.println("BACK-END service class directory name = " + serviceImplFile);

			// BACK-END CONTROLLER CLASS FILE
			File controllerFile = new File(
					backEndDir + "src/main/java/com/realnet/controller/" + controller_name_first_upper + ".java");
			System.out.println("BACK-END controller class directory name = " + controllerFile);

			// FileWriter fw = null; BufferedWriter bw = null;

			if (!modelFile.exists()) {
				modelFile.createNewFile();
			}
			fw = new FileWriter(modelFile.getAbsoluteFile());
			bw = new BufferedWriter(fw);
			bw.write(model.toString());
			bw.close();

			if (!repositoryFile.exists()) {
				repositoryFile.createNewFile();
			}
			fw = new FileWriter(repositoryFile.getAbsoluteFile());
			bw = new BufferedWriter(fw);
			bw.write(repository.toString());
			bw.close();

			if (!serviceFile.exists()) {
				serviceFile.createNewFile();
			}
			fw = new FileWriter(serviceFile.getAbsoluteFile());
			bw = new BufferedWriter(fw);
			bw.write(service.toString());
			bw.close();

			if (!serviceImplFile.exists()) {
				serviceImplFile.createNewFile();
			}
			fw = new FileWriter(serviceImplFile.getAbsoluteFile());
			bw = new BufferedWriter(fw);
			bw.write(service_impl.toString());
			bw.close();

			if (!controllerFile.exists()) {
				controllerFile.createNewFile();
			}
			fw = new FileWriter(controllerFile.getAbsoluteFile());
			bw = new BufferedWriter(fw);
			bw.write(controller.toString());
			bw.close();

			// ==============<<<<<<BACK-END HEADER-LINE FILES>>>>>>=================//
			if (form_type.equalsIgnoreCase("HEADER_LINE")) {
				// BACK-END HEADER-LINE (LINE-MODEL CLASS FILE)
				File lineModelFile = new File(
						backEndDir + "src/main/java/com/realnet/model/" + line_table_name_first_upper + ".java");
				System.out.println("BACK-END LINE-MODEL class directory name = " + lineModelFile);
				
				// BACK-END HEADER-LINE (LINE-REPOSITORY CLASS FILE)
				File lineRepositoryFile = new File(
						backEndDir + "src/main/java/com/realnet/repository/" + line_repository_name_first_upper + ".java");
				System.out.println("BACK-END LINE-REPOSITORY class directory name = " + lineRepositoryFile);
				
				// BACK-END HEADER-LINE (LINE-CONTROLLER CLASS FILE)
				File lineControllerFile = new File(
						backEndDir + "src/main/java/com/realnet/controller/" + line_controller_name_first_upper + ".java");
				System.out.println("BACK-END LINE-CONTROLLER class directory name = " + lineControllerFile);
				
				
				if (!lineModelFile.exists()) {
					lineModelFile.createNewFile();
				}
				fw = new FileWriter(lineModelFile.getAbsoluteFile());
				bw = new BufferedWriter(fw);
				bw.write(line_model.toString());
				bw.close();
				
				if(!lineRepositoryFile.exists()) {
					lineRepositoryFile.createNewFile();
				}
				fw = new FileWriter(lineRepositoryFile.getAbsoluteFile());
				bw = new BufferedWriter(fw);
				bw.write(line_repository.toString());
				bw.close();
				
				if(!lineControllerFile.exists()) {
					lineControllerFile.createNewFile();
				}
				fw= new FileWriter(lineControllerFile.getAbsoluteFile());
				bw = new BufferedWriter(fw);
				bw.write(line_controller.toString());
				bw.close();
			}
			
			// =========== PROPERTIES FILE CODE ===========
			properties_file_fields_code.append("created_by, last_updated_by, creation_date, last_update_date, account_id");
			if (form_type.equalsIgnoreCase("HEADER_ONLY") || form_type.equalsIgnoreCase("HEADER_LINE")) {
				properties_file_code.append(
						// BACK-END HEADER PROPERTIES
						"table_name = " + table_name_first_upper + "\n"
						+ "controller_name = " + controller_name_first_upper + "\n" 
						+ "repository_name = " + repository_name_first_upper + "\n" 
						+ "service_name = " + service_name_first_upper + "\n" 
						+ "service_impl_name = " + service_impl_name_first_upper + "\n"
						+ properties_file_fields_code + "\n"
						// FRONT-END HEADER PROPERTIES
						+ "ng.model_ts_name = " + ng_model_ts_name + "\n"
						+ "ng.component_ts_name = " + ng_component_ts_name + "\n"
						+ "ng.module_ts_name = " + ng_module_ts_name + "\n"
						+ "ng.service_ts_name = " + ng_service_ts_name + "\n"
						+ "ng.routing_module_ts_name = " + ng_routing_module_ts_name + "\n"
						+ "ng.all_grid_view_component_name = " + ng_all_grid_view_component_name + "\n"
						+ "ng.add_form_component_name = " + ng_add_form_component_name + "\n"
						+ "ng.edit_component_name = " + ng_edit_component_name + "\n"
						+ "ng.read_only_component_name = " + ng_read_only_component_name + "\n"
						+ "ng.extension_add_component_name = " + ng_extension_add_component_name + "\n");
			}
			
			// need mod
			if (form_type.equalsIgnoreCase("LINE_ONLY") || form_type.equalsIgnoreCase("HEADER_LINE")) {
				String line_fields = properties_file_fields_code.insert(0, "line-").toString();
				properties_file_code.append(
						// BACK-END LINE PROPERTIES
						"line_table_name = " + line_table_name_first_upper + "\n"
						+ "line_controller_name = " + line_controller_name_first_upper + "\n"
						+ "line_repository_name = " + line_repository_name_first_upper + "\n"
						+ properties_file_fields_code + "\n"
						// FRONT-END LINE PROPERTIES
						+ "ng.line_model_ts_name = " + line_fields + "\n"
						+ "ng.line_all_grid_view_component_name = " + ng_line_all_grid_view_component_name + "\n"
						+ "ng.line_add_form_component_name = " + ng_line_add_form_component_name + "\n"
						+ "ng.line_edit_component_name = " + ng_line_edit_component_name + "\n"
						+ "ng.line_read_only_component_name = " + ng_line_read_only_component_name + "\n"
						+ "ng.line_extension_add_component_name = " + ng_line_extension_add_component_name);
			}
			
			File propertiesFile = new File(
					backEndDir + "src/main/resources/" + ui_name + ".properties");
			System.out.println("Niladri Properties file path = " + propertiesFile);
			//System.out.println(properties_file_fields_code.toString());
			if (!propertiesFile.exists()) {
				propertiesFile.getParentFile().mkdirs();
				propertiesFile.createNewFile();
			}
			
			fw = new FileWriter(propertiesFile.getAbsoluteFile());
			bw = new BufferedWriter(fw);
			bw.write(properties_file_code.toString());
			bw.close();
			
			
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Failed to delete the file");
			ErrorPojo errorPojo = new ErrorPojo();
			Error error = new Error();
			error.setTitle(Constant.FORM_BUILDER_API_TITLE);
			error.setMessage(Constant.FORM_BUILD_SUCCESS);
			errorPojo.setError(error);
			return new ResponseEntity<ErrorPojo>(errorPojo, HttpStatus.EXPECTATION_FAILED);
		}
		SuccessPojo successPojo = new SuccessPojo();
		Success success = new Success();
		success.setTitle(Constant.FORM_BUILDER_API_TITLE);
		success.setMessage(Constant.FORM_BUILD_FAILURE);
		successPojo.setSuccess(success);
		return new ResponseEntity<SuccessPojo>(successPojo, HttpStatus.OK);
		//return new ModelAndView("redirect:rn_wireframe_grid_view");
	}
}