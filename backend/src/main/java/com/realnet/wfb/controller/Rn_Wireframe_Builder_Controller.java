package com.realnet.wfb.controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
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

import com.realnet.actionbuilder.entity.Rn_cff_ActionBuilder_Header;
import com.realnet.actionbuilder.service.Rn_Cff_ActionBuilder_Service;
import com.realnet.fnd.entity.Error;
import com.realnet.fnd.entity.ErrorPojo;
import com.realnet.fnd.entity.Rn_Instance_Type;
import com.realnet.fnd.entity.Rn_Lookup_Values;
import com.realnet.fnd.entity.Rn_Module_Setup;
import com.realnet.fnd.entity.Rn_Project_Setup;
import com.realnet.fnd.entity.Success;
import com.realnet.fnd.entity.SuccessPojo;
import com.realnet.fnd.repository.Rn_InstanceTypeRepository;
import com.realnet.fnd.service.Rn_LookUp_Service;
import com.realnet.qb.entity.ExportDataDTO;
import com.realnet.qb.entity.Rn_CreateQuery;
import com.realnet.qb.service.Rn_CreateQuery_Service;
import com.realnet.users.entity.User;
import com.realnet.users.service.UserService;
import com.realnet.utils.Constant;
import com.realnet.wfb.entity.Rn_Fb_Header;
import com.realnet.wfb.entity.Rn_Fb_Line;
import com.realnet.wfb.service.Rn_WireFrame_Service;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RestController
@RequestMapping(value = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(tags = { "Spring MVC WireFrame" })
public class Rn_Wireframe_Builder_Controller {
	
	@Value("${angularProjectPath}")
	private String angularProjectPath;
	
	@Value("${projectPath}")
	private String projectPath;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private Environment environment;
	
	@Autowired
	private Rn_CreateQuery_Service createQueryService;
	
	@Autowired(required=false)
	private UserService userService;
	
	@Autowired
	private Rn_InstanceTypeRepository instanceTypeRepo;

	@Autowired
	private Rn_WireFrame_Service wireFrameService;

	@Autowired
	private Rn_LookUp_Service lookUpService;

	@Autowired
	private Rn_Cff_ActionBuilder_Service actionBuilderService;

	List<String> controllerName = new ArrayList<String>();
	
	@Transactional
	@PostMapping(value = "/createTable")
	public ResponseEntity<?> createTable(@RequestParam("header_id") Integer id, @Valid @RequestBody ExportDataDTO exportDataDTO) throws IOException { 
		// this is rn_fb_header ID
		StringBuilder sqlQuery = new StringBuilder();
		
		// get data from Angular Form (form data)
		boolean data = exportDataDTO.getData();
		System.out.println("id::"+id);
		System.out.println("valus of data"+data);
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
		
		System.out.println("Table Name::"+table_name+"\n\nForm Type:;"+form_type);

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

		for (int i = 0; i < fbl.size(); i++) 
		{
			System.out.println("Line for loop::"+fbl.get(i).getKey1());
			mapping = fbl.get(i).getMapping();
			String data_type = fbl.get(i).getDataType();
			String key1 = fbl.get(i).getKey1();
			  if(key1 != null && !key1.isEmpty()) 
			  {
				System.out.println("Line for loop under check null if::"+fbl.get(i).getKey1());
				if (key1.equals("PRI")) {
					sqlQuery.append(mapping + " " + data_type + "(45) NOT NULL AUTO_INCREMENT, ");
				} else if (data_type.equals("longtext") || data_type.equals("datetime") || data_type.equals("double")) {
					sqlQuery.append(mapping + " " + data_type + ", ");
				} else {
					sqlQuery.append(mapping + " " + data_type + "(45), ");
				}
			  }
		}
		
		StringBuilder attributeFlexData = new StringBuilder(); 
		List<Rn_Lookup_Values> attribute_flex = lookUpService.getExtensions();
		for (int i = 0; i < attribute_flex.size(); i++) {
			String lookup_code = attribute_flex.get(i).getLookupCode();
			String lower_case = lookup_code.toLowerCase();
//			String first_upper = RealNetUtils.toFirstUpperCase(lower_case);
//			String only_upper = lookup_code.toUpperCase();
			attributeFlexData.append(lower_case + " varchar(25), ");
			
		}
		System.out.println("Attribute flex Query = " + attributeFlexData.toString());
		//sqlQuery.append(attributeFlexData +" account_id int(11), CREATED_BY varchar(25),LAST_UPDATED_BY varchar(25), CREATION_DATE datetime, LAST_UPDATE_DATE datetime, PRIMARY KEY(id));");
		
		sqlQuery.append(
				" account_id int(11), CREATED_BY varchar(25),LAST_UPDATED_BY varchar(25), CREATION_DATE datetime, LAST_UPDATE_DATE datetime,"
						+ " extn1 varchar(25),extn2 varchar(25),extn3 varchar(25),extn4 varchar(25),extn5 varchar(25),extn6 varchar(25),"
						+ " extn7 varchar(25),extn8 varchar(25),extn9 varchar(25),extn10 varchar(25),extn11 varchar(25),extn12 varchar(25),"
						+ " extn13 varchar(25),extn14 varchar(25),extn15 varchar(25), flex1 varchar(25), flex2 varchar(25), flex3 varchar(25), flex4 varchar(25),"
						+ " flex5 varchar(25),  PRIMARY KEY(id));");

		System.out.println("Spring Boot sqlQuery = " + sqlQuery);

		String final_query = sqlQuery.toString().toLowerCase();
		jdbcTemplate.execute(final_query);

		User loggedInUser = userService.getLoggedInUser();
		long user_id = loggedInUser.getUserId();
		long account_id = loggedInUser.getSys_account().getId();
		int project_id = fbh.getModule().getProject().getId();
		
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
		
		System.out.println("Save query in create table"+savedQuery);
		
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
	
	
	
	@Transactional
	@PostMapping(value = "/createTableHL")
	public ResponseEntity<?> createHLTable(@RequestParam("header_id") Integer id, @Valid @RequestBody ExportDataDTO exportDataDTO) throws IOException { // this is rn_fb_header ID
		// Rn_Fb_Header table value by Id
		Rn_Fb_Header fbh = wireFrameService.getById(id);
		String table_name = fbh.getTableName();
		String line_table_name = fbh.getLineTableName();
		String form_type = fbh.getFormType();

		//// for type2 header vlaues//////
		if (form_type.equals("header_line") || form_type.equals("multiline")) {
			List<Rn_Fb_Line> fblh = wireFrameService.getHeaderFields(id);
			System.out.println("mappingH :: " + fblh.get(0).getMapping());

			StringBuilder sqlQuery = new StringBuilder();
			boolean data = exportDataDTO.getData();

			if (form_type.equals("header_line") || form_type.equals("multiline")) {

				if (data) {
					System.out.println("Data Present");
				} else {
					System.out.println("Data Not Present");
				}

				String dropQuery = "DROP TABLE IF EXISTS " + table_name + "";
				jdbcTemplate.execute(dropQuery);

				sqlQuery.append("create table if not exists " + table_name + " (");

				for (int i = 0; i < fblh.size(); i++) {
					String mappingH = fblh.get(i).getMapping();
					String data_type = fblh.get(i).getDataType();
					String key1 = fblh.get(i).getKey1();

//			if (mapping != "p_id"){

					if (key1.equals("PRI")) {
						sqlQuery.append(mappingH + " " + data_type + "(45) NOT NULL AUTO_INCREMENT, ");
					} else {
						sqlQuery.append(mappingH + " " + data_type + "(45), ");
					}
				}

				StringBuilder attributeFlexData = new StringBuilder(); 
				List<Rn_Lookup_Values> attribute_flex = lookUpService.getExtensions();
				for (int i = 0; i < attribute_flex.size(); i++) {
					String lookup_code = attribute_flex.get(i).getLookupCode();
					String lower_case = lookup_code.toLowerCase();
//					String first_upper = RealNetUtils.toFirstUpperCase(lower_case);
//					String only_upper = lookup_code.toUpperCase();
					attributeFlexData.append(lower_case + " varchar(25), ");
				}
				System.out.println("Attribute flex Query = " + attributeFlexData.toString());
				//sqlQuery.append(attributeFlexData +" account_id int(11), CREATED_BY varchar(25),LAST_UPDATED_BY varchar(25), CREATION_DATE datetime, LAST_UPDATE_DATE datetime, PRIMARY KEY(id));");
				
				sqlQuery.append(
						" account_id int(11), CREATED_BY varchar(25),LAST_UPDATED_BY varchar(25), CREATION_DATE datetime, LAST_UPDATE_DATE datetime,"
								+ " extn1 varchar(25),extn2 varchar(25),extn3 varchar(25),extn4 varchar(25),extn5 varchar(25),extn6 varchar(25),"
								+ " extn7 varchar(25),extn8 varchar(25),extn9 varchar(25),extn10 varchar(25),extn11 varchar(25),extn12 varchar(25),"
								+ "extn13 varchar(25),extn14 varchar(25),extn15 varchar(25), flex1 varchar(25), flex2 varchar(25), flex3 varchar(25), flex4 varchar(25),"
								+ " flex5 varchar(25),  PRIMARY KEY(id));");

				System.out.println("sqlQuery    ::    " + sqlQuery);

				String final_query = sqlQuery.toString();
				String final_query_lower_case = final_query.toLowerCase();

				jdbcTemplate.execute(final_query_lower_case);

			}

		}

/////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////Create Line Table///////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////

		if (form_type.equals("header_line") || form_type.equals("multiline") || form_type.equals("line_only")) {
			List<Rn_Fb_Line> fbll = wireFrameService.getLineFields(id);
			System.out.println("mappingL :: " + fbll.get(0).getMapping());

			StringBuilder sqlQueryL = new StringBuilder();

			boolean dataL = exportDataDTO.getData();

			if (form_type.equals("header_line") || form_type.equals("line_only") || form_type.equals("multiline")) {

				if (dataL) {
					System.out.println("Data Present");
				} else {
					System.out.println("Data Not Present");
				}

				String dropQueryL = "DROP TABLE IF EXISTS " + line_table_name + "";
				jdbcTemplate.execute(dropQueryL);

				sqlQueryL.append(
						"create table if not exists " + line_table_name + " ( l_id int(11) NOT NULL AUTO_INCREMENT,");

				for (int i = 0; i < fbll.size(); i++) {
					String mappingL = fbll.get(i).getMapping();
					String data_typeL = fbll.get(i).getDataType();
					String key1L = fbll.get(i).getKey1();

					if (key1L.equals("PRI")) {

						if (form_type.equals("header_line") || form_type.equals("multiline")) {
						}

					} else {
						sqlQueryL.append(mappingL + " " + data_typeL
								+ "(45), "); /* System.out.println("PRIMARY KEY NOT FOUND IN THE TABLE "); */
					}
				}

//---- > need to check it				
				sqlQueryL.append(
						" account_id int(11), CREATED_BY varchar(25),LAST_UPDATED_BY varchar(25), CREATION_DATE datetime, LAST_UPDATE_DATE datetime, account varchar(45), "
								+ "form_name varchar(45), extn1 varchar(25),extn2 varchar(25),extn3 varchar(25),extn4 varchar(25),extn5 varchar(25),"
								+ "extn6 varchar(25), extn7 varchar(25),extn8 varchar(25),extn9 varchar(25),extn10 varchar(25),extn11 varchar(25),"
								+ "extn12 varchar(25), extn13 varchar(25),extn14 varchar(25),extn15 varchar(25), flex1 varchar(25), flex2 varchar(25), "
								+ "flex3 varchar(25), flex4 varchar(25), flex5 varchar(25),");

				if (form_type.equals("header_line") || form_type.equals("multiline")) {
					sqlQueryL.append(" PRIMARY KEY(l_id));");
				} else if (form_type.equals("line_only") || form_type.equals("header_only")) {
					sqlQueryL.append(" PRIMARY KEY(id));");

				}
				System.out.println("sqlQueryL    ::    " + sqlQueryL);

				String final_queryL = sqlQueryL.toString();
				String final_query_lower_caseL = final_queryL.toLowerCase();
				jdbcTemplate.execute(final_query_lower_caseL);

				User loggedInUser = userService.getLoggedInUser();
				long user_idL = loggedInUser.getUserId();
				long account_idL = loggedInUser.getSys_account().getId();
				int project_idL = fbh.getModule().getProject().getId();
				
				Rn_CreateQuery rn_create_query_tL = new Rn_CreateQuery();
				System.out.println("export as data::" + dataL);

				rn_create_query_tL.setProjectId(project_idL);
				rn_create_query_tL.setAccountId(account_idL);
				rn_create_query_tL.setTableName(line_table_name);
				rn_create_query_tL.setCreateQuery(final_queryL);

				rn_create_query_tL.setCreatedBy(user_idL);
				rn_create_query_tL.setUpdatedBy(user_idL);
				rn_create_query_tL.setBuild(false);
				rn_create_query_tL.setData(dataL);
	
				Rn_CreateQuery savedQuery = createQueryService.save(rn_create_query_tL);
				
				if (savedQuery == null) {
					ErrorPojo errorPojo = new ErrorPojo();
					Error error = new Error();
					error.setTitle(Constant.QUERY_API_TITLE);
					error.setMessage(Constant.QUERY_CREATE_FAILURE);
					errorPojo.setError(error);
					return new ResponseEntity<ErrorPojo>(errorPojo, HttpStatus.EXPECTATION_FAILED);
				}
			}
		}
		SuccessPojo successPojo = new SuccessPojo();
		Success success = new Success();
		success.setTitle(Constant.QUERY_API_TITLE);
		success.setMessage(Constant.QUERY_CREATE_SUCCESS);
		successPojo.setSuccess(success);
		return new ResponseEntity<SuccessPojo>(successPojo, HttpStatus.CREATED);
	}

	@Transactional
	@PostMapping(value = "/createTableMultiline")
	public ResponseEntity<?> createTableMultiline(@RequestParam("header_id") Integer id, @Valid @RequestBody ExportDataDTO exportDataDTO) throws IOException { // this is rn_fb_header ID
		
		// Rn_Fb_Header table value by Id
		Rn_Fb_Header fbh = wireFrameService.getById(id);
		String table_name = fbh.getTableName();
		String mutiline_table_name = fbh.getMultilineTableName();
		String form_type = fbh.getFormType();

		//// for type2 header vlaues//////
		if (form_type.equals("header_line") || form_type.equals("multiline")) {
			List<Rn_Fb_Line> fblh = wireFrameService.getHeaderFields(id);
			System.out.println("mappingH :: " + fblh.get(0).getMapping());

			StringBuilder sqlQuery = new StringBuilder();
			boolean data = exportDataDTO.getData();

			if (form_type.equals("header_line") || form_type.equals("multiline")) {

				if (data) {
					System.out.println("Data Present");
				} else {
					System.out.println("Data Not Present");
				}

				String dropQuery = "DROP TABLE IF EXISTS " + table_name + "";
				jdbcTemplate.execute(dropQuery);

				sqlQuery.append("create table if not exists " + table_name + " (");

				for (int i = 0; i < fblh.size(); i++) {
					String mappingH = fblh.get(i).getMapping();
					String data_type = fblh.get(i).getDataType();
					String key1 = fblh.get(i).getKey1();

//			if (mapping != "p_id"){

					if (key1.equals("PRI")) {
						sqlQuery.append(mappingH + " " + data_type + "(45) NOT NULL AUTO_INCREMENT, ");
					} else if (key1.equalsIgnoreCase("HID")) {
						sqlQuery.append(mappingH + " " + data_type + "(45), ");
					} else {
						sqlQuery.append(mappingH + " " + data_type + "(45), ");
					}
				}

//---- > need to check it		
				sqlQuery.append(
						" account_id int(11), CREATED_BY varchar(25),LAST_UPDATED_BY varchar(25), CREATION_DATE datetime, LAST_UPDATE_DATE datetime, account varchar(45), "
								+ "form_name varchar(45), extn1 varchar(25),extn2 varchar(25),extn3 varchar(25),extn4 varchar(25),extn5 varchar(25),"
								+ "extn6 varchar(25), extn7 varchar(25),extn8 varchar(25),extn9 varchar(25),extn10 varchar(25),extn11 varchar(25),"
								+ "extn12 varchar(25), extn13 varchar(25),extn14 varchar(25),extn15 varchar(25), flex1 varchar(25), flex2 varchar(25), "
								+ "flex3 varchar(25), flex4 varchar(25), flex5 varchar(25), form_code varchar(200), PRIMARY KEY(id));");

				System.out.println("sqlQuery    ::    " + sqlQuery);

				String final_query = sqlQuery.toString();
				String final_query_lower_case = final_query.toLowerCase();

				jdbcTemplate.execute(final_query_lower_case);

			}

		}

////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////Create Line Table///////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////

		if (form_type.equals("multiline")) {
			List<Rn_Fb_Line> fbll = wireFrameService.getLineFields(id);

			int line_t_no = fbll.get(0).getLine_table_no();
			System.out.println("mappingL :: " + fbll.get(0).getMapping());

			String[] abc = mutiline_table_name.split(",");

			for (String name : abc) {
				int t_length = abc.length;

				t_length++;
				System.out.println("multiline t_length value :: " + t_length);

				String ml_tables = name;
				System.out.println("multiline table names :" + ml_tables);

				StringBuilder sqlQueryL = new StringBuilder();

				boolean dataL = exportDataDTO.getData();

				for (int j = 1; j < t_length; j++) {
					if (dataL) {
						System.out.println("Data Present");
					} else {
						System.out.println("Data Not Present");
					}
					if (j == line_t_no) {
						String dropQueryL = "DROP TABLE IF EXISTS " + ml_tables + "";
						jdbcTemplate.execute(dropQueryL);
						sqlQueryL.append("create table if not exists " + ml_tables + " ( ");

						for (int i = 0; i < fbll.size(); i++) {
							String mappingL = fbll.get(i).getMapping();
							String data_typeL = fbll.get(i).getDataType();
							String key1L = fbll.get(i).getKey1();

							if (key1L.equals("PRI")) {
								sqlQueryL.append(
										"l" + j + "_" + mappingL + " " + data_typeL + "(45) NOT NULL AUTO_INCREMENT, ");
							}

							else if (key1L.equalsIgnoreCase("HID")) {
								sqlQueryL.append("l" + j + "_" + mappingL + " " + data_typeL + "(11),");
							} else {
								sqlQueryL.append("l" + j + "_" + mappingL + " " + data_typeL + "(45), ");
							}
						}
						sqlQueryL.append(
								" account_id int(11), CREATED_BY varchar(25),LAST_UPDATED_BY varchar(25), CREATION_DATE datetime, LAST_UPDATE_DATE datetime, account varchar(45), "
										+ "form_name varchar(45), extn1 varchar(25),extn2 varchar(25),extn3 varchar(25),extn4 varchar(25),extn5 varchar(25),"
										+ "extn6 varchar(25), extn7 varchar(25),extn8 varchar(25),extn9 varchar(25),extn10 varchar(25),extn11 varchar(25),"
										+ "extn12 varchar(25), extn13 varchar(25),extn14 varchar(25),extn15 varchar(25), flex1 varchar(25), flex2 varchar(25), "
										+ "flex3 varchar(25), flex4 varchar(25), flex5 varchar(25),form_code varchar(200),  PRIMARY KEY(id));");
						System.out.println("sqlQueryL   for " + ml_tables + "  is   ::    " + sqlQueryL);
						String final_queryL = sqlQueryL.toString();
						String final_query_lower_caseL = final_queryL.toLowerCase();
						jdbcTemplate.execute(final_query_lower_caseL);
					}
				}
			}
		}

		//return new ModelAndView("redirect:rn_wireframe_line_edit?header_id=" + header_id + "");
		SuccessPojo successPojo = new SuccessPojo();
		Success success = new Success();
		success.setTitle(Constant.QUERY_API_TITLE);
		success.setMessage(Constant.QUERY_CREATE_SUCCESS);
		successPojo.setSuccess(success);
		return new ResponseEntity<SuccessPojo>(successPojo, HttpStatus.CREATED);
	}

	@GetMapping(value = "/build_wireframe")
	public ResponseEntity<?> build_wireframe(@RequestParam("header_id") Integer id) throws IOException { // this is rn_fb_header ID
		
		System.out.println("Builder controller has been started,header id="+id);
		StringBuilder togglescript = new StringBuilder();
		StringBuilder strContentprefield = new StringBuilder();
		StringBuilder strContentreadonly = new StringBuilder();
		StringBuilder controller = new StringBuilder();
		StringBuilder dao = new StringBuilder();
		StringBuilder dao_impl = new StringBuilder();
		StringBuilder service = new StringBuilder();
		StringBuilder service_impl = new StringBuilder();
		StringBuilder para = new StringBuilder();
		StringBuilder set_para = new StringBuilder();
		StringBuilder model_class = new StringBuilder();
		StringBuilder model_class1 = new StringBuilder();
		StringBuilder model_class2 = new StringBuilder();
		StringBuilder model_class3 = new StringBuilder();
		StringBuilder model_class4 = new StringBuilder();
		StringBuilder table_grid_view = new StringBuilder();
		StringBuilder table_field = new StringBuilder();
		StringBuilder table_field_value = new StringBuilder();
		StringBuilder grid_controller = new StringBuilder();
		StringBuilder sqlField = new StringBuilder();
		StringBuilder setField = new StringBuilder();
		StringBuilder table_field_for_id = new StringBuilder();
		StringBuilder table_field_value_for_id = new StringBuilder();
		StringBuilder service_save_id = new StringBuilder();
		StringBuilder service_save_id1 = new StringBuilder();
		StringBuilder service_save_field = new StringBuilder();
		StringBuilder service_save_field1 = new StringBuilder();
		StringBuilder form_id = new StringBuilder();
		StringBuilder sqlField_id = new StringBuilder();
		StringBuilder setField_id = new StringBuilder();
		StringBuilder dao_impl_prefield = new StringBuilder();
		StringBuilder prefield_controller = new StringBuilder();
		StringBuilder prefield_controller_readonly = new StringBuilder();
		StringBuilder sbmit_parameterfield = new StringBuilder();
		StringBuilder sbmit_parameterid = new StringBuilder();
		StringBuilder save_controller = new StringBuilder();
		StringBuilder form = new StringBuilder();
		StringBuilder sectionform = new StringBuilder();
		StringBuilder importsection = new StringBuilder();
		StringBuilder headsection = new StringBuilder();
		StringBuilder form_prefield = new StringBuilder();
		StringBuilder importsectionprefield = new StringBuilder();
		StringBuilder headsectionprefield = new StringBuilder();
		StringBuilder form_readonly = new StringBuilder();
		StringBuilder importsectionreadonly = new StringBuilder();
		StringBuilder headsectionreadonly = new StringBuilder();
		StringBuilder field_for_save_id = new StringBuilder();
		StringBuilder field_for_save_field = new StringBuilder();
		StringBuilder dao_impl_save = new StringBuilder();
		StringBuilder id_notpri_para = new StringBuilder();
		StringBuilder id_notpri_set = new StringBuilder();
		StringBuilder date_array_set_in_dao = new StringBuilder();
		StringBuilder id_para_submit = new StringBuilder();
		StringBuilder var_for_pass_para = new StringBuilder();
		StringBuilder var_for_pass_para_id = new StringBuilder();
		StringBuilder model_class_for_var = new StringBuilder();
		StringBuilder model_class_for_set = new StringBuilder();
		StringBuilder date_para = new StringBuilder();
		StringBuilder date_set = new StringBuilder();
		StringBuilder date_model_variable = new StringBuilder();
		StringBuilder date_model_set_variable = new StringBuilder();
		StringBuilder date_array_para = new StringBuilder();

		StringBuilder longtext_para = new StringBuilder();
		StringBuilder longtext_set = new StringBuilder();
		StringBuilder longtext_model_variable = new StringBuilder();
		StringBuilder longtext_model_set_variable = new StringBuilder();
		StringBuilder longtext_array_para = new StringBuilder();
		StringBuilder longtext_field_for_grid = new StringBuilder();
		StringBuilder longtext_field_for_grid_value = new StringBuilder();
		StringBuilder longtext_array_value = new StringBuilder();
		StringBuilder longtext_array_value_for_lower = new StringBuilder();
		StringBuilder longtext_array_set_in_dao_for_date = new StringBuilder();
		StringBuilder longtext_array_set_in_dao = new StringBuilder();

		StringBuilder double_para = new StringBuilder();
		StringBuilder double_set = new StringBuilder();
		StringBuilder double_model_variable = new StringBuilder();
		StringBuilder double_model_set_variable = new StringBuilder();
		StringBuilder double_array_para = new StringBuilder();
		StringBuilder double_field_for_grid = new StringBuilder();
		StringBuilder double_field_for_grid_value = new StringBuilder();
		StringBuilder double_array_value = new StringBuilder();
		StringBuilder double_array_value_for_lower = new StringBuilder();
		StringBuilder double_array_set_in_dao_for_date = new StringBuilder();
		StringBuilder double_array_set_in_dao = new StringBuilder();

		StringBuilder id_array_set_in_dao2 = new StringBuilder();
		StringBuilder date_array_value = new StringBuilder();
		StringBuilder id_array_set_in_dao = new StringBuilder();
		StringBuilder date_array_set_in_dao_for_date = new StringBuilder();
		StringBuilder var_for_pass_para_id_for_upper = new StringBuilder();
		StringBuilder date_array_value_for_lower = new StringBuilder();
		StringBuilder id_not_pri_field_for_grid = new StringBuilder();
		StringBuilder id_not_pri_field_for_grid_value = new StringBuilder();
		StringBuilder date_field_for_grid = new StringBuilder();
		StringBuilder date_field_for_grid_value = new StringBuilder();
		StringBuilder line_value = new StringBuilder();
		StringBuilder for_line_part = new StringBuilder();
		StringBuilder line_script = new StringBuilder();
		StringBuilder varcharValues = new StringBuilder();

		StringBuilder header_line_submit = new StringBuilder();
		StringBuilder header_line_submit_id = new StringBuilder();
		StringBuilder header_line_submit_varchar = new StringBuilder();
		StringBuilder get_id_for_sbmit_header_line = new StringBuilder();
		StringBuilder header_id_for_line = new StringBuilder();
		StringBuilder line_id_for_rowcount = new StringBuilder();
		StringBuilder array_para_for_id_line = new StringBuilder();
		StringBuilder array_para_for_id_line1 = new StringBuilder();
		StringBuilder array_para_for_varchar_line = new StringBuilder();

		StringBuilder action2 = new StringBuilder();
		StringBuilder action3 = new StringBuilder();
		StringBuilder parameter_for_line_id = new StringBuilder();
		StringBuilder parameter_for_line_varchar = new StringBuilder();
		StringBuilder dropdown_controller = new StringBuilder();
		StringBuilder get_parameter_for_id_line = new StringBuilder();
		StringBuilder set_parameter_header_id_line = new StringBuilder();
		StringBuilder set_parameter_for_id_line = new StringBuilder();
		StringBuilder set_parameter_for_varchar_line = new StringBuilder();
		StringBuilder dao_impl_save_line = new StringBuilder();
		StringBuilder model_class_for_line = new StringBuilder();
		StringBuilder model_class_line1 = new StringBuilder();
		StringBuilder model_class_line2 = new StringBuilder();
		StringBuilder model_class_line3 = new StringBuilder();
		StringBuilder model_class_line4 = new StringBuilder();
		StringBuilder id_primary_parameter_submit = new StringBuilder();
		StringBuilder set_id_primary_for_header = new StringBuilder();
		StringBuilder autocomplete_script_latest = new StringBuilder();
		StringBuilder header_id_for_line_for_comma = new StringBuilder();
		StringBuilder get_set_for_line_update_id = new StringBuilder();
		StringBuilder get_set_for_line_update_varchar = new StringBuilder();
		StringBuilder dao_impl_for_update_line = new StringBuilder();
		StringBuilder line_script_update = new StringBuilder();
		StringBuilder for_line_part_update = new StringBuilder();
		StringBuilder for_line_part_readonly = new StringBuilder();
		StringBuilder line_value_readonly = new StringBuilder();
		StringBuilder dropdown_script = new StringBuilder();
		StringBuilder dependent_dropdown = new StringBuilder();
		StringBuilder autocomplete_script = new StringBuilder();
		StringBuilder model_class_for_attribute = new StringBuilder();
		StringBuilder model_class_for_attribute2 = new StringBuilder();
		StringBuilder attribute_for_select_statment = new StringBuilder();
		StringBuilder attribute_set_for_grid_dao = new StringBuilder();
		StringBuilder attribute_for_select_statment_lower = new StringBuilder();
		StringBuilder attribute_set_for_grid_dao_line = new StringBuilder();
		StringBuilder get_parameter = new StringBuilder();
		StringBuilder set_parameter = new StringBuilder();

		StringBuilder action = new StringBuilder();
		StringBuilder date_script = new StringBuilder();
		StringBuilder dropdown_script_latest = new StringBuilder();
		StringBuilder dropdown_script_latest1 = new StringBuilder();

		StringBuilder dropdown_script_latest_for_line = new StringBuilder();
		StringBuilder sbmitparameterid = new StringBuilder();
		StringBuilder varcharValues_para = new StringBuilder();

		StringBuilder update_field_prefield_controller = new StringBuilder();
		StringBuilder readonly_script = new StringBuilder();

		StringBuilder togglescript_l = new StringBuilder();

		StringBuilder date_script_l = new StringBuilder();

		StringBuilder dropdown_script_latest_l = new StringBuilder();

		StringBuilder autocomplete_script_latest_l = new StringBuilder();

		StringBuilder line_value1 = new StringBuilder();
		StringBuilder line_value_update1 = new StringBuilder();

		// CFF ACTION BUILDER
		StringBuilder cff_add_button_code_in_update_jsp = new StringBuilder();
		StringBuilder cff_insert_button_setter = new StringBuilder();
		StringBuilder cff_update_button_pk = new StringBuilder();

		// PROPERTIES FILE CODE
		StringBuilder properties_file_code = new StringBuilder();
		StringBuilder properties_file_fields_code = new StringBuilder("fields = ");
		StringBuilder properties_file_line_code = new StringBuilder();
		StringBuilder properties_file_line_fields_code = new StringBuilder();
		
		// HEADER VALUE
		Rn_Fb_Header rn_fb_header = wireFrameService.getById(id);
				
		// LINE VALUES
		List<Rn_Fb_Line> lineList = rn_fb_header.getRn_fb_lines();
		
		// EXTRA BUTTON LIST (IF USER ADD EXTRA BUTTON THEN THIS WILL CALL)
		List<Rn_Fb_Line> onlyButtonList = wireFrameService.getExtraButton(id);
		
		// VARCHAR VALUES
		List<Rn_Fb_Line> rn_userlist = wireFrameService.getVarcharFields(id);

		// PK VALUES
		List<Rn_Fb_Line> id_list = wireFrameService.getPrimaryKeyField(id);
		
		// SECTION VALUES
		List<Rn_Fb_Line> section_values = wireFrameService.getSection(id);
		
		// MODULE DETAILS
		Rn_Module_Setup module = rn_fb_header.getModule();
		
		
		// PROJECT DETAILS
		Rn_Project_Setup project = module.getProject();
		String project_name = project.getProjectName();
		System.out.println("Project Name => " + project_name);
		String module_name = module.getModuleName();
		System.out.println("Module Name = " + module_name);
		
		String technology_stack = project.getTechnologyStack();

				
		// ATTRIBUTE FLEX VALUES
		List<Rn_Lookup_Values> attribute_flex = lookUpService.getExtensions();
		

		String field_name_for_id = id_list.get(0).getMapping();
		String field_name_for_id_lower = field_name_for_id.toLowerCase();
		String field_name_for_id_upper = field_name_for_id.toUpperCase();
		String field_name_first_upper = field_name_for_id_lower.substring(0, 1).toUpperCase()
				+ field_name_for_id_lower.substring(1);

//		List<Rn_Fb_Line> id_notprimary = rn_wireframe_dao.wf_id_notPri_intValues(f_code);
//		List<Rn_Fb_Line> datetime_list = rn_wireframe_dao.wf_listDatetime(f_code);
//		List<Rn_Fb_Line> longtext_list = rn_wireframe_dao.wf_listLongtext(f_code);
//		List<Rn_Fb_Line> double_list = rn_wireframe_dao.wf_listDouble(f_code);
		List<Rn_Fb_Line> id_notprimary = wireFrameService.getIntegerFields(id);

		List<Rn_Fb_Line> datetime_list = wireFrameService.getDateTimeFields(id);

		List<Rn_Fb_Line> longtext_list = wireFrameService.getLongtextFields(id);

		List<Rn_Fb_Line> double_list = wireFrameService.getDoubleFields(id);

		System.out.println("-----------value of id-------" + id);

		System.out.println("-----------build from started-------");
//		int project_id = (Integer) request.getSession().getAttribute("project_id");
//
//		// get project details
//		List<Rn_Project_Setup> report0 = rn_project_setup_service.prefield(project_id);
//		String project_name = report0.get(0).getProject_name();
//		System.out.println("Project Name => " + project_name);
//
//		int m_id = (Integer) request.getSession().getAttribute("module_id");
//
//		List<Rn_module_setup_t> report1 = rn_module_setup_t_service.prefield_module(m_id);
//
//		String module_name = report1.get(0).getModule_name();
//		System.out.println("Module Name = " + module_name); // 
//
//		List<Rn_Fb_Header> report = rn_wireframe_dao.selectHeaderTableValues(f_code);

		String controller_name = rn_fb_header.getControllerName();
		String dao_name = rn_fb_header.getDaoName();
		String dao_impl_name = rn_fb_header.getDaoImplName();
		String jsp_name = rn_fb_header.getJspName();
		String table_name = rn_fb_header.getTableName();
		String service_name = rn_fb_header.getServiceName();
		String service_impl_name = rn_fb_header.getServiceImplName();
		String line_table_name = rn_fb_header.getLineTableName();
		String form_code = rn_fb_header.getFormCode();
		String form_type = rn_fb_header.getFormType();

		String ui_name = rn_fb_header.getUiName();

		System.out.println("---value of line table name---" + line_table_name);

		String controller_name_lower = controller_name.toLowerCase();
		String controller_name_first_upper = controller_name_lower.substring(0, 1).toUpperCase()
				+ controller_name_lower.substring(1);

		String service_name_lower = service_name.toLowerCase();
		String service_name_first_upper = service_name_lower.substring(0, 1).toUpperCase()
				+ service_name_lower.substring(1);
		String service_name_upper = service_name.toUpperCase();

		String service_impl_name_lower = service_impl_name.toLowerCase();
		String service_impl_name_first_upper = service_impl_name_lower.substring(0, 1).toUpperCase()
				+ service_impl_name_lower.substring(1);

		String dao_name_lower = dao_name.toLowerCase();
		String dao_name_first_upper = dao_name_lower.substring(0, 1).toUpperCase() + dao_name_lower.substring(1);

		String dao_impl_name_lower = dao_impl_name.toLowerCase();
		String dao_impl_name_first_upper = dao_impl_name_lower.substring(0, 1).toUpperCase()
				+ dao_impl_name_lower.substring(1);

		String table_name_lower = table_name.toLowerCase();
		String table_name_first_upper = table_name_lower.substring(0, 1).toUpperCase() + table_name_lower.substring(1);
		String table_name_upper = table_name.toUpperCase();

		System.out.println("table name  first upper::" + table_name_first_upper);
		System.out.println("controler name from build from::" + controller_name);
		System.out.println("service name from build from::" + dao_name);
		System.out.println("jsp name from build from::" + jsp_name);

		// ---------------------for------line-----------------------------------
		
		//List<Rn_Fb_Line> line_id_primary = rn_wireframe_dao.wf_line_primary_id(f_code);
		//List<Rn_Fb_Line> line_section = rn_wireframe_dao.wf_line_section(f_code);
		//List<Rn_Fb_Line> line_varchar = rn_wireframe_dao.for_line_varchar(f_code);
		List<Rn_Fb_Line> line_id_primary = wireFrameService.getLinePrimarkKeyField(id);
		List<Rn_Fb_Line> line_section = wireFrameService.getLineSection(id);
		List<Rn_Fb_Line> line_varchar = wireFrameService.getLineVarcharFields(id);
		

		// PK VALUE
		for (int i = 0; i < id_list.size(); i++) {

			String field_name = id_list.get(i).getMapping();
			String lower_case = field_name.toLowerCase();
			String first_upper = lower_case.substring(0, 1).toUpperCase() + lower_case.substring(1);
			sbmitparameterid.append("\nString\t" + lower_case + "=request.getParameter(\"" + lower_case + "\");");

			sbmit_parameterid.append("\nString\t" + lower_case + "=request.getParameter(\"" + lower_case + "\");");
			set_id_primary_for_header.append("\n" + table_name_lower + ".set" + first_upper + "(id);"); // lower_case

			id_primary_parameter_submit.append("\nif(request.getParameter(\"" + lower_case + "\")!=\"\")" + "\n{"
					+ "\nint id=Integer.parseInt(request.getParameter(\"" + lower_case + "\"));"
					+ set_id_primary_for_header + "\n}");

			// FOR ACTION BUILDER CODE (PK FOR UPDATE OP)
			cff_update_button_pk.append(table_name_first_upper + " " + table_name_lower + " = " + dao_name_lower
					+ ".getById(" + lower_case + " );\n");

			// ui_name.properties file
			properties_file_fields_code.append(lower_case + ", ");
		}

		// VARCHAR
		for (int i = 0; i < rn_userlist.size(); i++) {
			String field_name = rn_userlist.get(i).getMapping();
			String type_field = rn_userlist.get(i).getType_field();
			String lower_case = field_name.toLowerCase();
			String first_upper = lower_case.substring(0, 1).toUpperCase() + lower_case.substring(1);

			System.out.println("------for lowecase string-----------::" + lower_case);
			System.out.println("------for uper first letter---------::" + first_upper);

			varcharValues_para.append(",String\t" + lower_case);

			varcharValues.append("," + lower_case);

			para.append("\n\t String " + lower_case + "=request.getParameter(\"" + lower_case + "\");");
			if (type_field.equals("checkbox") || type_field.equals("togglebutton")) {
				para.append("\nif (" + lower_case + "==null)" + "		\n	{" + "			\n	" + lower_case
						+ "=\"N\";" + "			\n}");
			}

			set_para.append("\n\t" + table_name_lower + ".set" + first_upper + "(" + lower_case + ");");

			sbmit_parameterfield
					.append("\nString[]\t" + lower_case + "=request.getParameterValues(\"" + lower_case + "\");");

			// FOR ACTION BUILDER CODE
			cff_insert_button_setter.append(table_name_lower + ".set" + first_upper + "(" + lower_case + ");\n");

			// ui_name.properties file
			properties_file_fields_code.append(lower_case + ", ");

		}

		// INT
		for (int i = 0; i < id_notprimary.size(); i++) {
			String field_name = id_notprimary.get(i).getMapping();
			String data_type = id_notprimary.get(i).getDataType();
			String lower_case = field_name.toLowerCase();
			String only_upper = field_name.toUpperCase();
			String first_upper = lower_case.substring(0, 1).toUpperCase() + lower_case.substring(1);

			System.out.println("model_class3 field_name" + field_name);
			if (data_type.equals("int")) {

//   id_para_submit.append("int\t" +lower_case+"=Integer.parseInt(request.getParameter(\""+lower_case+"\"));");

				id_notpri_para
						.append("\nString[]\t" + lower_case + "=request.getParameterValues(\"" + lower_case + "\");");

				id_notpri_set.append("\n\t" + table_name_lower + ".set" + first_upper + "(" + lower_case + ");");

				model_class_for_var
						.append("\n@Column(name = \"" + only_upper + "\")" + "\nprivate int \t" + lower_case + ";");

				model_class_for_set
						.append("\n\npublic int get" + first_upper + "() \n{" + "\nreturn\t" + lower_case + ";\n}"

								+ "\n\npublic void set" + first_upper + "(int\t" + lower_case + ")\n{" + "\nthis."
								+ lower_case + "=" + lower_case + ";" + "\n}");

				var_for_pass_para.append(",String\t" + lower_case);

				var_for_pass_para_id.append("," + lower_case + "");

				var_for_pass_para_id_for_upper.append(only_upper + ",");

				id_array_set_in_dao.append(
						"\n" + table_name_lower + ".set" + first_upper + "(rs.getInt(\"" + only_upper + "\"));");
				id_array_set_in_dao2.append(
						"\n" + table_name_lower + ".set" + first_upper + "(Integer.parseInt(" + lower_case + "[i]));");

				// FOR ACTION BUILDER CODE
				cff_insert_button_setter.append(table_name_lower + ".set" + first_upper + "(" + lower_case + ");\n");

				// ui_name.properties file
				properties_file_fields_code.append(lower_case + ", ");
				// properties_file_setters_code.append("${table_name}.set" + first_upper + "(" +
				// lower_case + "),");
			}
		}

		// DATE TIME
		for (int i = 0; i < datetime_list.size(); i++) {
			String field_name = datetime_list.get(i).getMapping();
			String data_type = datetime_list.get(i).getDataType();

			String lower_case = field_name.toLowerCase();
			String only_upper = field_name.toUpperCase();
			String first_upper = lower_case.substring(0, 1).toUpperCase() + lower_case.substring(1);

			if (data_type.equals("datetime")) {
				date_para.append("\n Date\t" + lower_case + " = null;" + "\ntry\n {" + "\n" + lower_case
						+ " = new SimpleDateFormat(\"dd-MM-yyyy\").parse(request.getParameter(\"" + lower_case + "\"));"
						+ "\n} catch (ParseException e) \n{"

						+ "\ne.printStackTrace();" + "\n}");
				date_set.append("\n" + table_name_lower + ".set" + first_upper + "(" + lower_case + ");");

				date_model_variable
						.append("\n\n@Column(name = \"" + only_upper + "\")" + "\nprivate Date\t" + lower_case + ";");

				date_model_set_variable
						.append("\n\npublic Date get" + first_upper + "(){" + "\nreturn\t" + lower_case + ";" + "\n }"

								+ "\n\npublic void set" + first_upper + "(Date\t" + lower_case + ")" + "\n{" + "\nthis."
								+ lower_case + " = " + lower_case + ";" + "\n}");
				date_array_para.append(",Date\t" + lower_case);

				date_array_value.append(only_upper + ",");

				date_array_value_for_lower.append("," + lower_case);

				date_array_set_in_dao
						.append("\n" + table_name_lower + ".set" + first_upper + "(\"" + only_upper + "\");");

				date_array_set_in_dao_for_date.append(
						"\n" + table_name_lower + ".set" + first_upper + "(rs.getDate(\"" + only_upper + "\"));");

				// ACTION BUILDER CODE
				cff_insert_button_setter.append(table_name_lower + ".set" + first_upper + "(" + lower_case + ");\n");

				// ui_name.properties file
				properties_file_fields_code.append(lower_case + ", ");
				// properties_file_setters_code.append("${table_name}.set" + first_upper + "(" +
				// lower_case + "),");
			}

		}

		// LONGTEXT
		for (int i = 0; i < longtext_list.size(); i++) {
			String field_name = longtext_list.get(i).getMapping();
			String data_type = longtext_list.get(i).getDataType();

			String lower_case = field_name.toLowerCase();
			String only_upper = field_name.toUpperCase();
			String first_upper = lower_case.substring(0, 1).toUpperCase() + lower_case.substring(1);

			if (data_type.equals("longtext")) {
				longtext_para.append("\n\t String " + lower_case + "=request.getParameter(\"" + lower_case + "\");");
				/*
				 * longtext_para.append("\n String\t" +lower_case+" = null;" );
				 */
				longtext_set.append("\n" + table_name_lower + ".set" + first_upper + "(" + lower_case + ");");

				longtext_model_variable
						.append("\n\n@Column(name = \"" + only_upper + "\")" + "\nprivate String\t" + lower_case + ";");

				longtext_model_set_variable
						.append("\n\npublic String get" + first_upper + "(){" + "\nreturn\t" + lower_case + ";" + "\n }"

								+ "\n\npublic void set" + first_upper + "(String\t" + lower_case + ")" + "\n{"
								+ "\nthis." + lower_case + " = " + lower_case + ";" + "\n}");
				longtext_array_para.append(",String\t" + lower_case);

				longtext_array_value.append(only_upper + ",");

				longtext_array_value_for_lower.append("," + lower_case);

				longtext_array_set_in_dao
						.append("\n" + table_name_lower + ".set" + first_upper + "(\"" + only_upper + "\");");

				longtext_array_set_in_dao_for_date.append(
						"\n" + table_name_lower + ".set" + first_upper + "(rs.getString(\"" + only_upper + "\"));");

				// FOR ACTION BUILDER CODE
				cff_insert_button_setter.append(table_name_lower + ".set" + first_upper + "(" + lower_case + ");\n");

				// ui_name.properties file
				properties_file_fields_code.append(lower_case + ", ");
				// cff_properties_file_setters_code.append("${table_name}.set" + first_upper +
				// "(" + lower_case + "),");
			}
		}

		// DOUBLE
		for (int i = 0; i < double_list.size(); i++) {
			String field_name = double_list.get(i).getMapping();
			String data_type = double_list.get(i).getDataType();

			String lower_case = field_name.toLowerCase();
			String only_upper = field_name.toUpperCase();
			String first_upper = lower_case.substring(0, 1).toUpperCase() + lower_case.substring(1);

			if (data_type.equals("double")) {
				double_para.append("\n\t String " + lower_case + "=request.getParameter(\"" + lower_case + "\");");

				double_para.append("\n String\t" + lower_case + " = null;");
				double_set.append("\n" + table_name_lower + ".set" + first_upper + "(" + lower_case + ");");

				double_model_variable
						.append("\n\n@Column(name = \"" + only_upper + "\")" + "\nprivate String\t" + lower_case + ";");

				double_model_set_variable
						.append("\n\npublic String get" + first_upper + "(){" + "\nreturn\t" + lower_case + ";" + "\n }"

								+ "\n\npublic void set" + first_upper + "(String\t" + lower_case + ")" + "\n{"
								+ "\nthis." + lower_case + " = " + lower_case + ";" + "\n}");
				double_array_para.append(",String\t" + lower_case);

				double_array_value.append(only_upper + ",");

				double_array_value_for_lower.append("," + lower_case);

				double_array_set_in_dao
						.append("\n" + table_name_lower + ".set" + first_upper + "(\"" + only_upper + "\");");

				double_array_set_in_dao_for_date.append(
						"\n" + table_name_lower + ".set" + first_upper + "(rs.getString(\"" + only_upper + "\"));");

				// FOR ACTION BUILDER CODE
				cff_insert_button_setter.append(table_name_lower + ".set" + first_upper + "(" + lower_case + ");\n");

				// ui_name.properties file
				properties_file_fields_code.append(lower_case + ", ");
				// cff_properties_file_setters_code.append("${table_name}.set" + first_upper +
				// "(" + lower_case + "),");
			}

		}
		// ---------- datatype end here-------------
		
		// -------------------------------attribute flex values---------------------------------------------------------------
		for (int i = 0; i < attribute_flex.size(); i++) {

			String lookup_code = attribute_flex.get(i).getLookupCode();
			String lower_case = lookup_code.toLowerCase();
			String first_upper = lower_case.substring(0, 1).toUpperCase() + lower_case.substring(1);
			String only_upper = lookup_code.toUpperCase();

			attribute_set_for_grid_dao
					.append("\n" + table_name_lower + ".set" + first_upper + "(rs.getString(\"" + only_upper + "\"));");

			if (line_table_name != null && !line_table_name.isEmpty()) {
				System.out.println("---under iif------");
				String line_table_name_lower = line_table_name.toLowerCase();
				attribute_set_for_grid_dao_line.append("\n" + line_table_name_lower + ".set" + first_upper
						+ "(rs.getString(\"" + only_upper + "\"));");
			}

			attribute_for_select_statment.append(only_upper);

			if (i < (attribute_flex.size() - 1)) {
				attribute_for_select_statment.append(",");
			}

			attribute_for_select_statment.append("\t");

			attribute_for_select_statment_lower.append(lower_case);

			if (i < (attribute_flex.size() - 1)) {
				attribute_for_select_statment_lower.append(",");
			}
			attribute_for_select_statment_lower.append("\t");

			model_class_for_attribute
					.append("\n@Column(name = \"" + only_upper + "\")" + "\nprivate String \t" + lower_case + ";");

			model_class_for_attribute2
					.append("\n\npublic String get" + first_upper + "() \n{" + "\nreturn\t" + lower_case + ";\n}"

							+ "\n\npublic void set" + first_upper + "(String\t" + lower_case + ")\n{" + "\nthis."
							+ lower_case + "=" + lower_case + ";" + "\n}");

			get_parameter.append("\nString \t" + lower_case + "=request.getParameter(\"" + lower_case + "\");");

			set_parameter.append(
					"\n" + table_name_lower + ".set" + first_upper + "(" + lower_case + ");" + "\n" + table_name_lower
							+ ".setCreated_by(user_id);" + "\n" + table_name_lower + ".setLast_updated_by(user_id);");

			// .properties file field code
			properties_file_fields_code.append(lower_case + ", ");
			properties_file_line_fields_code.append(lower_case + ", ");
			
		}

		/* =======    builder file code start   ============== */
		
		prefield_controller.append("\n\n//-----------------------for prefield part-----------------------------------"
				+ "\n\n@RequestMapping(value = \"/" + table_name_lower + "_update\", method = RequestMethod.GET)"
				+ "\npublic ModelAndView loadReport1(@RequestParam(value = \"" + field_name_for_id_lower
				+ "\") String\t" + field_name_for_id_lower + ", ModelAndView modelview,"
				+ "\nHttpServletRequest request, ModelMap map) throws IOException {"
				+ "\nint u_id = Integer.parseInt(" + field_name_for_id_lower + ");"
				+ "\n" + table_name_first_upper
				+ "\t" + table_name_lower + " = new\t" + table_name_first_upper + "();");
		
		if (line_table_name != null && !line_table_name.isEmpty()) {
			String line_table_name_lower = line_table_name.toLowerCase();
			String line_table_name_first_upper = line_table_name_lower.substring(0, 1).toUpperCase()
					+ line_table_name_lower.substring(1);
			prefield_controller.append("\n" + line_table_name_first_upper + "\t" + line_table_name_lower + " = new\t"
					+ line_table_name_first_upper + "();");
			
			// .PROPERTIES FILE CODE
			properties_file_line_code.append("line_table_name = " + line_table_name_first_upper + "\n");
		}
		
		// CFF ACTION BUILDER CODE IN CONTROLLER..
		prefield_controller.append("\nmap.addAttribute(\"" + table_name_lower + "_update\"," + table_name_lower + ");"
				+ "\nList<" + table_name_first_upper + "> report =" + service_name_lower + ".prefield(u_id);\n"
				+ "int updt_id = report.get(0).getId();\n" 
				// 
				+ "map.addAttribute(\"" + "cff_id\", update_id);\n" 
				+ "\nmap.addAttribute(\"" + table_name_lower + "_update\", report);");

		if (line_table_name != null && !line_table_name.isEmpty()) {
			String line_table_name_lower = line_table_name.toLowerCase();
			String line_table_name_first_upper = line_table_name_lower.substring(0, 1).toUpperCase()
					+ line_table_name_lower.substring(1);
			prefield_controller.append("List<" + line_table_name_first_upper + "> grp_menu_list_line = "
					+ service_name_lower + ".update_group_menu_line(u_id);"
					+ "\nmap.addAttribute(\"linelist\", grp_menu_list_line);" + "\nmap.addAttribute(\""
					+ line_table_name_lower + "_update\"," + line_table_name_lower + ");");
		}

		prefield_controller.append("\nreturn new ModelAndView(\"" + table_name_first_upper + "_update\");" + "\n}");

		prefield_controller_readonly
				.append("\n\n//--------------------for readonly------------------------------------------------"
						+ "\n\n@RequestMapping(value = \"/" + table_name_lower
						+ "_readonly\", method = RequestMethod.GET)"
						+ "\npublic ModelAndView loadReport2(@RequestParam(value = \"" + field_name_for_id_lower
						+ "\") String\t" + field_name_for_id_lower + ", ModelAndView modelview,"
						+ "\nHttpServletRequest request, ModelMap map) throws IOException \n{"
						+ "\nint u_id = Integer.parseInt(" + field_name_for_id_lower + ");" + "\n"
						+ table_name_first_upper + "\t" + table_name_lower + " = new\t" + table_name_first_upper
						+ "();");

		if (line_table_name != null && !line_table_name.isEmpty()) {

			String line_table_name_lower = line_table_name.toLowerCase();
			String line_table_name_first_upper = line_table_name_lower.substring(0, 1).toUpperCase()
					+ line_table_name_lower.substring(1);
			prefield_controller_readonly.append("\n" + line_table_name_first_upper + "\t" + line_table_name_lower
					+ " = new\t" + line_table_name_first_upper + "();");
		}
		prefield_controller_readonly.append("\nmap.addAttribute(\"" + table_name_lower + "_update\"," + table_name_lower
				+ ");" + "\nList<" + table_name_first_upper + "> report =" + service_name_lower + ".prefield(u_id);"
				+ "\nmap.addAttribute(\"" + table_name_lower + "_update\", report);");

		if (line_table_name != null && !line_table_name.isEmpty()) {
			String line_table_name_lower = line_table_name.toLowerCase();
			String line_table_name_first_upper = line_table_name_lower.substring(0, 1).toUpperCase()
					+ line_table_name_lower.substring(1);
			prefield_controller_readonly.append("List<" + line_table_name_first_upper + "> grp_menu_list_line = "
					+ service_name_lower + ".update_group_menu_line(u_id);"
					+ "\nmap.addAttribute(\"linelist\", grp_menu_list_line);" + "\nmap.addAttribute(\""
					+ line_table_name_lower + "_update\"," + line_table_name_lower + ");");
		}
		prefield_controller_readonly.append("\nreturn new ModelAndView(\"" + table_name_first_upper + "_readonly\");"

				+ "\n}");
		grid_controller.append(
				"\n\n//-----------------------------------for grid view only--------------------------------------------------"
						+ "\n\n@RequestMapping(value=\"/" + table_name_lower + "_grid_view\")"
						+ "\npublic ModelAndView\t" + table_name_lower
						+ "Details(ModelAndView model) throws IOException" + "\n{" + "\nList<" + table_name_first_upper
						+ ">\t" + table_name_lower + "=" + dao_name_lower + ".userlist();" + "\nmodel.addObject(\""
						+ table_name_lower + "\", " + table_name_lower + ");"
						+ "\nmodel.setViewName(\"" + table_name_first_upper + "_grid\");" + "\nreturn model;" + "\n}");

		for (int i = 0; i < id_list.size(); i++) {
			String field_name = id_list.get(i).getMapping();
			String lower_case = field_name.toLowerCase();
			String first_upper = lower_case.substring(0, 1).toUpperCase() + lower_case.substring(1);
			service_save_id.append("String\t" + lower_case + ",");
			service_save_id1.append(lower_case + ",");

			header_id_for_line.append(lower_case);

			header_id_for_line_for_comma.append(lower_case + ",");

			parameter_for_line_id.append("int\t" + lower_case + ",");

			if (line_table_name != null && !line_table_name.isEmpty()) {
				String line_table_name_lower = line_table_name.toLowerCase();
				set_parameter_header_id_line
						.append("\n" + line_table_name_lower + ".set" + first_upper + "(" + lower_case + ");");
			}

		}
		for (int i = 0; i < rn_userlist.size(); i++) {
			String field_name = rn_userlist.get(i).getMapping();
			String lower_case = field_name.toLowerCase();
			service_save_field.append("String\t" + lower_case);
			if (i < (rn_userlist.size() - 1)) {
				service_save_field.append(",");
			}
			service_save_field.append("\t");

			service_save_field1.append(lower_case);

			if (i < (rn_userlist.size() - 1)) {
				service_save_field1.append(",");
			}
			service_save_field1.append("\t");
		}

		save_controller.append(
				"\n\n//--------------------------submit update part---------------------------------------------------"
						+ "\n\n@RequestMapping(value = \"/" + table_name_lower
						+ "_update_submit\", method = RequestMethod.POST)"
						+ "\npublic ModelAndView saveReportRegister(@ModelAttribute\t" + table_name_first_upper + "\t"
						+ table_name_lower + ","
						+ "\nBindingResult resultReportRegister, ModelMap map, HttpServletRequest request) throws ParseException {"
						+ "\nint report = 0;"
// changed by @Niladri
//						+ sbmitparameterid + id_notpri_para + date_para + longtext_para + double_para + para
//						+ "\nint rowcount=" + field_name_for_id_lower + ".length();" 
//						+ "\nreport = " + service_name_lower + ".save(rowcount," + service_save_id1 + var_for_pass_para_id
//						+ service_save_field1 + date_array_value_for_lower + longtext_array_value_for_lower
//						+ double_array_value_for_lower + ");"
						+ "\nreport = " + service_name_lower + ".saveheader(" + table_name_lower + ");"
						+ "\nString check = request.getParameter(\"repupdt\");"
						+ "\nmap.addAttribute(\"check\", check);" + "\nmap.addAttribute(\"report\", report);"

						+ "\n\t" + table_name_first_upper + "\t rep_reg = new \t" + table_name_first_upper + "();"
						+ "\nmap.addAttribute(\"rep_reg\", rep_reg);"

						+ "\nList<" + table_name_first_upper + "> report_list = " + service_name_lower + ".userlist();"
						+ "\nmap.addAttribute(\"report_list\", report_list);"

						+ "\nreturn new ModelAndView(\"redirect:" + table_name_lower + "_grid_view\");" + "\n}");

// ----------------------header-line submit controller---------------------------------------------------

		if(form_type.equalsIgnoreCase("HEADER_LINE") || form_type.equalsIgnoreCase("LINE_ONLY")) {
			// .PROPERTIES FILE CODE FOR HL, LO FORM
			properties_file_line_code.append("line_fields = ");
		}
		// HEADER-LINE PK
		if (line_table_name != null && !line_table_name.isEmpty()) {
			String line_table_name_lower = line_table_name.toLowerCase();
			for (int i = 0; i < line_id_primary.size(); i++) {
				String field_name = line_id_primary.get(i).getMapping();
				String lower_case = field_name.toLowerCase();
				String first_upper = lower_case.substring(0, 1).toUpperCase() + lower_case.substring(1);
				String only_upper = field_name.toUpperCase();

				header_line_submit_id
						.append("\nString \t" + lower_case + "[]=request.getParameterValues(\"" + lower_case + "\");");

				line_id_for_rowcount.append(lower_case);

				parameter_for_line_id.append("String\t" + lower_case + "[],");

				array_para_for_id_line.append(lower_case + ",");
				array_para_for_id_line1.append(lower_case);

				get_parameter_for_id_line
						.append("\nif (" + lower_case + "[i] != null && \t" + lower_case + "[i].length() > 0)" + "\n{"
								+ " \ninfonum = Integer.parseInt(" + lower_case + "[i]);" + "\n}" + "\nelse" + "\n{"
								+ "\ninfonum =" + table_name_lower + ".get" + field_name_first_upper + "();" + " \n}");

				set_parameter_for_id_line.append("\n" + line_table_name_lower + ".set" + first_upper + "(infonum);");

				model_class_line1
						.append("\n@Column(name = \"" + only_upper + "\")" + "\nprivate int \t" + lower_case + ";");

				model_class_line2
						.append("\n\npublic int get" + first_upper + "() \n{" + "\nreturn\t" + lower_case + ";\n}"

								+ "\n\npublic void set" + first_upper + "(int\t" + lower_case + ")\n{" + "\nthis."
								+ lower_case + "=" + lower_case + ";" + "\n}");

				get_set_for_line_update_id.append(
						"\n" + line_table_name_lower + ".set" + first_upper + "(rs.getInt(\"" + only_upper + "\"));");

				// .PROPERTIES FILE CODE FOR HL, LO FORM
				properties_file_line_code.append(lower_case + ", ");

			}
		}

		// HEADER-LINE VARCHAR LIST && DATETIME
		if (line_table_name != null && !line_table_name.isEmpty()) {
			String line_table_name_lower = line_table_name.toLowerCase();
			for (int i = 0; i < line_varchar.size(); i++) {
				String field_name = line_varchar.get(i).getMapping();
				String lower_case = field_name.toLowerCase();
				String first_upper = lower_case.substring(0, 1).toUpperCase() + lower_case.substring(1);
				String only_upper = field_name.toUpperCase();

				String data_type = line_varchar.get(i).getDataType();

				// DATE TIME
				if (data_type.equals("datetime")) {
					header_line_submit_varchar.append("\nDate	\t" + lower_case + " = null;" + "\ntry" + "\n{" + "\n"
							+ lower_case + " = new SimpleDateFormat(\"dd-MM-yyyy\").parse(request.getParameter(\""
							+ lower_case + "\"));" + "\n} catch (ParseException e) " + "\n{" + "\ne.printStackTrace();"
							+ "\n}");

					model_class_line3.append(
							"\n@Column(name = \"" + only_upper + "\")" + "\nprivate Date \t" + lower_case + ";");

					model_class_line4
							.append("\n\npublic Date get" + first_upper + "() \n{" + "\nreturn\t" + lower_case + ";\n}"

									+ "\n\npublic void set" + first_upper + "(Date\t" + lower_case + ")\n{" + "\nthis."
									+ lower_case + "=" + lower_case + ";" + "\n}");

					get_set_for_line_update_varchar.append("\n" + line_table_name_lower + ".set" + first_upper
							+ "(rs.getDate(\"" + only_upper + "\"));");
					set_parameter_for_varchar_line
							.append("\n" + line_table_name_lower + ".set" + first_upper + "(" + lower_case + ");");
					
					// .PROPERTIES FILE CODE FOR HL, LO FORM
					properties_file_line_code.append(lower_case + ", ");
				}

				// ALL (INT, DOUBLE , LONGTEXT, VARCHAR)
				if (!data_type.equals("datetime")) {

					header_line_submit_varchar.append(
							"\nString \t" + lower_case + "[]=request.getParameterValues(\"" + lower_case + "\");");

					model_class_line3.append(
							"\n@Column(name = \"" + only_upper + "\")" + "\nprivate String \t" + lower_case + ";");

					model_class_line4.append(
							"\n\npublic String get" + first_upper + "() \n{" + "\nreturn\t" + lower_case + ";\n}"

									+ "\n\npublic void set" + first_upper + "(String\t" + lower_case + ")\n{"
									+ "\nthis." + lower_case + "=" + lower_case + ";" + "\n}");

					get_set_for_line_update_varchar.append("\n" + line_table_name_lower + ".set" + first_upper
							+ "(rs.getString(\"" + only_upper + "\"));");

					set_parameter_for_varchar_line
							.append("\n" + line_table_name_lower + ".set" + first_upper + "(" + lower_case + "[i]);");
					
					// .PROPERTIES FILE CODE FOR HL, LO FORM
					properties_file_line_code.append(lower_case + ", ");
				}

				if (data_type.equals("datetime")) {
					parameter_for_line_varchar.append("Date\t" + lower_case + "");
				}
				if (!data_type.equals("datetime")) {
					parameter_for_line_varchar.append("String\t" + lower_case + "[]");
				}

				if (i < (line_varchar.size() - 1)) {
					parameter_for_line_varchar.append(",");
				}
				parameter_for_line_varchar.append("\t");

				array_para_for_varchar_line.append(lower_case);

				if (i < (line_varchar.size() - 1)) {
					array_para_for_varchar_line.append(",");
				}
				array_para_for_varchar_line.append("\t");

			}

		}
		if (line_table_name != null && !line_table_name.isEmpty()) {
			String line_table_name_lower = line_table_name.toLowerCase();
			action.append(line_table_name_lower + "_submit_HeaderLine");
			action2.append(line_table_name_lower + "_submit_HeaderLine");
		} else {
			action.append(table_name_lower + "_submit");
			action2.append(table_name_lower + "_update_submit");
			action3.append(table_name_lower + "_field_submit");
		}

		if (line_table_name != null && !line_table_name.isEmpty()) {
			String line_table_name_lower = line_table_name.toLowerCase();
			String line_table_name_first_upper = line_table_name_lower.substring(0, 1).toUpperCase()
					+ line_table_name_lower.substring(1);
			header_line_submit.append(
					"\n\n//-----------------------------header line submit------------------------------------------------"

							+ "\n\n@RequestMapping(\"/" + action + "\")"
							+ "\npublic ModelAndView saveGroupReport(@ModelAttribute\t" + table_name_first_upper + "\t"
							+ table_name_lower + ", BindingResult resultMenuGroupHeader," + "\n"
							+ line_table_name_first_upper + "\t" + line_table_name_lower
							+ ", BindingResult resultReportMenuLines,"
							+ "\nModelMap map, HttpServletRequest request) throws ParseException" + "\n{"
							+ "int user_id=(Integer)request.getSession().getAttribute(\"userid\");" + "\n"
							+ id_primary_parameter_submit + id_para_submit + para + date_para + longtext_para
							+ double_para + get_parameter + "\n" + id_notpri_set + set_para + date_set + longtext_set
							+ double_set + set_parameter + "\nint\t" + header_id_for_line + "=" + service_name_lower
							+ ".saveheader(" + table_name_lower + ");"

							+ header_line_submit_id + header_line_submit_varchar + "\nint rowcount="
							+ line_id_for_rowcount + ".length;"

							+ "\nString check = request.getParameter(\"menuupdt\");"
							+ "\nmap.addAttribute(\"check\", check);" + "\nint group_line = " + service_name_lower
							+ ".addmenuGroupLine(rowcount," + header_id_for_line_for_comma + array_para_for_id_line
							+ array_para_for_varchar_line + ");" + "\nmap.addAttribute(\"group_line\", group_line);"

							+ "\nList<" + table_name_first_upper + "> header_list =" + dao_name_lower + ".userlist();"
							+ "\nmap.addAttribute(\"header_list\",header_list);"

							+ "\nreturn new ModelAndView(\"redirect:" + table_name_lower + "_grid_view\");"

							+ " \n}");
		}
		for (int i = 0; i < rn_userlist.size(); i++) {

			String type_field = rn_userlist.get(i).getType_field();
			String mapping = rn_userlist.get(i).getMapping();
			String mapping_lower = mapping.toLowerCase();
			String sp_name = rn_userlist.get(i).getSp_name_for_dropdown();

			if (!sp_name.isEmpty() && type_field.equals("dropdown")) {
				dropdown_controller.append("\n@RequestMapping(value = \"/" + mapping_lower
						+ "_list\", method = RequestMethod.GET)" + "\npublic void \t" + mapping_lower
						+ "_list( HttpServletRequest request, HttpServletResponse response) {"
						+ "\nList<LookupValues> droplist = new ArrayList<LookupValues>();" + "\ntry {"
						+ "\nCallableStatement cStmt;" + "\ntry {"
						+ "\ncStmt = hibernateConfiguration.dataSource().getConnection()" + "\n.prepareCall(\"{call \t"
						+ sp_name + "()}\");" + "\nResultSet rs = cStmt.executeQuery();" + "\nwhile (rs.next()) "
						+ "\n{" + "\nString data = rs.getString(1);" + "\nLookupValues menu = new LookupValues();"
						+ "\nmenu.setDrop_value(data);" + "\ndroplist.add(menu);" + "\n}"
						+ "\n} catch (SQLException e) {" + "\ne.printStackTrace();" + "\n}" + "\ncatch (Exception e) {"
						+ "\nSystem.out.println(e.getMessage());" + "\n}" + "\nString json = null;"
						+ "\njson = new Gson().toJson(droplist);" + "\nresponse.setContentType(\"application/json\");"
						+ "\nresponse.getWriter().write(json);" + "\n} catch (IOException e) {"
						+ "\ne.printStackTrace();" + " \n}}");
			}

			if (!sp_name.isEmpty() && type_field.equals("autocomplete")) {
				dropdown_controller.append("\n@RequestMapping(value = \"/" + mapping_lower
						+ "_list\", method = RequestMethod.GET)" + "\npublic void \t" + mapping_lower
						+ "_list( HttpServletRequest request, HttpServletResponse response) {"
						+ "\nList<LookupValues> droplist = new ArrayList<LookupValues>();" + "\ntry {"
						+ "\nCallableStatement cStmt;" + "\ntry {"
						+ "\ncStmt = hibernateConfiguration.dataSource().getConnection()" + "\n.prepareCall(\"{call \t"
						+ sp_name + "()}\");" + "\nResultSet rs = cStmt.executeQuery();" + "\nwhile (rs.next()) "
						+ "\n{" + "\nString data = rs.getString(1);" + "\nLookupValues menu = new LookupValues();"
						+ "\nmenu.setDrop_value(data);" + "\ndroplist.add(menu);" + "\n}"
						+ "\n} catch (SQLException e) {" + "\ne.printStackTrace();" + "\n}" + "\ncatch (Exception e) {"
						+ "\nSystem.out.println(e.getMessage());" + "\n}" + "\nString json = null;"
						+ "\njson = new Gson().toJson(droplist);" + "\nresponse.setContentType(\"application/json\");"
						+ "\nresponse.getWriter().write(json);" + "\n} catch (IOException e) {"
						+ "\ne.printStackTrace();" + " \n}}");
			}

		}

		for (int i = 0; i < line_varchar.size(); i++) {

			String type_field = line_varchar.get(i).getType_field();
			String mapping = line_varchar.get(i).getMapping();
			String mapping_lower = mapping.toLowerCase();
			String sp_name = line_varchar.get(i).getSp_name_for_dropdown();

			if (!sp_name.isEmpty() && type_field.equals("dropdown")) {
				dropdown_controller.append("\n@RequestMapping(value = \"/" + mapping_lower
						+ "_list_line\", method = RequestMethod.GET)" + "\npublic void \t" + mapping_lower
						+ "_list( HttpServletRequest request, HttpServletResponse response) {"
						+ "\nList<LookupValues> droplist = new ArrayList<LookupValues>();" + "\ntry {"
						+ "\nCallableStatement cStmt;" + "\ntry {"
						+ "\ncStmt = hibernateConfiguration.dataSource().getConnection()" + "\n.prepareCall(\"{call \t"
						+ sp_name + "()}\");" + "\nResultSet rs = cStmt.executeQuery();" + "\nwhile (rs.next()) "
						+ "\n{" + "\nString data = rs.getString(1);" + "\nLookupValues menu = new LookupValues();"
						+ "\nmenu.setDrop_value(data);" + "\ndroplist.add(menu);" + "\n}"
						+ "\n} catch (SQLException e) {" + "\ne.printStackTrace();" + "\n}" + "\ncatch (Exception e) {"
						+ "\nSystem.out.println(e.getMessage());" + "\n}" + "\nString json = null;"
						+ "\njson = new Gson().toJson(droplist);" + "\nresponse.setContentType(\"application/json\");"
						+ "\nresponse.getWriter().write(json);" + "\n} catch (IOException e) {"
						+ "\ne.printStackTrace();" + " \n}}");
			}

			if (!sp_name.isEmpty() && type_field.equals("autocomplete")) {
				dropdown_controller.append("\n@RequestMapping(value = \"/" + mapping_lower
						+ "_list_line\", method = RequestMethod.GET)" + "\npublic void \t" + mapping_lower
						+ "_list( HttpServletRequest request, HttpServletResponse response) {"
						+ "\nList<LookupValues> droplist = new ArrayList<LookupValues>();" + "\ntry {"
						+ "\nCallableStatement cStmt;" + "\ntry {"
						+ "\ncStmt = hibernateConfiguration.dataSource().getConnection()" + "\n.prepareCall(\"{call \t"
						+ sp_name + "()}\");" + "\nResultSet rs = cStmt.executeQuery();" + "\nwhile (rs.next()) "
						+ "\n{" + "\nString data = rs.getString(1);" + "\nLookupValues menu = new LookupValues();"
						+ "\nmenu.setDrop_value(data);" + "\ndroplist.add(menu);" + "\n}"
						+ "\n} catch (SQLException e) {" + "\ne.printStackTrace();" + "\n}" + "\ncatch (Exception e) {"
						+ "\nSystem.out.println(e.getMessage());" + "\n}" + "\nString json = null;"
						+ "\njson = new Gson().toJson(droplist);" + "\nresponse.setContentType(\"application/json\");"
						+ "\nresponse.getWriter().write(json);" + "\n} catch (IOException e) {"
						+ "\ne.printStackTrace();" + " \n}}");
			}

		}

		controller.append("\npackage com.realnet." + module_name + ".controller;"
				+ "\n import javax.servlet.http.HttpServletRequest;"
				+ "\nimport org.springframework.beans.factory.annotation.Autowired;"
				+ "\nimport org.springframework.stereotype.Controller;"
				+ "\nimport org.springframework.validation.BindingResult;"
				+ "\nimport org.springframework.web.bind.annotation.ModelAttribute;"
				+ "\nimport org.springframework.web.bind.annotation.RequestMapping;"
				+ "\nimport org.springframework.web.bind.annotation.RequestMethod;"
				+ "\nimport org.springframework.web.servlet.ModelAndView;" + "\nimport java.io.IOException;"
				+ "\nimport java.util.List;" + "\nimport org.springframework.orm.hibernate5.HibernateTemplate;"
				+ "\nimport org.springframework.web.bind.annotation.RequestParam;"
				+ "\nimport javax.transaction.Transactional;" + "\nimport org.springframework.ui.ModelMap;"
				+ "\nimport javax.servlet.http.HttpServletResponse;" + "\nimport java.util.ArrayList;"
				+ "\nimport java.sql.CallableStatement;" + "\nimport java.sql.ResultSet;"
				+ "\nimport java.sql.SQLException;" + "\nimport com.google.gson.Gson;" + "\nimport java.util.Date;"
				+ "\nimport com.realnet.configuration.HibernateConfiguration;" + "\nimport java.text.SimpleDateFormat;"
				+ "\nimport com.realnet." + module_name + ".dao." + dao_name_first_upper + ";" + "\nimport com.realnet."
				+ module_name + ".service." + service_name_first_upper + ";" + "\nimport com.realnet." + module_name
				+ ".model." + table_name_first_upper + ";" + "import java.text.ParseException;"
				+ "import javax.servlet.http.HttpSession;");

		if (line_table_name != null && !line_table_name.isEmpty()) {
			String line_table_name_lower = line_table_name.toLowerCase();
			String line_table_name_first_upper = line_table_name_lower.substring(0, 1).toUpperCase()
					+ line_table_name_lower.substring(1);
			controller.append("import com.realnet." + module_name + ".model." + line_table_name_first_upper + ";");
		}
		controller.append("\n@Controller" + "\npublic class\t" + controller_name_first_upper + "\n{"

				+ "\n@Autowired" + "\nprivate HibernateTemplate  hibernateTemplate;" + "\n@Autowired" + "\nprivate\t"
				+ service_name_first_upper + "\t" + service_name_lower + ";" + "\n@Autowired"
				+ "\nHibernateConfiguration hibernateConfiguration;" + "\n@Autowired" + "\nprivate\t"
				+ dao_name_first_upper + "\t" + dao_name_lower + ";"
				+ "\n\n//----------------------entry form sbmit------------------------------------------"

				+ "\n@Transactional" + "\n@RequestMapping(value = \"/" + table_name_lower
				+ "_submit\", method = RequestMethod.POST)"
				+ "\npublic ModelAndView saveServiceRequest(@ModelAttribute\t" + table_name_first_upper + "\t"
				+ table_name_lower + ",BindingResult resultKoel_user ,"
				+ "\nModelMap map, HttpServletRequest request)  " + "\n{"
				+ "int user_id=(Integer)request.getSession().getAttribute(\"userid\");" + id_para_submit + para
				+ date_para + longtext_para + double_para + get_parameter + "\n" + id_notpri_set + set_para + date_set
				+ longtext_set + double_set + set_parameter + "\nhibernateTemplate.saveOrUpdate(" + table_name_lower
				+ ");" + "\nreturn new ModelAndView(\"redirect:" + table_name_lower + "_grid_view\");" + "\n" + "\n\n}"
				+ "\n\n" + "\n@RequestMapping(\"/" + table_name_lower + "_entryform\")"
				+ "\npublic ModelAndView input_form3(HttpServletRequest request, ModelMap map) " + "\n{"
				+ "\nreturn new ModelAndView(\"" + table_name_first_upper + "_view\");" + "\n}"

				+ grid_controller + prefield_controller + "\n\n" + update_field_prefield_controller + "\n\n"
				+ prefield_controller_readonly + "\n\n" + save_controller + header_line_submit + dropdown_controller
				+ "\n}");

		for (int i = 0; i < id_list.size(); i++) {
			String field_name = id_list.get(i).getMapping();
			String lower_case = field_name.toLowerCase();
			String first_upper = lower_case.substring(0, 1).toUpperCase() + lower_case.substring(1);
			String only_upper = field_name.toUpperCase();

			System.out.println("model_class3 field_name" + field_name);

			model_class3.append("\n@Column(name = \"" + only_upper + "\")" + "\nprivate int \t" + lower_case + ";");

			model_class4.append("\n\npublic int get" + first_upper + "() \n{" + "\nreturn\t" + lower_case + ";\n}"

					+ "\n\npublic void set" + first_upper + "(int\t" + lower_case + ")\n{" + "\nthis." + lower_case
					+ "=" + lower_case + ";" + "\n}");

		}
		for (int i = 0; i < rn_userlist.size(); i++) {
			String field_name = rn_userlist.get(i).getMapping();
			String lower_case = field_name.toLowerCase();
			String first_upper = lower_case.substring(0, 1).toUpperCase() + lower_case.substring(1);
			String only_upper = field_name.toUpperCase();

			model_class1.append("\n@Column(name = \"" + only_upper + "\")" + "\nprivate String \t" + lower_case + ";");

			model_class2.append("\n\npublic String get" + first_upper + "() \n{" + "\nreturn\t" + lower_case + ";\n}"

					+ "\n\npublic void set" + first_upper + "(String\t" + lower_case + ")\n{" + "\nthis." + lower_case
					+ "=" + lower_case + ";" + "\n}");

		}

// --------------------------------  ModeL class code------------------------------------------------//

		model_class.append("   package com.realnet." + module_name + ".model;"

				+ "\nimport javax.persistence.Column;" + "\nimport javax.persistence.Entity;"
				+ "\nimport javax.persistence.GeneratedValue;" + "\nimport javax.persistence.GenerationType;"
				+ "\nimport javax.persistence.Id;" + "\nimport javax.persistence.SequenceGenerator;"
				+ "\nimport javax.persistence.Table;" + "\nimport org.hibernate.annotations.GenericGenerator;"
				+ "\nimport java.util.Date;" + "\n@Entity" + "\n@Table(name = \"" + table_name_upper + "\")"
				+ "\npublic class\t" + table_name_first_upper + "\n{" + "\n@Id"
				+ "\n@GeneratedValue(strategy = GenerationType.AUTO, generator =\"native\")"
				+ "\n@GenericGenerator(name = \"native\", strategy = \"native\")" + model_class3 + model_class_for_var
				+ date_model_variable + longtext_model_variable + double_model_variable + "\n" + model_class4 + "\n"
				+ model_class1 + "\n" + model_class2 + date_model_set_variable + longtext_model_set_variable
				+ double_model_set_variable + model_class_for_set + model_class_for_attribute
				+ model_class_for_attribute2

				+ "\n@Column(name = \"CREATED_BY\")" + "\nprivate int 	created_by;"

				+ "\n@Column(name = \"LAST_UPDATED_BY\")" + "\nprivate int 	last_updated_by;"

				+ "\n@Column(name = \"CREATION_DATE\")" + "\nprivate Date 	creation_date;"

				+ "\n@Column(name = \"LAST_UPDATE_DATE\")" + "\nprivate Date 	last_update_date;"

				+ "\npublic int getCreated_by() {" + "	\nreturn created_by;" + "\n}"

				+ "\npublic void setCreated_by(int created_by) {" + "\n	this.created_by = created_by;" + "\n}"

				+ "\npublic int getLast_updated_by() {" + "\n	return last_updated_by;" + "\n}"

				+ "\npublic void setLast_updated_by(int last_updated_by) {"
				+ "\n	this.last_updated_by = last_updated_by;" + "\n}"

				+ "\npublic Date getCreation_date() {" + "\n	return creation_date;" + "\n}"

				+ "\npublic void setCreation_date(Date creation_date) {" + "\n	this.creation_date = creation_date;"
				+ "\n}"

				+ "\npublic Date getLast_update_date() {" + "\n		return last_update_date;" + "\n}"

				+ "\npublic void setLast_update_date(Date last_update_date) {"
				+ "\n	this.last_update_date = last_update_date;" + "\n}"

				+ "\n@Column(name = \"ACCOUNT_ID\")" + "\nprivate int 	account_id;"

				+ "\npublic int getAccount_id() {" + "\n	return account_id;" + "\n}"

				+ "\npublic void setAccount_id(int account_id) {" + "\n	this.account_id = account_id;" + "\n}" + "\n}");

		if (line_table_name != null && !line_table_name.isEmpty()) {
			String line_table_name_lower = line_table_name.toLowerCase();
			String line_table_name_first_upper = line_table_name_lower.substring(0, 1).toUpperCase()
					+ line_table_name_lower.substring(1);
			String line_table_name_upper = line_table_name_lower.toUpperCase();

			model_class_for_line.append("\npackage com.realnet." + module_name + ".model;"

					+ "\nimport javax.persistence.Column;" + "\nimport javax.persistence.Entity;"
					+ "\nimport javax.persistence.GeneratedValue;" + "\nimport javax.persistence.GenerationType;"
					+ "\nimport javax.persistence.Id;" + "\nimport javax.persistence.SequenceGenerator;"
					+ "\nimport javax.persistence.Table;" + "\nimport org.hibernate.annotations.GenericGenerator;"
					+ "\nimport java.util.Date;" + "\n@Entity" + "\n@Table(name = \"" + line_table_name_upper + "\")"
					+ "\npublic class\t" + line_table_name_first_upper + "\n{" + "\n@Id"
					+ "\n@GeneratedValue(strategy = GenerationType.AUTO, generator =\"native\")"
					+ "\n@GenericGenerator(name = \"native\", strategy = \"native\")" + model_class_line1
					+ model_class_line3 + model_class3 + model_class_line2 + model_class_line4 + model_class4
					+ model_class_for_attribute + model_class_for_attribute2

					+ "\n@Column(name = \"ACCOUNT_ID\")" + "\nprivate int 	account_id;"

					+ "\npublic int getAccount_id() {" + "\n	return account_id;" + "\n}"

					+ "\npublic void setAccount_id(int account_id) {" + "\n	this.account_id = account_id;" + "\n}"
					+ "\n}");
		}

// ------------------------------service part-------------------------------------//

		service.append("\npackage com.realnet." + module_name + ".service;" + "\nimport java.util.Date;"
				+ "\nimport java.util.ArrayList;" + "\nimport java.util.List;"
				+ "\nimport org.springframework.stereotype.Service;" + "\nimport com.realnet." + module_name + ".model."
				+ table_name_first_upper + ";");
		if (line_table_name != null && !line_table_name.isEmpty()) {
			String line_table_name_lower = line_table_name.toLowerCase();
			String line_table_name_first_upper = line_table_name_lower.substring(0, 1).toUpperCase()
					+ line_table_name_lower.substring(1);
			service.append("\nimport com.realnet." + module_name + ".model." + line_table_name_first_upper + ";");
		}
		service.append("\npublic interface\t" + service_name_first_upper + "\n{"

				+ "\npublic List<" + table_name_first_upper + "> prefield(int u_id);" + "\npublic List<"
				+ table_name_first_upper + "> userlist();" + "\npublic int saveheader(" + table_name_first_upper + "\t"
				+ table_name_lower + ");");

		if (table_name != null && !table_name.isEmpty()) {
			service.append("\npublic int save(int rowcount," + service_save_id + var_for_pass_para + service_save_field
					+ date_array_para + longtext_array_para + double_array_para + "); ");
		}

		if (line_table_name != null && !line_table_name.isEmpty() && !line_table_name.equals("")) {
			service.append("\npublic int addmenuGroupLine(int rowcount," + parameter_for_line_id
					+ parameter_for_line_varchar + ");");

			String line_table_name_lower = line_table_name.toLowerCase();
			String line_table_name_first_upper = line_table_name_lower.substring(0, 1).toUpperCase()
					+ line_table_name_lower.substring(1);
			service.append("\npublic List<" + line_table_name_first_upper + "> update_group_menu_line(int \t"
					+ array_para_for_id_line1 + ");");

		}

		service.append("\n}");

		service_impl.append("\npackage com.realnet." + module_name + ".service;" + "\nimport java.util.Date;"
				+ "\nimport java.util.ArrayList;" + "\nimport java.util.List;"
				+ "\nimport org.springframework.beans.factory.annotation.Autowired;"
				+ "\nimport org.springframework.stereotype.Component;"
				+ "\nimport org.springframework.stereotype.Service;" + "\nimport com.realnet." + module_name + ".dao."
				+ dao_name_first_upper + ";" + "\nimport com.realnet." + module_name + ".model."
				+ table_name_first_upper + ";");

		if (line_table_name != null && !line_table_name.isEmpty()) {
			String line_table_name_lower = line_table_name.toLowerCase();
			String line_table_name_first_upper = line_table_name_lower.substring(0, 1).toUpperCase()
					+ line_table_name_lower.substring(1);
			service_impl.append("\nimport com.realnet." + module_name + ".model." + line_table_name_first_upper + ";");
		}
		service_impl.append("\n@Service" + "\npublic class\t" + service_impl_name_first_upper + "\timplements\t"
				+ service_name_first_upper + "\n {" + "\n\n@Autowired" + "\nprivate \t" + dao_name_first_upper + "\t"
				+ dao_name_lower + ";" + "\n\n@Override" + " \npublic List<" + table_name_first_upper
				+ "> prefield(int u_id)" + "\n{" + "\nreturn\t" + dao_name_lower + ".prefield(u_id);" + "\n }"
				+ "\n@Override" + "\npublic List<" + table_name_first_upper + "> userlist() " + "\n{" + "\nreturn\t"
				+ dao_name_lower + ".userlist();" + "\n}" + "\n@Override"

				+ "\npublic int save(int rowcount," + service_save_id + var_for_pass_para + service_save_field
				+ date_array_para + longtext_array_para + double_array_para + ") " + "\n{" + "\nreturn\t"
				+ dao_name_lower + ".save(rowcount," + service_save_id1 + var_for_pass_para_id + service_save_field1
				+ date_array_value_for_lower + longtext_array_value_for_lower + double_array_value_for_lower + ");"
				+ "\n}" + "\npublic int saveheader(" + table_name_first_upper + "\t" + table_name_lower + ")" + "{"
				+ "return \t" + dao_name_lower + ".saveheader(" + table_name_lower + ");" + "}");

		if (line_table_name != null && !line_table_name.isEmpty() && !line_table_name.equals("")) {
			service_impl.append("\npublic int addmenuGroupLine(int rowcount," + parameter_for_line_id
					+ parameter_for_line_varchar + ")" + "\n{" + "return \t" + dao_name_lower
					+ ".addmenuGroupLine(rowcount," + header_id_for_line_for_comma + array_para_for_id_line
					+ array_para_for_varchar_line + ");" + "\n}");
			String line_table_name_lower = line_table_name.toLowerCase();
			String line_table_name_first_upper = line_table_name_lower.substring(0, 1).toUpperCase()
					+ line_table_name_lower.substring(1);
			service_impl.append("\npublic List<" + line_table_name_first_upper + "> update_group_menu_line(int \t"
					+ array_para_for_id_line1 + ")" + "\n{"

					+ "\nreturn \t" + dao_name_lower + ".update_group_menu_line(" + array_para_for_id_line1 + ");"

					+ "\n}");
		}
		service_impl.append("\n}"

		);

		
//------------------------------------DAO code ------------------------------------------------
		 
      dao.append("\npackage com.realnet." + module_name + ".dao;" + "\nimport java.util.Date;"
				+ "\nimport java.util.List;" + "\nimport com.realnet." + module_name + ".model."
				+ table_name_first_upper + ";");
		if (line_table_name != null && !line_table_name.isEmpty()) {
			String line_table_name_lower = line_table_name.toLowerCase();
			String line_table_name_first_upper = line_table_name_lower.substring(0, 1).toUpperCase()
					+ line_table_name_lower.substring(1);
			dao.append("\nimport com.realnet." + module_name + ".model." + line_table_name_first_upper + ";");
		}
		dao.append("\npublic interface\t" + dao_name_first_upper + " {	" + "\npublic List<" + table_name_first_upper
				+ "> userlist();" + "\npublic List<" + table_name_first_upper + "> prefield(int u_id);"
				+ "\npublic int save(int rowcount," + service_save_id + var_for_pass_para + service_save_field
				+ date_array_para + longtext_array_para + double_array_para + ");"

				+ "\npublic int saveheader(" + table_name_first_upper + "\t" + table_name_lower + ");");
		if (line_table_name != null && !line_table_name.isEmpty()) {
			String line_table_name_lower = line_table_name.toLowerCase();
			String line_table_name_first_upper = line_table_name_lower.substring(0, 1).toUpperCase()
					+ line_table_name_lower.substring(1);
			dao.append("public int addmenuGroupLine(int rowcount," + parameter_for_line_id + parameter_for_line_varchar
					+ ");");

			dao.append("\npublic List<" + line_table_name_first_upper + "> update_group_menu_line(int \t"
					+ array_para_for_id_line1 + ");");
		}
		dao.append("\n}");

		for (int i = 0; i < id_list.size(); i++) {
			String field_name = id_list.get(i).getMapping();
			String lower_case = field_name.toLowerCase();
			String first_upper = lower_case.substring(0, 1).toUpperCase() + lower_case.substring(1);
			String upper_case = field_name.toUpperCase();

			sqlField_id.append(upper_case + ",");

			setField_id.append("\n" + table_name_lower + ".set" + first_upper + "(rs.getInt(\"" + upper_case + "\"));");

			field_for_save_id.append("\n" + table_name_lower + ".set" + first_upper + "(infonum);");

			get_id_for_sbmit_header_line.append(first_upper);

		}
		for (int i = 0; i < rn_userlist.size(); i++) {
			String field_name = rn_userlist.get(i).getMapping();
			String lower_case = field_name.toLowerCase();
			String first_upper = lower_case.substring(0, 1).toUpperCase() + lower_case.substring(1);
			String upper_case = field_name.toUpperCase();

			sqlField.append(upper_case);

			if (i < (rn_userlist.size() - 1)) {
				sqlField.append(",");
			}
			sqlField.append("\t");

			setField.append("\n" + table_name_lower + ".set" + first_upper + "(rs.getString(\"" + upper_case + "\"));");

			field_for_save_field.append("\n" + table_name_lower + ".set" + first_upper + "(" + lower_case + ");");

		}

		dao_impl_prefield
				.append("\n\n@Override" + "\npublic List<" + table_name_first_upper + "> prefield(int u_id)" + "\n{"

						+ "\nString sql =\"SELECT " + sqlField_id + var_for_pass_para_id_for_upper + date_array_value
						+ longtext_array_value + double_array_value + sqlField + "," + attribute_for_select_statment
						+ ",ACCOUNT_ID,CREATED_BY,CREATION_DATE,LAST_UPDATED_BY,LAST_UPDATE_DATE FROM\t"
						+ table_name_upper + "\tWHERE\t" + field_name_for_id_upper + "= \"+u_id+\"\";" + "\nList<"
						+ table_name_first_upper + "> userlist = jdbcTemplate.query(sql, new RowMapper<"
						+ table_name_first_upper + ">() {" + "\n@Override" + "\npublic\t" + table_name_first_upper
						+ "\tmapRow(ResultSet rs, int rowNum) throws SQLException {" + "\n" + table_name_first_upper
						+ "\t" + table_name_lower + " = new \t" + table_name_first_upper + "();" + setField_id
						+ id_array_set_in_dao + date_array_set_in_dao_for_date + longtext_array_set_in_dao_for_date
						+ double_array_set_in_dao_for_date + setField + attribute_set_for_grid_dao + "\n"
						+ table_name_lower + ".setAccount_id(rs.getInt(\"ACCOUNT_ID\"));" + "\n" + table_name_lower
						+ ".setCreated_by(rs.getInt(\"CREATED_BY\"));" + "\n" + table_name_lower
						+ ".setCreation_date(rs.getDate(\"CREATION_DATE\"));" + "\n" + table_name_lower
						+ ".setLast_updated_by(rs.getInt(\"LAST_UPDATED_BY\"));" + "\n" + table_name_lower
						+ ".setLast_update_date(rs.getDate(\"LAST_UPDATE_DATE\"));" + "\nreturn\t" + table_name_lower
						+ ";" + "\n}"

						+ "\n});"

						+ "\nreturn userlist;" + "\n}");

		dao_impl_save.append("\n@Override" + "\n@Transactional" + "\npublic int save(int rowcount," + service_save_id
				+ var_for_pass_para + service_save_field + date_array_para + longtext_array_para + double_array_para
				+ ")" + "\n{" + "\nint infonum = 0;" + "\nfor (int i = 0; i < rowcount; i++) " + "\n{" + "\n"
				+ table_name_first_upper + "\t" + table_name_lower + "= new\t" + table_name_first_upper + "();"
				+ "\nif (" + field_name_for_id_lower + " != null && " + field_name_for_id_lower + ".length() > 0) "
				+ "\n{" + "\ninfonum = Integer.parseInt(" + field_name_for_id_lower + ");" + "\n} else " + "\n{"
				+ "\ninfonum = " + table_name_lower + ".get" + field_name_first_upper + "();" + "\n}"

				+ field_for_save_id + id_array_set_in_dao2 + date_set + longtext_set + double_set + field_for_save_field

				+ "\nhibernateTemplate.saveOrUpdate(" + table_name_lower + ");" + "\n}"

				+ "\nreturn 1;" + "\n}");

		if (line_table_name != null && !line_table_name.isEmpty() && !line_table_name.equals("")) {
			String line_table_name_lower = line_table_name.toLowerCase();
			String line_table_name_first_upper = line_table_name_lower.substring(0, 1).toUpperCase()
					+ line_table_name_lower.substring(1);
			dao_impl_save_line.append("\n@Override" + "\n@Transactional" + "\npublic int addmenuGroupLine(int rowcount,"
					+ parameter_for_line_id + parameter_for_line_varchar + ")" + "\n{" + "\n" + table_name_first_upper
					+ "\t" + table_name_lower + "=new\t" + table_name_first_upper + "();" + "\nint infonum=0;"

					+ " \nfor(int i=0; i<rowcount; i++)" + "\n{" + "\nif(" + header_id_for_line + "!=0)" + "\n{"

					+ "\n" + line_table_name_first_upper + "\t" + line_table_name_lower + "=new\t"
					+ line_table_name_first_upper + "();"

					+ get_parameter_for_id_line + set_parameter_header_id_line + set_parameter_for_id_line
					+ set_parameter_for_varchar_line + "\nhibernateTemplate.saveOrUpdate(" + line_table_name_lower
					+ ");"

					+ "\n }" + "\n}" + "\nreturn 1;"

					+ " \n}");

// -----------------------------update line part-------------------------------------------//

			dao_impl_for_update_line.append("\npublic List<" + line_table_name_first_upper
					+ "> update_group_menu_line(int \t" + array_para_for_id_line1 + ")" + "\n{" + "\n"
					+ table_name_first_upper + "\t" + table_name_lower + " = new \t" + table_name_first_upper + "();"
					+ "\nString sql = \"select\t" + array_para_for_id_line + array_para_for_varchar_line + ","
					+ attribute_for_select_statment_lower + "from\t" + line_table_name_lower + "\twhere\t"
					+ array_para_for_id_line1 + "='\"+" + array_para_for_id_line1 + "+\"'\";" + "\nList<"
					+ line_table_name_first_upper + "> group_list = jdbcTemplate.query(sql, new RowMapper<"
					+ line_table_name_first_upper + ">() {" + "\n@Override" + "\npublic \t"
					+ line_table_name_first_upper + "\tmapRow(ResultSet rs, int rowNum) throws SQLException {"

					+ "\n" + line_table_name_first_upper + "\t" + line_table_name_lower + " = new \t"
					+ line_table_name_first_upper + "();"

					+ get_set_for_line_update_id + get_set_for_line_update_varchar + attribute_set_for_grid_dao_line
					+ "\nreturn \t" + line_table_name_lower + ";" + "\n}"

					+ "\n});"

					+ "\nreturn group_list;"

					+ "\n}");
		}

		dao_impl.append("\npackage com.realnet." + module_name + ".dao;" + "\nimport java.util.Date;"
				+ "\nimport java.sql.ResultSet;" + "\nimport java.sql.SQLException;" + "\nimport java.util.List;"
				+ "\nimport javax.sql.DataSource;" + "\nimport javax.transaction.Transactional;"
				+ "\nimport org.hibernate.Criteria;" + "\nimport org.hibernate.criterion.Restrictions;"
				+ "\nimport org.springframework.beans.factory.annotation.Autowired;"
				+ "\nimport org.springframework.dao.DataAccessException;"
				+ "\nimport org.springframework.jdbc.core.JdbcTemplate;"
				+ "\nimport org.springframework.jdbc.core.ResultSetExtractor;"
				+ "\nimport org.springframework.jdbc.core.RowMapper;"
				+ "\nimport org.springframework.jdbc.core.namedparam.MapSqlParameterSource;"
				+ "\nimport org.springframework.jdbc.core.namedparam.SqlParameterSource;"
				+ "\nimport org.springframework.orm.hibernate5.HibernateTemplate;"
				+ "\nimport org.springframework.stereotype.Repository;"
//                         +"\nimport com.mysql.cj.xdevapi.SessionFactory;"

				+ "\nimport com.realnet." + module_name + ".model." + table_name_first_upper + ";");

		if (line_table_name != null && !line_table_name.isEmpty()) {
			String line_table_name_lower = line_table_name.toLowerCase();
			String line_table_name_first_upper = line_table_name_lower.substring(0, 1).toUpperCase()
					+ line_table_name_lower.substring(1);
			dao_impl.append("import com.realnet." + module_name + ".model." + line_table_name_first_upper + ";");
		}

		dao_impl.append("\n@Repository(\"" + dao_name_first_upper + "\")" + "\npublic class\t"
				+ dao_impl_name_first_upper + "\timplements\t" + dao_name_first_upper + "\n{" + "\n@Autowired"
				+ "\nprivate JdbcTemplate jdbcTemplate;" + "\n@Autowired"
				+ "\nprivate HibernateTemplate hibernateTemplate;"

				+ "\n\n@Override" + "\npublic List<" + table_name_first_upper + "> userlist() " + "\n{"
				+ "\nString sql =\"SELECT\t" + sqlField_id + var_for_pass_para_id_for_upper + date_array_value
				+ longtext_array_value + double_array_value + sqlField + "," + attribute_for_select_statment + " FROM\t"
				+ table_name_upper + "\";" + "\nList<" + table_name_first_upper
				+ "> userlist = jdbcTemplate.query(sql, new RowMapper<" + table_name_first_upper + ">()" + "\n{"
				+ "	\n@Override" + "\npublic\t" + table_name_first_upper
				+ "\tmapRow(ResultSet rs, int rowNum) throws SQLException" + "\n{" + "\n" + table_name_first_upper
				+ "\t" + table_name_lower + " = new\t" + table_name_first_upper + "();" + "\n" + setField_id
				+ id_array_set_in_dao + date_array_set_in_dao_for_date + longtext_array_set_in_dao_for_date
				+ double_array_set_in_dao_for_date + setField + attribute_set_for_grid_dao

				+ "\nreturn\t" + table_name_lower + ";" + "\n}"

				+ "\n});"

				+ "\nreturn userlist;" + "}" + dao_impl_prefield + dao_impl_save

				+ "\n\n@Transactional" + "\npublic int saveheader(" + table_name_first_upper + "\t" + table_name_lower
				+ ")" + "\n{" + "\nhibernateTemplate.saveOrUpdate(" + table_name_lower + ");" + "\nSystem.out.println("
				+ table_name_lower + ".get" + get_id_for_sbmit_header_line + "());" + "\nreturn \t" + table_name_lower
				+ ".get" + get_id_for_sbmit_header_line + "();"

				+ "\n} " + dao_impl_save_line + dao_impl_for_update_line

				+ "}"

		);

// ----------------------------------jsp coding------------------------

		// -----SECTION CODE FOR ENTRY_FORM-----------
		importsection
				.append("\n<%@ page language=\"java\" contentType=\"text/html; charset=UTF-8\" pageEncoding=\"UTF-8\"%>"

						+ "\n<%@ page import=\"java.util.ArrayList\"%>" + "\n<%@ page import=\"java.util.Date\"%>"
						+ "\n<%@ taglib prefix=\"c\" uri=\"http://java.sun.com/jsp/jstl/core\"%>"
						+ "\n<%@ taglib uri=\"http://java.sun.com/jsp/jstl/functions\" prefix=\"fn\"%>"
						+ "\n<%@ taglib uri=\"http://java.sun.com/jsp/jstl/fmt\" prefix=\"fmt\"%>");

		headsection.append("\n<html lang=\"en\">\n<head>"
				+ "\n<meta http-equiv=\"X-UA-Compatible\"  content=\"IE=edge,chrome=1\">"
				+ "\n<meta charset=\"utf-8\" />" + "\n<title>Realnet Oil Engines Ltd</title>"
				+ "\n<meta name=\"description\" content=\"Common form elements and layouts\" />"
				+ "\n<meta name=\"viewport\""
				+ "\ncontent=\"width=device-width, initial-scale=1.0, maximum-scale=1.0\" />"
				+ "\n<!-- bootstrap & fontawesome -->" + "\n<link rel=\"stylesheet\""
				+ "\n href=\"<c:url value='/resources/assets/css/bootstrap.min.css'/>\" />"
				+ "\n<link rel=\"stylesheet\""
				+ "\nhref=\"<c:url value='/resources/assets/font-awesome/4.2.0/css/font-awesome.min.css'/>\" />"
				+ "\n<!-- page specific plugin styles -->" + "\n<link rel=\"stylesheet\""
				+ "\nhref=\"<c:url value='/resources/assets/css/jquery-ui.custom.min.css' />\" />"
				+ "\n<link rel=\"stylesheet\"" + "\nhref=\"<c:url value='/resources/assets/css/chosen.min.css' />\" />"

				+ "<link rel=\"stylesheet\" href=\"https://cdnjs.cloudflare.com/ajax/libs/easy-autocomplete/1.3.5/easy-autocomplete.min.css\">"
				+ "<link href=\"https://cdnjs.cloudflare.com/ajax/libs/easy-autocomplete/1.3.5/easy-autocomplete.min.css\" rel=\"stylesheet\" type=\"text/css\">"
				+ "<script src=\"//code.jquery.com/jquery-1.11.2.min.js\"></script>"
				+ "<script src=\"https://cdnjs.cloudflare.com/ajax/libs/easy-autocomplete/1.3.5/jquery.easy-autocomplete.min.js\" type=\"text/javascript\" ></script>"
				+ " <script src=\"//code.jquery.com/jquery-1.11.2.min.js\"></script>"
				+ " <script src=\"https://cdnjs.cloudflare.com/ajax/libs/easy-autocomplete/1.3.5/jquery.easy-autocomplete.min.js\" type=\"text/javascript\" ></script>"
				+ "<link href=\"https://cdnjs.cloudflare.com/ajax/libs/easy-autocomplete/1.3.5/easy-autocomplete.min.css\" rel=\"stylesheet\" type=\"text/css\">"
				+ "<link rel=\"stylesheet\" href=\"https://cdnjs.cloudflare.com/ajax/libs/easy-autocomplete/1.3.5/easy-autocomplete.min.css\">"
				+ "<script type=\"text/javascript\" src=\"https://cdnjs.cloudflare.com/ajax/libs/easy-autocomplete/1.3.5/jquery.easy-autocomplete.min.js\"></script>"

				+ "\n<link rel=\"stylesheet\""
				+ "\nhref=\"<c:url value='/resources/assets/css/datepicker.min.css'/>\" />"
				+ "\n<link rel=\"stylesheet\""
				+ "\nhref=\"<c:url value='/resources/assets/css/bootstrap-timepicker.min.css'/>\" />"
				+ "\n<link rel=\"stylesheet\""
				+ "\nhref=\"<c:url value='/resources/assets/css/daterangepicker.min.css' />\" />"
				+ "\n<link rel=\"stylesheet\""
				+ "\nhref=\"<c:url value='/resources/assets/css/bootstrap-datetimepicker.min.css'/>\" />"
				+ "\n<link rel=\"stylesheet\""
				+ "\nhref=\"<c:url value='/resources/assets/css/colorpicker.min.css' />\" />" + "\n<!-- text fonts -->"
				+ "\n<link rel=\"stylesheet\""
				+ "\n\nhref=\"<c:url value='/resources/assets/fonts/fonts.googleapis.com.css' />\" />"
				+ "\n<!-- ace styles -->" + "\n<link rel=\"stylesheet\""
				+ "\nhref=\"<c:url value='/resources/assets/css/ace.min.css\" class=\"ace-main-stylesheet\" id=\"main-ace-style' />\" />"
				+ "\n<!-- inline styles related to this page -->"

				+ "\n<!-- ace settings handler -->"
				+ "\n<script src=\"<c:url value='/resources/assets/js/ace-extra.min.js'/>\""
				+ "\ntype=\"text/javascript\"></script>"

				+ "\n<script>" + "\nsubmitForms = function()" + "\n{" + "\ndocument.forms[\"userdetails1\"].submit();"
				+ "\ndocument.forms[\"userdetails2\"].submit();" + "}" + "\n</script> "
				+ "\n<script src=\"resources/assets/js/ace-extra.min.js\"></script>"
				+ "\n<script src=\"https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js\"></script>"
				+ "\n<script src=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.4.0/js/bootstrap.min.js\"></script>"

				+ "\n<!-- MultiSelect DropDown Scripts  -->"
				+ "\n<script src=\"https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js\"></script>"
				+ "\n<script src=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.4.0/js/bootstrap.min.js\"></script>"
				+ " \n<script src=\"multiselect.min.js\"></script>"
				+ "   \n <script src=\"http://ajax.aspnetcdn.com/ajax/modernizr/modernizr-2.8.3.js\"></script>"
				+ "    \n<script src=\"http://code.jquery.com/jquery-1.11.3.min.js\"></script>"
				+ "    \n<script src=\"http://code.jquery.com/ui/1.11.1/jquery-ui.min.js\"></script>"
				+ "    \n<script src=\"http://cdn-na.infragistics.com/igniteui/2019.2/latest/js/infragistics.core.js\"></script>"
				+ "   \n <script src=\"http://cdn-na.infragistics.com/igniteui/2019.2/latest/js/infragistics.lob.js\"></script>"
				+ " " + " " + "<!-- Ignite UI Required Combined CSS Files -->"
				+ " \n  <link href=\"http://cdn-na.infragistics.com/igniteui/2019.2/latest/css/themes/infragistics/infragistics.theme.css\" rel=\"stylesheet\" />"
				+ "   \n<link href=\"http://cdn-na.infragistics.com/igniteui/2019.2/latest/css/structure/infragistics.css\" rel=\"stylesheet\" /> "

				+ " \n</head>");

		
		for (int i21 = 0; i21 < section_values.size(); i21++) {
			String field_nameS = section_values.get(i21).getFieldName();
			int sectionNum = section_values.get(i21).getSection_num();

			int section_num1 = sectionNum;

			int count = 1;

			System.out.println("count Value in section_values loop :: " + count);

			System.out.println("secTemp Value in section_values loop :: " + section_num1);
			
			// NUMBER OF FIELDS PRESENT IN THIS SECTION.
			//List<Rn_Fb_Line> sectionNumFields = rn_wireframe_dao.wf_section_num_fields(f_code, section_num1);
			List<Rn_Fb_Line> sectionNumFields = wireFrameService.getSectionFields(section_num1, id);

			// ENTRY-FORM section TABLE LOGIC
			form.append("\n<div class=\"table-header\" style=\"margin-bottom: 30px; margin-top: 30px;\"> " + field_nameS
					+ "</div>");

			// IF ITS SECTION1, THEN ADD ACTION LINK FOR SUBMIT THE FORM.
			if (sectionNum == 1) {
				form.append("\n<form action=\"" + action + "\" class=\"form-horizontal\" id=\"Regi\" method=\"Post\">"
						+ "\n<input type=\"hidden\" name=\"test\" id=\"test\" value=\"\" />");
			}

			form.append(" \n<table>" + "\n<tr>");

			if (sectionNum == 1) {
				form.append("<td  style=\"display:none;\">" 
						+ "\n<input type=\"text\" id=\"" + header_id_for_line
						+ "\" class=\"col-xs-10 col-sm-11\"  name=\"" + header_id_for_line + "\" value=\"\"/>"
						+ "</td>");
			}
			
			// INTEGER BUT NOT PK
			for (int i = 0; i < id_notprimary.size(); i++) {
				String field_name1 = id_notprimary.get(i).getFieldName();
				String field_name = id_notprimary.get(i).getMapping();
				String data_type = id_notprimary.get(i).getDataType();
				String lower_case = field_name.toLowerCase();

				// GET THE SECTION NUMBER TO MATCH WITH THE CURRENT SECTION NUMBER
				int fieldNum = id_notprimary.get(i).getSection_num(); 

				if (field_name1 != null || !field_name1.isEmpty()) {
					
					// if section_num == 1,2.. and datatype = int then logic will be...
					if (data_type.equals("int") && sectionNum == fieldNum) {
						form.append("\n<td>" 
								+ "\n<div class=\"form-group\" style=\"margin-left: 10%; margin-right: 10%;\">"
								+ "\n<label class=\"control-label col-xs-12 col-sm-3 no-padding-right\">"
								+ field_name1 + "\n<i class=\"menu-icon fa red\"> *</i>" + "\n</label>"
								+ "\n<div class=\"col-xs-12 col-sm-9\">" + "\n<div class=\"clearfix\">"
								+ "\n<input   value=\"${" + table_name_lower + "_update." + lower_case
								+ "}\" type=\"text\" name=\"" + lower_case + "\"id=\"" + lower_case
								+ "\"class=\"col-xs-12 col-sm-4\" required/>" + "\n</div>" + "\n</div>"
								+ "\n</div>" + "\n</td>");
					}

					form.append("\n</tr>");

					form.append("\n<tr>");
				}
			}
			
			// DATE-TIME then logic will be...
			for (int i = 0; i < datetime_list.size(); i++) {

				System.out.println("---under for loop latest fo date------");

				String field_name = datetime_list.get(i).getFieldName();
				String data_type = datetime_list.get(i).getDataType();
				int fieldNum = datetime_list.get(i).getSection_num();

//				String mandatory = datetime_list.get(i).getMandatory();
//				String readonly = datetime_list.get(i).getReadonly();
//				String hidden = datetime_list.get(i).getHidden();
				boolean mandatory = datetime_list.get(i).isMandatory();
				boolean readonly = datetime_list.get(i).isReadonly();
				boolean hidden = datetime_list.get(i).isHidden();

				String mapping = datetime_list.get(i).getMapping();

				String mapping_lower = mapping.toLowerCase();

				System.out.println("-----value of mantadoryr laest-----: " + mandatory);

				if (field_name != null || !field_name.isEmpty())

				{

					if (data_type.equals("datetime") && sectionNum == fieldNum)

					{

						System.out.println("---under if condition loop latest------");

						form.append(
								"\n<td>" + "\n<div class=\"form-group\" style=\"margin-left: 10%; margin-right: 10%;\">"
										+ "\n<label class=\"control-label col-xs-12 col-sm-3 no-padding-right\">"
										+ "\n " + field_name);

						System.out.println("-------------mandatory for testing=------------------" + mandatory);

						if (mandatory == true) {
							System.out.println("-------------in loop 1-------------------");
							form.append("\n <i class=\"menu-icon fa red\"> *</i>");
						}

						form.append("\n</label>" + "\n<div class=\"col-xs-12 col-sm-9\">" + "\n<div class=\"clearfix\">"
								+ "<div class=\"input-group input-append date\" id=\"datePicker" + i + "\">");

						form.append("\n<input");

						if (hidden == true) {
							form.append("  type=\"hidden\" ");
						} else {
							form.append("  type=\"text\" ");
						}

						form.append("name=\"" + mapping_lower + "\" id=\"" + mapping_lower
								+ "\" placeholder=\"picup Date\" class=\"form-control\"");

						if (mandatory == true) {
							System.out.println("-------------in loop 1 part 2 required-------------------");
							form.append("required");
						}

						if (readonly == true) {
							System.out.println("-------------in loop 1 part 2 required-------------------");
							form.append("  readonly");
						}
						form.append("/>\n"
								+ "\n<span class=\"input-group-addon\"> <i class=\"fa fa-calendar bigger-110\"></i>"
								+ "\n</span>" + "\n</div>" + "\n</div>" + "\n</div>" + "\n</div>" + "\n</td>");
					}

					date_script.append(" $('#datePicker" + i
							+ "').datepicker({format : 'dd-mm-yyyy'}).on('changeDate', function(e) {$('#eventForm').formValidation('revalidateField', 'date');});\n\n");

					form.append("\n</tr>");

				}
			}

			form.append("\n<tr>");

			// NUMBER OF FIELD PRESENT IN CURRENT SECTION LOOP START
			for (int i = 0; i < sectionNumFields.size(); i++)
			{
				String field_name4 = sectionNumFields.get(i).getFieldName();
				String mapping4 = sectionNumFields.get(i).getMapping();
				int fieldNum = sectionNumFields.get(i).getSection_num();

				System.out.println("field name ganesh-------" + field_name4 + " and i = " + i);
//				String mandatory = sectionNumFields.get(i).getMandatory();
//				String hidden = sectionNumFields.get(i).getHidden();
				boolean mandatory = sectionNumFields.get(i).isMandatory();
				boolean hidden = sectionNumFields.get(i).isHidden();
				String type_field = sectionNumFields.get(i).getType_field();

				int dividend = count;
				int divisor = 3;
				int remainder = dividend % divisor;

				System.out.println("remainder::" + remainder);
				System.out.println("count::" + count);
				count++;
				System.out.println("\n\n section number::" + section_num1 + "\n" + count);

				// textfield code
				if (type_field.equals("textfield") && sectionNum == fieldNum) {
					form.append("\n<td>" 
							+ "\n<div class=\"form-group\" style=\"margin-left: 10%; margin-right: 10%;\">"
							+ "\n<label class=\"control-label col-xs-12 col-sm-3 no-padding-right\">" 
							+ "\n "+ field_name4);

//					if (mandatory == true) {
					if (mandatory == true) {
						form.append("\n <i class=\"menu-icon fa red\"> *</i>");
					}
					
					// label close
					form.append("\n</label>"
					+ "\n<div class=\"col-xs-12 col-sm-9\">" + "\n<div class=\"clearfix\">");
					form.append("\n<input value=\"${" + table_name_lower + "_update." + mapping4 + "}\"");

//					if (hidden == true) {
					if (hidden == true) {
						form.append("  type=\"hidden\" ");
					} else {
						form.append("  type=\"text\" ");
					}
					
					form.append("name=" + mapping4 + " id=" + mapping4 + "   class=\"form-control\"");

//					if (mandatory == true) {
					if (mandatory == true) {
						form.append("required");
					}

					form.append("/>\n</div>" + "\n</div>" + "\n</div>" + "\n</td>");
				}
				
				// url code
				if (type_field.equals("url") && sectionNum == fieldNum) {
					form.append("\n<td>" + "\n<div class=\"form-group\" style=\"margin-left: 10%; margin-right: 10%;\">"
							+ "\n<label class=\"control-label col-xs-12 col-sm-3 no-padding-right\">" + "\n "
							+ field_name4);

//					if (mandatory == true) {
					if (mandatory == true) {
						form.append("\n <i class=\"menu-icon fa red\"> *</i>");
					}

					form.append("\n</label>" + "\n<div class=\"col-xs-12 col-sm-9\">" + "\n<div class=\"clearfix\">");
					form.append("\n<input value=\"${" + table_name_lower + "_update." + mapping4
							+ "}\" placeholder=\"URL\" pattern=\"https://.*\" size=\"30\" ");

//					if (hidden == true) {
					if (hidden == true) {
						form.append("  type=\"hidden\" ");
					} else {
						form.append("  type=\"text\" ");
					}
					
					form.append("name=" + mapping4 + " id=" + mapping4 + "   class=\"form-control\"");

//					if (mandatory == true) {
					if (mandatory == true) {
						form.append("required");
					}

					form.append("/>\n</div>" + "\n</div>" + "\n</div>" + "\n</td>");
				}
				// email field code
				if (type_field.equals("email") && sectionNum == fieldNum) {
					form.append("\n<td>" + "\n<div class=\"form-group\" style=\"margin-left: 10%; margin-right: 10%;\">"
							+ "\n<label class=\"control-label col-xs-12 col-sm-3 no-padding-right\">" + "\n "
							+ field_name4);

//					if (mandatory == true) {
					if (mandatory == true) {
						form.append("\n <i class=\"menu-icon fa red\"> *</i>");
					}

					form.append("\n</label>" + "\n<div class=\"col-xs-12 col-sm-9\">" + "\n<div class=\"clearfix\">");
					form.append("\n<input value=\"${" + table_name_lower + "_update." + mapping4
							+ "}\" placeholder=\"E_Mail\"  onblur=\"validateEmail(this);\"");

//					if (hidden == true) {
					if (hidden == true) {
						form.append("  type=\"hidden\" ");
					} else {
						form.append("  type=\"email\" ");
					}
					
					form.append("name=" + mapping4 + " id=" + mapping4 + "   class=\"form-control\"");

//					if (mandatory == true) {
					if (mandatory == true) {
						form.append("required");
					}

					form.append("/>\n</div>" + "\n</div>" + "\n</div>" + "\n</td>");

					togglescript.append("\nfunction validateEmail(emailField) {"
							+ "\n			var reg = /^([A-Za-z0-9_\\-\\.])+\\@([A-Za-z0-9_\\-\\.])+\\.([A-Za-z]{2,4})$/;"
							+ "\n			if (reg.test(emailField.value) == false) {"
							+ "\n				alert('Invalid Email Address');" + "\n				return false;"
							+ "\n			}" + "\n			return true;" + "\n		}");
				}

				if (type_field.equals("masked") && sectionNum == fieldNum) {
					form.append("\n<td>" + "\n<div class=\"form-group\" style=\"margin-left: 10%; margin-right: 10%;\">"
							+ "\n<label class=\"control-label col-xs-12 col-sm-3 no-padding-right\">" + "\n "
							+ field_name4);

//					if (mandatory == true) {
					if (mandatory == true) {
						form.append("\n <i class=\"menu-icon fa red\"> *</i>");
					}

					form.append("\n</label>" + "\n<div class=\"col-xs-12 col-sm-9\">" + "\n<div class=\"clearfix\">");
					form.append("\n<input value=\"${" + table_name_lower + "_update." + mapping4
							+ "}\" placeholder=\"Masked\" maxlength=\"8\"");

//					if (hidden == true) {
					if (hidden == true) {
						form.append("  type=\"hidden\" ");
					} else {
						form.append("  type=\"password\" ");
					}
					
					form.append("name=" + mapping4 + " id=" + mapping4 + "   class=\"form-control\"");

//					if (mandatory == true) {
					if (mandatory == true) {
						form.append("required");
					}

					form.append("/>\n</div>" + "\n</div>" + "\n</div>" + "\n</td>");
				}
				// currency_field code
				if (type_field.equals("currency_field") && sectionNum == fieldNum) {
					form.append("\n<td>" + "\n<div class=\"form-group\" style=\"margin-left: 10%; margin-right: 10%;\">"
							+ "\n<label class=\"control-label col-xs-12 col-sm-3 no-padding-right\">" + "\n "
							+ field_name4);

					if (mandatory == true) {
						form.append("\n <i class=\"menu-icon fa red\"> *</i>");
					}

					form.append("\n</label>" + "\n<div class=\"col-xs-12 col-sm-9\">" + "\n<div class=\"clearfix\">"
							+ "\n<div class=\"input-group\" style=\"position: relative;display: table;border-collapse: separate\"> ");

					form.append("\n<input value=\"${" + table_name_lower + "_update." + mapping4
							+ "}\"  placeholder=\"$1,000,000.00\" onblur=\"handleChange()\"");

					if (hidden == true) {
						form.append("  type=\"hidden\" ");
					} else {
						form.append("  type=\"text\" ");
					}
					form.append("name=" + mapping4 + " id=" + mapping4 + "   class=\"form-control\"");

					if (mandatory == true) {
						form.append("required");
					}

					form.append("/>" + "\n<span class=\"input-group-addon\">$</span>" + "\n</div>" + "\n</div>"
							+ "\n</div>" + "\n</div>" + "\n</td>");

					togglescript.append("\nfunction handleChange() {" + "\nvar x = document.getElementById(\""
							+ mapping4 + "\").value;" + "  \n  if (x.indexOf(\"$\") != 0)" + "    \n{"
							+ "      \n  x = \"$\" + x.toString().replace(/\\B(?=(\\d{3})+(?!\\d))/g, \",\");"
							+ "    \n}" + "    " + "    \ndocument.getElementById(\"" + mapping4 + "\").value = x;"
							+ "\n};");

				}
				// contact number code
				if (type_field.equals("contact_field") && sectionNum == fieldNum) {
					form.append("\n<td>" + "\n<div class=\"form-group\" style=\"margin-left: 10%; margin-right: 10%;\">"
							+ "\n<label class=\"control-label col-xs-12 col-sm-3 no-padding-right\">" + "\n "
							+ field_name4);

					if (mandatory == true) {
						form.append("\n <i class=\"menu-icon fa red\"> *</i>");
					}

					form.append("\n</label>" + "\n<div class=\"col-xs-12 col-sm-9\">" + "\n<div class=\"clearfix\">"

							+ "\n<div style=\"position:relative\">" + "\n<select style=\"   height:\";\" >"
							+ "\n<option>+91</option>" + "\n<option>+44</option>" + "\n<option>+64</option>"
							+ "\n<option>+21</option>" + "\n<option>+244</option>" + "\n<option>+376</option>"
							+ "\n<option>+354</option>" + "\n<option>+291</option>" + "\n<option>+54</option>"
							+ "\n<option>+264</option>" + "\n<option>+121</option>" + "\n</select>");
					form.append("\n<input value=\"${" + table_name_lower + "_update." + mapping4
							+ "}\"   pattern=\"[0-9]{10}\"");

					if (hidden == true) {
						form.append("  type=\"hidden\" ");
					} else {
						form.append("  type=\"tel\" ");
					}
					/*
					 * if(calculated_field.equals("Y")) { form_readonly.append(
					 * "name=\"total\" id=\"total\" class=\"stunden form-control\" "); } else {
					 * form_readonly.append( "name="+mapping_lower+" id=\""
					 * +mapping_lower+"\" class=\"col-xs-12 col-sm-4\" readonly"); }
					 */
					form.append("name=" + mapping4 + " id=" + mapping4 + "   class=\"form-control\"");

					if (mandatory == true) {
						form.append("required");
					}

					form.append("/>\n</div>" + "\n</div>" + "\n</div>" + "\n</div>" + "\n</td>");
				}
				// file upload field code
				if (type_field.equals("upload_field") && sectionNum == fieldNum) {
					form.append("\n<td>" + "\n<div class=\"form-group\" style=\"margin-left: 10%; margin-right: 10%;\">"
							+ "\n<label class=\"control-label col-xs-12 col-sm-3 no-padding-right\">" + "\n "
							+ field_name4);

					System.out.println("-------------mandatory for testing=------------------" + mandatory);

					if (mandatory == true) {
						System.out.println("-------------in loop 1-------------------");
						form.append("\n <i class=\"menu-icon fa red\"> *</i>");
					}

					form.append("\n</label>" + "\n<div class=\"col-xs-12 col-sm-9\">" + "\n<div class=\"clearfix\">");

					form.append("\n<input value=\"${" + table_name_lower + "_update." + mapping4
							+ "}\"   placeholder=\"Upload File\" style=\"width:100%; padding-left:10px;\"");
					if (hidden == true) {
						form.append("  type=\"hidden\" ");
					} else {
						form.append("  value=\"${" + table_name_lower + "_update." + mapping4 + "}\" type=\"file\" ");
					}

					form.append("name=" + mapping4 + " id=" + mapping4 + "  class=\"form-control\"");

					if (mandatory == true) {
						System.out.println("-------------in loop 1 part 2 required-------------------");
						form.append("required");
					}

					form.append("/>\n" + " <c:out value=\"${" + table_name_lower + "_update." + mapping4 + "}\"/>"

							+ "\n</div>" + "\n</div>" + "\n</div>" + "\n</td>");

					dropdown_script_latest1.append("\n$(function () {          " + "\n$(\"#" + mapping4
							+ "\").ace_file_input({" + "	\nno_file : 'No File ...'," + "	\nbtn_choose : 'Choose',"
							+ "\n	btn_change : 'Change'," + "	\ndroppable : false," + "	\nonchange : null,"
							+ "	\nthumbnail : false" + "\n//| true | large" + "\n//whitelist:'gif|png|jpg|jpeg'"
							+ "\n//blacklist:'exe|php'" + "\n//onchange:''" + "\n//" + "\n});" + "\n});"

					);

				}
				
				// textarea field code
				if (type_field.equals("textarea") && sectionNum == fieldNum) {
					form.append("\n<td>" + "\n<div class=\"form-group\" style=\"margin-left: 10%; margin-right: 10%;\">"
							+ "\n<label class=\"control-label col-xs-12 col-sm-3 no-padding-right\">" + "\n "
							+ field_name4);

					if (mandatory == true) {
						form.append("\n <i class=\"menu-icon fa red\"> *</i>");
					}

					form.append("\n</label>" + "\n<div class=\"col-xs-12 col-sm-9\">" + "\n<div class=\"clearfix\">");
					form.append("\n<textarea rows=\"" + 0 + "\" cols=\"" + 34 + "\" value=\"${" + table_name_lower
							+ "_update." + mapping4 + "}\"");

					if (hidden == true) {
						form.append("  type=\"hidden\" ");
					} else {
						form.append("  type=\"text\" ");
					}
					
					form.append("name=" + mapping4 + " id=" + mapping4 + " class=\"form-control\"");

					if (mandatory == true) {
						form.append("required");
					}

					form.append("/>${" + table_name_lower + "_update." + mapping4 + "}</textarea>\n</div>" + "\n</div>"
							+ "\n</div>" + "\n</td>");
				}
				
				// datetime field code
				if (type_field.equals("datetime") && sectionNum == fieldNum) {
					form.append("\n<td>" + "\n<div class=\"form-group\" style=\"margin-left: 10%; margin-right: 10%;\">"
							+ "\n<label class=\"control-label col-xs-12 col-sm-3 no-padding-right\">" + "\n "
							+ field_name4);

					if (mandatory == true) {
						form.append("\n <i class=\"menu-icon fa red\"> *</i>");
					}

					form.append("\n</label>" + "\n<div class=\"col-xs-12 col-sm-9\">" + "\n<div class=\"clearfix\">"
							+ "<div class=\"input-group input-append date\" id=\"datePicker\">");

					form.append("\n<input  value=\"${" + table_name_lower + "_update." + mapping4 + "}\"");

					if (hidden == true) {
						form.append("  type=\"hidden\" ");
					} else {
						form.append("  type=\"text\" ");
					}

					form.append("name=" + mapping4 + " id=" + mapping4
							+ " placeholder=\"picup Date\" class=\"form-control\"");

					if (mandatory == true) {
						form.append("required");
					}

					form.append(
							"/>\n" + "\n<span class=\"input-group-addon\"> <i class=\"fa fa-calendar bigger-110\"></i>"
									+ "\n</span>" + "\n</div>" + "\n</div>" + "\n</div>" + "\n</td>");

					date_script.append(
							" $('#datePicker').datepicker({format : 'dd-mm-yyyy'}).on('changeDate', function(e) {$('#eventForm').formValidation('revalidateField', 'date');});\n\n");

				}
				// autocomplete type code
				if (type_field.equals("autocomplete") && sectionNum == fieldNum) {
					form.append("\n<td>" + "\n<div class=\"form-group\" style=\"margin-left: 10%; margin-right: 10%;\">"
							+ "\n<label class=\"control-label col-xs-12 col-sm-3 no-padding-right\">" + "\n "
							+ field_name4);

					if (mandatory == true) {
						form.append("\n <i class=\"menu-icon fa red\"> *</i>");
					}

					form.append("\n</label>" + "\n<div class=\"col-xs-12 col-sm-9\">" + "\n<div class=\"clearfix\">");

					form.append("\n<input  value=\"${" + table_name_lower + "_update." + mapping4
							+ "}\" placeholder=\"AutoComplete\"");

					if (hidden == true) {
						form.append("  type=\"hidden\" ");
					} else {
						form.append("  type=\"text\" ");
					}

					form.append("name=" + mapping4 + " id=" + mapping4 + "  class=\"form-control\"");
					if (mandatory == true) {
						System.out.println("-------------in loop 1 part 2 required-------------------");
						form.append("required");
					}

					form.append("/>\n</div>" + "\n</div>" + "\n</div>" + "\n</td>");

					autocomplete_script_latest.append("<script>" + "\nvar options = {"
							+ "\nurl: '${pageContext.request.contextPath}/" + mapping4 + "_list',"
							+ "\ngetValue: \"+mapping_lower+\"," + "\nlist: {" + "match: {" + "\nenabled: true"
							+ "\n}}," + "\ntheme: \"square\"" + "\n};" + "\n$(\"#" + mapping4
							+ "\").easyAutocomplete(options);"

							+ "\nvar options={" + "\nurl: '${pageContext.request.contextPath}/" + mapping4 + "_list',"
							+ "\ngetValue: \"+mapping_lower+\"," + "\nlist: {" + "\nmatch: {" + "\nenabled: true"
							+ "\n}}," + "\n};" + "\n$(\"#" + mapping4 + "\").easyAutocomplete(options);" + "\n</script>"

					);

				}
				// dropdown type code
				if (type_field.equals("dropdown") && sectionNum == fieldNum) {
					form.append("\n<td>" + "\n<div class=\"form-group\" style=\"margin-left: 10%; margin-right: 10%;\">"
							+ "\n<label class=\"control-label col-xs-12 col-sm-3 no-padding-right\">" + "\n "
							+ field_name4);

					if (mandatory == true) {
						form.append("\n <i class=\"menu-icon fa red\"> *</i>");
					}

					form.append("\n</label>" + "\n<div class=\"col-xs-12 col-sm-9\">");

					form.append(
							"\n<input name=\"" + mapping4 + "\" id=\"" + mapping4 + "\"  value=\"${" + table_name_lower
									+ "_update." + mapping4 + "}\"  class=\"col-xs-3 col-sm-3 form-control state\"");

					if (mandatory == true) {
						form.append("required");
					}

					form.append("/>\n</div>"

							+ "\n</div>" + "\n</td>");

					dropdown_script_latest.append("\n var colors = [" + " \n           { Name: \"option1\" },"
							+ "   \n         { Name: \"option2\" }," + "     \n       { Name: \"option3\" },"
							+ "       \n     { Name: \"option4\" }," + "         \n   { Name: \"option5\" },"
							+ "           \n { Name: \"option6\" }," + "            \n{ Name: \"option7\" },"
							+ "            \n{ Name: \"option8\" }" + "        \n];" + "        \n$(function () {"
							+ "          \n  $(\"#singleSelectCombo\").igCombo({" + "            \n    width: 325, "
							+ "              \n  dataSource: colors," + "                \ntextKey: \"Name\","
							+ "                \nvalueKey: \"Name\"," + "                \ndropDownOnFocus: true,"
							+ "                \ndropDownOrientation: \"bottom\"" + "            \n});"
							+ "            \n$(\'#" + mapping4 + "\').igCombo({" + "              \n  width: \"100%\","
							+ "                \ndataSource: colors," + "                \ntextKey: \"Name\","
							+ "                \nvalueKey: \"Name\"," + "                \nmultiSelection: {"
							+ "                  \n  enabled: true" + "                \n},"
							+ "                \ndropDownOrientation: \"bottom\"" + "            \n});"
							+ "            \n$(\"#checkboxSelectCombo\").igCombo({" + "              \n  width: 325,"
							+ "                \ndataSource: colors," + "               \n textKey: \"Name\","
							+ "                \nvalueKey: \"Name\"," + "                \nmultiSelection: {"
							+ "                  \n  enabled: true," + "                    \nshowCheckboxes: true"
							+ "                \n}," + "                \ndropDownOrientation: \"bottom\""
							+ "            \n});" + "        \n});"

					);
				}
				// toggle_button type code
				if (type_field.equals("togglebutton") && sectionNum == fieldNum) {
					form.append("\n<td>" + "\n<div class=\"form-group\" style=\"margin-left: 10%; margin-right: 10%;\">"
							+ "\n<label class=\"control-label col-xs-12 col-sm-3 no-padding-right\">" + "\n "
							+ field_name4);

					if (mandatory == true) {
						System.out.println("-------------in loop 1-------------------");
						form.append("\n <i class=\"menu-icon fa red\"> *</i>");
					}

					form.append("\n</label>" + "\n<div class=\"col-xs-12 col-sm-9\">" + "\n<div class=\"clearfix\">");

					form.append("\n<input");

					if (hidden == true) {
						form.append("  type=\"hidden\" ");
					} else {
						form.append("  type=\"checkbox\" ");
					}

					form.append("name=" + mapping4 + " id=" + mapping4
							+ "  class=\"ace ace-switch ace-switch-6\" onblur=\"CheckUserStatus" + mapping4 + "()\"");

					if (mandatory == true) {
						System.out.println("-------------in loop 1 part 2 required-------------------");
						form.append("required");
					}

					form.append("/>\n" + "\n<span class=\"lbl\"></span>"

							+ "\n</div>" + "\n</div>" + "\n</div>" + "\n</td>");

					togglescript.append("function CheckUserStatus" + mapping4 + "() {" + "\r\n" + "\r\n"
							+ "	\n		if (document.getElementById(\"" + mapping4 + "\").checked == true) {\r\n"
							+ "	\n			document.getElementById(\"" + mapping4 + "\").value = 'Y';\r\n" + "\r\n"
							+ "			} else if (document.getElementById(\"" + mapping4
							+ "\").checked == false) {\r\n" + "				document.getElementById(\"" + mapping4
							+ "\").value = 'N';\r\n" + "			}\r\n" + "		}");
					togglescript.append("		\n" + "		\nvar d=$(\'#" + mapping4 + "\').val();" + "    	 \n"
							+ "		\nif(d!=null){" + "			\n	" + "        	\nif(d=='Y'){" + "        		\n"
							+ "            \ndocument.getElementById(\"" + mapping4 + "\").checked = true;"
							+ "        		\n}" + "        	\nelse if(d=='N'){"
							+ "        	  \n  document.getElementById(\"" + mapping4 + "\").checked = false;"
							+ "        	\n}" + "        	\n" + "        	\ndocument.getElementById(\"" + mapping4
							+ "\").value=d;" + "			\n}" + "		\n" + "		\nvar count=50"
							+ "		\nfor(var i=1;i<count;i++){" + "			\nvar d=$(\"" + mapping4
							+ "\"+i).val();" + "	    	  \n//alert(d)" + "			\nif(d!=null){"
							+ "	        	\nif(d=='Y'){" + "	        		\n//$(\"#active\"+i).val(true);"
							+ "	            \ndocument.getElementById(\"" + mapping4 + "\"+i).checked = true;"
							+ "	        	\n}else if(d=='N'){" + "	        	  \n  document.getElementById(\""
							+ mapping4 + "\"+i).checked = false;" + "	        	\n}"
							+ "	        	\ndocument.getElementById(\"" + mapping4 + "\"+i).value=d;"
							+ "				\n}" + "			\n}		" + "		\n");

				}
				// checkbox type code
				if (type_field.equals("checkbox") && sectionNum == fieldNum) {
					form.append("\n<td>" + "\n<div class=\"form-group\" style=\"margin-left: 10%; margin-right: 10%;\">"
							+ "\n<label class=\"control-label col-xs-12 col-sm-3 no-padding-right\">" + "\n "
							+ field_name4);

					if (mandatory == true) {
						form.append("\n <i class=\"menu-icon fa red\"> *</i>");
					}

					form.append("\n</label>" + "\n<div class=\"col-xs-12 col-sm-9\">" + "\n<div class=\"clearfix\">");

					form.append("\n<input  style=\"width:10%; margin-left:20px\" class=\"form-control\"");

					if (hidden == true) {
						form.append("  type=\"hidden\" ");
					} else {
						form.append("  type=\"checkbox\" ");
					}

					form.append(
							"name=" + mapping4 + " id=" + mapping4 + "  onblur=\"CheckUserStatus" + mapping4 + "()\"");

					if (mandatory == true) {
						form.append("required");
					}
					form.append("/>\n" + "\n</div>" + "\n</div>" + "\n</div>" + "\n</td>");
					togglescript.append("function CheckUserStatus" + mapping4 + "() {" + "\r\n" + "\r\n"
							+ "	\n		if (document.getElementById(\"" + mapping4 + "\").checked == true) {\r\n"
							+ "	\n			document.getElementById(\"" + mapping4 + "\").value = 'Y';\r\n" + "\r\n"
							+ "			} else if (document.getElementById(\"" + mapping4
							+ "\").checked == false) {\r\n" + "				document.getElementById(\"" + mapping4
							+ "\").value = 'N';\r\n" + "			}\r\n" + "		}");
					togglescript.append("		\n" + "		\nvar d=$(\'#" + mapping4 + "\').val();" + "    	 \n"
							+ "		\nif(d!=null){" + "			\n	" + "        	\nif(d=='Y'){" + "        		\n"
							+ "            \ndocument.getElementById(\"" + mapping4 + "\").checked = true;"
							+ "        		\n}" + "        	\nelse if(d=='N'){"
							+ "        	  \n  document.getElementById(\"" + mapping4 + "\").checked = false;"
							+ "        	\n}" + "        	\n" + "        	\ndocument.getElementById(\"" + mapping4
							+ "\").value=d;" + "			\n}" + "		\n" + "		\nvar count=50"
							+ "		\nfor(var i=1;i<count;i++){" + "			\nvar d=$(\"" + mapping4
							+ "\"+i).val();" + "	    	  \n//alert(d)" + "			\nif(d!=null){"
							+ "	        	\nif(d=='Y'){" + "	        		\n//$(\"#active\"+i).val(true);"
							+ "	            \ndocument.getElementById(\"" + mapping4 + "\"+i).checked = true;"
							+ "	        	\n}else if(d=='N'){" + "	        	  \n  document.getElementById(\""
							+ mapping4 + "\"+i).checked = false;" + "	        	\n}"
							+ "	        	\ndocument.getElementById(\"" + mapping4 + "\"+i).value=d;"
							+ "				\n}" + "			\n}		" + "		\n");
				}
				if (remainder == 0) {
					form.append("\n</tr>");
					form.append("\n<tr>");
				}

			}
			form.append("\n</table>");

		}

		// section
		form.append("\n<%@include file=\"/WEB-INF/tiles/acemaster/" + module_name + "/" + table_name_lower
				+ "_extension.jsp\"%>");

		if (line_table_name != null && !line_table_name.isEmpty()) {

			String line_table_name_lower = line_table_name.toLowerCase();
			String line_table_name_first_upper = line_table_name_lower.substring(0, 1).toUpperCase()
					+ line_table_name_lower.substring(1);
			form.append(" <%@include file=\"/WEB-INF/tiles/acemaster/" + module_name + "/" + line_table_name_first_upper
					+ "_view.jsp\"%>");
		}

		// -----------------------------coding for line
		// part-----------------------------
		if (line_table_name != null && !line_table_name.isEmpty()) {
			for (int i = 0; i < line_section.size(); i++) {
				String field_name = line_section.get(i).getFieldName();
				for_line_part
						.append("\n<div class=\"table-header\" style=\"margin-top: 30px;\">" + field_name + "</div>");

			}
			for_line_part.append(
					"\n<div>" + "\n<% int n=0; %>" + "\n<input type=\"hidden\" id=\"rowcount1\" name=\"rowcount1\">"
							+ "\n<input type=\"hidden\" id=\"delrow1\" name=\"delrow1\" value=\"\">"
							+ "\n<table id=\"dynamic-table1\" class=\"table table-striped table-bordered table-hover\">"
							+ "\n<thead>" + "\n<tr>");

			// for loop for id is primary

			for (int i = 0; i < line_id_primary.size(); i++) {
				for_line_part.append("\n<th>" + "\n<center>Select</center>" + "\n</th>");
			}

			// for loop for other field (like varchar field) this is only heading

			for (int i = 0; i < line_varchar.size(); i++) {
				String field_name4 = line_varchar.get(i).getFieldName();
				for_line_part.append("\n<th>" + "\n<center>" + field_name4 + "</center>" + "\n</th>");
			}

			if (line_table_name != null && !line_table_name.isEmpty()) {
				String line_table_name_lower = line_table_name.toLowerCase();
				for_line_part.append("\n\n<%@include file=\"/WEB-INF/tiles/acemaster/" + module_name + "/"
						+ line_table_name_lower + "_extension.jsp\"%>\n\n</tr>");

			}
			for_line_part.append("\n</thead>" + "\n<tbody>" + "\n</tbody>" + "\n</table>"

					+ "\n<input type=\"button\" class=\"btn btn-md btn-success\""
					+ "\nstyle=\"background-color: #87b87f; border-color: #87b87f; margin-top: 1%; margin-bottom: 1%;\""
					+ "\nvalue=\"Add Row\" onclick=\"AddRow()\">"

					+ "\n<input type=\"button\" class=\"btn btn-md btn-success\""
					+ "\nstyle=\"background-color: #87b87f; border-color: #87b87f; margin-top: 1%; margin-bottom: 1%; font-size: auto;\""
					+ "\nvalue=\"Delete Row\" onclick=\"del()\">" + "\n</div>");

		}

//---------------------line part update------------------------------	

		if (line_table_name != null && !line_table_name.isEmpty()) {

			String line_table_name_lower = line_table_name.toLowerCase();
			for (int i = 0; i < line_id_primary.size(); i++) {
				String mapping4 = line_id_primary.get(i).getMapping();
				String lower_case = mapping4.toLowerCase();

				line_value_update1.append("<td style=\"display:none\">'+($('#dynamic-table1 tr').length)+'"
						+ "<input type=\"hidden\" value=\"${" + line_table_name_lower + "_update." + lower_case
						+ "}\" id=\"" + lower_case + "\" name=\"" + lower_case + "\"  <%=j%>/>"

						+ "<td>" + "<center>"
						+ "<input type=\"checkbox\" class=\"Deleterow\" id=\"chk\" name=\"chk\" style=\"text-align:center;\"/>"
						+ "</center>" + "</td>");
			}

			// for loop for other field values
//				            for(int i=0;i<line_varchar.size();i++)
//			            	  {
//			                	String field_name4=line_varchar.get(i).getMapping();
//			          			String mapping4=line_varchar.get(i).getMapping();
//			          			String data_type4=line_varchar.get(i).getDataType();
//			          			String lower_case=mapping4.toLowerCase();
//							
//			          			System.out.println("field name-------"+field_name4); 
//			          		    System.out.println("mapping-------"+mapping4);
//			          		    System.out.println("data type-------"+data_type4);
//			          		    
//			          		    String mandatory=line_varchar.get(i).getMandatory();
//						   	    String readonly=line_varchar.get(i).getReadonly();
//						   	    String hidden=line_varchar.get(i).getHidden();
//						   	    
//						   	    String type_field=line_varchar.get(i).getType_field();
//						   	    
//						   	     if(type_field.equals("l_textfield"))
//						   	     {  
//						   	       
//						   	    	line_value_update.append("<td>"
//			          				                    +"<input");
//			          			     if(hidden.equals("Y"))
//			          			     {
//			          			    	line_value_update.append("\ttype=\"hidden\"");
//			          			     }else{
//			          			    	line_value_update.append("\ttype=\"text\"");
//			          			     }
//			          			   
//			          			   line_value_update.append("\t value=\"${"+line_table_name_lower+"_update."+lower_case+"}\" id=\""+lower_case+"\" name=\""+lower_case+"\" style=\"width:100%;\" <%=j%>");
//			          			   
//			          			   if(mandatory.equals("Y"))
//			          			     {
//			          				    line_value_update.append(" \trequired ");
//			          			      }
//			          			   
//			          			   if(readonly.equals("Y"))
//		          			       {
//			          				 line_value_update.append(" \treadonly ");
//		          			        }
//			          			   
//			          			 line_value_update.append(" />");
//			          			line_value_update.append("</td>");
//			                     }
//						   	     
//						   	   if(type_field.equals("l_date"))
//						   	     {  
//						   	       
//						   		line_value_update.append("<td>"
//			          			                          +"<div class=\"clearfix\">"
//			          			                            +"<div class=\"input-group input-append date\" id=\"datePicker\" style=\"width:100%;\">"
//			          				                           +"<input  type=\"text\" value=\"${"+line_table_name_lower+"_update."+lower_case+"}\" name=\""+lower_case+"\" id=\""+lower_case+"\" placeholder=\"picup Date\" class=\"form-control\"   <%=j%>/>"
//			          				                              +"<span class=\"input-group-addon\">"
//			          				                              +"<i class=\"fa fa-calendar bigger-110\"></i></span>"
//			          				                           +"</div>"
//			          				                          +"</div>"
//			          		                               +"</td>");
//			                     }
//						   	   
//						   	 if(type_field.equals("l_dropdown"))
//					   	     {  
//					   	       
//						   		line_value_update.append("<td>"
//		          			                        + "<select  id=\""+lower_case+"\" name=\""+lower_case+"\" class=\"ChangeDropdown\" style=\"width:100%;\" required <%=j%>>"
//		          		                                +"<option selected disabled value=\"\"></option>"
//		          		                              +"</select>"
//		          		                            +"</td>");
//		                     }
//						   	 
//						   	 
//						   	 if(type_field.equals("l_autocomplete"))
//					   	     {  
//					   	       
//						   		line_value_update.append("<td>"
//			      				                    +"<input");
//			      			     if(hidden.equals("Y"))
//			      			     {
//			      			    	line_value_update.append("\ttype=\"hidden\"");
//			      			     }else{
//			      			    	line_value_update.append("\ttype=\"text\"");
//			      			     }
//			      			   
//			      			   line_value_update.append("\t value=\"${"+line_table_name_lower+"_update."+lower_case+"}\"  id=\""+lower_case+"\" name=\""+lower_case+"\" style=\"width:100%;\" <%=j%>");
//			      			   
//			      			   if(mandatory.equals("Y"))
//			      			     {
//			      				 line_value_update.append(" \trequired ");
//			      			      }
//			      			   
//			      			   if(readonly.equals("Y"))
//			  			       {
//			      				 line_value_update.append(" \treadonly ");
//			  			        }
//			      			   
//			      			 line_value_update.append(" />");
//			      			line_value_update.append("</td>");
//		                     }
//						   	 
//						   	 
//						   	 if(type_field.equals("l_togglebutton"))
//					   	     {  
//					   	       
//						   		line_value_update.append("<td>"
//		          			                         +"<center>"
//		          		                                 +"<div class=\"clearfix\">"
//		          			                              +" <input name=\""+lower_case+"\" value=\"${"+line_table_name_lower+"_update."+lower_case+"}\"  id=\""+lower_case+"\" class=\"ace ace-switch ace-switch-6\" type=\"checkbox\" onclick=\"CheckUserStatusGrid(id)\" <%=j%>/>"
//		          				                             +"<span class=\"lbl\"></span>"
//		          			                            +"</div>"
//		          		                                 +"</center>"
//		          		                               +"</td>");
//		                     }
//						   	 
//						   	 if(type_field.equals("l_checkbox"))
//					   	     {  
//					   	       
//						   		line_value_update.append("<td>"
//		          			                        +"<center>"
//		          			   	                      +"<input type=\"checkbox\"  value=\"${"+line_table_name_lower+"_update."+lower_case+"}\" id=\""+lower_case+"\" name=\""+lower_case+"\" style=\"text-align:center;\"/<%=j%>>"
//		          			   	                    +"</center>"
//		          			                      +"</td>");
//		                     }
//						   	 
//							
//			            }

//							
			// for loop for field id and not pri

			for (int i = 0; i < line_id_primary.size(); i++) {
				String field_name4 = line_id_primary.get(i).getMapping();
				String lower_case = field_name4.toLowerCase();

				line_value1.append("<td style=\"display:none\">'+($('#dynamic-table1 tr').length)+'"
						+ "<input type=\"hidden\"  id=\"" + lower_case + "'+$('#dynamic-table1 tr').length+'\" name=\""
						+ lower_case + "\" />"

						+ "<td>" + "<center>"
						+ "<input type=\"checkbox\" class=\"Deleterow\" id=\"chk'+$('#dynamic-table1 tr').length+'\" name=\"chk\" style=\"text-align:center;\"/>"
						+ "</center>" + "</td>");
			}

			// for loop for other field values

			for (int i = 0; i < line_varchar.size(); i++) {
				String field_name4 = line_varchar.get(i).getMapping();
				String mapping4 = line_varchar.get(i).getMapping();
				String data_type4 = line_varchar.get(i).getDataType();
				String lower_case = field_name4.toLowerCase();
				System.out.println("field name-------" + field_name4);
				System.out.println("mapping-------" + mapping4);
				System.out.println("data type-------" + data_type4);

				String mapping_lower = mapping4.toLowerCase();

//				String mandatory = line_varchar.get(i).getMandatory();
//				String readonly = line_varchar.get(i).getReadonly();
//				String hidden = line_varchar.get(i).getHidden();
				boolean mandatory = line_varchar.get(i).isMandatory();
				boolean readonly = line_varchar.get(i).isReadonly();
				boolean hidden = line_varchar.get(i).isHidden();

				String type_field = line_varchar.get(i).getType_field();

				if (type_field.equals("l_textfield")) {

					line_value.append("<td>" + "<input");
					if (hidden == true) {
						line_value.append("\ttype=\"hidden\"");
					} else {
						line_value.append("\ttype=\"text\"");
					}

					line_value.append("\t  value=\"${" + line_table_name_lower + "_update." + lower_case + "}\" id=\""
							+ lower_case + "\" name=\"" + lower_case + "\" style=\"width:100%;\"");

					if (mandatory == true) {
						line_value.append(" \trequired ");
					}

					if (readonly == true) {
						line_value.append(" \treadonly ");
					}

					line_value.append(" />");
					line_value.append("</td>");
				}

				if (type_field.equals("url")) {

					line_value.append("<td>" + "<input");
					if (hidden == true) {
						line_value.append("\ttype=\"hidden\"");
					} else {
						line_value.append("\ttype=\"text\"");
					}

					line_value.append("\t value=\"${" + line_table_name_lower + "_update." + lower_case + "}\"  id=\""
							+ lower_case + "\" name=\"" + lower_case
							+ "\" style=\"width:100%;\" placeholder=\"URL\"pattern=\"https://.*\" size=\"30\"");

					if (mandatory == true) {
						line_value.append(" \trequired ");
					}

					if (readonly == true) {
						line_value.append(" \treadonly ");
					}

					line_value.append(" />");
					line_value.append("</td>");
				}

				if (type_field.equals("email")) {

					line_value.append("<td>" + "<input");
					if (hidden == true) {
						line_value.append("\ttype=\"hidden\"");
					} else {
						line_value.append("\ttype=\"email\"");
					}

					line_value.append("\t value=\"${" + line_table_name_lower + "_update." + lower_case + "}\"  id=\""
							+ lower_case + "\" name=\"" + lower_case
							+ "\" style=\"width:100%;\" placeholder=\"E_Mail\"  onblur=\"validateEmail(this);\" ");

					if (mandatory == true) {
						line_value.append(" \trequired ");
					}

					if (readonly == true) {
						line_value.append(" \treadonly ");
					}

					line_value.append(" />");
					line_value.append("</td>");

					togglescript_l.append("\nfunction validateEmail(emailField) {"
							+ "\n			var reg = /^([A-Za-z0-9_\\-\\.])+\\@([A-Za-z0-9_\\-\\.])+\\.([A-Za-z]{2,4})$/;"
							+ "\n			if (reg.test(emailField.value) == false) {"
							+ "\n				alert('Invalid Email Address');" + "\n				return false;"
							+ "\n			}" + "\n			return true;" + "\n		}");
				}

				if (type_field.equals("masked")) {

					line_value.append("<td>" + "<input");
					if (hidden == true) {
						line_value.append("\ttype=\"hidden\"");
					} else {
						line_value.append("\ttype=\"password\"");
					}

					line_value.append("\t value=\"${" + line_table_name_lower + "_update." + lower_case + "}\"  id=\""
							+ lower_case + "\" name=\"" + lower_case
							+ "\" style=\"width:100%;\" placeholder=\"Masked\" maxlength=\"8\"");

					if (mandatory == true) {
						line_value.append(" \trequired ");
					}

					if (readonly == true) {
						line_value.append(" \treadonly ");
					}

					line_value.append(" />");
					line_value.append("</td>");
				}

				if (type_field.equals("textarea")) {

					line_value.append("<td>" + "<textarea");
					if (hidden == true) {
						line_value.append("\ttype=\"hidden\"");
					} else {
						line_value.append("\ttype=\"text\"");
					}

					line_value.append("\t value=\"${" + line_table_name_lower + "_update." + lower_case + "}\"  id=\""
							+ lower_case + "\" name=\"" + lower_case + "\" style=\"width:100%;\" rows=\"" + 0
							+ "\" cols=\"" + 34 + "\"");

					if (mandatory == true) {
						line_value.append(" \trequired ");
					}

					if (readonly == true) {
						line_value.append(" \treadonly ");
					}

					line_value.append(" />${" + mapping_lower + "_update." + lower_case + "}</textarea>");
					line_value.append("</td>");
				}

				if (type_field.equals("currency_field")) {

					line_value.append("<td>" + "<input");
					if (hidden == true) {
						line_value.append("\ttype=\"hidden\"");
					} else {
						line_value.append("\ttype=\"text\"");
					}

					line_value.append("\t value=\"${" + line_table_name_lower + "_update." + lower_case + "}\"  id=\""
							+ lower_case + "\" name=\"" + lower_case
							+ "\" style=\"width:100%;\" placeholder=\"$1,000,000.00\" onblur=\"handleChange()\"");

					if (mandatory == true) {
						line_value.append(" \trequired ");
					}

					if (readonly == true) {
						line_value.append(" \treadonly ");
					}

					line_value.append(" />");
					line_value.append("</td>");

					togglescript_l.append("\nfunction handleChange() {" + "\nvar x = document.getElementById(\""
							+ lower_case + "\").value;" + "  \n  if (x.indexOf(\"$\") != 0)" + "    \n{"
							+ "      \n  x = \"$\" + x.toString().replace(/\\B(?=(\\d{3})+(?!\\d))/g, \",\");"
							+ "    \n}" + "    " + "    \ndocument.getElementById(\"" + lower_case + "\").value = x;"
							+ "\n};");

				}

				if (type_field.equals("datetime")) {

					line_value.append("<td>" + "<div class=\"clearfix\">"
							+ "<div class=\"input-group input-append date\" id=\"datePicker1" + i + "\" "
							+ " style=\"width:100%;\">" + "<input value=\"${" + line_table_name_lower + "_update."
							+ lower_case + "}\"  type=\"text\" name=\"" + lower_case + "\" id=\"" + lower_case
							+ "\" placeholder=\"picup Date\" class=\"form-control\"   required/>"
							+ "<span class=\"input-group-addon\">"
							+ "<i class=\"fa fa-calendar bigger-110\"></i></span>" + "</div>" + "</div>" + "</td>");

					date_script_l.append(" $('#datePicker1" + i
							+ "').datepicker({format : 'dd-mm-yyyy'}).on('changeDate', function(e) {$('#eventForm').formValidation('revalidateField', 'date');});\n\n");

				}

				if (type_field.equals("contact_field")) {

					line_value.append("<td>" + "<div style=\"position:relative\">" + "<select style=\"   height:\";\" >"
							+ "<option>+91</option>" + "<option>+44</option>" + "<option>+64</option>"
							+ "<option>+21</option>" + "<option>+244</option>" + "<option>+376</option>"
							+ "<option>+354</option>" + "<option>+291</option>" + "<option>+54</option>"
							+ "<option>+264</option>" + "<option>+121</option>" + "</select>"

							+ "<input");
					if (hidden == true) {
						line_value.append("\ttype=\"hidden\"");
					} else {
						line_value.append("\ttype=\"tel\"");
					}

					line_value.append(
							"\t value=\"${" + line_table_name_lower + "_update." + lower_case + "}\"  id=\"" + lower_case
									+ "\" name=\"" + lower_case + "\" style=\"width:70%;\"  pattern=\"[0-9]{10}\"");

					if (mandatory == true) {
						line_value.append(" \trequired ");
					}

					if (readonly == true) {
						line_value.append(" \treadonly ");
					}

					line_value.append(" />");
					line_value.append("</td>");
				}

				if (type_field.equals("upload_field")) {

					line_value.append("<td>" + "<input");
					if (hidden == true) {
						line_value.append("\ttype=\"hidden\"");
					} else {
						line_value.append("\ttype=\"file\"");
					}

					line_value.append("\t value=\"${" + line_table_name_lower + "_update." + lower_case + "}\"  id=\""
							+ lower_case + "\" name=\"" + lower_case
							+ "\" style=\"width:100%;\"  placeholder=\"Upload File\" style=\"width:100%; padding-left:10px;\"");

					if (mandatory == true) {
						line_value.append(" \trequired ");
					}

					if (readonly == true) {
						line_value.append(" \treadonly ");
					}

					line_value.append(" />");
					line_value.append("</td>");

					dropdown_script_latest_l.append("\n$(function () {          " + "\n$(\"#" + lower_case
							+ "\").ace_file_input({" + "	\nno_file : 'No File ...'," + "	\nbtn_choose : 'Choose',"
							+ "\n	btn_change : 'Change'," + "	\ndroppable : false," + "	\nonchange : null,"
							+ "	\nthumbnail : false" + "\n//| true | large" + "\n//whitelist:'gif|png|jpg|jpeg'"
							+ "\n//blacklist:'exe|php'" + "\n//onchange:''" + "\n//" + "\n});" + "\n});"

					);

				}

				if (type_field.equals("dropdown")) {

					line_value.append("<td>" + "<input value=\"${" + line_table_name_lower + "_update." + lower_case
							+ "}\"  id=\"" + lower_case + "\" name=\"" + lower_case + "\"   ");
					line_value.append(" />");
					line_value.append("</td>");

					dropdown_script_latest_l.append("\n var colors = [" + " \n           { Name: \"option1\" },"
							+ "   \n         { Name: \"option2\" }," + "     \n       { Name: \"option3\" },"
							+ "       \n     { Name: \"option4\" }," + "         \n   { Name: \"option5\" },"
							+ "           \n { Name: \"option6\" }," + "            \n{ Name: \"option7\" },"
							+ "            \n{ Name: \"option8\" }" + "        \n];" + "        \n$(function () {"
							+ "          \n  $(\"#singleSelectCombo\").igCombo({" + "            \n    width: 325, "
							+ "              \n  dataSource: colors," + "                \ntextKey: \"Name\","
							+ "                \nvalueKey: \"Name\"," + "                \ndropDownOnFocus: true,"
							+ "                \ndropDownOrientation: \"bottom\"" + "            \n});"
							+ "            \n$(\'#" + lower_case + "\').igCombo({"
							+ "              \n  width: \"100%\"," + "                \ndataSource: colors,"
							+ "                \ntextKey: \"Name\"," + "                \nvalueKey: \"Name\","
							+ "                \nmultiSelection: {" + "                  \n  enabled: true"
							+ "                \n}," + "                \ndropDownOrientation: \"bottom\""
							+ "            \n});" + "            \n$(\"#checkboxSelectCombo\").igCombo({"
							+ "              \n  width: 325," + "                \ndataSource: colors,"
							+ "               \n textKey: \"Name\"," + "                \nvalueKey: \"Name\","
							+ "                \nmultiSelection: {" + "                  \n  enabled: true,"
							+ "                    \nshowCheckboxes: true" + "                \n},"
							+ "                \ndropDownOrientation: \"bottom\"" + "            \n});"
							+ "        \n});"

					);
				}

				if (type_field.equals("autocomplete")) {

					line_value.append("<td>" + "<input");
					if (hidden == true) {
						line_value.append("\ttype=\"hidden\"");
					} else {
						line_value.append("\ttype=\"text\"");
					}

					line_value.append("\t value=\"${" + line_table_name_lower + "_update." + lower_case + "}\"  id=\""
							+ lower_case + "\" name=\"" + lower_case + "\" style=\"width:100%;\"");

					if (mandatory == true) {
						line_value.append(" \trequired ");
					}

					if (readonly == true) {
						line_value.append(" \treadonly ");
					}

					line_value.append(" />");
					line_value.append("</td>");

					autocomplete_script_latest_l
							.append("<script>" + "\nvar options = {" + "\nurl: '${pageContext.request.contextPath}/"
									+ lower_case + "_list'," + "\ngetValue: \"+mapping_lower+\"," + "\nlist: {"
									+ "match: {" + "\nenabled: true" + "\n}}," + "\ntheme: \"square\"" + "\n};"
									+ "\n$(\"#" + lower_case + "\").easyAutocomplete(options);"

									+ "\nvar options={" + "\nurl: '${pageContext.request.contextPath}/" + lower_case
									+ "_list'," + "\ngetValue: \"+mapping_lower+\"," + "\nlist: {" + "\nmatch: {"
									+ "\nenabled: true" + "\n}}," + "\n};" + "\n$(\"#" + lower_case
									+ "\").easyAutocomplete(options);" + "\n</script>"

							);

				}

				if (type_field.equals("togglebutton")) {

					line_value.append("<td>" + "<center>" + "<div class=\"clearfix\">" + " <input value=\"${"
							+ line_table_name_lower + "_update." + lower_case + "}\"  name=\"" + lower_case + "\" id=\""
							+ lower_case
							+ "\" class=\"ace ace-switch ace-switch-6\" type=\"checkbox\" onblur=\"CheckUserStatus"
							+ lower_case + "()\"/>" + "<span class=\"lbl\"></span>" + "</div>" + "</center>" + "</td>");

					togglescript_l.append("function CheckUserStatus" + lower_case + "() {" + "\r\n" + "\r\n"
							+ "	\n		if (document.getElementById(\"" + lower_case + "\").checked == true) {\r\n"
							+ "	\n			document.getElementById(\"" + lower_case + "\").value = 'Y';\r\n" + "\r\n"
							+ "			} else if (document.getElementById(\"" + lower_case
							+ "\").checked == false) {\r\n" + "				document.getElementById(\"" + lower_case
							+ "\").value = 'N';\r\n" + "			}\r\n" + "		}");

					togglescript_l.append("		\n" + "		\nvar d=$(\'#" + lower_case + "\').val();" + "    	 \n"
							+ "		\nif(d!=null){" + "			\n	" + "        	\nif(d=='Y'){" + "        		\n"
							+ "            \ndocument.getElementById(\"" + lower_case + "\").checked = true;"
							+ "        		\n}" + "        	\nelse if(d=='N'){"
							+ "        	  \n  document.getElementById(\"" + lower_case + "\").checked = false;"
							+ "        	\n}" + "        	\n" + "        	\ndocument.getElementById(\"" + lower_case
							+ "\").value=d;" + "			\n}" + "		\n" + "		\nvar count=50"
							+ "		\nfor(var i=1;i<count;i++){" + "			\nvar d=$(\"" + lower_case
							+ "\"+i).val();" + "	    	  \n//alert(d)" + "			\nif(d!=null){"
							+ "	        	\nif(d=='Y'){" + "	        		\n//$(\"#active\"+i).val(true);"
							+ "	            \ndocument.getElementById(\"" + lower_case + "\"+i).checked = true;"
							+ "	        	\n}else if(d=='N'){" + "	        	  \n  document.getElementById(\""
							+ lower_case + "\"+i).checked = false;" + "	        	\n}"
							+ "	        	\ndocument.getElementById(\"" + lower_case + "\"+i).value=d;"
							+ "				\n}" + "			\n}		" + "		\n");

				}

				if (type_field.equals("checkbox")) {

					line_value.append("<td>" + "<center>" + "<input value=\"${" + line_table_name_lower + "_update."
							+ lower_case + "}\"  type=\"checkbox\"  id=\"" + lower_case + "\" name=\"" + lower_case
							+ "\" onblur=\"CheckUserStatus" + lower_case + "()\"" + "\" style=\"text-align:center;\"/>"
							+ "</center>" + "</td>");

					togglescript_l.append("function CheckUserStatus" + lower_case + "() {" + "\r\n" + "\r\n"
							+ "	\n		if (document.getElementById(\"" + lower_case + "\").checked == true) {\r\n"
							+ "	\n			document.getElementById(\"" + lower_case + "\").value = 'Y';\r\n" + "\r\n"
							+ "			} else if (document.getElementById(\"" + lower_case
							+ "\").checked == false) {\r\n" + "				document.getElementById(\"" + lower_case
							+ "\").value = 'N';\r\n" + "			}\r\n" + "		}");
					togglescript_l.append("		\n" + "		\nvar d=$(\'#" + lower_case + "\').val();" + "    	 \n"
							+ "		\nif(d!=null){" + "			\n	" + "        	\nif(d=='Y'){" + "        		\n"
							+ "            \ndocument.getElementById(\"" + lower_case + "\").checked = true;"
							+ "        		\n}" + "        	\nelse if(d=='N'){"
							+ "        	  \n  document.getElementById(\"" + lower_case + "\").checked = false;"
							+ "        	\n}" + "        	\n" + "        	\ndocument.getElementById(\"" + lower_case
							+ "\").value=d;" + "			\n}" + "		\n" + "		\nvar count=50"
							+ "		\nfor(var i=1;i<count;i++){" + "			\nvar d=$(\"" + lower_case
							+ "\"+i).val();" + "	    	  \n//alert(d)" + "			\nif(d!=null){"
							+ "	        	\nif(d=='Y'){" + "	        		\n//$(\"#active\"+i).val(true);"
							+ "	            \ndocument.getElementById(\"" + lower_case + "\"+i).checked = true;"
							+ "	        	\n}else if(d=='N'){" + "	        	  \n  document.getElementById(\""
							+ lower_case + "\"+i).checked = false;" + "	        	\n}"
							+ "	        	\ndocument.getElementById(\"" + lower_case + "\"+i).value=d;"
							+ "				\n}" + "			\n}		" + "		\n");
				}

			}

			for_line_part_update.append(
					"\n<div class=\"table-header\" style=\"margin-top: 30px;\">Group Lines Details</div>" + "\n<div>"
							+ "\n<% int n=0; %>" + "\n<input type=\"hidden\" id=\"rowcount1\" name=\"rowcount1\">"
							+ "\n<input type=\"hidden\" id=\"delrow1\" name=\"delrow1\" value=\"\">"
							+ "\n<table id=\"dynamic-table1\" class=\"table table-striped table-bordered table-hover\">"
							+ "\n<thead>" + "\n<tr>");

			// for loop for id is primary

			for (int i = 0; i < line_id_primary.size(); i++) {
				for_line_part_update.append("\n<th>" + "\n<center>Select</center>" + "\n</th>");
			}

			// for loop for other field (like varchar field)

			for (int i = 0; i < line_varchar.size(); i++) {
				String field_name4 = line_varchar.get(i).getFieldName();
				for_line_part_update.append("\n<th>" + "\n<center>" + field_name4 + "</center>" + "\n</th>");
			}

			for_line_part_update.append("\n\n<%@include file=\"/WEB-INF/tiles/acemaster/" + module_name + "/"
					+ line_table_name_lower + "_extension.jsp\"%>\n\n</tr>");

			for_line_part_update.append("\n</thead>" + "\n<tbody>"
			// for update
					+ "\n<% int j=1; %>" + "\n<c:forEach var=\"" + line_table_name_lower
					+ "_update\" items=\"${linelist}\" varStatus=\"status\">" + "\n<tr>" + line_value_update1 + line_value
					+ "<%@include file=\"/WEB-INF/tiles/acemaster/" + module_name + "/" + line_table_name_lower
					+ "_ext_update.jsp\"%></tr>" + "\n<%  j=j+1; %>" + "\n</c:forEach>" + "\n</tbody>" + "\n</table>"

					+ "\n<input type=\"button\" class=\"btn btn-md btn-success\""
					+ "\nstyle=\"background-color: #87b87f; border-color: #87b87f; margin-top: 1%; margin-bottom: 1%;\""
					+ "\nvalue=\"Add Row\" onclick=\"AddRow()\">"

					+ "\n<input type=\"button\" class=\"btn btn-md btn-success\""
					+ "\nstyle=\"background-color: #87b87f; border-color: #87b87f; margin-top: 1%; margin-bottom: 1%; font-size: auto;\""
					+ "\nvalue=\"Delete Row\" onclick=\"del()\">" + "\n</div>");
		}
//-----------------------------------------------------line readonly--------------------------------------------------------------------------							
		if (line_table_name != null && !line_table_name.isEmpty()) {

			String line_table_name_lower = line_table_name.toLowerCase();
			for (int i = 0; i < line_id_primary.size(); i++) {
				String mapping4 = line_id_primary.get(i).getMapping();
				String lower_case = mapping4.toLowerCase();

				line_value_readonly.append("<td style=\"display:none\">'+($('#dynamic-table1 tr').length)+'"
						+ "<input type=\"hidden\" value=\"${" + line_table_name_lower + "_update." + lower_case
						+ "}\" id=\"" + lower_case + "\" name=\"" + lower_case + "\"  <%=j%>/>"

						+ "<td>" + "<center>"
						+ "<input type=\"checkbox\" class=\"Deleterow\" id=\"chk\" name=\"chk\" style=\"text-align:center;\"/>"
						+ "</center>" + "</td>");
			}

			// for loop for other field values

			for (int i = 0; i < line_varchar.size(); i++) {
				String field_name4 = line_varchar.get(i).getFieldName();
				String mapping4 = line_varchar.get(i).getMapping();
				String data_type4 = line_varchar.get(i).getDataType();
				System.out.println("field name-------" + field_name4);
				System.out.println("mapping-------" + mapping4);
				System.out.println("data type-------" + data_type4);

				boolean mandatory = line_varchar.get(i).isMandatory();
				boolean hidden = line_varchar.get(i).isHidden();
				boolean readonly = line_varchar.get(i).isReadonly();
				String type_field = line_varchar.get(i).getType_field();
				  	 

			}

			for_line_part_readonly.append(
					"\n<div class=\"table-header\" style=\"margin-top: 30px;\">Group Lines Details</div>" + "\n<div>"
							+ "\n<% int n=0; %>" + "\n<input type=\"hidden\" id=\"rowcount1\" name=\"rowcount1\">"
							+ "\n<input type=\"hidden\" id=\"delrow1\" name=\"delrow1\" value=\"\">"
							+ "\n<table id=\"dynamic-table1\" class=\"table table-striped table-bordered table-hover\">"
							+ "\n<thead>" + "\n<tr>");

			// for loop for id is primary

			for (int i = 0; i < line_id_primary.size(); i++) {
				for_line_part_readonly.append("\n<th>" + "\n<center>Select</center>" + "\n</th>");
			}

			// for loop for other field (like varchar field)

			for (int i = 0; i < line_varchar.size(); i++) {
				String field_name4 = line_varchar.get(i).getMapping();
				for_line_part_readonly.append("\n<th>" + "\n<center>" + field_name4 + "</center>" + "\n</th>");
			}

			for_line_part_readonly.append("\n</tr>" + "\n</thead>" + "\n<tbody>"
			// for update
					+ "\n<% int j=1; %>" + "\n<c:forEach var=\"" + line_table_name_lower
					+ "_update\" items=\"${linelist}\" varStatus=\"status\">" + "\n<tr>" + line_value_readonly);

			for (int i = 0; i < line_varchar.size(); i++) {
				String mapping = line_varchar.get(i).getMapping();

				for_line_part_readonly.append("\n <td><input	type=\"text\"	 value=\"${" + line_table_name_lower
						+ "_update." + mapping + "}\" id=\"" + mapping + "<%=j%>\" name=\"" + mapping
						+ "\" style=\"width:100%;\" readonly />" + "</td>");

			}
			for_line_part_readonly.append("\n\n<%@include file=\"/WEB-INF/tiles/acemaster/" + module_name + "/"
					+ line_table_name_lower + "_ext_readonly.jsp\"%>\n</tr>" + "\n<%  j=j+1; %>" + "\n</c:forEach>"
					+ "\n</tbody>" + "\n</table>"

					+ "\n<input type=\"button\" class=\"btn btn-md btn-success\""
					+ "\nstyle=\"background-color: #87b87f; border-color: #87b87f; margin-top: 1%; margin-bottom: 1%;\""
					+ "\nvalue=\"Add Row\" >"

					+ "\n<input type=\"button\" class=\"btn btn-md btn-success\""
					+ "\nstyle=\"background-color: #87b87f; border-color: #87b87f; margin-top: 1%; margin-bottom: 1%; font-size: auto;\""
					+ "\nvalue=\"Delete Row\" >" + "\n</div>");

		}

		// script for line entry part
		line_script.append("\n<script language=\"javascript\">" + "\nfunction AddRow()  "
				+ "\n{$('#dynamic-table1 tr').last().after('<tr>" + line_value1 + line_value);

		if (line_table_name != null && !line_table_name.isEmpty()) {
			String line_table_name_lower = line_table_name.toLowerCase();
			line_script.append("<%@include file=\"/WEB-INF/tiles/acemaster/" + module_name + "/" + line_table_name_lower
					+ "_extension2.jsp\"%>");
		}

		line_script.append("</tr>');");

		for (int i = 0; i < line_varchar.size(); i++) {
			String type_field = line_varchar.get(i).getType_field();
			if (type_field.equals("datetime")) {
				line_script.append("\n$('#datePicker1" + i
						+ "').datepicker({format : 'dd-mm-yyyy'}).on('changeDate', function(e) {$('#eventForm').formValidation('revalidateField', 'date');});");
			}
		}
		line_script.append("\n$('#rowcount1').val($('#dynamic-table1 tr').length-1);"
				+ "\nvar count = $('#rowcount1').val();" + dropdown_script_latest_for_line
				+ "\n$('.Deleterow').click(function() { " + "\nvar index = $('.Deleterow').index(this)+1;"
				+ "\n$('#delrow1').val(index);" + "\n});" + "\n}" + "\n</script>" + "\n<script>" + "\nfunction del(){"
				+ "\nvar index=$('#delrow1').val();" + "\nif(index!= \"\")" + "\n{"
				+ "\ndocument.getElementById(\"dynamic-table1\").deleteRow(index);" + "\n$('#delrow1').val(\"\");"
				+ "\n}" + "\n}" + "\n</script>"

				+ "<script>"

				+ togglescript_l + date_script_l + dropdown_script_latest_l + autocomplete_script_latest_l
				+ "</script>");

		// script for line update part
		if (line_table_name != null && !line_table_name.isEmpty()) {
			String line_table_name_lower = line_table_name.toLowerCase();
			line_script_update.append("\n<script language=\"javascript\">" + "\nfunction AddRow()  "
					+ "\n{$('#dynamic-table1 tr').last().after('<tr>" + line_value1 + line_value);
			if (line_table_name != null && !line_table_name.isEmpty()) {
				line_table_name_lower = line_table_name.toLowerCase();
				line_script_update.append("<%@include file=\"/WEB-INF/tiles/acemaster/" + module_name + "/"
						+ line_table_name_lower + "_extension2.jsp\"%>");
			}
			line_script_update.append("</tr>');" + "\n$('#rowcount1').val($('#dynamic-table1 tr').length-1);"
					+ "\nvar count = $('#rowcount1').val();" + "\n$.ajax({"
					+ "\nurl: '${pageContext.request.contextPath}/loadreports'," + "\ndata: ({name : 'name'}),"
					+ "\nsuccess: function(data) {" + "\nvar select = $('#name'+count);"
					+ "\nselect.find('option').remove();" + "\n$.each(data, function(index, value) {"
					+ "\n$('<option>').val(value).text(value).appendTo(select);" + "\n});" + "\n}" + "\n});"

					+ "\n$('.Deleterow').click(function() { " + "\nvar index = $('.Deleterow').index(this)+1;"
					+ "\n$('#delrow1').val(index);" + "\n});" + "\n}" + "\n</script>"

					+ "\n<script>" + "\nfunction del(){" + "\nvar index=$('#delrow1').val();" + "\nif(index!= \"\")"
					+ "\n{" + "\ndocument.getElementById(\"dynamic-table1\").deleteRow(index);"
					+ "\n$('#delrow1').val(\"\");" + "\n}" + "\n}" + "\n</script>"

					+ "<script>"

					+ togglescript_l + date_script_l + dropdown_script_latest_l + autocomplete_script_latest_l
					+ "</script>");

		}

		sectionform.append(importsection + " \n" + headsection
				+ "\n<body>\n"
				// FORM_CODE IN ENTRY_FORM (SECTION)
				+ "<input type=\"hidden\" name=\"form_code\" id=\"form_code\" value=\"" + form_code + "\" />\n"
				+ "<div class=\"main-container\" id=\"main-container\">" + "\n<div class=\"main-content\">"
				+ "\n<div class=\"main-content-inner\">" + "\n<div class=\"breadcrumbs\" id=\"breadcrumbs\">"
				+ "\n<ul class=\"breadcrumb\">"
				+ "\n<li><i class=\"ace-icon fa fa-home home-icon\"></i> <a href=\"#\">Home</a>" + "\n</li>"
				+ "\n<li><a href=\"#\">ManageUsers</a></li>" + "\n</ul>" + "\n</div>"

				+ "\n<div class=\"page-content\">" + "\n<div class=\"page-header\">" + "\n<h1>" + ui_name + ""
				+ "<div style=\"float: right; padding-right: 5%;\">"
				+ "<a href=\"rn_form_builder_extension\"><i class=\"fa fa-paper-plane-o fa-1x\" aria-hidden=\"true\" title=\"help\"></i></a>"

				+ "</div></h1>" + "\n</div>"

				+ "\n<div class=\"row\">" + "\n<div class=\"col-xs-12\">" + "\n<div class=\"widget-body\">"
				+ "\n<div class=\"widget-main\">" + "\n<div id=\"fuelux-wizard-container\">"
				+ "\n<div class=\"step-content pos-rel\">" + "\n<div class=\"step-pane active\" data-step=\"1\">" + "");

		sectionform.append(form);

		sectionform.append("\n<div class=\"wizard-actions\">");

		for (int i = 0; i < lineList.size(); i++) {
			String type_field = lineList.get(i).getType_field();
			String field_name = lineList.get(i).getFieldName();
			String mapping = lineList.get(i).getMapping();
			System.out.println("BUTTON LOOP = " + type_field + " - " + field_name + " - " + mapping);

			final String type_field_button = "button";
			final String field_name_button = "submit";
			if (type_field_button.equals(type_field) && field_name_button.equals(field_name)) {
				sectionform
						.append("\n<button type=\"submit\" class=\"btn btn-success center\" onclick=\"submitForms()\">"
								+ "\n" + mapping + "" + "\n</button>");
			}
		}

		/*
		 * @Author : Niladri 
		 * @Date: 13.10.20 PROPERTIES FILE CODE
		 */
		properties_file_fields_code.append("created_by, last_updated_by, creation_date, last_update_date, account_id");
		String fields_code = properties_file_fields_code.toString();


		properties_file_code.append("table_name = " + table_name_first_upper + "\n" 
				//+ "line_table_name = " + line_table_name + "\n"
				+ "controller_name = " + controller_name_first_upper + "\n" 
				+ "dao_name = " + dao_name_first_upper + "\n"
				+ "dao_impl_name = " + dao_impl_name_first_upper + "\n" 
				+ "service_name = " + service_name_first_upper + "\n" 
				+ "service_impl_name = " + service_impl_name_first_upper + "\n" 
				+ "jsp_name = " + jsp_name + "\n" 
				+ "form_code = " + form_code + "\n"
				+ fields_code + "\n");
		
		if (form_type.equalsIgnoreCase("header_line") || form_type.equalsIgnoreCase("line_only")) {
			properties_file_line_fields_code.append("created_by, last_updated_by, creation_date, last_update_date, account_id");
			properties_file_line_code.append(properties_file_line_fields_code);
			//System.out.println("after added who columns = " + properties_file_line_code);
			String line_code = "";
			if(properties_file_line_code.toString().endsWith(",")) {
				line_code = properties_file_line_code.substring(0, properties_file_line_code.length()-1).toString();
			} else {
				line_code = properties_file_line_code.toString();
			}
			properties_file_code.append(line_code);
		}

		FileWriter fwriter = null;
		BufferedWriter bwriter = null;
		try {
			// in Projects folder
			File propertiesFile = new File(
					projectPath + "/Projects/" + project_name + "/src/main/resources/" + form_code + ".properties");
			System.out.println("in /Project Properties file path = " + propertiesFile);
			if (!propertiesFile.exists()) {
				propertiesFile.getParentFile().mkdirs();
				propertiesFile.createNewFile();
			}
			fwriter = new FileWriter(propertiesFile.getAbsoluteFile());
			bwriter = new BufferedWriter(fwriter);
			bwriter.write(properties_file_code.toString());
			bwriter.close();
			fwriter.close();
			
			// in main project folder
			File propertiesFile2 = new File(
				projectPath + "/src/main/resources/" + form_code + ".properties");
			System.out.println("in /main Project Properties file path = " + propertiesFile);
			if (!propertiesFile2.exists()) {
				propertiesFile2.createNewFile();
			}
			fwriter = new FileWriter(propertiesFile.getAbsoluteFile());
			bwriter = new BufferedWriter(fwriter);
			bwriter.write(properties_file_code.toString());
			bwriter.close();
			fwriter.close();
			
		}catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		/*
		 * @Author : Niladri Sen
		 * @ CFF_Action_Builder Code Date : 6.10.20
		 * : (INSERT || UPDATE)
		 */
		int buttonLoopCount = 0;
		for (Rn_Fb_Line btnList : onlyButtonList) {
			System.out.println("NILADRI BUTON LOOP COUNT = " + ++buttonLoopCount);
			String field_name = btnList.getFieldName();
			String mapping = btnList.getMapping();
			String method_name = btnList.getMethodName();

			System.out.println("Niladri BUTTON-LOOP field name = " + field_name + " || mapping = " + mapping
					+ " || method_name = " + method_name);

			// JSP CODE FOR EXTRA BUTTON IS COMMON
			cff_add_button_code_in_update_jsp.append("\r\n<div>\r\n"
					+ "							<a href=\"" + method_name + "?id=${cff_id}\">\r\n"
					+ "								<button type=\"button\" id=\"" + mapping + "\" name = \"" + mapping
					+ "\" class=\"btn btn-success btn-sm btn-next\">" + field_name + "</button>\r\n"
					+ "							</a>\r\n" 
					+ "						</div>\r\n");
			StringBuilder cff_add_button_controller_code = new StringBuilder();
			StringBuilder cff_add_button_controller_imports = new StringBuilder();
			cff_add_button_controller_imports.append("package com.realnet." + module_name + ".controller;\r\n" + "\r\n"
					+ "import java.io.IOException;\r\n" + "import java.text.ParseException;\r\n"
					+ "import java.time.LocalDateTime;\r\n" + "import java.time.format.DateTimeFormatter;\r\n"
					+ "import java.util.List;\r\n" + "\r\n" + "import javax.servlet.http.HttpServletRequest;\r\n"
					+ "import javax.validation.Valid;\r\n" + "\r\n"
					+ "import org.springframework.beans.factory.annotation.Autowired;\r\n"
					+ "import org.springframework.stereotype.Controller;\r\n"
					+ "import org.springframework.ui.ModelMap;\r\n"
					+ "import org.springframework.validation.BindingResult;\r\n"
					+ "import org.springframework.web.bind.annotation.GetMapping;\r\n"
					+ "import org.springframework.web.bind.annotation.ModelAttribute;\r\n"
					+ "import org.springframework.web.bind.annotation.PostMapping;\r\n"
					+ "import org.springframework.web.bind.annotation.RequestMapping;\r\n"
					+ "import org.springframework.web.bind.annotation.RequestMethod;\r\n"
					+ "import org.springframework.web.bind.annotation.RequestParam;\r\n"
					+ "import org.springframework.web.servlet.ModelAndView;\r\n" + "\r\n"
					// + "import com.realnet.actionbuilder.dao.Rn_cff_ActionBuilder_Utils_Dao;\r\n"
					+ "import com.realnet." + module_name + ".model." + table_name_first_upper + ";\n"
					+ "import com.realnet." + module_name + ".dao." + dao_name_first_upper + ";\n"
					+ "import com.realnet." + module_name + ".service." + service_name_first_upper + ";\n" + "");

			// CONTROLLER NAME FOR EACH BUTTON TYPE
			String controllerName = method_name.substring(0, 1).toUpperCase()
					+ method_name.substring(1).concat("Controller");
			System.out.println("Niladri controllerName = " + controllerName);
			// CONTROLLER PREFIX
			cff_add_button_controller_code.append(
					cff_add_button_controller_imports + "@Controller\r\n" + "public class " + controllerName + " {\r\n"
							+ "	@Autowired" + "	private " + dao_name_first_upper + "\t" + dao_name_lower + ";\n");

			// FIELD NAME SHOULD CHANGE IN FUTURE FOR COMPARISON.... (SELF TABLE COPY)
//			if (WFConstant.INSERT_BUTTON.equals(field_name) || WFConstant.UPDATE_BUTTON.equals(field_name)) {
				// FOR INSERT CODE
				// NEED MODIFICATION FOR STAR AND END STRING COLLITION
				cff_add_button_controller_code.append("	// INSERT FIELDS USING ACTION BUILDER\r\n"
						+ "@GetMapping(value = \"/" + method_name + "\")\r\n" + "	public ModelAndView " + method_name
						+ "(@RequestParam(value = \"id\") String h_id) throws IOException {\r\n"
						+ "		int hId = Integer.parseInt(h_id);\r\n"
						+ "		//System.out.println(\"JSP ID = \" + hId);\r\n" + "	// CFF_LAYOUT_CONTROLLER_START\r\n"
						+ "		System.out.println(\"PLEASE INSERT CODE... GO TO ACTION BUILDER... \");\r\n" 
						+ "	// CFF_LAYOUT_CONTROLLER_END\r\n" + "		return new ModelAndView(\"redirect:"
						+ table_name_lower + "_update?id=\" + hId);\r\n" + "	}\n" + "}");
				
			// FILE PATH CHANGE NEEDED...
			File file = new File(projectPath + "/Projects/" + project_name + "/src/main/java/com/realnet/" + module_name
					+ "/controller/" + controllerName + ".java");
			System.out.println("Niladri file path = " + file.getAbsolutePath());
			if (!file.exists()) {
				file.getParentFile().mkdirs();
				file.createNewFile();
			}

			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(cff_add_button_controller_code.toString());
			bw.close();

			/*
			 * CONTROLLER NAME AND FILE-LOCATION SHOULD SAVE IN CFF ACTION BUILDER HEADER
			 * TABLE
			 */
			String file_location = file.getAbsolutePath().replace("\\", "/");
			// SHOULD BE LIKE = /Projects/only_header_testing/Demo.java
			file_location = file_location.substring(file_location.lastIndexOf("/Projects/"));
			/*
			 * ====== MODIFICATION ==== path =
			 * path.substring(path.lastIndexOf("/Projects/")+1); path =
			 * path.substring(path.indexOf("/")); OP = /only_header_testing/Demo.java
			 */
			System.out.println("FILE LOCATION SUBSTRING = " + file_location);
			controllerName = controllerName.concat(".java");
			Rn_cff_ActionBuilder_Header header = new Rn_cff_ActionBuilder_Header();
			header.setRn_fb_header(rn_fb_header); // SAVE rn_fb_header_id
			header.setTechnologyStack(technology_stack);
			header.setControllerName(controllerName); // NO NEED
			header.setMethodName(method_name);
			header.setFileLocation(file_location);
			header.setActionName(method_name); // action name and method name is same
			actionBuilderService.save(header);
		}
		// EXTRA BUTTON LOOP END

		sectionform.append("\n</div> " + "\n</form>");

		sectionform.append("\n</div>");
		sectionform.append("\n</div>" + "\n</div>" + "\n</div>" + "\n</div>" + "\n</div>" + "\n</div>" + "\n</div>"
				+ "\n</div>" + "\n</div>" + "\n</div>" + "\n</div>"
				+ "<script src=\"<c:url value='/resources/assets/js/bootstrap-datepicker.min.js'/>\" type=\"text/javascript\">"
				+ "\n</script>"
				+ "<script src=\"<c:url value='cdnjs.cloudflare.com/ajax/libs/easy-autocomplete/1.3.5/jquery.easy-autocomplete.min.js'/>\" type=\"text/javascript\">"

				+ "\n</script>" + "<script>" + date_script + dropdown_script_latest + dropdown_script_latest1

				+ togglescript + "</script>" + "\n<!-- ace scripts -->"
				+ "	\n<script src=\"<c:url value='/resources/assets/js/ace-elements.min.js'/>\""
				+ "		\ntype=\"text/javascript\"></script>"
				+ "	\n<script src=\"<c:url value='/resources/assets/js/ace.min.js'/>\""
				+ "	\n	type=\"text/javascript\"></script>" + "	\n<!-- inline scripts related to this page -->"

				+ autocomplete_script_latest + line_script + dropdown_script + dependent_dropdown + autocomplete_script
				+ "\n</body>\n</html>");

		// -----------grid view code----------------------------

		for (int i = 0; i < id_list.size(); i++) {
			String field_name1 = id_list.get(i).getFieldName();
			String field_name = id_list.get(i).getMapping();
			String field_name_lower = field_name.toLowerCase();
			table_field_for_id.append("\n<th class=\"center\">" + field_name1 + "</th>");

			table_field_value_for_id.append("<td class=\"center\">" + "<a href=\"" + table_name_lower + "_readonly?"
					+ field_name_lower + "=${" + table_name_lower + "." + field_name_lower + "}\">"
					+ "<i class=\"fa fa-eye green\" aria-hidden=\"true\"></i>/" + "</a>" + "<a href=\""
					+ table_name_lower + "_update?" + field_name_lower + "=${" + table_name_lower + "."
					+ field_name_lower + "}\">" + "<i class=\"fa fa-edit red\" aria-hidden=\"true\"></i>/" + "</a>"
					+ "<a href=\"rolenewviewreports?user_id=${rn_userlist.id}\">"
					+ "<i class=\"fa fa-graduation-cap\" aria-hidden=\"true\"></i>" + "</a>" + "</td>");

		}

		for (int i = 0; i < id_notprimary.size(); i++) {
			String field_name1 = id_notprimary.get(i).getFieldName();
			String field_name = id_notprimary.get(i).getMapping();
			String data_type = id_notprimary.get(i).getDataType();
			String lower_case = field_name.toLowerCase();
			if (data_type.equals("int")) {

				id_not_pri_field_for_grid.append("\n<th class=\"center\">" + field_name1 + "</th>");
				id_not_pri_field_for_grid_value
						.append("\n<td class=\"center\">${" + table_name_lower + "." + lower_case + "}</td>");
			}

		}

		for (int i = 0; i < datetime_list.size(); i++) {
			String field_name1 = datetime_list.get(i).getFieldName();
			String field_name = datetime_list.get(i).getMapping();
			String data_type = datetime_list.get(i).getDataType();

			String lower_case = field_name.toLowerCase();
			if (data_type.equals("datetime")) {

				date_field_for_grid.append("\n<th class=\"center\">" + field_name1 + "</th>");
				date_field_for_grid_value
						.append("\n<td class=\"center\">${" + table_name_lower + "." + lower_case + "}</td>");

			}

		}

		for (int i = 0; i < longtext_list.size(); i++) {
			String field_name1 = longtext_list.get(i).getFieldName();
			String field_name = longtext_list.get(i).getMapping();
			String data_type = longtext_list.get(i).getDataType();

			String lower_case = field_name.toLowerCase();
			if (data_type.equals("longtext")) {

				longtext_field_for_grid.append("\n<th class=\"center\">" + field_name1 + "</th>");
				longtext_field_for_grid_value
						.append("\n<td class=\"center\">${" + table_name_lower + "." + lower_case + "}</td>");

			}

		}

		for (int i = 0; i < double_list.size(); i++) {
			String field_name1 = double_list.get(i).getFieldName();
			String field_name = double_list.get(i).getMapping();
			String data_type = double_list.get(i).getDataType();

			String lower_case = field_name.toLowerCase();
			if (data_type.equals("double")) {

				double_field_for_grid.append("\n<th class=\"center\">" + field_name1 + "</th>");
				double_field_for_grid_value
						.append("\n<td class=\"center\">${" + table_name_lower + "." + lower_case + "}</td>");

			}

		}
		for (int i = 0; i < rn_userlist.size(); i++) {
			String field_name1 = rn_userlist.get(i).getFieldName();
			String field_name = rn_userlist.get(i).getMapping();
			String field_name_lower = field_name.toLowerCase();

			table_field.append("\n<th class=\"center\">" + field_name1 + "</th>");

			table_field_value
					.append("\n<td class=\"center\">${" + table_name_lower + "." + field_name_lower + "}</td>");

		}

		table_grid_view.append(" \n<%@page contentType=\"text/html\" pageEncoding=\"UTF-8\"%>"
				+ "\n<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">"
				+ "\n<%@ taglib uri=\"http://java.sun.com/jsp/jstl/core\" prefix=\"c\" %>"
				+ "\n<%@ taglib prefix=\"form\" uri=\"http://www.springframework.org/tags/form\"%>" + "\n<html>"
				+ "\n<head>" + "\n<meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge,chrome=1\" />"
				+ "\n<meta charset=\"utf-8\"/>" + "\n<title>Realnet Oil Engines Ltd</title>"
				+ "\n<meta name=\"description\" content=\"Static &amp; Dynamic Tables\" />"
				+ "\n<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0, maximum-scale=1.0\"/>"
				+ "\n<!-- bootstrap & fontawesome -->"
				+ "\n<link rel=\"stylesheet\" href=\"resources/assets/css/bootstrap.min.css\" />"
				+ "\n<link rel=\"stylesheet\" href=\"resources/assets/font-awesome/4.2.0/css/font-awesome.min.css\" />"
				+ "\n<!-- page specific plugin styles -->" + "\n<!-- text fonts -->"
				+ "\n<link rel=\"stylesheet\" href=\"resources/assets/fonts/fonts.googleapis.com.css\"/>"
				+ "\n<!-- ace styles -->"
				+ "\n<link rel=\"stylesheet\" href=\"resources/assets/css/ace.min.css\" class=\"ace-main-stylesheet\" id=\"main-ace-style\" />"

				+ "\n</head>" + "\n<body class=\"no-skin\">" + "\n<div class=\"main-container\" id=\"main-container\">"
				+ "\n<div class=\"main-content\">" + "\n<div class=\"main-content-inner\">"
				+ "\n<div class=\"breadcrumbs\" id=\"breadcrumbs\">" + "\n<script type=\"text/javascript\">"
				+ "\ntry{ace.settings.check('breadcrumbs' , 'fixed')}catch(e){}" + "\n</script>"
				+ "\n<ul class=\"breadcrumb\">" + "\n<li>" + "\n<i class=\"ace-icon fa fa-home home-icon\"></i>"
				+ "\n<a href=\"#\">Home</a>" + "\n</li>"

				+ "\n<li class=\"active\"> User Details</li>" 
				+ "\n</ul>"
				+ " \n </div>"
				+ "\n<div class=\"page-content\">" 
				+ "\n <div class=\"page-header\">" 
				+ "\n<h1>" + "\n" + ui_name + ""
				+ "\n<div style=\"float: right; padding-right: 3%;\">" 
				+ "\n<a href=\"" + table_name_lower + "_entryform\"> <i class=\"fa fa-plus fa-1g\" aria-hidden=\"true\" title=\"Add New Report\"></i></a>"
				+ "<a href=\"rn_help_menu?form_code=" + form_code + "\"><i\r\n" + 
				"									class=\"fa fa-question-circle fa-1g\" title=\"Help Menu\"></i></a>"
				+ "\n</div>" 
				+ "\n</h1>" + "\n</div><!-- /.page-header -->"

				+ "\n<div class=\"row\">" + "\n<div class=\"col-xs-12\">" + "\n<div class=\"row\">"
				+ "\n<div class=\"col-xs-12\">" + "\n<div class=\"clearfix\">"
				+ "\n<div class=\"pull-right tableTools-container\"></div>" + "\n</div>"
				+ "\n<div class=\"table-header\">" + "\n" + ui_name + "" + "\n</div>" + "\n<div>"

				+ "\n<table   class=\"table table-striped table-bordered table-hover\" id=\"table1\"  cellspacing=\"0\" width=\"1500px\" style=\"width:100%; margin: 0 auto;\">"
				+ "\n<thead>" + "\n<tr>" + table_field_for_id + id_not_pri_field_for_grid + date_field_for_grid
				+ table_field + longtext_field_for_grid + double_field_for_grid
				+ "\n<%@include file=\"/WEB-INF/tiles/acemaster/" + module_name + "/" + table_name_lower
				+ "_add_grid.jsp\"%>" + "\n</tr>" + "\n</thead>" + "\n<tbody>" + "\n<c:forEach var=\""
				+ table_name_lower + "\" items=\"${" + table_name_lower + "}\" varStatus=\"status\">" + "\n<tr>	"
				+ table_field_value_for_id + id_not_pri_field_for_grid_value + date_field_for_grid_value
				+ table_field_value + longtext_field_for_grid_value + double_field_for_grid_value

				+ "<%@include file=\"/WEB-INF/tiles/acemaster/" + module_name + "/" + table_name_lower
				+ "_add_grid2.jsp\"%>\n</tr>" + "\n</c:forEach>" + "\n</tbody>" + " \n</table>"

				+ "\n</div>" + "\n</div>" + "\n</div>" + "\n</div>" + "\n</div>" + "\n</div>" + "\n</div>" + "\n</div>"

				+ "\n<a href=\"#\" id=\"btn-scroll-up\" class=\"btn-scroll-up btn btn-sm btn-inverse\">"
				+ "\n<i class=\"ace-icon fa fa-angle-double-up icon-only bigger-110\"></i>" + "\n</a>" + "\n</div>"
				+ "\n<script src=\"resources/assets/js/jquery.2.1.1.min.js\"></script>"

				+ "\n<script src=\"resources/assets/js/bootstrap.min.js\"></script>"
				+ "\n<script src=\"resources/assets/js/jquery.dataTables.min.js\"></script>"
				+ "\n<script src=\"resources/assets/js/jquery.dataTables.bootstrap.min.js\"></script>"
				+ "\n<script src=\"resources/assets/js/dataTables.tableTools.min.js\"></script>"
				+ "\n<script src=\"resources/assets/js/dataTables.colVis.min.js\"></script>"
				+ "\n<script src=\"resources/assets/js/ace-elements.min.js\"></script>"
				+ "\n<script src=\"resources/assets/js/ace.min.js\"></script>"

				+ "\n<script type=\"text/javascript\">"

				+ "\n$(document).ready(function() " + "\n{" + "\n$('#table1').DataTable( {" + "\n\"scrollX\": true"

				+ "\n} );" + "\n} );" + "\njQuery(function($)" + "\n{" + "\nvar oTable1 = " + "\n$('#dynamic-table')"
				+ "\n.dataTable( {" + "\nbAutoWidth: false," + "\n\"aoColumns\": [" + "\n{ \"bSortable\": false },"
				+ "\n null, null,null, null, null," + "\n{ \"bSortable\": false }" + "\n]," + "\n\"aaSorting\": [],"

				+ "\n} );" + "\n})" + "\n</script>" + "\n</body>" + "\n</html>" + "by ganesh bute");

		
		
// ------------------------------------ jsp readonly---------------------------------------------------------
		
		importsectionreadonly.append("\n<%@ page language=\"java\" contentType=\"text/html; charset=UTF-8\" pageEncoding=\"UTF-8\"%>"
						+ "\n<%@ page import=\"java.util.ArrayList\"%>" + "\n<%@ page import=\"java.util.Date\"%>"
						+ "\n<%@ taglib prefix=\"c\" uri=\"http://java.sun.com/jsp/jstl/core\"%>"
						+ "\n<%@ taglib uri=\"http://java.sun.com/jsp/jstl/functions\" prefix=\"fn\"%>"
						+ "\n<%@ taglib uri=\"http://java.sun.com/jsp/jstl/fmt\" prefix=\"fmt\"%>");

		headsectionreadonly.append("\n<html lang=\"en\">\n<head>"
				+ "\n<meta http-equiv=\"X-UA-Compatible\"  content=\"IE=edge,chrome=1\">"
				+ "\n<meta charset=\"utf-8\" />" + "\n<title>Realnet Oil Engines Ltd</title>"
				+ "\n<meta name=\"description\" content=\"Common form elements and layouts\" />"
				+ "\n<meta name=\"viewport\""
				+ "\ncontent=\"width=device-width, initial-scale=1.0, maximum-scale=1.0\" />"
				+ "\n<!-- bootstrap & fontawesome -->" + "\n<link rel=\"stylesheet\""
				+ "\n href=\"<c:url value='/resources/assets/css/bootstrap.min.css'/>\" />"
				+ "\n<link rel=\"stylesheet\""
				+ "\nhref=\"<c:url value='/resources/assets/font-awesome/4.2.0/css/font-awesome.min.css'/>\" />"
				+ "\n<!-- page specific plugin styles -->" + "\n<link rel=\"stylesheet\""
				+ "\nhref=\"<c:url value='/resources/assets/css/jquery-ui.custom.min.css' />\" />"
				+ "\n<link rel=\"stylesheet\"" + "\nhref=\"<c:url value='/resources/assets/css/chosen.min.css' />\" />"
				+ "\n<link rel=\"stylesheet\""
				+ "\nhref=\"<c:url value='/resources/assets/css/datepicker.min.css'/>\" />"
				+ "\n<link rel=\"stylesheet\""
				+ "\nhref=\"<c:url value='/resources/assets/css/bootstrap-timepicker.min.css'/>\" />"
				+ "\n<link rel=\"stylesheet\""
				+ "\nhref=\"<c:url value='/resources/assets/css/daterangepicker.min.css' />\" />"
				+ "\n<link rel=\"stylesheet\""
				+ "\nhref=\"<c:url value='/resources/assets/css/bootstrap-datetimepicker.min.css'/>\" />"
				+ "\n<link rel=\"stylesheet\""
				+ "\nhref=\"<c:url value='/resources/assets/css/colorpicker.min.css' />\" />" + "\n<!-- text fonts -->"
				+ "\n<link rel=\"stylesheet\""
				+ "\n\nhref=\"<c:url value='/resources/assets/fonts/fonts.googleapis.com.css' />\" />"
				+ "\n<!-- ace styles -->" + "\n<link rel=\"stylesheet\""
				+ "\nhref=\"<c:url value='/resources/assets/css/ace.min.css\" class=\"ace-main-stylesheet\" id=\"main-ace-style' />\" />"
				+ "\n<!-- inline styles related to this page -->"

				+ "\n<!-- ace settings handler -->"
				+ "\n<script src=\"<c:url value='/resources/assets/js/ace-extra.min.js'/>\""
				+ "\ntype=\"text/javascript\"></script>"

				+ "\n<script>" + "\nsubmitForms = function()" + "\n{" + "\ndocument.forms[\"userdetails1\"].submit();"
				+ "\ndocument.forms[\"userdetails2\"].submit();" + "}" + "\n</script> "

				+ "\n\n<!-- MultiSelect DropDown Scripts  -->"
				+ "\n<script src=\"https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js\"></script>"
				+ "\n<script src=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.4.0/js/bootstrap.min.js\"></script>"
				+ " \n<script src=\"multiselect.min.js\"></script>"
				+ "   \n <script src=\"http://ajax.aspnetcdn.com/ajax/modernizr/modernizr-2.8.3.js\"></script>"
				+ "    \n<script src=\"http://code.jquery.com/jquery-1.11.3.min.js\"></script>"
				+ "    \n<script src=\"http://code.jquery.com/ui/1.11.1/jquery-ui.min.js\"></script>"
				+ "    \n<script src=\"http://cdn-na.infragistics.com/igniteui/2019.2/latest/js/infragistics.core.js\"></script>"
				+ "   \n <script src=\"http://cdn-na.infragistics.com/igniteui/2019.2/latest/js/infragistics.lob.js\"></script>"
				+ " " + " " + "<!-- Ignite UI Required Combined CSS Files -->"
				+ "   \n<link href=\"http://cdn-na.infragistics.com/igniteui/2019.2/latest/css/themes/infragistics/infragistics.theme.css\" rel=\"stylesheet\" />"
				+ "   \n<link href=\"http://cdn-na.infragistics.com/igniteui/2019.2/latest/css/structure/infragistics.css\" rel=\"stylesheet\" /> "
				+ " \n</head>");

		for (int i21 = 0; i21 < section_values.size(); i21++) 
		{
			String field_nameS = section_values.get(i21).getFieldName();
			int sectionNum = section_values.get(i21).getSection_num();

			int secTemp = sectionNum;

			int section_num1 = sectionNum;

			int count = 1;

			System.out.println("count Value in section_values loop :: " + count);

			System.out.println("secTemp Value in section_values loop :: " + section_num1);
			//List<Rn_Fb_Line> sectionNumFields = rn_wireframe_dao.wf_section_num_fields(f_code, section_num1);
			List<Rn_Fb_Line> sectionNumFields = wireFrameService.getSectionFields(section_num1, id);

			System.out.println("secTemp Value in section_values loop :: " + secTemp);

			if (sectionNum == 1) {
				form_readonly.append(
						"\n<form action=\"" + action2 + "\" class=\"form-horizontal\" id=\"Regi\" method=\"Post\">");

			}

			form_readonly.append("\n<div class=\"table-header\" style=\"margin-bottom: 30px; margin-top: 30px;\"> "
					+ field_nameS + "</div>");

			form_readonly.append(" \n<table>" + "\n<c:forEach var=\"" + table_name_lower + "_update\" items=\"${"
					+ table_name_lower + "_update}\" varStatus=\"status\">");
			form_readonly.append("\n<input type=\"hidden\" name=\"" + field_name_for_id + "\" id=\"" + field_name_for_id
					+ "\" value=\"${" + table_name_lower + "_update.id}\" />" + "\n<tr>");

			form_readonly.append(
					"\n<tr>" + "\n<td>" + "\n<div class=\"form-group\" style=\"margin-left: 10%; margin-right: 10%;\">"
							+ "\n<label class=\"control-label col-xs-12 col-sm-3 no-padding-right\">" + "\n<a>Edit</a>"
							+ "\n</label>" + "\n</div>" + "\n</td>" + "\n</tr>");

			for (int i = 0; i < id_notprimary.size(); i++) {
				String field_name1 = id_notprimary.get(i).getFieldName();
				String field_name = id_notprimary.get(i).getMapping();
				String data_type = id_notprimary.get(i).getDataType();
				String lower_case = field_name.toLowerCase();
				int fieldNum = id_notprimary.get(i).getSection_num();

				if (data_type.equals("int") && sectionNum == fieldNum) {
					form_readonly.append(
							"\n<td>" + "\n<div class=\"form-group\" style=\"margin-left: 10%; margin-right: 10%;\">"
									+ "\n<label class=\"control-label col-xs-12 col-sm-3 no-padding-right\">"
									+ field_name1 + "\n<i class=\"menu-icon fa red\"> *</i>" + "\n</label>"
									+ "\n<div class=\"col-xs-12 col-sm-9\">" + "\n<div class=\"clearfix\">"
									+ "\n<input   value=\"${" + table_name_lower + "_update." + lower_case
									+ "}\"type=\"text\" name=\"" + lower_case + "\"id=\"" + lower_case
									+ "\"class=\"col-xs-12 col-sm-4\" readonly/>" + "\n</div>" + "\n</div>" + "\n</div>"
									+ "\n</td>");
				}

			}
			form_readonly.append("\n</tr>");

			form_readonly.append("\n<tr>");

			for (int i = 0; i < datetime_list.size(); i++) {

				String field_name = datetime_list.get(i).getFieldName();
				String data_type = datetime_list.get(i).getDataType();
				int fieldNum = datetime_list.get(i).getSection_num();

//				String mandatory = datetime_list.get(i).getMandatory();
				boolean mandatory = datetime_list.get(i).isMandatory();
				String mapping = datetime_list.get(i).getMapping();
				String mapping_lower = mapping.toLowerCase();

				System.out.println("-----value of mantadoryr laest-----" + mandatory);

				if (data_type.equals("datetime") && sectionNum == fieldNum) {

					System.out.println("---under if condition loop latest------");

					form_readonly.append(
							"\n<td>" + "\n<div class=\"form-group\" style=\"margin-left: 10%; margin-right: 10%;\">"
									+ "\n<label class=\"control-label col-xs-12 col-sm-3 no-padding-right\">" + "\n "
									+ field_name);

					System.out.println("-------------mandatory for testing=------------------" + mandatory);

					if (mandatory == true) {
						System.out.println("-------------in loop 1-------------------");
						form_readonly.append("\n <i class=\"menu-icon fa red\"> *</i>");
					}

					form_readonly
							.append("\n</label>" + "\n<div class=\"col-xs-12 col-sm-9\">" + "\n<div class=\"clearfix\">"
									+ "<div class=\"input-group input-append date\" id=\"datePicker\">");

					form_readonly.append("\n<input");

					form_readonly.append("  type=\"text\" ");

					form_readonly.append("name=" + mapping_lower + "  value=\"${" + table_name_lower + "_update."
							+ mapping_lower + "}\"  id=" + mapping_lower
							+ " placeholder=\"picup Date\" class=\"form-control\"");

					form_readonly.append("  readonly");

					form_readonly.append(
							"/>\n" + "\n<span class=\"input-group-addon\"> <i class=\"fa fa-calendar bigger-110\"></i>"
									+ "\n</span>" + "\n</div>" + "\n</div>" + "\n</div>" + "\n</div>" + "\n</td>");
				}

			}

			form_readonly.append("\n</tr>");

			form_readonly.append("\n<tr>");

			for (int i = 0; i < sectionNumFields.size(); i++) {
				String field_name4 = sectionNumFields.get(i).getFieldName();
				String mapping4 = sectionNumFields.get(i).getMapping();
				String data_type4 = sectionNumFields.get(i).getDataType();
				int fieldNum = sectionNumFields.get(i).getSection_num();

				System.out.println("field name-------" + field_name4);
				System.out.println("mapping-------" + mapping4);
				System.out.println("data type-------" + data_type4);

				System.out.println("field name-------" + field_name4);
				System.out.println("mapping-------" + mapping4);
				System.out.println("data type-------" + data_type4);

				System.out.println("field name-------" + field_name4);
				System.out.println("mapping-------" + mapping4);
				System.out.println("data type-------" + data_type4);

//				String mandatory = sectionNumFields.get(i).getMandatory();
//				String hidden = sectionNumFields.get(i).getHidden();
				boolean mandatory = sectionNumFields.get(i).isMandatory();
				boolean hidden = sectionNumFields.get(i).isHidden();

				// String mapping=rn_userlist.get(i).getMapping();

				String type_field = sectionNumFields.get(i).getType_field();

				if (i <= 2) {
					if (type_field.equals("textfield") && sectionNum == fieldNum) {
						form_readonly.append(
								"\n<td>" + "\n<div class=\"form-group\" style=\"margin-left: 10%; margin-right: 10%;\">"
										+ "\n<label class=\"control-label col-xs-12 col-sm-3 no-padding-right\">"
										+ "\n " + field_name4);

						System.out.println("-------------mandatory for testing=------------------" + mandatory);

						if (mandatory == true) {
							System.out.println("-------------in loop 1-------------------");
							form_readonly.append("\n <i class=\"menu-icon fa red\"> *</i>");
						}

						form_readonly.append(
								"\n</label>" + "\n<div class=\"col-xs-12 col-sm-9\">" + "\n<div class=\"clearfix\">");
						form_readonly.append("\n<input ");

						if (hidden == true) {
							form_readonly.append("  type=\"hidden\" ");
						} else {
							form_readonly.append(
									"  value=\"${" + table_name_lower + "_update." + mapping4 + "}\" type=\"text\" ");
						}
						
						form_readonly.append("name=" + mapping4 + " id=" + mapping4 + "  class=\"form-control\"");

						if (mandatory == true) {
							System.out.println("-------------in loop 1 part 2 required-------------------");
							form_readonly.append("required");
						}

						System.out.println("-------------in loop 1 part 2 required-------------------");
						form_readonly.append("  readonly");

						form_readonly.append("/>\n</div>" + "\n</div>" + "\n</div>" + "\n</td>");

						readonly_script.append("\n<script>" + "\ndocument.getElementById(\'" + mapping4
								+ "\').onclick = function() {" + "  \n  document.getElementById(\'" + mapping4
								+ "\').removeAttribute('readonly');" + "\n};" + "\n</script>");
					}

					if (type_field.equals("url") && sectionNum == fieldNum) {
						form_readonly.append(
								"\n<td>" + "\n<div class=\"form-group\" style=\"margin-left: 10%; margin-right: 10%;\">"
										+ "\n<label class=\"control-label col-xs-12 col-sm-3 no-padding-right\">"
										+ "\n " + field_name4);

						System.out.println("-------------mandatory for testing=------------------" + mandatory);

						if (mandatory == true) {
							System.out.println("-------------in loop 1-------------------");
							form_readonly.append("\n <i class=\"menu-icon fa red\"> *</i>");
						}

						form_readonly.append(
								"\n</label>" + "\n<div class=\"col-xs-12 col-sm-9\">" + "\n<div class=\"clearfix\">");
						form_readonly.append("\n<input   placeholder=\"URL\"pattern=\"https://.*\" size=\"30\" ");

						if (hidden == true) {
							form_readonly.append("  type=\"hidden\" ");
						} else {
							form_readonly.append(
									"  value=\"${" + table_name_lower + "_update." + mapping4 + "}\" type=\"text\" ");
						}
						/*
						 * if(calculated_field.equals("Y")) { form_readonly.append(
						 * "name=\"total\" id=\"total\" class=\"stunden form-control\" "); } else {
						 * form_readonly.append( "name="+mapping_lower_case+" id=\""
						 * +mapping_lower_case+"\" class=\"col-xs-12 col-sm-4\" readonly"); }
						 */
						form_readonly.append("name=" + mapping4 + " id=" + mapping4 + "  class=\"form-control\"");

						if (mandatory == true) {
							System.out.println("-------------in loop 1 part 2 required-------------------");
							form_readonly.append("required");
						}

						System.out.println("-------------in loop 1 part 2 required-------------------");
						form_readonly.append("  readonly");

						form_readonly.append("/>\n</div>" + "\n</div>" + "\n</div>" + "\n</td>");
					}

					if (type_field.equals("email") && sectionNum == fieldNum) {
						form_readonly.append(
								"\n<td>" + "\n<div class=\"form-group\" style=\"margin-left: 10%; margin-right: 10%;\">"
										+ "\n<label class=\"control-label col-xs-12 col-sm-3 no-padding-right\">"
										+ "\n " + field_name4);

						System.out.println("-------------mandatory for testing=------------------" + mandatory);

						if (mandatory == true) {
							System.out.println("-------------in loop 1-------------------");
							form_readonly.append("\n <i class=\"menu-icon fa red\"> *</i>");
						}

						form_readonly.append(
								"\n</label>" + "\n<div class=\"col-xs-12 col-sm-9\">" + "\n<div class=\"clearfix\">");
						form_readonly.append("\n<input    placeholder=\"E_Mail\" onblur=\"validateEmail(this);\"");

						if (hidden == true) {
							form_readonly.append("  type=\"hidden\" ");
						} else {
							form_readonly.append(
									"  value=\"${" + table_name_lower + "_update." + mapping4 + "}\" type=\"email\" ");
						}
						/*
						 * if(calculated_field.equals("Y")) { form_readonly.append(
						 * "name=\"total\" id=\"total\" class=\"stunden form-control\" "); } else {
						 * form_readonly.append( "name="+mapping_lower_case+" id=\""
						 * +mapping_lower_case+"\" class=\"col-xs-12 col-sm-4\" readonly"); }
						 */
						form_readonly.append("name=" + mapping4 + " id=" + mapping4 + "  class=\"form-control\"");

						if (mandatory == true) {
							System.out.println("-------------in loop 1 part 2 required-------------------");
							form_readonly.append("required");
						}

						System.out.println("-------------in loop 1 part 2 required-------------------");
						form_readonly.append("  readonly");

						form_readonly.append("/>\n</div>" + "\n</div>" + "\n</div>" + "\n</td>");
					}

					if (type_field.equals("masked") && sectionNum == fieldNum) {
						form_readonly.append(
								"\n<td>" + "\n<div class=\"form-group\" style=\"margin-left: 10%; margin-right: 10%;\">"
										+ "\n<label class=\"control-label col-xs-12 col-sm-3 no-padding-right\">"
										+ "\n " + field_name4);

						System.out.println("-------------mandatory for testing=------------------" + mandatory);

						if (mandatory == true) {
							System.out.println("-------------in loop 1-------------------");
							form_readonly.append("\n <i class=\"menu-icon fa red\"> *</i>");
						}

						form_readonly.append(
								"\n</label>" + "\n<div class=\"col-xs-12 col-sm-9\">" + "\n<div class=\"clearfix\">");
						form_readonly.append("\n<input   placeholder=\"Masked\" maxlength=\"8\"");

						if (hidden == true) {
							form_readonly.append("  type=\"hidden\" ");
						} else {
							form_readonly.append(
									"  value=\"${" + table_name_lower + "_update." + mapping4 + "}\" type=\"password\" ");
						}
						/*
						 * if(calculated_field.equals("Y")) { form_readonly.append(
						 * "name=\"total\" id=\"total\" class=\"stunden form-control\" "); } else {
						 * form_readonly.append( "name="+mapping_lower_case+" id=\""
						 * +mapping_lower_case+"\" class=\"col-xs-12 col-sm-4\" readonly"); }
						 */
						form_readonly.append("name=" + mapping4 + " id=" + mapping4 + "  class=\"form-control\"");

						if (mandatory == true) {
							System.out.println("-------------in loop 1 part 2 required-------------------");
							form_readonly.append("required");
						}

						System.out.println("-------------in loop 1 part 2 required-------------------");
						form_readonly.append("  readonly");

						form_readonly.append("/>\n</div>" + "\n</div>" + "\n</div>" + "\n</td>");
					}

					if (type_field.equals("currency_field") && sectionNum == fieldNum) {
						form_readonly.append(
								"\n<td>" + "\n<div class=\"form-group\" style=\"margin-left: 10%; margin-right: 10%;\">"
										+ "\n<label class=\"control-label col-xs-12 col-sm-3 no-padding-right\">"
										+ "\n " + field_name4);

						System.out.println("-------------mandatory for testing=------------------" + mandatory);

						if (mandatory == true) {
							System.out.println("-------------in loop 1-------------------");
							form_readonly.append("\n <i class=\"menu-icon fa red\"> *</i>");
						}

						form_readonly.append(
								"\n</label>" + "\n<div class=\"col-xs-12 col-sm-9\">" + "\n<div class=\"clearfix\">");
						form_readonly.append("\n<input   placeholder=\"$1,000,000.00\" onblur=\"handleChange()\"");

						if (hidden == true) {
							form_readonly.append("  type=\"hidden\" ");
						} else {
							form_readonly.append(
									"  value=\"${" + table_name_lower + "_update." + mapping4 + "}\" type=\"text\" ");
						}
						/*
						 * if(calculated_field.equals("Y")) { form_readonly.append(
						 * "name=\"total\" id=\"total\" class=\"stunden form-control\" "); } else {
						 * form_readonly.append( "name="+mapping_lower_case+" id=\""
						 * +mapping_lower_case+"\" class=\"col-xs-12 col-sm-4\" readonly"); }
						 */
						form_readonly.append("name=" + mapping4 + " id=" + mapping4 + "  class=\"form-control\"");

						if (mandatory == true) {
							System.out.println("-------------in loop 1 part 2 required-------------------");
							form_readonly.append("required");
						}

						System.out.println("-------------in loop 1 part 2 required-------------------");
						form_readonly.append("  readonly");

						form_readonly.append("/>\n</div>" + "\n</div>" + "\n</div>" + "\n</td>");
					}

					if (type_field.equals("contact_field") && sectionNum == fieldNum) {
						form_readonly.append(
								"\n<td>" + "\n<div class=\"form-group\" style=\"margin-left: 10%; margin-right: 10%;\">"
										+ "\n<label class=\"control-label col-xs-12 col-sm-3 no-padding-right\">"
										+ "\n " + field_name4);

						System.out.println("-------------mandatory for testing=------------------" + mandatory);

						if (mandatory == true) {
							System.out.println("-------------in loop 1-------------------");
							form_readonly.append("\n <i class=\"menu-icon fa red\"> *</i>");
						}

						form_readonly.append("\n</label>" + "\n<div class=\"col-xs-12 col-sm-9\">"
								+ "\n<div class=\"clearfix\">"

								+ "\n<div style=\"position:relative\">" + "\n<select style=\"   height:\";\" >"
								+ "\n<option>+91</option>" + "\n<option>+44</option>" + "\n<option>+64</option>"
								+ "\n<option>+21</option>" + "\n<option>+244</option>" + "\n<option>+376</option>"
								+ "\n<option>+354</option>" + "\n<option>+291</option>" + "\n<option>+54</option>"
								+ "\n<option>+264</option>" + "\n<option>+121</option>" + "\n</select>");

						form_readonly.append("\n<input    pattern=\"[0-9]{10}\"");

						if (hidden == true) {
							form_readonly.append("  type=\"hidden\" ");
						} else {
							form_readonly.append(
									"  value=\"${" + table_name_lower + "_update." + mapping4 + "}\" type=\"tel\" ");
						}
						/*
						 * if(calculated_field.equals("Y")) { form_readonly.append(
						 * "name=\"total\" id=\"total\" class=\"stunden form-control\" "); } else {
						 * form_readonly.append( "name="+mapping_lower_case+" id=\""
						 * +mapping_lower_case+"\" class=\"col-xs-12 col-sm-4\" readonly"); }
						 */
						form_readonly.append("name=" + mapping4 + " id=" + mapping4 + "  class=\"form-control\"");

						if (mandatory == true) {
							System.out.println("-------------in loop 1 part 2 required-------------------");
							form_readonly.append("required");
						}

						System.out.println("-------------in loop 1 part 2 required-------------------");
						form_readonly.append("  readonly");

						form_readonly.append("/>\n</div>" + "\n</div>" + "\n</div>" + "\n</div>" + "\n</td>");
					}

					if (type_field.equals("upload_field") && sectionNum == fieldNum) {
						form_readonly.append(
								"\n<td>" + "\n<div class=\"form-group\" style=\"margin-left: 10%; margin-right: 10%;\">"
										+ "\n<label class=\"control-label col-xs-12 col-sm-3 no-padding-right\">"
										+ "\n " + field_name4);

						System.out.println("-------------mandatory for testing=------------------" + mandatory);

						if (mandatory == true) {
							System.out.println("-------------in loop 1-------------------");
							form_readonly.append("\n <i class=\"menu-icon fa red\"> *</i>");
						}

						form_readonly.append(
								"\n</label>" + "\n<div class=\"col-xs-12 col-sm-9\">" + "\n<div class=\"clearfix\">");

						/* + "\n<div class=\"inputWithIcon\">"); */

						form_readonly.append("\n<input "
								+ "\"   placeholder=\"Upload File\" style=\"width:100%; padding-left:10px;\"");
						if (hidden == true) {
							form_readonly.append("  type=\"hidden\" ");
						} else {
							form_readonly.append(
									"  value=\"${" + table_name_lower + "_update." + mapping4 + "}\" type=\"file\" ");
						}
						
						form_readonly.append("name=" + mapping4 + " id=" + mapping4 + "  class=\"form-control\"");

						if (mandatory == true) {
							System.out.println("-------------in loop 1 part 2 required-------------------");
							form_readonly.append("required");
						}

						System.out.println("-------------in loop 1 part 2 required-------------------");
						form_readonly.append("  readonly");

						form_readonly.append("/>\n" +

								" <c:out value=\"${" + table_name_lower + "_update." + mapping4 + "}\"/>"

								
								+ "\n</div>" + "\n</div>" + "\n</div>" + "\n</td>");
					}

					if (type_field.equals("textarea") && sectionNum == fieldNum) {
						form_readonly.append(
								"\n<td>" + "\n<div class=\"form-group\" style=\"margin-left: 10%; margin-right: 10%;\">"
										+ "\n<label class=\"control-label col-xs-12 col-sm-3 no-padding-right\">"
										+ "\n " + field_name4);

						System.out.println("-------------mandatory for testing=------------------" + mandatory);

						if (mandatory == true) {
							System.out.println("-------------in loop 1-------------------");
							form_readonly.append("\n <i class=\"menu-icon fa red\"> *</i>");
						}

						form_readonly.append(
								"\n</label>" + "\n<div class=\"col-xs-12 col-sm-9\">" + "\n<div class=\"clearfix\">");
						form_readonly.append("\n<textarea rows=\"" + 0 + "\" cols=\"" + 34 + "\" ");

						if (hidden == true) {
							form_readonly.append("  type=\"hidden\" ");
						} else {
							form_readonly.append(
									"  value=\"${" + table_name_lower + "_update." + mapping4 + "}\" type=\"text\" ");
						}
						

						form_readonly.append("name=" + mapping4 + " id=" + mapping4 + "  class=\"form-control\"");

						if (mandatory == true) {
							System.out.println("-------------in loop 1 part 2 required-------------------");
							form_readonly.append("required");
						}

						System.out.println("-------------in loop 1 part 2 required-------------------");
						form_readonly.append("  readonly");

						form_readonly.append("/>${" + table_name_lower + "_update." + mapping4 + "}</textarea>\n</div>"
								+ "\n</div>" + "\n</div>" + "\n</td>");
					}

					if (type_field.equals("datetime") && sectionNum == fieldNum) {
						form_readonly.append(
								"\n<td>" + "\n<div class=\"form-group\" style=\"margin-left: 10%; margin-right: 10%;\">"
										+ "\n<label class=\"control-label col-xs-12 col-sm-3 no-padding-right\">"
										+ "\n " + field_name4);

						System.out.println("-------------mandatory for testing=------------------" + mandatory);

						if (mandatory == true) {
							System.out.println("-------------in loop 1-------------------");
							form_readonly.append("\n <i class=\"menu-icon fa red\"> *</i>");
						}

						form_readonly.append(
								"\n</label>" + "\n<div class=\"col-xs-12 col-sm-9\">" + "\n<div class=\"clearfix\">"
										+ "<div class=\"input-group input-append date\" id=\"datePicker1\">");

						form_readonly.append("\n<input  ");

						if (hidden == true) {
							form_readonly.append("  type=\"hidden\" ");
						} else {
							form_readonly.append(
									"   value=\"${" + table_name_lower + "_update." + mapping4 + "}\"  type=\"text\" ");
						}

						form_readonly.append("name=" + mapping4 + " id=" + mapping4
								+ " placeholder=\"picup Date\" class=\"form-control\"");

						if (mandatory == true) {
							System.out.println("-------------in loop 1 part 2 required-------------------");
							form_prefield.append("required");
						}

						form_readonly.append("  readonly");

						form_readonly.append("/>\n"
								+ "\n<span class=\"input-group-addon\"> <i class=\"fa fa-calendar bigger-110\"></i>"
								+ "\n</span>" + "\n</div>" + "\n</div>" + "\n</div>" + "\n</div>" + "\n</td>");
					}

					if (type_field.equals("autocomplete") && sectionNum == fieldNum) {
						form_readonly.append(
								"\n<td>" + "\n<div class=\"form-group\" style=\"margin-left: 10%; margin-right: 10%;\">"
										+ "\n<label class=\"control-label col-xs-12 col-sm-3 no-padding-right\">"
										+ "\n " + field_name4);

						System.out.println("-------------mandatory for testing=------------------" + mandatory);

						if (mandatory == true) {
							System.out.println("-------------in loop 1-------------------");
							form_readonly.append("\n <i class=\"menu-icon fa red\"> *</i>");
						}

						form_readonly.append(
								"\n</label>" + "\n<div class=\"col-xs-12 col-sm-9\">" + "\n<div class=\"clearfix\">");

						form_readonly.append("\n<input   placeholder=\"AutoComplete\"");

						if (hidden == true) {
							form_readonly.append(
									" value=\"${" + table_name_lower + "_update." + mapping4 + "}\"  type=\"hidden\" ");
						} else {
							form_readonly.append(
									"  value=\"${" + table_name_lower + "_update." + mapping4 + "}\" type=\"text\" ");
						}
						

						form_readonly.append("name=" + mapping4 + " id=" + mapping4 + "  class=\"form-control\"");
						if (mandatory == true) {
							System.out.println("-------------in loop 1 part 2 required-------------------");
							form.append("required");
						}

						form_readonly.append("  readonly");

						form_readonly.append("/>\n</div>" + "\n</div>" + "\n</div>" + "\n</td>");

					}

					if (type_field.equals("dropdown") && sectionNum == fieldNum) {
						form_readonly.append(
								"\n<td>" + "\n<div class=\"form-group\" style=\"margin-left: 10%; margin-right: 10%;\">"
										+ "\n<label class=\"control-label col-xs-12 col-sm-3 no-padding-right\">"
										+ "\n " + field_name4);

						if (mandatory == true) {
							form_readonly.append("\n <i class=\"menu-icon fa red\"> *</i>");
						}

						form_readonly.append("\n</label>" + "\n<div class=\"col-xs-12 col-sm-9\">");

						form_readonly.append("\n<input name=\"" + mapping4 + "\" id=\"" + mapping4 + "\"  value=\"${"
								+ table_name_lower + "_update." + mapping4
								+ "}\"  class=\"col-xs-3 col-sm-3 form-control state\""

								+ "\n");

						if (mandatory == true) {
							System.out.println("-------------in loop 1 part 2 required-------------------");
							form_readonly.append("required");
						}

						form_readonly.append("  readonly");
						form_readonly.append("\n/></div>"

								+ "\n</div>" + "\n</td>");
					}

					if (type_field.equals("togglebutton") && sectionNum == fieldNum) {
						form_readonly.append(
								"\n<td>" + "\n<div class=\"form-group\" style=\"margin-left: 10%; margin-right: 10%;\">"
										+ "\n<label class=\"control-label col-xs-12 col-sm-3 no-padding-right\">"
										+ "\n " + field_name4);

						System.out.println("-------------mandatory for testing=------------------" + mandatory);

						if (mandatory == true) {
							System.out.println("-------------in loop 1-------------------");
							form_readonly.append("\n <i class=\"menu-icon fa red\"> *</i>");
						}

						form_readonly.append(
								"\n</label>" + "\n<div class=\"col-xs-12 col-sm-9\">" + "\n<div class=\"clearfix\">");

						form_readonly.append("\n<input");

						if (hidden == true) {
							form_readonly.append("  type=\"hidden\" ");
						} else {
							form_readonly.append("  type=\"checkbox\" ");
						}

						form_readonly.append(
								" value=\"${" + table_name_lower + "_update." + mapping4 + "}\" name=" + mapping4 + " id="
										+ mapping4 + "  class=\"ace ace-switch ace-switch-6\" onblur=\"CheckUserStatus"
										+ mapping4 + "()\"");

						form_readonly.append("readonly />\n" + "\n<span class=\"lbl\"></span>"

								+ "\n</div>" + "\n</div>" + "\n</div>" + "\n</td>");
					}

					if (type_field.equals("checkbox") && sectionNum == fieldNum) {
						form_readonly.append(
								"\n<td>" + "\n<div class=\"form-group\" style=\"margin-left: 10%; margin-right: 10%;\">"
										+ "\n<label class=\"control-label col-xs-12 col-sm-3 no-padding-right\">"
										+ "\n " + field_name4);

						System.out.println("-------------mandatory for testing=------------------" + mandatory);

						if (mandatory == true) {
							System.out.println("-------------in loop 1-------------------");
							form_readonly.append("\n <i class=\"menu-icon fa red\"> *</i>");
						}

						form_readonly.append(
								"\n</label>" + "\n<div class=\"col-xs-12 col-sm-9\">" + "\n<div class=\"clearfix\">");

						form_readonly.append("\n<input  style=\"width:10%; margin-left:20px\" class=\"form-control\"");

						form_readonly.append("  type=\"checkbox\" ");

						form_readonly.append(" value=\"${" + table_name_lower + "_update." + mapping4 + "}\" name="
								+ mapping4 + " id=" + mapping4 + "  onblur=\"CheckUserStatus" + mapping4 + "()\" ");

						form_readonly.append("  readonly");

						form_readonly.append("/>\n" + "\n</div>" + "\n</div>" + "\n</div>" + "\n</td>");
					}
				}
			}

			form_readonly.append("\n</tr>");

			form_readonly.append("\n<tr>");
			for (int i = 0; i < sectionNumFields.size(); i++) {
				String field_name4 = sectionNumFields.get(i).getFieldName();
				String mapping4 = sectionNumFields.get(i).getMapping();
				String data_type4 = sectionNumFields.get(i).getDataType();
				int fieldNum = sectionNumFields.get(i).getSection_num();
				System.out.println("field name-------" + field_name4);
				System.out.println("mapping-------" + mapping4);
				System.out.println("data type-------" + data_type4);

				System.out.println("field name-------" + field_name4);
				System.out.println("mapping-------" + mapping4);
				System.out.println("data type-------" + data_type4);

				System.out.println("field name-------" + field_name4);
				System.out.println("mapping-------" + mapping4);
				System.out.println("data type-------" + data_type4);

//				String mandatory = sectionNumFields.get(i).getMandatory();
//				String hidden = sectionNumFields.get(i).getHidden();
				boolean mandatory = sectionNumFields.get(i).isMandatory();
				boolean hidden = sectionNumFields.get(i).isHidden();

				// String mapping=sectionNumFields.get(i).getMapping();

				String type_field = sectionNumFields.get(i).getType_field();

				if (i > 2 && i <= 5) {
					if (type_field.equals("textfield") && sectionNum == fieldNum) {
						form_readonly.append(
								"\n<td>" + "\n<div class=\"form-group\" style=\"margin-left: 10%; margin-right: 10%;\">"
										+ "\n<label class=\"control-label col-xs-12 col-sm-3 no-padding-right\">"
										+ "\n " + field_name4);

						System.out.println("-------------mandatory for testing=------------------" + mandatory);

						if (mandatory == true) {
							System.out.println("-------------in loop 1-------------------");
							form_readonly.append("\n <i class=\"menu-icon fa red\"> *</i>");
						}

						form_readonly.append(
								"\n</label>" + "\n<div class=\"col-xs-12 col-sm-9\">" + "\n<div class=\"clearfix\">");
						form_readonly.append("\n<input value=\"${" + table_name_lower + "_update." + mapping4 + "}\"");

						if (hidden == true) {
							form_readonly.append("  type=\"hidden\" ");
						} else {
							form_readonly.append(
									"  value=\"${" + table_name_lower + "_update." + mapping4 + "}\" type=\"text\" ");
						}
						
						form_readonly.append("name=" + mapping4 + " id=" + mapping4 + "  class=\"form-control\"");

						if (mandatory == true) {
							System.out.println("-------------in loop 1 part 2 required-------------------");
							form_readonly.append("required");
						}

						System.out.println("-------------in loop 1 part 2 required-------------------");
						form_readonly.append("  readonly");

						form_readonly.append("/>\n</div>" + "\n</div>" + "\n</div>" + "\n</td>");

						readonly_script.append("\n<script>" + "\ndocument.getElementById(\'" + mapping4
								+ "\').onclick = function() {" + "  \n  document.getElementById(\'" + mapping4
								+ "\').removeAttribute('readonly');" + "\n};" + "\n</script>");
					}

					if (type_field.equals("url") && sectionNum == fieldNum) {
						form_readonly.append(
								"\n<td>" + "\n<div class=\"form-group\" style=\"margin-left: 10%; margin-right: 10%;\">"
										+ "\n<label class=\"control-label col-xs-12 col-sm-3 no-padding-right\">"
										+ "\n " + field_name4);

						System.out.println("-------------mandatory for testing=------------------" + mandatory);

						if (mandatory == true) {
							System.out.println("-------------in loop 1-------------------");
							form_readonly.append("\n <i class=\"menu-icon fa red\"> *</i>");
						}

						form_readonly.append(
								"\n</label>" + "\n<div class=\"col-xs-12 col-sm-9\">" + "\n<div class=\"clearfix\">");
						form_readonly.append("\n<input   placeholder=\"URL\"pattern=\"https://.*\" size=\"30\" ");

						if (hidden == true) {
							form_readonly.append("  type=\"hidden\" ");
						} else {
							form_readonly.append(
									"  value=\"${" + table_name_lower + "_update." + mapping4 + "}\" type=\"text\" ");
						}
						
						form_readonly.append("name=" + mapping4 + " id=" + mapping4 + "  class=\"form-control\"");

						if (mandatory == true) {
							System.out.println("-------------in loop 1 part 2 required-------------------");
							form_readonly.append("required");
						}

						System.out.println("-------------in loop 1 part 2 required-------------------");
						form_readonly.append("  readonly");

						form_readonly.append("/>\n</div>" + "\n</div>" + "\n</div>" + "\n</td>");
					}

					if (type_field.equals("email") && sectionNum == fieldNum) {
						form_readonly.append(
								"\n<td>" + "\n<div class=\"form-group\" style=\"margin-left: 10%; margin-right: 10%;\">"
										+ "\n<label class=\"control-label col-xs-12 col-sm-3 no-padding-right\">"
										+ "\n " + field_name4);

						System.out.println("-------------mandatory for testing=------------------" + mandatory);

						if (mandatory == true) {
							System.out.println("-------------in loop 1-------------------");
							form_readonly.append("\n <i class=\"menu-icon fa red\"> *</i>");
						}

						form_readonly.append(
								"\n</label>" + "\n<div class=\"col-xs-12 col-sm-9\">" + "\n<div class=\"clearfix\">");
						form_readonly.append("\n<input   placeholder=\"E_Mail\"  onblur=\"validateEmail(this);\"");

						if (hidden == true) {
							form_readonly.append("  type=\"hidden\" ");
						} else {
							form_readonly.append(
									"  value=\"${" + table_name_lower + "_update." + mapping4 + "}\" type=\"email\" ");
						}
						/*
						 * if(calculated_field.equals("Y")) { form_readonly.append(
						 * "name=\"total\" id=\"total\" class=\"stunden form-control\" "); } else {
						 * form_readonly.append( "name="+mapping_lower_case+" id=\""
						 * +mapping_lower_case+"\" class=\"col-xs-12 col-sm-4\" readonly"); }
						 */
						form_readonly.append("name=" + mapping4 + " id=" + mapping4 + "  class=\"form-control\"");

						if (mandatory == true) {
							System.out.println("-------------in loop 1 part 2 required-------------------");
							form_readonly.append("required");
						}

						System.out.println("-------------in loop 1 part 2 required-------------------");
						form_readonly.append("  readonly");

						form_readonly.append("/>\n</div>" + "\n</div>" + "\n</div>" + "\n</td>");
					}

					if (type_field.equals("masked") && sectionNum == fieldNum) {
						form_readonly.append(
								"\n<td>" + "\n<div class=\"form-group\" style=\"margin-left: 10%; margin-right: 10%;\">"
										+ "\n<label class=\"control-label col-xs-12 col-sm-3 no-padding-right\">"
										+ "\n " + field_name4);

						System.out.println("-------------mandatory for testing=------------------" + mandatory);

						if (mandatory == true) {
							System.out.println("-------------in loop 1-------------------");
							form_readonly.append("\n <i class=\"menu-icon fa red\"> *</i>");
						}

						form_readonly.append(
								"\n</label>" + "\n<div class=\"col-xs-12 col-sm-9\">" + "\n<div class=\"clearfix\">");
						form_readonly.append("\n<input   placeholder=\"Masked\"   maxlength=\"8\"");

						if (hidden == true) {
							form_readonly.append("  type=\"hidden\" ");
						} else {
							form_readonly.append(
									"  value=\"${" + table_name_lower + "_update." + mapping4 + "}\" type=\"password\" ");
						}
						/*
						 * if(calculated_field.equals("Y")) { form_readonly.append(
						 * "name=\"total\" id=\"total\" class=\"stunden form-control\" "); } else {
						 * form_readonly.append( "name="+mapping_lower_case+" id=\""
						 * +mapping_lower_case+"\" class=\"col-xs-12 col-sm-4\" readonly"); }
						 */
						form_readonly.append("name=" + mapping4 + " id=" + mapping4 + "  class=\"form-control\"");

						if (mandatory == true) {
							System.out.println("-------------in loop 1 part 2 required-------------------");
							form_readonly.append("required");
						}

						System.out.println("-------------in loop 1 part 2 required-------------------");
						form_readonly.append("  readonly");

						form_readonly.append("/>\n</div>" + "\n</div>" + "\n</div>" + "\n</td>");
					}

					if (type_field.equals("currency_field") && sectionNum == fieldNum) {
						form_readonly.append(
								"\n<td>" + "\n<div class=\"form-group\" style=\"margin-left: 10%; margin-right: 10%;\">"
										+ "\n<label class=\"control-label col-xs-12 col-sm-3 no-padding-right\">"
										+ "\n " + field_name4);

						System.out.println("-------------mandatory for testing=------------------" + mandatory);

						if (mandatory == true) {
							System.out.println("-------------in loop 1-------------------");
							form_readonly.append("\n <i class=\"menu-icon fa red\"> *</i>");
						}

						form_readonly.append(
								"\n</label>" + "\n<div class=\"col-xs-12 col-sm-9\">" + "\n<div class=\"clearfix\">");
						form_readonly.append("\n<input   placeholder=\"$1,000,000.00\"  onblur=\"handleChange()\"");

						if (hidden == true) {
							form_readonly.append("  type=\"hidden\" ");
						} else {
							form_readonly.append(
									"  value=\"${" + table_name_lower + "_update." + mapping4 + "}\" type=\"text\" ");
						}
						/*
						 * if(calculated_field.equals("Y")) { form_readonly.append(
						 * "name=\"total\" id=\"total\" class=\"stunden form-control\" "); } else {
						 * form_readonly.append( "name="+mapping_lower_case+" id=\""
						 * +mapping_lower_case+"\" class=\"col-xs-12 col-sm-4\" readonly"); }
						 */
						form_readonly.append("name=" + mapping4 + " id=" + mapping4 + "  class=\"form-control\"");

						if (mandatory == true) {
							System.out.println("-------------in loop 1 part 2 required-------------------");
							form_readonly.append("required");
						}

						System.out.println("-------------in loop 1 part 2 required-------------------");
						form_readonly.append("  readonly");

						form_readonly.append("/>\n</div>" + "\n</div>" + "\n</div>" + "\n</td>");
					}

					if (type_field.equals("contact_field") && sectionNum == fieldNum) {
						form_readonly.append(
								"\n<td>" + "\n<div class=\"form-group\" style=\"margin-left: 10%; margin-right: 10%;\">"
										+ "\n<label class=\"control-label col-xs-12 col-sm-3 no-padding-right\">"
										+ "\n " + field_name4);

						System.out.println("-------------mandatory for testing=------------------" + mandatory);

						if (mandatory == true) {
							System.out.println("-------------in loop 1-------------------");
							form_readonly.append("\n <i class=\"menu-icon fa red\"> *</i>");
						}

						form_readonly.append("\n</label>" + "\n<div class=\"col-xs-12 col-sm-9\">"
								+ "\n<div class=\"clearfix\">"

								+ "\n<div style=\"position:relative\">" + "\n<select style=\"   height:\";\" >"
								+ "\n<option>+91</option>" + "\n<option>+44</option>" + "\n<option>+64</option>"
								+ "\n<option>+21</option>" + "\n<option>+244</option>" + "\n<option>+376</option>"
								+ "\n<option>+354</option>" + "\n<option>+291</option>" + "\n<option>+54</option>"
								+ "\n<option>+264</option>" + "\n<option>+121</option>" + "\n</select>");
						form_readonly.append("\n<input    pattern=\"[0-9]{10}\"");

						if (hidden == true) {
							form_readonly.append("  type=\"hidden\" ");
						} else {
							form_readonly.append(
									"  value=\"${" + table_name_lower + "_update." + mapping4 + "}\" type=\"tel\" ");
						}
						/*
						 * if(calculated_field.equals("Y")) { form_readonly.append(
						 * "name=\"total\" id=\"total\" class=\"stunden form-control\" "); } else {
						 * form_readonly.append( "name="+mapping_lower_case+" id=\""
						 * +mapping_lower_case+"\" class=\"col-xs-12 col-sm-4\" readonly"); }
						 */
						form_readonly.append("name=" + mapping4 + " id=" + mapping4 + "  class=\"form-control\"");

						if (mandatory == true) {
							System.out.println("-------------in loop 1 part 2 required-------------------");
							form_readonly.append("required");
						}

						System.out.println("-------------in loop 1 part 2 required-------------------");
						form_readonly.append("  readonly");

						form_readonly.append("/>\n</div>" + "\n</div>" + "\n</div>" + "\n</div>" + "\n</td>");
					}

					if (type_field.equals("upload_field") && sectionNum == fieldNum) {
						form_readonly.append(
								"\n<td>" + "\n<div class=\"form-group\" style=\"margin-left: 10%; margin-right: 10%;\">"
										+ "\n<label class=\"control-label col-xs-12 col-sm-3 no-padding-right\">"
										+ "\n " + field_name4);

						System.out.println("-------------mandatory for testing=------------------" + mandatory);

						if (mandatory == true) {
							System.out.println("-------------in loop 1-------------------");
							form_readonly.append("\n <i class=\"menu-icon fa red\"> *</i>");
						}

						form_readonly.append(
								"\n</label>" + "\n<div class=\"col-xs-12 col-sm-9\">" + "\n<div class=\"clearfix\">");

						/* +"\n<div class=\"inputWithIcon\">"); */

						form_readonly.append("\n<input value=\"${" + table_name_lower + "_update." + mapping4
								+ "}\"   placeholder=\"Upload File\" style=\"width:100%; padding-left:10px;\"");

						if (hidden == true) {
							form_readonly.append("  type=\"hidden\" ");
						} else {
							form_readonly.append(
									"  value=\"${" + table_name_lower + "_update." + mapping4 + "}\" type=\"file\" ");
						}
						/*
						 * if(calculated_field.equals("Y")) { form_readonly.append(
						 * "name=\"total\" id=\"total\" class=\"stunden form-control\" "); } else {
						 * form_readonly.append( "name="+mapping_lower_case+" id=\""
						 * +mapping_lower_case+"\" class=\"col-xs-12 col-sm-4\" readonly"); }
						 */
						form_readonly.append("name=" + mapping4 + " id=" + mapping4 + "  class=\"form-control\"");

						if (mandatory == true) {
							System.out.println("-------------in loop 1 part 2 required-------------------");
							form_readonly.append("required");
						}

						System.out.println("-------------in loop 1 part 2 required-------------------");
						form_readonly.append("  readonly");

						form_readonly.append("/>\n" +

								" <c:out value=\"${" + table_name_lower + "_update." + mapping4 + "}\"/>"

								/*
								 * +
								 * " \n <a title=\"Upload File\"  data-title=\"Edit\"  ata-toggle=\"popover\" data-trigger=\"hover\" data-placement=\"bottom\" data-content=\"upload selected file\" >"
								 * +
								 * "  \n  <i class=\"fa fa-upload fa-lg fa-fw\" id=\"upload_btn\" aria-hidden=\"true\""
								 * + "	 \n  </i>" + " \n</a>"
								 * 
								 * +
								 * " \n <a id=\"userButton\"  class=\"common\"  data-title=\"Browse File\" data-toggle=\"popover\" data-trigger=\"hover\" data-placement=\"bottom\" data-content=\"browse files from directory\" >"
								 * +
								 * "  \n  <i class=\"fa fa-paperclip fa-lg fa-fw\" id=\"paperclip\" aria-hidden=\"true\""
								 * + "	 \n  </i>" + " \n</a>"
								 * 
								 * +
								 * " \n <a id=\"userButton\"  class=\"common\"  data-title=\"Upload Image File\" data-toggle=\"popover\" data-trigger=\"hover\" data-placement=\"bottom\" data-content=\"upload selected image file\" >"
								 * + "  \n  <i class=\"fa fa-camera fa-lg\" id=\"camera\" aria-hidden=\"true\""
								 * + "	 \n  </i>" + " \n</a>"
								 * 
								 * + "\n</div>"
								 */
								+ "\n</div>" + "\n</div>" + "\n</div>" + "\n</td>");
					}

					if (type_field.equals("textarea") && sectionNum == fieldNum) {
						form_readonly.append(
								"\n<td>" + "\n<div class=\"form-group\" style=\"margin-left: 10%; margin-right: 10%;\">"
										+ "\n<label class=\"control-label col-xs-12 col-sm-3 no-padding-right\">"
										+ "\n " + field_name4);

						if (mandatory == true) {
							form_readonly.append("\n <i class=\"menu-icon fa red\"> *</i>");
						}

						form_readonly.append(
								"\n</label>" + "\n<div class=\"col-xs-12 col-sm-9\">" + "\n<div class=\"clearfix\">");
						form_readonly.append("\n<textarea rows=\"" + 0 + "\" cols=\"" + 34 + "\"");

						if (hidden == true) {
							form_readonly.append("  type=\"hidden\" ");
						} else {
							form_readonly.append(
									"   value=\"${" + table_name_lower + "_update." + mapping4 + "}\"  type=\"text\" ");
						}
						/*
						 * if(calculated_field.equals("Y")) { form_readonly.append(
						 * "name=\"total\" id=\"total\" class=\"stunden form-control\" "); } else {
						 * form_readonly.append( "name="+mapping_lower_case+" id=\""
						 * +mapping_lower_case+"\" class=\"col-xs-12 col-sm-4\" readonly"); }
						 */
						form_readonly.append("name=" + mapping4 + " id=" + mapping4 + "  class=\"form-control\"");

						if (mandatory == true) {
							System.out.println("-------------in loop 1 part 2 required-------------------");
							form_readonly.append("required");
						}

						form_readonly.append("  readonly");
						form_readonly.append("/>${" + table_name_lower + "_update." + mapping4 + "}</textarea>\n</div>"
								+ "\n</div>" + "\n</div>" + "\n</td>");
					}

					if (type_field.equals("datetime") && sectionNum == fieldNum) {
						form_readonly.append(
								"\n<td>" + "\n<div class=\"form-group\" style=\"margin-left: 10%; margin-right: 10%;\">"
										+ "\n<label class=\"control-label col-xs-12 col-sm-3 no-padding-right\">"
										+ "\n " + field_name4);

						System.out.println("-------------mandatory for testing=------------------" + mandatory);

						if (mandatory == true) {
							System.out.println("-------------in loop 1-------------------");
							form_readonly.append("\n <i class=\"menu-icon fa red\"> *</i>");
						}

						form_readonly.append(
								"\n</label>" + "\n<div class=\"col-xs-12 col-sm-9\">" + "\n<div class=\"clearfix\">"
										+ "<div class=\"input-group input-append date\" id=\"datePicker1\">");

						form_readonly.append("\n<input  ");

						if (hidden == true) {
							form_readonly.append("  type=\"hidden\" ");
						} else {
							form_readonly.append(
									"   value=\"${" + table_name_lower + "_update." + mapping4 + "}\"  type=\"text\" ");
						}

						form_readonly.append("name=" + mapping4 + " id=" + mapping4
								+ " placeholder=\"picup Date\" class=\"form-control\"");

						if (mandatory == true) {
							System.out.println("-------------in loop 1 part 2 required-------------------");
							form_prefield.append("required");
						}

						form_readonly.append("  readonly");

						form_readonly.append("/>\n"
								+ "\n<span class=\"input-group-addon\"> <i class=\"fa fa-calendar bigger-110\"></i>"
								+ "\n</span>" + "\n</div>" + "\n</div>" + "\n</div>" + "\n</div>" + "\n</td>");
					}

					if (type_field.equals("autocomplete") && sectionNum == fieldNum) {
						form_readonly.append(
								"\n<td>" + "\n<div class=\"form-group\" style=\"margin-left: 10%; margin-right: 10%;\">"
										+ "\n<label class=\"control-label col-xs-12 col-sm-3 no-padding-right\">"
										+ "\n " + field_name4);

						System.out.println("-------------mandatory for testing=------------------" + mandatory);

						if (mandatory == true) {
							System.out.println("-------------in loop 1-------------------");
							form_readonly.append("\n <i class=\"menu-icon fa red\"> *</i>");
						}

						form_readonly.append(
								"\n</label>" + "\n<div class=\"col-xs-12 col-sm-9\">" + "\n<div class=\"clearfix\">");

						form_readonly.append("\n<input   placeholder=\"AutoComplete\"");

						if (hidden == true) {
							form_readonly.append(
									" value=\"${" + table_name_lower + "_update." + mapping4 + "}\"  type=\"hidden\" ");
						} else {
							form_readonly.append(
									"  value=\"${" + table_name_lower + "_update." + mapping4 + "}\" type=\"text\" ");
						}
						/*
						 * if(calculated_field.equals("Y")) { form_readonly.append(
						 * "name=\"total\" id=\"total\" class=\"stunden form-control\" "); } else {
						 * form_readonly.append( "name="+mapping_lower_case+" id=\""
						 * +mapping_lower_case+"\" class=\"col-xs-12 col-sm-4\" readonly"); }
						 */

						form_readonly.append("name=" + mapping4 + " id=" + mapping4 + "  class=\"form-control\"");
						if (mandatory == true) {
							System.out.println("-------------in loop 1 part 2 required-------------------");
							form_readonly.append("required");
						}

						form_readonly.append("  readonly");

						form_readonly.append("/>\n</div>" + "\n</div>" + "\n</div>" + "\n</td>");

					}

					if (type_field.equals("dropdown") && sectionNum == fieldNum) {
						form_readonly.append(
								"\n<td>" + "\n<div class=\"form-group\" style=\"margin-left: 10%; margin-right: 10%;\">"
										+ "\n<label class=\"control-label col-xs-12 col-sm-3 no-padding-right\">"
										+ "\n " + field_name4);

						if (mandatory == true) {
							form_readonly.append("\n <i class=\"menu-icon fa red\"> *</i>");
						}

						form_readonly.append("\n</label>" + "\n<div class=\"col-xs-12 col-sm-9\">");

						form_readonly.append("\n<input name=\"" + mapping4 + "\" id=\"" + mapping4 + "\"  value=\"${"
								+ table_name_lower + "_update." + mapping4
								+ "}\"  class=\"col-xs-3 col-sm-3 form-control state\""

								+ "\n");

						if (mandatory == true) {
							System.out.println("-------------in loop 1 part 2 required-------------------");
							form_readonly.append("required");
						}

						form_readonly.append("  readonly");
						form_readonly.append("\n/></div>"

								+ "\n</div>" + "\n</td>");
					}

					if (type_field.equals("togglebutton") && sectionNum == fieldNum) {
						form_readonly.append(
								"\n<td>" + "\n<div class=\"form-group\" style=\"margin-left: 10%; margin-right: 10%;\">"
										+ "\n<label class=\"control-label col-xs-12 col-sm-3 no-padding-right\">"
										+ "\n " + field_name4);

						System.out.println("-------------mandatory for testing=------------------" + mandatory);

						if (mandatory == true) {
							System.out.println("-------------in loop 1-------------------");
							form_readonly.append("\n <i class=\"menu-icon fa red\"> *</i>");
						}

						form_readonly.append(
								"\n</label>" + "\n<div class=\"col-xs-12 col-sm-9\">" + "\n<div class=\"clearfix\">");

						form_readonly.append("\n<input");

						if (hidden == true) {
							form_readonly.append("  type=\"hidden\" ");
						} else {
							form_readonly.append("  type=\"checkbox\" ");
						}

						form_readonly.append(
								" value=\"${" + table_name_lower + "_update." + mapping4 + "}\" name=" + mapping4 + " id="
										+ mapping4 + "  class=\"ace ace-switch ace-switch-6\" onblur=\"CheckUserStatus"
										+ mapping4 + "()\"");

						form_readonly.append("readonly />\n" + "\n<span class=\"lbl\"></span>"

								+ "\n</div>" + "\n</div>" + "\n</div>" + "\n</td>");
					}

					if (type_field.equals("checkbox") && sectionNum == fieldNum) {
						form_readonly.append(
								"\n<td>" + "\n<div class=\"form-group\" style=\"margin-left: 10%; margin-right: 10%;\">"
										+ "\n<label class=\"control-label col-xs-12 col-sm-3 no-padding-right\">"
										+ "\n " + field_name4);

						System.out.println("-------------mandatory for testing=------------------" + mandatory);

						if (mandatory == true) {
							System.out.println("-------------in loop 1-------------------");
							form_readonly.append("\n <i class=\"menu-icon fa red\"> *</i>");
						}

						form_readonly.append(
								"\n</label>" + "\n<div class=\"col-xs-12 col-sm-9\">" + "\n<div class=\"clearfix\">");

						form_readonly.append("\n<input  style=\"width:10%; margin-left:20px\" class=\"form-control\"");

						form_readonly.append("  type=\"checkbox\" ");

						form_readonly.append(" value=\"${" + table_name_lower + "_update." + mapping4 + "}\" name="
								+ mapping4 + " id=" + mapping4 + "  onblur=\"CheckUserStatusHeader1()\" ");

						form_readonly.append("  readonly");

						form_readonly.append("/>\n" + "\n</div>" + "\n</div>" + "\n</div>" + "\n</td>");
					}
				}
			}

			form_readonly.append("\n</tr>");

			form_readonly.append("\n<tr>");

			for (int i = 0; i < sectionNumFields.size(); i++) {
				String field_name4 = sectionNumFields.get(i).getFieldName();
				String mapping4 = sectionNumFields.get(i).getMapping();
				String data_type4 = sectionNumFields.get(i).getDataType();

				int fieldNum = sectionNumFields.get(i).getSection_num();

				System.out.println("field name-------" + field_name4);
				System.out.println("mapping-------" + mapping4);
				System.out.println("data type-------" + data_type4);

				System.out.println("field name-------" + field_name4);
				System.out.println("mapping-------" + mapping4);
				System.out.println("data type-------" + data_type4);

				System.out.println("field name-------" + field_name4);
				System.out.println("mapping-------" + mapping4);
				System.out.println("data type-------" + data_type4);

//				String mandatory = sectionNumFields.get(i).getMandatory();
//				String hidden = sectionNumFields.get(i).getHidden();
				boolean mandatory = sectionNumFields.get(i).isMandatory();
				boolean hidden = sectionNumFields.get(i).isHidden();

				// String mapping=sectionNumFields.get(i).getMapping();

				String type_field = sectionNumFields.get(i).getType_field();

				if (i > 5 && i <= 8) {
					if (type_field.equals("textfield") && sectionNum == fieldNum) {
						form_readonly.append(
								"\n<td>" + "\n<div class=\"form-group\" style=\"margin-left: 10%; margin-right: 10%;\">"
										+ "\n<label class=\"control-label col-xs-12 col-sm-3 no-padding-right\">"
										+ "\n " + field_name4);

						System.out.println("-------------mandatory for testing=------------------" + mandatory);

						if (mandatory == true) {
							System.out.println("-------------in loop 1-------------------");
							form_readonly.append("\n <i class=\"menu-icon fa red\"> *</i>");
						}

						form_readonly.append(
								"\n</label>" + "\n<div class=\"col-xs-12 col-sm-9\">" + "\n<div class=\"clearfix\">");
						form_readonly.append("\n<input ");

						if (hidden == true) {
							form_readonly.append("  type=\"hidden\" ");
						} else {
							form_readonly.append(
									"  value=\"${" + table_name_lower + "_update." + mapping4 + "}\" type=\"text\" ");
						}
						/*
						 * if(calculated_field.equals("Y")) { form_readonly.append(
						 * "name=\"total\" id=\"total\" class=\"stunden form-control\" "); } else {
						 * form_readonly.append( "name="+mapping_lower_case+" id=\""
						 * +mapping_lower_case+"\" class=\"col-xs-12 col-sm-4\" readonly"); }
						 */
						form_readonly.append("name=" + mapping4 + " id=" + mapping4 + "  class=\"form-control\"");

						if (mandatory == true) {
							System.out.println("-------------in loop 1 part 2 required-------------------");
							form_readonly.append("required");
						}

						System.out.println("-------------in loop 1 part 2 required-------------------");
						form_readonly.append("  readonly");

						form_readonly.append("/>\n</div>" + "\n</div>" + "\n</div>" + "\n</td>");

						readonly_script.append("\n<script>" + "\ndocument.getElementById(\'" + mapping4
								+ "\').onclick = function() {" + "  \n  document.getElementById(\'" + mapping4
								+ "\').removeAttribute('readonly');" + "\n};" + "\n</script>");
					}

					if (type_field.equals("url") && sectionNum == fieldNum) {
						form_readonly.append(
								"\n<td>" + "\n<div class=\"form-group\" style=\"margin-left: 10%; margin-right: 10%;\">"
										+ "\n<label class=\"control-label col-xs-12 col-sm-3 no-padding-right\">"
										+ "\n " + field_name4);

						System.out.println("-------------mandatory for testing=------------------" + mandatory);

						if (mandatory == true) {
							System.out.println("-------------in loop 1-------------------");
							form_readonly.append("\n <i class=\"menu-icon fa red\"> *</i>");
						}

						form_readonly.append(
								"\n</label>" + "\n<div class=\"col-xs-12 col-sm-9\">" + "\n<div class=\"clearfix\">");
						form_readonly.append("\n<input   placeholder=\"URL\"pattern=\"https://.*\" size=\"30\" ");

						if (hidden == true) {
							form_readonly.append("  type=\"hidden\" ");
						} else {
							form_readonly.append(
									"  value=\"${" + table_name_lower + "_update." + mapping4 + "}\" type=\"text\" ");
						}
						/*
						 * if(calculated_field.equals("Y")) { form_readonly.append(
						 * "name=\"total\" id=\"total\" class=\"stunden form-control\" "); } else {
						 * form_readonly.append( "name="+mapping_lower_case+" id=\""
						 * +mapping_lower_case+"\" class=\"col-xs-12 col-sm-4\" readonly"); }
						 */
						form_readonly.append("name=" + mapping4 + " id=" + mapping4 + "  class=\"form-control\"");

						if (mandatory == true) {
							System.out.println("-------------in loop 1 part 2 required-------------------");
							form_readonly.append("required");
						}

						System.out.println("-------------in loop 1 part 2 required-------------------");
						form_readonly.append("  readonly");

						form_readonly.append("/>\n</div>" + "\n</div>" + "\n</div>" + "\n</td>");
					}

					if (type_field.equals("email") && sectionNum == fieldNum) {
						form_readonly.append(
								"\n<td>" + "\n<div class=\"form-group\" style=\"margin-left: 10%; margin-right: 10%;\">"
										+ "\n<label class=\"control-label col-xs-12 col-sm-3 no-padding-right\">"
										+ "\n " + field_name4);

						System.out.println("-------------mandatory for testing=------------------" + mandatory);

						if (mandatory == true) {
							System.out.println("-------------in loop 1-------------------");
							form_readonly.append("\n <i class=\"menu-icon fa red\"> *</i>");
						}

						form_readonly.append(
								"\n</label>" + "\n<div class=\"col-xs-12 col-sm-9\">" + "\n<div class=\"clearfix\">");
						form_readonly.append("\n<input   placeholder=\"E_Mail\" onblur=\"validateEmail(this);\"");

						if (hidden == true) {
							form_readonly.append("  type=\"hidden\" ");
						} else {
							form_readonly.append(
									"  value=\"${" + table_name_lower + "_update." + mapping4 + "}\" type=\"email\" ");
						}
						
						form_readonly.append("name=" + mapping4 + " id=" + mapping4 + "  class=\"form-control\"");

						if (mandatory == true) {
							System.out.println("-------------in loop 1 part 2 required-------------------");
							form_readonly.append("required");
						}

						System.out.println("-------------in loop 1 part 2 required-------------------");
						form_readonly.append("  readonly");

						form_readonly.append("/>\n</div>" + "\n</div>" + "\n</div>" + "\n</td>");
					}

					if (type_field.equals("masked") && sectionNum == fieldNum) {
						form_readonly.append(
								"\n<td>" + "\n<div class=\"form-group\" style=\"margin-left: 10%; margin-right: 10%;\">"
										+ "\n<label class=\"control-label col-xs-12 col-sm-3 no-padding-right\">"
										+ "\n " + field_name4);

						System.out.println("-------------mandatory for testing=------------------" + mandatory);

						if (mandatory == true) {
							System.out.println("-------------in loop 1-------------------");
							form_readonly.append("\n <i class=\"menu-icon fa red\"> *</i>");
						}

						form_readonly.append(
								"\n</label>" + "\n<div class=\"col-xs-12 col-sm-9\">" + "\n<div class=\"clearfix\">");
						form_readonly.append("\n <input   placeholder=\"Masked\"  maxlength=\"8\"");

						if (hidden == true) {
							form_readonly.append("  type=\"hidden\" ");
						} else {
							form_readonly.append(
									"  value=\"${" + table_name_lower + "_update." + mapping4 + "}\" type=\"password\" ");
						}
						
						form_readonly.append("name=" + mapping4 + " id=" + mapping4 + "  class=\"form-control\"");

						if (mandatory == true) {
							System.out.println("-------------in loop 1 part 2 required-------------------");
							form_readonly.append("required");
						}

						System.out.println("-------------in loop 1 part 2 required-------------------");
						form_readonly.append("  readonly");

						form_readonly.append("/>\n</div>" + "\n</div>" + "\n</div>" + "\n</td>");
					}

					if (type_field.equals("currency_field") && sectionNum == fieldNum) {
						form_readonly.append(
								"\n<td>" + "\n<div class=\"form-group\" style=\"margin-left: 10%; margin-right: 10%;\">"
										+ "\n<label class=\"control-label col-xs-12 col-sm-3 no-padding-right\">"
										+ "\n " + field_name4);

						System.out.println("-------------mandatory for testing=------------------" + mandatory);

						if (mandatory == true) {
							System.out.println("-------------in loop 1-------------------");
							form_readonly.append("\n <i class=\"menu-icon fa red\"> *</i>");
						}

						form_readonly.append(
								"\n</label>" + "\n<div class=\"col-xs-12 col-sm-9\">" + "\n<div class=\"clearfix\">");
						form_readonly.append("\n<input   placeholder=\"$1,000,000.00\" onblur=\"handleChange()\"");

						if (hidden == true) {
							form_readonly.append("  type=\"hidden\" ");
						} else {
							form_readonly.append(
									"  value=\"${" + table_name_lower + "_update." + mapping4 + "}\" type=\"text\" ");
						}
						/*
						 * if(calculated_field.equals("Y")) { form_readonly.append(
						 * "name=\"total\" id=\"total\" class=\"stunden form-control\" "); } else {
						 * form_readonly.append( "name="+mapping_lower_case+" id=\""
						 * +mapping_lower_case+"\" class=\"col-xs-12 col-sm-4\" readonly"); }
						 */
						form_readonly.append("name=" + mapping4 + " id=" + mapping4 + "  class=\"form-control\"");

						if (mandatory == true) {
							System.out.println("-------------in loop 1 part 2 required-------------------");
							form_readonly.append("required");
						}

						System.out.println("-------------in loop 1 part 2 required-------------------");
						form_readonly.append("  readonly");

						form_readonly.append("/>\n</div>" + "\n</div>" + "\n</div>" + "\n</td>");
					}

					if (type_field.equals("upload_field") && sectionNum == fieldNum) {
						form_readonly.append(
								"\n<td>" + "\n<div class=\"form-group\" style=\"margin-left: 10%; margin-right: 10%;\">"
										+ "\n<label class=\"control-label col-xs-12 col-sm-3 no-padding-right\">"
										+ "\n " + field_name4);

						System.out.println("-------------mandatory for testing=------------------" + mandatory);

						if (mandatory == true) {
							System.out.println("-------------in loop 1-------------------");
							form_readonly.append("\n <i class=\"menu-icon fa red\"> *</i>");
						}

						form_readonly.append(
								"\n</label>" + "\n<div class=\"col-xs-12 col-sm-9\">" + "\n<div class=\"clearfix\">");

						/* + "\n<div class=\"inputWithIcon\">"); */

						form_readonly.append("\n<input "
								+ "\"   placeholder=\"Upload File\" style=\"width:100%; padding-left:10px;\"");
						if (hidden == true) {
							form_readonly.append("  type=\"hidden\" ");
						} else {
							form_readonly.append(
									"  value=\"${" + table_name_lower + "_update." + mapping4 + "}\" type=\"file\" ");
						}
						/*
						 * if(calculated_field.equals("Y")) { form_readonly.append(
						 * "name=\"total\" id=\"total\" class=\"stunden form-control\" "); } else {
						 * form_readonly.append( "name="+mapping_lower_case+" id=\""
						 * +mapping_lower_case+"\" class=\"col-xs-12 col-sm-4\" readonly"); }
						 */
						form_readonly.append("name=" + mapping4 + " id=" + mapping4 + "  class=\"form-control\"");

						if (mandatory == true) {
							System.out.println("-------------in loop 1 part 2 required-------------------");
							form_readonly.append("required");
						}

						System.out.println("-------------in loop 1 part 2 required-------------------");
						form_readonly.append("  readonly");

						form_readonly.append("/>\n" +

								" <c:out value=\"${" + table_name_lower + "_update." + mapping4 + "}\"/>"

								
								+ "\n</div>" + "\n</div>" + "\n</div>" + "\n</td>");
					}

					if (type_field.equals("textarea") && sectionNum == fieldNum) {
						form_readonly.append(
								"\n<td>" + "\n<div class=\"form-group\" style=\"margin-left: 10%; margin-right: 10%;\">"
										+ "\n<label class=\"control-label col-xs-12 col-sm-3 no-padding-right\">"
										+ "\n " + field_name4);

						System.out.println("-------------mandatory for testing=------------------" + mandatory);

						if (mandatory == true) {
							System.out.println("-------------in loop 1-------------------");
							form_readonly.append("\n <i class=\"menu-icon fa red\"> *</i>");
						}

						form_readonly.append(
								"\n</label>" + "\n<div class=\"col-xs-12 col-sm-9\">" + "\n<div class=\"clearfix\">");
						form_readonly.append("\n<textarea rows=\"" + 0 + "\" cols=\"" + 34 + "\" ");

						if (hidden == true) {
							form_readonly.append("  type=\"hidden\" ");
						} else {
							form_readonly.append(
									"  value=\"${" + table_name_lower + "_update." + mapping4 + "}\" type=\"text\" ");
						}
						
						form_readonly.append("name=" + mapping4 + " id=" + mapping4 + "  class=\"form-control\"");

						if (mandatory == true) {
							System.out.println("-------------in loop 1 part 2 required-------------------");
							form_readonly.append("required");
						}

						System.out.println("-------------in loop 1 part 2 required-------------------");
						form_readonly.append("  readonly");

						form_readonly.append("/>${" + table_name_lower + "_update." + mapping4 + "}</textarea>\n</div>"
								+ "\n</div>" + "\n</div>" + "\n</td>");
					}

					if (type_field.equals("textarea") && sectionNum == fieldNum) {
						form_readonly.append(
								"\n<td>" + "\n<div class=\"form-group\" style=\"margin-left: 10%; margin-right: 10%;\">"
										+ "\n<label class=\"control-label col-xs-12 col-sm-3 no-padding-right\">"
										+ "\n " + field_name4);

						if (mandatory == true) {
							form_readonly.append("\n <i class=\"menu-icon fa red\"> *</i>");
						}

						form_readonly.append(
								"\n</label>" + "\n<div class=\"col-xs-12 col-sm-9\">" + "\n<div class=\"clearfix\">");
						form_readonly.append("\n<textarea rows=\"" + 0 + "\" cols=\"" + 34 + "\" ");

						if (hidden == true) {
							form.append("  type=\"hidden\" ");
						} else {
							form_readonly.append(
									"   value=\"${" + table_name_lower + "_update." + mapping4 + "}\"  type=\"text\" ");
						}

						/*
						 * if(calculated_field.equals("Y")) { form_readonly.append(
						 * "name=\"total\" id=\"total\" class=\"stunden form-control\" "); } else {
						 * form_readonly.append( "name="+mapping_lower_case+" id=\""
						 * +mapping_lower_case+"\" class=\"col-xs-12 col-sm-4\" readonly"); }
						 */
						form_readonly.append("name=" + mapping4 + " id=" + mapping4 + "  class=\"form-control\"");

						if (mandatory == true) {
							form_readonly.append("required");
						}

						form_readonly.append("  readonly");

						form_readonly.append("/>${" + table_name_lower + "_update." + mapping4 + "}</textarea>\n</div>"
								+ "\n</div>" + "\n</div>" + "\n</td>");
					}

					if (type_field.equals("datetime") && sectionNum == fieldNum) {
						form_readonly.append(
								"\n<td>" + "\n<div class=\"form-group\" style=\"margin-left: 10%; margin-right: 10%;\">"
										+ "\n<label class=\"control-label col-xs-12 col-sm-3 no-padding-right\">"
										+ "\n " + field_name4);

						System.out.println("-------------mandatory for testing=------------------" + mandatory);

						if (mandatory == true) {
							System.out.println("-------------in loop 1-------------------");
							form_readonly.append("\n <i class=\"menu-icon fa red\"> *</i>");
						}

						form_readonly.append(
								"\n</label>" + "\n<div class=\"col-xs-12 col-sm-9\">" + "\n<div class=\"clearfix\">"
										+ "<div class=\"input-group input-append date\" id=\"datePicker1\">");

						form_readonly.append("\n<input  ");

						if (hidden == true) {
							form_readonly.append("  type=\"hidden\" ");
						} else {
							form_readonly.append(
									"   value=\"${" + table_name_lower + "_update." + mapping4 + "}\"  type=\"text\" ");
						}

						form_readonly.append("name=" + mapping4 + " id=" + mapping4
								+ " placeholder=\"picup Date\" class=\"form-control\"");

						if (mandatory == true) {
							System.out.println("-------------in loop 1 part 2 required-------------------");
							form_prefield.append("required");
						}

						form_readonly.append("  readonly");

						form_readonly.append("/>\n"
								+ "\n<span class=\"input-group-addon\"> <i class=\"fa fa-calendar bigger-110\"></i>"
								+ "\n</span>" + "\n</div>" + "\n</div>" + "\n</div>" + "\n</div>" + "\n</td>");
					}

					if (type_field.equals("autocomplete") && sectionNum == fieldNum) {
						form_readonly.append(
								"\n<td>" + "\n<div class=\"form-group\" style=\"margin-left: 10%; margin-right: 10%;\">"
										+ "\n<label class=\"control-label col-xs-12 col-sm-3 no-padding-right\">"
										+ "\n " + field_name4);

						System.out.println("-------------mandatory for testing=------------------" + mandatory);

						if (mandatory == true) {
							System.out.println("-------------in loop 1-------------------");
							form_readonly.append("\n <i class=\"menu-icon fa red\"> *</i>");
						}

						form_readonly.append(
								"\n</label>" + "\n<div class=\"col-xs-12 col-sm-9\">" + "\n<div class=\"clearfix\">");

						form_readonly.append("\n<input   placeholder=\"AutoComplete\"");

						if (hidden == true) {
							form_readonly.append(
									" value=\"${" + table_name_lower + "_update." + mapping4 + "}\"  type=\"hidden\" ");
						} else {
							form_readonly.append(
									"  value=\"${" + table_name_lower + "_update." + mapping4 + "}\" type=\"text\" ");
						}

						form_readonly.append("name=" + mapping4 + " id=" + mapping4 + "  class=\"form-control\"");

						if (mandatory == true) {
							System.out.println("-------------in loop 1 part 2 required-------------------");
							form_readonly.append("required");
						}

						form_readonly.append("  readonly");

						form_readonly.append("/>\n</div>" + "\n</div>" + "\n</div>" + "\n</td>");

					}

					if (type_field.equals("dropdown") && sectionNum == fieldNum) {
						form_readonly.append(
								"\n<td>" + "\n<div class=\"form-group\" style=\"margin-left: 10%; margin-right: 10%;\">"
										+ "\n<label class=\"control-label col-xs-12 col-sm-3 no-padding-right\">"
										+ "\n " + field_name4);

						if (mandatory == true) {
							form_readonly.append("\n <i class=\"menu-icon fa red\"> *</i>");
						}

						form_readonly.append("\n</label>" + "\n<div class=\"col-xs-12 col-sm-9\">");

						form_readonly.append("\n<input name=\"" + mapping4 + "\" id=\"" + mapping4 + "\"  value=\"${"
								+ table_name_lower + "_update." + mapping4
								+ "}\"  class=\"col-xs-3 col-sm-3 form-control state\""

								+ "\n");

						if (mandatory == true) {
							System.out.println("-------------in loop 1 part 2 required-------------------");
							form_readonly.append("required");
						}

						form_readonly.append("  readonly");

						form_readonly.append("\n/></div>"

								+ "\n</div>" + "\n</td>");
					}

					if (type_field.equals("togglebutton") && sectionNum == fieldNum) {
						form_readonly.append(
								"\n<td>" + "\n<div class=\"form-group\" style=\"margin-left: 10%; margin-right: 10%;\">"
										+ "\n<label class=\"control-label col-xs-12 col-sm-3 no-padding-right\">"
										+ "\n " + field_name4);

						System.out.println("-------------mandatory for testing=------------------" + mandatory);

						if (mandatory == true) {
							System.out.println("-------------in loop 1-------------------");
							form_readonly.append("\n <i class=\"menu-icon fa red\"> *</i>");
						}

						form_readonly.append(
								"\n</label>" + "\n<div class=\"col-xs-12 col-sm-9\">" + "\n<div class=\"clearfix\">");

						form_readonly.append("\n<input");

						if (hidden == true) {
							form_readonly.append("  type=\"hidden\" ");
						} else {
							form_readonly.append("  type=\"checkbox\" ");
						}

						form_readonly.append(
								" value=\"${" + table_name_lower + "_update." + mapping4 + "}\" name=" + mapping4 + " id="
										+ mapping4 + "  class=\"ace ace-switch ace-switch-6\" onblur=\"CheckUserStatus"
										+ mapping4 + "()\"");

						form_readonly.append("readonly />\n" + "\n<span class=\"lbl\"></span>"

								+ "\n</div>" + "\n</div>" + "\n</div>" + "\n</td>");
					}

					if (type_field.equals("checkbox") && sectionNum == fieldNum) {
						form_readonly.append(
								"\n<td>" + "\n<div class=\"form-group\" style=\"margin-left: 10%; margin-right: 10%;\">"
										+ "\n<label class=\"control-label col-xs-12 col-sm-3 no-padding-right\">"
										+ "\n " + field_name4);

						System.out.println("-------------mandatory for testing=------------------" + mandatory);

						if (mandatory == true) {
							System.out.println("-------------in loop 1-------------------");
							form_readonly.append("\n <i class=\"menu-icon fa red\"> *</i>");
						}

						form_readonly.append(
								"\n</label>" + "\n<div class=\"col-xs-12 col-sm-9\">" + "\n<div class=\"clearfix\">");

						form_readonly.append("\n<input  style=\"width:10%; margin-left:20px\" class=\"form-control\"");

						form_readonly.append("  type=\"checkbox\" ");

						form_readonly.append(" value=\"${" + table_name_lower + "_update." + mapping4 + "}\" name="
								+ mapping4 + " id=" + mapping4 + "  onblur=\"CheckUserStatus" + mapping4 + "()\" ");

						form_readonly.append("  readonly");

						form_readonly.append("/>\n" + "\n</div>" + "\n</div>" + "\n</div>" + "\n</td>");
					}
				}
			}
			form_readonly.append("\n</tr>");

			form_readonly.append("\n<tr>");
			for (int i = 0; i < sectionNumFields.size(); i++) {
				String field_name4 = sectionNumFields.get(i).getFieldName();
				String mapping4 = sectionNumFields.get(i).getMapping();
				String data_type4 = sectionNumFields.get(i).getDataType();

				int fieldNum = sectionNumFields.get(i).getSection_num();

				System.out.println("field name-------" + field_name4);
				System.out.println("mapping-------" + mapping4);
				System.out.println("data type-------" + data_type4);

				System.out.println("field name-------" + field_name4);
				System.out.println("mapping-------" + mapping4);
				System.out.println("data type-------" + data_type4);

				System.out.println("field name-------" + field_name4);
				System.out.println("mapping-------" + mapping4);
				System.out.println("data type-------" + data_type4);

//				String mandatory = sectionNumFields.get(i).getMandatory();
//				String readonly = sectionNumFields.get(i).getReadonly();
//				String hidden = sectionNumFields.get(i).getHidden();
				
				boolean mandatory = sectionNumFields.get(i).isMandatory();
				boolean readonly = sectionNumFields.get(i).isReadonly();
				boolean hidden = sectionNumFields.get(i).isHidden();

				String type_field = sectionNumFields.get(i).getType_field();

				if (i > 8 && i <= 11) {
					if (type_field.equals("textfield") && sectionNum == fieldNum) {
						form_readonly.append(
								"\n<td>" + "\n<div class=\"form-group\" style=\"margin-left: 10%; margin-right: 10%;\">"
										+ "\n<label class=\"control-label col-xs-12 col-sm-3 no-padding-right\">"
										+ "\n " + field_name4);

						System.out.println("-------------mandatory for testing=------------------" + mandatory);

						if (mandatory == true) {
							System.out.println("-------------in loop 1-------------------");
							form_readonly.append("\n <i class=\"menu-icon fa red\"> *</i>");
						}

						form_readonly.append(
								"\n</label>" + "\n<div class=\"col-xs-12 col-sm-9\">" + "\n<div class=\"clearfix\">");
						form_readonly.append("\n<input ");

						if (hidden == true) {
							form_readonly.append("  type=\"hidden\" ");
						} else {
							form_readonly.append(
									"  value=\"${" + table_name_lower + "_update." + mapping4 + "}\" type=\"text\" ");
						}
						
						form_readonly.append("name=" + mapping4 + " id=" + mapping4 + "  class=\"form-control\"");

						if (mandatory == true) {
							System.out.println("-------------in loop 1 part 2 required-------------------");
							form_readonly.append("required");
						}

						System.out.println("-------------in loop 1 part 2 required-------------------");
						form_readonly.append("  readonly");

						form_readonly.append("/>\n</div>" + "\n</div>" + "\n</div>" + "\n</td>");

						readonly_script.append("\n<script>" + "\ndocument.getElementById(\'" + mapping4
								+ "\').onclick = function() {" + "  \n  document.getElementById(\'" + mapping4
								+ "\').removeAttribute('readonly');" + "\n};" + "\n</script>");
					}

					if (type_field.equals("url") && sectionNum == fieldNum) {
						form_readonly.append(
								"\n<td>" + "\n<div class=\"form-group\" style=\"margin-left: 10%; margin-right: 10%;\">"
										+ "\n<label class=\"control-label col-xs-12 col-sm-3 no-padding-right\">"
										+ "\n " + field_name4);

						System.out.println("-------------mandatory for testing=------------------" + mandatory);

						if (mandatory == true) {
							System.out.println("-------------in loop 1-------------------");
							form_readonly.append("\n <i class=\"menu-icon fa red\"> *</i>");
						}

						form_readonly.append(
								"\n</label>" + "\n<div class=\"col-xs-12 col-sm-9\">" + "\n<div class=\"clearfix\">");
						form_readonly.append("\n<input   placeholder=\"URL\"pattern=\"https://.*\" size=\"30\" ");

						if (hidden == true) {
							form_readonly.append("  type=\"hidden\" ");
						} else {
							form_readonly.append(
									"  value=\"${" + table_name_lower + "_update." + mapping4 + "}\" type=\"text\" ");
						}
						
						form_readonly.append("name=" + mapping4 + " id=" + mapping4 + "  class=\"form-control\"");

						if (mandatory == true) {
							System.out.println("-------------in loop 1 part 2 required-------------------");
							form_readonly.append("required");
						}

						System.out.println("-------------in loop 1 part 2 required-------------------");
						form_readonly.append("  readonly");

						form_readonly.append("/>\n</div>" + "\n</div>" + "\n</div>" + "\n</td>");
					}

					if (type_field.equals("email") && sectionNum == fieldNum) {
						form_readonly.append(
								"\n<td>" + "\n<div class=\"form-group\" style=\"margin-left: 10%; margin-right: 10%;\">"
										+ "\n<label class=\"control-label col-xs-12 col-sm-3 no-padding-right\">"
										+ "\n " + field_name4);

						System.out.println("-------------mandatory for testing=------------------" + mandatory);

						if (mandatory == true) {
							System.out.println("-------------in loop 1-------------------");
							form_readonly.append("\n <i class=\"menu-icon fa red\"> *</i>");
						}

						form_readonly.append(
								"\n</label>" + "\n<div class=\"col-xs-12 col-sm-9\">" + "\n<div class=\"clearfix\">");
						form_readonly.append("\n<input   placeholder=\"E_Mail\" onblur=\"validateEmail(this);\"");

						if (hidden == true) {
							form_readonly.append("  type=\"hidden\" ");
						} else {
							form_readonly.append(
									"  value=\"${" + table_name_lower + "_update." + mapping4 + "}\" type=\"email\" ");
						}
						
						form_readonly.append("name=" + mapping4 + " id=" + mapping4 + "  class=\"form-control\"");

						if (mandatory == true) {
							System.out.println("-------------in loop 1 part 2 required-------------------");
							form_readonly.append("required");
						}

						System.out.println("-------------in loop 1 part 2 required-------------------");
						form_readonly.append("  readonly");

						form_readonly.append("/>\n</div>" + "\n</div>" + "\n</div>" + "\n</td>");
					}

					if (type_field.equals("masked") && sectionNum == fieldNum) {
						form_readonly.append(
								"\n<td>" + "\n<div class=\"form-group\" style=\"margin-left: 10%; margin-right: 10%;\">"
										+ "\n<label class=\"control-label col-xs-12 col-sm-3 no-padding-right\">"
										+ "\n " + field_name4);

						System.out.println("-------------mandatory for testing=------------------" + mandatory);

						if (mandatory == true) {
							System.out.println("-------------in loop 1-------------------");
							form_readonly.append("\n <i class=\"menu-icon fa red\"> *</i>");
						}

						form_readonly.append(
								"\n</label>" + "\n<div class=\"col-xs-12 col-sm-9\">" + "\n<div class=\"clearfix\">");
						form_readonly.append("\n<input   placeholder=\"Masked\" maxlength=\"8\"");

						if (hidden == true) {
							form_readonly.append("  type=\"hidden\" ");
						} else {
							form_readonly.append(
									"  value=\"${" + table_name_lower + "_update." + mapping4 + "}\" type=\"password\" ");
						}
						
						form_readonly.append("name=" + mapping4 + " id=" + mapping4 + "  class=\"form-control\"");

						if (mandatory == true) {
							System.out.println("-------------in loop 1 part 2 required-------------------");
							form_readonly.append("required");
						}

						System.out.println("-------------in loop 1 part 2 required-------------------");
						form_readonly.append("  readonly");

						form_readonly.append("/>\n</div>" + "\n</div>" + "\n</div>" + "\n</td>");
					}

					if (type_field.equals("currency_field") && sectionNum == fieldNum) {
						form_readonly.append(
								"\n<td>" + "\n<div class=\"form-group\" style=\"margin-left: 10%; margin-right: 10%;\">"
										+ "\n<label class=\"control-label col-xs-12 col-sm-3 no-padding-right\">"
										+ "\n " + field_name4);

						System.out.println("-------------mandatory for testing=------------------" + mandatory);

						if (mandatory == true) {
							System.out.println("-------------in loop 1-------------------");
							form_readonly.append("\n <i class=\"menu-icon fa red\"> *</i>");
						}

						form_readonly.append(
								"\n</label>" + "\n<div class=\"col-xs-12 col-sm-9\">" + "\n<div class=\"clearfix\">");
						form_readonly.append("\n<input   placeholder=\"$1,000,000.00\" onblur=\"handleChange()\"");

						if (hidden == true) {
							form_readonly.append("  type=\"hidden\" ");
						} else {
							form_readonly.append(
									"  value=\"${" + table_name_lower + "_update." + mapping4 + "}\" type=\"text\" ");
						}
						
						form_readonly.append("name=" + mapping4 + " id=" + mapping4 + "  class=\"form-control\"");

						if (mandatory == true) {
							System.out.println("-------------in loop 1 part 2 required-------------------");
							form_readonly.append("required");
						}

						System.out.println("-------------in loop 1 part 2 required-------------------");
						form_readonly.append("  readonly");

						form_readonly.append("/>\n</div>" + "\n</div>" + "\n</div>" + "\n</td>");
					}

					if (type_field.equals("upload_field") && sectionNum == fieldNum) {
						form_readonly.append(
								"\n<td>" + "\n<div class=\"form-group\" style=\"margin-left: 10%; margin-right: 10%;\">"
										+ "\n<label class=\"control-label col-xs-12 col-sm-3 no-padding-right\">"
										+ "\n " + field_name4);

						System.out.println("-------------mandatory for testing=------------------" + mandatory);

						if (mandatory == true) {
							System.out.println("-------------in loop 1-------------------");
							form_readonly.append("\n <i class=\"menu-icon fa red\"> *</i>");
						}

						form_readonly.append(
								"\n</label>" + "\n<div class=\"col-xs-12 col-sm-9\">" + "\n<div class=\"clearfix\">");

						/* + "\n<div class=\"inputWithIcon\">"); */

						form_readonly.append("\n<input "
								+ "\"   placeholder=\"Upload File\" style=\"width:100%; padding-left:10px;\"");
						if (hidden == true) {
							form_readonly.append("  type=\"hidden\" ");
						} else {
							form_readonly.append(
									"  value=\"${" + table_name_lower + "_update." + mapping4 + "}\" type=\"file\" ");
						}
						
						form_readonly.append("name=" + mapping4 + " id=" + mapping4 + "  class=\"form-control\"");

						if (mandatory == true) {
							System.out.println("-------------in loop 1 part 2 required-------------------");
							form_readonly.append("required");
						}

						System.out.println("-------------in loop 1 part 2 required-------------------");
						form_readonly.append("  readonly");

						form_readonly.append("/>\n" +

								" <c:out value=\"${" + table_name_lower + "_update." + mapping4 + "}\"/>"
								+ "\n</div>" + "\n</div>" + "\n</div>" + "\n</td>");
					}

					if (type_field.equals("textarea") && sectionNum == fieldNum) {
						form_readonly.append(
								"\n<td>" + "\n<div class=\"form-group\" style=\"margin-left: 10%; margin-right: 10%;\">"
										+ "\n<label class=\"control-label col-xs-12 col-sm-3 no-padding-right\">"
										+ "\n " + field_name4);

						if (mandatory == true) {
							form_readonly.append("\n <i class=\"menu-icon fa red\"> *</i>");
						}

						form_readonly.append(
								"\n</label>" + "\n<div class=\"col-xs-12 col-sm-9\">" + "\n<div class=\"clearfix\">");
						form_readonly.append("\n<textarea rows=\"" + 0 + "\" cols=\"" + 34 + "\" ");

						if (hidden == true) {
							form_readonly.append("  type=\"hidden\" ");
						} else {
							form_readonly.append(
									"   value=\"${" + table_name_lower + "_update." + mapping4 + "}\"  type=\"text\" ");
						}
						/*
						 * if(calculated_field.equals("Y")) { form_readonly.append(
						 * "name=\"total\" id=\"total\" class=\"stunden form-control\" "); } else {
						 * form_readonly.append( "name="+mapping_lower_case+" id=\""
						 * +mapping_lower_case+"\" class=\"col-xs-12 col-sm-4\" readonly"); }
						 */
						form_readonly.append("name=" + mapping4 + " id=" + mapping4 + "  class=\"form-control\"");

						if (mandatory == true) {
							form_readonly.append("required");
						}

						form_readonly.append("  readonly");

						form_readonly.append("/>${" + table_name_lower + "_update." + mapping4 + "}</textarea>\n</div>"
								+ "\n</div>" + "\n</div>" + "\n</td>");
					}

					if (type_field.equals("datetime") && sectionNum == fieldNum) {
						form_readonly.append(
								"\n<td>" + "\n<div class=\"form-group\" style=\"margin-left: 10%; margin-right: 10%;\">"
										+ "\n<label class=\"control-label col-xs-12 col-sm-3 no-padding-right\">"
										+ "\n " + field_name4);

						System.out.println("-------------mandatory for testing=------------------" + mandatory);

						if (mandatory == true) {
							System.out.println("-------------in loop 1-------------------");
							form_readonly.append("\n <i class=\"menu-icon fa red\"> *</i>");
						}

						form_readonly.append(
								"\n</label>" + "\n<div class=\"col-xs-12 col-sm-9\">" + "\n<div class=\"clearfix\">"
										+ "<div class=\"input-group input-append date\" id=\"datePicker1\">");

						form_readonly.append("\n<input  ");

						if (hidden == true) {
							form_readonly.append("  type=\"hidden\" ");
						} else {
							form_readonly.append(
									"   value=\"${" + table_name_lower + "_update." + mapping4 + "}\"  type=\"text\" ");
						}

						form_readonly.append("name=" + mapping4 + " id=" + mapping4
								+ " placeholder=\"picup Date\" class=\"form-control\"");

						if (mandatory == true) {
							System.out.println("-------------in loop 1 part 2 required-------------------");
							form_prefield.append("required");
						}

						form_readonly.append("  readonly");

						form_readonly.append("/>\n"
								+ "\n<span class=\"input-group-addon\"> <i class=\"fa fa-calendar bigger-110\"></i>"
								+ "\n</span>" + "\n</div>" + "\n</div>" + "\n</div>" + "\n</div>" + "\n</td>");
					}

					if (type_field.equals("autocomplete") && sectionNum == fieldNum) {
						form_readonly.append(
								"\n<td>" + "\n<div class=\"form-group\" style=\"margin-left: 10%; margin-right: 10%;\">"
										+ "\n<label class=\"control-label col-xs-12 col-sm-3 no-padding-right\">"
										+ "\n " + field_name4);

						System.out.println("-------------mandatory for testing=------------------" + mandatory);

						if (mandatory == true) {
							System.out.println("-------------in loop 1-------------------");
							form_readonly.append("\n <i class=\"menu-icon fa red\"> *</i>");
						}

						form_readonly.append(
								"\n</label>" + "\n<div class=\"col-xs-12 col-sm-9\">" + "\n<div class=\"clearfix\">");

						form_readonly.append("\n<input   placeholder=\"AutoComplete\"");

						if (hidden == true) {
							form_readonly.append("  type=\"hidden\" ");
						} else {
							form_readonly.append(
									"  value=\"${" + table_name_lower + "_update." + mapping4 + "}\" type=\"text\" ");
						}
						/*
						 * if(calculated_field.equals("Y")) { form_readonly.append(
						 * "name=\"total\" id=\"total\" class=\"stunden form-control\" "); } else {
						 * form_readonly.append( "name="+mapping_lower_case+" id=\""
						 * +mapping_lower_case+"\" class=\"col-xs-12 col-sm-4\" readonly"); }
						 */

						form_readonly.append("name=" + mapping4 + " id=" + mapping4 + "  class=\"form-control\"");
						if (mandatory == true) {
							System.out.println("-------------in loop 1 part 2 required-------------------");
							form_readonly.append("required");
						}

						form_readonly.append("  readonly");

						form_readonly.append("/>\n</div>" + "\n</div>" + "\n</div>" + "\n</td>");

					}

					if (type_field.equals("dropdown") && sectionNum == fieldNum) {
						form_readonly.append(
								"\n<td>" + "\n<div class=\"form-group\" style=\"margin-left: 10%; margin-right: 10%;\">"
										+ "\n<label class=\"control-label col-xs-12 col-sm-3 no-padding-right\">"
										+ "\n " + field_name4);

						if (mandatory == true) {
							form_readonly.append("\n <i class=\"menu-icon fa red\"> *</i>");
						}

						form_readonly.append("\n</label>" + "\n<div class=\"col-xs-12 col-sm-9\">");

						form_readonly.append("\n<input name=\"" + mapping4 + "\" id=\"" + mapping4 + "\"  value=\"${"
								+ table_name_lower + "_update." + mapping4
								+ "}\"  class=\"col-xs-3 col-sm-3 form-control state\""

								+ "\n");

						if (readonly == true) {
							System.out.println("-------------in loop 1 part 2 required-------------------");
							form_readonly.append("  readonly");
						}
						form_readonly.append("\n/></div>"

								+ "\n</div>" + "\n</td>");
					}

					if (type_field.equals("togglebutton") && sectionNum == fieldNum) {
						form_readonly.append(
								"\n<td>" + "\n<div class=\"form-group\" style=\"margin-left: 10%; margin-right: 10%;\">"
										+ "\n<label class=\"control-label col-xs-12 col-sm-3 no-padding-right\">"
										+ "\n " + field_name4);

						System.out.println("-------------mandatory for testing=------------------" + mandatory);

						if (mandatory == true) {
							System.out.println("-------------in loop 1-------------------");
							form_readonly.append("\n <i class=\"menu-icon fa red\"> *</i>");
						}

						form_readonly.append(
								"\n</label>" + "\n<div class=\"col-xs-12 col-sm-9\">" + "\n<div class=\"clearfix\">");

						form_readonly.append("\n<input");

						if (hidden == true) {
							form_readonly.append("  type=\"hidden\" ");
						} else {
							form_readonly.append("  type=\"checkbox\" ");
						}

						form_readonly.append(
								" value=\"${" + table_name_lower + "_update." + mapping4 + "}\" name=" + mapping4 + " id="
										+ mapping4 + "  class=\"ace ace-switch ace-switch-6\" onblur=\"CheckUserStatus"
										+ mapping4 + "()\"");

						form_readonly.append("readonly />\n" + "\n<span class=\"lbl\"></span>"

								+ "\n</div>" + "\n</div>" + "\n</div>" + "\n</td>");
					}

					if (type_field.equals("checkbox") && sectionNum == fieldNum) {
						form_readonly.append(
								"\n<td>" + "\n<div class=\"form-group\" style=\"margin-left: 10%; margin-right: 10%;\">"
										+ "\n<label class=\"control-label col-xs-12 col-sm-3 no-padding-right\">"
										+ "\n " + field_name4);

						System.out.println("-------------mandatory for testing=------------------" + mandatory);

						if (mandatory == true) {
							System.out.println("-------------in loop 1-------------------");
							form_readonly.append("\n <i class=\"menu-icon fa red\"> *</i>");
						}

						form_readonly.append(
								"\n</label>" + "\n<div class=\"col-xs-12 col-sm-9\">" + "\n<div class=\"clearfix\">");

						form_readonly.append("\n<input  style=\"width:10%; margin-left:20px\" class=\"form-control\"");

						form_readonly.append("  type=\"checkbox\" ");

						form_readonly.append(" value=\"${" + table_name_lower + "_update." + mapping4 + "}\" name="
								+ mapping4 + " id=" + mapping4 + "  onblur=\"CheckUserStatus" + mapping4 + "()\" ");

						form_readonly.append("  readonly");

						form_readonly.append("/>\n" + "\n</div>" + "\n</div>" + "\n</div>" + "\n</td>");
					}
				}
			}

			form_readonly.append("\n\n</tr>\n</c:forEach>");

			form_readonly.append("\n</table>");

			if (line_table_name != null && !line_table_name.isEmpty()) {
				String line_table_name_lower = line_table_name.toLowerCase();
				String line_table_name_first_upper = line_table_name_lower.substring(0, 1).toUpperCase()
						+ line_table_name_lower.substring(1);
				form_readonly.append("\n\n<%@include file=\"/WEB-INF/tiles/acemaster/" + module_name + "/"
						+ line_table_name_first_upper + "_readonly.jsp\"%>");
			}

		}

		form_readonly.append("\n" + "<div class=\"hr hr-dotted\"></div>" + "\n<div class=\"wizard-actions\">"
				+ "\n<button type=\"submit\" class=\"btn btn-success center\" onclick=\"submitForms()\">" + "\nUpdate"
				+ "\n</button>" + "\n</div> ");
		form_readonly.append("\n"

				+ "\n</form>");
		form_readonly.append("\n\n<%@include file=\"/WEB-INF/tiles/acemaster/" + module_name + "/" + table_name_lower
				+ "_ext_Readonly.jsp\"%>");

		strContentreadonly.append(importsectionreadonly + " \n" + headsectionreadonly
				+ "\n<body>\n"
				// READ-ONLY FORM_CODE
				+ "<input type=\"hidden\" name=\"form_code\" id=\"form_code\" value=\"" + form_code + "\" />\n"
				+ "<div class=\"main-container\" id=\"main-container\">"

				+ "\n<div class=\"main-content\">" + "\n<div class=\"main-content-inner\">"
				+ "\n<div class=\"breadcrumbs\" id=\"breadcrumbs\">" + "\n<ul class=\"breadcrumb\">"
				+ "\n<li><i class=\"ace-icon fa fa-home home-icon\"></i> <a href=\"#\">Home</a>" + "\n</li>"

				+ "\n<li><a href=\"#\">ManageUsers</a></li>" + "\n<li class=\"active\">" + ui_name + "</li>" + "\n</ul>"
				+ "\n</div>"

				+ "\n<div class=\"page-content\">" + "\n<div class=\"page-header\">" + "\n<h1>" + ui_name + "</h1>"
				+ "\n</div>"

				+ "\n<div class=\"row\">" + "\n<div class=\"col-xs-12\">"

				+ "\n<div class=\"widget-body\">" + "\n<div class=\"widget-main\">"
				+ "\n<div id=\"fuelux-wizard-container\">" + "\n<div class=\"step-content pos-rel\">"
				+ "\n<div class=\"step-pane active\" data-step=\"1\">"

				+ form_readonly

				+ "\n</div>" + "\n</div>" + "\n</div>" + "\n</div>" + "\n</div>" + "\n</div>" + "\n</div>" + "\n</div>"
				+ "\n</div>" + "\n</div>" + "\n</div>" + "\n</div>\n</body>\n" + line_script_update + "<script>"
				+ date_script + dropdown_script_latest + dropdown_script_latest1

				+ togglescript + "\n</script>" + readonly_script + "\n<script>" + "\nvar readonly = true;"
				+ "\n$('a').on('click', (e) => {" + "  \nreadonly = !readonly"
				+ "  \n$('input').attr('readonly', readonly);" + "  // Extra" + "  \nif (readonly) {"
				+ "    \n$('input').attr('placeholder', \"I'm readonly\");" + "  \n} else {"
				+ "   \n $('input').attr('placeholder', \"I'm not readonly\");" + "  \n}" + "\n});" + "\n</script>" +

				"\n<!-- ace scripts -->"
				+ "	\n<script src=\"<c:url value='/resources/assets/js/ace-elements.min.js'/>\""
				+ "		\ntype=\"text/javascript\"></script>"
				+ "	\n<script src=\"<c:url value='/resources/assets/js/ace.min.js'/>\""
				+ "	\n	type=\"text/javascript\"></script>" + "	\n<!-- inline scripts related to this page -->"

				+ autocomplete_script_latest + line_script + dropdown_script + dependent_dropdown + autocomplete_script
				+ "</html>");

//-------------------------------------jsp prefield update----------------------------------------

		importsectionprefield
				.append("\n<%@ page language=\"java\" contentType=\"text/html; charset=UTF-8\" pageEncoding=\"UTF-8\"%>"

						+ "\n<%@ page import=\"java.util.ArrayList\"%>" + "\n<%@ page import=\"java.util.Date\"%>"
						+ "\n<%@ taglib prefix=\"c\" uri=\"http://java.sun.com/jsp/jstl/core\"%>"
						+ "\n<%@ taglib uri=\"http://java.sun.com/jsp/jstl/functions\" prefix=\"fn\"%>"
						+ "\n<%@ taglib uri=\"http://java.sun.com/jsp/jstl/fmt\" prefix=\"fmt\"%>");

		headsectionprefield.append("\n<html lang=\"en\">\n<head>"
				+ "\n<meta http-equiv=\"X-UA-Compatible\"  content=\"IE=edge,chrome=1\">"
				+ "\n<meta charset=\"utf-8\" />" + "\n<title>Realnet Oil Engines Ltd</title>"
				+ "\n<meta name=\"description\" content=\"Common form elements and layouts\" />"
				+ "\n<meta name=\"viewport\""
				+ "\ncontent=\"width=device-width, initial-scale=1.0, maximum-scale=1.0\" />"
				+ "\n<!-- bootstrap & fontawesome -->" + "\n<link rel=\"stylesheet\""
				+ "\n href=\"<c:url value='/resources/assets/css/bootstrap.min.css'/>\" />"
				+ "\n<link rel=\"stylesheet\""
				+ "\nhref=\"<c:url value='/resources/assets/font-awesome/4.2.0/css/font-awesome.min.css'/>\" />"
				+ "\n<!-- page specific plugin styles -->" + "\n<link rel=\"stylesheet\""
				+ "\nhref=\"<c:url value='/resources/assets/css/jquery-ui.custom.min.css' />\" />"
				+ "\n<link rel=\"stylesheet\"" + "\nhref=\"<c:url value='/resources/assets/css/chosen.min.css' />\" />"
				+ "\n<link rel=\"stylesheet\""
				+ "\nhref=\"<c:url value='/resources/assets/css/datepicker.min.css'/>\" />"
				+ "\n<link rel=\"stylesheet\""
				+ "\nhref=\"<c:url value='/resources/assets/css/bootstrap-timepicker.min.css'/>\" />"
				+ "\n<link rel=\"stylesheet\""
				+ "\nhref=\"<c:url value='/resources/assets/css/daterangepicker.min.css' />\" />"
				+ "\n<link rel=\"stylesheet\""
				+ "\nhref=\"<c:url value='/resources/assets/css/bootstrap-datetimepicker.min.css'/>\" />"
				+ "\n<link rel=\"stylesheet\""
				+ "\nhref=\"<c:url value='/resources/assets/css/colorpicker.min.css' />\" />" + "\n<!-- text fonts -->"
				+ "\n<link rel=\"stylesheet\""
				+ "\n\nhref=\"<c:url value='/resources/assets/fonts/fonts.googleapis.com.css' />\" />"
				+ "\n<!-- ace styles -->" + "\n<link rel=\"stylesheet\""
				+ "\nhref=\"<c:url value='/resources/assets/css/ace.min.css\" class=\"ace-main-stylesheet\" id=\"main-ace-style' />\" />"
				+ "\n<!-- inline styles related to this page -->"

				+ "\n<!-- ace settings handler -->"
				+ "\n<script src=\"<c:url value='/resources/assets/js/ace-extra.min.js'/>\""
				+ "\ntype=\"text/javascript\"></script>"

				+ "\n<script>" + "\nsubmitForms = function()" + "\n{" + "\ndocument.forms[\"userdetails1\"].submit();"
				+ "\ndocument.forms[\"userdetails2\"].submit();" + "}" + "\n</script> "

				+ "\n\n<!-- MultiSelect DropDown Scripts  -->"
				+ "\n<script src=\"https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js\"></script>"
				+ "\n<script src=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.4.0/js/bootstrap.min.js\"></script>"
				+ " \n<script src=\"multiselect.min.js\"></script>"
				+ "   \n <script src=\"http://ajax.aspnetcdn.com/ajax/modernizr/modernizr-2.8.3.js\"></script>"
				+ "    \n<script src=\"http://code.jquery.com/jquery-1.11.3.min.js\"></script>"
				+ "    \n<script src=\"http://code.jquery.com/ui/1.11.1/jquery-ui.min.js\"></script>"
				+ "    \n<script src=\"http://cdn-na.infragistics.com/igniteui/2019.2/latest/js/infragistics.core.js\"></script>"
				+ "   \n <script src=\"http://cdn-na.infragistics.com/igniteui/2019.2/latest/js/infragistics.lob.js\"></script>"
				+ " " + " " + "<!-- Ignite UI Required Combined CSS Files -->"
				+ "  \n <link href=\"http://cdn-na.infragistics.com/igniteui/2019.2/latest/css/themes/infragistics/infragistics.theme.css\" rel=\"stylesheet\" />"
				+ "   \n<link href=\"http://cdn-na.infragistics.com/igniteui/2019.2/latest/css/structure/infragistics.css\" rel=\"stylesheet\" /> "

				+ " \n</head>");

		for (int i21 = 0; i21 < section_values.size(); i21++)

		{
			String field_nameS = section_values.get(i21).getFieldName();

			int sectionNum = section_values.get(i21).getSection_num();

			int secTemp = sectionNum;

			int section_num1 = sectionNum;

			int count = 1;

			System.out.println("count Value in section_values loop :: " + count);

			System.out.println("secTemp Value in section_values loop :: " + section_num1);
			//List<Rn_Fb_Line> sectionNumFields = rn_wireframe_dao.wf_section_num_fields(f_code, section_num1);
			List<Rn_Fb_Line> sectionNumFields = wireFrameService.getSectionFields(section_num1, id);

			System.out.println("secTemp Value in section_values loop :: " + secTemp);

			if (sectionNum == 1) {
				form_id.append("\n<input   value=\"${" + table_name_lower + "_update." + field_name_for_id_lower
						+ "}\"type=\"hidden\" name=\"" + field_name_for_id_lower + "\"id=\"" + field_name_for_id_lower
						+ "\"class=\"col-xs-12 col-sm-4\" />");

				form_prefield.append(
						"\n<form action=\"" + action2 + "\" class=\"form-horizontal\" id=\"Regi\" method=\"Post\">"

								+ "");

				/*
				 * form_prefield2.append("\n<form action=\""
				 * +action3+"\" class=\"form-horizontal\" id=\"Regi\" method=\"Post\">"
				 * 
				 * + "");
				 */
			}

			form_prefield.append("\n<div class=\"table-header\" style=\"margin-bottom: 30px; margin-top: 30px;\"> "
					+ field_nameS + "</div>");
			form_prefield.append(" \n<table>" + "\n<c:forEach var=\"" + table_name_lower + "_update\" items=\"${"
					+ table_name_lower + "_update}\" varStatus=\"status\">" + "\n<input type=\"hidden\" name=\""
					+ field_name_for_id + "\" id=\"" + field_name_for_id + "\" value=\"${" + table_name_lower
					+ "_update.id}\" />" + "\n<tr>");
			if (sectionNum == 1) {
				form_id.append(form_id);
			}
			form_prefield.append("\n</tr>");

			form_prefield.append("\n<tr>");
			for (int i = 0; i < id_notprimary.size(); i++) {
				String field_name1 = id_notprimary.get(i).getFieldName();
				String field_name = id_notprimary.get(i).getMapping();
				String data_type = id_notprimary.get(i).getDataType();
				String lower_case = field_name.toLowerCase();
				int fieldNum = id_notprimary.get(i).getSection_num();

				if (data_type.equals("int") && sectionNum == fieldNum) {
					form_prefield.append("\n<td>"
							+ "\n<div class=\"form-group\" style=\"margin-left: 10%; margin-right: 10%;\">"
							+ "\n<label class=\"control-label col-xs-12 col-sm-3 no-padding-right\">" + field_name1

							+ "\n</label>" + "\n<div class=\"col-xs-12 col-sm-9\">" + "\n<div class=\"clearfix\">"
							+ "\n<input   value=\"${" + table_name_lower + "_update." + lower_case
							+ "}\"type=\"text\" name=\"" + lower_case + "\"id=\"" + lower_case
							+ "\"class=\"col-xs-12 col-sm-4\" />" + "\n</div>" + "\n</div>" + "\n</div>" + "\n</td>");
				}
			}
			form_prefield.append("\n</tr>");

			form_prefield.append("\n<tr>");

			for (int i = 0; i < datetime_list.size(); i++) {

				String field_name = datetime_list.get(i).getFieldName();
				String data_type = datetime_list.get(i).getDataType();
				int fieldNum = datetime_list.get(i).getSection_num();

//				String mandatory = datetime_list.get(i).getMandatory();
//				String readonly = datetime_list.get(i).getReadonly();
//				String hidden = datetime_list.get(i).getHidden();
				boolean mandatory = datetime_list.get(i).isMandatory();
				boolean readonly = datetime_list.get(i).isReadonly();
				boolean hidden = datetime_list.get(i).isHidden();

				String mapping = datetime_list.get(i).getMapping();
				String mapping_lower = mapping.toLowerCase();

				System.out.println("-----value of mantadoryr laest-----" + mandatory);

				if (data_type.equals("datetime") && sectionNum == fieldNum) {

					System.out.println("---under if condition loop latest------");

					form_prefield.append(
							"\n<td>" + "\n<div class=\"form-group\" style=\"margin-left: 10%; margin-right: 10%;\">"
									+ "\n<label class=\"control-label col-xs-12 col-sm-3 no-padding-right\">" + "\n "
									+ field_name);

					System.out.println("-------------mandatory for testing=------------------" + mandatory);

					if (mandatory == true) {
						System.out.println("-------------in loop 1-------------------");
						form_prefield.append("\n <i class=\"menu-icon fa red\"> *</i>");
					}

					form_prefield
							.append("\n</label>" + "\n<div class=\"col-xs-12 col-sm-9\">" + "\n<div class=\"clearfix\">"
									+ "<div class=\"input-group input-append date\" id=\"datePicker" + i + "\">");

					form_prefield.append("\n<input");

					if (hidden == true) {
						form_prefield.append("  type=\"hidden\" ");
					} else {
						form_prefield.append("  type=\"text\" ");
					}

					form_prefield.append("name=" + mapping_lower + "  value=\"${" + table_name_lower + "_update."
							+ mapping_lower + "}\"  id=" + mapping_lower
							+ " placeholder=\"picup Date\" class=\"form-control\"");

					if (mandatory == true) {
						System.out.println("-------------in loop 1 part 2 required-------------------");
						form_prefield.append("required");
					}

					if (readonly == true) {
						System.out.println("-------------in loop 1 part 2 required-------------------");
						form_prefield.append("  readonly");
					}
					form_prefield.append(
							"/>\n" + "\n<span class=\"input-group-addon\"> <i class=\"fa fa-calendar bigger-110\"></i>"
									+ "\n</span>" + "\n</div>" + "\n</div>" + "\n</div>" + "\n</div>" + "\n</td>");
				}

			}

			form_prefield.append("\n</tr>");

			form_prefield.append("\n<tr>");

			for (int i = 0; i < sectionNumFields.size(); i++) {
				String field_name4 = sectionNumFields.get(i).getFieldName();
				String mapping4 = sectionNumFields.get(i).getMapping();
				String data_type4 = sectionNumFields.get(i).getDataType();

				int fieldNum = sectionNumFields.get(i).getSection_num();

				System.out.println("field name-------" + field_name4);
				System.out.println("mapping-------" + mapping4);
				System.out.println("data type-------" + data_type4);

				System.out.println("field name-------" + field_name4);
				System.out.println("mapping-------" + mapping4);
				System.out.println("data type-------" + data_type4);

//				String mandatory = sectionNumFields.get(i).getMandatory();
//				String hidden = sectionNumFields.get(i).getHidden();
				boolean mandatory = sectionNumFields.get(i).isMandatory();
				boolean hidden = sectionNumFields.get(i).isHidden();

				String type_field = sectionNumFields.get(i).getType_field();

				int dividend = count;
				int divisor = 3;
				int remainder = dividend % divisor;

				System.out.println("remainder::" + remainder);
				System.out.println("before count::" + count);
				count++;
				System.out.println(
						"\n\n\n\n\n\n\n update loop ::\nsection count::" + sectionNum + " \nafter count::" + count);

				// if(i<=2)
				// {
				if (type_field.equals("textfield") && sectionNum == fieldNum) {
					form_prefield.append(
							"\n<td>" + "\n<div class=\"form-group\" style=\"margin-left: 10%; margin-right: 10%;\">"
									+ "\n<label class=\"control-label col-xs-12 col-sm-3 no-padding-right\">" + "\n "
									+ field_name4);

					System.out.println("-------------mandatory for testing=------------------" + mandatory);

					if (mandatory == true) {
						System.out.println("-------------in loop 1-------------------");
						form_prefield.append("\n <i class=\"menu-icon fa red\"> *</i>");
					}

					form_prefield.append(
							"\n</label>" + "\n<div class=\"col-xs-12 col-sm-9\">" + "\n<div class=\"clearfix\">");
					form_prefield.append("\n<input ");

					if (hidden == true) {
						form_prefield.append("  type=\"hidden\" ");
					} else {
						form_prefield
								.append("  value=\"${" + table_name_lower + "_update." + mapping4 + "}\" type=\"text\" ");
					}

					form_prefield.append("name=" + mapping4 + " id=" + mapping4 + "  class=\"form-control\"");

					if (mandatory == true) {
						System.out.println("-------------in loop 1 part 2 required-------------------");
						form_prefield.append("required");
					}

					form_prefield.append("/>\n</div>" + "\n</div>" + "\n</div>" + "\n</td>");
				}

				if (type_field.equals("url") && sectionNum == fieldNum) {
					form_prefield.append(
							"\n<td>" + "\n<div class=\"form-group\" style=\"margin-left: 10%; margin-right: 10%;\">"
									+ "\n<label class=\"control-label col-xs-12 col-sm-3 no-padding-right\">" + "\n "
									+ field_name4);

					System.out.println("-------------mandatory for testing=------------------" + mandatory);

					if (mandatory == true) {
						System.out.println("-------------in loop 1-------------------");
						form_prefield.append("\n <i class=\"menu-icon fa red\"> *</i>");
					}

					form_prefield.append(
							"\n</label>" + "\n<div class=\"col-xs-12 col-sm-9\">" + "\n<div class=\"clearfix\">");
					form_prefield.append("\n<input   placeholder=\"URL\"pattern=\"https://.*\" size=\"30\" ");

					if (hidden == true) {
						form_prefield.append("  type=\"hidden\" ");
					} else {
						form_prefield
								.append("  value=\"${" + table_name_lower + "_update." + mapping4 + "}\" type=\"text\" ");
					}

					form_prefield.append("name=" + mapping4 + " id=" + mapping4 + "  class=\"form-control\"");

					if (mandatory == true) {
						System.out.println("-------------in loop 1 part 2 required-------------------");
						form_prefield.append("required");
					}

					form_prefield.append("/>\n</div>" + "\n</div>" + "\n</div>" + "\n</td>");
				}

				if (type_field.equals("email") && sectionNum == fieldNum) {
					form_prefield.append(
							"\n<td>" + "\n<div class=\"form-group\" style=\"margin-left: 10%; margin-right: 10%;\">"
									+ "\n<label class=\"control-label col-xs-12 col-sm-3 no-padding-right\">" + "\n "
									+ field_name4);

					System.out.println("-------------mandatory for testing=------------------" + mandatory);

					if (mandatory == true) {
						System.out.println("-------------in loop 1-------------------");
						form_prefield.append("\n <i class=\"menu-icon fa red\"> *</i>");
					}

					form_prefield.append(
							"\n</label>" + "\n<div class=\"col-xs-12 col-sm-9\">" + "\n<div class=\"clearfix\">");
					form_prefield.append("\n<input   placeholder=\"E_Mail\" onblur=\"validateEmail(this);\"");

					if (hidden == true) {
						form_prefield.append("  type=\"hidden\" ");
					} else {
						form_prefield.append(
								"  value=\"${" + table_name_lower + "_update." + mapping4 + "}\" type=\"email\" ");
					}

					form_prefield.append("name=" + mapping4 + " id=" + mapping4 + "  class=\"form-control\"");

					if (mandatory == true) {
						System.out.println("-------------in loop 1 part 2 required-------------------");
						form_prefield.append("required");
					}

					form_prefield.append("/>\n</div>" + "\n</div>" + "\n</div>" + "\n</td>");
				}

				if (type_field.equals("masked") && sectionNum == fieldNum) {
					form_prefield.append(
							"\n<td>" + "\n<div class=\"form-group\" style=\"margin-left: 10%; margin-right: 10%;\">"
									+ "\n<label class=\"control-label col-xs-12 col-sm-3 no-padding-right\">" + "\n "
									+ field_name4);

					System.out.println("-------------mandatory for testing=------------------" + mandatory);

					if (mandatory == true) {
						System.out.println("-------------in loop 1-------------------");
						form_prefield.append("\n <i class=\"menu-icon fa red\"> *</i>");
					}

					form_prefield.append(
							"\n</label>" + "\n<div class=\"col-xs-12 col-sm-9\">" + "\n<div class=\"clearfix\">");
					form_prefield.append("\n<input  placeholder=\"Masked\" maxlength=\"8\"");

					if (hidden == true) {
						form_prefield.append("  type=\"hidden\" ");
					} else {
						form_prefield.append(
								"  value=\"${" + table_name_lower + "_update." + mapping4 + "}\" type=\"password\" ");
					}

					form_prefield.append("name=" + mapping4 + " id=" + mapping4 + "  class=\"form-control\"");

					if (mandatory == true) {
						System.out.println("-------------in loop 1 part 2 required-------------------");
						form_prefield.append("required");
					}

					form_prefield.append("/>\n</div>" + "\n</div>" + "\n</div>" + "\n</td>");
				}

				if (type_field.equals("currency_field") && sectionNum == fieldNum) {
					form_prefield.append(
							"\n<td>" + "\n<div class=\"form-group\" style=\"margin-left: 10%; margin-right: 10%;\">"
									+ "\n<label class=\"control-label col-xs-12 col-sm-3 no-padding-right\">" + "\n "
									+ field_name4);

					System.out.println("-------------mandatory for testing=------------------" + mandatory);

					if (mandatory == true) {
						System.out.println("-------------in loop 1-------------------");
						form_prefield.append("\n <i class=\"menu-icon fa red\"> *</i>");
					}

					form_prefield.append(
							"\n</label>" + "\n<div class=\"col-xs-12 col-sm-9\">" + "\n<div class=\"clearfix\">");
					form_prefield.append("\n<input   placeholder=\"$1,000,000.00\" onblur=\"handleChange()\"");

					if (hidden == true) {
						form_prefield.append("  type=\"hidden\" ");
					} else {
						form_prefield
								.append("  value=\"${" + table_name_lower + "_update." + mapping4 + "}\" type=\"text\" ");
					}

					form_prefield.append("name=" + mapping4 + " id=" + mapping4 + "  class=\"form-control\"");

					if (mandatory == true) {
						System.out.println("-------------in loop 1 part 2 required-------------------");
						form_prefield.append("required");
					}

					form_prefield.append("/>\n</div>" + "\n</div>" + "\n</div>" + "\n</td>");
				}

				if (type_field.equals("contact_field") && sectionNum == fieldNum) {
					form_prefield.append(
							"\n<td>" + "\n<div class=\"form-group\" style=\"margin-left: 10%; margin-right: 10%;\">"
									+ "\n<label class=\"control-label col-xs-12 col-sm-3 no-padding-right\">" + "\n "
									+ field_name4);

					System.out.println("-------------mandatory for testing=------------------" + mandatory);

					if (mandatory == true) {
						System.out.println("-------------in loop 1-------------------");
						form_prefield.append("\n <i class=\"menu-icon fa red\"> *</i>");
					}

					form_prefield
							.append("\n</label>" + "\n<div class=\"col-xs-12 col-sm-9\">" + "\n<div class=\"clearfix\">"

									+ "\n<div style=\"position:relative\">" + "\n<select style=\"   height:\";\" >"
									+ "\n<option>+91</option>" + "\n<option>+44</option>" + "\n<option>+64</option>"
									+ "\n<option>+21</option>" + "\n<option>+244</option>" + "\n<option>+376</option>"
									+ "\n<option>+354</option>" + "\n<option>+291</option>" + "\n<option>+54</option>"
									+ "\n<option>+264</option>" + "\n<option>+121</option>" + "\n</select>");
					form_prefield.append("\n<input   pattern=\"[0-9]{10}\"");

					if (hidden == true) {
						form_prefield.append("  type=\"hidden\" ");
					} else {
						form_prefield
								.append("  value=\"${" + table_name_lower + "_update." + mapping4 + "}\" type=\"tel\" ");
					}
				
					form_prefield.append("name=" + mapping4 + " id=" + mapping4 + "  class=\"form-control\"");

					if (mandatory == true) {
						System.out.println("-------------in loop 1 part 2 required-------------------");
						form_prefield.append("required");
					}

					form_prefield.append("/>\n</div>" + "\n</div>" + "\n</div>" + "\n</div>" + "\n</td>");
				}

				if (type_field.equals("upload_field") && sectionNum == fieldNum) {
					form_prefield.append(
							"\n<td>" + "\n<div class=\"form-group\" style=\"margin-left: 10%; margin-right: 10%;\">"
									+ "\n<label class=\"control-label col-xs-12 col-sm-3 no-padding-right\">" + "\n "
									+ field_name4);

					System.out.println("-------------mandatory for testing=------------------" + mandatory);

					if (mandatory == true) {
						System.out.println("-------------in loop 1-------------------");
						form_prefield.append("\n <i class=\"menu-icon fa red\"> *</i>");
					}

					form_prefield.append(
							"\n</label>" + "\n<div class=\"col-xs-12 col-sm-9\">" + "\n<div class=\"clearfix\">");

					/* + "\n<div class=\"inputWithIcon\">"); */

					form_prefield.append("\n<input value=\"${" + table_name_lower + "_update." + mapping4
							+ "}\"   placeholder=\"Upload File\" style=\"width:100%; padding-left:10px;\"");
					if (hidden == true) {
						form_prefield.append("  type=\"hidden\" ");
					} else {
						form_prefield
								.append("  value=\"${" + table_name_lower + "_update." + mapping4 + "}\" type=\"file\" ");
					}

					form_prefield.append("name=" + mapping4 + " id=" + mapping4 + "  class=\"form-control\"");

					if (mandatory == true) {
						System.out.println("-------------in loop 1 part 2 required-------------------");
						form_prefield.append("required");
					}

					form_prefield
							.append("/>\n" + " <c:out value=\"${" + table_name_lower + "_update." + mapping4 + "}\"/>"

									+ "\n</div>" + "\n</div>" + "\n</div>" + "\n</td>");
				}

				if (type_field.equals("textarea") && sectionNum == fieldNum) {
					form_prefield.append(
							"\n<td>" + "\n<div class=\"form-group\" style=\"margin-left: 10%; margin-right: 10%;\">"
									+ "\n<label class=\"control-label col-xs-12 col-sm-3 no-padding-right\">" + "\n "
									+ field_name4);

					if (mandatory == true) {
						form_prefield.append("\n <i class=\"menu-icon fa red\"> *</i>");
					}

					form_prefield.append(
							"\n</label>" + "\n<div class=\"col-xs-12 col-sm-9\">" + "\n<div class=\"clearfix\">");
					form_prefield.append("\n<textarea rows=\"" + 0 + "\" cols=\"" + 34 + "\"  ");

					if (hidden == true) {
						form_prefield.append("  type=\"hidden\" ");
					} else {
						form_prefield.append(
								"   value=\"${" + table_name_lower + "_update." + mapping4 + "}\"  type=\"text\" ");
					}

					form_prefield.append("name=" + mapping4 + " id=" + mapping4 + "  class=\"form-control\"");

					if (mandatory == true) {
						form_prefield.append("required");
					}

					form_prefield.append("/>${" + table_name_lower + "_update." + mapping4 + "}</textarea>\n</div>"
							+ "\n</div>" + "\n</div>" + "\n</td>");
				}

				if (type_field.equals("datetime") && sectionNum == fieldNum) {
					form_prefield.append(
							"\n<td>" + "\n<div class=\"form-group\" style=\"margin-left: 10%; margin-right: 10%;\">"
									+ "\n<label class=\"control-label col-xs-12 col-sm-3 no-padding-right\">" + "\n "
									+ field_name4);

					System.out.println("-------------mandatory for testing=------------------" + mandatory);

					if (mandatory == true) {
						System.out.println("-------------in loop 1-------------------");
						form_prefield.append("\n <i class=\"menu-icon fa red\"> *</i>");
					}

					form_prefield
							.append("\n</label>" + "\n<div class=\"col-xs-12 col-sm-9\">" + "\n<div class=\"clearfix\">"
									+ "<div class=\"input-group input-append date\" id=\"datePicker\">");

					form_prefield.append("\n<input  ");

					if (hidden == true) {
						form_prefield.append("  type=\"hidden\" ");
					} else {
						form_prefield.append(
								"   value=\"${" + table_name_lower + "_update." + mapping4 + "}\"  type=\"text\" ");
					}

					form_prefield.append("name=" + mapping4 + " id=" + mapping4
							+ " placeholder=\"picup Date\" class=\"form-control\"");

					if (mandatory == true) {
						System.out.println("-------------in loop 1 part 2 required-------------------");
						form_prefield.append("required");
					}

					form_prefield.append(
							"/>\n" + "\n<span class=\"input-group-addon\"> <i class=\"fa fa-calendar bigger-110\"></i>"
									+ "\n</span>" + "\n</div>" + "\n</div>" + "\n</div>" + "\n</div>" + "\n</td>");
				}

				if (type_field.equals("autocomplete") && sectionNum == fieldNum) {
					form_prefield.append(
							"\n<td>" + "\n<div class=\"form-group\" style=\"margin-left: 10%; margin-right: 10%;\">"
									+ "\n<label class=\"control-label col-xs-12 col-sm-3 no-padding-right\">" + "\n "
									+ field_name4);

					System.out.println("-------------mandatory for testing=------------------" + mandatory);

					if (mandatory == true) {
						System.out.println("-------------in loop 1-------------------");
						form_prefield.append("\n <i class=\"menu-icon fa red\"> *</i>");
					}

					form_prefield.append(
							"\n</label>" + "\n<div class=\"col-xs-12 col-sm-9\">" + "\n<div class=\"clearfix\">");

					form_prefield.append("\n<input   placeholder=\"AutoComplete\"");

					if (hidden == true) {
						form_prefield.append(
								" value=\"${" + table_name_lower + "_update." + mapping4 + "}\"  type=\"hidden\" ");
					} else {
						form_prefield
								.append("  value=\"${" + table_name_lower + "_update." + mapping4 + "}\" type=\"text\" ");
					}
					/*
					 * if(calculated_field.equals("Y")) { form_prefield.append(
					 * "name=\"total\" id=\"total\" class=\"stunden form-control\" "); } else {
					 * form_prefield.append( "name="+mapping_lower_case+" id=\""
					 * +mapping_lower_case+"\" class=\"col-xs-12 col-sm-4\" "); }
					 */

					form_prefield.append("name=" + mapping4 + " id=" + mapping4 + "  class=\"form-control\"");

					if (mandatory == true) {
						System.out.println("-------------in loop 1 part 2 required-------------------");
						form_prefield.append("required");
					}

					form_prefield.append("/>\n</div>" + "\n</div>" + "\n</div>" + "\n</td>");

				}

				if (type_field.equals("dropdown") && sectionNum == fieldNum) {
					form_prefield.append(
							"\n<td>" + "\n<div class=\"form-group\" style=\"margin-left: 10%; margin-right: 10%;\">"
									+ "\n<label class=\"control-label col-xs-12 col-sm-3 no-padding-right\">" + "\n "
									+ field_name4);

					if (mandatory == true) {
						form_prefield.append("\n <i class=\"menu-icon fa red\"> *</i>");
					}

					form_prefield.append("\n</label>" + "\n<div class=\"col-xs-12 col-sm-9\">");

					form_prefield.append(
							"\n<input name=\"" + mapping4 + "\" id=\"" + mapping4 + "\"  value=\"${" + table_name_lower
									+ "_update." + mapping4 + "}\"  class=\"col-xs-3 col-sm-3 form-control state\"");

					if (mandatory == true) {
						System.out.println("-------------in loop 1 part 2 required-------------------");
						form_prefield.append("required");
					}

					form_prefield.append("\n/></div>"

							+ "\n</div>" + "\n</td>");

				}

				if (type_field.equals("togglebutton") && sectionNum == fieldNum) {
					form_prefield.append(
							"\n<td>" + "\n<div class=\"form-group\" style=\"margin-left: 10%; margin-right: 10%;\">"
									+ "\n<label class=\"control-label col-xs-12 col-sm-3 no-padding-right\">" + "\n "
									+ field_name4);

					System.out.println("-------------mandatory for testing=------------------" + mandatory);

					if (mandatory == true) {
						System.out.println("-------------in loop 1-------------------");
						form_prefield.append("\n <i class=\"menu-icon fa red\"> *</i>");
					}

					form_prefield.append(
							"\n</label>" + "\n<div class=\"col-xs-12 col-sm-9\">" + "\n<div class=\"clearfix\">");

					form_prefield.append("\n<input");

					if (hidden == true) {
						form_prefield.append("  type=\"hidden\" ");
					} else {
						form_prefield.append("  type=\"checkbox\" ");
					}

					form_prefield.append(" value=\"${" + table_name_lower + "_update." + mapping4 + "}\" name=" + mapping4
							+ " id=" + mapping4 + "  class=\"ace ace-switch ace-switch-6\" onblur=\"CheckUserStatus"
							+ mapping4 + "()\"");

					if (mandatory == true) {
						System.out.println("-------------in loop 1 part 2 required-------------------");
						form_prefield.append("required");
					}

					form_prefield.append("/>\n" + "\n<span class=\"lbl\"></span>"

							+ "\n</div>" + "\n</div>" + "\n</div>" + "\n</td>");
				}

				if (type_field.equals("checkbox") && sectionNum == fieldNum) {
					form_prefield.append(
							"\n<td>" + "\n<div class=\"form-group\" style=\"margin-left: 10%; margin-right: 10%;\">"
									+ "\n<label class=\"control-label col-xs-12 col-sm-3 no-padding-right\">" + "\n "
									+ field_name4);

					System.out.println("-------------mandatory for testing=------------------" + mandatory);

					if (mandatory == true) {
						System.out.println("-------------in loop 1-------------------");
						form_prefield.append("\n <i class=\"menu-icon fa red\"> *</i>");
					}

					form_prefield.append(
							"\n</label>" + "\n<div class=\"col-xs-12 col-sm-9\">" + "\n<div class=\"clearfix\">");

					form_prefield.append("\n<input  style=\"width:10%; margin-left:20px\" class=\"form-control\"");

					if (hidden == true) {
						form_prefield.append("  type=\"hidden\" ");
					} else {
						form_prefield.append("  type=\"checkbox\" ");
					}

					form_prefield.append(" value=\"${" + table_name_lower + "_update." + mapping4 + "}\" name=" + mapping4
							+ " id=" + mapping4 + "  onblur=\"CheckUserStatus" + mapping4 + "()\"");

					if (mandatory == true) {
						System.out.println("-------------in loop 1 part 2 required-------------------");
						form_prefield.append("required");
					}

					form_prefield.append("/>\n" + "\n</div>" + "\n</div>" + "\n</div>" + "\n</td>");
				}
				// }

				if (remainder == 0) {
					form_prefield.append("\n</tr>");
					form_prefield.append("\n<tr>");
				}
			}

			// for loop close

			form_prefield.append("\n</tr>" + "\n</c:forEach>");

			form_prefield.append("\n</table>");

			if (line_table_name != null && !line_table_name.isEmpty()) {
				String line_table_name_lower = line_table_name.toLowerCase();
				String line_table_name_first_upper = line_table_name_lower.substring(0, 1).toUpperCase()
						+ line_table_name_lower.substring(1);
				form_prefield.append("\n\n<%@include file=\"/WEB-INF/tiles/acemaster/" + module_name + "/"
						+ line_table_name_first_upper + "_update.jsp\"%>");
			}

		} // section if condition end

		form_prefield.append("\n<c:forEach var=\"" + table_name_lower + "_update\" items=\"${" + table_name_lower
				+ "_update}\" varStatus=\"status\">");
		form_prefield.append("\n\n<%@include file=\"/WEB-INF/tiles/acemaster/" + module_name + "/" + table_name_lower
				+ "_ext_Update.jsp\"%>" + "\n</c:forEach>");

		form_prefield.append("\n" + "<div class=\"hr hr-dotted\"></div>" + "\n<div class=\"wizard-actions\">"
				+ "\n<button type=\"submit\" class=\"btn btn-success center\" onclick=\"submitForms()\">" + "\nUpdate"
				+ "\n</button>" + "\n</div> ");

		form_prefield.append("\n</form>");

		strContentprefield.append(importsectionprefield + " \n" + headsectionprefield
				+ "\n<body>\n"
				// UPDATE FORM FORM_CODE
				+ "<input type=\"hidden\" name=\"form_code\" id=\"form_code\" value=\"" + form_code + "\" />\n"
				+ "<div class=\"main-container\" id=\"main-container\">" + "\n<div class=\"main-content\">"
				+ "\n<div class=\"main-content-inner\">" + "\n<div class=\"breadcrumbs\" id=\"breadcrumbs\">"
				+ "\n<ul class=\"breadcrumb\">"
				+ "\n<li><i class=\"ace-icon fa fa-home home-icon\"></i> <a href=\"#\">Home</a>" + "\n</li>"

				+ "\n<li><a href=\"#\">ManageUsers</a></li>" + "\n<li class=\"active\">" + ui_name + "</li>" + "\n</ul>"
				+ "\n</div>"

				+ "\n<div class=\"page-content\">" + "\n<div class=\"page-header\">" + "\n<h1>" + ui_name
				+ " \n <div style=\"float: right; padding-right: 5%;\">"
				+ " \n <a href=\"#myModel\" id=\"${rn_rb_reports_t_update.id}\">"
				+ "  \n  <i class=\"fa fa-ticket\" aria-hidden=\"true\""
				+ "	 \n  data-toggle=\"modal\" data-target=\"#myModal\">" + "	 \n  </i>" + " \n</a>" + "\n</div>"

				+ "\n</h1>"
				// ADD EXTRA BUTTON JSP CODE HERE
				+ cff_add_button_code_in_update_jsp + "\n</div>"

				+ "\n<div class=\"row\">" + "\n<div class=\"col-xs-12\">"

				+ "\n<div class=\"widget-body\">" + "\n<div class=\"widget-main\">"
				+ "\n<div id=\"fuelux-wizard-container\">" + "\n<div class=\"step-content pos-rel\">"
				+ "\n<div class=\"step-pane active\" data-step=\"1\">" + form_prefield
				+ "\n<div class=\"modal fade\"  id=\"myModal\" role=\"dialog\">" + "\n<div class=\"modal-dialog\">"
				+ "\n	<!-- Modal content-->" + "\n	<div class=\"modal-content\">"
				+ "\n		<div class=\"modal-header\">" + "\n			<button type=\"button\" class=\"close\""
				+ "\n				data-dismiss=\"modal\">&times;</button>" + "	\n	</div>"
				+ "	\n	<input type=\"hidden\" name=\"test\" id=\"test\" >"

				+ "\n<c:forEach var=\"" + table_name_lower + "_update\" items=\"${" + table_name_lower
				+ "_update}\" varStatus=\"status\">"

				+ "\n			<input value=\"${rn_rb_reports_t_update.id}\""
				+ "\n				type=\"hidden\" name=\"id\" id=\"id\""
				+ "\n				class=\"col-xs-12 col-sm-4\" />"

				+ "	\n		<div class=\"modal-body mx-3\">" + "		\n		<div class=\"md-form mb-5\">"
				+ "	\n				<label data-error=\"wrong\" data-success=\"right\">Account</label>"
				+ "	\n				<input type=\"text\" id=\"acc_id\" name=\"acc_id\""
				+ "	\n					class=\"form-control validate\"" + "	\n					value=\"${"
				+ table_name_lower + "_update.account_id}\" readonly>" + "	\n			</div>" + "	\n		</div>"
				+ "	\n		<div class=\"modal-body mx-3\">" + "	\n			<div class=\"md-form mb-5\">"
				+ "	\n				<label data-error=\"wrong\" data-success=\"right\">Form_Name</label>"
				+ "	\n				<input type=\"text\" id=\"form_name\" name=\"form_name\""
				+ "	\n					class=\"form-control validate\"" + "	\n					 readonly>"
				+ "	\n			</div>" + "	\n		</div>" + "	\n		<div class=\"modal-body mx-3\">"
				+ "	\n			<div class=\"md-form mb-5\">"
				+ "	\n				<label data-error=\"wrong\" data-success=\"right\">Form_Code</label>"
				+ "	\n				<input type=\"text\" id=\"form_code\" name=\"form_code\""
				+ "	\n					class=\"form-control validate\"" + "	\n					 readonly>"
				+ "	\n			</div>" + "	\n		</div>" + "	\n		<div class=\"modal-body mx-3\">"
				+ "	\n			<div class=\"md-form mb-5\">"
				+ "	\n				<label data-error=\"wrong\" data-success=\"right\">Created_By</label>"
				+ "	\n				<input type=\"text\" id=\"created_by\""
				+ "	\n					name=\"created_by\" class=\"form-control validate\""
				+ "	\n					value=\"${" + table_name_lower + "_update.created_by}\""
				+ "	\n					readonly>" + "	\n			</div>" + "	\n		</div>"
				+ "	\n		<div class=\"modal-body mx-3\">" + "	\n			<div class=\"md-form mb-5\">"
				+ "	\n				<label data-error=\"wrong\" data-success=\"right\">Creation_Date</label>"
				+ "	\n				<input type=\"text\" id=\"creation_date\""
				+ "	\n					name=\"creation_date\" class=\"form-control validate\""
				+ "	\n					value=\"${" + table_name_lower + "_update.creation_date}\""
				+ "	\n					readonly>" + "	\n			</div>" + "	\n		</div>"
				+ "	\n		<div class=\"modal-body mx-3\">" + "	\n			<div class=\"md-form mb-5\">"
				+ "	\n				<label data-error=\"wrong\" data-success=\"right\">Last_Updated_by</label>"
				+ "	\n				<input type=\"text\" id=\"last_updated_by\""
				+ "	\n					name=\"last_updated_by\""
				+ "	\n					class=\"form-control validate\"" + "	\n					value=\"${"
				+ table_name_lower + "_update.last_updated_by}\"" + "	\n					readonly>"
				+ "	\n			</div>" + "	\n		</div>" + "	\n		<div class=\"modal-body mx-3\">"
				+ "	\n			<div class=\"md-form mb-5\">"
				+ "	\n				<label data-error=\"wrong\" data-success=\"right\">Last_Update_Date</label>"
				+ "	\n				<input type=\"text\" id=\"last_update_date\""
				+ "	\n					name=\"last_update_date\""
				+ "	\n					class=\"form-control validate\"" + "	\n					value=\"${"
				+ table_name_lower + "_update.last_update_date}\"" + "	\n					readonly>"
				+ "	\n			</div>" + "	\n		</div>" + "\n</c:forEach>"
				+ "	\n	<div class=\"modal-body\"></div>" + "	\n	<div class=\"modal-footer\">"
				+ "	\n		<button type=\"button\" class=\"btn btn-default\""
				+ "	\n			data-dismiss=\"modal\">Close</button>" + "	\n	</div>" + "	\n	</div>" + "\n</div>"
				+ "\n</div>"

				+ "\n<div class=\"modal-body mx-3\">"

				+ "\n</div>" + "\n</div>" + "\n</div>" + "\n</div>" + "\n</div>" + "\n</div>" + "\n</div>" + "\n</div>"
				+ "\n</div>" + "\n</div>" + "\n</div>" + "\n</div>\n"
				+ "\n<script src=\"<c:url value='/resources/assets/js/bootstrap-datepicker.min.js'/>\" type=\"text/javascript\">"
				+ "\n</script>"
				+ "<script src=\"<c:url value='cdnjs.cloudflare.com/ajax/libs/easy-autocomplete/1.3.5/jquery.easy-autocomplete.min.js'/>\" type=\"text/javascript\">"

				+ "\n</script>" + "\n<script>" + date_script + "\n</script>" + "\n" + line_script_update + "<script>"
				+ date_script + dropdown_script_latest + dropdown_script_latest1

				+ togglescript + "</script>"

				+ "\n<!-- ace scripts -->"
				+ "	\n<script src=\"<c:url value='/resources/assets/js/ace-elements.min.js'/>\""
				+ "		\ntype=\"text/javascript\"></script>"
				+ "	\n<script src=\"<c:url value='/resources/assets/js/ace.min.js'/>\""
				+ "	\n	type=\"text/javascript\"></script>" + "	\n<!-- inline scripts related to this page -->"

				+ autocomplete_script_latest
//							+ line_script 
				+ dropdown_script + dependent_dropdown + autocomplete_script + "\n</body>\n</html>");

		// ---------------------for production-----------------------------

		String devPath = environment.getRequiredProperty("devPath");

		List<Rn_Instance_Type> rn_instance_type_t = instanceTypeRepo.findAll();

		String instance_type = rn_instance_type_t.get(0).getInstanceType();

		System.out.println("instance type = " + instance_type);
		System.out.println("Dev path::" + devPath);

		if (instance_type.equals("Dev")) 
		{

			File temp22 = new File(devPath + "/WEB-INF/tiles.xml");
			File newtemp22 = new File(devPath + "/WEB-INF/xyz.xml");
			BufferedReader br22 = new BufferedReader(new FileReader(temp22));
			BufferedWriter bw22 = new BufferedWriter(new FileWriter(newtemp22));
			String removeStr22 = "</tiles-definitions>";
			String currentLine22;

			System.out.println(temp22.getName());
			while ((currentLine22 = br22.readLine()) != null) {
				String trimmedLine = currentLine22.trim();
				if (trimmedLine.equals(removeStr22)) {
					currentLine22 = "";
				}
				bw22.write(currentLine22 + System.getProperty("line.separator"));

			}
			bw22.close();
			br22.close();
			boolean delete22 = temp22.delete();
			boolean b22 = newtemp22.renameTo(temp22);

			StringBuilder tiles22 = new StringBuilder();

			try {

				tiles22.append(
						"\n<definition name=\"" + table_name_first_upper + "_grid\" extends=\"acemaster.definition\">"
								+ "\n<put-attribute name=\"title\" value=\"WASIB\"/>"
								+ "\n<put-attribute name=\"body\" value=\"/WEB-INF/tiles/acemaster/" + module_name + "/"
								+ table_name_first_upper + "_grid.jsp\"/>" + "\n</definition>");

				tiles22.append(
						"\n<definition name=\"" + table_name_first_upper + "_view\" extends=\"acemaster.definition\">"
								+ "\n<put-attribute name=\"title\" value=\"WASIB\"/>"
								+ "\n<put-attribute name=\"body\" value=\"/WEB-INF/tiles/acemaster/" + module_name + "/"
								+ table_name_first_upper + "_view.jsp\"/>" + "\n</definition>");

				tiles22.append(
						"\n<definition name=\"" + table_name_first_upper + "_update\" extends=\"acemaster.definition\">"
								+ "\n<put-attribute name=\"title\" value=\"WASIB\"/>"
								+ "\n<put-attribute name=\"body\" value=\"/WEB-INF/tiles/acemaster/" + module_name + "/"
								+ table_name_first_upper + "_update.jsp\"/>" + "\n</definition>");

				tiles22.append("\n<definition name=\"" + table_name_first_upper
						+ "_readonly\" extends=\"acemaster.definition\">"
						+ "\n<put-attribute name=\"title\" value=\"WASIB\"/>"
						+ "\n<put-attribute name=\"body\" value=\"/WEB-INF/tiles/acemaster/" + module_name + "/"
						+ table_name_first_upper + "_readonly.jsp\"/>" + "\n</definition>" + "\n</tiles-definitions>");

				String filename = devPath + "/WEB-INF/tiles.xml";

				FileWriter fw = new FileWriter(filename, true);
				fw.write(tiles22.toString());
				fw.close();

			} catch (IOException e) {
				e.printStackTrace();
			}
		} // DEV INSTANCE END
			// ====== ECLIPSE INSTANCE START ========
		else {
//			String tilespath = request.getServletContext().getRealPath("/WEB-INF");
//			File tilespath1 = new File(tilespath + "/tiles.xml");
//			System.out.println("TILES PATH = " + tilespath1);

			// NEED TO MODIFY.... ... (NILADRI'S CODE)
			File temp = new File(projectPath + "/Projects/" + project_name + "/src/main/webapp/WEB-INF/tiles.xml");
			File newtemp = new File(projectPath + "/Projects/" + project_name + "/src/main/webapp/WEB-INF/xyz.xml");
			
			// IF tiles.xml FILE IS NOT PRESENT
			if(!temp.exists()) {
				temp.createNewFile();
			}

//			File newTilespath = new File(tilespath + "/xyz.xml");

			// (NILADRI'S CODE)
			BufferedReader br = new BufferedReader(new FileReader(temp));
			BufferedWriter bw1 = new BufferedWriter(new FileWriter(newtemp));

			// (GANESH'S CODE)
//			BufferedReader br2 = new BufferedReader(new FileReader(tilespath1));
//			BufferedWriter bw2 = new BufferedWriter(new FileWriter(newTilespath));
			String removeStr = "</tiles-definitions>";
			String currentLine;
			String currentLine2;

			// (NILADRI'S CODE)
			System.out.println(temp.getName());
			while ((currentLine = br.readLine()) != null) {
				String trimmedLine = currentLine.trim();
				if (trimmedLine.equals(removeStr)) {
					currentLine = "";
				}
				bw1.write(currentLine + System.getProperty("line.separator"));
			}
			bw1.close();
			br.close();
			boolean delete = temp.delete();
			boolean b = newtemp.renameTo(temp);

//			// (GANESH'S CODE)
//			// for production
//			while ((currentLine2 = br2.readLine()) != null) {
//				String trimmedLine = currentLine2.trim();
//				if (trimmedLine.equals(removeStr)) {
//					currentLine2 = "";
//				}
//				bw2.write(currentLine2 + System.getProperty("line.separator"));
//
//			}
//			bw2.close();
//			br2.close();
//			boolean delete2 = tilespath1.delete();
//			boolean b2 = newTilespath.renameTo(tilespath1);

			StringBuilder tiles = new StringBuilder();

			try {
				tiles.append(
						"\n<definition name=\"" + table_name_first_upper + "_grid\" extends=\"acemaster.definition\">"
								+ "\n<put-attribute name=\"title\" value=\"WASIB\"/>"
								+ "\n<put-attribute name=\"body\" value=\"/WEB-INF/tiles/acemaster/" + module_name + "/"
								+ table_name_first_upper + "_grid.jsp\"/>" + "\n</definition>");

				tiles.append(
						"\n<definition name=\"" + table_name_first_upper + "_view\" extends=\"acemaster.definition\">"
								+ "\n<put-attribute name=\"title\" value=\"WASIB\"/>"
								+ "\n<put-attribute name=\"body\" value=\"/WEB-INF/tiles/acemaster/" + module_name + "/"
								+ table_name_first_upper + "_view.jsp\"/>" + "\n</definition>");

				tiles.append(
						"\n<definition name=\"" + table_name_first_upper + "_update\" extends=\"acemaster.definition\">"
								+ "\n<put-attribute name=\"title\" value=\"WASIB\"/>"
								+ "\n<put-attribute name=\"body\" value=\"/WEB-INF/tiles/acemaster/" + module_name + "/"
								+ table_name_first_upper + "_update.jsp\"/>" + "\n</definition>");

				tiles.append("\n<definition name=\"" + table_name_first_upper
						+ "_readonly\" extends=\"acemaster.definition\">"
						+ "\n<put-attribute name=\"title\" value=\"WASIB\"/>"
						+ "\n<put-attribute name=\"body\" value=\"/WEB-INF/tiles/acemaster/" + module_name + "/"
						+ table_name_first_upper + "_readonly.jsp\"/>" + "\n</definition>"

						+ "\n</tiles-definitions>");

				// (NILADRI'S CODE)
				String filename = projectPath + "/Projects/" + project_name + "/src/main/webapp/WEB-INF/tiles.xml";

				// (GANESH'S CODE)
//				String filename2 = tilespath + "/tiles.xml";

//				File tilesFile = new File(filename);
//				if(!tilesFile.getParentFile().exists()) {
//					tilesFile.getParentFile().mkdirs();
//					if(!tilesFile.exists()) {
//						tilesFile.createNewFile();
//					}
//				}
				// (NILADRI'S CODE)
				FileWriter fw = new FileWriter(filename, true);
				fw.write(tiles.toString());
				fw.close();

//				// (GANESH'S CODE)
//				FileWriter fw2 = new FileWriter(filename2, true);
//				fw2.write(tiles.toString());
//				fw2.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

		System.out.println("after tiles === " + instance_type);

		// FOR ECLIPSE INSTANCE TYPE
		if (instance_type.equals("Eclipse")) {

			try {
				// String moveToclasses =
				// request.getServletContext().getRealPath("/WEB-INF/classes");

//				String jspPath = request.getServletContext().getRealPath("/WEB-INF/tiles/acemaster");
//				jspPath = jspPath.replace("\\", "/");
				
				// projectPath + "/Projects/" + project_name + "/src/main/webapp/WEB-INF/tiles.xml";
				String jspPath = projectPath + "/Projects/" + project_name + "/src/main/webapp/WEB-INF/tiles/acemaster";
				jspPath = jspPath.replace("\\", "/");

				System.out.println("jsp real path::" + jspPath);

				// java files
//		        File file1 = new File(projectPath+"/"+project_name+"/src/main/java/com/realnet/"+module_name+"/controller/"+controller_name_first_upper+".java");
//		        File file2 = new File(projectPath+"/"+project_name+"/"+module_name+"/dao/"+dao_name_first_upper+".java");
//				File file3 = new File(projectPath+"/"+project_name+"/"+module_name+"/dao/"+dao_impl_name_first_upper+".java");
//		        File file4 = new File(projectPath+"/"+project_name+"/"+module_name+"/service/"+service_name_first_upper+".java");
//				File file5 = new File(projectPath+"/"+project_name+"/"+module_name+"/service/"+service_impl_name_first_upper+".java");
//		        File file7 = new File(projectPath+"/"+project_name+"/"+module_name+"/model/"+table_name_first_upper+".java");

				File file1 = new File(projectPath + "/Projects/" + project_name + "/src/main/java/com/realnet/"
						+ module_name + "/controller/" + controller_name_first_upper + ".java");
				File file2 = new File(projectPath + "/Projects/" + project_name + "/src/main/java/com/realnet/"
						+ module_name + "/dao/" + dao_name_first_upper + ".java");
				File file3 = new File(projectPath + "/Projects/" + project_name + "/src/main/java/com/realnet/"
						+ module_name + "/dao/" + dao_impl_name_first_upper + ".java");
				File file4 = new File(projectPath + "/Projects/" + project_name + "/src/main/java/com/realnet/"
						+ module_name + "/service/" + service_name_first_upper + ".java");
				File file5 = new File(projectPath + "/Projects/" + project_name + "/src/main/java/com/realnet/"
						+ module_name + "/service/" + service_impl_name_first_upper + ".java");
				File file7 = new File(projectPath + "/Projects/" + project_name + "/src/main/java/com/realnet/"
						+ module_name + "/model/" + table_name_first_upper + ".java");

				if (!file1.getParentFile().exists()) {
					boolean created = file1.getParentFile().mkdirs();
					if (created) {
						System.out.println("CONTROLLER PATH CREATED...\n" + file1.getPath());
					}
				}
				if (!file2.getParentFile().exists()) {
					boolean created = file2.getParentFile().mkdirs();
					if (created) {
						System.out.println("DAO PATH CREATED...\n" + file2.getPath());
					}
				}
				if (!file4.getParentFile().exists()) {
					boolean created = file4.getParentFile().mkdirs();
					if (created) {
						System.out.println("SERVICE PATH CREATED...\n" + file4.getPath());
					}
				}
				if (!file7.getParentFile().exists()) {
					boolean created = file7.getParentFile().mkdirs();
					if (created) {
						System.out.println("MODEL PATH CREATED...\n" + file7.getPath());
					}
				}

				// CRUD JSP IN PROJECTS FOLDER
				File file6 = new File(
						projectPath + "/Projects/" + project_name + "/src/main/webapp/WEB-INF/tiles/acemaster/"
								+ module_name + "/" + table_name_first_upper + "_view.jsp");

				File file8 = new File(
						projectPath + "/Projects/" + project_name + "/src/main/webapp/WEB-INF/tiles/acemaster/"
								+ module_name + "/" + table_name_first_upper + "_grid.jsp");
				File file9 = new File(
						projectPath + "/Projects/" + project_name + "/src/main/webapp/WEB-INF/tiles/acemaster/"
								+ module_name + "/" + table_name_first_upper + "_update.jsp");
				File file10 = new File(
						projectPath + "/Projects/" + project_name + "/src/main/webapp/WEB-INF/tiles/acemaster/"
								+ module_name + "/" + table_name_first_upper + "_readonly.jsp");

				if (!file6.getParentFile().exists()) {
					boolean created = file6.getParentFile().mkdirs();
					if (created) {
						System.out.println("JSP GRID VIEW PATH CREATED");
					}
				}

				// for production crud JSP (IN MAIN PROJECT FOR TESTING)
				File jspPath1 = new File(jspPath + "/" + module_name + "/" + table_name_first_upper + "_view.jsp");
				File jspPath3 = new File(jspPath + "/" + module_name + "/" + table_name_first_upper + "_update.jsp");
				File jspPath4 = new File(jspPath + "/" + module_name + "/" + table_name_first_upper + "_readonly.jsp");

				if (!jspPath1.getParentFile().exists()) {
					jspPath1.getParentFile().mkdirs();
				}

				System.out.println("jsp actual file path = \n" + jspPath1 + "\n" + jspPath3 + "\n" + jspPath4);

				// extension jsps
				File file15 = new File(
						projectPath + "/Projects/" + project_name + "/src/main/webapp/WEB-INF/tiles/acemaster/"
								+ module_name + "/" + table_name_lower + "_extension.jsp");
//		        File file20 = new File(projectPath+"/src/main/webapp/WEB-INF/tiles/acemaster/extpages/"+table_name_lower+"_extension2.jsp");
				File file18 = new File(
						projectPath + "/Projects/" + project_name + "/src/main/webapp/WEB-INF/tiles/acemaster/"
								+ module_name + "/" + table_name_lower + "_add_grid.jsp");
				File file19 = new File(
						projectPath + "/Projects/" + project_name + "/src/main/webapp/WEB-INF/tiles/acemaster/"
								+ module_name + "/" + table_name_lower + "_add_grid2.jsp");
				File file21 = new File(
						projectPath + "/Projects/" + project_name + "/src/main/webapp/WEB-INF/tiles/acemaster/"
								+ module_name + "/" + table_name_lower + "_ext_Update.jsp");
				File file22 = new File(
						projectPath + "/Projects/" + project_name + "/src/main/webapp/WEB-INF/tiles/acemaster/"
								+ module_name + "/" + table_name_lower + "_ext_Readonly.jsp");

				if (!file15.getParentFile().exists()) {
					file15.getParentFile().mkdirs();
				}

				// extension jsps
				File jspExt1 = new File(jspPath + "/" + module_name + "/" + table_name_lower + "_extension.jsp");
				File jspExt2 = new File(jspPath + "/" + module_name + "/" + table_name_lower + "_add_grid.jsp");
				File jspExt3 = new File(jspPath + "/" + module_name + "/" + table_name_lower + "_add_grid2.jsp");
				File jspExt4 = new File(jspPath + "/" + module_name + "/" + table_name_lower + "_ext_Update.jsp");
				File jspExt5 = new File(jspPath + "/" + module_name + "/" + table_name_lower + "_ext_Readonly.jsp");

				if (!jspExt1.getParentFile().exists()) {
					jspExt1.getParentFile().mkdirs();
				}

				System.out.println("file name in build form" + file1);

				if (!file1.exists()) {
					file1.createNewFile();
				}

				FileWriter fw = new FileWriter(file1.getAbsoluteFile());
				BufferedWriter bw = new BufferedWriter(fw);
				bw.write(controller.toString());
				bw.close();

				System.out.println("DoneController");

				if (!file15.exists()) {
					file15.createNewFile();

				}

				fw = new FileWriter(file15.getAbsoluteFile());
				bw = new BufferedWriter(fw);
				bw.write("");
				bw.close();

				if (!file18.exists()) {
					file18.createNewFile();

				}

				fw = new FileWriter(file18.getAbsoluteFile());
				bw = new BufferedWriter(fw);
				bw.write("");
				bw.close();

				if (!file19.exists()) {
					file19.createNewFile();

				}

				fw = new FileWriter(file19.getAbsoluteFile());
				bw = new BufferedWriter(fw);
				bw.write("");
				bw.close();

				if (!file21.exists()) {
					file21.createNewFile();

				}

				fw = new FileWriter(file21.getAbsoluteFile());
				bw = new BufferedWriter(fw);
				bw.write("");
				bw.close();

				if (!file22.exists()) {
					file22.createNewFile();

				}

				fw = new FileWriter(file22.getAbsoluteFile());
				bw = new BufferedWriter(fw);
				bw.write("");
				bw.close();

				if (!file2.exists()) {
					file2.createNewFile();
				}
				fw = new FileWriter(file2.getAbsoluteFile());
				bw = new BufferedWriter(fw);
				bw.write(dao.toString());
				bw.close();

				System.out.println("Done dao");

				if (!file3.exists()) {
					file3.createNewFile();
				}
				fw = new FileWriter(file3.getAbsoluteFile());
				bw = new BufferedWriter(fw);
				bw.write(dao_impl.toString());
				bw.close();

				System.out.println("Done dao impl");

				if (!file4.exists()) {
					file4.createNewFile();
				}
				fw = new FileWriter(file4.getAbsoluteFile());
				bw = new BufferedWriter(fw);
				bw.write(service.toString());
				bw.close();

				System.out.println("Done service");

				if (!file5.exists()) {
					file5.createNewFile();
				}
				fw = new FileWriter(file5.getAbsoluteFile());
				bw = new BufferedWriter(fw);
				bw.write(service_impl.toString());
				bw.close();

				System.out.println("Done service impl");

				if (!file6.exists()) {
					file6.createNewFile();
				}
				fw = new FileWriter(file6.getAbsoluteFile());
				bw = new BufferedWriter(fw);
				bw.write(sectionform.toString());
				bw.close();

				System.out.println("Done jsp");

				if (!file7.exists()) {
					file7.createNewFile();
				}
				fw = new FileWriter(file7.getAbsoluteFile());
				bw = new BufferedWriter(fw);
				bw.write(model_class.toString());
				bw.close();

				System.out.println("Done service impl");

				if (!file8.exists()) {
					file8.createNewFile();
				}
				fw = new FileWriter(file8.getAbsoluteFile());
				bw = new BufferedWriter(fw);
				bw.write(table_grid_view.toString());
				bw.close();

				System.out.println("Done service impl");

				if (!file9.exists()) {
					file9.createNewFile();
				}
				fw = new FileWriter(file9.getAbsoluteFile());
				bw = new BufferedWriter(fw);
				bw.write(strContentprefield.toString());
				bw.close();

				System.out.println("Done service impl1");

				if (!file10.exists()) {
					file10.createNewFile();
				}
				fw = new FileWriter(file10.getAbsoluteFile());
				bw = new BufferedWriter(fw);
				bw.write(strContentreadonly.toString());
				bw.close();
				System.out.println("Done service impl2");

				// JSP FILE CREATED IN OUR MAIN PROJECT
				if (!jspPath1.exists()) {
					jspPath1.createNewFile();
				}
				fw = new FileWriter(jspPath1.getAbsoluteFile());
				bw = new BufferedWriter(fw);
				bw.write(sectionform.toString());
				bw.close();

				System.out.println("Done service impl3");
				/*
				 * if (!jspPath2.exists()) { jspPath2.createNewFile(); } fw = new
				 * FileWriter(jspPath2.getAbsoluteFile()); bw = new BufferedWriter(fw);
				 * bw.write(table_grid_view.toString()); bw.close();
				 */

				System.out.println("Done service impl3");

				if (!jspPath3.exists()) {
					jspPath3.createNewFile();
				}
				fw = new FileWriter(jspPath3.getAbsoluteFile());
				bw = new BufferedWriter(fw);
				bw.write(strContentprefield.toString());
				bw.close();

				System.out.println("Done service impl4");

				if (!jspPath4.exists()) {
					jspPath4.createNewFile();
				}
				fw = new FileWriter(jspPath4.getAbsoluteFile());
				bw = new BufferedWriter(fw);
				bw.write(strContentreadonly.toString());
				bw.close();

				System.out.println("Done service impl5");

				// for production part exxtension jsp
				if (!jspExt1.exists()) {
					jspExt1.createNewFile();

				}

				fw = new FileWriter(jspExt1.getAbsoluteFile());
				bw = new BufferedWriter(fw);
				bw.write("");
				bw.close();

				System.out.println("Done service impl6");
				if (!jspExt2.exists()) {
					jspExt2.createNewFile();

				}

				fw = new FileWriter(jspExt2.getAbsoluteFile());
				bw = new BufferedWriter(fw);
				bw.write("");
				bw.close();
				System.out.println("Done service impl7");

				if (!jspExt3.exists()) {
					jspExt3.createNewFile();

				}

				fw = new FileWriter(jspExt3.getAbsoluteFile());
				bw = new BufferedWriter(fw);
				bw.write("");
				bw.close();

				System.out.println("Done service impl8");

				if (!jspExt4.exists()) {
					jspExt4.createNewFile();

				}

				fw = new FileWriter(jspExt4.getAbsoluteFile());
				bw = new BufferedWriter(fw);
				bw.write("");
				bw.close();

				System.out.println("Done service impl9");

				if (!jspExt5.exists()) {
					jspExt5.createNewFile();

				}

				fw = new FileWriter(jspExt5.getAbsoluteFile());
				bw = new BufferedWriter(fw);
				bw.write("");
				bw.close();
				System.out.println("Done service impl10");

				System.out.println("builder ended");

				if (line_table_name != null && !line_table_name.isEmpty()) {
					String line_table_name_lower = line_table_name.toLowerCase();
					String line_table_name_first_upper = line_table_name_lower.substring(0, 1).toUpperCase()
							+ line_table_name_lower.substring(1);
					String line_table_name_upper = line_table_name_lower.toUpperCase();
					File file11 = new File(
							projectPath + "/Projects/" + project_name + "/src/main/webapp/WEB-INF/tiles/acemaster/"
									+ module_name + "/" + line_table_name_first_upper + "_view.jsp");
					File file12 = new File(projectPath + "/Projects/" + project_name + "/src/main/java/com/realnet/"
							+ module_name + "/model/" + line_table_name_first_upper + ".java");
					File file13 = new File(
							projectPath + "/Projects/" + project_name + "/src/main/webapp/WEB-INF/tiles/acemaster/"
									+ module_name + "/" + line_table_name_first_upper + "_update.jsp");
					File file14 = new File(
							projectPath + "/Projects/" + project_name + "/src/main/webapp/WEB-INF/tiles/acemaster/"
									+ module_name + "/" + line_table_name_first_upper + "_readonly.jsp");
					File file16 = new File(
							projectPath + "/Projects/" + project_name + "/src/main/webapp/WEB-INF/tiles/acemaster/"
									+ module_name + "/" + line_table_name_lower + "_extension.jsp");
					File file17 = new File(
							projectPath + "/Projects/" + project_name + "/src/main/webapp/WEB-INF/tiles/acemaster/"
									+ module_name + "/" + line_table_name_lower + "_ext_update.jsp");
					File file20 = new File(
							projectPath + "/Projects/" + project_name + "/src/main/webapp/WEB-INF/tiles/acemaster/"
									+ module_name + "/" + line_table_name_lower + "_extension2.jsp");
					File file23 = new File(
							projectPath + "/Projects/" + project_name + "/src/main/webapp/WEB-INF/tiles/acemaster/"
									+ module_name + "/" + line_table_name_lower + "_ext_readonly.jsp");

					if (!file11.getParentFile().exists()) {
						file11.getParentFile().mkdirs();
					}
					System.out.println(
							"builder ended............................................." + line_table_name_first_upper);

					if (!file16.exists()) {
						file16.createNewFile();
					}
					fw = new FileWriter(file16.getAbsoluteFile());
					bw = new BufferedWriter(fw);
					bw.write("");
					bw.close();

					if (!file17.exists()) {
						file17.createNewFile();
					}
					fw = new FileWriter(file17.getAbsoluteFile());
					bw = new BufferedWriter(fw);
					bw.write("");
					bw.close();

					if (!file20.exists()) {
						file20.createNewFile();
					}
					fw = new FileWriter(file20.getAbsoluteFile());
					bw = new BufferedWriter(fw);
					bw.write("");
					bw.close();

					if (!file23.exists()) {
						file23.createNewFile();
					}
					fw = new FileWriter(file23.getAbsoluteFile());
					bw = new BufferedWriter(fw);
					bw.write("");
					bw.close();

					if (!file11.exists()) {
						file11.createNewFile();
					}
					fw = new FileWriter(file11.getAbsoluteFile());
					bw = new BufferedWriter(fw);
					bw.write(for_line_part.toString());
					bw.close();

					if (!file12.exists()) {
						file12.createNewFile();
					}
					fw = new FileWriter(file12.getAbsoluteFile());
					bw = new BufferedWriter(fw);
					bw.write(model_class_for_line.toString());
					bw.close();

					if (!file13.exists()) {
						file13.createNewFile();
					}
					fw = new FileWriter(file13.getAbsoluteFile());
					bw = new BufferedWriter(fw);
					bw.write(for_line_part_update.toString());
					bw.close();

					if (!file14.exists()) {
						file14.createNewFile();
					}
					fw = new FileWriter(file14.getAbsoluteFile());
					bw = new BufferedWriter(fw);
					bw.write(for_line_part_readonly.toString());
					bw.close();

				} // end line

			} catch (Exception e) {
				System.out.println(e);
			}
		}
		// for dev instance type
		else {
			try {
				System.out.println("DEV PATH::" + devPath);
				// java files
				File file1 = new File(devPath + "/WEB-INF/classes/DEV/com/realnet/" + module_name + "/controller/"
						+ controller_name_first_upper + ".java");
				File file2 = new File(devPath + "/WEB-INF/classes/DEV/com/realnet/" + module_name + "/dao/"
						+ dao_name_first_upper + ".java");
				File file3 = new File(devPath + "/WEB-INF/classes/DEV/com/realnet/" + module_name + "/dao/"
						+ dao_impl_name_first_upper + ".java");
				File file4 = new File(devPath + "/WEB-INF/classes/DEV/com/realnet/" + module_name + "/service/"
						+ service_name_first_upper + ".java");
				File file5 = new File(devPath + "/WEB-INF/classes/DEV/com/realnet/" + module_name + "/service/"
						+ service_impl_name_first_upper + ".java");
				File file7 = new File(devPath + "/WEB-INF/classes/DEV/com/realnet/" + module_name + "/model/"
						+ table_name_first_upper + ".java");

				System.out.println("DEV complete PATH::" + file1);

				// jsp for dev

				// File file6 = new
				// File(devPath+"/WEB-INF/tiles/acemaster/FB_jsp/"+table_name_first_upper+"_view.jsp");
				// File file8 = new
				// File(devPath+"/WEB-INF/tiles/acemaster/FB_jsp/"+table_name_first_upper+"_grid.jsp");
				// File file9 = new
				// File(devPath+"/WEB-INF/tiles/acemaster/FB_jsp/"+table_name_first_upper+"_update.jsp");
				// File file10 = new
				// File(devPath+"/WEB-INF/tiles/acemaster/FB_jsp/"+table_name_first_upper+"_readonly.jsp");

				File file6 = new File(devPath + "/WEB-INF/tiles/acemaster/" + module_name + "/" + table_name_first_upper
						+ "_view.jsp");

				File file8 = new File(devPath + "/WEB-INF/tiles/acemaster/" + module_name + "/" + table_name_first_upper
						+ "_grid.jsp");
				File file9 = new File(devPath + "/WEB-INF/tiles/acemaster/" + module_name + "/" + table_name_first_upper
						+ "_update.jsp");
				File file10 = new File(devPath + "/WEB-INF/tiles/acemaster/" + module_name + "/"
						+ table_name_first_upper + "_readonly.jsp");

				// extension files
				File file15 = new File(devPath + "/WEB-INF/tiles/acemaster/" + module_name + "/" + table_name_lower
						+ "_extension.jsp");

				File file18 = new File(
						devPath + "/WEB-INF/tiles/acemaster/" + module_name + "/" + table_name_lower + "_add_grid.jsp");
				File file19 = new File(devPath + "/WEB-INF/tiles/acemaster/" + module_name + "/" + table_name_lower
						+ "_add_grid2.jsp");
				File file21 = new File(devPath + "/WEB-INF/tiles/acemaster/" + module_name + "/" + table_name_lower
						+ "_ext_Update.jsp");
				File file22 = new File(devPath + "/WEB-INF/tiles/acemaster/" + module_name + "/" + table_name_lower
						+ "_ext_Readonly.jsp");

				if (!file1.exists()) {
					file1.createNewFile();

				}

				FileWriter fw = new FileWriter(file1.getAbsoluteFile());
				BufferedWriter bw = new BufferedWriter(fw);
				bw.write(controller.toString());
				bw.close();

				System.out.println("DoneController");

				if (!file15.exists()) {
					file15.createNewFile();

				}

				fw = new FileWriter(file15.getAbsoluteFile());
				bw = new BufferedWriter(fw);
				bw.write("");
				bw.close();

				if (!file18.exists()) {
					file18.createNewFile();

				}

				fw = new FileWriter(file18.getAbsoluteFile());
				bw = new BufferedWriter(fw);
				bw.write("");
				bw.close();

				if (!file19.exists()) {
					file19.createNewFile();

				}

				fw = new FileWriter(file19.getAbsoluteFile());
				bw = new BufferedWriter(fw);
				bw.write("");
				bw.close();

				if (!file21.exists()) {
					file21.createNewFile();

				}

				fw = new FileWriter(file21.getAbsoluteFile());
				bw = new BufferedWriter(fw);
				bw.write("");
				bw.close();

				if (!file22.exists()) {
					file22.createNewFile();

				}

				fw = new FileWriter(file22.getAbsoluteFile());
				bw = new BufferedWriter(fw);
				bw.write("");
				bw.close();

				if (!file2.exists()) {
					file2.createNewFile();
				}
				fw = new FileWriter(file2.getAbsoluteFile());
				bw = new BufferedWriter(fw);
				bw.write(dao.toString());
				bw.close();

				System.out.println("Done dao");

				if (!file3.exists()) {
					file3.createNewFile();
				}
				fw = new FileWriter(file3.getAbsoluteFile());
				bw = new BufferedWriter(fw);
				bw.write(dao_impl.toString());
				bw.close();

				System.out.println("Done dao impl");

				if (!file4.exists()) {
					file4.createNewFile();
				}
				fw = new FileWriter(file4.getAbsoluteFile());
				bw = new BufferedWriter(fw);
				bw.write(service.toString());
				bw.close();

				System.out.println("Done service");

				if (!file5.exists()) {
					file5.createNewFile();
				}
				fw = new FileWriter(file5.getAbsoluteFile());
				bw = new BufferedWriter(fw);
				bw.write(service_impl.toString());
				bw.close();

				System.out.println("Done service impl");

				if (!file6.exists()) {
					file6.createNewFile();
				}
				fw = new FileWriter(file6.getAbsoluteFile());
				bw = new BufferedWriter(fw);
				bw.write(sectionform.toString());
				bw.close();

				System.out.println("Done jsp");

				if (!file7.exists()) {
					file7.createNewFile();
				}
				fw = new FileWriter(file7.getAbsoluteFile());
				bw = new BufferedWriter(fw);
				bw.write(model_class.toString());
				bw.close();

				System.out.println("Done service impl");

				if (!file8.exists()) {
					file8.createNewFile();
				}
				fw = new FileWriter(file8.getAbsoluteFile());
				bw = new BufferedWriter(fw);
				bw.write(table_grid_view.toString());
				bw.close();

				System.out.println("Done service impl");

				if (!file9.exists()) {
					file9.createNewFile();
				}
				fw = new FileWriter(file9.getAbsoluteFile());
				bw = new BufferedWriter(fw);
				bw.write(strContentprefield.toString());
				bw.close();

				if (!file10.exists()) {
					file10.createNewFile();
				}
				fw = new FileWriter(file10.getAbsoluteFile());
				bw = new BufferedWriter(fw);
				bw.write(strContentreadonly.toString());
				bw.close();

				controllerName.add(devPath + "/WEB-INF/classes/DEV/com/realnet/" + module_name + "/model/"
						+ table_name_first_upper + ".java");
				controllerName.add(devPath + "/WEB-INF/classes/DEV/com/realnet/" + module_name + "/dao/"
						+ dao_name_first_upper + ".java");
				controllerName.add(devPath + "/WEB-INF/classes/DEV/com/realnet/" + module_name + "/dao/"
						+ dao_impl_name_first_upper + ".java");
				controllerName.add(devPath + "/WEB-INF/classes/DEV/com/realnet/" + module_name + "/service/"
						+ service_name_first_upper + ".java");
				controllerName.add(devPath + "/WEB-INF/classes/DEV/com/realnet/" + module_name + "/service/"
						+ service_impl_name_first_upper + ".java");
				controllerName.add(devPath + "/WEB-INF/classes/DEV/com/realnet/" + module_name + "/controller/"
						+ controller_name_first_upper + ".java");

				if (line_table_name != null && !line_table_name.isEmpty()) {
					String line_table_name_lower = line_table_name.toLowerCase();
					String line_table_name_first_upper = line_table_name_lower.substring(0, 1).toUpperCase()
							+ line_table_name_lower.substring(1);
					String line_table_name_upper = line_table_name.toUpperCase();

					File file11 = new File(devPath + "/WEB-INF/tiles/acemaster/" + module_name + "/"
							+ line_table_name_first_upper + "_view.jsp");
					File file12 = new File(devPath + "/WEB-INF/classes/DEV/com/realnet/" + module_name + "/model/"
							+ line_table_name_first_upper + ".java");
					File file13 = new File(devPath + "/WEB-INF/tiles/acemaster/" + module_name + "/"
							+ line_table_name_first_upper + "_update.jsp");
					File file14 = new File(devPath + "/WEB-INF/tiles/acemaster/" + module_name + "/"
							+ line_table_name_first_upper + "_readonly.jsp");
					File file16 = new File(devPath + "/WEB-INF/tiles/acemaster/" + module_name + "/"
							+ line_table_name_lower + "_extension.jsp");
					File file17 = new File(devPath + "/WEB-INF/tiles/acemaster/" + module_name + "/"
							+ line_table_name_lower + "_ext_update.jsp");
					File file20 = new File(devPath + "/WEB-INF/tiles/acemaster/" + module_name + "/"
							+ line_table_name_lower + "_extension2.jsp");
					File file23 = new File(devPath + "/WEB-INF/tiles/acemaster/" + module_name + "/"
							+ line_table_name_lower + "_ext_readonly.jsp");

					if (!file16.exists()) {
						file16.createNewFile();
					}
					fw = new FileWriter(file16.getAbsoluteFile());
					bw = new BufferedWriter(fw);
					bw.write("");
					bw.close();

					if (!file17.exists()) {
						file17.createNewFile();
					}
					fw = new FileWriter(file17.getAbsoluteFile());
					bw = new BufferedWriter(fw);
					bw.write("");
					bw.close();

					if (!file20.exists()) {
						file20.createNewFile();
					}
					fw = new FileWriter(file20.getAbsoluteFile());
					bw = new BufferedWriter(fw);
					bw.write("");
					bw.close();

					if (!file23.exists()) {
						file23.createNewFile();
					}
					fw = new FileWriter(file23.getAbsoluteFile());
					bw = new BufferedWriter(fw);
					bw.write("");
					bw.close();

					if (!file11.exists()) {
						file11.createNewFile();
					}
					fw = new FileWriter(file11.getAbsoluteFile());
					bw = new BufferedWriter(fw);
					bw.write(for_line_part.toString());
					bw.close();

					if (!file12.exists()) {
						file12.createNewFile();
					}
					fw = new FileWriter(file12.getAbsoluteFile());
					bw = new BufferedWriter(fw);
					bw.write(model_class_for_line.toString());
					bw.close();

					if (!file13.exists()) {
						file13.createNewFile();
					}
					fw = new FileWriter(file13.getAbsoluteFile());
					bw = new BufferedWriter(fw);
					bw.write(for_line_part_update.toString());
					bw.close();

					if (!file14.exists()) {
						file14.createNewFile();
					}
					fw = new FileWriter(file14.getAbsoluteFile());
					bw = new BufferedWriter(fw);
					bw.write(for_line_part_readonly.toString());
					bw.close();

					controllerName.add(devPath + "/WEB-INF/classes/DEV/com/realnet/" + module_name + "/model/"
							+ line_table_name_first_upper + ".java");
				}
				// compiler
				//TomcatCompilerDev(devPath);

			} catch (Exception e) {
				e.printStackTrace();
				ErrorPojo errorPojo = new ErrorPojo();
				Error error = new Error();
				error.setTitle(Constant.FORM_BUILDER_API_TITLE);
				error.setMessage(Constant.FORM_BUILD_FAILURE);
				errorPojo.setError(error);
				return new ResponseEntity<ErrorPojo>(errorPojo, HttpStatus.EXPECTATION_FAILED);
			}

		}

		//return new ModelAndView("redirect:rn_wireframe_grid_view");
		SuccessPojo successPojo = new SuccessPojo();
		Success success = new Success();
		success.setTitle(Constant.FORM_BUILDER_API_TITLE);
		success.setMessage(Constant.FORM_BUILD_SUCCESS);
		successPojo.setSuccess(success);
		return new ResponseEntity<SuccessPojo>(successPojo, HttpStatus.OK);

	}

	/*
	public void TomcatCompiler() {
		String changedir = "pwd";

		System.out.println("ganesh bute call compile method" + controllerName);

		for (int i = 0; i < controllerName.size(); i++) {

			System.out.println("under method");
			
			String moveToclasses = request.getServletContext().getRealPath("/WEB-INF/classes");

			System.out.println("move to::" + moveToclasses);

			String librarypath = request.getServletContext().getRealPath("/WEB-INF/lib");

			System.out.println("library path::" + librarypath);

			String ServerCompile = "javac -classpath " + moveToclasses
					+ environment.getRequiredProperty("tomcatcmdPath") + librarypath
					+ environment.getRequiredProperty("controllerNamePath") + "*"
					+ environment.getRequiredProperty("tomcatcmdPath") + "." + " -d " + moveToclasses + " "
					+ controllerName.get(i) + "";

			System.out.println("path=" + moveToclasses + "\ncommand=" + ServerCompile);
			Process proc = null;
			String line = "";

			try {

				proc = Runtime.getRuntime().exec(ServerCompile);
				System.out.println("compile done");
				// proc = Runtime.getRuntime().exec(changedir);
				// Read the output

				BufferedReader reader = new BufferedReader(new InputStreamReader(proc.getInputStream()));

				while ((line = reader.readLine()) != null) {
					System.out.print(line + "\n");
				}
				proc.waitFor();
			} catch (Exception e) {
				e.printStackTrace();
			}

		} // for loop

	}// method

	void TomcatCompilerDev(String devPath) {
		String changedir = "pwd";
		System.out.println("ganesh bute call compile method" + controllerName + "\n");

		for (int i = 0; i < controllerName.size(); i++) {
			System.out.println("under for loop");
			String moveToclasses = devPath + "/WEB-INF/classes";
			String librarypath = devPath + "/WEB-INF/lib";
			System.out.println("move to::" + moveToclasses + "\nlibrary path::" + librarypath);
			System.out.println("check envirment path::" + controllerName.get(i));

			

			String ServerCompile = "javac -classpath \"" + moveToclasses + "\""
					+ environment.getRequiredProperty("tomcatcmdPath") + "\"" + librarypath + "\""
					+ environment.getRequiredProperty("controllerNamePath") + "*"
					+ environment.getRequiredProperty("tomcatcmdPath") + "." + " -d " + "\"" + moveToclasses + "\""
					+ " \"" + controllerName.get(i) + "\"" + "";

			System.out.println("path=" + moveToclasses + "\ncommand=" + ServerCompile);
			Process proc = null;
			String line = "";

			try {	`
				proc = Runtime.getRuntime().exec(ServerCompile);
				System.out.println("compile done");
				BufferedReader reader = new BufferedReader(new InputStreamReader(proc.getInputStream()));

				while ((line = reader.readLine()) != null) {
					System.out.print(line + "\n");
				}
				proc.waitFor();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}*/
	

	@GetMapping(value = "/delete_wireframe")
	public ResponseEntity<?> delete_wireframe(@RequestParam("header_id") Integer id) throws IOException { // this is rn_fb_header ID
		
		// HEADER VALUE
		Rn_Fb_Header rn_fb_header = wireFrameService.getById(id);
						
//		// LINE VALUES
//		List<Rn_Fb_Line> lineList = rn_fb_header.getRn_fb_lines();
		
		// MODULE DETAILS
		Rn_Module_Setup module = rn_fb_header.getModule();
		// PROJECT DETAILS
		Rn_Project_Setup project = module.getProject();
		String project_name = project.getProjectName();
		System.out.println("Project Name => " + project_name);
		String module_name = module.getModuleName();
		System.out.println("Module Name = " + module_name);		

		System.out.println("f_code value in only_line_build_wireframe ::  " + id);
		
		String controller_name = rn_fb_header.getControllerName();
		String dao_name = rn_fb_header.getDaoName();
		String dao_impl_name = rn_fb_header.getDaoImplName();
		String jsp_name = rn_fb_header.getJspName();
		String table_name = rn_fb_header.getTableName();
		String service_name = rn_fb_header.getServiceName();
		String service_impl_name = rn_fb_header.getServiceImplName();
		String line_table_name = rn_fb_header.getLineTableName();
		System.out.println("---value of line table name---" + line_table_name);

		String controller_name_lower = controller_name.toLowerCase();
		String controller_name_first_upper = controller_name_lower.substring(0, 1).toUpperCase()
				+ controller_name_lower.substring(1);

		String service_name_lower = service_name.toLowerCase();
		String service_name_first_upper = service_name_lower.substring(0, 1).toUpperCase()
				+ service_name_lower.substring(1);
		String service_impl_name_lower = service_impl_name.toLowerCase();
		String service_impl_name_first_upper = service_impl_name_lower.substring(0, 1).toUpperCase()
				+ service_impl_name_lower.substring(1);
		String dao_name_lower = dao_name.toLowerCase();
		String dao_name_first_upper = dao_name_lower.substring(0, 1).toUpperCase() + dao_name_lower.substring(1);
		String dao_impl_name_lower = dao_impl_name.toLowerCase();
		String dao_impl_name_first_upper = dao_impl_name_lower.substring(0, 1).toUpperCase()
				+ dao_impl_name_lower.substring(1);
		String table_name_lower = table_name.toLowerCase();
		String table_name_first_upper = table_name_lower.substring(0, 1).toUpperCase() + table_name_lower.substring(1);

		File file1 = new File(projectPath + "/Projects/" + project_name + "/src/main/java/com/realnet/" + module_name
				+ "/controller/" + controller_name_first_upper + ".java");
		File file2 = new File(projectPath + "/Projects/" + project_name + "/src/main/java/com/realnet/" + module_name
				+ "/dao/" + dao_name_first_upper + ".java");
		File file3 = new File(projectPath + "/Projects/" + project_name + "/src/main/java/com/realnet/" + module_name
				+ "/dao/" + dao_impl_name_first_upper + ".java");
		File file4 = new File(projectPath + "/Projects/" + project_name + "/src/main/java/com/realnet/" + module_name
				+ "/service/" + service_name_first_upper + ".java");
		File file5 = new File(projectPath + "/Projects/" + project_name + "/src/main/java/com/realnet/" + module_name
				+ "/service/" + service_impl_name_first_upper + ".java");
		File file6 = new File(projectPath + "/Projects/" + project_name + "/src/main/java/com/realnet/" + module_name
				+ "/model/" + table_name_first_upper + ".java");

		// crud jsp
		File file7 = new File(projectPath + "/Projects/" + project_name + "/src/main/webapp/WEB-INF/tiles/acemaster/"
				+ module_name + "/" + table_name_first_upper + "_view.jsp");
		File file8 = new File(projectPath + "/Projects/" + project_name + "/src/main/webapp/WEB-INF/tiles/acemaster/"
				+ module_name + "/" + table_name_first_upper + "_grid.jsp");
		File file9 = new File(projectPath + "/Projects/" + project_name + "/src/main/webapp/WEB-INF/tiles/acemaster/"
				+ module_name + "/" + table_name_first_upper + "_update.jsp");
		File file10 = new File(projectPath + "/Projects/" + project_name + "/src/main/webapp/WEB-INF/tiles/acemaster/"
				+ module_name + "/" + table_name_first_upper + "_readonly.jsp");

		// extension jsps
		File file11 = new File(projectPath + "/Projects/" + project_name + "/src/main/webapp/WEB-INF/tiles/acemaster/"
				+ module_name + "/" + table_name_lower + "_extension.jsp");
		File file12 = new File(projectPath + "/Projects/" + project_name + "/src/main/webapp/WEB-INF/tiles/acemaster/"
				+ module_name + "/" + table_name_lower + "_add_grid.jsp");
		File file13 = new File(projectPath + "/Projects/" + project_name + "/src/main/webapp/WEB-INF/tiles/acemaster/"
				+ module_name + "/" + table_name_lower + "_add_grid2.jsp");
		File file14 = new File(projectPath + "/Projects/" + project_name + "/src/main/webapp/WEB-INF/tiles/acemaster/"
				+ module_name + "/" + table_name_lower + "_ext_Update.jsp");
		File file15 = new File(projectPath + "/Projects/" + project_name + "/src/main/webapp/WEB-INF/tiles/acemaster/"
				+ module_name + "/" + table_name_lower + "_ext_Readonly.jsp");

		if (file1.delete() && file2.delete() && file3.delete() && file4.delete() && file5.delete() && file6.delete()
				&& file7.delete() && file8.delete() && file9.delete() && file10.delete() && file11.delete()
				&& file12.delete() && file13.delete() && file14.delete() && file15.delete()) {
			System.out.println("File deleted successfully");
			SuccessPojo successPojo = new SuccessPojo();
			Success success = new Success();
			success.setTitle(Constant.FORM_BUILDER_API_TITLE);
			success.setMessage(Constant.FORM_DELETE_SUCCESS);
			successPojo.setSuccess(success);
			return new ResponseEntity<SuccessPojo>(successPojo, HttpStatus.OK);
		} else {
			System.out.println("Failed to delete the file");
			ErrorPojo errorPojo = new ErrorPojo();
			Error error = new Error();
			error.setTitle(Constant.FORM_BUILDER_API_TITLE);
			error.setMessage(Constant.FORM_DELETE_FAILURE);
			errorPojo.setError(error);
			return new ResponseEntity<ErrorPojo>(errorPojo, HttpStatus.EXPECTATION_FAILED);
		}
	}

	@GetMapping(value = "/delete_line_wireframe")
	public ResponseEntity<?> delete_line_wireframe(@RequestParam("header_id") Integer id) throws IOException { // this is rn_fb_header ID
		// HEADER VALUE
		Rn_Fb_Header rn_fb_header = wireFrameService.getById(id);
						
		// LINE VALUES
		List<Rn_Fb_Line> lineList = rn_fb_header.getRn_fb_lines();
				
		// MODULE DETAILS
		Rn_Module_Setup module = rn_fb_header.getModule();
		// PROJECT DETAILS
		Rn_Project_Setup project = module.getProject();
		String project_name = project.getProjectName();
		String project_prefix = project.getProjectPrefix();
		String db_name = project.getDbName();
		String db_user = project.getDbUserName();
		String db_password = project.getDbPassword();
		String db_port_no = project.getPortNumber();
		
		System.out.println("Project Name => " + project_name);
		String module_name = module.getModuleName();
		System.out.println("Module Name = " + module_name);		

		System.out.println("f_code value in only_line_build_wireframe ::  " + id);
				
		String controller_name = rn_fb_header.getControllerName();
		String dao_name = rn_fb_header.getDaoName();
		String dao_impl_name = rn_fb_header.getDaoImplName();
		String jsp_name = rn_fb_header.getJspName();
		String table_name = rn_fb_header.getTableName();
		String service_name = rn_fb_header.getServiceName();
		String service_impl_name = rn_fb_header.getServiceImplName();
		String line_table_name = rn_fb_header.getLineTableName();
		String only_line_table_name = rn_fb_header.getLineTableName();
		System.out.println("---value of line table name---" + line_table_name);

		System.out.println("add base source code" + module_name);

		System.out.println("---value of line table name---" + line_table_name);

		String controller_name_lower = controller_name.toLowerCase();
		String controller_name_first_upper = controller_name_lower.substring(0, 1).toUpperCase()
				+ controller_name_lower.substring(1);

		String service_name_lower = service_name.toLowerCase();
		String service_name_first_upper = service_name_lower.substring(0, 1).toUpperCase()
				+ service_name_lower.substring(1);
		String service_impl_name_lower = service_impl_name.toLowerCase();
		String service_impl_name_first_upper = service_impl_name_lower.substring(0, 1).toUpperCase()
				+ service_impl_name_lower.substring(1);
		String dao_name_lower = dao_name.toLowerCase();
		String dao_name_first_upper = dao_name_lower.substring(0, 1).toUpperCase() + dao_name_lower.substring(1);
		String dao_impl_name_lower = dao_impl_name.toLowerCase();
		String dao_impl_name_first_upper = dao_impl_name_lower.substring(0, 1).toUpperCase()
				+ dao_impl_name_lower.substring(1);
		String table_name_lower = table_name.toLowerCase();

		if (only_line_table_name != null && !only_line_table_name.isEmpty()) {
			String only_line_name_lower = only_line_table_name.toLowerCase();
			String only_line_name_first_upper = only_line_name_lower.substring(0, 1).toUpperCase()
					+ only_line_name_lower.substring(1);

			File file1 = new File(projectPath + "/Projects/" + project_name + "/src/main/java/com/realnet/"
					+ module_name + "/controller/" + controller_name_first_upper + ".java");

			File file2 = new File(projectPath + "/Projects/" + project_name + "/src/main/java/com/realnet/"
					+ module_name + "/dao/" + dao_impl_name_first_upper + ".java");

			File file3 = new File(projectPath + "/Projects/" + project_name + "/src/main/java/com/realnet/"
					+ module_name + "/dao/" + dao_name_first_upper + ".java");

			File file4 = new File(
					projectPath + "/Projects/" + project_name + "/src/main/webapp/WEB-INF/tiles/acemaster/"
							+ module_name + "/" + only_line_name_first_upper + "_grid.jsp");

			File file5 = new File(
					projectPath + "/Projects/" + project_name + "/src/main/webapp/WEB-INF/tiles/acemaster/"
							+ module_name + "/" + only_line_name_first_upper + "_view.jsp");

			File file6 = new File(projectPath + "/Projects/" + project_name + "/src/main/java/com/realnet/"
					+ module_name + "/model/" + only_line_name_first_upper + ".java");

			File file7 = new File(
					projectPath + "/Projects/" + project_name + "/src/main/webapp/WEB-INF/tiles/acemaster/"
							+ module_name + "/" + only_line_name_first_upper + "_update.jsp");

			File file8 = new File(
					projectPath + "/Projects/" + project_name + "/src/main/webapp/WEB-INF/tiles/acemaster/"
							+ module_name + "/" + only_line_name_first_upper + "_readonly.jsp");

			File file9 = new File(projectPath + "/Projects/" + project_name
					+ "/src/main/webapp/WEB-INF/tiles/acemaster/" + module_name + "/" + table_name + "_extension.jsp");

			File file10 = new File(projectPath + "/Projects/" + project_name
					+ "/src/main/webapp/WEB-INF/tiles/acemaster/" + module_name + "/" + table_name + "_extension.jsp");

			File file11 = new File(projectPath + "/Projects/" + project_name
					+ "/src/main/webapp/WEB-INF/tiles/acemaster/" + module_name + "/" + table_name + ".jsp");

			File file12 = new File(projectPath + "/Projects/" + project_name
					+ "/src/main/webapp/WEB-INF/tiles/acemaster/" + module_name + "/" + table_name + "_ext_Update.jsp");

			File file13 = new File(
					projectPath + "/Projects/" + project_name + "/src/main/webapp/WEB-INF/tiles/acemaster/"
							+ module_name + "/" + table_name + "_ext_Readonly.jsp");

			File file14 = new File(projectPath + "/Projects/" + project_name
					+ "/src/main/webapp/WEB-INF/tiles/acemaster/" + module_name + "/" + table_name + "_add_grid.jsp");

			File file15 = new File(projectPath + "/Projects/" + project_name
					+ "/src/main/webapp/WEB-INF/tiles/acemaster/" + module_name + "/" + table_name + "_add_grid2.jsp");

			File file16 = new File(projectPath + "/Projects/" + project_name
					+ "/src/main/webapp/WEB-INF/tiles/acemaster/" + module_name + "/" + table_name + "_ext_update.jsp");

			File file17 = new File(
					projectPath + "/Projects/" + project_name + "/src/main/webapp/WEB-INF/tiles/acemaster/"
							+ module_name + "/" + table_name + "_ext_readonly.jsp");

			File file18 = new File(
					projectPath + "/Projects/" + project_name + "/src/main/webapp/WEB-INF/tiles/acemaster/"
							+ module_name + "/" + table_name + "_ext_Update_head.jsp");

			if (file1.delete() && file2.delete() && file3.delete() && file4.delete() && file5.delete() && file6.delete()
					&& file7.delete() && file8.delete() && file9.delete() && file10.delete() && file11.delete()
					&& file12.delete() && file13.delete() && file14.delete() && file15.delete() && file16.delete()
					&& file17.delete() && file18.delete()) {
				System.out.println("File deleted successfully");
				SuccessPojo successPojo = new SuccessPojo();
				Success success = new Success();
				success.setTitle(Constant.FORM_BUILDER_API_TITLE);
				success.setMessage(Constant.FORM_DELETE_SUCCESS);
				successPojo.setSuccess(success);
				return new ResponseEntity<SuccessPojo>(successPojo, HttpStatus.OK);
			} else {
				System.out.println("Failed to delete the file");
				ErrorPojo errorPojo = new ErrorPojo();
				Error error = new Error();
				error.setTitle(Constant.FORM_BUILDER_API_TITLE);
				error.setMessage(Constant.FORM_DELETE_FAILURE);
				errorPojo.setError(error);
				return new ResponseEntity<ErrorPojo>(errorPojo, HttpStatus.EXPECTATION_FAILED);
			}
		}
		//return new ModelAndView("redirect:rn_wireframe_grid_view");
		SuccessPojo successPojo = new SuccessPojo();
		Success success = new Success();
		success.setTitle(Constant.FORM_BUILDER_API_TITLE);
		success.setMessage(Constant.FORM_DELETE_SUCCESS);
		successPojo.setSuccess(success);
		return new ResponseEntity<SuccessPojo>(successPojo, HttpStatus.OK);

	}

	@GetMapping(value = "/delete_header_line_wireframe")
	public ResponseEntity<?> delete_header_line_wireframe(@RequestParam("header_id") Integer id) throws IOException { // this is rn_fb_header ID

		// HEADER VALUE
		Rn_Fb_Header rn_fb_header = wireFrameService.getById(id);
						
		// LINE VALUES
		List<Rn_Fb_Line> lineList = rn_fb_header.getRn_fb_lines();
				
		// MODULE DETAILS
		Rn_Module_Setup module = rn_fb_header.getModule();
		// PROJECT DETAILS
		Rn_Project_Setup project = module.getProject();
		String project_name = project.getProjectName();
		String project_prefix = project.getProjectPrefix();
		String db_name = project.getDbName();
		String db_user = project.getDbUserName();
		String db_password = project.getDbPassword();
		String db_port_no = project.getPortNumber();
		
		System.out.println("Project Name => " + project_name);
		String module_name = module.getModuleName();
		System.out.println("Module Name = " + module_name);		

		System.out.println("f_code value in only_line_build_wireframe ::  " + id);
				
		String controller_name = rn_fb_header.getControllerName();
		String dao_name = rn_fb_header.getDaoName();
		String dao_impl_name = rn_fb_header.getDaoImplName();
		String jsp_name = rn_fb_header.getJspName();
		String table_name = rn_fb_header.getTableName();
		String service_name = rn_fb_header.getServiceName();
		String service_impl_name = rn_fb_header.getServiceImplName();
		String line_table_name = rn_fb_header.getLineTableName();
		System.out.println("---value of line table name---" + line_table_name);

		System.out.println("add base source code" + module_name);

		System.out.println("---value of line table name---" + line_table_name);

		String controller_name_lower = controller_name.toLowerCase();
		String controller_name_first_upper = controller_name_lower.substring(0, 1).toUpperCase()
				+ controller_name_lower.substring(1);

		String service_name_lower = service_name.toLowerCase();
		String service_name_first_upper = service_name_lower.substring(0, 1).toUpperCase()
				+ service_name_lower.substring(1);
		String service_impl_name_lower = service_impl_name.toLowerCase();
		String service_impl_name_first_upper = service_impl_name_lower.substring(0, 1).toUpperCase()
				+ service_impl_name_lower.substring(1);
		String dao_name_lower = dao_name.toLowerCase();
		String dao_name_first_upper = dao_name_lower.substring(0, 1).toUpperCase() + dao_name_lower.substring(1);
		String dao_impl_name_lower = dao_impl_name.toLowerCase();
		String dao_impl_name_first_upper = dao_impl_name_lower.substring(0, 1).toUpperCase()
				+ dao_impl_name_lower.substring(1);
		String table_name_lower = table_name.toLowerCase();
		String table_name_first_upper = table_name_lower.substring(0, 1).toUpperCase() + table_name_lower.substring(1);

		File file1 = new File(projectPath + "/Projects/" + project_name + "/src/main/java/com/realnet/" + module_name
				+ "/controller/" + controller_name_first_upper + ".java");
		File file2 = new File(projectPath + "/Projects/" + project_name + "/src/main/java/com/realnet/" + module_name
				+ "/dao/" + dao_name_first_upper + ".java");
		File file3 = new File(projectPath + "/Projects/" + project_name + "/src/main/java/com/realnet/" + module_name
				+ "/dao/" + dao_impl_name_first_upper + ".java");
		File file4 = new File(projectPath + "/Projects/" + project_name + "/src/main/java/com/realnet/" + module_name
				+ "/service/" + service_name_first_upper + ".java");
		File file5 = new File(projectPath + "/Projects/" + project_name + "/src/main/java/com/realnet/" + module_name
				+ "/service/" + service_impl_name_first_upper + ".java");
		File file6 = new File(projectPath + "/Projects/" + project_name + "/src/main/java/com/realnet/" + module_name
				+ "/model/" + table_name_first_upper + ".java");

		// crud jsp
		File file7 = new File(projectPath + "/Projects/" + project_name + "/src/main/webapp/WEB-INF/tiles/acemaster/"
				+ module_name + "/" + table_name_first_upper + "_view.jsp");
		File file8 = new File(projectPath + "/Projects/" + project_name + "/src/main/webapp/WEB-INF/tiles/acemaster/"
				+ module_name + "/" + table_name_first_upper + "_grid.jsp");
		File file9 = new File(projectPath + "/Projects/" + project_name + "/src/main/webapp/WEB-INF/tiles/acemaster/"
				+ module_name + "/" + table_name_first_upper + "_update.jsp");
		File file10 = new File(projectPath + "/Projects/" + project_name + "/src/main/webapp/WEB-INF/tiles/acemaster/"
				+ module_name + "/" + table_name_first_upper + "_readonly.jsp");

		// extension jsps
		File file11 = new File(projectPath + "/Projects/" + project_name + "/src/main/webapp/WEB-INF/tiles/acemaster/"
				+ module_name + "/" + table_name_lower + "_extension.jsp");
		File file12 = new File(projectPath + "/Projects/" + project_name + "/src/main/webapp/WEB-INF/tiles/acemaster/"
				+ module_name + "/" + table_name_lower + "_add_grid.jsp");
		File file13 = new File(projectPath + "/Projects/" + project_name + "/src/main/webapp/WEB-INF/tiles/acemaster/"
				+ module_name + "/" + table_name_lower + "_add_grid2.jsp");
		File file14 = new File(projectPath + "/Projects/" + project_name + "/src/main/webapp/WEB-INF/tiles/acemaster/"
				+ module_name + "/" + table_name_lower + "_ext_Update.jsp");
		File file15 = new File(projectPath + "/Projects/" + project_name + "/src/main/webapp/WEB-INF/tiles/acemaster/"
				+ module_name + "/" + table_name_lower + "_ext_Readonly.jsp");

		if (file1.delete() && file2.delete() && file3.delete() && file4.delete() && file5.delete() && file6.delete()
				&& file7.delete() && file8.delete() && file9.delete() && file10.delete() && file11.delete()
				&& file12.delete() && file13.delete() && file14.delete() && file15.delete()) {
			System.out.println("File deleted successfully");
		} else {
			System.out.println("Failed to delete the file");
			ErrorPojo errorPojo = new ErrorPojo();
			Error error = new Error();
			error.setTitle(Constant.FORM_BUILDER_API_TITLE);
			error.setMessage(Constant.FORM_DELETE_FAILURE);
			errorPojo.setError(error);
			return new ResponseEntity<ErrorPojo>(errorPojo, HttpStatus.EXPECTATION_FAILED);
		}

		if (line_table_name != null && !line_table_name.isEmpty()) {
			String line_table_name_lower = line_table_name.toLowerCase();
			String line_table_name_first_upper = line_table_name_lower.substring(0, 1).toUpperCase()
					+ line_table_name_lower.substring(1);

			File file16 = new File(
					projectPath + "/Projects/" + project_name + "/src/main/webapp/WEB-INF/tiles/acemaster/"
							+ module_name + "/" + line_table_name_first_upper + "_view.jsp");
			File file17 = new File(projectPath + "/Projects/" + project_name + "/src/main/java/com/realnet/"
					+ module_name + "/model/" + line_table_name_first_upper + ".java");
			File file18 = new File(
					projectPath + "/Projects/" + project_name + "/src/main/webapp/WEB-INF/tiles/acemaster/"
							+ module_name + "/" + line_table_name_first_upper + "_update.jsp");
			File file19 = new File(
					projectPath + "/Projects/" + project_name + "/src/main/webapp/WEB-INF/tiles/acemaster/"
							+ module_name + "/" + line_table_name_first_upper + "_readonly.jsp");
			File file20 = new File(
					projectPath + "/Projects/" + project_name + "/src/main/webapp/WEB-INF/tiles/acemaster/"
							+ module_name + "/" + line_table_name_lower + "_extension.jsp");
			File file21 = new File(
					projectPath + "/Projects/" + project_name + "/src/main/webapp/WEB-INF/tiles/acemaster/"
							+ module_name + "/" + line_table_name_lower + "_ext_update.jsp");
			File file22 = new File(
					projectPath + "/Projects/" + project_name + "/src/main/webapp/WEB-INF/tiles/acemaster/"
							+ module_name + "/" + line_table_name_lower + "_extension2.jsp");
			File file23 = new File(
					projectPath + "/Projects/" + project_name + "/src/main/webapp/WEB-INF/tiles/acemaster/"
							+ module_name + "/" + line_table_name_lower + "_ext_readonly.jsp");

			System.out.println(
					"builder ended............................................." + line_table_name_first_upper);

			if (file16.delete() && file17.delete() && file18.delete() && file19.delete() && file20.delete()
					&& file21.delete() && file22.delete() && file23.delete()) {
				System.out.println("File deleted successfully");
			} else {
				System.out.println("Failed to delete the file");
				ErrorPojo errorPojo = new ErrorPojo();
				Error error = new Error();
				error.setTitle(Constant.FORM_BUILDER_API_TITLE);
				error.setMessage(Constant.FORM_DELETE_FAILURE);
				errorPojo.setError(error);
				return new ResponseEntity<ErrorPojo>(errorPojo, HttpStatus.EXPECTATION_FAILED);
			}
		}

		//return new ModelAndView("redirect:rn_wireframe_grid_view");
		SuccessPojo successPojo = new SuccessPojo();
		Success success = new Success();
		success.setTitle(Constant.FORM_BUILDER_API_TITLE);
		success.setMessage(Constant.FORM_DELETE_SUCCESS);
		successPojo.setSuccess(success);
		return new ResponseEntity<SuccessPojo>(successPojo, HttpStatus.OK);
	}

}
