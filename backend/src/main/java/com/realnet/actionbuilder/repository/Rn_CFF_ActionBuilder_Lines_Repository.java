package com.realnet.actionbuilder.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.realnet.actionbuilder.entity.Rn_cff_ActionBuilder_Lines;

@Repository
public interface Rn_CFF_ActionBuilder_Lines_Repository extends JpaRepository<Rn_cff_ActionBuilder_Lines, Integer> {
	// for pagination
	Page<Rn_cff_ActionBuilder_Lines> findAll(Pageable p);

	@Query(value = "SELECT * from RN_CFF_ACTION_BUILDER_LINES_T WHERE HEADER_ID=:headerId", nativeQuery = true)
	List<Rn_cff_ActionBuilder_Lines> findByHeaderId(@Param("headerId") int header_id);
	
	// for pagination
	@Query(value = "SELECT * from RN_CFF_ACTION_BUILDER_LINES_T WHERE HEADER_ID=:headerId", nativeQuery = true)
	Page<Rn_cff_ActionBuilder_Lines> findByHeaderId(@Param("headerId") int headerId, Pageable p);
	
	@Query(value = "SELECT * from RN_CFF_ACTION_BUILDER_LINES_T WHERE HEADER_ID=:headerId ORDER BY SEQ", nativeQuery = true)
	List<Rn_cff_ActionBuilder_Lines> findByHeaderIdAndOrderBySeq(@Param("headerId") int headerId);
	
	
	@Query(value = "SELECT * from RN_CFF_ACTION_BUILDER_LINES_T WHERE HEADER_ID=:headerId AND SEQ=:seq", nativeQuery = true)
	Rn_cff_ActionBuilder_Lines findByHeaderIdAndSeq(@Param("headerId") int headerId, @Param("seq") int seq);
}
