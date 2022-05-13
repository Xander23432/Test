package com.realnet.ncso.repository1;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.realnet.ncso.entity1.BeCustomers;

@Repository
@Transactional
public interface BeCustomerRepository extends JpaRepository<BeCustomers,Long>{

}
