package com.realnet.fnd.repository1;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.realnet.fnd.entity1.GrpMenuAccess;
import com.realnet.fnd.entity1.GrpMenuAccesscompositeKey;

@Repository
public interface GrpMenuAccessRepository extends JpaRepository<GrpMenuAccess,GrpMenuAccesscompositeKey>{

}
