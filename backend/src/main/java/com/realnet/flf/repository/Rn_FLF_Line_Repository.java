package com.realnet.flf.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.realnet.flf.entity.Rn_FLF_Line;

@Repository
public interface Rn_FLF_Line_Repository extends JpaRepository<Rn_FLF_Line, Integer> {
	// for pagination
	Page<Rn_FLF_Line> findAll(Pageable p);

	@Query(value = "SELECT * from RN_FLF_LINE_T WHERE HEADER_ID=:headerId", nativeQuery = true)
	List<Rn_FLF_Line> findByHeaderId(@Param("headerId") int header_id);

	// for pagination
	@Query(value = "SELECT * from RN_FLF_LINE_T WHERE HEADER_ID=:headerId", nativeQuery = true)
	Page<Rn_FLF_Line> findByHeaderId(@Param("headerId") int headerId, Pageable p);

}
