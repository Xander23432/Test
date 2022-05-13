package com.realnet.icon.service;

import com.realnet.icon.entity.WatchListEntity;

public interface WatchListService {
	
	public WatchListEntity getById(int id);
	WatchListEntity save(WatchListEntity watch) ;
    boolean deleteById(int id);
    public int getWatchlistCount(int projectId);
    public boolean IsWatchlist(int projectId, Long userId);
}
