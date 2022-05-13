package com.realnet.ncso.service.impl1;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.realnet.ncso.entity1.EbsInvType;
import com.realnet.ncso.repository1.EbsInvTypeRepository;

@Service
public class EbsInvTypeServiceImpl {
	private EbsInvTypeRepository ebsInvTypeRepository;
	@Autowired
	public EbsInvTypeServiceImpl(EbsInvTypeRepository ebsInvTypeRepository) {
		super();
		this.ebsInvTypeRepository = ebsInvTypeRepository;
	}
	public List<EbsInvType> getAll(){
		return ebsInvTypeRepository.findAll();
	}
}
