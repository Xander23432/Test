package com.realnet.fnd.repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import com.realnet.comm.entity.PlayEntity;
@Repository
public interface PlayRepository extends JpaRepository<PlayEntity, Integer>{
	Page<PlayEntity> findAll(Pageable p);
	
	@Query("Select s from PlayEntity s Where s.wf_instance_id= :id" )
	 PlayEntity findByWf_instance_id(@Param("id") int id);
}
