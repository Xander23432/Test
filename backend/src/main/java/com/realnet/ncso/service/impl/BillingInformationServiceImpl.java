package com.realnet.ncso.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.realnet.ncso.entity.BillingInformation;
import com.realnet.ncso.repository.BillingInformationRepository;
import com.realnet.ncso.service.BillingInformationService;

@Service
public class BillingInformationServiceImpl implements BillingInformationService {
	
	@Autowired
	private BillingInformationRepository billingInformationRepository;

	@Override
	public BillingInformation addToDb(BillingInformation billingInformation) {
		BillingInformation save = this.billingInformationRepository.save(billingInformation);
		return save;
	}

	@Override
	public BillingInformation updateToDb(BillingInformation billingInformation) {
		BillingInformation save = this.billingInformationRepository.save(billingInformation);
		return save;
	}

	@Override
	public BillingInformation getOneById(Long id) {
		Optional<BillingInformation> findById = this.billingInformationRepository.findById(id);
		return findById.get();
	}

	@Override
	public List<BillingInformation> getAllFromDb() {
		List<BillingInformation> findAll = this.billingInformationRepository.findAll();
		return findAll;
	}

	@Override
	public void deleteFromDbById(Long id) {
		this.billingInformationRepository.deleteById(id);
		System.out.println("Deletion Success..." + id);
	}

}
