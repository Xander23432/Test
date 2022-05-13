package com.realnet.icon.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.realnet.icon.entity.StarEntity;

@Repository
public interface StarRepository  extends JpaRepository<StarEntity, Integer>{
	
	 @Query(value="SELECT count(*) from star where object_id=?1",nativeQuery = true)
     int countStar(int projectId);
     
     @Query(value="SELECT count(*) from star where object_id=?1 AND created_by=?2",nativeQuery = true)
     int IsStar(int projectId, Long userId);
     
}
