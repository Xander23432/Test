package com.realnet.ncso.service;

import java.util.List;

import com.realnet.ncso.entity.BillingInformation;

public interface BillingInformationService {
	
	public BillingInformation addToDb(BillingInformation billingInformation);
	
	public BillingInformation updateToDb(BillingInformation billingInformation);
	
	public BillingInformation getOneById(Long id);
	
	public List<BillingInformation> getAllFromDb();
	
	public void deleteFromDbById(Long id);

}
