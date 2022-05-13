package com.realnet.icon.service;

import com.realnet.icon.entity.StarEntity;

public interface StarService {
	
	public StarEntity getById(int id);
	StarEntity save(StarEntity star) ;
    boolean deleteById(int id);
    public int getStarCount(int projectId);
    public boolean IsStar(int projectId, Long userId);
}
