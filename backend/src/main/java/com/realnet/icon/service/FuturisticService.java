package com.realnet.icon.service;

import com.realnet.icon.entity.FuturisticEntity;

public interface FuturisticService {

	public FuturisticEntity getById(int id);
	FuturisticEntity save(FuturisticEntity futuristic);
	boolean deleteById(int id);
	public int getFuturisticCount(int projectId);
    public boolean IsFuturistic(int projectId, Long userId);
}
