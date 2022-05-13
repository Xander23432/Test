package com.realnet.masters.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.realnet.masters.entity.BE_ITEM_MASTER;
@Repository
public interface BeItemMasterRepo extends JpaRepository<BE_ITEM_MASTER,Long>{

}
