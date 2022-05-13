package com.realnet.icon.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.realnet.exceptions.ResourceNotFoundException;
import com.realnet.icon.entity.FavouriteEntity;
import com.realnet.icon.repository.FavouriteRepository;
import com.realnet.utils.Constant;

@Service
public class FavouriteServiceImpl implements FavouriteService {

//	@Autowired
//	private Rn_ProjectSetup_Service projectSetupService;
//	@Autowired
//	private Rn_ModuleSetup_Service moduleSetupService;
//	
	@Autowired
	private FavouriteRepository favouriteRepo;
	
	@Override
	public FavouriteEntity save(FavouriteEntity favourite) {
		// TODO Auto-generated method stub
		FavouriteEntity savedFav = favouriteRepo.save(favourite);
		return savedFav;
	}

	
	@Override
	public boolean deleteById(int id) {
		if (!favouriteRepo.existsById(id)) {
			throw new ResourceNotFoundException(Constant.NOT_EXIST_EXCEPTION);
		}
		FavouriteEntity bcf_extractor = favouriteRepo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(Constant.NOT_FOUND_EXCEPTION + " :" + id));
		favouriteRepo.delete(bcf_extractor);
		return true;
	}

	
	@Override
	public FavouriteEntity getById(int id) {
		// TODO Auto-generated method stub
		FavouriteEntity fav = favouriteRepo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("favourite not found :: " + id));
		return fav;
	}
	
	@Override
	public int getFavCount(int projectId)
	{
		int count=favouriteRepo.countFav(projectId);
		return count;
	}


	@Override
	public boolean IsFav(int projectId, Long userId) {
		// TODO Auto-generated method stub
		int count=favouriteRepo.IsFav(projectId, userId);
		if(count>0) {			
		return true;
		}
		else return false;
	}
	


}
