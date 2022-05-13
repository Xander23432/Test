package com.realnet.icon.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.realnet.icon.entity.FuturisticEntity;

@Repository
public interface FuturisticRepository extends JpaRepository<FuturisticEntity, Integer> {

	 @Query(value="SELECT count(*) from futuristic where object_id=?1",nativeQuery = true)
	 int countFuturistic(int projectId);
	    
	 @Query(value="SELECT count(*) from futuristic where object_id=?1 AND created_by=?2",nativeQuery = true)
	 int IsFuturistic(int projectId, Long userId);
}
 