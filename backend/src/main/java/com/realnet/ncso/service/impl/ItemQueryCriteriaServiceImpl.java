package com.realnet.ncso.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.realnet.ncso.entity.ItemQueryCriteria;
import com.realnet.ncso.repository.ItemQueryCriteriaRepository;
import com.realnet.ncso.service.ItemQueryCriteriaService;

@Service
public class ItemQueryCriteriaServiceImpl implements ItemQueryCriteriaService {
	
	@Autowired
	private ItemQueryCriteriaRepository itemQueryCriteriaRepository;

	@Override
	public ItemQueryCriteria addToDb(ItemQueryCriteria itemQueryCriteria) {
		ItemQueryCriteria save = this.itemQueryCriteriaRepository.save(itemQueryCriteria);
		return save;
	}

	@Override
	public ItemQueryCriteria updateToDb(ItemQueryCriteria itemQueryCriteria) {
		ItemQueryCriteria save = this.itemQueryCriteriaRepository.save(itemQueryCriteria);
		return save;
	}

	@Override
	public ItemQueryCriteria getOneById(Long id) {
		Optional<ItemQueryCriteria> findById = this.itemQueryCriteriaRepository.findById(id);
		return findById.get();
	}

	@Override
	public List<ItemQueryCriteria> getAllFromDb() {
		List<ItemQueryCriteria> findAll = this.itemQueryCriteriaRepository.findAll();
		return findAll;
	}

	@Override
	public void deleteFromDbById(Long id) {
		this.itemQueryCriteriaRepository.deleteById(id);
		System.out.println("Deletion Success...");
	}

}
