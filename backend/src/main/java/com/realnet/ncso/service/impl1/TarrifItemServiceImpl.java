package com.realnet.ncso.service.impl1;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.realnet.ncso.entity1.TarrifItem;
import com.realnet.ncso.repository1.tarrifItemRepository;

@Service
public class TarrifItemServiceImpl {
	
	private tarrifItemRepository taRepository;
	@Autowired
	public TarrifItemServiceImpl(tarrifItemRepository taRepository) {
		super();
		this.taRepository = taRepository;
	}
	public Page<TarrifItem> getAll(Pageable page ){
		//Page<TarrifItem> p = taRepository.findAll(pa);
	//	return p.getContent();
		return taRepository.getAllTarrifItem(page);
	}
	public List<TarrifItem> getAllByItemCode(Pageable page,String itemCode){
		return taRepository.getAllTerrifItemFromItemCode(page,itemCode);
	}
	
	public Optional<TarrifItem> getOne(Long id){
		return taRepository.findById(id);
	}
	public Optional<TarrifItem> getOne(String id){
		return taRepository.findByItemCode(id);
	}
}
