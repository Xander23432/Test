package com.realnet.icon.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.realnet.exceptions.ResourceNotFoundException;
import com.realnet.icon.entity.ArchivedEntity;
import com.realnet.icon.repository.ArchivedRepository;
import com.realnet.utils.Constant;

@Service
public class ArchivedServiceImpl implements ArchivedService{
	
	@Autowired
	private ArchivedRepository archRepo;

	@Override
	public ArchivedEntity getById(int id) {
		// TODO Auto-generated method stub
		ArchivedEntity arch=archRepo.findById(id)
				.orElseThrow(()-> new ResourceNotFoundException("archived not find ::" + id));
		return arch;
	}

	@Override
	public ArchivedEntity save(ArchivedEntity archived) {
		// TODO Auto-generated method stub
		ArchivedEntity savedArch=archRepo.save(archived);
		return savedArch;
	}

	@Override
	public boolean deleteById(int id) {
		// TODO Auto-generated method stub
		if(!archRepo.existsById(id)) {
			throw new ResourceNotFoundException(Constant.NOT_EXIST_EXCEPTION);
		}
		ArchivedEntity bcf_extractor = archRepo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(Constant.NOT_FOUND_EXCEPTION + " :" + id));
		archRepo.delete(bcf_extractor);
		return true;
	}

	@Override
	public boolean IsArchived(int projectId, Long userId) {
		// TODO Auto-generated method stub
		int count=archRepo.IsArchived(projectId, userId);
		if(count>0)
		{			
		  return true;
		}
		else return false;
	}
}
