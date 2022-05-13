package com.realnet.wfb.controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

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
import com.realnet.qb.service.Rn_CreateQuery_Service;
import com.realnet.users.service.UserService;
import com.realnet.utils.Constant;
import com.realnet.wfb.entity.Rn_Fb_Header;
import com.realnet.wfb.entity.Rn_Fb_Line;
import com.realnet.wfb.service.Rn_WireFrame_Service;

import io.swagger.annotations.Api;

@RestController
@RequestMapping(value = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(tags = { "Spring MVC MultiLine WireFrame" })
public class Rn_Wireframe_Multiline_FormBuilder {
	
	@Value("${projectPath}")
	private String projectPath;
	
	@Autowired
	private Rn_InstanceTypeRepository instanceTypeRepo;

	@Autowired
	private Rn_WireFrame_Service wireFrameService;

	@Autowired
	private Rn_LookUp_Service lookUpService;

	@RequestMapping(value = "/ml_build_form", method = RequestMethod.GET)
	public ModelAndView ml_build_form(@RequestParam("header_id") Integer id, @Valid @RequestBody ExportDataDTO exportDataDTO) throws IOException { // this is rn_fb_header ID
		System.out.println("Multi-Line Build Form Controller Strated...");

		//String projectPath = rn_project_setup_dao.getProjectPath();

		StringBuilder strContent = new StringBuilder();
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
		StringBuilder dao_save_id = new StringBuilder();
		StringBuilder dao_save_field = new StringBuilder();
		StringBuilder sqlField_id = new StringBuilder();
		StringBuilder setField_id = new StringBuilder();
		StringBuilder dao_impl_prefield = new StringBuilder();
		StringBuilder prefield_controller = new StringBuilder();
		StringBuilder prefield_controller_readonly = new StringBuilder();
		StringBuilder sbmit_parameterfield = new StringBuilder();
		StringBuilder sbmit_parameter_field = new StringBuilder();
		StringBuilder sbmit_parameterid = new StringBuilder();
		StringBuilder sbmit_parameter_id = new StringBuilder();
		StringBuilder save_controller = new StringBuilder();
		StringBuilder form = new StringBuilder();
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
		StringBuilder id_not_pri_para = new StringBuilder();
		StringBuilder id_notpri_set = new StringBuilder();
		StringBuilder id_pri_set = new StringBuilder();

		StringBuilder date_array_set_in_dao = new StringBuilder();
		StringBuilder id_para_submit = new StringBuilder();
		StringBuilder var_for_pass_para = new StringBuilder();
		StringBuilder var_for_pass_para_id = new StringBuilder();
		StringBuilder model_class_for_var = new StringBuilder();
		StringBuilder model_class_for_set = new StringBuilder();
		StringBuilder date_para = new StringBuilder();
		StringBuilder date_paraNValues = new StringBuilder();
		StringBuilder date_set = new StringBuilder();
		StringBuilder date_model_variable = new StringBuilder();
		StringBuilder date_model_set_variable = new StringBuilder();
		StringBuilder date_array_para = new StringBuilder();
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
		StringBuilder header_line_submit = new StringBuilder();
		StringBuilder header_line_submit_id = new StringBuilder();
		StringBuilder header_line_submit_varchar = new StringBuilder();
		StringBuilder get_id_for_sbmit_header_line = new StringBuilder();
		StringBuilder header_id_for_line = new StringBuilder();
		StringBuilder line_id_for_rowcount = new StringBuilder();
		StringBuilder setModel = new StringBuilder();
		StringBuilder array_para_for_id_line = new StringBuilder();
		StringBuilder array_para_for_varchar_line = new StringBuilder();
		StringBuilder action2 = new StringBuilder();
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
		StringBuilder line_value_update = new StringBuilder();
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
		StringBuilder session_id = new StringBuilder();
		StringBuilder action = new StringBuilder();
		StringBuilder date_script = new StringBuilder();
		StringBuilder dropdown_script_latest = new StringBuilder();
		StringBuilder dropdown_script_latest_for_line = new StringBuilder();
		StringBuilder button = new StringBuilder();
		StringBuilder who_columns = new StringBuilder();
		StringBuilder model_who_columns = new StringBuilder();
		StringBuilder model_who_columns2 = new StringBuilder();
		StringBuilder model_who_update_date = new StringBuilder();
		StringBuilder model_who_update_by = new StringBuilder();
		StringBuilder who_columns_update = new StringBuilder();
		StringBuilder who_columns_update_dao = new StringBuilder();
		StringBuilder who_columns_dao = new StringBuilder();
		StringBuilder updatesession = new StringBuilder();
		StringBuilder static_form_code = new StringBuilder();
		StringBuilder href = new StringBuilder();
		StringBuilder popup = new StringBuilder();
		StringBuilder popup_id = new StringBuilder();
		StringBuilder popup_var = new StringBuilder();
		StringBuilder button_prefield = new StringBuilder();
		StringBuilder popup_model = new StringBuilder();
		StringBuilder popup_dao_field = new StringBuilder();
		StringBuilder popup_sql_field = new StringBuilder();
		StringBuilder account_columns = new StringBuilder();
		StringBuilder model_hid_column = new StringBuilder();
		StringBuilder model_hid_getter = new StringBuilder();
		StringBuilder model_hid_setter = new StringBuilder();
		StringBuilder controller_final_code = new StringBuilder();

		/*
		 * HttpSession session2 = request.getSession(false); String f_code2= (String)
		 * session2.getAttribute("form_code");
		 */

//		HttpSession session = request.getSession(false);
//		int f_code = (Integer) session.getAttribute("header_id1");

		System.out.println("ganesh butte header id:" + id);

		List<Rn_Fb_Line> rn_userlist = wireFrameService.getVarcharFields(id);

		List<Rn_Fb_Line> id_list = wireFrameService.getPrimaryKeyField(id);

		List<Rn_Lookup_Values> attribute_flex = lookUpService.getExtensions();

		List<Rn_Fb_Line> id_notprimary = wireFrameService.getIntegerFields(id);

		List<Rn_Fb_Line> hid_notprimary = wireFrameService.getHidIntegerFields(id);

		List<Rn_Fb_Line> datetime_list = wireFrameService.getDateTimeFields(id);

		List<Rn_Fb_Line> section_values = wireFrameService.getSection(id);

		List<Rn_Fb_Line> section_num = wireFrameService.getSection(id);

		List<Rn_Fb_Line> buttonList = wireFrameService.getLineFields(id);

		System.out.println("-----------value of id-------" + id);

		System.out.println("-----------build from started-------");

//		List<FbLine> popup_select_var = contactDAO1.popup(f_code);

		Rn_Fb_Header report = wireFrameService.getById(id);

		String form_code = report.getFormCode();
		System.out.println("checked form code in build form::::" + form_code);

		String controller_name = report.getControllerName();
		String dao_name = report.getDaoName();
		String dao_impl_name = report.getDaoImplName();
		String jsp_name = report.getJspName();
		String table_name = report.getTableName();
		String service_name = report.getServiceName();
		String service_impl_name = report.getServiceImplName();

		String line_table_name = report.getLineTableName();

		//int u_id = (Integer) request.getSession().getAttribute("project_id");

		Rn_Module_Setup report1 = report.getModule();

		String module_name = report1.getModuleName();

		System.out.println("add base source code" + module_name);
		
		// get project details
		Rn_Project_Setup report0 = report1.getProject();
		String project_name = report0.getProjectName();
		String project_prefix = report0.getProjectPrefix();
		String db_name = report0.getDbName();
		String db_user = report0.getDbUserName();
		String db_password = report0.getDbPassword();
		String db_port_no = report0.getPortNumber();

		//int m_id = (Integer) request.getSession().getAttribute("module_id");

		

		final String FOLDER0 = projectPath + "/" + project_name + "/src/main/webapp/WEB-INF/tiles/acemaster/"+ module_name;
		File newFolder0 = new File(FOLDER0);
		boolean created0 = newFolder0.mkdir();

		System.out.println("---value of line table name in multilineFormBuilderControllee class---" + line_table_name);

		String multiple_line_name = report.getMultilineTableName();

		String[] single_line_name1 = multiple_line_name.split(",");

		int z = 0;
		String multiple_line_table_name_lower = "";
		String multiple_line_table_name_upper = "";
		String multiple_line_table_name_first_upper = "";

		for (int k = 0; k < single_line_name1.length; k++) {
			z++;
			multiple_line_table_name_lower = single_line_name1[0].toLowerCase();
			multiple_line_table_name_upper = single_line_name1[0].toUpperCase();
			multiple_line_table_name_first_upper = multiple_line_table_name_lower.substring(0, 1).toUpperCase()
					+ multiple_line_table_name_lower.substring(1);

			System.out.println(
					"---------value of multiple_line_table_name_lower------------::" + multiple_line_table_name_lower);
		}

		String only_line_name = report.getLineTableName();
		System.out.println("---------value of only line name------------::" + only_line_name);

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
		String service_impl_name_upper = service_impl_name.toUpperCase();

		String dao_name_lower = dao_name.toLowerCase();
		String dao_name_first_upper = dao_name_lower.substring(0, 1).toUpperCase() + dao_name_lower.substring(1);
		String dao_name_upper = dao_name.toUpperCase();

		String dao_impl_name_lower = dao_impl_name.toLowerCase();
		String dao_impl_name_first_upper = dao_impl_name_lower.substring(0, 1).toUpperCase()
				+ dao_impl_name_lower.substring(1);
		String dao_impl_name_upper = dao_impl_name.toUpperCase();

		System.out.println("controler name from build from::" + controller_name);
		System.out.println("service name from build from::" + dao_name);
		System.out.println("jsp name from build from::" + jsp_name);

		// ---------------------for------line-----------------------------------

		List<Rn_Fb_Line> line_id_primary = wireFrameService.getLinePrimarkKeyField(id);

		List<Rn_Fb_Line> line_varchar = wireFrameService.getLineVarcharFields(id);

		List<Rn_Fb_Line> line_id_not_primary = wireFrameService.getLineIntegerFields(id);

		List<Rn_Fb_Line> line_section = wireFrameService.getLineSection(id);

		// -------------------------------attribute flex
		// values-------------------------

		if (table_name != null && !table_name.isEmpty()) {
			String table_name_lower = table_name.toLowerCase();
			String table_name_first_upper = table_name_lower.substring(0, 1).toUpperCase()
					+ table_name_lower.substring(1);
			String table_name_upper = table_name.toUpperCase();

			for (int i = 0; i < attribute_flex.size(); i++) {

				String lookup_code = attribute_flex.get(i).getLookupCode();
				String lower_case = lookup_code.toLowerCase();
				String first_upper = lower_case.substring(0, 1).toUpperCase() + lower_case.substring(1);
				String only_upper = lookup_code.toUpperCase();

				attribute_set_for_grid_dao.append(
						"\n" + table_name_lower + ".set" + first_upper + "(rs.getString(\"" + only_upper + "\"));");

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

				set_parameter.append("\n" + table_name_lower + ".set" + first_upper + "(" + lower_case + ");");

			}

		}

		if (table_name != null && !table_name.isEmpty()) {
			String table_name_lower = table_name.toLowerCase();
			String table_name_first_upper = table_name_lower.substring(0, 1).toUpperCase()
					+ table_name_lower.substring(1);
			String table_name_upper = table_name.toUpperCase();

			for (int i = 0; i < id_list.size(); i++) {
				String field_name = id_list.get(i).getMapping();
				String lower_case = field_name.toLowerCase();
				String first_upper = lower_case.substring(0, 1).toUpperCase() + lower_case.substring(1);
				String only_upper = field_name.toUpperCase();

				sbmit_parameterid
						.append("\nString[]\t" + lower_case + "=request.getParameterValues(\"" + lower_case + "\");");
				sbmit_parameter_id.append(
						"\nint\t" + lower_case + "=Integer.parseInt(request.getParameter(\"" + lower_case + "\"));");
				set_id_primary_for_header.append("\n" + table_name_lower + ".set" + first_upper + "(id);");

				id_primary_parameter_submit.append("\nif(request.getParameter(\"" + lower_case + "\")!=\"\")" + "\n{"
						+ "\nint id=Integer.parseInt(request.getParameter(\"" + lower_case + "\"));"
						+ set_id_primary_for_header + "\n}");

				id_pri_set.append("\n\t" + table_name_lower + ".set" + first_upper + "(" + lower_case + ");");

			}
		}

		if (table_name != null && !table_name.isEmpty()) {
			String table_name_lower = table_name.toLowerCase();
			String table_name_first_upper = table_name_lower.substring(0, 1).toUpperCase()
					+ table_name_lower.substring(1);
			String table_name_upper = table_name.toUpperCase();

			for (int i = 0; i < rn_userlist.size(); i++) {
				String field_name = rn_userlist.get(i).getMapping();
				String lower_case = field_name.toLowerCase();
				String first_upper = lower_case.substring(0, 1).toUpperCase() + lower_case.substring(1);
				System.out.println("------for lowecase string-----------::" + lower_case);
				System.out.println("------for uper first letter---------::" + first_upper);

				para.append("\n\t String " + lower_case + "=request.getParameter(\"" + lower_case + "\");");

				set_para.append("\n\t" + table_name_lower + ".set" + first_upper + "(" + lower_case + ");");

				sbmit_parameterfield
						.append("\nString[]\t" + lower_case + "=request.getParameterValues(\"" + lower_case + "\");");
				sbmit_parameter_field
						.append("\nString\t" + lower_case + "=request.getParameter(\"" + lower_case + "\");");
			}
		}
		if (table_name != null && !table_name.isEmpty()) {
			String table_name_lower = table_name.toLowerCase();
			String table_name_first_upper = table_name_lower.substring(0, 1).toUpperCase()
					+ table_name_lower.substring(1);
			String table_name_upper = table_name.toUpperCase();

			for (int i = 0; i < hid_notprimary.size(); i++) {
				String field_name = hid_notprimary.get(i).getMapping();
				String data_type = hid_notprimary.get(i).getDataType();
				String lower_case = field_name.toLowerCase();
				String only_upper = field_name.toUpperCase();
				String first_upper = lower_case.substring(0, 1).toUpperCase() + lower_case.substring(1);

				model_hid_column
						.append("\n@Column(name = \"" + only_upper + "\")" + "\nprivate int \t" + lower_case + ";");

				model_hid_getter
						.append("\n\npublic int get" + first_upper + "() \n{" + "\nreturn\t" + lower_case + ";\n}");

				model_hid_setter.append("\n\npublic void set" + first_upper + "(int\t" + lower_case + ")\n{" + "\nthis."
						+ lower_case + "=" + lower_case + ";" + "\n}");
			}
		}

		if (table_name != null && !table_name.isEmpty()) {
			String table_name_lower = table_name.toLowerCase();
			String table_name_first_upper = table_name_lower.substring(0, 1).toUpperCase()
					+ table_name_lower.substring(1);
			String table_name_upper = table_name.toUpperCase();

			for (int i = 0; i < id_notprimary.size(); i++) {
				String field_name = id_notprimary.get(i).getMapping();
				String data_type = id_notprimary.get(i).getDataType();
				String lower_case = field_name.toLowerCase();
				String only_upper = field_name.toUpperCase();
				String first_upper = lower_case.substring(0, 1).toUpperCase() + lower_case.substring(1);
				if (data_type.equals("int")) {

					id_para_submit.append(
							"int\t" + lower_case + "=Integer.parseInt(request.getParameter(\"" + lower_case + "\"));");

					id_notpri_para.append(
							"\nString[]\t" + lower_case + "=request.getParameterValues(\"" + lower_case + "\");");

					id_not_pri_para.append("\nint \t" + lower_case + "= Integer.parseInt(request.getParameter(\""
							+ lower_case + "\"));");

					id_notpri_set.append("\n\t" + table_name_lower + ".set" + first_upper + "(" + lower_case + ");");

					model_class_for_var
							.append("\n@Column(name = \"" + only_upper + "\")" + "\nprivate int \t" + lower_case + ";");

					model_class_for_set
							.append("\n\npublic int get" + first_upper + "() \n{" + "\nreturn\t" + lower_case + ";\n}"

									+ "\n\npublic void set" + first_upper + "(int\t" + lower_case + ")\n{" + "\nthis."
									+ lower_case + "=" + lower_case + ";" + "\n}");

					var_for_pass_para.append("String[]\t" + lower_case + ",");

					var_for_pass_para_id.append(lower_case + ",");

					var_for_pass_para_id_for_upper.append(only_upper + ",");

					id_array_set_in_dao.append(
							"\n" + table_name_lower + ".set" + first_upper + "(rs.getInt(\"" + only_upper + "\"));");
					id_array_set_in_dao2.append("\n" + table_name_lower + ".set" + first_upper + "(Integer.parseInt("
							+ lower_case + "[i]));");

				}
			}
		}

		if (table_name != null && !table_name.isEmpty()) {
			String table_name_lower = table_name.toLowerCase();
			String table_name_first_upper = table_name_lower.substring(0, 1).toUpperCase()
					+ table_name_lower.substring(1);
			String table_name_upper = table_name.toUpperCase();

			for (int i = 0; i < datetime_list.size(); i++) {
				String field_name = datetime_list.get(i).getMapping();
				String data_type = datetime_list.get(i).getDataType();

				String lower_case = field_name.toLowerCase();
				String only_upper = field_name.toUpperCase();
				String first_upper = lower_case.substring(0, 1).toUpperCase() + lower_case.substring(1);

				if (data_type.equals("datetime")) {
					date_para.append("\n Date\t" + lower_case + " = null;" + "\ntry\n {" + "\n" + lower_case
							+ " = new SimpleDateFormat(\"dd-MM-yyyy\").parse(request.getParameter(\"" + lower_case
							+ "\"));" + "\n} catch (ParseException e) \n{"

							+ "\ne.printStackTrace();" + "\n}");
					date_set.append("\n" + table_name_lower + ".set" + first_upper + "(" + lower_case + ");");

					date_model_variable.append(
							"\n\n@Column(name = \"" + only_upper + "\")" + "\nprivate Date\t" + lower_case + ";");

					date_model_set_variable.append(
							"\n\npublic Date get" + first_upper + "(){" + "\nreturn\t" + lower_case + ";" + "\n }"

									+ "\n\npublic void set" + first_upper + "(Date\t" + lower_case + ")" + "\n{"
									+ "\nthis." + lower_case + " = " + lower_case + ";" + "\n}");
					date_array_para.append("Date\t" + lower_case + ",");

					date_array_value.append(only_upper + ",");

					date_array_value_for_lower.append(lower_case + ",");

					date_array_set_in_dao
							.append("\n" + table_name_lower + ".set" + first_upper + "(\"" + only_upper + "\");");

					date_array_set_in_dao_for_date.append(
							"\n" + table_name_lower + ".set" + first_upper + "(rs.getDate(\"" + only_upper + "\"));");

				}

			}

		}

		if (table_name != null && !table_name.isEmpty()) {
			String table_name_lower = table_name.toLowerCase();
			String table_name_first_upper = table_name_lower.substring(0, 1).toUpperCase()
					+ table_name_lower.substring(1);
			String table_name_upper = table_name.toUpperCase();

			String field_name_for_id = id_list.get(0).getMapping();
			System.out.println("field" + field_name_for_id);
			String field_name_for_id_lower = field_name_for_id.toLowerCase();
			String field_name_for_id_upper = field_name_for_id.toUpperCase();
			String field_name_first_upper = field_name_for_id_lower.substring(0, 1).toUpperCase()
					+ field_name_for_id_lower.substring(1);

			prefield_controller
					.append("\n\n//-----------------------for prefield part-----------------------------------"
							+ "\n\n@RequestMapping(value = \"/" + table_name_lower
							+ "_update\", method = RequestMethod.GET)"
							+ "\npublic ModelAndView loadReport1(@RequestParam(value = \"" + field_name_for_id_lower
							+ "\") String\t" + field_name_for_id_lower + ", ModelAndView modelview,"
							+ "\nHttpServletRequest request, ModelMap map) throws IOException \n{"
							+ "\nint u_id = Integer.parseInt(" + field_name_for_id_lower + ");" + "\n"
							+ table_name_first_upper + "\t" + table_name_lower + " = new\t" + table_name_first_upper
							+ "();");

			prefield_controller.append("\nmap.addAttribute(\"" + table_name_lower + "_updt\"," + table_name_lower + ");"
					+ "\nList<" + table_name_first_upper + "> report =" + service_name_lower + ".prefield(u_id);"
					+ "\nmap.addAttribute(\"" + table_name_lower + "_update\", report);");

			prefield_controller.append("\nreturn new ModelAndView(\"" + table_name_first_upper + "_update\");" + "\n}");

		}
		if (table_name != null && !table_name.isEmpty()) {
			String table_name_lower = table_name.toLowerCase();
			String table_name_first_upper = table_name_lower.substring(0, 1).toUpperCase()
					+ table_name_lower.substring(1);
			String table_name_upper = table_name.toUpperCase();

			String field_name_for_id = id_list.get(0).getMapping();
			System.out.println("field" + field_name_for_id);
			String field_name_for_id_lower = field_name_for_id.toLowerCase();
			String field_name_for_id_upper = field_name_for_id.toUpperCase();
			String field_name_first_upper = field_name_for_id_lower.substring(0, 1).toUpperCase()
					+ field_name_for_id_lower.substring(1);

			prefield_controller_readonly
					.append("\n\n//--------------------for readonly------------------------------------------------"
							+ "\n\n@RequestMapping(value = \"/" + table_name_lower
							+ "_readonly\", method = RequestMethod.GET)"
							+ "\npublic ModelAndView loadReport2(@RequestParam(value = \"" + field_name_for_id_lower
							+ "\") String\t" + field_name_for_id_lower + ", ModelAndView modelview,"
							+ "\nHttpServletRequest request, ModelMap map) throws IOException \n{"
							+ "\nint u_id = Integer.parseInt(" + field_name_for_id_lower + ");" + "\n"

							+ "\nList<" + table_name_first_upper + "> report =" + service_name_lower
							+ ".prefield(u_id);" + "\nmap.addAttribute(\"" + table_name_lower + "_update\", report);"

							+ "\nList<" + table_name_first_upper + "> report2 =" + service_name_lower + ".prefield_"
							+ table_name_lower + "(u_id);" + "\nmap.addAttribute(\"" + table_name_lower
							+ "_update\", report2);"

							+ "\nList<" + multiple_line_table_name_first_upper + "> report3 =" + dao_name_lower
							+ ".prefield_" + multiple_line_table_name_lower + "(u_id);" + "\nmap.addAttribute(\""
							+ multiple_line_table_name_lower + "_update\", report3);" + "");

			// changes done by karamjit
			/*
			 * if (multiple_line_name != null && !multiple_line_name.isEmpty()) { String
			 * line_table_name_lower = multiple_line_name.toLowerCase(); String
			 * line_table_name_first_upper = line_table_name_lower.substring(0,
			 * 1).toUpperCase() + line_table_name_lower.substring(1); String
			 * line_table_name_upper = multiple_line_name.toUpperCase();
			 * 
			 * prefield_controller_readonly.append("List<" + line_table_name_first_upper +
			 * "> report_"+line_table_name_lower+" " + "=" + service_name_lower+
			 * ".prefield_"+line_table_name_lower+"(u_id);" +
			 * " \n map.addAttribute(\""+line_table_name_lower+"_update\", report_"
			 * +line_table_name_lower+" );"); }
			 */

//			if (multiple_line_name != null && !multiple_line_name.isEmpty()) {
//				String line_table_name_lower = multiple_line_name.toLowerCase();
//				String line_table_name_first_upper = line_table_name_lower.substring(0, 1).toUpperCase()
//						+ line_table_name_lower.substring(1);
//				String line_table_name_upper = multiple_line_name.toUpperCase();
//				
//				prefield_controller_readonly.append("List<" + line_table_name_first_upper + "> grp_menu_list_line = "
//						+ service_name_lower + ".update_group_menu_line(u_id);"
//						+ "\nmap.addAttribute(\"linelist\", grp_menu_list_line);");
//			}
			prefield_controller_readonly
					.append("\nreturn new ModelAndView(\"" + table_name_first_upper + "_readonly\");"

							+ "\n}");
		}
		// changes done by karamjit

		if (table_name != null && !table_name.isEmpty()) {
			System.out.println("karamjit grid controller");
			String table_name_lower = table_name.toLowerCase();
			System.out.println(" table_name in karamjit grid controller ::" + table_name);
			System.out.println(" table_name_lower in karamjit grid controller ::" + table_name_lower);
			String table_name_first_upper = table_name_lower.substring(0, 1).toUpperCase()
					+ table_name_lower.substring(1);
			String table_name_upper = table_name.toUpperCase();

			grid_controller.append("\n"
					+ "\n//-----------------------------------for grid view only--------------------------------------------------"
					+ "\n\n@RequestMapping(value=\"/" + table_name_lower + "_grid_view\")" + "\npublic ModelAndView\t"
					+ table_name_lower + "Details(ModelAndView model) throws IOException" + "\n{" + "\nList<"
					+ table_name_first_upper + ">\t" + table_name_lower + "=" + dao_name_lower + ".userlist();"
					+ "\nmodel.addObject(\"" + table_name_lower + "\", " + table_name_lower + ");"
					+ "\nSystem.out.println(\"sujit\");" + "\nmodel.setViewName(\"" + table_name_first_upper
					+ "_grid\");" + "\nreturn model;" + "\n}" + "");
		}
		if (table_name != null && !table_name.isEmpty()) {
			String table_name_lower = table_name.toLowerCase();
			String table_name_first_upper = table_name_lower.substring(0, 1).toUpperCase()
					+ table_name_lower.substring(1);
			String table_name_upper = table_name.toUpperCase();

			for (int i = 0; i < id_list.size(); i++) {
				String field_name = id_list.get(i).getMapping();
				String lower_case = field_name.toLowerCase();
				String first_upper = lower_case.substring(0, 1).toUpperCase() + lower_case.substring(1);
				String only_upper = field_name.toUpperCase();

				service_save_id.append("String[]\t" + lower_case + ",");
				service_save_id1.append(lower_case + ",");

				header_id_for_line.append(lower_case);

				header_id_for_line_for_comma.append(lower_case + ",");

				parameter_for_line_id.append("int\t" + lower_case + ",");

			}
		}
		if (table_name != null && !table_name.isEmpty()) {
			String table_name_lower = table_name.toLowerCase();
			String table_name_first_upper = table_name_lower.substring(0, 1).toUpperCase()
					+ table_name_lower.substring(1);
			String table_name_upper = table_name.toUpperCase();
			for (int i = 0; i < rn_userlist.size(); i++) {
				String field_name = rn_userlist.get(i).getMapping();
				String lower_case = field_name.toLowerCase();
				String first_upper = lower_case.substring(0, 1).toUpperCase() + lower_case.substring(1);
				String only_upper = field_name.toUpperCase();

				service_save_field.append("String[]\t" + lower_case);
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
		}
		if (multiple_line_name != null && !multiple_line_name.isEmpty()) {
			updatesession.append(
					"\n\nHttpSession session=request.getSession();" + "\nsession.setAttribute(\"header_id\",id);\n");
		}

		// changes done by karamjit
		if (table_name != null && !table_name.isEmpty()) {
			String table_name_lower = table_name.toLowerCase();
			String table_name_first_upper = table_name_lower.substring(0, 1).toUpperCase()
					+ table_name_lower.substring(1);
			String table_name_upper = table_name.toUpperCase();

			String field_name_for_id = id_list.get(0).getMapping();
			System.out.println("field" + field_name_for_id);
			String field_name_for_id_lower = field_name_for_id.toLowerCase();
			String field_name_for_id_upper = field_name_for_id.toUpperCase();
			String field_name_first_upper = field_name_for_id_lower.substring(0, 1).toUpperCase()
					+ field_name_for_id_lower.substring(1);

			save_controller.append(
					"\n\n//--------------------------submit update part---------------------------------------------------"

							+ "\n@Transactional" + "\n@RequestMapping(value = \"/" + table_name_lower
							+ "_update_submit\", method = RequestMethod.POST)"
							+ "\npublic ModelAndView saveReportRegister(@ModelAttribute\t" + table_name_first_upper
							+ "\t" + table_name_lower + ","
							+ "\nBindingResult resultReportRegister, ModelMap map, HttpServletRequest request) throws ParseException {"
							+ "\nint report = 0;" + "int user_id = 0;"
//							+ "(Integer) request.getSession().getAttribute(\"userid\");"
							+ sbmit_parameter_id + id_not_pri_para + date_para + sbmit_parameter_field + "\n"
							+ id_pri_set + id_notpri_set + set_para + date_set + "\n"
							+ "\nhibernateTemplate.saveOrUpdate(" + table_name_lower + ");\n" + "\nint header_id="
							+ table_name_lower + ".getId();" + "\nHttpSession session=request.getSession(); "
							+ "\nsession.setAttribute(\"header_id\",header_id);\n"

							+ "\nreturn new ModelAndView(\"redirect:" + multiple_line_table_name_lower + "_update\");"
							+ "\n}");

			// changes done by karamjit

			// ----------------------header submit
			// controller---------------------------------------------------

			// changes done by karamjit
			action.append(table_name_lower + "_submit");
			action2.append(table_name_lower + "_update_submit");
			// changes done by karamjit

			for (int i = 0; i < rn_userlist.size(); i++) {

				String type_field = rn_userlist.get(i).getType_field();
				String mapping = rn_userlist.get(i).getMapping();
				String mapping_lower = mapping.toLowerCase();
				String sp_name = rn_userlist.get(i).getSp_name_for_dropdown();

				if (type_field.equals("dropdown")) {
					dropdown_controller.append("\n@RequestMapping(value = \"/" + mapping_lower
							+ "_list\", method = RequestMethod.GET)" + "\npublic void \t" + mapping_lower
							+ "_list( HttpServletRequest request, HttpServletResponse response) {"
							+ "\nList<LookupValues> droplist = new ArrayList<LookupValues>();" + "\ntry {"
							+ "\nCallableStatement cStmt;" + "\ntry {"
							+ "\ncStmt = hibernateConfiguration.dataSource().getConnection()"
							+ "\n.prepareCall(\"{call \t" + sp_name + "()}\");"
							+ "\nResultSet rs = cStmt.executeQuery();" + "\nwhile (rs.next()) " + "\n{"
							+ "\nString data = rs.getString(1);" + "\nLookupValues menu = new LookupValues();"
							+ "\nmenu.setDrop_value(data);" + "\ndroplist.add(menu);" + "\n}"
							+ "\n} catch (SQLException e) {" + "\ne.printStackTrace();" + "\n}"
							+ "\ncatch (Exception e) {" + "\nSystem.out.println(e.getMessage());" + "\n}"
							+ "\nString json = null;" + "\njson = new Gson().toJson(droplist);"
							+ "\nresponse.setContentType(\"application/json\");" + "\nresponse.getWriter().write(json);"
							+ "\n} catch (IOException e) {" + "\ne.printStackTrace();" + " \n}}");
				}

				if (type_field.equals("autocomplete")) {
					dropdown_controller.append("\n@RequestMapping(value = \"/" + mapping_lower
							+ "_list\", method = RequestMethod.GET)" + "\npublic void \t" + mapping_lower
							+ "_list( HttpServletRequest request, HttpServletResponse response) {"
							+ "\nList<LookupValues> droplist = new ArrayList<LookupValues>();" + "\ntry {"
							+ "\nCallableStatement cStmt;" + "\ntry {"
							+ "\ncStmt = hibernateConfiguration.dataSource().getConnection()"
							+ "\n.prepareCall(\"{call \t" + sp_name + "()}\");"
							+ "\nResultSet rs = cStmt.executeQuery();" + "\nwhile (rs.next()) " + "\n{"
							+ "\nString data = rs.getString(1);" + "\nLookupValues menu = new LookupValues();"
							+ "\nmenu.setDrop_value(data);" + "\ndroplist.add(menu);" + "\n}"
							+ "\n} catch (SQLException e) {" + "\ne.printStackTrace();" + "\n}"
							+ "\ncatch (Exception e) {" + "\nSystem.out.println(e.getMessage());" + "\n}"
							+ "\nString json = null;" + "\njson = new Gson().toJson(droplist);"
							+ "\nresponse.setContentType(\"application/json\");" + "\nresponse.getWriter().write(json);"
							+ "\n} catch (IOException e) {" + "\ne.printStackTrace();" + " \n}}");
				}

			}

			for (int i = 0; i < line_varchar.size(); i++) {

				String type_field = line_varchar.get(i).getType_field();
				String mapping = line_varchar.get(i).getMapping();
				String mapping_lower = mapping.toLowerCase();
				String sp_name = line_varchar.get(i).getSp_name_for_dropdown();

				if (type_field.equals("dropdown")) {
					dropdown_controller.append("\n@RequestMapping(value = \"/" + mapping_lower
							+ "_list_line\", method = RequestMethod.GET)" + "\npublic void \t" + mapping_lower
							+ "_list( HttpServletRequest request, HttpServletResponse response) {"
							+ "\nList<LookupValues> droplist = new ArrayList<LookupValues>();" + "\ntry {"
							+ "\nCallableStatement cStmt;" + "\ntry {"
							+ "\ncStmt = hibernateConfiguration.dataSource().getConnection()"
							+ "\n.prepareCall(\"{call \t" + sp_name + "()}\");"
							+ "\nResultSet rs = cStmt.executeQuery();" + "\nwhile (rs.next()) " + "\n{"
							+ "\nString data = rs.getString(1);" + "\nLookupValues menu = new LookupValues();"
							+ "\nmenu.setDrop_value(data);" + "\ndroplist.add(menu);" + "\n}"
							+ "\n} catch (SQLException e) {" + "\ne.printStackTrace();" + "\n}"
							+ "\ncatch (Exception e) {" + "\nSystem.out.println(e.getMessage());" + "\n}"
							+ "\nString json = null;" + "\njson = new Gson().toJson(droplist);"
							+ "\nresponse.setContentType(\"application/json\");" + "\nresponse.getWriter().write(json);"
							+ "\n} catch (IOException e) {" + "\ne.printStackTrace();" + " \n}}");
				}

				if (type_field.equals("autocomplete")) {
					dropdown_controller.append("\n@RequestMapping(value = \"/" + mapping_lower
							+ "_list_line\", method = RequestMethod.GET)" + "\npublic void \t" + mapping_lower
							+ "_list( HttpServletRequest request, HttpServletResponse response) {"
							+ "\nList<LookupValues> droplist = new ArrayList<LookupValues>();" + "\ntry {"
							+ "\nCallableStatement cStmt;" + "\ntry {"
							+ "\ncStmt = hibernateConfiguration.dataSource().getConnection()"
							+ "\n.prepareCall(\"{call \t" + sp_name + "()}\");"
							+ "\nResultSet rs = cStmt.executeQuery();" + "\nwhile (rs.next()) " + "\n{"
							+ "\nString data = rs.getString(1);" + "\nLookupValues menu = new LookupValues();"
							+ "\nmenu.setDrop_value(data);" + "\ndroplist.add(menu);" + "\n}"
							+ "\n} catch (SQLException e) {" + "\ne.printStackTrace();" + "\n}"
							+ "\ncatch (Exception e) {" + "\nSystem.out.println(e.getMessage());" + "\n}"
							+ "\nString json = null;" + "\njson = new Gson().toJson(droplist);"
							+ "\nresponse.setContentType(\"application/json\");" + "\nresponse.getWriter().write(json);"
							+ "\n} catch (IOException e) {" + "\ne.printStackTrace();" + " \n}}");
				}

			}
		}

		if (multiple_line_name != null && !multiple_line_name.isEmpty()) {
			String[] single_line_name = multiple_line_name.split(",");
			// StringBuilder setModel=new StringBuilder();

			for (String name : single_line_name) {

				String table_line_lower = name.toLowerCase();
				String table_line_upper = name.toUpperCase();
				String table_line_first_upper = table_line_lower.substring(0, 1).toUpperCase()
						+ table_line_lower.substring(1);

				setModel.append("\nimport com.realnet." + module_name + ".model." + table_line_first_upper + ";");
			}
		}

		if (table_name != null && !table_name.isEmpty()) {

			System.out.println("controller code in if condtion");
			String table_name_lower = table_name.toLowerCase();
			String table_name_first_upper = table_name_lower.substring(0, 1).toUpperCase()
					+ table_name_lower.substring(1);
			String table_name_upper = table_name.toUpperCase();

			controller.append("\n" + "\npackage com.realnet." + module_name + ".controller;"
					+ "\nimport javax.servlet.http.HttpServletRequest;"
					+ "\nimport org.springframework.beans.factory.annotation.Autowired;"
					+ "\nimport org.springframework.stereotype.Controller;"
					+ "\nimport org.springframework.validation.BindingResult;"
					+ "\nimport org.springframework.web.bind.annotation.ModelAttribute;"
					+ "\nimport org.springframework.web.bind.annotation.RequestMapping;"
					+ "\nimport org.springframework.web.bind.annotation.RequestMethod;"
					+ "\nimport org.springframework.web.servlet.ModelAndView;" + "\nimport java.io.IOException;"
					+ "\nimport java.util.List;" + "\nimport org.springframework.orm.hibernate5.HibernateTemplate;"
					+ "\nimport com.realnet." + module_name + ".configuration.HibernateConfiguration;"

					+ setModel + "\nimport org.springframework.web.bind.annotation.RequestParam;"
					+ "\nimport javax.transaction.Transactional;" + "\nimport org.springframework.ui.ModelMap;"
					+ "import javax.servlet.http.HttpServletResponse;" + "import com.realnet." + module_name
					+ ".model.LookupValues;" + "import javax.servlet.http.HttpSession;" + "import java.util.ArrayList;"
					+ "import java.sql.CallableStatement;" + "import java.sql.ResultSet;"
					+ "import java.sql.SQLException;" + "import com.google.gson.Gson;" + "import java.util.Date;"
					+ "import java.text.SimpleDateFormat;" + "import com.realnet." + module_name + ".dao."
					+ dao_name_first_upper + ";" + "import com.realnet." + module_name + ".service."
					+ service_name_first_upper + ";"
//					+ "\nimport org.apache.poi.hssf.model.Model;"
					+ "\nimport com.realnet." + module_name + ".model." + table_name_first_upper + ";"

					+ "import java.text.ParseException;");

			StringBuilder redirection_jsp = new StringBuilder();

			if (multiple_line_name != null && !multiple_line_name.isEmpty()) {
				String[] single_line_name = multiple_line_name.split(",");

				String table_line_lower = single_line_name[0].toLowerCase();
				String table_line_first_upper = table_line_lower.substring(0, 1).toUpperCase()
						+ table_line_lower.substring(1);

				redirection_jsp.append(table_line_first_upper + "_line_view");
				System.out.println("redirection::::" + redirection_jsp);
			} else {
				redirection_jsp.append("redirect:" + table_name_lower + "_grid_view");
			}

			session_id.append(
					"\nint header_id=" + table_name_lower + ".getId();" + "\nHttpSession session=request.getSession(); "
							+ "\nsession.setAttribute(\"header_id\",header_id);");

//			account_columns.append("\n"+table_name+".setAccount_id(account_id);");

			who_columns.append("\n" + table_name + ".setCreated_by(user_id);");

			who_columns_update.append("\n" + table_name + ".setLast_updated_by(user_id);");

			controller.append("\n@Controller" + "\npublic class\t" + controller_name_first_upper + "\n{"
					+ "\n@Autowired" + "\nprivate HibernateConfiguration hibernateConfiguration;" + "\n@Autowired"
					+ "\nprivate HibernateTemplate  hibernateTemplate;" + "@Autowired" + "\nprivate\t"
					+ service_name_first_upper + "\t" + service_name_lower + ";"

					+ "\n@Autowired" + "\nprivate\t" + dao_name_first_upper + "\t" + dao_name_lower + ";"
					+ "\n\n//----------------------entry form submit------------------------------------------"

					+ "\n//-----------------------------------for grid view only------------------------------"
					+ "\n\n@RequestMapping(value=\"/" + table_name_lower + "_grid_view\")" + "\npublic ModelAndView\t"
					+ table_name_lower + "Details(ModelAndView model) throws IOException" + "\n{" + "\nList<"
					+ table_name_first_upper + ">\t" + table_name_lower + "=" + dao_name_lower + ".userlist();"
					+ "\nmodel.addObject(\"" + table_name_lower + "\", " + table_name_lower + ");"
					+ "\nSystem.out.println(\"sujit\");" + "\nmodel.setViewName(\"" + table_name_first_upper
					+ "_grid\");" + "\nreturn model;" + "\n}"

					// ----------------------entry form sbmit---------------------------------
					+ "\n@Transactional" + "\n@RequestMapping(value = \"/" + table_name_lower
					+ "_submit\", method = RequestMethod.POST)"
					+ "\npublic ModelAndView saveServiceRequest(@ModelAttribute\t" + table_name_first_upper + "\t"
					+ table_name_lower + ",BindingResult resultKoel_user ,"
					+ "\nModelMap map, HttpServletRequest request)" + "\n{" + "\n int account_id = 0;"
//					+ "(Integer) request.getSession().getAttribute(\"accountid\");"
					+ "\n int user_id = 0;"
//					+ "(Integer) request.getSession().getAttribute(\"userid\");"
					+ id_para_submit + para + date_para + get_parameter + "\n" + id_notpri_set + set_para + date_set
					+ set_parameter + account_columns + who_columns + who_columns_update
					+ "\nhibernateTemplate.saveOrUpdate(" + table_name_lower + ");" + session_id
					+ "\nreturn new ModelAndView(\"" + redirection_jsp + "\");" + "\n" + "\n\n}"

					// ------------------entry form------------------------------------------
					+ "\n\n" + "\n@RequestMapping(\"/" + table_name_lower + "_entryform\")"
					+ "\npublic ModelAndView input_form3(HttpServletRequest request, ModelMap map) " + "\n{"
					+ "\nreturn new ModelAndView(\"" + jsp_name + "\");" + "\n}");

			if (multiple_line_name != null && !multiple_line_name.isEmpty()) {
				controller.append("\n");
			} else {
				controller.append("\n}");
			}

		}

		if (table_name != null && !table_name.isEmpty()) {
			String table_name_lower = table_name.toLowerCase();
			String table_name_first_upper = table_name_lower.substring(0, 1).toUpperCase()
					+ table_name_lower.substring(1);
			String table_name_upper = table_name.toUpperCase();

			for (int i = 0; i < id_list.size(); i++) {
				String field_name = id_list.get(i).getMapping();
				String lower_case = field_name.toLowerCase();
				String first_upper = lower_case.substring(0, 1).toUpperCase() + lower_case.substring(1);
				String only_upper = field_name.toUpperCase();

				model_class3.append("\n@Column(name = \"" + only_upper + "\")" + "\nprivate int \t" + lower_case + ";");

				model_class4.append("\n\npublic int get" + first_upper + "() \n{" + "\nreturn\t" + lower_case + ";\n}"

						+ "\n\npublic void set" + first_upper + "(int\t" + lower_case + ")\n{" + "\nthis." + lower_case
						+ "=" + lower_case + ";" + "\n}");

			}
		}

		if (table_name != null && !table_name.isEmpty()) {
			String table_name_lower = table_name.toLowerCase();
			String table_name_first_upper = table_name_lower.substring(0, 1).toUpperCase()
					+ table_name_lower.substring(1);
			String table_name_upper = table_name.toUpperCase();
			for (int i = 0; i < rn_userlist.size(); i++) {
				String field_name = rn_userlist.get(i).getMapping();
				String lower_case = field_name.toLowerCase();
				String first_upper = lower_case.substring(0, 1).toUpperCase() + lower_case.substring(1);
				String only_upper = field_name.toUpperCase();

				model_class1
						.append("\n@Column(name = \"" + only_upper + "\")" + "\nprivate String \t" + lower_case + ";");

				model_class2
						.append("\n\npublic String get" + first_upper + "() \n{" + "\nreturn\t" + lower_case + ";\n}"

								+ "\n\npublic void set" + first_upper + "(String\t" + lower_case + ")\n{" + "\nthis."
								+ lower_case + "=" + lower_case + ";" + "\n}");
			}
		}

		// --------------------------------ModeL class
		// code------------------------------------------------//
		for (int i = 0; i < id_list.size(); i++) {
			String field_name = id_list.get(i).getMapping();
			String lower_case = field_name.toLowerCase();
			String first_upper = lower_case.substring(0, 1).toUpperCase() + lower_case.substring(1);
			String only_upper = field_name.toUpperCase();

			model_who_columns.append("\n @Column(name = \"CREATED_BY\")" + "\nprivate int created_by;"

					+ "\npublic int getCreated_by()" + "\n{" + "\nreturn created_by;" + "\n}"

					+ "\npublic void setCreated_by(int " + lower_case + ")" + "\n{" + "\nthis.created_by = "
					+ lower_case + ";" + "\n}");

			model_who_update_by.append("\n@Column(name = \"LAST_UPDATED_BY\")" + "\nprivate int last_updated_by;"

					+ "\npublic int getLast_updated_by()" + "\n{" + "\nreturn last_updated_by;" + "\n}"

					+ "\npublic void setLast_updated_by(int " + lower_case + ")" + "\n{" + "\nthis.last_updated_by = "
					+ lower_case + ";" + "\n}");

		}

		model_who_columns2.append("\n@DateTimeFormat(pattern = \"YYYY-MM-DD\")"
				+ "\n@Column(name = \"CREATION_DATE\" , updatable = false)"
				+ "\nprivate Date creation_date= new java.sql.Date(new java.util.Date().getTime());"

				+ "\npublic Date getCreation_date()" + "\n{" + "\nreturn creation_date;" + "\n}"

				+ "\npublic void setCreation_date(Date creation_date)" + "\n{" + "\nthis.creation_date = creation_date;"
				+ "\n}");

		model_who_update_date.append("\n@DateTimeFormat(pattern = \"YYYY-MM-DD\")"
				+ "\n@Column(name = \"LAST_UPDATE_DATE\" , updatable = true)"
				+ "\nprivate Date last_update_date= new java.sql.Date(new java.util.Date().getTime());"

				+ "\npublic Date getLast_update_date()" + "\n{" + "\nreturn last_update_date;" + "\n}"

				+ "\npublic void setLast_update_date(Date last_update_date)" + "\n{"
				+ "\nthis.last_update_date = last_update_date;" + "\n}");

		popup_model.append("\n@Column(name = \"ACCOUNT\")" + "\nprivate String 	account;"

				+ "\npublic String getAccount()" + "\n{" + "\nreturn account;" + "\n}"

				+ "\npublic void setAccount(String account)" + "\n{" + "\nthis.account = account;" + "\n}"

				+ "\n@Column(name = \"FORM_NAME\")" + "\nprivate String 	form_name;"

				+ "\npublic String getForm_name()" + "\n{" + "\nreturn form_name;" + "\n}"

				+ "\npublic void setForm_name(String form_name)" + "\n{" + "\nthis.form_name = form_name;" + "\n}"

				+ "\n@Column(name = \"FORM_CODE\")" + "\nprivate String 	form_code;"

				+ "\npublic String getForm_code()" + "\n{" + "\n\nreturn form_code;" + "\n}"

				+ "\npublic void setForm_code(String form_code)" + "\n{" + "\nthis.form_code = form_code;" + "\n}");

		if (table_name != null && !table_name.isEmpty()) {
			String table_name_lower = table_name.toLowerCase();
			String table_name_first_upper = table_name_lower.substring(0, 1).toUpperCase()
					+ table_name_lower.substring(1);
			String table_name_upper = table_name.toUpperCase();

			model_class.append("   package com.realnet." + module_name + ".model;"

					+ "\nimport javax.persistence.Column;" + "\nimport javax.persistence.Entity;"
					+ "\nimport javax.persistence.GeneratedValue;" + "\nimport javax.persistence.GenerationType;"
					+ "\nimport javax.persistence.Id;" + "\nimport javax.persistence.SequenceGenerator;"
					+ "\nimport javax.persistence.Table;" + "import org.hibernate.annotations.GenericGenerator;"
					+ "import org.springframework.format.annotation.DateTimeFormat;" + "import java.util.Date;"
					+ "\n@Entity" + "\n@Table(name = \"" + table_name_upper + "\")" + "\npublic class\t"
					+ table_name_first_upper + "\n{" + "\n@Id"
					+ "\n@GeneratedValue(strategy = GenerationType.IDENTITY, generator =\"native\")"
					+ "\n@GenericGenerator(name = \"native\", strategy = \"native\")" + model_class3 + model_hid_column
					+ model_hid_getter + model_hid_setter + model_class_for_var + date_model_variable + "\n"
					+ model_class4 + "\n" + model_class1 + "\n" + model_class2 + date_model_set_variable
					+ model_class_for_set + model_who_columns + model_who_columns2 + model_who_update_date
					+ model_who_update_by + model_class_for_attribute + model_class_for_attribute2 + popup_model
					+ "\n@Column(name = \"ACCOUNT_ID\")" + "\nprivate int account_id;" + "\npublic int getAccount_id()"
					+ "\n{" + "\nreturn account_id;" + "\n}" + "\npublic void setAccount_id(int account_id)" + "\n{"
					+ "\nthis.account_id = account_id;" + "\n}"

					+ "\n}");

		}
		// ------------------------------service
		// part-------------------------------------//

		if (table_name != null && !table_name.isEmpty()) {
			String table_name_lower = table_name.toLowerCase();
			String table_name_first_upper = table_name_lower.substring(0, 1).toUpperCase()
					+ table_name_lower.substring(1);
			String table_name_upper = table_name.toUpperCase();

			service.append("\npackage com.realnet." + module_name + ".service;" + "\nimport java.util.Date;"
					+ "\nimport java.util.ArrayList;" + "\nimport java.util.List;"
					+ "\nimport org.springframework.stereotype.Service;" + "\nimport com.realnet." + module_name
					+ ".model." + table_name_first_upper + ";" + setModel);

			service.append("\npublic interface\t" + service_name_first_upper + "\n{");

			// changes done by karamjit

			/*
			 * if (multiple_line_name != null && !multiple_line_name.isEmpty()) { String
			 * multiple_line_name_lower = multiple_line_name.toLowerCase(); String
			 * multiple_line_name_first_upper = multiple_line_name_lower.substring(0,
			 * 1).toUpperCase()+ multiple_line_name_lower.substring(1); String
			 * multiple_line_name_upper = multiple_line_name.toUpperCase();
			 * service.append("\npublic List<" + multiple_line_name_first_upper +
			 * "> prefield_"+multiple_line_name_lower+"(int u_id);" ); }
			 */

			// changes done by karamjit

			service.append("\npublic List<" + table_name_first_upper + "> prefield(int u_id);" + "\npublic List<"
					+ table_name_first_upper + "> prefield_" + table_name_lower + "(int u_id);" + "\npublic List<"
					+ table_name_first_upper + "> userlist();" + "\npublic int save(int rowcount,int user_id,"
					+ service_save_id + var_for_pass_para + date_array_para + service_save_field + ") ;"
					+ "\npublic int saveheader(" + table_name_first_upper + "\t" + table_name_lower + ");");

			service.append("\n}");

			service_impl.append("\npackage com.realnet." + module_name + ".service;" + "\nimport java.util.Date;"
					+ "\nimport java.util.ArrayList;" + "\nimport java.util.List;"
					+ "\nimport org.springframework.beans.factory.annotation.Autowired;"
					+ "\nimport org.springframework.stereotype.Component;"
					+ "\nimport org.springframework.stereotype.Service;" + "\nimport com.realnet." + module_name
					+ ".dao." + dao_name_first_upper + ";" + "\nimport com.realnet." + module_name + ".model."
					+ table_name_first_upper + ";" + setModel);

			service_impl.append("\n@Service" + "\npublic class\t" + service_impl_name_first_upper + "\timplements\t"
					+ service_name_first_upper + "\n {" + "\n\n@Autowired" + "\nprivate \t" + dao_name_first_upper
					+ "\t" + dao_name_lower + ";" + "\n\n@Override" + " \npublic List<" + table_name_first_upper
					+ "> prefield(int u_id)" + "\n{" + "\nreturn\t" + dao_name_lower + ".prefield(u_id);" + "\n }"
					+ "\n\n@Override" + " \npublic List<" + table_name_first_upper + "> prefield_" + table_name_lower
					+ "(int u_id)" + "\n{" + "\nreturn\t" + dao_name_lower + ".prefield_" + table_name_lower + "(u_id);"
					+ "\n }");

			// changes done by karamjit

//					if (multiple_line_name != null && !multiple_line_name.isEmpty()) {
//						String multiple_line_name_lower = multiple_line_name.toLowerCase();
//						String multiple_line_name_first_upper = multiple_line_name_lower.substring(0, 1).toUpperCase()+ multiple_line_name_lower.substring(1);
//						String multiple_line_name_upper = multiple_line_name.toUpperCase();
//						
//						service_impl.append("\n\n@Override" + " \npublic List<" + multiple_line_name_first_upper+ "> prefield_"+multiple_line_name_lower+"(int u_id)" 
//					+ "\n{" + "\nreturn\t" + dao_name_lower + ".prefield_"+multiple_line_name_lower+"(u_id);" 
//					+ "\n }");
//						
//					}
			// changes done by karamjit

			service_impl.append("\n@Override" + "\npublic List<" + table_name_first_upper + "> userlist() " + "\n{"
					+ "\nreturn\t" + dao_name_lower + ".userlist();" + "\n}" + "\n@Override"
					+ "\npublic int save(int rowcount,int user_id," + service_save_id + var_for_pass_para
					+ date_array_para + service_save_field + ") " + "\n{" + "\nreturn\t" + dao_name_lower
					+ ".save(rowcount,user_id," + service_save_id1 + var_for_pass_para_id + date_array_value_for_lower
					+ service_save_field1 + ");" + "\n}"

					+ "public int saveheader(" + table_name_first_upper + "\t" + table_name_lower + ")" + "{"
					+ "return \t" + dao_name_lower + ".saveheader(" + table_name_lower + ");" + "}");

			service_impl.append("\n}"

			);
		}

		// ------------------------------------dao code
		// ------------------------------------------------//

		if (table_name != null && !table_name.isEmpty()) {
			String table_name_lower = table_name.toLowerCase();
			String table_name_first_upper = table_name_lower.substring(0, 1).toUpperCase()
					+ table_name_lower.substring(1);
			String table_name_upper = table_name.toUpperCase();

			for (int i = 0; i < id_list.size(); i++) {
				String field_name = id_list.get(i).getMapping();
				String lower_case = field_name.toLowerCase();
				String first_upper = lower_case.substring(0, 1).toUpperCase() + lower_case.substring(1);
				String only_upper = field_name.toUpperCase();

				dao_save_id.append("String[]\t" + lower_case + ",");
				// service_save_id1.append(lower_case+",");

			}
		}

		if (table_name != null && !table_name.isEmpty()) {
			String table_name_lower = table_name.toLowerCase();
			String table_name_first_upper = table_name_lower.substring(0, 1).toUpperCase()
					+ table_name_lower.substring(1);
			String table_name_upper = table_name.toUpperCase();
			for (int i = 0; i < rn_userlist.size(); i++) {
				String field_name = rn_userlist.get(i).getMapping();
				String lower_case = field_name.toLowerCase();
				String first_upper = lower_case.substring(0, 1).toUpperCase() + lower_case.substring(1);
				String only_upper = field_name.toUpperCase();

				dao_save_field.append("String[]\t" + lower_case);
				if (i < (rn_userlist.size() - 1)) {
					dao_save_field.append(",");

				}
				dao_save_field.append("\t");
				// service_save_field1.append(lower_case+",");

			}

		}

		StringBuilder setModel4 = new StringBuilder();
		if (multiple_line_name != null && !multiple_line_name.isEmpty()) {
			String[] single_line_name = multiple_line_name.split(",");
			for (String name : single_line_name) {

				String table_line_lower = name.toLowerCase();
				String table_line_upper = name.toUpperCase();
				String table_line_first_upper = table_line_lower.substring(0, 1).toUpperCase()
						+ table_line_lower.substring(1);

				setModel4.append("\nimport com.realnet." + module_name + ".model." + table_line_first_upper + ";");
			}
		}

		if (table_name != null && !table_name.isEmpty()) {
			String table_name_lower = table_name.toLowerCase();
			String table_name_first_upper = table_name_lower.substring(0, 1).toUpperCase()
					+ table_name_lower.substring(1);
			String table_name_upper = table_name.toUpperCase();

			dao.append("\npackage com.realnet." + module_name + ".dao;" + "\nimport java.util.Date;"
					+ "\nimport java.util.List;" + "\nimport com.realnet." + module_name + ".model."
					+ table_name_first_upper + ";" + setModel);

			/*
			 * //changes done by karamjit if (multiple_line_name != null &&
			 * !multiple_line_name.isEmpty()) { String multiple_line_name_lower =
			 * multiple_line_name.toLowerCase(); String multiple_line_name_first_upper =
			 * multiple_line_name_lower.substring(0, 1).toUpperCase()+
			 * multiple_line_name_lower.substring(1); String multiple_line_name_upper =
			 * multiple_line_name.toUpperCase(); dao.append("\nimport com.springmvc.model."
			 * + multiple_line_name_first_upper + ";"); }
			 */
			// changes done by karamjit

			dao.append(
					"\npublic interface\t" + dao_name_first_upper + " {	" + "\npublic List<" + table_name_first_upper
							+ "> userlist();" + "\npublic List<" + table_name_first_upper + "> prefield(int u_id);");
			// changes done by karamjit
			/*
			 * if (multiple_line_name != null && !multiple_line_name.isEmpty()) { String
			 * multiple_line_name_lower = multiple_line_name.toLowerCase(); String
			 * multiple_line_name_first_upper = multiple_line_name_lower.substring(0,
			 * 1).toUpperCase()+ multiple_line_name_lower.substring(1); String
			 * multiple_line_name_upper = multiple_line_name.toUpperCase();
			 * dao.append("\n\n public List<" + multiple_line_name_first_upper+
			 * "> prefield_"+multiple_line_name_lower+"(int u_id);"); }
			 */
			// changes done by karamjit

			dao.append("\n\n public List<" + table_name_first_upper + "> prefield_" + table_name_lower + "(int u_id);");

			dao.append("\npublic int save(int rowcount,int user_id," + dao_save_id + var_for_pass_para + date_array_para
					+ dao_save_field + ");"

					+ "\npublic int saveheader(" + table_name_first_upper + "\t" + table_name_lower + ");");

			if (multiple_line_name != null && !multiple_line_name.isEmpty()) {
				dao.append("\n");
			} else {
				dao.append("\n}");
			}
		}

		if (table_name != null && !table_name.isEmpty()) {
			String table_name_lower = table_name.toLowerCase();
			String table_name_first_upper = table_name_lower.substring(0, 1).toUpperCase()
					+ table_name_lower.substring(1);
			String table_name_upper = table_name.toUpperCase();

			for (int i = 0; i < id_list.size(); i++) {
				String field_name = id_list.get(i).getMapping();
				String lower_case = field_name.toLowerCase();
				String first_upper = lower_case.substring(0, 1).toUpperCase() + lower_case.substring(1);
				String upper_case = field_name.toUpperCase();

				sqlField_id.append(upper_case + ",");

				setField_id.append(
						"\n" + table_name_lower + ".set" + first_upper + "(rs.getInt(\"" + upper_case + "\"));");

				field_for_save_id.append("\n" + table_name_lower + ".set" + first_upper + "(infonum);");

				get_id_for_sbmit_header_line.append(first_upper);

			}
		}

		if (table_name != null && !table_name.isEmpty()) {
			String table_name_lower = table_name.toLowerCase();
			String table_name_first_upper = table_name_lower.substring(0, 1).toUpperCase()
					+ table_name_lower.substring(1);
			String table_name_upper = table_name.toUpperCase();
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

				setField.append(
						"\n" + table_name_lower + ".set" + first_upper + "(rs.getString(\"" + upper_case + "\"));");

				field_for_save_field
						.append("\n" + table_name_lower + ".set" + first_upper + "(" + lower_case + "[i]);");
			}

		}

		if (table_name != null && !table_name.isEmpty()) {
			String table_name_lower = table_name.toLowerCase();
			String table_name_first_upper = table_name_lower.substring(0, 1).toUpperCase()
					+ table_name_lower.substring(1);
			String table_name_upper = table_name.toUpperCase();

			popup_dao_field.append("\n" + table_name_lower + ".setCreated_by(rs.getInt(\"CREATED_BY\"));"

					+ "\n" + table_name_lower + ".setLast_updated_by(rs.getInt(\"LAST_UPDATED_BY\"));"

					+ "\n" + table_name_lower + ".setCreation_date(rs.getDate(\"CREATION_DATE\"));"

					+ "\n" + table_name_lower + ".setLast_update_date(rs.getDate(\"LAST_UPDATE_DATE\"));"

					+ "\n" + table_name_lower + ".setAccount(rs.getString(\"ACCOUNT\"));"

					+ "\n" + table_name_lower + ".setForm_name(rs.getString(\"FORM_NAME\"));");

			popup_sql_field.append(",CREATED_BY,LAST_UPDATED_BY,CREATION_DATE,LAST_UPDATE_DATE,ACCOUNT,FORM_NAME ");

		}

		if (table_name != null && !table_name.isEmpty()) {
			String table_name_lower = table_name.toLowerCase();
			String table_name_first_upper = table_name_lower.substring(0, 1).toUpperCase()
					+ table_name_lower.substring(1);
			String table_name_upper = table_name.toUpperCase();

			String field_name_for_id = id_list.get(0).getMapping();
			System.out.println("field" + field_name_for_id);
			String field_name_for_id_lower = field_name_for_id.toLowerCase();
			String field_name_for_id_upper = field_name_for_id.toUpperCase();
			String field_name_first_upper = field_name_for_id_lower.substring(0, 1).toUpperCase()
					+ field_name_for_id_lower.substring(1);

			dao_impl_prefield
					.append("\n\n@Override" + "\npublic List<" + table_name_first_upper + "> prefield(int u_id)" + "\n{"

							+ "\nString sql =\"SELECT " + sqlField_id + var_for_pass_para_id_for_upper
							+ date_array_value + sqlField + "," + attribute_for_select_statment + popup_sql_field
							+ "FROM\t" + table_name_upper + "\tWHERE\t" + field_name_for_id_upper + "= \"+u_id+\"\";"
							+ "\nList<" + table_name_first_upper + "> userlist = jdbcTemplate.query(sql, new RowMapper<"
							+ table_name_first_upper + ">() {" + "\n@Override" + "\npublic\t" + table_name_first_upper
							+ "\tmapRow(ResultSet rs, int rowNum) throws SQLException {" + "\n" + table_name_first_upper
							+ "\t" + table_name_lower + " = new \t" + table_name_first_upper + "();" + setField_id
							+ id_array_set_in_dao + date_array_set_in_dao_for_date + setField
							+ attribute_set_for_grid_dao + popup_dao_field + "\nreturn\t" + table_name_lower + ";"
							+ "\n}" + "\n});" + "\nreturn userlist;" + "\n}");

			dao_impl_prefield.append("\n\n@Override" + "\npublic List<" + table_name_first_upper + "> prefield_"
					+ table_name_lower + "(int u_id)" + "\n{"

					+ "\nString sql =\"SELECT " + sqlField_id + var_for_pass_para_id_for_upper + date_array_value
					+ sqlField + "," + attribute_for_select_statment + popup_sql_field + "FROM\t" + table_name_upper
					+ "\tWHERE\t " + field_name_for_id_upper + " = \"+u_id+\"\";" + "\nList<" + table_name_first_upper
					+ "> userlist = jdbcTemplate.query(sql, new RowMapper<" + table_name_first_upper + ">() {"
					+ "\n@Override" + "\npublic\t" + table_name_first_upper
					+ "\tmapRow(ResultSet rs, int rowNum) throws SQLException {" + "\n" + table_name_first_upper + "\t"
					+ table_name_lower + " = new \t" + table_name_first_upper + "();" + setField_id
					+ id_array_set_in_dao + date_array_set_in_dao_for_date + setField + attribute_set_for_grid_dao
					+ popup_dao_field + "\nreturn\t" + table_name_lower + ";" + "\n}" + "\n});" + "\nreturn userlist;"
					+ "\n}");
			// changes done by karamjit

			// changes done by karamjit

			who_columns_update_dao.append("\n" + table_name + " .setLast_updated_by(user_id);");

			who_columns_dao.append("\n" + table_name + ".setCreated_by(user_id);");

			dao_impl_save.append("\n@Override" + "\n@Transactional" + "\npublic int save(int rowcount,int user_id,"
					+ dao_save_id + var_for_pass_para + date_array_para + dao_save_field + ")" + "\n{"
					+ "\nint infonum = 0;" + "\nfor (int i = 0; i < rowcount; i++) " + "\n{" + "\n"
					+ table_name_first_upper + "\t" + table_name_lower + "= new\t" + table_name_first_upper + "();"
					+ "\nif (" + field_name_for_id_lower + " != null && " + field_name_for_id_lower + ".length > 0) "
					+ "\n{" + "\ninfonum = Integer.parseInt(" + field_name_for_id_lower + "[i]);" + "\n} else " + "\n{"
					+ "\ninfonum = " + table_name_lower + ".get" + field_name_first_upper + "();" + "\n}"
					+ field_for_save_id + id_array_set_in_dao2 + date_set + field_for_save_field

					+ who_columns_update_dao + who_columns_dao + "\nhibernateTemplate.saveOrUpdate(" + table_name_lower
					+ ");" + "\n}"

					+ "\nreturn 1;" + "\n}");
		}

		StringBuilder setModel2 = new StringBuilder();
		if (multiple_line_name != null && !multiple_line_name.isEmpty()) {
			String[] single_line_name = multiple_line_name.split(",");
			for (String name : single_line_name) {

				String table_line_lower = name.toLowerCase();
				String table_line_upper = name.toUpperCase();
				String table_line_first_upper = table_line_lower.substring(0, 1).toUpperCase()
						+ table_line_lower.substring(1);

				setModel2.append("\nimport com.realnet." + module_name + ".model." + table_line_first_upper + ";");
			}
		}

		if (table_name != null && !table_name.isEmpty()) {
			String table_name_lower = table_name.toLowerCase();
			String table_name_first_upper = table_name_lower.substring(0, 1).toUpperCase()
					+ table_name_lower.substring(1);
			String table_name_upper = table_name.toUpperCase();
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
					+ "\nimport org.springframework.jdbc.core.namedparam.SqlParameterSource;" + setModel2
					+ "\nimport org.springframework.orm.hibernate5.HibernateTemplate;"
					+ "\nimport org.springframework.stereotype.Repository;"

					// + "\nimport com.mysql.cj.xdevapi.SessionFactory;"
					// + "\nimport com.realnet.configuration.HibernateConfiguration;" + "\nimport
					// com.realnet.comm.model."

					+ "\nimport com.mysql.cj.xdevapi.SessionFactory;"
					+ "\nimport com.realnet.configuration.HibernateConfiguration;" + "\nimport com.realnet."
					+ module_name + ".model."

					+ table_name_first_upper + ";" + setModel);

			dao_impl.append("\n@Repository(\"" + dao_name_first_upper + "\")" + "\npublic class\t"
					+ dao_impl_name_first_upper + "\timplements\t" + dao_name_first_upper + "\n{" + "\n@Autowired"
					+ "\nprivate JdbcTemplate jdbcTemplate;" + "\n@Autowired"
					+ "\nprivate HibernateTemplate hibernateTemplate;" + "\n@Autowired"
					+ "\nprivate HibernateConfiguration hibernateConfiguration; " + "\n\n@Override" + "\npublic List<"
					+ table_name_first_upper + "> userlist() " + "\n{" + "\nString sql =\"SELECT\t" + sqlField_id
					+ var_for_pass_para_id_for_upper + date_array_value + sqlField + "," + attribute_for_select_statment
					+ "FROM\t" + table_name_upper + "\";" + "\nList<" + table_name_first_upper
					+ "> userlist = jdbcTemplate.query(sql, new RowMapper<" + table_name_first_upper + ">()" + "\n{"
					+ "	\n@Override" + "\npublic\t" + table_name_first_upper
					+ "\tmapRow(ResultSet rs, int rowNum) throws SQLException" + "\n{" + "\n" + table_name_first_upper
					+ "\t" + table_name_lower + " = new\t" + table_name_first_upper + "();" + "\n" + setField_id
					+ id_array_set_in_dao + date_array_set_in_dao_for_date + setField + attribute_set_for_grid_dao

					+ "\nreturn\t" + table_name_lower + ";" + "\n}"

					+ "\n});"

					+ "\nreturn userlist;" + "}" + dao_impl_prefield + dao_impl_save

					+ "\n\n@Transactional" + "\npublic int saveheader(" + table_name_first_upper + "\t"
					+ table_name_lower + ")" + "\n{" + "\nhibernateTemplate.saveOrUpdate(" + table_name_lower + ");"
					+ "\nSystem.out.println(" + table_name_lower + ".get" + get_id_for_sbmit_header_line + "());"
					+ "\nreturn \t" + table_name_lower + ".get" + get_id_for_sbmit_header_line + "();"

					+ "\n} " + dao_impl_save_line + dao_impl_for_update_line + "");

			if (multiple_line_name != null && !multiple_line_name.isEmpty()) {
				dao_impl.append("\n");
			} else {
				dao_impl.append("\n}");
			}

		}

		// ----------------------------------jsp coding------------------------

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

				+ " \n</head>");

		form.append("\n<form action=\"" + action + "\" class=\"form-horizontal\" id=\"Regi\" method=\"Post\">"
				+ "\n<input type=\"hidden\" name=\"test\" id=\"test\" value=\"\" />" + " \n<table>" + "\n<tr>"
				+ "<td  style=\"display:none;\">" + " <input type=\"text\" id=\"" + header_id_for_line
				+ "\" class=\"col-xs-10 col-sm-11\"  name=\"" + header_id_for_line + "\" value=\"\"/>" + "</td>");

		if (table_name != null && !table_name.isEmpty()) {
			String table_name_lower = table_name.toLowerCase();
			String table_name_first_upper = table_name_lower.substring(0, 1).toUpperCase()
					+ table_name_lower.substring(1);
			String table_name_upper = table_name.toUpperCase();
			for (int i = 0; i < id_notprimary.size(); i++) {

				String field_name = id_notprimary.get(i).getMapping();
				String data_type = id_notprimary.get(i).getDataType();
				String lower_case = field_name.toLowerCase();
				String only_upper = field_name.toUpperCase();
				String first_upper = lower_case.substring(0, 1).toUpperCase() + lower_case.substring(1);

				if (data_type.equals("int")) {
					form.append("\n<td>" + "\n<div class=\"form-group\" style=\"margin-left: 10%; margin-right: 10%;\">"
							+ "\n<label class=\"control-label col-xs-12 col-sm-3 no-padding-right\">" + first_upper
							+ "\n<i class=\"menu-icon fa red\"> *</i>" + "\n</label>"
							+ "\n<div class=\"col-xs-12 col-sm-9\">" + "\n<div class=\"clearfix\">"
							+ "\n<input   value=\"${" + table_name_lower + "_updt." + lower_case
							+ "}\"type=\"text\" name=\"" + lower_case + "\"id=\"" + lower_case
							+ "\"class=\"col-xs-12 col-sm-4\" required/>" + "\n</div>" + "\n</div>" + "\n</div>"
							+ "\n</td>");
				}
			}

		}
		form.append("\n</tr>");

		form.append("\n<tr>");

		if (table_name != null && !table_name.isEmpty()) {
			String table_name_lower = table_name.toLowerCase();
			String table_name_first_upper = table_name_lower.substring(0, 1).toUpperCase()
					+ table_name_lower.substring(1);
			String table_name_upper = table_name.toUpperCase();

			for (int i = 0; i < datetime_list.size(); i++) {

				System.out.println("---under for loop latest------");

				String field_name = datetime_list.get(i).getMapping();
				String data_type = datetime_list.get(i).getDataType();
				String lower_case = field_name.toLowerCase();
				String only_upper = field_name.toUpperCase();
				String first_upper = lower_case.substring(0, 1).toUpperCase() + lower_case.substring(1);

				boolean mandatory = datetime_list.get(i).isMandatory();
				boolean readonly = datetime_list.get(i).isReadonly();
				boolean hidden = datetime_list.get(i).isHidden();

				String mapping = datetime_list.get(i).getMapping();

				String mapping_lower = mapping.toLowerCase();

				boolean dependent = datetime_list.get(i).isDependent();
				String dependent_on = datetime_list.get(i).getDependent_on();
				String dependent_sp = datetime_list.get(i).getDependent_sp();
				String dependent_sp_param = datetime_list.get(i).getDependent_sp_param();

				boolean sequence = datetime_list.get(i).isSequence();
				String seq_name = datetime_list.get(i).getSeq_name();
				String seq_sp = datetime_list.get(i).getSeq_sp();
				String seq_sp_param = datetime_list.get(i).getSeq_sp_param();

				boolean validation_1 = datetime_list.get(i).isValidation_1();
				String val_type = datetime_list.get(i).getVal_type();
				String val_sp = datetime_list.get(i).getVal_sp();
				String val_sp_param = datetime_list.get(i).getVal_sp_param();

				boolean default_1 = datetime_list.get(i).isDefault_1();
				String default_typename = datetime_list.get(i).getDefault_type();
				String default_value = datetime_list.get(i).getDefault_value();
				String default_sp = datetime_list.get(i).getDefault_sp();
				String default_sp_param = datetime_list.get(i).getDefault_sp_param();

				boolean calculated_field = datetime_list.get(i).isCalculated_field();
				String cal_sp = datetime_list.get(i).getCal_sp();
				String cal_sp_param = datetime_list.get(i).getCal_sp_param();

				boolean add_to_grid = datetime_list.get(i).getAdd_to_grid();

				boolean sp_for_dropdown = datetime_list.get(i).getSp_for_dropdown();
				boolean sp_for_autocomplete = datetime_list.get(i).getSp_for_autocomplete();

				String sp_name_for_dropdown = datetime_list.get(i).getSp_name_for_dropdown();
				String sp_name_for_autocomplete = datetime_list.get(i).getSp_name_for_autocomplete();

				System.out.println("-----value of mantadoryr laest-----" + mandatory);

				if (data_type.equals("datetime")) {

					System.out.println("---under if condition loop latest------");

					form.append("\n<td>" + "\n<div class=\"form-group\" style=\"margin-left: 10%; margin-right: 10%;\">"
							+ "\n<label class=\"control-label col-xs-12 col-sm-3 no-padding-right\">" + "\n "
							+ field_name);

					System.out.println("-------------mandatory for testing=------------------" + mandatory);

					//if (mandatory == true) {
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

					//if (mandatory == true) {
					if (mandatory == true) {
						System.out.println("-------------in loop 1 part 2 required-------------------");
						form.append("required");
					}

					//if (readonly == true) {
					if (readonly == true) {
						System.out.println("-------------in loop 1 part 2 required-------------------");
						form.append("  readonly");
					}
					form.append(
							"/>\n" + "\n<span class=\"input-group-addon\"> <i class=\"fa fa-calendar bigger-110\"></i>"
									+ "\n</span>" + "\n</div>" + "\n</div>" + "\n</div>" + "\n</div>" + "\n</td>");
				}

				date_script.append(" $('#datePicker" + i
						+ "').datepicker({format : 'dd-mm-yyyy'}).on('changeDate', function(e) {$('#eventForm').formValidation('revalidateField', 'date');});\n\n");

			}
		}

		form.append("\n</tr>");

		form.append("\n<tr>");

		if (table_name != null && !table_name.isEmpty()) {
			String table_name_lower = table_name.toLowerCase();
			String table_name_first_upper = table_name_lower.substring(0, 1).toUpperCase()
					+ table_name_lower.substring(1);
			String table_name_upper = table_name.toUpperCase();
			for (int i = 0; i < rn_userlist.size(); i++) {
				String field_name4 = rn_userlist.get(i).getMapping();
				String mapping4 = rn_userlist.get(i).getMapping();
				String data_type4 = rn_userlist.get(i).getDataType();
				String lower_case = field_name4.toLowerCase();
				System.out.println("field name-------" + field_name4);
				System.out.println("mapping-------" + mapping4);
				System.out.println("data type-------" + data_type4);

				String mapping_lower = mapping4.toLowerCase();

				boolean mandatory = rn_userlist.get(i).isMandatory();
				boolean readonly = rn_userlist.get(i).isReadonly();
				boolean hidden = rn_userlist.get(i).isHidden();

				// String mapping=rn_userlist.get(i).getMapping();

				String type_field = rn_userlist.get(i).getType_field();

				boolean dependent = rn_userlist.get(i).isDependent();
				String dependent_on = rn_userlist.get(i).getDependent_on();
				String dependent_sp = rn_userlist.get(i).getDependent_sp();
				String dependent_sp_param = rn_userlist.get(i).getDependent_sp_param();

				boolean sequence = rn_userlist.get(i).isSequence();
				String seq_name = rn_userlist.get(i).getSeq_name();
				String seq_sp = rn_userlist.get(i).getSeq_sp();
				String seq_sp_param = rn_userlist.get(i).getSeq_sp_param();

				boolean validation_1 = rn_userlist.get(i).isValidation_1();
				String val_type = rn_userlist.get(i).getVal_type();
				String val_sp = rn_userlist.get(i).getVal_sp();
				String val_sp_param = rn_userlist.get(i).getVal_sp_param();

				boolean default_1 = rn_userlist.get(i).isDefault_1();
				String default_typename = rn_userlist.get(i).getDefault_type();
				String default_value = rn_userlist.get(i).getDefault_value();
				String default_sp = rn_userlist.get(i).getDefault_sp();
				String default_sp_param = rn_userlist.get(i).getDefault_sp_param();

				boolean calculated_field = rn_userlist.get(i).isCalculated_field();
				String cal_sp = rn_userlist.get(i).getCal_sp();
				String cal_sp_param = rn_userlist.get(i).getCal_sp_param();

				boolean add_to_grid = rn_userlist.get(i).getAdd_to_grid();

				boolean sp_for_dropdown = rn_userlist.get(i).getSp_for_dropdown();
				boolean sp_for_autocomplete = rn_userlist.get(i).getSp_for_autocomplete();

				String sp_name_for_dropdown = rn_userlist.get(i).getSp_name_for_dropdown();
				String sp_name_for_autocomplete = rn_userlist.get(i).getSp_name_for_autocomplete();

				if (i <= 2) {
					if (type_field.equals("textfield")) {
						form.append(
								"\n<td>" + "\n<div class=\"form-group\" style=\"margin-left: 10%; margin-right: 10%;\">"
										+ "\n<label class=\"control-label col-xs-12 col-sm-3 no-padding-right\">"
										+ "\n " + field_name4);

						System.out.println("-------------mandatory for testing=------------------" + mandatory);

						if (mandatory == true) {
							System.out.println("-------------in loop 1-------------------");
							form.append("\n <i class=\"menu-icon fa red\"> *</i>");
						}

						form.append(
								"\n</label>" + "\n<div class=\"col-xs-12 col-sm-9\">" + "\n<div class=\"clearfix\">");
						form.append("\n<input");

						if (hidden == true) {
							form.append("  type=\"hidden\" ");
						} else {
							form.append("  type=\"text\" ");
						}
						if (calculated_field) {
							form.append("name=\"total\" id=\"total\" class=\"stunden form-control\" ");
						} else {
							form.append("name=\"" + mapping_lower + "\" id=\"" + mapping_lower
									+ "\" class=\"col-xs-12 col-sm-4\" pattern=\"[A-Za-z]{1,30}\"");
						}

						if (mandatory == true) {
							System.out.println("-------------in loop 1 part 2 required-------------------");
							form.append("required");
						}

						if (readonly == true) {
							System.out.println("-------------in loop 1 part 2 required-------------------");
							form.append("  readonly");
						}
						form.append("/>\n</div>" + "\n</div>" + "\n</div>" + "\n</td>");
					}

					if (type_field.equals("autocomplete")) {
						form.append(
								"\n<td>" + "\n<div class=\"form-group\" style=\"margin-left: 10%; margin-right: 10%;\">"
										+ "\n<label class=\"control-label col-xs-12 col-sm-3 no-padding-right\">"
										+ "\n " + field_name4);

						System.out.println("-------------mandatory for testing=------------------" + mandatory);

						//if (mandatory == true) {
						if (mandatory == true) {
							System.out.println("-------------in loop 1-------------------");
							form.append("\n <i class=\"menu-icon fa red\"> *</i>");
						}

						form.append(
								"\n</label>" + "\n<div class=\"col-xs-12 col-sm-9\">" + "\n<div class=\"clearfix\">");

						form.append("\n<input");

						if (hidden == true) {
							form.append("  type=\"hidden\" ");
						} else {
							form.append("  type=\"text\" ");
						}
						if (calculated_field == true) {
							form.append("name=\"total\" id=\"total\" class=\"stunden form-control\" ");
						} else {
							form.append("name=" + mapping_lower + " id=\"" + mapping_lower
									+ "\" class=\"col-xs-12 col-sm-4\" ");
						}

						/*
						 * if(mandatory.equals("Y")) { System.out.
						 * println("-------------in loop 1 part 2 required-------------------" );
						 * form.append("required"); }
						 */

						/*
						 * if(readonly.equals("Y")) { System.out.
						 * println("-------------in loop 1 part 2 required-------------------" );
						 * form.append("  readonly"); }
						 */
						form.append("/>\n</div>" + "\n</div>" + "\n</div>" + "\n</td>");

						autocomplete_script_latest
								.append("<script>" + "var options = {" + "url: '${pageContext.request.contextPath}/"
										+ mapping_lower + "_list'," + "getValue: \"drop_value\"," + "list: {"
										+ "match: {" + "enabled: true" + "}}," + "theme: \"square\"" + "};" + "$(\"#"
										+ mapping_lower + "\").easyAutocomplete(options);" + "</script>");

					}

					if (type_field.equals("dropdown")) {
						form.append(
								"\n<td>" + "\n<div class=\"form-group\" style=\"margin-left: 10%; margin-right: 10%;\">"
										+ "\n<label class=\"control-label col-xs-12 col-sm-3 no-padding-right\">"
										+ "\n " + field_name4);

						System.out.println("-------------mandatory for testing=------------------" + mandatory);

						//if (mandatory == true) {
						if (mandatory == true) {
							System.out.println("-------------in loop 1-------------------");
							form.append("\n <i class=\"menu-icon fa red\"> *</i>");
						}

						form.append(
								"\n</label>" + "\n<div class=\"col-xs-12 col-sm-9\">" + "\n<div class=\"clearfix\">");

						if (dependent == true) {

							form.append("\n<select name=\"" + mapping_lower + "\" id=\"" + mapping_lower
									+ "\"  class=\"col-xs-3 col-sm-3 form-control state\">"
									+ "\n<option >---select---</option>" + "\n</select>");
						} else {

							form.append("\n<select name=\"" + mapping_lower + "\" id=\"" + mapping_lower
									+ "\"  value=\"${drop_value}\" class=\"col-xs-3 col-sm-3 form-control");
							form.append("\t" + mapping4);
							form.append(
									"\" >" + "\n<option >---select---</option>" + "\n<option value=\"${drop_value}\"");

							if (mandatory == true) {
								System.out.println("-------------in loop 1 part 2 required-------------------");
								form.append("required");
							}

							if (readonly == true) {
								System.out.println("-------------in loop 1 part 2 required-------------------");
								form.append("readonly");
							}
							form.append(">${drop_value}</option>\n" + "</select>\n");
						}
						form.append("</div>" + "\n</div>" + "\n</div>" + "\n</td>");

						dropdown_script_latest.append("\n$.ajax({" + "\nurl: '${pageContext.request.contextPath}/"
								+ mapping_lower + "_list'," + "\nsuccess: function(data)" + "\n{"
								+ "\nconsole.log(data);" + "\nvar select = $('#" + mapping_lower + "');"
								+ "\n$('<option>').text('select').val(0).appendTo(select);"
								+ "\nselect.find('option').remove();" + "\n$.each(data, function(index, value) {"
								+ "\n$('<option>').text(value.drop_value).val(value.country_id).appendTo(select);"
								+ "\n});" + "\n}" + "\n});");

					}

					if (type_field.equals("togglebutton")) {
						form.append(
								"\n<td>" + "\n<div class=\"form-group\" style=\"margin-left: 10%; margin-right: 10%;\">"
										+ "\n<label class=\"control-label col-xs-12 col-sm-3 no-padding-right\">"
										+ "\n " + field_name4);

						System.out.println("-------------mandatory for testing=------------------" + mandatory);

						if (mandatory == true) {
							System.out.println("-------------in loop 1-------------------");
							form.append("\n <i class=\"menu-icon fa red\"> *</i>");
						}

						form.append(
								"\n</label>" + "\n<div class=\"col-xs-12 col-sm-9\">" + "\n<div class=\"clearfix\">");

						form.append("\n<input");

						if (hidden == true) {
							form.append("  type=\"hidden\" ");
						} else {
							form.append("  type=\"checkbox\" ");
						}

						form.append("name=" + mapping_lower + " id=" + mapping_lower
								+ "  class=\"ace ace-switch ace-switch-6\" onblur=\"CheckUserStatus()\"");

						if (mandatory == true) {
							System.out.println("-------------in loop 1 part 2 required-------------------");
							form.append("required");
						}

						form.append("/>\n" + "\n<span class=\"lbl\"></span>"

								+ "\n</div>" + "\n</div>" + "\n</div>" + "\n</td>");
					}

					if (type_field.equals("checkbox")) {
						form.append(
								"\n<td>" + "\n<div class=\"form-group\" style=\"margin-left: 10%; margin-right: 10%;\">"
										+ "\n<label class=\"control-label col-xs-12 col-sm-3 no-padding-right\">"
										+ "\n " + field_name4);

						System.out.println("-------------mandatory for testing=------------------" + mandatory);

						if (mandatory == true) {
							System.out.println("-------------in loop 1-------------------");
							form.append("\n <i class=\"menu-icon fa red\"> *</i>");
						}

						form.append(
								"\n</label>" + "\n<div class=\"col-xs-12 col-sm-9\">" + "\n<div class=\"clearfix\">");

						form.append("\n<input  style=\"width:10%; margin-left:20px\" class=\"form-control\"");

						if (hidden == true) {
							form.append("  type=\"hidden\" ");
						} else {
							form.append("  type=\"checkbox\" ");
						}

						form.append("name=" + mapping_lower + " id=" + mapping_lower
								+ "  onblur=\"CheckUserStatusHeader1()\"");

						if (mandatory == true) {
							System.out.println("-------------in loop 1 part 2 required-------------------");
							form.append("required");
						}

						form.append("/>\n" + "\n</div>" + "\n</div>" + "\n</div>" + "\n</td>");
					}

				}

			}
		}

		form.append("\n</tr>");

		form.append("\n<tr>");

		if (table_name != null && !table_name.isEmpty()) {
			String table_name_lower = table_name.toLowerCase();
			String table_name_first_upper = table_name_lower.substring(0, 1).toUpperCase()
					+ table_name_lower.substring(1);
			String table_name_upper = table_name.toUpperCase();
			for (int i = 0; i < rn_userlist.size(); i++) {
				String field_name4 = rn_userlist.get(i).getMapping();
				String mapping4 = rn_userlist.get(i).getMapping();
				String data_type4 = rn_userlist.get(i).getDataType();
				String lower_case = field_name4.toLowerCase();
				System.out.println("field name-------" + field_name4);
				System.out.println("mapping-------" + mapping4);
				System.out.println("data type-------" + data_type4);

				String mapping_lower = mapping4.toLowerCase();
				boolean mandatory = rn_userlist.get(i).isMandatory();
				boolean readonly = rn_userlist.get(i).isReadonly();
				boolean hidden = rn_userlist.get(i).isHidden();

				// String mapping=rn_userlist.get(i).getMapping();

				String type_field = rn_userlist.get(i).getType_field();

				boolean dependent = rn_userlist.get(i).isDependent();
				String dependent_on = rn_userlist.get(i).getDependent_on();
				String dependent_sp = rn_userlist.get(i).getDependent_sp();
				String dependent_sp_param = rn_userlist.get(i).getDependent_sp_param();

				boolean sequence = rn_userlist.get(i).isSequence();
				String seq_name = rn_userlist.get(i).getSeq_name();
				String seq_sp = rn_userlist.get(i).getSeq_sp();
				String seq_sp_param = rn_userlist.get(i).getSeq_sp_param();

				boolean validation_1 = rn_userlist.get(i).isValidation_1();
				String val_type = rn_userlist.get(i).getVal_type();
				String val_sp = rn_userlist.get(i).getVal_sp();
				String val_sp_param = rn_userlist.get(i).getVal_sp_param();

				boolean default_1 = rn_userlist.get(i).isDefault_1();
				String default_typename = rn_userlist.get(i).getDefault_type();
				String default_value = rn_userlist.get(i).getDefault_value();
				String default_sp = rn_userlist.get(i).getDefault_sp();
				String default_sp_param = rn_userlist.get(i).getDefault_sp_param();

				boolean calculated_field = rn_userlist.get(i).isCalculated_field();
				String cal_sp = rn_userlist.get(i).getCal_sp();
				String cal_sp_param = rn_userlist.get(i).getCal_sp_param();

				boolean add_to_grid = rn_userlist.get(i).getAdd_to_grid();

				boolean sp_for_dropdown = rn_userlist.get(i).getSp_for_dropdown();
				boolean sp_for_autocomplete = rn_userlist.get(i).getSp_for_autocomplete();

				String sp_name_for_dropdown = rn_userlist.get(i).getSp_name_for_dropdown();
				String sp_name_for_autocomplete = rn_userlist.get(i).getSp_name_for_autocomplete();

				if (i > 2 && i <= 5) {

					if (type_field.equals("textfield")) {
						form.append(
								"\n<td>" + "\n<div class=\"form-group\" style=\"margin-left: 10%; margin-right: 10%;\">"
										+ "\n<label class=\"control-label col-xs-12 col-sm-3 no-padding-right\">"
										+ "\n " + field_name4);

						System.out.println("-------------mandatory for testing=------------------" + mandatory);

						if (mandatory == true) {
							System.out.println("-------------in loop 1-------------------");
							form.append("\n <i class=\"menu-icon fa red\"> *</i>");
						}

						form.append(
								"\n</label>" + "\n<div class=\"col-xs-12 col-sm-9\">" + "\n<div class=\"clearfix\">");
						form.append("\n<input");

						if (hidden == true) {
							form.append("  type=\"hidden\" ");
						} else {
							form.append("  type=\"text\" ");
						}
						if (calculated_field == true) {
							form.append("name=\"total\" id=\"total\" class=\"stunden form-control\" ");
						} else {
							form.append("name=\"" + mapping_lower + "\" id=\"" + mapping_lower
									+ "\" class=\"col-xs-12 col-sm-4\" pattern=\"[A-Za-z]{1,30}\"");
						}

						if (mandatory == true) {
							System.out.println("-------------in loop 1 part 2 required-------------------");
							form.append("required");
						}

						if (readonly == true) {
							System.out.println("-------------in loop 1 part 2 required-------------------");
							form.append("  readonly");
						}
						form.append("/>\n</div>" + "\n</div>" + "\n</div>" + "\n</td>");
					}

					if (type_field.equals("autocomplete")) {
						form.append(
								"\n<td>" + "\n<div class=\"form-group\" style=\"margin-left: 10%; margin-right: 10%;\">"
										+ "\n<label class=\"control-label col-xs-12 col-sm-3 no-padding-right\">"
										+ "\n " + field_name4);

						System.out.println("-------------mandatory for testing=------------------" + mandatory);

						if (mandatory == true) {
							System.out.println("-------------in loop 1-------------------");
							form.append("\n <i class=\"menu-icon fa red\"> *</i>");
						}

						form.append(
								"\n</label>" + "\n<div class=\"col-xs-12 col-sm-9\">" + "\n<div class=\"clearfix\">");

						form.append("\n<input");

						if (hidden == true) {
							form.append("  type=\"hidden\" ");
						} else {
							form.append("  type=\"text\" ");
						}
						if (calculated_field == true) {
							form.append("name=\"total\" id=\"total\" class=\"stunden form-control\" ");
						} else {
							form.append("name=" + mapping_lower + " id=\"" + mapping_lower
									+ "\" class=\"col-xs-12 col-sm-4\" ");
						}

						/*
						 * if(mandatory.equals("Y")) { System.out.
						 * println("-------------in loop 1 part 2 required-------------------" );
						 * form.append("required"); }
						 */

						/*
						 * if(readonly.equals("Y")) { System.out.
						 * println("-------------in loop 1 part 2 required-------------------" );
						 * form.append("  readonly"); }
						 */
						form.append("/>\n</div>" + "\n</div>" + "\n</div>" + "\n</td>");

						autocomplete_script_latest
								.append("<script>" + "var options = {" + "url: '${pageContext.request.contextPath}/"
										+ mapping_lower + "_list'," + "getValue: \"drop_value\"," + "list: {"
										+ "match: {" + "enabled: true" + "}}," + "theme: \"square\"" + "};" + "$(\"#"
										+ mapping_lower + "\").easyAutocomplete(options);" + "</script>");

					}

					if (type_field.equals("dropdown")) {
						form.append(
								"\n<td>" + "\n<div class=\"form-group\" style=\"margin-left: 10%; margin-right: 10%;\">"
										+ "\n<label class=\"control-label col-xs-12 col-sm-3 no-padding-right\">"
										+ "\n " + field_name4);

						System.out.println("-------------mandatory for testing=------------------" + mandatory);

						if (mandatory == true) {
							System.out.println("-------------in loop 1-------------------");
							form.append("\n <i class=\"menu-icon fa red\"> *</i>");
						}

						form.append(
								"\n</label>" + "\n<div class=\"col-xs-12 col-sm-9\">" + "\n<div class=\"clearfix\">");

						if (dependent == true) {

							form.append("\n<select name=\"state_name\" id=\"" + mapping_lower
									+ "\"  class=\"col-xs-3 col-sm-3 form-control state\">"
									+ "\n<option >---select---</option>" + "\n</select>");
						} else {

							form.append("\n<select name=\"" + mapping_lower + "\" id=\"" + mapping_lower
									+ "\"  value=\"${drop_value}\" class=\"col-xs-3 col-sm-3 form-control");
							form.append("\t");
							form.append(
									"\" >" + "\n<option >---select---</option>" + "\n<option value=\"${drop_value}\"");

							if (mandatory == true) {
								System.out.println("-------------in loop 1 part 2 required-------------------");
								form.append("required");
							}

							if (readonly == true) {
								System.out.println("-------------in loop 1 part 2 required-------------------");
								form.append("readonly");
							}
							form.append(">${drop_value}</option>\n" + "</select>\n");
						}
						form.append("</div>" + "\n</div>" + "\n</div>" + "\n</td>");

						dropdown_script_latest.append("\n$.ajax({" + "\nurl: '${pageContext.request.contextPath}/"
								+ mapping_lower + "_list'," + "\nsuccess: function(data)" + "\n{"
								+ "\nconsole.log(data);" + "\nvar select = $('#" + mapping_lower + "');"
								+ "\n$('<option>').text('select').val(0).appendTo(select);"
								+ "\nselect.find('option').remove();" + "\n$.each(data, function(index, value) {"
								+ "\n$('<option>').text(value.drop_value).val(value.country_id).appendTo(select);"
								+ "\n});" + "\n}" + "\n});");

					}

					if (type_field.equals("togglebutton")) {
						form.append(
								"\n<td>" + "\n<div class=\"form-group\" style=\"margin-left: 10%; margin-right: 10%;\">"
										+ "\n<label class=\"control-label col-xs-12 col-sm-3 no-padding-right\">"
										+ "\n " + field_name4);

						System.out.println("-------------mandatory for testing=------------------" + mandatory);

						if (mandatory == true) {
							System.out.println("-------------in loop 1-------------------");
							form.append("\n <i class=\"menu-icon fa red\"> *</i>");
						}

						form.append(
								"\n</label>" + "\n<div class=\"col-xs-12 col-sm-9\">" + "\n<div class=\"clearfix\">");

						form.append("\n<input");

						if (hidden == true) {
							form.append("  type=\"hidden\" ");
						} else {
							form.append("  type=\"checkbox\" ");
						}

						form.append("name=" + mapping_lower + " id=" + mapping_lower
								+ "  class=\"ace ace-switch ace-switch-6\" onblur=\"CheckUserStatus()\"");

						if (mandatory == true) {
							System.out.println("-------------in loop 1 part 2 required-------------------");
							form.append("required");
						}

						form.append("/>\n" + "\n<span class=\"lbl\"></span>"

								+ "\n</div>" + "\n</div>" + "\n</div>" + "\n</td>");
					}

					if (type_field.equals("checkbox")) {
						form.append(
								"\n<td>" + "\n<div class=\"form-group\" style=\"margin-left: 10%; margin-right: 10%;\">"
										+ "\n<label class=\"control-label col-xs-12 col-sm-3 no-padding-right\">"
										+ "\n " + field_name4);

						System.out.println("-------------mandatory for testing=------------------" + mandatory);

						if (mandatory == true) {
							System.out.println("-------------in loop 1-------------------");
							form.append("\n <i class=\"menu-icon fa red\"> *</i>");
						}

						form.append(
								"\n</label>" + "\n<div class=\"col-xs-12 col-sm-9\">" + "\n<div class=\"clearfix\">");

						form.append("\n<input  style=\"width:10%; margin-left:20px\" class=\"form-control\"");

						if (hidden == true) {
							form.append("  type=\"hidden\" ");
						} else {
							form.append("  type=\"checkbox\" ");
						}

						form.append("name=" + mapping_lower + " id=" + mapping_lower
								+ "  onblur=\"CheckUserStatusHeader1()\"");

						if (mandatory == true) {
							System.out.println("-------------in loop 1 part 2 required-------------------");
							form.append("required");
						}

						form.append("/>\n" + "\n</div>" + "\n</div>" + "\n</div>" + "\n</td>");
					}

				}
			}
		}

		form.append("\n</tr>");

		form.append("\n<tr>");

		if (table_name != null && !table_name.isEmpty()) {
			String table_name_lower = table_name.toLowerCase();
			String table_name_first_upper = table_name_lower.substring(0, 1).toUpperCase()
					+ table_name_lower.substring(1);
			String table_name_upper = table_name.toUpperCase();

			for (int i = 0; i < rn_userlist.size(); i++) {
				String field_name4 = rn_userlist.get(i).getMapping();
				String mapping4 = rn_userlist.get(i).getMapping();
				String data_type4 = rn_userlist.get(i).getDataType();
				String lower_case = field_name4.toLowerCase();
				System.out.println("field name-------" + field_name4);
				System.out.println("mapping-------" + mapping4);
				System.out.println("data type-------" + data_type4);
				String mapping_lower = mapping4.toLowerCase();

				boolean mandatory = rn_userlist.get(i).isMandatory();
				boolean readonly = rn_userlist.get(i).isReadonly();
				boolean hidden = rn_userlist.get(i).isHidden();

				// String mapping=rn_userlist.get(i).getMapping();

				String type_field = rn_userlist.get(i).getType_field();

				boolean dependent = rn_userlist.get(i).isDependent();
				String dependent_on = rn_userlist.get(i).getDependent_on();
				String dependent_sp = rn_userlist.get(i).getDependent_sp();
				String dependent_sp_param = rn_userlist.get(i).getDependent_sp_param();

				boolean sequence = rn_userlist.get(i).isSequence();
				String seq_name = rn_userlist.get(i).getSeq_name();
				String seq_sp = rn_userlist.get(i).getSeq_sp();
				String seq_sp_param = rn_userlist.get(i).getSeq_sp_param();

				boolean validation_1 = rn_userlist.get(i).isValidation_1();
				String val_type = rn_userlist.get(i).getVal_type();
				String val_sp = rn_userlist.get(i).getVal_sp();
				String val_sp_param = rn_userlist.get(i).getVal_sp_param();

				boolean default_1 = rn_userlist.get(i).isDefault_1();
				String default_typename = rn_userlist.get(i).getDefault_type();
				String default_value = rn_userlist.get(i).getDefault_value();
				String default_sp = rn_userlist.get(i).getDefault_sp();
				String default_sp_param = rn_userlist.get(i).getDefault_sp_param();

				boolean calculated_field = rn_userlist.get(i).isCalculated_field();
				String cal_sp = rn_userlist.get(i).getCal_sp();
				String cal_sp_param = rn_userlist.get(i).getCal_sp_param();

				boolean add_to_grid = rn_userlist.get(i).getAdd_to_grid();

				boolean sp_for_dropdown = rn_userlist.get(i).getSp_for_dropdown();
				boolean sp_for_autocomplete = rn_userlist.get(i).getSp_for_autocomplete();

				String sp_name_for_dropdown = rn_userlist.get(i).getSp_name_for_dropdown();
				String sp_name_for_autocomplete = rn_userlist.get(i).getSp_name_for_autocomplete();

				if (i > 5 && i <= 8) {
					if (type_field.equals("textfield")) {
						form.append(
								"\n<td>" + "\n<div class=\"form-group\" style=\"margin-left: 10%; margin-right: 10%;\">"
										+ "\n<label class=\"control-label col-xs-12 col-sm-3 no-padding-right\">"
										+ "\n " + field_name4);

						System.out.println("-------------mandatory for testing=------------------" + mandatory);

						if (mandatory == true) {
							System.out.println("-------------in loop 1-------------------");
							form.append("\n <i class=\"menu-icon fa red\"> *</i>");
						}

						form.append(
								"\n</label>" + "\n<div class=\"col-xs-12 col-sm-9\">" + "\n<div class=\"clearfix\">");
						form.append("\n<input");

						if (hidden == true) {
							form.append("  type=\"hidden\" ");
						} else {
							form.append("  type=\"text\" ");
						}
						if (calculated_field == true) {
							form.append("name=\"total\" id=\"total\" class=\"stunden form-control\" ");
						} else {
							form.append("name=\"" + mapping_lower + "\" id=\"" + mapping_lower
									+ "\" class=\"col-xs-12 col-sm-4\" pattern=\"[A-Za-z]{1,30}\"");
						}

						if (mandatory == true) {
							System.out.println("-------------in loop 1 part 2 required-------------------");
							form.append("required");
						}

						if (readonly == true) {
							System.out.println("-------------in loop 1 part 2 required-------------------");
							form.append("  readonly");
						}
						form.append("/>\n</div>" + "\n</div>" + "\n</div>" + "\n</td>");
					}

					if (type_field.equals("autocomplete")) {
						form.append(
								"\n<td>" + "\n<div class=\"form-group\" style=\"margin-left: 10%; margin-right: 10%;\">"
										+ "\n<label class=\"control-label col-xs-12 col-sm-3 no-padding-right\">"
										+ "\n " + field_name4);

						System.out.println("-------------mandatory for testing=------------------" + mandatory);

						if (mandatory == true) {
							System.out.println("-------------in loop 1-------------------");
							form.append("\n <i class=\"menu-icon fa red\"> *</i>");
						}

						form.append(
								"\n</label>" + "\n<div class=\"col-xs-12 col-sm-9\">" + "\n<div class=\"clearfix\">");

						form.append("\n<input");

						if (hidden == true) {
							form.append("  type=\"hidden\" ");
						} else {
							form.append("  type=\"text\" ");
						}
						if (calculated_field == true) {
							form.append("name=\"total\" id=\"total\" class=\"stunden form-control\" ");
						} else {
							form.append("name=" + mapping_lower + " id=\"" + mapping_lower
									+ "\" class=\"col-xs-12 col-sm-4\" ");
						}

						/*
						 * if(mandatory.equals("Y")) { System.out.
						 * println("-------------in loop 1 part 2 required-------------------" );
						 * form.append("required"); }
						 */

						/*
						 * if(readonly.equals("Y")) { System.out.
						 * println("-------------in loop 1 part 2 required-------------------" );
						 * form.append("  readonly"); }
						 */
						form.append("/>\n</div>" + "\n</div>" + "\n</div>" + "\n</td>");

						autocomplete_script_latest
								.append("<script>" + "var options = {" + "url: '${pageContext.request.contextPath}/"
										+ mapping_lower + "_list'," + "getValue: \"drop_value\"," + "list: {"
										+ "match: {" + "enabled: true" + "}}," + "theme: \"square\"" + "};" + "$(\"#"
										+ mapping_lower + "\").easyAutocomplete(options);" + "</script>");

					}

					if (type_field.equals("dropdown")) {
						form.append(
								"\n<td>" + "\n<div class=\"form-group\" style=\"margin-left: 10%; margin-right: 10%;\">"
										+ "\n<label class=\"control-label col-xs-12 col-sm-3 no-padding-right\">"
										+ "\n " + field_name4);

						System.out.println("-------------mandatory for testing=------------------" + mandatory);

						if (mandatory == true) {
							System.out.println("-------------in loop 1-------------------");
							form.append("\n <i class=\"menu-icon fa red\"> *</i>");
						}

						form.append(
								"\n</label>" + "\n<div class=\"col-xs-12 col-sm-9\">" + "\n<div class=\"clearfix\">");

						if (dependent == true) {

							form.append("\n<select name=\"state_name\" id=\"" + mapping_lower
									+ "\"  class=\"col-xs-3 col-sm-3 form-control state\">"
									+ "\n<option >---select---</option>" + "\n</select>");
						} else {

							form.append("\n<select name=\"" + mapping_lower + "\" id=\"" + mapping_lower
									+ "\"  value=\"${drop_value}\" class=\"col-xs-3 col-sm-3 form-control");
							form.append("\t");
							form.append(
									"\" >" + "\n<option >---select---</option>" + "\n<option value=\"${drop_value}\"");

							if (mandatory == true) {
								System.out.println("-------------in loop 1 part 2 required-------------------");
								form.append("required");
							}

							if (readonly == true) {
								System.out.println("-------------in loop 1 part 2 required-------------------");
								form.append("readonly");
							}
							form.append(">${drop_value}</option>\n" + "</select>\n");
						}
						form.append("</div>" + "\n</div>" + "\n</div>" + "\n</td>");

						dropdown_script_latest.append("\n$.ajax({" + "\nurl: '${pageContext.request.contextPath}/"
								+ mapping_lower + "_list'," + "\nsuccess: function(data)" + "\n{"
								+ "\nconsole.log(data);" + "\nvar select = $('#" + mapping_lower + "');"
								+ "\n$('<option>').text('select').val(0).appendTo(select);"
								+ "\nselect.find('option').remove();" + "\n$.each(data, function(index, value) {"
								+ "\n$('<option>').text(value.drop_value).val(value.country_id).appendTo(select);"
								+ "\n});" + "\n}" + "\n});");

					}

					if (type_field.equals("togglebutton")) {
						form.append(
								"\n<td>" + "\n<div class=\"form-group\" style=\"margin-left: 10%; margin-right: 10%;\">"
										+ "\n<label class=\"control-label col-xs-12 col-sm-3 no-padding-right\">"
										+ "\n " + field_name4);

						System.out.println("-------------mandatory for testing=------------------" + mandatory);

						if (mandatory == true) {
							System.out.println("-------------in loop 1-------------------");
							form.append("\n <i class=\"menu-icon fa red\"> *</i>");
						}

						form.append(
								"\n</label>" + "\n<div class=\"col-xs-12 col-sm-9\">" + "\n<div class=\"clearfix\">");

						form.append("\n<input");

						if (hidden == true) {
							form.append("  type=\"hidden\" ");
						} else {
							form.append("  type=\"checkbox\" ");
						}

						form.append("name=" + mapping_lower + " id=" + mapping_lower
								+ "  class=\"ace ace-switch ace-switch-6\" onblur=\"CheckUserStatus()\"");

						if (mandatory == true) {
							System.out.println("-------------in loop 1 part 2 required-------------------");
							form.append("required");
						}

						form.append("/>\n" + "\n<span class=\"lbl\"></span>"

								+ "\n</div>" + "\n</div>" + "\n</div>" + "\n</td>");
					}

					if (type_field.equals("checkbox")) {
						form.append(
								"\n<td>" + "\n<div class=\"form-group\" style=\"margin-left: 10%; margin-right: 10%;\">"
										+ "\n<label class=\"control-label col-xs-12 col-sm-3 no-padding-right\">"
										+ "\n " + field_name4);

						System.out.println("-------------mandatory for testing=------------------" + mandatory);

						if (mandatory == true) {
							System.out.println("-------------in loop 1-------------------");
							form.append("\n <i class=\"menu-icon fa red\"> *</i>");
						}

						form.append(
								"\n</label>" + "\n<div class=\"col-xs-12 col-sm-9\">" + "\n<div class=\"clearfix\">");

						form.append("\n<input  style=\"width:10%; margin-left:20px\" class=\"form-control\"");

						if (hidden == true) {
							form.append("  type=\"hidden\" ");
						} else {
							form.append("  type=\"checkbox\" ");
						}

						form.append("name=" + mapping_lower + " id=" + mapping_lower
								+ "  onblur=\"CheckUserStatusHeader1()\"");

						if (mandatory == true) {
							System.out.println("-------------in loop 1 part 2 required-------------------");
							form.append("required");
						}

						form.append("/>\n" + "\n</div>" + "\n</div>" + "\n</div>" + "\n</td>");
					}

				}
			}
		}
		form.append("\n</tr>");

		form.append("\n<tr>");

		if (table_name != null && !table_name.isEmpty()) {
			String table_name_lower = table_name.toLowerCase();
			String table_name_first_upper = table_name_lower.substring(0, 1).toUpperCase()
					+ table_name_lower.substring(1);
			String table_name_upper = table_name.toUpperCase();
			for (int i = 0; i < rn_userlist.size(); i++) {
				String field_name4 = rn_userlist.get(i).getMapping();
				String mapping4 = rn_userlist.get(i).getMapping();
				String data_type4 = rn_userlist.get(i).getDataType();
				String lower_case = field_name4.toLowerCase();
				System.out.println("field name-------" + field_name4);
				System.out.println("mapping-------" + mapping4);
				System.out.println("data type-------" + data_type4);

				String mapping_lower = mapping4.toLowerCase();
				boolean mandatory = rn_userlist.get(i).isMandatory();
				boolean readonly = rn_userlist.get(i).isReadonly();
				boolean hidden = rn_userlist.get(i).isHidden();

				// String mapping=rn_userlist.get(i).getMapping();

				String type_field = rn_userlist.get(i).getType_field();

				boolean dependent = rn_userlist.get(i).isDependent();
				String dependent_on = rn_userlist.get(i).getDependent_on();
				String dependent_sp = rn_userlist.get(i).getDependent_sp();
				String dependent_sp_param = rn_userlist.get(i).getDependent_sp_param();

				boolean sequence = rn_userlist.get(i).isSequence();
				String seq_name = rn_userlist.get(i).getSeq_name();
				String seq_sp = rn_userlist.get(i).getSeq_sp();
				String seq_sp_param = rn_userlist.get(i).getSeq_sp_param();

				boolean validation_1 = rn_userlist.get(i).isValidation_1();
				String val_type = rn_userlist.get(i).getVal_type();
				String val_sp = rn_userlist.get(i).getVal_sp();
				String val_sp_param = rn_userlist.get(i).getVal_sp_param();

				boolean default_1 = rn_userlist.get(i).isDefault_1();
				String default_typename = rn_userlist.get(i).getDefault_type();
				String default_value = rn_userlist.get(i).getDefault_value();
				String default_sp = rn_userlist.get(i).getDefault_sp();
				String default_sp_param = rn_userlist.get(i).getDefault_sp_param();

				boolean calculated_field = rn_userlist.get(i).isCalculated_field();
				String cal_sp = rn_userlist.get(i).getCal_sp();
				String cal_sp_param = rn_userlist.get(i).getCal_sp_param();

				boolean add_to_grid = rn_userlist.get(i).getAdd_to_grid();

				boolean sp_for_dropdown = rn_userlist.get(i).getSp_for_dropdown();
				boolean sp_for_autocomplete = rn_userlist.get(i).getSp_for_autocomplete();

				String sp_name_for_dropdown = rn_userlist.get(i).getSp_name_for_dropdown();
				String sp_name_for_autocomplete = rn_userlist.get(i).getSp_name_for_autocomplete();

				if (i > 8 && i <= 11) {
					if (type_field.equals("textfield")) {
						form.append(
								"\n<td>" + "\n<div class=\"form-group\" style=\"margin-left: 10%; margin-right: 10%;\">"
										+ "\n<label class=\"control-label col-xs-12 col-sm-3 no-padding-right\">"
										+ "\n " + field_name4);

						System.out.println("-------------mandatory for testing=------------------" + mandatory);

						if (mandatory == true) {
							System.out.println("-------------in loop 1-------------------");
							form.append("\n <i class=\"menu-icon fa red\"> *</i>");
						}

						form.append(
								"\n</label>" + "\n<div class=\"col-xs-12 col-sm-9\">" + "\n<div class=\"clearfix\">");
						form.append("\n<input");

						if (hidden == true) {
							form.append("  type=\"hidden\" ");
						} else {
							form.append("  type=\"text\" ");
						}
						if (calculated_field == true) {
							form.append("name=\"total\" id=\"total\" class=\"stunden form-control\" ");
						} else {
							form.append("name=\"" + mapping_lower + "\" id=\"" + mapping_lower
									+ "\" class=\"col-xs-12 col-sm-4\" pattern=\"[A-Za-z]{1,30}\"");
						}

						if (mandatory == true) {
							System.out.println("-------------in loop 1 part 2 required-------------------");
							form.append("required");
						}

						if (readonly == true) {
							System.out.println("-------------in loop 1 part 2 required-------------------");
							form.append("  readonly");
						}
						form.append("/>\n</div>" + "\n</div>" + "\n</div>" + "\n</td>");
					}

					if (type_field.equals("autocomplete")) {
						form.append(
								"\n<td>" + "\n<div class=\"form-group\" style=\"margin-left: 10%; margin-right: 10%;\">"
										+ "\n<label class=\"control-label col-xs-12 col-sm-3 no-padding-right\">"
										+ "\n " + field_name4);

						System.out.println("-------------mandatory for testing=------------------" + mandatory);

						if (mandatory == true) {
							System.out.println("-------------in loop 1-------------------");
							form.append("\n <i class=\"menu-icon fa red\"> *</i>");
						}

						form.append(
								"\n</label>" + "\n<div class=\"col-xs-12 col-sm-9\">" + "\n<div class=\"clearfix\">");

						form.append("\n<input");

						if (hidden == true) {
							form.append("  type=\"hidden\" ");
						} else {
							form.append("  type=\"text\" ");
						}
						if (calculated_field == true) {
							form.append("name=\"total\" id=\"total\" class=\"stunden form-control\" ");
						} else {
							form.append("name=" + mapping_lower + " id=\"" + mapping_lower
									+ "\" class=\"col-xs-12 col-sm-4\" ");
						}

						/*
						 * if(mandatory.equals("Y")) { System.out.
						 * println("-------------in loop 1 part 2 required-------------------" );
						 * form.append("required"); }
						 */

						/*
						 * if(readonly.equals("Y")) { System.out.
						 * println("-------------in loop 1 part 2 required-------------------" );
						 * form.append("  readonly"); }
						 */
						form.append("/>\n</div>" + "\n</div>" + "\n</div>" + "\n</td>");
						autocomplete_script_latest
								.append("<script>" + "var options = {" + "url: '${pageContext.request.contextPath}/"
										+ mapping_lower + "_list'," + "getValue: \"drop_value\"," + "list: {"
										+ "match: {" + "enabled: true" + "}}," + "theme: \"square\"" + "};" + "$(\"#"
										+ mapping_lower + "\").easyAutocomplete(options);" + "</script>");

					}

					if (type_field.equals("dropdown")) {
						form.append(
								"\n<td>" + "\n<div class=\"form-group\" style=\"margin-left: 10%; margin-right: 10%;\">"
										+ "\n<label class=\"control-label col-xs-12 col-sm-3 no-padding-right\">"
										+ "\n " + field_name4);

						System.out.println("-------------mandatory for testing=------------------" + mandatory);

						if (mandatory == true) {
							System.out.println("-------------in loop 1-------------------");
							form.append("\n <i class=\"menu-icon fa red\"> *</i>");
						}

						form.append(
								"\n</label>" + "\n<div class=\"col-xs-12 col-sm-9\">" + "\n<div class=\"clearfix\">");

						if (dependent == true) {

							form.append("\n<select name=\"" + mapping_lower + "\" id=\"" + mapping_lower
									+ "\"  class=\"col-xs-3 col-sm-3 form-control state\">"
									+ "\n<option >---select---</option>" + "\n</select>");
						} else {

							form.append("\n<select name=\"" + mapping_lower + "\" id=\"" + mapping_lower
									+ "\"  value=\"${drop_value}\" class=\"col-xs-3 col-sm-3 form-control");
							form.append("\t");
							form.append(
									"\" >" + "\n<option >---select---</option>" + "\n<option value=\"${drop_value}\"");

							if (mandatory == true) {
								System.out.println("-------------in loop 1 part 2 required-------------------");
								form.append("required");
							}

							if (readonly == true) {
								System.out.println("-------------in loop 1 part 2 required-------------------");
								form.append("readonly");
							}
							form.append(">${drop_value}</option>\n" + "</select>\n");
						}
						form.append("</div>" + "\n</div>" + "\n</div>" + "\n</td>");

						dropdown_script_latest.append("\n$.ajax({" + "\nurl: '${pageContext.request.contextPath}/"
								+ mapping_lower + "_list'," + "\nsuccess: function(data)" + "\n{"
								+ "\nconsole.log(data);" + "\nvar select = $('#" + mapping_lower + "');"
								+ "\n$('<option>').text('select').val(0).appendTo(select);"
								+ "\nselect.find('option').remove();" + "\n$.each(data, function(index, value) {"
								+ "\n$('<option>').text(value.drop_value).val(value.country_id).appendTo(select);"
								+ "\n});" + "\n}" + "\n});");

					}

					if (type_field.equals("togglebutton")) {
						form.append(
								"\n<td>" + "\n<div class=\"form-group\" style=\"margin-left: 10%; margin-right: 10%;\">"
										+ "\n<label class=\"control-label col-xs-12 col-sm-3 no-padding-right\">"
										+ "\n " + field_name4);

						System.out.println("-------------mandatory for testing=------------------" + mandatory);

						if (mandatory == true) {
							System.out.println("-------------in loop 1-------------------");
							form.append("\n <i class=\"menu-icon fa red\"> *</i>");
						}

						form.append(
								"\n</label>" + "\n<div class=\"col-xs-12 col-sm-9\">" + "\n<div class=\"clearfix\">");

						form.append("\n<input");

						if (hidden == true) {
							form.append("  type=\"hidden\" ");
						} else {
							form.append("  type=\"checkbox\" ");
						}

						form.append("name=" + mapping_lower + " id=" + mapping_lower
								+ "  class=\"ace ace-switch ace-switch-6\" onblur=\"CheckUserStatus()\"");

						if (mandatory == true) {
							System.out.println("-------------in loop 1 part 2 required-------------------");
							form.append("required");
						}

						form.append("/>\n" + "\n<span class=\"lbl\"></span>"

								+ "\n</div>" + "\n</div>" + "\n</div>" + "\n</td>");
					}

					if (type_field.equals("checkbox")) {
						form.append(
								"\n<td>" + "\n<div class=\"form-group\" style=\"margin-left: 10%; margin-right: 10%;\">"
										+ "\n<label class=\"control-label col-xs-12 col-sm-3 no-padding-right\">"
										+ "\n " + field_name4);

						System.out.println("-------------mandatory for testing=------------------" + mandatory);

						if (mandatory == true) {
							System.out.println("-------------in loop 1-------------------");
							form.append("\n <i class=\"menu-icon fa red\"> *</i>");
						}

						form.append(
								"\n</label>" + "\n<div class=\"col-xs-12 col-sm-9\">" + "\n<div class=\"clearfix\">");

						form.append("\n<input  style=\"width:10%; margin-left:20px\" class=\"form-control\"");

						if (hidden == true) {
							form.append("  type=\"hidden\" ");
						} else {
							form.append("  type=\"checkbox\" ");
						}

						form.append("name=" + mapping_lower + " id=" + mapping_lower
								+ "  onblur=\"CheckUserStatusHeader1()\"");

						if (mandatory == true) {
							System.out.println("-------------in loop 1 part 2 required-------------------");
							form.append("required");
						}

						form.append("/>\n" + "\n</div>" + "\n</div>" + "\n</div>" + "\n</td>");
					}

				}
			}

			form.append("\n</tr>");

			if (multiple_line_name != null && !multiple_line_name.isEmpty()) {
				String[] single_line_name = multiple_line_name.split(",");

				button.append(
						"\n<button type=\"submit\" class=\"btn btn-success btn-sm btn-next\" onclick=\"doSubmit()\">"
								+ "\nSave and Next" + "\n<i class=\"ace-icon fa fa-arrow-right icon-on-right\"></i>"
								+ "\n</button>");
			} else {
				button.append("\n<button type=\"submit\" class=\"btn btn-success center\" onclick=\"submitForms()\">"
						+ "\nCREATE" + "\n</button>");
			}
			form.append("\n<%@include file=\"/WEB-INF/tiles/acemaster/" + module_name + "/" + table_name_lower
					+ "_extension.jsp\"%>\n</table>");
			form.append("\n"
//					+ "<div class=\"hr hr-dotted\"></div>" 
					+ "\n<div class=\"wizard-actions\">"

					+ button + "\n</div> " + "\n</form>");
		}

		// -----------------------------coding for line
		// part----------------------------------------------------------------------------

		for_line_part.append("\n<div class=\"table-header\" style=\"margin-top: 30px;\">Group Lines Details</div>"
				+ "\n<div>" + "\n<% int n=0; %>" + "\n<input type=\"hidden\" id=\"rowcount1\" name=\"rowcount1\">"
				+ "\n<input type=\"hidden\" id=\"delrow1\" name=\"delrow1\" value=\"\">"
				+ "\n<table id=\"dynamic-table1\" class=\"table table-striped table-bordered table-hover\">"
				+ "\n<thead>" + "\n<tr>");

		// for loop for id is primary

		for (int i = 0; i < line_id_primary.size(); i++) {
			String field_name4 = line_id_primary.get(i).getMapping();
			String mapping4 = line_id_primary.get(i).getMapping();
			String data_type4 = line_id_primary.get(i).getDataType();

			for_line_part.append("\n<th>" + "\n<center>Select</center>" + "\n</th>");
		}

		// for loop for other field (like varchar field) this is only heading

		for (int i = 0; i < line_varchar.size(); i++) {
			String field_name4 = line_varchar.get(i).getMapping();
			String mapping4 = line_varchar.get(i).getMapping();
			String data_type4 = line_varchar.get(i).getDataType();

			for_line_part.append("\n<th>" + "\n<center>" + field_name4 + "</center>" + "\n</th>");
		}

		for_line_part.append("\n</thead>" + "\n<tbody>"
		// upate code

				+ "\n</tbody>" + "\n</table>"

				+ "\n<input type=\"button\" class=\"btn btn-md btn-success\""
				+ "\nstyle=\"background-color: #87b87f; border-color: #87b87f; margin-top: 1%; margin-bottom: 1%;\""
				+ "\nvalue=\"Add Row\" onclick=\"AddRow()\">"

				+ "\n<input type=\"button\" class=\"btn btn-md btn-success\""
				+ "\nstyle=\"background-color: #87b87f; border-color: #87b87f; margin-top: 1%; margin-bottom: 1%; font-size: auto;\""
				+ "\nvalue=\"Delete Row\" onclick=\"del()\">" + "\n</div>");

//--------------------------------------update part-------------------------------------------------//

		if (table_name != null && !table_name.isEmpty()) {
			String table_name_lower = table_name.toLowerCase();
			String table_name_first_upper = table_name_lower.substring(0, 1).toUpperCase()
					+ table_name_lower.substring(1);
			String table_name_upper = table_name.toUpperCase();

			for (int i = 0; i < id_list.size(); i++) {
				String field_name_for_id = id_list.get(0).getMapping();
				System.out.println("field" + field_name_for_id);
				String field_name_for_id_lower = field_name_for_id.toLowerCase();

				popup_id.append("\n<input   value=\"${" + table_name_lower + "_updt." + field_name_for_id_lower
						+ "}\"type=\"hidden\" name=\"" + field_name_for_id_lower + "\"id=\"" + field_name_for_id_lower
						+ "\"class=\"col-xs-12 col-sm-4\" />");
			}
		}

		if (table_name != null && !table_name.isEmpty()) {
			String table_name_lower = table_name.toLowerCase();
			String table_name_first_upper = table_name_lower.substring(0, 1).toUpperCase()
					+ table_name_lower.substring(1);
			String table_name_upper = table_name.toUpperCase();

			popup_var.append("\n\n\n\n<div class=\"modal-body mx-3\">" + "\n<div class=\"md-form mb-5\">"
					+ "\n<label data-error=\"wrong\" data-success=\"right\">Account</label>"
					+ "\n<input type=\"text\" id=\"account_id\" name=\"account_id\" class=\"form-control validate\" value=\"${"
					+ table_name_lower + "_updt.account}\" readonly>" + "\n</div>" + "\n</div>"

					+ "\n<div class=\"modal-body mx-3\">" + "\n<div class=\"md-form mb-5\">"
					+ "\n<label data-error=\"wrong\" data-success=\"right\">Form_Name</label>"
					+ "\n<input type=\"text\" id=\"form_name\" name=\"form_name\" class=\"form-control validate\" value=\"${"
					+ table_name_lower + "_updt.form_name}\" readonly>" + "\n</div>" + "\n</div>"

					+ "\n<div class=\"modal-body mx-3\">" + "\n<div class=\"md-form mb-5\">"
					+ "\n<label data-error=\"wrong\" data-success=\"right\">Form_Code</label>"
					+ "\n<input type=\"text\" id=\"form_code\" name=\"form_code\" class=\"form-control validate\" value=\"${"
					+ table_name_lower + "_updt.form_code}\" readonly>" + "\n</div>" + "\n</div>"

					+ "\n<div class=\"modal-body mx-3\">" + "\n<div class=\"md-form mb-5\">"
					+ "\n<label data-error=\"wrong\" data-success=\"right\">Created_By</label>"
					+ "\n<input type=\"text\" id=\"created_by\" name=\"created_by\" class=\"form-control validate\" value=\"${"
					+ table_name_lower + "_updt.created_by}\" readonly>" + "\n</div>" + "\n</div>"

					+ "\n<div class=\"modal-body mx-3\">" + "\n<div class=\"md-form mb-5\">"
					+ "\n<label data-error=\"wrong\" data-success=\"right\">Creation_Date</label>"
					+ "\n<input type=\"text\" id=\"creation_date\" name=\"creation_date\" class=\"form-control validate\" value=\"${"
					+ table_name_lower + "_updt.creation_date}\" readonly>" + "\n</div>" + "\n</div>"

					+ "\n<div class=\"modal-body mx-3\">" + "\n<div class=\"md-form mb-5\">"
					+ "\n<label data-error=\"wrong\" data-success=\"right\">Last_Updated_by</label>"
					+ "\n<input type=\"text\" id=\"last_updated_by\" name=\"last_updated_by\" class=\"form-control validate\" value=\"${"
					+ table_name_lower + "_updt.last_updated_by}\" readonly>" + "\n</div>" + "\n</div>"

					+ "\n<div class=\"modal-body mx-3\">" + "\n<div class=\"md-form mb-5\">"
					+ "\n<label data-error=\"wrong\" data-success=\"right\">Last_Update_Date</label>"
					+ "\n<input type=\"text\" id=\"last_update_date\" name=\"last_update_date\" class=\"form-control validate\" value=\"${"
					+ table_name_lower + "_updt.last_update_date}\" readonly>" + "\n</div>" + "\n</div>");

		}

		if (table_name != null && !table_name.isEmpty()) {
			String table_name_lower = table_name.toLowerCase();
			String table_name_first_upper = table_name_lower.substring(0, 1).toUpperCase()
					+ table_name_lower.substring(1);
			String table_name_upper = table_name.toUpperCase();
			popup.append("\n\n<!------------------------popup start------------------------------------------ -->"

					+ "\n<div class=\"modal fade\" id=\"myModal\" role=\"dialog\">" + "\n<div class=\"modal-dialog\">"

					+ "\n<!-- Modal content-->" + "\n<div class=\"modal-content\">" + "\n<div class=\"modal-header\">"
					+ "\n<button type=\"button\" class=\"close\" data-dismiss=\"modal\">&times;</button>" + "\n</div>"

					+ "\n<input type=\"hidden\" name=\"test\" id=\"test\" value=\"\"> "

					+ "\n<c:forEach var=\"" + table_name_lower + "_updt\" items=\"${" + table_name_lower
					+ "_update}\" varStatus=\"status\">"

					+ popup_id

					+ popup_var

					+ "\n</c:forEach>"

					+ "\n<div class=\"modal-body\">"

					+ "\n</div>" + "\n<div class=\"modal-footer\">"
					+ "\n<button type=\"button\" class=\"btn btn-default\" data-dismiss=\"modal\">Close</button>"
					+ "\n</div>" + "\n</div>" + "\n</div>" + "\n</div>"

					+ "\n<div class=\"modal-body mx-3\">");
		}

		// -----------------------------------------------------line

		line_script.append("\n$('#rowcount1').val($('#dynamic-table1 tr').length-1);"
				+ "\nvar count = $('#rowcount1').val();" + dropdown_script_latest_for_line
				+ "\n$('.Deleterow').click(function() { " + "\nvar index = $('.Deleterow').index(this)+1;"
				+ "\n$('#delrow1').val(index);" + "\n});" + "\n}" + "\n</script>" + "\n<script>" + "\nfunction del(){"
				+ "\nvar index=$('#delrow1').val();" + "\nif(index!= \"\")" + "\n{"
				+ "\ndocument.getElementById(\"dynamic-table1\").deleteRow(index);" + "\n$('#delrow1').val(\"\");"
				+ "\n}" + "\n}" + "\n</script>");

		// script for line update part
		line_script_update.append("\n<script language=\"javascript\">" + "\nfunction AddRow()  "
				+ "\n{$('#dynamic-table1 tr').last().after('<tr>" + line_value);

		line_script_update.append("</tr>');" + "\n$('#rowcount1').val($('#dynamic-table1 tr').length-1);"
				+ "\nvar count = $('#rowcount1').val();" + "\n$.ajax({"
				+ "\nurl: '${pageContext.request.contextPath}/loadreports'," + "\ndata: ({name : 'name'}),"
				+ "\nsuccess: function(data) {" + "\nvar select = $('#name'+count);"
				+ "\nselect.find('option').remove();" + "\n$.each(data, function(index, value) {"
				+ "\n$('<option>').val(value).text(value).appendTo(select);" + "\n});" + "\n}" + "\n});"

				+ "\n$('.Deleterow').click(function() { " + "\nvar index = $('.Deleterow').index(this)+1;"
				+ "\n$('#delrow1').val(index);" + "\n});" + "\n}" + "\n</script>"

				+ "\n<script>" + "\nfunction del(){" + "\nvar index=$('#delrow1').val();" + "\nif(index!= \"\")" + "\n{"
				+ "\ndocument.getElementById(\"dynamic-table1\").deleteRow(index);" + "\n$('#delrow1').val(\"\");"
				+ "\n}" + "\n}" + "\n</script>");

		// List<RN_EXT_FIELDS> ext_user = contactDAO.ext_userlist2(f_code);
		String table_name_lower2 = table_name.toLowerCase();
		if (multiple_line_name != null && !multiple_line_name.isEmpty()) {
			table_name_lower2 = table_name.toLowerCase();

			String[] single_line_name = multiple_line_name.split(",");
			href.append(
					"<a id=\"anchorTag\" href=\"form_builder_extension\"><i class=\"fa fa-paper-plane-o fa-1x\" aria-hidden=\"true\" title=\"help\"></i></a>");

			static_form_code.append("\n<% String f_code=\"" + form_code + "\";" + "\n%>"
					+ "\n<script type=\"text/javascript\">" + "\nvar form_code = '<%=f_code%>';"
					+ "\nconsole.log(form_code);" + "\ndocument.getElementById(\"anchorTag\").onclick = function()"
					+ "{" + "\nconsole.log(\"called\");"
					+ "\nwindow.location.href = './form_builder_multiextension?form_code='+form_code;" + "\n}"
					+ "\n</script>");

		} else {
			href.append(
					"<a href=\"form_builder_extension\"><i class=\"fa fa-paper-plane-o fa-1x\" aria-hidden=\"true\" title=\"help\"></i></a>");

		}

		strContent.append(importsection + " \n" + headsection
				+ "\n<body>\n<div class=\"main-container\" id=\"main-container\">" + "\n<div class=\"main-content\">"
				+ "\n<div class=\"main-content-inner\">" + "\n<div class=\"breadcrumbs\" id=\"breadcrumbs\">"
				+ "\n<ul class=\"breadcrumb\">"
				+ "\n<li><i class=\"ace-icon fa fa-home home-icon\"></i> <a href=\"#\">Home</a>" + "\n</li>"
				+ "\n<li><a href=\"#\">ManageUsers</a></li>" + "\n<li class=\"active\">" + table_name_lower2 + "</li>"
				+ "\n</ul>" + "\n</div>" + "\n<div class=\"page-content\">" + "\n<div class=\"page-header\">" + "\n<h1>"
				+ table_name_lower2 + "" + "<div style=\"float: right; padding-right: 5%;\">" + href + "</div></h1>"
				+ "\n</div>" + "\n<div class=\"row\">" + "\n<div class=\"col-xs-12\">"

				+ static_form_code + "\n<div class=\"widget-body\">" + "\n<div class=\"widget-main\">"
				+ "\n<div id=\"fuelux-wizard-container\">" + "\n<div class=\"step-content pos-rel\">"
				+ "\n<div class=\"step-pane active\" data-step=\"1\">"
				+ "<div class=\"table-header\" style=\"margin-bottom: 30px; margin-top: 30px;\">" + "Section" + "</div>"
				+ form + "\n</div>" + "\n</div>" + "\n</div>" + "\n</div>" + "\n</div>" + "\n</div>" + "\n</div>"
				+ "\n</div>" + "\n</div>" + "\n</div>" + "\n</div>"
				+ "<script src=\"<c:url value='/resources/assets/js/bootstrap-datepicker.min.js'/>\" type=\"text/javascript\">"
				/*
				 * + "\n</script>" + "<script>" + date_script + dropdown_script_latest +
				 * "</script>" + autocomplete_script_latest + line_script + dropdown_script +
				 * dependent_dropdown + autocomplete_script
				 */
				+ "\n</body>\n</html>");

		// ---------------------------------grid view
		// code-------------------------------------------------
		if (table_name != null && !table_name.isEmpty()) {
			String table_name_lower = table_name.toLowerCase();
			String table_name_first_upper = table_name_lower.substring(0, 1).toUpperCase()
					+ table_name_lower.substring(1);
			String table_name_upper = table_name.toUpperCase();
			for (int i = 0; i < id_list.size(); i++) {
				String field_name = id_list.get(i).getMapping();
				String field_name_lower = field_name.toLowerCase();
				table_field_for_id.append("\n<th class=\"center\">" + field_name + "</th>");

				// table_field_value_for_id.append("\n<td class=\"center\"><a
				// href=\""+table_name_lower+"_update?"+field_name+"=${"+table_name_lower+"."+field_name+"}\""</td>");

				table_field_value_for_id.append("<td class=\"center\">" + "<a href=\"" + table_name_lower + "_readonly?"
						+ field_name_lower + "=${" + table_name_lower + "." + field_name_lower + "}\">"
						+ "<i class=\"fa fa-eye green\" aria-hidden=\"true\"></i>/" + "<a href=\"" + table_name_lower
						+ "_update?" + field_name_lower + "=${" + table_name_lower + "." + field_name_lower + "}\">"
						+ "<i class=\"fa fa-edit red\" aria-hidden=\"true\"></i>/"
						+ "<a href=\"rolenewviewreports?user_id=${rn_userlist.id}\">"
						+ "<i class=\"fa fa-graduation-cap\" aria-hidden=\"true\"></i>" + "</a>" + "</td>");

			}
		}

		if (table_name != null && !table_name.isEmpty()) {
			String table_name_lower = table_name.toLowerCase();
			String table_name_first_upper = table_name_lower.substring(0, 1).toUpperCase()
					+ table_name_lower.substring(1);
			String table_name_upper = table_name.toUpperCase();

			for (int i = 0; i < id_notprimary.size(); i++) {
				String field_name = id_notprimary.get(i).getMapping();
				String data_type = id_notprimary.get(i).getDataType();
				String lower_case = field_name.toLowerCase();
				String only_upper = field_name.toUpperCase();
				String first_upper = lower_case.substring(0, 1).toUpperCase() + lower_case.substring(1);
				if (data_type.equals("int")) {

					id_not_pri_field_for_grid.append("\n<th class=\"center\">" + field_name + "</th>");
					id_not_pri_field_for_grid_value
							.append("\n<td class=\"center\">${" + table_name_lower + "." + lower_case + "}</td>");
				}

			}
		}

		if (table_name != null && !table_name.isEmpty()) {
			String table_name_lower = table_name.toLowerCase();
			String table_name_first_upper = table_name_lower.substring(0, 1).toUpperCase()
					+ table_name_lower.substring(1);
			String table_name_upper = table_name.toUpperCase();

			for (int i = 0; i < datetime_list.size(); i++) {
				String field_name = datetime_list.get(i).getMapping();
				String data_type = datetime_list.get(i).getDataType();

				String lower_case = field_name.toLowerCase();
				String only_upper = field_name.toUpperCase();
				String first_upper = lower_case.substring(0, 1).toUpperCase() + lower_case.substring(1);

				if (data_type.equals("datetime")) {

					date_field_for_grid.append("\n<th class=\"center\">" + field_name + "</th>");
					date_field_for_grid_value
							.append("\n<td class=\"center\">${" + table_name_lower + "." + lower_case + "}</td>");

				}

			}
		}
		if (table_name != null && !table_name.isEmpty()) {
			String table_name_lower = table_name.toLowerCase();
			String table_name_first_upper = table_name_lower.substring(0, 1).toUpperCase()
					+ table_name_lower.substring(1);
			String table_name_upper = table_name.toUpperCase();

			for (int i = 0; i < rn_userlist.size(); i++) {
				String field_name = rn_userlist.get(i).getMapping();
				String field_name_lower = field_name.toLowerCase();

				table_field.append("\n<th class=\"center\">" + field_name + "</th>");

				table_field_value
						.append("\n<td class=\"center\">${" + table_name_lower + "." + field_name_lower + "}</td>");

			}
		}

		if (table_name != null && !table_name.isEmpty()) {
			String table_name_lower = table_name.toLowerCase();
			String table_name_first_upper = table_name_lower.substring(0, 1).toUpperCase()
					+ table_name_lower.substring(1);
			String table_name_upper = table_name.toUpperCase();
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
					+ "\n</head>" + "\n<body class=\"no-skin\">"
					+ "\n<div class=\"main-container\" id=\"main-container\">" + "\n<div class=\"main-content\">"
					+ "\n<div class=\"main-content-inner\">" + "\n<div class=\"breadcrumbs\" id=\"breadcrumbs\">"
					+ "\n<script type=\"text/javascript\">"
					+ "\ntry{ace.settings.check('breadcrumbs' , 'fixed')}catch(e){}" + "\n</script>"
					+ "\n<ul class=\"breadcrumb\">" + "\n<li>" + "\n<i class=\"ace-icon fa fa-home home-icon\"></i>"
					+ "\n<a href=\"#\">Home</a>" + "\n</li>"

					+ "\n<li class=\"active\"> " + table_name_lower + "</li>" + "\n</ul>" + " \n </div>"
					+ "\n<div class=\"page-content\">" + "\n <div class=\"page-header\">" + "\n<h1>" + "\n"
					+ table_name_lower + "" + "\n<div style=\"float: right; padding-right: 3%;\">" + "\n<a href=\""
					+ table_name_lower
					+ "_entryform\"> <i class=\"fa fa-plus fa-1g\" aria-hidden=\"true\" title=\"Add New Report\"></i></a>"
					+ "\n</div>" + "\n</h1>" + "\n</div><!-- /.page-header -->"

					+ "\n<div class=\"row\">" + "\n<div class=\"col-xs-12\">" + "\n<div class=\"row\">"
					+ "\n<div class=\"col-xs-12\">" + "\n<div class=\"clearfix\">"
					+ "\n<div class=\"pull-right tableTools-container\"></div>" + "\n</div>"
					+ "\n<div class=\"table-header\">" + "\nGrid View" + "\n</div>" + "\n<div>"

					+ "\n<table   class=\"table table-striped table-bordered table-hover\" id=\"table1\"  cellspacing=\"0\" width=\"1500px\" style=\"width:100%; margin: 0 auto;\">"
					+ "\n<thead>" + "\n<tr>" + table_field_for_id + id_not_pri_field_for_grid + date_field_for_grid
					+ table_field + "\n<%@include file=\"/WEB-INF/tiles/acemaster/" + module_name + "/"
					+ table_name_lower + "_add_grid.jsp\"%>\n</tr>" + "\n</thead>" + "\n<tbody>" + "\n<c:forEach var=\""
					+ table_name_lower + "\" items=\"${" + table_name_lower + "}\" varStatus=\"status\">" + "\n<tr>	"
					+ table_field_value_for_id + id_not_pri_field_for_grid_value + date_field_for_grid_value
					+ table_field_value

					+ "<%@include file=\"/WEB-INF/tiles/acemaster/" + module_name + "/" + table_name_lower
					+ "_add_grid2.jsp\"%>\n</tr>" + "\n</c:forEach>" + "\n</tbody>" + " \n</table>"

					+ "\n</div>" + "\n</div>" + "\n</div>" + "\n</div>" + "\n</div>" + "\n</div>" + "\n</div>"
					+ "\n</div>"

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

					+ "\n} );" + "\n} );" + "\njQuery(function($)" + "\n{" + "\nvar oTable1 = "
					+ "\n$('#dynamic-table')" + "\n.dataTable( {" + "\nbAutoWidth: false," + "\n\"aoColumns\": ["
					+ "\n{ \"bSortable\": false }," + "\n null, null,null, null, null," + "\n{ \"bSortable\": false }"
					+ "\n]," + "\n\"aaSorting\": [],"

					+ "\n} );" + "\n})" + "\n</script>" + "\n</body>" + "\n</html>");

			// ------------------------------------ jsp
			// readonly---------------------------------------------------------

			importsectionreadonly.append(
					"\n<%@ page language=\"java\" contentType=\"text/html; charset=UTF-8\" pageEncoding=\"UTF-8\"%>"

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
					+ "\n<link rel=\"stylesheet\""
					+ "\nhref=\"<c:url value='/resources/assets/css/chosen.min.css' />\" />"
					+ "\n<link rel=\"stylesheet\""
					+ "\nhref=\"<c:url value='/resources/assets/css/datepicker.min.css'/>\" />"
					+ "\n<link rel=\"stylesheet\""
					+ "\nhref=\"<c:url value='/resources/assets/css/bootstrap-timepicker.min.css'/>\" />"
					+ "\n<link rel=\"stylesheet\""
					+ "\nhref=\"<c:url value='/resources/assets/css/daterangepicker.min.css' />\" />"
					+ "\n<link rel=\"stylesheet\""
					+ "\nhref=\"<c:url value='/resources/assets/css/bootstrap-datetimepicker.min.css'/>\" />"
					+ "\n<link rel=\"stylesheet\""
					+ "\nhref=\"<c:url value='/resources/assets/css/colorpicker.min.css' />\" />"
					+ "\n<!-- text fonts -->" + "\n<link rel=\"stylesheet\""
					+ "\n\nhref=\"<c:url value='/resources/assets/fonts/fonts.googleapis.com.css' />\" />"
					+ "\n<!-- ace styles -->" + "\n<link rel=\"stylesheet\""
					+ "\nhref=\"<c:url value='/resources/assets/css/ace.min.css\" class=\"ace-main-stylesheet\" id=\"main-ace-style' />\" />"
					+ "\n<!-- inline styles related to this page -->"

					+ "\n<!-- ace settings handler -->"
					+ "\n<script src=\"<c:url value='/resources/assets/js/ace-extra.min.js'/>\""
					+ "\ntype=\"text/javascript\"></script>"

					+ "\n<script>" + "\nsubmitForms = function()" + "\n{"
					+ "\ndocument.forms[\"userdetails1\"].submit();" + "\ndocument.forms[\"userdetails2\"].submit();"
					+ "}" + "\n</script> "

					+ " \n</head>");

			form_readonly.append("\n<form action=\"submit_form\" class=\"form-horizontal\" id=\"Regi\" method=\"Post\">"
					+ "\n<input type=\"hidden\" name=\"test\" id=\"test\" value=\"\" />" + " \n<table>"
					+ "\n<c:forEach var=\"" + table_name_lower + "_updt\" items=\"${" + table_name_lower
					+ "_update}\" varStatus=\"status\">"

					+ "\n<tr>");
		}
		if (table_name != null && !table_name.isEmpty()) {
			String table_name_lower = table_name.toLowerCase();
			String table_name_first_upper = table_name_lower.substring(0, 1).toUpperCase()
					+ table_name_lower.substring(1);
			String table_name_upper = table_name.toUpperCase();

			for (int i = 0; i < id_notprimary.size(); i++) {

				String field_name = id_notprimary.get(i).getMapping();
				String data_type = id_notprimary.get(i).getDataType();
				String lower_case = field_name.toLowerCase();
				String only_upper = field_name.toUpperCase();
				String first_upper = lower_case.substring(0, 1).toUpperCase() + lower_case.substring(1);

				if (data_type.equals("int")) {
					form_readonly.append(
							"\n<td>" + "\n<div class=\"form-group\" style=\"margin-left: 10%; margin-right: 10%;\">"
									+ "\n<label class=\"control-label col-xs-12 col-sm-3 no-padding-right\">"
									+ first_upper + "\n<i class=\"menu-icon fa red\"> *</i>" + "\n</label>"
									+ "\n<div class=\"col-xs-12 col-sm-9\">" + "\n<div class=\"clearfix\">"
									+ "\n<input   value=\"${" + table_name_lower + "_updt." + lower_case
									+ "}\"type=\"text\" name=\"" + lower_case + "\"id=\"" + lower_case
									+ "\"class=\"col-xs-12 col-sm-4\" readonly/>" + "\n</div>" + "\n</div>" + "\n</div>"
									+ "\n</td>");
				}

			}
		}
		form_readonly.append("\n</tr>");

		form_readonly.append("\n<tr>");

		if (table_name != null && !table_name.isEmpty()) {
			String table_name_lower = table_name.toLowerCase();
			String table_name_first_upper = table_name_lower.substring(0, 1).toUpperCase()
					+ table_name_lower.substring(1);
			String table_name_upper = table_name.toUpperCase();

			for (int i = 0; i < datetime_list.size(); i++) {

				String field_name = datetime_list.get(i).getMapping();
				String data_type = datetime_list.get(i).getDataType();
				String lower_case = field_name.toLowerCase();
				String only_upper = field_name.toUpperCase();
				String first_upper = lower_case.substring(0, 1).toUpperCase() + lower_case.substring(1);

				boolean mandatory = datetime_list.get(i).isMandatory();
				boolean readonly = datetime_list.get(i).isReadonly();
				boolean hidden = datetime_list.get(i).isHidden();

				String mapping = datetime_list.get(i).getMapping();
				String mapping_lower = mapping.toLowerCase();

				boolean dependent = datetime_list.get(i).isDependent();
				String dependent_on = datetime_list.get(i).getDependent_on();
				String dependent_sp = datetime_list.get(i).getDependent_sp();
				String dependent_sp_param = datetime_list.get(i).getDependent_sp_param();

				boolean sequence = datetime_list.get(i).isSequence();
				String seq_name = datetime_list.get(i).getSeq_name();
				String seq_sp = datetime_list.get(i).getSeq_sp();
				String seq_sp_param = datetime_list.get(i).getSeq_sp_param();

				boolean validation_1 = datetime_list.get(i).isValidation_1();
				String val_type = datetime_list.get(i).getVal_type();
				String val_sp = datetime_list.get(i).getVal_sp();
				String val_sp_param = datetime_list.get(i).getVal_sp_param();

				boolean default_1 = datetime_list.get(i).isDefault_1();
				String default_typename = datetime_list.get(i).getDefault_type();
				String default_value = datetime_list.get(i).getDefault_value();
				String default_sp = datetime_list.get(i).getDefault_sp();
				String default_sp_param = datetime_list.get(i).getDefault_sp_param();

				boolean calculated_field = datetime_list.get(i).isCalculated_field();
				String cal_sp = datetime_list.get(i).getCal_sp();
				String cal_sp_param = datetime_list.get(i).getCal_sp_param();

				boolean add_to_grid = datetime_list.get(i).getAdd_to_grid();

				boolean sp_for_dropdown = datetime_list.get(i).getSp_for_dropdown();
				boolean sp_for_autocomplete = datetime_list.get(i).getSp_for_autocomplete();

				String sp_name_for_dropdown = datetime_list.get(i).getSp_name_for_dropdown();
				String sp_name_for_autocomplete = datetime_list.get(i).getSp_name_for_autocomplete();

				System.out.println("-----value of mantadoryr laest-----" + mandatory);

				if (data_type.equals("datetime")) {

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

					form_readonly.append("name=" + mapping_lower + "  value=\"${" + table_name_lower + "_updt."
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

			for (int i = 0; i < rn_userlist.size(); i++) {
				String field_name4 = rn_userlist.get(i).getMapping();
				String field_name_lower = field_name4.toLowerCase();
				String mapping4 = rn_userlist.get(i).getMapping();
				String mapping_lower_case = mapping4.toLowerCase();
				String data_type4 = rn_userlist.get(i).getDataType();

				System.out.println("field name-------" + field_name4);
				System.out.println("mapping-------" + mapping4);
				System.out.println("data type-------" + data_type4);

				System.out.println("field name-------" + field_name4);
				System.out.println("mapping-------" + mapping4);
				System.out.println("data type-------" + data_type4);

				String lower_case = field_name4.toLowerCase();
				System.out.println("field name-------" + field_name4);
				System.out.println("mapping-------" + mapping4);
				System.out.println("data type-------" + data_type4);

				boolean mandatory = rn_userlist.get(i).isMandatory();
				boolean readonly = rn_userlist.get(i).isReadonly();
				boolean hidden = rn_userlist.get(i).isHidden();

				// String mapping=rn_userlist.get(i).getMapping();

				String type_field = rn_userlist.get(i).getType_field();

				boolean dependent = rn_userlist.get(i).isDependent();
				String dependent_on = rn_userlist.get(i).getDependent_on();
				String dependent_sp = rn_userlist.get(i).getDependent_sp();
				String dependent_sp_param = rn_userlist.get(i).getDependent_sp_param();

				boolean sequence = rn_userlist.get(i).isSequence();
				String seq_name = rn_userlist.get(i).getSeq_name();
				String seq_sp = rn_userlist.get(i).getSeq_sp();
				String seq_sp_param = rn_userlist.get(i).getSeq_sp_param();

				boolean validation_1 = rn_userlist.get(i).isValidation_1();
				String val_type = rn_userlist.get(i).getVal_type();
				String val_sp = rn_userlist.get(i).getVal_sp();
				String val_sp_param = rn_userlist.get(i).getVal_sp_param();

				boolean default_1 = rn_userlist.get(i).isDefault_1();
				String default_typename = rn_userlist.get(i).getDefault_type();
				String default_value = rn_userlist.get(i).getDefault_value();
				String default_sp = rn_userlist.get(i).getDefault_sp();
				String default_sp_param = rn_userlist.get(i).getDefault_sp_param();

				boolean calculated_field = rn_userlist.get(i).isCalculated_field();
				String cal_sp = rn_userlist.get(i).getCal_sp();
				String cal_sp_param = rn_userlist.get(i).getCal_sp_param();

				boolean add_to_grid = rn_userlist.get(i).getAdd_to_grid();

				boolean sp_for_dropdown = rn_userlist.get(i).getSp_for_dropdown();
				boolean sp_for_autocomplete = rn_userlist.get(i).getSp_for_autocomplete();

				String sp_name_for_dropdown = rn_userlist.get(i).getSp_name_for_dropdown();
				String sp_name_for_autocomplete = rn_userlist.get(i).getSp_name_for_autocomplete();

				if (i <= 2) {
					if (type_field.equals("textfield")) {
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
							form_readonly.append("  value=\"${" + table_name_lower + "_updt." + mapping_lower_case
									+ "}\" type=\"text\" ");
						}
						if (calculated_field == true) {
							form_readonly.append("name=\"total\" id=\"total\" class=\"stunden form-control\" ");
						} else {
							form_readonly.append("name=\"" + mapping_lower_case + "\" id=\"" + mapping_lower_case
									+ "\" class=\"col-xs-12 col-sm-4\" pattern=\"[A-Za-z]{1,30}\"");
						}

						if (mandatory == true) {
							System.out.println("-------------in loop 1 part 2 required-------------------");
							form_readonly.append("required");
						}

						System.out.println("-------------in loop 1 part 2 required-------------------");
						form_readonly.append("  readonly");

						form_readonly.append("/>\n</div>" + "\n</div>" + "\n</div>" + "\n</td>");
					}

					if (type_field.equals("autocomplete")) {
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
							form_readonly.append(" value=\"${" + table_name_lower + "_updt." + mapping_lower_case
									+ "}\"  type=\"hidden\" ");
						} else {
							form_readonly.append("  value=\"${" + table_name_lower + "_updt." + mapping_lower_case
									+ "}\" type=\"text\" ");
						}
						if (calculated_field == true) {
							form_readonly.append("name=\"total\" id=\"total\" class=\"stunden form-control\" ");
						} else {
							form_readonly.append("name=" + mapping_lower_case + " id=\"" + mapping_lower_case
									+ "\" class=\"col-xs-12 col-sm-4\" readonly");
						}

						/*
						 * if(mandatory.equals("Y")) { System.out.
						 * println("-------------in loop 1 part 2 required-------------------" );
						 * form.append("required"); }
						 */

						/*
						 * if(readonly.equals("Y")) { System.out.
						 * println("-------------in loop 1 part 2 required-------------------" );
						 * form.append("  readonly"); }
						 */
						form_readonly.append("/>\n</div>" + "\n</div>" + "\n</div>" + "\n</td>");

					}

					if (type_field.equals("dropdown")) {
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

						if (dependent == true) {

							form_readonly.append("\n<select name=\"state_name\" id=\"" + mapping4
									+ "\"  class=\"col-xs-3 col-sm-3 form-control state\">"
									+ "\n<option >---select---</option>" + "\n</select>");
						} else {

							form_readonly.append("\n<select name=\"drop_value\" id=\"" + mapping4
									+ "\"  value=\"${drop_value}\" class=\"col-xs-3 col-sm-3 form-control");
							form_readonly.append("\t" + mapping4);
							form_readonly.append("\" readonly>" + "\n<option >---select---</option>"
									+ "\n<option value=\"${drop_value}\"");

							if (mandatory == true) {
								System.out.println("-------------in loop 1 part 2 required-------------------");
								form_readonly.append("required");
							}

							if (readonly == true) {
								System.out.println("-------------in loop 1 part 2 required-------------------");
								form_readonly.append("readonly");
							}
							form_readonly.append(">${drop_value}</option>\n" + "</select>\n");
						}
						form_readonly.append("</div>" + "\n</div>" + "\n</div>" + "\n</td>");
					}

					if (type_field.equals("togglebutton")) {
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

						form_readonly.append(" value=\"${" + table_name_lower + "_updt." + field_name_lower
								+ "}\" name=" + mapping4 + " id=" + mapping4
								+ "  class=\"ace ace-switch ace-switch-6\" onblur=\"CheckUserStatus()\"");

						form_readonly.append("readonly />\n" + "\n<span class=\"lbl\"></span>"

								+ "\n</div>" + "\n</div>" + "\n</div>" + "\n</td>");
					}

					if (type_field.equals("checkbox")) {
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

						form_readonly.append(
								" value=\"${" + table_name_lower + "_updt." + field_name_lower + "}\" name=" + mapping4
										+ " id=" + mapping4 + "  onblur=\"CheckUserStatusHeader1()\" readonly");

						form_readonly.append("/>\n" + "\n</div>" + "\n</div>" + "\n</div>" + "\n</td>");
					}
				}
			}

			form_readonly.append("\n</tr>");

			form_readonly.append("\n<tr>");
			for (int i = 0; i < rn_userlist.size(); i++) {
				String field_name4 = rn_userlist.get(i).getMapping();
				String field_name_lower = field_name4.toLowerCase();
				String mapping4 = rn_userlist.get(i).getMapping();
				String mapping_lower_case = mapping4.toLowerCase();
				String data_type4 = rn_userlist.get(i).getDataType();

				System.out.println("field name-------" + field_name4);
				System.out.println("mapping-------" + mapping4);
				System.out.println("data type-------" + data_type4);

				System.out.println("field name-------" + field_name4);
				System.out.println("mapping-------" + mapping4);
				System.out.println("data type-------" + data_type4);

				String lower_case = field_name4.toLowerCase();
				System.out.println("field name-------" + field_name4);
				System.out.println("mapping-------" + mapping4);
				System.out.println("data type-------" + data_type4);

				boolean mandatory = rn_userlist.get(i).isMandatory();
				boolean readonly = rn_userlist.get(i).isReadonly();
				boolean hidden = rn_userlist.get(i).isHidden();

				// String mapping=rn_userlist.get(i).getMapping();

				String type_field = rn_userlist.get(i).getType_field();

				boolean dependent = rn_userlist.get(i).isDependent();
				String dependent_on = rn_userlist.get(i).getDependent_on();
				String dependent_sp = rn_userlist.get(i).getDependent_sp();
				String dependent_sp_param = rn_userlist.get(i).getDependent_sp_param();

				boolean sequence = rn_userlist.get(i).isSequence();
				String seq_name = rn_userlist.get(i).getSeq_name();
				String seq_sp = rn_userlist.get(i).getSeq_sp();
				String seq_sp_param = rn_userlist.get(i).getSeq_sp_param();

				boolean validation_1 = rn_userlist.get(i).isValidation_1();
				String val_type = rn_userlist.get(i).getVal_type();
				String val_sp = rn_userlist.get(i).getVal_sp();
				String val_sp_param = rn_userlist.get(i).getVal_sp_param();

				boolean default_1 = rn_userlist.get(i).isDefault_1();
				String default_typename = rn_userlist.get(i).getDefault_type();
				String default_value = rn_userlist.get(i).getDefault_value();
				String default_sp = rn_userlist.get(i).getDefault_sp();
				String default_sp_param = rn_userlist.get(i).getDefault_sp_param();

				boolean calculated_field = rn_userlist.get(i).isCalculated_field();
				String cal_sp = rn_userlist.get(i).getCal_sp();
				String cal_sp_param = rn_userlist.get(i).getCal_sp_param();

				boolean add_to_grid = rn_userlist.get(i).getAdd_to_grid();

				boolean sp_for_dropdown = rn_userlist.get(i).getSp_for_dropdown();
				boolean sp_for_autocomplete = rn_userlist.get(i).getSp_for_autocomplete();

				String sp_name_for_dropdown = rn_userlist.get(i).getSp_name_for_dropdown();
				String sp_name_for_autocomplete = rn_userlist.get(i).getSp_name_for_autocomplete();

				if (i > 2 && i <= 5) {
					if (type_field.equals("textfield")) {
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
							form_readonly.append("  value=\"${" + table_name_lower + "_updt." + mapping_lower_case
									+ "}\" type=\"text\" ");
						}
						if (calculated_field == true) {
							form_readonly.append("name=\"total\" id=\"total\" class=\"stunden form-control\" ");
						} else {
							form_readonly.append("name=\"" + mapping_lower_case + "\" id=\"" + mapping_lower_case
									+ "\" class=\"col-xs-12 col-sm-4\" pattern=\"[A-Za-z]{1,30}\"");
						}

						if (mandatory == true) {
							System.out.println("-------------in loop 1 part 2 required-------------------");
							form_readonly.append("required");
						}

						System.out.println("-------------in loop 1 part 2 required-------------------");
						form_readonly.append("  readonly");

						form_readonly.append("/>\n</div>" + "\n</div>" + "\n</div>" + "\n</td>");
					}

					if (type_field.equals("autocomplete")) {
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
							form_readonly.append(" value=\"${" + table_name_lower + "_updt." + mapping_lower_case
									+ "}\"  type=\"hidden\" ");
						} else {
							form_readonly.append("  value=\"${" + table_name_lower + "_updt." + mapping_lower_case
									+ "}\" type=\"text\" ");
						}
						if (calculated_field == true) {
							form_readonly.append("name=\"total\" id=\"total\" class=\"stunden form-control\" ");
						} else {
							form_readonly.append("name=" + mapping_lower_case + " id=\"" + mapping_lower_case
									+ "\" class=\"col-xs-12 col-sm-4\" readonly");
						}

						/*
						 * if(mandatory.equals("Y")) { System.out.
						 * println("-------------in loop 1 part 2 required-------------------" );
						 * form.append("required"); }
						 */

						/*
						 * if(readonly.equals("Y")) { System.out.
						 * println("-------------in loop 1 part 2 required-------------------" );
						 * form.append("  readonly"); }
						 */
						form_readonly.append("/>\n</div>" + "\n</div>" + "\n</div>" + "\n</td>");

					}

					if (type_field.equals("dropdown")) {
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

						if (dependent == true) {

							form_readonly.append("\n<select name=\"state_name\" id=\"" + mapping4
									+ "\"  class=\"col-xs-3 col-sm-3 form-control state\">"
									+ "\n<option >---select---</option>" + "\n</select>");
						} else {

							form_readonly.append("\n<select name=\"drop_value\" id=\"" + mapping4
									+ "\"  value=\"${drop_value}\" class=\"col-xs-3 col-sm-3 form-control");
							form_readonly.append("\t" + mapping4);
							form_readonly.append("\" readonly>" + "\n<option >---select---</option>"
									+ "\n<option value=\"${drop_value}\"");

							if (mandatory == true) {
								System.out.println("-------------in loop 1 part 2 required-------------------");
								form_readonly.append("required");
							}

							if (readonly == true) {
								System.out.println("-------------in loop 1 part 2 required-------------------");
								form_readonly.append("readonly");
							}
							form_readonly.append(">${drop_value}</option>\n" + "</select>\n");
						}
						form_readonly.append("</div>" + "\n</div>" + "\n</div>" + "\n</td>");
					}

					if (type_field.equals("togglebutton")) {
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

						form_readonly.append(" value=\"${" + table_name_lower + "_updt." + field_name_lower
								+ "}\" name=" + mapping4 + " id=" + mapping4
								+ "  class=\"ace ace-switch ace-switch-6\" onblur=\"CheckUserStatus()\"");

						form_readonly.append("readonly />\n" + "\n<span class=\"lbl\"></span>"

								+ "\n</div>" + "\n</div>" + "\n</div>" + "\n</td>");
					}

					if (type_field.equals("checkbox")) {
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

						form_readonly.append(
								" value=\"${" + table_name_lower + "_updt." + field_name_lower + "}\" name=" + mapping4
										+ " id=" + mapping4 + "  onblur=\"CheckUserStatusHeader1()\" readonly");

						form_readonly.append("/>\n" + "\n</div>" + "\n</div>" + "\n</div>" + "\n</td>");
					}
				}
			}
		}

		form_readonly.append("\n</tr>");

		form_readonly.append("\n<tr>");

		if (table_name != null && !table_name.isEmpty()) {
			String table_name_lower = table_name.toLowerCase();
			String table_name_first_upper = table_name_lower.substring(0, 1).toUpperCase()
					+ table_name_lower.substring(1);
			String table_name_upper = table_name.toUpperCase();

			for (int i = 0; i < rn_userlist.size(); i++) {
				String field_name4 = rn_userlist.get(i).getMapping();
				String field_name_lower = field_name4.toLowerCase();
				String mapping4 = rn_userlist.get(i).getMapping();
				String mapping_lower_case = mapping4.toLowerCase();
				String data_type4 = rn_userlist.get(i).getDataType();

				System.out.println("field name-------" + field_name4);
				System.out.println("mapping-------" + mapping4);
				System.out.println("data type-------" + data_type4);

				System.out.println("field name-------" + field_name4);
				System.out.println("mapping-------" + mapping4);
				System.out.println("data type-------" + data_type4);

				String lower_case = field_name4.toLowerCase();
				System.out.println("field name-------" + field_name4);
				System.out.println("mapping-------" + mapping4);
				System.out.println("data type-------" + data_type4);

				boolean mandatory = rn_userlist.get(i).isMandatory();
				boolean readonly = rn_userlist.get(i).isReadonly();
				boolean hidden = rn_userlist.get(i).isHidden();

				// String mapping=rn_userlist.get(i).getMapping();

				String type_field = rn_userlist.get(i).getType_field();

				boolean dependent = rn_userlist.get(i).isDependent();
				String dependent_on = rn_userlist.get(i).getDependent_on();
				String dependent_sp = rn_userlist.get(i).getDependent_sp();
				String dependent_sp_param = rn_userlist.get(i).getDependent_sp_param();

				boolean sequence = rn_userlist.get(i).isSequence();
				String seq_name = rn_userlist.get(i).getSeq_name();
				String seq_sp = rn_userlist.get(i).getSeq_sp();
				String seq_sp_param = rn_userlist.get(i).getSeq_sp_param();

				boolean validation_1 = rn_userlist.get(i).isValidation_1();
				String val_type = rn_userlist.get(i).getVal_type();
				String val_sp = rn_userlist.get(i).getVal_sp();
				String val_sp_param = rn_userlist.get(i).getVal_sp_param();

				boolean default_1 = rn_userlist.get(i).isDefault_1();
				String default_typename = rn_userlist.get(i).getDefault_type();
				String default_value = rn_userlist.get(i).getDefault_value();
				String default_sp = rn_userlist.get(i).getDefault_sp();
				String default_sp_param = rn_userlist.get(i).getDefault_sp_param();

				boolean calculated_field = rn_userlist.get(i).isCalculated_field();
				String cal_sp = rn_userlist.get(i).getCal_sp();
				String cal_sp_param = rn_userlist.get(i).getCal_sp_param();

				boolean add_to_grid = rn_userlist.get(i).getAdd_to_grid();

				boolean sp_for_dropdown = rn_userlist.get(i).getSp_for_dropdown();
				boolean sp_for_autocomplete = rn_userlist.get(i).getSp_for_autocomplete();

				String sp_name_for_dropdown = rn_userlist.get(i).getSp_name_for_dropdown();
				String sp_name_for_autocomplete = rn_userlist.get(i).getSp_name_for_autocomplete();

				if (i > 5 && i <= 8) {
					if (type_field.equals("textfield")) {
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
							form_readonly.append("  value=\"${" + table_name_lower + "_updt." + mapping_lower_case
									+ "}\" type=\"text\" ");
						}
						if (calculated_field == true) {
							form_readonly.append("name=\"total\" id=\"total\" class=\"stunden form-control\" ");
						} else {
							form_readonly.append("name=\"" + mapping_lower_case + "\" id=\"" + mapping_lower_case
									+ "\" class=\"col-xs-12 col-sm-4\" pattern=\"[A-Za-z]{1,30}\"");
						}

						if (mandatory == true) {
							System.out.println("-------------in loop 1 part 2 required-------------------");
							form_readonly.append("required");
						}

						System.out.println("-------------in loop 1 part 2 required-------------------");
						form_readonly.append("  readonly");

						form_readonly.append("/>\n</div>" + "\n</div>" + "\n</div>" + "\n</td>");
					}

					if (type_field.equals("autocomplete")) {
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
							form_readonly.append(" value=\"${" + table_name_lower + "_updt." + mapping_lower_case
									+ "}\"  type=\"hidden\" ");
						} else {
							form_readonly.append("  value=\"${" + table_name_lower + "_updt." + mapping_lower_case
									+ "}\" type=\"text\" ");
						}
						if (calculated_field == true) {
							form_readonly.append("name=\"total\" id=\"total\" class=\"stunden form-control\" ");
						} else {
							form_readonly.append("name=" + mapping_lower_case + " id=\"" + mapping_lower_case
									+ "\" class=\"col-xs-12 col-sm-4\" readonly");
						}

						/*
						 * if(mandatory.equals("Y")) { System.out.
						 * println("-------------in loop 1 part 2 required-------------------" );
						 * form.append("required"); }
						 */

						/*
						 * if(readonly.equals("Y")) { System.out.
						 * println("-------------in loop 1 part 2 required-------------------" );
						 * form.append("  readonly"); }
						 */
						form_readonly.append("/>\n</div>" + "\n</div>" + "\n</div>" + "\n</td>");

					}

					if (type_field.equals("dropdown")) {
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

						if (dependent == true) {

							form_readonly.append("\n<select name=\"state_name\" id=\"" + mapping4
									+ "\"  class=\"col-xs-3 col-sm-3 form-control state\">"
									+ "\n<option >---select---</option>" + "\n</select>");
						} else {

							form_readonly.append("\n<select name=\"drop_value\" id=\"" + mapping4
									+ "\"  value=\"${drop_value}\" class=\"col-xs-3 col-sm-3 form-control");
							form_readonly.append("\t" + mapping4);
							form_readonly.append("\" readonly>" + "\n<option >---select---</option>"
									+ "\n<option value=\"${drop_value}\"");

							if (mandatory == true) {
								System.out.println("-------------in loop 1 part 2 required-------------------");
								form_readonly.append("required");
							}

							if (readonly == true) {
								System.out.println("-------------in loop 1 part 2 required-------------------");
								form_readonly.append("readonly");
							}
							form_readonly.append(">${drop_value}</option>\n" + "</select>\n");
						}
						form_readonly.append("</div>" + "\n</div>" + "\n</div>" + "\n</td>");
					}

					if (type_field.equals("togglebutton")) {
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

						form_readonly.append(" value=\"${" + table_name_lower + "_updt." + field_name_lower
								+ "}\" name=" + mapping4 + " id=" + mapping4
								+ "  class=\"ace ace-switch ace-switch-6\" onblur=\"CheckUserStatus()\"");

						form_readonly.append("readonly />\n" + "\n<span class=\"lbl\"></span>"

								+ "\n</div>" + "\n</div>" + "\n</div>" + "\n</td>");
					}

					if (type_field.equals("checkbox")) {
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

						form_readonly.append(
								" value=\"${" + table_name_lower + "_updt." + field_name_lower + "}\" name=" + mapping4
										+ " id=" + mapping4 + "  onblur=\"CheckUserStatusHeader1()\" readonly");

						form_readonly.append("/>\n" + "\n</div>" + "\n</div>" + "\n</div>" + "\n</td>");
					}
				}
			}
		}
		form_readonly.append("\n</tr>");

		form_readonly.append("\n<tr>");

		if (table_name != null && !table_name.isEmpty()) {
			String table_name_lower = table_name.toLowerCase();
			String table_name_first_upper = table_name_lower.substring(0, 1).toUpperCase()
					+ table_name_lower.substring(1);
			String table_name_upper = table_name.toUpperCase();
			for (int i = 0; i < rn_userlist.size(); i++) {
				String field_name4 = rn_userlist.get(i).getMapping();
				String field_name_lower = field_name4.toLowerCase();
				String mapping4 = rn_userlist.get(i).getMapping();
				String mapping_lower_case = mapping4.toLowerCase();
				String data_type4 = rn_userlist.get(i).getDataType();

				System.out.println("field name-------" + field_name4);
				System.out.println("mapping-------" + mapping4);
				System.out.println("data type-------" + data_type4);

				System.out.println("field name-------" + field_name4);
				System.out.println("mapping-------" + mapping4);
				System.out.println("data type-------" + data_type4);

				String lower_case = field_name4.toLowerCase();
				System.out.println("field name-------" + field_name4);
				System.out.println("mapping-------" + mapping4);
				System.out.println("data type-------" + data_type4);

				boolean mandatory = rn_userlist.get(i).isMandatory();
				boolean readonly = rn_userlist.get(i).isReadonly();
				boolean hidden = rn_userlist.get(i).isHidden();

				// String mapping=rn_userlist.get(i).getMapping();

				String type_field = rn_userlist.get(i).getType_field();

				boolean dependent = rn_userlist.get(i).isDependent();
				String dependent_on = rn_userlist.get(i).getDependent_on();
				String dependent_sp = rn_userlist.get(i).getDependent_sp();
				String dependent_sp_param = rn_userlist.get(i).getDependent_sp_param();

				boolean sequence = rn_userlist.get(i).isSequence();
				String seq_name = rn_userlist.get(i).getSeq_name();
				String seq_sp = rn_userlist.get(i).getSeq_sp();
				String seq_sp_param = rn_userlist.get(i).getSeq_sp_param();

				boolean validation_1 = rn_userlist.get(i).isValidation_1();
				String val_type = rn_userlist.get(i).getVal_type();
				String val_sp = rn_userlist.get(i).getVal_sp();
				String val_sp_param = rn_userlist.get(i).getVal_sp_param();

				boolean default_1 = rn_userlist.get(i).isDefault_1();
				String default_typename = rn_userlist.get(i).getDefault_type();
				String default_value = rn_userlist.get(i).getDefault_value();
				String default_sp = rn_userlist.get(i).getDefault_sp();
				String default_sp_param = rn_userlist.get(i).getDefault_sp_param();

				boolean calculated_field = rn_userlist.get(i).isCalculated_field();
				String cal_sp = rn_userlist.get(i).getCal_sp();
				String cal_sp_param = rn_userlist.get(i).getCal_sp_param();

				boolean add_to_grid = rn_userlist.get(i).getAdd_to_grid();

				boolean sp_for_dropdown = rn_userlist.get(i).getSp_for_dropdown();
				boolean sp_for_autocomplete = rn_userlist.get(i).getSp_for_autocomplete();

				String sp_name_for_dropdown = rn_userlist.get(i).getSp_name_for_dropdown();
				String sp_name_for_autocomplete = rn_userlist.get(i).getSp_name_for_autocomplete();

				if (i > 8 && i <= 11) {
					if (type_field.equals("textfield")) {
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
							form_readonly.append("  value=\"${" + table_name_lower + "_updt." + mapping_lower_case
									+ "}\" type=\"text\" ");
						}
						if (calculated_field == true) {
							form_readonly.append("name=\"total\" id=\"total\" class=\"stunden form-control\" ");
						} else {
							form_readonly.append("name=\"" + mapping_lower_case + "\" id=\"" + mapping_lower_case
									+ "\" class=\"col-xs-12 col-sm-4\" pattern=\"[A-Za-z]{1,30}\"");
						}

						if (mandatory == true) {
							System.out.println("-------------in loop 1 part 2 required-------------------");
							form_readonly.append("required");
						}

						System.out.println("-------------in loop 1 part 2 required-------------------");
						form_readonly.append("  readonly");

						form_readonly.append("/>\n</div>" + "\n</div>" + "\n</div>" + "\n</td>");
					}

					if (type_field.equals("autocomplete")) {
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
							form_readonly.append(" value=\"${" + table_name_lower + "_updt." + mapping_lower_case
									+ "}\"  type=\"hidden\" ");
						} else {
							form_readonly.append("  value=\"${" + table_name_lower + "_updt." + mapping_lower_case
									+ "}\" type=\"text\" ");
						}
						if (calculated_field == true) {
							form_readonly.append("name=\"total\" id=\"total\" class=\"stunden form-control\" ");
						} else {
							form_readonly.append("name=" + mapping_lower_case + " id=\"" + mapping_lower_case
									+ "\" class=\"col-xs-12 col-sm-4\" readonly");
						}

						/*
						 * if(mandatory.equals("Y")) { System.out.
						 * println("-------------in loop 1 part 2 required-------------------" );
						 * form.append("required"); }
						 */

						/*
						 * if(readonly.equals("Y")) { System.out.
						 * println("-------------in loop 1 part 2 required-------------------" );
						 * form.append("  readonly"); }
						 */
						form_readonly.append("/>\n</div>" + "\n</div>" + "\n</div>" + "\n</td>");

					}

					if (type_field.equals("dropdown")) {
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

						if (dependent == true) {

							form_readonly.append("\n<select name=\"state_name\" id=\"" + mapping4
									+ "\"  class=\"col-xs-3 col-sm-3 form-control state\">"
									+ "\n<option >---select---</option>" + "\n</select>");
						} else {

							form_readonly.append("\n<select name=\"drop_value\" id=\"" + mapping4
									+ "\"  value=\"${drop_value}\" class=\"col-xs-3 col-sm-3 form-control");
							form_readonly.append("\t" + mapping4);
							form_readonly.append("\" readonly>" + "\n<option >---select---</option>"
									+ "\n<option value=\"${drop_value}\"");

							if (mandatory == true) {
								System.out.println("-------------in loop 1 part 2 required-------------------");
								form_readonly.append("required");
							}

							if (readonly == true) {
								System.out.println("-------------in loop 1 part 2 required-------------------");
								form_readonly.append("readonly");
							}
							form_readonly.append(">${drop_value}</option>\n" + "</select>\n");
						}
						form_readonly.append("</div>" + "\n</div>" + "\n</div>" + "\n</td>");
					}

					if (type_field.equals("togglebutton")) {
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

						form_readonly.append(" value=\"${" + table_name_lower + "_updt." + field_name_lower
								+ "}\" name=" + mapping4 + " id=" + mapping4
								+ "  class=\"ace ace-switch ace-switch-6\" onblur=\"CheckUserStatus()\"");

						form_readonly.append("readonly />\n" + "\n<span class=\"lbl\"></span>"

								+ "\n</div>" + "\n</div>" + "\n</div>" + "\n</td>");
					}

					if (type_field.equals("checkbox")) {
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

						form_readonly.append(
								" value=\"${" + table_name_lower + "_updt." + field_name_lower + "}\" name=" + mapping4
										+ " id=" + mapping4 + "  onblur=\"CheckUserStatusHeader1()\" readonly");

						form_readonly.append("/>\n" + "\n</div>" + "\n</div>" + "\n</div>" + "\n</td>");
					}
				}
			}

			form_readonly.append("\n</tr>\n\n</c:forEach>");

			form_readonly.append("\n</table>");

			form_readonly.append("\n<%@include file=\"/WEB-INF/tiles/acemaster/" + module_name + "/" + table_name_lower
					+ "_ext_Readonly.jsp\"%>"
//					+ "<div class=\"hr hr-dotted\"></div>"
					/*
					 * + "\n<div class=\"wizard-actions\">"
					 * +"\n<button type=\"submit\" class=\"btn btn-success center\" onclick=\"submitForms()\">"
					 * +"\nCREATE" +"\n</button>" +"\n</div> "
					 */
					+ "\n</form>");

			strContentreadonly.append(importsectionreadonly + " \n" + headsectionreadonly
					+ "\n<body>\n<div class=\"main-container\" id=\"main-container\">"
					+ "\n<div class=\"main-content\">" + "\n<div class=\"main-content-inner\">"
					+ "\n<div class=\"breadcrumbs\" id=\"breadcrumbs\">" + "\n<ul class=\"breadcrumb\">"
					+ "\n<li><i class=\"ace-icon fa fa-home home-icon\"></i> <a href=\"#\">Home</a>" + "\n</li>"

					+ "\n<li><a href=\"#\">ManageUsers</a></li>" + "\n<li class=\"active\">" + table_name_lower
					+ "</li>" + "\n</ul>" + "\n</div>"

					+ "\n<div class=\"page-content\">" + "\n<div class=\"page-header\">" + "\n<h1>" + table_name_lower
					+ "</h1>" + "\n</div>"

					+ "\n<div class=\"row\">" + "\n<div class=\"col-xs-12\">"

					+ "\n<div class=\"widget-body\">" + "\n<div class=\"widget-main\">"
					+ "\n<div id=\"fuelux-wizard-container\">" + "\n<div class=\"step-content pos-rel\">"
					+ "\n<div class=\"step-pane active\" data-step=\"1\">"
					+ "<div class=\"step-pane active\" data-step=\"1\"><div class=\"table-header\" style=\"margin-bottom: 30px; margin-top: 30px;  margin-right: 3px; width:100%;\">"
					+ "<label style=\"margin-top:2px; margin-left:5px; color: #303030; font-size:18px;\">  Section  </label></div>"
					+ form_readonly

					+ "\n<div class=\"modal-footer wizard-actions\" style=\"margin-bottom: 19px;padding-top: 30px;\">"

					+ "\n<a href= \"" + table_name_lower
					+ "_grid_view\"><button type=\"submit\"  class=\"btn btn-success btn-sm btn-back\"  >"
					+ "\n<i class=\"ace-icon fa fa-arrow-left icon-on-left\"></i> Back" + "\n</button></a>"

					+ "\n<c:forEach var=\"values\" items=\"${" + multiple_line_table_name_lower
					+ "_update}\" varStatus=\"status\">"

					+ "\n<a href= \"" + multiple_line_table_name_lower + "_readonly?id=${values.header_id}\">"
					+ "\n<button type=\"submit\"  class=\"btn btn-success btn-sm btn-next\"  >" + "  Next  "
					+ "\n<i class=\"ace-icon fa fa-arrow-right icon-on-right\"></i>" + "\n</button></a>"
					+ "\n</c:forEach>" + "\n</div>" + "\n</div>" + "\n</div>" + "\n</div>" + "\n</div>" + "\n</div>"
					+ "\n</div>" + "\n</div>" + "\n</div>" + "\n</div>" + "\n</div>" + "\n</div>" + "\n</div>"
					+ "\n</div>\n</body>\n" + line_script_update + "</html>");
		}

		// -------------------------------------jsp prefield
		// update----------------------------------------

		if (table_name != null && !table_name.isEmpty()) {
			String table_name_lower = table_name.toLowerCase();
			String table_name_first_upper = table_name_lower.substring(0, 1).toUpperCase()
					+ table_name_lower.substring(1);
			String table_name_upper = table_name.toUpperCase();

			String field_name_for_id = id_list.get(0).getMapping();
			System.out.println("field" + field_name_for_id);
			String field_name_for_id_lower = field_name_for_id.toLowerCase();
			String field_name_for_id_upper = field_name_for_id.toUpperCase();
			String field_name_first_upper = field_name_for_id_lower.substring(0, 1).toUpperCase()
					+ field_name_for_id_lower.substring(1);

			importsectionprefield.append(
					"\n<%@ page language=\"java\" contentType=\"text/html; charset=UTF-8\" pageEncoding=\"UTF-8\"%>"

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
					+ "\n<link rel=\"stylesheet\""
					+ "\nhref=\"<c:url value='/resources/assets/css/chosen.min.css' />\" />"
					+ "\n<link rel=\"stylesheet\""
					+ "\nhref=\"<c:url value='/resources/assets/css/datepicker.min.css'/>\" />"
					+ "\n<link rel=\"stylesheet\""
					+ "\nhref=\"<c:url value='/resources/assets/css/bootstrap-timepicker.min.css'/>\" />"
					+ "\n<link rel=\"stylesheet\""
					+ "\nhref=\"<c:url value='/resources/assets/css/daterangepicker.min.css' />\" />"
					+ "\n<link rel=\"stylesheet\""
					+ "\nhref=\"<c:url value='/resources/assets/css/bootstrap-datetimepicker.min.css'/>\" />"
					+ "\n<link rel=\"stylesheet\""
					+ "\nhref=\"<c:url value='/resources/assets/css/colorpicker.min.css' />\" />"
					+ "\n<!-- text fonts -->" + "\n<link rel=\"stylesheet\""
					+ "\n\nhref=\"<c:url value='/resources/assets/fonts/fonts.googleapis.com.css' />\" />"
					+ "\n<!-- ace styles -->" + "\n<link rel=\"stylesheet\""
					+ "\nhref=\"<c:url value='/resources/assets/css/ace.min.css\" class=\"ace-main-stylesheet\" id=\"main-ace-style' />\" />"
					+ "\n<!-- inline styles related to this page -->" + "\n<!-- ace settings handler -->"
					+ "\n<script src=\"<c:url value='/resources/assets/js/ace-extra.min.js'/>\""
					+ "\ntype=\"text/javascript\"></script>" + "\n<script>" + "\nsubmitForms = function()" + "\n{"
					+ "\ndocument.forms[\"userdetails1\"].submit();" + "\ndocument.forms[\"userdetails2\"].submit();"
					+ "}" + "\n</script> " + "\n<script src=\"resources/assets/js/ace-extra.min.js\">" + "</script>"
					+ "\n<script src=\"https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js\">"
					+ "</script>"
					+ "\n<script src=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.4.0/js/bootstrap.min.js\">"
					+ "</script>" + " \n</head>");

			form_id.append("\n<input   value=\"${" + table_name_lower + "_updt." + field_name_for_id_lower
					+ "}\"type=\"hidden\" name=\"" + field_name_for_id_lower + "\"id=\"" + field_name_for_id_lower
					+ "\"class=\"col-xs-12 col-sm-4\" />");

			form_prefield
					.append("\n<form action=\"" + action2 + "\" class=\"form-horizontal\" id=\"Regi\" method=\"Post\">"
							+ "\n<input type=\"hidden\" name=\"test\" id=\"test\" value=\"\" />" + " \n<table>"
							+ "\n<c:forEach var=\"" + table_name_lower + "_updt\" items=\"${" + table_name_lower
							+ "_update}\" varStatus=\"status\">"

							+ "\n<tr>" + form_id);
		}

		form_prefield.append("\n</tr>");

		form_prefield.append("\n<tr>");

		if (table_name != null && !table_name.isEmpty()) {
			String table_name_lower = table_name.toLowerCase();
			String table_name_first_upper = table_name_lower.substring(0, 1).toUpperCase()
					+ table_name_lower.substring(1);
			String table_name_upper = table_name.toUpperCase();

			for (int i = 0; i < id_notprimary.size(); i++) {

				String field_name = id_notprimary.get(i).getMapping();
				String data_type = id_notprimary.get(i).getDataType();
				String lower_case = field_name.toLowerCase();
				String only_upper = field_name.toUpperCase();
				String first_upper = lower_case.substring(0, 1).toUpperCase() + lower_case.substring(1);

				if (data_type.equals("int")) {
					form_prefield.append("\n<td>"
							+ "\n<div class=\"form-group\" style=\"margin-left: 10%; margin-right: 10%;\">"
							+ "\n<label class=\"control-label col-xs-12 col-sm-3 no-padding-right\">" + first_upper

							+ "\n</label>" + "\n<div class=\"col-xs-12 col-sm-9\">" + "\n<div class=\"clearfix\">"
							+ "\n<input   value=\"${" + table_name_lower + "_updt." + lower_case
							+ "}\"type=\"text\" name=\"" + lower_case + "\"id=\"" + lower_case
							+ "\"class=\"col-xs-12 col-sm-4\" />" + "\n</div>" + "\n</div>" + "\n</div>" + "\n</td>");
				}

			}
		}
		form_prefield.append("\n</tr>");

		form_prefield.append("\n<tr>");

		if (table_name != null && !table_name.isEmpty()) {
			String table_name_lower = table_name.toLowerCase();
			String table_name_first_upper = table_name_lower.substring(0, 1).toUpperCase()
					+ table_name_lower.substring(1);
			String table_name_upper = table_name.toUpperCase();

			for (int i = 0; i < datetime_list.size(); i++) {

				String field_name = datetime_list.get(i).getMapping();
				String data_type = datetime_list.get(i).getDataType();
				String lower_case = field_name.toLowerCase();
				String only_upper = field_name.toUpperCase();
				String first_upper = lower_case.substring(0, 1).toUpperCase() + lower_case.substring(1);

				boolean mandatory = datetime_list.get(i).isMandatory();
				boolean readonly = datetime_list.get(i).isReadonly();
				boolean hidden = datetime_list.get(i).isHidden();

				String mapping = datetime_list.get(i).getMapping();
				String mapping_lower = mapping.toLowerCase();

				boolean dependent = datetime_list.get(i).isDependent();
				String dependent_on = datetime_list.get(i).getDependent_on();
				String dependent_sp = datetime_list.get(i).getDependent_sp();
				String dependent_sp_param = datetime_list.get(i).getDependent_sp_param();

				boolean sequence = datetime_list.get(i).isSequence();
				String seq_name = datetime_list.get(i).getSeq_name();
				String seq_sp = datetime_list.get(i).getSeq_sp();
				String seq_sp_param = datetime_list.get(i).getSeq_sp_param();

				boolean validation_1 = datetime_list.get(i).isValidation_1();
				String val_type = datetime_list.get(i).getVal_type();
				String val_sp = datetime_list.get(i).getVal_sp();
				String val_sp_param = datetime_list.get(i).getVal_sp_param();

				boolean default_1 = datetime_list.get(i).isDefault_1();
				String default_typename = datetime_list.get(i).getDefault_type();
				String default_value = datetime_list.get(i).getDefault_value();
				String default_sp = datetime_list.get(i).getDefault_sp();
				String default_sp_param = datetime_list.get(i).getDefault_sp_param();

				boolean calculated_field = datetime_list.get(i).isCalculated_field();
				String cal_sp = datetime_list.get(i).getCal_sp();
				String cal_sp_param = datetime_list.get(i).getCal_sp_param();

				boolean add_to_grid = datetime_list.get(i).getAdd_to_grid();

				boolean sp_for_dropdown = datetime_list.get(i).getSp_for_dropdown();
				boolean sp_for_autocomplete = datetime_list.get(i).getSp_for_autocomplete();

				String sp_name_for_dropdown = datetime_list.get(i).getSp_name_for_dropdown();
				String sp_name_for_autocomplete = datetime_list.get(i).getSp_name_for_autocomplete();

				System.out.println("-----value of mantadoryr laest-----" + mandatory);

				if (data_type.equals("datetime")) {

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

					form_prefield.append("name=" + mapping_lower + "  value=\"${" + table_name_lower + "_updt."
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

		}

		form_prefield.append("\n</tr>");

		form_prefield.append("\n<tr>");

		if (table_name != null && !table_name.isEmpty()) {
			String table_name_lower = table_name.toLowerCase();
			String table_name_first_upper = table_name_lower.substring(0, 1).toUpperCase()
					+ table_name_lower.substring(1);
			String table_name_upper = table_name.toUpperCase();
			for (int i = 0; i < rn_userlist.size(); i++) {
				String field_name4 = rn_userlist.get(i).getMapping();
				String field_name_lower = field_name4.toLowerCase();
				String mapping4 = rn_userlist.get(i).getMapping();
				String mapping_lower_case = mapping4.toLowerCase();
				String data_type4 = rn_userlist.get(i).getDataType();

				System.out.println("field name-------" + field_name4);
				System.out.println("mapping-------" + mapping4);
				System.out.println("data type-------" + data_type4);

				String lower_case = field_name4.toLowerCase();
				System.out.println("field name-------" + field_name4);
				System.out.println("mapping-------" + mapping4);
				System.out.println("data type-------" + data_type4);

				boolean mandatory = rn_userlist.get(i).isMandatory();
				boolean readonly = rn_userlist.get(i).isReadonly();
				boolean hidden = rn_userlist.get(i).isHidden();

				// String mapping=rn_userlist.get(i).getMapping();

				String type_field = rn_userlist.get(i).getType_field();

				boolean dependent = rn_userlist.get(i).isDependent();
				String dependent_on = rn_userlist.get(i).getDependent_on();
				String dependent_sp = rn_userlist.get(i).getDependent_sp();
				String dependent_sp_param = rn_userlist.get(i).getDependent_sp_param();

				boolean sequence = rn_userlist.get(i).isSequence();
				String seq_name = rn_userlist.get(i).getSeq_name();
				String seq_sp = rn_userlist.get(i).getSeq_sp();
				String seq_sp_param = rn_userlist.get(i).getSeq_sp_param();

				boolean validation_1 = rn_userlist.get(i).isValidation_1();
				String val_type = rn_userlist.get(i).getVal_type();
				String val_sp = rn_userlist.get(i).getVal_sp();
				String val_sp_param = rn_userlist.get(i).getVal_sp_param();

				boolean default_1 = rn_userlist.get(i).isDefault_1();
				String default_typename = rn_userlist.get(i).getDefault_type();
				String default_value = rn_userlist.get(i).getDefault_value();
				String default_sp = rn_userlist.get(i).getDefault_sp();
				String default_sp_param = rn_userlist.get(i).getDefault_sp_param();

				boolean calculated_field = rn_userlist.get(i).isCalculated_field();
				String cal_sp = rn_userlist.get(i).getCal_sp();
				String cal_sp_param = rn_userlist.get(i).getCal_sp_param();

				boolean add_to_grid = rn_userlist.get(i).getAdd_to_grid();

				boolean sp_for_dropdown = rn_userlist.get(i).getSp_for_dropdown();
				boolean sp_for_autocomplete = rn_userlist.get(i).getSp_for_autocomplete();

				String sp_name_for_dropdown = rn_userlist.get(i).getSp_name_for_dropdown();
				String sp_name_for_autocomplete = rn_userlist.get(i).getSp_name_for_autocomplete();

				if (i <= 2) {
					if (type_field.equals("textfield")) {
						form_prefield.append(
								"\n<td>" + "\n<div class=\"form-group\" style=\"margin-left: 10%; margin-right: 10%;\">"
										+ "\n<label class=\"control-label col-xs-12 col-sm-3 no-padding-right\">"
										+ "\n " + field_name4);

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
							form_prefield.append("  value=\"${" + table_name_lower + "_updt." + mapping_lower_case
									+ "}\" type=\"text\" ");
						}
						if (calculated_field == true) {
							form_prefield.append("name=\"total\" id=\"total\" class=\"stunden form-control\" ");
						} else {
							form_prefield.append("name=\"" + mapping_lower_case + "\" id=\"" + mapping_lower_case
									+ "\" class=\"col-xs-12 col-sm-4\" pattern=\"[A-Za-z]{1,30}\"");
						}

						if (mandatory == true) {
							System.out.println("-------------in loop 1 part 2 required-------------------");
							form_prefield.append("required");
						}

						if (readonly == true) {
							System.out.println("-------------in loop 1 part 2 required-------------------");
							form_prefield.append("  readonly");
						}
						form_prefield.append("/>\n</div>" + "\n</div>" + "\n</div>" + "\n</td>");
					}

					if (type_field.equals("autocomplete")) {
						form_prefield.append(
								"\n<td>" + "\n<div class=\"form-group\" style=\"margin-left: 10%; margin-right: 10%;\">"
										+ "\n<label class=\"control-label col-xs-12 col-sm-3 no-padding-right\">"
										+ "\n " + field_name4);

						System.out.println("-------------mandatory for testing=------------------" + mandatory);

						if (mandatory == true) {
							System.out.println("-------------in loop 1-------------------");
							form_prefield.append("\n <i class=\"menu-icon fa red\"> *</i>");
						}

						form_prefield.append(
								"\n</label>" + "\n<div class=\"col-xs-12 col-sm-9\">" + "\n<div class=\"clearfix\">");

						form_prefield.append("\n<input");

						if (hidden == true) {
							form_prefield.append(" value=\"${" + table_name_lower + "_updt." + mapping_lower_case
									+ "}\"  type=\"hidden\" ");
						} else {
							form_prefield.append("  value=\"${" + table_name_lower + "_updt." + mapping_lower_case
									+ "}\" type=\"text\" ");
						}
						if (calculated_field == true) {
							form_prefield.append("name=\"total\" id=\"total\" class=\"stunden form-control\" ");
						} else {
							form_prefield.append("name=" + mapping_lower_case + " id=\"" + mapping_lower_case
									+ "\" class=\"col-xs-12 col-sm-4\" ");
						}

						/*
						 * if(mandatory.equals("Y")) { System.out.
						 * println("-------------in loop 1 part 2 required-------------------" );
						 * form.append("required"); }
						 */

						/*
						 * if(readonly.equals("Y")) { System.out.
						 * println("-------------in loop 1 part 2 required-------------------" );
						 * form.append("  readonly"); }
						 */
						form_prefield.append("/>\n</div>" + "\n</div>" + "\n</div>" + "\n</td>");

					}

					if (type_field.equals("dropdown")) {
						form_prefield.append(
								"\n<td>" + "\n<div class=\"form-group\" style=\"margin-left: 10%; margin-right: 10%;\">"
										+ "\n<label class=\"control-label col-xs-12 col-sm-3 no-padding-right\">"
										+ "\n " + field_name4);

						System.out.println("-------------mandatory for testing=------------------" + mandatory);

						if (mandatory == true) {
							System.out.println("-------------in loop 1-------------------");
							form_prefield.append("\n <i class=\"menu-icon fa red\"> *</i>");
						}

						form_prefield.append(
								"\n</label>" + "\n<div class=\"col-xs-12 col-sm-9\">" + "\n<div class=\"clearfix\">");

						if (dependent == true) {

							form_prefield.append("\n<select name=\"state_name\" id=\"" + mapping4
									+ "\"  class=\"col-xs-3 col-sm-3 form-control state\">"
									+ "\n<option >---select---</option>" + "\n</select>");
						} else {

							form_prefield.append("\n<select name=\"drop_value\" id=\"" + mapping4
									+ "\"  value=\"${drop_value}\" class=\"col-xs-3 col-sm-3 form-control");
							form_prefield.append("\t" + mapping4);
							form_prefield.append(
									"\" >" + "\n<option >---select---</option>" + "\n<option value=\"${drop_value}\"");

							if (mandatory == true) {
								System.out.println("-------------in loop 1 part 2 required-------------------");
								form_prefield.append("required");
							}

							if (readonly == true) {
								System.out.println("-------------in loop 1 part 2 required-------------------");
								form_prefield.append("readonly");
							}
							form_prefield.append(">${drop_value}</option>\n" + "</select>\n");
						}
						form_prefield.append("</div>" + "\n</div>" + "\n</div>" + "\n</td>");
					}

					if (type_field.equals("togglebutton")) {
						form_prefield.append(
								"\n<td>" + "\n<div class=\"form-group\" style=\"margin-left: 10%; margin-right: 10%;\">"
										+ "\n<label class=\"control-label col-xs-12 col-sm-3 no-padding-right\">"
										+ "\n " + field_name4);

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

						form_prefield.append(" value=\"${" + table_name_lower + "_updt." + field_name_lower
								+ "}\" name=" + mapping4 + " id=" + mapping4
								+ "  class=\"ace ace-switch ace-switch-6\" onblur=\"CheckUserStatus()\"");

						if (mandatory == true) {
							System.out.println("-------------in loop 1 part 2 required-------------------");
							form_prefield.append("required");
						}

						form_prefield.append("/>\n" + "\n<span class=\"lbl\"></span>"

								+ "\n</div>" + "\n</div>" + "\n</div>" + "\n</td>");
					}

					if (type_field.equals("checkbox")) {
						form_prefield.append(
								"\n<td>" + "\n<div class=\"form-group\" style=\"margin-left: 10%; margin-right: 10%;\">"
										+ "\n<label class=\"control-label col-xs-12 col-sm-3 no-padding-right\">"
										+ "\n " + field_name4);

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

						form_prefield.append(" value=\"${" + table_name_lower + "_updt." + field_name_lower
								+ "}\" name=" + mapping4 + " id=" + mapping4 + "  onblur=\"CheckUserStatusHeader1()\"");

						if (mandatory == true) {
							System.out.println("-------------in loop 1 part 2 required-------------------");
							form_prefield.append("required");
						}

						form_prefield.append("/>\n" + "\n</div>" + "\n</div>" + "\n</div>" + "\n</td>");
					}
				}
			}
		}

		form_prefield.append("\n</tr>");

		form_prefield.append("\n<tr>");

		if (table_name != null && !table_name.isEmpty()) {
			String table_name_lower = table_name.toLowerCase();
			String table_name_first_upper = table_name_lower.substring(0, 1).toUpperCase()
					+ table_name_lower.substring(1);
			String table_name_upper = table_name.toUpperCase();
			for (int i = 0; i < rn_userlist.size(); i++) {
				String field_name4 = rn_userlist.get(i).getMapping();
				String field_name_lower = field_name4.toLowerCase();
				String mapping4 = rn_userlist.get(i).getMapping();
				String mapping_lower_case = mapping4.toLowerCase();
				String data_type4 = rn_userlist.get(i).getDataType();

				System.out.println("field name-------" + field_name4);
				System.out.println("mapping-------" + mapping4);
				System.out.println("data type-------" + data_type4);

				String lower_case = field_name4.toLowerCase();
				System.out.println("field name-------" + field_name4);
				System.out.println("mapping-------" + mapping4);
				System.out.println("data type-------" + data_type4);

				boolean mandatory = rn_userlist.get(i).isMandatory();
				boolean readonly = rn_userlist.get(i).isReadonly();
				boolean hidden = rn_userlist.get(i).isHidden();

				// String mapping=rn_userlist.get(i).getMapping();

				String type_field = rn_userlist.get(i).getType_field();

				boolean dependent = rn_userlist.get(i).isDependent();
				String dependent_on = rn_userlist.get(i).getDependent_on();
				String dependent_sp = rn_userlist.get(i).getDependent_sp();
				String dependent_sp_param = rn_userlist.get(i).getDependent_sp_param();

				boolean sequence = rn_userlist.get(i).isSequence();
				String seq_name = rn_userlist.get(i).getSeq_name();
				String seq_sp = rn_userlist.get(i).getSeq_sp();
				String seq_sp_param = rn_userlist.get(i).getSeq_sp_param();

				boolean validation_1 = rn_userlist.get(i).isValidation_1();
				String val_type = rn_userlist.get(i).getVal_type();
				String val_sp = rn_userlist.get(i).getVal_sp();
				String val_sp_param = rn_userlist.get(i).getVal_sp_param();

				boolean default_1 = rn_userlist.get(i).isDefault_1();
				String default_typename = rn_userlist.get(i).getDefault_type();
				String default_value = rn_userlist.get(i).getDefault_value();
				String default_sp = rn_userlist.get(i).getDefault_sp();
				String default_sp_param = rn_userlist.get(i).getDefault_sp_param();

				boolean calculated_field = rn_userlist.get(i).isCalculated_field();
				String cal_sp = rn_userlist.get(i).getCal_sp();
				String cal_sp_param = rn_userlist.get(i).getCal_sp_param();

				boolean add_to_grid = rn_userlist.get(i).getAdd_to_grid();

				boolean sp_for_dropdown = rn_userlist.get(i).getSp_for_dropdown();
				boolean sp_for_autocomplete = rn_userlist.get(i).getSp_for_autocomplete();

				String sp_name_for_dropdown = rn_userlist.get(i).getSp_name_for_dropdown();
				String sp_name_for_autocomplete = rn_userlist.get(i).getSp_name_for_autocomplete();

				if (i > 2 && i <= 5) {
					if (type_field.equals("textfield")) {
						form_prefield.append(
								"\n<td>" + "\n<div class=\"form-group\" style=\"margin-left: 10%; margin-right: 10%;\">"
										+ "\n<label class=\"control-label col-xs-12 col-sm-3 no-padding-right\">"
										+ "\n " + field_name4);

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
							form_prefield.append("  value=\"${" + table_name_lower + "_updt." + mapping_lower_case
									+ "}\" type=\"text\" ");
						}
						if (calculated_field == true) {
							form_prefield.append("name=\"total\" id=\"total\" class=\"stunden form-control\" ");
						} else {
							form_prefield.append("name=\"" + mapping_lower_case + "\" id=\"" + mapping_lower_case
									+ "\" class=\"col-xs-12 col-sm-4\" pattern=\"[A-Za-z]{1,30}\"");
						}

						if (mandatory == true) {
							System.out.println("-------------in loop 1 part 2 required-------------------");
							form_prefield.append("required");
						}

						if (readonly == true) {
							System.out.println("-------------in loop 1 part 2 required-------------------");
							form_prefield.append("  readonly");
						}
						form_prefield.append("/>\n</div>" + "\n</div>" + "\n</div>" + "\n</td>");
					}

					if (type_field.equals("autocomplete")) {
						form_prefield.append(
								"\n<td>" + "\n<div class=\"form-group\" style=\"margin-left: 10%; margin-right: 10%;\">"
										+ "\n<label class=\"control-label col-xs-12 col-sm-3 no-padding-right\">"
										+ "\n " + field_name4);

						System.out.println("-------------mandatory for testing=------------------" + mandatory);

						if (mandatory == true) {
							System.out.println("-------------in loop 1-------------------");
							form_prefield.append("\n <i class=\"menu-icon fa red\"> *</i>");
						}

						form_prefield.append(
								"\n</label>" + "\n<div class=\"col-xs-12 col-sm-9\">" + "\n<div class=\"clearfix\">");

						form_prefield.append("\n<input");

						if (hidden == true) {
							form_prefield.append(" value=\"${" + table_name_lower + "_updt." + mapping_lower_case
									+ "}\"  type=\"hidden\" ");
						} else {
							form_prefield.append("  value=\"${" + table_name_lower + "_updt." + mapping_lower_case
									+ "}\" type=\"text\" ");
						}
						if (calculated_field == true) {
							form_prefield.append("name=\"total\" id=\"total\" class=\"stunden form-control\" ");
						} else {
							form_prefield.append("name=" + mapping_lower_case + " id=\"" + mapping_lower_case
									+ "\" class=\"col-xs-12 col-sm-4\" ");
						}

						/*
						 * if(mandatory.equals("Y")) { System.out.
						 * println("-------------in loop 1 part 2 required-------------------" );
						 * form.append("required"); }
						 */

						/*
						 * if(readonly.equals("Y")) { System.out.
						 * println("-------------in loop 1 part 2 required-------------------" );
						 * form.append("  readonly"); }
						 */
						form_prefield.append("/>\n</div>" + "\n</div>" + "\n</div>" + "\n</td>");

					}

					if (type_field.equals("dropdown")) {
						form_prefield.append(
								"\n<td>" + "\n<div class=\"form-group\" style=\"margin-left: 10%; margin-right: 10%;\">"
										+ "\n<label class=\"control-label col-xs-12 col-sm-3 no-padding-right\">"
										+ "\n " + field_name4);

						System.out.println("-------------mandatory for testing=------------------" + mandatory);

						if (mandatory == true) {
							System.out.println("-------------in loop 1-------------------");
							form_prefield.append("\n <i class=\"menu-icon fa red\"> *</i>");
						}

						form_prefield.append(
								"\n</label>" + "\n<div class=\"col-xs-12 col-sm-9\">" + "\n<div class=\"clearfix\">");

						if (dependent == true) {

							form_prefield.append("\n<select name=\"state_name\" id=\"" + mapping4
									+ "\"  class=\"col-xs-3 col-sm-3 form-control state\">"
									+ "\n<option >---select---</option>" + "\n</select>");
						} else {

							form_prefield.append("\n<select name=\"drop_value\" id=\"" + mapping4
									+ "\"  value=\"${drop_value}\" class=\"col-xs-3 col-sm-3 form-control");
							form_prefield.append("\t" + mapping4);
							form_prefield.append(
									"\" >" + "\n<option >---select---</option>" + "\n<option value=\"${drop_value}\"");

							if (mandatory == true) {
								System.out.println("-------------in loop 1 part 2 required-------------------");
								form_prefield.append("required");
							}

							if (readonly == true) {
								System.out.println("-------------in loop 1 part 2 required-------------------");
								form_prefield.append("readonly");
							}
							form_prefield.append(">${drop_value}</option>\n" + "</select>\n");
						}
						form_prefield.append("</div>" + "\n</div>" + "\n</div>" + "\n</td>");
					}

					if (type_field.equals("togglebutton")) {
						form_prefield.append(
								"\n<td>" + "\n<div class=\"form-group\" style=\"margin-left: 10%; margin-right: 10%;\">"
										+ "\n<label class=\"control-label col-xs-12 col-sm-3 no-padding-right\">"
										+ "\n " + field_name4);

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

						form_prefield.append(" value=\"${" + table_name_lower + "_updt." + field_name_lower
								+ "}\" name=" + mapping4 + " id=" + mapping4
								+ "  class=\"ace ace-switch ace-switch-6\" onblur=\"CheckUserStatus()\"");

						if (mandatory == true) {
							System.out.println("-------------in loop 1 part 2 required-------------------");
							form_prefield.append("required");
						}

						form_prefield.append("/>\n" + "\n<span class=\"lbl\"></span>"

								+ "\n</div>" + "\n</div>" + "\n</div>" + "\n</td>");
					}

					if (type_field.equals("checkbox")) {
						form_prefield.append(
								"\n<td>" + "\n<div class=\"form-group\" style=\"margin-left: 10%; margin-right: 10%;\">"
										+ "\n<label class=\"control-label col-xs-12 col-sm-3 no-padding-right\">"
										+ "\n " + field_name4);

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

						form_prefield.append(" value=\"${" + table_name_lower + "_updt." + field_name_lower
								+ "}\" name=" + mapping4 + " id=" + mapping4 + "  onblur=\"CheckUserStatusHeader1()\"");

						if (mandatory == true) {
							System.out.println("-------------in loop 1 part 2 required-------------------");
							form_prefield.append("required");
						}

						form_prefield.append("/>\n" + "\n</div>" + "\n</div>" + "\n</div>" + "\n</td>");
					}
				}
			}
		}

		form_prefield.append("\n</tr>");

		form_prefield.append("\n<tr>");

		if (table_name != null && !table_name.isEmpty()) {
			String table_name_lower = table_name.toLowerCase();
			String table_name_first_upper = table_name_lower.substring(0, 1).toUpperCase()
					+ table_name_lower.substring(1);
			String table_name_upper = table_name.toUpperCase();

			for (int i = 0; i < rn_userlist.size(); i++) {
				String field_name4 = rn_userlist.get(i).getMapping();
				String field_name_lower = field_name4.toLowerCase();
				String mapping4 = rn_userlist.get(i).getMapping();
				String mapping_lower_case = mapping4.toLowerCase();
				String data_type4 = rn_userlist.get(i).getDataType();

				System.out.println("field name-------" + field_name4);
				System.out.println("mapping-------" + mapping4);
				System.out.println("data type-------" + data_type4);

				String lower_case = field_name4.toLowerCase();
				System.out.println("field name-------" + field_name4);
				System.out.println("mapping-------" + mapping4);
				System.out.println("data type-------" + data_type4);

				boolean mandatory = rn_userlist.get(i).isMandatory();
				boolean readonly = rn_userlist.get(i).isReadonly();
				boolean hidden = rn_userlist.get(i).isHidden();

				// String mapping=rn_userlist.get(i).getMapping();

				String type_field = rn_userlist.get(i).getType_field();

				boolean dependent = rn_userlist.get(i).isDependent();
				String dependent_on = rn_userlist.get(i).getDependent_on();
				String dependent_sp = rn_userlist.get(i).getDependent_sp();
				String dependent_sp_param = rn_userlist.get(i).getDependent_sp_param();

				boolean sequence = rn_userlist.get(i).isSequence();
				String seq_name = rn_userlist.get(i).getSeq_name();
				String seq_sp = rn_userlist.get(i).getSeq_sp();
				String seq_sp_param = rn_userlist.get(i).getSeq_sp_param();

				boolean validation_1 = rn_userlist.get(i).isValidation_1();
				String val_type = rn_userlist.get(i).getVal_type();
				String val_sp = rn_userlist.get(i).getVal_sp();
				String val_sp_param = rn_userlist.get(i).getVal_sp_param();

				boolean default_1 = rn_userlist.get(i).isDefault_1();
				String default_typename = rn_userlist.get(i).getDefault_type();
				String default_value = rn_userlist.get(i).getDefault_value();
				String default_sp = rn_userlist.get(i).getDefault_sp();
				String default_sp_param = rn_userlist.get(i).getDefault_sp_param();

				boolean calculated_field = rn_userlist.get(i).isCalculated_field();
				String cal_sp = rn_userlist.get(i).getCal_sp();
				String cal_sp_param = rn_userlist.get(i).getCal_sp_param();

				boolean add_to_grid = rn_userlist.get(i).getAdd_to_grid();

				boolean sp_for_dropdown = rn_userlist.get(i).getSp_for_dropdown();
				boolean sp_for_autocomplete = rn_userlist.get(i).getSp_for_autocomplete();

				String sp_name_for_dropdown = rn_userlist.get(i).getSp_name_for_dropdown();
				String sp_name_for_autocomplete = rn_userlist.get(i).getSp_name_for_autocomplete();

				if (i > 5 && i <= 8) {
					if (type_field.equals("textfield")) {
						form_prefield.append(
								"\n<td>" + "\n<div class=\"form-group\" style=\"margin-left: 10%; margin-right: 10%;\">"
										+ "\n<label class=\"control-label col-xs-12 col-sm-3 no-padding-right\">"
										+ "\n " + field_name4);

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
							form_prefield.append("  value=\"${" + table_name_lower + "_updt." + mapping_lower_case
									+ "}\" type=\"text\" ");
						}
						if (calculated_field == true) {
							form_prefield.append("name=\"total\" id=\"total\" class=\"stunden form-control\" ");
						} else {
							form_prefield.append("name=\"" + mapping_lower_case + "\" id=\"" + mapping_lower_case
									+ "\" class=\"col-xs-12 col-sm-4\" pattern=\"[A-Za-z]{1,30}\"");
						}

						if (mandatory == true) {
							System.out.println("-------------in loop 1 part 2 required-------------------");
							form_prefield.append("required");
						}

						if (readonly == true) {
							System.out.println("-------------in loop 1 part 2 required-------------------");
							form_prefield.append("  readonly");
						}
						form_prefield.append("/>\n</div>" + "\n</div>" + "\n</div>" + "\n</td>");
					}

					if (type_field.equals("autocomplete")) {
						form_prefield.append(
								"\n<td>" + "\n<div class=\"form-group\" style=\"margin-left: 10%; margin-right: 10%;\">"
										+ "\n<label class=\"control-label col-xs-12 col-sm-3 no-padding-right\">"
										+ "\n " + field_name4);

						System.out.println("-------------mandatory for testing=------------------" + mandatory);

						if (mandatory == true) {
							System.out.println("-------------in loop 1-------------------");
							form_prefield.append("\n <i class=\"menu-icon fa red\"> *</i>");
						}

						form_prefield.append(
								"\n</label>" + "\n<div class=\"col-xs-12 col-sm-9\">" + "\n<div class=\"clearfix\">");

						form_prefield.append("\n<input");

						if (hidden == true) {
							form_prefield.append(" value=\"${" + table_name_lower + "_updt." + mapping_lower_case
									+ "}\"  type=\"hidden\" ");
						} else {
							form_prefield.append("  value=\"${" + table_name_lower + "_updt." + mapping_lower_case
									+ "}\" type=\"text\" ");
						}
						if (calculated_field == true) {
							form_prefield.append("name=\"total\" id=\"total\" class=\"stunden form-control\" ");
						} else {
							form_prefield.append("name=" + mapping_lower_case + " id=\"" + mapping_lower_case
									+ "\" class=\"col-xs-12 col-sm-4\" ");
						}

						/*
						 * if(mandatory.equals("Y")) { System.out.
						 * println("-------------in loop 1 part 2 required-------------------" );
						 * form.append("required"); }
						 */

						/*
						 * if(readonly.equals("Y")) { System.out.
						 * println("-------------in loop 1 part 2 required-------------------" );
						 * form.append("  readonly"); }
						 */
						form_prefield.append("/>\n</div>" + "\n</div>" + "\n</div>" + "\n</td>");

					}

					if (type_field.equals("dropdown")) {
						form_prefield.append(
								"\n<td>" + "\n<div class=\"form-group\" style=\"margin-left: 10%; margin-right: 10%;\">"
										+ "\n<label class=\"control-label col-xs-12 col-sm-3 no-padding-right\">"
										+ "\n " + field_name4);

						System.out.println("-------------mandatory for testing=------------------" + mandatory);

						if (mandatory == true) {
							System.out.println("-------------in loop 1-------------------");
							form_prefield.append("\n <i class=\"menu-icon fa red\"> *</i>");
						}

						form_prefield.append(
								"\n</label>" + "\n<div class=\"col-xs-12 col-sm-9\">" + "\n<div class=\"clearfix\">");

						if (dependent == true) {

							form_prefield.append("\n<select name=\"state_name\" id=\"" + mapping4
									+ "\"  class=\"col-xs-3 col-sm-3 form-control state\">"
									+ "\n<option >---select---</option>" + "\n</select>");
						} else {

							form_prefield.append("\n<select name=\"drop_value\" id=\"" + mapping4
									+ "\"  value=\"${drop_value}\" class=\"col-xs-3 col-sm-3 form-control");
							form_prefield.append("\t" + mapping4);
							form_prefield.append(
									"\" >" + "\n<option >---select---</option>" + "\n<option value=\"${drop_value}\"");

							if (mandatory == true) {
								System.out.println("-------------in loop 1 part 2 required-------------------");
								form_prefield.append("required");
							}

							if (readonly == true) {
								System.out.println("-------------in loop 1 part 2 required-------------------");
								form_prefield.append("readonly");
							}
							form_prefield.append(">${drop_value}</option>\n" + "</select>\n");
						}
						form_prefield.append("</div>" + "\n</div>" + "\n</div>" + "\n</td>");
					}

					if (type_field.equals("togglebutton")) {
						form_prefield.append(
								"\n<td>" + "\n<div class=\"form-group\" style=\"margin-left: 10%; margin-right: 10%;\">"
										+ "\n<label class=\"control-label col-xs-12 col-sm-3 no-padding-right\">"
										+ "\n " + field_name4);

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

						form_prefield.append(" value=\"${" + table_name_lower + "_updt." + field_name_lower
								+ "}\" name=" + mapping4 + " id=" + mapping4
								+ "  class=\"ace ace-switch ace-switch-6\" onblur=\"CheckUserStatus()\"");

						if (mandatory == true) {
							System.out.println("-------------in loop 1 part 2 required-------------------");
							form_prefield.append("required");
						}

						form_prefield.append("/>\n" + "\n<span class=\"lbl\"></span>"

								+ "\n</div>" + "\n</div>" + "\n</div>" + "\n</td>");
					}

					if (type_field.equals("checkbox")) {
						form_prefield.append(
								"\n<td>" + "\n<div class=\"form-group\" style=\"margin-left: 10%; margin-right: 10%;\">"
										+ "\n<label class=\"control-label col-xs-12 col-sm-3 no-padding-right\">"
										+ "\n " + field_name4);

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

						form_prefield.append(" value=\"${" + table_name_lower + "_updt." + field_name_lower
								+ "}\" name=" + mapping4 + " id=" + mapping4 + "  onblur=\"CheckUserStatusHeader1()\"");

						if (mandatory == true) {
							System.out.println("-------------in loop 1 part 2 required-------------------");
							form_prefield.append("required");
						}

						form_prefield.append("/>\n" + "\n</div>" + "\n</div>" + "\n</div>" + "\n</td>");
					}
				}
			}
		}
		form_prefield.append("\n</tr>");

		form_prefield.append("\n<tr>");

		if (table_name != null && !table_name.isEmpty()) {
			String table_name_lower = table_name.toLowerCase();
			String table_name_first_upper = table_name_lower.substring(0, 1).toUpperCase()
					+ table_name_lower.substring(1);
			String table_name_upper = table_name.toUpperCase();
			for (int i = 0; i < rn_userlist.size(); i++) {
				String field_name4 = rn_userlist.get(i).getMapping();
				String field_name_lower = field_name4.toLowerCase();
				String mapping4 = rn_userlist.get(i).getMapping();
				String mapping_lower_case = mapping4.toLowerCase();
				String data_type4 = rn_userlist.get(i).getDataType();

				System.out.println("field name-------" + field_name4);
				System.out.println("mapping-------" + mapping4);
				System.out.println("data type-------" + data_type4);

				String lower_case = field_name4.toLowerCase();
				System.out.println("field name-------" + field_name4);
				System.out.println("mapping-------" + mapping4);
				System.out.println("data type-------" + data_type4);

				boolean mandatory = rn_userlist.get(i).isMandatory();
				boolean readonly = rn_userlist.get(i).isReadonly();
				boolean hidden = rn_userlist.get(i).isHidden();

				// String mapping=rn_userlist.get(i).getMapping();

				String type_field = rn_userlist.get(i).getType_field();

				boolean dependent = rn_userlist.get(i).isDependent();
				String dependent_on = rn_userlist.get(i).getDependent_on();
				String dependent_sp = rn_userlist.get(i).getDependent_sp();
				String dependent_sp_param = rn_userlist.get(i).getDependent_sp_param();

				boolean sequence = rn_userlist.get(i).isSequence();
				String seq_name = rn_userlist.get(i).getSeq_name();
				String seq_sp = rn_userlist.get(i).getSeq_sp();
				String seq_sp_param = rn_userlist.get(i).getSeq_sp_param();

				boolean validation_1 = rn_userlist.get(i).isValidation_1();
				String val_type = rn_userlist.get(i).getVal_type();
				String val_sp = rn_userlist.get(i).getVal_sp();
				String val_sp_param = rn_userlist.get(i).getVal_sp_param();

				boolean default_1 = rn_userlist.get(i).isDefault_1();
				String default_typename = rn_userlist.get(i).getDefault_type();
				String default_value = rn_userlist.get(i).getDefault_value();
				String default_sp = rn_userlist.get(i).getDefault_sp();
				String default_sp_param = rn_userlist.get(i).getDefault_sp_param();

				boolean calculated_field = rn_userlist.get(i).isCalculated_field();
				String cal_sp = rn_userlist.get(i).getCal_sp();
				String cal_sp_param = rn_userlist.get(i).getCal_sp_param();

				boolean add_to_grid = rn_userlist.get(i).getAdd_to_grid();

				boolean sp_for_dropdown = rn_userlist.get(i).getSp_for_dropdown();
				boolean sp_for_autocomplete = rn_userlist.get(i).getSp_for_autocomplete();

				String sp_name_for_dropdown = rn_userlist.get(i).getSp_name_for_dropdown();
				String sp_name_for_autocomplete = rn_userlist.get(i).getSp_name_for_autocomplete();

				if (i > 8 && i <= 11) {
					if (type_field.equals("textfield")) {
						form_prefield.append(
								"\n<td>" + "\n<div class=\"form-group\" style=\"margin-left: 10%; margin-right: 10%;\">"
										+ "\n<label class=\"control-label col-xs-12 col-sm-3 no-padding-right\">"
										+ "\n " + field_name4);

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
							form_prefield.append("  value=\"${" + table_name_lower + "_updt." + mapping_lower_case
									+ "}\" type=\"text\" ");
						}
						if (calculated_field == true) {
							form_prefield.append("name=\"total\" id=\"total\" class=\"stunden form-control\" ");
						} else {
							form_prefield.append("name=\"" + mapping_lower_case + "\" id=\"" + mapping_lower_case
									+ "\" class=\"col-xs-12 col-sm-4\" pattern=\"[A-Za-z]{1,30}\"");
						}

						if (mandatory == true) {
							System.out.println("-------------in loop 1 part 2 required-------------------");
							form_prefield.append("required");
						}

						if (readonly == true) {
							System.out.println("-------------in loop 1 part 2 required-------------------");
							form_prefield.append("  readonly");
						}
						form_prefield.append("/>\n</div>" + "\n</div>" + "\n</div>" + "\n</td>");
					}

					if (type_field.equals("autocomplete")) {
						form_prefield.append(
								"\n<td>" + "\n<div class=\"form-group\" style=\"margin-left: 10%; margin-right: 10%;\">"
										+ "\n<label class=\"control-label col-xs-12 col-sm-3 no-padding-right\">"
										+ "\n " + field_name4);

						System.out.println("-------------mandatory for testing=------------------" + mandatory);

						if (mandatory == true) {
							System.out.println("-------------in loop 1-------------------");
							form_prefield.append("\n <i class=\"menu-icon fa red\"> *</i>");
						}

						form_prefield.append(
								"\n</label>" + "\n<div class=\"col-xs-12 col-sm-9\">" + "\n<div class=\"clearfix\">");

						form_prefield.append("\n<input");

						if (hidden == true) {
							form_prefield.append(" value=\"${" + table_name_lower + "_updt." + mapping_lower_case
									+ "}\"  type=\"hidden\" ");
						} else {
							form_prefield.append("  value=\"${" + table_name_lower + "_updt." + mapping_lower_case
									+ "}\" type=\"text\" ");
						}
						if (calculated_field == true) {
							form_prefield.append("name=\"total\" id=\"total\" class=\"stunden form-control\" ");
						} else {
							form_prefield.append("name=" + mapping_lower_case + " id=\"" + mapping_lower_case
									+ "\" class=\"col-xs-12 col-sm-4\" ");
						}

						/*
						 * if(mandatory.equals("Y")) { System.out.
						 * println("-------------in loop 1 part 2 required-------------------" );
						 * form.append("required"); }
						 */

						/*
						 * if(readonly.equals("Y")) { System.out.
						 * println("-------------in loop 1 part 2 required-------------------" );
						 * form.append("  readonly"); }
						 */
						form_prefield.append("/>\n</div>" + "\n</div>" + "\n</div>" + "\n</td>");

					}

					if (type_field.equals("dropdown")) {
						form_prefield.append(
								"\n<td>" + "\n<div class=\"form-group\" style=\"margin-left: 10%; margin-right: 10%;\">"
										+ "\n<label class=\"control-label col-xs-12 col-sm-3 no-padding-right\">"
										+ "\n " + field_name4);

						System.out.println("-------------mandatory for testing=------------------" + mandatory);

						if (mandatory == true) {
							System.out.println("-------------in loop 1-------------------");
							form_prefield.append("\n <i class=\"menu-icon fa red\"> *</i>");
						}

						form_prefield.append(
								"\n</label>" + "\n<div class=\"col-xs-12 col-sm-9\">" + "\n<div class=\"clearfix\">");

						if (dependent == true) {

							form_prefield.append("\n<select name=\"state_name\" id=\"" + mapping4
									+ "\"  class=\"col-xs-3 col-sm-3 form-control state\">"
									+ "\n<option >---select---</option>" + "\n</select>");
						} else {

							form_prefield.append("\n<select name=\"drop_value\" id=\"" + mapping4
									+ "\"  value=\"${drop_value}\" class=\"col-xs-3 col-sm-3 form-control");
							form_prefield.append("\t" + mapping4);
							form_prefield.append(
									"\" >" + "\n<option >---select---</option>" + "\n<option value=\"${drop_value}\"");

							if (mandatory == true) {
								System.out.println("-------------in loop 1 part 2 required-------------------");
								form_prefield.append("required");
							}

							if (readonly == true) {
								System.out.println("-------------in loop 1 part 2 required-------------------");
								form_prefield.append("readonly");
							}
							form_prefield.append(">${drop_value}</option>\n" + "</select>\n");
						}
						form_prefield.append("</div>" + "\n</div>" + "\n</div>" + "\n</td>");
					}

					if (type_field.equals("togglebutton")) {
						form_prefield.append(
								"\n<td>" + "\n<div class=\"form-group\" style=\"margin-left: 10%; margin-right: 10%;\">"
										+ "\n<label class=\"control-label col-xs-12 col-sm-3 no-padding-right\">"
										+ "\n " + field_name4);

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

						form_prefield.append(" value=\"${" + table_name_lower + "_updt." + field_name_lower
								+ "}\" name=" + mapping4 + " id=" + mapping4
								+ "  class=\"ace ace-switch ace-switch-6\" onblur=\"CheckUserStatus()\"");

						if (mandatory == true) {
							System.out.println("-------------in loop 1 part 2 required-------------------");
							form_prefield.append("required");
						}

						form_prefield.append("/>\n" + "\n<span class=\"lbl\"></span>"

								+ "\n</div>" + "\n</div>" + "\n</div>" + "\n</td>");
					}

					if (type_field.equals("checkbox")) {
						form_prefield.append(
								"\n<td>" + "\n<div class=\"form-group\" style=\"margin-left: 10%; margin-right: 10%;\">"
										+ "\n<label class=\"control-label col-xs-12 col-sm-3 no-padding-right\">"
										+ "\n " + field_name4);

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

						form_prefield.append(" value=\"${" + table_name_lower + "_updt." + field_name_lower
								+ "}\" name=" + mapping4 + " id=" + mapping4 + "  onblur=\"CheckUserStatusHeader1()\"");

						if (mandatory == true) {
							System.out.println("-------------in loop 1 part 2 required-------------------");
							form_prefield.append("required");
						}

						form_prefield.append("/>\n" + "\n</div>" + "\n</div>" + "\n</div>" + "\n</td>");
					}
				}
			}

			form_prefield.append("\n</tr>\n\n\n</c:forEach>" + popup);

			form_prefield.append("\n</table>");

			if (multiple_line_name != null && !multiple_line_name.isEmpty()) {
				String[] single_line_name = multiple_line_name.split(",");

				button_prefield.append(
						"\n<button type=\"submit\" class=\"btn btn-success btn-sm btn-next\" onclick=\"doSubmit()\">"
								+ "\nSave and Next" + "\n<i class=\"ace-icon fa fa-arrow-right icon-on-right\"></i>"
								+ "\n</button>");
			} else {
				button_prefield
						.append("\n<button type=\"submit\" class=\"btn btn-success center\" onclick=\"submitForms()\">"
								+ "\nCREATE" + "\n</button>");
			}

			form_prefield.append("\n\n<%@include file=\"/WEB-INF/tiles/acemaster/" + module_name + "/"
					+ table_name_lower + "_ext_Update.jsp\"%>" + "\n<div class=\"wizard-actions\">" + button_prefield
					+ "\n</div> " + "\n</form>");

			if (table_name != null && !table_name.isEmpty()) {
				String field_name_for_id = id_list.get(0).getMapping();
				System.out.println("field" + field_name_for_id);
				String field_name_for_id_lower = field_name_for_id.toLowerCase();

				strContentprefield.append(importsectionprefield + " \n" + headsectionprefield
						+ "\n<body>\n<div class=\"main-container\" id=\"main-container\">"
						+ "\n<div class=\"main-content\">" + "\n<div class=\"main-content-inner\">"
						+ "\n<div class=\"breadcrumbs\" id=\"breadcrumbs\">" + "\n<ul class=\"breadcrumb\">"
						+ "\n<li><i class=\"ace-icon fa fa-home home-icon\"></i> <a href=\"#\">Home</a>" + "\n</li>"
						+ "\n<li><a href=\"#\">ManageUsers</a></li>" + "\n<li class=\"active\">" + table_name_lower
						+ "</li>" + "\n</ul>" + "\n</div>" + "\n<div class=\"page-content\">"
						+ "\n<div class=\"page-header\">" + "\n<h1>" + table_name_lower
						+ "<div style=\"float: right; padding-right: 5%;\">\n" + "<a href=\"#myModel\" id=\"${"
						+ table_name_lower + "_updt." + field_name_for_id_lower + "}\">"
						+ "\n<i class=\"fa fa-question-circle\" aria-hidden=\"true\" data-toggle=\"modal\" data-target=\"#myModal\" ></i></a>"
						+ "\n</div>" + "\n</h1>" + "\n</div>" + "\n<div class=\"row\">" + "\n<div class=\"col-xs-12\">"
						+ "\n<div class=\"widget-body\">" + "\n<div class=\"widget-main\">"
						+ "\n<div id=\"fuelux-wizard-container\">" + "\n<div class=\"step-content pos-rel\">"
						+ "\n<div class=\"step-pane active\" data-step=\"1\">"

						+ "<div class=\"table-header\" style=\"margin-bottom: 30px; margin-top: 30px;  margin-right: 3px; width:100%;\">"
						+ "<label style=\"margin-top:2px; margin-left:5px; color: #303030; font-size:18px;\">"
						+ " Section	 </label></div>		" + form_prefield + "\n</div>" + "\n</div>" + "\n</div>"
						+ "\n</div>" + "\n</div>" + "\n</div>" + "\n</div>" + "\n</div>" + "\n</div>" + "\n</div>"
						+ "\n</div>\n"
						+ "\n<script src=\"<c:url value='/resources/assets/js/bootstrap-datepicker.min.js'/>\" type=\"text/javascript\">"
						+ "\n</script>"
						/*
						 * + "\n<script>" + date_script + "\n</script>"
						 */
						+ "\n"
						/* + line_script_update */
						+ "\n</body>\n</html>");
			}

			/*
			 * try { StringBuilder st=new StringBuilder();
			 * 
			 * st. append("\n<definition name=\"demo\" extends=\"acemaster.definition\"> "
			 * +"\n<put-attribute name=\"title\" value=\"WASIB\" />"
			 * +"\n<put-attribute name=\"body\" value=\"/WEB-INF/tiles/acemaster/FB_jsp/rn_student_info_view.jsp\" />"
			 * + "\n</definition>" + "\n\n</tiles-definitions>"); String filename=
			 * "F:/Ganesh/REALNET_2019/REAL_NET_GB1/src/main/webapp/WEB-INF/tiles2.xml";
			 * FileWriter fw = new FileWriter(filename,true); //the true will append the new
			 * data fw.write(st.toString());//appends the string to the file fw.close(); }
			 * catch(IOException ioe) { System.err.println("IOException: " +
			 * ioe.getMessage()); }
			 */

			controller_final_code.append(
					"" + controller + grid_controller + prefield_controller + "\n\n" + prefield_controller_readonly
							+ "\n\n" + save_controller + header_line_submit + dropdown_controller + "\n");

			System.out.println("file------begin");

			File temp = new File(projectPath + "/" + project_name + "/src/main/webapp/WEB-INF/tiles.xml");

			File newtemp = new File(projectPath + "/" + project_name + "/src/main/webapp/WEB-INF/xyz.xml");
			BufferedReader br = new BufferedReader(new FileReader(temp));
			BufferedWriter bw1 = new BufferedWriter(new FileWriter(newtemp));
			String removeStr = "</tiles-definitions>";
			String currentLine;
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

			StringBuilder tiles = new StringBuilder();

			try {

				tiles.append(
						"\n<definition name=\"" + table_name_first_upper + "_grid\" extends=\"acemaster.definition\">"
								+ "\n<put-attribute name=\"title\" value=\"WASIB\"/>"
								+ "\n<put-attribute name=\"body\" value=\"/WEB-INF/tiles/acemaster/" + module_name + "/"
								+ table_name_first_upper + "_grid.jsp\"/>" + "\n</definition>");

				tiles.append("\n<definition name=\"" + jsp_name + "\" extends=\"acemaster.definition\">"
						+ "\n<put-attribute name=\"title\" value=\"WASIB\"/>"
						+ "\n<put-attribute name=\"body\" value=\"/WEB-INF/tiles/acemaster/" + module_name + "/"
						+ jsp_name + ".jsp\"/>" + "\n</definition>");

				tiles.append(
						"\n<definition name=\"" + table_name_first_upper + "_update\" extends=\"acemaster.definition\">"
								+ "\n<put-attribute name=\"title\" value=\"WASIB\"/>"
								+ "\n<put-attribute name=\"body\" value=\"/WEB-INF/tiles/acemaster/" + module_name + "/"
								+ table_name_first_upper + "_update.jsp\"/>" + "\n</definition>");

				tiles.append("\n<definition name=\"" + table_name_first_upper
						+ "_readonly\" extends=\"acemaster.definition\">"
						+ "\n<put-attribute name=\"title\" value=\"WASIB\"/>"
						+ "\n<put-attribute name=\"body\" value=\"/WEB-INF/tiles/acemaster/" + module_name + "/"
						+ table_name_first_upper + "_readonly.jsp\"/>" + "\n</definition>" + "\n</tiles-definitions>");

				/*
				 * tiles.append("\n<definition name=\"" + table_name_first_upper+
				 * "_extension\" extends=\"acemaster.definition\">" +
				 * "\n<put-attribute name=\"title\" value=\"WASIB\"/>" +
				 * "\n<put-attribute name=\"body\" value=\"/WEB-INF/tiles/acemaster/extpages/" +
				 * table_name_first_upper + "_extension.jsp\"/>" + "\n</definition>" +
				 * "\n</tiles-definitions>");
				 */

				// String
				// filename="F:/shubham/Wecan6/REAL_NET/src/main/webapp/WEB-INF/tiles.xml";
				String filename = projectPath + "/" + project_name + "/src/main/webapp/WEB-INF/tiles.xml";

				FileWriter fw = new FileWriter(filename, true);
				fw.write(tiles.toString());
				fw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

			List<Rn_Instance_Type> rn_instance_type_t = instanceTypeRepo.findAll();

			String instance_type = rn_instance_type_t.get(0).getInstanceType();

			if (instance_type.equals("Eclipse")) {

				try {

					File file1 = new File(projectPath + "/" + project_name + "/src/main/java/com/realnet/" + module_name
							+ "/controller/" + controller_name_first_upper + ".java");

					File file2 = new File(projectPath + "/" + project_name + "/src/main/java/com/realnet/" + module_name
							+ "/dao/" + dao_name_first_upper + ".java");

					File file3 = new File(projectPath + "/" + project_name + "/src/main/java/com/realnet/" + module_name
							+ "/dao/" + dao_impl_name_first_upper + ".java");

					File file4 = new File(projectPath + "/" + project_name + "/src/main/java/com/realnet/" + module_name
							+ "/service/" + service_name_first_upper + ".java");

					File file5 = new File(projectPath + "/" + project_name + "/src/main/java/com/realnet/" + module_name
							+ "/service/" + service_impl_name_first_upper + ".java");

					File file6 = new File(projectPath + "/" + project_name + "/src/main/webapp/WEB-INF/tiles/acemaster/"
							+ module_name + "/" + jsp_name + ".jsp");

					File file7 = new File(projectPath + "/" + project_name + "/src/main/java/com/realnet/" + module_name
							+ "/model/" + table_name_first_upper + ".java");

					File file8 = new File(projectPath + "/" + project_name + "/src/main/webapp/WEB-INF/tiles/acemaster/"
							+ module_name + "/" + table_name_first_upper + "_grid.jsp");

					File file9 = new File(projectPath + "/" + project_name + "/src/main/webapp/WEB-INF/tiles/acemaster/"
							+ module_name + "/" + table_name_first_upper + "_update.jsp");

					File file10 = new File(
							projectPath + "/" + project_name + "/src/main/webapp/WEB-INF/tiles/acemaster/" + module_name
									+ "/" + table_name_first_upper + "_readonly.jsp");

					File file15 = new File(
							projectPath + "/" + project_name + "/src/main/webapp/WEB-INF/tiles/acemaster/" + module_name
									+ "/" + table_name_lower + "_extension.jsp");

					File file18 = new File(
							projectPath + "/" + project_name + "/src/main/webapp/WEB-INF/tiles/acemaster/" + module_name
									+ "/" + table_name_lower + "_add_grid.jsp");

					File file19 = new File(
							projectPath + "/" + project_name + "/src/main/webapp/WEB-INF/tiles/acemaster/" + module_name
									+ "/" + table_name_lower + "_add_grid2.jsp");

					File file21 = new File(
							projectPath + "/" + project_name + "/src/main/webapp/WEB-INF/tiles/acemaster/" + module_name
									+ "/" + table_name_lower + "_ext_Update.jsp");

					File file22 = new File(
							projectPath + "/" + project_name + "/src/main/webapp/WEB-INF/tiles/acemaster/" + module_name
									+ "/" + table_name_lower + "_ext_Readonly.jsp");

					System.out.println("file name in build form" + file1);

					if (!file1.exists()) {
						file1.createNewFile();

					}

					FileWriter fw = new FileWriter(file1.getAbsoluteFile());
					BufferedWriter bw = new BufferedWriter(fw);
					bw.write(controller_final_code.toString());
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
					bw.write(strContent.toString());
					bw.close();

					System.out.println("Done jsp");

					if (!file7.exists()) {
						file7.createNewFile();
					}
					fw = new FileWriter(file7.getAbsoluteFile());
					bw = new BufferedWriter(fw);
					bw.write(model_class.toString());
					bw.close();

					System.out.println("Done Model");

					if (!file8.exists()) {
						file8.createNewFile();
					}
					fw = new FileWriter(file8.getAbsoluteFile());
					bw = new BufferedWriter(fw);
					bw.write(table_grid_view.toString());
					bw.close();

					System.out.println("Done grid  view");

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

				} catch (Exception e) {
					System.out.println(e);
				}
			} else {

				try {

					File file1 = new File(projectPath + "/" + project_name + "/src/main/java/DEV/" + module_name + "/"
							+ controller_name_first_upper + ".java");

					File file2 = new File(projectPath + "/" + project_name + "/src/main/java/DEV/" + module_name + "/"
							+ dao_name_first_upper + ".java");

					File file3 = new File(projectPath + "/" + project_name + "/src/main/java/DEV/" + module_name + "/"
							+ dao_impl_name_first_upper + ".java");

					File file4 = new File(projectPath + "/" + project_name + "/src/main/java/DEV/" + module_name + "/"
							+ service_name_first_upper + ".java");

					File file5 = new File(projectPath + "/" + project_name + "/src/main/java/DEV/" + module_name + "/"
							+ service_impl_name_first_upper + ".java");

					File file6 = new File(projectPath + "/" + project_name + "/src/main/java/DEV/" + module_name + "/"
							+ jsp_name + ".jsp");

					File file7 = new File(projectPath + "/" + project_name + "/src/main/java/DEV/" + module_name + "/"
							+ table_name_first_upper + ".java");

					File file8 = new File(projectPath + "/" + project_name + "/src/main/java/DEV/" + module_name + "/"
							+ table_name_first_upper + "_grid.jsp");

					File file9 = new File(projectPath + "/" + project_name + "/src/main/java/DEV/" + module_name + "/"
							+ table_name_first_upper + "_update.jsp");

					File file10 = new File(projectPath + "/" + project_name + "/src/main/java/DEV/" + module_name + "/"
							+ table_name_first_upper + "_readonly.jsp");

					File file15 = new File(projectPath + "/" + project_name + "/src/main/java/DEV/" + module_name + "/"
							+ table_name_lower + "_extension.jsp");

					File file18 = new File(projectPath + "/" + project_name + "/src/main/java/DEV/" + module_name + "/"
							+ table_name_lower + "_add_grid.jsp");
					File file19 = new File(projectPath + "/" + project_name + "/src/main/java/DEV/" + module_name + "/"
							+ table_name_lower + "_add_grid2.jsp");

					File file21 = new File(projectPath + "/" + project_name + "/src/main/java/DEV/" + module_name + "/"
							+ table_name_lower + "_ext_Update.jsp");

					File file22 = new File(projectPath + "/" + project_name + "/src/main/java/DEV/" + module_name + "/"
							+ table_name_lower + "_ext_Readonly.jsp");

					if (!file1.exists()) {
						file1.createNewFile();

					}

					FileWriter fw = new FileWriter(file1.getAbsoluteFile());
					BufferedWriter bw = new BufferedWriter(fw);
					bw.write(controller_final_code.toString());
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
					bw.write(strContent.toString());
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

				} catch (Exception e) {
					System.out.println(e);
				}
			}
		}
		System.out.println("OUT MULTI LINE METHOD");
		if (multiple_line_name != null && !multiple_line_name.isEmpty()) {
			System.out.println("IN MULTI LINE METHOD");
//		  multilineWireframeBuilderDao.multipleLine(session,table_name,multiple_line_name,f_code,form_code);
		}
		System.out.println("OUT ONLY LINE METHOD");

		if (only_line_name != null && !only_line_name.isEmpty()) {
			System.out.println("IN ONLY LINE METHOD");
//			multilineWireframeBuilderDao.onlyLine(session,f_code);
		}

		return new ModelAndView("redirect:rn_wireframe_grid_view");
	}

	@GetMapping(value = "/delete_multiline_wireframe")
	public  ResponseEntity<?> delete_wireframe(@RequestParam("header_id") Integer id) throws IOException { // this is rn_fb_header ID
		
		System.out.println("f_code value in only_line_build_wireframe ::  " + id);

		Rn_Fb_Header rn_fb_header = wireFrameService.getById(id);

		String controller_name = rn_fb_header.getControllerName();
		String dao_name = rn_fb_header.getDaoName();
		String dao_impl_name = rn_fb_header.getDaoImplName();
		String jsp_name = rn_fb_header.getJspName();
		String table_name = rn_fb_header.getTableName();
		String service_name = rn_fb_header.getServiceName();
		String service_impl_name = rn_fb_header.getServiceImplName();
		String line_table_name = rn_fb_header.getLineTableName();

		// get module details
		Rn_Module_Setup module = rn_fb_header.getModule();
		String module_name = module.getModuleName();

		// get project details
		Rn_Project_Setup project = module.getProject();
		String project_name = project.getProjectName();
		String project_prefix = project.getProjectPrefix();
		String db_name = project.getDbName();
		String db_user = project.getDbUserName();
		String db_password = project.getDbPassword();
		String db_port_no = project.getPortNumber();

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

		File file1 = new File(projectPath + "/" + project_name + "/src/main/java/com/realnet/" + module_name
				+ "/controller/" + controller_name_first_upper + ".java");

		File file2 = new File(projectPath + "/" + project_name + "/src/main/java/com/realnet/" + module_name + "/dao/"
				+ dao_name_first_upper + ".java");

		File file3 = new File(projectPath + "/" + project_name + "/src/main/java/com/realnet/" + module_name + "/dao/"
				+ dao_impl_name_first_upper + ".java");

		File file4 = new File(projectPath + "/" + project_name + "/src/main/java/com/realnet/" + module_name
				+ "/service/" + service_name_first_upper + ".java");

		File file5 = new File(projectPath + "/" + project_name + "/src/main/java/com/realnet/" + module_name
				+ "/service/" + service_impl_name_first_upper + ".java");

		File file6 = new File(projectPath + "/" + project_name + "/src/main/webapp/WEB-INF/tiles/acemaster/"
				+ module_name + "/" + jsp_name + ".jsp");

		File file7 = new File(projectPath + "/" + project_name + "/src/main/java/com/realnet/" + module_name + "/model/"
				+ table_name_first_upper + ".java");

		File file8 = new File(projectPath + "/" + project_name + "/src/main/webapp/WEB-INF/tiles/acemaster/"
				+ module_name + "/" + table_name_first_upper + "_grid.jsp");

		File file9 = new File(projectPath + "/" + project_name + "/src/main/webapp/WEB-INF/tiles/acemaster/"
				+ module_name + "/" + table_name_first_upper + "_update.jsp");

		File file10 = new File(projectPath + "/" + project_name + "/src/main/webapp/WEB-INF/tiles/acemaster/"
				+ module_name + "/" + table_name_first_upper + "_readonly.jsp");

		File file11 = new File(projectPath + "/" + project_name + "/src/main/webapp/WEB-INF/tiles/acemaster/"
				+ module_name + "/" + table_name_lower + "_extension.jsp");

		File file12 = new File(projectPath + "/" + project_name + "/src/main/webapp/WEB-INF/tiles/acemaster/"
				+ module_name + "/" + table_name_lower + "_add_grid.jsp");

		File file13 = new File(projectPath + "/" + project_name + "/src/main/webapp/WEB-INF/tiles/acemaster/"
				+ module_name + "/" + table_name_lower + "_add_grid2.jsp");

		File file14 = new File(projectPath + "/" + project_name + "/src/main/webapp/WEB-INF/tiles/acemaster/"
				+ module_name + "/" + table_name_lower + "_ext_Update.jsp");

		File file15 = new File(projectPath + "/" + project_name + "/src/main/webapp/WEB-INF/tiles/acemaster/"
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
}
