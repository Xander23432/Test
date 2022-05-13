package com.realnet.ncso.service.impl1;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.realnet.ncso.entity1.EbsInvoiceType;
import com.realnet.ncso.repository1.EbsInvoiceTypeRepository;

@Service
public class EbsInvoiceTypeServiceImpl {
	private EbsInvoiceTypeRepository ebsInvoiceTypeRepository;
	@Autowired
	public EbsInvoiceTypeServiceImpl(EbsInvoiceTypeRepository ebsInvoiceTypeRepository) {
		super();
		this.ebsInvoiceTypeRepository = ebsInvoiceTypeRepository;
	}
	public List<Object> getAllApproved(Pageable page){
		return ebsInvoiceTypeRepository.getApproved(page).getContent();
	}
	public List<EbsInvoiceType> getAll(){
		return ebsInvoiceTypeRepository.findAll();
	}
	
}
