package com.realnet.fnd.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
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
import org.springframework.web.bind.annotation.RestController;

import com.realnet.fnd.entity.Error;
import com.realnet.fnd.entity.ErrorPojo;
import com.realnet.fnd.entity.Rn_Module_Setup;
import com.realnet.fnd.entity.Rn_Project_Setup;
import com.realnet.fnd.entity.Success;
import com.realnet.fnd.entity.SuccessPojo;
import com.realnet.fnd.entity.projectCopyDTO;
import com.realnet.fnd.response.CustomResponse;
import com.realnet.fnd.service.Rn_ModuleSetup_Service;
import com.realnet.fnd.service.Rn_ProjectSetup_Service;
import com.realnet.users.entity.User;
import com.realnet.users.entity1.AppUser;
import com.realnet.users.service.UserService;
import com.realnet.users.service1.AppUserServiceImpl;
import com.realnet.utils.Constant;
import com.realnet.wfb.entity.Rn_Fb_Header;
import com.realnet.wfb.entity.Rn_Fb_Line;
import com.realnet.wfb.service.Rn_WireFrame_Service;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(value = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(tags = { "Project Setup" })
public class Rn_ProjectSetupController {

	@Autowired
	private AppUserServiceImpl userService;

	@Autowired
	private Rn_ProjectSetup_Service projectSetupService;

	@Autowired
	private Rn_ModuleSetup_Service moduleSetupService;

	@Autowired
	private Rn_WireFrame_Service wireframeService;

	@Value("${projectPath}")
	private String projectPath;

	// NEED MOD
	// GET ALL SORTED AND PAGINATED DATA
	@ApiOperation(value = "List of Projects", response = CustomResponse.class)
	@GetMapping("/project-setup")
	public CustomResponse getProjects(@RequestParam(value = "page", defaultValue = "0", required = false) Integer page,
			@RequestParam(value = "size", defaultValue = "20", required = false) Integer size) {
		// sorted data
//		Pageable paging = PageRequest.of(page, size, Sort.by(Constant.SORT_BY_CREATION_DATE).descending());
		Pageable paging = PageRequest.of(page, size);
		Page<Rn_Project_Setup> result = projectSetupService.getAll(paging);

		CustomResponse resp = new CustomResponse();
		resp.setPageStats(result, true);
		resp.setItems(result.getContent());
		return resp;

	}

	// GET BY ID
	@ApiOperation(value = "Get A Project", response = Rn_Project_Setup.class)
	@GetMapping("/project-setup/{id}")
	public ResponseEntity<?> getProjectDetails(@PathVariable(value = "id") int id) {
		Rn_Project_Setup bcf_tech_stack = projectSetupService.getById(id);
		// Map<String, Rn_Project_Setup> extractorMap =
		// Collections.singletonMap("extractior", bcf_extractor);
		// return new ResponseEntity<Map<String, Rn_Project_Setup>>(extractorMap,
		// HttpStatus.OK);
		// return ResponseEntity.ok().body(bcf_extractor);
		return new ResponseEntity<Rn_Project_Setup>(bcf_tech_stack, HttpStatus.OK);

	}

	@ApiOperation(value = "Add new Project")
	@PostMapping(value = "/project-setup")
	public ResponseEntity<?> saveProject(@Valid @RequestBody Rn_Project_Setup projectReq,HttpSession session1) throws IOException {

		Rn_Project_Setup savedProject = projectSetupService.save(projectReq);
		System.out.println("Data by  save project name:"+savedProject);

		boolean status = projectSetupService.moveUploadedTechnologyToBaseProjectDir(savedProject);
		log.debug("final status {}", status);
		session1.setAttribute("projectId", savedProject.getId());
		if (status) {
			SuccessPojo successPojo = new SuccessPojo();
			Success success = new Success();
			success.setTitle(Constant.PROJECT_SETUP_API_TITLE);
			success.setMessage(Constant.PROJECT_CREATED_SUCCESSFULLY);
			successPojo.setSuccess(success);
			return new ResponseEntity<SuccessPojo>(successPojo, HttpStatus.CREATED);
		} else {
			ErrorPojo errorPojo = new ErrorPojo();
			Error error = new Error();
			error.setTitle(Constant.PROJECT_SETUP_API_TITLE);
			error.setMessage(Constant.PROJECT_CREATED_SUCCESSFULLY);
			errorPojo.setError(error);
			return new ResponseEntity<ErrorPojo>(errorPojo, HttpStatus.EXPECTATION_FAILED);
		}
	}

	// UPDATE
	@ApiOperation(value = "Update A Project", response = Rn_Project_Setup.class)
	@PutMapping("/project-setup/{id}")
	public ResponseEntity<?> updateProject(
			@RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authToken,
			@PathVariable(value = "id") int id, @Valid @RequestBody Rn_Project_Setup project) {
		AppUser loggedInUser = userService.getLoggedInUser();

		// project.setUpdatedBy(loggedInUser.getUserId());

		Rn_Project_Setup updatedProject = projectSetupService.updateById(id, project);

		if (project.getId() != updatedProject.getId()) {
			ErrorPojo errorPojo = new ErrorPojo();
			Error error = new Error();
			error.setTitle(Constant.PROJECT_SETUP_API_TITLE);
			error.setMessage(Constant.PROJECT_NOT_DELETED);
			errorPojo.setError(error);
			return new ResponseEntity<ErrorPojo>(errorPojo, HttpStatus.EXPECTATION_FAILED);
		}

		SuccessPojo successPojo = new SuccessPojo();
		Success success = new Success();
		success.setTitle(Constant.EXTRACTOR_API_TITLE);
		success.setMessage(Constant.PROJECT__UPDATED_SUCCESSFULLY);
		successPojo.setSuccess(success);
		return new ResponseEntity<SuccessPojo>(successPojo, HttpStatus.OK);
	}

	// DELETE
	@DeleteMapping("/project-setup/{id}")
	public ResponseEntity<?> deleteProject(@PathVariable(value = "id") int id) {
		boolean deleted = projectSetupService.deleteById(id);
		if (deleted) {
			SuccessPojo successPojo = new SuccessPojo();
			Success success = new Success();
			success.setTitle(Constant.PROJECT_SETUP_API_TITLE);
			success.setMessage(Constant.PROJECT_DELETED_SUCCESSFULLY);
			successPojo.setSuccess(success);
			return new ResponseEntity<SuccessPojo>(successPojo, HttpStatus.OK);
		} else {
			ErrorPojo errorPojo = new ErrorPojo();
			Error error = new Error();
			error.setTitle(Constant.PROJECT_SETUP_API_TITLE);
			error.setMessage(Constant.PROJECT_NOT_DELETED);
			errorPojo.setError(error);
			return new ResponseEntity<ErrorPojo>(errorPojo, HttpStatus.EXPECTATION_FAILED);
		}
	}

	// ------------- NEED MODIFICATION ---------------
	// COPY PROJECTS
	@ApiOperation(value = "Copy Project", response = Rn_Project_Setup.class)
	@PostMapping("/project-copy")
	public ResponseEntity<?> copyProject(@Valid @RequestBody projectCopyDTO projectCopyDTO) {
		AppUser user = userService.getLoggedInUser();
		Long userId = user.getUserId();
//		Long accId = user.getSys_account().getId();

		// PROJECT COPY LOGIC
		int from_project_id = projectCopyDTO.getFrom_projectId();
		String toProjectName = projectCopyDTO.getTo_projectName();
		String toTechStack = projectCopyDTO.getTo_tech_stack();

		Rn_Project_Setup oldProject = projectSetupService.getById(from_project_id);
		Rn_Project_Setup newProject = new Rn_Project_Setup();
		newProject.setCreatedBy(userId);
//		newProject.setAccountId(accId);
		newProject.setProjectName(toProjectName); // change
		newProject.setProjectPrefix(oldProject.getProjectPrefix());
		newProject.setCopyTo(oldProject.getCopyTo());
		newProject.setTechnologyStack(toTechStack); // change
		newProject.setTechStackId(oldProject.getTechStackId());
		newProject.setDescription(oldProject.getDescription());
		newProject.setDbName(oldProject.getDbName());
		newProject.setDbUserName(oldProject.getDbUserName());
		newProject.setDbPassword(oldProject.getDbPassword());
		newProject.setPortNumber(oldProject.getPortNumber());
		//newProject.setModules(oldProject.getModules());
		
		Rn_Project_Setup savedProject = projectSetupService.save(newProject);
		if (savedProject == null) {
			ErrorPojo errorPojo = new ErrorPojo();
			Error error = new Error();
			error.setTitle(Constant.PROJECT_SETUP_API_TITLE);
			error.setMessage(Constant.PROJECT_COPY_FAILURE);
			errorPojo.setError(error);
			return new ResponseEntity<ErrorPojo>(errorPojo, HttpStatus.EXPECTATION_FAILED);
		}
		
		List<Rn_Module_Setup> modules = oldProject.getModules();
		for (Rn_Module_Setup oldModule : modules) {
			Rn_Module_Setup newModule = new Rn_Module_Setup();
			newModule.setCreatedBy(userId);
//			newModule.setAccountId(accId);
			newModule.setModuleName(oldModule.getModuleName());
			newModule.setDescription(oldModule.getDescription());
			newModule.setModulePrefix(oldModule.getModulePrefix());
			newModule.setCopyTo(oldModule.getCopyTo());
			newModule.setTechnologyStack(oldModule.getTechnologyStack());
			newModule.setProject(savedProject); // change
			Rn_Module_Setup savedModule = moduleSetupService.save(newModule);
			if (savedModule == null) {
				ErrorPojo errorPojo = new ErrorPojo();
				Error error = new Error();
				error.setTitle(Constant.WIREFRAME_API_TITLE);
				error.setMessage(Constant.WIREFRAME_COPY_FAILURE);
				errorPojo.setError(error);
				return new ResponseEntity<ErrorPojo>(errorPojo, HttpStatus.EXPECTATION_FAILED);
			}
			// no need if first statement works
			List<Rn_Fb_Header> rn_fb_header = oldModule.getRn_fb_headers();
			for(Rn_Fb_Header oldHeader: rn_fb_header) {
				Rn_Fb_Header newHeader = new Rn_Fb_Header();
				newHeader.setCreatedBy(userId);
//				newHeader.setAccountId(accId);
				newHeader.setTechStack(oldHeader.getTechStack());
				newHeader.setObjectType(oldHeader.getObjectType());
				newHeader.setSubObjectType(oldHeader.getSubObjectType());
				newHeader.setUiName(oldHeader.getUiName());
				newHeader.setFormType(oldHeader.getFormType());
				newHeader.setFormCode(oldHeader.getFormCode());
				newHeader.setTableName(oldHeader.getTableName());
				newHeader.setLineTableName(oldHeader.getLineTableName());
				newHeader.setMultilineTableName(oldHeader.getMultilineTableName());
				newHeader.setJspName(oldHeader.getJspName());
				newHeader.setControllerName(oldHeader.getControllerName());
				newHeader.setServiceName(oldHeader.getServiceName());
				newHeader.setServiceImplName(oldHeader.getServiceImplName());
				newHeader.setDaoName(oldHeader.getDaoName());
				newHeader.setDaoImplName(oldHeader.getDaoImplName());
				newHeader.setBuild(oldHeader.isBuild());
				newHeader.setUpdated(oldHeader.isUpdated());
				newHeader.setMenuName(oldHeader.getMenuName());
				newHeader.setHeaderName(oldHeader.getHeaderName());
				newHeader.setConvertedTableName(oldHeader.getControllerName());
				newHeader.setModule(savedModule); // change
				//newHeader.setRn_cff_actionBuilder(oldHeader.getRn_cff_actionBuilder());

				Rn_Fb_Header savedHeader = wireframeService.save(newHeader);
				if (savedHeader == null) {
					ErrorPojo errorPojo = new ErrorPojo();
					Error error = new Error();
					error.setTitle(Constant.WIREFRAME_API_TITLE);
					error.setMessage(Constant.WIREFRAME_COPY_FAILURE);
					errorPojo.setError(error);
					return new ResponseEntity<ErrorPojo>(errorPojo, HttpStatus.EXPECTATION_FAILED);
				}
				List<Rn_Fb_Line> oldLines = oldHeader.getRn_fb_lines();
				for (Rn_Fb_Line line : oldLines) {
					Rn_Fb_Line newLine = new Rn_Fb_Line();
					newLine.setCreatedBy(userId);
//					newLine.setAccountId(accId);
					newLine.setForm_type(line.getForm_type());
					newLine.setFieldName(line.getFieldName());
					newLine.setMapping(line.getMapping());
					newLine.setDataType(line.getDataType());
					newLine.setFormCode(line.getFormCode());
					newLine.setKey1(line.getKey1());
					newLine.setType_field(line.getType_field());
					newLine.setMethodName(line.getMethodName());
					newLine.setSeq(line.getSeq());
					newLine.setSection_num(line.getSection_num());
					newLine.setButton_num(line.getButton_num());
					newLine.setType1(line.getType1());
					newLine.setType2(line.getType2());
					newLine.setMandatory(line.isMandatory());
					newLine.setHidden(line.isHidden());
					newLine.setReadonly(line.isReadonly());
					newLine.setDependent(line.isDependent());
					newLine.setDependent_on(line.getDependent_on());
					newLine.setDependent_sp(line.getDependent_sp());
					newLine.setDependent_sp_param(line.getDependent_sp_param());
					newLine.setValidation_1(line.isValidation_1());
					newLine.setVal_type(line.getVal_type());
					newLine.setVal_sp(line.getVal_sp());
					newLine.setVal_sp_param(line.getVal_sp_param());
					newLine.setSequence(line.isSequence());
					newLine.setSeq_name(line.getSeq_name());
					newLine.setSeq_sp(line.getSeq_sp());
					newLine.setSeq_sp_param(line.getSeq_sp_param());
					newLine.setDefault_1(line.isDefault_1());
					newLine.setDefault_type(line.getDefault_type());
					newLine.setDefault_value(line.getDefault_value());
					newLine.setDefault_sp(line.getDefault_sp());
					newLine.setDefault_sp_param(line.getDefault_sp_param());
					newLine.setCalculated_field(line.isCalculated_field());
					newLine.setCal_sp(line.getCal_sp());
					newLine.setCal_sp_param(line.getCal_sp_param());
					newLine.setAdd_to_grid(line.getAdd_to_grid());
					newLine.setSp_name_for_autocomplete(line.getSp_name_for_autocomplete());
					newLine.setSp_for_autocomplete(line.getSp_for_autocomplete());
					newLine.setSp_name_for_dropdown(line.getSp_name_for_dropdown());
					newLine.setSp_for_dropdown(line.getSp_for_dropdown());
					newLine.setLine_table_name(line.getLine_table_name());
					newLine.setLine_table_no(line.getLine_table_no());
					//newLine.setRn_fb_header(line.getRn_fb_header());
					newLine.setRn_fb_header(savedHeader);
					Rn_Fb_Line savedLine = wireframeService.saveLine(newLine);
					if (savedLine == null) {
						ErrorPojo errorPojo = new ErrorPojo();
						Error error = new Error();
						error.setTitle(Constant.WIREFRAME_API_TITLE);
						error.setMessage(Constant.WIREFRAME_COPY_FAILURE);
						errorPojo.setError(error);
						return new ResponseEntity<ErrorPojo>(errorPojo, HttpStatus.EXPECTATION_FAILED);
					}
				}
			}
		}
		SuccessPojo successPojo = new SuccessPojo();
		Success success = new Success();
		success.setTitle(Constant.PROJECT_SETUP_API_TITLE);
		success.setMessage(Constant.PROJECT_COPY_SUCCESS);
		successPojo.setSuccess(success);
		return new ResponseEntity<SuccessPojo>(successPojo, HttpStatus.CREATED);
	}

}
