package com.realnet.icon.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.realnet.icon.entity.ArchivedEntity;

@Repository
public interface ArchivedRepository extends JpaRepository<ArchivedEntity, Integer> {
	
	@Query(value="SELECT count(*) from archived where object_id=?1 AND created_by=?2",nativeQuery = true)
    int IsArchived(int projectId, Long userId);
}
