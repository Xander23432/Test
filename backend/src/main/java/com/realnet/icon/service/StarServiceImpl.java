package com.realnet.icon.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.realnet.exceptions.ResourceNotFoundException;
import com.realnet.icon.entity.StarEntity;
import com.realnet.icon.repository.StarRepository;
import com.realnet.utils.Constant;

@Service
public class StarServiceImpl implements StarService{

	@Autowired
	private StarRepository starRepo;
	
	@Override
	public StarEntity getById(int id) {
		// TODO Auto-generated method stub
		StarEntity star=starRepo.findById(id).
				orElseThrow(() -> new ResourceNotFoundException("pinned not found :: " + id));
		return star;
	}

	@Override
	public StarEntity save(StarEntity star) {
		// TODO Auto-generated method stub
		StarEntity savedStar=starRepo.save(star);
		return savedStar;
	}

	@Override
	public boolean deleteById(int id) {
		// TODO Auto-generated method stub
		if (!starRepo.existsById(id)) {
			throw new ResourceNotFoundException(Constant.NOT_EXIST_EXCEPTION);
		}
		StarEntity bcf_extractor = starRepo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(Constant.NOT_FOUND_EXCEPTION + " :" + id));
		starRepo.delete(bcf_extractor);
		return true;
	}

	@Override
	public int getStarCount(int projectId) {
		// TODO Auto-generated method stub
		int count=starRepo.countStar(projectId);
		return count;
	}

	@Override
	public boolean IsStar(int projectId, Long userId) {
		// TODO Auto-generated method stub
		int count=starRepo.IsStar(projectId, userId);
		if(count>0)
		{			
		  return true;
		}
		else return false;
	}

}
