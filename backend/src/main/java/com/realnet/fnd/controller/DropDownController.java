package com.realnet.fnd.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.realnet.fnd.entity.DropDownDTO;
import com.realnet.fnd.service.Rn_ModuleSetup_Service;
import com.realnet.fnd.service.Rn_ProjectSetup_Service;
import com.realnet.wfb.entity.WFDropDownDTO;
import com.realnet.wfb.service.Rn_WireFrame_Service;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(value = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(tags = { "DropDown Apis" })
public class DropDownController {

	@Autowired
	private Rn_WireFrame_Service wireframeService;

	@Autowired
	private Rn_ModuleSetup_Service moduleService;

	@Autowired
	private Rn_ProjectSetup_Service projectService;

	// get project list (id and name)
	@ApiOperation(value = "Get Module List For DropDown", response = DropDownDTO.class)
	@GetMapping("/project-list")
	@ResponseBody
	public List<DropDownDTO> getProjectList() {
		List<DropDownDTO> projects = projectService.getprojectsForDropDown();
		return projects;
	}

	// get module list (id and name)
	@ApiOperation(value = "Get Module List For DropDown", response = DropDownDTO.class)
	@GetMapping("/module-list")
	@ResponseBody
	public List<DropDownDTO> getModuleList() {

		List<DropDownDTO> wireframes = moduleService.getModulesForDropDown();
		return wireframes;
	}

	// get wireframe list (id and name)
	@ApiOperation(value = "Get Wireframe List For DropDown", response = WFDropDownDTO.class)
	@GetMapping("/wireframe-list")
	@ResponseBody
	public List<DropDownDTO> getWireFrameList() {
		// List<Rn_Bcf_Technology_Stack> technologyStack =
		// technologyStackService.getAll();
		List<DropDownDTO> wireframes = wireframeService.getFbHeadersForDropDown();
		return wireframes;
	}

	// ========== DEPENDENT DROPDOWNS ================
	// get module list (id and name)
	@ApiOperation(value = "Get Module List In A Project For DropDown", response = DropDownDTO.class)
	@GetMapping("/project-modules/{projectId}")
	@ResponseBody
	public List<DropDownDTO> getProjectModuleList(@PathVariable("projectId") Integer projectId) {

		List<DropDownDTO> modules = moduleService.getProjectModulesForDropDown(projectId);
		return modules;
	}

	// get module list (id and name)
	@ApiOperation(value = "Get Wireframe List In A Module For DropDown", response = DropDownDTO.class)
	@GetMapping("/module-wireframes/{moduleId}")
	@ResponseBody
	public List<DropDownDTO> getModuleWireFrameList(@PathVariable("moduleId") Integer moduleId) {
		List<DropDownDTO> wireframes = wireframeService.getModuleWireframesForDropDown(moduleId);
		
		
		return wireframes;
	}

}
