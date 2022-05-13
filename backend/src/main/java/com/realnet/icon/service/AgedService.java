package com.realnet.icon.service;

import com.realnet.icon.entity.AgedEntity;

public interface AgedService {

	public AgedEntity getById(int id);
	AgedEntity save(AgedEntity aged) ;
    boolean deleteById(int id);
    public boolean IsAged(int projectId, Long userId);
}
