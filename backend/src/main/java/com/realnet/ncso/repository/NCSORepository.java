package com.realnet.ncso.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.realnet.ncso.entity.NonContainerServiceOrder;

public interface NCSORepository extends JpaRepository<NonContainerServiceOrder, Long>{

}
