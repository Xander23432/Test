package com.realnet.ncso.repository1;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.realnet.ncso.entity1.EbsInvType;

@Repository
public interface EbsInvTypeRepository extends JpaRepository<EbsInvType,String>{

}
