package com.realnet.fnd.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


import com.realnet.fnd.entity.DropDownDTO;
import com.realnet.fnd.entity.Rn_Project_Setup;

public interface Rn_ProjectSetup_Service {
	List<Rn_Project_Setup> getAll();

	Page<Rn_Project_Setup> getAll(Pageable p);

	Rn_Project_Setup getById(int id);

	Rn_Project_Setup save(Rn_Project_Setup project);

	Rn_Project_Setup updateById(int id, Rn_Project_Setup project);

	boolean deleteById(int id);

	// copy uploaded technology into base project folder
	boolean moveUploadedTechnologyToBaseProjectDir(Rn_Project_Setup project);
	
	//List<DropDownDTO> copyProject(String from_tech_stack, String from_object_type, String from_sub_object_type);

	List<DropDownDTO> getprojectsForDropDown();
}
