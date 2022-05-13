package com.realnet.icon.service;

import com.realnet.icon.entity.ArchivedEntity;

public interface ArchivedService {

	public ArchivedEntity getById(int id);
	ArchivedEntity save(ArchivedEntity archived) ;
    boolean deleteById(int id);
    public boolean IsArchived(int projectId, Long userId);
}
