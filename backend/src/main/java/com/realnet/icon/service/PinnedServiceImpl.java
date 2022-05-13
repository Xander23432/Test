package com.realnet.icon.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.realnet.exceptions.ResourceNotFoundException;
import com.realnet.icon.entity.PinnedEntity;
import com.realnet.icon.repository.PinnedRepository;
import com.realnet.utils.Constant;

@Service
public class PinnedServiceImpl implements PinnedService {
	
	@Autowired
	private PinnedRepository pinnedRepo;

	@Override
	public PinnedEntity getById(int id) {
		// TODO Auto-generated method stub
		PinnedEntity pin= pinnedRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("pinned not found :: " + id));
		return pin;
	}

	@Override
	public PinnedEntity save(PinnedEntity pinned) {
		// TODO Auto-generated method stub
		PinnedEntity savedPin=pinnedRepo.save(pinned);
		return savedPin;
	}

	@Override
	public boolean deleteById(int id) {
		if (!pinnedRepo.existsById(id)) {
			throw new ResourceNotFoundException(Constant.NOT_EXIST_EXCEPTION);
		}
		PinnedEntity bcf_extractor = pinnedRepo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(Constant.NOT_FOUND_EXCEPTION + " :" + id));
		pinnedRepo.delete(bcf_extractor);
		return true;
	}

	@Override
	public int getPinnedCount(int projectId) {
		// TODO Auto-generated method stub
		int count=pinnedRepo.countPinned(projectId);
		return count;
	}

	@Override
	public boolean IsPinned(int projectId, Long userId) {
		// TODO Auto-generated method stub
		int count=pinnedRepo.IsPinned(projectId, userId);
		if(count>0)
		{			
		  return true;
		}
		else return false;
	}
}
