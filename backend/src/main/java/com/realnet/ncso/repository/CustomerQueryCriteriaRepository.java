package com.realnet.ncso.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.realnet.ncso.entity.CustomerQueryCriteria;

public interface CustomerQueryCriteriaRepository extends JpaRepository<CustomerQueryCriteria, Long> {

}
