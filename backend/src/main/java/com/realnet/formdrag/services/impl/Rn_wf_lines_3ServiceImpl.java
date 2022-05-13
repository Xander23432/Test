package com.realnet.formdrag.services.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.realnet.formdrag.entity.Rn_wf_lines_3;
import com.realnet.formdrag.repository.Rn_wf_lines_3Repository;
import com.realnet.formdrag.services.Rn_wf_lines_3Service;

@Service
public class Rn_wf_lines_3ServiceImpl implements Rn_wf_lines_3Service {
	
	@Autowired
	private Rn_wf_lines_3Repository repo;

	@Override
	public Rn_wf_lines_3 addToDB(Rn_wf_lines_3 rn_wf_lines_3) {
		Rn_wf_lines_3 save = this.repo.save(rn_wf_lines_3);
		return save;
	}


	@Override
	public Rn_wf_lines_3 getOneFromDBById(Long id) {
		Optional<Rn_wf_lines_3> findById = this.repo.findById(id);
		return findById.get();
	}


	@Override
	public Rn_wf_lines_3 updateInDB(Rn_wf_lines_3 rn_wf_lines_3) {
		Rn_wf_lines_3 save = this.repo.save(rn_wf_lines_3);
		return save;
	}


	@Override
	public List<Rn_wf_lines_3> getAll() {
		List<Rn_wf_lines_3> findAll = this.repo.findAll();
		return findAll;
	}


	@Override
	public void deleteOneFromDBById(Long id) {
		this.repo.deleteById(id);
		
	}
	
	
}
