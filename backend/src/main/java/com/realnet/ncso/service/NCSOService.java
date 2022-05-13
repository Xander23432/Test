package com.realnet.ncso.service;

import java.util.List;

import com.realnet.ncso.entity.NonContainerServiceOrder;

public interface NCSOService {
	
	// add to database
	public NonContainerServiceOrder addToDB(NonContainerServiceOrder nonContainerServiceOrder);
	
	// update
	public NonContainerServiceOrder updateToDB(NonContainerServiceOrder nonContainerServiceOrder);
	
	// get one
	public NonContainerServiceOrder getOneFromDBById(Long id);
	
	// get all
	public List<NonContainerServiceOrder> getAllFromDB();
	
	// delete 
	public void deleteFromDBById(Long id);
}
