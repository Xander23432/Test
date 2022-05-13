package com.realnet.ncso.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.realnet.ncso.entity.Items;
import com.realnet.ncso.repository.ItemsRepository;
import com.realnet.ncso.service.ItemService;

@Service
public class ItemsServiceImpl implements ItemService {
	
	@Autowired
	private ItemsRepository itemsRepository;

	@Override
	public Items addToDb(Items attachments) {
		Items save = this.itemsRepository.save(attachments);
		return save;
	}

	@Override
	public Items updateToDb(Items attachments) {
		Items save = this.itemsRepository.save(attachments);
		return save;
	}

	@Override
	public Items getOneAttchById(Long id) {
		Optional<Items> findById = this.itemsRepository.findById(id);
		return findById.get();
	}

	@Override
	public List<Items> getAllAttach() {
		List<Items> findAll = this.itemsRepository.findAll();
		return findAll;
	}

	@Override
	public void deleteAttchById(Long id) {
		this.itemsRepository.deleteById(id);
		System.out.println("Deletion successfull...");
	}

}
