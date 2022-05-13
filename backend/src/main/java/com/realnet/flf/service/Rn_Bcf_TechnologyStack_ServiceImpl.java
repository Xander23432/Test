package com.realnet.flf.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.realnet.flf.entity.ActiveTechStack_DTO;
//import com.realnet.codeextractor.entity.ActiveTechStack_DTO;
import com.realnet.flf.entity.Rn_Bcf_Technology_Stack;
import com.realnet.flf.repository.Rn_Bcf_TechnologyStack_Repository;
import com.realnet.exceptions.ResourceNotFoundException;
import com.realnet.utils.Constant;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class Rn_Bcf_TechnologyStack_ServiceImpl implements Rn_Bcf_TechnologyStack_Service {

	@Autowired
	private Rn_Bcf_TechnologyStack_Repository technologStackRepository;

	@Override
	public List<Rn_Bcf_Technology_Stack> getAll() {
		return technologStackRepository.findAll();
	}

	@Override
	public Page<Rn_Bcf_Technology_Stack> getAll(Pageable page) {
		return technologStackRepository.findAll(page);
	}

	@Override
	public Rn_Bcf_Technology_Stack getById(int id) {
		Rn_Bcf_Technology_Stack bcf_extractor = technologStackRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(Constant.NOT_FOUND_EXCEPTION + id));
		return bcf_extractor;
	}

	@Override
	public Rn_Bcf_Technology_Stack save(Rn_Bcf_Technology_Stack bcf_extractor) {
		Rn_Bcf_Technology_Stack savedTechnology = technologStackRepository.save(bcf_extractor);
		return savedTechnology;
	}

	@Override
	public Rn_Bcf_Technology_Stack updateById(int id, Rn_Bcf_Technology_Stack technologyRequest) {
		Rn_Bcf_Technology_Stack old_extractor = technologStackRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(Constant.NOT_FOUND_EXCEPTION + id));

		old_extractor.setTech_stack(technologyRequest.getTech_stack());
		old_extractor.setTech_stack_key(technologyRequest.getTech_stack_key());
		old_extractor.setBase_prj_file_name(technologyRequest.getBase_prj_file_name());
		old_extractor.setTags(technologyRequest.getTags());
		old_extractor.setActive(technologyRequest.isActive());

		final Rn_Bcf_Technology_Stack updated_technology = technologStackRepository.save(old_extractor);
		return updated_technology;
	}

	@Override
	public boolean deleteById(int id) {
		if (!technologStackRepository.existsById(id)) {
			throw new ResourceNotFoundException(Constant.NOT_EXIST_EXCEPTION);
		}
		Rn_Bcf_Technology_Stack bcf_extractor = technologStackRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(Constant.NOT_FOUND_EXCEPTION + id));
		technologStackRepository.delete(bcf_extractor);
		return true;
	}

	@Override
	public List<ActiveTechStack_DTO> getListOfActivateTechnology() {
//		List<Rn_Bcf_Technology_Stack> activeTechList = technologStackRepository.activeTechList();
//		log.debug("ACTIVE TECHNOLOGY LIST : {} ", activeTechList);
//		
		List<Rn_Bcf_Technology_Stack> activeTech = technologStackRepository.activeTechStacks();
		log.debug("ACTIVE TECHNOLOGY : {} ", activeTech);

//		activeTech.forEach(data -> {
//			ActiveTechStack_DTO dto = new ActiveTechStack_DTO();
//			dto.setId(data.getId());
//			dto.setName(data.getBase_prj_file_name());
//		});

		List<ActiveTechStack_DTO> activeTechDTO = new ArrayList<ActiveTechStack_DTO>();
		for (Rn_Bcf_Technology_Stack tech : activeTech) {
			ActiveTechStack_DTO dto = new ActiveTechStack_DTO();
			dto.setId(tech.getId());
			dto.setName(tech.getBase_prj_file_name());
			activeTechDTO.add(dto);
		}
		log.debug("activeTechDTO : {} ", activeTechDTO);
		System.out.println("ServiceImpl : activeTechDTO " + activeTechDTO);
		return activeTechDTO;
	}

}
