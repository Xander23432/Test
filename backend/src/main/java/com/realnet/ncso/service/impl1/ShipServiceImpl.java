package com.realnet.ncso.service.impl1;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.realnet.ncso.repository1.ShipsRepository;

@Service
public class ShipServiceImpl {
	private ShipsRepository shipsRepository;
	
	@Autowired
	public ShipServiceImpl(ShipsRepository shipsRepository) {
		this.shipsRepository = shipsRepository;
	}
	
	
	public Page<Object> getAll(Pageable page){
//		Page<ShipMix> p = shipsRepository.getAll(page);
		Page<Object> o = shipsRepository.getAll(page);
		return o;
	}
	@Cacheable(cacheNames = {"user","user"})
	public List<Object> getAllSearch(String id){
		List<Object> o = shipsRepository.getAllSearch(id);
		return o;
	}
	
	public Optional<Object> getOneNovis(String name,String inVoyNbr){
		return shipsRepository.getOneNovis(name, inVoyNbr);
	}
	@Cacheable(cacheNames = {"user","user"})
	public List<Object> getAllPmod(String search){
		List<Object>  o = shipsRepository.getAllPmod(search);
		return o;
	}
	public Optional<Object> getOnePmod(String vcn){
		return shipsRepository.getOnePmod(vcn);
	}
	
}
