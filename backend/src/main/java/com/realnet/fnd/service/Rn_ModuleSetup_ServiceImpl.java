package com.realnet.fnd.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.realnet.exceptions.ResourceNotFoundException;
import com.realnet.fnd.entity.DropDownDTO;
import com.realnet.fnd.entity.Rn_Module_Setup;
import com.realnet.fnd.repository.Rn_ModuleSetup_Repository;
import com.realnet.rb.entity.Rn_report_builder;
import com.realnet.utils.Constant;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class Rn_ModuleSetup_ServiceImpl implements Rn_ModuleSetup_Service {

	@Autowired
	private Rn_ProjectSetup_Service projectSetupService;

	@Autowired
	private Rn_ModuleSetup_Repository moduleSetupRepository;

	@Override
	public List<Rn_Module_Setup> getAll() {
		return moduleSetupRepository.findAll();
	}

	@Override
	public Page<Rn_Module_Setup> getAll(Pageable page) {
		return moduleSetupRepository.findAll(page);
	}
	
	@Override
	public Page<Rn_Module_Setup> getProjectModules(int projectId, Pageable page) {
		return moduleSetupRepository.findProjectModules(projectId, page);
	}

	@Override
	public Rn_Module_Setup getById(int id) {
		Rn_Module_Setup bcf_extractor = moduleSetupRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(Constant.NOT_FOUND_EXCEPTION + " :" + id));
		return bcf_extractor;
	}
	
//	@Override
//	public Rn_report_builder getReportById(int id) {
//		Rn_report_builder bcf_extractor = moduleSetupRepository.findById(id)
//				.orElseThrow(() -> new ResourceNotFoundException(Constant.NOT_FOUND_EXCEPTION + " :" + id));
//		return bcf_extractor;
//	}

	@Override
	public Rn_Module_Setup save(Rn_Module_Setup moduleRequest) {
		Rn_Module_Setup savedModule = moduleSetupRepository.save(moduleRequest);
		return savedModule;
	}

	@Override
	public Rn_Module_Setup updateById(int id, Rn_Module_Setup moduleRequest) {
		Rn_Module_Setup oldModule = moduleSetupRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(Constant.NOT_FOUND_EXCEPTION + " :" + id));
		oldModule.setModuleName(moduleRequest.getModuleName());
		oldModule.setModulePrefix(moduleRequest.getModulePrefix());
		oldModule.setDescription(moduleRequest.getDescription());
	oldModule.setTechnologyStack(moduleRequest.getTechnologyStack());
		oldModule.setCopyTo(moduleRequest.getCopyTo());
//		log.debug("Projects -> {}", moduleRequest.getProject()); // return null
//		oldModule.setProject(moduleRequest.getProject()); // no need
		final Rn_Module_Setup updatedModule = moduleSetupRepository.save(oldModule);
		return updatedModule;
	}

	@Override
	public boolean deleteById(int id) {
		if (!moduleSetupRepository.existsById(id)) {
			throw new ResourceNotFoundException(Constant.NOT_EXIST_EXCEPTION);
		}
		Rn_Module_Setup bcf_extractor = moduleSetupRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(Constant.NOT_FOUND_EXCEPTION + " :" + id));
		moduleSetupRepository.delete(bcf_extractor);
		return true;
	}

	@Override
	public List<DropDownDTO> getModulesForDropDown() {
		//List<Rn_Module_Setup> moduleList = moduleSetupRepository.findModulesForDropDown();
		List<Rn_Module_Setup> moduleList = this.getAll();
		ArrayList<DropDownDTO> dtos = new ArrayList<DropDownDTO>();
		for (Rn_Module_Setup module : moduleList) {
			DropDownDTO dto = new DropDownDTO();
			dto.setId(module.getId());
			dto.setName(module.getModuleName());
			dtos.add(dto);
		}
		return dtos;
	}

	@Override
	public List<DropDownDTO> getProjectModulesForDropDown(int projectId) {
		List<Rn_Module_Setup> moduleList = moduleSetupRepository.findProjectModulesForDropDown(projectId);

		ArrayList<DropDownDTO> dtos = new ArrayList<DropDownDTO>();
		for (Rn_Module_Setup module : moduleList) {
			DropDownDTO dto = new DropDownDTO();
			dto.setId(module.getId());
			dto.setName(module.getModuleName());
			dtos.add(dto);
		}
		return dtos;
	}

}
