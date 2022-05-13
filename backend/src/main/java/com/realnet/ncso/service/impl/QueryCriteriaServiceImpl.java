package com.realnet.ncso.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.realnet.ncso.entity.QueryCriteria;
import com.realnet.ncso.repository.QueryCriteriaRepository;
import com.realnet.ncso.service.QueryCriteriaService;

@Service
public class QueryCriteriaServiceImpl implements QueryCriteriaService {
	
	@Autowired
	private QueryCriteriaRepository queryCriteriaRepository;

	@Override
	public QueryCriteria addToDB(QueryCriteria oueryCriteria) {
		QueryCriteria save = this.queryCriteriaRepository.save(oueryCriteria);
		return save;
	}

	@Override
	public QueryCriteria updateToDB(QueryCriteria queryCriteria) {
		QueryCriteria save = this.queryCriteriaRepository.save(queryCriteria);
		return save;
	}

	@Override
	public QueryCriteria getOneById(Long id) {
		Optional<QueryCriteria> findById = this.queryCriteriaRepository.findById(id);
		return findById.get();
	}

	@Override
	public List<QueryCriteria> getAllFromDb() {
		List<QueryCriteria> findAll = this.queryCriteriaRepository.findAll();
		return findAll;
	}

	@Override
	public void deleteFromDbById(Long id) {
		this.queryCriteriaRepository.deleteById(id);
		System.out.println("Deletion Successfull...");
	}

}
