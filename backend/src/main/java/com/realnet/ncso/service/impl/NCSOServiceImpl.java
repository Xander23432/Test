package com.realnet.ncso.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.realnet.ncso.entity.NonContainerServiceOrder;
import com.realnet.ncso.repository.NCSORepository;
import com.realnet.ncso.service.NCSOService;

@Service
public class NCSOServiceImpl implements NCSOService {
	
	@Autowired
	private NCSORepository nCSORepository;

	@Override
	public NonContainerServiceOrder addToDB(NonContainerServiceOrder nonContainerServiceOrder) {
		NonContainerServiceOrder order = this.nCSORepository.save(nonContainerServiceOrder);
		return order;
	}

	@Override
	public NonContainerServiceOrder updateToDB(NonContainerServiceOrder nonContainerServiceOrder) {
		NonContainerServiceOrder order = this.nCSORepository.save(nonContainerServiceOrder);
		return order;
	}

	@Override
	public NonContainerServiceOrder getOneFromDBById(Long id) {
		Optional<NonContainerServiceOrder> findById = this.nCSORepository.findById(id);
		return findById.get();
	}

	@Override
	public List<NonContainerServiceOrder> getAllFromDB() {
		List<NonContainerServiceOrder> findAll = this.nCSORepository.findAll();
		return findAll;
	}

	@Override
	public void deleteFromDBById(Long id) {
		this.nCSORepository.deleteById(id);
		System.out.println("Deletion successfully...");
	}

}
