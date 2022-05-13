package com.realnet.masters.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.realnet.masters.repository.BeLeaseMasterRepo;

@Service
public class BeLeaseMasterService {
	private BeLeaseMasterRepo beLeaseMasterRepo;
	@Autowired
	public BeLeaseMasterService(BeLeaseMasterRepo beLeaseMasterRepo) {
		super();
		this.beLeaseMasterRepo = beLeaseMasterRepo;
	}
//	public List<BeLe>
	
}
