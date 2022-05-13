package com.realnet.ncso.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.realnet.ncso.entity.CustomerQueryCriteria;
import com.realnet.ncso.repository.CustomerQueryCriteriaRepository;
import com.realnet.ncso.service.CustomerQueryCriteriaService;

@Service
public class CustomerQueryCriteriaServiceImpl implements CustomerQueryCriteriaService {
	
	@Autowired
	private CustomerQueryCriteriaRepository customerQueryCriteriaRepository;

	@Override
	public CustomerQueryCriteria addToDb(CustomerQueryCriteria customerQueryCriteria) {
		CustomerQueryCriteria save = this.customerQueryCriteriaRepository.save(customerQueryCriteria);
		return save;
	}

	@Override
	public CustomerQueryCriteria updateToDb(CustomerQueryCriteria customerQueryCriteria) {
		CustomerQueryCriteria save = this.customerQueryCriteriaRepository.save(customerQueryCriteria);
		return save;
	}

	@Override
	public CustomerQueryCriteria getOneById(Long id) {
		Optional<CustomerQueryCriteria> findById = this.customerQueryCriteriaRepository.findById(id);
		return findById.get();
	}

	@Override
	public List<CustomerQueryCriteria> getAllFromDb() {
		List<CustomerQueryCriteria> findAll = this.customerQueryCriteriaRepository.findAll();
		return findAll;
	}

	@Override
	public void deleteFromDBById(Long id) {
		this.customerQueryCriteriaRepository.deleteById(id);
		System.out.println("deletion success...");
	}

}
