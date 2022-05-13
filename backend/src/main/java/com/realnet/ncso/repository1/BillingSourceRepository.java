package com.realnet.ncso.repository1;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.realnet.ncso.entity1.BiillingSourceCompositeKey;
import com.realnet.ncso.entity1.BillingSource;

@Repository
public interface BillingSourceRepository extends JpaRepository<BillingSource,BiillingSourceCompositeKey>{
	@Query(value="SELECT Distinct(bs.source_table),bs.source_id FROM BILLING_SOURCE bs WHERE bs.BILLING_ID =?1",nativeQuery = true)
	List<Object> findAllBs(Long billingId);
	
	@Query(value="SELECT vcn FROM MARINE m WHERE m.ID =?1",nativeQuery=true)
	List<Object> findAllMarine(BigDecimal sourceId);
	
	@Query(value="SELECT * FROM BILLING_SOURCE bs WHERE BILLING_ID = ?1  "
			+ "AND CREDIT_NOTE_NO IS NOT NULL AND CREDIT_NOTE_BILLING_ID IS NOT NULL"
			,nativeQuery=true)
	List<BillingSource> findAllCustom(Long billingId);
}
