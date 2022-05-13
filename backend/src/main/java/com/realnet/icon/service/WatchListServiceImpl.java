package com.realnet.icon.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.realnet.exceptions.ResourceNotFoundException;
import com.realnet.icon.entity.WatchListEntity;
import com.realnet.icon.repository.WatchListRepository;
import com.realnet.utils.Constant;

@Service
public class WatchListServiceImpl implements WatchListService {

	@Autowired
	private WatchListRepository watchListRepo;
	
	@Override
	public WatchListEntity getById(int id) {
		// TODO Auto-generated method stub
		WatchListEntity watch=watchListRepo.findById(id)
				.orElseThrow(() ->new  ResourceNotFoundException("watchlist not found ::" + id));
		return watch;
	}

	@Override
	public WatchListEntity save(WatchListEntity watch) {
		// TODO Auto-generated method stub
		WatchListEntity savedWatch=watchListRepo.save(watch);
		return savedWatch;
	}

	@Override
	public boolean deleteById(int id) {
		// TODO Auto-generated method stub
		if(!watchListRepo.existsById(id)) {
			throw new ResourceNotFoundException(Constant.NOT_EXIST_EXCEPTION);
		}
		WatchListEntity bcf_extractor = watchListRepo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(Constant.NOT_FOUND_EXCEPTION + " :" + id));
		watchListRepo.delete(bcf_extractor);
		return true;
	}

	@Override
	public int getWatchlistCount(int projectId) {
		// TODO Auto-generated method stub
		int count=watchListRepo.countWatchlist(projectId);
		return count;
	}

	@Override
	public boolean IsWatchlist(int projectId, Long userId) {
		// TODO Auto-generated method stub
				int count=watchListRepo.IsWatchlist(projectId, userId);
				if(count>0)
				{			
				  return true;
				}
				else return false;
			}

}
