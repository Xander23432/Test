package com.realnet.ncso.service;

import java.util.List;

import com.realnet.ncso.entity.ItemQueryCriteria;

public interface ItemQueryCriteriaService {
	
	public ItemQueryCriteria addToDb(ItemQueryCriteria itemQueryCriteria);
	
	public ItemQueryCriteria updateToDb(ItemQueryCriteria itemQueryCriteria);
	
	public ItemQueryCriteria getOneById(Long id);
	
	public List<ItemQueryCriteria> getAllFromDb();
	
	public void deleteFromDbById(Long id);

}
