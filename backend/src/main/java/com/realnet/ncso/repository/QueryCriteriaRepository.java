package com.realnet.ncso.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.realnet.ncso.entity.QueryCriteria;

public interface QueryCriteriaRepository extends JpaRepository<QueryCriteria, Long> {

}
