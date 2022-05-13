package com.realnet.icon.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.realnet.exceptions.ResourceNotFoundException;
import com.realnet.icon.entity.AgedEntity;
import com.realnet.icon.repository.AgedRepository;
import com.realnet.utils.Constant;

@Service
public class AgedServiceImpl  implements AgedService{

	@Autowired
	private AgedRepository agedRepo;
	
	@Override
	public AgedEntity getById(int id) {
		// TODO Auto-generated method stub
		AgedEntity aged=agedRepo.findById(id)
				.orElseThrow(()-> new ResourceNotFoundException("aged not find ::" + id));
		return aged;
	}

	@Override
	public AgedEntity save(AgedEntity aged) {
		// TODO Auto-generated method stub
		AgedEntity savedAged=agedRepo.save(aged);
		return savedAged;
	}

	@Override
	public boolean deleteById(int id) {
		// TODO Auto-generated method stub
		if(!agedRepo.existsById(id)) {
			throw new ResourceNotFoundException(Constant.NOT_EXIST_EXCEPTION);
		}
		AgedEntity bcf_extractor = agedRepo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(Constant.NOT_FOUND_EXCEPTION + " :" + id));
		agedRepo.delete(bcf_extractor);
		return true;
	}

	@Override
	public boolean IsAged(int projectId, Long userId) {
		// TODO Auto-generated method stub
		int count=agedRepo.IsAged(projectId, userId);
		if(count>0)
		{			
		  return true;
		}
		else return false;
	}
}
