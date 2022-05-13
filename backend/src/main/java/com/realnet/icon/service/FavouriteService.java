package com.realnet.icon.service;

import com.realnet.icon.entity.FavouriteEntity;

public interface FavouriteService {

	 public FavouriteEntity getById(int id);
     FavouriteEntity save(FavouriteEntity favourite) ;
     boolean deleteById(int id);
     public int getFavCount(int projectId);
     public boolean IsFav(int projectId, Long userId);
}
