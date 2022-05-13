package com.realnet.masters.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.realnet.masters.entity.BeLeaseMaster;

@Repository
public interface BeLeaseMasterRepo extends JpaRepository<BeLeaseMaster,Long>{

}
