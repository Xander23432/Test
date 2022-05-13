package com.realnet.fnd.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.realnet.fnd.entity.DropDownDTO;
import com.realnet.fnd.entity.Rn_Module_Setup;
import com.realnet.rb.entity.Rn_report_builder;

public interface Rn_ModuleSetup_Service {
	List<Rn_Module_Setup> getAll();
	Page<Rn_Module_Setup> getAll(Pageable p);
	Page<Rn_Module_Setup> getProjectModules(int projectId, Pageable p);
	Rn_Module_Setup getById(int id);
	Rn_Module_Setup save(Rn_Module_Setup module);
	Rn_Module_Setup updateById(int id, Rn_Module_Setup module);
	boolean deleteById(int id);
	
	
	List<DropDownDTO> getModulesForDropDown();
	List<DropDownDTO> getProjectModulesForDropDown(int projectId);
	

}
