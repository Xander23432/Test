package com.realnet.ncso.service.impl1;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.realnet.ncso.entity1.EbsCustomer;
import com.realnet.ncso.repository1.EbsCustomerRepository;

@Service
public class EbsCustomerServiceImpl {
	private EbsCustomerRepository ebsCustomerRepository;
	@Autowired
	public EbsCustomerServiceImpl(EbsCustomerRepository ebsCustomerRepository) {
		super();
		this.ebsCustomerRepository = ebsCustomerRepository;
	}
	@Cacheable(cacheNames = {"user","user"})
	public List<EbsCustomer> getAll(String search){
		System.out.println("+++++++++++++++++++++++++++++++++++"+search);
		//Page<EbsCustomer> p1 = ebsCustomerRepository.findAllCustom(p,search);
		//System.out.println(p1);
		System.out.println(search);
		List<EbsCustomer>  p2= ebsCustomerRepository.findAllSearch(search);
		System.out.println(p2);
		return p2;
	}
	public Optional<EbsCustomer> getOne(Long id){
		return ebsCustomerRepository.findById(id);
	}
	public List<EbsCustomer> getAll1() {
		// TODO Auto-generated method stub
		return ebsCustomerRepository.findAll();
	}
}
