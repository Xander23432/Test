package com.realnet.icon.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.realnet.icon.entity.PinnedEntity;

@Repository
public interface PinnedRepository extends JpaRepository<PinnedEntity, Integer>{

	 @Query(value="SELECT count(*) from pinned where object_id=?1",nativeQuery = true)
	    int countPinned(int projectId);
	    
	    @Query(value="SELECT count(*) from pinned where object_id=?1 AND created_by=?2",nativeQuery = true)
	    int IsPinned(int projectId, Long userId);
}
