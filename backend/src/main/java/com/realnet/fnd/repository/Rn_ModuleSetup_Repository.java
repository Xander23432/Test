package com.realnet.fnd.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.realnet.fnd.entity.Rn_Module_Setup;

@Repository
public interface Rn_ModuleSetup_Repository extends JpaRepository<Rn_Module_Setup, Integer> {
	// for pagination
	Page<Rn_Module_Setup> findAll(Pageable p);
	
	// LIST OF MODULES IN A PROJECT (PAGINATED DATA)
	@Query(value = "SELECT * FROM rn_module_setup WHERE PROJECT_ID=:projectId", nativeQuery = true)
	Page<Rn_Module_Setup> findProjectModules(@Param("projectId") Integer projectId, Pageable p);
	
	// LIST OF MODULES IN A PROJECT
	@Query(value = "SELECT * FROM rn_module_setup WHERE PROJECT_ID=:projectId", nativeQuery = true)
	List<Rn_Module_Setup> findProjectModulesForDropDown(@Param("projectId") Integer projectId);
	
	
	//NOT WORKING
//	@Query(value = "SELECT ID, MODULE_NAME FROM RN_MODULE_SETUP", nativeQuery = true)
//	List<Rn_Module_Setup> findModulesForDropDown();
//	// LIST OF MODULES IN A PROJECT
//	@Query(value = "SELECT ID, MODULE_NAME FROM RN_MODULE_SETUP WHERE PROJECT_ID=:projectId", nativeQuery = true)
//	List<Rn_Module_Setup> findProjectModulesForDropDown(@Param("projectId") Integer projectId);
}
