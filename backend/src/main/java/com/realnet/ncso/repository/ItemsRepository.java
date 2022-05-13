package com.realnet.ncso.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.realnet.ncso.entity.Items;

public interface ItemsRepository extends JpaRepository<Items, Long> {

}
