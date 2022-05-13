package com.realnet.actionbuilder.controller;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import javax.validation.Valid;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.realnet.actionbuilder.entity.PropertiesDTO;
import com.realnet.actionbuilder.entity.Rn_cff_ActionBuilder_Header;
import com.realnet.actionbuilder.entity.Rn_cff_ActionBuilder_HeaderDTO;
import com.realnet.actionbuilder.entity.Rn_cff_ActionBuilder_Lines;
import com.realnet.actionbuilder.service.Rn_Cff_ActionBuilder_Service;
import com.realnet.fnd.entity.Error;
import com.realnet.fnd.entity.ErrorPojo;
import com.realnet.fnd.entity.Rn_Module_Setup;
import com.realnet.fnd.entity.Rn_Project_Setup;
import com.realnet.fnd.entity.Success;
import com.realnet.fnd.entity.SuccessPojo;
import com.realnet.fnd.response.ActionBuilderResponse;
import com.realnet.fnd.response.CustomResponse;
import com.realnet.utils.ActionTypeConstants;
import com.realnet.utils.Constant;
import com.realnet.utils.RealNetUtils;
import com.realnet.wfb.entity.Rn_Fb_Header;
import com.realnet.wfb.service.Rn_WireFrame_Service;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(value = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(tags = { "Action Builder" })
public class Rn_Cff_ActionBuilderController {

	@Autowired
	private Rn_Cff_ActionBuilder_Service actionBuilderService;

	@Autowired
	private Rn_WireFrame_Service wireFrameService;

	@Value("${projectPath}")
	private String projectPath;

	// GET ALL SORTED AND PAGINATED DATA
	@ApiOperation(value = "List of Actions", response = ActionBuilderResponse.class)
	@GetMapping("/action-builder")
	public ActionBuilderResponse getActions(@RequestParam(value = "page", defaultValue = "0", required = false) Integer page,
			@RequestParam(value = "size", defaultValue = "20", required = false) Integer size) {
		// sorted data
		Pageable paging = PageRequest.of(page, size, Sort.by(Constant.SORT_BY_CREATION_DATE).descending());
		Page<Rn_cff_ActionBuilder_Header> result = actionBuilderService.getAll(paging);
		//log.debug("Action Builders -> {}", result.getContent());

		ActionBuilderResponse resp = new ActionBuilderResponse();
		resp.setPageStats(result, true);
		//comment on error
		resp.setItems(result.getContent());
		return resp;

	}

	// GET BY ID
	@ApiOperation(value = "Get An Action", response = Rn_cff_ActionBuilder_Header.class)
	@GetMapping("/action-builder/{id}")
	public ResponseEntity<?> getActionBuilderDetails(@PathVariable(value = "id") int id) {
		Rn_cff_ActionBuilder_Header bcf_tech_stack = actionBuilderService.getById(id);
		return new ResponseEntity<Rn_cff_ActionBuilder_Header>(bcf_tech_stack, HttpStatus.OK);

	}

	// save actions
	@ApiOperation(value = "Add new Action")
	@PostMapping(value = "/action-builder")
	public ResponseEntity<?> saveAction(@Valid @RequestBody Rn_cff_ActionBuilder_HeaderDTO actionReq) {
		int fbHeaderId = actionReq.getRn_fb_header_id();
		Rn_Fb_Header fbHeader = wireFrameService.getById(fbHeaderId);
		Rn_cff_ActionBuilder_Header action = new Rn_cff_ActionBuilder_Header();
		action.setActionName(actionReq.getActionName());
		action.setTechnologyStack(actionReq.getTechnologyStack());
		action.setControllerName(actionReq.getControllerName());
		action.setMethodName(actionReq.getMethodName());
		action.setFileLocation(actionReq.getFileLocation());
		action.setRn_fb_header(fbHeader);
		Rn_cff_ActionBuilder_Header savedAction = actionBuilderService.save(action);

		if (savedAction != null) {
			SuccessPojo successPojo = new SuccessPojo();
			Success success = new Success();
			success.setTitle(Constant.ACTION_BUILDER_API_TITLE);
			success.setMessage(Constant.ACTION_CREATED_SUCCESSFULLY);
			successPojo.setSuccess(success);
			return new ResponseEntity<SuccessPojo>(successPojo, HttpStatus.CREATED);
		} else {
			ErrorPojo errorPojo = new ErrorPojo();
			Error error = new Error();
			error.setTitle(Constant.ACTION_BUILDER_API_TITLE);
			error.setMessage(Constant.ACTION_CREATED_SUCCESSFULLY);
			errorPojo.setError(error);
			return new ResponseEntity<ErrorPojo>(errorPojo, HttpStatus.EXPECTATION_FAILED);
		}
	}

	// UPDATE
	@ApiOperation(value = "Update A Project", response = Rn_cff_ActionBuilder_Header.class)
	@PutMapping("/action-builder/{id}")
	public ResponseEntity<?> updateAction(
			@RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authToken,
			@PathVariable(value = "id") int id, @Valid @RequestBody Rn_cff_ActionBuilder_Header actionRequest) {

		Rn_cff_ActionBuilder_Header updatedAction = actionBuilderService.updateById(id, actionRequest);

		if (actionRequest.getId() != updatedAction.getId()) {
			ErrorPojo errorPojo = new ErrorPojo();
			Error error = new Error();
			error.setTitle(Constant.ACTION_BUILDER_API_TITLE);
			error.setMessage(Constant.ACTION_NOT_DELETED);
			errorPojo.setError(error);
			return new ResponseEntity<ErrorPojo>(errorPojo, HttpStatus.EXPECTATION_FAILED);
		}
		SuccessPojo successPojo = new SuccessPojo();
		Success success = new Success();
		success.setTitle(Constant.EXTRACTOR_API_TITLE);
		success.setMessage(Constant.ACTION_UPDATED_SUCCESSFULLY);
		successPojo.setSuccess(success);
		return new ResponseEntity<SuccessPojo>(successPojo, HttpStatus.OK);
	}

	// DELETE
	@DeleteMapping("/action-builder/{id}")
	public ResponseEntity<?> deleteAction(@PathVariable(value = "id") int id) {
		boolean deleted = actionBuilderService.deleteById(id);
		if (deleted) {
			SuccessPojo successPojo = new SuccessPojo();
			Success success = new Success();
			success.setTitle(Constant.ACTION_BUILDER_API_TITLE);
			success.setMessage(Constant.ACTION_DELETED_SUCCESSFULLY);
			successPojo.setSuccess(success);
			return new ResponseEntity<SuccessPojo>(successPojo, HttpStatus.OK);
		} else {
			ErrorPojo errorPojo = new ErrorPojo();
			Error error = new Error();
			error.setTitle(Constant.ACTION_BUILDER_API_TITLE);
			error.setMessage(Constant.ACTION_NOT_DELETED);
			errorPojo.setError(error);
			return new ResponseEntity<ErrorPojo>(errorPojo, HttpStatus.EXPECTATION_FAILED);
		}
	}

	// ============ LINE PART ===========================

	/*
	 * This is using pageImpl class
	 * please note down that will be useful to sort list of data
	 * */
	// GET ALL SORTED AND PAGINATED DATA
	@ApiOperation(value = "List of Action Lines", response = CustomResponse.class)
	@GetMapping("/action-builder-line")
	public CustomResponse getActionLines(@RequestParam("headerId") Integer headerId,
			@RequestParam(value = "page", defaultValue = "0", required = false) Integer page,
			@RequestParam(value = "size", defaultValue = "20", required = false) Integer size) {
		// sorted data
//		Pageable paging = PageRequest.of(page, size, Sort.by(Constant.SORT_BY_CREATION_DATE_NATIVE_QUERY).descending());
//		Page<Rn_cff_ActionBuilder_Lines> resultt = actionBuilderService.getLinesByHeaderId(headerId, paging);
		// else
		Rn_cff_ActionBuilder_Header header = actionBuilderService.getById(headerId);
		List<Rn_cff_ActionBuilder_Lines> lines = header.getActionBuilderLines();
		Pageable paging = PageRequest.of(page, size, Sort.by(Constant.SORT_BY_CREATION_DATE).descending());
		Page<Rn_cff_ActionBuilder_Lines> result = new PageImpl<>(lines, paging, lines.size());
		CustomResponse resp = new CustomResponse();
		resp.setPageStats(result, true);
		resp.setItems(result.getContent());
		return resp;
	}

	// GET LINE BY ID
	@ApiOperation(value = "Get An Action Line", response = Rn_cff_ActionBuilder_Lines.class)
	@GetMapping("/action-builder-line/{id}")
	public ResponseEntity<?> getActionBuilderLineDetails(@PathVariable(value = "id") int id) {
		Rn_cff_ActionBuilder_Lines line = actionBuilderService.getLineById(id);
		return new ResponseEntity<Rn_cff_ActionBuilder_Lines>(line, HttpStatus.OK);
	}

	@ApiOperation(value = "Add new Action Line")
	@PostMapping(value = "/action-builder-line")
	public ResponseEntity<?> saveProject(@RequestParam("headerId") Integer headerId,
			@Valid @RequestBody Rn_cff_ActionBuilder_Lines actionReq) {
		Rn_cff_ActionBuilder_Lines savedAction = actionBuilderService.saveLine(headerId, actionReq);

		if (savedAction != null) {
			SuccessPojo successPojo = new SuccessPojo();
			Success success = new Success();
			success.setTitle(Constant.ACTION_BUILDER_API_TITLE);
			success.setMessage(Constant.ACTION_CREATED_SUCCESSFULLY);
			successPojo.setSuccess(success);
			return new ResponseEntity<SuccessPojo>(successPojo, HttpStatus.CREATED);
		} else {
			ErrorPojo errorPojo = new ErrorPojo();
			Error error = new Error();
			error.setTitle(Constant.ACTION_BUILDER_API_TITLE);
			error.setMessage(Constant.ACTION_CREATED_SUCCESSFULLY);
			errorPojo.setError(error);
			return new ResponseEntity<ErrorPojo>(errorPojo, HttpStatus.EXPECTATION_FAILED);
		}
	}

	// UPDATE
	@ApiOperation(value = "Update A Action Line", response = Rn_cff_ActionBuilder_Lines.class)
	@PutMapping("/action-builder-line/{id}")
	public ResponseEntity<?> updateProject(
			@RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authToken,
			@PathVariable(value = "id") int id, @Valid @RequestBody Rn_cff_ActionBuilder_Lines actionRequest) {

		Rn_cff_ActionBuilder_Lines updatedAction = actionBuilderService.updateLineById(id, actionRequest);

		if (actionRequest.getId() != updatedAction.getId()) {
			ErrorPojo errorPojo = new ErrorPojo();
			Error error = new Error();
			error.setTitle(Constant.ACTION_BUILDER_API_TITLE);
			error.setMessage(Constant.ACTION_NOT_DELETED);
			errorPojo.setError(error);
			return new ResponseEntity<ErrorPojo>(errorPojo, HttpStatus.EXPECTATION_FAILED);
		}
		SuccessPojo successPojo = new SuccessPojo();
		Success success = new Success();
		success.setTitle(Constant.EXTRACTOR_API_TITLE);
		success.setMessage(Constant.ACTION_UPDATED_SUCCESSFULLY);
		successPojo.setSuccess(success);
		return new ResponseEntity<SuccessPojo>(successPojo, HttpStatus.OK);
	}

	// DELETE
	@DeleteMapping("/action-builder-line/{id}")
	public ResponseEntity<?> deleteProject(@PathVariable(value = "id") int id) {
		boolean deleted = actionBuilderService.deleteLineById(id);
		if (deleted) {
			SuccessPojo successPojo = new SuccessPojo();
			Success success = new Success();
			success.setTitle(Constant.ACTION_BUILDER_API_TITLE);
			success.setMessage(Constant.ACTION_DELETED_SUCCESSFULLY);
			successPojo.setSuccess(success);
			return new ResponseEntity<SuccessPojo>(successPojo, HttpStatus.OK);
		} else {
			ErrorPojo errorPojo = new ErrorPojo();
			Error error = new Error();
			error.setTitle(Constant.ACTION_BUILDER_API_TITLE);
			error.setMessage(Constant.ACTION_NOT_DELETED);
			errorPojo.setError(error);
			return new ResponseEntity<ErrorPojo>(errorPojo, HttpStatus.EXPECTATION_FAILED);
		}
	}

	// ------------ ACTION BUILDER BUSINESS LOGIC

	/*
	 * WORKING 2 CONTROLLERS... 1. LAYOUT CONTROLLER 2. DATA CONTROLLER
	 */
	// NEW LOGIC 29/09/20
	@GetMapping(value = "/cff_layout")
	public ResponseEntity<?> newLogicForActionBuilderCode1(@RequestParam(value = "headerId") Integer headerId)
			throws IOException {

		// int header_id = Integer.parseInt(h_id);
		System.out.println("HEADER_ID IN CFF LAYOUT CONTROLLER = " + headerId);
		StringBuilder dynamicCode = new StringBuilder();

		List<Rn_cff_ActionBuilder_Lines> actionBuilderLines = actionBuilderService
				.getLinesByHeaderIdAndOrderBySeq(headerId);

		// SETTER ATTRIBUES FOR MODEL CLASS
//		ArrayList<Integer> variableCount = new ArrayList<Integer>();

		// MODEL CLASS INSTANCE FOR PASSING IN DAO METHOD
//		ArrayList<Integer> modelCount = new ArrayList<Integer>();
//		final int size = 5;
//		int[] modelCount = new int[size];

		int count = 0;
		for (Rn_cff_ActionBuilder_Lines lines : actionBuilderLines) {
			String actionType1 = lines.getActionType1();
			String actionType2 = lines.getActionType2();

			int seq = lines.getSeq();

			// if (action_type1.equals("variable")) {
			if (ActionTypeConstants.AC1_VARIABLE.equalsIgnoreCase(actionType1)) {
				System.out.println("VARIABLE COUNT = " + ++count);
				dynamicCode.append("$variable(" + seq + ")\n");
//				variableCount.add(seq);
			}

			// OPERATION (CALCULATION)
			// if (action_type1.equals("operation")) {
			if (ActionTypeConstants.AC1_OPERATION.equalsIgnoreCase(actionType1)) {
				System.out.println("OPERATION COUNT" + count++);
				dynamicCode.append("$operation(" + seq + ")\n");
			}

			// FOR LOOP OPEN
			// if (action_type1.equals("forloop") && action_type2.equals("open")) {
			if (ActionTypeConstants.AC1_FOR_LOOP.equalsIgnoreCase(actionType1)
					&& ActionTypeConstants.AC2_OPEN.equalsIgnoreCase(actionType2)) {
				System.out.println("FORLOOP COUNT" + count++);
				dynamicCode.append("$forloop(" + seq + ") {\n");
			}
			// FOR LOOP CLOSE
			// if (action_type1.equals("forloop") && action_type2.equals("close")) {
			if (ActionTypeConstants.AC1_FOR_LOOP.equalsIgnoreCase(actionType1)
					&& ActionTypeConstants.AC2_CLOSE.equalsIgnoreCase(actionType2)) {
				dynamicCode.append("}\n");
			}

			// LOG CONSOLE
			// if (action_type1.equals("log") && action_type2.equals("console")) {
			if (ActionTypeConstants.AC1_LOG.equalsIgnoreCase(actionType1)
					&& ActionTypeConstants.AC2_CONSOLE.equalsIgnoreCase(actionType2)) {
				System.out.println("LOG COUNT" + count++);
				dynamicCode.append("$log.consol(" + seq + ")\n");
			}

			// COMMENTS
			// if (action_type1.equals("comment")) {
			if (ActionTypeConstants.AC1_COMMENT.equalsIgnoreCase(actionType1)) {
				System.out.println("COMMENT COUNT" + count++);
				dynamicCode.append("$comment(" + seq + ")\n");
			}

			// IF OPEN
			// if (action_type1.equals("if") && action_type2.equals("open")) {
			if (ActionTypeConstants.AC1_IF.equalsIgnoreCase(actionType1)
					&& ActionTypeConstants.AC2_OPEN.equalsIgnoreCase(actionType2)) {
				System.out.println("IF COUNT" + count++);
				dynamicCode.append("$if(" + seq + ") {\n");
			}
			// IF CLOSE
			// if (action_type1.equals("if") && action_type2.equals("close")) {
			if (ActionTypeConstants.AC1_IF.equalsIgnoreCase(actionType1)
					&& ActionTypeConstants.AC2_CLOSE.equalsIgnoreCase(actionType2)) {
				dynamicCode.append("}\n");
			}

			// ELSE OPEN
			// if (action_type1.equals("else") && action_type2.equals("open")) {
			if (ActionTypeConstants.AC1_ELSE.equalsIgnoreCase(actionType1)
					&& ActionTypeConstants.AC2_OPEN.equalsIgnoreCase(actionType2)) {
				System.out.println("ELSE COUNT" + count++);
				dynamicCode.append("{\n");
			}
			// ELSE CLOSE
			// if (action_type1.equals("else") && action_type2.equals("close")) {
			if (ActionTypeConstants.AC1_ELSE.equalsIgnoreCase(actionType1)
					&& ActionTypeConstants.AC2_CLOSE.equalsIgnoreCase(actionType2)) {
				dynamicCode.append("}\n");
			}

			// ELSE-IF OPEN
			// if (action_type1.equals("else-if") && action_type2.equals("open")) {
			if (ActionTypeConstants.AC1_ELSE_IF.equalsIgnoreCase(actionType1)
					&& ActionTypeConstants.AC2_OPEN.equalsIgnoreCase(actionType2)) {
				System.out.println("ELSE-IF COUNT" + count++);
				dynamicCode.append("$elif(" + seq + ") {\n");
			}
			// ELSE-IF CLOSE
			// if (action_type1.equals("else-if") && action_type2.equals("close")) {
			if (ActionTypeConstants.AC1_ELSE_IF.equalsIgnoreCase(actionType1)
					&& ActionTypeConstants.AC2_CLOSE.equalsIgnoreCase(actionType2)) {
				dynamicCode.append("}\n");
			}

			// WHILE OPEN
			// if (action_type1.equals("while") && action_type2.equals("open")) {
			if (ActionTypeConstants.AC1_WHILE.equalsIgnoreCase(actionType1)
					&& ActionTypeConstants.AC2_OPEN.equalsIgnoreCase(actionType2)) {
				System.out.println("WHILE COUNT = " + count++);
				dynamicCode.append("$while(" + seq + ") {\n");
			}

			// WHILE CLOSE
			// if (action_type1.equals("while") && action_type2.equals("close")) {
			if (ActionTypeConstants.AC1_WHILE.equalsIgnoreCase(actionType1)
					&& ActionTypeConstants.AC2_CLOSE.equalsIgnoreCase(actionType2)) {
				dynamicCode.append("}\n");
			}

			// WHILE LOGICAL OPERATIONS
			// if (action_type1.equals("while-op")) {
			if (ActionTypeConstants.AC1_WHILE_OP.equalsIgnoreCase(actionType1)) {
				System.out.println("WHILE-OP COUNT = " + count++);
				dynamicCode.append("$while-op(" + seq + ")\n");
			}

			// LOGICAL INCREMENT DECREMENT
			// if (action_type1.equals("while-fwd")) {
			if (ActionTypeConstants.AC1_WHILE_FWD.equalsIgnoreCase(actionType1)) {
				System.out.println("WHILE FWD COUNT = " + count++);
				dynamicCode.append("$while-fwd(" + seq + ")\n");
			}

			// (DO-WHILE) DO OPEN
			// if (action_type1.equals("do") && action_type2.equals("open")) {
			if (ActionTypeConstants.AC1_DO.equalsIgnoreCase(actionType1)
					&& ActionTypeConstants.AC2_OPEN.equalsIgnoreCase(actionType2)) {
				dynamicCode.append("do {\n");
			}
			// LOGICAL INCREMENT DECREMENT
			// if (action_type1.equals("do-fwd")) {
			if (ActionTypeConstants.AC1_DO_FWD.equalsIgnoreCase(actionType1)) {
				dynamicCode.append("$do-fwd(" + seq + ")\n");
			}
			// (DO-WHILE) DO CLOSE
			// if (action_type1.equals("do") && action_type2.equals("close")) {
			if (ActionTypeConstants.AC1_DO.equalsIgnoreCase(actionType1)
					&& ActionTypeConstants.AC2_CLOSE.equalsIgnoreCase(actionType2)) {
				dynamicCode.append("}\n");
			}

			// (DO-WHILE) WHILE CONDITION
			// if (action_type1.equals("while") && action_type2.equals("condition")) {
			if (ActionTypeConstants.AC1_WHILE.equalsIgnoreCase(actionType1)
					&& ActionTypeConstants.AC2_CONDITION.equalsIgnoreCase(actionType2)) {
				dynamicCode.append("$do-while(" + seq + ")\n");
			}

			// ========== LIST =====
			// if (action_type1.equals("list")) {
			if (ActionTypeConstants.AC1_LIST.equalsIgnoreCase(actionType1)) {
				System.out.println("ArrayList VARIABLE COUNT = " + count++);
				dynamicCode.append("$arraylist(" + seq + ")\n");
			}

			// ========== MAP =====
			// if (action_type1.equals("map")) {
			if (ActionTypeConstants.AC1_MAP.equalsIgnoreCase(actionType1)) {
				System.out.println("HashMap VARIABLE COUNT = " + count++);
				dynamicCode.append("$hashmap(" + seq + ")\n");
			}

			// ========== SET =====
			// if (action_type1.equals("set")) {
			if (ActionTypeConstants.AC1_SET.equalsIgnoreCase(actionType1)) {
				System.out.println("HashSet VARIABLE COUNT = " + count++);
				dynamicCode.append("$hashset(" + seq + ")\n");
			}

			// UPDATE TABLE USING CORE JDBC, JDBC_TEMPLATE, HIBERNATE
			if (ActionTypeConstants.AC1_UPDATE_TABLE.equals(actionType1)) {
				if (ActionTypeConstants.AC2_JDBC_TEMPLATE.equals(actionType2)) {
					System.out.println("UPDATE TABLE USING JDBC TEMPLATE");
					dynamicCode.append("$updt-tbl-jdbc-tmplt(" + seq + ")\n");
				} else if (ActionTypeConstants.AC2_SQL_STATEMENT.equals(actionType2)) {
					System.out.println("UPDATE TABLE USING JDBC TEMPLATE");
					dynamicCode.append("$updt-tbl-sql-stmt(" + seq + ")\n");
				} else {
					System.out.println("UPDATE TABLE USING HIBERNATE");
					dynamicCode.append("$updt-tbl-hibernate(" + seq + ")\n");
				}
			}

			// INSERT INTO TABLE USING HIBERNATE
			if (ActionTypeConstants.AC1_INSERT_TABLE.equals(actionType1)) {
				System.out.println("INSERT TABLE USING HIBERNATE");
				dynamicCode.append("$insert-tbl-hibernate(" + seq + ")\n");
			}

			/*
			 * FILE STRING REPLACE & FILE STRING REPLACE LOGIC
			 */
			// FILE STRING REPLACE
			if (ActionTypeConstants.AC1_STRING_REPLACE.equals(actionType1)
					&& ActionTypeConstants.AC2_FILE_STRING_REPLACE.equals(actionType2)) {
				System.out.println("READ FILE AND STRING REPLACE..");
				dynamicCode.append("$file-str-replace(" + seq + ")\n");
			}
			// FILE STRING REPLACE
			if (ActionTypeConstants.AC1_STRING_REPLACE.equals(actionType1)
					&& ActionTypeConstants.AC2_VARIABLE_STRING_REPLACE.equals(actionType2)) {
				System.out.println("STRING REPLACE IN A VARIABLE");
				dynamicCode.append("$var-str-replace(" + seq + ")\n");
			}

			/*
			 * // NEW CASE (need testing) if
			 * (ActionTypeConstants.AC1_STRING_REPLACE.equals(action_type1)) {
			 * if(ActionTypeConstants.AC2_FILE_STRING_REPLACE.equals(action_type2)) {
			 * System.out.println("READ FILE AND STRING REPLACE..");
			 * dynamicCode.append("$file-str-replace(" + seq + ")\n"); } else
			 * if(ActionTypeConstants.AC2_VARIABLE_STRING_REPLACE.equals(action_type2)) {
			 * System.out.println("STRING REPLACE IN A VARIABLE");
			 * dynamicCode.append("$var-str-replace(" + seq + ")\n"); } else { break; } }
			 */

			// STRING APPEND IN A VARIABLE
			if (ActionTypeConstants.AC1_STRING_APPEND.equals(actionType1)
					&& ActionTypeConstants.AC2_VARIABLE_STRING_APPEND.equals(actionType2)) {
				System.out.println("STRING APPEND IN A VARIABLE");
				dynamicCode.append("$var-str-apnd(" + seq + ")\n");
			}

			// STRING APPEND IN A FILE
			if (ActionTypeConstants.AC1_STRING_APPEND.equals(actionType1)
					&& ActionTypeConstants.AC2_FILE_STRING_APPEND.equals(actionType2)) {
				System.out.println("STRING APPEND IN A FILE");
				dynamicCode.append("$file-str-apnd(" + seq + ")\n");
			}

			/*
			 * ACTION_TYPE1 ::: MODEL_ DAO, SERVICE
			 */
			if (ActionTypeConstants.AC1_MODEL.equals(actionType1)) {
				System.out.println("MODEL CLASS CODE");
				dynamicCode.append("$model(" + seq + ")\n");

			}
			if (ActionTypeConstants.AC1_DAO.equals(actionType1)) {
				dynamicCode.append("$dao(" + seq + ")\n");
			}

//			if (ActionTypeConstants.AC1_SERVICE.equals(action_type1) {
//				
//			}

		}
		// LAYOUT OUTPUT
		// System.out.println(dynamicCode.toString());

		// OUTPUT FILE FOR TESTING
//		Rn_cff_ActionBuilder_Header rn_cff_ActionBuilder_header = rn_cff_actionBuilder_Service
//				.getActionBuilderHeaderById(header_id);
		Rn_cff_ActionBuilder_Header rn_cff_ActionBuilder_header = actionBuilderService.getById(headerId);
		
		

		String file_location = rn_cff_ActionBuilder_header.getFileLocation();
		String path = projectPath.concat(file_location);
		System.out.println("\nCFF LAYOUT CONTROLLER ::\nFILE PATH = " + path);

		try {

			// DYNAMIC PATH
			File file = new File(path);
			
			// if file is not present then create the action builder controller
			if(!file.exists()) {
				Rn_Fb_Header fbHeader = rn_cff_ActionBuilder_header.getRn_fb_header();
				Rn_Module_Setup module =  fbHeader.getModule();
				Rn_Project_Setup project = module.getProject();
				
				String project_name = project.getProjectName();
				String module_name = module.getModuleName();
				
				String table_name_first_upper = RealNetUtils.toFirstUpperCase(fbHeader.getTableName());
				String table_name_lower = table_name_first_upper.toLowerCase();
				String dao_name_first_upper = RealNetUtils.toFirstUpperCase(fbHeader.getDaoName());
				String dao_name_lower = dao_name_first_upper.toLowerCase();
				String service_name_first_upper = RealNetUtils.toFirstUpperCase(fbHeader.getServiceName());
				
				String controllerName = rn_cff_ActionBuilder_header.getControllerName(); // controller name
				String methodName = rn_cff_ActionBuilder_header.getMethodName();
				String actionName = rn_cff_ActionBuilder_header.getActionName(); // api
				StringBuilder cff_add_button_controller_code = new StringBuilder(); // action name and method name is same
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
				System.out.println("Niladri controllerName = " + controllerName);
				// CONTROLLER PREFIX
				cff_add_button_controller_code.append(
						cff_add_button_controller_imports 
						+ "@Controller\r\n" + "public class " + controllerName + " {\r\n"
								+ "	@Autowired" + "	private " + dao_name_first_upper + "\t" + dao_name_lower + ";\n");
				
					cff_add_button_controller_code.append("	// INSERT FIELDS USING ACTION BUILDER\r\n"
							+ "@GetMapping(value = \"/" + actionName + "\")\r\n"
							+ "	public ModelAndView " + methodName + "(@RequestParam(value = \"id\") String h_id) throws IOException {\r\n"
							+ "		int hId = Integer.parseInt(h_id);\r\n"
							+ "		//System.out.println(\"JSP ID = \" + hId);\r\n" 
							+ "	// CFF_LAYOUT_CONTROLLER_START\r\n"
							+ "		System.out.println(\"PLEASE INSERT CODE... GO TO ACTION BUILDER... \");\r\n" 
							+ "	// CFF_LAYOUT_CONTROLLER_END\r\n" 
							+ "		return new ModelAndView(\"redirect:" + table_name_lower + "_update?id=\" + hId);\r\n"
							+ "	}\n"
							+ "}");

				// FILE PATH CHANGE NEEDED...
				File actionBuilderFile = new File(projectPath + "/Projects/" + project_name + "/src/main/java/com/realnet/" + module_name
						+ "/controller/" + controllerName + ".java");
				System.out.println("Niladri action builder file path = " + file.getAbsolutePath());
				if (!actionBuilderFile.exists()) {
					actionBuilderFile.getParentFile().mkdirs();
					actionBuilderFile.createNewFile();
				}

				FileWriter fw = new FileWriter(actionBuilderFile.getAbsoluteFile());
				BufferedWriter bw = new BufferedWriter(fw);
				bw.write(cff_add_button_controller_code.toString());
				bw.close();
			} // action builder file creation code end
			
			String fileString = FileUtils.readFileToString(file, StandardCharsets.UTF_8);
			String start = "// CFF_LAYOUT_CONTROLLER_START";
			String end = "// CFF_LAYOUT_CONTROLLER_END";
			fileString = RealNetUtils.stringReplace(fileString, start, end, dynamicCode.toString());
			BufferedWriter bw = new BufferedWriter(new FileWriter(file, false)); // OVER WRITE FILE
			bw.write(fileString);
			bw.close();

			SuccessPojo successPojo = new SuccessPojo();
			Success success = new Success();
			success.setTitle(Constant.ACTION_BUILDER_API_TITLE);
			success.setMessage(Constant.ACTION_CFF_LAYOUT_SUCCESS);
			successPojo.setSuccess(success);
			return new ResponseEntity<SuccessPojo>(successPojo, HttpStatus.OK);
		}
//		catch (FileNotFoundException e) {
//			log.error("FileNotFoundException Caught..");
//			e.getMessage();
//		} 
		catch (IOException e) {
			log.error("Exception Caught..");
			e.getMessage();
			ErrorPojo errorPojo = new ErrorPojo();
			Error error = new Error();
			error.setTitle(Constant.ACTION_BUILDER_API_TITLE);
			error.setMessage(Constant.ACTION_CFF_LAYOUT_FAILURE);
			errorPojo.setError(error);
			return new ResponseEntity<ErrorPojo>(errorPojo, HttpStatus.EXPECTATION_FAILED);
		}
		// CFF LAYOUT CONTROLLER END
		// return new ModelAndView("redirect:cff_action_builder_line_grid_view?id=" +
		// header_id);
	}

	@GetMapping(value = "/cff_data")
	public ResponseEntity<?> newLogicForActionBuilderCode2(@RequestParam(value = "headerId") Integer headerId)
			throws IOException {
		System.out.println("HEADER_ID IN CFF DATA CONTROLLER = " + headerId);

		Rn_cff_ActionBuilder_Header rn_cff_ActionBuilder_header = actionBuilderService.getById(headerId);

		String file_location = rn_cff_ActionBuilder_header.getFileLocation();

		String filePath = projectPath.concat(file_location);
		System.out.println("CFF DATA CONTROLLER ::\nFILE PATH = " + filePath);

		// OUTPUT FILE FOR TESTING
//		String filePath = "D:\\Office Project\\ris\\src\\main\\java\\com\\realnet\\actionbuilder\\controller\\Rn_cff_TestController.java";
		File file = new File(filePath);

		StringBuilder code = new StringBuilder();
		Path path = Paths.get(filePath);
		List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
		StringBuilder setter_code = new StringBuilder();

		// int forOuter = 0;
		for (String line : lines) {
			// System.out.println("OUTER-FOR COUNT = " + forOuter++);

			if (line.startsWith("$variable") || line.startsWith("$forloop") || line.startsWith("$log.consol")
					|| line.startsWith("$comment") || line.startsWith("$operation") || line.startsWith("$if")
					|| line.startsWith("$elif") || line.startsWith("$while") || line.startsWith("$do")
					|| line.startsWith("$arraylist") || line.startsWith("$hashmap") || line.startsWith("$hashset")
					|| line.startsWith("$updt-tbl-hibernate") || line.startsWith("$updt-tbl-jdbc-tmplt")
					|| line.startsWith("$updt-tbl-sql-stmt") || line.startsWith("$insert-tbl-hibernate")
					|| line.startsWith("$file-str-replace") || line.startsWith("$var-str-replace")
					|| line.startsWith("$var-str-apnd") || line.startsWith("$file-str-apnd")
					|| line.startsWith("$model") || line.startsWith("$dao")) {

				System.out.println("REPLACED $ LINE = " + line);
				line = line.replaceAll("[^0-9]", ""); // GET THE (SEQ_ID)
//				int seq = -1; // modify
//				if(!line.isEmpty() && line != null) {
//					seq = Integer.parseInt(line);
//				}
				int seq = Integer.parseInt(line);
				System.out.println("SEQ = " + seq);
				// SAVE SEQ NUMBERS
				ArrayList<Integer> seqs = new ArrayList<Integer>();
				seqs.add(seq);

				// GET LINE TABLE DATA BY SEQ
//				Rn_cff_ActionBuilder_Lines actionBuilderLines = rn_actionbuilder_dao
//						.getActionBuilderLinesBySeqAndHeaderId(seq, header_id);
				Rn_cff_ActionBuilder_Lines actionBuilderLines = actionBuilderService.getLinesByHeaderIdAndSeq(headerId,
						seq);

				String action_type1 = actionBuilderLines.getActionType1();
				String action_type2 = actionBuilderLines.getActionType2();

				if (action_type1.equals("variable")) {
					String data_type = actionBuilderLines.getDataType();
					String variable_name = actionBuilderLines.getVariableName();
					String assignment = actionBuilderLines.getAssignment();

					// FOR STRING TYPE OF VALUE
					if (data_type.equalsIgnoreCase("String")) {
						assignment = "\"" + assignment + "\"";
					}
					// FOR CHAR TYPE OF VALUE
					if (data_type.equalsIgnoreCase("char")) {
						assignment = "'" + assignment + "'";
					}
//					if(assignment.equalsIgnoreCase("null")) {
//						
//					}
					// MODEL SETTER CODE
					String attri_name = RealNetUtils.toFirstUpperCase(variable_name);
					setter_code.append("model_obj.set" + attri_name + "(" + attri_name.toLowerCase() + ");\n");

					String output = data_type + " " + variable_name + " = " + assignment + ";\n";
					line = output;
				}

				// FOR LOOP
				if (ActionTypeConstants.AC1_FOR_LOOP.equalsIgnoreCase(action_type1)
						&& ActionTypeConstants.AC2_OPEN.equalsIgnoreCase(action_type2)) {
					// if (action_type1.equals("forloop") && action_type2.equals("open")) {
					String data_type = actionBuilderLines.getDataType();
					String variable_name = actionBuilderLines.getVariableName();
					String assignment = actionBuilderLines.getAssignment();
					String condition = actionBuilderLines.getConditions();
					String forward = actionBuilderLines.getForward();

					String output = "for(" + data_type + " " + variable_name + " = " + assignment + "; " + condition
							+ "; " + variable_name + forward + ") {\n";
					line = output;
				}

				// LOG NEED TO DEVELOP (if , before { logic implement)
				if (ActionTypeConstants.AC1_LOG.equalsIgnoreCase(action_type1)
						&& ActionTypeConstants.AC2_CONSOLE.equalsIgnoreCase(action_type2)) {
					// if (action_type1.equals("log") && action_type2.equals("console")) {
					String message = actionBuilderLines.getMessage().trim();
					if (message.contains("{")) {
						int i = message.indexOf("{");
						message = "\"" + message.substring(0, i) + " \"+ " + message.substring(i);
					} else {
						message = "\"" + message + "\"";
					}
					message = message.replaceAll("[{}]", "");
					message = message.replaceAll("\\s+", " "); // ( )+
//					message = message.replace(" ", " + \" \" + ");
					String output = "System.out.println(" + message + ");\n";
					line = output;
				}

				// COMMENT
				if (ActionTypeConstants.AC1_COMMENT.equalsIgnoreCase(action_type1)) {
					// if (action_type1.equals("comment")) {
					String message = actionBuilderLines.getMessage().trim();
					String output = "// " + message + "\n";
					line = output;
				}

				// OPERATION ( ALL KIND OF LOGICAL OP)
				if (ActionTypeConstants.AC1_OPERATION.equalsIgnoreCase(action_type1)) {
					// if (action_type1.equals("operation")) {
					String variable_name = actionBuilderLines.getVariableName();
					String equation = actionBuilderLines.getEquation();
					int colonIndex = equation.lastIndexOf(";"); // for removing last colon
					equation = equation.substring(0, colonIndex) + ";";
					String output = "";
//					try {
					if (variable_name != null && !variable_name.isEmpty()) {
						output = variable_name + " = " + equation + ";\n";
					} else {
						output = equation + ";\n";
					}
//					} catch (NullPointerException e) {
//						e.printStackTrace();
//					}
					line = output;
				}

				// IF
				if (ActionTypeConstants.AC1_IF.equalsIgnoreCase(action_type1)
						&& ActionTypeConstants.AC2_OPEN.equalsIgnoreCase(action_type2)) {
					// if (action_type1.equals("if") && action_type2.equals("open")) {
					String condition = actionBuilderLines.getConditions();
					String output = "if(" + condition + ") {\n";
					line = output;
				}

				// ELSE-IF
				if (ActionTypeConstants.AC1_ELSE_IF.equalsIgnoreCase(action_type1)
						&& ActionTypeConstants.AC2_OPEN.equalsIgnoreCase(action_type2)) {
					// if (action_type1.equals("else-if") && action_type2.equals("open")) {
					String condition = actionBuilderLines.getConditions();
					String output = "else if(" + condition + ") {\n";
					line = output;
				}

				// WHILE LOOP
				if (ActionTypeConstants.AC1_WHILE.equalsIgnoreCase(action_type1)
						&& ActionTypeConstants.AC2_OPEN.equalsIgnoreCase(action_type2)) {
					// if (action_type1.equals("while") && action_type2.equals("open")) {
					String condition = actionBuilderLines.getConditions();
					String output = "while(" + condition + ") {\n";
					line = output;
				}

				// EQUAION (ANY LOGICAL OPERATION) NEED TO MODIFY ////
				if (ActionTypeConstants.AC1_WHILE_OP.equalsIgnoreCase(action_type1)) {
					// if (action_type1.equals("while-op")) {
					String variable_name = actionBuilderLines.getVariableName();
					String equation = actionBuilderLines.getEquation();

					String output = null;
					if (!variable_name.isEmpty() || variable_name != null)
						output = variable_name + " = " + equation + ";\n";
					output = equation + ";\n";
					line = output;
				}
				// WHILE FWD
				if (ActionTypeConstants.AC1_WHILE_FWD.equalsIgnoreCase(action_type1)) {
					// if (action_type1.equals("while-fwd")) {
					String forward = actionBuilderLines.getForward();
					String output = forward + ";\n";
					line = output;
				}

				// (DO-WHILE) DO FWD
				if (ActionTypeConstants.AC1_DO_FWD.equalsIgnoreCase(action_type1)) {
					// if (action_type1.equals("do-fwd")) {
					String forward = actionBuilderLines.getForward();
					String output = forward + ";\n";
					line = output;
				}

				// (DO-WHILE) WHILE CONDITION
				if (ActionTypeConstants.AC1_WHILE.equalsIgnoreCase(action_type1)
						&& ActionTypeConstants.AC2_CONDITION.equalsIgnoreCase(action_type2)) {
					// if (action_type1.equals("while") && action_type2.equals("condition")) {
					String condition = actionBuilderLines.getConditions();
					String output = "while(" + condition + ");\n";
					line = output;
				}

				// ======= LIST ========
				if (ActionTypeConstants.AC1_LIST.equalsIgnoreCase(action_type1)) {
					// if (action_type1.equals("list")) {
					String data_type = actionBuilderLines.getDataType();
					String variable_name = actionBuilderLines.getVariableName();
					String assignment = actionBuilderLines.getAssignment();

					StringBuilder output = new StringBuilder();
					output.append(
							"List<" + data_type + "> " + variable_name + " = new ArrayList<" + data_type + ">();\n");

					// CONVERT COMMA SEPERATED STRING INTO ARRAYLIST
					String arr[] = assignment.split("\\s*,\\s*");
					List<String> lists = Arrays.asList(arr);
					for (String list : lists) {
						output.append(variable_name + ".add(\"" + list + "\");\n");
					}
					// System.out.println(op.toString());
					line = output.toString();
				}

				// ======== MAP ==========
				if (ActionTypeConstants.AC1_MAP.equalsIgnoreCase(action_type1)) {
					// if (action_type1.equals("map")) {
					String data_type = actionBuilderLines.getDataType();
					data_type = data_type.replaceAll("\\s+", "");
					String key_type = data_type.substring(0, data_type.indexOf(","));
					String value_type = data_type.substring(data_type.indexOf(",") + 1);

					String variable_name = actionBuilderLines.getVariableName();
					String assignment = actionBuilderLines.getAssignment();
					assignment = assignment.replaceAll("\\s+", "");

					StringBuilder output = new StringBuilder();
					output.append("Map<" + data_type + "> " + variable_name + " = new HashMap<" + data_type + ">();\n");

					// CONVERT COMMA SEPERATED STRING INTO HASHMAP
					String arr[] = assignment.split("(\\},\\{)+");
					List<String> lists = Arrays.asList(arr);
					// int forCount = 0;
					for (String list : lists) {
						// System.out.println(forCount++);
						list = list.replaceAll("[\\{.\\}]+", "");
						// list = list.replaceAll("\\s+", "");

						// NEED MODIFICATION FOR TYPE OF KEY VALUE PAIR (compare with data_type)
						String key = list.substring(0, list.indexOf(","));
						if (key_type.equalsIgnoreCase("String")) {
							key = "\"" + key + "\"";
						}
						String value = list.substring(list.indexOf(",") + 1);
						if (value_type.equalsIgnoreCase("String")) {
							value = "\"" + value + "\"";
						}
						output.append(variable_name + ".put(" + key + "," + value + ");\n");
					}
					// System.out.println(output.toString());
					line = output.toString();
				}

				// ======= SET =========
				if (ActionTypeConstants.AC1_SET.equalsIgnoreCase(action_type1)) {
					// if (action_type1.equals("set")) {
					String data_type = actionBuilderLines.getDataType();
					String variable_name = actionBuilderLines.getVariableName();
					String assignment = actionBuilderLines.getAssignment();

					StringBuilder output = new StringBuilder();
					output.append("Set<" + data_type + "> " + variable_name + " = new HashSet<" + data_type + ">();\n");

					// CONVERT COMMA SEPERATED STRING INTO ARRAYLIST
					String arr[] = assignment.split("\\s*,\\s*");
					List<String> lists = Arrays.asList(arr);
					for (String list : lists) {
						output.append(variable_name + ".add(\"" + list + "\");\n");
					}
					// System.out.println(op.toString());
					line = output.toString();
				}

//--- =@@@@@@@@@@@@@@ NEED MODIFICATION IN STRING DEPENDENCIES -------
				/*
				 * TABLE UPDATE CODE USING 1. SIMPLE SQL, 2. JDBC-TEMPLATE, 3. HIBERNATE
				 */
				if (ActionTypeConstants.AC1_UPDATE_TABLE.equals(action_type1)) {
					// TABLE UPDATE USING JDBC TEMPLATE
					if (ActionTypeConstants.AC2_JDBC_TEMPLATE.equals(action_type2)) {
						String output = "rn_cff_actionbuilder_utils_dao.updateUsingJdbcTemplate(table_name, column_name, value, id);\n";
						line = output;
					} else if (ActionTypeConstants.AC2_SQL_STATEMENT.equals(action_type2)) {
						// TABLE UPDATE USING CORE JDBC
						String output = "rn_cff_actionbuilder_utils_dao.updateUsingJDBC(table_name, column_name, value, id);\n";
						line = output;
					} else {
						// TABLE UPDATE USING HIBERNATE
						String output = "rn_cff_actionbuilder_utils_dao.updateUsingHibernate(table_name, column_name, value, id);\n";
						line = output;
					}
				}

				// INSERT INTO TABLE insert-tbl-hibernate
//				if (ActionTypeConstants.AC1_INSERT_TABLE.equals(action_type1)) {
//					
//				}

				// FILE STRING REPLACE
				if (ActionTypeConstants.AC1_STRING_REPLACE.equals(action_type1)
						&& ActionTypeConstants.AC2_FILE_STRING_REPLACE.equals(action_type2)) {
					// NEED MOD
					for (int s : seqs) {
						System.out.println("ALL SEQ FOUND = " + s);
					}
					// RealNetUtils -- || -- rn_cff_actionbuilder_utils_dao
					String output = "rn_cff_actionbuilder_utils_dao.fileStringReplace(path, oldString, newString);\n";
					line = output;
				}
				// VARIABLE STRING REPLACE
				if (ActionTypeConstants.AC1_STRING_REPLACE.equals(action_type1)
						&& ActionTypeConstants.AC2_VARIABLE_STRING_REPLACE.equals(action_type2)) {
					String output = "str = rn_cff_actionbuilder_utils_dao.variableStringReplace(str, oldString, newString);\n";
					line = output;
				}

				// STRING APPEND IN A VARIABLE
				if (ActionTypeConstants.AC1_STRING_APPEND.equals(action_type1)
						&& ActionTypeConstants.AC2_VARIABLE_STRING_APPEND.equals(action_type2)) {
					String output = "str = rn_cff_actionbuilder_utils_dao.stringAppendInVariable(str, newString);\n";
					line = output;
				}

				// STRING APPEND IN A FILE
				if (ActionTypeConstants.AC1_STRING_APPEND.equals(action_type1)
						&& ActionTypeConstants.AC2_FILE_STRING_APPEND.equals(action_type2)) {
					String output = "rn_cff_actionbuilder_utils_dao.stringAppendInFile(path, str);\n";
					line = output;
				}

				/*
				 * MODEL, DAO CODE
				 */
				if (ActionTypeConstants.AC1_MODEL.equals(action_type1)) {
					String model_name = actionBuilderLines.getVariableName();
					StringBuilder output = new StringBuilder();

					output.append(model_name + "  model_obj = new " + model_name + "();\n");
					output.append(setter_code);
					line = output.toString();
				}

				if (ActionTypeConstants.AC1_DAO.equals(action_type1)) {
					String dao_name = actionBuilderLines.getVariableName();
					StringBuilder output = new StringBuilder();
					output.append(dao_name.toLowerCase() + ".save(model_obj);\n");
					line = output.toString();
				}

			} // PARENT IF END

			code.append(line + "\n");
		}
		// System.out.println(code.toString());

		try {

			BufferedWriter bw = new BufferedWriter(new FileWriter(file));
			bw.write(code.toString());
			bw.close();
		} catch (FileNotFoundException e) {
			log.error("Exception Caught..");
			e.getMessage();
			ErrorPojo errorPojo = new ErrorPojo();
			Error error = new Error();
			error.setTitle(Constant.ACTION_BUILDER_API_TITLE);
			error.setMessage(Constant.ACTION_CFF_DATA_FAILURE);
			errorPojo.setError(error);
			return new ResponseEntity<ErrorPojo>(errorPojo, HttpStatus.EXPECTATION_FAILED);
		}
		SuccessPojo successPojo = new SuccessPojo();
		Success success = new Success();
		success.setTitle(Constant.ACTION_BUILDER_API_TITLE);
		success.setMessage(Constant.ACTION_CFF_DATA_SUCCESS);
		successPojo.setSuccess(success);
		return new ResponseEntity<SuccessPojo>(successPojo, HttpStatus.OK);
		// return new ModelAndView("redirect:cff_action_builder_line_grid_view?id=" +
		// header_id);

	}

	// READ PROPERTIES FILE AND POPULATE DROPDOWN IN ACTION BUILDER FORM
	@GetMapping(value = "/cff_dropdown")
	public @ResponseBody PropertiesDTO fieldsDropdownProperties() throws IOException {

		/*
		 * {form_code}.properties file name implementation : need to get the form_code
		 * from the JSP and append .properties to it)
		 */
		Resource resource = new ClassPathResource("nil_test1_view.properties");
		Properties props = PropertiesLoaderUtils.loadProperties(resource);

		String table_name = props.getProperty("table_name");
		String controller_name = props.getProperty("controller_name");
		String dao_name = props.getProperty("dao_name");
		String dao_impl_name = props.getProperty("dao_impl_name");
		String service_name = props.getProperty("service_name");
		String service_impl_name = props.getProperty("service_impl_name");
		String jsp_name = props.getProperty("jsp_name");
		String form_code = props.getProperty("form_code");
		String fields = props.getProperty("fields");
		String line_table_name = props.getProperty("line_table_name");
		String line_fields = props.getProperty("line_fields");

		System.out.println(table_name + "\n" + controller_name + "\n" + dao_name + "\n" + dao_impl_name + "\n"
				+ service_name + "\n" + service_impl_name + "\n" + jsp_name + "\n" + form_code + "\n" + fields);

		String arr[] = fields.split("\\s*,\\s*");
		List<String> fieldList = Arrays.asList(arr);

		PropertiesDTO propertiesDTO = new PropertiesDTO();
		propertiesDTO.setController_name(controller_name);
		propertiesDTO.setTable_name(table_name);
		propertiesDTO.setDao_name(dao_name);
		propertiesDTO.setDao_impl_name(dao_impl_name);
		propertiesDTO.setService_name(service_name);
		propertiesDTO.setService_impl_name(service_impl_name);
		propertiesDTO.setFieldList(fieldList);

		if (line_table_name != null && !line_table_name.isEmpty())
			propertiesDTO.setLine_table_name(line_table_name);
		if (line_fields != null && !line_fields.isEmpty()) {
			String[] arr2 = line_fields.split("\\s*,\\s*");
			List<String> lineFieldList = Arrays.asList(arr2);
			propertiesDTO.setLine_fields(lineFieldList);
		}
		return propertiesDTO;
	}

}
