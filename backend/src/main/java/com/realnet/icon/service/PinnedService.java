package com.realnet.icon.service;

import com.realnet.icon.entity.PinnedEntity;

public interface PinnedService {

	 public PinnedEntity getById(int id);
	 PinnedEntity save(PinnedEntity pinned) ;
     boolean deleteById(int id);
     public int getPinnedCount(int projectId);
     public boolean IsPinned(int projectId, Long userId);
}
