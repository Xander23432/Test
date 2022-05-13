package com.realnet.icon.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.realnet.exceptions.ResourceNotFoundException;
import com.realnet.icon.entity.FuturisticEntity;
import com.realnet.icon.entity.StarEntity;
import com.realnet.icon.repository.FuturisticRepository;
import com.realnet.utils.Constant;

@Service
public class FuturisticServiceImpl implements FuturisticService {
	
	@Autowired
	private FuturisticRepository futuristicRepo;

	@Override
	public FuturisticEntity getById(int id) {
		// TODO Auto-generated method stub
		FuturisticEntity futuristic= futuristicRepo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("futuristic not found ::" + id));
		return futuristic;
	}

	@Override
	public FuturisticEntity save(FuturisticEntity futuristic) {
		// TODO Auto-generated method stub
		FuturisticEntity savedFuture=futuristicRepo.save(futuristic);
		return savedFuture;
	}

	@Override
	public boolean deleteById(int id) {
		// TODO Auto-generated method stub
		if(!futuristicRepo.existsById(id)) {
			
			throw new ResourceNotFoundException(Constant.NOT_EXIST_EXCEPTION);
		}
		FuturisticEntity bcf_extractor = futuristicRepo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(Constant.NOT_FOUND_EXCEPTION + " :" + id));
		futuristicRepo.delete(bcf_extractor);
		return true;
	}

	@Override
	public int getFuturisticCount(int projectId) {
		// TODO Auto-generated method stub
		int count=futuristicRepo.countFuturistic(projectId);
		return count;
	}

	@Override
	public boolean IsFuturistic(int projectId, Long userId) {
		// TODO Auto-generated method stub
		int count=futuristicRepo.IsFuturistic(projectId, userId);
		if(count>0)
		{			
		  return true;
		}
		else return false;
	}
}
