package com.realnet.flf.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.realnet.flf.entity.Rn_FLF_Header;

@Repository
public interface Rn_FLF_Header_Repository extends JpaRepository<Rn_FLF_Header, Integer> {
	// for pagination
	Page<Rn_FLF_Header> findAll(Pageable p);
}
