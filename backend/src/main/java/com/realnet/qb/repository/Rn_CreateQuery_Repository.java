package com.realnet.qb.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.realnet.qb.entity.Rn_CreateQuery;

public interface Rn_CreateQuery_Repository extends JpaRepository<Rn_CreateQuery, Integer> {
	// for pagination
	Page<Rn_CreateQuery> findAll(Pageable p);
}
