package com.realnet.ncso.repository1;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.realnet.ncso.entity1.EbsCustomer;

@Repository
public interface EbsCustomerRepository extends JpaRepository<EbsCustomer,Long>{

	@Query(value="  SELECT * FROM EBS_CUSTOMER\n"
			+ "   WHERE \n"
			+ "		CUSTOMER_CODE LIKE NVL(:str,CUSTOMER_CODE)\n"
			+ "		OR CUSTOMER_ID LIKE NVL(:str,CUSTOMER_ID)\n"
			+ "		OR CUSTOMER_NAME LIKE NVL(:str,CUSTOMER_NAME)\n"
			+ "		OR STATUS LIKE NVL(:str,STATUS) "
			 ,nativeQuery=true)
	Page<EbsCustomer> findAllCustom(Pageable p, @Param("str") String str);
	
	@Query(value="SELECT * FROM EBS_CUSTOMER c "
			+ " where c.customer_code LIKE COALESCE( ?1,c.customer_code) ",
		nativeQuery=true)
	List<EbsCustomer> findAllSearch(String str );
}
