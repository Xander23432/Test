package com.realnet.masters.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.realnet.masters.entity.BE_ITEM_MASTER;
import com.realnet.masters.repository.BeItemMasterRepo;

@Service
public class BeItemMasterService {
	private BeItemMasterRepo beItemMasterRepo;
	@Autowired
	public BeItemMasterService(BeItemMasterRepo beItemMasterRepo) {
		super();
		this.beItemMasterRepo = beItemMasterRepo;
	}
	public List<BE_ITEM_MASTER> getAll(){
		return beItemMasterRepo.findAll();
	}
	public BE_ITEM_MASTER addOne(BE_ITEM_MASTER be) {
		return beItemMasterRepo.save(be);
	}
	public BE_ITEM_MASTER updateOne(BE_ITEM_MASTER be) {
		return beItemMasterRepo.save(be);
	}
	public BE_ITEM_MASTER getOne(Long id) {
		return beItemMasterRepo.findById(id).orElse(null);
	}
}
