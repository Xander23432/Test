package com.realnet.ncso.service;

import java.util.List;

import com.realnet.ncso.entity.CustomerQueryCriteria;

public interface CustomerQueryCriteriaService {
	
	public CustomerQueryCriteria addToDb(CustomerQueryCriteria customerQueryCriteria);
	
	public CustomerQueryCriteria updateToDb(CustomerQueryCriteria customerQueryCriteria);
	
	public CustomerQueryCriteria getOneById(Long id);
	
	public List<CustomerQueryCriteria> getAllFromDb();
	
	public void deleteFromDBById(Long id);

}
