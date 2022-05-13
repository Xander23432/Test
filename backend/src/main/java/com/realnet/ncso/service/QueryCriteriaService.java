package com.realnet.ncso.service;

import java.util.List;

import com.realnet.ncso.entity.QueryCriteria;

public interface QueryCriteriaService {
	
	public QueryCriteria addToDB(QueryCriteria oueryCriteria);
	
	public QueryCriteria updateToDB(QueryCriteria queryCriteria);
	
	public QueryCriteria getOneById(Long id);
	
	public List<QueryCriteria> getAllFromDb();
	
	public void deleteFromDbById(Long id);
}
